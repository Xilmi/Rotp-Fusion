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

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

public class ParamString extends AbstractParam<String> {
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamString(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue);
	}
	// ===== Overriders =====
	//
	@Override public void setFromCfgValue(String newValue)	{ value(newValue); }	
	@Override public void prev() {}
	@Override public void next() {}
	@Override public void toggle(MouseWheelEvent e)	{}
	@Override public void toggle(MouseEvent e, BaseModPanel frame) {}
	@Override public void updateOptionTool() {
		if (!isDuplicate() && dynOpts() != null)
			set(dynOpts().getString(getLangLabel(), creationValue()));
	}
	@Override public void updateOption() {
		if (!isDuplicate() && dynOpts() != null)
			dynOpts().setString(getLangLabel(), get());
	}
	@Override protected String getOptionValue(IGameOptions options) {
		return options.dynOpts().getString(getLangLabel(), creationValue());
	}
	@Override protected void setOptionValue(IGameOptions options, String value) {
		options.dynOpts().setString(getLangLabel(), value);
	}
}
