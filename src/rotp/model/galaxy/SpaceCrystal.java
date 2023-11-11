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
package rotp.model.galaxy;

import java.awt.Image;

import rotp.model.colony.Colony;
import rotp.model.combat.CombatStackSpaceCrystal;
import rotp.model.planet.PlanetType;
import rotp.ui.main.GalaxyMapPanel;

public class SpaceCrystal extends SpaceMonster {
    private static final long serialVersionUID = 1L;
    public SpaceCrystal() {
        super("SPACE_CRYSTAL", -3);
    }
    @Override
    public void initCombat() {
        combatStacks().clear();
        addCombatStack(new CombatStackSpaceCrystal(this));       
    }
    public void degradePlanet(StarSystem sys) {
        Colony col = sys.colony();
        if (col != null) {
            sys.empire().lastAttacker(this);
            col.destroy();  
        }  
        sys.planet().degradeToType(PlanetType.DEAD);
        float maxWaste = sys.planet().maxWaste();
        sys.planet().addWaste(maxWaste);
        sys.planet().removeExcessWaste();
        sys.abandoned(false);
    }
    // ShipMonster overriders
	@Override public int maxMapScale()	{ return GalaxyMapPanel.MAX_FLEET_HUGE_SCALE; }
	@Override public Image shipImage()	{ return image("SPACE_CRYSTAL"); }
}
