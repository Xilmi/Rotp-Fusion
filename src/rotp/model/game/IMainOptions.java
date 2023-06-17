package rotp.model.game;

import static rotp.model.galaxy.StarSystem.fontPct;
import static rotp.model.galaxy.StarSystem.minFont;
import static rotp.model.galaxy.StarSystem.minFont2;
import static rotp.ui.UserPreferences.backupTurns;
import static rotp.ui.UserPreferences.disableAdvisor;
import static rotp.ui.UserPreferences.displayMode;
import static rotp.ui.UserPreferences.displayModeToSettingName;
import static rotp.ui.UserPreferences.graphicsMode;
import static rotp.ui.UserPreferences.graphicsModeToSettingName;
import static rotp.ui.UserPreferences.musicVolume;
import static rotp.ui.UserPreferences.playMusic;
import static rotp.ui.UserPreferences.playSounds;
import static rotp.ui.UserPreferences.saveDir;
import static rotp.ui.UserPreferences.saveDirectoryPath;
import static rotp.ui.UserPreferences.selectedScreen;
import static rotp.ui.UserPreferences.sensitivityMode;
import static rotp.ui.UserPreferences.sensitivityToSettingName;
import static rotp.ui.UserPreferences.showMemory;
import static rotp.ui.UserPreferences.soundVolume;
import static rotp.ui.UserPreferences.texturesMode;
import static rotp.ui.UserPreferences.texturesToSettingName;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLAG_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_HUGE_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_LARGE_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_SMALL_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_TRANSPORT_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_UNARMED_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_RALLY_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_STARGATE_SCALE;
import static rotp.ui.util.IParam.langLabel;

import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.JFileChooser;

import rotp.Rotp;
import rotp.ui.game.BaseModPanel;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolInt;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamOptions;
import rotp.ui.util.ParamString;
import rotp.util.FontManager;
import rotp.util.sound.SoundManager;

public interface IMainOptions extends IBaseOptsTools {
	String WINDOW_MODE			= "GAME_SETTINGS_WINDOWED";
	String BORDERLESS_MODE		= "GAME_SETTINGS_BORDERLESS";
	String FULLSCREEN_MODE		= "GAME_SETTINGS_FULLSCREEN";
	String GRAPHICS_LOW			= "GAME_SETTINGS_GRAPHICS_LOW";
	String GRAPHICS_MEDIUM		= "GAME_SETTINGS_GRAPHICS_MED";
	String GRAPHICS_HIGH		= "GAME_SETTINGS_GRAPHICS_HIGH";
	String TEXTURES_NO			= "GAME_SETTINGS_TEXTURES_NO";
	String TEXTURES_INTERFACE	= "GAME_SETTINGS_TEXTURES_INTERFACE";
	String TEXTURES_MAP			= "GAME_SETTINGS_TEXTURES_MAP";
	String TEXTURES_BOTH		= "GAME_SETTINGS_TEXTURES_BOTH";
	String SAVEDIR_DEFAULT		= "GAME_SETTINGS_SAVEDIR_DEFAULT";
	String SAVEDIR_CUSTOM		= "GAME_SETTINGS_SAVEDIR_CUSTOM";
	String SENSITIVITY_HIGH		= "GAME_SETTINGS_SENSITIVITY_HIGH";
	String SENSITIVITY_MEDIUM	= "GAME_SETTINGS_SENSITIVITY_MEDIUM";
	String SENSITIVITY_LOW		= "GAME_SETTINGS_SENSITIVITY_LOW";
	int	   MAX_BACKUP_TURNS		= 20; // modnar: change max turns between backups to 20
	
	// ==================== Duplicates for Base Main Settings Options ====================
	//
	ParamList displayMode		= new ParamList( GAME_UI, "DISPLAY_MODE",
			Arrays.asList(
					WINDOW_MODE,
					BORDERLESS_MODE,
					FULLSCREEN_MODE
					),
			WINDOW_MODE) {
		{ isDuplicate(true); isCfgFile(true); showFullGuide(true); }
		@Override public String getCfgValue()		{ return displayModeToSettingName(get()); }
		@Override public String getOption()			{ return displayMode(); }
		@Override public void setOption(String s)	{ displayMode(s); }
	};
//	default boolean fullScreen()	{ return displayMode().equals(FULLSCREEN_MODE); }
//	default boolean windowed()		{ return displayMode().equals(WINDOW_MODE); }
//	default boolean borderless()	{ return displayMode().equals(BORDERLESS_MODE); }
//	default String displayMode()	{ return displayMode.get(); }
	
	ParamList graphicsMode		= new ParamList( GAME_UI, "GRAPHICS",
			Arrays.asList(
					GRAPHICS_HIGH,
					GRAPHICS_MEDIUM,
					GRAPHICS_LOW
					),
			GRAPHICS_HIGH) {
		{ isDuplicate(true); isCfgFile(true); showFullGuide(true); }
		@Override public String getCfgValue()		{ return graphicsModeToSettingName(get()); }
		@Override public String getOption()			{ return graphicsMode(); }
		@Override public void setOption(String s)	{ graphicsMode(s); }
	};
//	default boolean playAnimations()	{ return !graphicsMode.get().equals(GRAPHICS_LOW); }
//	default boolean antiAliasing()		{ return graphicsMode.get().equals(GRAPHICS_HIGH); }

	ParamList texturesMode		= new ParamList( GAME_UI, "TEXTURES",
			Arrays.asList(
					TEXTURES_NO,
					TEXTURES_INTERFACE,
					TEXTURES_MAP,
					TEXTURES_BOTH
					),
			TEXTURES_BOTH) {
		{ isDuplicate(true); isCfgFile(true); showFullGuide(true); }
		@Override public String getCfgValue()		{ return texturesToSettingName(get()); }
		@Override public String getOption()			{ return texturesMode(); }
		@Override public void setOption(String s)	{ texturesMode(s); }
	};
//	default boolean texturesInterface()		{ return texturesInterface(); }
//	default boolean texturesMap()			{ return texturesMap(); }

	ParamList sensitivityMode		= new ParamList( GAME_UI, "SENSITIVITY",
			Arrays.asList(
					SENSITIVITY_LOW,
					SENSITIVITY_MEDIUM,
					SENSITIVITY_HIGH
					),
			SENSITIVITY_MEDIUM) {
		{ isDuplicate(true); isCfgFile(true); showFullGuide(true); }
		@Override public String getCfgValue()		{ return sensitivityToSettingName(get()); }
		@Override public String getOption()			{ return sensitivityMode(); }
		@Override public void setOption(String s)	{ sensitivityMode(s); }
	};
//	default boolean sensitivityMedium()		{ return sensitivityMedium(); }
//	default boolean sensitivityLow()		{ return sensitivityLow(); }

	// Do no use alone, Go through soundVolume
	ParamBoolean playSounds		= new ParamBoolean(GAME_UI, "SOUNDS", true) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public Boolean getOption()		{ return playSounds(); }
		@Override public void setOption(Boolean b)	{
			if(playSounds() != b)
				//playSounds(!s); // ! because following line will toggle!
				SoundManager.current().toggleSounds();
		}
	};
	ParamBoolInt soundVolume	= new ParamBoolInt(GAME_UI, playSounds,
			"SOUNDS_ON", "SOUNDS_OFF", 10, 0, 10) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public String getCfgLabel()		{ return "SOUND_VOLUME"; }
		@Override public Integer getOption()		{ return soundVolume(); }
		@Override public void setOption(Integer i)	{
			soundVolume(Math.abs(i));
			SoundManager.current().resetSoundVolumes();
		}
	};
//	default int soundVolume()		{ return soundVolume.get(); }
//	default boolean playSounds()	{ return playSounds.get(); }
	
	// Do no use alone, Go through musicVolume
	ParamBoolean playMusic		= new ParamBoolean(GAME_UI, "MUSIC", true) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public Boolean getOption()		{ return playMusic(); }
		@Override public void setOption(Boolean b)	{
			if(playMusic() != b)
				//playMusic(!s); // ! because following line will toggle!
				SoundManager.current().toggleMusic();
		}
	};
	ParamBoolInt musicVolume	= new ParamBoolInt(GAME_UI, playMusic,
			"MUSIC_ON", "MUSIC_OFF", 10, 0, 10) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public String getCfgLabel()		{ return "MUSIC_VOLUME"; }
		@Override public Integer getOption()		{ return musicVolume(); }
		@Override public void setOption(Integer i)	{
			musicVolume(Math.abs(i));
			SoundManager.current().resetMusicVolumes();
		}
	};
//	default int musicVolume()		{ return musicVolume.get(); }
//	default boolean playMusic()		{ return playMusic.get(); }

	ParamBoolean showMemory		= new ParamBoolean(GAME_UI, "MEMORY", false) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public String getCfgLabel()		{ return "SHOW_MEMORY"; }
		@Override public Boolean getOption()		{ return showMemory(); }
		@Override public void setOption(Boolean b)	{ showMemory(b); }
	};
//	default boolean showMemory()		{ return showMemory.get(); }

	ParamInteger selectedScreen		= new ParamInteger(GAME_UI, "SELECTED_SCREEN",
			-1, -1, Rotp.maxScreenIndex(), true) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public Integer getOption()		{ return selectedScreen(); }
		@Override public void setOption(Integer i)	{ selectedScreen(i); }
	};
//	default int selectedScreen()		{ return selectedScreen.get(); }


	ParamInteger backupTurns	= new ParamInteger(GAME_UI, "BACKUP", 5, 0, MAX_BACKUP_TURNS, 1, 5, 10) {
		{ isDuplicate(true); isCfgFile(true); specialZero(GAME_UI + "BACKUP_OFF"); }
		@Override public String getCfgLabel()		{ return "BACKUP_TURNS"; }
		@Override public Integer getOption()		{ return backupTurns(); }
		@Override public void setOption(Integer i)	{ backupTurns(i); }
	};
//	default int backupTurns()		{ return backupTurns.get(); }

	ParamString saveDirectory	= new ParamString(GAME_UI, "SAVEDIR", "") {
		{ isDuplicate(true); isCfgFile(true); }
		@Override protected String descriptionId()	{
			String es = get().isEmpty()? "1" : "2";
			String label = super.descriptionId() + es;
			return label;			
		}
		@Override public String getGuiDescription()	{ return langLabel(descriptionId(), get()); }
		@Override public String guideValue()		{
			String es = get().isEmpty()? "_DEFAULT" : "_CUSTOM";
			String label = getLangLabel() + es;
			return langLabel(label);
		}
		@Override public void toggle(MouseEvent e, BaseModPanel frame)	{
			if (getDir(e) == 0) {
				set("");
				return;
			}
	        final JFileChooser fc = new JFileChooser();
	        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        File saveDir = new File(saveDirectoryPath());
	        fc.setCurrentDirectory(saveDir);
	        int returnVal = fc.showOpenDialog(frame);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            String path = fc.getSelectedFile().getAbsolutePath();
	            set(path);
	        }
	    }
		@Override public String	getCfgLabel()		{ return "SAVE_DIR"; }
		@Override public String	getOption()			{ return saveDir(); }
		@Override public void	setOption(String s)	{ saveDir(s); }
	};
//	default int saveDir()		{ return saveDirectory.get(); }
	ParamBoolean disableAdvisor		= new ParamBoolean(MOD_UI, "DISABLE_ADVISOR", true)
	{
		{  isDuplicate(true); isCfgFile(true); }
		@Override public Boolean getOption()		{ return disableAdvisor(); }
		@Override public void setOption(Boolean b)	{ disableAdvisor(b); }
	};

	
	// ==================== Mod Options ====================
	//
	ParamOptions menuStartup		= new ParamOptions(MOD_UI, "MENU_STARTUP", ParamOptions.LAST)
	{	{ isCfgFile(true); } };
	ParamInteger galaxyPreviewColorStarsSize = new ParamInteger(MOD_UI, "GALAXY_PREVIEW_COLOR_SIZE" , 5, 0, 20, 1, 2, 5)
	{	{ isCfgFile(true); } };
	ParamInteger minListSizePopUp	= new ParamInteger(MOD_UI, "MIN_LIST_SIZE_POP_UP" , 4, 0, 10, true)
	{
		{ isCfgFile(true); }
	}	.specialZero(MOD_UI + "MIN_LIST_SIZE_POP_UP_NEVER");
	ParamBoolean useFusionFont		= new ParamBoolean(MOD_UI, "USE_FUSION_FONT", false)
	{
		{ isCfgFile(true); }
		@Override public void setOption(Boolean val) { FontManager.INSTANCE.resetGalaxyFont(); }
	};
	ParamFloat   showFlagFactor		= new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { MAX_FLAG_SCALE = (int) (80 * val); }
	};
	ParamFloat   showPathFactor		= new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { MAX_RALLY_SCALE = (int) (100 * val); }
	};
	ParamInteger showNameMinFont	= new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT", 8, 2, 24, 1, 2, 5) {
		{ isCfgFile(true); }
		@Override public void setOption(Integer val) {
			minFont	 = val;
			minFont2 = Math.round(val/showInfoFontRatio.get());
		}
	};
	ParamFloat   showInfoFontRatio	= new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { minFont2	= Math.round(minFont/val);
		}
	};
	ParamFloat   mapFontFactor		= new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { fontPct = Math.round(val * 100); }
	};
	ParamFloat   showFleetFactor	= new ParamFloat( MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) {
			MAX_STARGATE_SCALE			= (int) (40 * val);
			MAX_FLEET_UNARMED_SCALE		= (int) (40 * val);
			MAX_FLEET_TRANSPORT_SCALE	= (int) (60 * val);
			MAX_FLEET_SMALL_SCALE		= (int) (60 * val);
			MAX_FLEET_LARGE_SCALE		= (int) (80 * val);
			MAX_FLEET_HUGE_SCALE		= (int) (100 * val);
		}
	};
	ParamBoolean compactOptionOnly	= new ParamBoolean(MOD_UI, "COMPACT_OPTION_ONLY", false)
	{	{ isCfgFile(true); } };

	// ==================== GUI List Declarations ====================
	//
	LinkedList<IParam> mainOptionsUI  = new LinkedList<>(
			Arrays.asList(
					displayMode, graphicsMode, texturesMode, sensitivityMode, selectedScreen, disableAdvisor,
					null,
					soundVolume, musicVolume, showMemory, backupTurns, saveDirectory,
					null,
					mapFontFactor, showNameMinFont, showInfoFontRatio, useFusionFont, galaxyPreviewColorStarsSize,
					null,
					showFleetFactor, showFlagFactor, showPathFactor, minListSizePopUp, menuStartup, compactOptionOnly
					));

}
