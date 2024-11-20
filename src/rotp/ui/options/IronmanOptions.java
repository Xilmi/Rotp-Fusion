package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class IronmanOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = IRONMAN_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("IRONMAN_MAIN"),
				ironmanMode
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("IRONMAN_CUSTOM"),
				fixedEventsMode,
				persistentArtifact,
				ironmanNoLoad, ironmanLoadDelay,
				researchMoo1, persistentRNG,
				allowSpeciesDetails
				)));
		return map;
	};
}
