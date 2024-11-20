package rotp.ui.options;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class ShipCombatSettings extends AbstractOptionsSubUI {
	static final String OPTION_ID = SHIP_COMBAT_SETTINGS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiMajor(false));
		map.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiMajor(false));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey());
		minorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiMinor(false));
		minorList.add(HEADER_SPACER_50);
		minorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiMinor(false));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey());
		majorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiMajor(false));
		majorList.add(HEADER_SPACER_50);
		majorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiMajor(false));
		return majorList;
	}
}
