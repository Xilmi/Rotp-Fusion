
/*
 * Licensed under the GNU General License, Version 3 (the "License");
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


/**
 * Base for every User Entry Lines
 * @param <T> the Value's Code View Class
 * @param <V>  The Value's validation class
 */
public class Lines<T, V extends Validation<T>>
		extends WriteUtil {
	
    // ==================================================
	// Former Constant Properties, linked to config
    //
    private static String keyValueSeparator;
    private static String baseKeyFormat;
    private static String keyValueSeparatorPrt;
    private static String keyFormat;
    private static String keyValueFormat;
    private static String clogId;
    private static String valueSubHead;
    private static String commentSubHead;
	private static String profilesBreakLines;

    // ==================================================
	// Variables Properties
    //
    private boolean clogged = false; // if clogged, userEntry isn't allowed to change
	private String entryName = "none";
	private Entry<T, V> entryValue;
	private String comment = null;

	// ==================================================
    // Constructors
    //
	/**
	 * create and initialize a new {@code LineValid} with default value
	 * @param validationData the {@code Abstract_ValidData<T>} validationData
	 */
	Lines(Validation<T> validationData) {
		entryValue = new Entry<T, V>(validationData);
		setName(validationData.getDefaultName());
	}
	
	Lines(Validation<T> validationData,
			 String line) {	
		entryValue = new Entry<T, V>(validationData);
		newLine(line);
	}

	Lines(Validation<T> validationData,
			 String name,
			 String value) {
		entryValue = new Entry<T, V>(validationData, value);
		setName(name);
	}

	Lines(Validation<T> validationData,
			 String name,
			 AbstractT<T> value) {
		entryValue = new Entry<T, V>(validationData, value);
		setName(name);
	}

	Lines(Validation<T> validationData,
			 String name,
			 String value,
			 String comment) {
		entryValue = new Entry<T, V>(validationData, value);
		setName(name);
		setComment(comment);
	}

	Lines(Validation<T> validationData,
			 String name,
			 AbstractT<T> value, 
			 String comment) {
		entryValue = new Entry<T, V>(validationData, value);
		setName(name);
		setComment(comment);
	}

	@SuppressWarnings("unused")
	private Lines() {} // Forbidden constructor
	
	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		keyValueSeparator    = PM.getConfig("keyValueSeparator");
		keyValueSeparatorPrt = keyValueSeparator + PM.getConfig("valueSpacer");
		profilesBreakLines   = " " + PM.getConfig("continueOnNewLine");
		baseKeyFormat  = "%-" + PM.getConfig("lineSplitPosition") + "s";
		keyFormat      = baseKeyFormat + keyValueSeparatorPrt;
		keyValueFormat = "%-" + PM.getConfig("commentEndPosition") + "s";
		valueSubHead   = String.format(baseKeyFormat, " ") + commentPrt();
		commentSubHead = String.format(keyValueFormat, " ") + commentPrt();
	   clogId = PM.getConfig("clogId");
	}
	// ==================================================
	// Setters
	//
	private void initValue(String newValue) {
		entryValue.set(newValue);
	}

	Lines<T, V> newLine(String line) {
 		if (PMutil.isBlank(line)) {
 			initValue("");
 			return this;
 			}
 		// Get the comment if one
 		setComment(WriteUtil.extractComment(line));
 		// Split the Key and the value
		String[] list = WriteUtil.removeComment(line).split(keyValueSeparator, 2);
		setName(list[0]);
		if (list.length == 2) {
			initValue(list[1]);
		}
		return this;
	}

	/**
	 * set a new {@code String} key
	 * @param newKey the new {@code String} key
	 * @return current object to allow chaining
	 */
	Lines<T, V> setName(String newKey) {
		entryName = PMutil.clean(newKey);
		if (entryName.contains(clogId)) {
			clogged = true;
			entryValue.clogged(true);
			entryName = entryName.replace(clogId, "");
		}
		return this;
	}

	/**
	 * set a new {@code Abstract_U<T>} value
	 * @param newValue the new {@code Abstract_U<T>} Value
	 * @return current object to allow chaining
	 */
	Lines<T, V> setValue(AbstractT<T> newValue) {
		entryValue.setValue(newValue);
		return this;
	}

	/**
	 * set a new {@code String} comment
	 * @param newComment the new {@code String} Comment
	 * @return current object to allow chaining
	 */
	Lines<T, V> setComment(String newComment) {
		comment = newComment;
		return this;
	}
	
	// ==================================================
	// Getters
	//
	/**
	 * @return the entry Name as {@code String}
	 */
	String getName() {
		return entryName;
	}

	/**
	 * @return the entryValue value as {@code Abstract_U<T>}
	 */
	public AbstractT<T> getValue() {
		return entryValue.getValue();
	}

	/**
	 * @param defaultValue The value to return on Blank
	 * @return the entryValue value as {@code Abstract_U<T>}
	 */
	AbstractT<T> getValue(AbstractT<T> defaultValue) {
		return entryValue.getValue(defaultValue);
	}

	/**
	 * @param defaultValue The value to return on Blank
	 * @return the entryValue value as {@code Abstract_U<T>}
	 */
	AbstractT<T> getOrDefault(AbstractT<T> defaultValue) {
		return entryValue.getOrDefault(defaultValue);
	}

	/**
	 * @return the comment value as {@code String}
	 */
	String getComment() {
		return comment;
	}

	/**
	 * Return the entryValue as {@code EntryValid}
	 */
	Entry<T, V> getValueAsEntry() {
		return entryValue;
	}

	/**
	 * Return the Value Validation Data
	 */
	Validation<T> getValidationData() {
		return getValueAsEntry().getValidation();
	}

	// ==================================================
	// Testers
	//
	/**
	 * Check if this entry line is dedicated for this Validation,
	 * if true, set new parameters
	 * @param line the {@code String to analyze}
	 * @return <b>true</b> if line was used
	 */
	boolean isLineForMe(String line) {
		if (getName().equalsIgnoreCase(getKey(line))) {
			newLine(line);
			return true;
		}
		return false;
	}
	
	/**
	 * Ask if something has been set by user entry
	 * @return {@code boolean}
	 */
	boolean isBlankUserEntry() { 
		return entryValue.isBlankUserEntry();
	}

	/**
	 * Ask if about the value state (code view)
	 * @return {@code boolean}
	 */
	boolean isBlankValue() { 
		return entryValue.isBlankValue();
	}

	/**
	 * Ask if about the value state (code view)
	 * @return {@code boolean}
	 */
	boolean isValidValue() { 
		return entryValue.isValidValue();
	}

	/**
	 * Test if the user Entry is part of the category
	 * @param category the {@code String} category to filter with
	 * @return as {@code boolean} never null
	 */
	boolean isUserEntryFromCategory(String category) {
		if (getValueAsEntry() == null) {
			return false;
		}
		return getValidationData().isValidUserView(getValue().getUserEntry(), category);
	}

	/**
	 * Test if the value is part of the category
	 * @param category the {@code String} category to filter with
	 * @return as {@code boolean} never null
	 */
	boolean isValueFromCategory(String category) {
		if (getValueAsEntry() == null) {
			return false;
		}
		return getValidationData().isValidCodeView(getValue(), category);
	}

	/**
	 * Test if the Filter contains the value
	 * @param filter the {@code String} containing filter
	 * @return as {@code boolean} never null
	 */
	boolean isValueInFilter(String filter) {
		if (getValue() == null) {
			return false;
		}
		String value = getValue().toString();
		if (value != null && !value.isEmpty()) {
			return PMutil.containsIgnoreCase(filter, value);
		}
		return false;
	}

	/**
	 * Test if the value contains the filter
	 * @param filter the {@code String} filter to be contained
	 * @return as {@code boolean} never null
	 */
	boolean isFilterInValue(String filter) {
		if (getValue() == null) {
			return false;
		}
		String value = getValue().toString();
		if (value != null) {
			return PMutil.containsIgnoreCase(value, filter);
		}
		return false;
	}

	/**
	 * Test if the value equals the filter
	 * @param filter the {@code String} filter to test
	 * @return as {@code boolean} never null
	 */
	boolean isValueAsFilter(String filter) {
		if (getValue() != null
				&& filter != null) {
			String value = getValue().toString();
			if (value != null) {
				return value.equalsIgnoreCase(filter);
			}
		}
		return false;
	}

	// ==================================================
    // Overriders and overridden
    //
	String nameToString() {
		if (clogged) {
			return entryName + clogId;
		}
		return entryName;
	}

	@Override public String toString() {
		boolean hasComment = false;
		String out = "";
		// add the Key and prepare for value
		out += String.format(keyFormat, nameToString());
		// add the value
		out += PMutil.neverNull(entryValue.toString());
		// check for comment overriding
		if (getValidationData().isShowWithOptions()) {
			comment = getValidationData().getOptionsRange();
		}
		// Check for comment
		if (comment != null && !comment.isBlank()) {
			hasComment = true;
			// format to position comments
			out = String.format(keyValueFormat, out);
			// Manage spaces that may have been removed
			if (!" ".equals(PMutil.getLastChar(out))) {
				out += " ";
			}
			// Add comment
			out += toComment(comment);		
		}
		// Break the long lines
		if (hasComment) {
			return multiLines(out, " ", ""
					, commentSubHead, profilesBreakLines, false);
		}
		return multiLines(out, " ", ""
				, valueSubHead, profilesBreakLines, false);
	}

	// ==================================================
    // Static Methods
    //
	/**
	 * Test if the {@code String} has a key and extract it
	 * @param line the {@code String} to analyze
	 * @return the {@code String} key if one, never null
	 */
	static String getKey(String line) {
		line = PMutil.clean(line);
		if (line.isBlank()) {
			return "";
		}
		return line.split(keyValueSeparator, 2)[0].strip();
	}
	/**
	 * Test if the {@code String} has a value and extract it
	 * @param line the {@code String} to analyze
	 * @return the {@code String} value if one, never null
	 */
	static String getValueAsString(String line) {
		line = PMutil.clean(line);
		if (!line.isBlank()) {
			String[] list = WriteUtil.removeComment(line).split(keyValueSeparator, 2);
			if (list.length == 2) {
				return list[1].strip();
			}
		}
		return "";
	}
}

