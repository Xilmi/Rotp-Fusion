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

import rotp.model.empires.Empire;
import rotp.model.galaxy.StarSystem;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetFactory;
import rotp.model.tech.TechTree;
import rotp.ui.notifications.GNNNotification;
import rotp.util.Base;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// modnar: add Precursor Relic random event
// give free Techs in Construction category
// generates colonizable planets in some systems with "No Colonizable Planets"
public class RandomEventPrecursorRelic implements Base, Serializable, RandomEvent {
    private static final long serialVersionUID = 1L;
    private static final int MAX_TECHS_DISCOVERED = 5; // 5 free techs
    private int empId;
    @Override
    public boolean goodEvent()    		{ return true; } // classify as positive event, has galactic effects
    @Override
    public boolean repeatable()    		{ return false; } // not repeatable
    @Override
    public int minimumTurn()            { return RandomEvents.START_TURN + 50; } // delay Precursor Relic event spawn
    @Override
    public String notificationText()    {
        String s1 = text("EVENT_RELIC_PLANETS");
        s1 = galaxy().empire(empId).replaceTokens(s1, "target");
        return s1;
    }
    @Override
    public void trigger(Empire emp) {
        
        // find all star systems that have no planets
        List<StarSystem> systems = new ArrayList<>();
        for (StarSystem sys : galaxy().starSystems()) {
            if (sys.planet().type().isAsteroids()) 
                systems.add(sys);
        }
        if (systems.isEmpty())
            return;

        // go through planet generation again for those empty star systems
        for (StarSystem sys : systems) {
            sys.planet(PlanetFactory.createPlanet(sys, session().populationBonus()));
        }
        
        // find unknown Techs in Construction category
        List<String> availableTechs = new ArrayList<>();

        TechTree empTech = emp.tech();
        int maxConsLevel = (int) empTech.construction().techLevel()+10;
        List<String> unkConstructionTechs =  empTech.construction().allTechs();
        unkConstructionTechs.removeAll(empTech.construction().knownTechs());
        for (String techId: unkConstructionTechs) {
            if (tech(techId).level() <= maxConsLevel)
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
        if (emp.isPlayer() || player().hasContact(emp))
            GNNNotification.notifyRandomEvent(notificationText(), "GNN_Event_Relic_Planets");

        for (String techId: discoveredTechs)
            emp.plunderShipTech(tech(techId), -1);
    }
}
