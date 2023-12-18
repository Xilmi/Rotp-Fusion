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
package rotp.model.galaxy;

import java.awt.Image;
import java.util.List;

import rotp.model.combat.CombatStackGuardianJellyfish;
import rotp.model.ships.ShipArmor;
import rotp.model.ships.ShipComputer;
import rotp.model.ships.ShipDesign;
import rotp.model.ships.ShipDesignLab;
import rotp.model.ships.ShipECM;
import rotp.model.ships.ShipEngine;
import rotp.model.ships.ShipManeuver;
import rotp.model.ships.ShipShield;
import rotp.model.ships.ShipWeaponBeam;
import rotp.model.tech.TechShipWeapon;
import rotp.ui.main.GalaxyMapPanel;

public class GuardianJellyfish extends SpaceMonster {
	private static final long serialVersionUID = 1L;
	private static final String IMAGE_KEY = "SPACE_JELLYFISH";
	public GuardianJellyfish(Float speed, Float level) {
		super(IMAGE_KEY, -2, speed, level);
	}
	@Override public void initCombat()			 {
		super.initCombat();
		addCombatStack(new CombatStackGuardianJellyfish(this, IMAGE_KEY, stackLevel()));	   
	}
	@Override public Image	 image()			 { return image(IMAGE_KEY); }
	@Override public void	 plunder()			 { removeGuardian(); }
	@Override public Image	 shipImage()		 { return image(IMAGE_KEY); }
	@Override public int	 maxMapScale()		 { return GalaxyMapPanel.MAX_FLEET_HUGE_SCALE; }
	@Override public boolean isMonsterGuardian() { return true; }
	@Override public boolean isGuardian()		 { return true; }
	@Override public SpaceMonster getCopy() 	 { return new GuardianJellyfish(null, null); }
	@Override public ShipDesign design()		 {
		ShipDesignLab lab = empire().shipLab();
		
		ShipDesign design = lab.newBlankDesign(Math.max(ShipDesign.MEDIUM,
								stackLevel(ShipDesign.LARGE, ShipDesign.HUGE)));
		design.mission	(ShipDesign.DESTROYER);

		List<ShipEngine> engines = lab.engines();
		design.engine	(engines.get(stackLevel(0, engines.size()-1)));

		List<ShipComputer> computers = lab.computers();
		design.computer	(computers.get(stackLevel(2, computers.size()-1)));

		List<ShipArmor> armors = lab.armors();
		design.armor	(armors.get(stackLevel(2, armors.size()-1)));

		List<ShipShield> shields = lab.shields();
		design.shield	(shields.get(stackLevel(2, shields.size()-1)));

		List<ShipECM> ecms = lab.ecms();
		design.ecm		(ecms.get(stackLevel(2, ecms.size()-1)));

		List<ShipManeuver> maneuvers = lab.maneuvers();
		design.maneuver	(maneuvers.get(stackLevel(0, maneuvers.size()-1)));

		int wpnAll = max(1, stackLevel(10));
		for (int i=4; i>0; i--) {
			int count = wpnAll/i;
			if (count != 0) {
				// jellyfish darts
				design.weapon(i-1, new ShipWeaponBeam((TechShipWeapon) tech("ShipWeapon:24"), false), count);
				// design.addWeapon(new ShipWeaponBeam((TechShipWeapon) tech("ShipWeapon:24"), false), count);
				wpnAll -= count;
			}
		}
		// design.special(0, lab.specialBattleScanner());
		// design.special(1, lab.specialTeleporter());
		// design.special(2, lab.specialCloak());
		// design.name(text(IMAGE_KEY));
		// lab.iconifyDesign(design);
		return design;
	}

}
