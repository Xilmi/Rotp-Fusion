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

import static rotp.util.Base.textSubs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import rotp.model.game.DynamicOptions;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.game.BaseModPanel;
import rotp.util.LabelManager;

public abstract class AbstractParam <T> implements InterfaceParam{
	// Ignore UCDetector public warning!
	
	private final String name;
	private final String gui;
    private BasePanel panel;
	private T value;
	private T defaultValue;
	private T minValue	= null;
	private T maxValue	= null;
	private T baseInc	= null;
	private T shiftInc	= null;
	private T ctrlInc	= null;
	private boolean isDuplicate = false;
	private MOO1GameOptions duplicateOptions = null;

	// ========== constructors ==========
	//
	/**
	 * @param gui  The label header
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
			, T baseInc, T shiftInc, T ctrlInc) {
		this(gui, name, defaultValue, minValue, maxValue);
		this.baseInc  = baseInc;
		this.shiftInc = shiftInc;
		this.ctrlInc  = ctrlInc;
	}
	// ===== For duplicates to be overridden =====
	public void reInit() {}
	public void setOption(T option) {}
	public T getFromOption() { return null; }

	// ========== Public Interfaces ==========
	//
	//	public abstract void setFromCfgValue(String val);
	//	public abstract void next();
	//	public abstract void prev();
	//	public abstract void toggle(MouseWheelEvent e);
	//	public abstract void toggle(MouseEvent e);

	@Override public String toString() {
		return getCfgLabel() + " = " + getCfgValue();
	}
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
			setFromCfgValue(options.getString(labelId(), getCfgValue(defaultValue())));
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
			options.setString(labelId(), getCfgValue());
	}
	@Override public void copyOption(DynamicOptions src, DynamicOptions dest) {
		if (!isDuplicate() && src != null && dest != null)
			dest.setString(labelId(), src.getString(labelId(), getCfgValue(defaultValue())));
	}
	@Override public String getCfgValue() { return getCfgValue(value); }
	@Override public String getCfgLabel() { return name; }
	@Override public String getGuiDescription() { return text(descriptionId()); }
	@Override public String getGuiDisplay()	{ return text(labelId(), getGuiValue()) + END; }
	@Override public String getGuiDisplay(int idx)	{
		String str = text(labelId()); // Get from label.txt
		String[] strArr = str.split(textSubs[0]);

		switch(idx) {
		case 0:
			if (strArr.length > 0)
				return strArr[0];
			else
				return "";
		case 1:
			if (strArr.length > 1)
				return getGuiValue() + strArr[1];
			else
				return getGuiValue();
		default:
			return "";
		}
	}
	@Override public boolean isDefaultValue()	{ 
//		System.out.println(
//				getGuiDisplay(0) + "	" +
//				defaultValue.equals(get()) + "	" +
//				defaultValue + " =?	" + get()
//				);
		return defaultValue.equals(get());
	}
	@Override public boolean isDuplicate()	{ return isDuplicate; }
	@Override public void setFromDefault()	{ set(defaultValue()); }
	@Override public void toggle(MouseEvent e, MouseWheelEvent w) {
		if (e == null)
			toggle(w);
		else
			toggle(e);
	}
	@Override public void setPanel(BaseModPanel p) { panel = p; }
	// ========== Methods to be overridden ==========
	//
	T value(T value) 		{ return set(value); }
	public int getIndex()	{ return 0; }
	public T defaultValue()	{ return defaultValue; }
	public T get()			{
		if (isDuplicate()) {
			value = getFromOption();
		}
		return value;
	}	
	public T setFromIndex(int i)		  { return null; }
	protected String getCfgValue(T value) { return String.valueOf(value); }
	public	  String getGuiValue()		  { return String.valueOf(value); }
	public	  String getGuiValue(int idx) { return getGuiValue(); } // For List
	// ========== Public Getters ==========
	//
	public String getLabel(){ return text(labelId()); }
	public T minValue()	{ return minValue; }	
	public T maxValue()	{ return maxValue; }	
	T baseInc()			{ return baseInc; }	
	// ========== Public Setters ==========
	//
	public T set(T newValue) {
		value = newValue;
		setOption(newValue);
		return value;
	}
	public void duplicateOptions (MOO1GameOptions options) { duplicateOptions = options;}
	public void maxValue (T newValue)	 { maxValue = newValue;}
	public void minValue (T newValue)	 { minValue = newValue;}
	public void defaultValue(T newValue) { defaultValue = newValue; }
	// ========== Private Methods ==========
	//
	// ========== Protected Methods ==========
	//
	protected void isDuplicate(boolean newValue) { isDuplicate = newValue ; }
	protected String descriptionId() { return labelId() + LABEL_DESCRIPTION; }
	protected String labelId()		 { return gui + name; }
	protected BasePanel getPanel()	 { return panel; }
	protected boolean hasPanel()	 { return panel != null; }
	protected T getInc(MouseEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	protected T getInc(MouseWheelEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	protected int getDir(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) return -1;
		if (SwingUtilities.isLeftMouseButton(e)) return 1;
		return 0;
	}
	protected int getDir(MouseWheelEvent e) {
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
		return s.equalsIgnoreCase("YES");
	}
	protected static String text(String key) {
		return LabelManager.current().label(key);
	}
	protected static String text(String key, String... vals) {
		String str = text(key);
		for (int i=0;i<vals.length;i++)
			str = str.replace(textSubs[i], vals[i]);
		return str;
	}
}
