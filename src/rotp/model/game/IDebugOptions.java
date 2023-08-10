package rotp.model.game;

import static rotp.ui.UserPreferences.showMemory;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IDebugOptions extends IBaseOptsTools {
	String DEBUG_GUI_ID		= "DEBUG_OPTIONS";
    String MEMORY_LOGFILE	= "AutoRunMemory.txt";
    String AUTORUN_LOGFILE	= "AutoRunPlayer.txt";
    String NOTIF_LOGFILE	= "AutoRunNotifications.txt";
    String AUTORUN_OTHERFILE= "AutoRunOther.txt";


	ParamBoolean debugShowMemory	= new ParamBoolean(GAME_UI, "MEMORY", false) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public String getCfgLabel()		{ return "SHOW_MEMORY"; }
		@Override public Boolean getOption()		{ return showMemory(); }
		@Override public void setOption(Boolean b)	{ showMemory(b); }
	};
	default boolean debugShowMemory()		{ return debugShowMemory.get(); }

	ParamBoolean debugConsoleMemory	= new ParamBoolean(GAME_UI, "MEMORY_CONSOLE", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugConsoleMemory()	{ return debugConsoleMemory.get(); }

	ParamBoolean debugFileMemory	= new ParamBoolean(GAME_UI, "MEMORY_FILE", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugFileMemory()		{ return debugFileMemory.get(); }

	
	ParamBoolean debugAutoRun		= new ParamBoolean(GAME_UI, "DEBUG_AUTO_PLAY", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugAutoRun()			{ return debugAutoRun.get(); }

	ParamBoolean debugLogNotif		= new ParamBoolean(GAME_UI, "DEBUG_LOG_NOTIF", true)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugLogNotif()			{ return debugLogNotif.get(); }

	LinkedList<LinkedList<IParam>> debugOptionsMap = 
			new LinkedList<LinkedList<IParam>>() { {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_MEMORY"),
				debugShowMemory, debugConsoleMemory, debugFileMemory
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_AUTO_PLAY"),
				debugAutoRun, debugLogNotif
				)));
		}
	};
	ParamSubUI debugOptionsUI = new ParamSubUI( MOD_UI, "DEBUG_OPTIONS_UI", debugOptionsMap,
			"DEBUG_OPTIONS_TITLE", DEBUG_GUI_ID)
	{ { isCfgFile(true); } };


	LinkedList<IParam> debugOptions = debugOptionsUI.optionsList();
}
