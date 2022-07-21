
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

import java.util.ArrayList;
import java.util.List;

/**
 * Options management for data validation
 * @param <T>  the Base Type for Code View
 */
class OptionValidation<T> extends WriteUtil {

	private List<Options<T>> optionList = new ArrayList<Options<T>>();
	private ValidationCriteria criteria = new ValidationCriteria();
	private List<T> defaultRandomLimits = new ArrayList<T>();
	private List<T> limits = new ArrayList<T>();
	private T blankCodeView = null;

	OptionValidation() {}

	OptionValidation(List<T> options) {
		initList(options);
	}
	
	private void initList(List<T> options) {
		if (options == null) {
			return;
		}
		for (T option : options) {
			this.addOption(option,
					PMutil.suggestedUserViewFromCodeView(option));
		}
	}

	// ------------------------------------------------
    // Basic Setters
    //	
	/**
	 * Set the {@code ValidationCriteria} criteria
	 * @param newCriteria
	 */
	protected void setCriteria(ValidationCriteria newCriteria) {
		criteria = newCriteria;
	}
	/**
	 * Set the {@code Code View} limits
	 * @param Limit1
	 * @param Limit2
	 */
	protected void setLimits(T Limit1, T Limit2) {
		limits = new ArrayList<T>();
		limits.add(Limit1);
		limits.add(Limit2);
	}
	/**
	 * Set the {@code Code View} defaultRandomLimits
	 * @param Limit1
	 * @param Limit2
	 */
	protected void setDefaultRandomLimits(T Limit1, T Limit2) {
		defaultRandomLimits = new ArrayList<T>();
		defaultRandomLimits.add(Limit1);
		defaultRandomLimits.add(Limit2);
	}
	/**
	 * @param blankCodeView the new Code View to set
	 */
	protected void setBlankCodeView(T blankCodeView) {
		this.blankCodeView = blankCodeView;
	}   

	// ------------------------------------------------
	/**
	 * Get the allowed limit index
	 * @param i the limit Index
	 * @return the Option limit Index
	 */
	int getOptionLimitIndex(int i) {
		int lim = -1;
		if (hasOptions()) {
			lim =  optionList.size()-1;
			if (hasLimits()) {
				lim = getCodeViewIndex(limits.get(i), lim);
			}
		}
		return lim;
	}
	/**
	 * Get the allowed Option default random index
	 * @param i the limit Index
	 * @return the Option default random Index
	 */
	int getOptionDefaultRandomIndex(int i) {
		int lim = getOptionLimitIndex(i);
		if (hasOptions() && hasRandomLimits()) {
			if (hasRandomLimits()) {
				lim = getCodeViewIndex(defaultRandomLimits.get(i), lim);
			} 
		}
		return lim;
	}
	/**
	 * Get the two limits
	 * @return the limits
	 */
	List<T> limits() {
		return limits;
	}
	/**
	 * Get the optionList
	 * @return the optionList
	 */
	public List<Options<T>> optionList() {
		return optionList;
	}
	/**
	 * Get the defaultRandomLimits
	 * @return the defaultRandomLimits
	 */
	List<T> defaultRandomLimits() {
		return defaultRandomLimits;
	}
	/**
	 * Get the {@code ValidationCriteria}
	 * @return the criteria
	 */
	public ValidationCriteria getCriteria() {
		return criteria;
	}
	/**
	 * @return the blankCodeView
	 */
	protected T getBlankCodeView() {
		return blankCodeView;
	}
	public List<String> getOptionList() {
		List<String> options = new ArrayList<String>();
		for (Options<T> option : optionList) {
			options.add(option.getUserView());
		}
		return options;
	}
	public List<String> getDescriptionList() {
		List<String> options = new ArrayList<String>();
		for (Options<T> option : optionList) {
			options.add(option.getDescription());
		}
		return options;
	}
	public void setDescriptions(List<String> list) {
		int i = 0;
		for (Options<T> option : optionList) {
			option.setDescription(list.get(i));
			i++;
		}
	}
	// ------------------------------------------------
    // Other Methods List Setters & Getters
    //	
	/**
	 * Test if the user entry is part of the validation list
	 * and return the {@code Code View} value as {@code String}
	 * @param userEntry the value to test
	 * @return the code view, "" if none
	 */
 	List<T> getCodeView(List<String> userViewList) {
		List<T> codeViewList = new ArrayList<T>();
		if (userViewList != null) {
			for (String userView : userViewList) {
				codeViewList.add(getCodeViewOrDefault(
									userView, getBlankCodeView()));
			}
		}
		return codeViewList;
	}
	/**
	 * Test if the user entry is part of the validation list
	 * and return the {@code Code View} value as {@code String}
	 * @param userEntry the value to test
	 * @return the code view, "" if none
	 */
 	T getCodeView(String userEntry) {
		return getCodeViewOrDefault(userEntry, getBlankCodeView());
	}
 	/**
	 * Test if the user entry is part of the validation list
	 * and return the {@code Code View} value as {@code String} or default Value
	 * @param userEntry the value to test
	 * @param onWrong   the value to return if not on the list
	 * @return the code view, onWrong if none
	 */
 	T getCodeViewOrDefault(String userEntry, T onWrong) {
 		if (userEntry != null) {
 			for (Options<T> element : optionList) {
				if (element.isValidUserView(userEntry, criteria)) {
					return element.getCodeView();
				}
			}
 		}
		return onWrong;
	}
	/**
	 *  @return The CodeView from its index
	 */
 	T getCodeView(int index) {
		index = Math.max(0, Math.min(optionList.size()-1, index));
		return optionList.get(index).getCodeView();
	}
	// ==================================================
    // Test Methods
    //
	/**
	 * @return <b>true</b> if the Validation List is not empty
	 */
	private boolean hasOptions() { // never overridden in this class
		return optionList().size() > 0;
	}
	/**
	 * @return <b>true</b> if both limits are set
	 */
	boolean hasLimits() {
		return limits.size() > 1;
	}
	/**
	 * @return <b>true</b> if both default random limits are set
	 */
	boolean hasRandomLimits() {
		return defaultRandomLimits.size() > 1;
	}
	// ==================================================
    // Other Methods
    //
	/**
	 * Test if the user entry is part of the validation list
	 * and return the category
	 * @param userEntry the {@code User View} value to test
	 * @return the category, "" if none
	 */
 	protected String getCategory(String userEntry) {
 		if (userEntry != null) {
 			for (Options<T> element : optionList()) {
 				if (element.isValidUserView(userEntry, getCriteria())) {
 					return element.getCategory();
 				}
 			}
 		}
		return "";
	}
	/**
	 * Test if the user view is part of the validation list
	 * and return its index, or the default one if none
	 * @param userView the {@code String} to search
	 * @param defaultIndex the {@code int} default returned value
	 * @return the {@code int} index
	 */
	int getUserViewIndex(String userView, int defaultIndex) {
		userView = PMutil.clean(userView);
		int index = 0;
		for (Options<T> element : optionList()) {
			if (element.isValidUserView(userView, getCriteria())) {
				return index;
			}
			index++;
		}
		// Not in the List, then it's either a given index or default
		return PMutil.toIntegerOrDefault(userView, defaultIndex);
	}
	/**
	 * Test if the code view is part of the validation list
	 * and return its index, or the default one if none
	 * @param userView the {@code T Class} to search
	 * @param defaultIndex the {@code int} default returned value
	 * @return the {@code int} index
	 */
	int getCodeViewIndex(T codeView, int defaultIndex) {
		if (codeView == null) {
			return defaultIndex;
		}
		int index = 0;
		for (Options<T> element : optionList()) {
			if (element.isValidCodeView(codeView, getCriteria())) {
				return index;
			}
			index++;
		}
		// Not in the List, then it's either a given index or default
		return PMutil.toIntegerOrDefault(codeView.toString(), defaultIndex);
	}
	/**
	 * Test if the user view is part of the category list
	 * @param userEntry the user view to check
	 */
	protected boolean isValidUserView(String UserView) {
		if (UserView != null) {
			for (Options<T> element : optionList()) {
				if (element.isValidUserView(UserView, getCriteria())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Test if the user view is part of the category validation list
	 * @param userEntry the user view to check
	 * @param category the filter to apply
	 */
	protected boolean isValidUserView(String UserView, String category) {
		if (UserView != null) {
			for (Options<T> element : optionList()) {
				if (element.isValidUserView(UserView, category, getCriteria())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Test if the user view is part of the category validation list
	 * @param codeView the user view to check
	 * @param category the filter to apply
	 */
	private boolean isValidCodeView(T codeView, String category) {
		if (codeView != null) {
			for (Options<T> element : optionList()) {
				if (element.isValidCodeView(codeView, category, getCriteria())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Test if the value is part of the category validation list
	 * @param value the value to check
	 * @param category the filter to apply
	 */
	protected boolean isValidCodeView(AbstractT<T> value, String category) {
		if (value != null) {
			boolean result = true;
			for ( T codeView : value.getCodeList()) {
				result &= isValidCodeView(codeView, category);
			}
			return result;
		}
		return false;
	}
	/**
	 * Test if the user view is part of the validation list
	 * @param codeView the user view to check
	 * @param category the filter to apply
	 */
	private boolean isValidCodeView(T codeView) {
		if (codeView != null) {
			for (Options<T> element : optionList()) {
				if (element.isValidCodeView(codeView, getCriteria())) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Test if the value is valid codeView
	 * @param value the value to check
	 */
	protected boolean isValidCodeView(AbstractT<T> value) {
		if (value != null) {
			if (getCriteria().isBlankAllowed() && value.isBlank()) {
				return true;
			}
			boolean result = true;
			for ( T codeView : value.getCodeList()) {
				result &= isValidCodeView(codeView);
			}
			return result;
		}
		return false;
	}
	/**
	 * @return UserViewList to String List
	 */
	protected List<String> getOptionsStringList() {
		List<String> result = new ArrayList<String>();
		for (Options<T> element : optionList()) {
			result.add(element.getUserView());
		}
		return result;
	}
	/**
	 * Generate String with all Options = their description
	 * @return the String, never null
	 */
	public String getOptionsDescription() {
		String result = "";
		String line;
		for (Options<T> element : optionList) {
			line = element.toString();
			if (!line.isBlank()) {
				result += line + NL;
			}
		}
		return result;
	}
	// ------------------------------------------------
    // Validation List Setters & Getters
    //	
	private void autoUpdateLimits() {
		setLimits(getCodeView(0), getCodeView(optionList.size()-1));
		setDefaultRandomLimits(getCodeView(0)
				, getCodeView(optionList.size()-1));
	}

	private void addOption(Options<T> element) {
		optionList.add(element);
		autoUpdateLimits();
	}
	
	protected void addOption(T codeView) {
		addOption(new Options<T>(codeView));
	}
	
	protected void addOption(T codeView, String userView) {
		addOption(new Options<T>(codeView, userView));
	}
	
	protected void addOption(T codeView, String userView, 
					String description, String category) {
		addOption(new Options<T>(codeView, userView, description, category));
	}
	
	protected void addOption(T codeView, String description, String category) {
		addOption(new Options<T>(codeView, description, category));
	}
}
