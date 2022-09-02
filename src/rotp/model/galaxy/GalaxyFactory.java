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

import static rotp.ui.UserPreferences.loadWithNewOptions;
import static rotp.ui.UserPreferences.randomAlienRaces;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import rotp.mod.br.addOns.RacesOptions;
import rotp.mod.br.addOns.ShipSetAddOns;
import rotp.model.empires.CustomRaceFactory;
import rotp.model.empires.Empire;
import rotp.model.empires.Leader;
import rotp.model.empires.Race;
import rotp.model.galaxy.GalaxyShape.EmpireSystem;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.planet.Planet;
import rotp.model.tech.Tech; // modnar: add game mode to start all Empires with 2 random techs
import rotp.model.tech.TechTree; // modnar: add game mode to start all Empires with 2 random techs
import rotp.ui.UserPreferences; // modnar: add game mode to start all Empires with 2 random techs
import rotp.ui.game.PlayerRaceCustomizationUI;
import rotp.ui.util.planets.PlanetImager;
import rotp.util.Base;

public class GalaxyFactory implements Base {
	static GalaxyFactory instance = new GalaxyFactory();
	public static GalaxyFactory current()   { return instance; }
	/**
	 * Companion world greek letter prefix
	 */
	public static final String[] compSysName = new String[]{"α", "β", "γ", "δ", "ε", "ζ"};// BR : added two possibilities
	private static boolean[] isRandomOpponent; // BR: only Random Races will be customized
	private static String playerDataRaceKey;   // BR: in case Alien races are a copy of player race

	public Galaxy newGalaxy(GalaxyCopy gc) { // BR: For restarting with new options
		for (Race r: Race.races()) {
			r.loadNameList();
			r.loadLeaderList();
			r.loadHomeworldList();
		}
		IGameOptions opts = gc.options();
		opts.randomizeColors();
		Galaxy g = new Galaxy(gc);

		GameSession.instance().galaxy(g);
		Race playerRace = Race.keyed(opts.selectedPlayerRace());
		if(!ShipSetAddOns.isOriginalShipSet())
			playerRace.preferredShipSet = ShipSetAddOns.playerShipSet();

		LinkedList<String> alienRaces = gc.alienRaces();
		addNebulas(g, gc);
		List<String> systemNames = playerRace.systemNames;
		Collections.shuffle(systemNames);

		addPlayerSystemForGalaxy(g, 0, null, gc);
		addAlienRaceSystemsForGalaxy(g, 1, null, gc, alienRaces);
		addUnsettledSystemsForGalaxy(g, gc);		
		init(g, System.currentTimeMillis());
		
//		showEmp(g);
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
		if(!ShipSetAddOns.isOriginalShipSet())
			playerRace.preferredShipSet = ShipSetAddOns.playerShipSet();

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
//		showEmp(g);
		return g;
	}
	@SuppressWarnings("unused")
	private void showEmp(Galaxy g) {
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
					+ String.format("%-22sID=", emp.diplomatAI())
					+ String.format("%-4sx=", id)
					+ String.format("%-11sy=", sys.x())
					+ String.format("%-11sAI=", sys.y())
					+ String.format("%-4s", emp.selectedAI)
					);
		}
		System.out.println();
	}
	// BR: Common part of "Restart" standard "Start"
	public void init(Galaxy g, long tm2) {
		// after systems created, add system views for each empire
		for (Empire e: g.empires()) {
			e.loadStartingTechs();
			
			// modnar: add game mode to start all Empires with 2 random techs
			if (UserPreferences.randomTechStart()) {
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

		g.player().makeNextTurnDecisions();
		g.player().refreshViews();
		long tm5 = System.currentTimeMillis();
		log("Next Turn Decision: "+(tm5-tm4)+"ms");

		PlanetImager.current().finished();
	}
	private LinkedList<String> buildAlienRaces() {
		LinkedList<String> raceList = new LinkedList<>();
		List<String> allRaceOptions = new ArrayList<>();
//		List<String> options = options().startingRaceOptions();
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
	private void addPlayerSystemForGalaxy(Galaxy g, int id, List<EmpireSystem> empSystems, GalaxyCopy gc) {
		// creates a star system for player, using selected options
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
		if (UserPreferences.customPlayerRace.get()) {
			playerDataRaceKey = PlayerRaceCustomizationUI.cr.getKey();
		}
		if (gc != null) { // Restart
			if (!loadWithNewOptions.get()) {
				playerDataRaceKey = gc.empires(0).abilitiesKey();
			}
		}
		Race playerDataRace = Race.keyed(playerDataRaceKey);
		
		// create home system for player
		StarSystem sys;
		EmpireSystem empSystem = null;
		sys = StarSystemFactory.current().newSystemForPlayer(playerRace, playerDataRace , g);
		
		if (gc == null) { // Start
			empSystem = empSystems.get(id);
			sys.setXY(empSystem.colonyX(), empSystem.colonyY());
		} else { // Restart
			StarSystem ref = gc.starSystem(gc.empires(id).capitalSysId());
			sys.setXY(ref.x(), ref.y());
		}
		sys.name(systemName);
		g.addStarSystem(sys);

		// modnar: add option to start game with additional colonies
		// between 0 to 6 additional colonies, set in UserPreferences
		int numCompWorlds;
		int[] compSysId;
		if (gc == null) { // Start
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
			numCompWorlds = gc.empires(id).getCompanionWorldsNumber();
			compSysId = new int[numCompWorlds];
			if (numCompWorlds > 0) {
				for (int i=0; i<numCompWorlds; i++) {
					StarSystem ref		= gc.starSystem(gc.empires(id).compSysId(i));
					StarSystem sysComp	= StarSystemFactory.current().copySystem(g, ref);
					sysComp.setXY(ref.x(), ref.y());
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
		Empire emp = new Empire(g, id, raceKey, playerDataRaceKey, sys, compSysId, color, leaderName, gc);
		g.addEmpire(emp);

		//log("Adding star system: ", sys.name(), " - ", playerRace.id, " : ", fmt(sys.x(),2), "@", fmt(sys.y(),2));

		// add other systems in this EmpireSystem
		// ensure 1st nearby system is colonizable
		if (gc == null) { // Start
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
			for (int i=1;i<=gc.numNearBySystem();i++) {
				StarSystem ref	= gc.starSystem(g.numStarSystems());
				StarSystem sys0	= StarSystemFactory.current().copySystem(g, ref);
				sys0.setXY(ref.x(), ref.y());
				g.addStarSystem(sys0);
			}
		}
	}
	private void addAlienRaceSystemsForGalaxy(Galaxy g, int startId,
			List<EmpireSystem> empSystems, GalaxyCopy gc, LinkedList<String> alienRaces) {
		IGameOptions opts = GameSession.instance().options();
		// creates a star system for each race, and then additional star
		// systems based on the galaxy size selected at startup

		// get possible banner colors, remove player's color, then randomize
		List<Integer> raceColors = new ArrayList<>();
		Integer playerC = options().selectedPlayerColor();
		boolean playerCExcluded = false;
		for (Integer i : opts.possibleColors()) {
			if ((i == playerC) && !playerCExcluded)
				playerCExcluded = true;
			else
				raceColors.add(i);
		};

		// possible the galaxy shape could not fit in all of the races
		int empId = startId;
		int maxRaces;
		if (gc == null) // Start
			maxRaces = min(alienRaces.size(), empSystems.size());
		else // Restart
			maxRaces = gc.empires().length-1;		
		// since we may have more races than colors we will need to reset the
		// color list each time we run out. 
		for (int h=0; h<maxRaces; h++) {
			StarSystem sys;
			String raceKey = alienRaces.get(h);
			Race race = Race.keyed(raceKey);
			if (raceColors.isEmpty()) 
				raceColors = opts.possibleColors();
			Integer colorId = raceColors.remove(0);

			// Create DataRace
			String dataRaceKey;
            if (gc != null) // BR: For Restart with new options 
            	dataRaceKey = gc.dataRace().get(h);
            else if (options().randomizeAIAbility())
                dataRaceKey = random(options().startingRaceOptions());
            else
                dataRaceKey = raceKey;
            if (gc == null && randomAlienRaces.isRandom() && isRandomOpponent[h]) {
            	if (randomAlienRaces.isPlayerCopy())
            		dataRaceKey = playerDataRaceKey;
            	else
            		dataRaceKey = CustomRaceFactory.getRandomAlienRaceKey();
            }
//            System.out.println("RO = " + isRandomOpponent[h] + "  Key = " + dataRaceKey);
			Race dataRace = Race.keyed(dataRaceKey);

			EmpireSystem empSystem = null;
			sys = StarSystemFactory.current().newSystemForRace(race, dataRace, g);
			if (gc == null) { // Start
				empSystem = empSystems.get(h);
				sys.setXY(empSystem.colonyX(), empSystem.colonyY());
			} else { // Restart
				StarSystem ref = gc.starSystem(gc.empires(empId).capitalSysId());
				sys.setXY(ref.x(), ref.y());
			}			
			sys.name(race.nextAvailableHomeworld());
			g.addStarSystem(sys);
			
			// modnar: add option to start game with additional colonies
			// between 0 to 6 additional colonies, set in UserPreferences
			int numCompWorlds;
			int[] compSysId;
			if (gc == null) { // Start
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
			} else { // Restart
				numCompWorlds = gc.empires(empId).getCompanionWorldsNumber();
				compSysId = new int[numCompWorlds];
				if (numCompWorlds > 0) {
					for (int i=0; i<numCompWorlds; i++) {
						StarSystem ref		= gc.starSystem(gc.empires(empId).compSysId(i));
						StarSystem sysComp	= StarSystemFactory.current().copySystem(g, ref);
						sysComp.setXY(ref.x(), ref.y());
						sysComp.name(compSysName[i]+" "+sys.name()); // companion world greek letter prefix
						g.addStarSystem(sysComp);
						compSysId[i] = sysComp.id;
					}
				}
			}

			// modnar: add option to start game with additional colonies
			// modnar: compSysId is the System ID array for these additional colonies
			Empire emp = new Empire(g, empId, raceKey, dataRaceKey, sys, compSysId, colorId, null, gc);
			g.addEmpire(emp);
			empId++;
			
			// create two nearby system within 3 light-years (required to be at least 1 habitable)
			if (gc == null) { // Start
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
				for (int i=1;i<=gc.numNearBySystem();i++) {
					StarSystem ref = gc.starSystem(g.numStarSystems());
					StarSystem sys0 = StarSystemFactory.current().copySystem(g, ref);
					sys0.setXY(ref.x(), ref.y());
					g.addStarSystem(sys0);
				}
			}
		}
	}
	private void addUnsettledSystemsForGalaxy(Galaxy g, GalaxyCopy gc) {
		// add Orion
		StarSystem ref = gc.starSystem(g.numStarSystems());
		StarSystem orion = StarSystemFactory.current().newOrionSystem(g);
		orion.setXY(ref.x(), ref.y());
		orion.name(text("PLANET_ORION"));
		g.addStarSystem(orion);
		
		// add all other systems
		int lim = gc.totalStarSystems();
		for (int i=g.numStarSystems(); i<lim; i++) {
			ref = gc.starSystem(i);
			StarSystem sys0 = StarSystemFactory.current().copySystem(g, ref);
			sys0.setXY(ref.x(), ref.y());
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
	private void addNebulas(Galaxy g, GalaxyCopy gc) { // BR: For Restart with new options
		int numNebula = gc.nebulas().size();
		float nebSize = gc.nebulaSizeMult();
		g.initNebulas(numNebula);
		for (Nebula nebula : gc.nebulas()) {
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
}
