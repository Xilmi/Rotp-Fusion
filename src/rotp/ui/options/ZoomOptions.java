package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class ZoomOptions extends AbstractOptionsSubUI {
	static final String OPTION_ID = ZOOM_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("ZOOM_FONT"),
				mapFontFactor,
				showNameMinFont,
				showInfoFontRatio
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("ZOOM_FLEET"),
				showFleetFactor,
				showFlagFactor,
				showPathFactor
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("ZOOM_REPLAY"),
				finalReplayZoomOut,
				empireReplayZoomOut,
				replayTurnPace
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						mapFontFactor,
						showNameMinFont,
						showInfoFontRatio,
						LINE_SPACER_25,
						showFleetFactor,
						showFlagFactor,
						showPathFactor,
						LINE_SPACER_25,
						finalReplayZoomOut,
						empireReplayZoomOut,
						replayTurnPace
						));
		return majorList;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						showFlagFactor,
						showFleetFactor,
						mapFontFactor
						));
		return minorList;
	}
}
