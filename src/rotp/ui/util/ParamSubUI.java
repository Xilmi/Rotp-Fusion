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

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.LinkedList;

import rotp.model.game.DynamicOptions;
import rotp.ui.RotPUI;
import rotp.ui.game.MergedSubListUI;

public class ParamSubUI extends AbstractParam<LinkedList<LinkedList<InterfaceParam>>> {
	
	private LinkedList<InterfaceParam> optionsList = new LinkedList<>();
	private final String GUI_TITLE_ID;
	private final String GUI_ID;
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamSubUI(String gui, String name,
			LinkedList<LinkedList<InterfaceParam>> optionsMap,
			String guiTitleID, String guiID)
	{
		super(gui, name, optionsMap);
		GUI_TITLE_ID = gui + guiTitleID;
		GUI_ID = guiID;
		for (LinkedList<InterfaceParam> list : optionsMap) {
			for (InterfaceParam param : list) {
				if (param != null && !param.isTitle())
					optionsList.add(param);
			}
		}
	}
	// ===== Overriders =====
	//
	@Override public boolean isDefaultValue()	{ 
		boolean is = true;
		for (InterfaceParam param : optionsList)
			is &= param.isDefaultValue();
		return is;
	}
	@Override public void setOptions(DynamicOptions options) {
		// System.out.println("setOptions(DynamicOptions options)");		
		for (InterfaceParam param : optionsList)
			param.setOptions(options);
	}
	@Override public void setFromOptions(DynamicOptions options) {
		// System.out.println("setFromOptions(DynamicOptions options)");		
		for (InterfaceParam param : optionsList)
			param.setFromOptions(options);
	}
	@Override public void setFromDefault() {
		// System.out.println("setFromDefault()");		
		for (InterfaceParam param : optionsList)
			param.setFromDefault();
	}
	@Override public void copyOption(DynamicOptions src, DynamicOptions dest) {
		// System.out.println("next");		
		for (InterfaceParam param : optionsList)
			param.copyOption(src, dest);
	}
	@Override public void setFromCfgValue(String val) {
		// System.out.println("next");		
		for (InterfaceParam param : optionsList)
			param.setFromCfgValue(val);
	}
	@Override public void next() { 
		// System.out.println("next");		
	}
	@Override public void prev() { 
		// System.out.println("prev()");		
	}
	@Override public void toggle(MouseWheelEvent e) {
		// System.out.println("toggle(MouseWheelEvent e)");		
	}
	@Override public void toggle(MouseEvent e) {
		// System.out.println("toggle(MouseEvent e)");		
	}
	@Override public void toggle(MouseEvent e, int p) { start(p); };
	@Override public String getGuiValue()	{
		String label = isDefaultValue()? "SUB_UI_DEFAULT_YES" : "SUB_UI_DEFAULT_NO";
		return text(label);
	}
	@Override public String getCfgValue() {
		// System.out.println("getCfgValue() = " + super.getCfgValue());
		return super.getCfgValue(); }
	@Override public String getCfgLabel() { 
		// System.out.println("getCfgLabel() = " + super.getCfgLabel());
		return super.getCfgLabel(); }
	@Override public boolean isSubMenu() { return true; }
	// ===== Other Methods =====
	//
	private void start(int p) {
		MergedSubListUI ui = RotPUI.mergedSubListUI();
		ui.initUI(GUI_TITLE_ID, GUI_ID, get());
		ui.start(p);
	}
}
