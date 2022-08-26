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

import java.util.LinkedList;

public class ParamBullets extends ParamList {
	
	private final LinkedList<Float> costList = new LinkedList<>();
	private final LinkedList<Integer> indexList = new LinkedList<>();

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamBullets(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param allowSave To allow the parameter to be saved in Remnants.cfg
	 */
	public ParamBullets(String gui, String name, String defaultValue, boolean allowSave) {
		super(gui, name, defaultValue);
		allowSave(allowSave);
	}
	// ===== Overriders =====
	//
	@Override public ParamBullets allowSave(boolean allow) {
		super.allowSave(allow);
		return this;
	}
	@Override public int getBoxSize() {
		return costList.size();
	}
	@Override public boolean isBullet() {
		return true;
	}
	@Override public float getCost() {
		return costList.get(getIndex());
	}
	@Override public float getCost(int idx) {
		return costList.get(idx);
	}
//	@Override public String getGuiSettingLabelValueCostStr() {
//		return getLabel() + ": " + getCostString();
//	}

	// ===== Specific Public Methods =====
	//
	/**
	 * Add a new Option with its Label
	 * @param option
	 * @param label
	 * @param cost The cost of this option
	 * @param index The index of the in game option
	 * @return this for chaining purpose
	 */
	public ParamBullets put(String option, String label, Float cost, Integer index) {
		costList.add(cost);
		indexList.add(index);
		super.put(option, label);
		return this;
	}
	/**
	 * Add a new Option with its Label
	 * @param option
	 * @param label
	 * @param cost The cost of this option
	 * @param index The index of the in game option
	 * @return this for chaining purpose
	 */
	public ParamBullets put(String option, String label, Integer cost, Integer index) {
		costList.add(cost.floatValue());
		indexList.add(index);
		super.put(option, label);
		return this;
	}
}
