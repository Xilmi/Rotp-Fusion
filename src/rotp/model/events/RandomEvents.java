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

public class RandomEvents implements Base, Serializable {
    private static final long serialVersionUID = 1L;
    private static final float START_CHANCE = 0.0f;
    private static final float CHANCE_INCR = 0.01f;
    private static final float MAX_CHANCE_INCR = 0.05f;
    public static int START_TURN = 50; // BR:Made it adjustable
    private List<RandomEvent> events;
    private List<RandomEvent> activeEvents;
	private RandomEvent lastEvent; // modnar: keep track of last event
    private float eventChance = START_CHANCE;

    public RandomEvents() {
    	activeEvents = new ArrayList<>();
        events = new ArrayList<>();
        loadEvents();
    }
    public void addActiveEvent(RandomEvent ev)     { activeEvents.add(ev); }
    public void removeActiveEvent(RandomEvent ev)  { activeEvents.remove(ev); }
    public void nextTurn() {
        if (options().disableRandomEvents()) 
            return;

        // possible that next-turn logic may remove an active event
        List<RandomEvent> tempEvents = new ArrayList<>(activeEvents);
        for (RandomEvent ev: tempEvents)
            ev.nextTurn();

        int turnNum = galaxy().currentTurn();
        if (turnNum < START_TURN)
            return;

        // BR: To allow RandomEventOption dynamic changes
        if (options().selectedRandomEventOption().equals(IGameOptions.RANDOM_EVENTS_OFF))
            return;

        eventChance = min(MAX_CHANCE_INCR, eventChance + CHANCE_INCR);
//        eventChance = 1.5f; // TO DO BR: REMOVE or COMMENT
//        System.out.println("eventChance = " + eventChance);
        if (random() > eventChance)
            return;
//        System.out.println("Event Triggered");

        List<RandomEvent> subList = eventSubList();
		if (subList.isEmpty())
		    return;
 
        RandomEvent triggeredEvent = random(subList);
//        triggeredEvent = new RandomEventPrecursorRelic(); // TO DO BR: REMOVE or COMMENT
        // RandomEvent triggeredEvent = random(events);
        // if (turnNum < triggeredEvent.minimumTurn())
        //     return;
        
		// modnar: make random events repeatable
		if (!triggeredEvent.repeatable()) {
			events.remove(triggeredEvent);
		}
		// BR: included in sublist generator
//		// modnar: with random events now repeatable
//		// don't trigger the same event twice in a row
//		if (triggeredEvent == lastEvent)
//			return;
//		// don't trigger when a duplicate event is still in effect
//		for (RandomEvent ev: activeEvents) {
//            if (triggeredEvent == ev)
//                return;
//        }
		
        eventChance = START_CHANCE;

        Empire affectedEmpire = triggeredEvent.goodEvent() ? empireForGoodEvent() : empireForBadEvent();
        triggeredEvent.trigger(affectedEmpire);
		lastEvent = triggeredEvent; // modnar: keep track of last event
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
    	if (event == lastEvent)
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
    private Empire empireForBadEvent() {
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
