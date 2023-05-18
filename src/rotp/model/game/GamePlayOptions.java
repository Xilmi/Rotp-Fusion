package rotp.model.game;

import static rotp.model.game.BaseOptions.MOD_UI;

import rotp.model.empires.GalacticCouncil;
import rotp.model.events.RandomEvents;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

public interface GamePlayOptions extends BaseOptions {

	// ========================================================================
	// GamePlay options
	ParamInteger retreatRestrictionTurns	= new ParamInteger(MOD_UI, "RETREAT_RESTRICTION_TURNS"
			, 100, 0, 100, 1, 5, 20);
	default int selectedRetreatRestrictionTurns()	{ return retreatRestrictionTurns.selected(dynOpts()); }

	ParamList retreatRestrictions			= new ParamList(MOD_UI, "RETREAT_RESTRICTIONS", "None") {
		{
			showFullGuide(true);
			put("None",		MOD_UI + "RETREAT_NONE");
			put("AI",		MOD_UI + "RETREAT_AI");
			put("Player",	MOD_UI + "RETREAT_PLAYER");
			put("Both",		MOD_UI + "RETREAT_BOTH");
		}
	};
	default int selectedRetreatRestrictions()	{
		retreatRestrictions.setFromOptions(dynOpts());
		return retreatRestrictions.getIndex();
		}

	ParamList targetBombard					= new ParamList(MOD_UI, "TARGET_BOMBARD", "None") {
		{
			showFullGuide(true);
			put("None",		MOD_UI + "RETREAT_NONE");
			put("AI",		MOD_UI + "RETREAT_AI");
			put("Player",	MOD_UI + "RETREAT_PLAYER");
			put("Both",		MOD_UI + "RETREAT_BOTH");
		}
	};
	default String selectedTargetBombard()		{ return targetBombard.selected(dynOpts()); }
	default boolean targetBombardAllowedForAI() {
		targetBombard.setFromOptions(dynOpts());
		switch (targetBombard.get()) {
			case  "Both":
			case  "AI":
				return true;
			default:
				return false;
		}
	}
	default boolean targetBombardAllowedForPlayer() {
		targetBombard.setFromOptions(dynOpts());
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
	default int selectedCustomDifficulty()		{ return customDifficulty.selected(dynOpts()); }

	ParamBoolean dynamicDifficulty	= new ParamBoolean(MOD_UI, "DYNAMIC_DIFFICULTY", false);
	default boolean selectedDynamicDifficulty()	{ return dynamicDifficulty.selected(dynOpts()); }

	ParamList scrapRefundOption		= new ParamList(MOD_UI, "SCRAP_REFUND", "All") {
		{
			showFullGuide(true);
			put("All",		MOD_UI + "SCRAP_REFUND_ALL");
			put("Empire",	MOD_UI + "SCRAP_REFUND_EMPIRE");
			put("Ally",		MOD_UI + "SCRAP_REFUND_ALLY");
			put("Never",	MOD_UI + "SCRAP_REFUND_NEVER");
		}
	};
	default String selectedScrapRefundOption()	{ return scrapRefundOption.selected(dynOpts()); }

	ParamFloat scrapRefundFactor	= new ParamFloat(MOD_UI, "SCRAP_REFUND_FACTOR"
			, 0.25f, 0f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
	default float selectedScrapRefundFactor()	{ return scrapRefundFactor.selected(dynOpts()); }

	ParamFloat missileSizeModifier	= new ParamFloat(MOD_UI, "MISSILE_SIZE_MODIFIER"
			, 2f/3f, 0.1f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
	default float selectedMissileSizeModifier()	{ return missileSizeModifier.selected(dynOpts()); }

	ParamBoolean challengeMode		= new ParamBoolean(MOD_UI, "CHALLENGE_MODE", false);
	default boolean selectedChallengeMode()		{ return challengeMode.selected(dynOpts()); }
	
	ParamFloat counciRequiredPct	= new ParamFloat(MOD_UI, "COUNCIL_REQUIRED_PCT"
			, GalacticCouncil.PCT_REQUIRED , 0f, 0.99f, 0.01f/3f, 0.02f, 0.1f, "0.0##", "‰") {
		@Override public Float set(Float newValue) {
			GalacticCouncil.PCT_REQUIRED = newValue;
			return super.set(newValue);
		}
	};
	default float selectedCounciRequiredPct()	{ return counciRequiredPct.selected(dynOpts()); }

	ParamInteger eventsStartTurn	= new ParamInteger(MOD_UI, "EVENTS_START_TURN"
			, RandomEvents.START_TURN, 1, null, 1, 5, 20) {
		@Override public Integer set(Integer newValue) {
			RandomEvents.START_TURN = newValue;
			return super.set(newValue);
		}
	};
	default int selectedEventsStartTurn()		{ return eventsStartTurn.selected(dynOpts()); }
	
	ParamInteger piratesDelayTurn	= new ParamInteger(MOD_UI, "PIRATES_DELAY_TURN", 25, 0, null, 1, 5, 20);
	default int selectedPiratesDelayTurn()		{ return piratesDelayTurn.selected(dynOpts()); }

	ParamInteger amoebaDelayTurn	= new ParamInteger(MOD_UI, "AMOEBA_DELAY_TURN", 100, 0, null, 1, 5, 20);
	default int selectedAmoebaDelayTurn()		{ return amoebaDelayTurn.selected(dynOpts()); }

	ParamInteger crystalDelayTurn	= new ParamInteger(MOD_UI, "CRYSTAL_DELAY_TURN", 100, 0, null, 1, 5, 20);
	default int selectedCrystalDelayTurn()		{ return crystalDelayTurn.selected(dynOpts()); }

	ParamInteger piratesReturnTurn	= new ParamInteger(MOD_UI, "PIRATES_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedPiratesReturnTurn()		{ return piratesReturnTurn.selected(dynOpts()); }

	ParamInteger amoebaReturnTurn	= new ParamInteger(MOD_UI, "AMOEBA_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedAmoebaReturnTurn()		{ return amoebaReturnTurn.selected(dynOpts()); }

	ParamInteger crystalReturnTurn	= new ParamInteger(MOD_UI, "CRYSTAL_RETURN_TURN", 0, 0, null, 1, 5, 20);
	default int selectedCrystalReturnTurn()		{ return crystalReturnTurn.selected(dynOpts()); }

	ParamInteger piratesMaxSystems	= new ParamInteger(MOD_UI, "PIRATES_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedPiratesMaxSystems()		{ return piratesMaxSystems.selected(dynOpts()); }

	ParamInteger amoebaMaxSystems	= new ParamInteger(MOD_UI, "AMOEBA_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedAmoebaMaxSystems()		{ return amoebaMaxSystems.selected(dynOpts()); }

	ParamInteger crystalMaxSystems	= new ParamInteger(MOD_UI, "CRYSTAL_MAX_SYSTEMS", 0, 0, null, 1, 5, 20);
	default int selectedCrystalMaxSystems()		{ return crystalMaxSystems.selected(dynOpts()); }

	
	
	
	ParamInteger bombingTarget		= new ParamInteger(MOD_UI, "BOMBING_TARGET", 10, null, null, 1, 5, 20);
	ParamList	 autoTerraformEnding	= new ParamList( MOD_UI, "AUTO_TERRAFORM_ENDING", "Populated") {
		{
			showFullGuide(true);
			put("Populated",	MOD_UI + "TERRAFORM_POPULATED");
			put("Terraformed",	MOD_UI + "TERRAFORM_TERRAFORMED");
			put("Cleaned",		MOD_UI + "TERRAFORM_CLEANED");
		}
	};

}
