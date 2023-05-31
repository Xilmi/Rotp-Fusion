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
import java.io.Serializable;

import rotp.model.game.IGameOptions;
import rotp.ui.game.BaseModPanel;

class ParamObject extends AbstractParam<Serializable> {
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	ParamObject(String gui, String name, Serializable defaultValue) {
		super(gui, name, defaultValue);
	}
	// ===== Overriders =====
	//
	@Override public void setFromCfgValue(String newValue)	{ setFromCfg(newValue); }	
	@Override public void prev() {}
	@Override public void next() {}
	@Override public void toggle(MouseWheelEvent e)	{}
	@Override public void toggle(MouseEvent e, BaseModPanel frame) {}
	@Override protected Serializable getOptionValue(IGameOptions options) {
		return options.dynOpts().getObject(getLangLabel(), creationValue());
	}
	@Override protected void setOptionValue(IGameOptions options, Serializable value) {
		options.dynOpts().setObject(getLangLabel(), value);
	}
}
