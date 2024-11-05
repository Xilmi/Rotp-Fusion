package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class FlagOptions implements IOptionsSubUI {
	static final String OPTION_ID = "AUTO_FLAG";

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AUTO_FLAG_ID_SELECTION"),
				autoFlagAssignation1, autoFlagAssignation2,
				autoFlagAssignation3, autoFlagAssignation4,

				headerSpacer,
				new ParamTitle("AUTO_FLAG_IN_NEBULA"),
				flagInNebulaColor, flagNotNebulaColor,

				headerSpacer,
				new ParamTitle("AUTO_FLAG_COLONY_TECH"),
				flagTechGaiaColor, flagTechFertileColor, flagTechGoodColor,
				flagTechStandardColor, flagTechBarrenColor, flagTechDeadColor,
				flagTechToxicColor, flagTechRadiatedColor, flagTechNoneColor
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AUTO_FLAG_VESTIGES"),
				flagRuinsOrionColor, flagRuinsAntaranColor, flagRuinsNoneColor,

				headerSpacer,
				new ParamTitle("AUTO_FLAG_RESOURCES"),
				flagOrionColor, flagAntaranColor,
				flagUltraRichColor, flagRichColor, flagAssetNormalColor,
				flagPoorColor, flagUltraPoorColor, flagNoneColor,
				
				headerSpacer,
				new ParamTitle("AUTO_FLAG_ENVIRONMENT"),
				flagEnvGaiaColor, flagEnvFertileColor,
				flagEnvNormalColor,	flagEnvHostileColor, flagEnvNoneColor
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("AUTO_FLAG_TYPE"),
				flagTerranColor, flagJungleColor, flagOceanColor,
				flagAridColor, flagSteppeColor, flagDesertColor, flagMinimalColor,
				flagBarrenColor, flagTundraColor, flagDeadColor,
				flagInfernoColor, flagToxicColor, flagRadiatedColor,
				flagAsteroidColor
				)));
		return map;
	}
}
