
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

package rotp.mod.br.AddOns;

import java.util.List;

import mod.br.AddOns.RaceFilter;
import mod.br.profileManager.ClientClasses;
import mod.br.profileManager.Group_Galaxy.StartPresetAI;
import mod.br.profileManager.Group_Galaxy.StartPresetOpponent;
import rotp.mod.br.profiles.Profiles;
import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * Some tools to give Races filtering
 */
public class RacesOptions {
	
	/**
	 * @return the Gui Race Filter
	 */
    public static List<String> getGuiFilteredRaceList() {
         return RaceFilter.selectedGuiRaceFilter();
    }
    /**
	 * @return Race Filter
	 */
	public static List<String> getFilteredRaceList() {
         return RaceFilter.selectedGameRaceFilter();
    }
	/**
	 * @return the Gui AI Filter
	 */
    public static List<String> getGuiFilteredAIList() {
         return RaceFilter.selectedGuiAIFilter();
    }
    /**
	 * @return AI Filter
	 */
	public static List<String> getFilteredAIList() {
         return RaceFilter.selectedGameAIFilter();
    }
	/**
	 * Set the starting opponents
	 * @param options the {@code IGameOptions} containing the parameters
	 */
	public static void loadStartingOpponents(IGameOptions options) {
		StartPresetOpponent startPresetOpponent;
		startPresetOpponent = (StartPresetOpponent) 
				Profiles.userProfiles().getParameter("START PRESET OPPONENT");
		startPresetOpponent.loadOpponents(new ClientClasses(options));
	}
	/**
	 * Set the starting opponents AI
	 * @param options the {@code IGameOptions} containing the parameters
	 */
	public static void loadStartingAIs(IGameOptions options) {
		StartPresetAI startPresetAI;
		startPresetAI = (StartPresetAI) 
				Profiles.userProfiles().getParameter("START PRESET AI");
		startPresetAI.loadAIs(new ClientClasses(options));
	}
}
