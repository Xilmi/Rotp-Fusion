package rotp.ui.console;

import rotp.model.empires.Empire;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;

public class FleetView implements IConsole {
	private final CommandConsole console;
	private Empire pl, empire;
	private StarSystem dest;
	private ShipFleet  fl;
	private String nebulaText;
	// private boolean isPlayer, contact;
	
	// ##### CONSTRUCTOR #####
	FleetView(CommandConsole parent)	{ console = parent; }

	// ##### Systems Report
	String getInfo(int f, String out)	{
		pl	= player();
		fl	= console.getFleet(f);
		if (fl.isEmpty())
			return out;

		if (fl.isOrbiting()) {
			dest = null;
		}
		else {
			dest = fl.destination();
		}
		empire 	= fl.empire();
		
		// Top Box
		out += longEmpireInfo(empire) + " Fleet" + NEWLINE;
		out += bracketed(FLEET_KEY, f) + " ";
		out = topBox(out);
		out = infoBox(out + NEWLINE);
		return out;
	}
	// ##### SUB BOXES
	private String topBox(String out)	{
		// draw orbiting data, bottom up
		if (fl.launched() || (fl.deployed() && !pl.knowETA(fl))) {
			if (pl.knowETA(fl) && (fl.hasDestination())) {
				String dest =  planetName(fl.destination().altId);
				String str2 = dest.isEmpty() ? text("MAIN_FLEET_DEST_UNSCOUTED") : text("MAIN_FLEET_DESTINATION", dest);
				out += str2;
			}
			String str3 = fl.retreating() ? text("MAIN_FLEET_RETREATING") : text("MAIN_FLEET_IN_TRANSIT");
			out += NEWLINE + str3;
			if (!fl.empire().isPlayer()) {
				if (pl.alliedWith(fl.empId)) {
					String str4 = text("MAIN_FLEET_ALLY");
					out += str4;
				} else if (pl.atWarWith(fl.empId)) {
					String str4 = text("MAIN_FLEET_ENEMY");
					out += "!" + str4;
				}
			}
		}
		else if (fl.deployed()) {
			String dest =  planetName(fl.destination().altId);
			String str2 = dest.isEmpty() ? text("MAIN_FLEET_DEST_UNSCOUTED") : text("MAIN_FLEET_DESTINATION", dest);
			out += str2;
			StarSystem sys1 = fl.system();
			String str3 = text("MAIN_FLEET_ORIGIN", planetName(sys1.altId));
			out += str3;
			String str4 = fl.retreating() && fl.empire().isPlayer() ? text("MAIN_FLEET_RETREATING") :text("MAIN_FLEET_DEPLOYED");
			out += str4;
		}
		else {
			StarSystem sys1 = fl.system();
			if (sys1 == null)
				out += NEWLINE + "ERROR: No system assigned to fleet ";
			else {
				String str2 = text("MAIN_FLEET_LOCATION", planetName(sys1.altId));
				String str3 = text("MAIN_FLEET_IN_ORBIT");
				out += str3 + " ";
				out += str2;
			}
		}
		return out;
	}
	private String infoBox(String out)	{
		if (fl.canBeSentBy(player()))
			out += "Deployable ship list";
		else
			out += "Ship list" + NEWLINE;
		String text	= null;
		nebulaText	= null;
		String retreatText	= null;
		String rallyText	= null;
		if (fl.canBeSentBy(player())) {
			if (!fl.canSendTo(id(dest))) {
				if (dest == null) {
					StarSystem currDest = fl.destination();
					if (currDest == null)
						text = "";
					else {
						if (fl.empire().isPlayer()) {
							retreatText = text("MAIN_FLEET_AUTO_RETREAT");
							rallyText = text("MAIN_FLEET_SET_RALLY");
						}
						int dist = fl.travelTurnsAdjusted(currDest);
						String destName = player().sv.name(currDest.id);
						if (destName.isEmpty())
							text = text("MAIN_FLEET_ETA_UNNAMED", dist);
						else
							text = text("MAIN_FLEET_ETA_NAMED", destName, dist);  
					}
				}
				else {
					String name = player().sv.name(dest.id);
					if (name.isEmpty())
						text = text("MAIN_FLEET_INVALID_DESTINATION2");
					else 
						text = text("MAIN_FLEET_INVALID_DESTINATION", name);
				}
			}
			else if (fl.isDeployed() || fl.isInTransit()) {
				if (fl.empire().isPlayer()) {
					retreatText = text("MAIN_FLEET_AUTO_RETREAT");
					rallyText = text("MAIN_FLEET_SET_RALLY");
				}
				dest = dest == null ? fl.destination() : dest;
				int dist = fl.travelTurnsAdjusted(dest);
				String destName = player().sv.name(dest.id);
				if (destName.isEmpty())
					text = text("MAIN_FLEET_ETA_UNNAMED", dist);
				else
					text = text("MAIN_FLEET_ETA_NAMED", destName, dist);
				if ((dist > 1) && fl.passesThroughNebula(dest))
					nebulaText = text("MAIN_FLEET_THROUGH_NEBULA");
			}
			else if (fl.canSendTo(id(dest))) {
				int dist = 0;
				if (fl.canReach(dest)) {
					dist = fl.travelTurnsAdjusted(dest);
					String destName = player().sv.name(dest.id);
					if (destName.isEmpty())
						text = text("MAIN_FLEET_ETA_UNNAMED", dist);
					else
						text = text("MAIN_FLEET_ETA_NAMED", destName, dist);
				}
				else {
					dist = player().rangeTo(dest);
					text = text("MAIN_FLEET_OUT_OF_RANGE_DESC", dist);
				}
				if ((dist > 1) && fl.passesThroughNebula(dest))
					nebulaText = text("MAIN_FLEET_THROUGH_NEBULA");
			}
			else if (fl.isOrbiting()) {
				text = text("MAIN_FLEET_CHOOSE_DEST");
			}
		}
		else if (fl.isInTransit() || fl.isDeployed()) {
			if (fl.empire().isPlayer()) {
				retreatText = text("MAIN_FLEET_AUTO_RETREAT");
				rallyText = text("MAIN_FLEET_SET_RALLY");
			}
			if (player().knowETA(fl)) {
				int dist = fl.travelTurnsRemainingAdjusted();
				if (fl.hasDestination()) {
					String destName = player().sv.name(fl.destSysId());
					if (destName.isEmpty())
						text = text("MAIN_FLEET_ETA_UNNAMED", dist);
					else
						text = text("MAIN_FLEET_ETA_NAMED", destName, dist);
				}
			}
			else {
				text = text("MAIN_FLEET_ETA_UNKNOWN");
			}
		}
		out = fleetBox(out);
		if (text != null)
			out += NEWLINE + text;

		if (rallyText != null)
			if (fl.isRallied())
				out += NEWLINE + rallyText;
		if (retreatText != null)
			if (fl.retreatOnArrival())
				out += NEWLINE + retreatText;
		return out;
	}
	private String fleetBox(String out)	{
		out += fleetDesignInfo(fl, NEWLINE);
		if (nebulaText != null)
			out += NEWLINE + nebulaText;
		return out;
	}
}
