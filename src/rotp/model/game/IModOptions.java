package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamTitle;

public interface IModOptions extends IFlagOptions, IPreGameOptions, IInGameOptions,
							IRaceOptions, IGovOptions, IGalaxyOptions {

	default void updateOptionsAndSaveToFileName(String fileName) {
		saveOptionsToFile(fileName, allModOptions());
	}
	default void updateFromFile(String fileName)	{ updateFromFile(fileName, allModOptions()); }
	default void setBaseAndModSettingsToDefault()	{ updateFromDefault(allModOptions()); }

	default LinkedList<InterfaceParam> governorOptions() {
		return rotp.model.game.GovernorOptions.governorOptions;
	}
	int id();
	void id(int id);
	DynOptions dynOpts();
	IGameOptions opts();
	
	default void	setAsGame()				{ id(GAME_ID); }
	default void	setAsSetup()			{ id(SETUP_ID); }
	default void	setAsUnknown()			{ id(UNKNOWN_ID); }
	default boolean	isGameOption()			{ return id() == GAME_ID; }
	default boolean	isSetupOption()			{ return id() == SETUP_ID; }
	default boolean	isUnknownOption()		{ return !isGameOption() && !isSetupOption(); }
	/**
	 * New options have been set:
	 * Switch between Game and Setup, reinitialize tools
	 */
	default void	updateGuiOptionsId()	{ RotPUI.currentOptions(id()); }
	
	void loadStartupOptions();
	/**
	 * Load file and update with specified options list then save back to file
	 * @param fileName
	 * @param paramList
	 */
	void saveOptionsToFile (String fileName, LinkedList<InterfaceParam> paramList);
	/**
	 * Save all options to file
	 * @param fileName
	 */
	void saveOptionsToFile (String fileName);
	/**
	 * Load the file and update the listed parameters
	 * (Options and options' tools)
	 * @param fileName
	 * @param paramList
	 */
	void updateFromFile (String fileName, LinkedList<InterfaceParam> paramList);
	/**
	 * update the listed parameters From their default values
	 * (Options and options' tools)
	 * @param paramList
	 */
	void updateFromDefault (LinkedList<InterfaceParam> paramList);

	void copyAliensAISettings(IGameOptions dest);
	
	// Tools For Debug
	default void	showOptionName()		{
		System.out.println("Option name = " + optionName());
	}
	default void	showOptionName(String header)	{
		System.out.println(header + " Option name = " + optionName());
	}
	default String	optionName()		{
		switch (id()) {
			case GAME_ID:
				return "Game Options";
			case SETUP_ID:
				return "Setup Options";
			default:
				return "Unknown Options";
		}
	}

	// ==================== All Parameters ====================
	//
	default LinkedList<InterfaceParam> allModOptions()	{ return allModOptions; }
	LinkedList<InterfaceParam> allModOptions = getAllModOptions();
	static LinkedList<InterfaceParam> getAllModOptions() {
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
	
	LinkedList<InterfaceParam> inGameOptions	= new LinkedList<>();
	LinkedList<LinkedList<InterfaceParam>> inGameOptionsMap = inGameOptionsMap(); 

	static LinkedList<LinkedList<InterfaceParam>> inGameOptionsMap()	{
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
				menuStartup,
				minListSizePopUp, showGridCircular, galaxyPreviewColorStarsSize,
				showAllAI, compactOptionOnly
				)));
		for (LinkedList<InterfaceParam> list : map) {
			for (InterfaceParam param : list) {
				if (param != null && !param.isTitle())
					inGameOptions.add(param);
			}
		}
		return map;
	};
}
