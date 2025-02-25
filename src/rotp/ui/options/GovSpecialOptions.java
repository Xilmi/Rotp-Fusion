package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class GovSpecialOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = GOVERNOR_SPECIAL_KEY;
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GOVERNOR_TERRAFORM"),
				terraformFactoryPct,
				terraformPopulationPct,
				terraformPopulation,
				terraformCost2Income,

				HEADER_SPACER_100,
				new ParamTitle("GOVERNOR_COLONY_GROWTH"),
				maxGrowthMode,
				compensateGrowth,
				minColonyGrowth,
				colonyEarlyBoostPct,

				LINE_SPACER_25,
				earlyBaseBuilding,
				earlyBaseBoostPct
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GOVERNOR_SUBSIDIES"),
				subsidyTerraformUse,
				subsidyNormalUse,

				HEADER_SPACER_100,
				new ParamTitle("GOVERNOR_OTHER_LIMITS"),
				defaultShipTakePct,
				workerToFactoryROI,
				maxColoniesForROI,
				showTriggeredROI,

				LINE_SPACER_25,
				colonyDistanceWeight
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						workerToFactoryROI
						));
		return minorList;
	}
}
