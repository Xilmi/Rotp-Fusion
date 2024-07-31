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
package rotp.model.ships;

import rotp.model.combat.CombatStack;
import rotp.model.combat.CombatStackColony;
import rotp.model.combat.CombatStackShip;
import rotp.model.combat.ShipCombatManager;
import rotp.model.tech.TechStreamProjector;

public final class ShipSpecialProjector extends ShipSpecial {
    private static final long serialVersionUID = 1L;
    public ShipSpecialProjector(TechStreamProjector t) {
        tech(t);
        sequence(t.level + .05f);
    }
    @Override
    public TechStreamProjector tech() { return (TechStreamProjector) super.tech(); }
    @Override
    public String name()              { return tech().name(); }
    @Override
    public String desc()              { return tech().brief(); }
    @Override
    public int range()                { return tech().range; }
    @Override
    public boolean isWeapon()         { return true; }
    @Override
    public float estimatedKills(CombatStack source, CombatStack target, int num) {
        float armorMod = tech().armorMod(num);
        float oldStackHit	= target.maxStackHits();
        float newStackHit	= (int) oldStackHit * armorMod;
        float dam = oldStackHit - newStackHit;
        return dam / target.maxStackHits() * target.num;
    }
    @Override
    public void fireUpon(CombatStack source, CombatStack target, int count, ShipCombatManager mgr) {
        float armorMod = tech().armorMod(count);
        if (target.isShip()) {
            CombatStackShip st	= (CombatStackShip) target;
            float oldStackHit	= st.maxStackHits();
            float newStackHit	= (int) oldStackHit * armorMod;
            float streamDamage	= oldStackHit - newStackHit;
            st.streamProjectorHits(st.streamProjectorHits() + streamDamage);
            st.maxStackHits(newStackHit);
            st.hits(min(st.hits()-1, newStackHit));
        }
        else if (target.isColony()) {
        	CombatStackColony st= (CombatStackColony) target;
            float oldStackHit	= st.maxStackHits();
            float newStackHit	= (int) oldStackHit * armorMod;
            float streamDamage	= oldStackHit - newStackHit;
            st.streamProjectorHits(st.streamProjectorHits() + streamDamage);
            st.maxStackHits(newStackHit);
            st.hits(min(st.hits()-1, st.maxStackHits()));
        } 
        else if (target.isMonster()) {
            float oldStackHit	= target.maxStackHits();
            float newStackHit	= (int) oldStackHit * armorMod;
            float streamDamage	= oldStackHit - newStackHit;
            target.streamProjectorHits(target.streamProjectorHits() + streamDamage);
            target.maxStackHits(newStackHit);
            target.hits(min(target.hits()-1, newStackHit));
        }
        if (source.mgr.showAnimations())
        {
            tech().drawSpecialAttack(source, target, 1, 0);
            tech().drawSuccessfulAttack(source, target, source.weaponNum(this), 0, 0);
        }
        if (target.hits() <= 0)
            target.loseShip();
        if (target.maxStackHits() <= 0)
            source.mgr.destroyStack(target);
    }
}
