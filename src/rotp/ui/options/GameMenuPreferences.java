package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class GameMenuPreferences extends AbstractOptionsSubUI {
	static final String OPTION_ID = GAME_MENU_PREF_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				showPendingOrders,
				displayFreeTech,

				LINE_SPACER_25,
				displayYear,
				showNextCouncil,
				raceStatusLog,
				raceStatusView
				)));
		map.add(new SafeListParam(Arrays.asList(
				minListSizePopUp,
				showAlternateAnimation,

				LINE_SPACER_25,
				shipBasedMissiles
				)));
		map.add(new SafeListParam(Arrays.asList(
				systemNameDisplay,
				shipDisplay,
				flightPathDisplay,
				showGridCircular,
				showShipRanges
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						showNextCouncil,
						showPendingOrders,
						LINE_SPACER_25,
						displayFreeTech,
						raceStatusLog,
						raceStatusView,
						LINE_SPACER_25,
						shipBasedMissiles
						));
		return majorList;
	}
}
