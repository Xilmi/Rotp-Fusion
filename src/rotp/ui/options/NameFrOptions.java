package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class NameFrOptions implements IOptionsSubUI {
	static final String OPTION_ID = "NAME_OPTIONS_FR";

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				altairiFr, ursinathiFr,
				nazlokFr, humanFr,
				kholdanFr, meklonarFr,
				fiershanFr, mentaranFr,
				ssslauraFr, cryslonoidFr,

				headerSpacer,
				moo1SpeciesName,
				clearSpeciesName,

				headerSpacer,
				activateSpeciesName
				)));
		return map;
	}
}
