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

import rotp.model.game.DynamicOptions;

public class ParamInteger extends AbstractParam<Integer> {

	private boolean loop = false;

	// ========== constructors ==========
	//
//	/**
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param defaultvalue The default value
//	 */
//	public ParamInteger(String gui, String name, Integer defaultValue) {
//		super(gui, name, defaultValue, null, null, 1, 1, 1);
//	}
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
		super(gui, name, defaultValue, minValue, maxValue, 1, 2, 5);
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
	 * @param isDuplicate  If is base option duplicate
	 */
	public ParamInteger(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc
			, Integer ctrlInc, boolean isDuplicate) {
		super(gui, name, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc);
		isDuplicate(isDuplicate);
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
	@Override public void setFromCfgValue(String newValue) {
		if (!isDuplicate())
			set(stringToInteger(newValue));
	}	
	@Override public void prev()					{ next(-baseInc()); }
	@Override public void next()					{ next(baseInc()); }
	@Override public void toggle(MouseEvent e)		{ next(getInc(e) * getDir(e)); }
	@Override public void toggle(MouseWheelEvent e)	{ next(getInc(e) * getDir(e)); }
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isDuplicate())
			set(options.getInteger(labelId(), defaultValue()));
	}
	@Override public void setOptions(DynamicOptions options) {
		options.setInteger(labelId(), get());
	}
	@Override public void copyOption(DynamicOptions src, DynamicOptions dest) {
		if (!isDuplicate())
			dest.setInteger(labelId(), src.getInteger(labelId(), defaultValue()));
	}
	// ===== Other Methods =====
	//
	public void next(MouseEvent e) { next(Math.abs(getInc(e))); }
	public void prev(MouseEvent e) { next(-Math.abs(getInc(e))); }
	private void next(int i) {
		if (i == 0) {
			setFromDefault();
			return;
		}
		Integer value = get() + i;
		if (maxValue() != null && value > maxValue()) {
			if (loop && minValue() != null) {
				set(minValue());
				return;
			} else {
				set(maxValue());
				return;
			}
		}
		else if (minValue() != null && value < minValue()) {
			if (loop && maxValue() != null) {
				set(maxValue());
				return;
			} else {
				set(minValue());
				return;
			}
		}
		set(value);
	}
}
