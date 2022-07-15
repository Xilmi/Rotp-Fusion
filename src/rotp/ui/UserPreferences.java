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
package rotp.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import rotp.Rotp;
import rotp.model.game.GameSession;
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
    private static final String keyFormat = "%-20s: ";
    private static boolean showMemory = false;
    private static boolean playMusic = true;
    private static boolean playSounds = true;
    private static int musicVolume = 10;
    private static int soundVolume = 10;
    private static int defaultMaxBases = 0;
    private static boolean displayYear = false;
    private static boolean governorOnByDefault = true;
    private static boolean governorAutoSpendByDefault = false;
    private static int customDifficulty = 100; // mondar: add custom difficulty level option, in units of percent
    private static boolean dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
    private static boolean alwaysStarGates = false; // modnar: add option to always have Star Gates tech
    private static boolean alwaysThorium = false; // modnar: add option to always have Thorium Cells tech
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
    private static String texturesMode = TEXTURES_MAP; // modnar: set default texture to only map, no interface
    private static String sensitivityMode = SENSITIVITY_MEDIUM;
    private static String saveDir = "";
    private static float uiTexturePct = 0.20f;
    private static int screenSizePct = 93;
    private static int backupTurns = 5; // modnar: change default turns between backups to 5
    private static float showFleetFactor   = 1.0f; // BR: adjust the Galaxy Map fleet disappearance
    private static float showFlagFactor    = 1.0f; // BR: adjust the Galaxy Map flag disappearance
    private static float showPathFactor    = 1.0f; // BR: adjust the Galaxy Map Path disappearance
    private static int   showNameMinFont   = 8;    // BR: adjust the Galaxy Map Star Name disappearance
    private static float showInfoFontRatio = 0.7f; // BR: adjust the Galaxy Map swap info to name only
    private static float mapFontFactor     = 1.0f; // BR: adjust the Galaxy Map Font Size

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
        alwaysStarGates = false; // modnar: add option to always have Star Gates tech
        alwaysThorium = false; // modnar: add option to always have Thorium Cells tech
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
        showFleetFactor   = 1.0f; // BR: adjust the Galaxy Map fleet disappearance
        showFlagFactor    = 1.0f; // BR: adjust the Galaxy Map flag disappearance
        showPathFactor    = 1.0f; // BR: adjust the Galaxy Map Path disappearance
        showNameMinFont   = 8;    // BR: adjust the Galaxy Map Star Name disappearance
        showInfoFontRatio = 0.7f; // BR: adjust the Galaxy Map swap info to name only
        mapFontFactor     = 1.0f; // BR: adjust the Colony Galaxy Map Font Size
        save();
    }
    // modnar: set MOD option to defaults, specifically for UI
    public static void setModToDefault() {
        customDifficulty = 180; // mondar: add custom difficulty level option, in units of percent
        dynamicDifficulty = false; // modnar: add dynamic difficulty option, change AI colony production
        alwaysStarGates = false; // modnar: add option to always have Star Gates tech
        alwaysThorium = false; // modnar: add option to always have Thorium Cells tech
        challengeMode = false; // modnar: add option to give AI more initial resources
        randomTechStart = false; // modnar: add option to start all Empires with 2 techs, no Artifacts
        battleScout = false; // modnar: add battleScout option to give player super Scout design
        companionWorlds = 0; // modnar: add option to start game with additional colonies
        missileSizeModifier = 2.0f/3.0f;
        retreatRestrictions = 0;
        retreatRestrictionTurns = 100;
        save();
    }
    public static void setForNewGame() {
        autoColonize = false;
        autoBombardMode = AUTOBOMBARD_NO;
        save();
    }
    
    // BR setters and getter for Show Factors
    /**
     * This Factor is used to adjust the fleet disappearance.
     * The bigger the value, the longer it's shown.
     * @param newValue between 0.5 and 2.0
     */
    public static void setShowFleetFactor(float newValue) {
    	float min = 0.5f;
    	float max = 2.0f;
    	showFleetFactor = Math.min(max, Math.max(min, newValue));
    }
    /**
     * This factor is used to adjust the fleet disappearance.
     * The bigger the value, the longer it's shown.
     * @return Scale factor between 0.5 and 2.0
     */
    public static Float getShowFleetFactor() {
    	return showFleetFactor;
    }
    /**
     * This Factor is used to adjust the flag disappearance.
     * The bigger the value, the longer it's shown.
     * @param newValue between 0.5 and 2.0
     */
    public static void setShowFlagFactor(float newValue) {
    	float min = 0.5f;
    	float max = 2.0f;
    	showFlagFactor = Math.min(max, Math.max(min, newValue));
    }
    /**
     * This factor is used to adjust the flag disappearance.
     * The bigger the value, the longer it's shown.
     * @return Scale factor between 0.5 and 2.0
     */
    public static Float getShowFlagFactor() {
    	return showFlagFactor;
    }
    /**
     * This Factor is used to adjust the path disappearance.
     * The bigger the value, the longer it's shown.
     * @param newValue between 0.5 and 2.0
     */
    public static void setShowPathFactor(float newValue) {
    	float min = 0.5f;
    	float max = 2.0f;
    	showPathFactor = Math.min(max, Math.max(min, newValue));
    }
    /**
     * This factor is used to adjust the path disappearance.
     * The bigger the value, the longer it's shown.
     * @return Scale factor between 0.5 and 2.0
     */
    public static Float getShowPathFactor() {
    	return showPathFactor;
    }
    /**
     * This Factor is used to adjust the Star Name disappearance.
     * The smaller the value, the longer it's shown.
     * @param newValue between 2 and 24
     */
    public static void setShowNameMinFont(int newValue) {
    	int min = 2;
    	int max = 24;
    	showNameMinFont = Math.min(max, Math.max(min, newValue));
    }
    /**
     * This factor is used to adjust the Star Name disappearance.
     * The smaller the value, the longer it's shown.
     * @return Scale factor between 2 and 24
     */
    public static Integer getShowNameMinFont() {
    	return showNameMinFont;
    }
    /**
     * This Factor is used to adjust the info box disappearance.
     * The bigger the value, the longer it's shown.
     * @param newValue between 0.25 and 2.0
     */
    public static void setShowInfoFontRatio(float newValue) {
    	float min = 0.25f;
    	float max = 2.0f;
    	showInfoFontRatio = Math.min(max, Math.max(min, newValue));
    }
    /**
     * This Factor is used to adjust the info box disappearance.
     * The bigger the value, the longer it's shown.
      * @return Scale factor between 0.25 and 2.0
     */
    public static Float getShowInfoFontRatio() {
    	return showInfoFontRatio;
    }
    /**
     * To adjust the Galaxy Map Font Size (Multiplier)
      * @param newValue between 0.5 and 2.0
     */
    public static void setMapFontFactor(float newValue) {
    	float min = 0.5f;
    	float max = 2.0f;
    	mapFontFactor = Math.min(max, Math.max(min, newValue));
    }
    /**
     * To adjust the Galaxy Map Font Size (Multiplier)
     * @return Scale factor between 0.5 and 2.0
     */
    public static Float getMapFontFactor() {
    	return mapFontFactor;
    }
    // \BR

    // BR setters for modnar parameters
    public static void setAlwaysStarGates(boolean newValue) {
    	alwaysStarGates = newValue;
    }
    public static void setAlwaysThorium(boolean newValue) {
    	alwaysThorium = newValue;
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
    } // \BR

    // modnar: MOD option toggles, specifically for UI
    public static void toggleAlwaysStarGates()       { alwaysStarGates = !alwaysStarGates; save(); }
    public static void toggleAlwaysThorium()         { alwaysThorium = !alwaysThorium; save(); }
    public static void toggleChallengeMode()         { challengeMode = !challengeMode; save(); }
    public static void toggleBattleScout()           { battleScout = !battleScout; save(); }
    public static void toggleCompanionWorlds(boolean up) {// BR: made bidirectional
    	if (up) {
	        if ((companionWorlds >= 6) || (companionWorlds < -4)) // BR: changed to 6; default = 4
	            companionWorlds = -4; // BR: changed to -4; default = 0
	        else
	            companionWorlds++;
    	} else {
	        if ((companionWorlds >= 6) || (companionWorlds < -4)) // BR: changed to 6; default = 4
	            companionWorlds = 6; // BR: changed to -4; default = 0
	        else
	            companionWorlds--;    		
    	}
        save();
    }
    public static void toggleRandomTechStart()       { randomTechStart = !randomTechStart; save(); }
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
    public static void toggleDynamicDifficulty()     { dynamicDifficulty = !dynamicDifficulty; save(); }

    public static int musicVolume()         { return musicVolume; }
    public static int soundVolume()         { return soundVolume; }
    public static boolean showMemory()      { return showMemory; }
    public static void toggleMemory()       { showMemory = !showMemory; save(); }
    public static boolean fullScreen()      { return displayMode.equals(FULLSCREEN_MODE); }
    public static boolean windowed()        { return displayMode.equals(WINDOW_MODE); }
    public static boolean borderless()      { return displayMode.equals(BORDERLESS_MODE); }
    public static String displayMode()      { return displayMode; }
    public static void toggleDisplayMode()   {
        switch(displayMode) {
            case WINDOW_MODE:     displayMode = BORDERLESS_MODE; break;
            case BORDERLESS_MODE: displayMode = FULLSCREEN_MODE; break;
            case FULLSCREEN_MODE: displayMode = WINDOW_MODE; break;
            default:              displayMode = WINDOW_MODE; break;
        }
        save();
    }
    public static String graphicsMode()     { return graphicsMode; }
    public static void toggleGraphicsMode()   {
        switch(graphicsMode) {
            case GRAPHICS_HIGH:   graphicsMode = GRAPHICS_MEDIUM; break;
            case GRAPHICS_MEDIUM: graphicsMode = GRAPHICS_LOW; break;
            case GRAPHICS_LOW:    graphicsMode = GRAPHICS_HIGH; break;
            default :             graphicsMode = GRAPHICS_HIGH; break;
        }
        save();
    }
    public static void toggleTexturesMode()   {
        switch(texturesMode) {
            case TEXTURES_NO:        texturesMode = TEXTURES_INTERFACE; break;
            case TEXTURES_INTERFACE: texturesMode = TEXTURES_MAP; break;
            case TEXTURES_MAP:       texturesMode = TEXTURES_BOTH; break;
            case TEXTURES_BOTH:      texturesMode = TEXTURES_NO; break;
            default :                texturesMode = TEXTURES_BOTH; break;
        }
        save();
    }
    public static String texturesMode()     { return texturesMode; }
    public static boolean texturesInterface() { return texturesMode.equals(TEXTURES_INTERFACE) || texturesMode.equals(TEXTURES_BOTH); }
    public static boolean texturesMap()       { return texturesMode.equals(TEXTURES_MAP) || texturesMode.equals(TEXTURES_BOTH); }

    public static void toggleSensitivityMode()   {
        switch(sensitivityMode) {
            case SENSITIVITY_LOW:       sensitivityMode = SENSITIVITY_MEDIUM; break;
            case SENSITIVITY_MEDIUM:    sensitivityMode = SENSITIVITY_HIGH; break;
            case SENSITIVITY_HIGH:      sensitivityMode = SENSITIVITY_LOW; break;
            default :                   sensitivityMode = SENSITIVITY_MEDIUM; break;
        }
        save();
    }
    public static String sensitivityMode()     { return sensitivityMode; }
    public static boolean sensitivityHigh()    { return sensitivityMode.equals(SENSITIVITY_HIGH); }
    public static boolean sensitivityMedium()  { return sensitivityMode.equals(SENSITIVITY_MEDIUM); }
    public static boolean sensitivityLow()     { return sensitivityMode.equals(SENSITIVITY_LOW); }

    public static String autoColonizeMode()     { return autoColonize ? AUTOCOLONIZE_YES : AUTOCOLONIZE_NO; }
    public static void toggleAutoColonize()     { autoColonize = !autoColonize; save();  }
    public static boolean autoColonize()        { return autoColonize; }

    public static void toggleAutoBombard()     {
        switch(autoBombardMode) {
            case AUTOBOMBARD_NO:     autoBombardMode = AUTOBOMBARD_NEVER; break;
            case AUTOBOMBARD_NEVER:  autoBombardMode = AUTOBOMBARD_YES; break;
            case AUTOBOMBARD_YES:    autoBombardMode = AUTOBOMBARD_WAR; break;
            case AUTOBOMBARD_WAR:    autoBombardMode = AUTOBOMBARD_INVADE; break;
            case AUTOBOMBARD_INVADE: autoBombardMode = AUTOBOMBARD_NO; break;
            default:                 autoBombardMode = AUTOBOMBARD_NO; break;
        }
        save();
    }
    public static String autoBombardMode()        { return autoBombardMode; }
    public static boolean autoBombardNo()         { return autoBombardMode.equals(AUTOBOMBARD_NO); }
    public static boolean autoBombardNever()      { return autoBombardMode.equals(AUTOBOMBARD_NEVER); }
    public static boolean autoBombardYes()        { return autoBombardMode.equals(AUTOBOMBARD_YES); }
    public static boolean autoBombardWar()        { return autoBombardMode.equals(AUTOBOMBARD_WAR); }
    public static boolean autoBombardInvading()   { return autoBombardMode.equals(AUTOBOMBARD_INVADE); }

    public static boolean playAnimations()  { return !graphicsMode.equals(GRAPHICS_LOW); }
    public static boolean antialiasing()    { return graphicsMode.equals(GRAPHICS_HIGH); }
    public static boolean playSounds()      { return playSounds; }
    public static void toggleSounds()       { playSounds = !playSounds;	save(); }
    public static boolean playMusic()       { return playMusic; }
    public static void toggleMusic()        { playMusic = !playMusic; save();  }
    public static int customDifficulty()     { return customDifficulty; } // mondar: add custom difficulty level option, in units of percent
    public static boolean dynamicDifficulty() { return dynamicDifficulty; } // modnar: add dynamic difficulty option, change AI colony production
    public static boolean alwaysStarGates()  { return alwaysStarGates; } // modnar: add option to always have Star Gates tech
    public static boolean alwaysThorium()    { return alwaysThorium; } // modnar: add option to always have Thorium Cells tech
    public static boolean challengeMode()    { return challengeMode; } // modnar: add option to give AI more initial resources
    public static boolean randomTechStart()  { return randomTechStart; } // modnar: add option to start all Empires with 2 techs, no Artifacts
    public static boolean battleScout()      { return battleScout; } // modnar: add battleScout option to give player super Scout design
    public static int companionWorlds()      { return Math.abs(companionWorlds); } // modnar: add option to start game with additional colonies
    public static int companionWorldsSigned() { return companionWorlds; } // BR: to manage old and new distribution
    public static float missileSizeModifier() { return missileSizeModifier; } 
    public static int retreatRestrictions() { return retreatRestrictions; }
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
    public static String saveDir()          { return saveDir; }
    public static void saveDir(String s)    { saveDir = s; save(); }
    public static String saveDirStr()       {
        if (saveDir.isEmpty())
            return SAVEDIR_DEFAULT;
        else
            return SAVEDIR_CUSTOM;
    }
    public static int backupTurns()         { return backupTurns; }
    public static boolean backupTurns(int i)   {
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
    public static void toggleYearDisplay()    { displayYear = !displayYear; save(); }
    public static boolean displayYear()       { return displayYear; }
    public static void setDefaultMaxBases(int bases)    { defaultMaxBases = bases; }
    public static int defaultMaxBases()    { return defaultMaxBases; }
    public static void setGovernorOn(boolean governorOn)    { governorOnByDefault = governorOn; save(); }
    public static boolean governorOnByDefault() { return governorOnByDefault; }
    public static void setAutoSpendOn(boolean autospendOn)  { governorAutoSpendByDefault = autospendOn; save(); }
    public static boolean governorAutoSpendByDefault() { return governorAutoSpendByDefault; }
    public static void setDivertColonyExcessToResearch(boolean divertOn)  {divertColonyExcessToResearch = divertOn; save(); }
    public static boolean divertColonyExcessToResearch()  { return divertColonyExcessToResearch; }
    public static boolean disableAdvisor() { return disableAdvisor; }
    public static void uiTexturePct(int i)    { uiTexturePct = i / 100.0f; }
    public static float uiTexturePct()        { return uiTexturePct; }
    
    public static void loadAndSave() {
        load();
        save();
    }
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
            out.println(keyFormat("DEFAULT_MAX_BASES") + defaultMaxBases);
            out.println(keyFormat("GOVERNOR_ON_BY_DEFAULT") + yesOrNo(governorOnByDefault));
            out.println(keyFormat("AUTOSPEND_ON_BY_DEFAULT") + yesOrNo(governorAutoSpendByDefault));
            out.println(keyFormat("DIVERT_COLONY_EXCESS_TO_RESEARCH")+ yesOrNo(divertColonyExcessToResearch));
            out.println(keyFormat("DISABLE_ADVISOR")+ yesOrNo(disableAdvisor));
            out.println(keyFormat("SCREEN_SIZE_PCT")+ screenSizePct());
            out.println(keyFormat("UI_TEXTURE_LEVEL")+(int) (uiTexturePct()*100));
            out.println(keyFormat("CUSTOM_DIFFICULTY")+ customDifficulty); // mondar: add custom difficulty level option, in units of percent
            out.println(keyFormat("DYNAMIC_DIFFICULTY")+ yesOrNo(dynamicDifficulty)); // modnar: add dynamic difficulty option, change AI colony production
            out.println(keyFormat("ALWAYS_STAR_GATES")+ yesOrNo(alwaysStarGates)); // modnar: add option to always have Star Gates tech
            out.println(keyFormat("ALWAYS_THORIUM")+ yesOrNo(alwaysThorium)); // modnar: add option to always have Thorium Cells tech
            out.println(keyFormat("CHALLENGE_MODE")+ yesOrNo(challengeMode)); // modnar: add option to give AI more initial resources
            out.println(keyFormat("RANDOM_TECH_START")+ yesOrNo(randomTechStart)); // modnar: add option to start all Empires with 2 techs, no Artifacts
            out.println(keyFormat("BATTLE_SCOUT")+ yesOrNo(battleScout)); // modnar: add battleScout option to give player super Scout design
            out.println(keyFormat("COMPANION_WORLDS")+ companionWorlds); // modnar: add option to start game with additional colonies
            out.println(keyFormat("MISSILE_SIZE_MODIFIER")+ missileSizeModifier);
            out.println(keyFormat("RETREAT_RESTRICTIONS")+ retreatRestrictions);
            out.println(keyFormat("RETREAT_RESTRICTION_TURNS")+ retreatRestrictionTurns);
            out.println(keyFormat("LANGUAGE")+ languageDir());
            out.println(keyFormat("SHOW_FLEET_FACTOR")    + getShowFleetFactor().toString());   // BR:
            out.println(keyFormat("SHOW_FLAG_FACTOR")     + getShowFlagFactor().toString());    // BR:
            out.println(keyFormat("SHOW_PATH_FACTOR")     + getShowPathFactor().toString());    // BR:
            out.println(keyFormat("SHOW_NAME_MIN_FONT")   + getShowNameMinFont().toString());   // BR:
            out.println(keyFormat("SHOW_INFO_FONT_RATIO") + getShowInfoFontRatio().toString()); // BR:
            out.println(keyFormat("MAP_FONT_FACTOR")      + getMapFontFactor().toString());     // BR:
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
            case "DISPLAY_MODE":  displayMode = displayModeFromSettingName(val); return;
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
            case "GOVERNOR_ON_BY_DEFAULT": governorOnByDefault = yesOrNo(val); return;
            case "AUTOSPEND_ON_BY_DEFAULT": governorAutoSpendByDefault = yesOrNo(val); return;
            case "DIVERT_COLONY_EXCESS_TO_RESEARCH": divertColonyExcessToResearch = yesOrNo(val); return;
            case "DISABLE_ADVISOR": disableAdvisor = yesOrNo(val); return;
            case "SCREEN_SIZE_PCT": screenSizePct(Integer.valueOf(val)); return;
            case "UI_TEXTURE_LEVEL": uiTexturePct(Integer.valueOf(val)); return;
            case "CUSTOM_DIFFICULTY": setCustomDifficulty(val); return; // mondar: add custom difficulty level option, in units of percent
            case "DYNAMIC_DIFFICULTY": dynamicDifficulty = yesOrNo(val); return; // modnar: add dynamic difficulty option, change AI colony production
            case "ALWAYS_STAR_GATES": alwaysStarGates = yesOrNo(val); return; // modnar: add option to always have Star Gates tech
            case "ALWAYS_THORIUM": alwaysThorium = yesOrNo(val); return; // modnar: add option to always have Thorium Cells tech
            case "CHALLENGE_MODE": challengeMode = yesOrNo(val); return; // modnar: add option to give AI more initial resources
            case "RANDOM_TECH_START": randomTechStart = yesOrNo(val); return; // modnar: add option to start all Empires with 2 techs, no Artifacts
            case "BATTLE_SCOUT": battleScout = yesOrNo(val); return; // modnar: add battleScout option to give player super Scout design
            case "COMPANION_WORLDS": setNumCompanionWorlds(val); return; // modnar: add option to start game with additional colonies
            case "MISSILE_SIZE_MODIFIER": setMissileSizeModifier(val); return;
            case "RETREAT_RESTRICTIONS": setRetreatRestrictions(val); return;
            case "RETREAT_RESTRICTION_TURNS": setRetreatRestrictionTurns(val); return;
            case "LANGUAGE":     selectLanguage(val); return;
            case "SHOW_FLEET_FACTOR":    setShowFleetFactor(stringToFloat(val));   return; // BR:
            case "SHOW_FLAG_FACTOR":     setShowFlagFactor(stringToFloat(val));    return; // BR:
            case "SHOW_PATH_FACTOR":     setShowPathFactor(stringToFloat(val));    return; // BR:
            case "SHOW_NAME_MIN_FONT":   setShowNameMinFont(stringToInteger(val)); return; // BR:
            case "SHOW_INFO_FONT_RATIO": setShowInfoFontRatio(stringToFloat(val)); return; // BR:
            case "MAP_FONT_FACTOR":      setMapFontFactor(stringToFloat(val));     return; // BR:
           default:
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
    // BR:
    /**
     * Convert String to Float and manage errors
     * @param string Source of Float
     * @return Float value, or <b>null</b> on error
     */
    public static Float stringToFloat(String string) {
    	try {
    		return Float.valueOf(string.trim());
        }
        catch (NumberFormatException nfe) {
        	return null; // silent error!
        }
    } // \BR:
    // BR:
    /**
     * Convert String to Integer and manage errors
     * @param string Source of Integer
     * @return Integer value, or <b>null</b> on error
     */
    public static Integer stringToInteger(String string) {
    	try {
    		return Integer.valueOf(string.trim());
        }
        catch (NumberFormatException nfe) {
        	return null; // silent error!
        }
    } // \BR:
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
            case WINDOW_MODE:     return "Windowed";
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
            case GRAPHICS_LOW:    return "Low";
            case GRAPHICS_MEDIUM: return "Medium";
            case GRAPHICS_HIGH:   return "High";
        }
        return "High";
    }
    public static String graphicsModeFromSettingName(String s) {
        switch(s) {
            case "Low":    return GRAPHICS_LOW;
            case "Medium": return GRAPHICS_MEDIUM;
            case "High":   return GRAPHICS_HIGH;
        }
        return GRAPHICS_HIGH;
    }
    public static String autoBombardToSettingName(String s) {
        switch(s) {
            case AUTOBOMBARD_NO:     return "No";
            case AUTOBOMBARD_NEVER:  return "Never";
            case AUTOBOMBARD_YES:    return "Yes";
            case AUTOBOMBARD_WAR:    return "War";
            case AUTOBOMBARD_INVADE: return "Invade";
        }
        return "No";
    }
    public static String autoBombardFromSettingName(String s) {
        switch(s) {
            case "No":     return AUTOBOMBARD_NO;
            case "Never":  return AUTOBOMBARD_NEVER;
            case "Yes":    return AUTOBOMBARD_YES;
            case "War":    return AUTOBOMBARD_WAR;
            case "Invade": return AUTOBOMBARD_INVADE;
        }
        return AUTOBOMBARD_NO;
    }
    public static String texturesToSettingName(String s) {
        switch(s) {
            case TEXTURES_NO:         return "No";
            case TEXTURES_INTERFACE: return "Interface";
            case TEXTURES_MAP:       return "Map";
            case TEXTURES_BOTH:      return "Both";
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
    public static void increaseMusicLevel()    {
        musicVolume = Math.min(10, musicVolume+1);
        SoundManager.current().resetMusicVolumes();
        save();
    }
    public static void decreaseMusicLevel()    {
        musicVolume = Math.max(0, musicVolume-1);
        SoundManager.current().resetMusicVolumes();
        save();
    };
    public static void increaseSoundLevel()    {
        soundVolume = Math.min(10, soundVolume+1);
        SoundManager.current().resetSoundVolumes();
        save();
    }
    public static void decreaseSoundLevel()    {
        soundVolume = Math.max(0, soundVolume-1);
        SoundManager.current().resetSoundVolumes();
        save();
    }
}
