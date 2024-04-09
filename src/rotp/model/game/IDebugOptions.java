package rotp.model.game;

import static rotp.ui.UserPreferences.showMemory;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.RotPUI;
import rotp.ui.console.CommandConsole;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IDebugOptions extends IBaseOptsTools {
	String DEBUG_GUI_ID		 = "DEBUG_OPTIONS";
    String MEMORY_LOGFILE	 = "AutoRunMemory.txt";
    String AUTORUN_LOGFILE	 = "AutoRunPlayer.txt";
    String NOTIF_LOGFILE	 = "AutoRunNotifications.txt";
    String AUTORUN_OTHERFILE = "AutoRunOther.txt";
    String AUTORUN_EVENTS	 = "AutoRunEvents.txt";
    String AUTORUN_BENCHMARK = "AutoRunBenchmark.txt";

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

	ParamList debugAutoRun		= new ParamList( MOD_UI, "DEBUG_AUTO_RUN", "Off") {
		{
			isDuplicate(false);
			isCfgFile(true);
			showFullGuide(true);
			put("Off",		MOD_UI + "DEBUG_AUTO_RUN_OFF");
			put("On",		MOD_UI + "DEBUG_AUTO_RUN_ON");
			put("Bench",	MOD_UI + "DEBUG_AUTO_RUN_BENCH");
			put("End",		MOD_UI + "DEBUG_AUTO_RUN_END");
		}
	};
	default boolean debugAutoRun()		{ return !debugAutoRun.get().equalsIgnoreCase("Off"); }
	default boolean debugBenchmark()	{ return debugAutoRun.get().equalsIgnoreCase("Bench"); }
	default void	debugBMContinue()	{ debugAutoRun.set("Bench"); }
	default boolean debugBMBreak()		{ return debugAutoRun.get().equalsIgnoreCase("End"); }

	ParamList debugARContinueOnLoss		= new ParamList( MOD_UI, "DEBUG_AR_CONTINUE_ON_LOSS", "BMOnly") {
		{
			isDuplicate(false);
			isCfgFile(true);
			showFullGuide(true);
			put("Always",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_ALWAYS");
			put("Never",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_NEVER");
			put("BMOnly",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_BM_ONLY");
		}
	};
	default boolean debugARStopOnLoss()	{
		switch (debugARContinueOnLoss.get()) {
		case "Never":
			return true;
		case "Always":
			return false;
		case "BMOnly":
			return !debugBenchmark();
		}
		return true;
	}
	ParamList debugBMShowAll		= new ParamList( MOD_UI, "DEBUG_BM_SHOW_ALL", "Off") {
		{
			isDuplicate(false);
			isCfgFile(true);
			showFullGuide(true);
			put("Off",		MOD_UI + "DEBUG_BM_SHOW_ALL_OFF");
			put("Alway",	MOD_UI + "DEBUG_BM_SHOW_ALL_ALWAY");
			put("Lost",		MOD_UI + "DEBUG_BM_SHOW_ALL_LOSS");
		}
	};
	default boolean debugBMShowAll()	{
		switch (debugBMShowAll.get()) {
		case "Off":
			return false;
		case "Alway":
			return true;
		case "Lost":
			return GameSession.instance().status().lost();
		}
		return false;
	}
	ParamInteger debugBMMaxTurns	= new ParamInteger(MOD_UI, "DEBUG_BM_MAX_TURNS",
									-1, -1, null, 1, 10, 100).specialNegative(NEGATIVE_DISABLED);
	default int debugBMMaxTurns()			{ return debugBMMaxTurns.get(); }
	
	ParamInteger debugBMLostTurns	= new ParamInteger(MOD_UI, "DEBUG_BM_LOST_TURNS",
									-1, -1, null, 1, 10, 100).specialNegative(NEGATIVE_DISABLED);
	default int debugBMLostTurns()			{ return debugBMLostTurns.get(); }

	ParamBoolean debugBMZoomOut		= new ParamBoolean(MOD_UI, "DEBUG_BM_ZOOM_OUT", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugBMZoomOut()			{ return debugBMZoomOut.get(); }

	ParamBoolean debugNoAutoSave	= new ParamBoolean(MOD_UI, "DEBUG_NO_AUTOSAVE", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugNoAutoSave()			{ return debugNoAutoSave.get(); }

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

	ParamBoolean continueAnyway		= new ParamBoolean(GAME_UI, "CONTINUE_ANYWAY", false)
	{ { isValueInit(false); } };
	default void continueAnyway(boolean b)	{ continueAnyway.set(b); }
	default boolean continueAnyway()		{ return continueAnyway.get(); }

	// ==================== GUI List Declarations ====================
	//
	static LinkedList<LinkedList<IParam>> debugOptionsMap() {
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_MEMORY"),
				debugShowMemory, debugConsoleMemory,
				debugShowMoreMemory, debugFileMemory,

				headerSpacer,
				new ParamTitle("DEBUG_RELEVANT"),
				IAdvOptions.councilWin, IAdvOptions.autoplay,
				IMainOptions.backupTurns
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_AUTO_RUN"),
				debugAutoRun, debugNoAutoSave,
				debugARContinueOnLoss,
				debugBMLostTurns, debugBMMaxTurns,
				debugBMZoomOut, debugBMShowAll,
				consoleAutoRun, debugLogNotif, debugLogEvents
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_OTHER"),
				IMainOptions.menuStartup,
				continueAnyway
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
