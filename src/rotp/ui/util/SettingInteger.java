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

import static rotp.ui.util.SettingBase.CostFormula.RELATIVE;
import static rotp.util.Base.random;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class SettingInteger extends SettingBase<Integer> {

	private static final boolean defaultIsList			= false;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= false;
	private static final boolean defaultSaveAllowed		= true;
	private static final Integer defaultBaseInc			= 1;
	private static final Integer defaultShiftInc		= 1;
	private static final Integer defaultCtrlInc			= 1;
	private static final int     randCount				= 100;

	private boolean loop	 = false;
	private Integer minValue = null;
	private Integer maxValue = null;
	private Integer baseInc	 = defaultBaseInc;
	private Integer shiftInc = defaultShiftInc;
	private Integer ctrlInc	 = defaultCtrlInc;
	private float[] posCostFactor;
	private float[] negCostFactor;
	private CostFormula costFormula = RELATIVE;
	private boolean useNegFormula	= false;
	private float	rawBaseCost		= 0f;

	// ========== constructors ==========
	//
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue) {
		super(guiLangLabel, nameLangLabel, defaultValue,
				defaultIsList, defaultIsBullet, defaultLabelsAreFinals, defaultSaveAllowed);
		put("-", "-", 0f, defaultValue);
		this.minValue	= minValue;
		this.maxValue	= maxValue;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue) {
		this(guiLangLabel, nameLangLabel, defaultValue, null, null);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param loop Once an end is reached, go to the other end
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue, boolean loop) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue);
		this.loop = loop;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue);
		this.baseInc	= baseInc;
		this.shiftInc	= shiftInc;
		this.ctrlInc	= ctrlInc;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param saveAllowed  To allow the parameter to be saved in Remnants.cfg
	 * @param costFormula Formula type to establish a cost
	 * @param costFactor To establish a cost
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc
			, boolean saveAllowed, CostFormula costFormula, float... costFactor) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc,
				saveAllowed, costFormula, costFactor, costFactor);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue() The default value
	 * @param minValue() The minimum value() (null = none)
	 * @param maxValue() The maximum value() (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param saveAllowed  To allow the parameter to be saved in Remnants.cfg
	 * @param costFormula Formula type to establish a cost
	 * @param posCostFactor To establish a cost
	 * @param negCostFactor To establish a cost
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc
			, boolean saveAllowed, CostFormula costFormula, float[] posCostFactor, float[] negCostFactor) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc);
		this.costFormula	= costFormula;
		this.posCostFactor	= posCostFactor;
		this.negCostFactor	= negCostFactor;
		saveAllowed(saveAllowed);
	}

	// ===== Overriders =====
	//
	@Override protected Integer randomize(float rand) {
		float lim1 = settingCost(maxValue);
		float lim2 = settingCost(minValue);
		if (rand > 0)
			rand *= Math.max(lim1, lim2);
		else
			rand *= -Math.min(lim1, lim2);

		int   step = Math.max(1, (maxValue - minValue) / (randCount - 1));
		int   bestVal = defaultValue();
		float bestDev = Math.abs(rand - settingCost(bestVal));
		float dev = Math.abs(rand - settingCost(maxValue));		
		if (dev < bestDev) {
			bestVal = maxValue;
			bestDev = dev;
		}
		for (int testVal=minValue; testVal<maxValue; testVal+=step) {
			dev = Math.abs(rand - settingCost(testVal));
			if (dev < bestDev) {
				bestVal = testVal;
				bestDev = dev;
			}
		}
		return bestVal;
	}
	@Override public SettingInteger saveAllowed(boolean allowed){
		super.saveAllowed(allowed);
		return this;
	}
	@Override public void setFromCfgValue(String newValue) {
		set(stringToInteger(newValue));
	}	
	@Override public void next() {
		next(baseInc);
	}
	@Override public void prev() {
		 next(-baseInc);
	}
	@Override public void toggle(MouseEvent e) {
		next(getInc(e) * getDir(e));
	}
	@Override public void toggle(MouseWheelEvent e) {
		next(getInc(e) * getDir(e));
	}
	@Override public float settingCost() {
		return settingCost(settingValue());
	}
	// ===== Other Methods =====
	//
	private void next(Integer i) {
		if (i == 0) {
			setToDefault(true);
			return;
		}
		Integer value = settingValue() + i;
		if (maxValue != null && value > maxValue) {
			if (loop && minValue != null)
				setAndSave(minValue);
			else
				setAndSave(maxValue);
			return;
		}
		else if (minValue != null && value < minValue) {
			if (loop && maxValue != null)
				setAndSave(maxValue);
			else
				setAndSave(minValue);
			return;
		}
		setAndSave(value);
	}
	private float settingCost(Integer value) {
		float baseCost = getBaseCost(value);
		if (posCostFactor == null || baseCost == 0)
			return baseCost;
		float cost = 0;
		if (useNegFormula)
			for (int i=0; i<negCostFactor.length; i++)
				cost -= negCostFactor[i] * Math.pow(baseCost, i);
		else
			for (int i=0; i<posCostFactor.length; i++)
				cost += posCostFactor[i] * Math.pow(baseCost, i);			
		return cost;
	}
	private float getBaseCost() {
		return getBaseCost(settingValue());
	}
	private float getBaseCost(Integer value) {
		switch (costFormula) {
		case DIFFERENCE:
			rawBaseCost   = value - defaultValue();
			useNegFormula = rawBaseCost < 0f;
			return Math.abs(rawBaseCost);
		case RELATIVE:
			rawBaseCost   = Math.abs((float)value / defaultValue());
			useNegFormula = rawBaseCost < 1f;
			if (rawBaseCost > 1f)
				return rawBaseCost-1;
			else
				return (1/rawBaseCost)-1;
		}
		useNegFormula = false;
		return 0f;
	}
	private Integer getInc(MouseEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
	}
	private Integer getInc(MouseWheelEvent e) {
		if (e.isShiftDown()) 
			return shiftInc;
		else if (e.isControlDown())
			return ctrlInc;
		else
			return baseInc;
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
}
