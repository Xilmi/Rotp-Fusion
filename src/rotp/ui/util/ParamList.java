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

import static rotp.ui.UserPreferences.minListSizePopUp;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.RotPUI;

public class ParamList extends AbstractParam<String> {

	private final IndexableMap valueLabelMap;
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultCfgLabel The default CfgLabel
	 */
	public ParamList(String gui, String name, String defaultCfgLabel) {
		super(gui, name, defaultCfgLabel);
		valueLabelMap = new IndexableMap();
	}
//	/**
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param defaultCfgLabel The default CfgLabel
//	 * @param list keys for map table
//	 * @param header The label Header
//	 */
//	public ParamList(String gui, String name, String defaultCfgLabel, LinkedList<String> list, String mid) {
//		super(gui, name, defaultCfgLabel);
//		valueLabelMap = new IndexableMap();
//		for (String element : list)
//			put(element, mid + element.toUpperCase());
//	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultCfgLabel The default CfgLabel
	 * @param optionLabelMap  existing IndexableMap
	 */
	public ParamList(String gui, String name, String defaultCfgLabel, IndexableMap optionLabelMap) {
		super(gui, name, defaultCfgLabel);
		this.valueLabelMap = optionLabelMap;
	}
//	/**
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param list keys for map table
//	 * @param defaultIndex index to the default value
//	 * @param isDuplicate if true the option already exist 
//	 */
//	public ParamList(String gui, String name, List<String> list, int defaultIndex, boolean isDuplicate) {
//		super(gui, name, list.get(defaultIndex));
//		isDuplicate(isDuplicate);
//		valueLabelMap = new IndexableMap();
//		for (String element : list)
//			put(element, element); // Temporary; needs to be further initialized
//	}
	/**
	 * Initializer for Duplicate
	 * @param gui  The label header
	 * @param name The name
	 * @param list keys for map table
	 * @param defaultIndex index to the default value
	 */
	public ParamList(String gui, String name, List<String> list, int defaultIndex) {
		super(gui, name, list.get(defaultIndex));
		isDuplicate(true);
		valueLabelMap = new IndexableMap();
		for (String element : list)
			put(element, element); // Temporary; needs to be further initialized
	}
//	/**
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param list keys for map table
//	 * @param defaultIndex index to the default value
//	 * @param isDuplicate if true the option already exist 
//	 */
//	public ParamList(String gui, String name, List<String> list, String defaultValue, boolean isDuplicate) {
//		super(gui, name, defaultValue);
//		isDuplicate(isDuplicate);
//		valueLabelMap = new IndexableMap();
//		for (String element : list)
//			put(element, element); // Temporary; needs to be further initialized
//	}
	/**
	 * Initializer for Duplicate
	 * @param gui  The label header
	 * @param name The name
	 * @param list keys for map table
	 * @param defaultIndex index to the default value
	 */
	public ParamList(String gui, String name, List<String> list, String defaultValue) {
		super(gui, name, defaultValue);
		isDuplicate(true);
		valueLabelMap = new IndexableMap();
		for (String element : list)
			put(element, element); // Temporary; needs to be further initialized
	}
	/**
	 * Initializer for Duplicate Dynamic (shape options)
	 * @param gui  The label header
	 * @param name The name
	 */
	public ParamList(String gui, String name) {
		super(gui, name, "");
		isDuplicate(true);
		valueLabelMap = new IndexableMap();
	}
	
	// ===== Initializers =====
	//
	public void reInit(List<String> list) {
		valueLabelMap.clear();
		for (String element : list)
			put(element, text(element)); // "text" should now be available
	}
	// ===== Overriders =====
	//
	@Override public void initGuiTexts() {
		int idx = getIndex(defaultValue());
		valueLabelMap.initGuiTexts();
		if (idx >= 0)
			defaultValue(valueLabelMap.cfgValueList.get(idx));
	}
	@Override protected String getCfgValue(String value) {
		return validateValue(value);
	}
	@Override public String getGuiValue() {
		return text(valueLabelMap.getLangLabelFromValue(validateValue(get())));
	}
	@Override public void next() {
		set(valueLabelMap.getNextLangLabelIgnoreCase(get()));
	}
	@Override public void prev() {
		set(valueLabelMap.getPrevValueIgnoreCase(get())); 
	}
	@Override public void toggle(MouseWheelEvent e) {
		if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	@Override public void toggle(MouseEvent e, Component frame) {
		if (getDir(e) == 0)
			setFromDefault();
		else if (frame != null && 
				(e.isControlDown() || listSize() >= minListSizePopUp.get()))
			setFromList(frame);
		else if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	@Override public void setFromCfgValue(String newCfgValue) {
		super.set(validateValue(newCfgValue));
	}
	@Override public int getIndex(){
		if (isDuplicate())
			return valueLabelMap.getLangLabelIndexIgnoreCase(get());
		else
			return valueLabelMap.getValueIndexIgnoreCase(get());
	}
	@Override public String setFromIndex(int idx) {
		return super.set(value(valueLabelMap.getCfgValue(idx)));
	}
	@Override public String getGuiValue(int idx) {
		return text(valueLabelMap.getLangLabel(idx));
	}
	// ===== Other Protected Methods =====
	//
	protected int getIndex(String value) {
		if (isDuplicate())
			return valueLabelMap.getLangLabelIndexIgnoreCase(value);
		else
			return valueLabelMap.getValueIndexIgnoreCase(value);
	}
	protected String getLangLabelFromValue(String newValue) {
		String newLangLabel = valueLabelMap.getLangLabelFromValue(newValue);
		return newLangLabel;
	}
//	protected String getValueFromLangLabel(String langLabel) {
//		if (isDuplicate())
//			return langLabel;
//		else
//			return valueLabelMap.getValueFromLangLabel(langLabel);	
//	}
	// ===== Other Public Methods =====
	//
	public LinkedList<String> getOptions() {
		LinkedList<String> list = new LinkedList<String>();
		if (isDuplicate()) // Values and labels are swap because values may be redundant
			list.addAll(valueLabelMap.langLabelList);
		else
			list.addAll(valueLabelMap.cfgValueList);
		return list;
	}
	public IndexableMap getOptionLabelMap() { return valueLabelMap; }
	/**
	 * Add a new Option with its Label
	 * @param option
	 * @param label
	 * @return this for chaining purpose
	 */
	public ParamList put(String option, String label) {
		valueLabelMap.put(option, label);
		return this;
	}
	// ===== Private Methods =====
	//
	/**
	 * Check if the entry is valid and return a valid value
	 * @param key the entry to check
	 * @return a valid value, preferably the value to test
	 */
	private String validateValue(String key) {
		if (valueLabelMap.valuesContainsIgnoreCase(key))
			return key;
		if (valueLabelMap.valuesContainsIgnoreCase(defaultValue()))
			return defaultValue();
		return valueLabelMap.getCfgValue(0);
	}
	private int listSize() { return valueLabelMap.listSize(); }
	private String currentOption() {
		int index = Math.max(0, getIndex());
		return valueLabelMap.guiTextList.get(index);
	}
	private void setFromList(Component frame) {
		String message	= "<html>" + getGuiDescription() + "</html>";
		String title	= text(labelId(), "");
		String input;
		// System.out.println("getIndex() = " + getIndex());
		// System.out.println("currentOption() = " + currentOption());
		initGuiTexts();
		String[] list= valueLabelMap.guiTextList.toArray(new String[listSize()]);
		input  = (String) ListDialog.showDialog(
				frame,	frame,			// Frame & Location component
				message, title,			// Message & Title
				list, currentOption(),	// List & Initial choice
				null, true,				// long Dialogue & isVertical
				RotPUI.scaledSize(360), RotPUI.scaledSize(300),	// size
				null, null,	// Font, Preview
				valueLabelMap.cfgValueList);	// Alternate return
		// System.out.println("input = " + input);
		// if (input != null && getIndex(input) >= 0)
		if (input != null && valueLabelMap.getValueIndexIgnoreCase(input) >= 0)
			set(input);
		// System.out.println("getIndex() = " + getIndex());
	}
	//========== Nested class ==========
	//
	public static class IndexableMap{
		
		private final LinkedList<String>  cfgValueList	= new LinkedList<>(); // also key list
		private final LinkedList<String>  langLabelList	= new LinkedList<>();
		private final LinkedList<String>  guiTextList	= new LinkedList<>();

		
		// ========== Constructors and Initializers ==========
		//
		public IndexableMap() {}
		
//		public IndexableMap(IndexableMap map) {
//			cfgValueList.addAll(map.cfgValueList);
//			langLabelList.addAll(map.langLabelList);
//		}
		
		private void clear () {
			cfgValueList.clear();
			langLabelList.clear();
		}
		// ========== Setters ==========
		//
		public void put(String option, String label) {
			cfgValueList.add(option);
			langLabelList.add(label);
		}
		void initGuiTexts() {
			guiTextList.clear();
			for (String label : langLabelList)
				guiTextList.add(text(label));
		}
		// ========== Getters ==========
		//
		private int listSize() { return cfgValueList.size(); }
		private String getCfgValue(int idx) {
			return cfgValueList.get(idx);
		}
		private String getLangLabel(int idx) {
			return langLabelList.get(idx);
		}
		private String getLangLabelFromValue(String value) {
			int index = getValueIndexIgnoreCase(value);
			return langLabelList.get(index);
		}
//		/**
//		 * get the value from the langLabel
//		 * @param langLabel The langLabel to search
//		 * @return the corresponding value
//		 */
//		private String getValueFromLangLabel(String langLabel) {
//			int index = getLangLabelIndexIgnoreCase(langLabel);
//			return cfgValueList.get(index);
//		}
		/**
		 * search for value regardless of the case and return the previous key
		 * @param value The value to search
		 * @return the previous value, looping at the beginning, the last if string is not found
		 */
		private String getPrevValueIgnoreCase(String value) {
			int index = getValueIndexIgnoreCase(value)-1;
			if (index < 0)
				return cfgValueList.get(cfgValueList.size()-1);
			return cfgValueList.get(index);
		}
		/**
		 * search for value regardless of the case and return the next key
		 * @param value The value to search
		 * @return the next value, looping at the end, the first if string is not found
		 */
		private String getNextLangLabelIgnoreCase(String value) {
			int index = getValueIndexIgnoreCase(value) + 1;
			if (index >= cfgValueList.size())
				return cfgValueList.get(0);
			return cfgValueList.get(index);
		}
		/**
		 * Test if value is part of cfgValue list regardless of the case
		 * @param value The key to search for
		 * @return true if value is found
		 */
		private boolean valuesContainsIgnoreCase(String value) {
			return getValueIndexIgnoreCase(value) != -1;
		}
//		/**
//		 * search for the value position in the guiText list
//		 * @param value The value to search regardless of the case
//		 * @return the value index, -1 if none
//		 */
//		private int getGuiTextIndexIgnoreCase(String value) {
//			int index = 0;
//			for (String entry : guiTextList) {
//				if (entry.equalsIgnoreCase(value))
//					return index;
//				index++;
//			}
//			return -1;
//		}
		/**
		 * search for the value position in the cfgValue list
		 * @param value The value to search regardless of the case
		 * @return the value index, -1 if none
		 */
		private int getValueIndexIgnoreCase(String value) {
			int index = 0;
			for (String entry : cfgValueList) {
				if (entry.equalsIgnoreCase(value))
					return index;
				index++;
			}
			return -1;
		}
		/**
		 * search for the langLabel position in the langLabel list
		 * @param langLabel The label to search regardless of the case
		 * @return the key index, -1 if none
		 */
		private int getLangLabelIndexIgnoreCase(String langLabel) {
			int index = 0;
			for (String entry : langLabelList) {
				if (entry.equalsIgnoreCase(langLabel))
					return index;
				index++;
			}
			return -1;
		}
	}
//	/**
//	 * Test if a {@code List<String>} contains a {@code String}
//	 * the case not being important
//	 * @param list	  the containing {@code List<String>}
//	 * @param element   the contained {@code String}
//	 * @return true if the conditions are verified
//	 */
//	public static Boolean containsIgnoreCase(Iterable<String> set, String element) {
//		if (set == null || element == null) {
//			return false;
//		}
//		for (String entry : set) {
//			if (entry.equalsIgnoreCase(element)) {
//				return true;
//			}
//		}
//		return false;
//	}
}
