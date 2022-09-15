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

import rotp.model.ships.ShipLibrary;

public class PlayerShipSet extends ParamList {
	
	private static final String ORIGINAL = "Original";

	/**
	 * @param gui  The label header
	 * @param name The name
	 */
	public PlayerShipSet(String gui, String name) {
		super(gui, name, "Original");
		for (String s : ShipLibrary.current().styles) {
			put(s, s);
		}
		put(ORIGINAL, ORIGINAL);
	}
	// ========== Public Getters ==========
	//
	/**
	 * @return Original Status
	 */
	public boolean isOriginal() {
		return get().equalsIgnoreCase(ORIGINAL);
	}
	/**
	 * @return ShipSet index translating Original option
	 */
	public int realShipSetId(String preferredShipSet) {
		int index;
		if (isOriginal())
			index = getIndex(preferredShipSet);
		else 
			index = getIndex();
		if (index == -1) index = 0;
		return index;
	}
}
