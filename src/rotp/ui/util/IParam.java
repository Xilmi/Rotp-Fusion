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

import static rotp.model.game.IDebugOptions.showVIPPanel;
import static rotp.ui.game.BaseModPanel.guideFontSize;
import static rotp.util.Base.NEWLINE;
import static rotp.util.Base.textSubs;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.RotPUI;
import rotp.ui.game.BaseModPanel;
import rotp.util.LabelManager;

public interface IParam extends InterfaceOptions{
	public static final String LABEL_DESCRIPTION = "_DESC";
	public static final String LABEL_HELP		 = "_HELP";
	public static final String END				 = "   ";
	public static final int INIT_DEPENDENCIES	 = 0;
	public static final int VALID_DEPENDENCIES	 = 1;

	/**
	 * To be used after starting RotP or loading options
	 * @param level: "0" for list initialization, "1" for value validation
	 */
	public default void initDependencies(int level)	{}
	// user input
	public default boolean next() { return false; } // Return forceUpdate
	public default boolean prev() { return false; } // Return forceUpdate
	public default boolean toggle(MouseWheelEvent e) { return false; } // Return forceUpdate
	public default boolean toggle(MouseEvent e, BaseModPanel frame) { return false; } // Return forceUpdate
	public default boolean toggle(MouseEvent e, MouseWheelEvent w, BaseModPanel frame) { return false; } // Return forceUpdate
	public default boolean toggle(MouseEvent e, String p, BaseModPanel frame) { return false; } // Return forceUpdate
	public default boolean toggle(MouseEvent e, String p, BaseModPanel pUI, BaseModPanel frame) { return false; } // Return forceUpdate
	public default void	updated(boolean updated)	{}
	// State
	public default boolean	isDuplicate()			{ return false; }
	public default boolean	isCfgFile()				{ return false; }
	public default boolean	isTitle()				{ return false; }
	public default boolean	isSubMenu()				{ return false; }
	public default boolean	isDefaultValue()		{ return false; }
	/**
	 * To check if the currently set value is still locally valid
	 */
	public default boolean	isValidValue()			{ return true; }
	public default boolean	isActive()				{ return true; }
	public default boolean	updated()				{ return true; }
	// Display
	public default void setFromCfgValue(String val) {}
	public default String	getCfgValue()			{ return ""; }
	public default String	getCfgLabel()			{ return ""; }
	public default String	getGuiDisplay()			{ return ""; }// Name, value, ... and more
	public default String	getGuiDisplay(int id)	{ return ""; }
	public default String	getGuiDescription()		{ return ""; }
	public default String	guideValue()			{ return ""; }// Only the value, (player view)
	public default String	guideDefaultValue()		{ return ""; }
	public default int		getIndex()				{ return -1; }
	public default String	getToolTip()			{ return getGuiDescription(); }
	public default String	getToolTip(int id)		{ return "";  }
	public default String   getGuiValue(int id)		{ return guideValue(); }
	public default String	getLangLabel()			{ return ""; }
	public default String	getLangLabel(int id)	{ return ""; }
	public default String[]	getModifiers()			{ return null; }
	public default void drawBox(Graphics2D g, int x0, int y0, int w, int h, int indent, int blankW) {
		int x1 = x0+w;
		g.drawLine(x0, y0, x0+indent, y0);
		g.drawLine(x0+indent+blankW, y0, x1, y0);
		if (h>0) {
			int y1 = y0+h;
			g.drawLine(x0, y0, x0, y1);			
			g.drawLine(x0, y1, x1, y1);			
			g.drawLine(x1, y0, x1, y1);			
		}
	}

	// Limited size for toolTip boxes
	public default String getDescription()		{
		if (getToolTip().isEmpty())
			return getGuiDescription();
		return getToolTip();
	};
	// Bigger Description for auto pop up help (guide)
	public default String getHeadGuide()		{
		String help = headerHelp(true);
		help += defaultValueHelp();
		help += modifierHelp();
		return help;
	}
	public default String getGuide(int id)		{ return getHeadGuide() + valueGuide(id); };
	public default String getGuide()			{ return getHeadGuide() + selectionGuide(); }
	// Full help for "F1" requests
	public default String getFullHelp()			{ return getGuide(); };
	public default String getHelp()				{ return getDescription(); };

	// ===== Local Help and guide Tools =====
	public default String headerHelp(String label, boolean sep)	{
		String name  = langLabel(label, "");
		String help  = langHelp(label);
		if (help.isEmpty())
			help = "<b><i>Sorry, no help available yet.<i/></b>";
		help = "<u><b>" + name + "</b></u>" + NEWLINE + help;
		if (sep)
			return help + baseSeparator();
		else
			return help;
	}
	public default String headerHelp(boolean sep)	{ return headerHelp(getLangLabel(), sep); }
//	public default String headerHelp()			{ return headerHelp(getLangLabel(), true); }
	public default String defaultValueHelp()	{
		String help = labelFormat("Default Value") + guideDefaultValue();
		if (!showVIPPanel.get())
			help += htmlTuneFont(-2, "&emsp<i>(set with Middle Click)<i/>");
		help += baseSeparator();
		return help;
	}
	// The value in help format
	public default String getSelectionStr()		{ return labelFormat(guideValue()); }
	public default String getValueStr(int id)	{
		if (id<0)
			return "";
		 return labelFormat(getGuiValue(id));
	}
	public default String getRowGuide(int id)	{
		if (id<0)
			return "";
		String help = realHelp(id);
		if (help == null)
			help = realDescription(id);
		if (help == null)
			help = "";
		return rowFormat(labelFormat(name(id)), help);
	}
	public default String valueGuide(int id)	{ return "";}
	public default String selectionGuide()		{
		String val  = labelFormat("Selected Value") + guideValue();
		if (getIndex() < 0) // not a list
			return val;
		// this is a list
		String help = valueGuide(getIndex());
		return val + baseSeparator() + help;
	}
	public default String modifierHelp()		{
		if (showVIPPanel.get())
			return "";
		String[] mod = getModifiers();
		if (mod == null)
			return "";
		String help = labelFormat("Key Modifiers")	+ NEWLINE
					+ "None "		 + mod[0] + " &nbsp : &nbsp"
					+ " Shift "		 + mod[1] + " &nbsp : &nbsp"
					+ " Ctrl "		 + mod[2] + " &nbsp : &nbsp"
					+ " Ctrl+Shift " + mod[3]
					+ baseSeparator();
		return help;
	}
	// ===== Upper level language tools =====
	public default String name(int id)				{
		if (id<0)
			return "";
		return langName(getLangLabel(id));
	}
	public default String realDescription(int id)	{
		if (id<0)
			return "";
		return langDesc(getLangLabel(id));
	}
	public default String realHelp(int id)			{
		if (id<0)
			return "";
		return langHelp(getLangLabel(id));
	}
	// ===== Formatters =====
	public static String tableFormat(String str)	{ return str; }
	public static String rowFormat(String... strA)	{
		String row = "";
		for (String str : strA)
			row += str + "&emsp ";
		return row;
	}
	public static String htmlTuneFont(int deltaSize, String str)	{
		int newSize = RotPUI.scaledSize(guideFontSize + deltaSize);
		String head = "<span style=\"font-size:" + newSize + ".0pt\">";
		return head + str + "</span>";
	}
	public static String getSeparator(int top, int thick, int down, String color)	{
		String sOpen	= "<div style=\" height: ";
		String sMid		= "px; font-size:0";
		String sClose	= "; \"></div>";
		String sColor	= "; background:#";
		return    sOpen + top	+ sMid + sClose
				+ sOpen + thick + sMid + sColor + color + sClose
				+ sOpen + down	+ sMid + sClose;
	}
	public static String baseSeparator()			{ return getSeparator(5, 2, 3, "7f7f7f"); }
	public static String rowsSeparator()			{ return getSeparator(4, 1, 2, "9F9F9F"); }
	public static String labelFormat(String str)	{
		if (str.isEmpty())
			return str;
		return "<b>" + str + ":</b>&nbsp ";
	}
	// ===== Lower level language tools =====
	public static String langName(String key)		{
		if (key == null)
			return "";
		String name = realLangLabel(key);
//		name = langLabel(key); // TODO BR: For debug... comment! or not
		if (name == null)
			return "";
		return name.split("%1")[0];
	}
	public static String langDesc(String key)		{
		if (key == null)
			return "";
//		System.out.println("langDesc: key+LABEL_DESCRIPTION = " + key+LABEL_DESCRIPTION);
		String desc = realLangLabel(key+LABEL_DESCRIPTION);
//		desc = langLabel(key+LABEL_DESCRIPTION); // TODO BR: For debug... comment!
		if (desc == null)
			return "";
//		System.out.println("desc = " + desc);
		return desc;
	}
	public static String langHelp(String key)		{
		if (key == null)
			return "";
//		System.out.println("langHelp: key+LABEL_HELP = " + key+LABEL_HELP);
		String help = realLangLabel(key+LABEL_HELP);
		if (help == null)
			return langDesc(key);
//		System.out.println("help = " + help);
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
