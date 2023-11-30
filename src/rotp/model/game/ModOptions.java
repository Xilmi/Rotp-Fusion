package rotp.model.game;

import java.util.LinkedList;

import rotp.ui.util.IParam;

public class ModOptions implements IModOptions {

	@Override public int id()							{ return 0; }
	@Override public void id(int id)					{ }
	@Override public DynOptions dynOpts()				{ return null; }
	@Override public IGameOptions opts()				{ return null; }
	@Override public IGameOptions copyAllOptions()		{ return null; }
	@Override public void prepareToSave(boolean secure)	{ }
	@Override public void UpdateOptionsTools()			{ }
	@Override public void loadStartupOptions()			{ }
	@Override public void resetAllNonCfgSettingsToDefault()	{ }
	@Override public void saveOptionsToFile(String s)	{ }
	@Override public void saveOptionsToFile(String s, LinkedList<IParam> p)	{ }
	@Override public void updateFromFile(String s, LinkedList<IParam> p)	{ }
	@Override public void resetPanelSettingsToDefault(LinkedList<IParam> p,
			boolean excludeCfg, boolean excludeSubMenu)	{ }
	@Override public void copyAliensAISettings(IGameOptions dest)			{ }
	@Override public void updateAllNonCfgFromFile(String fileName)				{ }
}
