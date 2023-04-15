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

import static rotp.ui.util.InterfaceParam.*;
import static rotp.util.Base.lineSplit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.game.BaseModPanel;


public class ParamButtonHelp extends AbstractParam<String> {
	
	private static final String[] modifiers = new String[] {
			"", "Shift is down", "Ctrl is down", "Ctrl and Shift are down"};
	private final String[] labels;
	// ===== Constructors =====
	//
	public ParamButtonHelp(String label, String base, String shift, String ctrl, String ctrlShift) {
		super("", label, "");
		labels = new String[] { base, shift, ctrl, ctrlShift };
	}
	
	private String helpLine(int idx, boolean full) {
		String help, line;
		String label = labels[idx];
		if (label == null || label.isEmpty())
			return "";
		if (full)
			help = langHelp(label);
		else
			help = langDesc(label);
		if (help.isEmpty())
			help = "---";
		line = " [" + langName(label) + "]";
		if (idx > 0)
			line += " - when " + modifiers[idx];
		line += lineSplit + help;
		return line;
//		return "==> " + modifiers[idx] + " [" + langName(label) + "]" + lineSplit + help;
	}
	private String buildHelp(boolean full) {
		String result = "";
		String help = "";
		for(int i=0; i<4; i++) {
			help = helpLine(i, full);
			if (!help.isEmpty())
				if (result.isEmpty())
					result = help;
				else
					result += lineSplit + lineSplit + help;
		}
		return result;
	}

	// ===== Overriders =====
	//
	@Override public String getGuide()		{ return buildHelp(false); }
	@Override public String getFullHelp()	{ return buildHelp(true); }
	
	@Override public void setFromCfgValue(String val) {}
	@Override public void next() {}
	@Override public void prev() {}
	@Override public void toggle(MouseWheelEvent e) {}
	@Override public void toggle(MouseEvent e, BaseModPanel frame) {}
}
