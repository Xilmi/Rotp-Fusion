package rotp.model.game;

import static rotp.model.game.IFlagOptions.autoFlagOptionsUI;
import static rotp.model.game.IFlagOptions.flagColorCount;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.model.empires.GalacticCouncil;
import rotp.model.events.RandomEvents;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

public interface IInGameOptions extends BaseOptionsTools {

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
			put("None",		MOD_UI + "RETREAT_NONE");
			put("AI",		MOD_UI + "RETREAT_AI");
			put("Player",	MOD_UI + "RETREAT_PLAYER");
			put("Both",		MOD_UI + "RETREAT_BOTH");
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

	ParamFloat missileSizeModifier	= new ParamFloat(MOD_UI, "MISSILE_SIZE_MODIFIER"
			, 2f/3f, 0.1f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
	default float selectedMissileSizeModifier()	{ return missileSizeModifier.get(); }

	ParamBoolean challengeMode		= new ParamBoolean(MOD_UI, "CHALLENGE_MODE", false);
	default boolean selectedChallengeMode()		{ return challengeMode.get(); }
	
	ParamFloat counciRequiredPct	= new ParamFloat(MOD_UI, "COUNCIL_REQUIRED_PCT"
			, GalacticCouncil.PCT_REQUIRED , 0f, 0.99f, 0.01f/3f, 0.02f, 0.1f, "0.0##", "‰") {
		@Override public void setOption(Float newValue) {
			GalacticCouncil.PCT_REQUIRED = newValue;
		}
	};
	default float selectedCounciRequiredPct()	{ return counciRequiredPct.get(); }

	ParamInteger eventsStartTurn	= new ParamInteger(MOD_UI, "EVENTS_START_TURN"
			, RandomEvents.START_TURN, 1, null, 1, 5, 20) {
		@Override public void setOption(Integer newValue) {
			RandomEvents.START_TURN = newValue;
		}
	};
	default int selectedEventsStartTurn()		{ return eventsStartTurn.get(); }
	
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
	default String selectedAutoTerraformEnding() { return autoTerraformEnding.get(); }

	ParamBoolean showAllAI			= new ParamBoolean(MOD_UI, "SHOW_ALL_AI", true);
	default boolean selectedShowAllAI()			 { return showAllAI.get(); }
	

	// ==================== GUI List Declarations ====================
	LinkedList<InterfaceParam> modOptionsDynamicA = new LinkedList<>(
			Arrays.asList(
				customDifficulty, dynamicDifficulty, challengeMode, showAllAI,
				null,
				missileSizeModifier, retreatRestrictions, retreatRestrictionTurns,
				null,
				bombingTarget, targetBombard, flagColorCount, autoFlagOptionsUI,
				null,
				scrapRefundFactor, scrapRefundOption, autoTerraformEnding
			));
	LinkedList<InterfaceParam> modOptionsDynamicB = new LinkedList<>(
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
