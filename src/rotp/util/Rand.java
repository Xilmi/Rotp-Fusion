
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

import java.util.List;
import java.util.Random;

public class Rand extends Random {
	// Kept for backward compatibility
	@SuppressWarnings("unused")
	private double[] CRND;
	@SuppressWarnings("unused")
	private double[] lasts;
	@SuppressWarnings("unused")
	private int lastId = 0;

	private SerialRandom rng;
	
    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;

    private SerialRandom rng() {
    	if (rng==null)
    		rng = new SerialRandom();
    	return rng;
    }
    
	public Rand()			{ rng = new SerialRandom(); }
    public Rand(int src)	{ this((long) src); }
	public Rand(long src)	{
		if (src<=0)
			rng = new SerialRandom(System.currentTimeMillis());
		else
			rng = new SerialRandom(src);
	}

	@Override public boolean nextBoolean()	{ return rng().nextBoolean(); }
	@Override public double  nextDouble()	{ return rng().nextDouble(); }
	@Override public float   nextFloat()	{ return (float) rng().nextDouble(); }
	@Override public int	 nextInt()		{ return rng().nextInt(); }
	@Override public long	 nextLong()		{ return rng().nextLong(); }
	
    @Override synchronized public double nextGaussian() { // Copied from java.util.Random
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * rng().nextDouble() - 1; // between -1 and 1
                v2 = 2 * rng().nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
    }
	// ===== Getters with max =====
	/**
	 * @return  int: 0 <= random value < max
	 */
	@Override public int nextInt(int max)	{ return rng().nextInt(max); }
	/**
	 * @return  double: 0 <= random value < max
	 */
	public double nextDouble (double max) { return rng().nextDouble(max);  }
	/**
	 * @return  float: 0 <= random value < max
	 */
	public float nextFloat (float max) { return (float) rng().nextDouble(max);  }
	// ===== Getters with limits =====
	/**
	 * @return  min(lim1, lim2) <= random double < max(lim1, lim2)
	 */
	public double nextDouble(double lim1, double lim2) {
		return nextDouble(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random float < max(lim1, lim2)
	 */
	public float nextFloat(float lim1, float lim2) {
		return nextFloat(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}
	/**
	 * @return  min(lim1, lim2) <= random int < max(lim1, lim2)
	 */
	public int nextInt(int lim1, int lim2) {
		return nextInt(Math.abs(lim2-lim1)) + Math.min(lim2, lim1);
	}

	// ===== Symmetric Getters with multiplier =====
	/**
	 * @return  -1 <= random double < 1
	 */
	public double sym() { return nextDouble() * 2.0 - 1.0; }
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
	 * @return  center-width/2 <= random double < center+width/2
	 */
	public double sym(double center, double width) {
		return (nextDouble() - 0.5) * width + center;
	}
	/**
	 * @return  center-width/2 <= random float < center+width/2
	 */
	public float sym(float center, float width) {
		return (nextFloat() - 0.5f) * width + center;
	}
	/**
	 * @return  center-width/2 <= random int < center+width/2
	 */
	public int sym(int center, int width) {
		return (int) ((nextFloat() - 0.5f) * width + center);
	}
    public <T> T random(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list.get(nextInt(list.size()));
    }
}
