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
package rotp.model.ai.interfaces;

import java.util.Collections;
import java.util.List;

import rotp.model.empires.Empire;
import rotp.model.empires.EmpireView;
import rotp.model.empires.SpyNetwork;
import rotp.model.empires.SpyNetwork.Sabotage;
import rotp.model.empires.SpyNetwork.SabotageTargets;
import rotp.model.galaxy.StarSystem;

public interface SpyMaster {
    int suggestedInternalSecurityLevel();
    void setSpyingAllocation(EmpireView v);
    void setSpyingMission(EmpireView v);
    Empire suggestToFrame(List<Empire> empires);
	default SpyNetwork.Sabotage bestSabotageChoice(EmpireView v)	{
		// invoked when a Sabotage attempt is successful
		// unfinished - AI needs to choose best sabotage type
		// BR: unfinished, but common to most AI... Ready to be overridden
		SabotageTargets targets = v.spies().sabotageTargets();
		if (v.embassy().anyWar()) {
			if (!targets.baseTargets.isEmpty())
				return Sabotage.MISSILES;
			else if (!targets.factoryTargets.isEmpty())
				return Sabotage.FACTORIES;
			else if (!targets.rebellionTargets.isEmpty())
				return Sabotage.REBELS;
			else
				return null;
		}
		else {
			if (!targets.rebellionTargets.isEmpty())
				return Sabotage.REBELS;
			else if (!targets.factoryTargets.isEmpty())
				return Sabotage.FACTORIES;
			else if (!targets.baseTargets.isEmpty())
				return Sabotage.MISSILES;
			else
				return null;
		}
	}
	default StarSystem bestSystemForSabotage(EmpireView v, SpyNetwork.Sabotage choice)	{
		// invoked when a Sabotage attempt is successful
		// choice: 1 - factories, 2 - missiles, 3 - rebellion
		// BR: Common to most AI, and needed to be fixed because of accessing to forbidden knowledge
		// Now access list already used to choose the sabotage type.

		SabotageTargets targets = v.spies().sabotageTargets();
		StarSystem.VIEWING_EMPIRE = v.owner();
		switch(choice) {
			case FACTORIES:
				Collections.sort(targets.factoryTargets, StarSystem.VDISTANCE);
				return targets.factoryTargets.get(0);
			case MISSILES:
				Collections.sort(targets.baseTargets, StarSystem.VDISTANCE);
				return targets.baseTargets.get(0);
			case REBELS:
				Collections.sort(targets.rebellionTargets, StarSystem.VPOPULATION);
				return targets.rebellionTargets.get(targets.rebellionTargets.size()-1);
			default:
				return null;
		}
	}
}
