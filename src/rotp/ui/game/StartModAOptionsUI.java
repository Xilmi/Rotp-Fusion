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
import static rotp.ui.UserPreferences.retreatRestrictionTurns;
import static rotp.ui.UserPreferences.retreatRestrictions;
import static rotp.ui.UserPreferences.richHomeworld;
import static rotp.ui.UserPreferences.ultraRichHomeworld;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.BaseText;
import rotp.ui.util.AbstractOptionsUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class StartModAOptionsUI extends AbstractOptionsUI {
	private static final long serialVersionUID = 1L;
	public static final String guiTitleID = "SETTINGS_MOD_TITLE";
	//
	// Text Box creation: Add your parameter Here then go to init0
	//	
	// First column (left)
	protected BaseText artifactHomeworldText;
	protected BaseText fertileHomeworldText;
	protected BaseText richHomeworldText;
	protected BaseText ultraRichHomeworldText;
	// Second column
	protected BaseText battleScoutText;
	protected BaseText companionWorldsText;
	protected BaseText randomTechStartText;
	protected BaseText retreatRestrictionsText;
	protected BaseText retreatRestrictionTurnsText;
	// Third column
	protected BaseText customDifficultyText;
	protected BaseText dynamicDifficultyText;
	protected BaseText missileSizeModifierText;
	protected BaseText challengeModeText;
	// Fourth column
	
	// Just call the "super" with GUI Title Label ID
	public StartModAOptionsUI() {
		super(guiTitleID);
	}
	// ========== Abstract Overriders ==========
	//
	@Override protected void init0() {
		// For "ParamX" class Settings (Starting with ParamX for the automation to work)
		// Complete this table... Et Voilà!
		// For Mixed Setup keep "paintComponent" up to date
		// First column (left)
		btList.add(artifactHomeworldText	= newBT()); paramList.add(artifactsHomeworld);
		btList.add(fertileHomeworldText		= newBT()); paramList.add(fertileHomeworld);
		btList.add(richHomeworldText		= newBT()); paramList.add(richHomeworld);
		btList.add(ultraRichHomeworldText	= newBT()); paramList.add(ultraRichHomeworld);
		endOfColumn();
		// Second column
		btList.add(companionWorldsText			= newBT()); paramList.add(companionWorlds);
		btList.add(battleScoutText				= newBT()); paramList.add(battleScout);
		btList.add(randomTechStartText			= newBT()); paramList.add(randomTechStart);
		btList.add(retreatRestrictionsText		= newBT()); paramList.add(retreatRestrictions);
		btList.add(retreatRestrictionTurnsText	= newBT()); paramList.add(retreatRestrictionTurns);
		endOfColumn();
		// Third column
		btList.add(customDifficultyText		= newBT()); paramList.add(customDifficulty);
		btList.add(dynamicDifficultyText	= newBT()); paramList.add(dynamicDifficulty);
		btList.add(missileSizeModifierText	= newBT()); paramList.add(missileSizeModifier);
		btList.add(challengeModeText	 	= newBT()); paramList.add(challengeMode);
		endOfColumn();
		// Fourth column
		// None yet
		// endOfColumn();
	}
	@Override protected void initCustom() {}
	@Override protected void paintCustomComponent(Graphics2D g) {}
	@Override protected void repaintCustomComponent() {}
	@Override protected void customMouseCommon(boolean up, boolean mid, 
			boolean shiftPressed, boolean ctrlPressed, MouseEvent e, MouseWheelEvent w) {}
}
