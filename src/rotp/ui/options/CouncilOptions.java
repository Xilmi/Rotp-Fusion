package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class CouncilOptions implements IOptionsSubUI {
	static final String OPTION_ID = COUNCIL_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				councilWin,
				counciRequiredPct,
				councilPlayerVote
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						councilWin,
						counciRequiredPct,
						councilPlayerVote
						));
		return majorList;
	}
}
