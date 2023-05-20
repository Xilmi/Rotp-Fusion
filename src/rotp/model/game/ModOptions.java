package rotp.model.game;

import static rotp.model.game.IGameOptions.DIFFICULTY_NORMAL;
import static rotp.model.game.IGameOptions.SHAPE_RECTANGLE;
import static rotp.model.game.IGameOptions.SIZE_SMALL;
import static rotp.model.game.IGameOptions.baseRaceOptions;
import static rotp.model.game.MOO1GameOptions.getGalaxyShapeOptions;
import static rotp.model.game.MOO1GameOptions.getGalaxySizeOptions;
import static rotp.model.game.MOO1GameOptions.getGameDifficultyOptions;
import static rotp.ui.util.InterfaceParam.langLabel;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.Rotp;
import rotp.ui.RotPUI;
import rotp.ui.util.GlobalCROptions;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamCR;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamString;
import rotp.ui.util.ParamTitle;
import rotp.ui.util.PlayerShipSet;
import rotp.ui.util.SpecificCROption;

public interface ModOptions extends FlagOptions, FactoryOptions, GamePlayOptions,
									DuplicateOptions, RemnantOptions {

	// ==================== Galaxy Menu addition ====================
	//
	ParamInteger galaxyRandSource		= new ParamInteger(MOD_UI, "GALAXY_RAND_SOURCE",
			0, 0, Integer.MAX_VALUE, 1, 100, 10000).loop(true);
	default int selectedGalaxyRandSource()		{ return galaxyRandSource.selected(dynOpts()); }
	ParamBoolean showNewRaces 			= new ParamBoolean(MOD_UI, "SHOW_NEW_RACES", false);
	default boolean selectedShowNewRaces()		{ return showNewRaces.selected(dynOpts()); }

	GlobalCROptions globalCROptions 	= new GlobalCROptions (BASE_UI, "OPP_CR_OPTIONS",
			SpecificCROption.BASE_RACE.value);
	default GlobalCROptions globalCROptions()	{ return globalCROptions; }
	default String selecteduseGlobalCROptions()	{ return globalCROptions.selected(dynOpts()); }

	ParamBoolean useSelectableAbilities	= new ParamBoolean(BASE_UI, "SELECT_CR_OPTIONS", false);
	default boolean selecteduseSelectableAbilities()	{ return useSelectableAbilities.selected(dynOpts()); }

	ParamString  shapeOption3   		= new ParamString(BASE_UI, "SHAPE_OPTION_3", "");
	default ParamString shapeOption3()			{ return shapeOption3; }
	default String selectedGalaxyShapeOption3()	{ return shapeOption3.selected(dynOpts()); }

	ParamList    shapeOption2   		= new ParamList( // Duplicate Do not add the list
			BASE_UI, "SHAPE_OPTION_2")	{
		{ showFullGuide(true); }
		@Override public String	get()	{
			return RotPUI.mergedGuiOptions().selectedGalaxyShapeOption2();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGalaxyShapeOption2(newValue);
		}
		@Override public String	headerHelp(boolean sep) {
			return headerHelp(shapeSelection.get() + "_O2", sep); }
		@Override public String getLangLabel(int id) {
			String label = super.getLangLabel(id);
			if (label != null && label.startsWith("SETUP_GALAXY_MAP_OPTION_")) {
				if (shapeOption1.get().endsWith("0"))
					label += "0";
				else
					label += "1";
			}
			return label;
		}
	};
	ParamList    shapeOption1   		= new ParamList( // Duplicate Do not add the list
			BASE_UI, "SHAPE_OPTION_1")	{
		{ showFullGuide(true); }
		@Override public String	get()	{
			return RotPUI.mergedGuiOptions().selectedGalaxyShapeOption1();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGalaxyShapeOption1(newValue);
		}
		@Override public String	headerHelp(boolean sep) {
			return headerHelp(shapeSelection.get() + "_O1", sep); }
	};
	ParamList    shapeSelection			= new ParamList( // Duplicate Do not add the list
			BASE_UI, "GALAXY_SHAPE", getGalaxyShapeOptions(),  SHAPE_RECTANGLE) {
		@Override public String     getFromOption() {
			return RotPUI.mergedGuiOptions().selectedGalaxyShape();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGalaxyShape(newValue);
		}
	};
	ParamList    sizeSelection 			= new ParamList( // Duplicate Do not add the list
			BASE_UI, "GALAXY_SIZE", getGalaxySizeOptions(), SIZE_SMALL) {
		{ showFullGuide(false); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedGalaxySize();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGalaxySize(newValue);
		}
		@Override public String name(int id) {
			String diffLbl = super.name(id);
			String label   = getLangLabel(id);
			int size = RotPUI.mergedGuiOptions().numberStarSystems(label);
			if (label.equals("SETUP_GALAXY_SIZE_DYNAMIC"))
				diffLbl += " (Variable; now = " + size + ")";
			else
				diffLbl += " (" + size + ")";
			return diffLbl;
		}
		@Override public String realHelp(int id) {
			String label   = getLangLabel(id);
			if (label.equals("SETUP_GALAXY_SIZE_DYNAMIC"))
				return super.realHelp(id);
			if (label.equals("SETUP_GALAXY_SIZE_MAXIMUM"))
				return super.realHelp(id);
			int size = RotPUI.mergedGuiOptions().numberStarSystems(label);
			if (size < 101)
				return langLabel("SETUP_GALAXY_SIZE_MOO1_DESC");
			if (size < 1001)
				return langLabel("SETUP_GALAXY_SIZE_UP1000_DESC");
			return langLabel("SETUP_GALAXY_SIZE_OVER1000_DESC");
		}
	};
	ParamList    difficultySelection	= new ParamList( // Duplicate Do not add the list
			BASE_UI, "GAME_DIFFICULTY", getGameDifficultyOptions(), DIFFICULTY_NORMAL) {
		{ showFullGuide(false); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedGameDifficulty();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGameDifficulty(newValue);
		}
		@Override public String name(int id) {
			String diffLbl = super.name(id);
			String label   = getLangLabel(id);
			if (label.equals("SETUP_DIFFICULTY_CUSTOM"))
				diffLbl += " (" + Integer.toString(RotPUI.mergedGuiOptions().selectedCustomDifficulty()) + "%)";
			else {
				float modifier = RotPUI.mergedGuiOptions().aiProductionModifier(label);
				diffLbl += " (" + Integer.toString(Math.round(100 * modifier)) + "%)";
			}
			return diffLbl;
		}
	};
	ParamInteger aliensNumber 			= new ParamInteger( // Duplicate Do not add the list
			BASE_UI, "ALIENS_NUMBER", 1, 0, 49, 1, 5, 20) {
		{ isDuplicate(true); }
		@Override public Integer getFromOption() {
			maxValue(RotPUI.mergedGuiOptions().maximumOpponentsOptions());
			return RotPUI.mergedGuiOptions().selectedNumberOpponents();
		}
		@Override public void setOption(Integer newValue) {
			RotPUI.mergedGuiOptions().selectedOpponentRace(newValue, null);
			RotPUI.mergedGuiOptions().selectedNumberOpponents(newValue);
		}
		@Override public Integer defaultValue() {
			return RotPUI.mergedGuiOptions().defaultOpponentsOptions();
		}
	};
	ParamString bitmapGalaxyLastFolder = new ParamString(BASE_UI, "BITMAP_LAST_FOLDER", Rotp.jarPath());

	static LinkedList<InterfaceParam> optionsGalaxy = new LinkedList<>(
			Arrays.asList(
					showNewRaces, globalCROptions, useSelectableAbilities, shapeOption3,
					galaxyRandSource,
					dynStarsPerEmpire // This one is a duplicate, but it helps readability
					));
	// ==================== Race Menu addition ====================
	//
	PlayerShipSet playerShipSet		= new PlayerShipSet(
			MOD_UI, "PLAYER_SHIP_SET");
	default int selectedPlayerShipSetId()	{
		playerShipSet.setFromOptions(dynOpts());
		return playerShipSet.realShipSetId(); 
	}

	ParamBoolean  playerIsCustom	= new ParamBoolean( BASE_UI, "BUTTON_CUSTOM_PLAYER_RACE", false);
	default boolean selectedPlayerIsCustom()	{ return playerIsCustom.selected(dynOpts()); }

	ParamCR       playerCustomRace	= new ParamCR(
			MOD_UI, baseRaceOptions().getFirst());
	// Custom Race Menu
	static LinkedList<InterfaceParam> optionsCustomRaceBase = new LinkedList<>(
			Arrays.asList(
					playerIsCustom, playerCustomRace
					));
	default LinkedList<InterfaceParam> optionsCustomRace() {
		LinkedList<InterfaceParam> list = new LinkedList<>();
		list.addAll(optionsCustomRaceBase);
		return list;
	}
	static LinkedList<InterfaceParam> optionsRace = new LinkedList<>(
			Arrays.asList(
					playerShipSet, playerIsCustom, playerCustomRace
					));

	LinkedList<InterfaceParam> allModOptions = allModOptions();
	
	static LinkedList<InterfaceParam> allModOptions() {
		LinkedList<InterfaceParam> allModOptions = new LinkedList<>();
		allModOptions.addAll(modOptionsStaticA);
		allModOptions.addAll(modOptionsStaticB);
		allModOptions.addAll(modOptionsDynamicA);
		allModOptions.addAll(modOptionsDynamicB);
		allModOptions.addAll(optionsGalaxy);
		allModOptions.addAll(optionsRace);
		allModOptions.addAll(optionsCustomRaceBase);
		allModOptions.addAll(autoFlagOptions);
		allModOptions.addAll(GovernorOptions.governorOptions);
		return allModOptions;
	};

	// ==================== GUI List Declarations ====================
	//
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
//				menuStartup, menuAfterGame, menuLoadGame, // TODO BR: REMOVE
				menuStartup,
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
