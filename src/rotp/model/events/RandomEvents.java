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

import rotp.model.empires.Empire;
import rotp.model.game.IGameOptions;
import rotp.util.Base;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomEvents implements Base, Serializable {
    private static final long serialVersionUID = 1L;
    private static final float START_CHANCE = 0.0f;
    private static final float CHANCE_INCR = 0.01f;
    private static final float MAX_CHANCE_INCR = 0.05f;
    private List<RandomEvent> events;
    private List<RandomEvent> activeEvents;
	private RandomEvent lastEvent; // modnar: keep track of last event
    private float eventChance = START_CHANCE;
    // BR: Added option for fixed random (reloading wont change the issue)
    private Long turnSeed   = null;
    private Long listSeed   = null;
    private Long targetSeed = null;

    public int startTurn() { // BR:Made it adjustable
    	return IGameOptions.eventsStartTurn.get();
    }
    public RandomEvents() {
    	activeEvents = new ArrayList<>();
        events = new ArrayList<>();
        turnSeed();
        listSeed();
        targetSeed();
        loadEvents();
    }
    private float chanceIncr()	  { return CHANCE_INCR * options().selectedEventsPace(); }
    private float maxChanceIncr() { return MAX_CHANCE_INCR * options().selectedEventsPace(); }
    // BR: Added option for fixed random (reloading wont change the issue)
    private Long turnSeed() {
    	if (turnSeed == null)
    		turnSeed = random.nextLong();
    	return turnSeed;
    }
    private Long listSeed() {
    	if (listSeed == null)
    		listSeed = random.nextLong();
    	return listSeed;
    }
    private Long targetSeed() {
    	if (targetSeed == null)
    		targetSeed = random.nextLong();
    	return targetSeed;
    }
    private float turnRnd() {
    	if (options().selectedFixedEventsMode()) {
    		turnSeed = new Random(turnSeed()).nextLong();
    		return new Random(turnSeed).nextFloat();
    	}
    	else
    		return random();
    }
    private int listRnd(int max) {
    	if (options().selectedFixedEventsMode()) {
    		listSeed = new Random(listSeed()).nextLong();
    		return new Random(listSeed).nextInt(max);
    	}
    	else
    		return random.nextInt(max);
    }
    private <T> T listRnd(List<T> list) {
    	if (list == null || list.isEmpty())
    		return null;
    	return list.get(listRnd(list.size()));
    }
    private int targetRnd() {
    	int numEmp = options().selectedNumberOpponents()+1;
    	targetSeed = new Random(targetSeed()).nextLong();
		return new Random(targetSeed).nextInt(numEmp);
    }

    public void addActiveEvent(RandomEvent ev)     { activeEvents.add(ev); }
    public void removeActiveEvent(RandomEvent ev)  {
    	activeEvents.remove(ev);
    	if (ev.hasPendingEvents()) // BR: May only happen with "fixed Event mode"
    		ev.trigger(ev.getPendingEmpire());
    }
    public void nextTurn() {
         // BR: To allow RandomEventOption dynamic changes
    	IGameOptions opts = options();
        if (opts.disableRandomEvents()) 
            return;

        // possible that next-turn logic may remove an active event
        List<RandomEvent> tempEvents = new ArrayList<>(activeEvents);
        for (RandomEvent ev: tempEvents)
            ev.nextTurn();

        int turnNum = galaxy().currentTurn();
        if (turnNum < startTurn())
            return;

        eventChance = min(maxChanceIncr(), eventChance + chanceIncr());
        if (turnRnd() > eventChance)
            return;

        List<RandomEvent> subList = eventSubList();
		if (subList.isEmpty())
		    return;
 
        RandomEvent triggeredEvent = listRnd(subList);
        // RandomEvent triggeredEvent = random(events);
        // if (turnNum < triggeredEvent.minimumTurn())
        //     return;
        
		// modnar: make random events repeatable
		if (!triggeredEvent.repeatable()) {
			events.remove(triggeredEvent);
		}
		
        eventChance = START_CHANCE; // Reset the probability counter
        
        Empire affectedEmpire;
        if (opts.selectedFixedEventsMode())
        	affectedEmpire = empireForFixedEvent();
        else if (!opts.selectedEventsFavorWeak())
        	affectedEmpire = randomEmpire();
        else if (triggeredEvent.goodEvent())
        	affectedEmpire = empireForGoodEvent();
        else
        	affectedEmpire = empireForBadEvent();
        
        triggeredEvent.trigger(affectedEmpire);
		lastEvent = triggeredEvent; // modnar: keep track of last event

		if (opts.debugAutoRun() && opts.debugLogEvents())
        	turnLog(IGameOptions.AUTORUN_EVENTS, triggeredEvent.notificationText());
    }
    public RandomEvent activeEventForKey(String key) {
        for (RandomEvent ev: activeEvents) {
            if (ev.systemKey().equals(key))
                return ev;
        }
        return null;
    }
    private List<RandomEvent> eventSubList() { // BR: To allow RandomEventOption dynamic changes
    	List<RandomEvent> subList = new ArrayList<>();
        for (RandomEvent ev: events)
        	if (isValidEvent (ev))
        		subList.add(ev);
        return subList;	
    }
    private boolean isValidEvent (RandomEvent event) {
    	if (!options().allowRandomEvent(event))
    		return false;
    	if (galaxy().currentTurn() < event.minimumTurn())
    		return false;
		// don't trigger the same event twice in a row
    	if (event == lastEvent && !options().selectedFixedEventsMode())
			return false;
		// don't trigger when a duplicate event is still in effect
		for (RandomEvent ev: activeEvents)
            if (event == ev)
                return false;
		return true;
    }
	private void loadEvents() {  // BR: To allow RandomEventOption dynamic changes
        addEvent(new RandomEventDonation());
        addEvent(new RandomEventDepletedPlanet());
        addEvent(new RandomEventEnrichedPlanet());
        addEvent(new RandomEventFertilePlanet());
        addEvent(new RandomEventComputerVirus());
        addEvent(new RandomEventEarthquake());
        addEvent(new RandomEventIndustrialAccident());
        addEvent(new RandomEventRebellion());
        addEvent(new RandomEventAncientDerelict());
        addEvent(new RandomEventAssassination());
        addEvent(new RandomEventPlague());
        addEvent(new RandomEventSupernova());
        addEvent(new RandomEventPiracy());
        addEvent(new RandomEventComet());
		addEvent(new RandomEventSpaceAmoeba());
		addEvent(new RandomEventSpaceCrystal());
		// modnar: add space pirate random event
		addEvent(new RandomEventSpacePirates());
        // modnar: add Precursor Relic random event
        addEvent(new RandomEventPrecursorRelic());
        // modnar: add Boost Planet baseSize random event
        addEvent(new RandomEventBoostPlanetSize());
        // modnar: add Gauntlet Relic random event
        addEvent(new RandomEventGauntletRelic());
        // addEvent(new RandomEventGenric("EventKey1"));
    }
    private void addEvent(RandomEvent ev) {
        // if (options().allowRandomEvent(ev)) // BR: To allow RandomEventOption dynamic changes
            events.add(ev);
    }
    private Empire empireForFixedEvent() { return galaxy().empire(targetRnd()); }
    private Empire randomEmpire()		 { return listRnd(galaxy().activeEmpires()); }
    private Empire empireForBadEvent()   {
    		          // chance of empires for bad events is based power for each empire
        Empire[] emps = galaxy().empires();
        float[] vals = new float[emps.length];
        float total = 0.0f;
        for (int i=0;i<emps.length;i++) {
            Empire emp = emps[i];
            float power = emp.extinct() ? 0 : emp.industrialPowerLevel(emp);
            vals[i] = power;
            total += power;
        }

        float r = total * random();
        for (int i=0;i<emps.length;i++) {
            if (r <= vals[i])
                return emps[i];
            r -= vals[i];
        }

        // should never get here... if we do, have event affect the player
        return player();
    }
    private Empire empireForGoodEvent() {
        // chance of empires for good events is based 1/power for each empire
        Empire[] emps = galaxy().empires();
        float[] vals = new float[emps.length];
        float total = 0.0f;
        for (int i=0;i<emps.length;i++) {
            Empire emp = emps[i];
            float power = emp.extinct() ? 0 : emp.industrialPowerLevel(emp);
            if (power > 0)
                power = 1/power;
            vals[i] = power;
            total += power;
        }

        float r = total * random();
        for (int i=0;i<emps.length;i++) {
            if (r <= vals[i])
                return emps[i];
            r -= vals[i];
        }

        // should never get here... if we do, have event affect the player
        return player();
    }
}
