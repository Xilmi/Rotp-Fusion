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
import rotp.model.tech.TechBiologicalWeapon;

public class ShipWeapon extends ShipComponent {
    private static final long serialVersionUID = 1L;
    private static final boolean showErrorMessages = false; // BR: Set to False
    private static final boolean showErrors = false; // BR: Set to False
    @Override
    public boolean isBeamWeapon()     { return false; }
    @Override
    public boolean isWeapon()         { return true; }
    public boolean noWeapon()         { return range() == 0; }
    public int minDamage()            { return 0; }
    public int maxDamage()            { return 0; }
    public int shots()                { return 1; }
    public int turnsToFire()          { return 1; }
    public float computerLevel()      { return 0; }
    public float decay()              { return 0; }
    public boolean canAttackPlanets() { return (!noWeapon() && (maxDamage() > 0)); }
    public float firepower()          { return firepower(0); }
    public float firepower(float shield) {
        // Use double for intermediate calculations for precision
        double minD = (double) minDamage();
        double maxD = (double) maxDamage();

        if (minD < 0 || maxD < minD) return 0.0f; // Basic validation

        double numOutcomes = maxD - minD + 1.0; // Total integer outcomes
        if (numOutcomes <= 0) return 0.0f;

        double effShield = (double)shield * shieldMod(); // Can be float

        // --- Calculation for DISCRETE integer damage rolls ---

        // Find the first integer damage D (>= minD) that is strictly > effShield
        double startD = Math.max(minD, Math.floor(effShield) + 1.0);

        // Count the number of integer damage values that penetrate
        double numPenetratingOutcomes = Math.max(0.0, maxD - startD + 1.0);

        // Calculate the sum of penetrating damages (using arithmetic series)
        double totalPenetratingDamageSum = numPenetratingOutcomes * (startD + maxD - 2.0 * effShield) / 2.0;

        // Average damage per shot = Sum / Total Possible Outcomes
        float dmg = (float)Math.max(0.0, totalPenetratingDamageSum / numOutcomes);
        // --- End of Discrete calculation block ---

        return attacksPerRound() * dmg * scatterAttacks();
    }
    public float max(ShipDesign d, int i) {
        return noWeapon() ? 0 : max(0, (float)Math.floor(d.availableSpaceForWeaponSlot(i) / space(d))) ;
    }
    @Override
    public float estimatedKills(CombatStack source, CombatStack target, int num) {
        if (groundAttacksOnly() && !target.isColony())
            return 0;
        float shieldLevel = target.shieldLevel();
        //ail: we multiply our damage by the amount of weapons shooting
        float dmg = firepower(shieldLevel) * num;
        // modnar: account for planetDamageMod()
        // correctly calculate damage estimate for attacking colony (in round-about way)
        // beams and torpedoes do half damage against colonies, planetDamageMod() = 0.5f
        // other weapons have planetDamageMod() = 1.0f, so this correction would have no effect for them
        // average(beamMax/2-shield, beamMin/2-shield)  // correct formula
        // = average(beamMax-2*shield, beamMin-2*shield)/2  // equivalent formula used here
        if (target.isColony()) {
            shieldLevel = target.shieldLevel() / planetDamageMod();
            dmg = firepower(shieldLevel) * num * planetDamageMod();
        }
		
        if (dmg == 0)
            return 0;
        //ail: The only thing that makes sense to return here is our total damage divided by the target's hitpoints
        return dmg/target.maxStackHits();
    }
    @Override
    public float estimatedBombardDamage(CombatStack source, CombatStackColony target) {
        int num = bombardAttacks();
        float shieldMod = source.targetShieldMod(this);
        float shieldLevel = shieldMod * target.shieldLevel();
        float beamMod = 1;
        float pct = (5 + source.attackLevel() - target.bombDefense()) / 10;
        pct = max(.05f, pct);
        pct = min(pct, 1); //cannot hit for more than 100%
        if(isBeamWeapon())
        {
            shieldLevel /= planetDamageMod(); 
            beamMod = planetDamageMod();
        }
        if(isBioWeapon() && target.num == 0)
        {
            float targetAntiDote = target.empire().tech().antidoteLevel();
            float damage = TechBiologicalWeapon.avgDamage(maxDamage(), (int)targetAntiDote) * 200;
            return damage * num;
        }
        return firepower(shieldLevel)* num * beamMod * pct;
    }
    @Override
    public float estimatedBombardDamage(ShipDesign des, CombatStackColony target) {
        int num = bombardAttacks();
        float shieldMod = des.targetShieldMod(this);
        float shieldLevel = shieldMod * target.shieldLevel();
        float beamMod = 1;
        float pct = (5 + des.attackLevel() + des.empire().shipAttackBonus() - target.bombDefense()) / 10;
        pct = max(.05f, pct);
        pct = min(pct, 1); //cannot hit for more than 100%
        if(isBeamWeapon())
        {
            shieldLevel /= planetDamageMod(); 
            beamMod = planetDamageMod();
        }
        if(isBioWeapon() && target.num == 0)
        {
            float targetAntiDote = target.empire().tech().antidoteLevel();
            float damage = TechBiologicalWeapon.avgDamage(maxDamage(), (int)targetAntiDote) * 200;
            return damage * num;
        }
        return firepower(shieldLevel)* num * beamMod * pct;
    }
    @Override
    public String fieldValue(int n, ShipDesign d, int bank) {
        switch(n) {
            case 0: return name().isEmpty() ? text("SHIP_DESIGN_COMPONENT_NONE") : name();
            case 1: return str((int)max(d, bank));
            case 2: int min = minDamage();
                int max = maxDamage();
                return (min == max) ? ""+min : text("SHIP_DESIGN_WEAPON_DMG_RANGE", min, max);
            case 3: return str((int)(cost(d)+(enginesRequired(d)*d.engine().cost(d))));
            case 4: return str((int)size(d));
            case 5: return str((int)power(d));
            case 6: return str((int)space(d));
            case 7: return desc();
        }
        return "";
    }
    public void drawIneffectiveAttack(CombatStack source, CombatStack target, int count) {
    	if (showErrors)
    		tech().drawIneffectiveAttack(source, target, source.weaponNum(this), count);
    	else {
	        try {
	            tech().drawIneffectiveAttack(source, target, source.weaponNum(this), count);
	        }
	        catch(Exception e) {
	        	if (showErrorMessages) {
	        		System.err.println("drawIneffectiveAttack Exception");
	        		System.err.println(e.getMessage());
	        	}
	        }
    	}
    }
    public void drawUnsuccessfulAttack(CombatStack source, CombatStack target, int count) {
    	if (showErrors)
    		tech().drawUnsuccessfulAttack(source, target, source.weaponNum(this), count);
    	else {
	        try {
	            tech().drawUnsuccessfulAttack(source, target, source.weaponNum(this), count);
	        }
	        catch(Exception e) {
	        	if (showErrorMessages) {
	        		System.err.println("drawUnsuccessfulAttack Exception");
	        		System.err.println(e.getMessage());
	        	}
	        }
    	}
    }
    public void drawSuccessfulAttack(CombatStack source, CombatStack target, float dmg, int count) {
    	if (showErrors)
    		tech().drawSuccessfulAttack(source, target, source.weaponNum(this), dmg, count);
    	else {
	        try {
	            tech().drawSuccessfulAttack(source, target, source.weaponNum(this), dmg, count);
	        }
	        catch(Exception e) {
	        	if (showErrorMessages) {
	        		System.err.println("drawSuccessfulAttack Exception");
	        		System.err.println(e.getMessage());
	        	}
	        }
    	}
    }
    public void drawSuccessfulAttack(CombatStack source, CombatStack target, float dmg, int count, float force) {
    	if (showErrors)
    		 tech().drawSuccessfulAttack(source, target, source.weaponNum(this), dmg, count, force);
    	else {
            try {
                tech().drawSuccessfulAttack(source, target, source.weaponNum(this), dmg, count, force);
            }
            catch(Exception e) {
            	if (showErrorMessages) {
    	        	System.err.println("drawSuccessfulAttack Exception");
    	        	System.err.println(e.getMessage());
            	}
            }
    	}
    }
}
