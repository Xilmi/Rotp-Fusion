/*
 * Copyright 2015-2020 Ray Fowler
 *
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rotp.ui.util;

import static rotp.util.Base.lineSplit;
import static rotp.util.Base.textSubs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.BasePanel;
import rotp.util.LabelManager;

public interface InterfaceParam extends InterfaceOptions{
	public static final String LABEL_DESCRIPTION = "_DESC";
	public static final String LABEL_HELP		 = "_HELP";

	public static final String BASE_SPLITER	 	= lineSplit +"----------" + lineSplit;
	public static final String DEFAULT_HEADER	= BASE_SPLITER +
												"Default Value (Middle Click) = ";
	
	public static final String BODY_SEPARATOR	= lineSplit + "WITH:" + lineSplit;
	public static final String HEAD_SEPARATOR	= lineSplit + "----------" + lineSplit;
	
	public static final String HELP_SEPARATOR	= " = ";
	public static final String END = "   ";
	
	public void setFromCfgValue(String val);
	public void next();
	public void prev();
	public void toggle(MouseWheelEvent e);
	public void toggle(MouseEvent e, BasePanel frame);
	public void toggle(MouseEvent e, MouseWheelEvent w, BasePanel frame);
	public String getCfgValue();
	public String getCfgLabel();
	public String getLangageLabel();
	public String getGuiDisplay();
	public String getGuiDisplay(int idx);
	public String getGuiDescription();
	public String getDefaultValue();
	public boolean isDefaultValue();

	public default void toggle(MouseEvent e, int p, BasePanel frame) {}
	public default String	getToolTip()		{ return getGuiDescription(); }
	public default String	getToolTip(int idx)	{ return "";  }
//	public default String	getGuide()			{ return getToolTip(); }
//	public default String	getGuide(int idx)	{ return getToolTip(idx); }
	public default boolean	isDuplicate()		{ return false; }
	public default boolean	isTitle()			{ return false; }
	public default boolean	isSubMenu()			{ return false; }
	public default String[]	getModifiers()		{ return null; }
	

//	public default String	getFullHelp()		{
//		if (getToolTip().isEmpty())
//			return getGuiDescription();
//		return getToolTip();
//	};
	public default String getFullHelp()			{ return getGuide(); };
	public default String getGuide()			{
		String help = HeaderHelp();
		help += defaultValueHelp();
		help += modifierHelp();
		help += selectionHelp();
		return help;
	};
	public default String HeaderHelp()			{
		String label = getLangageLabel();
		String name  = label(label, "");
		String help  = realLabel(label+LABEL_HELP);
		if (help == null)
			help = realLabel(label+LABEL_DESCRIPTION);
		if (help == null)
			help = "Sorry, no help available yet.";
		return "Option: " + name + lineSplit
				+ "==>" + help + BASE_SPLITER;
	}
	public default String defaultValueHelp()	{
		String help = "Default Value (Middle Click) = " + getDefaultValue()
					+ BASE_SPLITER;
		return help;
	}
	public default String getValueHelp()		{ return ""; }
	public default String selectionHelp()		{
		String help = getValueHelp();
		String val = "Selected Value = " + getGuiDisplay();
		if (help.isEmpty())
			return val;
		return val + lineSplit + "--> " + help;
	}
	public default String modifierHelp()		{
		String[] mod = getModifiers();
		if (mod == null)
			return "";
		String help = "Modifiers:"	+ lineSplit
					+ "Base = " 	+ mod[0]	+ lineSplit
					+ "Shift = "	+ mod[1]	+ lineSplit
					+ "Ctrl    = " 	+ mod[2]	+ lineSplit
					+ "Ctrl + Shift = "	+ mod[3]
					+ BASE_SPLITER;
		return help;
	}
	public default String label(String key)		{
		return LabelManager.current().label(key);
	}
	public default String label(String key, String... vals) {
		String str = label(key);
		for (int i=0;i<vals.length;i++)
			str = str.replace(textSubs[i], vals[i]);
		return str;
	}
	public default String realLabel(String key) {
		return LabelManager.current().realLabel(key);
	}
}
