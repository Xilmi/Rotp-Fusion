package rotp.ui.console;

import java.util.List;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.empires.SystemInfo;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.StarSystem;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetType;
import rotp.ui.RotPUI;
import rotp.ui.main.MainUI;
import rotp.util.Base;

public class StarView implements Base {
	private static final String newline = "<br>";
	private final CommandConsole console;
	private Empire pl, empire;
	private StarSystem sys;
	private SystemView sv;
	private boolean isPlayer, scouted, colony;
	
	StarView(CommandConsole parent)	{ console = parent; }
	
	private MainUI mainUI()	  { return RotPUI.instance().mainUI(); }

	String selectSystem (int altIndex) {
		altIndex = validPlanet(altIndex);
		console.selectedStar = altIndex;
		StarSystem selectedSystem = console.getSys(altIndex);
		mainUI().selectSystem(selectedSystem);
		return planetInfo(altIndex, "");
	}
	// ##### Systems Report
	String planetInfo(int p, String out)	{
		pl	= player();
		sys	= console.getSys(p);
		sv		= console.getView(p);
		empire 	= sv.empire();
		isPlayer= isPlayer(empire);
		scouted	= sv.scouted();
		colony	= sv.isColonized();

		out += newline + "(P " + p +") ";
		if (pl.hiddenSystem(sys)) // Dark Galaxy
			return out + " !!! Hidden";
		out = systemBox(out);
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
		if (planet != null)
			out += " / ECOmax = " + (int) planet.sizeAfterWaste();
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
		String out = "Population = " + pop;
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
	private String treatyStatus()		{
		int id = sys.id;
		int empId = pl.sv.empId(id);
		if (empId == pl.id)
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
	private String systemPopulation()	{
		int id	= sys.id;
		SystemInfo si = player().sv;
		Planet planet = sys.planet();
		String out = "";
		if (planet.maxSize() > 0) {
			int planetSize = (int) si.currentSize(id);
			int population = (int) si.population(id);
			if (si.isColonized(id) && si.colony(id).inRebellion())
				out += "! " + text("MAIN_PLANET_REBELLION");
			else if (planetSize == population)
				out += text("MAIN_PLANET_POP", population);
			else
				out += text("MAIN_PLANET_POP_SIZE", population, planetSize);
		}
		return out;
	}	
	private String systemSize()			{
		int id		= sys.id;
		Planet planet	= sys.planet();
		String out = "";
		if (!planet.type().isAsteroids() && (planet.maxSize() > 0)) {
			boolean ignoreWaste = planet.isColonized() && planet.empire().ignoresPlanetEnvironment();
			int planetSize = 0;
			if (planet.empire() != pl)
				planetSize = pl.sv.currentSize(id);
			else 
				planetSize = ignoreWaste ? (int) planet.currentSize() : (int) planet.sizeAfterWaste();
			if (!(ignoreWaste || (planet.waste() == 0)))
				out += "! Waste = " + planet.waste() + newline;
			if (pl.sv.isColonized(id) && pl.sv.colony(id).inRebellion())
				out += "! " + text("MAIN_PLANET_REBELLION");
			else if (planet.currentSize() == planet.maxSize())
				out += text("MAIN_PLANET_SIZE", planetSize);
			else if (pl.sv.isColonized(id) && pl.sv.empire(id).isAI())
				out += text("MAIN_PLANET_SIZE", planetSize);
			else
				out += text("MAIN_PLANET_SIZE+", planetSize);
		}
		return out;
	}	
	private String systemReportAge()	{
		int age = player().sv.spyReportAge(sys.id);
		if (age > 0)
			return text("RACES_REPORT_AGE", age);
		else
			return "";
	}	
	private String systemRange()		{
		int id	= sys.id;
		SystemInfo si = pl.sv;
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
	private int validPlanet(int p)	{ return bounds(0, p, galaxy().systemCount-1); }
}
