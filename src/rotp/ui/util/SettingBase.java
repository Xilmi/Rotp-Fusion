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
import static rotp.util.Base.textSubs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import rotp.model.empires.Race;
import rotp.model.game.DynamicOptions;
import rotp.ui.BaseText;
import rotp.util.LabelManager;

public class SettingBase<T> implements InterfaceParam {
	
	public enum CostFormula {DIFFERENCE, RELATIVE}

	private static final boolean defaultIsList			= true;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= false;
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
	private T selectedValue	  = null;
	private T defaultValue	  = null;
	private boolean isSpacer  = false;
	private boolean hasNoCost = false;
	private BaseText settingText;
	private BaseText[] optionsText;
	
	private float lastRandomSource;
	
	// ========== Constructors and initializers ==========
	//
	/**
	 * @param guiLabel		The label header
	 * @param nameLabel		The nameLabel
	 * @param defaultIndex	The default list index
	 * @param isList		Either a list or simple value
	 * @param isBullet		To be displayed as bullet list
	 * @param labelsAreFinals when false: Labels are combined withName and Gui Label
	 */
	SettingBase(String guiLabel, String nameLabel, T defaultValue,
			boolean isList, boolean isBullet, boolean labelsAreFinals) {
		this(guiLabel, nameLabel);
		this.defaultValue	= defaultValue;
		this.isList			= isList;
		this.isBullet		= isBullet;
		this.labelsAreFinals= labelsAreFinals;
	}
	/**
	 * @param guiLabel  The label header
	 * @param nameLabel The nameLabel
	 */
	public SettingBase(String guiLabel, String nameLabel) {
		this.guiLabel	= guiLabel;
		this.nameLabel	= nameLabel;
	}
	void settingText(BaseText settingText) {
		this.settingText = settingText;
	}
	private void optionsText(BaseText[] optionsText) {
		this.optionsText = optionsText;
	}
	void optionText(BaseText optionText, int i) {
		optionsText[i] = optionText;
	}
	public SettingBase<?> initOptionsText() {
		if (boxSize() > 0)
			optionsText(new BaseText[boxSize()]);
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
	// ========== Public Interfaces ==========
	//
	@Override public void setFromCfgValue(String cfgValue) {
		int index = cfgValidIndex(indexOfIgnoreCase(cfgValue, cfgValueList));
		selectedValue = valueList.get(index);
	}
	@Override public void next() {
		int selectedIndex = cfgValidIndex()+1;
		if (selectedIndex >= cfgValueList.size())
			selectedIndex = 0;
		selectedValue = valueList.get(selectedIndex);		
	}
	@Override public void prev() {
		int selectedIndex = cfgValidIndex()-1;
		if (selectedIndex < 0)
			selectedIndex = cfgValueList.size()-1;
		selectedValue = valueList.get(selectedIndex);		
	}
	@Override public void toggle(MouseEvent e, MouseWheelEvent w) {
		if (e == null)
			toggle(w);
		else
			toggle(e);
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
	@Override public void setFromDefault() {
		selectedValue = defaultValue;		
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isSpacer)
			options.setStringOptions(labelId(), getCfgValue());
	}
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isSpacer)
			setFromCfgValue(options.getStringOptions(labelId(), getDefaultCfgValue()));
	}
	@Override public String getCfgValue() 		{ return getCfgValue(settingValue()); }
	@Override public String getCfgLabel()		{ return nameLabel; }
	@Override public String getGuiDescription() { return text(descriptionId()); }
	@Override public String getGuiDisplay()		{ return text(labelId(), guiSettingValue()) + END; }

	// ========== Overridable Methods ==========
	//
	protected String getCfgValue(T value) {
		if (isList) {
			int index = valueValidIndex(valueList.indexOf(value));
			return cfgValueList.get(index);
		}
		return String.valueOf(value);
	}
	public void pushSetting() {}
	public void pullSetting() {}
	public float maxValueCostFactor() {
		if (isList) {
			return Collections.max(costList);
		}
		return 0f;
	}
	public float minValueCostFactor() {
		if (isList) {
			return Collections.min(costList);
		}
		return 0f;
	}
	public void updateGui() { 
		if (isSpacer())
			return;
		settingText().repaint();
		int selectedIndex = cfgValidIndex();
		for (int optionIdx=0; optionIdx < boxSize(); optionIdx++) {
			optionText(optionIdx).disabled(optionIdx == selectedIndex);
			optionText(optionIdx).repaint();
		}
	}
	public float settingCost() {
		if (isSpacer() || hasNoCost)
			return 0f;;
		return costList.get(costValidIndex());
	}
	public T settingValue() {
		if (selectedValue == null)
			return defaultValue;
		else
			return selectedValue;
	}
	protected T randomize(float rand) {
		if (isList) {
			if (rand > 0)
				rand *= Collections.max(costList);
			else
				rand *= -Collections.min(costList);				
			return getValueFromCost(rand);
		}
		return null; // Should be overridden
	}
	protected T getValueFromCost(float cost) {
		if (isList) {
			int bestIdx = 0;
			float bestDev =  Math.abs(cost - costList.getFirst());
			for (int i=1; i<costList.size(); i++) {
				float dev = Math.abs(cost - costList.get(i));
				if (dev < bestDev) {
					bestIdx = i;
					bestDev = dev;
				}
			}
			return valueList.get(bestIdx);
		}
		return null; // Should be overridden
	}
	// ========== Setter ==========
	//
	public void setRandom(float min, float max, boolean gaussian) {
		set(randomize(min, max, gaussian));
	}
	public void setRandom(float rand) {
		lastRandomSource = rand;
		set(randomize(rand));
	}
	public void setValueFromCost(float cost) {
		set(getValueFromCost(cost));
	}
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
	public SettingBase<?> index(int newIndex) {
		selectedValue = valueList.get(cfgValidIndex(newIndex));
		return this;
	}
	public SettingBase<?> set(T newValue) {
		selectedValue = newValue;
		if (isList)
			selectedValue = valueList.get(valueValidIndex());
		return this;
	}
	public void setFromLabel(String langLabel) {
		selectedValue = valueList.get(cfgValidIndex(indexOfIgnoreCase(langLabel, labelList)));
	}
	public String guiSettingValue() {
		return String.valueOf(settingValue());
	}
	public String guiOptionValue(int index) { // For List
		return String.valueOf(optionValue(index));
	}
	// ===== Getters =====
	//
	String guiSettingDisplayStr() {
		if (isBullet) 
			return guiSettingLabelCostStr();
		else
			return guiSettingLabelValueCostStr();		
	}
	T defaultValue()				{ return defaultValue; }
	public boolean isSpacer()		{ return isSpacer; }
	public boolean hasNoCost()		{ return hasNoCost; }
	public boolean isBullet()		{ return isBullet; }
	public float lastRandomSource()	{ return lastRandomSource; }
	int index()						{ return cfgValidIndex(); }
	BaseText settingText()		{ return settingText; }
	BaseText[] optionsText()	{ return optionsText; }
	BaseText optionText(int i)	{ return optionsText[i]; }
	String guiOptionLabel()	{ return guiOptionLabel(index()); }
	public String getLabel()		{ return text(labelId()); }
	public boolean isDefaultIndex()	{ return cfgValidIndex() == rawDefaultIndex(); }
	public float costFactor() {
		if (isList) {
			if (lastRandomSource<0)
				return -Collections.min(costList);
			else
				return Collections.max(costList);
		}
		if (settingCost()<0)
			return -Math.min(maxValueCostFactor(), minValueCostFactor());
		else
			return Math.max(maxValueCostFactor(), minValueCostFactor());
	}
	public int boxSize() {
		if (isBullet())
			return costList.size();
		else
			return 0;
	}
	String guiOptionLabel(int index) {
		return text(labelList.get(cfgValidIndex(index)));
	}
	public LinkedList<String> getOptions(){
		LinkedList<String> list = new LinkedList<String>();
		list.addAll(cfgValueList);
		return list;
	}
	String guiCostOptionStr(int idx) {
		return guiCostOptionStr(idx, 0);
	}
	// ===== Other Public Methods =====
	//
	public void guiSelect() {
		if (isSpacer())
			return;
		pushSetting();
		updateGui();
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
	/**
	 * @param min Limit Value in %
	 * @param max Limit Value in %
	 * @param gaussian yes = smooth edges
	 * @return a randomized value
	 */
	private T randomize(float min, float max, boolean gaussian) {
		if (this.isSpacer)
			return null;
		if (hasNoCost && isList && !valueList.isEmpty()) {
			int rand = random.nextInt(valueList.size());
			return valueList.get(rand);
		}
		float rand;
		float mini = Math.min(min, max)/100;
		float maxi = Math.max(min, max)/100;
		if (gaussian)
			rand = (maxi + mini + (maxi-mini) * (float) random.nextGaussian())/2;
		else
			rand = mini + (maxi-mini) * (float) random.nextFloat();
		lastRandomSource = rand;
		return randomize(rand);
	}
	private T optionValue(int index)	{ return valueList.get(valueValidIndex(index)); }
	private float optionCost(int index)	{ return costList.get(index); }
	protected String labelId()			{ return guiLabel + nameLabel; }
	private String descriptionId()		{ return labelId() + LABEL_DESCRIPTION; }
	private String getDefaultCfgValue() { return getCfgValue(defaultValue); }
	private String settingCostString()	{ return settingCostString(1); } // default decimal number
	private String settingCostString(int dec) { return costString(settingCost(), dec); }
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
	private String guiCostOptionStr(int idx, int dec) {
		String cost = String.format(costFormat,  optionCostStringIdx(idx, dec));
		String txt = cost + guiOptionLabel(idx);
		return txt;
	}
	private void setDefaultIndex(int index) {
		defaultValue = valueList.get(cfgValidIndex(index));
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
	private int bounds(int low, int val, int hi) {
		return Math.min(Math.max(low, val), hi);
	}
	private int cfgValidIndex() {
		return cfgValidIndex(rawSelectedIndex());
	}
	private int cfgValidIndex(int index) {
		if (index<0 || index>cfgValueList.size())
			return valueValidDefaultIndex();
		return index;
	}
	private int valueValidDefaultIndex() {
		return bounds(0, rawDefaultIndex(), valueList.size()-1);
	}
	private int valueValidIndex() {
		return valueValidIndex(rawSelectedIndex());
	}	
	private int valueValidIndex(int index) {
		if (index<0 || index>valueList.size())
			return valueValidDefaultIndex();
		return index;
	}	
	private int costValidDefaultIndex() {
		return bounds(0, rawDefaultIndex(), costList.size()-1);
	}
	private int rawSelectedIndex() {
		return valueList.indexOf(selectedValue);
	}
	private int rawDefaultIndex() {
		return valueList.indexOf(defaultValue);
	}
	private int costValidIndex() {
		return costValidIndex(rawSelectedIndex());
	}
	private int costValidIndex(int index) {
		if (index<0 || index>costList.size())
			return costValidDefaultIndex();
		return index;
	}
	protected static String text(String key) {
		return LabelManager.current().label(key);
	}
	private static String text(String key, String... vals) {
		String str = text(key);
		for (int i=0;i<vals.length;i++)
			str = str.replace(textSubs[i], vals[i]);
		return str;
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
}
