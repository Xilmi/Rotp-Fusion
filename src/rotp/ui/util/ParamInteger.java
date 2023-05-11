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

import static rotp.ui.util.InterfaceParam.langLabel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.model.game.DynamicOptions;
import rotp.ui.game.BaseModPanel;

public class ParamInteger extends AbstractParam<Integer> {

	private boolean	loop		 	= false;
	private boolean specialNegative	= false;
	private boolean specialZero		= false;
	private Integer	specialValue	= null;
	private String	negativeLabel	= "";
	private String	zeroLabel		= "";
	private String	specialLabel	= "";

	// ========== constructors ==========
	//
//	/**
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param defaultvalue The default value
//	 */
//	public ParamInteger(String gui, String name, Integer defaultValue) {
//		super(gui, name, defaultValue, null, null, 1, 1, 1, 1);
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
		super(gui, name, defaultValue, minValue, maxValue, 1, 1, 1, 1);
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
		super(gui, name, defaultValue, minValue, maxValue, 1, 2, 5, 10);
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
		super(gui, name, defaultValue, minValue, maxValue,
				baseInc, shiftInc, ctrlInc, shiftInc*ctrlInc/baseInc);
	}
	public ParamInteger loop(boolean loop) {
		this.loop = loop;
		return this;
	}
	public ParamInteger specialNegative(String messageLabel) {
		if (messageLabel == null) {
			specialNegative = false;
			negativeLabel	= "";
		}
		specialNegative	= true;
		negativeLabel	= messageLabel;
		return this;
	}
	public ParamInteger specialZero(String messageLabel) {
		if (messageLabel == null) {
			specialZero = false;
			zeroLabel	= "";
		}
		specialZero	= true;
		zeroLabel	= messageLabel;
		return this;
	}
	public ParamInteger specialValue(Integer value, String messageLabel) {
		specialValue = value;
		specialLabel = (messageLabel == null)? "" : messageLabel;
		return this;
	}
	// ===== Overriders =====
	//
	@Override public String[] getModifiers() {
		if (baseInc() == shiftInc())
			return null;
		return new String[] {baseInc().toString(),
							shiftInc().toString(),
							ctrlInc().toString(),
							shiftCtrlInc().toString()};
	}
	@Override public String guideValue() {
		if (isSpecialNegative())
			return langLabel(negativeLabel);
		if (isSpecialZero())
			return langLabel(zeroLabel);
		if (isSpecial())
			return langLabel(specialLabel);
		return super.guideValue();
	}
	@Override public void setFromCfgValue(String newValue) {
		if (!isDuplicate())
			set(stringToInteger(newValue));
	}	
	@Override public void prev() { next(-baseInc()); }
	@Override public void next() { next(baseInc()); }
	@Override public void toggle(MouseEvent e, BaseModPanel frame)	{ next(getInc(e) * getDir(e)); }
	@Override public void toggle(MouseWheelEvent e)	{ next(getInc(e) * getDir(e)); }
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
			set(options.getInteger(getLangLabel(), creationValue()));
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
		options.setInteger(getLangLabel(), get());
	}
	@Override public void copyOption(DynamicOptions src, DynamicOptions dest) {
		if (!isDuplicate() && src != null && dest != null)
			dest.setInteger(getLangLabel(), src.getInteger(getLangLabel(), creationValue()));
	}
	// ===== Other Public Methods =====
	//
	public void next(MouseEvent e)	{ next(Math.abs(getInc(e))); }
	public void prev(MouseEvent e)	{ next(-Math.abs(getInc(e))); }
	public boolean isSpecial()		{ return (specialValue != null) && (get() == specialValue); }
	public boolean isSpecialZero()	{ return specialZero && (get() == 0); }
	public boolean isSpecialNegative() { return specialNegative && (get() < 0); }
	// ===== Other Private Methods =====
	//
	private void next(int i) {
		if (i == 0) {
			setFromDefault();
			return;
		}
		Long value = (long) get() + i;
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
		set(value.intValue());
	}
}
