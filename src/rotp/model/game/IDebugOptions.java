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

	ParamBoolean debugAutoPlay		= new ParamBoolean(GAME_UI, "DEBUG_AUTO_PLAY", false)
	{ { isDuplicate(false); isCfgFile(true); } };
	default boolean debugAutoPlay()			{ return debugAutoPlay.get(); }

	LinkedList<LinkedList<IParam>> debugOptionsMap = 
			new LinkedList<LinkedList<IParam>>() { {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_MEMORY"),
				debugShowMemory, debugConsoleMemory, debugFileMemory

//				headerSpacer,
//				new ParamTitle("AUTO_FLAG_COLONY_TECH")
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("DEBUG_AUTO_PLAY"),
				debugAutoPlay
				)));
		}
	};
	ParamSubUI debugOptionsUI = new ParamSubUI( MOD_UI, "DEBUG_OPTIONS_UI", debugOptionsMap,
			"DEBUG_OPTIONS_TITLE", DEBUG_GUI_ID)
	{ { isCfgFile(true); } };


	LinkedList<IParam> debugOptions = debugOptionsUI.optionsList();
}
