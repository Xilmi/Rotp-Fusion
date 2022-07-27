
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

package mod.br.AddOns;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

import rotp.model.ships.ShipLibrary;
import rotp.ui.UserPreferences;

/**
 * @author BrokenRegistry
 * Control the ship set distribution
 */
public class ShipSetOptions {

	private static final String ORIGINAL = "Original";
	private static int maxShipSetIndex = shipSetList().size();
	private static List<String> raceList;
	private static List<String> selectedShipSetList;
	private static HashMap<String, String> raceLabel;
	private static HashMap<String, String> defaultShipSet;
	private static String playerShipSet = ORIGINAL;
	
    // ========== Initializer ==========
    //
	public static void init(List<String> raceList) { 
		raceList(raceList);
	}
    // ========== Tools ==========
    //
	/**
	 * @return Original Status
	 */
	public static boolean isOriginalShipSet() {
		return playerShipSet.equalsIgnoreCase(ORIGINAL);
	}
	/**
	 * @return ShipSet index translating Original option
	 */
	public static int realShipSetId(String original) {
		int index;
        if (isOriginalShipSet())
        	index = shipSetList().indexOf(original);
        else 
        	index = shipSetList().indexOf(playerShipSet);
        if (index == -1) index = 0;
		return index;
	}
	
    // ========== Getters ==========
    //
	/**
	 * @return the raceList
	 */
	public static List<String> raceList() {
		return raceList;
	}
	/**
	 * @return the shipSetList
	 */
	public static List<String> shipSetList() {
		return ShipLibrary.current().styles;
	}
	/**
	 * @return the shipSetOptions
	 */
	public static List<String> shipSetOptions() {
		List<String> base = shipSetList();
		base.add(ORIGINAL);
		return base;
	}
	/**
	 * @return the selectedShipSetList
	 */
	public static List<String> selectedShipSetList() {
		return selectedShipSetList;
	}
	/**
	 * @return the raceLabel Map
	 */
	public static HashMap<String, String> raceLabel() {
		return raceLabel;
	}
	/**
	 * @return the defaultShipSet Map
	 */
	public static HashMap<String, String> defaultShipSet() {
			return defaultShipSet;
	}
	/**
	 * @return the playerShipSet
	 */
	public static String playerShipSet() {
		return playerShipSet;
	}
    // ========== Setters ==========
    //
	/**
	 * @param newlist the new raceList to set
	 */
	public static void raceList(List<String> newlist) {
		raceList = newlist;
	}
	/**
	 * @param newlist the new selectedShipSetList to set
	 */
	public static void selectedShipSetList(List<String> newlist) {
		selectedShipSetList = newlist;
	}
	/**
	 * @param newMap the new raceLabel Map to set
	 */
	public static void raceLabel(HashMap<String, String> newMap) {
		raceLabel = newMap;
	}
	/**
	 * @param newMap the newdefaultShipSet Map to set
	 */
	public static void defaultShipSet(HashMap<String, String> newMap) {
		defaultShipSet = newMap;
	}
	/**
	 * @param newShipSet the playerShipSet to set
	 */
	public static void playerShipSet(String newShipSet) {
		playerShipSet = newShipSet;
	}
	/**
	 * @param newShipSet the playerShipSet to set
	 */
	public static void playerShipSet(int newShipSet) {
		if (newShipSet < 0) newShipSet = 0;
		if (newShipSet > maxShipSetIndex) newShipSet = maxShipSetIndex;
		playerShipSet = shipSetOptions().get(newShipSet);
	}
	/**
	 * @param up the toggle direction
	 * @return the new playerShipSet
	 */
	public static String togglePlayerShipSet(boolean up) {
		List<String> options = shipSetOptions();
		int index = options.indexOf(playerShipSet);
		if (up)  {
			index++;
			if (index > maxShipSetIndex) index = 0;
		} else {
			index--;
			if (index < 0) index = maxShipSetIndex;
		}
		playerShipSet = options.get(index);
		UserPreferences.save();
		return playerShipSet;
	}
	/**
	 * @param mb the mouse button
	 * @return the new playerShipSet
	 */
	public static String togglePlayerShipSet(int mb) {
		List<String> options = shipSetOptions();
		int index = options.indexOf(playerShipSet);
		switch(mb) {
		case MouseEvent.BUTTON1:
			index++;
			if (index > maxShipSetIndex) index = 0;
			break;
		case MouseEvent.BUTTON2:
			index = maxShipSetIndex;
			break;
		case MouseEvent.BUTTON3:
			index--;
			if (index < 0) index = maxShipSetIndex;
			break;
		}
		playerShipSet = options.get(index);
		UserPreferences.save();
		return playerShipSet;
	}
}
