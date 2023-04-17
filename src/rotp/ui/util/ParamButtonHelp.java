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

import static rotp.ui.util.InterfaceParam.langDesc;
import static rotp.ui.util.InterfaceParam.langHelp;
import static rotp.ui.util.InterfaceParam.*;
import static rotp.util.Base.lineSplit;


//public class ParamButtonHelp extends AbstractParam<String> {
public class ParamButtonHelp implements InterfaceParam {
	
//	private static final String[] modifiers = new String[] {
//			"", "Shift is down", "Ctrl is down", "Ctrl and Shift are down"};
	private static final String[] modifiers = new String[] {
			"", "(Shift) ", "(Ctrl) ", "(Ctrl)+(Shift) "};
	private final String[]	labels;
	private final String	name;
	// ===== Constructors =====
	//
	public ParamButtonHelp(String label, String base, String shift, String ctrl, String ctrlShift) {
		//super("", label, "");
		labels	= new String[] { base, shift, ctrl, ctrlShift };
		name	= label;
	}
	private String helpLine(int idx, int html) {
		String help, line;
		String label = labels[idx];
		if (label == null || label.isEmpty())
			return "";
		if (html == 1)
			help = langDesc(label);
		else
			help = langHelp(label);
		if (help.isEmpty())
			help = "---";
		if (html == 0) {
			line = modifiers[idx];
			line += "[" + langName(label) + "]";
			line += lineSplit + help;
			return line;
		}
		line = htmlTuneFont(-2, modifiers[idx]);
		line += "&nbsp <b>( "+ langName(label) + " )</b>";
		line += lineSplit + help;
		return line;
	}
	private String buildHelp(int html) {
		String result = "";
		String help = "";
		for(int i=0; i<4; i++) {
			help = helpLine(i, html);
			if (!help.isEmpty())
				if (result.isEmpty())
					result = help;
				else if (html == 0)
					result += lineSplit + lineSplit + help;
				else
					result += baseSeparator() + help;
		}
		return result;
	}

	// ===== Overriders =====
	//
	@Override public String getGuide()		{ return buildHelp(1); } // html
	@Override public String getFullHelp()	{ return buildHelp(2); } // html
	@Override public String getHelp()		{ return buildHelp(0); } // Full for old Help
	@Override public String getCfgLabel()	{ return name; }
	@Override public String getLangLabel()	{ return name; }
}
