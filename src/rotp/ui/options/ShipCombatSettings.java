package rotp.ui.options;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class ShipCombatSettings implements IOptionsSubUI {
	static final String OPTION_ID = SHIP_COMBAT_SETTINGS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }
	@Override public boolean hasExtraParam()	{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		map.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());
		return map;
	};
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey());
		majorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		majorList.add(headerSpacer50);
		majorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());
		return majorList;
	}
}
