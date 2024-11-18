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
		map.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiMajor());
		map.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiMajor());
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey());
		minorList.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUI());
		minorList.add(lineSpacer25);
		minorList.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUI());
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey());
		majorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiMajor());
		majorList.add(headerSpacer50);
		majorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiMajor());
		return majorList;
	}
}
