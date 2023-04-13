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

import static rotp.util.Base.lineSplit;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.BasePanel;

public class ParamButtonHelp extends AbstractParam<String> {
	
	private static final String[] modifiers = new String[] {
			"", "With Shift down: ", "With Ctrl down: ", "With Ctrl + Shift down: "};
	private final String[] labels;
	// ===== Constructors =====
	//
	public ParamButtonHelp(String base, String shift, String ctrl, String ctrlShift) {
		super("H", "G", "");
		labels = new String[] { base, shift, ctrl, ctrlShift };
	}
	
	private String guide(int idx) {
		String label = labels[idx];
		if (label == null || label.isEmpty())
			return "";
		String help = realLabel(label + LABEL_DESCRIPTION);
		if (help == null)
			help = "---";
		return modifiers[idx] + label(label) + lineSplit + help;
	}
	private String fullhelp(int idx) {
		String label = labels[idx];
		if (label == null || label.isEmpty())
			return "";
		String help = realLabel(label + LABEL_HELP);
		help = realLabel(label + LABEL_DESCRIPTION);
		if (help == null)
			help = "---";
		return modifiers[idx] + label(label) + lineSplit + help;
	}
	// ===== Overriders =====
	//
	@Override public String getGuide() {
		String result = "";
		String help = "";
		for(int i=0; i<4; i++) {
			help = guide(i);
			if (!help.isEmpty())
				if (result.isEmpty())
					result = help;
				else
					result += lineSplit + help;
		}
		return result;
	}
	@Override public String getFullHelp() {
		String result = "";
		String help = "";
		for(int i=0; i<4; i++) {
			help = fullhelp(i);
			if (!help.isEmpty())
				if (result.isEmpty())
					result = help;
				else
					result += lineSplit + help;
		}
		return result;
	}

	@Override public void setFromCfgValue(String val) {}
	@Override public void next() {}
	@Override public void prev() {}
	@Override public void toggle(MouseWheelEvent e) {}
	@Override public void toggle(MouseEvent e, BasePanel frame) {}
}
