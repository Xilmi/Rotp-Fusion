
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import rotp.ui.UserPreferences;

/**
 * @author BrokenRegistry
 * Control the race allowed when randomly selected
 */
public class RaceFilter {

	private static List<String> newRacesOnList;
	private static List<String> newRacesOffList = new ArrayList<String>();
	private static List<String> defaultRaceList;
	private static List<String> selectedGameRaceFilter;
	private static List<String> selectedGuiRaceFilter;
	private static String[]     startOpponentRace;

	private static List<String> defaultGameAIList; // = all Random
	private static List<String> defaultGuiAIList;
	private static List<String> randomAIList;
	private static List<String> allRandomAIList;
	private static List<String> selectedGameAIFilter;
	private static List<String> selectedGuiAIFilter;
	private static String[]     startOpponentAI;

    // ========== Setters ==========
    //
	/**
	 * @param newRaceList the new  newRaceOn List to set
	 */
	public static void newRacesOnList(List<String> newRaceList) {
		newRacesOnList = newRaceList;
	}
	/**
	 * @param newRaceList the new newRacesOff List to set
	 */
	public static void newRacesOffList(List<String> newRaceList) {
		newRacesOffList = newRaceList;
	}
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
	/**
	 * @param newAIList the new default Game AIList to set
	 */
	public static void defaultGameAIList(List<String> newAIList) {
		defaultGameAIList = newAIList;
		allRandomAIList = newAIList.subList(0, newAIList.size()-2);
		randomAIList = newAIList.subList(0, newAIList.size()-3);
	}
	/**
	 * @param newAIList the new default Gui AIList to set
	 */
	public static void defaultGuiAIList(List<String> newAIList) {
		defaultGuiAIList = newAIList;
	}
	/**
	 * @param newAIList the AI List to set
	 */
	public static void selectedGameAIFilter(List<String> newAIList) {
		selectedGameAIFilter = newAIList;
	}
	/**
	 * @param newAIList the AI Filter to set
	 */
	public static void selectedGuiAIFilter(List<String> newAIList) {
		selectedGuiAIFilter = newAIList;
	}
	/**
	 * @param newAIList the Preset Opponents to set
	 */
	public static void startOpponentAI(String[] newAIList) {
		startOpponentAI = newAIList;
	}
    // ========== Getters ==========
    //
	/**
	 * @return the newRacesOff List
	 */
	public static List<String> newRacesOnList() {
		return newRacesOnList;
	}
	/**
	 * @return the newRacesOff List
	 */
	public static List<String> newRacesOffList() {
		return newRacesOffList;
	}
	/**
	 * @return the Default Race List
	 */
	public static List<String> defaultRaceList() {
			return raceFilter(defaultRaceList);
	}
	/**
	 * @return the race List
	 */
	public static List<String> selectedGameRaceFilter() {
		if (selectedGameRaceFilter == null 
				|| selectedGameRaceFilter.toString().isBlank()) {
			return defaultRaceList();
		}
		return raceFilter(selectedGameRaceFilter);
	}
	/**
	 * @return the Race Filter
	 */
	public static List<String> selectedGuiRaceFilter() {
		if (selectedGuiRaceFilter == null 
				|| selectedGuiRaceFilter.toString().isBlank()) {
			return defaultRaceList();
		}
		return raceFilter(selectedGuiRaceFilter);
	}
	/**
	 * @return the Preset Opponents
	 */
	public static String[] startOpponentRace() {
		return startOpponentRace;
	}
	/**
	 * @return the Default Game AI List
	 */
	public static List<String> defaultGameAIList() {
		return defaultGameAIList;
	}
	/**
	 * @return the Default Gui AI List
	 */
	public static List<String> defaultGuiAIList() {
		return defaultGuiAIList;
	}
	/**
	 * @return the random AI List
	 */
	public static List<String> randomAIList() {
		return randomAIList;
	}
	/**
	 * @return the all random AI List
	 */
	public static List<String> allRandomAIList() {
		return allRandomAIList;
	}
	/**
	 * @return the race List
	 */
	public static List<String> selectedGameAIFilter() {
		if (selectedGameAIFilter == null 
				|| selectedGameAIFilter.toString().isBlank()) {
			return defaultGameAIList;
		}
		return selectedGameAIFilter;
	}
	/**
	 * @return the AI Filter
	 */
	public static List<String> selectedGuiAIFilter() {
		if (selectedGuiAIFilter == null 
				|| selectedGuiAIFilter.toString().isBlank()) {
			return defaultGuiAIList;
		}
		return selectedGuiAIFilter;
	}
	/**
	 * @return the Preset Opponents AI
	 */
	public static String[] startOpponentAI() {
		return startOpponentAI;
	}
    // ========== Other Methods ==========
    //
	/**
	 * @param raceList the List to filter
	 * @return The filtered list in conformity with UserPreferences
	 */
	public static List<String> raceFilter (List<String> raceList) {
		if (UserPreferences.newRacesOn()) {
			return raceList;
		}
		return raceList.stream()
				.filter(newRacesOffList::contains)
				.collect(Collectors.toList());
	}
}
