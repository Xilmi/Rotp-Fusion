package rotp.ui.console;

import java.util.List;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.empires.SystemInfo;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetType;
import rotp.model.ships.Design;
import rotp.model.ships.ShipDesignLab;
import rotp.ui.RotPUI;

public class StarView implements IConsole {
	private final CommandConsole console;
	private Empire pl, empire;
	private StarSystem sys;
	private SystemView sv;
	private SystemInfo si;
	private Colony colony;
	private int id;
	private boolean isPlayer, isScouted, isColony;
	
	// ##### CONSTRUCTOR #####
	StarView(CommandConsole parent)	{ console = parent; }

	void initId(int sysId)	{
		pl	= player();
		si	= pl.sv;
		sv	= si.view(sysId);
		sys	= galaxy().system(sysId);
		id	= sysId;
		empire 		= sv.empire();
		isPlayer	= isPlayer(empire);
		isScouted	= sv.scouted();
		isColony	= sv.isColonized();
		if (isPlayer)
			colony = sv.colony();
		else
			colony	= null;
	}

	void initAltId(int altId)	{
		initId(console.getSysId(altId));
//		pl	= player();
//		si	= player().sv;
//		sv	= console.getView(altId);
//		sys	= console.getSys(altId);
//		id	= sys.id;
//		empire 		= sv.empire();
//		isPlayer	= isPlayer(empire);
//		isScouted	= sv.scouted();
//		isColony	= sv.isColonized();
//		if (isPlayer)
//			colony = sv.colony();
//		else
//			colony	= null;
	}
	// ##### Systems Report
	String getInfo(String out)	{
		if (pl.hiddenSystem(sys)) // Dark Galaxy
			return out + " !!! Hidden";
		out = systemBox(out);
		out += fleetInfo();
		out = empireBox(out);
		out = terrainBox(out);
		out = distanceBox(out);
		//out = colonyControls(out);
		return out;
	}
	// ##### SUB BOXES
	private String systemBox(String out)	{
		out += planetName(sv, NEWLINE);
		out += cLn(planetEnvironment());
		out += cLn(transportInfo());
		out += cLn(researchProject());
		if (sv.isAlert())
			out += NEWLINE + "! Under Attack";
		return out;
	}
	private String empireBox(String out)	{
		if (sv.flagColorId() > 0)
		   	out += NEWLINE + systemFlag("Flag colors = ");
		if (!isColony)
			return out;
		out += NEWLINE + shortSystemInfo(sv);
		if (isPlayer)
			out += NEWLINE + playerColonyData();
		else {
			out += cLn(treatyStatus());
			out += NEWLINE + alienColonyData();
		}
		return out;
	}
	private String terrainBox(String out)	{
		if (isPlayer)
			return colonyControls(out);
		out += NEWLINE + text(sys.starType().description());		
		if (isScouted)
			out += NEWLINE + planetColonizable();
		return out;
	}
	private String distanceBox(String out)	{
		if (isPlayer)
			return out;
		out += cLn(systemRange());
		return out;
	}
	// ##### PLAYER COLONY CONTROLS
	private String colonyControls(String out)	{
		if (!isPlayer)
			return out;

		// Income
		int income = (int) colony.totalIncome();
		int prod   = (int) colony.production();
        String str1 = text("MAIN_COLONY_PRODUCTION");
        String str2 = str(income);
        String str3 = concat(str1, " ", str2, " ", "(", str(prod), ")");
		out += NEWLINE + str3;

        // Governor
		if (colony.isGovernor())
			out += NEWLINE + "Governor is On";
		else
			out += NEWLINE + "Governor is Off";
		
		// Categories
		for (int category=0; category<Colony.NUM_CATS; category++) {
			out += NEWLINE;
			int pct = 2 * colony.allocation(category);
			String labelText  = text(Colony.categoryName(category));
			String resultText = text(colony.category(category).upcomingResult());
			out += labelText + " = " + pct + "%, out = " + resultText;
			if (!colony.canAdjust(category))
				out += " (locked)";
		}
		
		// Ship Build Queue
		String str = text("MAIN_COLONY_SHIPYARD_CONSTRUCTION");
		String name = colony.shipyard().design().name();
		out += NEWLINE + str + " = " + name;
		Design d = colony.shipyard().design();
        if (d.scrapped())
        	out += " " + text("MAIN_COLONY_SHIP_SCRAPPED");
        else {
            int i = colony.shipyard().upcomingShipCount();
        	out += ", out = " + i;
        }
		String label = text("MAIN_COLONY_SHIPYARD_LIMIT");
		String amt   = colony.shipyard().buildLimitStr();
		out += NEWLINE + label + " " + amt;
		
		return out;
	}
	// ##### SUB ELEMENTS
	private String systemFlag(String out)	{
		int numFlag = options().selectedFlagColorCount();
		for (int i=0; i<numFlag; i++) {
			if (i>0)
				out += SPACER;
			out += sv.getFlagColorName(i);
		}
		return out;
	}
	private String planetColonizable()	{
		Empire pl = player();
		PlanetType pt = sv.planet().type();
		if (pl.canColonize(pt))
			return "Player can colonize";
		else if (pl.isLearningToColonize(pt))
			return "Player is learning tech to colonize";
		else if (pl.canLearnToColonize(pt))
			return "Player can learn tech to colonize";
		else if (pt.isAsteroids())
			return "Not colonizable";
		else
			return "Player does not have tech to colonize";
	}
	private String planetEnvironment()	{
		PlanetType planetType = sv.planetType();
		if (planetType == null)
			return "";
		String out = "Planet Type = " + planetType.name();
		String ecology = text(sv.ecologyType());
		if (!ecology.isEmpty())
			out += SPACER + ecology;
		String resource = text(sv.resourceType());
		if (!resource.isEmpty())
			out += SPACER + resource;
		if (sv.currentSize() > 0)
			out += NEWLINE + "Current Size = " + sv.currentSize();
		return out;		
	}
	private String playerColonyData()	{
		int pop	= sv.population();
		if (pop == 0)
			return text("MAIN_SYSTEM_DETAIL_NO_DATA");
		String out = "Population = " + pop;
		Planet planet = sv.planet();
		if (planet != null) {
			out += " / ECOmax = " + (int) planet.sizeAfterWaste();
		}
		if (si.isColonized(id) && si.colony(id).inRebellion())
			out += " ! " + text("MAIN_PLANET_REBELLION");
		out += NEWLINE + "Factories = " + sv.factories();
		Colony colony = sv.colony();
		if (colony != null)
			out += " / max = " + colony.industry().maxBuildableFactories();
		if (sv.shieldLevel() > 0)
		   	out += NEWLINE + "Shield Level = " + sv.shieldLevel();
		int bases = sv.bases();
		out += NEWLINE + "Bases = " + bases;
		if (colony != null) {
			int maxBase = colony.defense().maxBases();
			out += "/" + maxBase;
		}
		return out;
	}
	private String alienColonyData()	{
		int pop	= sv.population();
		if (pop == 0)
			return text("MAIN_SYSTEM_DETAIL_NO_DATA");
		String out = cLn(systemReportAge());
		out += "Population = " + pop;
		if (si.isColonized(id) && si.colony(id).inRebellion())
			out += " ! " + text("MAIN_PLANET_REBELLION");
		out += NEWLINE + "Factories = " + sv.factories();
		if (sv.shieldLevel() > 0)
		   	out += NEWLINE + "Shield Level = " + sv.shieldLevel();
		int bases = sv.bases();
		if (bases > 0)
			out += NEWLINE + "Bases = " + bases;
		return out;
	}
	private String transportInfo()		{
		if (sys.canShowIncomingTransports()) {
			if (isPlayer) {
				int friendPop = sys.colony().playerPopApproachingSystem();;
				int enemyPop  = sys.colony().enemyPopApproachingPlayerSystem();
				String str = "";
				if (friendPop > 0)
					str += text("Incoming population = ", friendPop);
				if (enemyPop > 0)
					return text("! Incoming enemy troop = ", enemyPop) + cLn(str);
			}
			else {
				int playerPop = sys.colony().playerPopApproachingSystem();;
				return text("Incoming player troop = ", playerPop);
			}
		}
		return "";
	}
	private String fleetInfo()			{
		String out = "";
		for (ShipFleet fl: sys.orbitingFleets()) {
			if (fl.visibleTo(pl)) {
				out += NEWLINE + "In Orbit " + longEmpireInfo(fl.empire()) + " fleet";
				out += NEWLINE + fleetDesignInfo(fl, NEWLINE);
			}
		}
		for (ShipFleet fl: pl.getEtaFleets(sys)) {
			out += NEWLINE + "Incoming " + longEmpireInfo(fl.empire()) + " fleet";
			out += NEWLINE + fleetDesignInfo(fl, NEWLINE);
			out += NEWLINE + "ETA = " + (int) Math.ceil(fl.travelTimeAdjusted(sys)) + " Years";
		}
		return out;
	}
	private String treatyStatus()		{
		int empId = si.empId(id);
		if (pl == empire)
			return "";
		if (pl.alliedWith(empId))
			return text("MAIN_FLEET_ALLY");
		else if (pl.atWarWith(empId))
			return text("MAIN_FLEET_ENEMY");
		else
			return "";
	}	
	private String researchProject()	{
		if (sys.hasEvent())
			return text(sys.eventKey());
		return "";
	}	
	private String systemReportAge()	{
		int age = player().sv.spyReportAge(sys.id);
		if (age > 0)
			return text("RACES_REPORT_AGE", age);
		else
			return "";
	}	
	private String systemRange()		{
		float range	= (float) Math.ceil(si.distance(id)*10)/10;
		String out  = "Distance = ";
		if (pl.alliedWith(id(sys.empire())))
			out += text("MAIN_ALLIED_COLONY");
		else
			out += text("MAIN_SYSTEM_RANGE", df1.format(range));
		out += SPACER;
		if (si.inShipRange(id)) {
			out += text("MAIN_IN_RANGE_DESC");
		}
		else if (si.inScoutRange(id)) {
			out += text("MAIN_SCOUT_RANGE_DESC");
		}
		else {
			out += text("MAIN_OUT_OF_RANGE_DESC");
		}
		return out;		
	}

	private String spending(int category, List<String> param, String out)	{
		if (param.isEmpty()) {
			return out + "Error: Missing parameter ";
		}

		String s  = param.remove(0);
		if (TOGGLE_LOCK.equalsIgnoreCase(s)) {
			colony.toggleLock(category);
			if (colony.canAdjust(category))
				return out + "Unlocked";
			else
				return out + "Locked";
		}
		
		if (!colony.canAdjust(category))
			return out + "Error: Category is locked";
		
		switch (s.toUpperCase()) {
			case SMART_ECO_MAX:
				colony.forcePct(category, 1);
				colony.keepEcoLockedToClean = true;
				colony.checkEcoAtClean();
				return out + "Maxed keeping ECO clean";
			case SMOOTH_MAX:
				colony.keepEcoLockedToClean = true;
				colony.smoothMaxSlider(category);
				colony.checkEcoAtClean();
				return out + "Smart Maxed";
			default:
				Integer val = getInteger(s);
				if (val == null)
					return out + "Unexpected parameter " + s;
				double pct = val/100.0;
				colony.forcePct(category, (float) pct);
				if (category != Colony.RESEARCH)
					RotPUI.instance().techUI().resetPlanetaryResearch();
				pct = colony.pct(category);
				return out + "Set to " + (int)(pct*100) + "%";
		}
	}
	// ##### Spending
	String toggleGovernor(String out)	{
		colony.setGovernor(!colony.isGovernor());
        if (colony.isGovernor()) {
            colony.govern();
            out += "Governor is now active";
        }
        else
        	out += "Governor is disabled";
		return out;
	}
	String shipSpending(List<String> param, String out)	{
		out += "Ship spending: ";
		return spending(Colony.SHIP, param, out);
	}
	String defSpending(List<String> param, String out)	{
		out += "Defense spending: ";
		return spending(Colony.DEFENSE, param, out);
	}
	String indSpending(List<String> param, String out)	{
		out += "Industry spending: ";
		return spending(Colony.INDUSTRY, param, out);
	}
	String ecoSpending(List<String> param, String out)	{
		int category = Colony.ECOLOGY;
		out += "Ecology spending: ";
		if (param.isEmpty()) {
			return out + "Error: Missing parameter ";
		}

		String s  = param.remove(0);
		if (TOGGLE_LOCK.equalsIgnoreCase(s)) {
			colony.toggleLock(category);
			if (colony.canAdjust(category))
				return out + "Unlocked";
			else
				return out + "Locked";
		}
		
		if (!colony.canAdjust(category))
			return out + "Error: Category is locked";
		
		switch (s.toUpperCase()) {
			case ECO_CLEAN:
				colony.keepEcoLockedToClean = true;
				colony.checkEcoAtClean();
				return out + "Set ECO to clean";
			case ECO_GROWTH:
				colony.smoothMaxSlider(category);
				return out + "Smart Maxed";
			case ECO_TERRAFORM:
				colony.checkEcoAtTerraform();
            	colony.keepEcoLockedToClean = false;
				return out + "Set ECO to Terraform";
			default:
				Integer val = getInteger(s);
				if (val == null)
					return out + "Unexpected parameter " + s;
				double pct = val/100.0;
				colony.forcePct(category, (float) pct);
				if (category != Colony.RESEARCH)
					RotPUI.instance().techUI().resetPlanetaryResearch();
				pct = colony.pct(category);
				return out + "Set to " + (int)(pct*100) + "%";
		}
	}
	String techSpending(List<String> param, String out)	{
		out += "Technology spending: ";
		return spending(Colony.RESEARCH, param, out);
	}
	String shipBuilding(List<String> param, String out)	{
		out += "Ship Building ";
		if (param.isEmpty()) {
			return out + "Error: Missing parameter ";
		}
		String s  = param.remove(0);
		Integer val = getInteger(s);
		if (val == null)
			return out + "Unexpected parameter " + s;
		int id = bounds(0, val, ShipDesignLab.MAX_DESIGNS-1);
		Design d;
		if (id == val)
			d = pl.shipLab().design(val);
		else if (!pl.tech().canBuildStargate())
			return out + "Error: Stargate Tech not yet available";
		else if (colony.shipyard().hasStargate())
			return out + "Error: Already has a Stargate";
		else
			d = pl.shipLab().stargateDesign();
		
		if (d.active())
			out += "Set to " + d.name();
		else
			return out + "Error Invalid design";
			
		colony.shipyard().design(d);
		return out;
	}
	String shipLimit(List<String> param, String out)	{
		out += "Ship Limit ";
		if (param.isEmpty()) {
			return out + "Error: Missing parameter ";
		}
		String s  = param.remove(0);
		Integer val = getInteger(s);
		if (val == null)
			return out + "Unexpected parameter " + s;
		colony.shipyard().buildLimit(val);
		out += "Set to " + colony.shipyard().buildLimitStr();
		return out;
	}
	String missBuilding(List<String> param, String out)	{
		out += "Missiles Limit ";
		if (param.isEmpty()) {
			return out + "Error: Missing parameter ";
		}
		String s  = param.remove(0);
		Integer val = getInteger(s);
		if (val == null)
			return out + "Unexpected parameter " + s;
		val = max(0, val);
		colony.defense().maxBases(val);
		out += "Set to " + colony.defense().maxBases();
		return out;
	}
}
