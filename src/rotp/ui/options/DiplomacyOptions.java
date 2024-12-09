package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class DiplomacyOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = DIPLOMACY_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(COUNCIL_OPTIONS_UI_KEY).getUiMajor(false));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RELATION_DIPLO"),
				aiHostility,
				specialPeaceTreaty
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("TECH_DIPLO"),
				techTrading,
				allowTechStealing
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						aiHostility,
						techTrading,
						specialPeaceTreaty
						));
		return majorList;
	}
}
