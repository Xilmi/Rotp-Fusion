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
import static rotp.model.game.IDebugOptions.debugAutoRun;
import static rotp.model.game.IFlagOptions.autoFlagOptionsUI;
import static rotp.model.game.IFlagOptions.flagColorCount;
import static rotp.model.game.IGalaxyOptions.difficultySelection;
import static rotp.model.game.IIronmanOptions.deterministicArtifact;
import static rotp.model.game.IIronmanOptions.ironmanLoadDelay;
import static rotp.model.game.IIronmanOptions.ironmanNoLoad;
import static rotp.model.game.IMainOptions.compactOptionOnly;
import static rotp.model.game.IMainOptions.galaxyPreviewColorStarsSize;
import static rotp.model.game.IMainOptions.raceStatusLog;
import static rotp.model.game.ICombatOptions.combatOptionsUI;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IInGameOptions extends IRandomEvents, IConvenienceOptions {

	// ========================================================================
	// GamePlay options
	ParamInteger retreatRestrictionTurns	= new ParamInteger(MOD_UI, "RETREAT_RESTRICTION_TURNS"
			, 100, 0, 100, 1, 5, 20);
	default int selectedRetreatRestrictionTurns()	{ return retreatRestrictionTurns.get(); }

	ParamList retreatRestrictions			= new ParamList(MOD_UI, "RETREAT_RESTRICTIONS", "None") {
		{
			showFullGuide(true);
			put("None",		MOD_UI + "RETREAT_NONE");
			put("AI",		MOD_UI + "RETREAT_AI");
			put("Player",	MOD_UI + "RETREAT_PLAYER");
			put("Both",		MOD_UI + "RETREAT_BOTH");
		}
	};
	default int selectedRetreatRestrictions()	{ return retreatRestrictions.getIndex(); }

	ParamList targetBombard					= new ParamList(MOD_UI, "TARGET_BOMBARD", "None") {
		{
			showFullGuide(true);
			put("None",		MOD_UI + "TARGET_BOMBARD_NONE");
			put("AI",		MOD_UI + "TARGET_BOMBARD_AI");
			put("Player",	MOD_UI + "TARGET_BOMBARD_PLAYER");
			put("Both",		MOD_UI + "TARGET_BOMBARD_BOTH");
		}
	};
	default String selectedTargetBombard()		{ return targetBombard.get(); }
	default boolean targetBombardAllowedForAI() {
		switch (targetBombard.get()) {
			case  "Both":
			case  "AI":
				return true;
			default:
				return false;
		}
	}
	default boolean targetBombardAllowedForPlayer() {
		switch (targetBombard.get()) {
			case  "Both":
			case  "Player":
				return true;
			default:
				return false;
		}
	}

	ParamInteger customDifficulty	= new ParamInteger(MOD_UI, "CUSTOM_DIFFICULTY"
			, 100, 20, 500, 1, 5, 20);
	default int selectedCustomDifficulty()		{ return customDifficulty.get(); }

	ParamBoolean dynamicDifficulty	= new ParamBoolean(MOD_UI, "DYNAMIC_DIFFICULTY", false);
	default boolean selectedDynamicDifficulty()	{ return dynamicDifficulty.get(); }

	ParamList scrapRefundOption		= new ParamList(MOD_UI, "SCRAP_REFUND", "All") {
		{
			showFullGuide(true);
			put("All",		MOD_UI + "SCRAP_REFUND_ALL");
			put("Empire",	MOD_UI + "SCRAP_REFUND_EMPIRE");
			put("Ally",		MOD_UI + "SCRAP_REFUND_ALLY");
			put("Never",	MOD_UI + "SCRAP_REFUND_NEVER");
		}
	};
	default String selectedScrapRefundOption()	{ return scrapRefundOption.get(); }

	ParamFloat scrapRefundFactor	= new ParamFloat(MOD_UI, "SCRAP_REFUND_FACTOR"
			, 0.25f, 0f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
	default float selectedScrapRefundFactor()	{ return scrapRefundFactor.get(); }

	ParamFloat missileBaseModifier	= new ParamFloat(MOD_UI, "MISSILE_BASE_MODIFIER"
			, 2f/3f, 0.1f, 2f, 0.01f, 0.05f, 0.2f, "0.##", "%") {
		// If not initialized: get the former common value 
		@Override protected Float getOptionValue(IGameOptions options) {
			Float val = options.dynOpts().getFloat(getLangLabel());
			if (val == null)
				val = options.dynOpts().getFloat(MOD_UI+"MISSILE_SIZE_MODIFIER", creationValue());
			return val;
		}
	};
	default float selectedMissileBaseModifier()	{ return missileBaseModifier.get(); }

	ParamFloat missileShipModifier	= new ParamFloat(MOD_UI, "MISSILE_SHIP_MODIFIER"
			, 2f/3f, 0.1f, 2f, 0.01f, 0.05f, 0.2f, "0.##", "%") {
		// If not initialized: get the former common value 
		@Override protected Float getOptionValue(IGameOptions options) {
			Float val = options.dynOpts().getFloat(getLangLabel());
			if (val == null)
				val = options.dynOpts().getFloat(MOD_UI+"MISSILE_SIZE_MODIFIER", creationValue());
			return val;
		}
	};
	default float selectedMissileShipModifier()	{ return missileShipModifier.get(); }

	ParamBoolean challengeMode		= new ParamBoolean(MOD_UI, "CHALLENGE_MODE", false);
	default boolean selectedChallengeMode()		{ return challengeMode.get(); }
	
	ParamFloat counciRequiredPct	= new ParamFloat(MOD_UI, "COUNCIL_REQUIRED_PCT"
			, 2f/3f , 0f, 0.99f, 0.01f/3f, 0.02f, 0.1f, "0.0##", "‰");

	ParamInteger bombingTarget		= new ParamInteger(MOD_UI, "BOMBING_TARGET", 10, null, null, 1, 5, 20);
	default int selectedBombingTarget()			{ return bombingTarget.get(); }

	ParamList autoTerraformEnding	= new ParamList( MOD_UI, "AUTO_TERRAFORM_ENDING", "Populated") {
		{
			showFullGuide(true);
			put("Populated",	MOD_UI + "TERRAFORM_POPULATED");
			put("Terraformed",	MOD_UI + "TERRAFORM_TERRAFORMED");
			put("Cleaned",		MOD_UI + "TERRAFORM_CLEANED");
		}
	};
	default String selectedAutoTerraformEnding()	{ return autoTerraformEnding.get(); }

	ParamBoolean showAllAI			= new ParamBoolean(MOD_UI, "SHOW_ALL_AI", true) {
		@Override public Boolean set(Boolean newValue) {
			super.set(newValue);
			rotp.ui.game.SetupGalaxyUI.specificAI().reInit(null);
			rotp.ui.game.SetupGalaxyUI.opponentAI().reInit(null);
			rotp.model.game.IAdvOptions.autoplay.reInit(null);
			return get();
		}
	};
	default boolean selectedShowAllAI()			{ return showAllAI.get(); }

	ParamBoolean trackUFOsAcrossTurns = new ParamBoolean(MOD_UI, "TRACK_UFOS_ACROSS_TURNS", false);
	default boolean selectedTrackUFOsAcrossTurns() { return trackUFOsAcrossTurns.get(); }

	ParamBoolean allowTechStealing	= new ParamBoolean(MOD_UI, "ALLOW_TECH_STEALING", true);
	default boolean selectedAllowTechStealing()	{ return allowTechStealing.get(); }
	default boolean forbidTechStealing()	 	{ return !allowTechStealing.get(); }

	ParamInteger maxSecurityPct		= new ParamInteger(MOD_UI, "MAX_SECURITY_PCT", 10, 10, 90, 1, 5, 20);
	default int selectedMaxSecurityPct()		{ return maxSecurityPct.get(); }

	ParamList darkGalaxy			= new ParamList( MOD_UI, "DARK_GALAXY", "No") {
		{
			showFullGuide(true);
			isValueInit(false);
			put("No",		MOD_UI + "DARK_GALAXY_NO");
			put("Shrink",	MOD_UI + "DARK_GALAXY_SHRINK");
			put("NoSpy",	MOD_UI + "DARK_GALAXY_NO_SPY");
			put("Spy",		MOD_UI + "DARK_GALAXY_SPY");
		}
	};
	default boolean selectedDarkGalaxy()	{
		return !darkGalaxy.get().equalsIgnoreCase("No") 
				&& GameSession.instance().inProgress(); // for the final replay
	}
	default boolean darkGalaxySpy()			{ return darkGalaxy.get().equalsIgnoreCase("Spy"); }
	default boolean darkGalaxyNoSpy()		{ return darkGalaxy.get().equalsIgnoreCase("NoSpy"); }
	default boolean darkGalaxyDark()		{ return darkGalaxy.get().equalsIgnoreCase("Shrink"); }
	
//	ParamBoolean transportAutoRefill	= new ParamBoolean(MOD_UI, "TRANSPORT_AUTO_REFILL", false);
//	default boolean transportAutoRefill()	{ return transportAutoRefill.get(); }
//	default void transportAutoRefillToggle(){ transportAutoRefill.toggle(); }

	ParamList transportAutoEco			= new ParamList( MOD_UI, "TRANSPORT_AUTO_ECO", "No") {
		{
			showFullGuide(true);
			isValueInit(false);
			put("No",	MOD_UI + "TRANSPORT_AUTO_ECO_NO");
			put("Yes",	MOD_UI + "TRANSPORT_AUTO_ECO_YES");
			put("Last",	MOD_UI + "TRANSPORT_AUTO_ECO_LAST");
		}
	};
	default boolean transportAutoEcoDefaultNo()	{ return transportAutoEco.get().equals("No"); }
	default boolean transportAutoEcoDefaultYes(){ return transportAutoEco.get().equals("Yes"); }
	default boolean transportAutoEcoLast()		{ return transportAutoEco.get().equals("Last"); }

	
	ParamBoolean spyOverSpend			= new ParamBoolean(MOD_UI, "SPY_OVERSPEND", true);
	default boolean spyOverSpend()			{ return spyOverSpend.get(); }

	// ==================== GUI List Declarations ====================
	static LinkedList<IParam> modDynamicAOptions() {
		return new LinkedList<>(
				Arrays.asList(
						customDifficulty, dynamicDifficulty,
						challengeMode, showAllAI,
						trackUFOsAcrossTurns,
						null,
						missileBaseModifier, missileShipModifier,
						retreatRestrictions, retreatRestrictionTurns,
						null,
						bombingTarget, targetBombard,
						flagColorCount, allowTechStealing,
						autoFlagOptionsUI,
						null,
						scrapRefundFactor, scrapRefundOption,
						autoTerraformEnding, maxSecurityPct
				));

	}
	static LinkedList<IParam> modDynamicBOptions() {
		return new LinkedList<>(
				Arrays.asList(
						counciRequiredPct, darkGalaxy,
						transportAutoEco,
						GovernorOptions.governorOptionsUI,
						null,
						eventsStartTurn, eventsPace,
						spyOverSpend,
						ICombatOptions.combatOptionsUI,
						null,
						fixedEventsMode, eventsFavorWeak,
						IRandomEvents.customRandomEventUI
						));
	}
	// ==================== GUI List Declarations ====================
	LinkedList<IParam> inGameOptions	= inGameOptions();
//	LinkedList<LinkedList<IParam>> inGameOptionsMap = inGameOptionsMap(); 
	static LinkedList<IParam> inGameOptions() {
		return IBaseOptsTools.getSingleList(inGameOptionsMap());
	}

	static LinkedList<LinkedList<IParam>> inGameOptionsMap()	{
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_DIFFICULTY"),
				difficultySelection, customDifficulty,
				dynamicDifficulty, challengeMode,

				headerSpacer,
				new ParamTitle("GAME_VARIOUS"),
				transportAutoEco, terraforming,
				colonizing, researchRate,
				warpSpeed, fuelRange,
				spyOverSpend,

				headerSpacer,
				new ParamTitle("IRONMAN_BASIC"),
				deterministicArtifact,
				ironmanNoLoad, ironmanLoadDelay
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct, aiHostility,
				techTrading, allowTechStealing, maxSecurityPct,

				headerSpacer,
				new ParamTitle("GAME_COMBAT"),
				retreatRestrictions, retreatRestrictionTurns,
				missileBaseModifier, missileShipModifier,
				targetBombard, bombingTarget, autoBombard_, autoColonize_,
				scrapRefundFactor, scrapRefundOption
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("SUB_PANEL_OPTIONS"),
				customRandomEventUI,
				autoFlagOptionsUI,
				GovernorOptions.governorOptionsUI,
				combatOptionsUI,
				IMainOptions.commonOptionsUI(),

				headerSpacer,
				new ParamTitle("GAME_OTHER"),
				randomEvents,
				flagColorCount, 
				showAlliancesGNN, hideMinorReports, showLimitedWarnings,
				techExchangeAutoRefuse, autoTerraformEnding, trackUFOsAcrossTurns,

				headerSpacer,
				new ParamTitle("BETA_TEST"),
				debugAutoRun, darkGalaxy
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("MENU_OPTIONS"),
				divertExcessToResearch, defaultMaxBases, displayYear,
				showNextCouncil, systemNameDisplay, shipDisplay, flightPathDisplay,
				showGridCircular, showShipRanges, galaxyPreviewColorStarsSize,
				showAllAI, raceStatusLog, compactOptionOnly
				)));
		return map;
	};
	String IN_GAME_GUI_ID	= "IN_GAME_OPTIONS";
	static ParamSubUI inGameOptionsUI() {
		return new ParamSubUI( MOD_UI, IN_GAME_GUI_ID, inGameOptionsMap())
		{ { isCfgFile(false); } };
	}
	ParamSubUI inGameOptionsUI	= inGameOptionsUI();

}
