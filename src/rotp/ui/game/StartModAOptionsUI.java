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

import static rotp.ui.UserPreferences.artifactsHomeworld;
import static rotp.ui.UserPreferences.battleScout;
import static rotp.ui.UserPreferences.challengeMode;
import static rotp.ui.UserPreferences.companionWorlds;
import static rotp.ui.UserPreferences.customDifficulty;
import static rotp.ui.UserPreferences.dynamicDifficulty;
import static rotp.ui.UserPreferences.fertileHomeworld;
import static rotp.ui.UserPreferences.missileSizeModifier;
import static rotp.ui.UserPreferences.randomTechStart;
import static rotp.ui.UserPreferences.restartAppliesSettings;
import static rotp.ui.UserPreferences.restartChangesAliensAI;
import static rotp.ui.UserPreferences.restartChangesPlayerAI;
import static rotp.ui.UserPreferences.restartChangesPlayerRace;
import static rotp.ui.UserPreferences.retreatRestrictionTurns;
import static rotp.ui.UserPreferences.retreatRestrictions;
import static rotp.ui.UserPreferences.richHomeworld;
import static rotp.ui.UserPreferences.ultraRichHomeworld;

import rotp.ui.util.AbstractOptionsUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class StartModAOptionsUI extends AbstractOptionsUI {
	private static final long serialVersionUID = 1L;
	public static final String guiTitleID = "SETTINGS_MOD_TITLE";
	
	// Just call the "super" with GUI Title Label ID
	public StartModAOptionsUI() {
		super(guiTitleID);
	}
	// ========== Abstract Overriders ==========
	//
	@Override protected void init0() {
		// First column (left)
		paramList.add(artifactsHomeworld);
		paramList.add(fertileHomeworld);
		paramList.add(richHomeworld);
		paramList.add(ultraRichHomeworld);
		endOfColumn();
		// Second column
		paramList.add(companionWorlds);
		paramList.add(battleScout);
		paramList.add(randomTechStart);
		paramList.add(retreatRestrictions);
		paramList.add(retreatRestrictionTurns);
		endOfColumn();
		// Third column
		paramList.add(customDifficulty);
		paramList.add(dynamicDifficulty);
		paramList.add(missileSizeModifier);
		paramList.add(challengeMode);
		endOfColumn();
		// Fourth column
		paramList.add(restartChangesPlayerRace);
		paramList.add(restartChangesPlayerAI);
		paramList.add(restartChangesAliensAI);
		paramList.add(restartAppliesSettings);
		endOfColumn();
	}
}
