package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class GovOptions implements IOptionsSubUI {
	static final String OPTION_ID = "GOV_2";
	static final String HEAD_ID	  = GOV_UI;
	static final String NAME_KEY  = "SETUP_MENU";
	static final String TITLE_KEY = "SETUP_TITLE";
	
	@Override public String optionId()			{ return OPTION_ID; }
	@Override public String headId()			{ return HEAD_ID; }
	@Override public String uiNameKey()			{ return NAME_KEY; }
	@Override public String uiTitleKey()		{ return TITLE_KEY; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(HEAD_ID + "TRANSPORT_OPTIONS"),
				autoTransportAI, autotransportGov, autotransportAll,
				transportNoRich, transportPoorX2, transportMaxDist,

				headerSpacer,
				new ParamTitle(HEAD_ID + "COLONY_OPTIONS"),
				missileBasesMin, shieldAlones,
				autoSpend, reserveForSlow, shipBuilding,
				maxGrowthMode, terraformEarly
				)));
		map.add(new SafeListParam(Arrays.asList(				
				new ParamTitle(HEAD_ID + "INTELLIGENCE_OPTIONS"),
				auto_Infiltrate, auto_Spy, respectPromises,
				
				headerSpacer,
				new ParamTitle(HEAD_ID + "FLEET_OPTIONS"),
				// autoShipsByDefault,	// TODO: for future use
				auto_Scout, autoScoutCount,
				govAutoColonize, autoColonyCount,
				auto_Attack, autoAttackCount,
				
				headerSpacer,
				new ParamTitle(HEAD_ID + "STARGATES_OPTIONS"),
				starGateOption
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(HEAD_ID + "ASPECT_OPTIONS"),
				originalPanel, customSize, animatedImage,
				brightnessPct, sizeFactorPct,
				horizontalPosition, verticalPosition,
				
				headerSpacer,
				new ParamTitle(HEAD_ID + "OTHER_OPTIONS"),
				governorByDefault, auto_Apply
				)));
		return map;
	};
}
