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

import static rotp.ui.UserPreferences.loadWithNewOptions;

import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.empires.Leader.Objective;
import rotp.model.empires.Leader.Personality;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;

// BR: Added symmetric galaxies functionalities
// moved modnar companion worlds here with some more random positioning
// added some comments to help my understanding of this class
public class GalaxyCopy extends GalaxyShape {
	private static final long serialVersionUID = 1L;
	private IGameOptions options;
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
	private String newRace;
	private String oldRace;

	// ========== Constructors and Initializers ==========
	//
	public GalaxyCopy (IGameOptions newO) {
		options	= newO;
	}
	public void copy (GameSession s) {
		IGameOptions oldO = s.options();
		newRace			= options.selectedPlayerRace();
		oldRace			= oldO.selectedPlayerRace();
		numOpponents	= oldO.selectedNumberOpponents();
		numEmpires		= numOpponents + 1;
		nebulaSizeMult	= oldO.nebulaSizeMult();

		if (loadWithNewOptions.get()) {
			initForNewOptions(s);
		} else {
			initForOldOptions(s);
		}
		selectedGalaxySize = s.options().selectedGalaxySize();
		copyGalaxy(s.galaxy());
		copyRaces(s.galaxy());
		numCompWorlds = empires[0].getCompanionWorldsNumber();
//		System.out.println();
//		System.out.println("====================================================================");
//		System.out.println();
//		System.out.println("copy checkIdentique = " + checkIdentique());
	}
	private void initForOldOptions(GameSession s) {
		// Copy what's needed from new options
		s.options().selectedPlayer().race = options.selectedPlayerRace();
		s.options().selectedHomeWorldName(options.selectedHomeWorldName());
		s.options().selectedLeaderName	 (options.selectedLeaderName());
		options = s.options();
	}
	private void initForNewOptions(GameSession s) {
		// Copy what's needed from old options
		IGameOptions oldO = s.options();
		options.copyForRestart(oldO);
	}
	@SuppressWarnings("unused")
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
	// ========== Public Setters ==========
	//
	public void selectedEmpire(int index) {
//		System.out.println("selected empire index = " + index);
		selectedEmpire	= index;
		if (index == 0) { // Selected current player:
			// Check total number of player race
			if (countRace(newRace) >= IGameOptions.MAX_OPPONENT_TYPE) {
//				System.out.println(" count race = " + countRace(newRace));
				// replace first occurrence with old player race
				swapRaces(newRace, oldRace);
			}
//			System.out.println("selectedEmpire = 0 checkIdentique = " + checkIdentique());
			return;
		}
		Empire	empSwap	= empires[index];
		empires[index]	= empires[0];
		empires[0]		= empSwap;
		dataRace.set(index-1, empires[0].abilitiesKey());
		// Swap near by systems for the AI
		int PlayerNbId	= firstNearbySystem(0);
		int SwapNbId	= firstNearbySystem(index);
		StarSystem sysSwap;
		for (int i=0; i<numNearBySystem(); i++) {
			sysSwap	= starSystem[SwapNbId+i];
			starSystem[SwapNbId+i]	 = starSystem[PlayerNbId+i];
			starSystem[PlayerNbId+i] = sysSwap;			
		}

		if (loadWithNewOptions.get()) {
			// Nothing to do, already the good player
		} else {
			options().selectedPlayer().race = empires[0].raceKey();
			options().selectedHomeWorldName	("");
			options().selectedLeaderName	(starSystem(empires[0].homeSysId()).name());
		}
//		System.out.println("selectedEmpire checkIdentique = " + checkIdentique());
	}

	// ========== Public Getters ==========
	//
	@Override public float maxScaleAdj()		{ return maxScaleAdj; }
	@Override public int totalStarSystems()		{ return numStarSystems; }
	@Override public IGameOptions options() 	{ return options; }
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
	private int firstNearbySystem(int empId) {
		return empId * (1 + numCompWorlds + numNearBySystem()) + numCompWorlds + 1;
	}
	private void copyGalaxy(Galaxy g) {
		width	= g.width();
		height	= g.height();
		
		maxScaleAdj		= g.maxScaleAdj();
		numStarSystems	= g.numStarSystems();
		nebulas			= g.nebulas();
		empires			= g.empires();
		starSystem		= g.starSystems();
	}
	private void copyRaces (Galaxy g) {
		dataRace	= new LinkedList<>();
		alienRaces	= new LinkedList<>();
		raceAI		= new LinkedList<>();
		personality	= new LinkedList<>();
		objective	= new LinkedList<>();
		for (Empire emp : g.empires()) {
			if (emp != null && emp.id!=0) {
				alienRaces.add(emp.race().name());
				dataRace.add(emp.abilitiesKey());
				raceAI.add(emp.selectedAI);
				personality.add(emp.leader().personality);
				objective.add(emp.leader().objective);
			}
		}
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
