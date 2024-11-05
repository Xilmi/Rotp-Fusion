package rotp.model.game;

import java.util.LinkedHashSet;
import java.util.List;

import rotp.ui.options.AllSubUI;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamSubUI;

public interface IModOptions extends IFlagOptions, IPreGameOptions, IInGameOptions,
							IRaceOptions, IGovOptions, IGalaxyOptions, IMainOptions {

	// ==================== All Parameters ====================
	//
	default SafeListParam allModOptions()	{ return allModOptions; }
	SafeListParam allModOptions = getAllModOptions();
	static SafeListParam getAllModOptions() {
		// Start with a set to filter duplicates
		LinkedHashSet<IParam> allModOptions = new LinkedHashSet<>();
//		allModOptions.addAll(IPreGameOptions.preGameOptionsMap().getList());
//		allModOptions.addAll(ISystemsOptions.systemsOptionsMap().getList());
//		allModOptions.addAll(IInGameOptions.inGameOptionsMap().getList());
//		allModOptions.addAll(IGalaxyOptions.getOptionsGalaxy());
//		allModOptions.addAll(optionsRace);
//		allModOptions.addAll(optionsCustomRaceBase);
//		allModOptions.addAll(IFlagOptions.autoFlagOptionsMap().getList());
//		allModOptions.addAll(governorOptions);
//		allModOptions.addAll(IMainOptions.vanillaSettingsUI());
//		allModOptions.addAll(IMainOptions.mainOptionsUI());
//		allModOptions.addAll(IMainOptions.commonOptionsMap().getList());
//		allModOptions.add(IMainOptions.realNebulaSize);
//		allModOptions.add(IMainOptions.realNebulaShape);
//		allModOptions.add(IMainOptions.realNebulaeOpacity);
//		allModOptions.add(IMainOptions.showGuide);
//		allModOptions.add(IMainOptions.disableAutoHelp);
//		allModOptions.add(IMainOptions.defaultSettings);
		allModOptions.addAll(AllSubUI.instance().getAllModOptions());
//		allModOptions.addAll(IDebugOptions.debugOptionsMap().getList());
//		allModOptions.addAll(ICombatOptions.combatOptionsMap().getList());
//		allModOptions.addAll(IIronmanOptions.ironmanOptionsMap().getList());
//		allModOptions.addAll(IMainOptions.specieNameOptionsMap().getList());
//		allModOptions.addAll(IMainOptions.specieNameOptionsMapFr().getList());
		allModOptions.remove(null);
		// Then create the final list (LinkedHashSet don't offer the .get(index) method)
		SafeListParam options = new SafeListParam("ALL_MOD_OPTIONS");
		options.addAll(allModOptions);
		return options;
	};

	// ==================== GUI List Declarations ====================
	//
	static SafeListParam allCfgOptions() {
		SafeListParam list = new SafeListParam("ALL_CFG_OPTIONS");
		for(IParam param:getAllModOptions())
			if (param.isCfgFile())
				list.add(param);
		return list;
	}
	static SafeListParam allDuplicateOptions() {
		SafeListParam list = new SafeListParam("ALL_DUPLICATE_OPTIONS");
		for(IParam param:getAllModOptions())
			if (param.isCfgFile() && param.isDuplicate())
				list.add(param);
		return list;
	}
	static SafeListParam allNotCfgOptions() {
		SafeListParam list = new SafeListParam("ALL_NOT_CFG_OPTIONS");
		for(IParam param:getAllModOptions())
			if (!param.isCfgFile())
				list.add(param);
		return list;
	}
	// ==================== GUI Sub List Declarations ====================
	//
	static List<ParamSubUI> subPanelList() { return AllSubUI.instance().subPanelList(); }
//	static List<ParamSubUI> subPanelList() {
//		List<ParamSubUI> list = new ArrayList<>();
//		list.add(debugOptionsUI);
//		list.add(customRandomEventUI);
//		list.add(governorOptionsUI);
//		list.add(combatOptionsUI);
//		list.add(ironmanOptionsUI);
//		list.add(autoFlagOptionsUI);
//		return list;
//	}
}
