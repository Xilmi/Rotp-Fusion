package rotp.model.game;

import static rotp.model.game.IAdvOptions.aiHostility;
import static rotp.model.game.IAdvOptions.colonizing;
import static rotp.model.game.IAdvOptions.councilWin;
import static rotp.model.game.IAdvOptions.fuelRange;
import static rotp.model.game.IAdvOptions.randomEvents;
import static rotp.model.game.IAdvOptions.researchRate;
import static rotp.model.game.IAdvOptions.techTrading;
import static rotp.model.game.IAdvOptions.terraforming;
import static rotp.model.game.IAdvOptions.warpSpeed;
import static rotp.model.game.ICombatOptions.combatOptionsUI;
import static rotp.model.game.IDebugOptions.debugAutoRun;
import static rotp.model.game.IFlagOptions.autoFlagOptionsUI;
import static rotp.model.game.IFlagOptions.flagColorCount;
import static rotp.model.game.IIronmanOptions.allowSpeciesDetails;
import static rotp.model.game.IIronmanOptions.ironmanLoadDelay;
import static rotp.model.game.IIronmanOptions.ironmanNoLoad;
import static rotp.model.game.IIronmanOptions.persistentArtifact;
import static rotp.model.game.IMainOptions.compactOptionOnly;
import static rotp.model.game.IMainOptions.galaxyPreviewColorStarsSize;
import static rotp.model.game.IMainOptions.raceStatusLog;

import java.util.Arrays;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.galaxy.ShipFleet;
import rotp.model.ships.ShipDesign;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IInGameOptions extends IRandomEvents, IConvenienceOptions {

	// ========================================================================
	// GamePlay options
	ParamList popGrowthFactor				= new ParamList(MOD_UI, "POP_GROWTH", "Normal")
			.showFullGuide(true)
			.put("Normal",		MOD_UI + "POP_GROWTH_NORMAL")
			.put("Reduced",		MOD_UI + "POP_GROWTH_REDUCED");
	default String selectedPopGrowthFactor()	{ return popGrowthFactor.get(); }

	ParamInteger retreatRestrictionTurns	= new ParamInteger(MOD_UI, "RETREAT_RESTRICTION_TURNS", 100)
			.setDefaultValue(MOO1_DEFAULT, 1)
			.setLimits(0, 100)
			.setIncrements(1, 5, 20);
	default int selectedRetreatRestrictionTurns()	{ return retreatRestrictionTurns.get(); }

	ParamList retreatRestrictions			= new ParamList(MOD_UI, "RETREAT_RESTRICTIONS", "None")
			.setDefaultValue(MOO1_DEFAULT, "Both")
			.showFullGuide(true)
			.put("None",	MOD_UI + "RETREAT_NONE")
			.put("AI",		MOD_UI + "RETREAT_AI")
			.put("Player",	MOD_UI + "RETREAT_PLAYER")
			.put("Both",	MOD_UI + "RETREAT_BOTH");
	default int selectedRetreatRestrictions()	{ return retreatRestrictions.getIndex(); }

	ParamList targetBombard					= new ParamList(MOD_UI, "TARGET_BOMBARD", "None")
			.showFullGuide(true)
			.put("None",	MOD_UI + "TARGET_BOMBARD_NONE")
			.put("AI",		MOD_UI + "TARGET_BOMBARD_AI")
			.put("Player",	MOD_UI + "TARGET_BOMBARD_PLAYER")
			.put("Both",	MOD_UI + "TARGET_BOMBARD_BOTH");
	default boolean targetBombardAllowedForAI() {
		switch (targetBombard.get().toUpperCase()) {
			case  "BOTH":
			case  "AI":
				return true;
			default:
				return false;
		}
	}
	default boolean targetBombardAllowedForPlayer() {
		switch (targetBombard.get().toUpperCase()) {
			case  "BOTH":
			case  "PLAYER":
				return true;
			default:
				return false;
		}
	}

	ParamInteger customDifficulty	= new ParamInteger(MOD_UI, "CUSTOM_DIFFICULTY", 100)
			.setLimits(20, 500)
			.setIncrements(1, 5, 20);
	default int selectedCustomDifficulty()		{ return customDifficulty.get(); }

	ParamBoolean dynamicDifficulty	= new ParamBoolean(MOD_UI, "DYNAMIC_DIFFICULTY", false);
	default boolean selectedDynamicDifficulty()	{ return dynamicDifficulty.get(); }

	ParamList scrapRefundOption		= new ParamList(MOD_UI, "SCRAP_REFUND", "All")
			.showFullGuide(true)
			.put("All",		MOD_UI + "SCRAP_REFUND_ALL")
			.put("Empire",	MOD_UI + "SCRAP_REFUND_EMPIRE")
			.put("Ally",	MOD_UI + "SCRAP_REFUND_ALLY")
			.put("Never",	MOD_UI + "SCRAP_REFUND_NEVER");
	default String selectedScrapRefundOption()	{ return scrapRefundOption.get(); }

	ParamFloat scrapRefundFactor	= new ParamFloat(MOD_UI, "SCRAP_REFUND_FACTOR", 0.25f)
			.setLimits(0f, 1f)
			.setIncrements(0.01f, 0.05f, 0.2f)
			.cfgFormat( "0.##")
			.guiFormat("%");
	default float selectedScrapRefundFactor()	{ return scrapRefundFactor.get(); }

	ParamFloat missileBaseModifier	= new ParamFloat(MOD_UI, "MISSILE_BASE_MODIFIER", 2f/3f)
			.setDefaultValue(MOO1_DEFAULT, 1f)
			.setDefaultValue(ROTP_DEFAULT, 1f)
			.setLimits(0.1f, 2f)
			.setIncrements(0.01f, 0.05f, 0.2f)
			.cfgFormat("0.##")
			.guiFormat("%")
			.formerName(MOD_UI+"MISSILE_SIZE_MODIFIER");
	default float selectedMissileBaseModifier()	{ return missileBaseModifier.get(); }

	ParamFloat missileShipModifier	= new ParamFloat(MOD_UI, "MISSILE_SHIP_MODIFIER", 2f/3f)
			.setDefaultValue(MOO1_DEFAULT, 1f)
			.setDefaultValue(ROTP_DEFAULT, 1f)
			.setLimits(0.1f, 2f)
			.setIncrements(0.01f, 0.05f, 0.2f)
			.cfgFormat("0.##")
			.guiFormat("%")
			.formerName(MOD_UI+"MISSILE_SIZE_MODIFIER");
	default float selectedMissileShipModifier()	{ return missileShipModifier.get(); }

	ParamBoolean challengeMode		= new ParamBoolean(MOD_UI, "CHALLENGE_MODE", false);
	default boolean selectedChallengeMode()		{ return challengeMode.get(); }
	
	ParamFloat counciRequiredPct	= new ParamFloat(MOD_UI, "COUNCIL_REQUIRED_PCT", 2f/3f)
			.setLimits(0f, 0.99f)
			.setIncrements(0.01f/3f, 0.02f, 0.1f)
			.cfgFormat("0.##")
			.guiFormat("‰");

	ParamInteger bombingTarget		= new ParamInteger(MOD_UI, "BOMBING_TARGET", 10)
			.setLimits(null, null)
			.setIncrements(1, 5, 20);
	default int selectedBombingTarget()			{ return bombingTarget.get(); }

	ParamInteger maxCombatTurns		= new ParamInteger(MOD_UI, "MAX_COMBAT_TURNS", 100)
			.setLimits(10, 1000)
			.setIncrements(1, 5, 20);
	default int maxCombatTurns()				{ return maxCombatTurns.get(); }

	ParamList autoTerraformEnding	= new ParamList( MOD_UI, "AUTO_TERRAFORM_ENDING", "Populated")
			.showFullGuide(true)
			.put("Populated",	MOD_UI + "TERRAFORM_POPULATED")
			.put("Terraformed",	MOD_UI + "TERRAFORM_TERRAFORMED")
			.put("Cleaned",		MOD_UI + "TERRAFORM_CLEANED");
	default String selectedAutoTerraformEnding()	{ return autoTerraformEnding.get(); }

	ParamBoolean trackUFOsAcrossTurns = new ParamBoolean(MOD_UI, "TRACK_UFOS_ACROSS_TURNS", false);
	default boolean selectedTrackUFOsAcrossTurns() { return trackUFOsAcrossTurns.get(); }

	ParamBoolean allowTechStealing	= new ParamBoolean(MOD_UI, "ALLOW_TECH_STEALING", true);
	default boolean forbidTechStealing()	 	{ return !allowTechStealing.get(); }

	ParamInteger maxSecurityPct		= new ParamInteger(MOD_UI, "MAX_SECURITY_PCT", 10)
			.setLimits(10, 90)
			.setIncrements(1, 5, 20);
	default int selectedMaxSecurityPct()		{ return maxSecurityPct.get(); }

	ParamList darkGalaxy			= new ParamList( MOD_UI, "DARK_GALAXY", "No")
			.showFullGuide(true)
			.isValueInit(false)
			.put("No",		MOD_UI + "DARK_GALAXY_NO")
			.put("Shrink",	MOD_UI + "DARK_GALAXY_SHRINK")
			.put("NoSpy",	MOD_UI + "DARK_GALAXY_NO_SPY")
			.put("Spy",		MOD_UI + "DARK_GALAXY_SPY");
	default boolean selectedDarkGalaxy()	{
		return !darkGalaxy.get().equalsIgnoreCase("No") 
				&& GameSession.instance().inProgress(); // for the final replay
	}
	default boolean darkGalaxySpy()			{ return darkGalaxy.get().equalsIgnoreCase("Spy"); }
	default boolean darkGalaxyNoSpy()		{ return darkGalaxy.get().equalsIgnoreCase("NoSpy"); }
	default boolean darkGalaxyDark()		{ return darkGalaxy.get().equalsIgnoreCase("Shrink"); }
	
	ParamList transportAutoEco			= new ParamList( MOD_UI, "TRANSPORT_AUTO_ECO", "No")
			.showFullGuide(true)
			.isValueInit(false)
			.put("No",	 MOD_UI + "TRANSPORT_AUTO_ECO_NO")
			.put("Yes",	 MOD_UI + "TRANSPORT_AUTO_ECO_YES")
			.put("Last", MOD_UI + "TRANSPORT_AUTO_ECO_LAST");
	default boolean transportAutoEcoDefaultNo()	{ return transportAutoEco.get().equals("No"); }
	default boolean transportAutoEcoDefaultYes(){ return transportAutoEco.get().equals("Yes"); }
	default boolean transportAutoEcoLast()		{ return transportAutoEco.get().equals("Last"); }

	ParamBoolean spyOverSpend			= new ParamBoolean(MOD_UI, "SPY_OVERSPEND", true);
	default boolean spyOverSpend()				{ return spyOverSpend.get(); }

	ParamList councilPlayerVote			= new ParamList( MOD_UI, "COUNCIL_PLAYER_VOTE", "By Size")
			.showFullGuide(true)
			.put("First",	MOD_UI + "COUNCIL_PLAYER_VOTE_FIRST")
			.put("By Size",	MOD_UI + "COUNCIL_PLAYER_VOTE_SIZE")
			.put("Last",	MOD_UI + "COUNCIL_PLAYER_VOTE_LAST");
	default boolean playerVotesFirst()	{ return councilPlayerVote.get().equalsIgnoreCase("First"); }
	default boolean playerVotesLast()	{ return councilPlayerVote.get().equalsIgnoreCase("Last"); }

	ParamBoolean defaultForwardRally	= new ParamBoolean(MOD_UI, "DEFAULT_FORWARD_RALLY", true);
	default boolean defaultForwardRally()	{ return defaultForwardRally.get(); }

	ParamBoolean defaultChainRally		= new ParamBoolean(MOD_UI, "DEFAULT_CHAIN_RALLY", true);
	default boolean defaultChainRally()		{ return defaultChainRally.get(); }
	
	String CHAIN_RALLY_SPEED_FLEET	= "FLEET";
	String CHAIN_RALLY_SPEED_MIN	= "MIN";
	String CHAIN_RALLY_SPEED_TOP	= "TOP";
	ParamList chainRallySpeed			= new ParamList( MOD_UI, "CHAIN_RALLY_SPEED", CHAIN_RALLY_SPEED_FLEET)
			.showFullGuide(true)
			.put(CHAIN_RALLY_SPEED_FLEET,	MOD_UI + "CHAIN_RALLY_SPEED_FLEET")
			.put(CHAIN_RALLY_SPEED_MIN,		MOD_UI + "CHAIN_RALLY_SPEED_MIN")
			.put(CHAIN_RALLY_SPEED_TOP,		MOD_UI + "CHAIN_RALLY_SPEED_TOP");
	default String chainRallySpeed()		{ return chainRallySpeed.get(); }
	default Float chainRallySpeed(Empire player, ShipFleet fleet)	{
		if (fleet == null)
			return chainRallySpeed(player);
		switch (chainRallySpeed.get().toUpperCase()) {
			case CHAIN_RALLY_SPEED_FLEET :
				return fleet.slowestStackSpeed();
			case CHAIN_RALLY_SPEED_MIN :
				return fleet.empire().minActiveDesignSpeed();
			case CHAIN_RALLY_SPEED_TOP :
				return fleet.empire().tech().topSpeed();
		}
		return null;
	}
	default Float chainRallySpeed(Empire player, ShipDesign design)	{
		if (design == null)
			return chainRallySpeed(player);
		switch (chainRallySpeed.get().toUpperCase()) {
			case CHAIN_RALLY_SPEED_FLEET :
				return (float) design.warpSpeed();
			case CHAIN_RALLY_SPEED_MIN :
				return player.minActiveDesignSpeed();
			case CHAIN_RALLY_SPEED_TOP :
				return player.tech().topSpeed();
		}
		return null;
	}
	default Float chainRallySpeed(Empire player)	{
		switch (chainRallySpeed.get().toUpperCase()) {
			case CHAIN_RALLY_SPEED_FLEET :
			case CHAIN_RALLY_SPEED_MIN :
				return player.minActiveDesignSpeed();
			case CHAIN_RALLY_SPEED_TOP :
				return player.tech().topSpeed();
		}
		return null;
	}

	String PEACE_TREATY_NORMAL		= "NORMAL";
	String PEACE_TREATY_ARMISTICE	= "NOWAR";
	String PEACE_TREATY_COLD_WAR	= "TRUCE";
	ParamList specialPeaceTreaty	= new ParamList( MOD_UI, "SPECIAL_PEACE_TREATY", PEACE_TREATY_NORMAL)
			.showFullGuide(true)
			.put(PEACE_TREATY_NORMAL,	 MOD_UI + "SPECIAL_PEACE_TREATY_NORMAL")
			.put(PEACE_TREATY_ARMISTICE, MOD_UI + "SPECIAL_PEACE_TREATY_NOWAR")
			.put(PEACE_TREATY_COLD_WAR,	 MOD_UI + "SPECIAL_PEACE_TREATY_TRUCE");
	default boolean allowPeaceTreaty()	{ return !specialPeaceTreaty.get().equalsIgnoreCase(PEACE_TREATY_ARMISTICE) ;}
	default boolean isColdWarMode()		{ return specialPeaceTreaty.get().equalsIgnoreCase(PEACE_TREATY_COLD_WAR) ;}

	String DEVELOPED_ALL		= "ALL";
	String DEVELOPED_NO_BASE	= "NO_BASES";
	String DEVELOPED_INDUSTRY	= "INDUSTRY";
	ParamList developedDefinition	= new ParamList(MOD_UI, "DEVELOPED_DEFINITION", DEVELOPED_ALL)
			.showFullGuide(true)
			.put(DEVELOPED_ALL, 	 MOD_UI + "DEVELOPED_ALL")
			.put(DEVELOPED_NO_BASE,  MOD_UI + "DEVELOPED_NO_BASE")
			.put(DEVELOPED_INDUSTRY, MOD_UI + "DEVELOPED_INDUSTRY");
	default boolean isDeveloped(Colony col)	{
		switch (developedDefinition.get()) {
		case DEVELOPED_NO_BASE:
			return col.industry().isCompleted() && col.ecology().isCompleted();
		case DEVELOPED_INDUSTRY:
			return col.industry().isCompleted();
		case DEVELOPED_ALL:
		default:
			return col.defense().isCompleted() && col.industry().isCompleted() && col.ecology().isCompleted();
		}
	}

	ParamInteger playerAttackConfidence  = new ParamInteger(MOD_UI, "PLAYER_ATTACK_CONFIDENCE", 100)
			.setLimits(100, 500)
			.setIncrements(1, 5, 20);
	ParamInteger playerDefenseConfidence = new ParamInteger(MOD_UI, "PLAYER_DEFENSE_CONFIDENCE", 100)
			.setLimits(100, 500)
			.setIncrements(1, 5, 20);
	ParamInteger aiAttackConfidence		 = new ParamInteger(MOD_UI, "AI_ATTACK_CONFIDENCE", 100)
			.setLimits(100, 500)
			.setIncrements(1, 5, 20);
	ParamInteger aiDefenseConfidence	 = new ParamInteger(MOD_UI, "AI_DEFENSE_CONFIDENCE", 100)
			.setLimits(100, 500)
			.setIncrements(1, 5, 20);
	default float playerAttackConfidence()	{ return playerAttackConfidence.get()/100f; }
	default float playerDefenseConfidence()	{ return playerDefenseConfidence.get()/100f; }
	default float aiAttackConfidence()		{ return aiAttackConfidence.get()/100f; }
	default float aiDefenseConfidence()		{ return aiDefenseConfidence.get()/100f; }

	// ==================== GUI List Declarations ====================
	static SafeListPanel inGameOptionsMap()	{
		SafeListPanel map = new SafeListPanel();
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_DIFFICULTY"),
				IGalaxyOptions.getDifficultySelection(), customDifficulty,
				dynamicDifficulty, challengeMode,

				headerSpacer,
				new ParamTitle("GAME_VARIOUS"),
				terraforming,
				colonizing, researchRate,
				warpSpeed, fuelRange, popGrowthFactor,
				IMainOptions.realNebulaSize, IMainOptions.realNebulaShape,
				IMainOptions.realNebulaeOpacity,
				developedDefinition,

				headerSpacer,
				new ParamTitle("IRONMAN_BASIC"),
				persistentArtifact,
				ironmanNoLoad, ironmanLoadDelay,
				allowSpeciesDetails
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct, councilPlayerVote,
				aiHostility, techTrading,
				allowTechStealing, maxSecurityPct,
				specialPeaceTreaty,

				headerSpacer,
				new ParamTitle("GAME_COMBAT"),
				maxCombatTurns,
				retreatRestrictions, retreatRestrictionTurns,
				missileBaseModifier, missileShipModifier,
				targetBombard, bombingTarget,
				scrapRefundFactor, scrapRefundOption,

				headerSpacer,
				new ParamTitle("BETA_TEST"),
				debugAutoRun, darkGalaxy
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SUB_PANEL_OPTIONS"),
				customRandomEventUI,
				randomEvents,
				autoFlagOptionsUI, flagColorCount,
				GovernorOptions.governorOptionsUI,
				combatOptionsUI,
				IMainOptions.commonOptionsUI(),

				headerSpacer,
				new ParamTitle("GAME_AUTOMATION"),
				autoBombard_, autoColonize_, spyOverSpend,
				transportAutoEco, defaultForwardRally,
				defaultChainRally, 	chainRallySpeed,
				showAlliancesGNN, hideMinorReports, showAllocatePopUp, showLimitedWarnings,
				techExchangeAutoRefuse, autoTerraformEnding, trackUFOsAcrossTurns
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("MENU_OPTIONS"),
				divertExcessToResearch, defaultMaxBases, displayYear,
				showNextCouncil, systemNameDisplay, shipDisplay, flightPathDisplay,
				showGridCircular, showShipRanges, galaxyPreviewColorStarsSize,
				raceStatusLog, compactOptionOnly,
				IMainOptions.showPendingOrders,
				
				headerSpacer,
				new ParamTitle("XILMI_AI_OPTIONS"),
				playerAttackConfidence, playerDefenseConfidence,
				aiAttackConfidence, aiDefenseConfidence,

				headerSpacer,
				new ParamTitle("ENOUGH_IS_ENOUGH"),
				IMainOptions.disableAutoHelp, IMainOptions.disableAdvisor
				)));
		return map;
	};
	String IN_GAME_GUI_ID	= "IN_GAME_OPTIONS";
	static ParamSubUI inGameOptionsUI() {
		return new ParamSubUI( MOD_UI, IN_GAME_GUI_ID, inGameOptionsMap()).isCfgFile(false);
	}
	ParamSubUI inGameOptionsUI	= inGameOptionsUI();
}
