package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class SetupMenuPreferences extends AbstractOptionsSubUI {
	static final String OPTION_ID = SETUP_MENU_PREF_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				useFusionFont,
				galaxyPreviewColorStarsSize,
				noFogOnIcons,
				showAlternateAnimation
				)));
		map.add(new SafeListParam(Arrays.asList(
				minListSizePopUp,
				compactOptionOnly,
				loadSaveWidth
				)));
		return map;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						compactOptionOnly
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						galaxyPreviewColorStarsSize,
						minListSizePopUp,
						showAlternateAnimation,
						noFogOnIcons,
						compactOptionOnly,
						useFusionFont,
						loadSaveWidth
						));
		return majorList;
	}
}
