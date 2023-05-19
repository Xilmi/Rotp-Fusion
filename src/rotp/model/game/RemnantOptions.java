package rotp.model.game;

import static rotp.model.game.BaseOptions.MOD_UI;
import static rotp.model.game.BaseOptions.headerSpacer;
import static rotp.model.game.DuplicateOptions.aiHostility;
import static rotp.model.game.DuplicateOptions.autoBombard_;
import static rotp.model.game.DuplicateOptions.autoColonize_;
import static rotp.model.game.DuplicateOptions.autoplay;
import static rotp.model.game.DuplicateOptions.colonizing;
import static rotp.model.game.DuplicateOptions.councilWin;
import static rotp.model.game.DuplicateOptions.fuelRange;
import static rotp.model.game.DuplicateOptions.galaxyAge;
import static rotp.model.game.DuplicateOptions.nebulae;
import static rotp.model.game.DuplicateOptions.planetQuality;
import static rotp.model.game.DuplicateOptions.randomEvents;
import static rotp.model.game.DuplicateOptions.randomizeAI;
import static rotp.model.game.DuplicateOptions.researchRate;
import static rotp.model.game.DuplicateOptions.starDensity;
import static rotp.model.game.DuplicateOptions.techTrading;
import static rotp.model.game.DuplicateOptions.terraforming;
import static rotp.model.game.DuplicateOptions.warpSpeed;
import static rotp.model.game.FactoryOptions.artifactsHomeworld;
import static rotp.model.game.FactoryOptions.battleScout;
import static rotp.model.game.FactoryOptions.companionWorlds;
import static rotp.model.game.FactoryOptions.dynStarsPerEmpire;
import static rotp.model.game.FactoryOptions.fertileHomeworld;
import static rotp.model.game.FactoryOptions.maximizeSpacing;
import static rotp.model.game.FactoryOptions.minDistArtifactPlanet;
import static rotp.model.game.FactoryOptions.minStarsPerEmpire;
import static rotp.model.game.FactoryOptions.prefStarsPerEmpire;
import static rotp.model.game.FactoryOptions.randomAlienRaces;
import static rotp.model.game.FactoryOptions.randomAlienRacesMax;
import static rotp.model.game.FactoryOptions.randomAlienRacesMin;
import static rotp.model.game.FactoryOptions.randomAlienRacesSmoothEdges;
import static rotp.model.game.FactoryOptions.randomAlienRacesTargetMax;
import static rotp.model.game.FactoryOptions.randomAlienRacesTargetMin;
import static rotp.model.game.FactoryOptions.randomTechStart;
import static rotp.model.game.FactoryOptions.restartAppliesSettings;
import static rotp.model.game.FactoryOptions.restartChangesAliensAI;
import static rotp.model.game.FactoryOptions.restartChangesPlayerAI;
import static rotp.model.game.FactoryOptions.restartChangesPlayerRace;
import static rotp.model.game.FactoryOptions.richHomeworld;
import static rotp.model.game.FactoryOptions.spacingLimit;
import static rotp.model.game.FactoryOptions.techCloaking;
import static rotp.model.game.FactoryOptions.techHyperspace;
import static rotp.model.game.FactoryOptions.techIndustry2;
import static rotp.model.game.FactoryOptions.techIrradiated;
import static rotp.model.game.FactoryOptions.techStargate;
import static rotp.model.game.FactoryOptions.techThorium;
import static rotp.model.game.FactoryOptions.techTransport;
import static rotp.model.game.FactoryOptions.ultraRichHomeworld;
import static rotp.model.game.FlagOptions.autoFlagOptionsUI;
import static rotp.model.game.FlagOptions.flagColorCount;
import static rotp.model.game.GamePlayOptions.amoebaDelayTurn;
import static rotp.model.game.GamePlayOptions.amoebaMaxSystems;
import static rotp.model.game.GamePlayOptions.amoebaReturnTurn;
import static rotp.model.game.GamePlayOptions.autoTerraformEnding;
import static rotp.model.game.GamePlayOptions.bombingTarget;
import static rotp.model.game.GamePlayOptions.challengeMode;
import static rotp.model.game.GamePlayOptions.counciRequiredPct;
import static rotp.model.game.GamePlayOptions.crystalDelayTurn;
import static rotp.model.game.GamePlayOptions.crystalMaxSystems;
import static rotp.model.game.GamePlayOptions.crystalReturnTurn;
import static rotp.model.game.GamePlayOptions.customDifficulty;
import static rotp.model.game.GamePlayOptions.dynamicDifficulty;
import static rotp.model.game.GamePlayOptions.eventsStartTurn;
import static rotp.model.game.GamePlayOptions.missileSizeModifier;
import static rotp.model.game.GamePlayOptions.piratesDelayTurn;
import static rotp.model.game.GamePlayOptions.piratesMaxSystems;
import static rotp.model.game.GamePlayOptions.piratesReturnTurn;
import static rotp.model.game.GamePlayOptions.retreatRestrictionTurns;
import static rotp.model.game.GamePlayOptions.retreatRestrictions;
import static rotp.model.game.GamePlayOptions.scrapRefundFactor;
import static rotp.model.game.GamePlayOptions.scrapRefundOption;
import static rotp.model.game.GamePlayOptions.showAllAI;
import static rotp.model.game.GamePlayOptions.targetBombard;
import static rotp.model.game.ModOptions.bitmapGalaxyLastFolder;
import static rotp.model.game.ModOptions.difficultySelection;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.model.galaxy.StarSystem;
import rotp.ui.main.GalaxyMapPanel;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamOptions;
import rotp.ui.util.ParamTitle;
import rotp.util.FontManager;

public interface RemnantOptions {
	ParamFloat   showFleetFactor	= new ParamFloat(
			MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_STARGATE_SCALE		 = (int) (40 * newValue);
			GalaxyMapPanel.MAX_FLEET_UNARMED_SCALE	 = (int) (40 * newValue);
			GalaxyMapPanel.MAX_FLEET_TRANSPORT_SCALE = (int) (60 *newValue);
			GalaxyMapPanel.MAX_FLEET_SMALL_SCALE	 = (int) (60 * newValue);
			GalaxyMapPanel.MAX_FLEET_LARGE_SCALE	 = (int) (80 * newValue);
			GalaxyMapPanel.MAX_FLEET_HUGE_SCALE		 = (int) (100 * newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   showFlagFactor		= new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_FLAG_SCALE = (int) (80 * newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   showPathFactor		= new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_RALLY_SCALE = (int) (100 * newValue);
			return super.set(newValue);
		}
	};
	ParamInteger showNameMinFont	= new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT", 8, 2, 24, 1, 2, 5) {
		@Override public Integer set(Integer newValue) {
			StarSystem.minFont	= newValue;
			StarSystem.minFont2	= Math.round(newValue/showInfoFontRatio.get());
			return super.set(newValue);
		}
	};
	ParamFloat   showInfoFontRatio	= new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			StarSystem.minFont2	= Math.round(StarSystem.minFont/newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   mapFontFactor		= new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			StarSystem.fontPct = Math.round(newValue * 100);
			return super.set(newValue);
		}
	};
	ParamOptions menuStartup		= new ParamOptions(MOD_UI, "MENU_STARTUP", ParamOptions.VANILLA);
	ParamOptions menuAfterGame		= new ParamOptions(MOD_UI, "MENU_AFTER_GAME", ParamOptions.VANILLA);
	ParamOptions menuLoadGame		= new ParamOptions(MOD_UI, "MENU_LOADING_GAME", ParamOptions.GAME);
	ParamBoolean compactOptionOnly	= new ParamBoolean(MOD_UI, "COMPACT_OPTION_ONLY", false);
	ParamBoolean showGridCircular	= new ParamBoolean(MOD_UI, "SHOW_GRID_CIRCULAR", false);
	ParamBoolean useFusionFont		= new ParamBoolean(MOD_UI, "USE_FUSION_FONT", false)
	{
		@Override public Boolean set(Boolean newValue) {
			FontManager.INSTANCE.resetGalaxyFont();
			return super.set(newValue);
		}
	};

	ParamBoolean showNextCouncil		= new ParamBoolean(MOD_UI, "SHOW_NEXT_COUNCIL", false); // Show years left until next council
	ParamInteger galaxyPreviewColorStarsSize = new ParamInteger(MOD_UI, "GALAXY_PREVIEW_COLOR_SIZE" , 5, 0, 20, 1, 2, 5);
	ParamInteger minListSizePopUp		= new ParamInteger(MOD_UI, "MIN_LIST_SIZE_POP_UP" , 4, 0, 10, true)
			.specialZero(MOD_UI + "MIN_LIST_SIZE_POP_UP_NEVER");
	ParamInteger showLimitedWarnings	= new ParamInteger(MOD_UI, "SHOW_LIMITED_WARNINGS" , -1, -1, 49, 1, 2, 5)
			.loop(true)
			.specialNegative(MOD_UI + "SHOW_LIMITED_WARNINGS_ALL");
	ParamBoolean showAlliancesGNN		= new ParamBoolean(MOD_UI, "SHOW_ALLIANCES_GNN", true);
	ParamBoolean techExchangeAutoRefuse = new ParamBoolean(MOD_UI, "TECH_EXCHANGE_AUTO_NO", false);

	// ==================== GUI List Declarations ====================
	LinkedList<InterfaceParam> modGlobalOptionsUI = new LinkedList<>(
			Arrays.asList(
			menuStartup, menuAfterGame, menuLoadGame, minListSizePopUp, showAlliancesGNN,
			null,
			showGridCircular, galaxyPreviewColorStarsSize, showLimitedWarnings, techExchangeAutoRefuse,
			null,
			showFleetFactor, showFlagFactor, showPathFactor, useFusionFont,
			null,
			showNameMinFont, showInfoFontRatio, mapFontFactor, showNextCouncil, compactOptionOnly
			));

    // All the Global parameters
	LinkedList<InterfaceParam> globalOptions = globalOptions();
	static LinkedList<InterfaceParam> globalOptions() {
		LinkedList<InterfaceParam> globalOptions = new LinkedList<>();
		globalOptions.addAll(modGlobalOptionsUI);
		globalOptions.add(bitmapGalaxyLastFolder);
		return globalOptions;
	}

	LinkedList<InterfaceParam> mergedStaticOptions	= new LinkedList<>();
	LinkedList<LinkedList<InterfaceParam>> mergedStaticOptionsMap = mergedStaticOptionsMap();
	static LinkedList<LinkedList<InterfaceParam>> mergedStaticOptionsMap()	{
		LinkedList<LinkedList<InterfaceParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("START_GALAXY_OPTIONS"),
				galaxyAge, starDensity, nebulae, maximizeSpacing,
				spacingLimit, minStarsPerEmpire, prefStarsPerEmpire, dynStarsPerEmpire,

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
				useFusionFont, compactOptionOnly
				)));
		for (LinkedList<InterfaceParam> list : map) {
			for (InterfaceParam param : list) {
				if (param != null && !param.isTitle())
					mergedStaticOptions.add(param);
			}
		}
		return map;
	};
	LinkedList<InterfaceParam> mergedDynamicOptions	= new LinkedList<>();
	LinkedList<LinkedList<InterfaceParam>> mergedDynamicOptionsMap = mergedDynamicOptionsMap(); 
	static LinkedList<LinkedList<InterfaceParam>> mergedDynamicOptionsMap()	{
		LinkedList<LinkedList<InterfaceParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_DIFFICULTY"),
				difficultySelection, customDifficulty,
				dynamicDifficulty, challengeMode,

				headerSpacer,
				new ParamTitle("GAME_VARIOUS"),
				terraforming, colonizing, researchRate,
				warpSpeed, fuelRange, 

				headerSpacer,
				new ParamTitle("GAME_OTHER"),
				showAlliancesGNN, showLimitedWarnings,
				techExchangeAutoRefuse, autoTerraformEnding, autoplay
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct,
				techTrading, aiHostility,

				headerSpacer,
				new ParamTitle("GAME_COMBAT"),
				retreatRestrictions, retreatRestrictionTurns, missileSizeModifier,
				targetBombard, bombingTarget, autoBombard_, autoColonize_,
				scrapRefundFactor, scrapRefundOption
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_OPT"),
				randomEvents, eventsStartTurn,
				piratesDelayTurn, piratesReturnTurn, piratesMaxSystems,
				amoebaDelayTurn, amoebaReturnTurn, amoebaMaxSystems,
				crystalDelayTurn, crystalReturnTurn, crystalMaxSystems,

				headerSpacer,
				new ParamTitle("PLANETS_FLAG_OPTIONS"),
				flagColorCount, autoFlagOptionsUI,

				headerSpacer,
				new ParamTitle("GOVERNOR_SETUP_MENU"),
				GovernorOptions.governorOptionsUI
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_FACTORS"),
				showFleetFactor, showFlagFactor, showPathFactor,
				showNameMinFont, showInfoFontRatio, mapFontFactor,
	
				headerSpacer,
				new ParamTitle("MENU_OPTIONS"),
				menuStartup, menuAfterGame, menuLoadGame,
				minListSizePopUp, showGridCircular, galaxyPreviewColorStarsSize,
				showAllAI, compactOptionOnly
				)));
		for (LinkedList<InterfaceParam> list : map) {
			for (InterfaceParam param : list) {
				if (param != null && !param.isTitle())
					mergedDynamicOptions.add(param);
			}
		}
		return map;
	};	
}
