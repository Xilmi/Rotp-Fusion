package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.IGalaxyOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

public final class RulesOptions implements IOptionsSubUI {
	static final String OPTION_ID = RULES_OPTIONS_UI_KEY;
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{ return inGameOptionsMap(); }

	public static SafeListPanel inGameOptionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_DIFFICULTY"),
				IGalaxyOptions.getDifficultySelection(), customDifficulty,
				dynamicDifficulty, challengeMode,

				headerSpacer50,
				new ParamTitle("GAME_VARIOUS"),
				terraforming,
				colonizing, researchRate,
				warpSpeed, fuelRange, popGrowthFactor,
				realNebulaSize, realNebulaShape,
				realNebulaeOpacity,
				developedDefinition,
				maxMissingPopulation, maxMissingFactories,

				headerSpacer50,
				new ParamTitle("IRONMAN_BASIC"),
				persistentArtifact,
				ironmanNoLoad, ironmanLoadDelay,
				allowSpeciesDetails
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct, councilPlayerVote,
				aiHostility, techTrading,
				allowTechStealing, maxTechsCaptured,
				maxSecurityPct,
				specialPeaceTreaty

				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SUB_PANEL_OPTIONS"),
				AllSubUI.randomEventsSubUI(),
				randomEvents,
				AllSubUI.governorSubUI(),
				AllSubUI.getHandle(SHIP_COMBAT_RULES_UI_KEY).getUI(),
//				AllSubUI.combatSubUI(),
				AllSubUI.commonSubUI(),
				
				headerSpacer50,
				new ParamTitle("BETA_TEST"),
				debugAutoRun, darkGalaxy
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_COMBAT"),
				maxCombatTurns,
				retreatRestrictions, retreatRestrictionTurns,
				missileBaseModifier, missileShipModifier,
				targetBombard, bombingTarget,
				scrapRefundOption, scrapRefundFactor,
				shipSpaceFactor,

				headerSpacer50,
				new ParamTitle("XILMI_AI_OPTIONS"),
				playerAttackConfidence, playerDefenseConfidence,
				aiAttackConfidence, aiDefenseConfidence
				)));
		return map;
	}
}
