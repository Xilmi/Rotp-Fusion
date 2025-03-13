package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class GalaxyRules extends AbstractOptionsSubUI {
	static final String OPTION_ID = GALAXY_RULES_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("PLANET_RULES"),
				popGrowthFactor,
				terraforming,
				colonizing,
				researchRate
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SPACE_RULES"),
				warpSpeed,
				fuelRange,
				darkGalaxy,

				LINE_SPACER_25,
				realNebulaSize,
				realNebulaShape
				)));
		SafeListParam list = new SafeListParam("");
		list.add(RELEVANT_TITLE);
		list.addAll(Arrays.asList(
				specialPeaceTreaty,
				realNebulaeOpacity
				));
		map.add(list);

		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						darkGalaxy,
						warpSpeed,
						fuelRange,
						realNebulaSize,
						realNebulaShape,
						realNebulaeOpacity,
						researchRate,
						popGrowthFactor
						));
		return majorList;
	}
}
