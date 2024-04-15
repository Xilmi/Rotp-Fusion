package rotp.ui.console;

import static rotp.ui.console.CommandConsole.cc;

import java.util.List;

import rotp.model.empires.DiplomaticTreaty;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.empires.SystemView;
import rotp.model.empires.TreatyAlliance;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.galaxy.Transport;
import rotp.model.ships.ShipDesign;
import rotp.util.Base;

public interface IConsole extends Base {
	int NULL_ID		= -1;
	int OPTION_ID	= 0;
	int SETTING_ID	= 1;
	int MENU_ID		= 2;
	String NEWLINE	= "<br>";
	String SPACER	= ", ";
	String AIMED_KEY		= "A";
	String DESIGN_KEY		= "D";
	String EMPIRE_KEY		= "E";
	String FLEET_KEY		= "F";
	String SYSTEM_KEY		= "P";
	String TRANSPORT_KEY	= "T";
	String MENU_KEY			= "M";
	String OPTION_KEY		= "O";
	String SETTING_KEY		= "S";

	String TOGGLE_GOV		= "TG";
	String SHIP_SPENDING	= "S";
	String DEF_SPENDING		= "D";
	String IND_SPENDING		= "I";
	String ECO_SPENDING		= "E";
	String TECH_SPENDING	= "R";
	String SHIP_BUILDING	= "SB";
	String SHIP_LIMIT		= "SL";
	String BASE_LIMIT		= "BL";

	String TOGGLE_LOCK		= "TL";
	String SMART_ECO_MAX	= "EM";
	String SMOOTH_MAX		= "SM";
	String ECO_CLEAN		= "C";
	String ECO_GROWTH		= "G";
	String ECO_TERRAFORM	= "T";

	String TROOP_SEND		= "SEND";
	String ABANDON			= "ABANDON";
	String CANCEL_SEND		= "CANCEL";

	String FLEET_SEND		= "SEND";
	String FLEET_UNDEPLOY	= "U";

	// ##### TOOLS
	default CommandConsole console()	{ return cc(); }
	default Empire empire(int empId)	{ return galaxy().empire(empId); }
	default String cLn(String s)		{ return s.isEmpty() ? "" : (NEWLINE + s); }
	default String ly(float dist)		{ return text("SYSTEMS_RANGE", df1.format(Math.ceil(10*dist)/10)); }
	default String bracketed(String key, int index)		{ return "(" + key + " " +index + ")"; }
	default String setDest(List<String> param, String out) {
		String s = param.get(0);
		Integer f;
		if (s.equalsIgnoreCase(AIMED_KEY))
			param.remove(0); // Parameter processed... Implicit target, Nothing to do
		else if (s.startsWith(SYSTEM_KEY)) { // New destination
			if (s.length() > 1) { // Parameter linked
				s = s.substring(1);
				param.remove(0); // Parameter processed
			}
			else { // Planet number in the following parameter
				param.remove(0); // Parameter processed
				if (param.isEmpty()) {
					out += NEWLINE + "Wrong Destination Parameter";
					return out;
				}
				else { // get planet Number
					s = param.remove(0);
				}
			}
			// Process the planet index
			f = getInteger(s);
			if (f != null) { // select a new Destination
				console().aimedStar(f);
			}
			else {
				out += NEWLINE + "Wrong Destination Parameter";
				return out;
			}
		}
		return out;
	}
	// ##### FLEETS
	default String fleetDesignInfo(ShipFleet fl, String sep)	{
		String out = "";
		int[] visible = fl.visibleShips(player().id);
		// count how many of those visible designs have ships
		int num = 0;
		String separator = "";
		for (int cnt: visible)
			if (cnt > 0) {
				out += separator + shipDesignInfo(fl, num);
				separator = sep;
				num++;
			}
		return out;
	}
	default String shipDesignInfo(ShipFleet fl, int i)	{
		Empire pl = player();
		boolean isPlayer = isPlayer(fl.empire());
		boolean contact	 = isPlayer || pl.hasContacted(fl.empId());
		String out = "";
		ShipDesign d = fl.visibleDesign(pl.id, i);
		// draw design ID
		if (isPlayer)
			out += bracketed(DESIGN_KEY, d.id()) + " ";
		// draw ship count
		int count = fl.num(d.id());
		out += count + " ";
		// draw ship name
		if (contact)
			out += d.name();
		else
			out += "Unknown ";
		return out;
	}
	default String transportInfo(Transport transport, String sep)	{
		Empire pl  = player();
		Empire emp = transport.empire();
		String out = bracketed(TRANSPORT_KEY, cc().getTransportIndex(transport));
		out += " Size = " + transport.launchSize();
		out += sep + "Owner = " + longEmpireInfo(emp);
		if (pl.knowETA(transport)) {
			SystemView sv = cc().getView(transport.from().altId);
			out += sep + "From " + planetName(sv, sep);
			sv = cc().getView(transport.destination().altId);
			out += sep + "To " + planetName(sv, sep);
			int eta = transport.travelTurnsRemainingAdjusted();
			out += sep + "ETA = " + eta + " year";
			if (eta>1)
				out += "s";
		}
		else
			out += sep + closestSystem(transport, sep);
		return out;
	}
	// ##### SYSTEMS
	default String planetName(int altId)	{ return planetName(cc().getView(altId), SPACER); }
	default String planetNameCR(int altId)	{ return planetName(cc().getView(altId), NEWLINE); }
	default String planetName(int altId, String sep)	{ return planetName(cc().getView(altId), sep); }
	default String planetName(SystemView sv, String sp)	{
		String out = bracketed(SYSTEM_KEY, sv.system().altId) + " ";
		String name = sv.name();
		out += name;
		if (!sv.scouted()) {
			if (!name.isEmpty())
				out += sp;
			out += "Unexplored";
			return out;
		}
		return out;
	}
	default String shortSystemInfo(SystemView sv)		{
        if (!sv.isColonized()) {
            if (!sv.scouted()) 
                return text("MAIN_UNSCOUTED");
            else if (sv.system().planet().isEnvironmentNone())
                return text("MAIN_NO_PLANETS");
            else if (sv.abandoned())
                return text("MAIN_ABANDONED");
            else
                return text("MAIN_NO_COLONIES");
        }
        String out;
        Empire emp	= sv.empire();
        String name	= emp.raceName();
        String id	= bracketed(EMPIRE_KEY, emp.id) + " ";
        if (!sv.scouted())
            out = text("MAIN_UNSCOUTED") + " " +  id + text("PLANET_WORLD", name);
        else if (emp.isHomeworld(sv.system()))
            out = id + text("PLANET_HOMEWORLD", name);
        else if (emp.isColony(sv.system()))
            out = id + text("PLANET_COLONY", name);
        else
            out = id + text("PLANET_WORLD", name);
        out = emp.replaceTokens(out, "alien");
        return out;
	}
	default String closestSystem(IMappedObject mapObj, String sep)	{
		StarSystem sys = player().closestSystem(mapObj);
		String out = "Closest System = ";
		out += planetName(sys.altId, sep);
		out += sep + "Distance = " + ly(sys.distanceTo(mapObj));
		return out;
	}
	// ##### EMPIRES
	default String shortEmpireInfo(Empire emp)			{
		if (player().knowsOf(emp))
			return bracketed(EMPIRE_KEY, emp.id);
		else
			return "Unknown";
	}
	default String longEmpireInfo(Empire emp)			{
		String out = shortEmpireInfo(emp);
		if (player().knowsOf(emp))
			out += " " + emp.name();
		return out;
	}
	default String empireContactInfo(Empire emp, String sep)	{
		if (!player().knowsOf(emp))
			return "Unknown Empire";
		String out = longEmpireInfo(emp);
		boolean inRange = true;
		if (emp.isPlayer()) {
			List<EmpireView> views = player().contacts();
			int n = views.size();
			out += sep + text("RACES_KNOWN_EMPIRES", n);
			if (n > 0) {
				int r = 0;
				for (EmpireView v : views)
					if (!v.diplomats())
						r++;
				out += sep + text("RACES_RECALLED_DIPLOMATS", r);
			}
		}
		else {
			EmpireView view = player().viewForEmpire(emp);
			inRange = view.inEconomicRange();
			if (inRange) {
				out += sep + empireTreaty(emp);
				out += sep + empireTrade(emp);
			}
			else
				out += sep + text("RACES_OUT_OF_RANGE");
			if (!emp.masksDiplomacy())
				out += sep + empireRelationPct(emp);
		}
		return out;
	}
	default String empireRelationPct(Empire emp)	{
		EmpireView view = emp.viewForEmpire(player());
		int relation = (int) (0.5f + view.embassy().relations());
		return "Relation = " + relation + "%";
	}
	default String empireTreaty(Empire emp)	{
		EmpireView view = emp.viewForEmpire(player());
		DiplomaticTreaty treaty = view.embassy().treaty();
		String out = treaty.status(player());
		boolean isAlly = treaty.isAlliance();
		if (isAlly) {
			TreatyAlliance alliance = (TreatyAlliance) treaty;
			int standing = alliance.standing(player());
			out += "level " + standing;
		}
		return out;
	}
	default String empireTrade(Empire emp)	{
		EmpireView view = emp.viewForEmpire(player());
		int level = view.trade().level();
		if (level == 0)
			return text("RACES_TRADE_NONE");
		else
			return text("RACES_TRADE_LEVEL", level);
	}


}
