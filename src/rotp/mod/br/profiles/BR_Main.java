
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

package rotp.mod.br.profiles;

import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * Access point for BrokenRegistry Mods
 *   - Profile Manager
 *   - Galaxy Options
 */
public class BR_Main {

	/**
	 * @param options The initial {@code IGameOptions} GUI option
	 */
	public static void initBR(IGameOptions options) {
		Profiles.initUserSettings(options);
	}
}
