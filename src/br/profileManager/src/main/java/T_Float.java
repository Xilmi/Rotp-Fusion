
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

package br.profileManager.src.main.java;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author BrokenRegistry
 *
 */
public class T_Float extends AbstractT <Float> {

	// ===== Constructors =====
	/**
	 * Base constructor for T_Float Class
	 */
	public T_Float() {
		super();
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the {@code Float} initial value
	 */
	public T_Float(Float value) {
		super(value);
	}
	/**
	 * Constructor with direct value List initialization
	 * @param value the {@code Float} initial value
	 */
	public T_Float(List<Float> value) {
		super(value);
	}
	/**
	 * Constructor with value initialization from userView
	 * @param value either userView or CodeView
	 */
	public T_Float(String value) {
		super();
		setFromUserView(value);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	T_Float(T_Float value) {
		super(value);
	}
	// ===== Methods Overriders =====
	@Override protected  T_Float This() { return this; }
	
	@Override protected Float toCodeView(String str) { return PMutil.toFloat(str); }

	@Override protected Float min(Float a, Float b) {return PMutil.min(a, b); }

	@Override protected Float max(Float a, Float b) {return PMutil.max(a, b); }

	@Override protected Float validBound(Float value, Float min, Float max) {
		return PMutil.validateLimits(value, min, max);
	}

	@Override protected Float random(Float min, Float max) {
		return nextRandom(min, max); 
	}

	@Override protected boolean equals(Float a, Float b) {return a == b; }

	@Override protected T_Float New() { return new T_Float(blankCodeView()); }
	
	// ===== Other Methods =====
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Float} number
	 * @param min inclusive minimum bound
	 * @param max exclusive maximum bound
	 * @return random {@code Float} value in the specified range
	 */
	static Float nextRandom(Float min, Float max) {
		if (min == null || max == null) {
			return null; // what else?
		}
		if (max == min) {
			return min;
		}
		if (max < min) {
			return (float) ThreadLocalRandom.current().nextDouble(max, min);
		}
		return (float) ThreadLocalRandom.current().nextDouble(min, max);
	}
}
