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

	ParamBoolean newWeaponSound		= new ParamBoolean(MOD_UI, "NEW_WEAPON_SOUND", true)
	{	{ isCfgFile(true); } };
	default boolean newWeaponSound() 		{ return newWeaponSound.get(); }

	ParamBoolean newWeaponAnimation	= new ParamBoolean(MOD_UI, "NEW_WEAPON_ANIMATION", true)
	{	{ isCfgFile(true); } };
	default boolean newWeaponAnimation()	{ return newWeaponAnimation.get(); }

	ParamBoolean alwaysShowsShield	= new ParamBoolean(MOD_UI, "ALWAYS_SHOWS_SHIELD", true)
	{	{ isCfgFile(true); } };
	default boolean alwaysShowsShield()		{ return alwaysShowsShield.get(); }

	ParamInteger beamWindupFrames	= new ParamInteger(MOD_UI, "BEAM_WINDUP_FRAMES" , 6, 1, 20, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int beamWindupFrames()			{ return beamWindupFrames.get(); }

	ParamInteger beamHoldFrames		= new ParamInteger(MOD_UI, "BEAM_HOLD_FRAMES" , 0, 0, 20, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int beamHoldFrames()			{ return beamHoldFrames.get(); }
	
	ParamInteger heavyBeamHoldFrames= new ParamInteger(MOD_UI, "HEAVY_BEAM_HOLD_FRAMES" , 6, 0, 20, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int heavyBeamHoldFrames()		{ return heavyBeamHoldFrames.get(); }
	
	ParamBoolean shieldFadingFrames	= new ParamBoolean(MOD_UI, "SHIELD_FADING_FRAMES", true)
	{	{ isCfgFile(true); } };
	default boolean shieldFadingFrames()	{ return shieldFadingFrames.get(); }

	ParamBoolean shieldEnveloping	= new ParamBoolean(MOD_UI, "SHIELD_ENVELOPING", true)
	{	{ isCfgFile(true); } };
	default boolean shieldEnveloping()		{ return shieldEnveloping.get(); }

	ParamInteger beamAnimationDelay	= new ParamInteger(MOD_UI, "BEAM_ANIMATION_DELAY" , 40, 0, 1000, 5, 20, 100)
	{	{ isCfgFile(true); } };
	default int beamAnimationDelay()		{ return beamAnimationDelay.get(); }

	ParamInteger showResultDelay	= new ParamInteger(MOD_UI, "SHOW_RESULT_DELAY" , 1000, 0, 5000, 100, 500, 2000)
	{	{ isCfgFile(true); } };
	default int showResultDelay()			{ return showResultDelay.get(); }

	ParamInteger shieldNoisePct		= new ParamInteger(MOD_UI, "SHIELD_NOISE_PCT" , 20, 0, 200, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldNoisePct()			{ return shieldNoisePct.get(); }

	ParamInteger shieldTransparency	= new ParamInteger(MOD_UI, "SHIELD_TRANSPARENCY" , 20, 0, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldTransparency()		{ return shieldTransparency.get(); }

	ParamInteger shieldFlickering	= new ParamInteger(MOD_UI, "SHIELD_FLICKERING" , 20, 0, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldFlickering()		{ return shieldFlickering.get(); }

	ParamInteger shieldBorder		= new ParamInteger(MOD_UI, "SHIELD_BORDER" , 0, -1, 5, 1, 1, 1)
	{	{ 
			isCfgFile(true);
			loop(true);
			specialZero(MOD_UI + "SHIELD_BORDER_SIZE");
			specialNegative(MOD_UI + "SHIELD_BORDER_SIZE_2");
		}
	};
	default int shieldBorder()				{ return shieldBorder.get(); }
	default int shieldBorder(int hullSize)	{
		if (shieldBorder.isSpecialZero())
			return hullSize+1;
		if (shieldBorder.isSpecialNegative())
			return 2* (hullSize+1);
		return shieldBorder.get();
	}
	ParamInteger weaponZposition	= new ParamInteger(MOD_UI, "WEAPON_Z_POS" , 150, -1000, 1000, 10, 50, 200)
	{	{ isCfgFile(true); } };
	default int weaponZposition()			{ return weaponZposition.get(); }

	ParamInteger weaponZRandom		= new ParamInteger(MOD_UI, "WEAPON_Z_RANDOM" , 50, 0, 500, 5, 20, 100)
	{	{ isCfgFile(true); } };
	default int weaponZRandom()				{ return weaponZRandom.get(); }
	
	ParamBoolean startShieldDemo	= new ParamBoolean(MOD_UI, "START_SHIELD_DEMO", false)
	{
		@Override public Boolean set(Boolean val) {
			if (val) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override public void run(){
						rotp.model.combat.DemoShields.main(null);
					}
				});
			}
			return super.set(false);
		}
	};
	default boolean startShieldDemo()		{ return startShieldDemo.get(); }
	

	// ==================== GUI List Declarations ====================
	//
	
	LinkedList<LinkedList<IParam>> zoomOptionsMap = new LinkedList<LinkedList<IParam>>()
	{ {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_FONT"),
				mapFontFactor, showNameMinFont, showInfoFontRatio,
				
				headerSpacer,
				new ParamTitle("ZOOM_FLEET"),
				showFleetFactor, showFlagFactor, showPathFactor,
				
				headerSpacer,
				new ParamTitle("ZOOM_REPLAY"),
				finalReplayZoomOut, empireReplayZoomOut, replayTurnPace
				)));
//		add(new LinkedList<>(Arrays.asList(
//				new ParamTitle("ZOOM_FLEET"),
//				showFleetFactor, showFlagFactor, showPathFactor
//				)));
//		add(new LinkedList<>(Arrays.asList(
//				new ParamTitle("ZOOM_REPLAY"),
//				finalReplayZoomOut, empireReplayZoomOut, replayTurnPace
//				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("WEAPON_ANIMATIONS"),
				newWeaponSound, newWeaponAnimation,
				alwaysShowsShield, showResultDelay,

				headerSpacer,
				beamWindupFrames, beamHoldFrames,
				heavyBeamHoldFrames, shieldFadingFrames,

				headerSpacer,
				 beamAnimationDelay, shieldEnveloping, shieldBorder,
				 shieldTransparency, shieldFlickering, shieldNoisePct,
				 weaponZposition, weaponZRandom,

				headerSpacer,
				startShieldDemo
				)));
		}
	};
	ParamSubUI zoomOptionsUI = new ParamSubUI( MOD_UI, "ZOOM_OPTIONS_UI", zoomOptionsMap,
			"ZOOM_OPTIONS_TITLE", ZOOM_GUI_ID)
	{ { isCfgFile(true); } };


	LinkedList<IParam> zoomOptions = zoomOptionsUI.optionsList();
}
