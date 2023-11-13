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
import rotp.model.galaxy.SpaceAmoeba;
import rotp.model.galaxy.SpaceMonster;
import rotp.model.game.IGameOptions;
import rotp.ui.notifications.GNNNotification;
import rotp.ui.util.ParamInteger;

public class RandomEventSpaceAmoeba extends RandomEventMonsters {
	private static final long serialVersionUID = 1L;
	// Static parameters for Tech Triggered Events
	public static final String TRIGGER_TECH		= "Cloning:1";
	public static final String TRIGGER_GNN_KEY	= "EVENT_SPACE_AMOEBA_TRIG";
	public static final String GNN_EVENT		= "GNN_Event_Amoeba";
	public static Empire triggerEmpire;

	@Override protected SpaceMonster newMonster(Float speed, Float level) {
		return new SpaceAmoeba(null, null);
	}
	@Override public boolean techDiscovered()	{ return triggerEmpire != null; }
	@Override protected String name()			{ return "AMOEBA"; }
	@Override ParamInteger delayTurn()			{ return IGameOptions.amoebaDelayTurn; }
	@Override ParamInteger returnTurn()			{ return IGameOptions.amoebaReturnTurn; }
	@Override protected void monsterDestroyed()	{
		//galaxy().events().removeActiveEvent(this);
		terminateEvent(this);
		monster.plunder();
		Empire emp = monster.lastAttacker();
		String notifKey = "EVENT_SPACE_AMOEBA_3";
		Integer saleAmount = null;
		if (options().monstersGiveLoot()) {
			notifKey = "EVENT_SPACE_AMOEBA_PLUNDER";
			saleAmount = galaxy().currentTurn();
			// Studying amoeba remains help completing the current research
			if (emp.tech().planetology().completeResearch())
				saleAmount *= 10;
			else
				saleAmount *= 25; // if no research then more gold
			// Selling the amoeba flesh gives reserve BC, scaling with turn number
			emp.addToTreasury(saleAmount);
		}
		if (player().knowsOf(empId)|| !player().sv.name(sysId).isEmpty())
		   	GNNNotification.notifyRandomEvent(notificationText(notifKey, emp, saleAmount), GNN_EVENT);
		monster = null;
	}
}
