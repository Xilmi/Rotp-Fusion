package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class IronmanLittle extends AbstractOptionsSubUI {
	static final String OPTION_ID = IRONMAN_LITTLE_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				persistentArtifact,
				allowSpeciesDetails
				)));
		map.add(new SafeListParam(Arrays.asList(
				ironmanNoLoad,
				ironmanLoadDelay
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						persistentArtifact,
						ironmanNoLoad,
						ironmanLoadDelay,
						allowSpeciesDetails
						));
		return majorList;
	}
}
