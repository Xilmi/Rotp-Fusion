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

import rotp.ui.game.BaseModPanel;

public class ParamBoolInt extends ParamInteger {
	private ParamBoolean boolParam;

	// ========== constructors ==========
	//

	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultvalue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 */
	public ParamBoolInt(String gui, ParamBoolean bool, String enabled, String disabled,
			Integer defaultValue, Integer minValue, Integer maxValue) {
		super(gui, enabled, defaultValue, minValue, maxValue);
		boolParam = bool;
		specialNegative(gui+disabled);
	}

	// ===== Overriders =====
	//
	@Override public String getGuiDisplay()		{
	    String val;
	    if (isEnabled())
	    	val = langLabel(super.getLangLabel(), guideValue());
	    else
	    	val = langLabel(negativeLabel());
	    return langLabel(boolParam.getLangLabel(), val +END);
	}
	@Override public String getLangLabel()		{ return boolParam.getLangLabel(); }
	@Override public Integer set(Integer val)	{
		super.set(val);
		boolParam.set(isEnabled());
		return val;
	}
	@Override public boolean prev() {
		if (isEnabled())
			return super.prev();
		return false;
	}
	@Override public boolean next() {
		if (isEnabled())
			return super.next();
		return false;
	}
	@Override public boolean toggle(MouseEvent e, BaseModPanel frame)	{
		set(-last());
		boolParam.set(isEnabled());
		return false;
	}
	// ===== Other Public Methods =====
	//
	public boolean isDisabled() { return last() < 0;}
	public boolean isEnabled() { 
		return last() >= 0;
	}
	// ===== Other Private Methods =====
	//
}
