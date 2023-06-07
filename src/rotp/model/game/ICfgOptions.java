package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import static rotp.model.galaxy.StarSystem.*;
import static rotp.ui.UserPreferences.*;
import static rotp.ui.main.GalaxyMapPanel.*;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamOptions;
import rotp.util.FontManager;

// Options saved in Remnants.cfg
public interface ICfgOptions extends IMainOptions {
	// ==================== Parameters saved in Remnant.cfg ====================
	ParamBoolean displayYear	= new ParamBoolean( // Duplicate Do not add the list
			GAME_UI, "DISPLAY_YEAR", false) {
		{ isDuplicate(true); isCfgFile(true); }
		@Override public Boolean getOption()			{ return displayYear(); }
		@Override public void setOption(Boolean val)	{ displayYear(val); }
	};
//	default boolean displayYear()		{ return displayYear.get(); }
//	default void toggleYearDisplay()	{ displayYear.toggle(); }

	ParamFloat   showFleetFactor	= new ParamFloat(
			MOD_UI, "SHOW_FLEET_FACTOR"
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
	ParamOptions menuStartup		= new ParamOptions(MOD_UI, "MENU_STARTUP", ParamOptions.LAST)
	{	{ isCfgFile(true); } };
	ParamBoolean compactOptionOnly	= new ParamBoolean(MOD_UI, "COMPACT_OPTION_ONLY", false)
	{	{ isCfgFile(true); } };
//	ParamBoolean showGridCircular	= new ParamBoolean(MOD_UI, "SHOW_GRID_CIRCULAR", false)
//	{	{ isCfgFile(true); } };
	ParamBoolean useFusionFont		= new ParamBoolean(MOD_UI, "USE_FUSION_FONT", false)
	{
		{ isCfgFile(true); }
		@Override public void setOption(Boolean val) { FontManager.INSTANCE.resetGalaxyFont(); }
	};

	ParamBoolean showNextCouncil		= new ParamBoolean(MOD_UI, "SHOW_NEXT_COUNCIL", false) // Show years left until next council
	{	{ isCfgFile(true); } };
	ParamInteger galaxyPreviewColorStarsSize = new ParamInteger(MOD_UI, "GALAXY_PREVIEW_COLOR_SIZE" , 5, 0, 20, 1, 2, 5)
	{	{ isCfgFile(true); } };
	ParamInteger minListSizePopUp		= new ParamInteger(MOD_UI, "MIN_LIST_SIZE_POP_UP" , 4, 0, 10, true)
	{
		{ isCfgFile(true); }
	}	.specialZero(MOD_UI + "MIN_LIST_SIZE_POP_UP_NEVER");
	ParamInteger showLimitedWarnings	= new ParamInteger(MOD_UI, "SHOW_LIMITED_WARNINGS" , -1, -1, 49, 1, 2, 5)
	{
		{ isCfgFile(true); }
	}	.loop(true)
		.specialNegative(MOD_UI + "SHOW_LIMITED_WARNINGS_ALL");
	ParamBoolean showAlliancesGNN		= new ParamBoolean(MOD_UI, "SHOW_ALLIANCES_GNN", true)
	{	{ isCfgFile(true); } };
	ParamBoolean techExchangeAutoRefuse = new ParamBoolean(MOD_UI, "TECH_EXCHANGE_AUTO_NO", false)
	{	{ isCfgFile(true); } };

	// ==================== GUI List Declarations ====================
	LinkedList<IParam> modGlobalOptionsUI = new LinkedList<>(
			Arrays.asList(
			menuStartup, minListSizePopUp, showAlliancesGNN, displayYear,
			null,
			galaxyPreviewColorStarsSize, showLimitedWarnings, techExchangeAutoRefuse, showNextCouncil,
			null,
			showFleetFactor, showFlagFactor, showPathFactor, useFusionFont,
			null,
			showNameMinFont, showInfoFontRatio, mapFontFactor, compactOptionOnly
			));
}
