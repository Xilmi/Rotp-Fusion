
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
public class T_Double extends AbstractT <Double> {

	// ===== Constructors =====
	/**
	 * Base constructor for T_Double Class
	 */
	public T_Double() {
		super();
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the {@code Double} initial value
	 */
	public T_Double(Double value) {
		super(value);
	}
	/**
	 * Constructor with direct value List initialization
	 * @param value the {@code Double} initial value
	 */
	public T_Double(List<Double> value) {
		super(value);
	}
	/**
	 * Constructor with value initialization from userView
	 * @param value either userView or CodeView
	 */
	public T_Double(String value) {
		super();
		setFromUserView(value);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	T_Double(T_Double value) {
		super(value);
	}
	// ===== Methods Overriders =====
	@Override protected  T_Double This() { return this; }
	
	@Override protected Double toCodeView(String str) { return PMutil.toDouble(str); }

	@Override protected Double min(Double a, Double b) {return PMutil.min(a, b); }

	@Override protected Double max(Double a, Double b) {return PMutil.max(a, b); }

	@Override protected Double validBound(Double value, Double min, Double max) {
		return PMutil.validateLimits(value, min, max);
	}

	@Override protected Double random(Double min, Double max) {
		return nextRandom(min, max); 
	}

	@Override protected boolean equals(Double a, Double b) {return a == b; }

	@Override protected T_Double New() { return new T_Double(blankCodeView()); }
	
	// ===== Other Methods =====
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Double} number
	 * @param min inclusive minimum bound
	 * @param max exclusive maximum bound
	 * @return random {@code Double} value in the specified range
	 */
	static Double nextRandom(Double min, Double max) {
		if (min == null || max == null) {
			return null; // what else?
		}
		if (max == min) {
			return min;
		}
		if (max < min) {
			return ThreadLocalRandom.current().nextDouble(max, min);
		}
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
}
