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

public abstract class RandomEvent implements Base, Serializable {
	private static final int NEVER_TURN	= 99999;
	private List<Empire> pendingEvents	= new ArrayList<>();
    private Integer lastEndedTurn; // BR: new parameter, could be null on reload

	abstract boolean goodEvent();
	abstract String notificationText();
	abstract void trigger(Empire e);
	abstract ParamInteger delayTurn();
	abstract ParamInteger returnTurn();

	int nextAllowedTurn()	{ return -1; } // for backward compatibility

	public boolean	repeatable()		{ return returnTurn().get() > 0; }
	public void		nextTurn()			{ }
	public String	systemKey()			{ return ""; }
	public String	statusMessage()		{ return ""; }
	public boolean	monsterEvent()		{ return false; }
	public boolean	isEventDisabled()	{
		// Specific Event disabled ?
		if (delayTurn().get() < 0)
			return true;
		String reo = options().selectedRandomEventOption();
		// Events Globally disabled ?
		if (reo.equals(IGameOptions.RANDOM_EVENTS_OFF))
			return true;
		// Monster Disabled ?
		if (monsterEvent() && reo.equals(IGameOptions.RANDOM_EVENTS_NO_MONSTERS))
			return true;
		return false;
	}
	public void		terminateEvent(RandomEvent event) {
   		galaxy().events().removeActiveEvent(event);
   		setLastEndedTurn();
	}

	public int minimumTurn()			{
		if (isEventDisabled())
			return NEVER_TURN;
      	int delayTurn = delayTurn().get() * difficultyFactor();
      	int turn = startTurn() + delayTurn;
      	return max(turn, nextRepeatTurn()); // BR: To allow repeatable event
	}
	public boolean hasPendingEvents()	{
		if (pendingEvents == null) // BR: For backward game compatibility
			return false;
		return pendingEvents.size() > 0; 
	}
	public Empire getPendingEmpire()	{
		if (pendingEvents == null) // BR: For backward game compatibility
			return null;
		if (pendingEvents.size()==0)
			return null;
		return pendingEvents.remove(0); 
	}
	private int		startTurn()			{ return IGameOptions.eventsStartTurn.get(); }
    private void	setLastEndedTurn()	{ lastEndedTurn = galaxy().currentTurn(); }
    private int		nextRepeatTurn()	{
    	if (repeatable())
    		if (lastEndedTurn == null)  { // May never have occurred
    			int nat = nextAllowedTurn();
    			if (nat < 0) // Never have occurred
    				return 0;
    			else  { // backward compatibility
    				lastEndedTurn = nat - returnTurn().get();
    				return nat;
    			}
    		}
    		else
    			return lastEndedTurn + returnTurn().get();
    	else
    		return NEVER_TURN;
    }
    private int		difficultyFactor()	{
		switch (options().selectedGameDifficulty()) {
	        case IGameOptions.DIFFICULTY_EASIEST:	return 4;
	        case IGameOptions.DIFFICULTY_EASIER:	return 3;
	        case IGameOptions.DIFFICULTY_EASY:		return 2;
	        default:								return 1;
		}
    }
}
