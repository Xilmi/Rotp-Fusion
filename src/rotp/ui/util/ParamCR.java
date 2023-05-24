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
import rotp.model.game.DynamicOptions;

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
	@Override public Serializable get() {
		if (super.get() == null)
			return CustomRaceDefinitions.getDefaultOptions();
		return super.get();
	}
	@Override public void setOptionsTools(DynamicOptions options) {
		System.out.println("ParamCR.setOptionsTools");
		if (!isDuplicate() && options != null)
			set((Serializable) options.getObject(getLangLabel(), creationValue()));
	}
	@Override public void setOptions(DynamicOptions options) {
		System.out.println("ParamCR.setOptions");
		if (!isDuplicate() && options != null)
			options.setObject(getLangLabel(), get());
	}
	// ===== Other Methods =====
	//
	public Race getRace() {
		return getCustomRace().getRace();
	}
	public CustomRaceDefinitions getCustomRace() {
		return new CustomRaceDefinitions((DynOptions) get());
	}
}
