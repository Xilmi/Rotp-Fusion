package rotp.ui.console;

import java.util.List;

import rotp.model.ships.ShipDesign;
import rotp.model.ships.ShipDesignLab;
import rotp.model.ships.ShipEngine;
import rotp.ui.util.ParamList;

public class ConsoleDesignOption implements IConsole {
	String SHIP_UI		= "SHIP_DESIGN_";

	ParamList hull, engines, maneuver, computer, armor, shield, ecm;
	ParamList[] weapons;
	ParamList[] specials;
	
	void init()	{
		hull	 = shipHull();
		engines	 = shipEngines();
		maneuver = shipManeuver();
		computer = shipComputer();
		armor	 = shipArmor();
		shield	 = shipShield();
		ecm		 = shipEcm();
		weapons	 = new ParamList[ShipDesign.maxWeapons];
		for (int i=0; i<ShipDesign.maxWeapons; i++)
			weapons[i] = shipWeapon();
		specials = new ParamList[ShipDesign.maxSpecials];
		for (int i=0; i<ShipDesign.maxSpecials; i++)
			specials[i] = shipSpecial();
	}
	private ParamList shipHull()		{
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipEngines()		{ // TODO BR: private ParamList shipEngines()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_ENGINE", "RETROS");
		param.showFullGuide(true);
		ShipDesignLab lab = player().shipLab();
		List<ShipEngine> engines = lab.engines();
		for (ShipEngine engine : engines) {
			String name = engine.name();
			String desc = engine.desc();
		}
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipManeuver()	{ // TODO BR: private ParamList shipManeuver()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipComputer()	{ // TODO BR: private ParamList shipComputer()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipArmor()		{ // TODO BR: private ParamList shipArmor()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipShield()		{ // TODO BR: private ParamList shipShield()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipEcm()			{ // TODO BR: private ParamList shipEcm()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipWeapon()			{ // TODO BR: private ParamList shipWeapon()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}
	private ParamList shipSpecial()			{ // TODO BR: private ParamList shipSpecial()
		ParamList param	= new ParamList(SHIP_UI, "SHIP_HULL", "SMALL");
		param.showFullGuide(true);
		param.put("SMALL",		SHIP_UI + "SIZE_SMALL");
		param.put("MEDIUM",		SHIP_UI + "SIZE_MEDIUM");
		param.put("LARGE",		SHIP_UI + "SIZE_LARGE");
		param.put("HUGE",		SHIP_UI + "SIZE_HUGE");
		return param;
	}

}
