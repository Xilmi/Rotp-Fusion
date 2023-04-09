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

import rotp.model.game.DynamicOptions;
import rotp.ui.BasePanel;

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
	@Override protected	  String getCfgValue(Boolean value)	{ return yesOrNo(value); }
	@Override public void setFromCfgValue(String newValue)	{ value(yesOrNo(newValue)); }	
	@Override public 	  String getGuiValue()	{ return yesOrNo(get()); }
	@Override public void prev() { next(); }
	@Override public void next() { set(!get()); }
	@Override public void toggle(MouseWheelEvent e)	{ next(); }
	@Override public void toggle(MouseEvent e, BasePanel frame) {
		if (getDir(e) == 0)
			setFromDefault();
		else
			next();
	}
	@Override public void setFromOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
			set(options.getBoolean(labelId(), defaultValue()));
	}
	@Override public void setOptions(DynamicOptions options) {
		if (!isDuplicate() && options != null)
			options.setBoolean(labelId(), get());
	}
	@Override public void copyOption(DynamicOptions src, DynamicOptions dest) {
		if (!isDuplicate() && src != null && dest != null)
			dest.setBoolean(labelId(), src.getBoolean(labelId(), defaultValue()));
	}
	// ===== Other Methods =====
	//
	public void toggle() { next(); }
}
