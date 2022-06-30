
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

/**
 * @author BrokenRegistry
 *
 */
public class T_String extends AbstractT<String> {
	
	private String localBlankCodeView = "";
	
	// ===== Constructors =====
	/**
	 * Base constructor for T_String Class
	 */
	public T_String() {
		super();
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the {@code String} initial value
	 */
	public T_String(String value) {
		super(value);
	}
	/**
	 * Constructor with direct List<value> initialization
	 * @param list the {@code String} initial value
	 */
	public T_String(List<String> list) {
		super(list);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	T_String(T_String value) {
		super(value);
	}
	// ===== Methods Overriders =====
	@Override protected  T_String This() { return this; }
	
	@Override protected String blankCodeView() { return localBlankCodeView; }

	@Override protected T_String blankCodeView(String value) {
		localBlankCodeView = value;
		return this;
	}

	@Override protected String toUserView(String val) { 
		return PMutil.suggestedUserViewFromCodeView(val);
	} 

	@Override protected String toCodeView(String str) { return null; } // to trigger the list choice

	@Override protected String min(String a, String b) {return a; }

	@Override protected String max(String a, String b) {return a; }

	@Override protected String validBound(String value, String min, String max) {
		return value;
	}

	@Override protected String random(String min, String max) {
		return blankCodeView(); // Nothing yet
	}

	@Override protected boolean equals(String a, String b) {return a == b; }
	
	@Override protected T_String New() { return new T_String(blankCodeView()); }
}
