package rotp.model.game;

import static rotp.model.game.DefaultValues.MOO1_DEFAULT;
import static rotp.model.game.DefaultValues.ROTP_DEFAULT;
import static rotp.model.game.IBaseOptsTools.headerSpacer;

import java.util.Arrays;

import rotp.model.game.GovernorOptions.GatesGovernor;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IGovOptions {
	
	// ==================== Governor Options ====================
	//
	String GOV_UI		= "GOVERNOR_";
	String GOV_GUI_ID	= "GOV_2";
	int	NOT_GOVERNOR	= 0;
	int	NO_REFRESH		= 0;
	int GOV_REFRESH		= 1;
	int GOV_RESET		= 2;
	
	// AutoTransport Options
	ParamBoolean autoTransportAI	= new ParamBoolean(GOV_UI, "AUTO_TRANSPORT", false);
	ParamBoolean autotransportGov	= new ParamBoolean(GOV_UI, "AUTOTRANSPORT_GOV", false);
	ParamBoolean autotransportAll	= new ParamBoolean(GOV_UI, "TRANSPORT_UNGOVERNED", false);
	ParamBoolean transportNoRich	= new ParamBoolean(GOV_UI, "TRANSPORT_RICH_OFF", true);
	ParamBoolean transportPoorX2	= new ParamBoolean(GOV_UI, "TRANSPORT_POOR_DBL", true);
	ParamInteger transportMaxDist	= new ParamInteger(GOV_UI, "TRANSPORT_MAX_TURNS", 5)
			.setLimits(1, 15)
			.setIncrements(1, 3, 5);

	// StarGates Options
	// Using an Enum object instead of a list will break the game save if the enum is changed! 
	ParamList	 starGateOption		= initStarGateOption();
	static ParamList initStarGateOption() {
		ParamList list = new ParamList(GOV_UI, "STARGATES_OPTIONS", GatesGovernor.Rich.name());
		list.showFullGuide(true);
		for (GatesGovernor value: GatesGovernor.values())
			list.put(value.name(), GOV_UI + "STARGATES_" + value.name().toUpperCase());
		return list;
	}

	// Colony Options
	ParamInteger missileBasesMin	= new ParamInteger(GOV_UI, "MIN_MISSILE_BASES", 0)
			.setLimits(0, 1000)
			.setIncrements(1, 5, 20);
	ParamBoolean shieldAlones		= new ParamBoolean(GOV_UI, "SHIELD_WITHOUT_BASES", false);
	ParamBoolean autoSpend			= new ParamBoolean(GOV_UI, "AUTOSPEND", false);
	ParamInteger reserveForSlow		= new ParamInteger(GOV_UI, "RESERVE", 0)
			.setLimits(0, 100000)
			.setIncrements(10, 50, 200);
	ParamBoolean shipBuilding		= new ParamBoolean(GOV_UI, "SHIP_BUILDING", true);
	ParamBoolean maxGrowthMode		= new ParamBoolean(GOV_UI, "LEGACY_GROWTH_MODE", true);
	ParamInteger terraformEarly		= new ParamInteger(GOV_UI, "TERRAFORM_EARLY", 0)
			.setLimits(0, 400)
			.setIncrements(1, 5, 20);

	// Intelligence Options
	ParamBoolean auto_Infiltrate	= new ParamBoolean(GOV_UI, "AUTO_INFILTRATE", true);
	ParamBoolean auto_Spy			= new ParamBoolean(GOV_UI, "AUTO_SPY", false);
	ParamBoolean respectPromises	= new ParamBoolean(GOV_UI, "SPARE_XENOPHOBES", false); // Keep the name for backward compatibility

	// Aspect Options
	ParamBoolean originalPanel		= new ParamBoolean(GOV_UI, "ORIGINAL_PANEL", false);
	ParamBoolean customSize			= new ParamBoolean(GOV_UI, "CUSTOM_SIZE", true);
	ParamInteger brightnessPct		= new ParamInteger(GOV_UI, "BRIGHTNESS", 100)
			.setLimits(20, 300)
			.setIncrements(1, 5, 20);
	ParamInteger sizeFactorPct		= new ParamInteger(GOV_UI, "SIZE_FACTOR", 100)
			.setLimits(20, 200)
			.setIncrements(1, 5, 20);
	ParamInteger horizontalPosition	= new ParamInteger(GOV_UI, "POSITION_X", 0)
			.setLimits(null, null)
			.setIncrements(1, 5, 20);
	ParamInteger verticalPosition	= new ParamInteger(GOV_UI, "POSITION_Y", 0)
			.setLimits(null, null)
			.setIncrements(1, 5, 20);

	// Fleet Options
	ParamBoolean auto_Scout			= new ParamBoolean(GOV_UI, "AUTO_SCOUT", true);
	ParamInteger autoScoutCount		= new ParamInteger(GOV_UI, "AUTO_SCOUT_COUNT",	1)
			.setLimits(1, 9999)
			.setIncrements(1, 5, 20);
	ParamBoolean govAutoColonize	= new ParamBoolean(GOV_UI, "AUTO_COLONIZE", true);
	ParamInteger autoColonyCount	= new ParamInteger(GOV_UI, "AUTO_COLONY_COUNT", 1)
			.setLimits(1, 9999)
			.setIncrements(1, 5, 20);
	ParamBoolean auto_Attack		= new ParamBoolean(GOV_UI, "AUTO_ATTACK", false);
	ParamInteger autoAttackCount	= new ParamInteger(GOV_UI, "AUTO_ATTACK_COUNT", 1)
			.setLimits(1, 9999)
			.setIncrements(1, 5, 20);
    // if true, new colonies will have auto ship building set to "on"
	ParamBoolean autoShipsDefault	= new ParamBoolean(GOV_UI, "AUTOSHIPS_BY_DEFAULT", false);

	// Other Options
	ParamBoolean animatedImage		= new ParamBoolean(GOV_UI, "ANIMATED_IMAGE", true);
	ParamBoolean auto_Apply			= new ParamBoolean(GOV_UI, "AUTO_APPLY", true);
	ParamBoolean governorByDefault	= new ParamBoolean(GOV_UI, "ON_BY_DEFAULT", true)
			.setDefaultValue(MOO1_DEFAULT, false)
			.setDefaultValue(ROTP_DEFAULT, false);

	// ==================== GUI List Declarations ====================
	//

	SafeListParam mergedStaticOptions	= new SafeListParam();
	SafeListPanel governorOptionsMap = governorOptionsMap();
	static SafeListPanel governorOptionsMap()	{
		SafeListPanel map = new SafeListPanel();
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(GOV_UI + "TRANSPORT_OPTIONS"),
				autoTransportAI, autotransportGov, autotransportAll,
				transportNoRich, transportPoorX2, transportMaxDist,

				headerSpacer,
				new ParamTitle(GOV_UI + "COLONY_OPTIONS"),
				missileBasesMin, shieldAlones,
				autoSpend, reserveForSlow, shipBuilding,
				maxGrowthMode, terraformEarly
				)));
		map.add(new SafeListParam(Arrays.asList(				
				new ParamTitle(GOV_UI + "INTELLIGENCE_OPTIONS"),
				auto_Infiltrate, auto_Spy, respectPromises,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "FLEET_OPTIONS"),
				// autoShipsByDefault,	// TODO: for future use
				auto_Scout, autoScoutCount,
				govAutoColonize, autoColonyCount,
				auto_Attack, autoAttackCount,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "STARGATES_OPTIONS"),
				starGateOption
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(GOV_UI + "ASPECT_OPTIONS"),
				originalPanel, customSize, animatedImage,
				brightnessPct, sizeFactorPct,
				horizontalPosition, verticalPosition,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "OTHER_OPTIONS"),
				governorByDefault, auto_Apply
				)));
		for (SafeListParam list : map) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					mergedStaticOptions.add(param);
			}
		}
		return map;
	};
	ParamSubUI	governorOptionsUI	= new ParamSubUI(GOV_UI, "SETUP_MENU",
			governorOptionsMap, "SETUP_TITLE", GOV_GUI_ID);
	SafeListParam governorOptions = governorOptionsUI.optionsList();
}
