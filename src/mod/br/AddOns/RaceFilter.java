
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

import java.util.List;

/**
 * @author BrokenRegistry
 * Control the race allowed when randomly selected
 */
public class RaceFilter {

	private static List<String> defaultRaceList;
	private static List<String> selectedGameRaceFilter;
	private static List<String> selectedGuiRaceFilter;
	private static String[]     startOpponentRace;

    // ========== Setters ==========
    //
	/**
	 * @param newRaceList the new default raceList to set
	 */
	public static void defaultRaceList(List<String> newRaceList) {
		defaultRaceList = newRaceList;
	}
	/**
	 * @param newRaceList the Race List to set
	 */
	public static void selectedGameRaceFilter(List<String> newRaceList) {
		selectedGameRaceFilter = newRaceList;
	}
	/**
	 * @param newRaceList the Race Filter to set
	 */
	public static void selectedGuiRaceFilter(List<String> newRaceList) {
		selectedGuiRaceFilter = newRaceList;
	}
	/**
	 * @param newRaceList the Preset Opponents to set
	 */
	public static void startOpponentRace(String[] newRaceList) {
		startOpponentRace = newRaceList;
	}
    // ========== Getters ==========
    //
	/**
	 * @return the Default Race List
	 */
	public static List<String> defaultRaceList() {
		return defaultRaceList;
	}
	/**
	 * @return the race List
	 */
	public static List<String> selectedGameRaceFilter() {
		if (selectedGameRaceFilter == null 
				|| selectedGameRaceFilter.toString().isBlank()) {
			return defaultRaceList;
		}
		return selectedGameRaceFilter;
	}
	/**
	 * @return the Race Filter
	 */
	public static List<String> selectedGuiRaceFilter() {
		if (selectedGuiRaceFilter == null 
				|| selectedGuiRaceFilter.toString().isBlank()) {
			return defaultRaceList;
		}
		return selectedGuiRaceFilter;
	}
	/**
	 * @return the Preset Opponents
	 */
	public static String[] startOpponentRace() {
		return startOpponentRace;
	}
    // ========== Other Methods ==========
    //
}
