package rotp.model.game;

import static rotp.model.game.BaseOptions.MOD_UI;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.model.galaxy.StarSystem;
import rotp.ui.main.GalaxyMapPanel;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamOptions;
import rotp.util.FontManager;

// Options saved in Remnants.cfg
public interface RemnantOptions {
	ParamFloat   showFleetFactor	= new ParamFloat(
			MOD_UI, "SHOW_FLEET_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_STARGATE_SCALE		 = (int) (40 * newValue);
			GalaxyMapPanel.MAX_FLEET_UNARMED_SCALE	 = (int) (40 * newValue);
			GalaxyMapPanel.MAX_FLEET_TRANSPORT_SCALE = (int) (60 *newValue);
			GalaxyMapPanel.MAX_FLEET_SMALL_SCALE	 = (int) (60 * newValue);
			GalaxyMapPanel.MAX_FLEET_LARGE_SCALE	 = (int) (80 * newValue);
			GalaxyMapPanel.MAX_FLEET_HUGE_SCALE		 = (int) (100 * newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   showFlagFactor		= new ParamFloat(MOD_UI, "SHOW_FLAG_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_FLAG_SCALE = (int) (80 * newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   showPathFactor		= new ParamFloat(MOD_UI, "SHOW_PATH_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			GalaxyMapPanel.MAX_RALLY_SCALE = (int) (100 * newValue);
			return super.set(newValue);
		}
	};
	ParamInteger showNameMinFont	= new ParamInteger(MOD_UI, "SHOW_NAME_MIN_FONT", 8, 2, 24, 1, 2, 5) {
		@Override public Integer set(Integer newValue) {
			StarSystem.minFont	= newValue;
			StarSystem.minFont2	= Math.round(newValue/showInfoFontRatio.get());
			return super.set(newValue);
		}
	};
	ParamFloat   showInfoFontRatio	= new ParamFloat(MOD_UI, "SHOW_INFO_FONT_RATIO"
			, 0.7f, 0.2f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			StarSystem.minFont2	= Math.round(StarSystem.minFont/newValue);
			return super.set(newValue);
		}
	};
	ParamFloat   mapFontFactor		= new ParamFloat(MOD_UI, "MAP_FONT_FACTOR"
			, 1.0f, 0.3f, 3f, 0.01f, 0.05f, 0.2f, "%", "%") {
		@Override public Float set(Float newValue) {
			StarSystem.fontPct = Math.round(newValue * 100);
			return super.set(newValue);
		}
	};
	ParamOptions menuStartup		= new ParamOptions(MOD_UI, "MENU_STARTUP", ParamOptions.LAST);
	ParamBoolean compactOptionOnly	= new ParamBoolean(MOD_UI, "COMPACT_OPTION_ONLY", false);
	ParamBoolean showGridCircular	= new ParamBoolean(MOD_UI, "SHOW_GRID_CIRCULAR", false);
	ParamBoolean useFusionFont		= new ParamBoolean(MOD_UI, "USE_FUSION_FONT", false)
	{
		@Override public Boolean set(Boolean newValue) {
			FontManager.INSTANCE.resetGalaxyFont();
			return super.set(newValue);
		}
	};

	ParamBoolean showNextCouncil		= new ParamBoolean(MOD_UI, "SHOW_NEXT_COUNCIL", false); // Show years left until next council
	ParamInteger galaxyPreviewColorStarsSize = new ParamInteger(MOD_UI, "GALAXY_PREVIEW_COLOR_SIZE" , 5, 0, 20, 1, 2, 5);
	ParamInteger minListSizePopUp		= new ParamInteger(MOD_UI, "MIN_LIST_SIZE_POP_UP" , 4, 0, 10, true)
			.specialZero(MOD_UI + "MIN_LIST_SIZE_POP_UP_NEVER");
	ParamInteger showLimitedWarnings	= new ParamInteger(MOD_UI, "SHOW_LIMITED_WARNINGS" , -1, -1, 49, 1, 2, 5)
			.loop(true)
			.specialNegative(MOD_UI + "SHOW_LIMITED_WARNINGS_ALL");
	ParamBoolean showAlliancesGNN		= new ParamBoolean(MOD_UI, "SHOW_ALLIANCES_GNN", true);
	ParamBoolean techExchangeAutoRefuse = new ParamBoolean(MOD_UI, "TECH_EXCHANGE_AUTO_NO", false);

	// ==================== GUI List Declarations ====================
	LinkedList<InterfaceParam> modGlobalOptionsUI = new LinkedList<>(
			Arrays.asList(
			menuStartup, minListSizePopUp, showAlliancesGNN,
			null,
			showGridCircular, galaxyPreviewColorStarsSize, showLimitedWarnings, techExchangeAutoRefuse,
			null,
			showFleetFactor, showFlagFactor, showPathFactor, useFusionFont,
			null,
			showNameMinFont, showInfoFontRatio, mapFontFactor, showNextCouncil, compactOptionOnly
			));
}
