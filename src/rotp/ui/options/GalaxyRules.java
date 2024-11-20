package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class GalaxyRules extends AbstractOptionsSubUI {
	static final String OPTION_ID = GALAXY_RULES_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				warpSpeed,
				fuelRange,

				LINE_SPACER_25,
				realNebulaSize,
				realNebulaShape
				)));
		map.add(new SafeListParam(Arrays.asList(
				popGrowthFactor,
				terraforming,
				colonizing,
				researchRate
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
						warpSpeed,
						fuelRange,
						terraforming,
						colonizing,
						realNebulaSize,
						realNebulaShape,
						realNebulaeOpacity,
						researchRate,
						popGrowthFactor
						));
		return majorList;
	}
}
