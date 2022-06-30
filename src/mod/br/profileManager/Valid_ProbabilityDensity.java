
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

import static br.profileManager.src.main.java.Validation.History.Default;

import java.util.List;

import br.profileManager.src.main.java.PMutil;
import br.profileManager.src.main.java.T_Float;
import br.profileManager.src.main.java.Validation;

/**
 * For the validation of the Probability density modifiers
 */
class Valid_ProbabilityDensity extends Validation<Float> {

	private final List<String> OPTIONS;
	private final Float DEFAULT;

	Valid_ProbabilityDensity(Float initial, List<String> options) {
		super(new T_Float(initial));
		OPTIONS = options;
		DEFAULT = initial;
		init();
	}

	private void init() {
		for (String option : OPTIONS) {
			addOption(Float.valueOf(OPTIONS.indexOf(option)), option);
		}
		setLimits(-1000f , 1000f);
		setDefaultRandomLimits(0.25f , 4f);
		setHistory(Default, new T_Float(DEFAULT));
	}
	
	/**
	 * Generate UserViewList and convert it to capitalized String
	 * @return UserView List in capitalized String
	 */
	@Override public String getOptionsRange() {
		return PMutil.capitalize(getOptionsStringList().toString());
	}
	/**
	 * @return <b>true</b> if the Validation List is not empty
	 */
	@Override protected boolean hasOptions() {
		return false; // it has but not to be used the conventional way 
	}
}