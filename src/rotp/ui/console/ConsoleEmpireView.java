package rotp.ui.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rotp.model.empires.DiplomaticEmbassy;
import rotp.model.empires.DiplomaticTreaty;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.empires.Leader;
import rotp.model.empires.SpyNetwork;
import rotp.model.empires.TreatyAlliance;
import rotp.model.game.GovernorOptions;
import rotp.model.incidents.DiplomaticIncident;
import rotp.model.tech.Tech;
import rotp.model.tech.TechCategory;
import rotp.model.tech.TechTree;

public class ConsoleEmpireView implements IConsole {
	private static final String INCIDENT_SEP = ", ";
	private static final String EMPIRE_SEP	 = ", ";
	private static final String OBSOLETE	 = "~ ";
	private static final String COULD_STEAL	 = "*** ";
	
	String contactInfo(Empire empire, String out)		{ return out + empireContactInfo(empire, NEWLINE); }
	String diplomacyInfo(Empire empire, String out)		{
		if (empire.isPlayer()) {
			out += empireName(empire);
			out += NEWLINE + playerDiploBaseInfo(empire);
			out += NEWLINE + playerDiplomacyBureau(empire);
			out += NEWLINE + playerCounterIntelligenceReport(empire);
			out += NEWLINE + playerIntelligenceReport(empire);
			out += NEWLINE + playerDiplomaticEvents(empire);
		}
		else {
			out += empireName(empire);
			out += NEWLINE + aiDiploBaseInfo(empire);
			out += NEWLINE + relationsMeter(empire);
			out += NEWLINE + aiDiplomacyBureau(empire);
			out += NEWLINE + aiTradeSummary(empire);
			out += NEWLINE + aiForeignRelations(empire);
			out += NEWLINE + aiDiplomaticEvents(empire);
		}
		return out;
	}
	String intelligenceInfo(Empire empire, String out)	{
		if (empire.isPlayer()) {
			out += empireName(empire);
			out += NEWLINE + playerCounterIntelligenceReport(empire);
			out += NEWLINE + playerIntelligenceReport(empire);
			out += NEWLINE + playerTechnologyList(empire);
		}
		else {
			out += empireName(empire);
			out += NEWLINE + aiIntelligenceReport(empire);
			out += NEWLINE + aiSpyOrders(empire);			
			out += NEWLINE + aiTechnologyList(empire);
		}
		return out;
	}
	String militaryInfo(Empire empire, String out)		{
		if (empire.isPlayer()) {
			out += empireName(empire);
		}
		else {
			out += empireName(empire);
		}
		return out;
	}
	String statusInfo(Empire empire, String out)		{
		if (empire.isPlayer()) {
			out += empireName(empire);
		}
		else {
			out += empireName(empire);
		}
		return out;
	}
	
	// DIPLOMATIC PANEL
	private String playerDiploBaseInfo(Empire empire)				{
		String out = text("RACES_DIPLOMACY_HOMEWORLD");
		out += EQUAL_SEP + empire.sv.name(empire.capitalSysId());

		out += NEWLINE + text("RACES_DIPLOMACY_LEADER");
		String s = text("TITLE_LEADERNAME", empire.labels().text("_nameTitle"), empire.leader().name());
		s = empire.replaceTokens(s, "alien");
		out += EQUAL_SEP + s;

		out += NEWLINE + text("RACES_DIPLOMACY_CURRENT_TRADE");
		int amt = (int) empire.totalTradeIncome();
		s = text("RACES_DIPLOMACY_TRADE_AMT", str(amt));
		out += EQUAL_SEP + s;

		out += NEWLINE + text("RACES_DIPLOMACY_TOTAL_TRADE");
		amt = empire.totalTradeTreaties();
		s = text("RACES_DIPLOMACY_TRADE_AMT", str(amt));
		out += EQUAL_SEP + s;
		return out;
	}
	private String playerDiplomaticEvents(Empire empire)			{
		String out = "";
		boolean displayYear = options().displayYear();
		if (displayYear)
			out += text("RACES_DIPLOMACY_EVENT_YEAR");
		else
			out += text("RACES_DIPLOMACY_EVENT_TURN");
		out += INCIDENT_SEP + text("RACES_DIPLOMACY_EVENT_RACE");
		out += INCIDENT_SEP + text("RACES_DIPLOMACY_EVENT_EFFECT");
		out += INCIDENT_SEP + text("RACES_DIPLOMACY_EVENT_TITLE");

		List<DiplomaticIncident> incidents = new ArrayList<>();
		HashMap<DiplomaticIncident, Empire> incidentMap = new HashMap<>();
		for (EmpireView view: empire.contacts()) {
			for (DiplomaticIncident inc : view.otherView().embassy().allIncidents()) {
				if ((inc.currentSeverity() != 0) && inc.triggeredByAction()) {
					incidents.add(inc);
					incidentMap.put(inc, view.empire());
				}
			}
		}
		Collections.sort(incidents, DiplomaticIncident.DATE);
		for (int i=0;i<incidents.size();i++) {
			DiplomaticIncident inc = incidents.get(i);
			Empire otherEmpire = incidentMap.get(inc);
			String year = displayYear ? str(inc.dateOccurred()) :  str(inc.turnOccurred());
			out += NEWLINE + year;
			out += INCIDENT_SEP + otherEmpire.raceName();
			if (otherEmpire.masksDiplomacy())
				out += INCIDENT_SEP + "hidden";
			else
				out += INCIDENT_SEP + fmt(inc.currentSeverity(),1);
			out += INCIDENT_SEP + inc.title() + ": " + inc.description();
		}
		return out;
	}
	private String playerDiplomacyBureau(Empire empire)				{
		String out = text("RACES_DIPLOMACY_BUREAU");
		List<EmpireView> views = empire.contacts();
		int recalls = 0;
		for (EmpireView v: views) {
			if (v.embassy().diplomatGone())
				recalls++;
		}
		out += NEWLINE + text("RACES_DIPLOMACY_KNOWN_EMPIRES");
		out += EQUAL_SEP + views.size();
		out += NEWLINE + text("RACES_DIPLOMACY_RECALLED_DIPLOMAT");
		out += EQUAL_SEP + recalls;
		return out;
	}
	private String empireName(Empire empire)						{ return "Empire Name" + EQUAL_SEP + empire.name(); }
	private String aiDiploBaseInfo(Empire empire)					{
		Leader leader = empire.leader();
		String out = text("RACES_DIPLOMACY_HOMEWORLD");
		out += EQUAL_SEP + empire.sv.name(empire.capitalSysId());

		out += NEWLINE + text("RACES_DIPLOMACY_LEADER");
		String str = text("TITLE_LEADERNAME", empire.labels().text("_nameTitle"), leader.name());
		str = empire.replaceTokens(str, "alien");
		out += EQUAL_SEP + str;

		out += NEWLINE + text("RACES_DIPLOMACY_CHARACTER");
		str = text("LEADER_PERSONALITY_FORMAT", leader.personality(), leader.objective());
		out += EQUAL_SEP + str;

		out += NEWLINE + text("RACES_DIPLOMACY_STATUS");
		EmpireView view = player().viewForEmpire(empire);
		DiplomaticTreaty treaty = view.embassy().treaty();
		str = treaty.status(player());
		if (treaty.isAlliance()) {
			TreatyAlliance alliance = (TreatyAlliance) treaty;
			int standing = alliance.standing(player());
			str += " level " + standing;
		}
		if (treaty.isPeace() && options().isColdWarMode())
			str += " " + text("RACES_COLD_WAR");	
		out += EQUAL_SEP + str;
		return out;
	}
	private String relationsMeter(Empire empire)					{
		if (empire.masksDiplomacy()) {
			String out = text("RACES_DIPLOMACY_RELATIONS_METER");
			String str = text("RACES_DIPLOMACY_RELATIONS_UNKNOWN");
			str = empire.replaceTokens(str, "alien");
			out += EQUAL_SEP + str;
			return out;
		}
		else
			return empireRelationPct(empire);
	}
	private String aiDiplomaticEvents(Empire empire)				{
		String out = "Diplomatic event history";
		boolean displayYear = options().displayYear();
		boolean masksDiplomacy = empire.masksDiplomacy();
		if (displayYear)
			out += NEWLINE + text("RACES_DIPLOMACY_EVENT_YEAR");
		else
			out += NEWLINE + text("RACES_DIPLOMACY_EVENT_TURN");
		out += INCIDENT_SEP + text("RACES_DIPLOMACY_EVENT_EFFECT");
		if (masksDiplomacy)
			out += EQUAL_SEP + "hidden";
		out += INCIDENT_SEP + text("RACES_DIPLOMACY_EVENT_TITLE");

		List<DiplomaticIncident> incidents = new ArrayList<>();
		EmpireView view = player().viewForEmpire(empire);
		for (DiplomaticIncident inc : view.otherView().embassy().allIncidents()) {
			if (inc.currentSeverity() != 0)
				incidents.add(inc);
		}
		Collections.sort(incidents, DiplomaticIncident.DATE);
		for (int i=0;i<incidents.size();i++) {
			DiplomaticIncident inc = incidents.get(i);
			String year = displayYear ? str(inc.dateOccurred()) :  str(inc.turnOccurred());
			out += NEWLINE + year;
			if (!masksDiplomacy)
				out += INCIDENT_SEP + fmt(inc.currentSeverity(),1);
			out += INCIDENT_SEP + inc.title() + ": " + inc.description();
		}
		return out;
	}
	private String aiDiplomacyBureau(Empire empire)					{
		String out = text("RACES_DIPLOMACY_BUREAU");
		EmpireView view = player().viewForEmpire(empire);
		boolean finalWar = view.embassy().finalWar();
		boolean outOfRange = !view.inEconomicRange();
		String desc;
		if (finalWar)
			desc = text("RACES_DIPLOMACY_BUREAU_DESC_FINAL");
		else if (outOfRange)
			desc = text("RACES_DIPLOMACY_BUREAU_DESC_RANGE");
		else {
			desc = text("RACES_DIPLOMACY_BUREAU_DESC");
			desc = empire.replaceTokens(desc, "alien");
		}
		out += NEWLINE + desc;
		return out;
	}
	private String aiTradeSummary(Empire empire)					{
		String out = text("RACES_DIPLOMACY_TRADE_SUMMARY");
		EmpireView view = player().viewForEmpire(empire);
		boolean finalWar = view.embassy().finalWar();
		boolean outOfRange = !view.inEconomicRange();
		String desc;
		if (finalWar)
			desc = text("RACES_DIPLOMACY_TRADE_DESC_FINAL");
		else if (outOfRange)
			desc = text("RACES_DIPLOMACY_TRADE_DESC_RANGE");
		else {
			desc = text("RACES_DIPLOMACY_TRADE_DESC");
			desc = empire.replaceTokens(desc, "alien");
		}
		out += NEWLINE + desc;
		out += NEWLINE + text("RACES_DIPLOMACY_TRADE_TREATY");
		out += EQUAL_SEP + text("RACES_DIPLOMACY_TRADE_AMT", view.trade().level());
		out += NEWLINE + text("RACES_DIPLOMACY_CURRENT_TRADE");
		int amt = (int) view.trade().profit();
		out += EQUAL_SEP + text("RACES_DIPLOMACY_TRADE_AMT", str(amt));
		return out;
	}
	private String aiForeignRelations(Empire empire)				{
		String out = text("RACES_DIPLOMACY_FOREIGN_RELATIONS");
		EmpireView view = player().viewForEmpire(empire);
		boolean outOfRange = !view.inEconomicRange();
		String desc;
		if (outOfRange)
			desc = text("RACES_DIPLOMACY_FOREIGN_RANGE");
		else
			desc = text("RACES_DIPLOMACY_FOREIGN_DESC");
		desc = empire.replaceTokens(desc, "alien");
		out += NEWLINE + desc;
		if (outOfRange)
			return out;
		List<EmpireView> contacts = empire.contacts();
		Collections.sort(contacts, EmpireView.PLAYER_LIST_ORDER);
		for (EmpireView contact: contacts) {
			if (contact.inEconomicRange()) {
				out += NEWLINE + contact.empire().raceName();
				DiplomaticTreaty treaty = contact.embassy().treaty();
				String str = treaty.status(player());
				if (treaty.isPeace() && options().isColdWarMode())
					str += " " + text("RACES_COLD_WAR");
				out += EQUAL_SEP + str;
			}
		}
		return out;
	}

	// INTELLIGENCE PANEL
	private String playerCounterIntelligenceReport(Empire empire)	{
		String out = text("RACES_DIPLOMACY_COUNTER_BUREAU");
		out += NEWLINE + text("RACES_INTEL_SECURITY_BONUS");
		int amt = (int) (100*player().totalInternalSecurityPct());
		if (amt == 0)
			out += EQUAL_SEP + text("RACES_INTEL_SECURITY_BONUS_NONE");
		else
			out += text("RACES_INTEL_SECURITY_BONUS_AMT", str(amt));
		out += NEWLINE + text("RACES_INTEL_TOTAL_SPENDING");
		amt = (int) empire.empireInternalSecurityCost();
		out += EQUAL_SEP + text("RACES_INTEL_SPENDING_ANNUAL", str(amt));
		out += NEWLINE + text("RACES_INTEL_SECURITY_TAX");
		out += EQUAL_SEP + text("RACES_INTEL_PERCENT_AMT",(int)(empire.internalSecurityCostPct()*100));
		out += NEWLINE + text("RACES_DIPLOMACY_COUNTER_DESC2");
		return out;
	}
	private String playerIntelligenceReport(Empire empire)			{
		String out = text("RACES_INTEL_BUREAU_DESC");
		out += NEWLINE + text("RACES_INTEL_BUREAU_SPIES");
		out += EQUAL_SEP + empire.totalActiveSpies();
		out += NEWLINE + text("RACES_INTEL_BUREAU_SPENDING");
		int amt = (int) empire.empireExternalSpyingCost();
		if (amt == 0)
			out += EQUAL_SEP + text("RACES_INTEL_SECURITY_BONUS_NONE");
		else
			out += EQUAL_SEP + text("RACES_INTEL_SPENDING_ANNUAL", str(amt));
		return out;
	}
	private String playerTechnologyList(Empire empire)				{
		String str = text("RACES_INTEL_UNKNOWN_TECHNOLOGY");
		str = empire.replaceTokens(str, "alien");
		String out = str;
		str = text("RACES_INTEL_UNKNOWN_TECH_DESC");
		str = empire.replaceTokens(str, "alien");
		out += NEWLINE + str;

		TechTree tree = empire.tech();
		Empire player = player();
		HashMap<Integer, List<String>> unknownTechs	= new HashMap<>();
		HashMap<Integer, List<String>> knownTechs	= new HashMap<>();
		HashMap<String,  List<Empire>> techOwners	= new HashMap<>();
		loadAllUnknownTechs(unknownTechs, knownTechs, techOwners);
		for (int id=0; id<TechTree.NUM_CATEGORIES; id++ ) {
			TechCategory cat = tree.category(id);
			out += NEWLINE + text(cat.id());
			List<String> aiUnknown = unknownTechs.get(cat.index());
			for (String idxStr: aiUnknown) {
				Tech t = tech(idxStr);
				if ((t.level() > 0) && !t.free) {
					List<Empire> emps = techOwners.get(idxStr);
					boolean couldSteal = false;
					for (Empire emp1: emps) {
						if(player.viewForEmpire(emp1).spies().possibleTechs().contains(idxStr))
							couldSteal = true;
					}
					String annotation = "";
					if(player.tech(idxStr).isObsolete(player))
						annotation = OBSOLETE;
					else if(couldSteal)
						annotation = COULD_STEAL;
					out += NEWLINE + annotation + tech(idxStr).name() + " ";
					String sep = "";
					for (Empire emp1: emps) {
						out += sep + emp1.raceName();
						sep = EMPIRE_SEP;
					}
				}
			}
		}
		return out;
	}
	private void loadAllUnknownTechs(
			HashMap<Integer, List<String>> unknownTechs,
			HashMap<Integer, List<String>> knownTechs,
			HashMap<String,  List<Empire>> techOwners) 				{

		for (int i=0;i<TechTree.NUM_CATEGORIES;i++) {
			knownTechs.put(i, new ArrayList<>());
			unknownTechs.put(i, new ArrayList<>());
		}
		Empire pl = player();
		TechTree plTree = pl.tech();
		List<String> tradedTechs = new ArrayList<>();
		for (String techId: pl.tech().tradedTechs())
			tradedTechs.add(techId);
		List<Empire> empires = pl.contactedEmpires();
		for (Empire emp: empires) {
			TechTree empTree = pl.viewForEmpire(emp).spies().tech();
			for (int i=0;i<TechTree.NUM_CATEGORIES;i++) {
				List<String> aiTechs = new ArrayList<>(empTree.category(i).knownTechs());
				List<String> plTechs = new ArrayList<>(plTree.category(i).knownTechs());
				aiTechs.removeAll(plTechs);
				for (String id : aiTechs) {
					List<String> currUnknowns = unknownTechs.get(i);
					if (!currUnknowns.contains(id) && !tradedTechs.contains(id))
						currUnknowns.add(id);
					if (!techOwners.containsKey(id))
						techOwners.put(id, new ArrayList<>());
					techOwners.get(id).add(emp);
				}
			}
		}
	}
	private void loadAllTechs(Empire empire,
			HashMap<Integer, List<String>> unknownTechs,
			HashMap<Integer, List<String>> knownTechs,
			HashMap<String,  List<Empire>> techOwners) 				{
		Empire player = player();
		
		TechTree empTree = player.viewForEmpire(empire).spies().tech();
		List<String> tradedTechs = new ArrayList<>();
		for (String techId: player.tech().tradedTechs())
			tradedTechs.add(techId);
		TechTree plTree = player.tech();
		for (int i=0;i<TechTree.NUM_CATEGORIES;i++) {
			List<String> aiKnown = new ArrayList<>(empTree.category(i).knownTechs());
			List<String> playerKnown = new ArrayList<>(plTree.category(i).knownTechs());
			List<String> aiUnknown = new ArrayList<>();
			for (String id : aiKnown) {
				if (!playerKnown.contains(id) && !tradedTechs.contains(id))
					aiUnknown.add(id);
			}
			aiKnown.removeAll(aiUnknown);
			Collections.sort(aiUnknown, Tech.REVERSE_LEVEL);
			Collections.sort(aiKnown, Tech.REVERSE_LEVEL);
			knownTechs.put(i, aiKnown);
			unknownTechs.put(i, aiUnknown);
		}
	}
	private String aiTechnologyList(Empire empire)					{
		String str = text("RACES_INTEL_KNOWN_TECHNOLOGY");
		str = empire.replaceTokens(str, "alien");
		String out = str;
		str = text("RACES_INTEL_KNOWN_TECH_DESC");
		str = empire.replaceTokens(str, "alien");
		out += NEWLINE + str;
		TechTree tree = empire.tech();
		Empire player = player();
		List<String> possibleTech = player.viewForEmpire(empire).spies().possibleTechs();
		HashMap<Integer, List<String>> unknownTechs	= new HashMap<>();
		HashMap<Integer, List<String>> knownTechs	= new HashMap<>();
		HashMap<String,  List<Empire>> techOwners	= new HashMap<>();
		loadAllTechs(empire, unknownTechs, knownTechs, techOwners);
		for (int id=0; id<TechTree.NUM_CATEGORIES; id++ ) {
			TechCategory cat = tree.category(id);
			String catStr = text(cat.id());
			out += NEWLINE + text(cat.id());
			List<String> aiUnknown = unknownTechs.get(cat.index());
			String unknownStr = "";
			if (!aiUnknown.isEmpty()) {
				for (String idxStr: aiUnknown) {
					Tech t = tech(idxStr);
					if ((t.level() > 0) && !t.free) {
						String annotation = "";
						if(player.tech(idxStr).isObsolete(player))
							annotation = OBSOLETE;
						else if(possibleTech.contains(idxStr))
							annotation = COULD_STEAL;
						unknownStr += NEWLINE + annotation + tech(idxStr).name();
					}
				}
			}
			if (!unknownStr.isEmpty())
				out += NEWLINE + catStr + " Unknown tech" + EQUAL_SEP + unknownStr;

			List<String> aiKnown = knownTechs.get(cat.index());
			String knownStr = "";
			if (!aiKnown.isEmpty()) {
				for (String idxStr: aiKnown) {
					Tech t = tech(idxStr);
					if ((t.level() > 0) && !t.free)
						knownStr += NEWLINE + tech(idxStr).name();
				}
			}
			if (!knownStr.isEmpty())
				out += NEWLINE + catStr + " Known tech" + EQUAL_SEP + knownStr;
		}
		return out;
	}
	private String aiIntelligenceReport(Empire empire)				{
		String title = text("RACES_INTEL_TITLE");
		String out	 = empire.replaceTokens(title, "alien");
		EmpireView view = player().viewForEmpire(empire);
		SpyNetwork spies = view.spies();
		GovernorOptions govOptions = govOptions();
		boolean timerIsActive	= view.timerIsActive();
		boolean isAutoSpy		= govOptions.isAutoSpy();
		boolean isAutoInfiltrate = govOptions.isAutoInfiltrate();
		boolean respectPromises  = govOptions.respectPromises();
		boolean govOn	= respectPromises && (isAutoSpy || isAutoInfiltrate);
		boolean showGov	= timerIsActive && govOn;
		float	spyMod	= player().spySpendingModifier();
		
		out += NEWLINE + text("RACES_INTEL_REPORT_AGE");
		int age = spies.reportAge();
		if (age < 0)
			out += EQUAL_SEP + text("RACES_INTEL_NO_DATA");
		else if (age == 0)
			out += EQUAL_SEP + text("RACES_INTEL_CURRENT");
		else
			out += EQUAL_SEP + text("RACES_INTEL_YEARS", str(age));

		out += NEWLINE + text("RACES_INTEL_SPY_NETWORK");
		int num = spies.numActiveSpies();
		int max = spies.maxSpies();
		out += EQUAL_SEP + text("RACES_INTEL_SPIES", str(num), str(max));
		
		// Security spending
		if (!view.embassy().unity()) {
			String newSpies = spies.newSpiesExpected();
			out += NEWLINE + text("RACES_INTEL_SPENDING");
			float spyingCost = player().totalTaxablePlanetaryProduction()
					* spies.allocationCostPct() * view.owner().spySpendingModifier();
			out += EQUAL_SEP + text("RACES_INTEL_SPENDING_ANNUAL", (int) spyingCost);
			out += SPACER +"Expected new spies" + EQUAL_SEP + newSpies;
		}

		// Governor instructions
		if (showGov) {
			out += NEWLINE + text("RACES_INTEL_GOVERNOR_INSTRUCTIONS");
			switch (spies.lastSpyThreatReply()) {
				case 0:
					out += EQUAL_SEP + text("RACES_INTEL_GOVERNOR_IGNORE");
					break;
				case 1:
					out += EQUAL_SEP + text("RACES_INTEL_GOVERNOR_HIDE");
					break;
				default:
					out += EQUAL_SEP + text("RACES_INTEL_GOVERNOR_SHUTDOWN");
			};
		}

		// Description
		if (view.embassy().unity())
			out += NEWLINE + text("RACES_INTEL_SPENDING_DESC_UNITY");
		else if (!view.inEconomicRange())
			out += NEWLINE + text("RACES_INTEL_SPENDING_RANGE");
		else if (spyMod < 1) {
			int spending = (int) (Math.ceil((1-spyMod)*100));
			out += NEWLINE + text("RACES_INTEL_SPENDING_CAPPED", spending);
		}
		else
			out += NEWLINE + text("RACES_INTEL_SPENDING_DESC");

		return out;
	}
	private String aiSpyOrders(Empire empire)						{
		Empire player = player();
		EmpireView view = player.viewForEmpire(empire);
		// no spy orders for new republic allies
		if (view.embassy().unity())
			return "";

		SpyNetwork spies = view.spies();
		boolean treatyBreak = false;
		boolean triggerWar = false;
		if (spies.maxSpies() > 0) {
			if (!spies.isHide() && player.alliedWith(empire.id))
				treatyBreak = true;
			else if (spies.isSabotage() && player.pactWith(empire.id))
				treatyBreak = true;  
		}
		if (!view.embassy().anyWar() && (spies.maxSpies() > 0)
				&& view.otherView().embassy().timerIsActive(DiplomaticEmbassy.TIMER_SPY_WARNING)) {
			if (!spies.isHide() || (view.empire().leader().isXenophobic())) {
				triggerWar = true;
			}
		}
		String out = text("RACES_INTEL_SPY_ORDERS");
		out += EQUAL_SEP + view.spies().missionName();
		String desc = "";
		if (triggerWar) 
			desc = text("RACES_INTEL_SPY_WARNING_WAR");
		else if (treatyBreak)
			desc = text("RACES_INTEL_SPY_WARNING");
		if (!desc.isEmpty())
			out += NEWLINE + empire.replaceTokens(desc, "alien");

		return out;
	}
		
	// MILITARY PANEL

	// STATUS PANEL
}
