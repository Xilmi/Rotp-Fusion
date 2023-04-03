
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

package rotp.util;

public class Rand {
	// BR: To avoid structure in multi dimensional random Multiple random generators will be used randomly.

	// Based on golden ratio: (The positive solution of x^2 = x + 1)
	// https://en.wikipedia.org/wiki/Golden_ratio
	private static final double IGR  = 0.6180339887498948482046; // Inverse of golden ratio
	private static final double SIGR = 0.3819660112501051517954; // Square of the inverse of golden ratio

	// Based on plastic number: (The real solution of x^3 = x + 1)
	// https://en.wikipedia.org/wiki/Plastic_number
	// Inverse of golden ratio: 2 / (sqrt(5) + 1)
	private static final double IPR  = 0.7548776662466927600495; // Inverse of plastic number
	private static final double SIPR = 0.5698402909980532659114; // Square of the inverse of plastic number

	// Based on super golden ratio: (The real solution of x^3 = x^2 + 1)
	// https://en.wikipedia.org/wiki/Supergolden_ratio
	private static final double ISGR  = 0.68232780382801932736948; // Inverse of super golden ratio
	private static final double SSIGR = 0.4655712318767680266567; // Square of the inverse of super golden ratio

	// Based on silver ratio: (1 + sqrt(2))
	// https://en.wikipedia.org/wiki/Golden_ratio
	private static final double ISR  = 0.4142135623730950488017; // Inverse of silver ratio
	private static final double SISR = 0.1715728752538099023966; // Square of the inverse of silver ratio

	private static final double[] CRND = { IGR, SIGR, IPR, SIPR, ISGR, SSIGR, ISR, SISR };

	private final double[] lasts = new double[CRND.length];
	private int lastId = 0;

	// ===== Constructors  and Initializers =====
	//
	public Rand() { init(Math.random()); }
	public Rand(double source) { init(source); }
	/**
	 * Initialize or reinitialize the randomizer
	 */
	public void init(double source) {
		if (source > 1.0)
			source = 1/source;
		if (source == 0 || source == 1.0)
			source = IGR;
		for (int i=0; i<lasts.length; i++)
			lasts[i] = source;
	}
	// ========== Private Methods ==========
	//
	private double rand(int i) { return lasts[i] = (lasts[i]  + CRND[i])%1; }

	// ========== Public getter Methods ==========
	//

	// ===== Basic getters
	/**
	 * @return  double: 0 <= random value < 1
	 */
	public double next() {
		lastId = (int) (rand(lastId) * CRND.length);
		return rand(lastId);
	}
	/**
	 * @return  float 0 <= random value < 1
	 */
	public float nextFloat() { return (float) next(); }
	/**
	 * @return  int: 0 <= random value < 2147483647
	 */
	public int nextInt() { return (int) (next() * Integer.MAX_VALUE); }
	/**
	 * @return  boolean: 0 <= random value < 2147483647
	 */
	public boolean nextBoolean() { return next() < 0.5; }

	// ===== Getters with max =====
	/**
	 * @return  double: 0 <= random value < max
	 */
	public double next (double max) { return max * next();  }
	/**
	 * @return  float: 0 <= random value < max
	 */
	public float next (float max) { return (float) (max * next());  }
	/**
	 * @return  int: 0 <= random value < max
	 */
	public int next (int max) { return (int) (max * next());  }

	// ===== Getters with limits =====
	/**
	 * @return  min(lim1, lim2) <= random double < max(lim1, lim2)
	 */
	public double next(double lim1, double lim2) {
		return next(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random float < max(lim1, lim2)
	 */
	public float next(float lim1, float lim2) {
		return next(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random int < max(lim1, lim2)
	 */
	public int next(int lim1, int lim2) {
		return next(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}

	// ===== Symmetric Getters with multiplier =====
	/**
	 * @return  -1 <= random double < 1
	 */
	public double sym() { return next() * 2.0 - 1.0; }
	/**
	 * @return  -1 <= random float < 1
	 */
	public float symFloat() { return (float) sym(); }
	/**
	 * @return  -1 <= random int <= 1
	 */
	public int symInt() { return (int) Math.signum(Math.round(sym()*1.5)); }

	// ===== Symmetric Getters with multiplier =====
	/**
	 * @return  -max <= random double < max
	 */
	public double sym(double max) { return max * sym(); }
	/**
	 * @return  -max <= random float < max
	 */
	public float sym(float max) { return max * symFloat(); }
	/**
	 * @return  -max <= random int < max
	 */
	public int sym(int max) { return (int) (max * sym()); }

	// ===== Symmetric Getters with Limits =====
	/**
	 * @return  ctr-width/2 <= random double < ctr+width/2
	 */
	public double sym(double ctr, double width) {
		return (next() - 0.5) * width + ctr;
	}
	/**
	 * @return  ctr-width/2 <= random float < ctr+width/2
	 */
	public float sym(float ctr, float width) {
		return (nextFloat() - 0.5f) * width + ctr;
	}
	/**
	 * @return  ctr-width/2 <= random int < ctr+width/2
	 */
	public int sym(int ctr, int width) {
		return (int) ((nextFloat() - 0.5f) * width + ctr);
	}
}
