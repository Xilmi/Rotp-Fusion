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
import static rotp.util.Base.lineSplit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.BasePanel;
import rotp.ui.RotPUI;

public class ParamList extends AbstractParam<String> {

	private final IndexableMap valueLabelMap;
	private boolean hasPreview = false;
	
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
//	/**
//	 * Initializer for Duplicate
//	 * @param gui  The label header
//	 * @param name The name
//	 * @param list keys for map table
//	 * @param defaultIndex index to the default value
//	 */
//	public ParamList(String gui, String name, List<String> list, int defaultIndex) {
//		super(gui, name, list.get(defaultIndex));
//		isDuplicate(true);
//		valueLabelMap = new IndexableMap();
//		for (String element : list)
//			put(element, element); // Temporary; needs to be further initialized
//	}
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
	public ParamList hasPreview(boolean hasPreview) {
		this.hasPreview = hasPreview;
		return this;
	}
	public void reInit(List<String> list) {
		valueLabelMap.clear();
		for (String element : list)
			put(element, text(element)); // "text" should now be available
	}
	// ===== Overriders =====
	//
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
	@Override public void toggle(MouseEvent e, BasePanel frame) {
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
	@Override public String	getFullHelp() {
		String help = getHeadHelp();
		help += BODY_SEPARATOR;
		help += getSubHelp();
		return help;
	}
	@Override public String	dialogHelp(int idx)	{
		if (idx == -1)
			return getHeadHelp();
		return getHeadHelp()
				+ "--- Selected Value --- \\n "
				+ getSubHelp(idx);
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
	private void initGuiTexts() {
		int idx = getIndex(defaultValue());
		initMapGuiTexts();
		if (idx >= 0)
			defaultValue(valueLabelMap.cfgValueList.get(idx));
	}
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
	private void setFromList(BasePanel frame) {
		String message	= "<html>" + getGuiDescription() + "</html>";
		String title	= text(labelId(), "");
		initGuiTexts();
		String[] list= valueLabelMap.guiTextList.toArray(new String[listSize()]);
		ListDialog dialog = new ListDialog(
				frame,	frame.getParent(),	// Frame & Location component
				message, title,				// Message & Title
				list, currentOption(),		// List & Initial choice
				null, true,					// long Dialogue & isVertical
				RotPUI.scaledSize(360), RotPUI.scaledSize(300),	// size
				null,						// Font
				frame,						// Preview
				valueLabelMap.cfgValueList,	// Alternate return
				this);

		String input = (String) dialog.showDialog();
		if (input != null && valueLabelMap.getValueIndexIgnoreCase(input) >= 0)
			set(input);
	}
	private String getHeadHelp() {
		String label = labelId();
		String name  = text(label, "");
		String help  = realText(label+LABEL_HELP);
		if (help == null)
			help = realText(label+LABEL_DESCRIPTION);
		if (help == null)
			help = "";
		return name + HEAD_SEPARATOR + help + lineSplit;
	}
	private String getSubHelp() {
		String help = "";
		int size = listSize();
		for (int i=0; i<size; i++)
			help += getSubHelp(i);
		return help;
	}
	private String getSubHelp(int id) {
		String name = name(id);
		String help = realHelp(id);
		if (help == null)
			help = realDescription(id);
		if (help == null)
			help = "";
		return "- " + name + HELP_SEPARATOR + help + lineSplit;
	}
	protected String labelText(String label) { return text(label); }
	private String label(int id)			 { return valueLabelMap.getLangLabel(id); }
	private String name(int id)				 { return text(label(id), ""); }
	private String realDescription(int id)	 { return realText(label(id)+LABEL_DESCRIPTION); }
	private String realHelp(int id)			 { return realText(label(id)+LABEL_HELP); }
	private void initMapGuiTexts() {
		valueLabelMap.guiTextList.clear();
		for (String label : valueLabelMap.langLabelList)
			valueLabelMap.guiTextList.add(labelText(label));
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
