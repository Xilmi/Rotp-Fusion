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

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.model.game.DynamicOptions;
import rotp.ui.game.BaseModPanel;

public class SettingInteger extends SettingBase<Integer> {

	private static final boolean defaultIsList			= false;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= false;
	private static final Integer defaultBaseInc			= 1;
	private static final Integer defaultShiftInc		= 1;
	private static final Integer defaultCtrlInc			= 1;
	private static final int     randCount				= 201;

	private boolean loop	 = false;
	private Integer minValue = null;
	private Integer maxValue = null;
	private Float	norm	 = 100f;
	private Integer baseInc	 = defaultBaseInc;
	private Integer shiftInc = defaultShiftInc;
	private Integer ctrlInc	 = defaultCtrlInc;
	protected float[] posCostFactor;
	protected float[] negCostFactor;
	private CostFormula costFormula = RELATIVE;
	private boolean useNegFormula	= false;
	private float	rawBaseCost		= 0f;

	// ========== constructors ==========
	//
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 */
	private SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue) {
		super(guiLangLabel, nameLangLabel, defaultValue,
				defaultIsList, defaultIsBullet, defaultLabelsAreFinals);
		this.minValue	= minValue;
		this.maxValue	= maxValue;
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
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
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param costFormula Formula type to establish a cost
	 * @param posCostFactor To establish a cost
	 * @param negCostFactor To establish a cost
	 */
	public SettingInteger(String guiLangLabel, String nameLangLabel, Integer defaultValue
			, Integer minValue, Integer maxValue
			, Integer baseInc, Integer shiftInc, Integer ctrlInc
			, CostFormula costFormula, float[] posCostFactor, float[] negCostFactor) {
		this(guiLangLabel, nameLangLabel, defaultValue, minValue, maxValue, baseInc, shiftInc, ctrlInc);
		this.costFormula	= costFormula;
		this.posCostFactor	= posCostFactor;
		this.negCostFactor	= negCostFactor;
	}

	// ===== Overriders =====
	//
	@Override public float maxValueCostFactor() {
		return settingCost(maxValue);
	}
	@Override public float minValueCostFactor() {
		return settingCost(minValue);
	}
	@Override protected Integer randomize(float rand) {
		float lim1 = settingCost(maxValue);
		float lim2 = settingCost(minValue);
		if (rand > 0)
			rand *= Math.max(lim1, lim2);
		else
			rand *= -Math.min(lim1, lim2);
		return getValueFromCost(rand);
	}
	@Override protected Integer getValueFromCost(float cost) {
		int   step = Math.max(1, (maxValue - minValue) / (randCount - 1));
		Integer   bestVal = defaultValue();
		float bestDev = Math.abs(cost - settingCost(bestVal));
		float dev = Math.abs(cost - settingCost(maxValue));		
		if (dev < bestDev) {
			bestVal = maxValue;
			bestDev = dev;
		}
		for (Integer testVal=minValue; testVal<maxValue; testVal+=step) {
			dev = Math.abs(cost - settingCost(testVal));
			if (dev < bestDev) {
				bestVal = testVal;
				bestDev = dev;
			}
		}
		return bestVal;
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
	@Override public void toggle(MouseEvent e, BaseModPanel frame) {
		Integer inc = getInc(e) * getDir(e);
		if (inc == 0)
			setFromDefault();
		else
			next(getInc(e) * getDir(e));
	}
	@Override public void toggle(MouseWheelEvent e) {
		Integer inc = getInc(e) * getDir(e);
		if (inc == 0)
			setFromDefault();
		else
			next(getInc(e) * getDir(e));
	}
	@Override public float settingCost() {
		if (isSpacer() || hasNoCost())
			return 0f;;
		return settingCost(settingValue());
	}
	@Override public void setOptions() {
		if (!isSpacer() && dynOpts() != null)
			dynOpts().setInteger(getLangLabel(), settingValue());
	}
	@Override public void setOptionTools() {
		if (!isSpacer() && dynOpts() != null)
			set(dynOpts().getInteger(getLangLabel(), defaultValue()));
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isSpacer() && options != null)
			options.setInteger(getLangLabel(), settingValue());
	}
	@Override public void setOptionsTools(DynamicOptions options) {
		options(options);
		if (!isSpacer() && options != null)
			set(options.getInteger(getLangLabel(), defaultValue()));
	}
	// ===== Other Methods =====
	//
	protected void next(Integer i) {
		if (i == 0) {
			setFromDefault();
			return;
		}
		Integer value = settingValue() + i;
		if (maxValue != null && value > maxValue) {
			if (loop && minValue != null)
				set(minValue);
			else
				set(maxValue);
			return;
		}
		else if (minValue != null && value < minValue) {
			if (loop && maxValue != null)
				set(maxValue);
			else
				set(minValue);
			return;
		}
		set(value);
	}
	protected float settingCost(Integer value) {
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
		case NORMALIZED:
			rawBaseCost   = (value - defaultValue())/norm;
			useNegFormula = false;
			return rawBaseCost;
		default:
			break;
		}
		useNegFormula = false;
		return 0f;
	}
	private Integer getInc(InputEvent e) {
		if (e.isShiftDown())
			if (e.isControlDown())
				return shiftInc*ctrlInc/baseInc;
			else
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
	private static Integer stringToInteger(String string) {
		try {
			return Integer.valueOf(string.trim());
		}
		catch (NumberFormatException nfe) {
			return null; // silent error!
		}
	}
}
