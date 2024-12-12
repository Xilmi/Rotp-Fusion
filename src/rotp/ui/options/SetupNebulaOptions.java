package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class SetupNebulaOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = SETUP_NEBULA_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEBULA_EFFECT"),
				nebulae,
				nebulaPlacing,
				nebulaEnrichment,
				nebulaHomeworld
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("NEBULA_TYPE"),
				realNebulaSize,
				realNebulaShape,
				realNebulaeOpacity
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						nebulae
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						nebulae,
						nebulaPlacing,
						nebulaEnrichment,
						nebulaHomeworld,
						realNebulaSize,
						realNebulaShape,
						realNebulaeOpacity
						));
		return majorList;
	}

}
