
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

import static br.profileManager.src.main.java.PMutil.genericTest;
import static br.profileManager.src.main.java.PMutil.suggestedUserViewFromCodeView;

/**
 * Single element for validation list
 * @param <T> the Value's Code View Class
 */
class Options<T> extends WriteUtil {

	private static String separatorSymbol;
	private static String separatorSpacer;
	private static int    lineSplitPosition;

	private String description;
	private String category ;
	private T codeView; // Computer Format
	private String userView; // Human Format
	
    // ------------------------------------------------
    // Constructors
    //
	Options(T codeView) {
		this(codeView, suggestedUserViewFromCodeView(codeView), "", "");
	}

	Options(T codeView, String userView) {
		this(codeView, userView, "", "");
	}

	Options(T codeView, String description, String category) {
		this(codeView, suggestedUserViewFromCodeView(codeView), description, category);
	}

	Options(T codeView, String userView, String description, String category) {
		setCategory(category);
		setDescription(description);
		setCodeView(codeView);
		setUserView(userView);
	}
	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		separatorSymbol   = PM.getConfig("separatorSymbol");
		separatorSpacer   = PM.getConfig("separatorSpacer");
		lineSplitPosition = PM.getIntConfig("lineSplitPosition");
	}
	// ------------------------------------------------
    // Other Methods
    //
	/**
	 * Test if codeView is is recognized as known codeView
	 * @param codeViewToTest the {@code String} to test
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return {@code boolean} <b>true</b> if recognized
	 */
	boolean isValidCodeView(T codeViewToTest,
							ValidationCriteria criteria) {
		return genericTest(codeViewToTest.toString(),
						   codeView.toString(),
						   criteria.codeViewIsCaseSensitive(),
						   criteria.codeViewEquals());
	}

	/**
	 * Test if codeView is part of the category validation list
	 * @param codeViewToTest the {@code T} to test
	 * @param category the {@code String} category filter
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return  {@code boolean} <b>true</b> if recognized
	 */
	boolean isValidCodeView(T codeViewToTest,
							String category,
							ValidationCriteria criteria) {
		return isValidCodeView(codeViewToTest, criteria)
				&& isValidCategory(category, criteria);
	}
	
	/**
	 * Test if user entry is recognized as known element
	 * @param userEntry the {@code String} to test
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return {@code boolean} <b>true</b> if recognized
	 */
	boolean isValidUserView(String userEntry,
							 ValidationCriteria criteria) {
		return genericTest(userEntry,
						   userView,
						   criteria.userViewIsCaseSensitive(),
						   criteria.userViewEquals());
	}

	/**
	 * Test if user entry is part of the category validation list
	 * @param userEntry the {@code String} to test
	 * @param category the {@code String} category filter
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return  {@code boolean} <b>true</b> if recognized
	 */
	boolean isValidUserView(String userEntry,
							 String category,
							 ValidationCriteria criteria) {
		return isValidUserView(userEntry, criteria) 
				&& isValidCategory(category, criteria);
	}
	
	/**
	 * Test if category is recognized as known element
	 * @param categoryToTest the {@code String} to test
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return {@code boolean} <b>true</b> if recognized
	 */
	boolean isValidCategory(String categoryToTest,
							ValidationCriteria criteria) {
		return genericTest(category,
						   categoryToTest,
						   criteria.categoryIsCaseSensitive(),
						   criteria.categoryEquals());
	}

	/**
	 * Test if category is recognized as a known member 
	 * @param categoryToTest the {@code String} to test
	 * @param criteria {@code ValidationCriteria} test criteria
	 * @return {@code boolean} <b>true</b> if recognized
	 */
	boolean isMember(String categoryToTest, 
					 ValidationCriteria criteria) {
		return genericTest(category,
						   categoryToTest,
						   criteria.categoryIsCaseSensitive(),
						   false);
	}
	
	/**
	 * @return string with formated userView = description,
	 * or empty if no description
	 */
	@Override public String toString() {
		if (description.isBlank()) {
			return "";
		}
		return multiLines(description,
				String.format(keyFormat(), userView),
				String.format(extFormat(), " ".repeat(userView.length())));
	}
	// ------------------------------------------------
    // Getters
    //
	String getCategory() {
		return category;
	}

	String getDescription() {
		return description;
	}

	T getCodeView() {
		return codeView;
	}

	String getUserView() {
		return userView;
	}

	// ------------------------------------------------
    // Setters
    //
	private void setCategory(String newCategory) {
		category = newCategory ;
	}
	private void setDescription(String newDescription) {
		description = newDescription;
	}
	private void setCodeView(T newCodeView) {
		codeView = newCodeView;
	}
	private void setUserView(String newUserView) {
		userView = newUserView;
	}

	// ==========================================================
    // Other Private Methods
    //
	private static Integer separatorPosition() {
		return lineSplitPosition - commentPrt().length();
	}	
    private static String keyFormat() { 
		return  "%-"  + separatorPosition().toString() + "s"
                + separatorSymbol + separatorSpacer;
	}
    private static String extFormat() { 
		return  "%-"  + separatorPosition().toString() + "s"
                + separatorSpacer + separatorSpacer;
	}
}
