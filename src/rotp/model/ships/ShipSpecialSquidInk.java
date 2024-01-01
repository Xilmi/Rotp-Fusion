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
package rotp.model.ships;

import rotp.model.combat.CombatStack;
import rotp.model.tech.TechSquidInk;

public final class ShipSpecialSquidInk extends ShipSpecial {
	private static final long serialVersionUID = 1L;
	public ShipSpecialSquidInk(TechSquidInk t) {
		tech(t);
		sequence(t.level + .05f);
	}
	@Override public TechSquidInk tech()		{ return (TechSquidInk) super.tech(); }
	@Override public boolean isWeapon()			{ return true; }
	@Override public int	 range()			{ return tech().range; }
	@Override public boolean canAttackPlanets()	{ return false; }
	@Override public boolean canAttackShips()	{ return true; }
	@Override public float	 estimatedKills(CombatStack source, CombatStack target, int num) {
		// base pct is random from 10 to 30
		float pct = .2f;
		return target.num * pct;
	}
	@Override public void	 fireUpon(CombatStack source, CombatStack target, int count) {
		float pct = (random()*.2f) + .1f;
		// modnar: bug fix for Black Hole damage numbers
		float pctLoss = (float)Math.max(0.0f, pct - (target.shieldLevel() / 50) - target.blackHoleDef());
		float dmg = Math.round(pctLoss*target.num)*target.maxStackHits();
		tech().drawSpecialAttack(source, target, count, dmg);
		target.takeSquidInkDamage(pct);
	}
 }
