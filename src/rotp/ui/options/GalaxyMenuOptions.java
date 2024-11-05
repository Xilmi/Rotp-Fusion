package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

public final class GalaxyMenuOptions implements IOptionsSubUI {
	static final 		String OPTION_ID = "GALAXY_SUBUI";
	public static final String GALAXY_ID = OPTION_ID;
	
	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean noPanel()			{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(optionsGalaxy());
		return map;
	}
	static SafeListParam optionsGalaxy() {
		GalaxyOption gal = GalaxyOption.instance;
		return new SafeListParam(GALAXY_ID,
			Arrays.asList(
					gal.showNewRaces, gal.globalCROptions,
					gal.useSelectableAbilities, gal.shapeOption3,
					gal.galaxyRandSource, gal.previewNebula,
					empiresSpreadingFactor,
					dynStarsPerEmpire // This one is a duplicate, but it helps readability
					));
	}
}
