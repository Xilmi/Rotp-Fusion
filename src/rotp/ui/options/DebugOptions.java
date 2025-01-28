package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class DebugOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = "DEBUG_OPTIONS";
	
	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("DEBUG_MEMORY"),
				debugShowMemory, debugConsoleMemory,
				debugShowMoreMemory, debugFileMemory,

				HEADER_SPACER_50,
				new ParamTitle("DEBUG_RELEVANT"),
				councilWin, autoplay,
				backupTurns
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("DEBUG_AUTO_RUN"),
				debugAutoRun, debugNoAutoSave,
				debugARContinueOnLoss,
				debugBMLostTurns, debugBMMaxTurns,
				debugBMZoomOut, debugBMShowAll,
				consoleAutoRun, debugLogNotif, debugLogEvents
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_OTHER"),
				menuStartup,
				continueAnyway,
				
				HEADER_SPACER_100,
				HEADER_SPACER_100,
				debugPlayerEmpire
				)));
		return map;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						debugAutoRun
						));
		return minorList;
	}
}
