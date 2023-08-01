package rotp.model.game;

import static rotp.model.game.IFlagOptions.autoFlagOptionsUI;
import static rotp.model.game.IFlagOptions.flagColorCount;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

public interface IInGameOptions extends IConvenienceOptions {

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

	ParamInteger eventsStartTurn	= new ParamInteger(MOD_UI, "EVENTS_START_TURN", 50, 1, null, 1, 5, 20);
	
	ParamInteger piratesDelayTurn	= new ParamInteger(MOD_UI, "PIRATES_DELAY_TURN", 25, 0, null, 1, 5, 20);
	default int selectedPiratesDelayTurn()		{ return piratesDelayTurn.get(); }

	ParamInteger amoebaDelayTurn	= new ParamInteger(MOD_UI, "AMOEBA_DELAY_TURN", 100, 0, null, 1, 5, 20);
	default int selectedAmoebaDelayTurn()		{ return amoebaDelayTurn.get(); }

	ParamInteger crystalDelayTurn	= new ParamInteger(MOD_UI, "CRYSTAL_DELAY_TURN", 100, 0, null, 1, 5, 20);
	default int selectedCrystalDelayTurn()		{ return crystalDelayTurn.get(); }

	ParamInteger piratesReturnTurn	= new ParamInteger(MOD_UI, "PIRATES_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedPiratesReturnTurn()		{ return piratesReturnTurn.get(); }

	ParamInteger amoebaReturnTurn	= new ParamInteger(MOD_UI, "AMOEBA_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedAmoebaReturnTurn()		{ return amoebaReturnTurn.get(); }

	ParamInteger crystalReturnTurn	= new ParamInteger(MOD_UI, "CRYSTAL_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedCrystalReturnTurn()		{ return crystalReturnTurn.get(); }

	ParamInteger piratesMaxSystems	= new ParamInteger(MOD_UI, "PIRATES_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedPiratesMaxSystems()		{ return piratesMaxSystems.get(); }

	ParamInteger amoebaMaxSystems	= new ParamInteger(MOD_UI, "AMOEBA_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedAmoebaMaxSystems()		{ return amoebaMaxSystems.get(); }

	ParamInteger crystalMaxSystems	= new ParamInteger(MOD_UI, "CRYSTAL_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedCrystalMaxSystems()		{ return crystalMaxSystems.get(); }

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

	ParamBoolean trackUFOsAcrossTurns = new ParamBoolean(MOD_UI, "TRACK_UFOS_ACROSS_TURNS", true);
	
	default boolean selectedTrackUFOsAcrossTurns() { return trackUFOsAcrossTurns.get(); }
	
	// ==================== GUI List Declarations ====================
	LinkedList<IParam> modOptionsDynamicA = new LinkedList<>(
			Arrays.asList(
				customDifficulty, dynamicDifficulty, challengeMode, showAllAI,
				null,
				missileBaseModifier, missileShipModifier, retreatRestrictions, retreatRestrictionTurns,
				null,
				bombingTarget, targetBombard, flagColorCount, autoFlagOptionsUI,
				null,
				scrapRefundFactor, scrapRefundOption, autoTerraformEnding, trackUFOsAcrossTurns
			));
	LinkedList<IParam> modOptionsDynamicB = new LinkedList<>(
			Arrays.asList(
				eventsStartTurn, counciRequiredPct, GovernorOptions.governorOptionsUI,
				null,
				amoebaDelayTurn, amoebaMaxSystems, amoebaReturnTurn,
				null,
				crystalDelayTurn, crystalMaxSystems, crystalReturnTurn,
				null,
				piratesDelayTurn, piratesMaxSystems, piratesReturnTurn
			));
}
