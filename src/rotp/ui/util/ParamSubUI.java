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

import static rotp.ui.util.IParam.langLabel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;
import rotp.ui.game.CompactOptionsUI;


public class ParamSubUI extends AbstractParam<LinkedList<LinkedList<IParam>>> {
	
	private final String GUI_TITLE_ID;
	private final String GUI_ID;
	public  final LinkedList<LinkedList<IParam>> optionsMap;
	public  final LinkedList<IParam> optionsList = new LinkedList<>();
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param optionsMap Full map of options
	 * @param guiTitleID Label for the GUI Title
	 * @param guiID Unique GUI ID for load and save
	 */
	public ParamSubUI(String gui, String name,
			LinkedList<LinkedList<IParam>> optionsMap,
			String guiTitleID, String guiID)
	{
		super(gui, name, optionsMap);
		GUI_TITLE_ID = gui + guiTitleID;
		GUI_ID = guiID;
		this.optionsMap = optionsMap;
		for (LinkedList<IParam> list : optionsMap) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					optionsList.add(param);
			}
		}
	}
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param optionsMap Full map of options
	 * @param guiTitleID Label for the GUI Title
	 * @param guiID Unique GUI ID for load and save
	 */
	public ParamSubUI(String gui, String guiId,
			LinkedList<LinkedList<IParam>> optionsMap) {
		this(gui, guiId+"_UI", optionsMap, guiId+"_TITLE", guiId);
	}
	// ===== Overriders =====
	//
	@Override public ParamSubUI isValueInit(boolean is) { super.isValueInit(is) ; return this; }
	@Override public ParamSubUI isDuplicate(boolean is) { super.isDuplicate(is) ; return this; }
	@Override public ParamSubUI isCfgFile(boolean is)	{ super.isCfgFile(is)   ; return this; }
	@Override public boolean isDefaultValue()	{ 
		boolean is = true;
		for (IParam param : optionsList)
			if (param != null)
				is &= param.isDefaultValue();
		return is;
	}
	@Override public void copyOption(IGameOptions src, IGameOptions dest, boolean updateTool) {
		super.copyOption(src, dest, updateTool);
		for (IParam param : optionsList)
			if (param != null && !param.isCfgFile())
				param.copyOption(src, dest, updateTool);
	}
	@Override public void updateOptionTool() {
		super.updateOptionTool();
		for (IParam param : optionsList)
			if (param != null)
				param.updateOptionTool();
	}
	@Override public void setFromDefault(boolean excludeCfg, boolean excludeSubMenu) {
		super.setFromDefault(excludeCfg, excludeSubMenu);
		for (IParam param : optionsList)
			if (param != null)
				if (!(excludeCfg && param.isCfgFile())
						&& !(excludeSubMenu && param.isSubMenu()))
					param.setFromDefault(excludeCfg, excludeSubMenu);
	}
	@Override protected LinkedList<LinkedList<IParam>> getOptionValue(
			IGameOptions options) {
		return last();
	}
	@Override protected void setOptionValue(IGameOptions options,
			LinkedList<LinkedList<IParam>> value) {}
	@Override public void setFromCfgValue(String val) {
		for (IParam param : optionsList)
			if (param != null && !param.isCfgFile())
				param.setFromCfgValue(val);
	}
	@Override public boolean next() { return false; }
	@Override public boolean prev() { return false; }
	@Override public boolean toggle(MouseWheelEvent e) { return false; }
	@Override public boolean toggle(MouseEvent e, BaseModPanel frame) { return false; }
	@Override public boolean toggle(MouseEvent e, String p, BaseModPanel pUI) {
		updated(true);
		CompactOptionsUI ui = new CompactOptionsUI(GUI_TITLE_ID, GUI_ID, optionsMap);
		ui.start(p, pUI);
		return false;
	};
	@Override public String guideValue()	{
		String label = isDefaultValue()? "SUB_UI_DEFAULT_YES" : "SUB_UI_DEFAULT_NO";
		return langLabel(label);
	}
	@Override public String getCfgValue()	{ return super.getCfgValue(); }
	@Override public String getCfgLabel()	{ return super.getCfgLabel(); }
	@Override public boolean isSubMenu()	{ return true; }
	@Override public String getHeadGuide()	{ return headerHelp(true); }

	// ===== Other Methods =====
	//
	public String titleId() { return GUI_TITLE_ID; }
	public LinkedList<IParam> optionsList() { return optionsList; }
	public void updateList() {
		optionsList.clear();
		for (LinkedList<IParam> list : optionsMap) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					optionsList.add(param);
			}
		}
	}
	public void newMap(LinkedList<LinkedList<IParam>> newMap) {
		optionsMap.clear();
		optionsMap.addAll(newMap);
		updateList();
	}
}
