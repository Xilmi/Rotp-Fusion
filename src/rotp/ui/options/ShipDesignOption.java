package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class ShipDesignOption extends AbstractOptionsSubUI {
	static final String OPTION_ID = SHIP_DESIGN_OPTIONS_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GOVERNOR_AUTO_SEND"),
				fleetAutoScoutMode,
				fleetAutoColonizeMode,
				fleetAutoAttackMode,

				HEADER_SPACER_100,
				new ParamTitle(AUTO_SHIP_DESIGN + "SIZE"),
				autoShipDesignSize,
				HEADER_SPACER_50,
				shipDesignCostMultSmall,
				shipDesignCostMultMedium,
				shipDesignCostMultLarge,
				shipDesignCostMultHuge,

				HEADER_SPACER_100,
				new ParamTitle(AUTO_SHIP_DESIGN + "PREF"),
				autoShipDesignBoolean,
				HEADER_SPACER_50,
				shipDesignSpeedMatching,
				shipDesignReinforcedArmor,
				shipDesignBioWeapon
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(AUTO_SHIP_DESIGN + "SPACE"),
				autoShipDesignSpace,
				HEADER_SPACER_50,
				shipDesignModuleSpace,
				shipDesignShieldFightBomb,
				shipDesignShieldDestroyer,
				shipDesignEcmFightDestroy,
				shipDesignEcmBomber,
				shipDesignManeuverBD,
				shipDesignManeuverFighter,
				shipDesignArmorFightBomb,
				shipDesignArmorDestroyer,
				shipDesignSpecialWeight
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle(AUTO_SHIP_DESIGN + "SPECIAL"),
				autoShipDesignSpecial,
				shipDesignSpecialTruePct,
				shipDesignSpecialFalsePct,
				HEADER_SPACER_50,
				shipDesignPrefPulsar,
				shipDesignPrefCloak,
				shipDesignPrefRepair,
				shipDesignPrefInertial,
				shipDesignPrefMissShield,
				shipDesignPrefRepulsor,
				shipDesignPrefStasis,
				shipDesignPrefStreamProj,
				shipDesignPrefWarpDissip,
				shipDesignPrefTechNull,
				shipDesignPrefBeamFocus
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						autoShipDesignSpace,
						autoShipDesignSize,
						autoShipDesignBoolean,
						autoShipDesignSpecial
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						autoShipDesignSpace,
						autoShipDesignSize,
						autoShipDesignBoolean,
						autoShipDesignSpecial,
						HEADER_SPACER_50,
						shipDesignSpecialTruePct,
						shipDesignSpecialFalsePct
						));
		return majorList;
	}

}
