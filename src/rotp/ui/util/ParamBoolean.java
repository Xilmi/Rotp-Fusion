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

import static rotp.ui.util.InterfaceParam.labelFormat;
import static rotp.ui.util.InterfaceParam.realLangLabel;
import static rotp.ui.util.InterfaceParam.rowFormat;
import static rotp.ui.util.InterfaceParam.rowsSeparator;
import static rotp.ui.util.InterfaceParam.tableFormat;

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
	// ===== Overriders =====
	//
	@Override public String	getFullHelp()		{ return getHeadGuide() + getTableHelp(); }
	@Override public String	valueGuide(int id)	{ return getTableHelp(); }
	@Override public int	getIndex()			{ return get()? 1 : 0; }
	@Override public String	getCfgValue(Boolean value)		 { return yesOrNo(value); }
	@Override public void	setFromCfgValue(String newValue) { value(yesOrNo(newValue)); }	
	@Override public String	guideValue()				{ return yesOrNo(get()); }
	@Override public String	guideDefaultValue()			{ return yesOrNo(defaultValue()); }
	@Override public void	prev()						{ next(); }
	@Override public void	next()						{ set(!get()); }
	@Override public void	toggle(MouseWheelEvent e)	{ next(); }
	@Override public void	toggle(MouseEvent e, BaseModPanel frame) {
		if (getDir(e) == 0)
			setFromDefault();
		else
			next();
	}
	@Override public void setOptionTools()	{
		if (!isDuplicate() && dynOpts() != null) {
			set(dynOpts().getBoolean(getLangLabel(), creationValue()));
		}
	}
	@Override public void setOptions()			 {
		if (!isDuplicate() && dynOpts() != null)
			dynOpts().setBoolean(getLangLabel(), get());
	}
	@Override protected Boolean getOptionValue(IGameOptions options) {
		return options.dynOpts().getBoolean(getLangLabel(), creationValue());
	}
	@Override protected void setOptionValue(IGameOptions options, Boolean value) {
		options.dynOpts().setBoolean(getLangLabel(), value);
	}
	// ===== Other Methods =====
	//
	public	void	toggle()				{ next(); }
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
