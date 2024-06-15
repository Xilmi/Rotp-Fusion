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

import static rotp.ui.util.IParam.langLabel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedHashMap;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

public class ParamInteger extends AbstractParam<Integer> {

	private boolean	loop		 	= false;
	private boolean specialNegative	= false;
	private boolean specialZero		= false;
	private String	negativeLabel	= "";
	private String	zeroLabel		= "";
	private LinkedHashMap<Integer, String> specialMap = new LinkedHashMap<>();

	// ========== constructors ==========
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 */
	public ParamInteger(String gui, String name, Integer defaultValue) {
		super(gui, name, defaultValue);
		setLimits(null, null);
		setIncrements(1, 1, 1, 1);
	}
	/**
	 * To set the increment based on key modifiers
	 * 
	 * @param baseInc  The base incrementThe lower limit, 
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param shiftCtrlInc  The increment when Ctrl and Shift are hold
	 * @return this for chaining purpose
	 */
	@Override public ParamInteger setIncrements(Integer baseInc, Integer shiftInc,
											Integer ctrlInc, Integer shiftCtrlInc) {
		super.setIncrements(baseInc, shiftInc, ctrlInc, shiftCtrlInc);
		return this;
	}
	/**
	 * To set the increment based on key modifiers
	 * 
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @return this for chaining purpose
	 */
	public ParamInteger setIncrements(Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		super.setIncrements(baseInc, shiftInc, ctrlInc, shiftInc*ctrlInc/baseInc);
		return this;
	}
	/**
	 * To set the parameter limits
	 * 
	 * @param min The lower limit, no limit if the value is null
	 * @param max The upper limit, no limit if the value is null
	 * @return this for chaining purpose
	 */
	@Override public ParamInteger setLimits(Integer min, Integer max) {
		super.setLimits(min, max);
		return this;
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
		specialMap.put(value, messageLabel);
		return this;
	}
	// ===== Overriders =====
	//
	@Override public ParamInteger isValueInit(boolean is) { super.isValueInit(is)  ; return this; }
	@Override public ParamInteger isDuplicate(boolean is) { super.isDuplicate(is)  ; return this; }
	@Override public ParamInteger isCfgFile(boolean is)	  { super.isCfgFile(is)    ; return this; }
	@Override public ParamInteger formerName(String link) { super.formerName(link) ; return this; }

	@Override public String[] getModifiers()	{
		if (baseInc().equals(shiftInc()))
			return null;
		return new String[] {baseInc().toString(),
							shiftInc().toString(),
							ctrlInc().toString(),
							shiftCtrlInc().toString()};
	}
	@Override public String guideValue()		{ return guideValue(get()); }
	@Override public String guideDefaultValue()	{ return guideValue(defaultValue()); }
	@Override public void setFromCfgValue(String newValue) {
		if (!isDuplicate())
			setFromCfg(stringToInteger(newValue));
	}	
	@Override public boolean prev() { return next(-baseInc()); }
	@Override public boolean next() { return next(baseInc()); }
	@Override public boolean toggle(MouseEvent e, BaseModPanel frame)	{ return next(getInc(e) * getDir(e)); }
	@Override public boolean toggle(MouseWheelEvent e)	{ return next(getInc(e) * getDir(e)); }
	@Override protected Integer getOptionValue(IGameOptions options) {
		Integer value = options.dynOpts().getInteger(getLangLabel());
		if (value == null)
			if (formerName() == null)
				value = creationValue();
			else
				value = options.dynOpts().getInteger(formerName(), creationValue());
		return value;
	}
	@Override protected void setOptionValue(IGameOptions options, Integer value) {
		options.dynOpts().setInteger(getLangLabel(), value);
	}
	// ===== Other Public Methods =====
	//
	public void next(MouseEvent e, float f)	{
		int inc = getInc(e);
		if (inc > 0)
			set((int) Math.floor(f));
		else
			set((int) Math.ceil(f));
		next(inc);
	}
	public boolean next(MouseEvent e)		{ return next(Math.abs(getInc(e))); }
	public boolean prev(MouseEvent e)		{ return next(-Math.abs(getInc(e))); }
	public boolean isSpecial()			{ return specialMap.containsKey(get()); }
	public boolean isSpecialZero()		{ return specialZero && (get().equals(0)); }
	public boolean isSpecialNegative()	{ return specialNegative && (get() < 0); }
	public String  negativeLabel()		{ return negativeLabel; }
	// ===== Other Private Methods =====
	//
	private boolean isSpecial(Integer val)			{ return specialMap.containsKey(val); }
	private boolean isSpecialZero(Integer val)		{ return specialZero && (val.equals(0)); }
	private boolean isSpecialNegative(Integer val)	{ return specialNegative && (val < 0); }
	private String guideValue(Integer val) {
		if (isSpecialNegative(val))
			return langLabel(negativeLabel);
		if (isSpecialZero(val))
			return langLabel(zeroLabel);
		if (isSpecial(val))
			return langLabel(specialMap.get(get()));
		return String.valueOf(val);
	}
	private boolean next(int i) {
		if (i == 0) {
			setFromDefault(false, true);
			return false;
		}
		Long value = (long) get() + i;
		if (maxValue() != null && value > maxValue()) {
			if (loop && minValue() != null) {
				set(minValue());
				return false;
			} else {
				set(maxValue());
				return false;
			}
		}
		else if (minValue() != null && value < minValue()) {
			if (loop && maxValue() != null) {
				set(maxValue());
				return false;
			} else {
				set(minValue());
				return false;
			}
		}
		set(value.intValue());
		return false;
	}
}
