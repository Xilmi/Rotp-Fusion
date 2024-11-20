package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class ComputerOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = COMPUTER_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				graphicsMode,
				texturesMode,
				sensitivityMode
				)));
		map.add(new SafeListParam(Arrays.asList(
				soundVolume,
				musicVolume
				)));
		map.add(new SafeListParam(Arrays.asList(
				colorSet,
				shipBasedMissiles,
				menuStartup
				)));
		map.add(AllSubUI.getHandle(BACKUP_OPTIONS_UI_KEY).getUiMajor(false));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						soundVolume,
						musicVolume
						));
		return majorList;
	}
}
