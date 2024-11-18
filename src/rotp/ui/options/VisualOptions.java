package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class VisualOptions implements IOptionsSubUI {
	static final String OPTION_ID = VISUAL_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				compactOptionOnly,
				galaxyPreviewColorStarsSize,
				
				divertExcessToResearch, defaultMaxBases, displayYear,
				showNextCouncil, 
				raceStatusLog, 
				showPendingOrders
				)));
		map.add(new SafeListParam(Arrays.asList(
				systemNameDisplay, shipDisplay, flightPathDisplay,
				showGridCircular, showShipRanges
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						defaultMaxBases, displayYear,
						showNextCouncil, systemNameDisplay, shipDisplay, flightPathDisplay,
						showGridCircular, showShipRanges, galaxyPreviewColorStarsSize,
						raceStatusLog, compactOptionOnly,
						showPendingOrders
						));
		return majorList;
	}
}
