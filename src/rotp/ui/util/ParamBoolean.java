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

import static rotp.ui.util.IParam.labelFormat;
import static rotp.ui.util.IParam.realLangLabel;
import static rotp.ui.util.IParam.rowFormat;
import static rotp.ui.util.IParam.rowsSeparator;
import static rotp.ui.util.IParam.tableFormat;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

public class ParamBoolean extends AbstractParam<Boolean> {
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamBoolean(String gui, String name, Boolean defaultValue) {
		super(gui, name, defaultValue);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param isValueInit Always initialized by the last value
	 */
	public ParamBoolean(String gui, String name, Boolean defaultValue, boolean isValueInit) {
		super(gui, name, defaultValue);
		isValueInit(isValueInit);
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param isValueInit Always initialized by the last value
	 * @param isCfgFile Saved in Remnant.cfg
	 */
	public ParamBoolean(String gui, String name, Boolean defaultValue, boolean isValueInit, boolean isCfgFile) {
		super(gui, name, defaultValue);
		isValueInit(isValueInit);
		isCfgFile(isCfgFile);
	}

	// ===== Overriders =====
	//
	@Override public String	getFullHelp()		{ return getHeadGuide() + getTableHelp(); }
	@Override public String	valueGuide(int id)	{ return getTableHelp(); }
	@Override public int	getIndex()			{ return get()? 1 : 0; }
	@Override public String	getCfgValue(Boolean value)		 { return yesOrNo(value); }
	@Override public void	setFromCfgValue(String newValue) { setFromCfg(yesOrNo(newValue)); }	
	@Override public String	guideValue()				{ return yesOrNo(get()); }
	@Override public String	guideDefaultValue()			{ return yesOrNo(defaultValue()); }
	@Override public boolean prev()						{ return next(); }
	@Override public boolean next()						{ set(!get()); return false; }
	@Override public boolean toggle(MouseWheelEvent e)	{ return next(); }
	@Override public boolean toggle(MouseEvent e, BaseModPanel frame) {
		if (getDir(e) == 0)
			setFromDefault(false, true);
		else
			return next();
		return false;
	}
	@Override protected Boolean getOptionValue(IGameOptions options) {
		return options.dynOpts().getBoolean(getLangLabel(), creationValue());
	}
	@Override protected void setOptionValue(IGameOptions options, Boolean value) {
		options.dynOpts().setBoolean(getLangLabel(), value);
	}
	// ===== Other Methods =====
	//
	public	boolean	toggle()				{ return next(); }
	private	String	valueHelp(boolean b)	{
		String label = getLangLabel();
		label += b ? "_YES" : "_NO";
		return realLangLabel(label);
	}
	private String	getRowHelp(boolean b)	{
		String help = valueHelp(b);
		if (help == null)
			if (b)
				help = "Enable this option.";
			else
				help = "Disable this option.";
		return rowFormat(labelFormat(yesOrNo(b)), help);
	}
	private String	getTableHelp()			{
		String rows = getRowHelp(true);
		rows += rowsSeparator() + getRowHelp(false);
		return tableFormat(rows);
	}
}
