package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class DiplomacyOptions implements IOptionsSubUI {
	static final String OPTION_ID = DIPLOMACY_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }
	@Override public boolean hasExtraParam()	{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(COUNCIL_OPTIONS_UI_KEY).getUiExt());
		map.add(new SafeListParam(Arrays.asList(
				aiHostility,
				techTrading,
				allowTechStealing,
				specialPeaceTreaty
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(OPTION_ID,
				Arrays.asList(
						aiHostility,
						techTrading,
						specialPeaceTreaty
						));
		return majorList;
	}
}
