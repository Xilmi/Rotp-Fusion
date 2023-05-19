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
import static rotp.ui.util.InterfaceParam.langLabel;
import static rotp.ui.util.InterfaceParam.rowsSeparator;
import static rotp.ui.util.InterfaceParam.tableFormat;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.RotPUI;
import rotp.ui.game.BaseModPanel;

public class ParamList extends AbstractParam<String> {

	private final IndexableMap valueLabelMap;
	private boolean showFullGuide = false;
	
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
	public void showFullGuide(boolean show) { showFullGuide = show; }
 	public void reInit(List<String> list) {
		valueLabelMap.clear();
		for (String element : list)
			put(element, langLabel(element)); // "text" should now be available
	}
	// ===== Overriders =====
	//
	@Override public String guideDefaultValue()			{ return name(defaultValueIndex()); }
	@Override public String getCfgValue(String value)	{ return validateValue(value); }
	@Override public String	guideValue()				{ return name(this.getIndex()); }
	@Override public void	next()						{
		set(valueLabelMap.getNextLangLabelIgnoreCase(get()));
	}
	@Override public void	prev()						{
		set(valueLabelMap.getPrevValueIgnoreCase(get())); 
	}
	@Override public void	toggle(MouseWheelEvent e)	{
		if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	@Override public void	toggle(MouseEvent e, BaseModPanel frame){
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
	@Override public void	setFromCfgValue(String newCfgValue)		{
		super.set(validateValue(newCfgValue));
	}
	@Override public int	getIndex()				{ return getValidIndex(); }
	@Override public String	setFromIndex(int id)	{
		return super.set(value(valueLabelMap.getCfgValue(getValidIndex(id))));
	}
	@Override public String	getGuiValue(int id)		{
		return langLabel(valueLabelMap.getLangLabel(getValidIndex(id)));
	}
	@Override public String	getGuide()				{
		if(showFullGuide)
			return getFullHelp();
		return super.getGuide();
	}
	@Override public String	getFullHelp()			{
		String help = getHeadGuide();
		help += getTableHelp();
		return help;
	}
	@Override public String getSelectionStr()		{ return getValueStr(getIndex(get())); }
	@Override public String getValueStr(int id)		{ return valueGuide(getValidIndex(id)); }
	@Override public String valueGuide(int id) 		{ return tableFormat(getRowGuide(id)); }
	@Override public String getLangLabel(int id)	{
		if (isDuplicate())
			return valueLabelMap.getCfgValue(id);
		else
			return valueLabelMap.getLangLabel(getValidIndex(id));
	}
	// ===== Other Protected Methods =====
	//
	protected int getIndex(String value)		{
		if (isDuplicate()) {
			int idx = valueLabelMap.getLangLabelIndexIgnoreCase(value);
			if (idx == -1)
				return valueLabelMap.getValueIndexIgnoreCase(value);
			return idx;
		}
		else
			return valueLabelMap.getValueIndexIgnoreCase(value);
	}
	protected String getLangLabelFromValue(String newValue) {
		String newLangLabel = valueLabelMap.getLangLabelFromValue(newValue);
		return newLangLabel;
	}
	protected String getMapValue(int id) { return valueLabelMap.cfgValueList.get(id); }
	// ===== Other Public Methods =====
	//
	public LinkedList<String> getOptions()	{
		LinkedList<String> list = new LinkedList<String>();
		if (isDuplicate()) // Values and labels are swap because values may be redundant
			list.addAll(valueLabelMap.langLabelList);
		else
			list.addAll(valueLabelMap.cfgValueList);
		return list;
	}
	public IndexableMap getOptionLabelMap()	{ return valueLabelMap; }
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
		int id = defaultValueIndex();
		initMapGuiTexts();
		if (id >= 0)
			defaultValue(valueLabelMap.cfgValueList.get(id));
	}
	/**
	 * Check if the entry is valid and return a valid value
	 * @param key the entry to check
	 * @return a valid value, preferably the value to test
	 */
	private String validateValue(String key)	{
		if (valueLabelMap.valuesContainsIgnoreCase(key))
			return key;
		if (valueLabelMap.valuesContainsIgnoreCase(defaultValue()))
			return defaultValue();
		return valueLabelMap.getCfgValue(0);
	}
	private int listSize()						{ return valueLabelMap.listSize(); }
	private String currentOption()				{
		int index = Math.max(0, getIndex());
		return valueLabelMap.guiTextList.get(index);
	}
	private void setFromList(BaseModPanel frame){
		String message	= "<html>" + getGuiDescription() + "</html>";
		String title	= langLabel(getLangLabel(), "");
		initGuiTexts();
		String[] list= valueLabelMap.guiTextList.toArray(new String[listSize()]);
		ListDialog dialog = new ListDialog(
				frame,	frame.getParent(),	// Frame & Location component
				message, title,				// Message & Title
				list, currentOption(),		// List & Initial choice
				null, true,					// long Dialogue & isVertical
				RotPUI.scaledSize(250), RotPUI.scaledSize(300),	// size
				null,						// Font
				frame,						// Preview
				valueLabelMap.cfgValueList,	// Alternate return
				this); 						// Parameter

		String input = (String) dialog.showDialog();
		if (input != null && valueLabelMap.getValueIndexIgnoreCase(input) >= 0)
			set(input);
	}
	private String getTableHelp()				{
		int size = listSize();
		String rows = "";
		if (size>0) {
			rows = getRowGuide(0);
			for (int i=1; i<size; i++)
				rows += rowsSeparator() + getRowGuide(i);
		}
		return tableFormat(rows);
	}
	private void initMapGuiTexts()				{
		valueLabelMap.guiTextList.clear();
		for (String label : valueLabelMap.langLabelList)
			valueLabelMap.guiTextList.add(langLabel(label));
	}
	private int defaultValueIndex()				{
		
		return getIndex(defaultValue());
	}
	public int	getRawIndex()					{
		String value = get();
		if (isDuplicate()) {
			int idx = valueLabelMap.getLangLabelIndexIgnoreCase(value);
			if (idx == -1)
				return valueLabelMap.getValueIndexIgnoreCase(value);
			else
				return idx;
		}
		else
			return valueLabelMap.getValueIndexIgnoreCase(value);
	}
	private int getValidIndex()					{
		int id = getRawIndex();
		if (id < 0)
			id = defaultValueIndex();
		if (id < 0)
			return 0;
		return id;		
	}
	private int getValidIndex(int id)			{
		if (id < 0)
			return getValidIndex();
		return id;
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
		private int    listSize()			{ return cfgValueList.size(); }
		private String getCfgValue(int id)	{ return cfgValueList.get(id); }
		private String getLangLabel(int id)	{ return langLabelList.get(id); }
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
}
