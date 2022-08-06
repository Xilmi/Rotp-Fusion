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
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class ParamList extends AbstractParam<String> {
	
	private IndexableMap optionLabelMap;
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamList(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue);
		optionLabelMap = new IndexableMap();
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param optionLabelMap  Option List with their labels
	 */
	public ParamList(String gui, String name, String defaultValue, LinkedHashMap <String, String> optionLabelMap) {
		super(gui, name, defaultValue);
		this.optionLabelMap = new IndexableMap(optionLabelMap);
	}
	// ===== Overriders =====
	//
	@Override public String getCfgValue() {
		return validate(value);
	}
	@Override public String getGuiValue() {
		return text(optionLabelMap.get(validate(value)));
	}
	@Override public String next() { 
		return setAndSave(optionLabelMap.getNextKeyIgnoreCase(value));
	}
	@Override public String prev() {
		return setAndSave(optionLabelMap.getPrevKeyIgnoreCase(value)); 
	}
	@Override public String toggle(MouseWheelEvent e) {
		if (getDir(e) > 0)
			return next();
		else 
			return prev();
	}
	@Override public String toggle(MouseEvent e) {
		if (getDir(e) == 0) 
			return setToDefault(true);
		else if (getDir(e) > 0)
			return next();
		else 
			return prev();
	}
	@Override public String setFromCfg(String newValue) {
		value = validate(newValue);
		return value;
	}
	@Override public String set(String newValue) {
		value = validate(newValue);
		return value;
	}
	// ===== Other Public Methods =====
	//
	public int getIndex(){
		return optionLabelMap.getKeyIndexIgnoreCase(value);
	}
	public LinkedList<String> getOptions(){
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(optionLabelMap.keySet());
		return list;
	}
	public LinkedHashMap <String, String> getOptionLabelMap() {
		return optionLabelMap;
	}
	/**
	 * Add a new Option with its Label
	 * @param option
	 * @param label
	 * @return this for chaining purpose
	 */
	public ParamList put(String option, String label) {
		optionLabelMap.put(option, label);
		return this;
	}
	/**
	 * Check if the entry is valid and return a valid value
	 * @param value the entry to check
	 * @return a valid value, preferably the value to test
	 */
	public String validate(String value) {
		if (optionLabelMap.keysContainsIgnoreCase(value))
			return value;
		if (optionLabelMap.keysContainsIgnoreCase(defaultValue))
			return defaultValue;
		return optionLabelMap.getKeyByIndex(0);
	}
	//========== Nested class ==========
	//
	@SuppressWarnings("serial")
	public static class IndexableMap extends LinkedHashMap<String, String>{
		
		public IndexableMap() { super(); }
		public IndexableMap(LinkedHashMap<String, String> optionLabelMap) {
			super(optionLabelMap);
		}
		/**
		 * search for string regardless of the case and return the previous key
		 * @param string The key to search
		 * @return the previous key, looping at the begining, the last if string is not found
		 */
		public String getPrevKeyIgnoreCase(String string) {
			int index = getKeyIndexIgnoreCase(string) -1;
			if (index < 0)
				return getKeyByIndex(keySet().size()-1);
			return getKeyByIndex(index);
		}
		/**
		 * search for string regardless of the case and return the previous Value
		 * @param string The Value to search
		 * @return the previous Value, looping at the begining, the last if string is not found
		 */
		public String getPrevValueIgnoreCase(String string) {
			int index = getValueIndexIgnoreCase(string) -1;
			if (index < 0)
				return getValueByIndex(values().size()-1);
			return getValueByIndex(index);
		}
		/**
		 * search for string regardless of the case and return the next key
		 * @param string The key to search
		 * @return the next key, looping at the end, the first if string is not found
		 */
		public String getNextKeyIgnoreCase(String string) {
			int index = getKeyIndexIgnoreCase(string) + 1;
			if (index >= keySet().size())
				return getKeyByIndex(0);
			return getKeyByIndex(index);
		}
		/**
		 * search for string regardless of the case and return the next value
		 * @param string The value to search
		 * @return the next value, looping at the end, the first if string is not found
		 */
		public String getNextValueIgnoreCase(String string) {
			int index = getValueIndexIgnoreCase(string) + 1;
			if (index >= values().size())
				return getValueByIndex(0);
			return getValueByIndex(index);
		}
		/**
		 * @param index The key's position
		 * @return the key or null if index is invalid
		 */
		public String getKeyByIndex(int index) {
			if(index >= 0 && keySet().size() > index) 
				return (String) keySet().toArray()[index];
			return null;
		}
		/**
		 * @param index The value's position
		 * @return the value or null if index is invalid
		 */
		public String getValueByIndex(int index) {
			if(index >= 0 && values().size() > index) 
				return (String) values().toArray()[index];
			return null;
		}
		/**
		 * Test if string is part of keys regardless of the case
		 * @param string The key to search for
		 * @return true if string is found
		 */
		public boolean keysContainsIgnoreCase(String string) {
			return getKeyIndexIgnoreCase(string) != -1;
		}
		/**
		 * Test if string is part of values regardless of the case
		 * @param string The value to search for
		 * @return true if string is found
		 */
		public boolean valuesContainsIgnoreCase(String string) {
			return getValueIndexIgnoreCase(string) != -1;
		}
		/**
		 * search for the string position in the key list
		 * @param string The value to search regardless of the case
		 * @return the key index, -1 if none
		 */
		public int getKeyIndexIgnoreCase(String string) {
			int index = 0;
			for (String entry : keySet()) {
				if (entry.equalsIgnoreCase(string))
					return index;
				index++;
			}
			return -1;
		}
		/**
		 * search for the string position in the value list
		 * @param string The value to search regardless of the case
		 * @return the key index, -1 if none
		 */
		public int getValueIndexIgnoreCase(String string) {
			int index = 0;
			for (String entry : values()) {
				if (entry.equalsIgnoreCase(string))
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
