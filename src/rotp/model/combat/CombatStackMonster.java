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
package rotp.model.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import rotp.model.ai.GuardianMonsterCaptain;
import rotp.model.empires.Empire;
import rotp.model.galaxy.SpaceMonster;
import rotp.model.galaxy.StarSystem;
import rotp.model.ships.ShipComponent;
import rotp.model.ships.ShipDesign;
import rotp.model.ships.ShipWeapon;
import rotp.model.ships.ShipWeaponMissileType;
import rotp.model.tech.TechCloaking;

public class CombatStackMonster extends CombatStack {
	protected ShipDesign design;
	protected final SpaceMonster monster;
	public final List<ShipComponent> weapons = new ArrayList<>();
	protected ShipComponent selectedWeapon;

	public	  int[] weaponCount		= new int[7];
	protected int[] weaponAttacks	= new int[7];
	protected int[] roundsRemaining	= new int[7]; // how many rounds you can fire (i.e. missiles)
	protected int[] shotsRemaining	= new int[7]; // how many uses (shots) left on the current turn
	protected int[] baseTurnsToFire	= new int[7]; // how many turns to wait before you can fire again
	protected int[] wpnTurnsToFire	= new int[7]; // how many turns to wait before you can fire again
	protected String name;
	protected int	 repulsorRange	 = 0;
	protected float	 displacementPct = 0;
	private	final float	monsterLevel;

	public CombatStackMonster(SpaceMonster mstr, String imageKey, Float level) {
		monster	= mstr;
		name  	= imageKey;
		num		= 1;
		if (level == null) {
			if (monster.isGuardian() && system() != null)
				monsterLevel = options().guardianMonstersLevel() * system().monsterPlanetValue();
			else
				monsterLevel = options().monstersLevel();			
		} else
			monsterLevel = level;
			
		init0();
	}
	protected void init0() {
		empire	= monster.empire();
	   	design	= monster.design();
	   	if (design != null)
	   		initShip();

		streamProjectorHits(0);
		startingMaxHits(maxStackHits());
		hits(maxStackHits());
		cloak();
		image	= image(name);
	   	captain = new GuardianMonsterCaptain(monster);
		move	= maxMove;
		shield	= maxShield;

		if (weapons.size() > 0)
			selectedWeapon = weapons.get(0);
	}
	private void initShip() {
		maxStackHits(design.hits());
		maxMove			= design.moveRange();
		maxShield		= system().inNebula() ? 0 : design.shieldLevel();
		attackLevel		= design.attackLevel() + empire.shipAttackBonus();
		maneuverability = design.maneuverability();
		repulsorRange	= design.repulsorRange();
		missileDefense	= design.missileDefense() + empire.shipDefenseBonus();
		beamDefense		= design.beamDefense() + empire.shipDefenseBonus();
		displacementPct	= design.missPct();
		repairPct		= designShipRepairPct();
		beamRangeBonus	= designBeamRangeBonus();
		image			= design.image();
		canCloak		= design.allowsCloaking();

		for (int i=0;i<ShipDesign.maxWeapons();i++) {
			if (validWeapon(i) && (design.wpnCount(i) > 0)) {
				weaponCount[weapons.size()]		= design.wpnCount(i);
				weaponAttacks[weapons.size()]	= design.weapon(i).attacksPerRound();
				roundsRemaining[weapons.size()]	= design.weapon(i).shots();
				baseTurnsToFire[weapons.size()]	= design.weapon(i).turnsToFire();
				wpnTurnsToFire[weapons.size()]	= 1;
				weapons.add(design.weapon(i));
			}
		}
		for (int i=0;i<ShipDesign.maxSpecials();i++) {
			if (design.special(i).isWeapon()) {
				weaponCount[weapons.size()]		= 1;
				weaponAttacks[weapons.size()]	= 1;
				roundsRemaining[weapons.size()]	= 1;
				baseTurnsToFire[weapons.size()]	= 1;
				wpnTurnsToFire[weapons.size()]	= 1;
				weapons.add(design.special(i));
			}
		}
	}
	private boolean validWeapon(int i) {
		ShipWeapon wpn = design.weapon(i);
		return wpn.isWeapon() && !wpn.noWeapon();
	}
	private float designShipRepairPct() {
		float healPct = 0;
		for (int i=0;i<ShipDesign.maxSpecials();i++)
			healPct = max(healPct, design.special(i).shipRepairPct());
		return healPct;
	}
	private int designBeamRangeBonus() {
		int rng = 0;   
		for (int j=0;j<ShipDesign.maxSpecials();j++)
			rng += design.special(j).beamRangeBonus();
		return rng;
	}
	private StarSystem system() { return monster.system(); }

	protected float	 monsterLevel()					{ return monsterLevel; }
	protected int	 monsterLevel(int val)			{ return (int) (val * monsterLevel); }

	@Override public boolean canAttack(CombatStack st)					{
		if (st == null)
			return false;
		if (st.inStasis)
			return false;
		for (int i=0;i<weapons.size();i++) {
			if (shipComponentCanAttack(st, i))
				return true;
		}
		return false;
	}
	@Override public void cloak()										{
		if (!cloaked && canCloak) {
			cloaked = true;
			transparency = TechCloaking.TRANSPARENCY;
		}
	}
	@Override public boolean canPotentiallyAttack(CombatStack target)	{
		Empire emp = target.empire;
		return emp != null; // You won't them attacking other wandering monsters!
	}
	@Override public boolean currentWeaponCanAttack(CombatStack target)	{
		if (selectedWeapon() == null)
			return false;
		int wpn = weapons.indexOf(selectedWeapon());
		return shipComponentCanAttack(target, wpn);
	}
	@Override public ShipDesign design()	{ return design; }
	@Override public void	 endTurn()		{
		super.endTurn();
		for (int i=0;i<shotsRemaining.length;i++) 
			wpnTurnsToFire[i] = shotsRemaining[i] == 0 ? baseTurnsToFire[i] : wpnTurnsToFire[i]-1;		  
	}
	@Override public void	 fireWeapon(CombatStack targetStack)		{ fireWeapon(targetStack, weaponIndex()); }
	@Override public void	 fireWeapon(CombatStack targetStack, int index)	{ 
		 if (targetStack == null)
			return;

		target = targetStack;
		target.damageSustained = 0;
		// only fire if we have shots remaining... this is a missile concern
		if ((roundsRemaining[index] > 0)) {
			selectedWeapon = weapons.get(index);
			// some weapons (beams) can fire multiple per round
			int shots = (int) selectedWeapon.attacksPerRound();
			int count = num*shots*weaponCount[index];
			if (selectedWeapon.isMissileWeapon()) {
				CombatStackMissile missile = new CombatStackMissile(this, (ShipWeaponMissileType) selectedWeapon, count);
				log(fullName(), " launching ", missile.fullName(), " at ", targetStack.fullName());
				mgr.addStackToCombat(missile);
			}
			else {
				log(fullName(), " firing ", str(count), " ", selectedWeapon.name(), " at ", targetStack.fullName());
				selectedWeapon.fireUpon(this, target, count);
			}
			if (target == null) 
				log("TARGET IS NULL AFTER BEING FIRED UPON!");
			shotsRemaining[index] = max(0, shotsRemaining[index]-1);
			if (selectedWeapon.isLimitedShotWeapon())
				roundsRemaining[index] = max(0, roundsRemaining[index]-1);
			if (target.damageSustained > 0)
				log("weapon damage: ", str(target.damageSustained));
		}
		rotateToUsableWeapon(targetStack);
		target.damageSustained = 0;
	}
	@Override public boolean hostileTo(CombatStack st, StarSystem sys)	{ return true; }
	@Override public boolean immuneToStasis()					{ return false; }
	@Override public float	 initiativeRank()					{ return 5; }
	@Override public boolean isMonster()						{ return true; }
	@Override public boolean isArmed()							{ return true; }
	@Override public int	 maxFiringRange(CombatStack tgt)	{
		if (roundsRemaining[0]>0)
			return 9;
		else if (wpnTurnsToFire[2] < 2)
			return 10;
		else
			return optimalFiringRange(tgt);
	}
	@Override public float	 missileInterceptPct(ShipWeaponMissileType wpn)   {
		if (design() == null)
			return super.missileInterceptPct(wpn);
		else
			return design().missileInterceptPct(wpn);
	}
	@Override public String	 name()								{ return text("PLANET_" + name); }
	@Override public int	 numWeapons()						{ return weapons.size(); }
	@Override public int	 optimalFiringRange(CombatStack tgt){ return 3; }
	@Override public void	 reloadWeapons()					{
		for (int i=0;i<shotsRemaining.length;i++) 
			shotsRemaining[i] = 1;
		for (ShipComponent c: weapons)
			c.reload(); 
	};
	@Override public void	 rotateToUsableWeapon(CombatStack target)	{
		if (selectedWeapon == null)
			return;
		int i = weapons.indexOf(selectedWeapon);
		int j = i;
		boolean looking = true;

		while (looking) {
			j++;
			if (j == weapons.size())
				j = 0;
			selectedWeapon = weapons.get(j);
			if ((j == i) || currentWeaponCanAttack(target))
				looking = false;
		}
	}
	@Override public ShipComponent selectedWeapon()			{ return selectedWeapon; }
	@Override public boolean	   selectBestWeapon(CombatStack target)	{
		if (target.destroyed())
			return false;
		if (currentWeaponCanAttack(target))
			return true;

		rotateToUsableWeapon(target);
		return currentWeaponCanAttack(target);
	}
	@Override public Color		   shieldBaseColor()		{ return Color.red; }
	@Override public int		   shotsRemaining(int i)	{ return shotsRemaining[i]; }
	@Override public ShipComponent weapon(int i)			{ return weapons.get(i); }
	@Override public int 	 weaponIndex()					{ return weapons.indexOf(selectedWeapon); }
	@Override public int	 weaponNum(ShipComponent comp)	{ return weapons.indexOf(comp); }
	@Override public int	 wpnCount(int i)				{ return weaponCount[i]; }

	private boolean shipComponentCanAttack(CombatStack target, int index)	{
		if (target == null)
			return false;

		if (roundsRemaining[index]< 1)
			return false;

		if (shotsRemaining[index] < 1)
			return false;
		
		if (wpnTurnsToFire[index] > 1)
			return false;

		if (target.inStasis || target.isMissile())
			return false;

		ShipComponent shipWeapon = weapons.get(index);

		if (!shipWeapon.isWeapon())
			return false;

		if (shipWeapon.isLimitedShotWeapon() && (roundsRemaining[index] < 1))
			return false;

		if (shipWeapon.groundAttacksOnly() && !target.isColony())
			return false;

		int minMove = movePointsTo(target);
		if (weaponRange(shipWeapon) < minMove)
			return false;

		return true;
	}
	public void		drawAttack() { 
		if (mgr.ui == null)
			return;
		
		brighten = 1.0f;
		for (int i=0;i<2;i++) {
			scale += 1.5;
			transparency -= 0.45;
			brighten -= .005;
			long t0 = System.currentTimeMillis();
			mgr.ui.paintAllImmediately();
			long t1 = System.currentTimeMillis() - t0;
			if (t1 < 50)
				sleep(50-t1);
		}
		for (int i=0;i<12;i++) {
			scale -= 0.25;
			transparency += 0.075;
			brighten -= 0.075f;
			long t0 = System.currentTimeMillis();
			mgr.ui.paintAllImmediately();
			long t1 = System.currentTimeMillis() - t0;
			if (t1 < 50)
				sleep(50-t1);
		}
		brighten = 0;
		mgr.ui.paintAllImmediately();
	}
}
