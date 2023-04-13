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

import rotp.model.game.DynamicOptions;

public class SettingBoolean extends SettingBase<Boolean> {
	
	private static final boolean defaultIsList			= true;
	private static final boolean defaultIsBullet		= false;
	private static final boolean defaultLabelsAreFinals	= true;
	private static final String  defaultBooleanYes		= "BOOLEAN_YES";
	private static final String  defaultBooleanNo		= "BOOLEAN_NO";

	// ========== constructors ==========
	//
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue The default value
	 * @param costTrue 
	 * @param costFalse 
	 */
	public SettingBoolean(String guiLangLabel, String nameLangLabel, Boolean defaultValue) {
		this(guiLangLabel, nameLangLabel, defaultValue, 0f, 0f,
				defaultBooleanYes, defaultBooleanNo,
				defaultIsBullet, defaultLabelsAreFinals);
		hasNoCost(true);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue The default value
	 * @param costTrue 
	 * @param costFalse 
	 */
	public SettingBoolean(String guiLangLabel, String nameLangLabel, Boolean defaultValue,
			float costTrue, float costFalse) {
		this(guiLangLabel, nameLangLabel, defaultValue, costTrue, costFalse,
				defaultBooleanYes, defaultBooleanNo,
				defaultIsBullet, defaultLabelsAreFinals);
	}
	/**
	 * @param guiLangLabel  The label header
	 * @param nameLangLabel The nameLangLabel
	 * @param defaultvalue The default value
	 * @param costTrue 
	 * @param costFalse 
	 * @param langLabelYes
	 * @param langLabelNo
	 * @param isBullet		To be displayed as bullet list
	 * @param labelsAreFinals when false: Labels are combined withName and Gui Label
	 */
	private SettingBoolean(String guiLangLabel, String nameLangLabel, Boolean defaultValue,
			float costTrue, float costFalse, String langLabelYes, String langLabelNo,
			boolean isBullet, boolean labelsAreFinals) {
		super(guiLangLabel, nameLangLabel, defaultValue, defaultIsList, isBullet, labelsAreFinals);
		put("No",  langLabelNo,  costFalse, false);
		put("Yes", langLabelYes, costTrue,  true);		
	}
	// ===== Overriders =====
	//
	@Override public String guiSettingValue() {
		return lmText(guiOptionLabel());
	}
	@Override public String guiOptionValue(int index) {
		return lmText(guiOptionLabel(index));
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isSpacer() && options != null)
			options.setBoolean(getLangageLabel(), settingValue());
	}
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isSpacer() && options != null)
			set(options.getBoolean(getLangageLabel(), defaultValue()));
	}
}
