
/**
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

package br.profileManager.src.main.java;

import static br.profileManager.src.main.java.PMutil.capitalize;
import static br.profileManager.src.main.java.PMutil.clean;
import static br.profileManager.src.main.java.Validation.History.Default;

/**
 * @param <T>  the Base Type for Code View
 * @param <V>  the Base Type for Validation Data
 */
public class Entry<
		T, V extends  Validation<T>>
		extends WriteUtil {

    private boolean clogged = false; // if clogged, userEntry isn't allowed to change
	private String userEntry = "";  // what we get from the file, Never Null
	private AbstractT<T> value;
	private Validation<T> validation;
	
    // ==================================================
    // Constructors
    //
	/**
	 * No empty class creation allowed!
	 */
	@SuppressWarnings("unused")
	private Entry() {}

	/**
	 * create and initialize a new {@code EntryValid} with default value
	 * @param validation the {@code Abstract_ValidData<T>} validation
	 */
	public Entry(Validation<T> validation) {
		setValidationData(validation);
		setValue(validation.getHistory(Default));
	}
	/**
	 * create and initialize a new {@code EntryValid}
	 * @param validation the {@code Abstract_ValidData<T>} validation
	 * @param userEntry the {@code String} userEntry
	 */
	public Entry(Validation<T> validation,
			  String userEntry) {
		setValidationData(validation);
		set(userEntry);
	}
	/**
	 * create and initialize a new {@code EntryValid}
	 * @param validation the {@code Abstract_ValidData<T>} validation
	 * @param value the {@code T} userEntry
	 */
	public Entry(Validation<T> validation,
			AbstractT<T> value) {
		setValidationData(validation);
		setValue(value);
	}
	// ==================================================
    // Overriders
    //
	@Override public String toString() { 
		return getOutputStr();
	}

	// ==================================================
    // Methods to be Overridden
    //
	/**
	 * Set a new codeView and analyze it
	 * @param codeView the new codeView
	 * @return this for chaining purpose 
	 */
	public Entry<T, V> setCodeView(T codeView) {
		value = getValidation().newValue(codeView);
		if (isClogged()) {
			setOutputStr(getUserEntry());
		}
			
		return this;
	}
	/**
	 * Clear the content of the element,
	 * But not the {@code PrintFormat} neither the {@code validation}
	 * @return this for chaining purpose
	 */
	public Entry<T, V> reset() {
		userEntry = "";
		if (value != null) {
			value.reset();
		}	
		return this;
	}
	/**
	 * Ask if something has been set by user entry
	 * @return {@code boolean}
	 */
	boolean isBlankUserEntry() { 
		return userEntry == null || userEntry.isBlank();
	}
	/**
	 * Ask if about the value state (code view)
	 * @return {@code boolean}
	 */
	boolean isBlankValue() {
		return value == null || value.isBlank();
	}
	/**
	 * Ask if about the value state (code view)
	 * @return {@code boolean}
	 */
	boolean isValidValue() {
		return validation.isValidCodeView(value);
	}

	// ==================================================
    // Setters simple
    //
	void clogged(boolean newValue) {
		clogged = newValue;
	}

	void setValidationData(Validation<T> newValidationData) {
		validation = newValidationData;
	}

	/**
	 * Set a new {@code String} userEntry and analyze it
	 * @param newValue the new {@code String} userEntry
	 * @return this for chaining purpose 
	 */
	public Entry<T, V> set(String newValue) {
		userEntry = clean(newValue);
		value = validation.entryAnalysis(getUserEntry(), isClogged());
		return this;
	}

	/**
	 * Set a new {@code AbstractT} value
	 * @param newValue the new {@code AbstractT} value
	 */
	void setValue(AbstractT<T> newValue) {
		if (newValue != null) {
			value = newValue;
			setOutputStr(value.toString());
		} else {
			value = validation.newValue();
			setOutputStr(getUserEntry());
		}
	}
	/**
	 * Set the new preformatted output {@code String}
	 * @param newOutputStr the new value
	 */
	void setOutputStr(String newOutputStr) {
		if (value == null) {
			value = validation.newValue();
		}
		if (isClogged()) {
			value.setUserViewOnly(getUserEntry());
			return;
		}
		value.setUserViewOnly(newOutputStr);
	}

	// ==================================================
    // Getters simple
    //
	/**
	 * ask for value as {@code T}
	 * @return the value
	 */
	public AbstractT<T> getValue() {
		return value;
	}

	boolean isClogged() {
		return clogged;
	}
	
	Validation<T> getValidation() {
		return validation;
	}
	/**
	 * Ask for userEntry as {@code String}
	 * @return the {@code String}, never null
	 */
	String getUserEntry() { 
		return userEntry;
	}	
	/**
	 * Ask for preformatted outputStr as {@code String}
	 * @return the {@code String}
	 */
	String getOutputStr() { 
		if (value == null) {
			return "";
		}
		return value.toString();
//		return value.userView();
	}	
	/**
	 * ask for value in lower case, with first char to upper case,
	 * with every word capitalized if eachWord is true
	 * @param onlyFirstWord if true only the first word is capitalized
	 * @return a {@code String} as requested
	 */
	String toCapitalized(Boolean onlyFirstWord) { 
		return capitalize(getOutputStr(), onlyFirstWord);
	}
	/**
	 * ask for value in lower case, with first char to upper case,
	 * with every word capitalized
	 * @return a {@code String} as requested
	 */
	String toCapitalized() { 
		return capitalize(getOutputStr());
	}
	/**
	 * ask for a stripped in lower case with first char to upper case, never null
	 * @return a {@code String} as requested
	 */
	String toSentence() {
		return PMutil.toSentence(getOutputStr());
	}

	// ==================================================
    // Other Methods
    //
	/**
	 * Remove the comments and clean
	 * @return this for chaining purpose
	 */
	Entry<T, V> removeComment() {
		set(WriteUtil.removeComment(getUserEntry()));
		return this;
	}
	
	// ==================================================
	// Methods using the Abstract methods
	// Just copy and paste in child classes
	// They should work even if not overridden
    //
	/**
	 * Ask for a non <b>null</b> nor <i>empty</i> nor <i>blank</i> value
	 * @param defaultValue value to <b>return</b if <b>null</b>, <i>empty</i> or <i>blank</i> 
	 * @return the value, following the conditions
	 */
	AbstractT<T> getValue(AbstractT<T> defaultValue) {
		if (isBlankValue()) {
			return defaultValue;
		}
		return getValue();
	}
	// Other name for compatibility
	/**
	 * Ask for a non <b>null</b> nor <i>empty</i> nor <i>blank</i> value
	 * @param defaultValue value to <b>return</b if <b>null</b>, <i>empty</i> or <i>blank</i> 
	 * @return the value, following the conditions
	 */
	AbstractT<T> getOrDefault(AbstractT<T> defaultValue) {
			return getValue(defaultValue);
	}
	/**
	 * Ask for a non <b>null</b> nor <i>empty</i> nor <i>blank</i> value
	 * @param defaultValue class object to <b>return</b if <b>null</b>, <i>empty</i> or <i>blank</i> 
	 * @return the class object, following the conditions
	 */
	AbstractT<T> getOrDefault(Entry<T, V> defaultValue) {
		return getValue(defaultValue.getValue());
	}
}
