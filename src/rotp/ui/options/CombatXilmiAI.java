package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class CombatXilmiAI extends AbstractOptionsSubUI {
	static final String OPTION_ID = COMBAT_XILMI_AI_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AUTO_COMBAT_PLAYER"),
				playerAttackConfidence,
				playerDefenseConfidence
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AUTO_COMBAT_AI"),
				aiAttackConfidence,
				aiDefenseConfidence
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						playerAttackConfidence,
						playerDefenseConfidence,
						aiAttackConfidence,
						aiDefenseConfidence
						));
		return majorList;
	}
}
