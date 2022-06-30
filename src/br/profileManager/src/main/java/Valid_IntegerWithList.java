
/*
 * Licensed under the GNU General License, Version 3 (the "License");
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

package br.profileManager.src.main.java;

import static br.profileManager.src.main.java.Validation.History.Default;

import java.util.List;

/**
 * For the validation of the configurations Action
 */
public class Valid_IntegerWithList extends Validation<Integer> {

	/**
	 * @param initial Initial Value
	 * @param options Option list
	 */
	public Valid_IntegerWithList(int initial, List<String> options) {
		super(new T_Integer(initial));
		init(options);
	}

	private void initCriteria() {
		getCriteria().isNullAllowed(false);
	}
	
	private void init(List<String> options) {
		initCriteria();
		options = PMutil.suggestedUserViewFromCodeView(options);
		for (String option : options) {
			addOption(options.indexOf(option), option);
		}
		setLimits(0 , options.size());
		setDefaultRandomLimits(0 , options.size());
		setHistory(Default, options);
	}
			
	/**
	 * Generate UserViewList and convert it to capitalized String
	 * @return UserView List in capitalized String
	 */
	@Override public String getOptionsRange() {
		return PMutil.capitalize(getOptionsStringList().toString());
	}
}
