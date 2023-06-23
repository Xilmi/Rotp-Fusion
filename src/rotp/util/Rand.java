
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

import java.util.Random;

public class Rand extends Random{
	// BR: To avoid structure in multi dimensional random Multiple random generators will be used randomly.
	// BR: Trying to be more slowly uniform!

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

	private final double[] CRND;

	private double[] lasts;
	private int lastId = 0;

	
	public static class RandX extends Rand{
		public RandX(double source)	{
			super(new double[] { IGR, IPR, ISGR, ISR }, source);
		}
	}
	public static class RandY extends Rand{
		public RandY(double source)	{
			super(new double[] { SIGR, SIPR, SSIGR, SISR }, source);
		}
	}

	// ===== Constructors  and Initializers =====
	//
	public Rand(double[] crnd, double source)	{
		CRND = crnd;
		init(source);
	}
	public Rand(double source)	{
		CRND = new double[] { IGR, SIGR, IPR, SIPR, ISGR, SSIGR, ISR, SISR };
		init(source);
	}
	/**
	 * Initialize or reinitialize the randomizer
	 */
	@Override public void setSeed(long seed) { init(seed); }
	public void init(double source) {
		if (CRND == null)
			return;
		lasts = new double[CRND.length];
		if (source > 1.0)
			source = 1/source;
		if (source <= 0)
			source = Math.random();
		else if (source == 1.0)
			source = IGR;
		// Some chaotic function to scramble the source
		// Required for Orion position to be very random
		source = Math.abs(Math.sin( 1/(source*(1-source)) ));
			
		for (int i=0; i<lasts.length; i++) {
			lasts[i] = source;
			for (int k=0; k<100; k++)
				rand(i);
			source = rand(i);
		}
		lastId = (int) (source * CRND.length);
	}
	// ========== Private and protected Methods ==========
	//
	private double rand(int i) { return lasts[i] = (lasts[i]  + CRND[i])%1; }
	private double next() {
			lastId = (int) (rand(lastId) * CRND.length);
		return rand(lastId);
	}
	@Override protected int next(int bits) { return nextInt(); }

	// ========== Public getter Methods ==========
	//

	// ===== Basic getters
	/**
	 * @return  double: 0 <= random double < 1
	 */
	@Override public double nextDouble() { return next(); }
	/**
	 * @return  float 0 <= random float < 1
	 */
	@Override public float nextFloat() { return (float) next(); }
	/**
	 * @return  int: 0 <= random int < 2147483647
	 */
	@Override public int nextInt() { return (int) (next() * Integer.MAX_VALUE); }
	/**
	 * @return  int: 0 <= random int < 2147483647
	 */
	@Override public long nextLong() { return (long) (next() * Long.MAX_VALUE); }
	/**
	 * @return  random boolean
	 */
	@Override public boolean nextBoolean() { return next() < 0.5; }

	// ===== Getters with max =====
	/**
	 * @return  double: 0 <= random value < max
	 */
	@Override public double nextDouble (double max) { return max * next();  }
	/**
	 * @return  float: 0 <= random value < max
	 */
	@Override public float nextFloat (float max) { return (float) (max * next());  }
	/**
	 * @return  int: 0 <= random value < max
	 */
	@Override public int nextInt (int max) { return (int) (max * next());  }

	// ===== Getters with limits =====
	/**
	 * @return  min(lim1, lim2) <= random double < max(lim1, lim2)
	 */
	@Override public double nextDouble(double lim1, double lim2) {
		return nextDouble(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random float < max(lim1, lim2)
	 */
	@Override public float nextFloat(float lim1, float lim2) {
		return nextFloat(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random int < max(lim1, lim2)
	 */
	@Override public int nextInt(int lim1, int lim2) {
		return nextInt(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
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
