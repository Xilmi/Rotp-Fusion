package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class ColonyRules extends AbstractOptionsSubUI {
	static final String OPTION_ID = COLONY_RULES_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("MISCELLANEOUS"),
				terraforming,
				colonizing,
				popGrowthFactor,
				researchRate
				)));

		SafeListParam list = new SafeListParam("");
		list.addAll(AllSubUI.getHandle(COLONY_SETTINGS_UI_KEY).getUiMajor(true));
		map.add(list);

		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						terraforming,
						colonizing
						));
		return majorList;
	}
}
