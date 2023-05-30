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

package rotp.ui.util;

import java.io.Serializable;

import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.empires.Race;
import rotp.model.game.DynOptions;

public class ParamCR extends ParamObject {

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 */
	public ParamCR(String gui, String name) {
		super(gui, name, null);
	}
	// ===== Overriders =====
	//
	@Override public Serializable defaultValue() {
		return CustomRaceDefinitions.getDefaultOptions();
	}
	// ===== Other Methods =====
	//
	public Race getRace() {
		Race r = getCustomRace().getRace();
		return r;
	}
	private CustomRaceDefinitions getCustomRace() {
		return new CustomRaceDefinitions((DynOptions) get());
	}
}
