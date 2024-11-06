package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class NameOptions implements IOptionsSubUI {
	static final String OPTION_ID = "NAME_OPTIONS";

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				altairi, ursinathi,
				nazlok, human,
				kholdan, meklonar,
				fiershan, mentaran,
				ssslaura, cryslonoid,

				headerSpacer50,
				moo1SpeciesName,
				clearSpeciesName,

				headerSpacer50,
				activateSpeciesName
				)));
		return map;
	}
}
