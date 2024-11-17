package rotp.ui.options;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class ShipCombatRules implements IOptionsSubUI {
	static final String OPTION_ID = SHIP_COMBAT_RULES_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return false; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		SafeListParam list1 = new SafeListParam("");
		list1.addAll(AllSubUI.getHandle(COMBAT_ASTEROID_UI_KEY).getUiExt());
		list1.add(headerSpacer50);
		list1.addAll(AllSubUI.getHandle(COMBAT_XILMI_AI_UI_KEY).getUiExt());
		list1.add(headerSpacer50);
		list1.addAll(AllSubUI.getHandle(COMBAT_TIMING_UI_KEY).getUiExt());
		map.add(list1);
		map.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		map.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());

		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey());
		majorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		majorList.add(headerSpacer50);
		majorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());
		return majorList;
	}
}
