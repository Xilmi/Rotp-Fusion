package rotp.ui.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import rotp.model.game.SafeListParam;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamSubUI;

public final class AllSubUI {
	private	static AllSubUI instance;
	public	static final String ALL_MOD_OPTIONS			= "ALL_MOD_OPTIONS";
	public	static final String ALL_CFG_OPTIONS			= "ALL_CFG_OPTIONS";
	public	static final String ALL_DUPLICATE_OPTIONS	= "ALL_DUPLICATE_OPTIONS";
	public	static final String ALL_NOT_CFG_OPTIONS		= "ALL_NOT_CFG_OPTIONS";
	private	final Map<String, IOptionsSubUI> uiMap = new HashMap<>();

	public static AllSubUI instance()			{
		if (instance == null)
			instance = new AllSubUI();
		return instance;
	}
	private AllSubUI()							{ init(); }
	private void put(IOptionsSubUI ui)			{ uiMap.put(ui.optionId(), ui); }
	private void init()							{
		put(new CombatOptions());
		put(new CommonOptions());
		put(new DebugOptions());
		put(new FlagOptions());
		put(new GalaxyMenuOptions());
		put(new GovOptions());
		put(new InGameOptions());
		put(new IronmanOptions());
		put(new MainOptions());
		put(new NameOptions());
		put(new NameFrOptions());
		put(new PreGameOptions());
		put(new RaceMenuOptions());
		put(new SystemsOptions());
	}

	public SafeListParam getAllModOptions()		{
		// Start with a set to filter duplicates
		LinkedHashSet<IParam> allModOptions = new LinkedHashSet<>();
		for (IOptionsSubUI ui : uiMap.values())
			allModOptions.addAll(ui.getList());
		
		// Then create the final list 
		SafeListParam list = new SafeListParam(ALL_MOD_OPTIONS);
		list.addAll(allModOptions);
		return list;
	}
	public SafeListParam allCfgOptions()		{
		SafeListParam list = new SafeListParam(ALL_CFG_OPTIONS);
		for(IParam param : getAllModOptions())
			if (param.isCfgFile())
				list.add(param);
		return list;
	}
	public SafeListParam allDuplicateOptions()	{
		SafeListParam list = new SafeListParam(ALL_DUPLICATE_OPTIONS);
		for(IParam param:getAllModOptions())
			if (param.isCfgFile() && param.isDuplicate())
				list.add(param);
		return list;
	}
	public SafeListParam allNotCfgOptions()		{
		SafeListParam list = new SafeListParam(ALL_NOT_CFG_OPTIONS);
		for(IParam param:getAllModOptions())
			if (!param.isCfgFile())
				list.add(param);
		return list;
	}

	public static ParamSubUI getUI(String name)	{ return instance().uiMap.get(name).getUI(); }
	public static ParamSubUI combatSubUI()		{ return getUI(CombatOptions.OPTION_ID); }
	public static ParamSubUI commonSubUI()		{ return getUI(CommonOptions.OPTION_ID); }
	public static ParamSubUI debugSubUI()		{ return getUI(DebugOptions.OPTION_ID); }
	public static ParamSubUI flagSubUI()		{ return getUI(FlagOptions.OPTION_ID); }
	public static ParamSubUI galaxySubUI()		{ return getUI(GalaxyMenuOptions.OPTION_ID); } // May be never used
	public static ParamSubUI governorSubUI()	{ return getUI(GovOptions.OPTION_ID); }
	public static ParamSubUI inGameSubUI()		{ return getUI(InGameOptions.OPTION_ID); }
	public static ParamSubUI ironmanSubUI()		{ return getUI(IronmanOptions.OPTION_ID); }
	public static ParamSubUI mainSubUI()		{ return getUI(MainOptions.OPTION_ID); }
	public static ParamSubUI nameSubUI()		{ return getUI(NameOptions.OPTION_ID); }
	public static ParamSubUI nameFrSubUI()		{ return getUI(NameFrOptions.OPTION_ID); }
	public static ParamSubUI preGameSubUI()		{ return getUI(PreGameOptions.OPTION_ID); }
	public static ParamSubUI raceSubUI()		{ return getUI(RaceMenuOptions.OPTION_ID); } // May be never used
	public static ParamSubUI systemSubUI()		{ return getUI(SystemsOptions.OPTION_ID); }

	public static SafeListParam optionsGalaxy() 	{ return GalaxyMenuOptions.optionsGalaxy(); }
	public static SafeListParam optionsCustomRace() { return RaceMenuOptions.optionsCustomRace(); }
	public static SafeListParam optionsRace() 		{ return RaceMenuOptions.optionsRace(); }
	
	// TODO BR: May be Not!!!
	public List<ParamSubUI> subPanelList()		{
		List<ParamSubUI> list = new ArrayList<>();
		for (IOptionsSubUI ui : uiMap.values())
			list.add(ui.getUI());
		return list;
	}
	public SafeListParam getParamList(String name)	{ return uiMap.get(name).getList(); }

}
