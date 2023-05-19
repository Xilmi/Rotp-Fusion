package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.Rotp;
import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamAAN2;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamTech;
import rotp.ui.util.RandomAlienRaces;

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

	// Restart Always looks for setup options!
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

	ParamTech techIrradiated	= new ParamTech("TECH_IRRADIATED",	3, "ControlEnvironment",6); // level 18
	ParamTech techCloaking		= new ParamTech("TECH_CLOAKING",	2, "Cloaking",			0); // level 27
	ParamTech techStargate		= new ParamTech("TECH_STARGATES",	4, "Stargate", 			0); // level 27
	ParamTech techHyperspace	= new ParamTech("TECH_HYPERSPACE",	0, "HyperspaceComm",	0); // level 34
	ParamTech techIndustry2		= new ParamTech("TECH_INDUSTRY_2",	1, "ImprovedIndustrial",7); // level 38
	ParamTech techThorium		= new ParamTech("TECH_THORIUM",		4, "FuelRange",			8); // level 41
	ParamTech techTransport		= new ParamTech("TECH_TRANSPORTERS",4, "CombatTransporter",	0); // level 45
	LinkedList<ParamTech> techModList		= new LinkedList<>(Arrays.asList(
			techIrradiated, techCloaking, techStargate, techHyperspace,
			techIndustry2, techThorium, techTransport
			));
	default LinkedList<ParamTech> techModList()			{ return techModList; }

	ParamInteger randomAlienRacesMin		 = new ParamInteger(MOD_UI, "RACES_RAND_MIN", -50, -100, 100, 1, 5, 20);
	ParamInteger randomAlienRacesMax		 = new ParamInteger(MOD_UI, "RACES_RAND_MAX", 50, -100, 100, 1, 5, 20);
	ParamInteger randomAlienRacesTargetMax	 = new ParamInteger(MOD_UI, "RACES_RAND_TARGET_MAX", 75, null, null, 1, 10, 100);
	ParamInteger randomAlienRacesTargetMin	 = new ParamInteger(MOD_UI, "RACES_RAND_TARGET_MIN", 0, null, null, 1, 10, 100);
	ParamBoolean randomAlienRacesSmoothEdges = new ParamBoolean(MOD_UI, "RACES_RAND_EDGES", true);

	RandomAlienRaces randomAlienRaces		 = new RandomAlienRaces(MOD_UI, "RACES_ARE_RANDOM", RandomAlienRaces.TARGET);
	default String selectedRandomAlienRaces()			{ return randomAlienRaces.selected(dynOpts()); }
	
	// ==================== GUI List Declarations ====================
	//
	LinkedList<InterfaceParam> modOptionsStaticA  = new LinkedList<>(
			Arrays.asList(
			artifactsHomeworld, fertileHomeworld, richHomeworld, ultraRichHomeworld,
			null,
			techIrradiated, techCloaking, techStargate, techHyperspace,
			null,
			techIndustry2, techThorium, techTransport, randomTechStart, 
			null,
			companionWorlds, battleScout
			));
	LinkedList<InterfaceParam> modOptionsStaticB  = new LinkedList<>(
			Arrays.asList(
			minStarsPerEmpire, prefStarsPerEmpire, maximizeSpacing, spacingLimit, minDistArtifactPlanet,
			null,
			randomAlienRacesTargetMax, randomAlienRacesTargetMin, randomAlienRaces,
			null,
			randomAlienRacesMax, randomAlienRacesMin, randomAlienRacesSmoothEdges,
			null,
			restartChangesPlayerAI, restartChangesAliensAI, restartAppliesSettings, restartChangesPlayerRace
			));

}
