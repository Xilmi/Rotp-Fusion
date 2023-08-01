package rotp.model.game;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import rotp.Rotp;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamAAN2;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamTech;
import rotp.ui.util.ParamTitle;
import rotp.ui.util.RandomAlienRaces;

public interface IPreGameOptions extends IAdvOptions {

	// ========================================================================
	// Factory options
	ParamAAN2 artifactsHomeworld		= new ParamAAN2("HOME_ARTIFACT");
	default ParamAAN2 selectedArtifactsHomeworld()	{ return artifactsHomeworld; }
	ParamAAN2 fertileHomeworld			= new ParamAAN2("HOME_FERTILE");
	default ParamAAN2 selectedFertileHomeworld()	{  return fertileHomeworld; }
	ParamAAN2 richHomeworld				= new ParamAAN2("HOME_RICH");
	default ParamAAN2 selectedRichHomeworld()		{ return richHomeworld; }
	ParamAAN2 ultraRichHomeworld		= new ParamAAN2("HOME_ULTRA_RICH");
	default ParamAAN2 selectedUltraRichHomeworld()	{ return ultraRichHomeworld; }

	ParamFloat minDistArtifactPlanet	= new ParamFloat( MOD_UI, "DIST_ARTIFACT_PLANET",
			0.0f, 0.0f, null, 0.2f, 1f, 5f, "0.0##", "0.0");
	default float selectedMinDistArtifactPlanet() { return minDistArtifactPlanet.get(); }

	ParamBoolean battleScout		= new ParamBoolean( MOD_UI, "BATTLE_SCOUT", false);
	default boolean selectedBattleScout()		{ return battleScout.get(); }

	ParamBoolean randomTechStart	= new ParamBoolean( MOD_UI, "RANDOM_TECH_START", false);
	default boolean selectedRandomTechStart()	{ return randomTechStart.get(); }

	ParamInteger companionWorlds	= new ParamInteger( MOD_UI, "COMPANION_WORLDS" , 0, -4, 6, true);
	default int selectedCompanionWorlds() 		{ return Math.abs(companionWorlds.get()); }
	default int signedCompanionWorlds() 		{ return companionWorlds.get(); }

	ParamInteger empiresSpreadingFactor	= new ParamInteger( MOD_UI, "EMPIRES_SPREADING_FACTOR",
			100, 10, 1000, 1, 5, 20);
	default int		selectedEmpireSpreadingPct()	{ return empiresSpreadingFactor.get(); }
	default float	selectedEmpireSpreadingFactor()	{ return 0.01f * empiresSpreadingFactor.get(); }
	default boolean	isCustomEmpireSpreadingFactor()	{ return !empiresSpreadingFactor.isDefaultValue(); }
	default void	resetEmpireSpreadingFactor()	{ empiresSpreadingFactor.setFromDefault(); }
	default void	toggleEmpireSpreadingFactor(MouseWheelEvent e)	{ empiresSpreadingFactor.toggle(e); }
	default String	empireSpreadingFactorMapKey()	{ return  MOD_UI + "EMPIRES_SPREADING_FACTOR_MAP"; }

	ParamInteger minStarsPerEmpire	= new ParamInteger( MOD_UI, "MIN_STARS_PER_EMPIRE"
			, 3, 3, Rotp.maximumSystems-1, 1, 5, 20);
	default int selectedMinStarsPerEmpire()		{ return minStarsPerEmpire.get(); }

	ParamInteger prefStarsPerEmpire	= new ParamInteger( MOD_UI, "PREF_STARS_PER_EMPIRE"
			, 10, 3, Rotp.maximumSystems-1, 1, 10, 100);
	default int selectedPrefStarsPerEmpire()	{ return prefStarsPerEmpire.get(); }

	ParamInteger dynStarsPerEmpire	= new ParamInteger( MOD_UI, "DYN_STARS_PER_EMPIRE"
			, 10, 3, Rotp.maximumSystems-1, 1, 10, 100) {
		@Override public Integer defaultValue() {
			return prefStarsPerEmpire.get();
		}
	};
	default int selectedDynStarsPerEmpire()		{ return Math.abs(dynStarsPerEmpire.get()); }

	// Restart Always looks for setup options!
	ParamBoolean restartChangesAliensAI		= new ParamBoolean( MOD_UI, "RESTART_CHANGES_ALIENS_AI", false);
	default boolean selectedRestartChangesAliensAI()	{ return restartChangesAliensAI.get(); }
	ParamBoolean restartChangesPlayerAI		= new ParamBoolean( MOD_UI, "RESTART_CHANGES_PLAYER_AI", false);
	default boolean selectedRestartChangesPlayerAI()	{ return restartChangesPlayerAI.get(); }
	ParamBoolean restartAppliesSettings		= new ParamBoolean( MOD_UI, "RESTART_APPLY_SETTINGS",false);
	default boolean selectedRestartAppliesSettings()	{ return restartAppliesSettings.get(); }
	ParamList    restartChangesPlayerRace	= new ParamList( MOD_UI, "RESTART_PLAYER_RACE", "Swap") {
		{
			showFullGuide(true);
			put("Last", 	MOD_UI + "RESTART_PLAYER_RACE_LAST");
			put("Swap",		MOD_UI + "RESTART_PLAYER_RACE_SWAP");
			put("GuiSwap",	MOD_UI + "RESTART_PLAYER_RACE_GUI_SWAP");
			put("GuiLast",	MOD_UI + "RESTART_PLAYER_RACE_GUI_LAST");
		}
	};
	default String selectedRestartChangesPlayerRace()	{ return restartChangesPlayerRace.get(); }

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
	default List<String> forbiddenTechList(boolean isPlayer)	{
		List<String> list = new ArrayList<>();
		for (ParamTech  tech : techModList) {
			if (tech.isNever(isPlayer)) {
				list.add(tech.techId());
			}
		}
		return list;
	}

	ParamInteger randomAlienRacesMin		 = new ParamInteger(MOD_UI, "RACES_RAND_MIN", -50, -100, 100, 1, 5, 20);
	ParamInteger randomAlienRacesMax		 = new ParamInteger(MOD_UI, "RACES_RAND_MAX", 50, -100, 100, 1, 5, 20);
	ParamInteger randomAlienRacesTargetMax	 = new ParamInteger(MOD_UI, "RACES_RAND_TARGET_MAX", 75, null, null, 1, 10, 100);
	ParamInteger randomAlienRacesTargetMin	 = new ParamInteger(MOD_UI, "RACES_RAND_TARGET_MIN", 0, null, null, 1, 10, 100);
	ParamBoolean randomAlienRacesSmoothEdges = new ParamBoolean(MOD_UI, "RACES_RAND_EDGES", true);

	RandomAlienRaces randomAlienRaces		 = new RandomAlienRaces(MOD_UI, "RACES_ARE_RANDOM", RandomAlienRaces.TARGET);
	default String selectedRandomAlienRaces()			{ return randomAlienRaces.get(); }
	
	// ==================== GUI List Declarations ====================
	//
	LinkedList<IParam> modOptionsStaticA  = new LinkedList<>(
			Arrays.asList(
			artifactsHomeworld, fertileHomeworld, richHomeworld, ultraRichHomeworld,
			null,
			techIrradiated, techCloaking, techStargate, techHyperspace,
			null,
			techIndustry2, techThorium, techTransport, randomTechStart, 
			null,
			companionWorlds, battleScout
			));
	LinkedList<IParam> modOptionsStaticB  = new LinkedList<>(
			Arrays.asList(
			minStarsPerEmpire, prefStarsPerEmpire, empiresSpreadingFactor, minDistArtifactPlanet,
			null,
			randomAlienRacesTargetMax, randomAlienRacesTargetMin, randomAlienRaces,
			null,
			randomAlienRacesMax, randomAlienRacesMin, randomAlienRacesSmoothEdges,
			null,
			restartChangesPlayerAI, restartChangesAliensAI, restartAppliesSettings, restartChangesPlayerRace
			));
	LinkedList<IParam> preGameOptions	= new LinkedList<>();
	LinkedList<LinkedList<IParam>> preGameOptionsMap = preGameOptionsMap();

	static LinkedList<LinkedList<IParam>> preGameOptionsMap()	{
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("START_GALAXY_OPTIONS"),
				galaxyAge, starDensity, nebulae, empiresSpreadingFactor,
				minStarsPerEmpire, prefStarsPerEmpire, dynStarsPerEmpire,

				headerSpacer,
				new ParamTitle("START_PLANET_OPTIONS"),
				planetQuality, minDistArtifactPlanet
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("START_EMPIRE_OPTIONS"),
				artifactsHomeworld, fertileHomeworld, richHomeworld, ultraRichHomeworld,
				companionWorlds, battleScout, randomTechStart, randomizeAI
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("START_TECH_CONTROL"),
				techIrradiated, techCloaking, techStargate, techHyperspace,
				techIndustry2, techThorium, techTransport,

				headerSpacer,
				new ParamTitle("START_RANDOM_ALIENS"),
				randomAlienRacesTargetMax, randomAlienRacesTargetMin, randomAlienRaces,
				randomAlienRacesMax, randomAlienRacesMin, randomAlienRacesSmoothEdges
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RESTART_OPTIONS"),
				restartChangesPlayerRace, restartChangesPlayerAI,
				restartChangesAliensAI, restartAppliesSettings,

				headerSpacer,
				new ParamTitle("MENU_OPTIONS"),
				IMainOptions.useFusionFont, IMainOptions.compactOptionOnly
				)));
		for (LinkedList<IParam> list : map) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					preGameOptions.add(param);
			}
		}
		return map;
	};
}
