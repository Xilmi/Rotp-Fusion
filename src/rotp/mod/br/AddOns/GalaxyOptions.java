
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

package rotp.mod.br.AddOns;

import mod.br.AddOns.StarsOptions;

/**
 * @author BrokenRegistry
 * Some tools to optimize empires spacing
 */
public class GalaxyOptions {

	/**
	 * Adjust Star Probability to the user preference
	 * @param pcts the original cumulative distribution
	 * @return the modified cumulative distribution
	 */
	public static float[] modifyStarProbability(float[] pcts) {
		 return StarsOptions.probabilityModifier("STARS").modifyProbability(pcts);
	}
	/**
	 * Adjust Purple Planet Probability to the user preference
	 * @param pcts the original cumulative distribution
	 * @param color
	 * @return the modified cumulative distribution
	 */
	public static float[] modifyPlanetProbability(float[] pcts, String color) {
		 return StarsOptions.probabilityModifier(color).modifyProbability(pcts);
	}
}
