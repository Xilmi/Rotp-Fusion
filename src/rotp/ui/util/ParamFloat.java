/*
 * Copyright 2015-2020 Ray Fowler
 *
 * Licensed under the GNU General Public License, Version 3 (the "License");
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

package rotp.ui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

public class ParamFloat extends AbstractParam<Float> {
	
	private String guiFormat = "%";
	private String cfgFormat = "0.0##";
	private boolean loop = false;
	
	// ========== Constructors ==========
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamFloat(String gui, String name, Float defaultValue) {
		super(gui, name, defaultValue);
		setLimits(null, null);
		setIncrements(1.0f, 1.0f, 1.0f, 1.0f);
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
	@Override public ParamFloat setIncrements(Float baseInc, Float shiftInc,
			Float ctrlInc, Float shiftCtrlInc) {
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
	public ParamFloat setIncrements(Float baseInc, Float shiftInc, Float ctrlInc) {
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
	@Override public ParamFloat setLimits(Float min, Float max) {
		super.setLimits(min, max);
		return this;
	}
	public ParamFloat cfgFormat(String format) { cfgFormat = format; return this; }
	public ParamFloat guiFormat(String format) { guiFormat = format; return this; }
	// ========== Overriders ==========
	//
	@Override public ParamFloat isValueInit(boolean is) { super.isValueInit(is)  ; return this; }
	@Override public ParamFloat isDuplicate(boolean is) { super.isDuplicate(is)  ; return this; }
	@Override public ParamFloat isCfgFile(boolean is)	{ super.isCfgFile(is)    ; return this; }
	@Override public ParamFloat formerName(String link)	{ super.formerName(link) ; return this; }
	@Override public String guideDefaultValue()	{ return getString(defaultValue()); }
	@Override public String[] getModifiers()	{
		if (baseInc().equals(shiftInc()))
			return null;
		return new String[] {getString(baseInc()),
							getString(shiftInc()),
							getString(ctrlInc()),
							getString(shiftCtrlInc())};
	}
	@Override public String getCfgValue(Float value) {
		if (isCfgPercent()) {
			return String.format("%d", Math.round(value * 100f));
		}
		if (isCfgPerThousand()) {
			return new DecimalFormat("0.0")
						.format(Math.round(value  * 1000f) / 10f);
		}
		return new DecimalFormat(cfgFormat).format(value);
	}
	@Override public String guideValue() {
		if (isGuiPercent()) {
			return String.format("%d", Math.round(get() * 100f));
		}
		if (isGuiPerThousand()) {
			return new DecimalFormat("0.0")
						.format(Math.round(get() * 1000f) / 10f);
		}
		return new DecimalFormat(guiFormat).format(get());
	}
	@Override public void setFromCfgValue(String newValue) {
		if (isCfgPercent()) {
			Integer val = stringToInteger(newValue.replace("%", ""));
			if (val == null) 
				setFromCfg(stringToFloat(newValue));
			else
				setFromCfg(val/100f);
		} else
			setFromCfg(stringToFloat(newValue));
	}	
	@Override public boolean next() { return next(baseInc()); }
	@Override public boolean prev() { return next(-baseInc()); }
	@Override public boolean toggle(MouseEvent e, BaseModPanel frame)		{ return next(getInc(e) * getDir(e)); }
	@Override public boolean toggle(MouseWheelEvent e) { return next(getInc(e) * getDir(e)); }
	@Override protected Float getOptionValue(IGameOptions options) {
		Float value = options.dynOpts().getFloat(getLangLabel());
		if (value == null)
			if (formerName() == null)
				value = creationValue();
			else
				value = options.dynOpts().getFloat(formerName(), creationValue());
		return value;
	}
	@Override protected void setOptionValue(IGameOptions options, Float value) {
		options.dynOpts().setFloat(getLangLabel(), value);
	}
	// ========== Other Methods ==========
	//
	public boolean next(MouseEvent e) { return next(Math.abs(getInc(e))); }
	public boolean prev(MouseEvent e) { return next(-Math.abs(getInc(e))); }
	private String getString(float value) {
		if (isGuiPercent()) {
			return String.format("%d", Math.round(value * 100f)) + "%";
		}
		if (isGuiPerThousand()) {
			return new DecimalFormat("0.0")
						.format(Math.round(value * 1000f) / 10f) + "%";
		}
		return new DecimalFormat(guiFormat).format(value);
	}
	private boolean next(float i) {
		if (i == 0) {
			setFromDefault(false, true);
			return false;
		}
		Float value = get() + i;
		if (maxValue() != null && value > maxValue()) {
			if (loop && minValue() != null) {
				set(minValue());
				return false;
			} else {
				set(maxValue());
				return false;
			}
		} else if (minValue() != null && value < minValue()) {
			if (loop && maxValue() != null) {
				set(maxValue());
				return false;
			} else {
				set(minValue());
				return false;
			}
		}
		set(value);
		return false;
	}
	private boolean isGuiPercent() { return guiFormat.equals("%"); }
	private boolean isCfgPercent() { return cfgFormat.equals("%"); }
	private boolean isGuiPerThousand() { return guiFormat.equals("‰"); }
	private boolean isCfgPerThousand() { return cfgFormat.equals("‰"); }
}
