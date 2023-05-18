package rotp.model.game;

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
		{ showFullGuide(true); }
	}		.put("None",	MOD_UI + "RETREAT_NONE")
			.put("AI",		MOD_UI + "RETREAT_AI")
			.put("Player",	MOD_UI + "RETREAT_PLAYER")
			.put("Both",	MOD_UI + "RETREAT_BOTH");
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
	default String selectedTargetBombard()			{ return targetBombard.selected(dynOpts()); }
//	default boolean targetBombardAllowedForAI() {
//		targetBombard.setFromOptions(dynOpts());
//		switch (targetBombard.get()) {
//			case  "Both":
//			case  "AI":
//				return true;
//			default:
//				return false;
//		}
//	}
//	default boolean targetBombardAllowedForPlayer() {
//		targetBombard.setFromOptions(dynOpts());
//		switch (targetBombard.get()) {
//			case  "Both":
//			case  "Player":
//				return true;
//			default:
//				return false;
//		}
//	}

//	ParamInteger customDifficulty			= new ParamInteger(MOD_UI, "CUSTOM_DIFFICULTY"
//			, 100, 20, 500, 1, 5, 20);
//	default int selectedCustomDifficulty()			{ return customDifficulty.selected(dynOpts()); }
//
//	ParamBoolean dynamicDifficulty			= new ParamBoolean(MOD_UI, "DYNAMIC_DIFFICULTY", false);
//	default boolean selectedDynamicDifficulty()		{ return dynamicDifficulty.selected(dynOpts()); }
//
//	ParamList scrapRefundOption				= new ParamList(MOD_UI, "SCRAP_REFUND", "All") {
//		{
//			showFullGuide(true);
//			put("All",		MOD_UI + "SCRAP_REFUND_ALL");
//			put("Empire",	MOD_UI + "SCRAP_REFUND_EMPIRE");
//			put("Ally",		MOD_UI + "SCRAP_REFUND_ALLY");
//			put("Never",	MOD_UI + "SCRAP_REFUND_NEVER");
//		}
//	};
//	default String selectedScrapRefundOption()		{ return scrapRefundOption.selected(dynOpts()); }
//
//	ParamFloat scrapRefundFactor			= new ParamFloat(MOD_UI, "SCRAP_REFUND_FACTOR"
//			, 0.25f, 0f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
//	default float selectedScrapRefundFactor()		{ return scrapRefundFactor.selected(dynOpts()); }
//
//	ParamFloat missileSizeModifier			= new ParamFloat(MOD_UI, "MISSILE_SIZE_MODIFIER"
//			, 2f/3f, 0.1f, 1f, 0.01f, 0.05f, 0.2f, "0.##", "%");
//	default float selectedMissileSizeModifier()		{ return missileSizeModifier.selected(dynOpts()); }
//
//	ParamBoolean challengeMode				= new ParamBoolean(MOD_UI, "CHALLENGE_MODE", false);
//	default boolean selectedChallengeMode()			{ return challengeMode.selected(dynOpts()); }

}
