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
package rotp.ui.game;

import static rotp.ui.UserPreferences.eventsStartTurn;
import static rotp.ui.UserPreferences.loadWithNewOptions;
import static rotp.ui.UserPreferences.maximizeSpacing;
import static rotp.ui.UserPreferences.minStarsPerEmpire;
import static rotp.ui.UserPreferences.prefStarsPerEmpire;
import static rotp.ui.UserPreferences.randomAlienRaces;
import static rotp.ui.UserPreferences.randomAlienRacesMax;
import static rotp.ui.UserPreferences.randomAlienRacesMin;
import static rotp.ui.UserPreferences.randomAlienRacesSmoothEdges;
import static rotp.ui.UserPreferences.randomAlienRacesTargetMax;
import static rotp.ui.UserPreferences.randomAlienRacesTargetMin;
import static rotp.ui.UserPreferences.spacingLimit;
import static rotp.ui.UserPreferences.techCloaking;
import static rotp.ui.UserPreferences.techHyperspace;
import static rotp.ui.UserPreferences.techIndustry2;
import static rotp.ui.UserPreferences.techIrradiated;
import static rotp.ui.UserPreferences.techStargate;
import static rotp.ui.UserPreferences.techThorium;
import static rotp.ui.UserPreferences.techTransport;

import rotp.ui.util.AbstractOptionsUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class StartModBOptionsUI extends AbstractOptionsUI {
	private static final long serialVersionUID = 1L;
	public static final String guiTitleID = "SETTINGS_MOD_TITLE_B";

	public StartModBOptionsUI() {
		super(guiTitleID);
	}
	@Override protected void init0() {
		// First column (left)
		paramList.add(maximizeSpacing);
		paramList.add(spacingLimit);
		paramList.add(randomAlienRacesTargetMax);
		paramList.add(randomAlienRacesTargetMin);
		paramList.add(randomAlienRaces);
		endOfColumn();
		// Second column
		paramList.add(minStarsPerEmpire);
		paramList.add(prefStarsPerEmpire);
		paramList.add(randomAlienRacesMax);
		paramList.add(randomAlienRacesMin);
		paramList.add(randomAlienRacesSmoothEdges);
		endOfColumn();
		// Third column
		paramList.add(loadWithNewOptions);
		paramList.add(techIrradiated);
		paramList.add(techCloaking);
		paramList.add(techStargate);
		paramList.add(techHyperspace);
		endOfColumn();
		// Fourth column
		paramList.add(eventsStartTurn);
		paramList.add(techIndustry2);
		paramList.add(techThorium);
		paramList.add(techTransport);
		endOfColumn();
	}
}
