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

public class ParamBoolean extends AbstractParam<Boolean> {
	
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamBoolean(String gui, String name, Boolean defaultValue) {
		super(gui, name, defaultValue);
	}

	@Override public String getCfgValue() {
		return yesOrNo(value);
	}
	@Override public Boolean next() {
		return setAndSave(!value); 
	}
	@Override public Boolean toggle(MouseWheelEvent e) {
		return next(); 
	}
	@Override public Boolean toggle(MouseEvent e) {
		if (getDir(e) == 0)
			return setToDefault(true);
		else
			return next();
	}
	@Override public Boolean setFromCfg(String newValue) {
		value = yesOrNo(newValue);
		return value;
	}	
	@Override public Boolean prev() { return next(); }
}
