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

import rotp.model.empires.Empire;
import rotp.model.ships.ShipDesign;

public abstract class GuardianMonsters extends SpaceMonster {
	private static final long serialVersionUID = 1L;
	private final String IMAGE_KEY;
	protected transient ShipDesign[] designs;
	private transient int designTurn;
	
	public GuardianMonsters(String name, int empId, Float speed, Float level) {
		super(name, empId, speed, level);
		IMAGE_KEY = name;
	}
	abstract void initDesigns();

	@Override public Image	 image()						{ return image(IMAGE_KEY); }
	@Override public void	 plunder()						{ removeGuardian(); }
	@Override public Image	 shipImage()					{ return image(IMAGE_KEY); }
	@Override public boolean isMonsterGuardian()			{ return true; }
	@Override public boolean isGuardian()					{ return true; }
	@Override public boolean isArmed()						{ return true; }
	@Override public boolean isArmedForShipCombat()			{ return true; }
	@Override public boolean isPotentiallyArmed(Empire e)	{ return true; }
	@Override public ShipDesign design(int i)				{ return designs()[i]; }
    @Override protected ShipDesign[] designs()				{
		// To allow the player to change the level in game.
		int turn = galaxy().currentTurn();
		if (designs == null || designTurn != turn)			{
			designTurn = turn;
			clearFleetStats();
			initDesigns();
		}
		return designs;
    }
}
