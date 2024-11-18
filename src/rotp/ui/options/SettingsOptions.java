package rotp.ui.options;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

public final class SettingsOptions implements IOptionsSubUI {
	static final String OPTION_ID = SETTINGS_OPTIONS_UI_KEY;
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{ return inGameOptionsMap(); }

	public static SafeListPanel inGameOptionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		SafeListParam list;
		map.add(AllSubUI.getHandle(GAME_AUTOMATION_UI_KEY).getUiMajor());

		list = new SafeListParam("GAME_VARIOUS");
		list.addAll(AllSubUI.getHandle(FLAG_OPTIONS_UI_KEY).getUiMinor());
		list.add(headerSpacer100);
		list.addAll(AllSubUI.getHandle(SHIP_COMBAT_SETTINGS_UI_KEY).getUiMinor());
		list.add(headerSpacer100);
		list.add(AllSubUI.getHandle(COMMON_OPTIONS_UI_KEY).getUI());
		list.add(headerSpacer100);
		list.addAll(AllSubUI.getHandle(HELP_AND_ADVICE_UI_KEY).getUiMajor());
		list.add(headerSpacer100);
		list.addAll(AllSubUI.getHandle(NEW_OPTIONS_BETA_UI_KEY).getUiMajor());
		map.add(list);

//		map.add(new SafeListParam(Arrays.asList(
//				new ParamTitle("SUB_PANEL_OPTIONS"),
//				AllSubUI.flagSubUI(),
//				flagColorCount,
//				AllSubUI.getHandle(SHIP_COMBAT_SETTINGS_UI_KEY).getUI(),
//				AllSubUI.commonSubUI(),
//				
//				headerSpacer50,
//				new ParamTitle("ENOUGH_IS_ENOUGH"),
//				disableAutoHelp, disableAdvisor,
//
//				headerSpacer100,
//				new ParamTitle("BETA_TEST"),
//				debugAutoRun
//				)));
		map.add(AllSubUI.getHandle(VISUAL_OPTIONS_UI_KEY).getUiMajor());
		return map;
	}
}
