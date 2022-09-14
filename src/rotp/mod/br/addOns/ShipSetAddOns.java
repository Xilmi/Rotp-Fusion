
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

package rotp.mod.br.addOns;

import java.util.List;

import mod.br.addOns.ShipSetOptions;

/**
 * @author BrokenRegistry
 * Control the ship set distribution
 */
public class ShipSetAddOns {
	
	/**
	 * @param newlist the new raceList to set
	 */
	public static void setToDefault() {
		ShipSetOptions.setToDefault();
	}
	/**
	 * @return ShipSet index translating Original option
	 */
	public static int realShipSetId(String original) {
		return ShipSetOptions.realShipSetId(original);
	}
	/**
	 * @return Original Status
	 */
	public static boolean isOriginalShipSet() {
		return ShipSetOptions.isOriginalShipSet();
	}
	/**
	 * @return the shipSetOptions
	 */
	public static List<String> shipSetOptions() {
		return ShipSetOptions.shipSetOptions();
	}
	/**
	 * @return the playerShipSet
	 */
	public static String playerShipSet() {
		return ShipSetOptions.playerShipSet();
	}
	/**
	 * @param newShipSet the playerShipSet to set
	 */
	public static void playerShipSet(String newShipSet) {
		ShipSetOptions.playerShipSet(newShipSet);
	}
	/**
	 * @param newShipSet the playerShipSet to set
	 */
	public static void playerShipSet(int newShipSet) {
		ShipSetOptions.playerShipSet(newShipSet);
	}
	/**
	 * @param up the toggle direction
	 * @return the new playerShipSet
	 */
	public static String togglePlayerShipSet(boolean up) {
		return ShipSetOptions.togglePlayerShipSet(up);
	}
	/**
	 * @param up the toggle direction
	 * @return the new playerShipSet
	 */
	public static String togglePlayerShipSet(int mb) {
		return ShipSetOptions.togglePlayerShipSet(mb);
	}
}
