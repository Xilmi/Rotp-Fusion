package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class VisualOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = VISUAL_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(GAME_MENU_PREF_UI_KEY).getUiMajor(false));
		map.add(AllSubUI.getHandle(SETTING_MENU_PREF_UI_KEY).getUiMajor(false));
		map.add(AllSubUI.getHandle(ZOOM_OPTIONS_UI_KEY).getUiMajor(false));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						compactOptionOnly,
						galaxyPreviewColorStarsSize,

						LINE_SPACER_25,
						displayYear,
						showNextCouncil, 
						raceStatusLog, 
						showPendingOrders
						));
		return majorList;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						compactOptionOnly,
						galaxyPreviewColorStarsSize
						));
		return minorList;
	}
}
