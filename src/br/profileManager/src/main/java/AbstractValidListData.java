package br.profileManager.src.main.java;
//
///*
// * Licensed under the GNU General Public License, Version 3 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     https://www.gnu.org/licenses/gpl-3.0.html
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package br.profileManager.src.main.java;
//
//import java.util.ArrayList;
//import java.util.EnumMap;
//import java.util.List;
//import java.util.Map;
//import static br.profileManager.src.main.java.WriteUtil.History.*;
//
///**
// * Common methods for data validation of List of Values
// * @param <ValueClass> the Value's Base Code View Class
// * @param <ListClass>  the Value's List Code View Class
// */
//@SuppressWarnings("unused") // used as List<ValueClass>
//public abstract class Abstract_ValidListData<ValueClass, ListClass extends List<ValueClass>> 
//						extends Abstract_Valid_Elements<ValueClass>{
//	
//	protected static final String LIST_SEPARATOR = "/";
//	
//	private Map<History, List<ValueClass>> historyMap = new EnumMap<>(History.class);
//	private Abstract_ValidData<ValueClass> validElement; // for single element validation
//	
//    // ==================================================
//    // Constructors and initializers
//    //
//	Abstract_ValidListData() {} // Forbidden constructor
//
//	/**
//	 * Base constructor
//	 * @param validElement should be a well initialized non list validation data
//	 */
//	Abstract_ValidListData(Abstract_ValidData<ValueClass> validElement) {
//		this.validElement = validElement;
//	}
//	
//	// ==================================================
//    // Abstract Methods Request
//    //
//	// ==================================================
//    // Methods extracted from single element validation Request
//    //
//	/**
//	 * Conversion from code view to user view for one element
//	 * <br> For already validated Data
//	 * @param codeView {@code Code View} the value to convert
//	 * @return {@code String} the user view
//	 */
//	String toUserView(List<ValueClass> codeView) {
//		List<String> userView = new ArrayList<String>();
//		for (ValueClass element : codeView) {
//			String value = validElement.getUserView(element);
//			// Check if value is valid
//			if (value != null) { 
//				userView.add(value);
//			} 
//		}
//		return String.join(LIST_SEPARATOR, userView);
//	}
//	
//	/**
//	 * Conversion from user view to code view for one element.
//	 * <br> For List, this include  Validation
//	 * @param codeView {@code String} the value to convert
//	 * @return {@code Code View} the code view
//	 */
//	List<ValueClass> toCodeView(String userView) {
//		return entryValidation(userView);
//	}
//
////	/**
////	 * Validate the limits for the Value class type for one element
////	 * @param value the {@code Code View} Value to validate
////	 * @param lim1  the {@code Code View} first limit
////	 * @param lim2  the {@code Code View} second limit
////	 * @return {@code Code View} Validated Value
////	 */
////	ValueClass validateLimits(ValueClass value,
////										ValueClass lim1, ValueClass lim2) {
////		return validElement.validateLimits(value, lim1, lim2);
////	}
//
//	/**
//	 * Process non Random user entry
//	 * @return {@code Code View} Validated Value
//	 */
//	List<ValueClass> entryValidation(String userEntry) {
//		userEntry = PMutil.clean(userEntry);
//		// First Check for blank values
//		if (userEntry.isBlank()) {
//			if (getValidationCriteria().isBlankAllowed()) {
//				return null;
//			}
//			return getHistoryCodeView(Default);
//		}
//
//		// Then Split the Elements and manage them
//		List<ValueClass> values = new ArrayList<ValueClass>();
//		for (String userView : splitUserView(userEntry)) {
//			ValueClass value = validElement.getCodeView(userView); // Raw conversion
//			// Check if value is valid
//			if (value == null) { 
//				// not Valid Check if part of the list 
//				if (validElement.hasList() 
//						&& validElement.isValidUserEntry(userView)) {
//					values.add(validElement.getCodeView(userView));
//				} 
//				// Nothing Valid, the list will ignore this element
//			} else {
//				// Check for limit before adding the value
//				values.add(validElement.validateLimits(
//							value, getLimits(0), getLimits(1)));
//			}
//		}
//		// Check that there was some good elements !!!
//		if (values.size() == 0) {
//			// ! Empty... Either blank or default
//			if (getValidationCriteria().isBlankAllowed()) {
//				return null;
//			}
//			else {
//				return getHistoryCodeView(Default);
//			}
//		}
//		return values;
//	}	
//
//    //
//	// ===== No Random Allowed Yet =====
//	//
////	/**
////	 * Process Random without parameters
////	 * @return {@code Integer} OutputString
////	 */
////	abstract ValueClass getRandom(ValueClass lim1, ValueClass lim2);
////
////	/**
////	 * Process Random with parameters
////	 * @param parameters {@code String[]} the extra parameters
////	 * @return {@code Code View} OutputString
////	 */
////	abstract List<ValueClass> randomWithLimit(String[] parameters);
////
////	/**
////	 * Process Random without parameters
////	 * @return {@code Code View} OutputString
////	 */
////	abstract ValueClass randomWithoutParameters();
//
//	// ==================================================
//    // Overriders
//    //	
//	/**
//	 * Get the default limit value
//	 * @return the default limit Code View
//	 */
//	@Override ValueClass getDefaultLimit() {
//		return getHistoryCodeView(Default).get(0);
//	}
//
//	// ==================================================
//    // Setters
//    //
//	/**
//	 * Set the "history" Code View
//	 * @param history  Field to be filled
//	 * @param newValue the new "history" Value
//	 */
//
//	protected void setHistoryCodeView(History history, List<ValueClass> newValue) {
//		if (history == Last) { // if in two step to allow breakpoint
//			if (!PMutil.neverNull(historyMap.get(Last)).isBlank()) {
//				return; // Last was already assigned	
//			}
//		}
//		historyMap.put(history, newValue);
//		if (history == Initial) {
//			historyMap.put(Current, newValue);
//		}
//	}
//
//	/**
//	 * Set the "history" User View
//	 * @param history  Field to be filled
//	 * @param newValue the new "history" Value
//	 */
//	protected void setHistoryUserView(History history, String newValue) {
//		setHistoryCodeView(history, toCodeView(newValue));
//	}
//
//	// ==================================================
//    // Getters
//    //
//	/**
//	 * Get the "history" Code View
//	 * @param history  Field to be retrieved
//	 * @return the "history" Code View
//	 */
//	protected List<ValueClass> getHistoryCodeView(History history) {
//		return historyMap.get(history);
//	}
//
//	/**
//	 * Get the "history" User View
//	 * @param history  Field to be retrieved
//	 * @return the "history" Code View
//	 */
//	protected String getHistoryUserView(History history) {
//		return toUserView(historyMap.get(history));
//	}
//	// ==================================================
//    // Other Methods
//    //
//	protected String[] splitUserView (String userView) {
//		if (userView == null) {
//			return null;
//		}
//		return userView.split(LIST_SEPARATOR);
//	}
//	
//	// ===== No Random Allowed Yet =====
//	//
//	
////	/**
////	 * Process Random within Given Limits
////	 * @param parameters {@code String[]} the extra parameters
////	 * @return {@code String} Random Value
////	 */
////	protected List<ValueClass> randomWithInListLimit(String[] parameters) {
////		int min = 0;
////		int max = listSize();
////		// First Limit
////		if (parameters.length >= 1) {
////			min = getUserViewIndex(parameters[0], min);
////		}
////		// Second Limit
////		if (parameters.length >= 2) {
////			max = getUserViewIndex(parameters[1], max);
////		}
////		// get Random
////		int id = PMutil.getRandom(min, max);
////		return getCodeView(id);
////	}
////
////	/**
////	 * Process Random with parameters
////	 * @param parameters {@code String[]} the extra parameters
////	 * @return {@code ValueClass} Output Value
////	 */
////	List<ValueClass> randomWithParameters(String[] parameters) {
////		if (parameters.length > 2) {
////			return randomWithList(parameters);
////		}
////		if (hasList() && isValidUserEntry(parameters[0])) {
////			return randomWithInListLimit(parameters);
////		}
////		return randomWithLimit(parameters);
////	}
////	
////	/**
////	 * Process Random among the given list
////	 * @param parameters {@code String[]} the extra parameters
////	 * @return {@code ValueClass} Random Value
////	 */
////	List<ValueClass> randomWithList(String[] parameters) {
////		int id = PMutil.getRandom(0, parameters.length);
////		return entryValidation(parameters[id]);
////	}
//}
