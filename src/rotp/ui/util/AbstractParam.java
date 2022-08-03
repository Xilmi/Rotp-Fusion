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

import static rotp.ui.UserPreferences.save;
import static rotp.util.Base.textSubs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;

import rotp.util.LabelManager;

public abstract class AbstractParam <T> {

	private static final String LABEL_DESCRIPTION = "_DESC";
	private static final String END = "   ";
	private final String name;
	private final String gui;
	protected T value;
	protected T defaultValue;
	protected T minValue = null;
	protected T maxValue = null;
	protected T baseInc = null;
	private T shiftInc = null;
	private T ctrlInc = null;
	protected boolean loop = true;
	// ========== constructors ==========
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public AbstractParam(String gui, String name, T defaultValue) {
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
	 * @param loop     what to do when reaching the limits
	 */
	public AbstractParam(String gui, String name, T defaultValue
			, T minValue, T maxValue, boolean loop) {
		this(gui, name, defaultValue);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.loop     = loop;
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
	 * @param loop     what to do when reaching the limits
	 */
	public AbstractParam(String gui, String name, T defaultValue
			, T minValue, T maxValue, boolean loop 
			, T baseInc, T shiftInc, T ctrlInc) {
		this(gui, name, defaultValue, minValue, maxValue, loop);
		this.baseInc  = baseInc;
		this.shiftInc = shiftInc;
		this.ctrlInc  = ctrlInc;
	}

	// ========== Public Abstract ==========
	//
	public abstract T setFromCfg(String val);
	public abstract T next();
	public abstract T prev();
	public abstract T toggle(MouseWheelEvent e);
	public abstract T toggle(MouseEvent e);

	@Override public String toString() {
		return getCfgLabel() + " = " + getCfgValue();
	}
	// ========== Public Getters ==========
	//
	public String getCfgValue() {
		return String.valueOf(value);
	}
	public String getGuiValue() {
		return String.valueOf(value);
	}
	public String getCfgLabel() {
		return name;
	}
	public String getGuiDescription() {
		return text(descriptionId());
	}
	public String getLabel() {
		return text(labelId());
	}
	public String getGuiDisplay() {
		return text(labelId(), getGuiValue()) + END; 
	}
	public T defaultValue() {
		return defaultValue;
	}
	public T get() {
		return value;
	}	
	public T toggle() {
		return next();
	}
	public T setToDefault(boolean save) {
		value = defaultValue;
		if (save) 
			save();
		return value;
	}
	public T set(T newValue) {
		value = newValue;
		return value;
	}	
	public T setAndSave(T newValue) {
		set(newValue); save();
		return value;
	}	
	public T toggle(MouseEvent e, MouseWheelEvent w) {
		if (e == null)
			return toggle(w);
		else
			return toggle(e);
	}
	// ========== Private Methods ==========
	//
	private String labelId() {
		return gui + name;
	}
	private String descriptionId() {
		return labelId() + LABEL_DESCRIPTION;
	}
	// ========== Protected Methods ==========
	//
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
	public static Float stringToFloat(String string) {
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
	public static Integer stringToInteger(String string) {
		try {
			return Integer.valueOf(string.trim());
		}
		catch (NumberFormatException nfe) {
			return null; // silent error!
		}
	}
	public static String yesOrNo(boolean b) { // BR it's already used everywhere!!!
		return b ? "YES" : "NO";
	}
	public static boolean yesOrNo(String s) {
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
	protected static String text(String key, int... vals) {
		String str = text(key);
		for (int i=0;i<vals.length;i++)
			str = str.replace(textSubs[i],String.valueOf(vals[i]));
		return str;
	}
	protected static String text(String key, String val1, int val2) {
		String str = text(key);
		str = str.replace(textSubs[0], val1);
		return str.replace(textSubs[1], String.valueOf(val2));
	}
	protected static String text(String key, String val1, String val2, int val3) {
		String str = text(key);
		str = str.replace(textSubs[0], val1);
		str = str.replace(textSubs[1], val2);
		return str.replace(textSubs[2], String.valueOf(val3));
	}
}
