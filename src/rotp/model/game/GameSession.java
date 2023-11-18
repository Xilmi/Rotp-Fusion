/*
 * Copyright 2015-2020 Ray Fowler
 *
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.game;

import static rotp.model.game.IGameOptions.GAME_OPTIONS_FILE;
import static rotp.model.game.IGameOptions.MEMORY_LOGFILE;
import static rotp.model.game.IGameOptions.AUTORUN_LOGFILE;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;

import rotp.Rotp;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.empires.EspionageMission;
import rotp.model.empires.Leader;
import rotp.model.empires.Race;
import rotp.model.empires.SabotageMission;
import rotp.model.empires.Spy;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.GalaxyFactory;
import rotp.model.galaxy.GalaxyFactory.GalaxyCopy;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.galaxy.Transport;
import rotp.model.ships.ShipDesign;
import rotp.model.ships.ShipManeuver;
import rotp.model.ships.ShipSpecial;
import rotp.model.ships.ShipWeapon;
import rotp.model.tech.Tech;
import rotp.model.tech.TechTree;
import rotp.ui.NoticeMessage;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.notifications.DiplomaticNotification;
import rotp.ui.notifications.GNNExpansionEvent;
import rotp.ui.notifications.GNNRankingNoticeCheck;
import rotp.ui.notifications.GameAlert;
import rotp.ui.notifications.SabotageNotification;
import rotp.ui.notifications.ShipConstructionNotification;
import rotp.ui.notifications.SpyReportAlert;
import rotp.ui.notifications.StealTechNotification;
import rotp.ui.notifications.SystemsScoutedNotification;
import rotp.ui.notifications.TurnNotification;
import rotp.ui.sprites.FlightPathSprite;
import rotp.util.Base;

public final class GameSession implements Base, Serializable {
    private static final long serialVersionUID = 1L;
    public static final int CURRENT_SAVE_VERSION = 1;
    public static final String SAVEFILE_DIRECTORY = ".";
    public static final String BACKUP_DIRECTORY   = "backup";
    public static final String SAVEFILE_EXTENSION = ".rotp";
    public static final String RECENT_SAVEFILE    = "recent"+SAVEFILE_EXTENSION;
    // BR: to save the beginning of the turn
    public static final String RECENT_START_SAVEFILE = "!!! To Replay Last Turn !!!"+SAVEFILE_EXTENSION;
    public static final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final Object ONE_GAME_AT_A_TIME = new Object();
    private static GameSession instance = new GameSession();
    public static GameSession instance()  { return instance; }

	private static final boolean showInfo = false; // BR: for debug
    private static final int MINIMUM_NEXT_TURN_TIME = 500;
    private static Thread nextTurnThread;
    private static volatile boolean suspendNextTurn = false;
    private static final ThreadFactory minThreadFactory = GameSession.minThreadFactory();
    private static ExecutorService smallSphereService = Executors.newSingleThreadExecutor(minThreadFactory);

    private static HashMap<String,Object> vars;
    private static boolean performingTurn;
    private static final List<TurnNotification> notifications = new ArrayList<>();
    private static HashMap<StarSystem, List<String>> systemsToAllocate;
    private static HashMap<String, List<StarSystem>> systemsScouted;
    private static HashMap<ShipDesign, Integer> shipsConstructed;
    private static final List<GameAlert> alerts = new ArrayList<>();
    private static int viewedAlerts;
    private static boolean ironmanLocked = false;
    private static boolean autoRunning = false;

    private IGameOptions options;
    private Galaxy galaxy;
    private final GameStatus status = new GameStatus();
    private long id;
    private boolean spyActivity = false;

    public GameStatus status()                   { return status; }
    public long id()                             { return id; }
    public ExecutorService smallSphereService()  { return smallSphereService; }

    public static boolean ironmanLocked() 		 { return ironmanLocked; }

    public void pauseNextTurnProcessing(String s)   {
        if (performingTurn) {
            log("Pausing Next Turn: ", s);
            suspendNextTurn = true;
        }
    }
    public void resumeNextTurnProcessing()  {
        log("Resuming Next Turn");
        suspendNextTurn = false;
    }
    public HashMap<ShipDesign, Integer> shipsConstructed() {
        if (shipsConstructed == null)
            shipsConstructed = new HashMap<>();
        return shipsConstructed;
    }
    public HashMap<StarSystem, List<String>> systemsToAllocate() {
        if (systemsToAllocate == null)
            systemsToAllocate = new HashMap<>();
        return systemsToAllocate;
    }
    public HashMap<String, List<StarSystem>> systemsScouted() {
        if (systemsScouted == null) {
            systemsScouted = new HashMap<>();
            systemsScouted.put("Scouts", new ArrayList<>());
            systemsScouted.put("Allies", new ArrayList<>());
            systemsScouted.put("Astronomers", new ArrayList<>());
        }
        return systemsScouted;
    }
    private List<TurnNotification> notifications() {
        return notifications;
    }
    private HashMap<String,Object> vars() {
        if (vars == null)
            vars = new HashMap<>();
        return vars;
    }
    public GameAlert currentAlert() {
        if (viewedAlerts >= alerts.size())
            return null;
        return alerts.get(viewedAlerts);
    }
    public int viewedAlerts()    { return viewedAlerts; }
    public int numAlerts()       { return alerts.size(); }
    public void addAlert(GameAlert a)  { alerts.add(a); }
    private void clearAlerts() {
        alerts.clear();
        viewedAlerts = 0;
    }
    public void dismissAlert() { viewedAlerts++; }

    public boolean performingTurn()      { return performingTurn; }
    @Override
    public IGameOptions options()        { return options; }
    public void options(IGameOptions o)  { options = o; o.setAsGame(); }
    @Override
    public Galaxy galaxy()               { return galaxy; }
    public void galaxy(Galaxy g)         { galaxy = g; }

    public float populationBonus()      { return 1.0f; }
    public float damageBonus()          { return 1.0f; }
    public float researchBonus()        { return 1.0f; }
    public float researchMapSizeAdjustment() {
        float stars = galaxy().numStarSystems();
        int races = options().selectedNumberOpponents()+2;
        float targetRatio = 12.0f;
        return sqrt(stars/races/targetRatio);
    }
    public void addShipsConstructed(ShipDesign design, int newCount) {
        if (!design.active())
            throw new RuntimeException("Constructed an inactive ship design");

        if (shipsConstructed().isEmpty())
            addTurnNotification(new ShipConstructionNotification());

        int existingCount = shipsConstructed().containsKey(design) ? shipsConstructed().get(design) : 0;
        shipsConstructed().put(design, existingCount+newCount);
    }
    public void enableSpyReport() {
        spyActivity = true;
    }
    public boolean spyActivity()            { return spyActivity; }
    public void addSystemScouted(StarSystem sys) {
        systemsScouted().get("Scouts").add(sys);
    }
    public void addSystemScoutedByAllies(StarSystem sys) {
        systemsScouted().get("Allies").add(sys);
    }
    public void addSystemScoutedByAstronomers(StarSystem sys) {
        systemsScouted().get("Astronomers").add(sys);
    }
    private void clearScoutedSystems() {
        systemsScouted().get("Scouts").clear();
        systemsScouted().get("Allies").clear();
        systemsScouted().get("Astronomers").clear();
    }
    public boolean haveScoutedSystems() {
        for (Collection<StarSystem> systems : systemsScouted().values()) {
            if (!systems.isEmpty())
                return true;
        }
        return false;
    }
    public void addSystemToAllocate(StarSystem sys, String reason) {
        // don't prompt to allocate systems that are in rebellion
        if (sys.isColonized() && sys.colony().inRebellion())
            return;

        log("Re-allocate: ", sys.name(), " :", reason);
        if (!systemsToAllocate().containsKey(sys))
            systemsToAllocate().put(sys, new ArrayList<>());

        if (!systemsToAllocate().get(sys).contains(reason))
            systemsToAllocate().get(sys).add(reason);
    }
    public boolean awaitingAllocation(StarSystem sys) {
        return systemsToAllocate().containsKey(sys);
    }
    public void addTurnNotification(TurnNotification notif) {
        notifications().add(notif);
    }
    public void removePendingNotification(String key) {
        List<TurnNotification> notifs = new ArrayList<>(notifications());
        for (TurnNotification notif: notifs) {
            if (notif.key().equals(key))
                notifications.remove(notif);
        }

    }
    public GameSession() {
        System.out.println("==================== Create Options (In-Game) ====================");
        options(RulesetManager.current().defaultRuleset());
    }
    public void startGame(IGameOptions newGameOptions) {
        stopCurrentGame();

        options(newGameOptions.copyAllOptions());
    	RotPUI.currentOptions(IGameOptions.GAME_ID);
    	instance.getGovernorOptions().gameStarted();
        startExecutors();

        synchronized(ONE_GAME_AT_A_TIME) {
            id = (long) (Long.MAX_VALUE*random());
            GalaxyFactory.current().newGalaxy();
            log("Galaxy complete");
            status().startGame();
            clearScoutedSystems();
            systemsToAllocate().clear();
            shipsConstructed().clear();
            spyActivity = false;
            galaxy().startGame();
            saveRecentSession(false);
            saveBackupSession(1);
        }
    }
    // BR: For Restart with new options
    public void restartGame(IGameOptions newGameOptions, GalaxyCopy src) {
    	stopCurrentGame();
        options(src.options().copyAllOptions());
    	RotPUI.currentOptions(IGameOptions.GAME_ID);
    	instance.getGovernorOptions().gameStarted();
        startExecutors();

        synchronized(ONE_GAME_AT_A_TIME) {
            id = (long) (Long.MAX_VALUE*random());
            GalaxyFactory.current().newGalaxy(src);
            log("Galaxy complete");
            status().startGame();
            clearScoutedSystems();
            systemsToAllocate().clear();
            shipsConstructed().clear();
            spyActivity = false;
            galaxy().startGame();
    		GameUI.gameName = generateGameName();
            saveRecentSession(false);
            saveBackupSession(1);
        }
    }
    private void  startExecutors() {
        smallSphereService = Executors.newSingleThreadExecutor();
    }
    private void stopCurrentGame() {
        RotPUI.instance().mainUI().clearAdvice();
        vars().clear();
        clearAlerts();
        // shut down any threads running from previous game
        smallSphereService().shutdownNow();
    }
    public void exit()                        { System.exit(0); }
    public Object var(String key)             { return vars().get(key); }
    public void var(String key, Object value) { vars().put(key, value); }
    public void removeVar(String key)         { vars().remove(key); }
    public void replaceVarValue(Object prevValue, Object newValue) {
        List<String> keys = new ArrayList<>();
        keys.addAll(vars().keySet());
        for (String key: keys) {
            if (var(key) == prevValue) {
                log("replacing value for session var: ", key);
                var(key, newValue);
            }
        }
    }
    public void removeVarValue(Object value) {
        List<String> keys = new ArrayList<>();
        keys.addAll(vars().keySet());
        for (String key: keys) {
            if (var(key) == value)
                vars().remove(key);
        }
    }
    public void nextTurn() {
    	if (options.debugAutoRun())
    		autoRunning = true;
    	nextTurnLoop();
    }
    public void pauseAutoRun() { autoRunning = false; }
    public boolean autoRunning() { return autoRunning; }
    private void nextTurnLoop() {
        if (performingTurn())
            return;
        
        performingTurn = true;
        nextTurnThread = new Thread(nextTurnProcess());
        nextTurnThread.start();
    }
    public void waitUntilNextTurnCanProceed() {
        while(suspendNextTurn)
            sleep(200);
    }
    public boolean sufficientHeapSpace(IGameOptions opts) {
        long maxHeap = Rotp.maxHeapMemory;
        long reqHeap = 200;
        return maxHeap > reqHeap;
    }
    public boolean inProgress()  { return status().inProgress(); }

    private void debugMonitor(long fileSize, long dt) {
    	boolean append = galaxy().currentTurn() > 1;
    	String turn;
    	String duration;
    	if (dt == 0) {
    		turn = getTurn(".5");
    		duration = " ";
    	}
    	else {
    		turn = getTurn(".0");
    		duration = msToHMS(dt);
    	}
    	String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String memS = concat(turn,
        		          " | ", Rotp.getMemoryInfo(false),
        		          " | File size:", String.format("%10d", fileSize),
        		          " | ", time);   	
        if (options.debugConsoleMemory())
        	System.out.println(memS);
        if (options.debugFileMemory())
        	writeToFile(MEMORY_LOGFILE, memS, true, append);        
        if (options.debugAutoRun()) {
        	String s = concat(turn,
					" | Col:", String.format("%5d", player().numColonies()),
					"/", StringUtils.rightPad(String.valueOf(galaxy().numColonizedSystems()), 5),
					" | Aliens:", String.format("%3d", player().numContacts()),
					"/", StringUtils.rightPad(String.valueOf(galaxy().numActiveEmpires()-1), 2),
					" | War:", String.format("%3d", player().numEnemies()),
					" | ", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()),
					" | ", duration
					);
        	writeToFile(AUTORUN_LOGFILE, s, true, append);
        	if (options.consoleAutoRun())
        		System.out.println(s);
        }
    }
    @SuppressWarnings("unused")
	private void ModnarPrivateLogging() {
		String LogPath = Rotp.jarPath();
		File TestLogFile = new File(LogPath, "TestLogFile.txt");
		if (galaxy.currentTurn() % 5 == 0) { // log every 5 turns
			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(TestLogFile, true)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			out.println("Turn: "+ str(galaxy.currentTurn()));
			for (Empire e: galaxy().empires()) {
				StarSystem sys1 = e.mostPopulousSystemForCiv(e);
				// float relationToPlayer = 0.0f;
				// if (!(e==player())) {
				// 	EmpireView pl = e.viewForEmpire(player());
				// 	relationToPlayer = pl.embassy().relations();
				// }
				out.println(String.format("%10s", e.raceName())
				+ String.format("%6d", e.numColonizedSystems())
				+ String.format("%12.2f", e.totalPlanetaryPopulation())
				+ String.format("%12.2f", e.totalPlanetaryProduction())
				+ String.format("%12.0f", e.totalFleetSize())
				+ String.format("%10.2f", 100*e.shipMaintCostPerBC()) + "%"
				+ String.format("%10.2f", 100*e.missileBaseCostPerBC()) + "%"
                + String.format("%10.2f", 100*e.totalSecurityCostPct()) + "%"
				+ String.format("%12.2f", e.totalPlanetaryResearch())
				+ String.format("%8.2f", e.tech().avgTechLevel())
				+ String.format("%6d", e.numEnemies())
				+ String.format("reserve %12.2f", e.totalReserve())
				+ String.format("trade %12.2f", e.netTradeIncome())
				+ String.format("%10s", sys1.name())
				+ String.format("%8.2f", sys1.colony().industry().factories())
				+ String.format("%8.2f", sys1.colony().reserveIncome())
				+ String.format("%8.2f", sys1.colony().totalIncome())
				+ String.format("%8.2f", sys1.colony().production())
				+ String.format("%8.2f", sys1.colony().defense().bases())
				
				);
			}
			out.close();
		}
    }
    private Runnable nextTurnProcess() {
        return () -> {
            try {
                rotp.ui.notifications.TradeTechNotification.resetSkipButton();
                performingTurn = true;
                Galaxy gal = galaxy();
                String turnTitle = nextTurnTitle();
                NoticeMessage.setStatus(turnTitle, text("TURN_SAVING"));
                FlightPathSprite.clearWorkingPaths();
                RotPUI.instance().mainUI().saveMapState();
                log("Next Turn - BEGIN: ", str(galaxy.currentYear()));
                log("Autosaving pre-turn");
                long ufs = instance.saveRecentSession(false);
                debugMonitor(ufs, 0);
				// ModnarPrivateLogging();

                long startMs = timeMs();
                systemsToAllocate().clear();
                clearScoutedSystems();
                shipsConstructed().clear();
                spyActivity = false;
                clearAlerts();
                clearNotificationLimits();
                RotPUI.instance().repaint();
                processNotifications();
                gal.preNextTurn();

                if (!inProgress())
                    return;

                // REMOVE THIS CODE
                //playerViewAllSystems();
                //playerViewAllHomeSystems();

                // all intra-empire events: civ turns, ship movement, etc
                gal.advanceTime();
                gal.moveShipsInTransit();

                gal.events().nextTurn();
                RotPUI.instance().selectMainPanel();

                gal.council().nextTurn();
                if (!options.debugAutoRun()) {
                	GNNRankingNoticeCheck.nextTurn();
                    GNNExpansionEvent.nextTurn();
                }

                gal.nextEmpireTurns();
                gal.clearSpaceMonsters();
                player().setVisibleShips();
                player().setVisibleMonsters();

                // test game over conditions
                //randomlyEndGame();

                if (!inProgress())
                    return;

                if (processNotifications()) {
                    log("Notifications processed 1 - back to MainPanel");
                    RotPUI.instance().selectMainPanel();
                }
                gal.postNextTurn1();
                if (!inProgress())
                    return;

                if (processNotifications()) {
                    log("Notifications processed 2 - back to MainPanel");
                    RotPUI.instance().selectMainPanel();
                }
                gal.refreshAllEmpireViews();
                gal.postNextTurn2();

                if (!inProgress())
                    return;
                if (processNotifications()) {
                    log("Notifications processed 3 - back to MainPanel");
                    RotPUI.instance().selectMainPanel();
                }
                // all diplomatic fallout: praise, warnings, treaty offers, war declarations
                gal.assessTurn();

                if (processNotifications()){
                    log("Notifications processed 4 - back to MainPanel");
                    RotPUI.instance().selectMainPanel();
                }
                gal.makeNextTurnDecisions();

                if (processNotifications()){
                    log("Notifications processed 5 - back to MainPanel");
                    RotPUI.instance().selectMainPanel();
                }
                if (!systemsToAllocate().isEmpty())
                    RotPUI.instance().allocateSystems();

                if (spyActivity)
                    SpyReportAlert.create();

                log("Refreshing Player Views");
                gal.clearSpaceMonsters();
                player().setVisibleMonsters();
                NoticeMessage.resetSubstatus(text("TURN_REFRESHING"));
                validate();
                //BR: Tentative to fix range area errors
                if (!options().debugAutoRun()) {
                	RotPUI.instance().mainUI().map().resetRangeAreas();
                    player().setEmpireMapAvgCoordinates();
                }
                gal.refreshAllEmpireViews();
                log("Autosaving post-turn");
                log("NEXT TURN PROCESSING TIME: ", str(timeMs()-startMs));
                NoticeMessage.resetSubstatus(text("TURN_SAVING"));
                ufs = instance.saveRecentSession(true);

                log("Reselecting main panel");
                RotPUI.instance().mainUI().showDisplayPanel();
                RotPUI.instance().selectMainPanel();
                notifications().clear();
                // ensure Next Turn takes at least a minimum time
                long spentMs = timeMs() - startMs;
                if (spentMs < MINIMUM_NEXT_TURN_TIME && !options.debugAutoRun()) {
                    try { Thread.sleep(MINIMUM_NEXT_TURN_TIME - spentMs);
                    } catch (InterruptedException e) { }
                }
                RotPUI.instance().repaint();
                log("Next Turn - END: ", str(galaxy.currentYear()));
            	debugMonitor(ufs, spentMs);
            }
            catch(Exception e) {
                err("Unexpected error during Next Turn:", e.toString());
                exception(e);
            }
            finally {
                RotPUI.instance().mainUI().restoreMapState();
                if (Rotp.memoryLow())
                    RotPUI.instance().mainUI().showMemoryLowPrompt();
                // handle game over possibility
                if (autoRunning && options().debugAutoRun()) {
                	if ((galaxy().numActiveEmpires() == 1 
                			&& options.selectedOpponentRaces()[0]!=null)
                			|| session().status().won()) {
                		RotPUI.instance().selectGameOverPanel();
                	}
                	else {
                		performingTurn = false;
                		nextTurnLoop();
                	}
                }
                else {
                    if (!session().status().inProgress())
                        RotPUI.instance().selectGameOverPanel();                	
                }
                performingTurn = false;
            }
        };
    }
    private void clearNotificationLimits() {
    	DiplomaticNotification.clearNotificationLimits();
    }
    public boolean processNotifications() {
        log("Processing player notifications: ", str(notifications().size()));
        if (!options().isAutoPlay() && haveScoutedSystems())
            session().addTurnNotification(new SystemsScoutedNotification());


        if (notifications().isEmpty())
            return false;
        // received a concurrent modification here... iterate over temp array
        List<TurnNotification> notifs = new ArrayList<>(notifications());
        Collections.sort(notifs);
        notifications().clear();

        RotPUI.instance().processNotifications(notifs);
        clearScoutedSystems();
        return true;
    }
    public void startGroundCombat() {
        for (EmpireView v : player().empireViews()) {
            if ((v!= null) && !v.embassy().contact()) {
                v.embassy().makeFirstContact();
                v.embassy().declareWar();
                break;
            }
        }

        if (galaxy().currentTurn() > 2)
            return;
        Empire pl = player();
        if (pl.hostiles().isEmpty())
            return;
        Empire emp = random(pl.hostiles()).empire();
        StarSystem sys = galaxy().system(pl.homeSysId());

        Transport tr = emp.allColonizedSystems().get(0).colony().transport();
        tr.setDest(sys);
        tr.size(30); // better hope you're playing the Bulrathi
        tr.launch();
        tr.arrive();
    }
    public void startShipCombat() {
        if (galaxy().currentTurn() > 2)
            return;
        Empire pl = player();
        if (pl.hostiles().isEmpty())
            return;
        Empire emp = random(pl.hostiles()).empire();
        StarSystem sys = galaxy().system(pl.homeSysId());
        sys.colony().defense().bases(3);

        // ShipDesign plSc = pl.shipLab().scoutDesign();
        ShipDesign plSh = pl.shipLab().fighterDesign();
        ShipFleet plFl = sys.orbitingFleetForEmpire(pl);
        if (plFl != null) {
            plFl.addShips(plSh.id(), 2);
        }
        else {
            plFl = new ShipFleet(pl.id, sys);
            plFl.addShips(plSh.id(), 2);
            sys.acceptFleet(plFl);
        }

        ShipDesign enSh = emp.shipLab().fighterDesign();
        ShipDesign enSh2 = emp.shipLab().bomberDesign();
        ShipFleet enFl = new ShipFleet(emp.id, sys);
        enFl.addShips(enSh.id(), 5);
        enFl.addShips(enSh2.id(), 3);
        sys.acceptFleet(enFl);
    }
    public void startShipCombat2() {
        if (galaxy().currentTurn() > 2)
            return;
        Empire pl = player();
        if (pl.hostiles().isEmpty())
            return;
        Empire emp = random(pl.hostiles()).empire();
        StarSystem sys = galaxy().system(pl.homeSysId());
        sys.colony().defense().bases(1);
        ShipDesign plCo= pl.shipLab().colonyDesign();
        ShipDesign plSc = pl.shipLab().scoutDesign();
        ShipDesign plSh = pl.shipLab().fighterDesign();
        ShipFleet plFl = sys.orbitingFleetForEmpire(pl);
        if (plFl != null) {
            plFl.removeShips(plSc.id(), 2, false);
            plFl.removeShips(plCo.id(), 1, false);
            //plFl.addShips(plSh, 2);
        }
        else {
            plFl = new ShipFleet(pl.id, sys);
            plFl.addShips(plSh.id(), 2);
            sys.acceptFleet(plFl);
        }

        ShipDesign enSh = emp.shipLab().fighterDesign();
        ShipWeapon miss = emp.shipLab().missileWeapon(0, 5);
        enSh.addWeapon(miss, 20);
        ShipFleet enFl = new ShipFleet(emp.id, sys);
        enFl.addShips(enSh.id(), 5);
        //enFl.addShips(enSh2, 3);
        sys.acceptFleet(enFl);
    }
    public void startGalacticCouncil() {
        if (galaxy().currentTurn() == 2) {
            for (Empire emp: galaxy().empires())
                emp.makeFullContact();
        }
    }
    public void startGNNNotification() {
        (new GNNRankingNoticeCheck()).showRanking();
    }
    public void randomlyEndGame() {
        if (galaxy().numberTurns() < 2)
            return;
        galaxy().council().leader(random(galaxy().empires()));
        player().lastAttacker(random(galaxy().empires()));

        int r = roll(0,11);
        switch(r) {
            case 0: session().status().loseOverthrown(); break;
            case 1: session().status().loseMilitary(); break;
            case 2: session().status().loseDiplomatic(); break;
            case 3: session().status().loseNewRepublic(); break;
            case 4: session().status().loseRebellion(); break;
            case 5: session().status().winDiplomatic(); break;
            case 6: session().status().winMilitary(); break;
            case 7: session().status().winMilitaryAlliance(); break;
            case 8: session().status().winNewRepublic(); break;
            case 9: session().status().winRebellion(); break;
            case 10: session().status().winRebellionAlliance(); break;
            case 11: session().status().wonCouncilAlliance(); break;
        }
    }
    public void formAllianceWithRandomContact() {
        int empId = random(player().contactedEmpires()).id;
        if (!player().alliedWith(empId)) {
            EmpireView v = player().viewForEmpire(empId);
            v.embassy().signAlliance();
        }
    }
    public void formAlliancesWithContacts() {
        for (Empire e: player().contactedEmpires()) {
            if (!player().alliedWith(e.id)) {
                EmpireView v = player().viewForEmpire(e.id);
                v.embassy().signAlliance();
            }
        }
    }
    public void randomlyLearnTechs() {
        if (galaxy().numberTurns() == 2) {
            err("Each empire randomly learning 10 unknown techs to facilitate TechExchange testing");
            for (Empire emp: galaxy().empires()) {
                for (int i=0;i<10;i++)
                    emp.tech().learnTech(emp.tech().randomUnknownTech(1,20, emp.isPlayer(), null, null).id()); // BR: always add in some Technologies
            }
            err("Each empire spying on each other");
            for (Empire emp1: galaxy().empires()) {
                for (Empire emp2: galaxy().empires()) {
                    if (emp1 != emp2) {
                        EmpireView v = emp1.viewForEmpire(emp2);
                        v.spies().updateTechList();
                    }
                }
            }
        }
    }
    public void startHighTechShipCombat() {
        if (galaxy().numberTurns() > 2)
            return;

        // make enemies
        for (EmpireView v : player().empireViews()) {
            if ((v!= null) && !v.embassy().contact()) {
                v.embassy().makeFirstContact();
                v.embassy().declareWar();
                break;
            }
        }

        // learn everything
        for (Empire emp: galaxy().empires())
            emp.tech().learnAll();

        Empire pl = player();
        if (pl.hostiles().isEmpty())
            return;
        Empire emp = random(pl.hostiles()).empire();
        StarSystem sys = galaxy().system(pl.homeSysId());
        sys.colony().defense().bases(3);

        ShipDesign plSc = pl.shipLab().scoutDesign();
        ShipDesign plSh = pl.shipLab().fighterDesign();
        ShipDesign plBo = pl.shipLab().bomberDesign();
        ShipDesign plCo = pl.shipLab().colonyDesign();
        ShipFleet plFl = sys.orbitingFleetForEmpire(pl);
        if (plFl == null) {
            plFl = new ShipFleet(pl.id, sys);
            sys.acceptFleet(plFl);
        }

        plFl.addShips(plSc.id(), 20);
        plFl.addShips(plSh.id(), 2);
        plFl.addShips(plBo.id(), 2);
        plFl.addShips(plCo.id(), 2);

        ShipSpecial sp1  = pl.shipLab().specialTeleporter();
        ShipSpecial sp2  = pl.shipLab().specialCloak();
        ShipSpecial sp3  = pl.shipLab().specialNamed("Stasis Field");
        ShipSpecial sp4  = pl.shipLab().specialNamed("High Energy Focus");
        ShipSpecial sp5  = pl.shipLab().specialNamed("Ionic Pulsar");
        ShipSpecial sp6  = pl.shipLab().specialNamed("Black Hole Generator");
        ShipSpecial sp7  = pl.shipLab().specialNamed("Warp Dissipator");
        ShipSpecial sp8  = pl.shipLab().specialNamed("Technology Nullifier");
        ShipSpecial sp9  = pl.shipLab().specialNamed("Displacement Device");

        ShipManeuver manv = pl.shipLab().maneuvers().get(pl.shipLab().maneuvers().size()-1);
        ShipWeapon wpn1 = pl.shipLab().beamWeapon(0, true);

        plSc.maneuver(manv);
        plSc.special(0, sp1);
        plSc.special(1, sp7);
        plSc.special(2, sp5);
        pl.shipViewFor(plSc).scan();

        plSh.maneuver(manv);
        plSh.weapon(0,wpn1);
        plSh.special(0,sp2);
        plSh.special(1,sp6);
        plSh.special(2,sp4);
        pl.shipViewFor(plSh).scan();

        plBo.maneuver(manv);
        plBo.special(0,sp3);
        plBo.special(1, sp8);
        plBo.special(2, sp9);
        pl.shipViewFor(plBo).scan();


        ShipDesign enSh = emp.shipLab().fighterDesign();
        ShipDesign enSh2 = emp.shipLab().bomberDesign();
        ShipFleet enFl = new ShipFleet(emp.id, sys);
        enFl.addShips(enSh.id(), 100);
        enFl.addShips(enSh2.id(), 30);
        sys.acceptFleet(enFl);
    }
    public void randomlyStealATech() {
        EmpireView view = random(player().empireViews());
        if (view != null) {
            StarSystem espionageSystem = galaxy().system(view.empire().homeSysId());
            Empire espionageEmpire = view.empire();

            List<Tech> techs = new ArrayList<>();
            for (int i=0;i<TechTree.NUM_CATEGORIES;i++)
                techs.add(tech(random(view.empire().tech().category(i).allTechs())));
            techs.remove(random(techs)); // one blank category
            Spy spy = (new Spy(view.spies())).makeSuper();
            EspionageMission mission = new EspionageMission(view.spies(), spy, techs,espionageSystem, techs);
            StealTechNotification.create(mission, espionageEmpire.id);
            for (EmpireView v: player().empireViews())
                if ((v != null) && (v.empire() != view.empire()))
                    mission.empiresToFrame().add(v.empire());
        }
    }
    public void randomlyCommitSabotage() {
        EmpireView view = random(player().empireViews());
        if (view != null) {
            view.embassy().makeFirstContact();
            StarSystem sys = random(view.empire().allColonizedSystems());
            player().sv.refreshFullScan(sys.id);
            Spy spy = (new Spy(view.spies())).makeSuper();
            SabotageMission mission = new SabotageMission(view.spies(), spy);
            SabotageNotification.addMission(mission, sys.id);
        }
    }
    public void playerViewAllHomeSystems() {
        // for testing the minimum empire distance code
        for (Empire emp: galaxy().empires()) {
            player().sv.refreshFullScan(emp.homeSysId());
        }
        for (StarSystem sys: galaxy().starSystems()) {
            if (sys.hasMonster())
                player().sv.refreshFullScan(sys.id);
        }
    }
    public void playerViewAllSystems() {
        // for testing the minimum empire distance code
        for (StarSystem sys: galaxy().starSystems()) {
                player().sv.refreshFullScan(sys.id);
        }
    }
    private String nextTurnTitle() {
        if (options().displayYear())
            return text("MAIN_ADVANCING_YEAR", galaxy().currentYear()+1);
        else
            return text("MAIN_ADVANCING_TURN", galaxy().currentTurn()+1);
    }
    public long saveSession(String filename, boolean backup) throws Exception {
        log("Saving game as file: ", filename, "  backup: "+backup);
        GameSession currSession = GameSession.instance();

    	File theDir = backup ? new File(backupDir()) : new File(saveDir());
        if (!theDir.exists())
            theDir.mkdirs();
        File saveFile = backup ? backupFileNamed(filename) : saveFileNamed(filename);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(saveFile));
        ZipEntry e = new ZipEntry("GameSession.dat");
        out.putNextEntry(e);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream objOut = null;
        try {
            objOut = new ObjectOutputStream(bos);
            objOut.writeObject(currSession);
            objOut.flush();
            byte[] data = bos.toByteArray();
            out.write(data, 0, data.length);
        }
        finally {
            try {
            bos.close();
            out.close();
            }
            catch(IOException ex) {}
        }
		return e.getSize();
    }
    private void resolveOptionsDiscrepansies(GameSession gs) {
    	// resolving AutoPlay potential issues
    	String autoPlaySetting = gs.options().selectedAutoplayOption();
    	if (!autoPlaySetting.equals(IGameOptions.AUTOPLAY_OFF))
    		gs.galaxy.player().changePlayerAI(autoPlaySetting);
    }
    private void loadPreviousSession(GameSession gs, boolean startUp) {
        stopCurrentGame();
        instance = gs;
        // BR: save the last loaded game initial parameters
        instance.options().setAsGame();
    	resolveOptionsDiscrepansies(gs);
    	RotPUI.currentOptions(IGameOptions.GAME_ID);
        instance.options().saveOptionsToFile(GAME_OPTIONS_FILE);

		if (showInfo)  showInfo(gs.galaxy());
        startExecutors();
        RotPUI.instance().mainUI().checkMapInitialized();
        if (!startUp) {
            RotPUI.instance().selectMainPanelLoadGame();
        }
        instance.getGovernorOptions().gameLoaded();
    }
	private void showInfo(Galaxy g) { // BR: for debug
		System.out.println("GameSession.showInfo = true ===========================================");
		System.out.println();
		for (Empire emp : g.empires()) {
			int id = emp.homeSysId();
			Race r = emp.race();
			StarSystem sys = g.system(id);
			Leader boss = emp.leader();
			System.out.println(
					String.format("%-16s", r.name())
					+ String.format("%-12s", sys.name())
					+ String.format("%-16s", emp.dataRace().name())
					+ String.format("%-12s", boss.personality())
					+ String.format("%-15s", boss.objective())
					+ String.format("%-22s", emp.diplomatAI())
//					+ String.format("ID=" + "%-4s", id)
//					+ String.format("x=" + "%-11s", sys.x())
//					+ String.format("y=" + "%-11s", sys.y())
					+ String.format("AI=" + "%-4s", emp.selectedAI)
					+ emp.getAiName()
					);
		}
		System.out.println();
	}
    public String saveDir() {
        return UserPreferences.saveDirectoryPath();
    }
    public String backupDir() {
        return concat(saveDir(),"/",GameSession.BACKUP_DIRECTORY);
    }
    public File saveFileNamed(String fileName) {
        return new File(saveDir(), fileName);
    }
    public File backupFileNamed(String fileName) {
        return new File(backupDir(), fileName);
    }
    public File recentSaveFile() {
        return new File(saveDir(), GameSession.RECENT_SAVEFILE);
    }
    private String backupFileName(int num) {
        Empire pl = player();
        String leader = pl.leader().name().replaceAll("\\s", "");
        String race = pl.raceName();
        String gShape = text(options().selectedGalaxyShape()).replaceAll("\\s", "");
        String gSize = text(options().selectedGalaxySize());
        String diff = text(options().selectedGameDifficulty());
        // modnar: add custom difficulty level option, set in Remnants.cfg
        // append this custom difficulty percentage to backup save file name if selected
        if (diff.equals("Custom")) {
            diff = diff + " (" + Integer.toString(options().selectedCustomDifficulty()) + "%)";
        }
        String turn = "T"+pad4.format(num);
        String opp = "vs"+options().selectedNumberOpponents();
        String dash = "-";
        return concat(leader,dash,race,dash,gShape,dash,gSize,dash,diff,dash,opp,dash,turn,SAVEFILE_EXTENSION);
    }
    public long saveRecentSession(boolean endOfTurn) {
    	long ufs = -1;
    	if (!endOfTurn) // BR: Always keep a copy of starting turn
    		saveRecentStartSession();
        String filename = RECENT_SAVEFILE;
        try {
            ufs = saveSession(filename, false);
            if (endOfTurn)
               saveBackupSession(galaxy().currentTurn());
        }
        catch(Exception e) {
            err("Error saving: ", filename, " - ", e.getMessage());
            if (endOfTurn)
                RotPUI.instance().mainUI().showAutosaveFailedPrompt(e.getMessage());
        }
		return ufs;
    }
    public void saveRecentStartSession() {
        String filename = RECENT_START_SAVEFILE;
        try {
            saveSession(filename, false);
        }
        catch(Exception e) {
            err("Error saving: ", filename, " - ", e.getMessage());
        }
    }
    public void saveBackupSession(int turn) {
        String filename = "nofile";
        try {
            int backupTurns = UserPreferences.backupTurns();
            if (backupTurns > 0) {
                if ((turn == 1) || (turn % backupTurns == 0)) {
                    filename = backupFileName(turn);
                    saveSession(filename, true);
                }
            }
        }
        catch(Exception e) {
            err("Error saving: ", filename, " - ", e.getMessage());
            RotPUI.instance().mainUI().showAutosaveFailedPrompt(e.getMessage());
        }
    }
    public boolean hasRecentSession() {
    	File f = new File(saveDir(), RECENT_SAVEFILE); // BR: To work on debug too...
        try {
            // InputStream file = new FileInputStream(RECENT_SAVEFILE);
            InputStream file = new FileInputStream(f);
            file.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
    public void loadLastSavedGame(boolean startUp) {
    	String ext = GameSession.SAVEFILE_EXTENSION;
    	File saveDir = new File(saveDir());
    	FilenameFilter filter = (File dir, String name1) -> name1.toLowerCase().endsWith(ext);
        File[] fileList = saveDir.listFiles(filter);
        String lastSave = "";
        long lastModifiedTime = Long.MIN_VALUE;
        if (fileList != null) {
            for (File file : fileList) {
                if (file.lastModified() > lastModifiedTime) {
                    lastSave = file.getName();
                    lastModifiedTime = file.lastModified();
                }
            }
        }
        loadSession(saveDir(), lastSave, startUp);
    }
    public void loadRecentSession(boolean startUp) {
        loadSession(saveDir(), RECENT_SAVEFILE, startUp);
    }
    // BR: added option to restart with new options
    public void loadSession(String dir, String filename, boolean startUp) {
        RotPUI.currentOptions(IGameOptions.GAME_ID);
        try {
            log("Loading game from file: ", filename);
            File saveFile = dir.isEmpty() ? new File(filename) : new File(dir, filename);
            GameSession newSession;
            // assume the file is not zipped, load it directly
            try (InputStream file = new FileInputStream(saveFile)) {
                newSession = loadObjectData(file);
            }

            // if newSession is null, see if it is zipped
            if (newSession == null) {
                try (ZipFile zipFile = new ZipFile(saveFile)) {
                    ZipEntry ze = zipFile.entries().nextElement();
                    InputStream zis = zipFile.getInputStream(ze);
                    newSession = loadObjectData(zis);
                    if (newSession == null)
                        throw new RuntimeException(text("LOAD_GAME_BAD_VERSION", filename));
                }
            }

            GameSession.instance = newSession;
            newSession.validate();
            newSession.validateOnLoadOnly();

            loadPreviousSession(newSession, startUp);
            newSession.ironmanValidation();
            // do not autosave the current session if that is the file we are trying to reload            
            if (!filename.equals(RECENT_SAVEFILE))
                saveRecentSession(false);
            else
            	saveRecentStartSession(); // BR: to keep a copy of the beginning of the turn
         }
        catch(IOException e) {
            throw new RuntimeException(text("LOAD_GAME_BAD_VERSION", filename));
        }

    }
    // BR: For restarting with new options
    public void loadSession(GameSession newSession) {
        GameSession.instance = newSession;
        newSession.validate();
        newSession.validateOnLoadOnly();
    	return;
    }
    private GameSession loadObjectData(InputStream is) {
        try {
            GameSession newSession;
            try (InputStream buffer = new BufferedInputStream(is)) {
                ObjectInput input = new ObjectInputStream(buffer);
                newSession = (GameSession) input.readObject();
            }
            return newSession;
        }
        catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    private void validate() {
        galaxy().validate();
    }
    private void ironmanValidation() {
    	ironmanLocked = false;
        if (options().selectedIronmanLoad()) {
        	int turn = galaxy().currentTurn();
	        int modulo = Math.floorMod(turn, options.selectedIronmanLoadDelay());
	        ironmanLocked = (modulo != 0) && (turn > 1);
        }
    }
    private void validateOnLoadOnly() {
    	autoRunning = false;
        GNNExpansionEvent.instance().validate(galaxy());

        // check for invalid colonies with too much waste & negative pop
        for (StarSystem sys: galaxy().starSystems()) {
            if (sys.isColonized())
                sys.colony().validateOnLoad();
        }
        // check for council last-vote init issue
        boolean allVotedOnlyForPlayer = true;
        for (Empire emp: galaxy().empires()) {
            if (emp.lastCouncilVoteEmpId() != 0)
                allVotedOnlyForPlayer = false;
        }

        if (allVotedOnlyForPlayer) {
            for (Empire emp: galaxy().empires())
                emp.lastCouncilVoteEmpId(Empire.NULL_ID);
        }

        Galaxy gal = this.galaxy();
        Empire pl = player();
        pl.setEmpireMapAvgCoordinates();

        float minX = gal.width();
        float minY = gal.height();
        float maxX = 0;
        float maxY = 0;

        List<StarSystem> alliedSystems = pl.allColonizedSystems();
        for (StarSystem sys : alliedSystems) {
            minX = min(minX,sys.x());
            maxX = max(maxX,sys.x());
            minY = min(minY,sys.y());
            maxY = max(maxY,sys.y());
        }
        float r = pl.scoutReach(6);
        minX = max(0,minX-r);
        maxX = min(gal.width(), maxX+r);
        minY = max(0,minY-r);
        maxY = min(gal.height(), maxY+r);
        pl.setBounds(minX, maxX, minY, maxY);
        pl.setVisibleShips();
        // BR: Backward compatibility tentative
        galaxy().validateOnLoad();
        ((MOO1GameOptions) options).validateOnLoad();
    }
    static ThreadFactory minThreadFactory() {
        return (Runnable r) -> {
            Thread t = new Thread(r);
            t.setPriority(Thread.MIN_PRIORITY);
            return t;
        };
    }

    private GovernorOptions governorOptions = new GovernorOptions();

    public GovernorOptions getGovernorOptions() {
        // can happen on deserialized stock save game
        if (governorOptions == null) {
            governorOptions = new GovernorOptions();
        }
        return governorOptions;
    }
}
