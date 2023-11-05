package rotp.model.game;

import java.util.LinkedList;

import rotp.ui.util.IParam;

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
	IGameOptions copyAllOptions();
	
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
		allModOptions.addAll(IPreGameOptions.modStaticAOptions());
		allModOptions.addAll(IPreGameOptions.modStaticBOptions());
		allModOptions.addAll(IInGameOptions.modDynamicAOptions());
		allModOptions.addAll(IInGameOptions.modDynamicBOptions());
		allModOptions.addAll(optionsGalaxy);
		allModOptions.addAll(optionsRace);
		allModOptions.addAll(optionsCustomRaceBase);
		allModOptions.addAll(IFlagOptions.autoFlagOptions());
		allModOptions.addAll(convenienceOptions); // Keep as variable
		allModOptions.addAll(governorOptions);
		allModOptions.addAll(mainOptionsUI); // Keep as variable
		allModOptions.addAll(IDebugOptions.debugOptions());
		allModOptions.addAll(IZoomOptions.zoomOptions());
		allModOptions.addAll(IIronmanOptions.ironmanOptions());
		return allModOptions;
	};

	// ==================== GUI List Declarations ====================
	//
	static LinkedList<IParam> allCfgOptions() {
		LinkedList<IParam> list = new LinkedList<>();
		for(IParam param:allOptions())
			if (param != null && param.isCfgFile())
				list.add(param);
		return list;
	}
	static LinkedList<IParam> allNotCfgOptions() {
		LinkedList<IParam> list = new LinkedList<>();
		for(IParam param:allOptions())
			if (param != null && !param.isCfgFile())
				list.add(param);
		return list;
	}
	static LinkedList<IParam> allOptions() {
		LinkedList<IParam> list = new LinkedList<>();
		allModOptions.addAll(IPreGameOptions.modStaticAOptions());
		allModOptions.addAll(IPreGameOptions.modStaticBOptions());
		allModOptions.addAll(IInGameOptions.modDynamicAOptions());
		allModOptions.addAll(IInGameOptions.modDynamicBOptions());
		allModOptions.addAll(optionsGalaxy);
		allModOptions.addAll(optionsRace);
		allModOptions.addAll(optionsCustomRaceBase);
		allModOptions.addAll(IFlagOptions.autoFlagOptions());
		allModOptions.addAll(convenienceOptions);
		allModOptions.addAll(governorOptions);
		allModOptions.addAll(mainOptionsUI);
		allModOptions.addAll(IDebugOptions.debugOptions());
		allModOptions.addAll(IRandomEvents.customRandomEventOptions());
		allModOptions.addAll(IZoomOptions.zoomOptions());
		allModOptions.addAll(IIronmanOptions.ironmanOptions());
		return list;
	}
    // All the Global parameters
	static LinkedList<IParam> globalOptions(boolean initialList) {
		LinkedList<IParam> globalOptions = new LinkedList<>();
		globalOptions.addAll(mainOptionsUI);
		globalOptions.remove(debugOptionsUI);
		globalOptions.remove(zoomOptionsUI);
		globalOptions.addAll(IDebugOptions.debugOptions());
		globalOptions.addAll(IZoomOptions.zoomOptions());
		globalOptions.add(bitmapGalaxyLastFolder);
		globalOptions.add(showNextCouncil);
		
		if (initialList) {
			globalOptions.addAll(convenienceOptions);
			globalOptions.add(governorByDefault);
			globalOptions.add(autoSpend);
			globalOptions.add(maxGrowthMode);
			globalOptions.add(auto_Apply);
		}
		return globalOptions;
	}
}
