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
import rotp.model.events.RandomEvents;
import rotp.model.game.GameSession;
import rotp.ui.util.EventsStartTurn;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamAAN2;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamCR;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamOptions;
import rotp.ui.util.ParamTech;
import rotp.ui.util.PlayerShipSet;
import rotp.ui.util.RandomAlienRaces;
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

	private static final String PREFERENCES_FILE  = "Remnants.cfg";
	public  static final String GAME_OPTIONS_FILE = "Game.options";
	public  static final String LAST_OPTIONS_FILE = "Last.options";
	public  static final String USER_OPTIONS_FILE = "User.options";
	private static final int MAX_BACKUP_TURNS = 20; // modnar: change max turns between backups to 20
	private static final String keyFormat = "%-25s: "; // BR: from 20 to 25 for a better alignment

	// BR common for All MOD entries
	public static final String BASE_UI = "SETUP_";
	public static final String ADV_UI  = "SETTINGS_";
	public static final String MOD_UI  = "SETTINGS_MOD_";

	// MOD GUI OPTIONS: I don't likes informations spread everywhere... Then here they are!
	// BR: ===== First Mod GUI: 
	public static final ParamAAN2 artifactsHomeworld = new ParamAAN2("HOME_ARTIFACT");
	public static final ParamAAN2 fertileHomeworld	 = new ParamAAN2("HOME_FERTILE");
	public static final ParamAAN2 richHomeworld 	 = new ParamAAN2("HOME_RICH");
	public static final ParamAAN2 ultraRichHomeworld = new ParamAAN2("HOME_ULTRA_RICH");
	public static final ParamBoolean battleScout	 = new ParamBoolean(
			MOD_UI, "BATTLE_SCOUT", false);
	public static final ParamBoolean randomTechStart = new ParamBoolean(
			MOD_UI, "RANDOM_TECH_START", false);
	public static final ParamInteger companionWorlds = new ParamInteger(
			MOD_UI, "COMPANION_WORLDS"
			, 0, -4, 6, true);
	public static final ParamInteger retreatRestrictionTurns = new ParamInteger(
			MOD_UI, "RETREAT_RESTRICTION_TURNS"
			, 100, 0, 100, 1, 5, 20);
	public static final ParamList retreatRestrictions = new ParamList(
			MOD_UI, "RETREAT_RESTRICTIONS", "None")
			.put("None",	MOD_UI + "RETREAT_NONE")
			.put("AI",		MOD_UI + "RETREAT_AI")
			.put("Player",	MOD_UI + "RETREAT_PLAYER")
			.put("Both",	MOD_UI + "RETREAT_BOTH");
	public static final ParamInteger customDifficulty = new ParamInteger(
			MOD_UI, "CUSTOM_DIFFICULTY"
			, 100, 20, 500, 1, 5, 20);
	public static final ParamBoolean dynamicDifficulty = new ParamBoolean(
			MOD_UI, "DYNAMIC_DIFFICULTY", false);
	public static final ParamFloat missileSizeModifier = new ParamFloat(
			MOD_UI, "MISSILE_SIZE_MODIFIER"
			, 2f/3f, 0.1f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
	public static final ParamBoolean challengeMode = new ParamBoolean(
			MOD_UI, "CHALLENGE_MODE", false);

	// BR: ===== Second Mod GUI:
	public static final ParamBoolean maximizeSpacing = new ParamBoolean(
			MOD_UI, "MAX_SPACINGS", false);
	public static final ParamInteger spacingLimit	 = new ParamInteger(
			MOD_UI, "MAX_SPACINGS_LIM"
			, 16, 3, Rotp.maximumSystems-1, 1, 10, 100);
	public static final ParamInteger minStarsPerEmpire = new ParamInteger(
			MOD_UI, "MIN_STARS_PER_EMPIRE"
			, 3, 3, Rotp.maximumSystems-1, 1, 5, 20);
	public static final ParamInteger prefStarsPerEmpire	= new ParamInteger(
			MOD_UI, "PREF_STARS_PER_EMPIRE"
			, 10, 3, Rotp.maximumSystems-1, 1, 10, 100);
	public static final ParamBoolean restartChangesAliensAI	= new ParamBoolean(
			MOD_UI, "RESTART_CHANGES_ALIENS_AI", false);
	public static final ParamBoolean restartChangesPlayerAI	= new ParamBoolean(
			MOD_UI, "RESTART_CHANGES_PLAYER_AI", false);
	public static final ParamBoolean restartAppliesSettings	= new ParamBoolean(
			MOD_UI, "RESTART_APPLY_SETTINGS",false);
	public static final ParamList restartChangesPlayerRace	= new ParamList(
			MOD_UI, "RESTART_PLAYER_RACE", "Swap")
			.put("Last", MOD_UI + "RESTART_PLAYER_RACE_LAST")
			.put("Swap", MOD_UI + "RESTART_PLAYER_RACE_SWAP")
			.put("GuiSwap",	 MOD_UI + "RESTART_PLAYER_RACE_GUI_SWAP")
			.put("GuiLast",	 MOD_UI + "RESTART_PLAYER_RACE_GUI_LAST");

	public static final EventsStartTurn eventsStartTurn	= new EventsStartTurn(
			MOD_UI, "EVENTS_STARS_TURN"
			, RandomEvents.START_TURN, 1, null, 1, 5, 20);
	public static final ParamTech techIrradiated = new 
			ParamTech("TECH_IRRADIATED",	3, "ControlEnvironment",6); // level 18
	public static final ParamTech techCloaking	 = new 
			ParamTech("TECH_CLOAKING",		2, "Cloaking",			0); // level 27
	public static final ParamTech techStargate	 = new 
			ParamTech("TECH_STARGATES",		4, "Stargate", 			0); // level 27
	public static final ParamTech techHyperspace = new 
			ParamTech("TECH_HYPERSPACE",	0, "HyperspaceComm",	0); // level 34
	public static final ParamTech techIndustry2	 = new 
			ParamTech("TECH_INDUSTRY_2",	1, "ImprovedIndustrial",7); // level 38
	public static final ParamTech techThorium	 = new 
			ParamTech("TECH_THORIUM",		4, "FuelRange",			8); // level 41
	public static final ParamTech techTransport  = new 
			ParamTech("TECH_TRANSPORTERS",	4, "CombatTransporter",	0); // level 45
	public static final ParamInteger randomAlienRacesMin = new ParamInteger(
			MOD_UI, "RACES_RAND_MIN"
			, -50, -100, 100, 1, 5, 20);
	public static final ParamInteger randomAlienRacesMax = new ParamInteger(
			MOD_UI, "RACES_RAND_MAX"
			, 50, -100, 100, 1, 5, 20);
	public static final ParamInteger randomAlienRacesTargetMax = new ParamInteger(
			MOD_UI, "RACES_RAND_TARGET_MAX"
			, 75, null, null, 1, 10, 100);
	public static final ParamInteger randomAlienRacesTargetMin = new ParamInteger(
			MOD_UI, "RACES_RAND_TARGET_MIN"
			, 0, null, null, 1, 10, 100);
	public static final ParamBoolean randomAlienRacesSmoothEdges = new ParamBoolean(
			MOD_UI, "RACES_RAND_EDGES", true);
	public static final RandomAlienRaces randomAlienRaces = new RandomAlienRaces (
			MOD_UI, "RACES_ARE_RANDOM", "No");
	
	public static final LinkedList<ParamTech> techModList = new LinkedList<>(Arrays.asList(
			techIrradiated, techCloaking, techStargate, techHyperspace,
			techIndustry2, techThorium, techTransport
			));

	// BR: ===== Global settings Mod GUI:
	private static boolean gamePlayed = false; // to differentiate startup from loaded game
	private static boolean loadRequest = false; // to Load options requested in menu
	public static final ParamOptions menuSpecial = new ParamOptions("");

	public static final ParamFloat showFleetFactor = new ParamFloat(
			MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%");
	public static final ParamFloat showFlagFactor = new ParamFloat(
			MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%");
	public static final ParamFloat showPathFactor = new ParamFloat(
			MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%");
	public static final ParamInteger showNameMinFont = new ParamInteger(
			MOD_UI, "SHOW_NAME_MIN_FONT"
			, 8, 2, 24, 1, 2, 5);
	public static final ParamFloat showInfoFontRatio = new ParamFloat(
			MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%");
	public static final ParamFloat mapFontFactor = new ParamFloat(
			MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%");
	public static final ParamOptions menuStartup = new ParamOptions(
			MOD_UI, "MENU_STARTUP", ParamOptions.VANILLA);
	public static final ParamOptions menuLoadGame = new ParamOptions(
			MOD_UI, "MENU_LOAD_GAME", ParamOptions.VANILLA);
	public static final ParamBoolean showGridCircular = new ParamBoolean(
			MOD_UI, "SHOW_GRID_CIRCULAR", false);
	public static final ParamBoolean showTooltips = new ParamBoolean(
			MOD_UI, "SHOW_TOOLTIPS", true);

	// This list is used as is by the ModGlobalOptionsUI menu
	public static final LinkedList<InterfaceParam> modGlobalOptionsUI = new LinkedList<>(
			Arrays.asList(
			menuStartup, menuLoadGame, showGridCircular, showTooltips,
			showFleetFactor, showFlagFactor, showPathFactor,
			showNameMinFont, showInfoFontRatio, mapFontFactor
			));
	// BR: Galaxy Menu addition
	public static final ParamBoolean showNewRaces = new ParamBoolean(
			MOD_UI, "SHOW_NEW_RACES", false);
	// BR: Race Menu addition
	public static final PlayerShipSet playerShipSet = new PlayerShipSet(
			MOD_UI, "PLAYER_SHIP_SET");
	public static final ParamBoolean playerIsCustom = new ParamBoolean(
			BASE_UI, "BUTTON_CUSTOM_PLAYER_RACE", false);
	public static final ParamCR  playerCustomRace = new ParamCR(
			MOD_UI, "PLAYER_CR");

	private static boolean showMemory = false;
	private static boolean playMusic = true;
	private static boolean playSounds = true;
	private static int musicVolume = 10;
	private static int soundVolume = 10;
	private static int defaultMaxBases = 0;
	private static boolean displayYear = false;
	private static boolean governorOnByDefault = true; // BR:
	private static boolean governorAutoSpendByDefault = false;
	private static boolean legacyGrowth = true; // BR:
	private static boolean governorAutoApply = true; // BR:
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
	public static void setForNewGame() {
		autoColonize = false;
		autoBombardMode = AUTOBOMBARD_NO;
	}
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
	// BR: redirection for compatibility
	public static int companionWorlds()			{ return Math.abs(companionWorlds.get()); } // modnar: add option to start game with additional colonies
	public static int companionWorldsSigned()	{ return companionWorlds.get(); } // BR: to manage old and new distribution
	public static float missileSizeModifier()	{ return missileSizeModifier.get(); } 
	public static int retreatRestrictions()		{ return retreatRestrictions.getIndex(); }
	public static int retreatRestrictionTurns()	{ return retreatRestrictionTurns.get(); }
	// \BR:
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
	public static boolean disableAdvisor()	{ return disableAdvisor; }
	public static void uiTexturePct(int i)	{ uiTexturePct = i / 100.0f; }
	public static float uiTexturePct()		{ return uiTexturePct; }
	public static boolean gamePlayed()		{return gamePlayed; }
	public static void gamePlayed(boolean played) { gamePlayed = played; }
	public static boolean loadRequest()			  {return loadRequest; }
	public static void loadRequest(boolean load)  { loadRequest = load; }
	
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
			out.println("===== Governor Settings =====");
			out.println();
			out.println(keyFormat("DEFAULT_MAX_BASES") + defaultMaxBases);
			out.println(keyFormat("GOVERNOR_ON_BY_DEFAULT") + yesOrNo(governorOnByDefault));
			out.println(keyFormat("AUTOSPEND_ON_BY_DEFAULT") + yesOrNo(governorAutoSpendByDefault));
			out.println(keyFormat("DIVERT_COLONY_EXCESS_TO_RESEARCH")+ yesOrNo(divertColonyExcessToResearch));
			out.println(keyFormat("LEGACY_GROWTH") + yesOrNo(legacyGrowth)); // BR:
			out.println(keyFormat("GOVERNOR_AUTO_APPLY") + yesOrNo(governorAutoApply)); // BR:

			out.println();
			out.println("===== MOD Global GUI Settings =====");
			out.println();
			for (InterfaceParam param : modGlobalOptionsUI) {
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
			case "LANGUAGE": selectLanguage(val); return;
			default:
			// BR: Global Mod GUI
				for (InterfaceParam param : modGlobalOptionsUI) {
					if (key.equalsIgnoreCase(param.getCfgLabel()))
						param.setFromCfgValue(val);
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
