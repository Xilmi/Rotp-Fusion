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

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class ParamInteger extends AbstractParam<Integer> {

	private boolean loop = false;

	// ========== constructors ==========
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 */
	public ParamInteger(String gui, String name, Integer defaultValue) {
		super(gui, name, defaultValue, null, null, 1, 1, 1);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue) {
		super(gui, name, defaultValue, minValue, maxValue, 1, 1, 1);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param loop Once an end is reached, go to the other end
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue, boolean loop) {
		super(gui, name, defaultValue, minValue, maxValue, 1, 1, 1);
		this.loop = loop;
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		super(gui, name, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc);
	}

	// ===== Overriders =====
	//
	@Override public Integer setFromCfgValue(String newValue) {
		return set(stringToInteger(newValue));
	}	
	@Override public Integer next() {
		return next(baseInc());
	}
	@Override public Integer toggle(MouseEvent e) {
		return next(getInc(e) * getDir(e));
	}
	@Override public Integer toggle(MouseWheelEvent e) {
		return next(getInc(e) * getDir(e));
	}
	@Override public Integer prev() {
		return next(-baseInc());
	}
	// ===== Other Methods =====
	//
	public int next(int i) {
		if (i == 0) return setFromDefault(true);
		Integer value = get() + i;
		if (maxValue() != null && value > maxValue()) {
			if (loop && minValue() != null)
				return set(minValue());
			else
				return set(maxValue());
		}
		else if (minValue() != null && value < minValue()) {
			if (loop && maxValue() != null)
				return set(maxValue());
			else
				return set(minValue());
		}
		return set(value);
	}
}
