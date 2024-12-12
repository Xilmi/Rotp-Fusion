package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class SetupRestartOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = SETUP_RESTART_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RESTART_OPTIONS"),
				restartChangesPlayerRace,
				restartAppliesSettings
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RESTART_AI"),
				restartChangesPlayerAI,
				restartChangesAliensAI
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						restartChangesPlayerRace
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						restartChangesPlayerRace,
						restartChangesPlayerAI,
						restartChangesAliensAI,
						restartAppliesSettings
						));
		return majorList;
	}

}
