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

import static rotp.util.Base.random;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import rotp.ui.BaseText;
import rotp.ui.UserPreferences;
import rotp.util.LabelManager;

public class SettingBase<T> {
	
	public enum CostFormula {DIFFERENCE, RELATIVE}

	private static final String LABEL_DESCRIPTION = "_DESC";
	private static final String END = "   ";
	private static final boolean defaultIsList			= true;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= false;
	private static final boolean defaultSaveAllowed		= true;
	private static final String  costFormat				= "%6s ";

	private final LinkedList<String> cfgValueList = new LinkedList<>();
	private final LinkedList<String> labelList	  = new LinkedList<>();
	private final LinkedList<Float>	 costList	  = new LinkedList<>();
	private final LinkedList<T> 	 valueList	  = new LinkedList<>();
	private final String nameLabel;
	private final String guiLabel;

	private boolean labelsAreFinals = defaultLabelsAreFinals;
	private boolean isList			= defaultIsList;
	private boolean isBullet		= defaultIsBullet;
	private boolean saveAllowed		= defaultSaveAllowed;
	private int selectedIndex = -1;
	private int defaultIndex  = -1;
	private T defaultValue	  = null;
	private boolean isSpacer  = false;
	private boolean hasNoCost = false;
	private BaseText settingText;
	private BaseText[] optionsText;
	
	// ========== Constructors and initializers ==========
	//
	/**
	 * @param guiLabel		The label header
	 * @param nameLabel		The nameLabel
	 * @param defaultIndex	The default list index
	 * @param isList		Either a list or simple value
	 * @param isBullet		To be displayed as bullet list
	 * @param labelsAreFinals when false: Labels are combined withName and Gui Label
	 * @param saveAllowed	To allow the parameter to be saved in Remnants.cfg
	 */
	public SettingBase(String guiLabel, String nameLabel, T defaultValue,
			boolean isList, boolean isBullet, boolean labelsAreFinals, boolean saveAllowed) {
		this(guiLabel, nameLabel);
		this.defaultValue	= defaultValue;
		this.isList			= isList;
		this.isBullet		= isBullet;
		this.labelsAreFinals= labelsAreFinals;
		this.saveAllowed	= saveAllowed;
	}
	/**
	 * @param guiLabel  The label header
	 * @param nameLabel The nameLabel
	 * @param defaultIndex  The default list index
	 */
	public SettingBase(String guiLabel, String nameLabel, int defaultIndex) {
		this(guiLabel, nameLabel);
		setDefaultIndex(defaultIndex);
	}
	/**
	 * @param guiLabel  The label header
	 * @param nameLabel The nameLabel
	 * @param defaultValue The default Value
	 */
	public SettingBase(String guiLabel, String nameLabel, T defaultValue) {
		this(guiLabel, nameLabel);
		this.defaultValue = defaultValue;
	}
	/**
	 * @param guiLabel  The label header
	 * @param nameLabel The nameLabel
	 */
	public SettingBase(String guiLabel, String nameLabel) {
		this.guiLabel	= guiLabel;
		this.nameLabel	= nameLabel;
	}

	public SettingBase<?> saveAllowed(boolean allowed){
		saveAllowed = allowed;
		return this;
	}
	public void settingText(BaseText settingText) {
		this.settingText = settingText;
	}
	public void optionsText(BaseText[] optionsText) {
		this.optionsText = optionsText;
	}
	public void optionText(BaseText optionText, int i) {
		optionsText[i] = optionText;
	}
	public SettingBase<?> initOptionsText() {
		if (boxSize() > 0)
			optionsText(new BaseText[boxSize()]);
		return this;
	}
	public SettingBase<?> isList(boolean isList) {
		this.isList = isList;
		return this;
	}
	public SettingBase<?> isSpacer(boolean isSpacer) {
		this.isSpacer = isSpacer;
		return this;
	}
	public SettingBase<?> hasNoCost(boolean hasNoCost) {
		this.hasNoCost = hasNoCost;
		return this;
	}
	public SettingBase<?> isBullet(boolean isBullet) {
		this.isBullet = isBullet;
		return this;
	}
	public SettingBase<?> labelsAreFinals(boolean labelsAreFinals) {
		this.labelsAreFinals = labelsAreFinals;
		return this;
	}
	SettingBase<?> clearLists(){
		cfgValueList.clear();
		labelList.clear();
		costList.clear();
		valueList.clear();
		return this;
	}

	// ========== Overridable Methods ==========
	//
	public void pushSetting() {}
	public void pullSetting() {}
	public void guiSelect() {
		if (isSpacer())
			return;
		pushSetting();
		updateGui();
	}
	public void updateGui() { 
		if (isSpacer())
			return;
		settingText().repaint();
		for (int optionIdx=0; optionIdx < boxSize(); optionIdx++) {
			optionText(optionIdx).disabled(optionIdx == selectedIndex);
			optionText(optionIdx).repaint();
		}
	}
	public void setRandom(float mean, float stDev) {
		set(randomize(mean, stDev));
	}
	public T randomize(float mean, float stDev) {
		if (this.isSpacer)
			return null;
		if (hasNoCost && isList && !valueList.isEmpty()) {
			int rand = random.nextInt(valueList.size());
			return valueList.get(rand);
		}
		float rand = mean + stDev * (float) random.nextGaussian();
		if (isList) {
			int bestIdx = 0;
			float bestDev =  Math.abs(rand - costList.getFirst());
			for (int i=1; i<costList.size(); i++) {
				float dev = Math.abs(rand - costList.get(i));
				if (dev < bestDev) {
					bestIdx = i;
					bestDev = dev;
				}
			}
			return valueList.get(bestIdx);
		}
		return null; // Should be overridden
	}
	public void toggle(MouseWheelEvent e) {
		if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	public void toggle(MouseEvent e) {
		if (getDir(e) == 0) 
			setToDefault(true);
		else if (getDir(e) > 0)
			next();
		else 
			prev();
	}
	public void next() {
		selectedIndex = cfgValidIndex()+1;
		if (selectedIndex >= cfgValueList.size())
			selectedIndex = 0;
		save();
	}
	public void prev() {
		selectedIndex = cfgValidIndex()-1;
		if (selectedIndex < 0)
			selectedIndex = cfgValueList.size()-1;
		save();
	}
	public float settingCost() {
		if (isSpacer() || hasNoCost)
			return 0f;;
		return costList.get(costValidIndex());
	}
	public T settingValue() {
		return valueList.get(valueValidIndex());
	}
	public String guiSettingDisplayStr() {
		if (isBullet) 
			return guiSettingLabelCostStr();
		else
			return guiSettingLabelValueCostStr();		
	}
	// ========== Setter ==========
	//
	public void newCost(int index, float newCost) {
		costList.set(index, newCost);
	}
	public SettingBase<?> defaultIndex(int index) {
		setDefaultIndex(bounds(0, index, cfgValueList.size()-1));
		return this;
	}
	public SettingBase<?> defaultCfgValue(String defaultCfgValue) {
		setDefaultIndex(cfgValidIndex(indexOfIgnoreCase(defaultCfgValue, cfgValueList)));
		return this;
	}
	public SettingBase<?> defaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
		if (isList) {
			setDefaultIndex(valueValidIndex(valueList.indexOf(defaultValue)));
		}
		return this;
	}
	public SettingBase<?> index(int newIndex) {
		selectedIndex = cfgValidIndex(newIndex);
		return this;
	}
	public SettingBase<?> setAndSave(T newValue) {
		set(newValue);
		save();
		return this;
	}
	public SettingBase<?> setFromIndexAndSave(int index) {
		index(index);
		save();
		return this;
	}
	public SettingBase<?> set(T newValue) {
		if (valueList.size()==0) { // List empty; then create one
			valueList.add(newValue);
			selectedIndex = 0;
			return this;
		}
		if (!isList) {
			valueList.set(0, newValue);
			return this;
		}
		selectedIndex = valueList.indexOf(newValue);
		if (selectedIndex>=0)
			return this;
		if (defaultValue == null) {
			selectedIndex = valueValidDefaultIndex();
			return this;
		}
		selectedIndex = valueValidIndex(valueList.indexOf(defaultValue));
		return this;
	}
	public void toggle(MouseEvent e, MouseWheelEvent w) {
		if (e == null)
			toggle(w);
		else
			toggle(e);
	}
	public void setToDefault(boolean save) {
		selectedIndex = cfgValidDefaultIndex();
		save();
	}
	public void setFromCfgValue(String cfgValue) {
		selectedIndex = cfgValidIndex(indexOfIgnoreCase(cfgValue, cfgValueList));
	}
	public void setFromLabel(String langLabel) {
		selectedIndex = cfgValidIndex(indexOfIgnoreCase(langLabel, labelList));
	}
	public String guiSettingValue() {
		return String.valueOf(settingValue());
	}
	public String guiOptionValue(int index) { // For List
		return String.valueOf(optionValue(index));
	}
	// ===== Getters =====
	//
	public T defaultValue()			{ return defaultValue; }
	public boolean isSpacer()		{ return isSpacer; }
	public boolean hasNoCost()		{ return hasNoCost; }
	public boolean isBullet()		{ return isBullet; }
	public int index()				{ return cfgValidIndex(); }
	public T optionValue(int index) { return valueList.get(valueValidIndex(index)); }
	public BaseText settingText()	{ return settingText; }
	public BaseText[] optionsText()	{ return optionsText; }
	public BaseText optionText(int i)	{ return optionsText[i]; }
	public float optionCost(int index)	{ return costList.get(index); }
	public String guiOptionLabel()	{ return guiOptionLabel(index()); }
	public String optionLabel()		{ return optionLabel(index()); }
	public String getLabel()		{ return text(labelId()); }
	public String cfgName()			{ return nameLabel; }
	public String labelId()			{ return guiLabel + nameLabel; }
	public boolean isDefaultIndex()	{ return cfgValidIndex() == defaultIndex; }
	public String cfgValue()		{
		if (isList)
			return cfgValueList.get(cfgValidIndex());
		return String.valueOf(settingValue());
	}
	public int boxSize() {
		if (isBullet())
			return costList.size();
		else
			return 0;
	}
	public String optionLabel(int index) {
		return labelList.get(cfgValidIndex(index));
	}
	public String guiOptionLabel(int index) {
		return text(labelList.get(cfgValidIndex(index)));
	}
	public LinkedList<String> getOptions(){
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(cfgValueList);
		return list;
	}
	public String settingCostString() {
		return settingCostString(1); // default decimal number
	}
	private String settingCostString(int dec) {
		return costString(settingCost(), dec);
	}
	private String optionCostStringIdx(int idx, int dec) {
		return costString(optionCost(idx), dec);
	}
	private String guiSettingLabelCostStr() {
		if (hasNoCost)
			return getLabel();
		return getLabel() + ": " + settingCostString();
	}
	private String guiSettingLabelValueCostStr() {
		if (hasNoCost)
			return getLabel() + ": " + guiSettingValue();
		return getLabel() + ": " + guiSettingValue() + " " + settingCostString();
	}
	String guiCostOptionStr(int idx) {
		return guiCostOptionStr(idx, 0);
	}
	private String guiCostOptionStr(int idx, int dec) {
		String cost = String.format(costFormat,  optionCostStringIdx(idx, dec));
		String txt = cost + guiOptionLabel(idx);
		return txt;
	}
	// ===== Other Public Methods =====
	//
	/**
	 * Add a new Option with its Label
	 * @param cfgValue
	 * @param langLabel
	 * @return this for chaining purpose
	 */
	public void put(String cfgValue, String langLabel) {
		cfgValueList.add(cfgValue);
		labelList.add(langLabel);
	}
	/**
	 * Add a new Option with its Label
	 * @param cfgValue
	 * @param langLabel
	 * @param cost
	 * @return this for chaining purpose
	 */
	public void put(String cfgValue, String langLabel, float cost) {
		cfgValueList.add(cfgValue);
		labelList.add(langLabel);
		costList.add(cost);
	}
	/**
	 * Add a new Option with its Label
	 * @param cfgValue
	 * @param langLabel
	 * @param cost
	 * @param value
	 * @return this for chaining purpose
	 */
	public void put(String cfgValue, String langLabel, float cost, T value) {
		cfgValueList.add(cfgValue);
		costList.add(cost);
		valueList.add(value);
		if (labelsAreFinals)
			labelList.add(langLabel);
		else
			labelList.add(labelId() +"_"+ langLabel);
	}
	/**
	 * Add a new Option with its Label
	 * @param cfgValue
	 * @param langLabel
	 * @param cost The cost of this option
	 * @param value
	 * @return this for chaining purpose
	 */
	public void put(String cfgValue, String langLabel, Integer cost, T value) {
		put(cfgValue, langLabel, cost.floatValue(), value);
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
	// ========== Private Methods ==========
	//
	private void setDefaultIndex(int index) {
		defaultIndex = index;
		if (selectedIndex == -1)
			selectedIndex = defaultIndex;
	}
	private String costString(float cost, int dec) {
		String str = "(";
		switch (dec) {
		case 0:
			str += "" + Math.round(cost);
			break;
		case 2:
			str +=  new DecimalFormat("0.00").format(cost);
			break;
		case 3:
			str +=  new DecimalFormat("0.000").format(cost);
			break;
		default:
			str +=  new DecimalFormat("0.0").format(cost);
			break;
		}
		return str + ")";
	}
	private void save() {
		if (saveAllowed) UserPreferences.save();
	}
	private int bounds(int low, int val, int hi) {
		return Math.min(Math.max(low, val), hi);
	}
	private int cfgValidDefaultIndex() {
		return bounds(0, defaultIndex, cfgValueList.size()-1);
	}
	private int cfgValidIndex() {
		return cfgValidIndex(selectedIndex);
	}
	private int cfgValidIndex(int index) {
		if (index<0 || index>cfgValueList.size())
			return valueValidDefaultIndex();
		return index;
	}
	private int valueValidDefaultIndex() {
		return bounds(0, defaultIndex, valueList.size()-1);
	}
	private int valueValidIndex() {
		return valueValidIndex(selectedIndex);
	}	
	private int valueValidIndex(int index) {
		if (index<0 || index>valueList.size())
			return valueValidDefaultIndex();
		return index;
	}	
	private int costValidDefaultIndex() {
		return bounds(0, defaultIndex, costList.size()-1);
	}
	private int costValidIndex() {
		return costValidIndex(selectedIndex);
	}
	private int costValidIndex(int index) {
		if (index<0 || index>costList.size())
			return costValidDefaultIndex();
		return index;
	}
	protected static String text(String key) {
		return LabelManager.current().label(key);
	}
	private int indexOfIgnoreCase(String string, LinkedList<String> list) {
		int index = 0;
		for (String entry : list) {
			if (entry.equalsIgnoreCase(string))
				return index;
			index++;
		}
		return -1;
	}
	/**
     * Test if a {@code List<String>} contains a {@code String}
     * the case not being important
     * @param list      the containing {@code List<String>}
     * @param element   the contained {@code String}
     * @return true if the conditions are verified
     */
	private static Boolean containsIgnoreCase(Iterable<String> set, String element) {
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
