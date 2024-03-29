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

import java.util.ArrayList;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.game.IGameOptions;
import rotp.model.tech.TechTree;
import rotp.ui.notifications.GNNNotification;
import rotp.ui.util.ParamInteger;

public class RandomEventAncientDerelict extends AbstractRandomEvent {
    private static final long serialVersionUID = 1L;
    private static final int MAX_TECHS_DISCOVERED = 10;
    private int empId;
    @Override ParamInteger delayTurn()		{ return IGameOptions.derelictDelayTurn; }
    @Override ParamInteger returnTurn()		{ return IGameOptions.derelictReturnTurn; }
    @Override public boolean goodEvent()	{ return true; }
    @Override
    public String notificationText()    {
        String s1 = text("EVENT_DERELICT");
        s1 = galaxy().empire(empId).replaceTokens(s1, "target");
        return s1;
    }
    @Override
    public void trigger(Empire emp) {
    	if (emp == null || emp.extinct())
    		return;
        List<String> availableTechs = new ArrayList<>();

        TechTree empTech = emp.tech();
        int maxCompLevel = (int) empTech.forceField().techLevel()+10;

        // BR: fixed tech Library deletion
        // allTechs() is the static tech library list!
        // List<String> unkComputerTechs =  empTech.forceField().allTechs();
        List<String> unkComputerTechs = new ArrayList<>();;
        unkComputerTechs.addAll(empTech.forceField().allTechs());

        unkComputerTechs.removeAll(empTech.forceField().knownTechs());
        for (String techId: unkComputerTechs) {
            if (tech(techId).level() <= maxCompLevel && !tech(techId).restricted)
                availableTechs.add(techId);
        }

        int maxWpnLevel = (int) empTech.weapon().techLevel()+10;
        // BR: fixed tech Library deletion
        // allTechs() is the static tech library list!
        //List<String> unkWeaponTechs =  empTech.weapon().allTechs();
        List<String> unkWeaponTechs = new ArrayList<>();;
        unkWeaponTechs.addAll(empTech.forceField().allTechs());
        unkWeaponTechs.removeAll(empTech.weapon().knownTechs());
        for (String techId: unkWeaponTechs) {
            if (tech(techId).level() <= maxWpnLevel && !tech(techId).restricted)
                availableTechs.add(techId);
        }

        if (availableTechs.isEmpty())
                return;

        List<String> discoveredTechs = new ArrayList<>();
        if (availableTechs.size() <= MAX_TECHS_DISCOVERED)
            discoveredTechs.addAll(availableTechs);
        else {
            while (discoveredTechs.size() < MAX_TECHS_DISCOVERED) {
                String techId = random(availableTechs);
                availableTechs.remove(techId);
                discoveredTechs.add(techId);
            }
        }

        empId = emp.id;
        if (emp.isPlayerControlled() || player().hasContact(emp))
            GNNNotification.notifyRandomEvent(notificationText(), "GNN_Event_Derelict");

        for (String techId: discoveredTechs)
            emp.plunderShipTech(tech(techId), -1);
    }
}
