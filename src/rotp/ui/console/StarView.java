package rotp.ui.console;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.empires.SystemInfo;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetType;
import rotp.util.Base;

public class StarView implements Base {
	private static final String newline = "<br>";
	private final CommandConsole console;
	private Empire pl, empire;
	private StarSystem sys;
	private SystemView sv;
	private SystemInfo si;
	private int id;
	private boolean isPlayer, scouted, colony;
	
	StarView(CommandConsole parent)	{ console = parent; }
	
	// ##### Systems Report
	String planetInfo(int p, String out)	{
		pl	= player();
		si	= player().sv;
		sv	= console.getView(p);
		sys	= console.getSys(p);
		id	= sys.id;
		empire 	= sv.empire();
		isPlayer= isPlayer(empire);
		scouted	= sv.scouted();
		colony	= sv.isColonized();

		out += newline + "(P " + p +") ";
		if (pl.hiddenSystem(sys)) // Dark Galaxy
			return out + " !!! Hidden";
		out = systemBox(out);
		out += fleetInfo();
		out = empireBox(out);
		out = terrainBox(out);
		out = distanceBox(out);
		return out;
	}
	// ##### SUB BOXES
	private String systemBox(String out)	{
		String name = sv.name();
		out += name;
		if (!scouted) {
			if (!name.isEmpty())
				out += newline;
			out += "Unexplored";
			return out;
		}
		out += cLn(planetEnvironment());
		out += cLn(transportInfo());
		out += cLn(researchProject());
		if (sv.isAlert())
			out += newline + "! Under Attack";
		return out;
	}
	private String empireBox(String out)	{
		if (sv.flagColorId() > 0)
		   	out += newline + systemFlag("Flag colors = ");
		if (!scouted)
			return out;
		out += cLn(sv.descriptiveName());
		if (!colony)
			return out;
		if (isPlayer) {
			out += " (Player)";
			out += newline + playerColonyData();
		}
		else {
			out += " (E "+ empire.id + ")";
			out += cLn(treatyStatus());
			out += newline + alienColonyData();
		}
		return out;
	}
	private String terrainBox(String out)	{
		if (isPlayer)
			return colonyControls(out);
		out += newline + text(sys.starType().description());		
		if (scouted)
			out += newline + planetColonizable();
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
		return out;
	}
	// ##### SUB ELEMENTS
	private String systemFlag(String out)	{
		int numFlag = options().selectedFlagColorCount();
		for (int i=0; i<numFlag; i++) {
			if (i>0)
				out += ", ";
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
			out += ", " + ecology;
		String resource = text(sv.resourceType());
		if (!resource.isEmpty())
			out += ", " + resource;
		if (sv.currentSize() > 0)
			out += newline + "Current Size = " + sv.currentSize();
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
		out += newline + "Factories = " + sv.factories();
		Colony colony = sv.colony();
		if (colony != null)
			out += " / max = " + colony.industry().maxBuildableFactories();
		if (sv.shieldLevel() > 0)
		   	out += newline + "Shield Level = " + sv.shieldLevel();
		int bases = sv.bases();
		if (bases > 0)
			out += newline + "Bases = " + bases;
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
		out += newline + "Factories = " + sv.factories();
		if (sv.shieldLevel() > 0)
		   	out += newline + "Shield Level = " + sv.shieldLevel();
		int bases = sv.bases();
		if (bases > 0)
			out += newline + "Bases = " + bases;
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
				out += newline + "In Orbit: ";
				if (pl == fl.empire())
					out += "player";
				else
					out += "alien (E " + fl.empId() + ") " + fl.empire().name();
				out += " fleet";				
			}
		}
		for (ShipFleet fl: pl.getEtaFleets(sys)) {
			out += newline + "Incoming ";
			if (pl == fl.empire())
				out += "player";
			else
				out += "alien (E " + fl.empId() + ") " + fl.empire().name();
			out += " fleet, ETA = " + (int) Math.ceil(fl.travelTime(sys)) + " Years";
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
		float range	  = (float) Math.ceil(si.distance(id)*10)/10;
		String out = "Distance = ";
		if (pl.alliedWith(id(sys.empire())))
			out += text("MAIN_ALLIED_COLONY");
		else
			out += text("MAIN_SYSTEM_RANGE", df1.format(range));
		out += ", ";
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
	// ##### Tools
	private String cLn(String s)	{ return s.isEmpty() ? "" : (newline + s); }
}
