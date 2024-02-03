package rotp.model.game;

import static rotp.ui.UserPreferences.showMemory;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.RotPUI;
import rotp.ui.console.CommandConsole;
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
    String AUTORUN_EVENTS   = "AutoRunEvents.txt";

	ParamBoolean debugShowMemory	= new ParamBoolean(GAME_UI, "MEMORY", false) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public String getCfgLabel()		{ return "SHOW_MEMORY"; }
		@Override public Boolean getOption()		{ return showMemory(); }
		@Override public void setOption(Boolean b)	{ showMemory(b); }
	};
	default boolean debugShowMemory()		{ return debugShowMemory.get(); }

    ParamBoolean debugShowMoreMemory	= new ParamBoolean(GAME_UI, "MORE_MEMORY", false)
  	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugShowMoreMemory()	{ return debugShowMoreMemory.get(); }

	ParamBoolean debugConsoleMemory	= new ParamBoolean(GAME_UI, "MEMORY_CONSOLE", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugConsoleMemory()	{ return debugConsoleMemory.get(); }

	ParamBoolean debugFileMemory	= new ParamBoolean(GAME_UI, "MEMORY_FILE", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugFileMemory()		{ return debugFileMemory.get(); }


	ParamBoolean debugAutoRun		= new ParamBoolean(GAME_UI, "DEBUG_AUTO_PLAY", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugAutoRun()			{ return debugAutoRun.get(); }

	ParamBoolean consoleAutoRun		= new ParamBoolean(GAME_UI, "CONSOLE_AUTO_PLAY", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean consoleAutoRun()		{ return consoleAutoRun.get(); }

	ParamBoolean debugLogNotif		= new ParamBoolean(GAME_UI, "DEBUG_LOG_NOTIF", true)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugLogNotif()			{ return debugLogNotif.get(); }

	ParamBoolean debugLogEvents		= new ParamBoolean(GAME_UI, "DEBUG_LOG_EVENTS", true)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugLogEvents()		{ return debugLogEvents.get(); }

	ParamBoolean showConsolePanel	= new ParamBoolean(GAME_UI, "SHOW_CONSOLE_PANEL", false)
	{
		{ isDuplicate(false); isCfgFile(true); }
		@Override public Boolean set(Boolean newValue) {
			super.set(newValue);
			RotPUI.useDebugFile = newValue;
			CommandConsole.showConsole(newValue);
			return newValue;
		}
	};
	default boolean selectedShowConsolePanel()	{ return showConsolePanel.get(); }
	default void showConsolePanel()				{ CommandConsole.showConsole(selectedShowConsolePanel()); }
	default void showConsolePanel(boolean b)	{
		showConsolePanel.set(b);
		showConsolePanel();
	}

	// ==================== GUI List Declarations ====================
	//
	static LinkedList<LinkedList<IParam>> debugOptionsMap() {
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_MEMORY"),
				debugShowMemory, debugConsoleMemory,
				debugShowMoreMemory, debugFileMemory,

				headerSpacer,
				showConsolePanel
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_AUTO_PLAY"),
				debugAutoRun, consoleAutoRun,
				debugLogNotif, debugLogEvents,

				headerSpacer,
				IAdvOptions.councilWin, IAdvOptions.autoplay
				)));
		return map;
	}
	ParamSubUI debugOptionsUI = debugOptionsUI();

	static ParamSubUI debugOptionsUI() {
		return new ParamSubUI( MOD_UI, "DEBUG_OPTIONS_UI", IDebugOptions.debugOptionsMap(),
				"DEBUG_OPTIONS_TITLE", DEBUG_GUI_ID)
		{ { isCfgFile(true); } };
	}
	static LinkedList<IParam> debugOptions() {
		return IBaseOptsTools.getSingleList(IDebugOptions.debugOptionsMap());
	}	
}
