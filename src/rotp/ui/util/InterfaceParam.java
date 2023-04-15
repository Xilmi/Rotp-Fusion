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

import rotp.ui.game.BaseModPanel;
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
	public void toggle(MouseEvent e, BaseModPanel frame);
	public void toggle(MouseEvent e, MouseWheelEvent w, BaseModPanel frame);
	public String getCfgValue();
	public String getCfgLabel();
	public String getLangageLabel();
	public String getGuiValue(); // Only the value, (player view)
	public String getGuiDisplay(); // Name, value, ... and more
	public String getGuiDisplay(int idx);
	public String getGuiDescription();
	public String getDefaultValue();
	public boolean isDefaultValue();

	public default void toggle(MouseEvent e, int p, BaseModPanel frame) {}
	public default String	getToolTip()		{ return getGuiDescription(); }
	public default String	getToolTip(int idx)	{ return "";  }
//	public default String	getGuide()			{ return getToolTip(); }
//	public default String	getGuide(int idx)	{ return getToolTip(idx); }
	public default boolean	isDuplicate()		{ return false; }
	public default boolean	isTitle()			{ return false; }
	public default boolean	isSubMenu()			{ return false; }
	public default String[]	getModifiers()		{ return null; }
	
	// Limited size for toolTip boxes
	public default String	getDescription()		{
		if (getToolTip().isEmpty())
			return getGuiDescription();
		return getToolTip();
	};
	// Bigger Description for auto pop up help (guide)
	public default String getGuide()			{
		String help = HeaderHelp();
		help += defaultValueHelp();
		help += modifierHelp();
		help += selectionHelp();
		return help;
	};
	// Full help for "F1" requests
	public default String getFullHelp()			{ return getGuide(); };

	// ===== Local Help and guide Tools =====
	public default String HeaderHelp()			{
		String label = getLangageLabel();
		String name  = langLabel(label, "");
		String help  = langHelp(label);
		if (help.isEmpty())
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
		String val = "Selected Value = " + getGuiValue();
		if (help.isEmpty())
			return val;
		return val + BASE_SPLITER + help;
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
	// ===== Lower level language tools =====
	public static String langName(String key)		{
		if (key == null)
			return "";
		String name = realLangLabel(key);
//		name = langLabel(key); // TODO BR: For debug... comment!
		if (name == null)
			return "";
		return name.split("%1")[0];
	}
	public static String langDesc(String key)		{
		if (key == null)
			return "";
		String desc =  realLangLabel(key+LABEL_DESCRIPTION);
//		desc = langLabel(key+LABEL_DESCRIPTION); // TODO BR: For debug... comment!
		if (desc == null)
			return "";
		return desc;
	}
	public static String langHelp(String key)		{
		if (key == null)
			return "";
		String help =  realLangLabel(key+LABEL_HELP);
		if (help == null)
			return langDesc(key);
		return help;
	}
	public static String langLabel(String key)		{
		if (key == null)
			return "";
		return LabelManager.current().label(key);
	}
	public static String langLabel(String key, String... vals) {
		if (key == null)
			return "";
		String str = langLabel(key);
		for (int i=0;i<vals.length;i++)
			str = str.replace(textSubs[i], vals[i]);
		return str;
	}
	public static String realLangLabel(String key) {
		if (key == null)
			return "";
		return LabelManager.current().realLabel(key);
	}
}
