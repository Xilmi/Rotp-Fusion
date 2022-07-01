
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

package mod.br.AddOns;

/**
 * @author BrokenRegistry
 * Some tools to optimize empires spacing
 */
public class GalaxySpacing {

    // ========================================================================
    // MAXIMIZE EMPIRES SPACING
    //
	/**
	 * Default value for "MAXIMIZE EMPIRES SPACING" Mod
	 */
	public static final boolean DEFAULT_MAXIMIZE_EMPIRES_SPACING = false;
    private static boolean maximizeEmpiresSpacing = DEFAULT_MAXIMIZE_EMPIRES_SPACING;
    
	/**
	 * @return maximizeEmpiresSpacing
	 */
	public static boolean isMaximizeEmpiresSpacing() {
		return maximizeEmpiresSpacing;
	}

	/**
	 * @param b <b>true</b> to maximize empire spreading
	 */
	public static void setMaximizeEmpiresSpacing(boolean b) {
		maximizeEmpiresSpacing = b;
	}

	private static float minEmpireBuffer;
	private static float maxMinEmpireBuffer;
	private static float minOrionBuffer;

	/**
	 * @param maxStars the {@code Integer} maximum number of stars per empire
	 * @param numOpps the {@code Integer} number of opponents
	 * @param sysBuffer the {@code float} some space reserve factor
	 */
	public static void initSpacing(int maxStars, int numOpps, float sysBuffer) {
		int minStarsPerEmpire = getMinStarsPerEmpire();
		if (isMaximizeEmpiresSpacing())
			minStarsPerEmpire = maxStars/numOpps;
		float maxMinEmpireFactor = 15f; // To avoid problems with strange galaxy shapes
		                                // Maybe To-Do Make this a new setting
		float minEmpireFactor = (minStarsPerEmpire + 1) / 3; // 8 spe -> 3; 12 spe -> 4;
		if (minEmpireFactor >= (maxMinEmpireFactor - 2))
			minEmpireFactor = maxMinEmpireFactor - 2;
		minEmpireBuffer    = sysBuffer * minEmpireFactor;
		maxMinEmpireBuffer = sysBuffer * maxMinEmpireFactor;
		minOrionBuffer     = sysBuffer * minEmpireFactor + 1;
	}

	/**
	 * @return minEmpireBuffer {@code float} value
	 */
	 public static float getMinEmpireBuffer() {
		 return minEmpireBuffer;
	}

	/**
	 * @return maxMinEmpireBuffer {@code float} value
	 */
	public static float getMaxMinEmpireBuffer() {
		 return maxMinEmpireBuffer;
	}

	/**
	 * @return minOrionBuffer {@code float} value
	 */
	public static float getMinOrionBuffer() {
		return minOrionBuffer;
	}	
	// ========================================================================
    // PREFERED STARS PER EMPIRE
    //
	/**
	 * Default value for "PREFERED STARS PER EMPIRE" Mod
	 */
	public static final int DEFAULT_PREFERRED_STARS_PER_EMPIRE = 16;
	private static final int MIN_PREF_ALLOWED = 1;

	private static int preferredStarsPerEmpire = DEFAULT_PREFERRED_STARS_PER_EMPIRE;
	
	/**
	 * @return preferedStarsPerEmpire {@code Integer} value
	 */
	public static int getPreferredStarsPerEmpire() {
		return preferredStarsPerEmpire;
	}

	/**
	 * @param newValue the new {@code Integer} value
	 */
	public static void setPreferredStarsPerEmpire(int newValue) {
		preferredStarsPerEmpire = Math.max(MIN_PREF_ALLOWED, newValue);
	}

    // ========================================================================
    // MIN STARS PER EMPIRE
    //
	/**
	 * Default value for "MIN STARS PER EMPIRE" Mod
	 */
	public static final int DEFAULT_MIN_STARS_PER_EMPIRE = 8;
	private static final int MIN_MIN_ALLOWED = 1;

	private static int minStarsPerEmpire  = DEFAULT_MIN_STARS_PER_EMPIRE;

	/**
	 * @return minStarsPerEmpire {@code Integer} value
	 */
	public static int getMinStarsPerEmpire() {
		return minStarsPerEmpire;
	}

	/**
	 * @param newMin the new {@code Integer} value
	 */
	public static void setMinStarsPerEmpire(int newMin) {
		minStarsPerEmpire = Math.max(MIN_MIN_ALLOWED, newMin);
	}
}
