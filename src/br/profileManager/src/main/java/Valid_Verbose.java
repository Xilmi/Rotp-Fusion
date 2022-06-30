
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

import static br.profileManager.src.main.java.Validation.History.*;

/**
 * For the validation of the configurations Action
 */
class Valid_Verbose extends Validation<String> {
	
	private static final String OPTION_ENABLED  = "OPTION";
	private static final String PARAMETER_ENABLED = "PARAMETER";
	private static final String COMMENT_ENABLED = "COMMENT";

	public  static final String PARAMETER_NAME = "Verbose";
	private static final String DEFAULT_VALUE  = "FULL";

	Valid_Verbose() {
		super(new T_String(DEFAULT_VALUE));
		setDefaultName(PARAMETER_NAME);
		setHistory(Default, DEFAULT_VALUE);
		setHistory(Current, DEFAULT_VALUE);
		
		setCriteria(new ValidationCriteria()
				.isRandomAllowed(false)
				.userViewEquals(false)
				.codeViewEquals(false));

		addOption("NO",
				"No option comments, No parameter comments, no option" ,
				"");
		addOption("OPTION",
				"No option comments, No parameter comments, Show Options" ,
				OPTION_ENABLED);
		addOption("PARAMETER", 
				"No option comments, Parameter comments, Show Options" ,
				OPTION_ENABLED + " " + PARAMETER_ENABLED);
		addOption("FULL", 
				"Option comments, Parameter Comments, Show Options",
				OPTION_ENABLED + " " + PARAMETER_ENABLED + " " + COMMENT_ENABLED);
	}

 	// ==========================================================
    // Nested Classes
    //
	/**
	 * Base for every User Entry Lines
	 */
	static class Line_Verbose extends Lines<String, Valid_Verbose>{

	 	// ==========================================================
	    // Constructors
	    //
		/**
		 * Create a new standard default valued LocalEnable
		 */
		Line_Verbose() {
			super(new Valid_Verbose());
			setValue(DEFAULT_VALUE);
		}

		/**
		 * Create a new standard LocalEnable with a custom value
		 */
		Line_Verbose(String value) {
			super(new Valid_Verbose());
			setValue(value);
		}

		// ==========================================================
		// Getters and Setters
		//
		void setValue(String value) {
			setValue(new T_String(value));
		}
		
		boolean isOptionEnabled() {
			return isValueFromCategory(OPTION_ENABLED);
		}
		
		boolean isSettingEnabled() {
			return isValueFromCategory(PARAMETER_ENABLED);
		}
		
		boolean isCommentEnabled() {
			return isValueFromCategory(COMMENT_ENABLED);
		}
	}
}
