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

package rotp.mod.br.AddOns;

import rotp.model.events.RandomEvents;
import rotp.ui.util.ParamInteger;

public class EventsStartTurn extends ParamInteger {
	
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 * @param minValue The minimum value (null = none)
	 * @param maxValue The maximum value (null = none)
	 * @param baseInc  The base increment
	 * @param shiftInc The increment when Shift is hold
	 * @param ctrlInc  The increment when Ctrl is hold
	 * @param loop     what to do when reaching the limits
	 */
	public EventsStartTurn(String gui, String name, Integer defaultValue
			, Integer minValue, Integer maxValue, boolean loop
			, Integer baseInc, Integer shiftInc, Integer ctrlInc) {
		super(gui, name, defaultValue, minValue, maxValue, loop, baseInc, shiftInc, ctrlInc);
	}

	@Override public Integer set(Integer newValue) {
		value = newValue;
		RandomEvents.START_TURN = value;
		return value;
	}	
}
