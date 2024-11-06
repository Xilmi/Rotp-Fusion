package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.IGalaxyOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class SystemsOptions implements IOptionsSubUI {
	static final String OPTION_ID = "SYSTEMS_OPTIONS";
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("ARTIFACT_OPTIONS"),
				artifactPlanetMult, artifactPlanetOffset,
				lineSpacer25,
				orionPlanetProb,
				lineSpacer25,
				allowRichPoorArtifact,
				
				headerSpacer50,
				new ParamTitle("GAME_OTHER"),
				orionToEmpireModifier
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RICH_OPTIONS"),
				ultraPoorPlanetMult, ultraPoorPlanetOffset,
				poorPlanetMult, poorPlanetOffset,
				lineSpacer25,
				richPlanetMult, richPlanetOffset,
				ultraRichPlanetMult, ultraRichPlanetOffset
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("HOMEWORLD_NEIGHBORHOOD"),
				firstRingSystemNumber,
				firstRingHabitable,
				firstRingRadius,
				secondRingSystemNumber,
				secondRingHabitable,
				secondRingRadius,

				headerSpacer50,
				new ParamTitle("LINKED_OPTIONS"),
				starDensity,
				IGalaxyOptions.getSizeSelection(),
				dynStarsPerEmpire
				)));
		return map;
	}
}
