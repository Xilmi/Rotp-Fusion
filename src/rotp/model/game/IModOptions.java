package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamTitle;

public interface IModOptions extends IFlagOptions, IPreGameOptions, IInGameOptions,
							IRaceOptions, IGovOptions, IGalaxyOptions, IMainOptions {

	default void updateFromFile(String fileName)	{ updateFromFile(fileName, allModOptions); }
	default void resetToDefault()					{ resetToDefault(allModOptions); }

	default LinkedList<IParam> governorOptions() {
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

	void prepareToSave(boolean secure);
	void UpdateOptionsTools();
	MOO1GameOptions copyAllOptions();
	
	void loadStartupOptions();
	/**
	 * Load file and update with specified options list then save back to file
	 * @param fileName
	 * @param paramList
	 */
	void saveOptionsToFile (String fileName, LinkedList<IParam> paramList);
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
	void updateFromFile (String fileName, LinkedList<IParam> paramList);
	/**
	 * update the listed parameters From their default values
	 * (Options and options' tools)
	 * @param paramList
	 */
	void resetToDefault (LinkedList<IParam> paramList);

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
	default LinkedList<IParam> allModOptions()	{ return allModOptions; }
	LinkedList<IParam> allModOptions = getAllModOptions();
	static LinkedList<IParam> getAllModOptions() {
		LinkedList<IParam> allModOptions = new LinkedList<>();
		allModOptions.addAll(modOptionsStaticA);
		allModOptions.addAll(modOptionsStaticB);
		allModOptions.addAll(modOptionsDynamicA);
		allModOptions.addAll(modOptionsDynamicB);
		allModOptions.addAll(optionsGalaxy);
		allModOptions.addAll(optionsRace);
		allModOptions.addAll(optionsCustomRaceBase);
		allModOptions.addAll(autoFlagOptions);
		allModOptions.addAll(convenienceOptions);
		allModOptions.addAll(governorOptions);
		allModOptions.addAll(mainOptionsUI);
		allModOptions.addAll(debugOptions);
		return allModOptions;
	};

	// ==================== GUI List Declarations ====================
	//
    // All the Global parameters
	static LinkedList<IParam> globalOptions(boolean initialList) {
		LinkedList<IParam> globalOptions = new LinkedList<>();
		globalOptions.addAll(mainOptionsUI);
		globalOptions.remove(debugOptionsUI);
		globalOptions.addAll(debugOptions);
		globalOptions.add(bitmapGalaxyLastFolder);
		
		if (initialList) {
			globalOptions.addAll(convenienceOptions);
			globalOptions.add(governorByDefault);
			globalOptions.add(autoSpend);
			globalOptions.add(maxGrowthMode);
			globalOptions.add(auto_Apply);
		}
		return globalOptions;
	}
	
	LinkedList<IParam> inGameOptions	= new LinkedList<>();
	LinkedList<LinkedList<IParam>> inGameOptionsMap = inGameOptionsMap(); 

	static LinkedList<LinkedList<IParam>> inGameOptionsMap()	{
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
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
				techExchangeAutoRefuse, autoTerraformEnding, trackUFOsAcrossTurns,
				autoplay
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_RELATIONS"),
				councilWin, counciRequiredPct, aiHostility,
				techTrading, allowTechStealing, maxSecurityPct,

				headerSpacer,
				new ParamTitle("GAME_COMBAT"),
				retreatRestrictions, retreatRestrictionTurns, missileBaseModifier, missileShipModifier,
				targetBombard, bombingTarget, autoBombard_, autoColonize_,
				scrapRefundFactor, scrapRefundOption
				
//				headerSpacer,
//				new ParamTitle("BETA_TEST"),
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_OPT"),
				randomEvents, fixedEventsMode, eventsStartTurn,
				piratesDelayTurn, piratesReturnTurn, piratesMaxSystems,
				amoebaDelayTurn, amoebaReturnTurn, amoebaMaxSystems,
				crystalDelayTurn, crystalReturnTurn, crystalMaxSystems,
				customRandomEventUI,

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
				divertExcessToResearch, defaultMaxBases, displayYear,
				showNextCouncil, systemNameDisplay, shipDisplay, flightPathDisplay,
				showGridCircular, showShipRanges, galaxyPreviewColorStarsSize,
				showAllAI, raceStatusLog, compactOptionOnly
				)));
		for (LinkedList<IParam> list : map) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					inGameOptions.add(param);
			}
		}
		return map;
	};
}
