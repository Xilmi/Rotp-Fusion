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
	
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamInteger(String gui, String name, Integer defaultValue) {
		super(gui, name, defaultValue, null, null, true, 1, 1, 1);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param loop     what to do when reaching the limits
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue, boolean loop) {
		super(gui, name, defaultValue, minValue, maxValue, loop, 1, 1, 1);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param loop     what to do when reaching the limits
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue, boolean loop
			, Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		super(gui, name, defaultValue, minValue, maxValue, loop, baseInc, shiftInc, ctrlInc);
	}

	@Override public Integer setFromCfg(String newValue) {
		value = stringToInteger(newValue);
		return value;
	}	
	@Override public Integer next() {
		return next(baseInc);
	}
	@Override public Integer toggle(MouseEvent e) {
		return next(getInc(e) * getDir(e));
	}
	@Override public Integer toggle(MouseWheelEvent e) {
		return next(getInc(e) * getDir(e));
	}
	@Override public Integer prev() {
		return next(-baseInc);
	}
	public int next(int i) {
		if (i == 0) return setToDefault(true);
		value+=i;
		if (maxValue != null && value > maxValue) {
			if (minValue != null)
				return setAndSave(minValue);
			else
				return setAndSave(maxValue);
		}
		else if (minValue != null && value < minValue) {
			if (maxValue != null)
				return setAndSave(maxValue);
			else
				return setAndSave(minValue);
		}
		return setAndSave(value);
	}
}
