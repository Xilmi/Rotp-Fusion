/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.galaxy;

import static rotp.model.empires.CustomRaceDefinitions.RANDOM_RACE_KEY;
import static rotp.model.game.MOO1GameOptions.setAliensAIOptions;
import static rotp.ui.UserPreferences.randomAlienRaces;
import static rotp.ui.UserPreferences.restartAppliesSettings;
import static rotp.ui.UserPreferences.restartChangesAliensAI;
import static rotp.ui.UserPreferences.restartChangesPlayerAI;
import static rotp.ui.UserPreferences.restartChangesPlayerRace;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rotp.mod.br.addOns.RacesOptions;
import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.empires.Empire;
import rotp.model.empires.Empire.EmpireBaseData;
import rotp.model.empires.Leader;
import rotp.model.empires.Race;
import rotp.model.galaxy.Galaxy.GalaxyBaseData;
import rotp.model.galaxy.GalaxyShape.EmpireSystem;
import rotp.model.galaxy.StarSystem.SystemBaseData;
import rotp.model.game.DynOptions;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.game.MOO1GameOptions;
import rotp.model.planet.Planet;
import rotp.model.tech.Tech; // modnar: add game mode to start all Empires with 2 random techs
import rotp.model.tech.TechTree; // modnar: add game mode to start all Empires with 2 random techs
import rotp.ui.UserPreferences; // modnar: add game mode to start all Empires with 2 random techs
import rotp.ui.util.planets.PlanetImager;
import rotp.util.Base;

public class GalaxyFactory implements Base {
	private static GalaxyFactory instance = new GalaxyFactory();
	public static GalaxyFactory current() { return instance; }
	/**
	 * Companion world greek letter prefix
	 */
	public static final String[] compSysName = new String[]{"α", "β", "γ", "δ", "ε", "ζ"};// BR : added two possibilities
	private static final boolean showEmp = true; // BR: for debug
	private static final boolean showAI	 = false; // BR: for debug
	private static boolean[] isRandomOpponent; // BR: only Random Races will be customized
	private static String playerDataRaceKey;   // BR: in case Alien races are a copy of player race

	public Galaxy newGalaxy(GalaxyCopy src) {
		GalaxyBaseData gc = src.galSrc;
		for (Race r: Race.races()) {
			r.loadNameList();
			r.loadLeaderList();
			r.loadHomeworldList();
		}
		IGameOptions opts;
		LinkedList<String> alienRaces;
		if (restartAppliesSettings.get()) {
			opts = GameSession.instance().options();
			alienRaces = buildAlienRaces();
		}
		else {
			opts = src.options();
			alienRaces = null;
		}
		opts.randomizeColors();
		Galaxy g = new Galaxy(gc);
		GameSession.instance().galaxy(g);
		
		Race playerRace = Race.keyed(gc.empires[0].raceKey, gc.empires[0].raceOptions);
		addNebulas(g, src);
		List<String> systemNames = playerRace.systemNames;
		Collections.shuffle(systemNames);
		
		addPlayerSystemForGalaxy(g, 0, null, src);
		addAlienRaceSystemsForGalaxy(g, 1, null, src, alienRaces);
		addUnsettledSystemsForGalaxy(g, gc);		
		init(g, System.currentTimeMillis());
		MOO1GameOptions.saveGameOptions((MOO1GameOptions) newGameOptions());
		return g;
	}
	public Galaxy newGalaxy() {
		for (Race r: Race.races()) {
			r.loadNameList();
			r.loadLeaderList();
			r.loadHomeworldList();
		}

		IGameOptions opts = GameSession.instance().options();
		opts.randomizeColors();
		GalaxyShape shape = opts.galaxyShape();

		// for extremely large maps, shape is not fully generated on Setup UI
		if (!shape.fullyInit())
			shape.fullGenerate();

		Galaxy g = new Galaxy(shape);
		GameSession.instance().galaxy(g);
		Race playerRace = Race.keyed(opts.selectedPlayerRace());

		LinkedList<String> alienRaces = buildAlienRaces();

		log("Creating Galaxy size: ", fmt(g.width(),2), "@", fmt(g.height(),2));
		long tm0 = System.currentTimeMillis();

		addNebulas(g, shape);
		long tm1 = System.currentTimeMillis();
		log(str(g.nebulas().size()) +" Nebulas: "+(tm1-tm0)+"ms");

		List<String> systemNames = playerRace.systemNames;
		Collections.shuffle(systemNames);

		List<EmpireSystem> empires = shape.empireSystems();
		addPlayerSystemForGalaxy(g, 0, empires, null);
		empires.remove(empires.get(0));
		addAlienRaceSystemsForGalaxy(g, 1, empires, null, alienRaces);
		addUnsettledSystemsForGalaxy(g, shape);
		
		// remove empty nebula
		List<Nebula> allNebula = new ArrayList<>(g.nebulas());
		for (Nebula neb: allNebula) {
			if (neb.noStars())
				g.nebulas().remove(neb);
		}
		
		// for larger nebula (>= 3 contained stars), enrich the center-most system
		// typical need larger maps (>200 stars) for this to happen
		for (Nebula n: g.nebulas())
			n.enrichCentralSystem();

		long tm2 = System.currentTimeMillis();
		log(str(g.numStarSystems()) ," Systems, ",str(Planet.COUNT)," Planets: "+(tm2-tm1)+"ms");
		init(g, tm2);
		MOO1GameOptions.saveGameOptions((MOO1GameOptions) newGameOptions());
		return g;
	}
	private void showAI(Galaxy g) {
		System.out.println("========================================================");
		for (Empire emp : g.empires()) {
			int id = emp.homeSysId();
			int ai = emp.selectedAI;
			String name	  = emp.race().name();
			String home	  = g.system(id).name();
			String aiName = emp.getAiName(); 
			System.out.println(
					String.format("%-4sName = ",     id)
					+ String.format("%-16sHome = ",  name)
					+ String.format("%-12sAI id = ", home)
					+ String.format("%-4sAI Name =", ai)
					+ aiName
					);
		}
		System.out.println();
	}
	private void showEmp(Galaxy g) {
		System.out.println("GalaxyFactory.showEmp = true ===========================================");
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
	@SuppressWarnings("unused")
	private boolean checkIdentique(Galaxy g) {
		System.out.println("num Star Systems = " + g.systemCount + "num Empire = " + g.empires().length);
		for (int i=0; i<g.systemCount; i++) {
			System.out.println("Systems " + i
					+ "	x = " + g.system(i).x()
					+ "	y = " + g.system(i).y()
					+ "	name = " + g.system(i).name()
					);
		}
		boolean identique = false;
		int lim = g.systemCount-1;
		for (int i=0; i<lim; i++) {
			float x1 = g.starSystems()[i].x();
			float y1 = g.starSystems()[i].y();
			for (int k=i+1; k<=lim; k++) {
				float x2 = g.starSystems()[k].x();
				float y2 = g.starSystems()[k].y();
				double distSq = Math.pow(x2-x1, 2.0) + Math.pow(y2-y1, 2.0);
				if (distSq < 0.5) {
					System.out.println("========== i = " + i + "  k = " + k + "  distSq = " + distSq);
					identique = true;
				}
			}
		}
		lim = g.empires().length-1;
		for (int i=0; i<lim; i++) {
			Empire e = g.empires()[i];
			if (e == null) 
				return identique;
			int h1 = e.homeSysId();
			for (int k=i+1; k<=lim; k++) {
				e = g.empires()[k];
				if (e == null) 
					return identique;
				int h2 = e.homeSysId();
				if (h1 == h2) {
					System.out.println("========== i = " + i + "  k = " + k + "  Home World ID = " + h1);
					identique = true;
				}
			}
		}
		return identique;
	}
	// BR: Common part of "Restart" standard "Start"
	private void init(Galaxy g, long tm2) {
		// after systems created, add system views for each empire
		for (Empire e: g.empires()) {
			e.loadStartingTechs();
			
			// modnar: add game mode to start all Empires with 2 random techs
			if (UserPreferences.randomTechStart.get()) {
				// randomUnknownTech, somewhat awkward to use in succession
				//e.tech().learnTech(e.tech().randomUnknownTech(1,4).id());
				//e.tech().learnTech(e.tech().randomUnknownTech(1,4).id());
				
				// generate full tech tree
				TechTree eTech = e.tech();
				List<String> firstTierTechs = new ArrayList<>();
				List<String> allTechs = new ArrayList<>();
				allTechs.addAll(eTech.computer().allTechs());
				allTechs.addAll(eTech.construction().allTechs());
				allTechs.addAll(eTech.forceField().allTechs());
				allTechs.addAll(eTech.planetology().allTechs());
				allTechs.addAll(eTech.propulsion().allTechs());
				allTechs.addAll(eTech.weapon().allTechs());
				for (String id: allTechs) {
					Tech t = tech(id);
					// pick only from first tier/quintile
					if ((t.level() >= 2) && (t.level() <= 5))
						firstTierTechs.add(id);
				}
				// shuffle for randomness
				Collections.shuffle(firstTierTechs);
				e.tech().learnTech(firstTierTechs.get(0));
				e.tech().learnTech(firstTierTechs.get(1));
			}
		}
		long tm3 = System.currentTimeMillis();
		log("load starting techs: "+(tm3-tm2)+"ms");

		for (Empire e: g.empires()) {
			e.loadStartingShipDesigns();
		}
		long tm3b = System.currentTimeMillis();
		log("load ship designs: "+(tm3b-tm3)+"ms");

		for (Empire e: g.empires()) {
			e.colonizeHomeworld();
			// modnar: add option to start game with additional colonies
			// modnar: colonize these 0 to 4 additional colonies
			for (int i=0; i<UserPreferences.companionWorlds(); i++) {
				e.colonizeCompanionWorld(e.compSysId(i));
			}
		}
		long tm3c = System.currentTimeMillis();
		log("colonize homeworld: "+(tm3c-tm3b)+"ms");

		// after all is done, set playerCiv
		g.player(g.empire(0));
		player().setBeginningColonyAllocations();

		for (Empire e : g.empires())
			e.ai().scientist().setDefaultTechTreeAllocations();

		for (Empire e1 : g.empires()) {
			for (Empire e2 : g.empires())
				e1.addViewFor(e2);
		}

		// this takes the longest time
		for (Empire e: g.empires()) {
			e.sv.refreshFullScan(e.homeSysId());
			e.setVisibleShips(e.homeSysId());
		}

		g.council().init();

		long tm4 = System.currentTimeMillis();
		log("Other inits: "+(tm4-tm3c)+"ms");

		g.player().refreshViews();
                g.player().makeNextTurnDecisions();
		long tm5 = System.currentTimeMillis();
		log("Next Turn Decision: "+(tm5-tm4)+"ms");

		PlanetImager.current().finished();
		
		MOO1GameOptions opts = (MOO1GameOptions) GameSession.instance().options();
		opts.dynamicOptions().setObject(Galaxy.EMPIRES_KEY, g.empires());
		// Save initial state
		g.backupStarSystem();

		if (showEmp) showEmp(g);
		if (showAI)  showAI(g);
	}
	private LinkedList<String> buildAlienRaces() {
		LinkedList<String> raceList = new LinkedList<>();
		List<String> allRaceOptions = new ArrayList<>();
		List<String> options = RacesOptions.getNewRacesOnOffList(); // BR:
		int maxRaces = options().selectedNumberOpponents();
		int mult = IGameOptions.MAX_OPPONENT_TYPE;

		// first, build randomized list of opponent races
		for (int i=0;i<mult;i++) {
			Collections.shuffle(options);
			allRaceOptions.addAll(options);
		}

		// next, remove from that list the player and any selected opponents
		String[] selectedOpponents = options().selectedOpponentRaces();
		isRandomOpponent = new boolean[selectedOpponents.length]; // BR: only Random Races will be customized
		allRaceOptions.remove(options().selectedPlayerRace());
		
		for (int i=0;i<maxRaces;i++) {
			if (selectedOpponents[i] != null) {
				allRaceOptions.remove(selectedOpponents[i]);
				isRandomOpponent[i] = false;
			} else {
				isRandomOpponent[i] = true;
			}
		}
		// build alien race list, replacing unselected opponents (null)
		// with remaining options
		for (int i=0;i<maxRaces;i++) {
			if (selectedOpponents[i] == null)
				raceList.add(allRaceOptions.remove(0));
			else
				raceList.add(selectedOpponents[i]);
		}
		return raceList;
	}
	private LinkedList<String> buildAlienAbilities() {
		LinkedList<String> scrambledOptions = new LinkedList<>();
		List<String> options = CustomRaceDefinitions.getAllowedAlienRaces();
		int maxRaces = options().selectedNumberOpponents();
		int mult = maxRaces/options.size() + 1;
		// Build randomized list of opponent races
		for (int i=0;i<mult;i++) {
			Collections.shuffle(options);
			scrambledOptions.addAll(options);
		}
		return scrambledOptions;
	}
	private void addPlayerSystemForGalaxy(Galaxy g, int id, List<EmpireSystem> empSystems, GalaxyCopy src) {
		// creates a star system for player, using selected options
		GalaxyBaseData galSrc = null; // Used for Restart
		EmpireBaseData empSrc = null; // Used for Restart
		if (src != null) {
			galSrc = src.galSrc;
			empSrc = galSrc.empires[id];
		}
		
		IGameOptions opts = GameSession.instance().options();
		String raceKey = opts.selectedPlayerRace();
		Race playerRace = Race.keyed(raceKey);
		String defaultName = playerRace.nextAvailableHomeworld();
		String systemName = options().selectedHomeWorldName();
		if (systemName.isEmpty())
			systemName = defaultName;
		String leaderName = opts.selectedLeaderName();
		Integer color = options().selectedPlayerColor();

		// Create DataRace
		playerDataRaceKey = raceKey;
		DynOptions options = null;
		if (UserPreferences.playerIsCustom.get())
			playerDataRaceKey = CustomRaceDefinitions.CUSTOM_RACE_KEY;
		if (src != null && !restartAppliesSettings.get()
				&& !restartChangesPlayerRace.get().equals("GuiLast")
				&& !restartChangesPlayerRace.get().equals("GuiSwap")) { // Use Restart info
			playerDataRaceKey = empSrc.dataRaceKey;
			options = empSrc.raceOptions;
		}
		Race playerDataRace = Race.keyed(playerDataRaceKey, options);
		
		// create home system for player
		StarSystem sys;
		EmpireSystem empSystem = null;
		sys = StarSystemFactory.current().newSystemForPlayer(playerRace, playerDataRace , g);
		
		if (src == null) { // Start
			empSystem = empSystems.get(id);
			sys.setXY(empSystem.colonyX(), empSystem.colonyY());
		} else { // Restart
			SystemBaseData ref = empSrc.homeSys;
			sys.setXY(ref.x, ref.y);
		}
		sys.name(systemName);
		g.addStarSystem(sys);

		// modnar: add option to start game with additional colonies
		// between 0 to 6 additional colonies, set in UserPreferences
		int numCompWorlds;
		int[] compSysId;
		if (src == null) { // Start
			numCompWorlds = UserPreferences.companionWorlds();
			compSysId = new int[numCompWorlds];
			if (numCompWorlds > 0) {
				for (int i=0; i<numCompWorlds; i++) { // BR: Symmetry management
					StarSystem sysComp = StarSystemFactory.current().newCompanionSystemForRace(g, 0);
					Point.Float pt = opts.galaxyShape().getCompanion(0, i);
					sysComp.setXY(pt.x, pt.y);
					sysComp.name(compSysName[i]+" "+sys.name()); // companion world greek letter prefix
					g.addStarSystem(sysComp);
					compSysId[i] = sysComp.id;
				}
			}
		} else { // Restart
			numCompWorlds = galSrc.numCompWorlds;
			compSysId = new int[numCompWorlds];
			if (numCompWorlds > 0) {
				for (int i=0; i<numCompWorlds; i++) {
					SystemBaseData ref = empSrc.companions[i];
					StarSystem sysComp = StarSystemFactory.current().copySystem(g, ref);
					sysComp.setXY(ref.x, ref.y);
					sysComp.name(compSysName[i]+" "+sys.name()); // companion world greek letter prefix
					g.addStarSystem(sysComp);
					compSysId[i] = sysComp.id;
				}
			}
		}
		// add Empire to galaxy
		// modnar: add option to start game with additional colonies
		// modnar: compSysId is the System ID array for these additional colonies
		// BR: Added dataRaceKey
		Empire emp = new Empire(g, id, playerRace, playerDataRace, sys, compSysId, color, leaderName, empSrc);
		g.addEmpire(emp);

		//log("Adding star system: ", sys.name(), " - ", playerRace.id, " : ", fmt(sys.x(),2), "@", fmt(sys.y(),2));

		// add other systems in this EmpireSystem
		// ensure 1st nearby system is colonizable
		if (src == null) { // Start
			boolean needHabitable = true;
			for (int i=1;i<empSystem.numSystems();i++) {
				StarSystem sys0 = StarSystemFactory.current().newSystem(g);
				if (needHabitable) {
					while ((sys0 == null) || !sys0.planet().isEnvironmentFriendly())
						sys0 = StarSystemFactory.current().newSystem(g);
					needHabitable = false;
				}
				sys0.setXY(empSystem.x(i), empSystem.y(i));
				g.addStarSystem(sys0);
			}
		} else { // Restart
			for (int i=1;i<=src.numNearBySystem();i++) {
				SystemBaseData ref = galSrc.starSystems[g.numStarSystems()];
				StarSystem sys0	= StarSystemFactory.current().copySystem(g, ref);
				sys0.setXY(ref.x, ref.y);
				g.addStarSystem(sys0);
			}
		}
//		System.out.println("End Player checkIdentique = " + checkIdentique(g));
	}
	private void addAlienRaceSystemsForGalaxy(Galaxy g, int startId,
			List<EmpireSystem> empSystems, GalaxyCopy src, LinkedList<String> alienRaces) {
		IGameOptions opts = GameSession.instance().options();
		// creates a star system for each race, and then additional star
		// systems based on the galaxy size selected at startup
		GalaxyBaseData galSrc	= null; // Used for Restart
		EmpireBaseData empSrc[]	= null; // Used for Restart
		EmpireBaseData eSrc		= null; // Used for Restart
		if (src != null) {
			galSrc = src.galSrc;
			empSrc = galSrc.empires;
		}
		// get possible banner colors, remove player's color, then randomize
		List<Integer> raceColors = new ArrayList<>();
		Integer playerC = options().selectedPlayerColor();
		boolean playerCExcluded = false;
		for (Integer i : opts.possibleColors()) {
			if ((i == playerC) && !playerCExcluded)
				playerCExcluded = true;
			else
				raceColors.add(i);
		}

		// possible the galaxy shape could not fit in all of the races
		int empId = startId;
		int maxRaces;
		if (src == null) // Start
			maxRaces = min(alienRaces.size(), empSystems.size());
		else // Restart
			maxRaces = empSrc.length-1;		

		LinkedList<String> alienAbilitiesList = null;
		if ((restartAppliesSettings.get() || src == null)
				&& randomAlienRaces.isFromFiles())
			alienAbilitiesList = buildAlienAbilities();
		
		// since we may have more races than colors we will need to reset the
		// color list each time we run out. 
		for (int h=0; h<maxRaces; h++) {
			StarSystem sys;
			String raceKey;

			if (src != null) { // BR: For Restart with new options
				eSrc = empSrc[h+1];
				raceKey = eSrc.raceKey;
			} else // Start
            	raceKey = alienRaces.get(h);

			Race race = Race.keyed(raceKey);
			if (raceColors.isEmpty()) 
				raceColors = opts.possibleColors();
			Integer colorId = raceColors.remove(0);

			// Create DataRace
			String dataRaceKey;
			DynOptions options = null;
			if (restartAppliesSettings.get()
					|| src == null) { // Then Same for Start and restart
            	if (randomAlienRaces.isRandom() && isRandomOpponent[h]) {
            		// Override random opponents
                	if (randomAlienRaces.isPlayerCopy()) {
                		dataRaceKey	= playerDataRaceKey;
                    	options		= g.empire(0).raceOptions();
                	} else if (randomAlienRaces.isFromFiles()) {
                		dataRaceKey	= alienAbilitiesList.removeFirst();
                	} else
                		dataRaceKey	= RANDOM_RACE_KEY;
                }
                else if (options().randomizeAIAbility())
                	dataRaceKey	= random(options().startingRaceOptions());
                else
                	dataRaceKey	= raceKey;
			} else {
				dataRaceKey	= eSrc.dataRaceKey;
        		options = eSrc.raceOptions;
            }
            Race dataRace = Race.keyed(dataRaceKey, options);

			EmpireSystem empSystem = null;
			sys = StarSystemFactory.current().newSystemForRace(race, dataRace, g);
			if (src == null) { // Start
				empSystem = empSystems.get(h);
				sys.setXY(empSystem.colonyX(), empSystem.colonyY());
			} else { // Restart
				SystemBaseData ref = empSrc[empId].homeSys;
				sys.setXY(ref.x, ref.y);
			}			
			sys.name(race.nextAvailableHomeworld());
			g.addStarSystem(sys);
			
			// modnar: add option to start game with additional colonies
			// between 0 to 6 additional colonies, set in UserPreferences
			int numCompWorlds;
			int[] compSysId;
			Empire emp;
			if (src == null) { // Start
				numCompWorlds = UserPreferences.companionWorlds();
				compSysId = new int[numCompWorlds];
				if (numCompWorlds > 0) {
					for (int i=0; i<numCompWorlds; i++) { // BR: Symmetry management
						StarSystem sysComp = StarSystemFactory.current().newCompanionSystemForRace(g, i+1);
						Point.Float pt = opts.galaxyShape().getCompanion(h+1, i);
						sysComp.setXY(pt.x, pt.y);
						sysComp.name(compSysName[i]+" "+sys.name()); // companion world greek letter prefix
						g.addStarSystem(sysComp);
						compSysId[i] = sysComp.id;
					}
				}
				emp = new Empire(g, empId, race, dataRace, sys, compSysId, colorId, null, null);
			} else { // Restart
				numCompWorlds = galSrc.numCompWorlds;
				compSysId = new int[numCompWorlds];
				if (numCompWorlds > 0) {
					for (int i=0; i<numCompWorlds; i++) {
						SystemBaseData ref = empSrc[empId].companions[i];
						StarSystem sysComp = StarSystemFactory.current().copySystem(g, ref);
						sysComp.setXY(ref.x, ref.y);
						sysComp.name(compSysName[i]+" "+sys.name()); // companion world greek letter prefix
						g.addStarSystem(sysComp);
						compSysId[i] = sysComp.id;
					}
				}
				emp = new Empire(g, empId, race, dataRace, sys, compSysId, colorId, null, empSrc[empId]);
			}
			g.addEmpire(emp);
			empId++;
			
			// create two nearby system within 3 light-years (required to be at least 1 habitable)
			if (src == null) { // Start
				boolean needHabitable = true;
				for (int i=1;i<empSystem.numSystems();i++) {
					StarSystem sys0 = StarSystemFactory.current().newSystem(g);
					if (opts.galaxyShape().isSymmetric()) { // BR: Symmetry management
						// BR: Symmetric Galaxy: copy Player nearby systems
						StarSystem refStar = g.starSystems()[numCompWorlds + i];
						sys0 = StarSystemFactory.current().copySystem(g, refStar);
					} else {
						if (needHabitable) {
							while ((sys0 == null) || !sys0.planet().isEnvironmentFriendly())
								sys0 = StarSystemFactory.current().newSystem(g);
							needHabitable = false;
						}
					}
					sys0.setXY(empSystem.x(i), empSystem.y(i));
					g.addStarSystem(sys0);
				}
			} else { // Restart
				for (int i=1; i<=src.numNearBySystem(); i++) {
					SystemBaseData ref = galSrc.starSystems[g.numStarSystems()];
					StarSystem sys0 = StarSystemFactory.current().copySystem(g, ref);
					sys0.setXY(ref.x, ref.y);
					g.addStarSystem(sys0);
				}
			}
//			System.out.println("End alien " + h + "  checkIdentique = " + checkIdentique(g));
		}
	}
	private void addUnsettledSystemsForGalaxy(Galaxy g, GalaxyBaseData galSrc) {
		// add Orion
		SystemBaseData ref = galSrc.starSystems[g.numStarSystems()];
		StarSystem orion = StarSystemFactory.current().newOrionSystem(g);
		orion.setXY(ref.x, ref.y);
		orion.name(text("PLANET_ORION"));
		g.addStarSystem(orion);
		
		// add all other systems
		int lim = galSrc.numStarSystems;
		for (int i=g.numStarSystems(); i<lim; i++) {
			ref = galSrc.starSystems[i];
			StarSystem sys0 = StarSystemFactory.current().copySystem(g, ref);
			sys0.setXY(ref.x, ref.y);
			g.addStarSystem(sys0);
		}
		log("total systems created: ", str(g.numStarSystems()));
	}
	private void addUnsettledSystemsForGalaxy(Galaxy g, GalaxyShape sh) {
		IGameOptions opts = GameSession.instance().options(); // BR:
		Point.Float pt = new Point.Float();
		// add Orion, index =0;
		StarSystem orion = StarSystemFactory.current().newOrionSystem(g);
		sh.coords(0, pt);
		orion.setXY(pt.x, pt.y);
		orion.name(text("PLANET_ORION"));
		g.addStarSystem(orion);
		
		// add all other systems, starting at index 1
		if (opts.galaxyShape().isSymmetric()) { // BR: Symmetry management
			for (int i=1; i<sh.numberStarSystems(); i+=sh.numEmpires) {
				// first symmetry system
				StarSystem refSys = StarSystemFactory.current().newSystem(g);
				sh.coords(i, pt);
				refSys.setXY(pt.x, pt.y);
				g.addStarSystem(refSys);
				// other symmetry systems
				for (int k=i+1; k<i+sh.numEmpires; k++) {
					StarSystem sys = StarSystemFactory.current().copySystem(g, refSys);
					sh.coords(k, pt);
					sys.setXY(pt.x, pt.y);
					g.addStarSystem(sys);
				}
			}
		} else {
			for (int i=1;i<sh.numberStarSystems();i++) {
				StarSystem sys = StarSystemFactory.current().newSystem(g);
				sh.coords(i, pt);
				sys.setXY(pt.x, pt.y);
				g.addStarSystem(sys);
			}
		}
		log("total systems created: ", str(g.numStarSystems()));
	}
	private void addNebulas(Galaxy g, GalaxyCopy src) { // BR: For Restart with new options
		int numNebula = src.galSrc.nebulas.size();
		float nebSize = src.nebulaSizeMult;
		g.initNebulas(numNebula);
		for (Nebula nebula : src.galSrc.nebulas) {
			g.addNebula(nebula, nebSize);
		}
	}
	private void addNebulas(Galaxy g, GalaxyShape shape) {
		IGameOptions opts = GameSession.instance().options();
		// creates a star system for each race, and then additional star
		// systems based on the galaxy size selected at startup
		int numNebula= opts.numberNebula();
		float nebSize = options().nebulaSizeMult();
		g.initNebulas(numNebula);
		
		// add the nebulas
		// for each nebula, try to create it at the options size
		// in unsuccessful, decrease option size until it is
		// less than 1 or less than half of the optoin size
		for (int i=0;i<numNebula;i++) {
			float size = nebSize;
			boolean added = false;
			while(!added) {
				added = g.addNebula(shape,size);
				if (!added) {
					size--;
					added = size < 1;
				}
			}
		}
	}
	public static class GalaxyCopy {
		private IGameOptions newOptions;
		private IGameOptions oldOptions;
		private GalaxyBaseData galSrc;
		private float nebulaSizeMult;
		private LinkedList<String> alienRaces;

		public GalaxyCopy (IGameOptions newOpts) { newOptions = newOpts; }
		public void copy (GameSession oldS) { // Copy from the old session
			galSrc			= new GalaxyBaseData(oldS.galaxy());
			oldOptions		= oldS.options();
			nebulaSizeMult	= oldOptions.nebulaSizeMult();
			newOptions.copyForRestart(oldOptions); // Copy galaxy settings from old options
		}
		public void selectEmpire(int index) {
			galSrc.swapPlayer(index);
			
			// Change player if required and
			// Set the Options
			if (!restartAppliesSettings.get()) { // Keeps old Settings
				String oldRace = empires(0).raceKey;
				// Old options replace new options
				// Copy what's needed from new options
				switch (restartChangesPlayerRace.get()) {
				case "GuiLast":
				case "GuiSwap":
						// set Gui Player
						oldOptions.selectedPlayer().race = newOptions.selectedPlayerRace();
						oldOptions.selectedHomeWorldName(newOptions.selectedHomeWorldName());
						oldOptions.selectedLeaderName	(newOptions.selectedLeaderName());
						empires(0).raceKey = newOptions.selectedPlayerRace();
						break;
					default: // Swap Race
						oldOptions.selectedPlayer().race = empires(0).raceKey;
						oldOptions.selectedHomeWorldName(empires(0).homeSys.starName);
						oldOptions.selectedLeaderName	(empires(0).leaderName);
				}
				if (restartChangesAliensAI.get()) {
					setAliensAIOptions((MOO1GameOptions) newOptions, (MOO1GameOptions) oldOptions);
				} 
				if (restartChangesPlayerAI.get()) {
					oldOptions.selectedAutoplayOption(newOptions.selectedAutoplayOption());
				} 
				newOptions = oldOptions;
				alienRaces = new LinkedList<>();
				for (EmpireBaseData e : empires())
					alienRaces.add(e.raceKey);

				// check for too many of new player race!
				String rSearch = alienRaces.getFirst();
				int occurrences = Collections.frequency(alienRaces, rSearch);
				if (occurrences > IGameOptions.MAX_OPPONENT_TYPE) {
					// Too many of new player race!
					// Change the next one to old race
					for (int i=1; i<alienRaces.size(); i++) {
						String race = alienRaces.get(i);
						if (race != null && race.equalsIgnoreCase(rSearch)) {
							alienRaces.add(i, oldRace);
							break;
						}
					}
				}
			}
		}
		public	IGameOptions options()			{ return newOptions; }
		private	int numNearBySystem()			{ return 2; }
		public	EmpireBaseData[] empires()		{ return galSrc.empires; }
		private	EmpireBaseData empires(int id)	{ return galSrc.empires[id]; }
	}
}
