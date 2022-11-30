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
import java.util.LinkedList;

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
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultCfgLabel The default CfgLabel
	 * @param list keys for map table
	 */
	public ParamList(String gui, String name, String defaultCfgLabel, LinkedList<String> list, String mid) {
		super(gui, name, defaultCfgLabel);
		valueLabelMap = new IndexableMap();
		for (String element : list)
			put(element, mid + element.toUpperCase());
	}
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
	// ===== Overriders =====
	//
	@Override protected String getCfgValue(String value) {
		return validateKey(value);
	}
	@Override public String getGuiValue() {
		return text(valueLabelMap.getLangLabelFromCfgValue(validateKey(get())));
	}
	@Override public void next() {
		set(valueLabelMap.getNextCfgLabelIgnoreCase(get()));
	}
	@Override public void prev() {
		set(valueLabelMap.getPrevCfgLabelIgnoreCase(get())); 
	}
	@Override public void toggle(MouseWheelEvent e) {
		if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	@Override public void toggle(MouseEvent e) {
		if (getDir(e) == 0)
			setFromDefault();
		else if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	@Override public void setFromCfgValue(String newCfgLabel) {
		super.set(validateKey(newCfgLabel));
	}
	@Override public String set(String newCfgLabel) {
		return super.set(newCfgLabel);
	}
	@Override public int getIndex(){
		return valueLabelMap.getCfgLabelIndexIgnoreCase(get());
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
		return valueLabelMap.getCfgLabelIndexIgnoreCase(value);
	}
	protected void setFromValue(String newValue) {
		String newCfgLabel = valueLabelMap.getCfgLabelFromValue(newValue);
		super.set(newCfgLabel);
	}
	protected String getValue() {
		return valueLabelMap.getLangLabelFromCfgValue(validateKey(get()));
	}
	// ===== Other Public Methods =====
	//
	public LinkedList<String> getOptions(){
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(valueLabelMap.cfgValueList);
		return list;
	}
	public IndexableMap getOptionLabelMap() {
		return valueLabelMap;
	}
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
	/**
	 * Check if the entry is valid and return a valid value
	 * @param key the entry to check
	 * @return a valid value, preferably the value to test
	 */
	private String validateKey(String key) {
		if (valueLabelMap.cfgValuesContainsIgnoreCase(key))
			return key;
		if (valueLabelMap.cfgValuesContainsIgnoreCase(defaultValue()))
			return defaultValue();
		return valueLabelMap.getCfgValue(0);
	}
	//========== Nested class ==========
	//
	static class IndexableMap{
		
		private final LinkedList<String>  cfgValueList	= new LinkedList<>(); // also key list
		private final LinkedList<String>  langLabelList	= new LinkedList<>();

		
		// ========== Constructors ==========
		//
		public IndexableMap() {}
		
		public IndexableMap(IndexableMap map) {
			cfgValueList.addAll(map.cfgValueList);
			langLabelList.addAll(map.langLabelList);
		}
		// ========== Setters ==========
		//
		public void put(String key, String value) {
			cfgValueList.add(key);
			langLabelList.add(value);
		}
		// ========== Getters ==========
		//
		private String getCfgValue(int idx) {
			return cfgValueList.get(idx);
		}
		private String getLangLabel(int idx) {
			return langLabelList.get(idx);
		}
		private String getCfgLabelFromValue(String value) {
			int index = getValueIndexIgnoreCase(value);
			return cfgValueList.get(index);
		}
		/**
		 * get the value from the cfgLabel
		 * @param cfgLabel The cfgLabel to search
		 * @return the corresponding value
		 */
		private String getLangLabelFromCfgValue(String cfgValue) {
			int index = getCfgLabelIndexIgnoreCase(cfgValue);
			return langLabelList.get(index);
		}
		/**
		 * search for cfgValue regardless of the case and return the previous key
		 * @param cfgValue The cfgValue to search
		 * @return the previous cfgValue, looping at the beginning, the last if string is not found
		 */
		private String getPrevCfgLabelIgnoreCase(String cfgValue) {
			int index = getCfgLabelIndexIgnoreCase(cfgValue)-1;
			if (index < 0)
				return cfgValueList.get(cfgValueList.size()-1);
			return cfgValueList.get(index);
		}
		/**
		 * search for cfgValue regardless of the case and return the next key
		 * @param cfgValue The cfgValue to search
		 * @return the next cfgValue, looping at the end, the first if string is not found
		 */
		private String getNextCfgLabelIgnoreCase(String cfgValue) {
			int index = getCfgLabelIndexIgnoreCase(cfgValue) + 1;
			if (index >= cfgValueList.size())
				return cfgValueList.get(0);
			return cfgValueList.get(index);
		}
		/**
		 * Test if cfgValue is part of cfgValue list regardless of the case
		 * @param cfgValue The key to search for
		 * @return true if cfgValue is found
		 */
		private boolean cfgValuesContainsIgnoreCase(String cfgValue) {
			return getCfgLabelIndexIgnoreCase(cfgValue) != -1;
		}
		/**
		 * search for the cfgValue position in the cfgValue list
		 * @param cfgValue The cfgValue to search regardless of the case
		 * @return the cfgValue index, -1 if none
		 */
		private int getCfgLabelIndexIgnoreCase(String cfgValue) {
			int index = 0;
			for (String entry : cfgValueList) {
				if (entry.equalsIgnoreCase(cfgValue))
					return index;
				index++;
			}
			return -1;
		}
		/**
		 * search for the value position in the value list
		 * @param value The label to search regardless of the case
		 * @return the key index, -1 if none
		 */
		private int getValueIndexIgnoreCase(String value) {
			int index = 0;
			for (String entry : langLabelList) {
				if (entry.equalsIgnoreCase(value))
					return index;
				index++;
			}
			return -1;
		}
	}
	/**
     * Test if a {@code List<String>} contains a {@code String}
     * the case not being important
     * @param list      the containing {@code List<String>}
     * @param element   the contained {@code String}
     * @return true if the conditions are verified
     */
	public static Boolean containsIgnoreCase(Iterable<String> set, String element) {
		if (set == null || element == null) {
			return false;
		}
		for (String entry : set) {
			if (entry.equalsIgnoreCase(element)) {
				return true;
			}
		}
		return false;
	}
}
