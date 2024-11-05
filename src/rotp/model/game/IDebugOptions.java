package rotp.model.game;

import static rotp.ui.UserPreferences.showMemory;

import rotp.ui.RotPUI;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.vipconsole.VIPConsole;

public interface IDebugOptions extends IBaseOptsTools {
    String MEMORY_LOGFILE	 = "AutoRunMemory.txt";
    String AUTORUN_LOGFILE	 = "AutoRunPlayer.txt";
    String NOTIF_LOGFILE	 = "AutoRunNotifications.txt";
    String AUTORUN_OTHERFILE = "AutoRunOther.txt";
    String AUTORUN_EVENTS	 = "AutoRunEvents.txt";
    String AUTORUN_BENCHMARK = "AutoRunBenchmark.txt";

	ParamBoolean debugShowMemory	= new DebugShowMemory();
	class DebugShowMemory extends ParamBoolean {
		DebugShowMemory() {
			super(GAME_UI, "MEMORY", false);
			isDuplicate(true);
			isCfgFile(true);
		}
		@Override public String getCfgLabel()		{ return "SHOW_MEMORY"; }
		@Override public Boolean getOption()		{ return showMemory(); }
		@Override public void setOption(Boolean b)	{ showMemory(b); }
	}
	default boolean debugShowMemory()		{ return debugShowMemory.get(); }

    ParamBoolean debugShowMoreMemory	= new ParamBoolean(GAME_UI, "MORE_MEMORY", false, true, true);
	default boolean debugShowMoreMemory()	{ return debugShowMoreMemory.get(); }

	ParamBoolean debugConsoleMemory	= new ParamBoolean(GAME_UI, "MEMORY_CONSOLE", false, true, true);
	default boolean debugConsoleMemory()	{ return debugConsoleMemory.get(); }

	ParamBoolean debugFileMemory	= new ParamBoolean(GAME_UI, "MEMORY_FILE", false, true, true);
	default boolean debugFileMemory()		{ return debugFileMemory.get(); }

	ParamList debugAutoRun			= new ParamList( MOD_UI, "DEBUG_AUTO_RUN", "Off")
		.isDuplicate(false)
		.isCfgFile(true)
		.showFullGuide(true)
		.put("Off",		MOD_UI + "DEBUG_AUTO_RUN_OFF")
		.put("On",		MOD_UI + "DEBUG_AUTO_RUN_ON")
		.put("Bench",	MOD_UI + "DEBUG_AUTO_RUN_BENCH")
		.put("End",		MOD_UI + "DEBUG_AUTO_RUN_END");
	default boolean debugAutoRun()		{ return !debugAutoRun.get().equalsIgnoreCase("Off"); }
	default boolean debugBenchmark()	{ return debugAutoRun.get().equalsIgnoreCase("Bench"); }
	default void	debugBMContinue()	{ debugAutoRun.set("Bench"); }
	default boolean debugBMBreak()		{ return debugAutoRun.get().equalsIgnoreCase("End"); }

	ParamList debugARContinueOnLoss	= new ParamList( MOD_UI, "DEBUG_AR_CONTINUE_ON_LOSS", "BMOnly")
		.isDuplicate(false)
		.isCfgFile(true)
		.showFullGuide(true)
		.put("Always",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_ALWAYS")
		.put("Never",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_NEVER")
		.put("BMOnly",	MOD_UI + "DEBUG_AR_CONTINUE_ON_LOSS_BM_ONLY");

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
	ParamList debugBMShowAll		= new ParamList( MOD_UI, "DEBUG_BM_SHOW_ALL", "Off")
		.isDuplicate(false)
		.isCfgFile(true)
		.showFullGuide(true)
		.put("Off",		MOD_UI + "DEBUG_BM_SHOW_ALL_OFF")
		.put("Alway",	MOD_UI + "DEBUG_BM_SHOW_ALL_ALWAY")
		.put("Lost",		MOD_UI + "DEBUG_BM_SHOW_ALL_LOSS");
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
	ParamInteger debugBMMaxTurns	= new ParamInteger(MOD_UI, "DEBUG_BM_MAX_TURNS", -1)
			.setLimits(-1, null)
			.setIncrements(1, 10, 100)
			.specialNegative(NEGATIVE_DISABLED);
	default int debugBMMaxTurns()			{ return debugBMMaxTurns.get(); }
	
	ParamInteger debugBMLostTurns	= new ParamInteger(MOD_UI, "DEBUG_BM_LOST_TURNS", -1)
			.setLimits(-1, null)
			.setIncrements(1, 10, 100)
			.specialNegative(NEGATIVE_DISABLED);
	default int debugBMLostTurns()			{ return debugBMLostTurns.get(); }

	ParamBoolean debugBMZoomOut		= new ParamBoolean(MOD_UI, "DEBUG_BM_ZOOM_OUT", false, true, true);
	default boolean debugBMZoomOut()		{ return debugBMZoomOut.get(); }

	ParamBoolean debugNoAutoSave	= new ParamBoolean(MOD_UI, "DEBUG_NO_AUTOSAVE", false, true, true);
	default boolean debugNoAutoSave()		{ return debugNoAutoSave.get(); }

	ParamBoolean consoleAutoRun		= new ParamBoolean(GAME_UI, "CONSOLE_AUTO_PLAY", false, true, true);
	default boolean consoleAutoRun()		{ return consoleAutoRun.get(); }

	ParamBoolean debugLogNotif		= new ParamBoolean(GAME_UI, "DEBUG_LOG_NOTIF", true, true, true);
	default boolean debugLogNotif()			{ return debugLogNotif.get(); }

	ParamBoolean debugLogEvents		= new ParamBoolean(GAME_UI, "DEBUG_LOG_EVENTS", true, true, true);
	default boolean debugLogEvents()		{ return debugLogEvents.get(); }

	ParamBoolean showVIPPanel		= new ShowVIPPanel();
	class ShowVIPPanel extends ParamBoolean {
		ShowVIPPanel() {
			super(GAME_UI, "SHOW_CONSOLE_PANEL", false);
			isDuplicate(false);
			isCfgFile(true);
		}
		@Override public Boolean set(Boolean newValue) {
			super.set(newValue);
			RotPUI.useDebugFile = newValue;
			RotPUI.isVIPConsole = newValue;
			VIPConsole.showConsole(newValue);
			return newValue;
		}
	}
	default boolean selectedShowVIPPanel()	{ return showVIPPanel.get(); }
	default void showVIPPanel()				{ VIPConsole.showConsole(selectedShowVIPPanel()); }
	default void showVIPPanel(boolean b)	{
		showVIPPanel.set(b);
		showVIPPanel();
	}

	ParamBoolean continueAnyway		= new ParamBoolean(GAME_UI, "CONTINUE_ANYWAY", false, false);
	default void continueAnyway(boolean b)	{ continueAnyway.set(b); }
	default boolean continueAnyway()		{ return continueAnyway.get(); }
}
