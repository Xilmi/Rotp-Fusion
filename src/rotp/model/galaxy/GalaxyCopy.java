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
package rotp.model.galaxy;

import static rotp.ui.UserPreferences.restartApplySettings;
import static rotp.ui.UserPreferences.restartChangeAI;
import static rotp.ui.UserPreferences.restartApplyPlayer;

import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.empires.Leader.Objective;
import rotp.model.empires.Leader.Personality;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.game.MOO1GameOptions;

// BR: Added symmetric galaxies functionalities
// moved modnar companion worlds here with some more random positioning
// added some comments to help my understanding of this class
public class GalaxyCopy extends GalaxyShape {
	private static final long serialVersionUID = 1L;
	private static final boolean debug = false;
	private IGameOptions newOptions;
	private IGameOptions oldOptions;
	private float maxScaleAdj;
	private float nebulaSizeMult;
	private int numStarSystems;
	private String selectedGalaxySize;
	private List<Nebula> nebulas;
	private int selectedEmpire = 0;
	private Empire[] empires;
	private StarSystem[] starSystem;
	private LinkedList<String> alienRaces;
	private LinkedList<String> dataRace;
	private LinkedList<Integer> raceAI;
	private LinkedList<Personality> personality;
	private LinkedList<Objective> objective;
	private int numCompWorlds;
	private String newRaceKey;
	private String oldRaceKey;
	private String newDataRaceKey;
	private String oldDataRaceKey;

	// ========== Constructors and Initializers ==========
	//
	public GalaxyCopy (IGameOptions newO) {
		newOptions	= newO;
	}
	public void copy (GameSession oldS) { // Copy from the old session
		// Copy Galaxy
		Galaxy oldG = oldS.galaxy();
		width	= oldG.width();
		height	= oldG.height();
		nebulas	= oldG.nebulas();
		empires	= oldG.empires();
		maxScaleAdj		= oldG.maxScaleAdj();
		starSystem		= oldG.originalStarSystem();
		numStarSystems	= oldG.numStarSystems();
		numCompWorlds	= empires[0].getCompanionWorldsNumber();
		
		oldOptions		= oldS.options();
		numOpponents	= oldOptions.selectedNumberOpponents();
		numEmpires		= numOpponents + 1;
		nebulaSizeMult	= oldOptions.nebulaSizeMult();
		oldRaceKey		= empires[0].raceKey();
		oldDataRaceKey	= empires[0].abilitiesKey();
		selectedGalaxySize	= oldOptions.selectedGalaxySize();

//		if (restartApplyPlayer.get())
//			newRaceKey	= newOptions.selectedPlayerRace();
//		else
//			newRaceKey	= oldRaceKey;

		// Copy Races
		dataRace	= new LinkedList<>();
		alienRaces	= new LinkedList<>();
		raceAI		= new LinkedList<>();
		personality	= new LinkedList<>();
		objective	= new LinkedList<>();
		for (Empire emp : oldG.empires()) {
			if (emp != null && emp.id!=0) {
				alienRaces.add(emp.race().name());
				dataRace.add(emp.abilitiesKey());
				personality.add(emp.leader().personality);
				objective.add(emp.leader().objective);
				if (!restartChangeAI.get())
					raceAI.add(emp.selectedAI);
				else
					raceAI.add(newOptions.selectedAI(emp));
			}
		}
		
		// Set the Options
		if (restartApplySettings.get()) {
			newOptions.copyForRestart(oldOptions); // Copy what's needed from old options
		} else {
			// Copy what's needed from new options
			if (restartApplyPlayer.get()) {
				oldOptions.selectedPlayer().race = newOptions.selectedPlayerRace();
				oldOptions.selectedHomeWorldName(newOptions.selectedHomeWorldName());
				oldOptions.selectedLeaderName	(newOptions.selectedLeaderName());
			}
			if (restartChangeAI.get()) {
				MOO1GameOptions.setAIOptions((MOO1GameOptions) newOptions,
											 (MOO1GameOptions) oldOptions);
			}
			newOptions = oldOptions;
		}
		if (debug) {
			System.out.println();
			System.out.println("====================================================================");
			System.out.println();
			System.out.println("copy checkIdentique = " + checkIdentique());
		}
	}
	// ========== Public Setters ==========
	//
	public void selectedEmpire(int index) {
		// Swap empires
		Empire	empSwap	= empires[index];
		empires[index]	= empires[0];
		empires[0]		= empSwap;
		dataRace.set(index-1, empires[0].abilitiesKey());
		
		// Swap near by systems (for the AI scouting)
		
		// Change player if required
		
		// Check for Max allowed identical races
		
		// Update options according to requirements
		
		if (debug)
			System.out.println("selected empire index = " + index);
		selectedEmpire	= index;
		if (index == 0) { // Selected current player:
			// Check total number of player race
			if (countRace(newRaceKey) >= IGameOptions.MAX_OPPONENT_TYPE) {
				if (debug)
					System.out.println(" count race = " + countRace(newRaceKey));
				// replace first occurrence with old player race
				swapRaces(newRaceKey, oldRaceKey);
			}
			if (debug)
				System.out.println("selectedEmpire = 0 checkIdentique = " + checkIdentique());
			return;
		}
		// Swap near by systems for the AI
		int PlayerNbId	= firstNearbySystem(0);
		int SwapNbId	= firstNearbySystem(index);
		StarSystem sysSwap;
		for (int i=0; i<numNearBySystem(); i++) {
			sysSwap	= starSystem[SwapNbId+i];
			starSystem[SwapNbId+i]	 = starSystem[PlayerNbId+i];
			starSystem[PlayerNbId+i] = sysSwap;			
		}

		if (restartApplySettings.get()) {
			// Nothing to do, already the good player
		} else {
			options().selectedPlayer().race = empires[0].raceKey();
			options().selectedHomeWorldName	("");
			options().selectedLeaderName (starSystem(empires[0].homeSysId()).name());
		}
		if (debug)
			System.out.println("selectedEmpire checkIdentique = " + checkIdentique());
	}

	// ========== Public Getters ==========
	//
	@Override public float maxScaleAdj()		{ return maxScaleAdj; }
	@Override public int totalStarSystems()		{ return numStarSystems; }
	@Override public IGameOptions options() 	{ return newOptions; }
	public StarSystem[] starSystem()			{ return starSystem; }
	public StarSystem starSystem(int i)			{ return starSystem[i]; }
	public String selectedGalaxySize()			{ return selectedGalaxySize; }
	public LinkedList<String> alienRaces()		{ return alienRaces; }
	public LinkedList<String> dataRace()		{ return dataRace; }
	public LinkedList<Integer> raceAI()			{ return raceAI; }
	public LinkedList<Personality> personality(){ return personality; }
	public LinkedList<Objective> objective()	{ return objective; }
	public List<Nebula> nebulas()				{ return nebulas; }
	public Empire[] empires()					{ return empires; }
	public Empire empires(int i)				{ return empires[i]; }
	public float nebulaSizeMult()				{ return nebulaSizeMult; }
	public int numNearBySystem()				{ return 2; }
	public int selectedEmpire()					{ return selectedEmpire; }
	
	// ========== Private Methods ==========
	private boolean checkIdentique() {
		System.out.println("num Star Systems = " + numStarSystems + "num Empire = " + numEmpires);
		for (int i=0; i<numStarSystems; i++) {
			System.out.println("Systems " + i
					+ "	x = " + starSystem[i].x()
					+ "	y = " + starSystem[i].y());			
		}
		boolean identique = false;
		int lim = starSystem.length-1;
		for (int i=0; i<lim; i++) {
			float x1 = starSystem[i].x();
			float y1 = starSystem[i].y();
			for (int k=i+1; k<=lim; k++) {
				float x2 = starSystem[k].x();
				float y2 = starSystem[k].y();
				double distSq = Math.pow(x2-x1, 2.0) + Math.pow(y2-y1, 2.0);
				if (distSq < 0.5) {
					System.out.println("========== i = " + i + "  k = " + k + "  distSq = " + distSq);
					identique = true;
				}
			}
		}
		lim = numOpponents;
		for (int i=0; i<lim; i++) {
			int h1 = empires[i].homeSysId();
			for (int k=i+1; k<=lim; k++) {
				int h2 = empires[k].homeSysId();
				if (h1 == h2) {
					System.out.println("========== i = " + i + "  k = " + k + "  Home World ID = " + h1);
					identique = true;
				}
			}
		}
		return identique;
	}
	private int firstNearbySystem(int empId) {
		return empId * (1 + numCompWorlds + numNearBySystem()) + numCompWorlds + 1;
	}
	private int countRace (String race) {
		int count = 0;
		for (String r : alienRaces) {
			if (r != null && r.equalsIgnoreCase(race)) count++;
		}
		return count;
	}
	private void swapRaces (String rSearch, String rReplace) {
		for (int i=0; i<alienRaces.size(); i++) {
			String race = alienRaces.get(i);
			if (race != null && race.equalsIgnoreCase(rSearch)) {
				alienRaces.add(i, rReplace);
				return;
			}
		}
	}
	
	@Override public void setSpecific(Float p)	{}
	@Override protected int galaxyWidthLY()		{ return 0; }
	@Override protected int galaxyHeightLY()	{ return 0; }
	@Override public void setRandom(Float p)	{}
	@Override public boolean valid(float x, float y)	{ return false; }
	@Override protected float sizeFactor(String size)	{ return 0; }

}
