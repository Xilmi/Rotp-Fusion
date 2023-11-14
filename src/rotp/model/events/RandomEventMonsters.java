/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.events;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.SpaceMonster;
import rotp.model.galaxy.StarSystem;
import rotp.ui.notifications.GNNNotification;

abstract class RandomEventMonsters extends AbstractRandomEvent {
//	private static final long serialVersionUID = 1L;
	protected int empId;
	protected int sysId;
	protected int turnCount = 0;
	protected SpaceMonster monster;
	protected float level = 1.0f;
	protected float speed = max(0.4f, 1f / (1.5f * max(1, 100.0f/galaxy().maxNumStarSystems())));
	
	@Override public boolean goodEvent()		{ return false; }
	@Override public boolean monsterEvent() 	{ return true; }
	@Override public SpaceMonster monster()		{
		if (monster == null)
			trackLostMonster(); // backward compatibility
		return monster;
	}
	@Override int nextAllowedTurn()				{ // for backward compatibility
		String nextAllowedTurnKey = name() + "_NEXT_ALLOWED_TURN";
		return (Integer) galaxy().dynamicOptions().getInteger(nextAllowedTurnKey, -1);
	}
	@Override public String statusMessage()		{ return text(statusMessageKey()); }
	@Override public String systemKey()			{ return "MAIN_PLANET_EVENT_" + name(); }
	@Override public String notificationText()	{
		String s1 = text("EVENT_SPACE_" + name());
		Empire emp = galaxy().empire(empId);
		s1 = s1.replace("[system]", emp.sv.name(sysId));
		s1 = s1.replace("[race]", emp.raceName());
		s1 = emp.replaceTokens(s1, "victim");
		return s1;
	}
	@Override public void trigger(Empire emp)	{
		if (emp != null) {
			log("Starting Space " + dispName() + " event against: "+emp.raceName());
			// System.out.println("Starting Space " + dispName() + " event against: "+emp.raceName());
		}
		if (emp == null || emp.extinct()) {
			empId = emp.id;
			sysId = emp.homeSysId(); // Former home of extinct empire
		}
		else {
			StarSystem targetSystem = random(emp.allColonizedSystems());
			targetSystem.eventKey(systemKey());
			empId = emp.id;
			sysId = targetSystem.id;
		}
		turnCount = 3;
		galaxy().events().addActiveEvent(this);
	}
	@Override public void nextTurn()			{
		if (isEventDisabled()) {
			terminateEvent(this);
			return;
		}
		if (monster == null)
			initMonster();

		if (turnCount == 3) 
			approachSystem();	 
		else if (turnCount == 0) 
			enterSystem();
		turnCount--;
	}
	@Override public int  startTurn()			{
		if (techDiscovered() && options().techRandomEvents())
			return 1;
		return super.startTurn();
	}
	
	abstract protected String name();
	abstract protected SpaceMonster newMonster(Float speed, Float level);
	abstract protected Integer lootMonster(boolean lootMode);
	protected int getNextSystem()				{
		StarSystem targetSystem = galaxy().system(sysId);
		// next system is one of the 10 nearest systems
		// more likely to go to new system (25%) than visited system (5%)
		int[] near = targetSystem.nearbySystems();
		boolean stopLooking = false;		
		int nextSysId = -1;
		int loops = 0;
		if (near.length > 0) {
			while (!stopLooking) {
				loops++;
				for (int i=0;i<near.length;i++) {
					float chance = monster.vistedSystems().contains(near[i]) ? 0.05f : 0.25f;
					if (random() < chance) {
						nextSysId = near[i];
						stopLooking = true;
						break;
					}
				}
				if (loops > 10) 
					stopLooking = true;
			}
		}
		return nextSysId;
	}
	private void monsterDestroyed()				{
		//galaxy().events().removeActiveEvent(this);
		terminateEvent(this);
		monster.plunder();
		Empire emp = monster.lastAttacker();
		String event = eventName();
		String notifKey = event + "_3";
		Integer saleAmount = null;
		if (options().monstersGiveLoot()) {
			notifKey = event + "_PLUNDER";
			saleAmount = lootMonster(true);
			emp.addToTreasury(saleAmount);
		} else
			saleAmount = lootMonster(false);
		
		if (player().knowsOf(empId)|| !player().sv.name(sysId).isEmpty())
		   	GNNNotification.notifyRandomEvent(notificationText(notifKey, emp, saleAmount), gnnEvent());
		monster = null;
	}
	private void trackLostMonster()				{ // backward compatibility
		Galaxy gal = galaxy();
		for (StarSystem sys:gal.starSystems()) {
			if (sys.hasEvent()) {
				String event = sys.eventKey();
				if (event.equals(systemKey())) {
					sysId = sys.id;
					empId = sys.empId();
					turnCount = 3;
					initMonster();
					return;
				}
			}
		}
		// No destination found: terminate event
		System.err.println("No destination found: terminate event " + systemKey());
		terminateEvent(this);
	}
	private String eventName()					{ return "EVENT_SPACE_" + name(); }
	private String dispName()					{ return capitalize(name()); }
	private String statusMessageKey()			{ return "SYSTEMS_STATUS_SPACE" + name(); }
	private String capitalize(String s)			{
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
	private String gnnEvent()					{ return "GNN_Event_" + dispName(); }	
	private void approachSystem()				{
		StarSystem targetSystem = galaxy().system(sysId);
		targetSystem.eventKey(systemKey());
		Empire pl = player();
		if (targetSystem.isColonized()) { 
			if (pl.knowsOf(targetSystem.empire()) || !pl.sv.name(sysId).isEmpty())
				GNNNotification.notifyRandomEvent(notificationText(eventName(), targetSystem.empire(), null), gnnEvent());
		}
		else if (pl.sv.isScouted(sysId))
			GNNNotification.notifyRandomEvent(notificationText(eventName()+"_1", null, null), gnnEvent());   
	}
	private boolean nextSystemAllowed()			{ // BR: To allow disappearance
		int maxSystem = options().selectedCrystalMaxSystems();
		return maxSystem == 0 || maxSystem > monster.vistedSystemsCount();
	}
	private void initMonster()					{
		level = options().monstersLevel();
		monster = newMonster(speed, level);
		StarSystem targetSystem = galaxy().system(sysId);
		double alpha = random()*Math.PI*2;
		float speed	= monster.travelSpeed();
		double dist	= 2.9 * speed;
		double dx	= dist*Math.cos(alpha);
		double dy	= dist*Math.sin(alpha);
		float x		= (float) (targetSystem.x() + dx);
		float y		= (float) (targetSystem.y() + dy);
		monster.setXY(x, y);
		monster.destSysId(sysId);
		monster.launch(x, y);
	}
	private void enterSystem()					{
		//System.out.println("Monster enter system");
		monster.visitSystem(sysId);
		monster.initCombat();
		StarSystem targetSystem = galaxy().system(sysId);
		targetSystem.clearEvent();
		Colony col = targetSystem.colony();
		if (!targetSystem.orbitingFleets().isEmpty())
			startCombat();
		else if ((col != null) && col.defense().isArmed())
			startCombat();
		
		if (monster.alive()) {
			degradePlanet(targetSystem);
			if (nextSystemAllowed())
				moveToNextSystem();
			else
				monsterVanished();
		}
		else 
			monsterDestroyed();		 
	}
	private void startCombat()					{
		StarSystem targetSystem = galaxy().system(sysId);
		galaxy().shipCombat().battle(targetSystem, monster);
	}
	private void monsterVanished()				{ // BR: To allow disappearance
		terminateEvent(this);
		if (player().knowsOf(galaxy().empire(empId)) || !player().sv.name(sysId).isEmpty())
			GNNNotification.notifyRandomEvent(notificationText(eventName()+"_4", monster.lastAttacker(), null), gnnEvent());
		monster = null;
	}
	private void moveToNextSystem()				{
		monster.sysId(sysId); // be sure the monster is a t the planet
		int nextSysId = getNextSystem();
		if (nextSysId < 0) {
			log("ERR: Could not find next system. Space " + dispName() + " removed.");
			// System.out.println("ERR: Could not find next system. Space Crystal removed.");
			terminateEvent(this);
			return;
		}
		log("Space " + dispName() + " moving to system: "+nextSysId);

		sysId = nextSysId;	
		monster.destSysId(sysId);
		monster.launch();
		turnCount = monster.travelTurnsRemaining();
		if (turnCount <= 3)
			approachSystem();	 
	}
	private void degradePlanet(StarSystem targetSystem)	{
		Empire emp = targetSystem.empire();
		// colony may have already been destroyed in combat
		if (targetSystem.isColonized() || targetSystem.abandoned())
			monster.degradePlanet(targetSystem);
		
		if (emp == null)
			return;
		Empire pl = player();
		if (pl.knowsOf(emp) || !pl.sv.name(sysId).isEmpty())
			GNNNotification.notifyRandomEvent(notificationText(eventName()+"_2", emp, null), gnnEvent());
	}
	protected String notificationText(String key, Empire emp, Integer amount)	{
		String s1 = text(key);
		if (emp != null) {
			s1 = s1.replace("[system]", emp.sv.name(sysId));
			s1 = s1.replace("[race]", emp.raceName());
			s1 = emp.replaceTokens(s1, "victim");
		}
		else 
			s1 = s1.replace("[system]", player().sv.name(sysId));
		if (amount != null)
			s1 = s1.replace("[amt]", amount.toString());
		return s1;
	}
}
