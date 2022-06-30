
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author BrokenRegistry
 * parameters are not hidden, used as record.
 * 
 * @param <T> the base Value Class
 */
public abstract class AbstractT <T> {

	private static String listSeparator;

	private T blankCodeView = null;
	private List<T> codeList;
	private List<String> userList;
	
	// ===== Constructors =====
	/**
	 * Base Constructor
	 */
	AbstractT() {
		init(blankCodeView());
	}
	/**
	 * Constructor with direct value initialization
	 * @param value the initial value
	 */
	AbstractT(T value) {
		init(value);
	}
	/**
	 * Constructor for direct value List initialization
	 * @param list the initial list
	 */
	AbstractT(List<T> value) {
		init(value);
	}
	/**
	 * Constructor for cloning
	 * @param value The value to clone
	 */
	AbstractT(AbstractT<T> value) {
		init(value);
	}
	
	// ===== Initializers =====
	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		listSeparator = PM.getConfig("listSeparator");
	}
	private void init(T value) {
		codeView(value);
		userView(toUserView(value));
	}
	private void init(List<T> value) {
		codeView(value);
		userView(toUserView(value));
	}
	private void init(AbstractT<T> value) {
		if (value != null) {
			if (value.codeList != null) {
				codeList = new ArrayList<T>(value.codeList);
			}
			if (value.userList != null) {
				userList = new ArrayList<String>(value.userList);
			}
		}
	}

	// ===== Abstract Request =====
	/**
	 * alternative to "<b>new</b>" valid in the abstract generic class
	 */
	abstract AbstractT<T> New();
	/**
	 * to get the child Class this
	 */
	abstract AbstractT <T> This();
	/**
	 * convert the UserView to CodeView
	 * @return The CodeView
	 */
	abstract T toCodeView(String str);
	/**
	 * @return The minimum of a, b
	 */
	abstract T min(T a, T b);
	/**
	 * @return The maximum of a, b
	 */
	abstract T max(T a, T b);
	/**
	 * @return The value as min <= value <= max
	 */
	abstract T validBound(T value, T min, T max);
	/**
	 * @return The a random value between min and max
	 */
	abstract T random(T min, T max);
	/**
	 * Check for equalities
	 */
	abstract boolean equals(T a, T b);

	// ===== Methods That may be overridden =====
	/**
	 * @return The Blank CodeView
	 */
	T blankCodeView() {
		return blankCodeView;
	};
	/**
	 * Set a new value for blankCodeView
	 * @param value The new value
	 * @return This for chaining purpose
	 */
	AbstractT <T> blankCodeView(T value) {
		blankCodeView = value;
		return This();
	}
	/**
	 * Base conversion from Code View to User View
	 * @param value the code view to convert
	 * @return the user view
	 */
	String toUserView(T value) {
		if (value != null) {
			return value.toString();
		}
	return "";
	}

	// ===== Overriders =====
	@Override public String toString() {
		if (isBlankUser()) {
			return "";
		}
		return String.join(listSeparator, userList);
	}

	@Override protected AbstractT<T> clone() {
		return New(this);
	}

	// ===== Method generalizing Overriders =====
	/**
	 * List conversion from Code View to User View
	 * @param value the code view List to convert
	 * @return the user view List
	 */
	List<String> toUserView(List<T> value) {
		ArrayList<String> list = new ArrayList<String>();
		if (value != null) {
			for (T element : value) {
				list.add(toUserView(element));
			}
		}
		return list;
	}
	/**
	 * List conversion from User View to Code View
	 * @param value the user view List to convert
	 * @return the code view List
	 */
	List<T> toCodeView(List<String> value) {
		ArrayList<T> list = new ArrayList<T>();
		if (value != null) {
			for (String element : value) {
				list.add(toCodeView(element));
			}
		}
		return list;
	}
	AbstractT<T> New(T value) {
		return New().setFromCodeView(value);
	}
	AbstractT<T> New(List<T> value) {
		return New().setFromCodeView(value);
	}
	AbstractT<T> New(AbstractT<T> value) {
		return New().setValue(value);
	}
	/**
	 * @return The a random value between min and max
	 */
	AbstractT <T> nextRandom(AbstractT <T> min, AbstractT <T> max) {
		return setFromCodeView(random(min.getCodeView(), max.getCodeView()));
	}
	String toUserViewNeverNull(T value) {
		String userView = toUserView(value);
		if (userView == null) {
			return "";
		}
		return userView;
	}
	/**
	 * @return The value as min <= value <= max
	 */
	List<T> validBound(List<T> value, T min, T max) {
		ArrayList<T> result = new ArrayList<T>();
		if (value != null) {
			for (T element : value) {
				result.add(validBound(element, min, max));
			}
		}
		return result;
	}
	/**
	 * @return The Validated Value as min <= value <= max
	 */
	AbstractT <T> validBound(T min, T max) {
		init(validBound(codeList, min, max));
		return null;
	}
	
	// ===== Public Testers =====
	boolean isBlankUser() {
		return userList.size() == 0
				|| getUserView().isBlank();
	}
	boolean isBlankCode() {
		return codeList.size() == 0 
				|| getCodeView() == null
				|| equals(getCodeView(), blankCodeView());
	}
	boolean isBlank() {
		return isBlankUser() && isBlankCode();
	}
	
	// ===== Private Setters =====
	private void userView(String value) {
		userList = new ArrayList<String>();
		userList.add(value);
	}
	private void userView(List<String> value) {
		userList = new ArrayList<String>(value);
	}
	private void codeView(T value) {
		codeList = new ArrayList<T>();
		codeList.add(value);
	}
	private void codeView(List<T> value) {
		if (value != null) {
			codeList = new ArrayList<T>(value);
		}
	}

	// ===== Public Setters =====
	/**
	 * To clear all fields
	 * @return This for chaining purpose
	 */
	public AbstractT<T> reset() {
		codeList = new ArrayList<T>();
		userList = new ArrayList<String>();
		return This();
	}
	/**
	 * This setter will only set the user view 
	 * @param value the user View
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setUserViewOnly(String value) {
		userView(value);
		return This();
	}
	/**
	 * This setter will initialize both code and user Lists 
	 * @param value the user View
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setFromUserView(String value) {
		init(toCodeView(value));
		return This();
	}
	/**
	 * This setter will only set the user view 
	 * @param value the user View List
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setUserViewOnly(List<String> value) {
		userView(value);
		return This();
	}
	/**
	 * This setter will initialize both code and user Lists 
	 * @param value the user View List
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setFromUserView(List<String> value) {
		init(toCodeView(value));
		return This();
	}
	/**
	 * This setter will only set the code view
	 * @param value the code View
	 * @return This for Chaining purpose
	 */
	public AbstractT<T> setCodeViewOnly(T value) {
		codeView(value);
		return This();
	}
	/**
	 * This setter will initialize both code and user Lists 
	 * @param value the code View
	 * @return This for Chaining purpose
	 */
	public AbstractT<T> setFromCodeView(T value) {
		init(value);
		return This();
	}
	/**
	 * This setter will only set the code view
	 * @param value the code View List
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setCodeViewOnly(List<T> value) {
		codeView(value);
		return This();
	}
	/**
	 * This setter will initialize both code and user Lists 
	 * @param value the code View List
	 * @return This for chaining purpose
	 */
	public AbstractT<T> setFromCodeView(List<T> value) {
		init(value);
		return This();
	}
	/**
	 * Copy a value to this 
	 * @param value the new value
	 * @return This for chaining purpose
	 */
	AbstractT <T> setValue(AbstractT<T> value) {
		blankCodeView (value.blankCodeView);
		if (value.codeList != null) {
			codeList = new ArrayList<T>(value.codeList);
		} else {
			codeList = new ArrayList<T>();
		}
		if (value.userList != null) {
			userList = new ArrayList<String>(value.userList);
		} else {
			userList = new ArrayList<String>();
		}
		return This();
	}

	// ===== Public Getters =====
	/**
	 * @return the full list as String with separators
	 */
	public String getUserEntry() {
		return String.join(listSeparator, userList);
	}
	/**
	 * @return the full list of user view
	 */
	public List<String> getUserList() {
		return new ArrayList<String>(userList);
	}
	/**
	 * @return the first element of user view
	 */
	public String getUserView() {
		if (userList.size() > 0) {
			return userList.get(0);
		}
		return "";
	}
	/**
	 * @return the full list of code view
	 */
	public List<T> getCodeList() {
		return new ArrayList<T>(codeList);
	}
	/**
	 * @return the first element of code view
	 */
	public T getCodeView() {
		if (codeList.size() > 0) {
			return codeList.get(0);
		}
		return blankCodeView();
	}
}
