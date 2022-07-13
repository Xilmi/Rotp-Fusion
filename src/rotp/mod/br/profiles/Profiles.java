
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

package rotp.mod.br.profiles;

import mod.br.profileManager.ClientClasses;
import mod.br.profileManager.UserProfiles;
import rotp.Rotp;
import rotp.mod.br.AddOns.GalaxyOptions;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * Access point for BrokenRegistry Mods
 *   - Profile Manager
 *   - Galaxy Options
 */

public class Profiles {
	private static final String configFileName = "ProfileManager.json";
	private static final UserProfiles userProfiles = 
			new UserProfiles(Rotp.jarPath(), configFileName);
	/**
	 * Global Parameter to allow or block the edition of game files
	 * Pressing "X" to load the file make it <b>true</b> 
	 */
	public static boolean ChangeGameFile = false;
	// ========================================================================
	// Getter
	//
	/**
	 * @return the user Profile
   	 */
	public static UserProfiles userProfiles() {
		return userProfiles;
	}
	// ========================================================================
	// Global Methods
	//
	/**
   	 * Load the configuration file and memorize first options
	 * @param options the {@code IGameOptions} containing the parameters
	 */
	public static void initUserSettings(IGameOptions options) {
		// System.out.println("Scaling Factor is: " + Rotp.getScalingFactor());
		userProfiles.initAndLoadProfiles(new ClientClasses(options));
	}
	/**
   	 * Check if User Settings are already initialized with gameOptions
	 * @return <b>true</b> if already initialized
   	 */
	public static boolean isInitialized () {
		return userProfiles.isInitialized();
	}
	/**
   	 * Load the configuration file,
   	 * Update with last Loaded Game options values
   	 * Save the new configuration file
	 * @param instance 
   	 */
	public static void saveGameOptionsToFile(GameSession instance) {
		userProfiles.saveGameToFile(new ClientClasses(instance));
	}
	/**
   	 * Load the configuration file,
   	 * Update with last Loaded Game options values
   	 * Save the new configuration file
	 * @param options class containing info
   	 */
	public static void saveGuiOptionsToFile(IGameOptions options) {
		userProfiles.saveGuiToFile(new ClientClasses(options));
	}
	/**
   	 * Load and execute the configuration file to Change the game file
	 * @param instance 
   	 */
	public static void changeGameSettings(GameSession instance) {
		userProfiles.changeGameSettings(new ClientClasses(instance));
	}
	/**
   	 * Load the configuration file to update the Action
   	 * Update with last Loaded Game options values
   	 * Save the new configuration file
	 * @param key the key to process
	 * @param global Global or Local ?
	 * @param group group name
	 * @param options class containing info
	 * @param newOptions class containing info
	 * @return <b>true</b> if something has been changed
   	 */
	public static boolean processKey(int key, boolean global,
			String group, IGameOptions options, IGameOptions newOptions) {
		return userProfiles.processKey(key, global, group,
							new ClientClasses(options, newOptions));
	}
	/**
   	 * Load The Profile Manager configuration file,
   	 * and save the current profile with the new configuration.
   	 * Or create it if there is none.
   	 */
	public static void loadProfileManagerConfig() {
		userProfiles.loadProfileManagerConfig();
	}
	// ========================================================================
	// Test For Enabled Methods
	//
	/**
  	 * Check if it is OK to use Spacing
	 * @return status
	 */
	public static boolean isFlagColorOrderEnabled() {
		return userProfiles.isParameterEnabled("FLAG COLOR ORDER");
	}
	/**
  	 * Check if it is OK to use Spacing
	 * @return status
	 */
	public static boolean isPreferredStarsPerEmpireEnabled() {
		return userProfiles.isParameterEnabled("PREF STARS PER EMPIRE");
	}
	/**
  	 * Check if it is OK to use Spacing
	 * @return status
	 */
	public static boolean isMinStarsPerEmpireEnabled() {
		return userProfiles.isParameterEnabled("MIN STARS PER EMPIRE");
	}
	/**
  	 * Check if it is OK to use Spacing
	 * @return status
	 */
	public static boolean isMaximizeSpacingEnabled() {
		return userProfiles.isParameterEnabled("MAXIMIZE EMPIRES SPACING")
				&& GalaxyOptions.isMaximizeEmpiresSpacing();
	}
	/**
  	 * Check if it is OK to use OpponentRaceList (for Random)
	 * @return status
	 */
	public static boolean isStartOpponentRaceListEnabled() {
		return userProfiles.isParameterEnabled("START PRESET OPPONENT");
	}
	/**
  	 * Check if it is OK to use OpponentRaceList (for Random)
	 * @return status
	 */
	public static boolean isStartOpponentAIListEnabled() {
		return userProfiles.isParameterEnabled("START PRESET AI");
	}
	/**
  	 * Check if it is OK to use GuiOpponentRaceList (for Random)
	 * @return status
	 */
	public static boolean isGuiOpponentRaceListEnabled() {
		return userProfiles.isParameterEnabled("GUI RACE FILTER");
	}
	/**
  	 * Check if it is OK to use GuiOpponentRaceList (for Random)
	 * @return status
	 */
	public static boolean isGuiOpponentAIListEnabled() {
		return userProfiles.isParameterEnabled("GUI AI FILTER");
	}
	/**
  	 * Check if it is OK to use Star Probability Modifier (for Random)
	 * @return status
	 */
	public static boolean isStarProbabilityEnabled() {
		return userProfiles.isParameterEnabled("STAR TYPE PROBABILITY");
	}
	/**
  	 * Check if it is OK to use Purple Planet Probability Modifier (for Random)
	 * @param type The planet color Type
	 * @return status
	 */
	public static boolean isPlanetProbabilityEnabled(String type) {
		return userProfiles.isParameterEnabled("PLANET TYPE PROBABILITY " + type);
	}
	
}
