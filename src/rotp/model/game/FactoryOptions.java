package rotp.model.game;

import rotp.Rotp;
import rotp.ui.RotPUI;
import rotp.ui.util.ParamAAN2;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

public interface FactoryOptions extends BaseOptions {

	// ========================================================================
	// Factory options
	ParamAAN2 artifactsHomeworld		= new ParamAAN2("HOME_ARTIFACT");
	default ParamAAN2 selectedArtifactsHomeworld() {
		artifactsHomeworld.setFromOptions(dynOpts());
		return artifactsHomeworld;
	}
	ParamAAN2 fertileHomeworld			= new ParamAAN2("HOME_FERTILE");
	default ParamAAN2 selectedFertileHomeworld() {
		fertileHomeworld.setFromOptions(dynOpts());
		return fertileHomeworld;
	}
	ParamAAN2 richHomeworld				= new ParamAAN2("HOME_RICH");
	default ParamAAN2 selectedRichHomeworld() {
		richHomeworld.setFromOptions(dynOpts());
		return richHomeworld;
	}
	ParamAAN2 ultraRichHomeworld		= new ParamAAN2("HOME_ULTRA_RICH");
	default ParamAAN2 selectedUltraRichHomeworld() {
		ultraRichHomeworld.setFromOptions(dynOpts());
		return ultraRichHomeworld;
	}
	ParamFloat minDistArtifactPlanet	= new ParamFloat(
			MOD_UI, "DIST_ARTIFACT_PLANET", 0.0f, 0.0f, null, 0.2f, 1f, 5f, "0.0##", "0.0");
	default float selectedMinDistArtifactPlanet() { return minDistArtifactPlanet.selected(dynOpts()); }

	ParamBoolean battleScout		= new ParamBoolean( MOD_UI, "BATTLE_SCOUT", false);
	default boolean selectedBattleScout()		{ return battleScout.selected(dynOpts()); }

	ParamBoolean randomTechStart	= new ParamBoolean( MOD_UI, "RANDOM_TECH_START", false);
	default boolean selectedRandomTechStart()	{ return randomTechStart.selected(dynOpts()); }

	ParamInteger companionWorlds	= new ParamInteger( MOD_UI, "COMPANION_WORLDS" , 0, -4, 6, true);
	default int selectedCompanionWorlds() 		{ return Math.abs(companionWorlds.selected(dynOpts())); }
	default int signedCompanionWorlds() 		{ return companionWorlds.selected(dynOpts()); }

	ParamBoolean maximizeSpacing	= new ParamBoolean( MOD_UI, "MAX_SPACINGS", false);
	default boolean selectedMaximizeSpacing()	{ return maximizeSpacing.selected(dynOpts()); }

	ParamInteger spacingLimit		= new ParamInteger( MOD_UI, "MAX_SPACINGS_LIM"
			, 16, 3, Rotp.maximumSystems-1, 1, 10, 100);
	default int selectedSpacingLimit()			{ return spacingLimit.selected(dynOpts()); }

	ParamInteger minStarsPerEmpire	= new ParamInteger( MOD_UI, "MIN_STARS_PER_EMPIRE"
			, 3, 3, Rotp.maximumSystems-1, 1, 5, 20);
	default int selectedMinStarsPerEmpire()		{ return minStarsPerEmpire.selected(dynOpts()); }

	ParamInteger prefStarsPerEmpire	= new ParamInteger( MOD_UI, "PREF_STARS_PER_EMPIRE"
			, 10, 3, Rotp.maximumSystems-1, 1, 10, 100);
	default int selectedPrefStarsPerEmpire()	{ return prefStarsPerEmpire.selected(dynOpts()); }

	ParamInteger dynStarsPerEmpire	= new ParamInteger( MOD_UI, "DYN_STARS_PER_EMPIRE"
			, 10, 3, Rotp.maximumSystems-1, 1, 10, 100) {
		@Override public Integer defaultValue() {
			return prefStarsPerEmpire.get();
		}
	};
	default int selectedDynStarsPerEmpire()		{ return Math.abs(dynStarsPerEmpire.selected(RotPUI.newOptions().dynOpts())); }

	// Always look for setup options!
	ParamBoolean restartChangesAliensAI		= new ParamBoolean( MOD_UI, "RESTART_CHANGES_ALIENS_AI", false);
	default boolean selectedRestartChangesAliensAI()	{ return restartChangesAliensAI.selected(RotPUI.newOptions().dynOpts()); }
	ParamBoolean restartChangesPlayerAI		= new ParamBoolean( MOD_UI, "RESTART_CHANGES_PLAYER_AI", false);
	default boolean selectedRestartChangesPlayerAI()	{ return restartChangesPlayerAI.selected(RotPUI.newOptions().dynOpts()); }
	ParamBoolean restartAppliesSettings		= new ParamBoolean( MOD_UI, "RESTART_APPLY_SETTINGS",false);
	default boolean selectedRestartAppliesSettings()	{ return restartAppliesSettings.selected(RotPUI.newOptions().dynOpts()); }
	ParamList    restartChangesPlayerRace	= new ParamList( MOD_UI, "RESTART_PLAYER_RACE", "Swap") {
		{
			showFullGuide(true);
			put("Last", 	MOD_UI + "RESTART_PLAYER_RACE_LAST");
			put("Swap",		MOD_UI + "RESTART_PLAYER_RACE_SWAP");
			put("GuiSwap",	MOD_UI + "RESTART_PLAYER_RACE_GUI_SWAP");
			put("GuiLast",	MOD_UI + "RESTART_PLAYER_RACE_GUI_LAST");
		}
	};
	default String selectedRestartChangesPlayerRace()	{ return restartChangesPlayerRace.selected(RotPUI.newOptions().dynOpts()); }

}
