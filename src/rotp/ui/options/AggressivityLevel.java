package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class AggressivityLevel extends AbstractOptionsSubUI {
	static final String OPTION_ID = AGGRESSIVITY_LEVEL_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AGGRESSIVENESS"),
				gameAgressiveness,
				skirmishesAllowed
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("DEBUG_RELEVANT"),
				councilWin,
				randomEvents,
				
				HEADER_SPACER_50,
				allowWarningExpansion
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						gameAgressiveness,
						skirmishesAllowed
						));
		return majorList;
	}
}
