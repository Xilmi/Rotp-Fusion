package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class SetupRandomOpponents extends AbstractOptionsSubUI {
	static final String OPTION_ID = SETUP_RANDOM_OPP_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RANDOM_OPP_DEF"),
				randomAlienRacesTargetMax,
				randomAlienRacesTargetMin,
				randomAlienRaces
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RANDOM_OPP_DIST"),
				randomAlienRacesMax,
				randomAlienRacesMin,
				randomAlienRacesSmoothEdges
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						randomAlienRaces
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						randomAlienRacesTargetMax,
						randomAlienRacesTargetMin,
						randomAlienRaces,
						randomAlienRacesMax,
						randomAlienRacesMin,
						randomAlienRacesSmoothEdges
						));
		return majorList;
	}

}
