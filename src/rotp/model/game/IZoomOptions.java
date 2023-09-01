package rotp.model.game;

import static rotp.model.galaxy.StarSystem.fontPct;
import static rotp.model.galaxy.StarSystem.minFont;
import static rotp.model.galaxy.StarSystem.minFont2;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLAG_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_HUGE_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_LARGE_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_SMALL_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_TRANSPORT_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_FLEET_UNARMED_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_RALLY_SCALE;
import static rotp.ui.main.GalaxyMapPanel.MAX_STARGATE_SCALE;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IZoomOptions extends IBaseOptsTools {
	String ZOOM_GUI_ID		= "ZOOM_OPTIONS";

	ParamFloat   showFlagFactor		= new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { MAX_FLAG_SCALE = (int) (80 * val); }
	};
	ParamFloat   showPathFactor		= new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { MAX_RALLY_SCALE = (int) (100 * val); }
	};
	ParamInteger showNameMinFont	= new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT", 8, 2, 24, 1, 2, 5) {
		{ isCfgFile(true); }
		@Override public void setOption(Integer val) {
			minFont	 = val;
			minFont2 = Math.round(val/showInfoFontRatio.get());
		}
	};
	ParamFloat   showInfoFontRatio	= new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { minFont2	= Math.round(minFont/val);
		}
	};
	ParamFloat   mapFontFactor		= new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { fontPct = Math.round(val * 100); }
	};
	ParamFloat   showFleetFactor	= new ParamFloat( MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) {
			MAX_STARGATE_SCALE			= (int) (40 * val);
			MAX_FLEET_UNARMED_SCALE		= (int) (40 * val);
			MAX_FLEET_TRANSPORT_SCALE	= (int) (60 * val);
			MAX_FLEET_SMALL_SCALE		= (int) (60 * val);
			MAX_FLEET_LARGE_SCALE		= (int) (80 * val);
			MAX_FLEET_HUGE_SCALE		= (int) (100 * val);
		}
	};
	
	ParamBoolean finalReplayZoomOut	 = new ParamBoolean(MOD_UI, "ZOOM_FINAL_REPLAY", false)
	{	{ isCfgFile(true); } };
	default boolean finalReplayZoomOut()	{ return finalReplayZoomOut.get(); }

	ParamBoolean empireReplayZoomOut = new ParamBoolean(MOD_UI, "ZOOM_EMPIRE_REPLAY", false)
	{	{ isCfgFile(true); } };
	default boolean empireReplayZoomOut()	{ return empireReplayZoomOut.get(); }

	ParamInteger replayTurnPace		 = new ParamInteger(MOD_UI, "REPLAY_TURN_PACE" , 1, 1, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int replayTurnPace()			{ return replayTurnPace.get(); }
	default void replayTurnPace(int div)	{ replayTurnPace.set(div); }
	
	// ==================== GUI List Declarations ====================
	//
	
	LinkedList<LinkedList<IParam>> zoomOptionsMap = new LinkedList<LinkedList<IParam>>()
	{ {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_FONT"),
				mapFontFactor, showNameMinFont, showInfoFontRatio
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_FLEET"),
				showFleetFactor, showFlagFactor, showPathFactor
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_REPLAY"),
				finalReplayZoomOut, empireReplayZoomOut, replayTurnPace
				)));
		}
	};
	ParamSubUI zoomOptionsUI = new ParamSubUI( MOD_UI, "ZOOM_OPTIONS_UI", zoomOptionsMap,
			"ZOOM_OPTIONS_TITLE", ZOOM_GUI_ID)
	{ { isCfgFile(true); } };


	LinkedList<IParam> zoomOptions = zoomOptionsUI.optionsList();
}
