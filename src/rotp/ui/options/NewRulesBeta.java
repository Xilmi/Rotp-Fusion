package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class NewRulesBeta extends AbstractOptionsSubUI {
	static final String OPTION_ID = NEW_RULES_BETA_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEW_ALPHA")
				)));

		SafeListParam list = new SafeListParam("NEW_BETA");
		list.add(new ParamTitle("NEW_BETA"));
		list.addAll(AllSubUI.getHandle(AGGRESSIVITY_LEVEL_UI_KEY).getUiMajor(false));
		list.addAll(Arrays.asList(
				HEADER_SPACER_100,
				new ParamTitle("RETREAT_RULES"),
				retreatDestination,
				hyperComRetreatExtended,
				noEnemyOnRetreatDestination
				));
		map.add(list);

		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEW_SAFE")
				)));

		map.add(AllSubUI.getHandle(NEW_OPTIONS_BETA_UI_KEY).getUiMajor(true));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				AllSubUI.getHandle(AGGRESSIVITY_LEVEL_UI_KEY).getUiMajor(false));
		majorList.add(HEADER_SPACER_50);
		majorList.add(retreatDestination);
		majorList.add(hyperComRetreatExtended);
		majorList.add(noEnemyOnRetreatDestination);
		return majorList;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						maxLandingTroops,
						maxLandingTroopsAmount,
						maxLandingTroopsFactor
						));
		return minorList;
	}
}
