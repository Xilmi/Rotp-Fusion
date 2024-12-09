package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class SetupHomeworldOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = SETUP_HOMEWORLD_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add( new SafeListParam(Arrays.asList(
				new ParamTitle("PLANET_ENV"),
				fertileHomeworld,
				gaiaHomeworld
				)));
		map.add( new SafeListParam(Arrays.asList(
				new ParamTitle("PLANET_RESOURCES"),
				orionLikeHomeworld,
				artifactsHomeworld,
				richHomeworld,
				ultraRichHomeworld
				)));
		map.add( new SafeListParam(Arrays.asList(
				new ParamTitle("EMPIRE_OTHER"),
				companionWorlds,
				battleScout,
				randomTechStart
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						randomizeAI
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						fertileHomeworld,
						artifactsHomeworld,
						richHomeworld,
						LINE_SPACER_25,
						companionWorlds,
						battleScout,
						randomTechStart
						));
		return majorList;
	}

}
