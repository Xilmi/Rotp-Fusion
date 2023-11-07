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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.game.IGameOptions;
import rotp.ui.util.ParamInteger;
import rotp.util.Base;

abstract class AbstractRandomEvent implements RandomEvent, Base, Serializable {
	private List<Empire> pendingEvents	= new ArrayList<>();
    private Integer lastEndedTurn; // BR: new parameter, could be null on reload

	abstract ParamInteger delayTurn();
	abstract ParamInteger returnTurn();

	int nextAllowedTurn()	 { return -1; } // for backward compatibility
	@Override public void	 nextTurn()		 { }
	@Override public boolean repeatable()	 { return returnTurn().get() > 0; }
	@Override public String	 systemKey()	 { return ""; }
	@Override public String	 statusMessage() { return ""; }
	@Override public boolean monsterEvent()	 { return false; }
	@Override public int startTurn()		 {
		if (monsterEvent() && techDiscovered() && options().techRandomEvents())
			return 1;
		return eventsStartTurn() + delayTurn().get() * difficultyFactor();
	}
    private void	setLastEndedTurn() 		 { lastEndedTurn = galaxy().currentTurn(); }
	private boolean	isRepeatable()			 { return returnTurn().get() > 0; }
	private int		eventsStartTurn()		 { return IGameOptions.eventsStartTurn.get(); }
	private boolean alreadyOccurred()		 {
		if (lastEndedTurn != null)
			return true;
		// Test for backward compatibility
		int nat = nextAllowedTurn(); // overridable call
		if (nat < 0) // Never have occurred
			return false;
		// old save: backward compatibility
		lastEndedTurn = nat - returnTurn().get();
		return true;
	}
	boolean	isEventDisabled()	{
		// Specific Event disabled ?
		if (delayTurn().get() < 0)
			return true;
		String reo = options().selectedRandomEventOption();
		// Events Globally disabled ?
		if (reo.equals(IGameOptions.RANDOM_EVENTS_OFF))
			return true;
		// Only Monsters ?
		if (!monsterEvent() && reo.equals(IGameOptions.RANDOM_EVENTS_ONLY_MONSTERS))
			return true;
		// Monster Disabled ?
		if (monsterEvent() && reo.equals(IGameOptions.RANDOM_EVENTS_NO_MONSTERS))
			return true;
		// Monster Tech Waiting ?
		if (!techDiscovered())
			return true;
		return false;
	}
	void terminateEvent(RandomEvent event) {
   		galaxy().events().removeActiveEvent(event);
   		setLastEndedTurn();
	}
	@Override public int minimumTurn()	{
		if (isEventDisabled())
			return Integer.MAX_VALUE;
		else if (alreadyOccurred())
			if (isRepeatable())
				return lastEndedTurn + returnTurn().get();
			else
				return Integer.MAX_VALUE;
		else // never occurred
			return startTurn();
	}	
	@Override public boolean hasPendingEvents()	{
		if (pendingEvents == null) // BR: For backward game compatibility
			return false;
		return pendingEvents.size() > 0; 
	}
	@Override public Empire getPendingEmpire()	{
		if (pendingEvents == null) // BR: For backward game compatibility
			return null;
		if (pendingEvents.size()==0)
			return null;
		return pendingEvents.remove(0); 
	}
    private int	 difficultyFactor()	 {
		switch (options().selectedGameDifficulty()) {
	        case IGameOptions.DIFFICULTY_EASIEST:	return 4;
	        case IGameOptions.DIFFICULTY_EASIER:	return 3;
	        case IGameOptions.DIFFICULTY_EASY:		return 2;
	        default:								return 1;
		}
    }
	@Override public void addPendingEvents(Empire emp) {
		pendingEvents.add(emp);
		if (options().debugAutoRun() && options().debugLogEvents())
        	turnLog(IGameOptions.AUTORUN_EVENTS, "Pending: " + emp.name() + " # " + notificationText());
	}
}
