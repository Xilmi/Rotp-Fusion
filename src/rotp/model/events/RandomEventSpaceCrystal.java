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
package rotp.model.events;

import rotp.model.empires.Empire;
import rotp.model.galaxy.SpaceCrystal;
import rotp.model.galaxy.SpaceMonster;
import rotp.model.game.IGameOptions;
import rotp.ui.util.ParamInteger;

public class RandomEventSpaceCrystal extends RandomEventMonsters {
	private static final long serialVersionUID = 1L;
	// Static parameters for Tech Triggered Events
	public static final String TRIGGER_TECH		= "Stargate:0";
	public static final String TRIGGER_GNN_KEY	= "EVENT_SPACE_CRYSTAL_TRIG";
	public static final String GNN_EVENT		= "GNN_Event_Crystal";
	public static Empire triggerEmpire;

	@Override protected SpaceMonster newMonster(Float speed, Float level) {
		return new SpaceCrystal(null, null);
	}
	@Override public boolean techDiscovered()	{ return triggerEmpire != null; }
	@Override protected String name()			{ return "CRYSTAL"; }
	@Override ParamInteger delayTurn()			{ return IGameOptions.crystalDelayTurn; }
	@Override ParamInteger returnTurn()			{ return IGameOptions.crystalReturnTurn; }
	@Override protected Integer lootMonster(boolean lootMode)	{
		if (!lootMode)
			return null;
		Integer saleAmount = galaxy().currentTurn();
		// Studying Crystal remains help completing the current research
		Empire emp = monster.lastAttacker();
		if (emp.tech().propulsion().completeResearch())
			saleAmount *= 10;
		else
			saleAmount *= 25; // if no research then more gold
		// Selling the Crystal part gives reserve BC, scaling with turn number
		return saleAmount;
	}
}
