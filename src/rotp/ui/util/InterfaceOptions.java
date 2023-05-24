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

import rotp.model.game.DynamicOptions;
import rotp.model.game.IGameOptions;

public interface InterfaceOptions {
	/**
	 * Set the option tools value from full dynamic options. 
	 * @param options
	 */
	public void setOptionLinks(IGameOptions options);
	/**
	 * Set the option tools value from specific options. 
	 * @param options
	 */
	public void setOptionTools();
	public void setOptions();
	// For settings only
	public default void setOptionsTools(DynamicOptions options) {};
	// For settings only
	public default void setOptions(DynamicOptions options) {};
	public default void setFromDefault() {}
	public default void copyOption(IGameOptions src, IGameOptions dest) {}
}
