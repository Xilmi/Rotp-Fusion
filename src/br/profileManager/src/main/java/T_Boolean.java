
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
public class T_Boolean extends AbstractT <Boolean> {

	// ===== Constructors =====
	/**
	 * Base constructor for T_Boolean Class
	 */
	public T_Boolean() {
		super();
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the {@code Boolean} initial value
	 */
	public T_Boolean(Boolean value) {
		super(value);
	}
	/**
	 * Constructor with direct value List initialization
	 * @param value the {@code Boolean} initial value
	 */
	public T_Boolean(List<Boolean> value) {
		super(value);
	}
	/**
	 * Constructor with value initialization from userView
	 * @param value either userView or CodeView
	 */
	public T_Boolean(String value) {
		super();
		setFromUserView(value);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	T_Boolean(T_Boolean value) {
		super(value);
	}
	// ===== Methods Overriders =====
	@Override protected  T_Boolean This() { return this; }
	
	@Override protected String toUserView(Boolean val) { return PMutil.toYesNoString(val); }

	@Override protected Boolean toCodeView(String str) { return PMutil.toBoolean(str); }

	@Override protected Boolean min(Boolean a, Boolean b) {return a; }

	@Override protected Boolean max(Boolean a, Boolean b) {return a; }

	@Override protected Boolean validBound(Boolean value, Boolean min, Boolean max) {
		return value;
	}

	@Override protected Boolean random(Boolean min, Boolean max) {
		return ThreadLocalRandom.current().nextBoolean(); 
	}

	@Override protected boolean equals(Boolean a, Boolean b) {return a == b; }

	@Override protected T_Boolean New() { return new T_Boolean(blankCodeView()); }
	
	// ===== Other Methods =====
	/**
	 * generate random {@code Boolean} number
	 * @return random {@code Boolean} value
	 */
	static Boolean nextRandom() {
		return ThreadLocalRandom.current().nextBoolean(); 
	}
}
