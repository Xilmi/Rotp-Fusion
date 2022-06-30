
/*
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

import static br.profileManager.src.main.java.Validation.History.Current;
import static br.profileManager.src.main.java.Validation.History.Default;
import static br.profileManager.src.main.java.Validation.History.Initial;
import static br.profileManager.src.main.java.Validation.History.Last;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Common methods for data validation
 * @param <T>  the Base Type for Code View
 */
public class Validation<T> extends OptionValidation<T> {

	/**
	 * List of memorized Values
	 */
	public enum History {
	    /**
	     * The very default settings, whit new installation 
	     */
	    Default,
	    /**
	     * GUI settings from the last time the program was launched
	     */
	    Last,
	    /**
	     * GUI settings at the current launch
	     */
	    Initial,
	    /**
	     * The current GUI settings
	     */
	    Current,
	    /**
	     * Settings from the last time a game was launched
	     */
	    Game
	}
	private static String randomId;
	private static String parametersSeparator;
	private static String listSeparator;

	private final AbstractT<T> factory;
	private final boolean isString;
	private final boolean isBoolean;

	private Map<Validation.History, AbstractT<T>> historyMap = new EnumMap<>(History.class);

	private String  defaultName     = "none";
	private boolean showWithOptions = false;
	private boolean showHistory     = true;
	private boolean showLocalEnable = true;

    // ==================================================
    // Constructors and initializers
    //
	/**
	 * Base Generic Constructor for Validation,
	 * is not an entry list
	 * @param initialValue 
	 */
	public Validation(AbstractT<T> initialValue) {
		this(initialValue, null);
	}
	/**
	 * Generic Constructor for Validation with list initialization
	 * @param initialValue  Initial setting
	 * @param options the list to initialize
	 */
	public Validation(AbstractT<T> initialValue, List<T> options) {
		super(options);
		factory   = initialValue.New();
		isString  = initialValue.getCodeView() instanceof String;
		isBoolean = initialValue.getCodeView() instanceof Boolean;
		setBlankCodeView(initialValue.blankCodeView());
		setHistory(Initial, initialValue);
		setHistory(Default, initialValue); // better not be null!
	}

	private AbstractT<T> clone(AbstractT<T> t) {
		if (t == null) {
			return t;
			}
		return t.clone();
	}
	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		randomId            = PM.getConfig("randomId");
		parametersSeparator = PM.getConfig("parametersSeparator");
		listSeparator       = PM.getConfig("listSeparator");
	}
	// ==================================================
    // Setters
    //
	/**
	 * Set the "history" Value
	 * @param history  Field to be filled
	 * @param newValue the new "history" Value
	 */
	public void setHistory(Validation.History history, AbstractT<T> newValue) {
		if (history == Last) { // if in two step to allow breakpoint
			if (historyMap.get(Last) != null) {
				return; // Last was already assigned	
			}
		}
		historyMap.put(history, clone(newValue));
		if (history == Initial) {
			historyMap.put(Current, clone(newValue));
		}
	}
	/**
	 * Copy one History to another
	 * @param history   The History case to fill
	 * @param source    The History to copy
	 */
	void setHistory(Validation.History history, Validation.History source) {
		setHistory(history, getHistory(source));
	}
	/**
	 * Set the "history" from User View
	 * @param history  Field to be filled
	 * @param userView the new "history" Value
	 */
	protected void setHistory(Validation.History history, String userView) {
		AbstractT<T> value = newValue(getCodeView(userView));
		setHistory(history, value);
	}
	/**
	 * Set the "history" from User View List
	 * @param history  Field to be filled
	 * @param userView the new "history" Value
	 */
	public void setHistory(Validation.History history, List<String> userView) {
		AbstractT<T> value = toValue(userView);
		setHistory(history, value);
	}
	/**
	 * Set the "history" Code View
	 * @param history  Field to be filled
	 * @param codeView the new "history" codeView
	 */
	protected void setHistoryCodeView(Validation.History history, T codeView) {
		AbstractT<T> value = newValue(codeView);
		setHistory(history, value);
	}
	/**
	 * Set the "history" Code View
	 * @param history  Field to be filled
	 * @param codeView the new "history" codeView
	 */
	protected void setHistoryCodeView(Validation.History history, List<T> codeView) {
		AbstractT<T> value = newValue(codeView);
		setHistory(history, value);
	}
	// ==================================================
    // Getters
    //
	/**
	 * Get the default limit value
	 * Nothing better than the default value!
	 * @return the default limit Code View, never <b>null</b>
	 */
	private T getDefaultLimit() {
		return getHistory(Default).getCodeView();
	}
	/**
	 * Get the "history" Value
	 * @param history  Field to be retrieved
	 * @return the "history" Value
	 */
	protected AbstractT<T> getHistory(Validation.History history) {
		AbstractT<T> out = historyMap.get(history);
		if (out == null) {
			return newValue();
		}
		return out.clone();
	}
	/**
	 * Check if the "history" Exist
	 * @param history  Field to be retrieved
	 * @return true if null
	 */
	protected boolean historyIsNull(Validation.History history) {
		return historyMap.get(history) == null;
	}
	/**
	 * Get the code view from User View then set the value
	 * @param userView  the user entry
	 * @return the validated Value
	 */
	protected AbstractT<T> toValue(String userView) {
		AbstractT<T> value = newValue();
		if (userView == null) {
			return value;
		}
		value.setUserViewOnly(userView);
		value.setCodeViewOnly(getCodeView(userView));
		return value;
	}
	/**
	 * Get the code view List from User View List then set the value
	 * @param userViewList  the user entry List
	 * @return the validated Value
	 */
	public AbstractT<T> toValue(List<String> userViewList) {
		AbstractT<T> value = newValue();
		if (userViewList != null) {
			value.setUserViewOnly(userViewList);
			value.setCodeViewOnly(getCodeView(userViewList));
		}
		return value;
	}
	/**
	 * Factory create a new empty Abstract_T<T>
	 * @return the new Abstract_T<T>
	 */
	protected AbstractT<T> newValue() {
		return factory.New(getBlankCodeView());
	}
	/**
	 * Factory create a new Value and set code View
	 * @param codeView  the value
	 * @return the new Abstract_T<T>
	 */
	public AbstractT<T> newValue(T codeView) {
		return factory.New(codeView).setUserViewOnly(getUserView(codeView));
	}
	/**
	 * Factory create a new Value and set code View List
	 * @param codeView  the value
	 * @return the new Abstract_T<T>
	 */
	private AbstractT<T> newValue(List<T> codeView) {
		return factory.New(codeView);
	}
	// ==================================================
    // Random Management Methods
    //
	/**
	 * Process Random within Given Limits
	 * @param parameters {@code String[]} the extra parameters
	 * @return valid Limits
	 */
	protected int[] validateLimits(String[] parameters) {
		int absMin = getOptionLimitIndex(0);
		int absMax = getOptionLimitIndex(1);
		int min = absMin;
		int max = absMax;
		// First Limit
		if (parameters.length >= 1) {
			if (isValidUserView(parameters[0])) {
				min = getUserViewIndex(parameters[0], min);
			} else {
				min = PMutil.toIntegerOrDefault(parameters[0], min);
			}
			min = Math.max(min, absMin);
		}
		// Second Limit
		if (parameters.length >= 2) {
			if (isValidUserView(parameters[1])) {
				max = getUserViewIndex(parameters[1], max);
			} else {
				max = PMutil.toIntegerOrDefault(parameters[1], max);
			}
			max = Math.min(max, absMax);
		}
		return new int[] {min, max};
	}
	/**
	 * Process Random without parameters
	 * @return {@code Integer} OutputString
	 */
	private AbstractT<T> getRandom(T lim1, T lim2) {
		T codeView = factory.random(lim1, lim2);
		return factory.New(codeView);
	}
	/**
	 * Process Random within Given Limits
	 * @param parameters {@code String[]} the extra parameters
	 * @return Random Value
	 */
	protected AbstractT<T> randomWithInListLimit(String[] parameters) {
		int[] limits = validateLimits(parameters);
		// get Random
		int id = PMutil.getRandom(limits[0], limits[1]+1);
		return factory.New(getCodeView(id));
	}
	/**
	 * Process Random with parameters
	 * @param parameters {@code String[]} the extra parameters
	 * @return Random Value
	 */
	private AbstractT<T> randomWithParameters(String[] parameters) {
		if (parameters.length > 2) {
			return randomWithOptions(parameters);
		}
		if (hasOptions()) {
			return randomWithInListLimit(parameters);
		}
		return randomWithLimit(parameters);
	}
	/**
	 * Process Random among the given option list
	 * @param parameters {@code String[]} the extra parameters
	 * @return Random Value
	 */
	protected AbstractT<T> randomWithOptions(String[] parameters) {
		int id = PMutil.getRandom(0, parameters.length);
		return entryValidation(parameters[id]);
	}
	/**
	 * Test if user Entry is asking for a random parameter
	 * @param userEntry the {@code String} to analyze
	 * @return <b>true</b> if is random
	 */
	public static boolean isRandom(String userEntry) {
		userEntry = PMutil.clean(userEntry).toUpperCase();
		if (userEntry.length() >= randomId.length()) {
			return userEntry.substring(0, randomId.length()).equals(randomId); 
		}
		return false;
	}
	/**
	 * Check for extra parameter in Random request
	 * @param userEntry the {@code String} to analyze
	 * @return <b>true</b> if has extra parameters
	 */
	private static boolean hasExtraParameters(String userEntry) {
		return !removeRandomId(userEntry).isBlank();
	}
	/**
	 * Remove the Random word and return the extra parameters
	 * @param userEntry the {@code String} to analyze
	 * @return the extra parameters
	 */
	public static String removeRandomId(String userEntry) {
		userEntry = PMutil.clean(userEntry);
		userEntry = userEntry.substring(randomId.length()).strip();
		// Check for misplaced parametersSeparator()
		if (!userEntry.isEmpty() &&
				userEntry.charAt(0) == parametersSeparator.charAt(0)) {
			userEntry = userEntry.substring(1).strip();
		}
		return userEntry;
	}
	/**
	 * Split the parameter String to a list of parameters
	 * @param parameters the {@code String} to analyze
	 * @return the parameter list
	 */
	public static String[] splitParameters(String parameters) {
		// parameters should already be tested
		return parameters.split(parametersSeparator);
	}
	/**
	 * Process Random with parameters
	 * @param parameters {@code String[]} the extra parameters
	 * @return {@code Code View} OutputString
	 */
	private AbstractT<T> randomWithLimit(String[] parameters) {
		T lim1 = getLimits(0);
		T lim2 = getLimits(1);
		if (parameters.length >= 1) {
			T codeView = factory.toCodeView(parameters[0]);
			AbstractT<T> value = factory.New(codeView);
			if (codeView != null) {
				lim1 = value.validBound(codeView, lim1, lim2);
			} 
		}
		// Second Limit
		if (parameters.length >= 2) {
			T codeView = factory.toCodeView(parameters[1]);
			AbstractT<T> value = newValue(codeView);
			if (codeView != null) {
				lim2 = value.validBound(codeView, getLimits(0), lim2);
			} 
		}
		// get Random
		return getRandom(lim1, lim2);
	}
	/**
	 * Process Random without parameters
	 * @return {@code Code View} Output Value
	 */
	protected AbstractT<T> randomWithoutParameters() {
		if (hasOptions()) {
			int id = PMutil.getRandom(
					getOptionDefaultRandomIndex(0),
					getOptionDefaultRandomIndex(1));
			return newValue(getCodeView(id));
		}
		return getRandom(getDefaultRandomLimits(0), getDefaultRandomLimits(1));
	}
	// ==================================================
    // Main Analysis Methods
    //
	/**
	 * Analyze List user Entry content
	 * @return value
	 */
	protected AbstractT<T> entryAnalysis(String userEntry, boolean clogged) {
		userEntry = PMutil.clean(userEntry);
		AbstractT<T> value;
		List<String> userViewList = new ArrayList<String>();
		List<T> codeViewList      = new ArrayList<T>();
		for (String element : userEntry.split(listSeparator)) {
			value = elementAnalysis(element); // Never null
			if (clogged) {
				userViewList.add(userEntry);
			} else {
				userViewList.add(value.getUserView());
			}
			codeViewList.add(value.getCodeView());
			}
		value = newValue();
		value.setUserViewOnly(userViewList);
		value.setCodeViewOnly(codeViewList);
		return value;
	}
	/**
	 * Analyze user Entry element content
	 * @return value
	 */
	protected AbstractT<T> elementAnalysis(String userEntry) {
		userEntry = PMutil.clean(userEntry);
		AbstractT<T> value = newValue();
		// Random Management
		if ( getCriteria().isRandomAllowed()
				&& isRandom(userEntry)) {
			if (hasExtraParameters(userEntry)) {
				String[] parameters = splitParameters(removeRandomId(userEntry));
				value = randomWithParameters(parameters);
				value.setUserViewOnly(userEntry);
				return value;
			}
			value = randomWithoutParameters();
			value.setUserViewOnly(userEntry);
			return value;
		}
		// Not Random
		value = entryValidation(userEntry);
		return value;
	}
	/**
	 * Process non Random user entry
	 * @return {@code Code View} Validated Value
	 */
	protected @SuppressWarnings("unchecked")
	AbstractT<T> entryValidation(String userEntry) {
		userEntry = PMutil.clean(userEntry);
		// First Check for blank values
		if (userEntry.isBlank()) {
			if (getCriteria().isBlankAllowed()) {
				return newValue(getBlankCodeView());
			}
			return getHistory(Default);
		}
		// Then Check check if part of the list 
		if (hasOptions()) {
			if (isValidUserView(userEntry)) {
				return toValue(userEntry);
			} 
			// Bad entry, then either blank or default
			else if (getCriteria().isBlankAllowed()) {
				return newValue(getBlankCodeView());
			}
			else {
				return getHistory(Default);
			}
			// end of hasList
		}
		// Not on the list! check for String entry
		if (isString) {
			return newValue((T)userEntry);
		}
		// Then Check if value is valid
		T codeView = factory.toCodeView(userEntry); // Raw conversion
		if (codeView != null) {
			AbstractT<T> value = newValue(codeView);
			// Check for limit before returning the value
			value.validBound(getLimits(0), getLimits(1));
			return value;
		}
		// No list! No codeView, Not a String, then either blank or default
		if (getCriteria().isBlankAllowed()) {
			return newValue();
		}
		else {
			return getHistory(Default);
		}
	}
	// ==================================================
    // Setters
    //
	/**
	 * Set the showWithOptions parameter value
	 * @param newValue
	 */
	protected void setShowWithOptions(boolean newValue) {
		showWithOptions = newValue;
	}
	/**
	 * Set the showHistory parameter value
	 * @param newValue
	 */
	protected void setShowHistory(boolean newValue) {
		showHistory = newValue;
	}
	/**
	 * Set the showLocalEnable parameter value
	 * @param newValue
	 */
	protected void setShowLocalEnable(boolean newValue) {
		showLocalEnable = newValue;
	}
	/**
	 * Set the default parameter Name 
	 * @param newValue the new default Parameter
	 */
	protected void setDefaultName(String newName) {
		defaultName = newName;
	}
	// ==================================================
    // Getters
    //
	/**
	 * Get the showWithOptions parameter value
	 * @return newValue
	 */
	protected boolean isShowWithOptions() {
		return showWithOptions;
	}
	/**
	 * Get the showHistory parameter value
	 * @return newValue
	 */
	protected boolean isShowHistory() {
		return showHistory;
	}
	/**
	 * Get the showLocalEnable parameter value
	 * @return newValue
	 */
	protected boolean isShowLocalEnabled() {
		return showLocalEnable;
	}
	/**
	 * Get the default parameter name
	 * @return the current value
	 */
	protected String getDefaultName() {
		return defaultName;
	}
	/**
	 * Get the {@code Code View} limits
	 * @return the limits
	 */
	private T getLimits(int index) {
		if (limits().isEmpty()) {
			return getDefaultLimit();
		}
		if (index < 0) {
			return limits().get(0);
		}
		if (index > limits().size()-1) {
			return limits().get(limits().size()-1);
		}
		return limits().get(index);
	}
	/**
	 * Get the {@code Code View} defaultRandomLimits
	 * index the limit index
	 * @return the limit
	 */
	private T getDefaultRandomLimits(int index) {
		if (defaultRandomLimits().isEmpty()) {
			return getDefaultLimit();
		}
		if (index < 0) {
			return defaultRandomLimits().get(0);
		}
		if (index > defaultRandomLimits().size()-1) {
			return defaultRandomLimits().get(defaultRandomLimits().size()-1);
		}
		return defaultRandomLimits().get(index);
	}
	// ==================================================
    // Other Methods
    //
	/**
	 * @return <b>true</b> if the Validation List is not empty
	 */
	protected boolean hasOptions() { // may be overridden
		return optionList().size() > 0;
	}
 	/**
	 * Test if the code view is part of the validation list
	 * and return the user view
	 * @param codeView the code view to test
	 * @return the user view, "" if none
	 */
	private String getUserView(T codeView) {
		if (codeView == null) {
			return getUserViewOrDefault(codeView, "");
		}
		return getUserViewOrDefault(codeView, codeView.toString()); // Not in the list
	}
	/**
	 * Test if the code view is part of the validation list
	 * and return the user view or default Value
	 * @param codeView the code view to test
	 * @param onWrong  the value to return if not on the list
	 * @return the user view, "" if none
	 */
	private String getUserViewOrDefault(T codeView, String onWrong) {
		if (codeView == null) {
			return onWrong;
		}
		// Try the options list
		for (Options<T> element : optionList()) {
				if (element.isValidCodeView(codeView, getCriteria())) {
				return element.getUserView();
			}
		}
		// Try the direct conversion
		String userView = factory.toUserView(codeView);
		if (userView == null) {
			return onWrong; // Not in the list
		}	
		return userView;
	}
	/**
	 * Generate UserViewList as String
	 * @return UserView List in capitalized String
	 */
	public String getOptionsRange() {
		if (hasOptions()) {
			return PMutil.capitalize(getOptionsStringList().toString());
		}
		if (isBoolean) {
			return PMutil.BOOLEAN_LIST.toString();
		}
		// Then it's Numeric
		return ("[Min=" + getLimits(0).toString()
				+ ", Max=" + getLimits(1).toString()
				+ ", Rnd Low=" + getDefaultRandomLimits(0).toString()
				+ ", Rnd Up=" + getDefaultRandomLimits(1).toString()
				+ "]");
	}
}
