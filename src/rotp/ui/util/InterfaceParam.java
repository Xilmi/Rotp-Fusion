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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface InterfaceParam extends InterfaceOptions{
	public static final String HELP_DESCRIPTION = "_HELP";
	public static final String LABEL_DESCRIPTION = "_DESC";
	public static final String END = "   ";
	
	public void setFromCfgValue(String val);
	public void next();
	public void prev();
	public void toggle(MouseWheelEvent e);
	public void toggle(MouseEvent e, Component frame);
	public void toggle(MouseEvent e, MouseWheelEvent w, Component frame);
	public String getCfgValue();
	public String getCfgLabel();
	public String getGuiDisplay();
	public String getGuiDisplay(int idx);
	public String getGuiDescription();
	public boolean isDefaultValue();

	public default void toggle(MouseEvent e, int p, Component frame) {};
	public default String	getToolTip()		{ return getGuiDescription(); }
	public default String	getToolTip(int idx)	{ return ""; }
	public default boolean	isDuplicate()		{ return false; }
	public default boolean	isTitle()			{ return false; }
	public default boolean	isSubMenu()			{ return false; };
	public default String	getFullHelp()		{
		if (getToolTip().isEmpty())
			return getGuiDescription();
		return getToolTip();
	};
}
