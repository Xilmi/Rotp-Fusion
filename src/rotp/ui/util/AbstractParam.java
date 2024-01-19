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

import static rotp.model.game.IGovOptions.NOT_GOVERNOR;
import static rotp.ui.util.IParam.langLabel;
import static rotp.util.Base.textSubs;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import rotp.model.game.DynamicOptions;
import rotp.model.game.GovernorOptions;
import rotp.model.game.IGameOptions;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.BaseModPanel;

public abstract class AbstractParam <T> implements IParam{
	// Ignore UCDetector public warning!
	
	private final String name; // 
	private final String gui;  // The label header
	private T value;
	private T defaultValue;
	private T minValue		= null;
	private T maxValue		= null;
	private T baseInc		= null;
	private T shiftInc		= null;
	private T ctrlInc		= null;
	private T shiftCtrlInc	= null;
	private int	isGovernor	= NOT_GOVERNOR;
	private boolean isDuplicate	= false;
	private boolean isCfgFile	= false;
	private boolean isValueInit	= true; // default values are initialized with current value.
	
	private boolean  updated = true;
	@Override public void updated(boolean val)	{ updated = val; }
	@Override public boolean updated()			{ return updated; }

	// ========== constructors ==========
	//
	/**
	 * @param gui  The label headerTechLibrary
	 * @param name The name
	 * @param defaultValue The default value
	 */
	AbstractParam(String gui, String name, T defaultValue) {
		this.gui = gui;
		this.name = name;
		this.defaultValue = defaultValue;
		value = defaultValue;
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value
	 * @param maxValue The maximum value
	 */
	private AbstractParam(String gui, String name, T defaultValue
			, T minValue, T maxValue) {
		this(gui, name, defaultValue);
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value
	 * @param maxValue The maximum value
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 */
	AbstractParam(String gui, String name, T defaultValue
			, T minValue, T maxValue
			, T baseInc, T shiftInc, T ctrlInc, T shiftCtrlInc) {
		this(gui, name, defaultValue, minValue, maxValue);
		this.baseInc	  = baseInc;
		this.shiftInc	  = shiftInc;
		this.ctrlInc	  = ctrlInc;
		this.shiftCtrlInc = shiftCtrlInc;
	}
	// ===== For duplicates to be overridden =====
	// For internal use only! Do not call from outside AbstracParam
	protected T getOption() {
		if (isDuplicate()) { // Just in case!
			System.err.println("getOption() Not set " + getCfgLabel());
		}		
		return value; // for cfg non duplicate
	}
	protected void setOption(T value) { setOptionValue(opts(), value); }
	protected abstract T getOptionValue(IGameOptions options);
	protected void setOptionValue(IGameOptions options, T value) {
		if (isDuplicate()) { // Just in case!
			System.err.println("getFromOption() not updated to getOptionValue: " + name);
		}		
	}
	public void transfert (IGameOptions opts, boolean set) {}
	// ========== Public Interfaces ==========
	//
	//	public abstract void setFromCfgValue(String val);
	//	public abstract void next();
	//	public abstract void prev();
	//	public abstract void toggle(MouseWheelEvent e);
	//	public abstract void toggle(MouseEvent e);
	@Override public void prepareToSave(IGameOptions o)	{ setOptionValue(o, get()); }
	@Override public String toString() {
		return getCfgLabel() + " = " + getCfgValue();
	}
	@Override public void updateOptionTool()	{
		if (isCfgFile)
			setOption(get());
		else
			set(getOptionValue(opts()));
	}
	@Override public void copyOption(IGameOptions src, IGameOptions dest, boolean updateTool) {
		if (src == null || dest == null)
			return;
		T val;
		if (isCfgFile && src == opts())
			val = getOption();
		else 
			val = getOptionValue(src);
		if (updateTool)
			set(val);
		if (isCfgFile && dest == opts())
			setOption(val);
		else
			setOptionValue(dest, val);
	}
	@Override public String getCfgValue()		{ return getCfgValue(value); }
	@Override public String getCfgLabel()		{ return name; }
	@Override public String getLangLabel()		{ return gui + name; }
	@Override public String getGuiDescription() { return langLabel(descriptionId()); }
	@Override public String guideValue()		{ return String.valueOf(value); }
	@Override public String getGuiDisplay()		{ return langLabel(getLangLabel(), guideValue()) + END; }
	@Override public String getGuiDisplay(int idx)	{
		String str = langLabel(getLangLabel()); // Get from label.txt
		String[] strArr = str.split(textSubs[0]);

		switch(idx) {
		case 0:
			if (strArr.length > 0)
				return strArr[0];
			else
				return "";
		case 1:
			if (strArr.length > 1)
				return guideValue() + strArr[1];
			else
				return guideValue();
		default:
			return "";
		}
	}
	@Override public String guideDefaultValue()	{ return defaultValue().toString(); }
	@Override public boolean isDefaultValue()	{ return defaultValue.equals(get()); }
	@Override public boolean isDuplicate()		{ return isDuplicate; }
	@Override public boolean isCfgFile()		{ return isCfgFile; }
//	@Override public void setFromDefault()		{ set(defaultValue()); }
	@Override public void setFromDefault(boolean excludeCfg, boolean excludeSubMenu)	{
		if (excludeCfg && isCfgFile())
			return;
		if (excludeSubMenu && isSubMenu())
			return;
		set(defaultValue());
	}
	@Override public void toggle(MouseEvent e, MouseWheelEvent w, BaseModPanel frame) {
		if (e == null)
			toggle(w);
		else
			toggle(e, frame);
	}
	@Override public String getGuiValue(int idx){ return guideValue(); } // For List
	// ========== Tools for overriders ==========
	//
	protected IGameOptions   opts()		{ return RotPUI.currentOptions(); }
	private	  DynamicOptions dynOpts()	{ return opts().dynOpts(); }
	protected T last()					{ return value; }
	// ========== Methods to be overridden ==========
	//
	public T defaultValue()				{ return defaultValue; }
	public T get()						{
		if (isCfgFile) {
			value = getOption();
		}
		else if (isDuplicate) {
			value = getOptionValue(opts());
		}
		return value;
	}	
	public T setFromIndex(int i)		{ return null; }
	public String getCfgValue(T value)	{ return String.valueOf(value); }
	// ========== Public Getters ==========
	//
	public String getLabel()	{ return langLabel(getLangLabel()); }
	protected T creationValue()	{ return isValueInit? value : defaultValue(); }
	T minValue()	{ return minValue; }	
	T maxValue()	{ return maxValue; }	
	T baseInc()		{ return baseInc; }	
	T shiftInc()	{ return shiftInc; }	
	T ctrlInc()		{ return ctrlInc; }	
	T shiftCtrlInc(){ return shiftCtrlInc; }	
	// ========== Public Setters ==========
	//
	public void toggle(boolean reverse)	{ if (reverse) prev(); else next(); }
	protected void setFromCfg(T newValue) {
		updated = true;
		value = newValue;
		updateOption(dynOpts());
	}
	public T silentSet(T newValue) { // Reserved call from governor class
		updated = true;
		value = newValue;
		setOption(newValue); // For overrider
		return value;
	}
	public T set(T newValue) {
		updated = true;
		boolean trueChange = false;
		if(value != null && newValue != null)
			trueChange = !value.equals(newValue);
		value = newValue;
		setOption(newValue); // For overrider
		if (trueChange && (isGovernor != NOT_GOVERNOR))
			GovernorOptions.callForRefresh(isGovernor);
		if (isCfgFile)
			UserPreferences.save();
		return value;
	}
	public void maxValue (T newValue)		{ maxValue = newValue;}
	public void minValue (T newValue)		{ minValue = newValue;}
	public void defaultValue(T newValue)	{ defaultValue = newValue; }
	public void isGovernor(int val)			{ isGovernor  = val ; }
	// ========== Private Methods ==========
	//
	// ========== Protected Methods ==========
	//
	protected void isValueInit(boolean is)	{ isValueInit = is ; }
	protected void isDuplicate(boolean is)	{ isDuplicate = is ; }
	protected void isCfgFile(boolean is)	{ isCfgFile   = is ; }
	protected String descriptionId()		{ return getLangLabel() + LABEL_DESCRIPTION; }
	protected T getInc(InputEvent e)		{
		if (e.isShiftDown())
			if (e.isControlDown())
				return shiftCtrlInc;
			else
				return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	protected int getDir(MouseEvent e)		{
		if (SwingUtilities.isRightMouseButton(e)) return -1;
		if (SwingUtilities.isLeftMouseButton(e)) return 1;
		return 0;
	}
	protected int getDir(MouseWheelEvent e)	{
		if (e.getWheelRotation() < 0) return 1;
		return -1;
	}
	//========== Static Methods ==========
	//
	/**
	 * Convert String to Float and manage errors
	 * @param string Source of Float
	 * @return Float value, or <b>null</b> on error
	 */
	static Float stringToFloat(String string) {
		try {
			return Float.valueOf(string.trim());
		}
		catch (NumberFormatException nfe) {
			return null; // silent error!
		}
	}
	/**
	 * Convert String to Integer and manage errors
	 * @param string Source of Integer
	 * @return Integer value, or <b>null</b> on error
	 */
	static Integer stringToInteger(String string) {
		try {
			return Integer.valueOf(string.trim());
		}
		catch (NumberFormatException nfe) {
			return null; // silent error!
		}
	}
	static String yesOrNo(boolean b) { // BR it's already used everywhere!!!
		return b ? "Yes" : "No";
	}
	static boolean yesOrNo(String s) {
		return s.equalsIgnoreCase("YES") || s.equalsIgnoreCase("TRUE");
	}
}
