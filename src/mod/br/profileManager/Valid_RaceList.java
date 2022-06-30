
/*
 * Licensed under the GNU General License, Version 3 (the "License");
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

package mod.br.profileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.PMutil;
import br.profileManager.src.main.java.Validation;
import mod.br.AddOns.RaceFilter;
import rotp.model.game.IGameOptions;

/**
 * For the validation of the Race Lists
 */
class Valid_RaceList extends Validation<String> {

	// ==================================================
	// Constructors and initializers
	//
	Valid_RaceList(AbstractT<String> initialValue, List<String> options) {
		super(initialValue, options);
	}

	// ========== Getters ==========
	//
	String[] analyze(ClientClasses go, List<String> userEntry
			, int maxOpponentType, boolean neverNull) {
		return new newAnalyze(go, userEntry, maxOpponentType, neverNull).selectedOpponents();
	}

	// ==============================================================
	// Nested Class
	//
	class newAnalyze {
		private List<String> startingList;	 // The original List
		private List<String> allRaceOptions; // The full Scrambled list
		private List<String> optionList;	 // The remaining List
		private String[] selectedOpponents;
		private int maxOpponentType;
		private int index;
		private int maxArray;
		private boolean neverNull;

		newAnalyze(ClientClasses go, List<String> userEntry
				, int maxOpponentType, boolean neverNull) {
			this.maxOpponentType = maxOpponentType;
			initOptions(go);

			List<String> randomGui  = RaceFilter.selectedGuiRaceFilter();
			List<String> randomGame = RaceFilter.selectedGameRaceFilter();
			int iMax = selectedOpponents.length;
			int iMaxList = userEntry.size()-1;
			int iList;

			String entry = "";
			// loop thru the list
			for (index=0; index<iMax; index++) {
				iList = Math.min(index, iMaxList);
				entry = userEntry.get(iList).strip().toUpperCase();
				switch(entry) {
				case "NULL":
					setNull();
					break;
				case "GUI":
					randomWithOptions(randomGui);
					break;
				case "GAME":
					randomWithOptions(randomGame);
					break;
				case "RANDOM":
					randomWithOptions(startingList);					
					break;
				case "":
					validBlankEntry();
					break;
				default:
					elementAnalysis(entry);
				}
			}
		}
		String[] selectedOpponents() {
			return selectedOpponents;
		}
		
		// ========== Analysis Methods ==========
		//
		private void randomWithOptions(List<String> parameters) {
			if (buildOptionList(parameters)) {
				int id = PMutil.getRandom(0, optionList.size());
				validEntry(optionList.get(id));
				return;
			}
			// End of possibilities
			setNull();
			return;
		}

		private AbstractT<String> randomWithInListLimit(String[] parameters) {
			int[] limits = validateLimits(parameters);
			List<String> subList = startingList.subList(limits[0], limits[1]);
			if (buildOptionList(subList)) {
				int id = PMutil.getRandom(0, optionList.size());
				return validEntry(optionList.get(id));
			}
			// End of possibilities
			return setNull();
		}

		private void elementAnalysis(String userEntry) {
			userEntry = PMutil.clean(userEntry);
			// Random Management
			if (isRandom(userEntry)) { // random with extra parameters
				String[] parameters = splitParameters(removeRandomId(userEntry));
				if (parameters.length > 2) {
					randomWithOptions(Arrays.asList(parameters));
					return;
				}
				randomWithInListLimit(parameters);
				return;
			}
			// Not Random
			// Check check if part of the list 
			if (isValidUserView(userEntry)) {
				validEntry(toValue(userEntry).getCodeView());
				return;
			} 
			// Bad entry, then either blank or null
			validBlankEntry();
			return;
		}
		
		// ========== Other Methods ==========
		// 
		private boolean buildOptionList(List<String> userList) {
			optionList = userList.stream()
					.filter(allRaceOptions::contains)
					.collect(Collectors.toList());
			return optionList.size() > 0;
		}
		
		private AbstractT<String> validEntry(String race) {
			if (isValidRace(race)) {
				allRaceOptions.remove(race);
				return setRace(race);
			}
			return setNull();
		}
		
		private AbstractT<String> validBlankEntry() {
			if (maxArray > index) {
				String selectedRace = selectedOpponents[index];
				if (isValidRace(selectedRace)) {
					allRaceOptions.remove(selectedRace);
					return setBlank();
				}
				// to much of this race selected
				return setNull();
			}
			// no race selected OK for blank value
			return setBlank();
		}
		
		private AbstractT<String> setRace(String race) {
			if (maxArray > index) {
				selectedOpponents[index] = race;
			}
			return newValue(race);
		}

		private AbstractT<String> setBlank() {
			return newValue(getBlankCodeView());
		}

		private AbstractT<String> setNull() {
			if (neverNull && allRaceOptions.size() > 0) {
				String race = allRaceOptions.remove(0);
				return setRace(race);
			}
			setRace(null);
			return newValue("Null");
		}

		private boolean isValidRace(String race) {
			return allRaceOptions.contains(race);
		}

		private void initOptions(ClientClasses go) {
			if (maxOpponentType == 0) {
				maxOpponentType = IGameOptions.MAX_OPPONENT_TYPE;
			}
			// init allRaceOptions
			startingList = go.newOptions().startingRaceOptions();
			allRaceOptions = new ArrayList<String>();
			for (int i=0; i<maxOpponentType; i++) {
				allRaceOptions.addAll(startingList);
			}
			// remove the player from the list and shuffle
			allRaceOptions.remove(go.newOptions().selectedPlayerRace());
			Collections.shuffle(allRaceOptions);
			// load already set opponents
	        selectedOpponents = go.newOptions().selectedOpponentRaces().clone();
			maxArray = selectedOpponents.length;
		}
	}
}
