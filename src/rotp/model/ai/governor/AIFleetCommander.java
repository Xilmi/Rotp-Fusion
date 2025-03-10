package rotp.model.ai.governor;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import rotp.model.ai.governor.ParamFleetAuto.SubFleet;
import rotp.model.ai.governor.ParamFleetAuto.SubFleetList;
import rotp.model.ai.interfaces.FleetCommander;
import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GovernorOptions;
import rotp.model.game.IGovOptions;
import rotp.util.Base;

// BR: Moved Governor Ship automation from empire to here
public class AIFleetCommander implements Base, FleetCommander {
	private static final float MAX_ALLOWED_SHIP_MAINT = 0.35f;
	private final Empire empire;
	public AIFleetCommander(Empire e)	{ empire = e; }
	@Override public boolean inExpansionMode()	{
		for(EmpireView contact : empire.contacts())
			if(empire.inShipRange(contact.empId()))
				return false;
		if((empire.tech().planetology().techLevel() > 19 || empire.ignoresPlanetEnvironment())
			&& empire.shipLab().needScouts == false)
			return false;
		return true;
	}
	@Override public void nextTurn()	{
		autocolonize();
		autoattack();
		autoscout();
	}
	@Override public float transportPriority(StarSystem sv){
			int id = sv.id;
			float pr = sv.colony().transportPriority();

			if (empire.sv.colony(id).inRebellion())
				return pr * 5;
			else if (empire.sv.isBorderSystem(id))
				return pr * 2;
			else if (empire.sv.isInnerSystem(id))
				return pr / 2;
			else
				return pr;
	}
	@Override public float maxShipMaintainance()	{ return MAX_ALLOWED_SHIP_MAINT; }

	/**
	 * Sort targets to by value and to be as close to source system as possible
	 */
	private interface SystemsSorter	{
		void sort(Integer sourceSystem, List<Integer> targets, int warpSpeed);
	}
	/**
	 * Sort fleets by value and to be as close to target system as possible
	 */
	private class ColonizePriority implements SystemsSorter	{
		@SuppressWarnings("unused")
		private final String message;
		public ColonizePriority(String message)	{
			this.message = message;
		}
		@Override public void sort(Integer sourceSystem, List<Integer> targets, int warpSpeed)	{
			// ok, let's use both distance and value of planet to prioritize colonization, 50% and 50%
			StarSystem source = empire.sv.system(sourceSystem);

			float maxTravelTime = -1;
			float maxValue = -1;
			for (int sid : targets) {
				float value = planetValue(sid);
				if (maxValue < 0 || maxValue < value)
					maxValue = value;

				float distance = source.travelTimeTo(empire.sv.system(sid), warpSpeed);
				if (maxTravelTime < 0 || maxTravelTime < distance)
					maxTravelTime = distance;
			}
			// could happen if we only have 1 colony ship already orbiting the only remaining colonizable planet
			if (Math.abs(maxTravelTime) <= 0.1)
				maxTravelTime = 1;
			//System.out.println(message+" maxDistance =" + maxDistance + " maxValue=" + maxValue);

			float maxDistance1 = maxTravelTime;
			float maxValue1 = maxValue;
			// planets with lowest weight are most desirable (closest/best)
			targets.sort((s1, s2) -> (int) Math.signum(
					autocolonizeWeight(source, s1, maxDistance1, maxValue1, warpSpeed) -
					autocolonizeWeight(source, s2, maxDistance1, maxValue1, warpSpeed)));
			//for (int si : targets) {
			//	double weight = autocolonizeWeight(source, si, maxDistance, maxValue, warpSpeed);
			//	System.out.format(message+" System %d %s travel=%.1f value=%.1f weight=%.2f%n",
			//			si, empire.sv.name(si), source.travelTimeTo(empire.sv.system(si), warpSpeed),
			//			planetValue(si), weight);
			//}
		}
	}
	private double autocolonizeWeight(StarSystem source, int targetId, float maxDistance, float maxValue, int warpSpeed) {
		// let's flip value percent and sort by descending order. That's because in rare cases distancePercent could be
		// greater than 1
		float valuePercent = 1 - planetValue(targetId) / maxValue;
		float distancePercent = source.travelTimeTo(empire.sv.system(targetId), warpSpeed) / maxDistance;
		// so distance is worth 50% of weight, value 50%
		float distanceWeight = govOptions().colonyDistanceWeight();
		float valueWeight = 1 - distanceWeight;
		return valuePercent * valueWeight + distancePercent * distanceWeight;
		// return valuePercent * 0.5 + distancePercent * 0.5;
	}
	// taken from AIFleetCommander.setColonyFleetPlan()
	private float planetValue(int sid) {
		float value = empire.sv.currentSize(sid);

		//increase value by 5 for each of our systems it is near, and
		//decrease by 2 for alien systems. This is an attempt to encourage
		//colonization of inner colonies (easier to defend) even if they
		//are not as good as outer colonies
		int[] nearbySysIds = empire.sv.galaxy().system(sid).nearbySystems();
		for (int nearSysId : nearbySysIds) {
			int nearEmpId = empire.sv.empId(nearSysId);
			if (nearEmpId != Empire.NULL_ID) {
				if (nearEmpId == empire.id)
					value += 5;
				else
					value -= 2;
			}
		}
		// assume that we will terraform  the planet
		value += empire.tech().terraformAdj();
		//multiply *2 for artifacts, *3 for super-artifacts
		value *= (1 + empire.sv.artifactLevel(sid));
		if (empire.sv.isUltraRich(sid))
			value *= 3;
		else if (empire.sv.isRich(sid))
			value *= 2;
		else if (empire.sv.isPoor(sid))
			value /= 2;
		else if (empire.sv.isUltraPoor(sid))
			value /= 3;
		return value;
	}
	

	private List<Integer> filterTargets(Predicate<Integer> filterFunction)	{
		List<Integer> targets = new LinkedList<>();
		for (int i = 0; i < empire.sv.count(); ++i) {
			if (filterFunction.test(i)) {
				targets.add(i);
			}
		}
		return targets;
	}
	private Predicate<Integer> filterScoutTarget(boolean extendedRange, float maxArrivalTime) {
		return (sysId)  -> {
			// scout time only gets set for scouted systems, not ones we were forced to retreat from, don't use scout time
			if (empire.sv.view(sysId).scouted() || empire.sv.view(sysId).isGuarded())
				return false;
			boolean inRange;
			if (extendedRange)
				inRange = empire.sv.inScoutRange(sysId);
			else
				inRange = empire.sv.inShipRange(sysId);
			if (!inRange)
				return false;
			// don't send scouts to occupied planets
			if (empire.sv.view(sysId).empire() != null)
				return false;
			// ships already on route- no need to scout
			List<ShipFleet> ownFleets = empire.ownFleetsTargetingSystem(empire.sv.system(sysId));
			if (!ownFleets.isEmpty())
				for (ShipFleet fl : ownFleets)
					if (fl.arrivalTimeAdjusted() <= maxArrivalTime)
						return false;
			// ships already deployed to - no need to scout // BR: For redeployment
			ownFleets = empire.ownFleetsDeployedToSystem(empire.sv.system(sysId));
			if (!ownFleets.isEmpty())
				for (ShipFleet fl : ownFleets)
					if (fl.arrivalTimeAdjusted() <= maxArrivalTime)
						return false;
			if (empire.sv.view(sysId).orbitingFleets() != null && !empire.sv.view(sysId).orbitingFleets().isEmpty()) {
				//	System.out.println("System "+i+" "+empire.sv.descriptiveName(i)+" has ships in orbit");
				for (ShipFleet fleet: empire.sv.view(sysId).orbitingFleets()) {
					if (fleet != null) {
						// WTF, planet should be scouting if own ships are orbiting
						if (fleet.empId() == empire.id)
							return false;
						// if fleet isn't armed- ignore it
						if (!fleet.isArmed())
							continue;
						// if fleet belongs to allied/non-aggression pact empire- ignore it
						if (empire.pactWith(fleet.empId) || empire.alliedWith(fleet.empId))
							continue;
						// don't scout systems guarded by by armed enemy.
						// System.out.println("System "+i+" "+empire.sv.descriptiveName(i)+" has armed enemy ships in orbit");
						return false;
					}
				}
			}
			return true;
		};
	}
	private void autoscout()	{
		GovernorOptions gov = session().getGovernorOptions();
		if (!gov.isAutoScout())
			return;
		ParamFleetAuto rules = IGovOptions.fleetAutoScoutMode;
		boolean smart = gov.autoScoutSmart();
		int maxTime = gov.autoScoutMaxTime();

		if (smart && maxTime > 0) {
			SubFleetList subFleetList = filterFleets(rules, false);
			if (subFleetList.isEmpty())
				return;
			subFleetList.sortByWarpSpeed();
			if (!gov.autoScoutNearFirst()) {
				// Send to not already targeted nearby stars
				autoscout(rules, subFleetList, 9999, maxTime);
				if (subFleetList.isEmpty())
					return;
			}
			// Send to already targeted nearby star
			autoscout(rules, subFleetList, maxTime, maxTime);
		}

		// Send to all other
		SubFleetList subFleetList = filterFleets(rules, smart);
		if (subFleetList.isEmpty())
			return;
		subFleetList.sortByWarpSpeed();
		autoscout(rules, subFleetList, 9999, 9999);
	}
	private void autoscout(ParamFleetAuto rules, SubFleetList subFleetList, float inTurn, float goTurn)	{
		boolean extendedRange = subFleetList.hasExtendedRange();
		List<Integer> targets = filterTargets(filterScoutTarget(extendedRange, galaxy().currentTime() + inTurn));
		// No systems to scout
		if (targets.isEmpty())
			return;

		// shuffle toScout list. empire is to prevent colony ship with auto-scouting on from going directly to the habitable planet.
		// That's because AFAIK map generation sets one of the 2 nearby planets to be habitable, and it's always first one in the list
		// So if we keep default order, that's cheating
		Collections.shuffle(targets);

		SystemsSorter systemsSorter = (sourceSystem, targets1, warpSpeed) -> {
			StarSystem source = empire.sv.system(sourceSystem);
			targets1.sort((s1, s2) -> (int) Math.signum(
					source.travelTimeTo(empire.sv.system(s1), warpSpeed) -
					source.travelTimeTo(empire.sv.system(s2), warpSpeed)));
		};
		// don't send out armed scout ships when enemy fleet is incoming, hence the need for defend predicate
		autoSendShips(targets, systemsSorter, subFleetList, rules, goTurn);
	}
	private void autocolonize()	{
		GovernorOptions options = session().getGovernorOptions();
		if (!options.isAutoColonize())
			return;

		ParamFleetAuto rules = IGovOptions.fleetAutoColonizeMode;
		SubFleetList subFleetList = filterFleets(rules, false);
		if (subFleetList.isEmpty())
			return;
		subFleetList.sortForColonize();

		boolean extendedRange = subFleetList.hasExtendedRange();
		List<Integer> targets = filterTargets(sysId -> {
			// only colonize scouted systems, systems with planets, unguarded systems.
			// don't attempt to colonize systems already owned by someone
			// TODO: Exclude systems that have enemy fleets orbiting?
			if (!empire.sv.view(sysId).isColonized() && empire.sv.view(sysId).scouted() && empire.canColonize(sysId)
					&& !empire.sv.isGuarded(sysId) && empire.sv.view(sysId).empire() == null ) {

				// if we don't have tech or ships to colonize empire planet, ignore it.
				// Since 2.15, for a game with restricted colonization option, we have to check each design if it can colonize
				if (!empire.ignoresPlanetEnvironment()
						|| !empire.acceptedPlanetEnvironment(empire.sv.system(sysId).planet().type()))
					if (!(empire.canColonize(sysId)
							&& subFleetList.canColonize(empire.sv.system(sysId).planet().type())))
						return false;

				boolean inRange;
				if (extendedRange)
					inRange = empire.sv.inScoutRange(sysId);
				else
					inRange = empire.sv.inShipRange(sysId);
				if (!inRange)
					return false;
				// colony ship already on route- no need to send more
				for (ShipFleet sf: empire.ownFleetsTargetingSystem(empire.sv.system(sysId)))
					if (sf != null && sf.canColonizeSystem(empire.sv.system(sysId)))
						return false;
				return true;
			}
			else
				return false;
		});

		// No systems to colonize
		if (targets.isEmpty())
			return;

		//for (Integer i: targets) {
		//	System.out.println("ToColonize "+empire.sv.name(i) + " scouted="+empire.sv.view(i).scouted()+" extrange=" + empire.sv.inScoutRange(i) + " range=" + empire.sv.inShipRange(i) + " type"+empire.sv.planetType(i));
		//}
		// don't send out armed colony ships when enemy fleet is incoming, hence the need for defend predicate
		autoSendShips(targets, new ColonizePriority("toColonize"), subFleetList, rules, 999);
	}
	// similar to autocolonize. Send ships to enemy planets and systems with enemy ships in orbit
	private void autoattack()	{
		GovernorOptions options = session().getGovernorOptions();
		if (!options.isAutoAttack())
			return;

		ParamFleetAuto rules = IGovOptions.fleetAutoAttackMode;
		SubFleetList subFleetList = filterFleets(rules, false);
		if (subFleetList.isEmpty())
			return;
		subFleetList.sortForAttack();

		// TODO BR: Customize Empire target
		boolean extendedRange = subFleetList.hasExtendedRange();
		List<Integer> hostileEmpires = IGovOptions.autoAttackEmpire.targetEmpires(empire);

		List<Integer> targets = filterTargets(sysId -> {
			// consider both scouted and unscouted systems if they belong to the enemy
			boolean inRange;
			if (extendedRange)
				inRange = empire.sv.inScoutRange(sysId);
			else
				inRange = empire.sv.inShipRange(sysId);
			if (!inRange)
				return false;

			List<ShipFleet> fleets = empire.sv.orbitingFleets(sysId);
			if (fleets != null) {
				for (ShipFleet sf: fleets) {
					if (sf != null && sf.empire() == empire && sf.isArmed()) {
						// don't target planets which already have own armed fleets in orbit
						return false;
					}
				}
			}
			// armed ships already on route- no need to send more
			for (ShipFleet sf: empire.ownFleetsTargetingSystem(empire.sv.system(sysId)))
				if (rules.alreadyHasDesignsOnRoute(sf, sysId))
					return false; // attack fleet already on its way, don't send more

			if (empire.sv.empire(sysId) != null && hostileEmpires.contains(empire.sv.empire(sysId).id)) {
				//System.out.println("System "+empire.sv.name(i)+" belongs to enemy empire, targeting");
				return true;
			}
			// empire will send ships to own colonies that have enemy ships in orbit. I guess that's OK
			if (fleets != null) {
				for (ShipFleet sf: fleets) {
					if (sf != null && hostileEmpires.contains(sf.empId) && !sf.retreating()) {
						//System.out.println("System "+empire.sv.name(i)+" has enemy ships, targeting");
						return true;
					}
				}
			}
			return false;
		});

		// No systems to colonize
		if (targets.isEmpty())
			return;

		//for (Integer i: targets)
		//	System.out.println("ToAttack "+empire.sv.name(i) + " scouted="+empire.sv.view(i).scouted()+" extrange=" + empire.sv.inScoutRange(i) + " range=" + empire.sv.inShipRange(i));

		autoSendShips(targets, new ColonizePriority("toAttack"), subFleetList, rules, 999);
	}
	private SubFleetList filterFleets(ParamFleetAuto rules, boolean colonyOnly)	{
		SubFleetList subFleetList = rules.newSubFleetList(empire);
		List<ShipFleet> allFleets = galaxy().ships.notInTransitFleets(empire.id);
		for (ShipFleet fleet : allFleets) {
			if (fleet == null)
				continue;
			if (!fleet.isOrbiting() || !fleet.canSend())	// we only use idle (orbiting) fleets
				continue;
			if (colonyOnly && empire != fleet.system().empire())
				continue;
			subFleetList.add(fleet);
		}
		return subFleetList;
	}
	private boolean alreadyHasDesignsOrbiting(int sysId, ParamFleetAuto rules)	{
		// if target system already has a fitting ship in orbit, don't send new ships there
		ShipFleet orbitingFleet = empire.sv.system(sysId).orbitingFleetForEmpire(empire);
		if (orbitingFleet != null)
			return rules.alreadyHasDesignsOrbiting(orbitingFleet, sysId);
		return false;
	}
	private void autoSendShips(List<Integer> targets,
							  SystemsSorter systemsSorter,
							  SubFleetList subFleetList,
							  ParamFleetAuto rules,
							  float maxTravelTime)	{

		if (subFleetList.isEmpty())
			return;

		if (targets.size() > subFleetList.size()) {
			// System.out.println("MORE TARGET SYSTEMS THAN SHIPS");
			// we have more stars to explore than we have ships, so
			// we take ships and send them to closest systems.
			for (Iterator<SubFleet> iFleet = subFleetList.iterator(); iFleet.hasNext(); ) {
				SubFleet subFleet = iFleet.next();
				if (targets.isEmpty())
					break;

				if (subFleet != null) {
					// don't send same fleet to multiple destinations by mistake
					if (!subFleet.fleet().isOrbiting())
						continue;
					// System.out.println("Deploying ships from Fleet " + fleet + " " + fleet.system().name());
					int warpSpeed = subFleet.warpSpeed();
					systemsSorter.sort(subFleet.fleet().sysId(), targets, warpSpeed);

					for (Iterator<Integer> iTarget = targets.iterator(); iTarget.hasNext(); ) {
						int sysId = iTarget.next();
						if (alreadyHasDesignsOrbiting(sysId, rules) || !rules.fitForSystem(subFleet, sysId))
							continue;
						float travelTime = subFleet.fleet().travelTimeTo(empire.sv.system(sysId), warpSpeed);
						if (travelTime > maxTravelTime)
							continue;
						boolean deployed = false;
						if (!subFleet.isExtendedRange() && empire.sv.inShipRange(sysId))
							deployed = deploy(subFleet, sysId);	// deploy
						else if (subFleet.isExtendedRange() && empire.sv.inScoutRange(sysId)) 
							deployed = deploy(subFleet, sysId);	// deploy
						if (deployed) {
							iTarget.remove();	// remove empire system as it has a ship assigned already
							iFleet.remove();
							break; // Go to next fleet
						}
					}
				}
			}
		}
		else {
			// System.out.println("MORE SHIPS THAN TARGET SYSTEMS");
			// We sort target systems by distance from home as the starting point
			int warpSpeed = subFleetList.minWarpSpeed();
			// System.out.println("Warp Speed "+warpSpeed);
			systemsSorter.sort(empire.homeSysId(), targets, warpSpeed);

			for (Iterator<Integer> iTarget = targets.iterator(); iTarget.hasNext(); ) {
				int sysId = iTarget.next();
				if (alreadyHasDesignsOrbiting(sysId, rules))
					continue;
				// System.out.println("Finding fleets for system " + empire.sv.name(si)+" "+empire.sv.descriptiveName(si));
				subFleetList.sortByTimeToSys(sysId);

				for (Iterator<SubFleet> iFleet = subFleetList.iterator(); iFleet.hasNext(); ) {
					SubFleet subFleet = iFleet.next();
					// if fleet was sent elsewhere during previous iteration, don't redirect it again
					if (!rules.fitForSystem(subFleet, sysId))
						continue;
					float travelTime = subFleet.fleet().travelTimeTo(empire.sv.system(sysId), warpSpeed);
					if (travelTime > maxTravelTime)
						continue;

					boolean deployed = false;
					if (!subFleet.isExtendedRange() && empire.sv.inShipRange(sysId))
						deployed = deploy(subFleet, sysId);	// deploy
					else if (subFleet.isExtendedRange() && empire.sv.inScoutRange(sysId)) 
						deployed = deploy(subFleet, sysId);	// deploy
					if (deployed) {
						iTarget.remove();	// remove empire system as it has a ship assigned already
						iFleet.remove();
						break; // go to next target
					}
				}
			}
		}
	}
	private boolean deploy(SubFleet subFleet, int target)	{
		if (subFleet.entireFleet())
			galaxy().ships.deployFleet(subFleet.fleet(), empire.sv.system(target).id);
		else
			return galaxy().ships.deploySubfleet(subFleet.fleet(), subFleet.shipCounts(), empire.sv.system(target).id);
		return true;
	}
}
