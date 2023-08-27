/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.events;

import java.util.List;

import rotp.model.colony.Colony;
import rotp.model.empires.Empire;
import rotp.model.galaxy.SpaceAmoeba;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.IGameOptions;
import rotp.ui.notifications.GNNNotification;
import rotp.ui.util.ParamInteger;

public class RandomEventSpaceAmoeba extends AbstractRandomEvent {
    private static final long serialVersionUID = 1L;
    private static final String NEXT_ALLOWED_TURN = "AMOEBA_NEXT_ALLOWED_TURN";
    public static SpaceAmoeba monster;
    private int empId;
    private int sysId;
    private int turnCount = 0;
    
    static {
        initMonster();
    }
    @Override ParamInteger delayTurn()		{ return IGameOptions.amoebaDelayTurn; }
    @Override ParamInteger returnTurn()		{ return IGameOptions.amoebaReturnTurn; }
    @Override public String statusMessage()	{ return text("SYSTEMS_STATUS_SPACE_AMOEBA"); }
    @Override public String systemKey()		{ return "MAIN_PLANET_EVENT_AMOEBA"; }
    @Override public boolean goodEvent()	{ return false; }
    @Override public boolean monsterEvent()	{ return true; }
    @Override int nextAllowedTurn() { // for backward compatibility
    	return (Integer) galaxy().dynamicOptions().getInteger(NEXT_ALLOWED_TURN, -1);
    }
    @Override
    public String notificationText()    {
        String s1 = text("EVENT_SPACE_AMOEBA");
    	Empire emp = galaxy().empire(empId);
        s1 = s1.replace("[system]", emp.sv.name(sysId));
        s1 = emp.replaceTokens(s1, "victim");
        return s1;
    }
    @Override
    public void trigger(Empire emp) {
        log("Starting Amoeba event against: "+emp.raceName());
//        System.out.println("Starting Amoeba event against: "+emp.raceName());
    	if (emp == null || emp.extinct()) {
            empId = emp.id;
            sysId = emp.homeSysId(); // Former home of extinct empire
    	}
    	else {
            List<StarSystem> allSystems = emp.allColonizedSystems();
            StarSystem targetSystem = random(allSystems);
            targetSystem.eventKey(systemKey());

            empId = emp.id;
            sysId = targetSystem.id;
    	}
        turnCount = 3;
        galaxy().events().addActiveEvent(this);
    }
    @Override
    public void nextTurn() {
        if (isEventDisabled()) {
        	terminateEvent(this);
            return;
        }
       if (turnCount == 3) 
            approachSystem();     
        else if (turnCount == 0) 
            enterSystem();
        turnCount--;
    }
    private boolean nextSystemAllowed() { // BR: To allow disappearance
    	int maxSystem = options().selectedAmoebaMaxSystems();
        return maxSystem == 0 || maxSystem > monster.vistedSystemsCount();
    }
    private static void initMonster() {
        monster = new SpaceAmoeba();
    }
    private void enterSystem() {
//        System.out.println("Amoeba enter system");
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
            amoebaDestroyed();         
    }
    private void startCombat() {
        StarSystem targetSystem = galaxy().system(sysId);
        galaxy().shipCombat().battle(targetSystem, monster);
    }
    private void approachSystem() {
        StarSystem targetSystem = galaxy().system(sysId);
        targetSystem.eventKey(systemKey());
        Empire pl = player();
        if (targetSystem.isColonized()) { 
            if (pl.knowsOf(targetSystem.empire()) || !pl.sv.name(sysId).isEmpty())
                GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_AMOEBA", targetSystem.empire()), "GNN_Event_Amoeba");
        }
        else if (!pl.sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_AMOEBA_1", null), "GNN_Event_Amoeba");   
    }
    private void degradePlanet(StarSystem targetSystem) {
        Empire emp = targetSystem.empire();
        // colony may have already been destroyed in combat
        if (targetSystem.isColonized() || targetSystem.abandoned())
            monster.degradePlanet(targetSystem);
        
        if (emp == null)
            return;
        Empire pl = player();
        if (pl.knowsOf(emp) || !pl.sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_AMOEBA_2", emp), "GNN_Event_Amoeba");
    }
    private void amoebaDestroyed() {
        //galaxy().events().removeActiveEvent(this);
        terminateEvent(this);
        monster.plunder();

        if (player().knowsOf(empId)|| !player().sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_AMOEBA_3", monster.lastAttacker()), "GNN_Event_Amoeba");
    }
    private void monsterVanished() { // BR: To allow disappearance
    	terminateEvent(this);
        if (player().knowsOf(galaxy().empire(empId)) || !player().sv.name(sysId).isEmpty())
            GNNNotification.notifyRandomEvent(notificationText("EVENT_SPACE_AMOEBA_4", monster.lastAttacker()), "GNN_Event_Amoeba");
    }
    private void moveToNextSystem() {
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
        
        if (nextSysId < 0) {
            log("ERR: Could not find next system. Space Amoeba removed.");
//            System.out.println("ERR: Could not find next system. Space Amoeba removed.");
            // galaxy().events().removeActiveEvent(this);
            terminateEvent(this);
            return;
        }
    
        log("Space Amoeba moving to system: "+nextSysId);
//        System.out.println("Space Amoeba moving to system: "+nextSysId);
        StarSystem nextSys = galaxy().system(nextSysId);
        float slowdownEffect = max(1, 100.0f / galaxy().maxNumStarSystems());
        turnCount = (int) Math.ceil(1.5*slowdownEffect*nextSys.distanceTo(targetSystem));
        sysId = nextSys.id;    
        if (turnCount <= 3)
            approachSystem();     
    }
    private String notificationText(String key, Empire emp)    {
        String s1 = text(key);
        if (emp != null) {
            s1 = s1.replace("[system]", emp.sv.name(sysId));
            s1 = emp.replaceTokens(s1, "victim");
        }
        else 
            s1 = s1.replace("[system]", player().sv.name(sysId));
        return s1;
    }
}
