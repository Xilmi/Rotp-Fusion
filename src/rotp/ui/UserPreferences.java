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

import static rotp.model.game.IConvenienceOptions.AUTOBOMBARD_INVADE;
import static rotp.model.game.IConvenienceOptions.AUTOBOMBARD_NEVER;
import static rotp.model.game.IConvenienceOptions.AUTOBOMBARD_NO;
import static rotp.model.game.IConvenienceOptions.AUTOBOMBARD_WAR;
import static rotp.model.game.IConvenienceOptions.AUTOBOMBARD_YES;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;

import rotp.Rotp;
import rotp.model.game.GameSession;
import rotp.model.game.IMainOptions;
import rotp.model.game.IModOptions;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamString;
import rotp.util.LanguageManager;

//public class UserPreferences implements RemnantOptions {
public class UserPreferences implements IMainOptions {

	private static final String PREFERENCES_FILE  = "Remnants.cfg";
	public  static final String GALAXY_TEXT_FILE  = "Galaxy.txt";
	private static final String keyFormat = "%-25s: "; // BR: from 20 to 25 for a better alignment

	private static boolean showMemory  = false;
	private static boolean playMusic   = true;
	private static boolean playSounds  = true;
	private static int musicVolume     = 10;
	private static int soundVolume     = 10;
	private static int defaultMaxBases = 0;
	private static boolean displayYear = false;
	private static boolean governorOnByDefault        = true; // BR:
	private static boolean governorAutoSpendByDefault = false;
	private static boolean legacyGrowth      = true; // BR:
	private static boolean governorAutoApply = true; // BR:
	private static boolean autoColonize      = false;
	private static boolean divertColonyExcessToResearch = true;
	private static boolean disableAdvisor = true;
	private static String autoBombardMode = AUTOBOMBARD_NO;
	private static String displayMode     = WINDOW_MODE;
	private static String graphicsMode    = GRAPHICS_HIGH;
	private static String texturesMode    = TEXTURES_BOTH;
	private static String sensitivityMode = SENSITIVITY_MEDIUM;
	private static String saveDir = "";
	private static float uiTexturePct = 0.20f;
	private static int screenSizePct = 93;
	private static int selectedScreen = -1; // BR: to specify the destination display
	private static int backupTurns = 5; // modnar: change default turns between backups to 5
	private static boolean initialList = true;
	private static LinkedList<IParam> optionList;
	private static LinkedList<IParam> optionList() {
		if (optionList == null)
			optionList = IModOptions.globalOptions(initialList);
		return optionList;
	}
	public static void initialList(boolean initial) {
		initialList = initial;
		optionList = IModOptions.globalOptions(initialList);
		save();
	}

	/**
	 * Advanced setting GUI Default Button Action
	 */
//	public static void setToDefault() {
//		autoColonize = false;
//		autoBombardMode = AUTOBOMBARD_NO;
//		displayMode = WINDOW_MODE;
//		graphicsMode = GRAPHICS_HIGH;
//		texturesMode = TEXTURES_BOTH;
//		sensitivityMode = SENSITIVITY_MEDIUM;
//		screenSizePct = 93;
//		selectedScreen = -1;
//		backupTurns = 5; // modnar: change default turns between backups to 5
//		saveDir = "";
//		uiTexturePct = 0.20f;
//		showMemory = false;
//		if (!playMusic)
//			SoundManager.current().toggleMusic();
//		if (!playSounds)
//			SoundManager.current().toggleSounds();
//		musicVolume = 10;
//		soundVolume = 10;
//		SoundManager.current().resetSoundVolumes();
//		save();
//	}
	public static void setForNewGame() {
		autoColonize = false;
		autoBombardMode = AUTOBOMBARD_NO;
	}
	public static void musicVolume(int i)    { musicVolume = i; }
	public static int musicVolume()		     { return musicVolume; }
	public static void soundVolume(int i)    { soundVolume = i; }
	public static int soundVolume()		     { return soundVolume; }
	public static void showMemory(boolean b) { showMemory = b; }
	public static boolean showMemory()	     { return showMemory; }
//	public static void toggleMemory()	     { showMemory = !showMemory; save(); }
	public static boolean fullScreen()	     { return displayMode.equals(FULLSCREEN_MODE); }
	public static boolean windowed()	     { return displayMode.equals(WINDOW_MODE); }
	public static boolean borderless()	     { return displayMode.equals(BORDERLESS_MODE); }
	public static String displayMode()	     { return displayMode; }
	public static void displayMode(String s) { displayMode = s; }
//	public static void toggleDisplayMode()   {
//		switch(displayMode) {
//			case WINDOW_MODE:	  displayMode = BORDERLESS_MODE; break;
//			case BORDERLESS_MODE: displayMode = FULLSCREEN_MODE; break;
//			case FULLSCREEN_MODE: displayMode = WINDOW_MODE; break;
//			default:			  displayMode = WINDOW_MODE; break;
//		}
//		save();
//	}
	public static void graphicsMode(String s) { graphicsMode = s; }
	public static String graphicsMode()	      { return graphicsMode; }
//	public static void toggleGraphicsMode()   {
//		switch(graphicsMode) {
//			case GRAPHICS_HIGH:   graphicsMode = GRAPHICS_MEDIUM; break;
//			case GRAPHICS_MEDIUM: graphicsMode = GRAPHICS_LOW; break;
//			case GRAPHICS_LOW:	  graphicsMode = GRAPHICS_HIGH; break;
//			default :			  graphicsMode = GRAPHICS_HIGH; break;
//		}
//		save();
//	}
//	public static void toggleTexturesMode()   {
//		switch(texturesMode) {
//			case TEXTURES_NO:		 texturesMode = TEXTURES_INTERFACE; break;
//			case TEXTURES_INTERFACE: texturesMode = TEXTURES_MAP; break;
//			case TEXTURES_MAP:	     texturesMode = TEXTURES_BOTH; break;
//			case TEXTURES_BOTH:	     texturesMode = TEXTURES_NO; break;
//			default :				 texturesMode = TEXTURES_BOTH; break;
//		}
//		save();
//	}
	public static void texturesMode(String s) { texturesMode = s; }
	public static String texturesMode()	      { return texturesMode; }
	public static boolean texturesInterface() { return texturesMode.equals(TEXTURES_INTERFACE) || texturesMode.equals(TEXTURES_BOTH); }
	public static boolean texturesMap()	      { return texturesMode.equals(TEXTURES_MAP) || texturesMode.equals(TEXTURES_BOTH); }

//	public static void toggleSensitivityMode()   {
//		switch(sensitivityMode) {
//			case SENSITIVITY_LOW:	 sensitivityMode = SENSITIVITY_MEDIUM;	break;
//			case SENSITIVITY_MEDIUM: sensitivityMode = SENSITIVITY_HIGH;	break;
//			case SENSITIVITY_HIGH:	 sensitivityMode = SENSITIVITY_LOW;		break;
//			default :				 sensitivityMode = SENSITIVITY_MEDIUM;	break;
//		}
//		save();
//	}
	public static void sensitivityMode(String s) { sensitivityMode = s; }
	public static String sensitivityMode()	     { return sensitivityMode; }
	// public static boolean sensitivityHigh()	 { return sensitivityMode.equals(SENSITIVITY_HIGH); }
	public static boolean sensitivityMedium()    { return sensitivityMode.equals(SENSITIVITY_MEDIUM); }
	public static boolean sensitivityLow()	     { return sensitivityMode.equals(SENSITIVITY_LOW); }

	// public static String autoColonizeMode()	  { return autoColonize ? AUTOCOLONIZE_YES : AUTOCOLONIZE_NO; }
//	public static void toggleAutoColonize()	  { autoColonize = !autoColonize; save();  }
	public static boolean getAutoColonize()	  { return autoColonize; }
//	public static void autoColonize(boolean val)  { autoColonize = val; }

//	public static void toggleAutoBombard()	  {
//		switch(autoBombardMode) {
//			case AUTOBOMBARD_NO:	 autoBombardMode = AUTOBOMBARD_NEVER; break;
//			case AUTOBOMBARD_NEVER:  autoBombardMode = AUTOBOMBARD_YES; break;
//			case AUTOBOMBARD_YES:	 autoBombardMode = AUTOBOMBARD_WAR; break;
//			case AUTOBOMBARD_WAR:	 autoBombardMode = AUTOBOMBARD_INVADE; break;
//			case AUTOBOMBARD_INVADE: autoBombardMode = AUTOBOMBARD_NO; break;
//			default:				 autoBombardMode = AUTOBOMBARD_NO; break;
//		}
//		save();
//	}
//	public static void autoBombardMode(String val)   { autoBombardMode = val; }
	public static String autoBombardMode()   { return autoBombardMode; }
	// public static boolean autoBombardNo()    { return autoBombardMode.equals(AUTOBOMBARD_NO); }
//	public static boolean autoBombardNever() { return autoBombardMode.equals(AUTOBOMBARD_NEVER); }
//	public static boolean autoBombardYes()   { return autoBombardMode.equals(AUTOBOMBARD_YES); }
//	public static boolean autoBombardWar()   { return autoBombardMode.equals(AUTOBOMBARD_WAR); }
//	public static boolean autoBombardInvading() { return autoBombardMode.equals(AUTOBOMBARD_INVADE); }

	public static boolean playAnimations()   { return !graphicsMode.equals(GRAPHICS_LOW); }
	public static boolean antialiasing()     { return graphicsMode.equals(GRAPHICS_HIGH); }
//	public static void playSounds(boolean b) { playSounds = b; }
	public static boolean playSounds()       { return playSounds; }
	public static void toggleSounds()        { playSounds = !playSounds; save(); }
//	public static void playMusic(boolean b)  { playMusic = b; }
	public static boolean playMusic()        { return playMusic; }
	public static void toggleMusic()         { playMusic = !playMusic; save();  } // called from sound manager
	
	public	static void		selectedScreen(int i)			{ selectedScreen = i; }
	public	static int		selectedScreen()				{ return selectedScreen; }
	public	static int		screenSizePct()					{ return screenSizePct; }
	public	static String	saveDirectoryPath()				{
		if (saveDir.isEmpty())
			return Rotp.jarPath();
		else
			return saveDir;
	}
	public	static String	backupDirectoryPath()			{
		return saveDirectoryPath()+"/"+GameSession.BACKUP_DIRECTORY;
	}
	public	static String	saveDir()						{ return saveDir; }
	public	static void		saveDir(String s)				{ saveDir = s; save(); }
//	public	static String	saveDirStr()					{
//		if (saveDir.isEmpty())
//			return SAVEDIR_DEFAULT;
//		else
//			return SAVEDIR_CUSTOM;
//	}
	public	static int		backupTurns()					{ return backupTurns; }
	public	static boolean	backupTurns(int i)				{
		int prev = backupTurns;
		backupTurns = Math.min(Math.max(0,i), MAX_BACKUP_TURNS);
		save();
		return prev != backupTurns;
	}
//	public	static void		toggleBackupTurns()				{
//		if ((backupTurns >= MAX_BACKUP_TURNS) || (backupTurns < 0)) // modnar: add negative check
//			backupTurns = 0;
//		else // modnar: change backupTurns to be: 0, 1, 5 ,10, 20
//			backupTurns = (int) Math.round(1.0f + 4.87f*backupTurns - 0.93f*Math.pow(backupTurns, 2) + 0.063f*Math.pow(backupTurns, 3) );
//		save();
//	}
//	public	static void		toggleYearDisplay()				{ displayYear = !displayYear; save(); }
//	public	static void		displayYear(boolean b)			{ displayYear = b; }
	public	static boolean	getDisplayYear()				{ return displayYear; }
	public	static int		getDefaultMaxBases()			{ return defaultMaxBases; }
//	public	static void		defaultMaxBases(int i)			{ defaultMaxBases = i; }
	public	static boolean	governorOnByDefault()			{ return governorOnByDefault; }
	public	static boolean	governorAutoSpendByDefault()	{ return governorAutoSpendByDefault; }
	public	static boolean	legacyGrowth()					{ return legacyGrowth; } // BR:
	public	static boolean	governorAutoApply()				{ return governorAutoApply; } // BR:
	public	static boolean	getDivertColonyExcessToResearch()	{ return divertColonyExcessToResearch; }
//	public	static void	divertExcessToResearch(boolean b)	{ divertColonyExcessToResearch = b; }
	public	static boolean	disableAdvisor()				{ return disableAdvisor; }
	public	static void		disableAdvisor(boolean b)		{ disableAdvisor = b; }
	private	static void		uiTexturePct(int i)				{ uiTexturePct = i / 100.0f; }
	static	float			uiTexturePct()					{ return uiTexturePct; }
	static void loadAndSave() {
		load();
		save();
	}
	@SuppressWarnings("null")
	public static void load() {
		
//		System.out.println("UserPreferences: load()");
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
			out.println(keyFormat("SELECTED_SCREEN")+ selectedScreen());
			out.println(keyFormat("UI_TEXTURE_LEVEL")+(int) (uiTexturePct()*100));
			out.println(keyFormat("DISABLE_ADVISOR") + yesOrNo(disableAdvisor));
			out.println(keyFormat("LANGUAGE")+ languageDir());
			// BR: Governors GUI
//			out.println();
//			out.println("===== Governor Settings =====");
//			out.println();
//			out.println(keyFormat("DEFAULT_MAX_BASES") + defaultMaxBases);
//			out.println(keyFormat("GOVERNOR_ON_BY_DEFAULT") + yesOrNo(governorOnByDefault));
//			out.println(keyFormat("AUTOSPEND_ON_BY_DEFAULT") + yesOrNo(governorAutoSpendByDefault));
//			out.println(keyFormat("DIVERT_COLONY_EXCESS_TO_RESEARCH")+ yesOrNo(divertColonyExcessToResearch));
//			out.println(keyFormat("LEGACY_GROWTH") + yesOrNo(legacyGrowth)); // BR:
//			out.println(keyFormat("GOVERNOR_AUTO_APPLY") + yesOrNo(governorAutoApply)); // BR:

			out.println();
			out.println("===== Extended Settings =====");
			out.println();
			for (IParam param : optionList()) {
				if (param != null && !param.isDuplicate())
					out.println(keyFormat(param.getCfgLabel()) + param.getCfgValue());
			}
			// ========== TEST ==========
			//
//		System.out.println("UserPreferences: save()");
//			for (IParam param : mainOptions)
//				if (param != null) {
//					String label = param.getCfgLabel();
//					String value = param.getCfgValue();
//					System.out.println(keyFormat(label) + value);
//				}
						
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
			case "SCREEN_SIZE_PCT": setScreenSizePct(Integer.valueOf(val)); return;
			case "SELECTED_SCREEN": setSelectedScreen(Integer.valueOf(val)); return;
			case "UI_TEXTURE_LEVEL": uiTexturePct(Integer.valueOf(val)); return;
			case "LANGUAGE": selectLanguage(val); return;
			default:
			// BR: Global Mod GUI
				for (IParam param : optionList()) {
					if (param != null 
							&& key.equalsIgnoreCase(param.getCfgLabel())) {
						if (param instanceof ParamString)
							param.setFromCfgValue(fullVal.trim());
						else
							param.setFromCfgValue(val);
						break;
					}
				}
				return;
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
	private	static void setSelectedScreen(int i) {
		selectedScreen = Math.max(-1, Math.min(i, Rotp.maxScreenIndex()));
	}
//	public	static void nextSelectedScreen()	 {
//		setSelectedScreen(selectedScreen + 1);
//		save();
//	}
//	public	static void prevSelectedScreen()	 {
//		setSelectedScreen(selectedScreen - 1);
//		save();
//	}
	private static void setScreenSizePct(int i) {
		screenSizePct = Math.max(25,Math.min(i,200));
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
	private static String displayModeFromSettingName(String s) {
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
	private static String graphicsModeFromSettingName(String s) {
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
	private static String autoBombardFromSettingName(String s) {
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
	private static String texturesFromSettingName(String s) {
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
	private static String sensitivityFromSettingName(String s) {
		switch(s) {
			case "High":   return SENSITIVITY_HIGH;
			case "Medium": return SENSITIVITY_MEDIUM;
			case "Low":    return SENSITIVITY_LOW;
		}
		return SENSITIVITY_MEDIUM;
	}
//	public static void increaseMusicLevel()	{
//		musicVolume = Math.min(10, musicVolume+1);
//		SoundManager.current().resetMusicVolumes();
//		save();
//	}
//	public static void decreaseMusicLevel()	{
//		musicVolume = Math.max(0, musicVolume-1);
//		SoundManager.current().resetMusicVolumes();
//		save();
//	};
//	public static void increaseSoundLevel()	{
//		soundVolume = Math.min(10, soundVolume+1);
//		SoundManager.current().resetSoundVolumes();
//		save();
//	}
//	public static void decreaseSoundLevel()	{
//		soundVolume = Math.max(0, soundVolume-1);
//		SoundManager.current().resetSoundVolumes();
//		save();
//	}
}
