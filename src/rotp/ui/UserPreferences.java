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
package rotp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

import rotp.Rotp;
import rotp.mod.br.AddOns.ShipSetAddOns;
import rotp.model.game.GameSession;
import rotp.ui.game.StartModBOptionsUI;
import rotp.ui.game.StartModViewOptionsUI;
import rotp.ui.game.StartModAOptionsUI;
import rotp.ui.util.AbstractParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamTech;
import rotp.ui.util.ParamAAN2;
import rotp.util.LanguageManager;
import rotp.util.sound.SoundManager;

public class UserPreferences {
	private static final String WINDOW_MODE = "GAME_SETTINGS_WINDOWED";
	private static final String BORDERLESS_MODE = "GAME_SETTINGS_BORDERLESS";
	private static final String FULLSCREEN_MODE = "GAME_SETTINGS_FULLSCREEN";
	private static final String GRAPHICS_LOW = "GAME_SETTINGS_GRAPHICS_LOW";
	private static final String GRAPHICS_MEDIUM = "GAME_SETTINGS_GRAPHICS_MED";
	private static final String GRAPHICS_HIGH = "GAME_SETTINGS_GRAPHICS_HIGH";
	private static final String AUTOCOLONIZE_YES = "GAME_SETTINGS_AUTOCOLONIZE_YES";
	private static final String AUTOCOLONIZE_NO = "GAME_SETTINGS_AUTOCOLONIZE_NO";
	private static final String AUTOBOMBARD_NO = "GAME_SETTINGS_AUTOBOMBARD_NO";
	private static final String AUTOBOMBARD_NEVER = "GAME_SETTINGS_AUTOBOMBARD_NEVER";
	private static final String AUTOBOMBARD_YES = "GAME_SETTINGS_AUTOBOMBARD_YES";
	private static final String AUTOBOMBARD_WAR = "GAME_SETTINGS_AUTOBOMBARD_WAR";
	private static final String AUTOBOMBARD_INVADE = "GAME_SETTINGS_AUTOBOMBARD_INVADE";
	private static final String TEXTURES_NO = "GAME_SETTINGS_TEXTURES_NO";
	private static final String TEXTURES_INTERFACE = "GAME_SETTINGS_TEXTURES_INTERFACE";
	private static final String TEXTURES_MAP = "GAME_SETTINGS_TEXTURES_MAP";
	private static final String TEXTURES_BOTH = "GAME_SETTINGS_TEXTURES_BOTH";
	private static final String SAVEDIR_DEFAULT = "GAME_SETTINGS_SAVEDIR_DEFAULT";
	private static final String SAVEDIR_CUSTOM = "GAME_SETTINGS_SAVEDIR_CUSTOM";
	private static final String SENSITIVITY_HIGH = "GAME_SETTINGS_SENSITIVITY_HIGH";
	private static final String SENSITIVITY_MEDIUM = "GAME_SETTINGS_SENSITIVITY_MEDIUM";
	private static final String SENSITIVITY_LOW = "GAME_SETTINGS_SENSITIVITY_LOW";

	private static final String PREFERENCES_FILE = "Remnants.cfg";
	private static final int MAX_BACKUP_TURNS = 20; // modnar: change max turns between backups to 20
	private static final String keyFormat = "%-25s: "; // BR: from 20 to 25 for a better alignment

	// BR common for All MOD entries
	public static final String BASE_UI = "SETUP_";
	public static final String ADV_UI  = "SETTINGS_";
	public static final String MOD_UI  = "SETTINGS_MOD_";

	// MOD GUI OPTIONS: I don't likes informations spread everywhere... Then here they are!
	// BR: ===== First Mod GUI: 
	public static final ParamAAN2 artifactHomeworld	= new ParamAAN2("HOME_ARTIFACT");
	public static final ParamAAN2 fertileHomeworld = new ParamAAN2("HOME_FERTILE");
	public static final ParamAAN2 richHomeworld 		= new ParamAAN2("HOME_RICH");
	public static final ParamAAN2 ultraRichHomeworld = new ParamAAN2("HOME_ULTRA_RICH");

	public static final LinkedList<AbstractParam<?>> modA = new LinkedList<AbstractParam<?>>(Arrays.asList(
			artifactHomeworld, fertileHomeworld, richHomeworld,ultraRichHomeworld
			));

	// BR: ===== Second Mod GUI:
	public static final ParamBoolean maximizeSpacing = new ParamBoolean(MOD_UI, "MAX_SPACINGS", false);
	public static final ParamFloat spacingLimit = new ParamFloat(MOD_UI, "MAX_SPACINGS_LIM"
			, 16f, 3.0f, null, false, 1f, 5f, 20f, "0.#", "0.#");
	public static final ParamInteger minStarsPerEmpire = new ParamInteger(MOD_UI, "MIN_STARS_PER_EMPIRE"
			, 3, 3, null, false, 1, 5, 20);
	public static final ParamFloat prefStarsPerEmpire = new ParamFloat(MOD_UI, "PREF_STARS_PER_EMPIRE"
			, 10f, 3.0f, null, false, 1f, 5f, 20f, "0.#", "0.#");
	public static final ParamTech techIrradiated = new 
			ParamTech("TECH_IRRADIATED",	3, "ControlEnvironment",	6); // level 18
	public static final ParamTech techCloaking	 = new 
			ParamTech("TECH_CLOAKING",		2, "Cloaking",				0); // level 27
	public static final ParamTech techStargate	 = new 
			ParamTech("TECH_STARGATES",		4, "Stargate", 				0); // level 27
	public static final ParamTech techHyperspace = new 
			ParamTech("TECH_HYPERSPACE",	0, "HyperspaceComm",		0); // level 34
	public static final ParamTech techIndustry2	 = new 
			ParamTech("TECH_INDUSTRY_2",	1, "ImprovedIndustrial",	7); // level 38
	public static final ParamTech techThorium	 = new 
			ParamTech("TECH_THORIUM",		4, "FuelRange",				8); // level 41
	public static final ParamTech techTransport  = new 
			ParamTech("TECH_TRANSPORTERS",	4, "CombatTransporter",		0); // level 45
	public static final ParamTech techTerra120	 = new 
			ParamTech("TECH_TERRAFORM_120",	3, "ImprovedTerraforming",	8); // level 50
	
	public static final LinkedList<AbstractParam<?>> modB = new LinkedList<AbstractParam<?>>(Arrays.asList(
		maximizeSpacing, spacingLimit, minStarsPerEmpire, prefStarsPerEmpire,
		techIrradiated, techCloaking, techStargate, techHyperspace,
		techIndustry2, techThorium, techTransport, techTerra120
		));

	// BR: ===== Viewing settings Mod GUI:
	public static final ParamFloat showFleetFactor = new ParamFloat(MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, false, 0.02f, 0.1f, 0.5f, "%", "%");
	public static final ParamFloat showFlagFactor = new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, false, 0.02f, 0.1f, 0.5f, "%", "%");
	public static final ParamFloat showPathFactor = new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, false, 0.02f, 0.1f, 0.5f, "%", "%");
	public static final ParamInteger showNameMinFont = new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT"
			, 8, 2, 24, false, 1, 2, 5);
	public static final ParamFloat showInfoFontRatio = new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, false, 0.02f, 0.1f, 0.5f, "%", "%");
	public static final ParamFloat mapFontFactor = new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, false, 0.02f, 0.1f, 0.5f, "%", "%");

	public static final LinkedList<AbstractParam<?>> modView = new LinkedList<AbstractParam<?>>(Arrays.asList(
			showFleetFactor, showFlagFactor, showPathFactor,
			showNameMinFont, showInfoFontRatio, mapFontFactor
			));

	private static boolean showMemory = false;
	private static boolean playMusic = true;
	private static boolean playSounds = true;
	private static int musicVolume = 10;
	private static int soundVolume = 10;
	private static int defaultMaxBases = 0;
	private static boolean displayYear = false;
	private static boolean newRacesOnByDefault = true; // BR: add option to get or reject the new races
	private static boolean governorOnByDefault = true; // BR:
	private static boolean gridCircularDisplay = true; // BR: add option to memorize the grid state
	private static boolean governorAutoSpendByDefault = false;
	private static boolean legacyGrowth = true; // BR:
	private static boolean governorAutoApply = true; // BR:
	private static int customDifficulty = 100; // mondar: add custom difficulty level option, in units of percent
	private static boolean dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
	private static boolean challengeMode = false; // modnar: add option to give AI more initial resources
	private static boolean randomTechStart = false; // modnar: add option to start all Empires with 2 techs, no Artifacts
	private static boolean battleScout = false; // modnar: add battleScout option to give player super Scout design
	private static int companionWorlds = 0; // modnar: add option to start game with additional colonies
	private static float missileSizeModifier = 2.0f/3.0f; //xilmi: add option to buff missiles by making them take less space and cost
	private static int retreatRestrictions = 0; //xilmi: add option to restrict retreating 0 - none, 1 - ai only, 2 - player only, 3 - everyone
	private static int retreatRestrictionTurns = 100; //xilmi: When retreat-restrictions are enabled for how many turns
	private static boolean autoColonize = false;
	private static boolean divertColonyExcessToResearch = true;
	private static boolean disableAdvisor = true;
	private static String autoBombardMode = AUTOBOMBARD_NO;
	private static String displayMode = BORDERLESS_MODE;
	private static String graphicsMode = GRAPHICS_HIGH;
	private static String texturesMode = TEXTURES_BOTH;
	private static String sensitivityMode = SENSITIVITY_MEDIUM;
	private static String saveDir = "";
	private static float uiTexturePct = 0.20f;
	private static int screenSizePct = 93;
	private static int backupTurns = 5; // modnar: change default turns between backups to 5

	/**
	 * setting GUI Default Button Action
	 */
	public static void setToDefault(String guiTitleID) {
		if (guiTitleID.equalsIgnoreCase(StartModAOptionsUI.guiTitleID)) {
			setModToDefault();
			return;
		}
		if (guiTitleID.equalsIgnoreCase(StartModBOptionsUI.guiTitleID)) {
			setMod2ToDefault();
			return;
		}
		if (guiTitleID.equalsIgnoreCase(StartModViewOptionsUI.guiTitleID)) {
			setModViewToDefault();
			return;
		}
	}
	/**
	 * Advanced setting GUI Default Button Action
	 */
	public static void setToDefault() {
		autoColonize = false;
		autoBombardMode = AUTOBOMBARD_NO;
		displayMode = WINDOW_MODE;
		graphicsMode = GRAPHICS_HIGH;
		texturesMode = TEXTURES_BOTH;
		sensitivityMode = SENSITIVITY_MEDIUM;
		screenSizePct = 93;
		backupTurns = 5; // modnar: change default turns between backups to 5
		customDifficulty = 100; // mondar: add custom difficulty level option, in units of percent
		dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
		challengeMode = false; // modnar: add option to give AI more initial resources
		randomTechStart = false; // modnar: add option to start all Empires with 2 techs, no Artifacts
		battleScout = false; // modnar: add battleScout option to give player super Scout design
		companionWorlds = 0; // modnar: add option to start game with additional colonies
		saveDir = "";
		uiTexturePct = 0.20f;
		showMemory = false;
		if (!playMusic)
			SoundManager.current().toggleMusic();
		if (!playSounds)
			SoundManager.current().toggleSounds();
		musicVolume = 10;
		soundVolume = 10;
		SoundManager.current().resetSoundVolumes();
		save();
	}
	// modnar: set MOD option to defaults, specifically for UI
	/**
	 * First Mod GUI Default Button Action
	 */
	public static void setModToDefault() {
		// Old settings
		customDifficulty = 100; // mondar: add custom difficulty level option, in units of percent
		dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
		newRacesOnByDefault = true; // BR: add option to get or reject the new races
		challengeMode = false; // modnar: add option to give AI more initial resources
		randomTechStart = false; // modnar: add option to start all Empires with 2 techs, no Artifacts
		battleScout = false; // modnar: add battleScout option to give player super Scout design
		companionWorlds = 0; // modnar: add option to start game with additional colonies
		missileSizeModifier = 2.0f/3.0f;
		retreatRestrictions = 0;
		retreatRestrictionTurns = 100;
		for (AbstractParam<?> param : modA) {
			param.setToDefault(false);
		}
		save();
	}
	/**
	 * Second Mod GUI Default Button Action
	 */
	public static void setMod2ToDefault() {
		// Old settings
		dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
		missileSizeModifier = 2.0f/3.0f;
		retreatRestrictions = 0;
		retreatRestrictionTurns = 100;
		newRacesOnByDefault = true; // BR: add option to get or reject the new races
		// New settings
		for (AbstractParam<?> param : modB) {
			param.setToDefault(false);
		}
		save();
	}
	/**
	 * Second Mod GUI Default Button Action
	 */
	public static void setModViewToDefault() {
		// Old settings
		// New settings
		for (AbstractParam<?> param : modView) {
			param.setToDefault(false);
		}
		save();
	}

	public static void setForNewGame() {
		autoColonize = false;
		autoBombardMode = AUTOBOMBARD_NO;
	}
//	// BR setters for GUI MOD parameters
	public static void setNewRacesOn(boolean newValue) {
		newRacesOnByDefault = newValue;
	}
	public static void setgridCircularDisplay(boolean newValue) {
		gridCircularDisplay = newValue;
	}
	public static void setChallengeMode(boolean newValue) {
		challengeMode = newValue;
	}
	public static void setBattleScout(boolean newValue) {
		battleScout = newValue;
	}
	public static void setCompanionWorlds(int newValue) {
		companionWorlds = newValue;
		if (companionWorlds > 6) { // BR: changed to 6; default = 4
			companionWorlds = 6;
		}
		else if (companionWorlds < -4) { // BR: changed to -4; default = 0
			companionWorlds = -4;
		}
	}
	public static void setRandomTechStart(boolean newValue) {
		randomTechStart = newValue; 
	}
	public static void setCustomDifficulty(int newValue) {
		customDifficulty = newValue;
		if (customDifficulty >= 500) {
			customDifficulty = 500;
		}
		else if (customDifficulty < 20) {
			customDifficulty = 20;
		}
	}
	public static void setMissileSizeModifier(float newValue) {
		missileSizeModifier = Math.max(0.1f, Math.min(1, newValue));
	}
	public static void setDynamicDifficulty(boolean newValue) {
		dynamicDifficulty = newValue; 
	}
	// \BR:

	public static void toggleNewRacesOn()	    { newRacesOnByDefault = !newRacesOnByDefault; save(); } // BR:
	public static boolean toggleGridCircularDisplay() {  // BR:
		gridCircularDisplay = !gridCircularDisplay;
		save();
		return gridCircularDisplay;
	}
	// modnar: MOD option toggles, specifically for UI
	public static void toggleChallengeMode()   { challengeMode = !challengeMode; save(); }
	public static void toggleBattleScout()     { battleScout = !battleScout; save(); }
	public static void toggleCompanionWorlds(boolean up) {// BR: made bidirectional
		if (up) {
			if ((companionWorlds >= 6) || (companionWorlds < -4)) // BR: changed to 6; default = 4
				companionWorlds = -4; // BR: changed to -4; default = 0
			else
				companionWorlds++;
		} else {
			if ((companionWorlds > 6) || (companionWorlds <= -4)) // BR: changed to 6; default = 4
				companionWorlds = 6; // BR: changed to -4; default = 0
			else
				companionWorlds--;			
		}
		save();
	}
	public static void toggleRandomTechStart() { randomTechStart = !randomTechStart; save(); }
	public static void toggleCustomDifficulty(int i) {
		if (customDifficulty+i >= 500)
			customDifficulty = 500;
		else if (customDifficulty+i < 20)
			customDifficulty = 20;
		else
			customDifficulty += i;
		save();
	}
	public static void toggleMissileSizeModifier(float f) {
		float newVal = missileSizeModifier + f;
		missileSizeModifier = Math.max(0.1f, Math.min(1, newVal));
		save();
	}
	public static void toggleRetreatRestrictions(int i) {
		// BR: modified to make it roll at the ends
		if (retreatRestrictions == 3 && i>0) 
			retreatRestrictions = 0;
		else if (retreatRestrictions == 0 && i<0) 
			retreatRestrictions = 3;
		else if (retreatRestrictions+i >= 3)
			retreatRestrictions = 3;
		else if (retreatRestrictions+i < 0)
			retreatRestrictions = 0;
		else
			retreatRestrictions += i;
		save();
	}
	public static void toggleRetreatRestrictionTurns(int i) {
		// BR: modified to make it roll at the ends
		if (retreatRestrictionTurns == 100 && i>0) 
			retreatRestrictionTurns = 0;
		else if (retreatRestrictionTurns == 0 && i<0) 
			retreatRestrictionTurns = 100;
		else if (retreatRestrictionTurns+i >= 100)
			retreatRestrictionTurns = 100;
		else if (retreatRestrictionTurns+i < 0)
			retreatRestrictionTurns = 0;
		else
			retreatRestrictionTurns += i;
		save();
	}
	public static void toggleDynamicDifficulty() { dynamicDifficulty = !dynamicDifficulty; save(); }
  
	public static int musicVolume()		   { return musicVolume; }
	public static int soundVolume()		   { return soundVolume; }
	public static boolean showMemory()	   { return showMemory; }
	public static void toggleMemory()	   { showMemory = !showMemory; save(); }
	public static boolean fullScreen()	   { return displayMode.equals(FULLSCREEN_MODE); }
	public static boolean windowed()	   { return displayMode.equals(WINDOW_MODE); }
	public static boolean borderless()	   { return displayMode.equals(BORDERLESS_MODE); }
	public static String displayMode()	   { return displayMode; }
	public static void toggleDisplayMode() {
		switch(displayMode) {
			case WINDOW_MODE:	  displayMode = BORDERLESS_MODE; break;
			case BORDERLESS_MODE: displayMode = FULLSCREEN_MODE; break;
			case FULLSCREEN_MODE: displayMode = WINDOW_MODE; break;
			default:			  displayMode = WINDOW_MODE; break;
		}
		save();
	}
	public static String graphicsMode()	 { return graphicsMode; }
	public static void toggleGraphicsMode()   {
		switch(graphicsMode) {
			case GRAPHICS_HIGH:   graphicsMode = GRAPHICS_MEDIUM; break;
			case GRAPHICS_MEDIUM: graphicsMode = GRAPHICS_LOW; break;
			case GRAPHICS_LOW:	  graphicsMode = GRAPHICS_HIGH; break;
			default :			  graphicsMode = GRAPHICS_HIGH; break;
		}
		save();
	}
	public static void toggleTexturesMode()   {
		switch(texturesMode) {
			case TEXTURES_NO:		 texturesMode = TEXTURES_INTERFACE; break;
			case TEXTURES_INTERFACE: texturesMode = TEXTURES_MAP; break;
			case TEXTURES_MAP:	     texturesMode = TEXTURES_BOTH; break;
			case TEXTURES_BOTH:	     texturesMode = TEXTURES_NO; break;
			default :				 texturesMode = TEXTURES_BOTH; break;
		}
		save();
	}
	public static String texturesMode()	 { return texturesMode; }
	public static boolean texturesInterface() { return texturesMode.equals(TEXTURES_INTERFACE) || texturesMode.equals(TEXTURES_BOTH); }
	public static boolean texturesMap()	   { return texturesMode.equals(TEXTURES_MAP) || texturesMode.equals(TEXTURES_BOTH); }

	public static void toggleSensitivityMode()   {
		switch(sensitivityMode) {
			case SENSITIVITY_LOW:	   sensitivityMode = SENSITIVITY_MEDIUM; break;
			case SENSITIVITY_MEDIUM:	sensitivityMode = SENSITIVITY_HIGH; break;
			case SENSITIVITY_HIGH:	  sensitivityMode = SENSITIVITY_LOW; break;
			default :				   sensitivityMode = SENSITIVITY_MEDIUM; break;
		}
		save();
	}
	public static String sensitivityMode()	 { return sensitivityMode; }
	public static boolean sensitivityHigh()	{ return sensitivityMode.equals(SENSITIVITY_HIGH); }
	public static boolean sensitivityMedium()  { return sensitivityMode.equals(SENSITIVITY_MEDIUM); }
	public static boolean sensitivityLow()	 { return sensitivityMode.equals(SENSITIVITY_LOW); }

	public static String autoColonizeMode()	 { return autoColonize ? AUTOCOLONIZE_YES : AUTOCOLONIZE_NO; }
	public static void toggleAutoColonize()	 { autoColonize = !autoColonize; save();  }
	public static boolean autoColonize()		{ return autoColonize; }

	public static void toggleAutoBombard()	 {
		switch(autoBombardMode) {
			case AUTOBOMBARD_NO:	 autoBombardMode = AUTOBOMBARD_NEVER; break;
			case AUTOBOMBARD_NEVER:  autoBombardMode = AUTOBOMBARD_YES; break;
			case AUTOBOMBARD_YES:	autoBombardMode = AUTOBOMBARD_WAR; break;
			case AUTOBOMBARD_WAR:	autoBombardMode = AUTOBOMBARD_INVADE; break;
			case AUTOBOMBARD_INVADE: autoBombardMode = AUTOBOMBARD_NO; break;
			default:				 autoBombardMode = AUTOBOMBARD_NO; break;
		}
		save();
	}
	public static String autoBombardMode()   { return autoBombardMode; }
	public static boolean autoBombardNo()    { return autoBombardMode.equals(AUTOBOMBARD_NO); }
	public static boolean autoBombardNever() { return autoBombardMode.equals(AUTOBOMBARD_NEVER); }
	public static boolean autoBombardYes()   { return autoBombardMode.equals(AUTOBOMBARD_YES); }
	public static boolean autoBombardWar()   { return autoBombardMode.equals(AUTOBOMBARD_WAR); }
	public static boolean autoBombardInvading() { return autoBombardMode.equals(AUTOBOMBARD_INVADE); }

	public static boolean playAnimations() { return !graphicsMode.equals(GRAPHICS_LOW); }
	public static boolean antialiasing() { return graphicsMode.equals(GRAPHICS_HIGH); }
	public static boolean playSounds()   { return playSounds; }
	public static void toggleSounds()    { playSounds = !playSounds;	save(); }
	public static boolean playMusic()    { return playMusic; }
	public static void toggleMusic()     { playMusic = !playMusic; save();  }
	public static int customDifficulty() { return customDifficulty; } // mondar: add custom difficulty level option, in units of percent
	public static boolean dynamicDifficulty() { return dynamicDifficulty; } // modnar: add dynamic difficulty option, change AI colony production
	public static boolean newRacesOn()        { return newRacesOnByDefault; } // BR: add option to get or reject the new races
	public static boolean gridCircularDisplay() { return gridCircularDisplay; } // BR: add option to memorize the grid state
	public static boolean challengeMode()   { return challengeMode; } // modnar: add option to give AI more initial resources
	public static boolean randomTechStart() { return randomTechStart; } // modnar: add option to start all Empires with 2 techs, no Artifacts
	public static boolean battleScout()     { return battleScout; } // modnar: add battleScout option to give player super Scout design
	public static int companionWorlds()     { return Math.abs(companionWorlds); } // modnar: add option to start game with additional colonies
	public static int companionWorldsSigned() { return companionWorlds; } // BR: to manage old and new distribution
	public static float missileSizeModifier() { return missileSizeModifier; } 
	public static int retreatRestrictions()   { return retreatRestrictions; }
	public static int retreatRestrictionTurns() { return retreatRestrictionTurns; }
	public static int screenSizePct()       { return screenSizePct; }
	public static void screenSizePct(int i) { setScreenSizePct(i); }
	public static String saveDirectoryPath() {
		if (saveDir.isEmpty())
			return Rotp.jarPath();
		else
			return saveDir;
	}
	public static String backupDirectoryPath() {
		return saveDirectoryPath()+"/"+GameSession.BACKUP_DIRECTORY;
	}
	public static String saveDir()       { return saveDir; }
	public static void saveDir(String s) { saveDir = s; save(); }
	public static String saveDirStr()    {
		if (saveDir.isEmpty())
			return SAVEDIR_DEFAULT;
		else
			return SAVEDIR_CUSTOM;
	}
	public static int backupTurns() { return backupTurns; }
	public static boolean backupTurns(int i) {
		int prev = backupTurns;
		backupTurns = Math.min(Math.max(0,i),MAX_BACKUP_TURNS);
		save();
		return prev != backupTurns;
	}
	public static void toggleBackupTurns() {
		if ((backupTurns >= MAX_BACKUP_TURNS) || (backupTurns < 0)) // modnar: add negative check
			backupTurns = 0;
		else // modnar: change backupTurns to be: 0, 1, 5 ,10, 20
			backupTurns = (int) Math.round(1.0f + 4.87f*backupTurns - 0.93f*Math.pow(backupTurns, 2) + 0.063f*Math.pow(backupTurns, 3) );
		save();
	}
	public static void toggleYearDisplay()	{ displayYear = !displayYear; save(); }
	public static boolean displayYear()	   { return displayYear; }
	public static void setDefaultMaxBases(int bases)	{ defaultMaxBases = bases; }
	public static int defaultMaxBases()	{ return defaultMaxBases; }
	public static void setGovernorOn(boolean governorOn)	{ governorOnByDefault = governorOn; save(); } // BR:
	public static boolean governorOnByDefault() { return governorOnByDefault; }
	public static void setAutoSpendOn(boolean autospendOn)  { governorAutoSpendByDefault = autospendOn; save(); }
	public static boolean governorAutoSpendByDefault() { return governorAutoSpendByDefault; }
	public static void setLegacyGrowth(boolean legacy_Growth)  { legacyGrowth = legacy_Growth; save(); } // BR:
	public static boolean legacyGrowth() { return legacyGrowth; } // BR:
	public static void setGovernorAutoApply(boolean auto_Apply)  { governorAutoApply = auto_Apply; save(); } // BR:
	public static boolean governorAutoApply() { return governorAutoApply; } // BR:
	public static void setDivertColonyExcessToResearch(boolean divertOn)  {divertColonyExcessToResearch = divertOn; save(); }
	public static boolean divertColonyExcessToResearch()  { return divertColonyExcessToResearch; }
	public static boolean disableAdvisor() { return disableAdvisor; }
	public static void uiTexturePct(int i)	{ uiTexturePct = i / 100.0f; }
	public static float uiTexturePct()		{ return uiTexturePct; }
	
	public static void loadAndSave() {
		load();
		save();
	}
	@SuppressWarnings("null")
	public static void load() {
		String path = Rotp.jarPath();
		File configFile = new File(path, PREFERENCES_FILE);
		// modnar: change to InputStreamReader, force UTF-8
		try ( BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(configFile), "UTF-8"));) {
			String input;
			if (in != null) {
				while ((input = in.readLine()) != null)
					loadPreferenceLine(input.trim());
			}
		}
		catch (FileNotFoundException e) {
			System.err.println(path+PREFERENCES_FILE+" not found.");
		}
		catch (IOException e) {
			System.err.println("UserPreferences.load -- IOException: "+ e.toString());
		}
	}
	public static int save() {
		String path = Rotp.jarPath();
		try (FileOutputStream fout = new FileOutputStream(new File(path, PREFERENCES_FILE));
			// modnar: change to OutputStreamWriter, force UTF-8
			PrintWriter out = new PrintWriter(new OutputStreamWriter(fout, "UTF-8")); ) {
			out.println("===== Base ROTP Settings =====");
			out.println();
			out.println(keyFormat("DISPLAY_MODE")+displayModeToSettingName(displayMode));
			out.println(keyFormat("GRAPHICS")+graphicsModeToSettingName(graphicsMode));
			out.println(keyFormat("MUSIC")+ yesOrNo(playMusic));
			out.println(keyFormat("SOUNDS")+ yesOrNo(playSounds));
			out.println(keyFormat("MUSIC_VOLUME")+ musicVolume);
			out.println(keyFormat("SOUND_VOLUME")+ soundVolume);
			out.println(keyFormat("SAVE_DIR")+ saveDir);
			out.println(keyFormat("BACKUP_TURNS")+ backupTurns);
			out.println(keyFormat("AUTOCOLONIZE")+ yesOrNo(autoColonize));
			out.println(keyFormat("AUTOBOMBARD")+autoBombardToSettingName(autoBombardMode));
			out.println(keyFormat("TEXTURES")+texturesToSettingName(texturesMode));
			out.println(keyFormat("SENSITIVITY")+sensitivityToSettingName(sensitivityMode));
			out.println(keyFormat("SHOW_MEMORY")+ yesOrNo(showMemory));
			out.println(keyFormat("DISPLAY_YEAR")+ yesOrNo(displayYear));
			out.println(keyFormat("SCREEN_SIZE_PCT")+ screenSizePct());
			out.println(keyFormat("UI_TEXTURE_LEVEL")+(int) (uiTexturePct()*100));
			out.println(keyFormat("DISABLE_ADVISOR") + yesOrNo(disableAdvisor));
			out.println(keyFormat("LANGUAGE")+ languageDir());
			// BR: Governors GUI
			out.println();
			out.println("===== In Game Settings =====");
			out.println();
			out.println(keyFormat("DEFAULT_MAX_BASES") + defaultMaxBases);
			out.println(keyFormat("GOVERNOR_ON_BY_DEFAULT") + yesOrNo(governorOnByDefault));
			out.println(keyFormat("AUTOSPEND_ON_BY_DEFAULT") + yesOrNo(governorAutoSpendByDefault));
			out.println(keyFormat("DIVERT_COLONY_EXCESS_TO_RESEARCH")+ yesOrNo(divertColonyExcessToResearch));
			out.println(keyFormat("LEGACY_GROWTH") + yesOrNo(legacyGrowth)); // BR:
			out.println(keyFormat("GOVERNOR_AUTO_APPLY") + yesOrNo(governorAutoApply)); // BR:
			out.println(keyFormat("GRID_CIRCULAR_DISPLAY") + yesOrNo(gridCircularDisplay)); // BR: add option to memorize the grid state
			// BR: First Mod GUI (Modnar)
			out.println();
			out.println("===== MOD A GUI Settings =====");
			out.println();
			for (AbstractParam<?> param : modA) {
				out.println(keyFormat(param.getCfgLabel()) + param.getCfgValue());
			}
			out.println(keyFormat("CUSTOM_DIFFICULTY")+ customDifficulty); // mondar: add custom difficulty level option, in units of percent
			out.println(keyFormat("DYNAMIC_DIFFICULTY")+ yesOrNo(dynamicDifficulty)); // modnar: add dynamic difficulty option, change AI colony production
			out.println(keyFormat("NEW_RACES_ON_BY_DEFAULT") + yesOrNo(newRacesOnByDefault)); // BR: add option to get or reject the new races
			out.println(keyFormat("CHALLENGE_MODE")+ yesOrNo(challengeMode)); // modnar: add option to give AI more initial resources
			out.println(keyFormat("RANDOM_TECH_START")+ yesOrNo(randomTechStart)); // modnar: add option to start all Empires with 2 techs, no Artifacts
			out.println(keyFormat("BATTLE_SCOUT")+ yesOrNo(battleScout)); // modnar: add battleScout option to give player super Scout design
			out.println(keyFormat("COMPANION_WORLDS")+ companionWorlds); // modnar: add option to start game with additional colonies
			out.println(keyFormat("MISSILE_SIZE_MODIFIER")+ missileSizeModifier);
			out.println(keyFormat("RETREAT_RESTRICTIONS")+ retreatRestrictions);
			out.println(keyFormat("RETREAT_RESTRICTION_TURNS")+ retreatRestrictionTurns);
			out.println(keyFormat("PLAYER_SHIP_SET")	  + ShipSetAddOns.playerShipSet()); 
			// BR: Second Mod GUI
			out.println();
			out.println("===== MOD B GUI Settings =====");
			out.println();
			for (AbstractParam<?> param : modB) {
				out.println(keyFormat(param.getCfgLabel()) + param.getCfgValue());
			}
			out.println();
			out.println("===== MOD Display GUI Settings =====");
			out.println();
			for (AbstractParam<?> param : modView) {
				out.println(keyFormat(param.getCfgLabel()) + param.getCfgValue());
			}
			return 0;
		}
		catch (IOException e) {
			System.err.println("UserPreferences.save -- IOException: "+ e.toString());
			return -1;
		}
	}

	private static String keyFormat(String s)  { return String.format(keyFormat, s); }

	private static void loadPreferenceLine(String line) {
		if (line.isEmpty())
			return;

		String[] args = line.split(":");
		if (args.length < 2)
			return;

		String key = args[0].toUpperCase().trim();
		String val = args[1].trim();
		// for values that may have embedded :, like the save dir path
		String fullVal = val;
		for (int i=2;i<args.length;i++)
			fullVal = fullVal+":"+args[i];
		if (key.isEmpty() || val.isEmpty())
				return;

		if (Rotp.logging)
			System.out.println("Key:"+key+"  value:"+val);
		switch(key) {
			case "DISPLAY_MODE": displayMode = displayModeFromSettingName(val); return;
			case "GRAPHICS":     graphicsMode = graphicsModeFromSettingName(val); return;
			case "MUSIC":        playMusic = yesOrNo(val); return;
			case "SOUNDS":       playSounds = yesOrNo(val); return;
			case "MUSIC_VOLUME": setMusicVolume(val); return;
			case "SOUND_VOLUME": setSoundVolume(val); return;
			case "SAVE_DIR":     saveDir  = fullVal.trim(); return;
			case "BACKUP_TURNS": backupTurns  = Integer.valueOf(val); return;
			case "AUTOCOLONIZE": autoColonize = yesOrNo(val); return;
			case "AUTOBOMBARD":  autoBombardMode = autoBombardFromSettingName(val); return;
			case "TEXTURES":     texturesMode = texturesFromSettingName(val); return;
			case "SENSITIVITY":  sensitivityMode = sensitivityFromSettingName(val); return;
			case "SHOW_MEMORY":  showMemory = yesOrNo(val); return;
			case "DISPLAY_YEAR": displayYear = yesOrNo(val); return;
			case "DEFAULT_MAX_BASES": defaultMaxBases = Integer.valueOf(val); return;
			case "GOVERNOR_ON_BY_DEFAULT":  governorOnByDefault = yesOrNo(val); return;
			case "AUTOSPEND_ON_BY_DEFAULT": governorAutoSpendByDefault = yesOrNo(val); return;
			case "DIVERT_COLONY_EXCESS_TO_RESEARCH": divertColonyExcessToResearch = yesOrNo(val); return;
			case "LEGACY_GROWTH": legacyGrowth = yesOrNo(val); return; // BR:
			case "GOVERNOR_AUTO_APPLY": governorAutoApply = yesOrNo(val); return; // BR:
			case "DISABLE_ADVISOR": disableAdvisor = yesOrNo(val); return;
			case "SCREEN_SIZE_PCT": screenSizePct(Integer.valueOf(val)); return;
			case "UI_TEXTURE_LEVEL": uiTexturePct(Integer.valueOf(val)); return;
			case "CUSTOM_DIFFICULTY": setCustomDifficulty(val); return; // mondar: add custom difficulty level option, in units of percent
			case "DYNAMIC_DIFFICULTY": dynamicDifficulty = yesOrNo(val); return; // modnar: add dynamic difficulty option, change AI colony production
			case "NEW_RACES_ON_BY_DEFAULT": newRacesOnByDefault = yesOrNo(val); return; // BR: add option to get or reject the new races
			case "GRID_CIRCULAR_DISPLAY": gridCircularDisplay = yesOrNo(val); return; // BR: add option to memorize the grid state
			case "CHALLENGE_MODE": challengeMode = yesOrNo(val); return; // modnar: add option to give AI more initial resources
			case "RANDOM_TECH_START": randomTechStart = yesOrNo(val); return; // modnar: add option to start all Empires with 2 techs, no Artifacts
			case "BATTLE_SCOUT": battleScout = yesOrNo(val); return; // modnar: add battleScout option to give player super Scout design
			case "COMPANION_WORLDS": setNumCompanionWorlds(val); return; // modnar: add option to start game with additional colonies
			case "MISSILE_SIZE_MODIFIER": setMissileSizeModifier(val); return;
			case "RETREAT_RESTRICTIONS": setRetreatRestrictions(val); return;
			case "RETREAT_RESTRICTION_TURNS": setRetreatRestrictionTurns(val); return;
			case "PLAYER_SHIP_SET": ShipSetAddOns.playerShipSet(val); return; // BR: add option to select Player Ship Set
			case "LANGUAGE": selectLanguage(val); return;
			default:
			// BR: Second Mod GUI
				for (AbstractParam<?> param : modA) {
					if (key.equalsIgnoreCase(param.getCfgLabel()))
						param.setFromCfg(val);
				}
				for (AbstractParam<?> param : modB) {
					if (key.equalsIgnoreCase(param.getCfgLabel()))
						param.setFromCfg(val);
				}
				for (AbstractParam<?> param : modView) {
					if (key.equalsIgnoreCase(param.getCfgLabel()))
						param.setFromCfg(val);
				}
				break;
		}
	}
	private static String yesOrNo(boolean b) {
		return b ? "YES" : "NO";
	}
	private static boolean yesOrNo(String s) {
		return s.equalsIgnoreCase("YES");
	}
	private static void selectLanguage(String s) {
		LanguageManager.selectLanguage(s);
	}
	// modnar: add option to start game with additional colonies
	private static void setNumCompanionWorlds(String s) {
		int val = Integer.valueOf(s); // BR: changed limits; was 0 & 4
		companionWorlds = Math.max(-4, Math.min(6, val)); // max number of companion worlds is 4
	}
	// modnar: add custom difficulty level option, in units of percent
	private static void setCustomDifficulty(String s) {
		int val = Integer.valueOf(s);
		customDifficulty = Math.max(20, Math.min(500, val)); // custom difficulty range: 20% to 500%
	}
	private static void setMissileSizeModifier(String s) {
		float val = Float.valueOf(s);
		missileSizeModifier = Math.max(0.1f, Math.min(1, val));
	}
	public static void setRetreatRestrictions(int val) { // BR: For Profile Manager
		retreatRestrictions = Math.max(0, Math.min(3, val));
	}
	private static void setRetreatRestrictions(String s) {
		int val = Integer.valueOf(s);
		retreatRestrictions = Math.max(0, Math.min(3, val));
	}
	public static void setRetreatRestrictionTurns(int val) { // BR: For Profile Manager
		retreatRestrictionTurns = Math.max(0, Math.min(100, val));
	}
	private static void setRetreatRestrictionTurns(String s) {
		int val = Integer.valueOf(s);
		retreatRestrictionTurns = Math.max(0, Math.min(100, val));
	}
	private static void setMusicVolume(String s) {
		int val = Integer.valueOf(s);
		musicVolume = Math.max(0, Math.min(10,val));
	}
	private static void setSoundVolume(String s) {
		int val = Integer.valueOf(s);
		soundVolume = Math.max(0, Math.min(10,val));
	}
	private static String languageDir() {
		return LanguageManager.selectedLanguageDir();
	}
	private static void setScreenSizePct(int i) {
		screenSizePct = Math.max(50,Math.min(i,100));
	}
	public static boolean shrinkFrame() {
		int oldSize = screenSizePct;
		setScreenSizePct(screenSizePct-5);
		return oldSize != screenSizePct;
	}
	public static boolean expandFrame() {
		int oldSize = screenSizePct;
		setScreenSizePct(screenSizePct+5);
		return oldSize != screenSizePct;
	}
	public static String displayModeToSettingName(String s) {
		switch(s) {
			case WINDOW_MODE:	  return "Windowed";
			case BORDERLESS_MODE: return "Borderless";
			case FULLSCREEN_MODE: return "Fullscreen";
		}
		return "Windowed";
	}
	public static String displayModeFromSettingName(String s) {
		switch(s) {
			case "Windowed":   return WINDOW_MODE;
			case "Borderless": return BORDERLESS_MODE;
			case "Fullscreen": return FULLSCREEN_MODE;
		}
		return WINDOW_MODE;
	}
	public static String graphicsModeToSettingName(String s) {
		switch(s) {
			case GRAPHICS_LOW:	  return "Low";
			case GRAPHICS_MEDIUM: return "Medium";
			case GRAPHICS_HIGH:   return "High";
		}
		return "High";
	}
	public static String graphicsModeFromSettingName(String s) {
		switch(s) {
			case "Low":	   return GRAPHICS_LOW;
			case "Medium": return GRAPHICS_MEDIUM;
			case "High":   return GRAPHICS_HIGH;
		}
		return GRAPHICS_HIGH;
	}
	public static String autoBombardToSettingName(String s) {
		switch(s) {
			case AUTOBOMBARD_NO:	 return "No";
			case AUTOBOMBARD_NEVER:  return "Never";
			case AUTOBOMBARD_YES:	 return "Yes";
			case AUTOBOMBARD_WAR:	 return "War";
			case AUTOBOMBARD_INVADE: return "Invade";
		}
		return "No";
	}
	public static String autoBombardFromSettingName(String s) {
		switch(s) {
			case "No":	   return AUTOBOMBARD_NO;
			case "Never":  return AUTOBOMBARD_NEVER;
			case "Yes":    return AUTOBOMBARD_YES;
			case "War":	   return AUTOBOMBARD_WAR;
			case "Invade": return AUTOBOMBARD_INVADE;
		}
		return AUTOBOMBARD_NO;
	}
	public static String texturesToSettingName(String s) {
		switch(s) {
			case TEXTURES_NO:		 return "No";
			case TEXTURES_INTERFACE: return "Interface";
			case TEXTURES_MAP:	     return "Map";
			case TEXTURES_BOTH:	     return "Both";
		}
		return "Both";
	}
	public static String texturesFromSettingName(String s) {
		switch(s) {
			case "No":        return TEXTURES_NO;
			case "Interface": return TEXTURES_INTERFACE;
			case "Map":       return TEXTURES_MAP;
			case "Both":      return TEXTURES_BOTH;
		}
		return TEXTURES_BOTH;
	}
	public static String sensitivityToSettingName(String s) {
		switch(s) {
			case SENSITIVITY_HIGH:   return "High";
			case SENSITIVITY_MEDIUM: return "Medium";
			case SENSITIVITY_LOW:    return "Low";
		}
		return "Medium";
	}
	public static String sensitivityFromSettingName(String s) {
		switch(s) {
			case "High":   return SENSITIVITY_HIGH;
			case "Medium": return SENSITIVITY_MEDIUM;
			case "Low":    return SENSITIVITY_LOW;
		}
		return SENSITIVITY_MEDIUM;
	}
	public static void increaseMusicLevel()	{
		musicVolume = Math.min(10, musicVolume+1);
		SoundManager.current().resetMusicVolumes();
		save();
	}
	public static void decreaseMusicLevel()	{
		musicVolume = Math.max(0, musicVolume-1);
		SoundManager.current().resetMusicVolumes();
		save();
	};
	public static void increaseSoundLevel()	{
		soundVolume = Math.min(10, soundVolume+1);
		SoundManager.current().resetSoundVolumes();
		save();
	}
	public static void decreaseSoundLevel()	{
		soundVolume = Math.max(0, soundVolume-1);
		SoundManager.current().resetSoundVolumes();
		save();
	}
}
