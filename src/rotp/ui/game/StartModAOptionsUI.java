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
import static rotp.ui.UserPreferences.fertileHomeworld;
import static rotp.ui.UserPreferences.richHomeworld;
import static rotp.ui.UserPreferences.ultraRichHomeworld;
import static rotp.ui.util.AbstractParam.yesOrNo;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.Vector;

import rotp.ui.BaseText;
import rotp.ui.UserPreferences;
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
		btList.add(companionWorldsText			= newBT());
		btList.add(battleScoutText				= newBT());
		btList.add(randomTechStartText			= newBT());
		btList.add(retreatRestrictionsText		= newBT());
		btList.add(retreatRestrictionTurnsText	= newBT());
		endOfColumn();
		// Third column
		btList.add(customDifficultyText		= newBT());
		btList.add(dynamicDifficultyText	= newBT());
		btList.add(missileSizeModifierText	= newBT());
		btList.add(challengeModeText	 	= newBT());
		endOfColumn();
		// Fourth column
		// None yet
		// endOfColumn();
	}
	@Override protected void initCustom() {
		// For Mixed Setup: keep "paintComponent" up to date
		challengeModeText.displayText(challengeModeStr());
		battleScoutText.displayText(battleScoutStr());
		companionWorldsText.displayText(companionWorldsStr());
		dynamicDifficultyText.displayText(dynamicDifficultyStr());
		customDifficultyText.displayText(customDifficultyStr());
		randomTechStartText.displayText(randomTechStartStr());
		missileSizeModifierText.displayText(missileSizeModifierStr());
		retreatRestrictionsText.displayText(retreatRestrictionsStr());
		retreatRestrictionTurnsText.displayText(retreatRestrictionTurnsStr());
	}
	@Override protected void paintCustomComponent(Graphics2D g) {
		// Non automated Settings there		
		paintSetting(g, companionWorldsText, text("SETTINGS_MOD_COMPANION_WORLDS_DESC"));
		goToNextSetting();
		paintSetting(g, battleScoutText, text("SETTINGS_MOD_BATTLE_SCOUT_DESC"));
		goToNextSetting();
		paintSetting(g, dynamicDifficultyText, text("SETTINGS_MOD_DYNAMIC_DIFFICULTY_DESC"));
		goToNextSetting();
		paintSetting(g, customDifficultyText, text("SETTINGS_MOD_CUSTOM_DIFFICULTY_DESC"));
		goToNextSetting();
		paintSetting(g, randomTechStartText, text("SETTINGS_MOD_RANDOM_TECH_START_DESC"));
		goToNextSetting();
		paintSetting(g, challengeModeText, text("SETTINGS_MOD_CHALLENGE_MODE_DESC"));
		goToNextSetting();
		paintSetting(g, missileSizeModifierText, text("SETTINGS_MOD_MISSILE_SIZE_MODIFIER_DESC"));
		goToNextSetting();
		paintSetting(g, retreatRestrictionsText, text("SETTINGS_MOD_RETREAT_RESTRICTIONS_DESC"));
		goToNextSetting();
		paintSetting(g, retreatRestrictionTurnsText, text("SETTINGS_MOD_RETREAT_RESTRICTION_TURNS_DESC"));
	}
	@Override protected void repaintCustomComponent() {
		// Non automated Settings there		
		challengeModeText.repaint(challengeModeStr());
		battleScoutText.repaint(battleScoutStr());
		companionWorldsText.repaint(companionWorldsStr());
		dynamicDifficultyText.repaint(dynamicDifficultyStr());
		customDifficultyText.repaint(customDifficultyStr());
		randomTechStartText.repaint(randomTechStartStr());
		missileSizeModifierText.repaint(missileSizeModifierStr());
		retreatRestrictionTurnsText.repaint(retreatRestrictionTurnsStr());
		retreatRestrictionsText.repaint(retreatRestrictionsStr());
	}
	@Override protected void customMouseCommon(boolean up, boolean mid, 
			boolean shiftPressed, boolean ctrlPressed, MouseEvent e, MouseWheelEvent w) {
		// Non automated mouse actions there		
		if (hoverBox == challengeModeText.bounds())
			toggleChallengeMode();
		else if (hoverBox == battleScoutText.bounds())
			toggleBattleScout();
		else if (hoverBox == companionWorldsText.bounds())
			toggleCompanionWorlds(up);
		else if (hoverBox == randomTechStartText.bounds())
			toggleRandomTechStart();
		else if (hoverBox == customDifficultyText.bounds())
			toggleCustomDifficulty(up, shiftPressed, ctrlPressed);
		else if (hoverBox == dynamicDifficultyText.bounds())
			toggleDynamicDifficulty();
		else if (hoverBox == customDifficultyText.bounds()) {
			if (up) {
				if (shiftPressed) 
					scrollCustomDifficulty(5);
				else if (ctrlPressed)
					scrollCustomDifficulty(20);
				else
					scrollCustomDifficulty(1); 
				return;
			}
			else {
				if (shiftPressed) 
					scrollCustomDifficulty(-5);
				else if (ctrlPressed)
					scrollCustomDifficulty(-20);
				else
					scrollCustomDifficulty(-1); 
				return;
			}
		} else if (hoverBox == missileSizeModifierText.bounds()) {
			if (up) {
				if (shiftPressed) 
					scrollMissileSizeModifier(0.05f);
				else if (ctrlPressed)
					scrollMissileSizeModifier(0.2f);
				else
					scrollMissileSizeModifier(0.01f); 
				return;
			}
			else {
				if (shiftPressed) 
					scrollMissileSizeModifier(-0.05f);
				else if (ctrlPressed)
					scrollMissileSizeModifier(-0.2f);
				else
					scrollMissileSizeModifier(-0.01f); 
				return;
			}
		} else if (hoverBox == retreatRestrictionsText.bounds()) {
			if (up) {
				scrollRetreatRestrictions(1); 
				return;
			}
			else {
				scrollRetreatRestrictions(-1); 
				return;
			}
		} else if (hoverBox == retreatRestrictionTurnsText.bounds()) {
			if (up) {
				if (shiftPressed) 
					scrollRetreatRestrictionTurns(5);
				else if (ctrlPressed)
					scrollRetreatRestrictionTurns(20);
				else
					scrollRetreatRestrictionTurns(1); 
				return;
			}
			else {
				if (shiftPressed) 
					scrollRetreatRestrictionTurns(-5);
				else if (ctrlPressed)
					scrollRetreatRestrictionTurns(-20);
				else
					scrollRetreatRestrictionTurns(-1); 
				return;
			}
		}
	}
	// ========== Custom Methods ==========
	//
	private String challengeModeStr() {
		String opt = text(yesOrNo(UserPreferences.challengeMode()));
		return text("SETTINGS_MOD_CHALLENGE_MODE", opt)+"   ";
	}
	private String battleScoutStr() {
		String opt = text(yesOrNo(UserPreferences.battleScout()));
		return text("SETTINGS_MOD_BATLLE_SCOUT", opt)+"   ";
	}
	private String companionWorldsStr() {
		String opt = String.format("%d",UserPreferences.companionWorldsSigned()); // BR:
		return text("SETTINGS_MOD_COMPANION_WORLDS", opt)+"   ";
	}
	private String randomTechStartStr() {
		String opt = text(yesOrNo(UserPreferences.randomTechStart()));
		return text("SETTINGS_MOD_RANDOM_TECH_START", opt)+"   ";
	}
	private String customDifficultyStr() {
		String opt = String.format("%d",UserPreferences.customDifficulty());
		return text("SETTINGS_MOD_CUSTOM_DIFFICULTY", opt)+"   ";
	}
	private String dynamicDifficultyStr() {
		String opt = text(yesOrNo(UserPreferences.dynamicDifficulty()));
		return text("SETTINGS_MOD_DYNAMIC_DIFFICULTY", opt)+"   ";
	}
	private String missileSizeModifierStr() {
		String opt = String.format("%d",(int)(UserPreferences.missileSizeModifier() * 100));
		return text("SETTINGS_MOD_MISSILE_SIZE_MODIFIER", opt)+"   ";
	}
	private String retreatRestrictionsStr() {
		String opt = "";
		switch(UserPreferences.retreatRestrictions())
		{
			case 0:
				opt = text("SETTINGS_MOD_RETREAT_NONE");
				break;
			case 1:
				opt = text("SETTINGS_MOD_RETREAT_AI");
				break;
			case 2:
				opt = text("SETTINGS_MOD_RETREAT_PLAYER");
				break;
			case 3:
				opt = text("SETTINGS_MOD_RETREAT_BOTH");
				break;
			default:
				opt = text("SETTINGS_MOD_RETREAT_NONE");
				break;
		}
		return text("SETTINGS_MOD_RETREAT_RESTRICTIONS", opt)+"   ";
	}
	private String retreatRestrictionTurnsStr() {
		String opt = String.format("%d",UserPreferences.retreatRestrictionTurns());
		return text("SETTINGS_MOD_RETREAT_RESTRICTION_TURNS", opt)+"   ";
	}
	/**
	 * @return Retreat Restriction Option List // BR:
	 */
	public static List<String> getRetreatRestrictionOptions() {
            List<String> list = new Vector<String>();
            list.add("SETTINGS_MOD_RETREAT_NONE");
            list.add("SETTINGS_MOD_RETREAT_AI");
            list.add("SETTINGS_MOD_RETREAT_PLAYER");
            list.add("SETTINGS_MOD_RETREAT_BOTH");
            return list;
	} // \BR:
	private void toggleChallengeMode() {
		softClick();
		UserPreferences.toggleChallengeMode();
		challengeModeText.repaint(challengeModeStr());
	}
	private void toggleBattleScout() {
		softClick();
		UserPreferences.toggleBattleScout();
		battleScoutText.repaint(battleScoutStr());
	}
	private void toggleCompanionWorlds(boolean up) { // BR: added bidirectional
		softClick();
		UserPreferences.toggleCompanionWorlds(up);
		companionWorldsText.repaint(companionWorldsStr());
	}
	private void toggleRandomTechStart() {
		softClick();
		UserPreferences.toggleRandomTechStart();
		randomTechStartText.repaint(randomTechStartStr());
	}
	private void toggleCustomDifficulty(boolean up, // BR: added bidirectional
			boolean shiftPressed, boolean ctrlPressed) {
		softClick();
		if (up) {
			if (shiftPressed) 
				scrollCustomDifficulty(5);
			else if (ctrlPressed)
				scrollCustomDifficulty(20);
			else
				scrollCustomDifficulty(1); 
		}
		else {
			if (shiftPressed) 
				scrollCustomDifficulty(-5);
			else if (ctrlPressed)
				scrollCustomDifficulty(-20);
			else
				scrollCustomDifficulty(-1); 
		}
		customDifficultyText.repaint(customDifficultyStr());
	}
	private void scrollCustomDifficulty(int i) {
		UserPreferences.toggleCustomDifficulty(i);
		customDifficultyText.repaint(customDifficultyStr());
	}
	private void toggleDynamicDifficulty() {
		softClick();
		UserPreferences.toggleDynamicDifficulty();
		dynamicDifficultyText.repaint(dynamicDifficultyStr());
	}
	private void scrollMissileSizeModifier(float f) {
		UserPreferences.toggleMissileSizeModifier(f);
		missileSizeModifierText.repaint(missileSizeModifierStr());
	}
	private void scrollRetreatRestrictions(int i) {
		softClick();
		UserPreferences.toggleRetreatRestrictions(i);
		retreatRestrictionsText.repaint(retreatRestrictionsStr());
	}
	private void scrollRetreatRestrictionTurns(int i) {
		UserPreferences.toggleRetreatRestrictionTurns(i);
		retreatRestrictionTurnsText.repaint(retreatRestrictionTurnsStr());
	}
}
