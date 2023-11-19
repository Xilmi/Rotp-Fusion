package rotp.model.game;

import static rotp.model.galaxy.StarSystem.getMinFont;
import static rotp.model.galaxy.StarSystem.setFontPct;
import static rotp.model.galaxy.StarSystem.setMinFont;
import static rotp.model.galaxy.StarSystem.setMinFont2;
import static rotp.ui.main.GalaxyMapPanel.maxFlagScale;
import static rotp.ui.main.GalaxyMapPanel.maxFleetHugeScale;
import static rotp.ui.main.GalaxyMapPanel.maxFleetLargeScale;
import static rotp.ui.main.GalaxyMapPanel.maxFleetSmallScale;
import static rotp.ui.main.GalaxyMapPanel.maxFleetTransportScale;
import static rotp.ui.main.GalaxyMapPanel.maxFleetUnarmedScale;
import static rotp.ui.main.GalaxyMapPanel.maxRallyScale;
import static rotp.ui.main.GalaxyMapPanel.maxStargateScale;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IZoomOptions extends IBaseOptsTools {
	String ZOOM_GUI_ID		= "ZOOM_OPTIONS";

	ParamFloat   showFlagFactor		= new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { maxFlagScale((int) (80 * val)); }
	};
	ParamFloat   showPathFactor		= new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { maxRallyScale((int) (100 * val)); }
	};
	ParamInteger showNameMinFont	= new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT", 8, 2, 24, 1, 2, 5) {
		{ isCfgFile(true); }
		@Override public void setOption(Integer val) {
			setMinFont(val);
			setMinFont2(Math.round(val/showInfoFontRatio.get()));
		}
	};
	ParamFloat   showInfoFontRatio	= new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { setMinFont2(Math.round(getMinFont()/val));
		}
	};
	ParamFloat   mapFontFactor		= new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) { setFontPct(Math.round(val * 100)); }
	};
	ParamFloat   showFleetFactor	= new ParamFloat( MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		{ isCfgFile(true); }
		@Override public void setOption(Float val) {
			maxStargateScale		((int) (40 * val));
			maxFleetUnarmedScale	((int) (40 * val));
			maxFleetTransportScale	((int) (60 * val));
			maxFleetSmallScale		((int) (60 * val));
			maxFleetLargeScale		((int) (80 * val));
			maxFleetHugeScale		((int) (100 * val));

//			MAX_STARGATE_SCALE			= (int) (40 * val);
//			MAX_FLEET_UNARMED_SCALE		= (int) (40 * val);
//			MAX_FLEET_TRANSPORT_SCALE	= (int) (60 * val);
//			MAX_FLEET_SMALL_SCALE		= (int) (60 * val);
//			MAX_FLEET_LARGE_SCALE		= (int) (80 * val);
//			MAX_FLEET_HUGE_SCALE		= (int) (100 * val);
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

	ParamBoolean playerSoundEcho		= new ParamBoolean(MOD_UI, "PLAYER_SOUND_ECHO", true)
	{	{ isCfgFile(true); } };
	default boolean playerSoundEcho() 		{ return playerSoundEcho.get(); }

	ParamBoolean alwaysShowsShield	= new ParamBoolean(MOD_UI, "ALWAYS_SHOWS_SHIELD", false)
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

	ParamBoolean shieldEnveloping	= new ParamBoolean(MOD_UI, "SHIELD_ENVELOPING", false)
	{	{ isCfgFile(true); } };
	default boolean shieldEnveloping()		{ return shieldEnveloping.get(); }

	ParamInteger beamAnimationFPS	= new ParamInteger(MOD_UI, "BEAM_ANIMATION_FPS" , 15, 5, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int beamAnimationFPS()			{ return beamAnimationFPS.get(); }
	default int beamAnimationDelay()		{ return 1000/beamAnimationFPS.get(); }

	ParamInteger showResultDelay	= new ParamInteger(MOD_UI, "SHOW_RESULT_DELAY" , 2000, 0, 5000, 100, 500, 2000)
	{	{ isCfgFile(true); } };
	default int showResultDelay()			{ return showResultDelay.get(); }

	ParamInteger shieldNoisePct		= new ParamInteger(MOD_UI, "SHIELD_NOISE_PCT" , 30, 0, 200, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldNoisePct()			{ return shieldNoisePct.get(); }

	ParamInteger shieldTransparency	= new ParamInteger(MOD_UI, "SHIELD_TRANSPARENCY" , 20, 0, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldTransparency()		{ return shieldTransparency.get(); }

	ParamInteger shieldFlickering	= new ParamInteger(MOD_UI, "SHIELD_FLICKERING" , 20, 0, 100, 1, 5, 20)
	{	{ isCfgFile(true); } };
	default int shieldFlickering()			{ return shieldFlickering.get(); }

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
	ParamInteger weaponZposition	= new ParamInteger(MOD_UI, "WEAPON_Z_POS" , 100, -1000, 1000, 10, 50, 200)
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
	
	ParamInteger echoSoundDelay		= new ParamInteger(MOD_UI, "ECHO_SOUND_DELAY" , 90, 10, 500, 5, 20, 50)
	{
		{ isCfgFile(true); }
		@Override public Integer set(Integer val) {
			if (val != get()) {
				rotp.util.sound.WavClip.clearDelayClips();
				rotp.util.sound.OggClip.clearDelayClips();
			}
			return super.set(val);
		}
	};
	default int echoSoundDelay()			{ return echoSoundDelay.get(); }

	ParamInteger echoSoundHullDelay	= new ParamInteger(MOD_UI, "ECHO_SOUND_HULL_DELAY" , 30, 0, 100, 1, 5, 20)
	{
		{ isCfgFile(true); }
		@Override public Integer set(Integer val) {
			if (val != get()) {
				rotp.util.sound.WavClip.clearDelayClips();
				rotp.util.sound.OggClip.clearDelayClips();
			}
			return super.set(val);
		}
	};
	default int echoSoundHullDelay()		{ return echoSoundHullDelay.get(); }

	ParamInteger echoSoundDecay		= new ParamInteger(MOD_UI, "ECHO_SOUND_DECAY" , 50, 0, 95, 1, 5, 20)
	{
		{ isCfgFile(true); }
		@Override public Integer set(Integer val) {
			if (val != get()) {
				rotp.util.sound.WavClip.clearDelayClips();
				rotp.util.sound.OggClip.clearDelayClips();
			}
			return super.set(val);
		}
	};
	default float echoSoundDecay()			{ return echoSoundDecay.get()/100f; }

	ParamBoolean former2DShield		= new ParamBoolean(MOD_UI, "FORMER_2D_SHIELD", true)
	{	{ isCfgFile(true); } };
	default boolean former2DShield()		{ return former2DShield.get(); }

	ParamList shieldType			= new ParamList( MOD_UI, "NEW_WEAPON_ANIMATION", "Yes") {
		{
			{ isCfgFile(true); }
			showFullGuide(true);
			put("No",	MOD_UI + "SHIELD_TYPE_NONE"); // for compatibility with former boolean
			put("Yes",	MOD_UI + "SHIELD_TYPE_3D");   // for compatibility with former boolean
			put("2D",	MOD_UI + "SHIELD_TYPE_2D");
			put("3B",	MOD_UI + "SHIELD_TYPE_3_BUFFERS");
		}
	};
	default boolean shieldType3D()			{ return shieldType.get().equalsIgnoreCase("Yes"); }
	default boolean shieldType2D()			{ return shieldType.get().equalsIgnoreCase("2D"); }
	default boolean shieldType3Buffer()		{ return shieldType.get().equalsIgnoreCase("3B"); }

	ParamBoolean showPendingOrders	= new ParamBoolean(MOD_UI, "SHOW_PENDING_ORDERS", false);
	default boolean showPendingOrders()		{ return showPendingOrders.get(); }

	// ==================== GUI List Declarations ====================
	//
	ParamSubUI zoomOptionsUI = zoomOptionsUI();

	static LinkedList<LinkedList<IParam>> zoomOptionsMap() {
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ZOOM_FONT"),
				mapFontFactor, showNameMinFont, showInfoFontRatio,
				showPendingOrders,
				
				headerSpacer,
				new ParamTitle("ZOOM_FLEET"),
				showFleetFactor, showFlagFactor, showPathFactor,
				
				headerSpacer,
				new ParamTitle("ZOOM_REPLAY"),
				finalReplayZoomOut, empireReplayZoomOut, replayTurnPace
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("WEAPON_ANIMATIONS"),
				showResultDelay,
				newWeaponSound, playerSoundEcho,
				echoSoundDecay, echoSoundDelay, echoSoundHullDelay,

				headerSpacer,
				beamWindupFrames, beamHoldFrames,
				heavyBeamHoldFrames, shieldFadingFrames
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("SHIELD_ANIMATIONS"),
				shieldType, alwaysShowsShield, 
	
				headerSpacer,
				beamAnimationFPS, shieldEnveloping, shieldBorder,
				shieldTransparency, shieldFlickering, shieldNoisePct,
				weaponZposition, weaponZRandom,
	
				headerSpacer,
				startShieldDemo
				)));
		return map;
	};
	static ParamSubUI zoomOptionsUI() {
		return new ParamSubUI( MOD_UI, "ZOOM_OPTIONS_UI", zoomOptionsMap(),
				"ZOOM_OPTIONS_TITLE", ZOOM_GUI_ID)
		{ { isCfgFile(true); } };
	}
	static LinkedList<IParam> zoomOptions() {
		return IBaseOptsTools.getSingleList(zoomOptionsMap());
	}
}
