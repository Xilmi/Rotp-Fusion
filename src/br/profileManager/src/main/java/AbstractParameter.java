
/*
 * Licensed under the GNU General License, Version 3 (the "License");
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

import br.profileManager.src.main.java.Valid_LocalEnable.Line_LocalEnable;
import static br.profileManager.src.main.java.Validation.History.*;
import static br.profileManager.src.main.java.LineString.lineFormat;

/**
 * @param <T>  the Base Code View Class
 * @param <V>  The Value's validation class
 * @param <O> The class that have to go thru the profile manager
 */
public abstract class AbstractParameter<
		T, V extends Validation<T>, O> extends WriteUtil {

	// Former Constant
	// Keep the initializations for Junit test
	private static String optionsHead    = lineFormat(toComment("Options"), "");
	private static String optionsSubHead = lineFormat(toComment("  \" \" "), "");
	private static String historyHead    = lineFormat("¦ History", "");
	private static String historyKey     = "¦ History";
	private static String parameterKey   = "¦==== Parameter";
	private static String historyElementsSeparator  = " ¦ ";
	private static String historyNameValueSeparator = ": ";
	private static String availableForChange = "---- Available for changes in game saves";
	private static String dynamicParameter   = "---- Follow the GUI, not stored in game";
	
	// ==================================================
	// Variables Properties
	//
	private String headComments;
	private String settingComments;
	private String optionsComments;
	private String bottomComments;
	
	private String parameterName;
	private Line_LocalEnable localEnable = new Line_LocalEnable();
	private Validation<T> validation;
	private Block<T, V> userProfiles;
	
	// ==================================================
	// Constructors and helpers
	//
	protected AbstractParameter(String parameterName,
				  Validation<T> valueValidationData) {
		setParameterName(parameterName);
		validation = valueValidationData;
		resetUserProfiles();
		initComments ();
	}
	
	void resetUserProfiles() {
 	userProfiles = new Block<T, V>(validation);
	}
	
	public static void newConfig(PMconfig PM) {
		optionsHead     = lineFormat(toComment(PM.getConfig("optionsKey")), "");
		optionsSubHead  = lineFormat(toComment(PM.getConfig("optionsSubKey")), "");
		historyHead     = lineFormat(PM.getConfig("historyKey"), "");
		historyKey      = PM.getConfig("historyKey");
		parameterKey    = PM.getConfig("parameterKey");
		historyElementsSeparator  = PM.getConfig("historyElementsSeparator");
		historyNameValueSeparator = PM.getConfig("historyNameValueSeparator");
		availableForChange = PM.getConfig("availableForChange");
		dynamicParameter   = PM.getConfig("dynamicParameter");
	}

	// ========================================================================
	// Abstract Methods
	//
	
	protected abstract AbstractT<T> getFromUI (O clientObject);

	protected abstract void putToGUI (O clientObject, AbstractT<T> value);

	protected abstract AbstractT<T> getFromGame (O clientObject);

	protected abstract void putToGame (O clientObject, AbstractT<T> value);

	protected abstract void initComments ();

	// ------------------------------------------------------------------------
	// Getters and Setters
	//
	/**
	 * @return availableForChange
	 */
	protected String availableForChange() {
 	return availableForChange;
	}
	/**
	 * @return dynamicParameter
	 */
	protected String dynamicParameter() {
 	return dynamicParameter;
	}
	/**
	 * @return the value validation class
	 */
	public Validation<T> getValidation() {
 	return validation;
	}
	
	/**
	 * Search for the winning code View
	 * @param profileNames  List of names to check
	 * @return The Value, if one.
	 */
	private AbstractT<T> getWinningCodeView (List<String> profileNames) {
 	AbstractT<T> value = null;
	 	if (localEnable.isLoadEnabled()) {
				// Loop thru profiles, last valid win
		 	for (String profile : profileNames) {	   
		 		value = userProfiles.getValueOrDefault(profile, value);
			}
	  	}
		return value;
	}

	/**
	 * Search for the winning code View and
	 * Override the Game File parameter with it
	 * @param clientObject   the {@code O Object}
	 * @param profileNames  List of names to check
	 */
	public void changeGameFileParameters(
 		O clientObject, List<String> profileNames) {
	 	AbstractT<T> value = getWinningCodeView (profileNames);
	 	// if one valid code View is found: set it
	 	if (value != null) {
	 		putToGame(clientObject, value);
	 	}
	}

	private void overrideGuiParameters(
	 		O clientObject, AbstractT<T> value) {
	 	// if one valid value is found: set it
	 	if (!PMutil.neverNull(value).isBlank()) {
	 		putToGUI(clientObject, value);
	 		// reload GUI and set History Current Value
	 		setFromGuiCodeView(clientObject);
	 	}
	}

	/**
	 * Search for the winning Value and
	 * Override the GUI parameter with it 
	 * @param clientObject   the {@code O Object}
	 * @param profileNames  List of names to check
	 */
	public void overrideGuiParameters(
	 		O clientObject, List<String> profileNames) {
		overrideGuiParameters(clientObject, getWinningCodeView (profileNames));
	}

	protected void putHistoryToGUI(Validation.History history, O clientObject) {
		overrideGuiParameters (clientObject, validation.getHistory(history));
	}

	/**
	 * Check if the "history" Exist
	 * @param history  Field to be retrieved
	 * @return true if not null
	 */
	protected boolean historyIsNull(Validation.History history) {
		return validation.historyIsNull(history);
	}

	/**
	 * Set "history" codeView
	 * @param history   The History case to fill
	 * @param codeView the new codeView
	 */
	protected void setHistoryCodeView(Validation.History history, T codeView) {
		validation.setHistoryCodeView(history, codeView);
	}

	/**
	 * Set "history" codeView List
	 * @param history  The History case to fill
	 * @param codeView the new codeView
	 */
	protected void setHistoryCodeView(Validation.History history, List<T> codeView) {
		validation.setHistoryCodeView(history, codeView);
	}

	/**
	 * Set "history" Value
	 * @param history   The History case to fill
	 * @param value the new value
	 */
	protected void setHistory(Validation.History history, AbstractT<T> newValue) {
		validation.setHistory(history, newValue);
	}

	/**
	 * Copy one History to another
	 * @param history   The History case to fill
	 * @param source    The History to copy
	 */
	protected void setHistory(Validation.History history, Validation.History source) {
		validation.setHistory(history, source);
	}

	/**
	 * Set "history" User View
	 * @param history   The History case to fill
	 * @param value the new value
	 */
	protected void setHistory(Validation.History history, String newValue) {
		validation.setHistory(history, newValue);
	}
	
	/**
	 * Get "history" value
	 * @return The "history" value
	 */
	protected AbstractT<T> getHistory(Validation.History history) {
		return validation.getHistory(history);
	}

	/**
	 * Conditions to set user choice to "history" value:
	 *	- if the key is absent: 
	 *	- if Writing is allowed and value not blank:
	 * Add profile if none
	 * @param history the target Field
	 * @param profile the profile to set
	 */
	public void actionToFile(Validation.History history, String profile) {
			actionToFile(profile, validation.getHistory(history));
	}

	/**
	 * Conditions to set user choice to "history" value:
	 *	- if the key is absent: 
	 *	- if Writing is allowed and value not blank:
	 * Add profile if none
	 * @param history the target Field
	 * @param profile the profile to set
	 */
	public void actionUpdateFile(Validation.History history, String profile) {
			actionUpdateFile(profile, validation.getHistory(history).getUserView());
	}

	/**
	 * Set Limits Value
	 * @param value the new values
	 */
	protected void setLimits(T Limit1, T Limit2) {
		validation.setLimits(Limit1, Limit2);
	}

	/**
	 * Set Default Random Limits Value
	 * @param value the new values
	 */
	protected void setDefaultRandomLimits(T Limit1, T Limit2) {
		validation.setDefaultRandomLimits(Limit1, Limit2);
	}

	/**
	 * Update Last GUI code View 
	 * @param clientObject   the {@code O Object}
 	 */
	public void setFromGuiCodeView(O clientObject) {
		validation.setHistory(Current, getFromUI(clientObject));
	}
	
	/**
	 * Update Last Game CodeView (Computer friendly) 
	 * @param clientObject   the {@code O Object}
 	 */
	public void setFromGameCodeView(O clientObject) {
		validation.setHistory(Game, getFromGame(clientObject));
	}

	/**
	 * @return the Parameter's Name 
	 */
	public String getParameterName() {
		return parameterName;
	}
	
	/**
	 * @param name  the Parameter's Name 
	 */
	private void setParameterName(String name) {
		parameterName = name;
	}
	
	/**
	 * @return Full Profiles list
	 */
	public List<String> getProfileList() {
		return userProfiles.getProfileList();
	}

	/**
	 * Get profile list from for the given category
	 * @param category the {@code String} category to filter with
	 * @return Filtered Profile list
	 */
	public List<String> getProfileListForCategory(String category) {
		return userProfiles.getProfileListForCategory(category);
	}

	/**
	 * Ask for profile codeView, or initial codeView
	 * @param   profile the profile name 
	 * @return  selected Profile user View
	 */
	public T getProfileCodeView(String profile) {
		return getProfileLine(profile).getValue().getCodeView();
	}

	/**
	 * Ask for profile userView, or initial
	 * @param   profile the profile name 
	 * @return  selected Profile user View
	 */
	public String getProfileUserView(String profile) {
		return getProfileLine(profile).getValue().getUserView();
	}

	/**
	 * Ask for profile line, or initial
	 * @return  selected Profile as Gen_Line
	 * @param   profile the profile name 
	 */
	public Lines<T, Validation<T>> getProfileLine(String profile) {
		if (profile != null) { 
			if (userProfiles.isValid(profile)) {
				return userProfiles.getLine(profile);
			}
		}
		profile = "Initial";
		return new Lines<T, Validation<T>>(
				validation)
				.setName(profile)
				.setValue(validation.getHistory(Initial));
	}

	// for default parameters and internal use
	/**
	 * @param name Key
	 * @param value User Entry
	 */
	public void addLine (String name,  AbstractT<T> value) {
		userProfiles.add(name, value);
	}

	/**
	 * @param name Key
	 * @param value User Entry
	 */
	public void addLine (String name, String value) {
		userProfiles.add(name, value);
	}

	// for default parameters and internal use
	/**
	 * @param name Key
	 * @param value User Entry
	 * @param comment Any comment, or null
	 */
	public void addLine (String name, String value, String comment) {
		userProfiles.add(name, value, comment);
	}

	void addLine (String newLine) { // from config files
		if (localEnable.isLineForMe(newLine)) {
			return; // the line has been taken
		}
		if (isHistory(newLine)) {
			return; // the line has been taken
		}
		userProfiles.add(newLine);
	}

	/**
	 * Find "Current" value and assign to "Last" 
	 * @param line the {@code String to process}
	 * @return isHistory?
	 */
	private boolean isHistory(String line) {
		boolean result = false;
		if (historyKey.equalsIgnoreCase(Lines.getKey(line))) {
			result = true;
			String key;
			String value;
			// Split the history elements
			for (String historyElement : 
					Lines.getValueAsString(line).split(historyElementsSeparator)) {
				// Split key and value
				String[] keyValue = historyElement.split(historyNameValueSeparator);
				key = keyValue[0].strip();
				value = "";
				if (keyValue.length >= 2
						&& !keyValue[1].isBlank()) {
					value = keyValue[1].strip();
					// The past history "Current" value become the Last
					if (key.equalsIgnoreCase(Current.toString())) {
						setHistory(Last, value);
					}
					// The past history "Game" is refreshed
					else if (key.equalsIgnoreCase(Game.toString())
							&& historyIsNull(Game)) {
						setHistory(Game, value);
					}
				}
				// loop
			}
		}
		return result;
	}
	/**
	 * Get the Local Enable loading state 
	 * @return loading status
	 */
	public Boolean isLoadEnabled() {
		return localEnable.isLoadEnabled();
	}
	/**
	 * Get the Local Enable writing state 
	 * @return writing status
	 */
	public Boolean isWriteEnabled() {
		return localEnable.isWriteEnabled();
	}
	protected AbstractT<T> elementAnalysis(String userEntry) {
		return getValidation().elementAnalysis(userEntry);
	}
	// ==========================================================
	// Other Methods
	//		
	void forceCreationMissingProfile(List<String> profileList) {
		if (PMutil.getForceCreationMissingProfile()) {
			userProfiles.forceCreationMissingProfile(profileList);
		}
	}

	/**
	 * @param groupCodeViews 
	 * @return parameter as String, ready to be printed
	 */
	public String toString(List<String> groupCodeViews) {
		String out = NL;

		// HEAD COMMENTS
		out += multiLines(headComments
				, " ", commentPrt(), commentPrt(), "", true);

		// SETTING NAME
		out += lineFormat(parameterKey, parameterName)
				.toString() + NL;

		// SETTING COMMENTS
		out += multiLines(settingComments
				, " ", commentPrt(), commentPrt(), "", true);

		// OPTIONS LIST
		out += multiLines(validation.getOptionsRange()
				, " " ,optionsHead, optionsSubHead, "", true);

		// OPTIONS DESCRIPTION
		out += toCommentLine(validation.getOptionsDescription(), 1, 1);
		
		// OPTIONS COMMENTS
		out += multiLines(optionsComments
				, " ", commentPrt(), commentPrt(), "", true);

		// HISTORY
		if (validation.isShowHistory()) {
			out += multiLines(
					Current.toString() + historyNameValueSeparator
					+ getHistory(Current).toString()
					+ historyElementsSeparator
					+ Last.toString() + historyNameValueSeparator
					+ getHistory(Last).toString()
					+ historyElementsSeparator
					+ Initial.toString() + historyNameValueSeparator
					+ getHistory(Initial).toString()
					+ historyElementsSeparator
					+ Default.toString() + historyNameValueSeparator
					+ getHistory(Default).toString()
					+ historyElementsSeparator
					+ Game.toString() + historyNameValueSeparator
					+ getHistory(Game).toString()
					, historyElementsSeparator
					, historyHead
					, historyHead
					, "", true);
		}

		// LOCAL ENABLE
		if (validation.isShowLocalEnabled()) {
			out += localEnable.toString() + NL;
		}

		// USER SETTINGS BLOCK
		out += NL + userProfiles.toString(groupCodeViews) + NL;

		// BOTTOM COMMENTS
		out += multiLines(bottomComments
				, " ", commentPrt(), commentPrt(), "", true);

		out += NL;
 	return out;
	}
	
	private void addProfileIfNone(String profile) {
		userProfiles.addMissing(profile);
	}

  /**
	 * Conditions to set user choice to value:
	 *	 - if Writing is allowed 
	 *	 - if the user choice is absent
	 * Add profile if none
	 * @param profile the profile to set
	 * @param value  the value to set
	 */
	private void actionToFile(String profile, AbstractT<T> value) {
		if (localEnable.isWriteEnabled()) {
			addLine(profile, value.toString());
		}
	}

	/**
	 * Conditions to set user choice to value:
	 *	- if the key is absent: 
	 *	- if Writing is allowed and value not blank:
	 * Add profile if none
	 * @param profile the profile to set
	 * @param value  the value to set
	 */	
	private void actionUpdateFile(String profile, String value) {
		if (localEnable.isWriteEnabled()) {
			addProfileIfNone(profile);
			if (!userProfiles.isBlankValue(profile)) {
				addLine(profile, value);
			}
		}
	}

	protected void setHeadComments(String comments) {
		headComments = comments;
	}

	protected void setSettingComments(String comments) { 
		settingComments = comments;
	}

	protected void setOptionsComments(String comments) { 
		optionsComments = comments;
	}

	protected void setBottomComments(String comments) {
		bottomComments = comments; 
	}

	// ==================================================
	// Static Methods
	//
	/**
	 * Test if the {@code String} announce a new parameter section
	 * @param key the {@code String} to analyze
	 * @return {@code Boolean} <b>true</b> if new parameter section, never null
	 */
	static boolean isHeadOfParameter(String key) {
		key = PMutil.clean(key);
		return key.equalsIgnoreCase(parameterKey);
	}
}
