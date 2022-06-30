
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
public class T_Integer extends AbstractT <Integer> {

	// ===== Constructors =====
	/**
	 * Base constructor for T_Integer Class
	 */
	public T_Integer() {
		super();
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the {@code Integer} initial value
	 */
	public T_Integer(Integer value) {
		super(value);
	}
	/**
	 * Constructor with direct value List initialization
	 * @param value the {@code Integer} initial value
	 */
	public T_Integer(List<Integer> value) {
		super(value);
	}
	/**
	 * Constructor with value initialization from userView
	 * @param value either userView or CodeView
	 */
	public T_Integer(String value) {
		super();
		setFromUserView(value);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	T_Integer(T_Integer value) {
		super(value);
	}
	// ===== Methods Overriders =====
	@Override protected  T_Integer This() { return this; }
	
	@Override protected Integer toCodeView(String str) { return PMutil.toInteger(str); }

	@Override protected Integer min(Integer a, Integer b) {return PMutil.min(a, b); }

	@Override protected Integer max(Integer a, Integer b) {return PMutil.max(a, b); }

	@Override protected Integer validBound(Integer value, Integer min, Integer max) {
		return PMutil.validateLimits(value, min, max);
	}

	@Override protected Integer random(Integer min, Integer max) {
		return nextRandom(min, max); 
	}

	@Override protected boolean equals(Integer a, Integer b) {return a == b; }

	@Override protected T_Integer New() { return new T_Integer(blankCodeView()); }
	
	// ===== Other Methods =====
	/**
	 * Check boundaries to avoid error throwing 
	 * and generate random {@code Integer} number
	 * @param min inclusive minimum bound
	 * @param max exclusive maximum bound
	 * @return random {@code Integer} value in the specified range
	 */
	static Integer nextRandom(Integer min, Integer max) {
		if (min == null || max == null) {
			return null; // what else?
		}
		if (max == min) {
			return min;
		}
		if (max < min) {
			return ThreadLocalRandom.current().nextInt(max, min);
		}
		return ThreadLocalRandom.current().nextInt(min, max);
	}
}
