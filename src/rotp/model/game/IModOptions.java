package rotp.model.game;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamSubUI;

public interface IModOptions extends IFlagOptions, IPreGameOptions, IInGameOptions,
							IRaceOptions, IGovOptions, IGalaxyOptions, IMainOptions {

	default SafeListParam governorOptions() { return GovernorOptions.governorOptions; }
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
	void saveOptionsToFile (String fileName, SafeListParam paramList);
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
//	void updateFromFile (String fileName, SafeListParam paramList, boolean includeCfg);
	void updateFromFile (String fileName, SafeListParam paramList);
	/**
	 * update the listed parameters From their default values
	 * (Options and options' tools)
	 * @param paramList
	 */
	void resetPanelSettingsToDefault (SafeListParam paramList, boolean excludeCfg, boolean excludeSubMenu);

	void resetAllNonCfgSettingsToDefault();
	void updateAllNonCfgFromFile(String fileName);
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
	default SafeListParam allModOptions()	{ return allModOptions; }
	SafeListParam allModOptions = getAllModOptions();
	static SafeListParam getAllModOptions() {
		// Start with a set to filter duplicates
		LinkedHashSet<IParam> allModOptions = new LinkedHashSet<>();
		allModOptions.addAll(IPreGameOptions.preGameOptionsMap().getList());
		allModOptions.addAll(ISystemsOptions.systemsOptionsMap().getList());
		allModOptions.addAll(IInGameOptions.inGameOptionsMap().getList());
		allModOptions.addAll(IGalaxyOptions.getOptionsGalaxy());
		allModOptions.addAll(optionsRace);
		allModOptions.addAll(optionsCustomRaceBase);
		allModOptions.addAll(IFlagOptions.autoFlagOptionsMap().getList());
		allModOptions.addAll(governorOptions);
		allModOptions.addAll(IMainOptions.vanillaSettingsUI());
//		allModOptions.addAll(IMainOptions.mainOptionsUI());
		allModOptions.addAll(IMainOptions.commonOptionsMap().getList());
		allModOptions.add(IMainOptions.realNebulaSize);
		allModOptions.add(IMainOptions.realNebulaShape);
		allModOptions.add(IMainOptions.realNebulaeOpacity);
		allModOptions.add(IMainOptions.showGuide);
		allModOptions.add(IMainOptions.disableAutoHelp);
		allModOptions.add(IMainOptions.defaultSettings);
		allModOptions.addAll(IDebugOptions.debugOptionsMap().getList());
		allModOptions.addAll(ICombatOptions.combatOptionsMap().getList());
		allModOptions.addAll(IIronmanOptions.ironmanOptionsMap().getList());
		allModOptions.addAll(IMainOptions.specieNameOptionsMap().getList());
		allModOptions.remove(null);
		// Then create the final list (LinkedHashSet don't offer the .get(index) method)
		SafeListParam options = new SafeListParam();
		options.addAll(allModOptions);
		return options;
	};

	// ==================== GUI List Declarations ====================
	//
	static SafeListParam allCfgOptions() {
		SafeListParam list = new SafeListParam();
		for(IParam param:getAllModOptions())
			if (param.isCfgFile())
				list.add(param);
		return list;
	}
	static SafeListParam allDuplicateOptions() {
		SafeListParam list = new SafeListParam();
		for(IParam param:getAllModOptions())
			if (param.isCfgFile() && param.isDuplicate())
				list.add(param);
		return list;
	}
	static SafeListParam allNotCfgOptions() {
		SafeListParam list = new SafeListParam();
		for(IParam param:getAllModOptions())
			if (!param.isCfgFile())
				list.add(param);
		return list;
	}
//	static SafeListParam allOptions() {
//		// Start with a set to filter duplicates
//		LinkedHashSet<IParam> allOptions = new LinkedHashSet<>();
//		allOptions.addAll(IPreGameOptions.modStaticAOptions());
//		allOptions.addAll(IPreGameOptions.modStaticBOptions());
//		allOptions.addAll(IInGameOptions.modDynamicAOptions());
//		allOptions.addAll(IInGameOptions.modDynamicBOptions());
//		allOptions.addAll(optionsGalaxy);
//		allOptions.addAll(optionsRace);
//		allOptions.addAll(optionsCustomRaceBase);
//		allOptions.addAll(IFlagOptions.autoFlagOptions());
//		allOptions.addAll(convenienceOptions);
//		allOptions.addAll(governorOptions);
//		allOptions.addAll(IMainOptions.mainOptionsUI());
//		allOptions.addAll(IDebugOptions.debugOptions());
//		allOptions.addAll(IRandomEvents.customRandomEventOptions());
//		allOptions.addAll(ICombatOptions.combatOptions());
//		allOptions.addAll(IIronmanOptions.ironmanOptions());
//		allOptions.remove(null);
//		// Then create the final list (LinkedHashSet don't offer the .get(index) method)
//		SafeListParam options = new SafeListParam();
//		options.addAll(allOptions);
//		return options;
//	}
//    // All the Global parameters
//	static SafeListParam globalOptions(boolean initialList) {
//		SafeListParam globalOptions = new SafeListParam();
//		globalOptions.addAll(IMainOptions.mainOptionsUI());
//		globalOptions.remove(debugOptionsUI);
//		globalOptions.remove(combatOptionsUI);
//		globalOptions.addAll(IDebugOptions.debugOptions());
//		globalOptions.addAll(ICombatOptions.combatOptions());
//		globalOptions.addAll(IMainOptions.commonOptions());
//		globalOptions.add(bitmapGalaxyLastFolder);
//		globalOptions.add(showNextCouncil);
//		globalOptions.add(realNebulaeOpacity);
//		globalOptions.add(realNebulaShape);
//		globalOptions.add(realNebulaeSize);
//		
//		if (initialList) {
//			globalOptions.addAll(convenienceOptions);
//			globalOptions.add(governorByDefault);
//			globalOptions.add(autoSpend);
//			globalOptions.add(maxGrowthMode);
//			globalOptions.add(auto_Apply);
//		}
//		return globalOptions;
//	}
	// ==================== GUI Sub List Declarations ====================
	//
	static List<ParamSubUI> subPanelList() {
		List<ParamSubUI> list = new ArrayList<>();
		list.add(debugOptionsUI);
		list.add(customRandomEventUI);
		list.add(governorOptionsUI);
		list.add(combatOptionsUI);
		list.add(ironmanOptionsUI);
		list.add(autoFlagOptionsUI);
		return list;
	}
}
