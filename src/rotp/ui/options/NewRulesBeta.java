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
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEW_BETA"),
				rallyCombat,
				rallyCombatLoss,
				LINE_SPACER_25,
				maxLandingTroops,
				maxLandingTroopsAmount,
				maxLandingTroopsFactor,
				maxLandingTroopsIAFactor
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEW_SAFE")
				)));

		map.add(AllSubUI.getHandle(NEW_OPTIONS_BETA_UI_KEY).getUiMajor(true));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						rallyCombat,
						rallyCombatLoss,
						LINE_SPACER_25,
						maxLandingTroops,
						maxLandingTroopsAmount,
						maxLandingTroopsFactor,
						maxLandingTroopsIAFactor
						));
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
