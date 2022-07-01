
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

import mod.br.AddOns.GalaxySpacing;
import mod.br.AddOns.StarsOptions;

/**
 * @author BrokenRegistry
 * Some tools to optimize empires spacing
 */
public class GalaxyOptions {
	
	/**
	 * @param maxStars the {@code Integer} maximum number of stars per empire
	 * @param numOpps the {@code Integer} number of opponents
	 * @param sysBuffer the {@code float} some space reserve factor
	 */
	public static void initSpacing(int maxStars, int numOpps, float sysBuffer) {
		GalaxySpacing.initSpacing(maxStars, numOpps, sysBuffer);
	}
	/**
	 * @return minEmpireBuffer {@code float} value
	 */
	public static float getMinEmpireBuffer() {
		 return GalaxySpacing.getMinEmpireBuffer();
	}
	/**
	 * @return maxMinEmpireBuffer {@code float} value
	 */
	public static float getMaxMinEmpireBuffer() {
		 return GalaxySpacing.getMaxMinEmpireBuffer();
	}
	/**
	 * @return minOrionBuffer {@code float} value
	 */
	public static float getMinOrionBuffer() {
		return GalaxySpacing.getMinOrionBuffer();
	}
	/**
	 * @return getMinStarsPerEmpire {@code float} value
	 */
	public static int getMinStarsPerEmpire() {
		return GalaxySpacing.getMinStarsPerEmpire();
	}
	/**
	 * @return getPreferedStarsPerEmpire {@code float} value
	 */
	public static int getPreferredStarsPerEmpire() {
		return GalaxySpacing.getPreferredStarsPerEmpire();
	}
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
