
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

import static rotp.model.game.IGameOptions.MAX_OPPONENTS;

import java.util.Arrays;
import java.util.List;

import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.PMutil;
import br.profileManager.src.main.java.Validation;
import mod.br.AddOns.RaceFilter;
import rotp.model.game.IGameOptions;

/**
 * For the validation of the Opponent AI Lists
 */
class Valid_AIList extends Validation<String> {

	// ==================================================
	// Constructors and initializers
	//
	Valid_AIList(AbstractT<String> initialValue, List<String> options) {
		super(initialValue, options);
	}

	// ========== Getters ==========
	//
	String[] analyze(ClientClasses go, List<String> userEntry, boolean gameCall) {
		return new newAnalyze(go, userEntry, gameCall).selectedAIs();
	}

	// ==============================================================
	// Nested Class
	//
	class newAnalyze {
		private List<String> defaultGuiList;	 // The longest one
		private List<String> defaultGameList;
		private List<String> randomList;	 // The original List 
		private List<String> allRandomList;	 // The original List
		private List<String> randomGuiList;
		private List<String> randomGameList;
		private String blankReplacement = IGameOptions.OPPONENT_AI_RANDOM;
		private String nullReplacement  = IGameOptions.OPPONENT_AI_RANDOM;
		private String[] selectedAIs;
		private int index;
		private int maxArray;
		private boolean gameCall;

		newAnalyze(ClientClasses go, List<String> userEntry, boolean gameCall) {
			initOptions();

			int iMax = selectedAIs.length;
			int iMaxList = userEntry.size()-1;
			int iList;

			String entry = "";
			// loop thru the list
			for (index=0; index<iMax; index++) {
				iList = Math.min(index, iMaxList);
				entry = userEntry.get(iList).strip().toUpperCase();
				selectedAIs[index] = go.newOptions().specificOpponentAIOption(index);
				switch(entry) {
				case "NULL":
					randomWithOptions(randomList);
					break;
				case "GUI":
					randomWithOptions(randomGuiList);
					break;
				case "GAME":
					randomWithOptions(randomGameList);
					break;
				case "RANDOM":
					randomWithOptions(randomList);					
					break;
				case "ALLRANDOM":
					randomWithOptions(allRandomList);					
					break;
				case "":
					validBlankEntry();
					break;
				default:
					elementAnalysis(entry);
				}
			}
		}
		
		String[] selectedAIs() {
			return selectedAIs;
		}
		
		// ========== Analysis Methods ==========
		//
		private void randomWithOptions(List<String> parameters) {
			int id = PMutil.getRandom(0, parameters.size());
			validEntry(parameters.get(id));
		}

		private AbstractT<String> randomWithInListLimit(String[] parameters) {
			int[] limits = validateLimits(parameters);
			List<String> subList = randomGuiList.subList(limits[0], limits[1]);
			int id = PMutil.getRandom(0, subList.size());
			return validEntry(subList.get(id));
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
		private AbstractT<String> validEntry(String AI) {
			if (isValidAI(AI)) {
				return setAI(AI);
			}
			return setNull();
		}
		
		private AbstractT<String> validBlankEntry() {
			if (maxArray > index) {
				String selectedAI = selectedAIs[index];
				if (isValidAI(selectedAI)) {
					return setBlank();
				}
				// to much of this race selected
				return setNull();
			}
			// no race selected OK for blank value
			return setBlank();
		}
		
		private AbstractT<String> setAI(String ai) {
			if (maxArray > index) {
				selectedAIs[index] = ai;
			}
			return newValue(ai);
		}

		private AbstractT<String> setBlank() {// not allowed!
			return newValue(blankReplacement);
		}

		private AbstractT<String> setNull() {
			return newValue(nullReplacement);
		}

		private boolean isValidAI(String AI) {
			if (gameCall) {
				return defaultGameList.contains(AI);
			}
			return defaultGuiList.contains(AI);
		}

		private void initOptions() {
			randomGuiList   = RaceFilter.selectedGuiAIFilter();
			randomGameList  = RaceFilter.selectedGameAIFilter();
			randomList      = RaceFilter.randomAIList();
			allRandomList   = RaceFilter.allRandomAIList();
			defaultGuiList  = RaceFilter.defaultGuiAIList();
			defaultGameList = RaceFilter.defaultGameAIList();
			selectedAIs     = new String[MAX_OPPONENTS+1];
			maxArray        = selectedAIs.length;
		}
	}
}
