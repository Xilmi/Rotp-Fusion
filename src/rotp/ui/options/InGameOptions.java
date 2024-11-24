package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.IGalaxyOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

public final class InGameOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = IN_GAME_OPTIONS_UI_KEY;
	
	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{ return inGameOptionsMap(); }

	public static SafeListPanel inGameOptionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_DIFFICULTY"),
				IGalaxyOptions.getDifficultySelection(), customDifficulty,
				dynamicDifficulty, challengeMode,

				HEADER_SPACER_50,
				new ParamTitle("GAME_VARIOUS"),
				terraforming,
				colonizing, researchRate,
				warpSpeed, fuelRange, popGrowthFactor,
				realNebulaSize, realNebulaShape,
				realNebulaeOpacity,
				developedDefinition,
				maxMissingPopulation, maxMissingFactories,

				HEADER_SPACER_50,
				new ParamTitle("IRONMAN_BASIC"),
				persistentArtifact,
				ironmanNoLoad, ironmanLoadDelay,
				allowSpeciesDetails

//				HEADER_SPACER_50,
//				new ParamTitle("BETA_TEST"),
//				//debugAutoRun, darkGalaxy,
//				rallyCombat, rallyCombatLoss // TODO BR: COMMENT before release
				)));
		SafeListParam list = new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct, councilPlayerVote,
				aiHostility, techTrading,
				allowTechStealing, maxTechsCaptured,
				maxSecurityPct,
				specialPeaceTreaty,

				HEADER_SPACER_50,
				new ParamTitle("GAME_COMBAT"),
				maxCombatTurns,
				retreatRestrictions, retreatRestrictionTurns,
				missileBaseModifier, missileShipModifier,
				targetBombard, bombingTarget,
				scrapRefundOption, scrapRefundFactor,
				shipSpaceFactor
//				maxLandingTroops,
//				maxLandingTroopsAmount,
//				maxLandingTroopsFactor
//
//				HEADER_SPACER_50,
//				new ParamTitle("BETA_TEST"),
//				//debugAutoRun, darkGalaxy,
//				rallyCombat, rallyCombatLoss // TODO BR: COMMENT before release
				));
		list.addAll(AllSubUI.getHandle(NEW_RULES_BETA_UI_KEY).getUiMinor(false));
		map.add(list);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SUB_PANEL_OPTIONS"),
				AllSubUI.randomEventsSubUI(),
				randomEvents,
				AllSubUI.flagSubUI(),
				flagColorCount,
				AllSubUI.governorSubUI(),
				AllSubUI.combatSubUI(),
				AllSubUI.commonSubUI(),

				HEADER_SPACER_50,
				new ParamTitle("GAME_AUTOMATION"),
				autoBombard_, autoColonize_, spyOverSpend,
				transportAutoEco, defaultForwardRally,
				defaultChainRally, 	chainRallySpeed,
				showAlliancesGNN, hideMinorReports,
				showAllocatePopUp, showLimitedWarnings,
				techExchangeAutoRefuse, autoTerraformEnding,
				trackUFOsAcrossTurns, useSmartRefit
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("MENU_OPTIONS"),
				divertExcessToResearch, defaultMaxBases, displayYear,
				showNextCouncil, systemNameDisplay,
				shipDisplay, flightPathDisplay,
				showGridCircular, showShipRanges,
				galaxyPreviewColorStarsSize,
				raceStatusLog, raceStatusView,
				compactOptionOnly, showPendingOrders,
				
				HEADER_SPACER_50,
				new ParamTitle("XILMI_AI_OPTIONS"),
				playerAttackConfidence, playerDefenseConfidence,
				aiAttackConfidence, aiDefenseConfidence,

				HEADER_SPACER_50,
				new ParamTitle("ENOUGH_IS_ENOUGH"),
				disableAutoHelp, disableAdvisor
				)));
		return map;
	}
}
