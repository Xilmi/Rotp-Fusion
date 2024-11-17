package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

public final class SettingsOptions implements IOptionsSubUI {
	static final String OPTION_ID = SETTINGS_OPTIONS_UI_KEY;
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{ return inGameOptionsMap(); }

	public static SafeListPanel inGameOptionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(AllSubUI.getHandle(GAME_AUTOMATION_UI_KEY).getUiExt());
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SUB_PANEL_OPTIONS"),
				AllSubUI.flagSubUI(),
				flagColorCount,
				AllSubUI.getHandle(SHIP_COMBAT_SETTINGS_UI_KEY).getUI(),
				AllSubUI.commonSubUI(),
				
				headerSpacer50,
				new ParamTitle("ENOUGH_IS_ENOUGH"),
				disableAutoHelp, disableAdvisor,

				headerSpacer100,
				new ParamTitle("BETA_TEST"),
				debugAutoRun
				)));
		map.add(AllSubUI.getHandle(VISUAL_OPTIONS_UI_KEY).getUiExt());
		return map;
	}
}
