
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

import static br.profileManager.src.main.java.Validation.History.Current;
import static br.profileManager.src.main.java.Validation.History.Default;

/**
 * For the validation of the configurations Action
 */
public class Valid_LocalEnable extends Validation<String> {

	private static String clogId;
	// From Valid_ConfigAction
		static final String LOAD_ENABLED    = "ENABLE_LOAD_LIST";
		static final String WRITE_ENABLED   = "ENABLE_WRITE_LIST";
		static final String GAME_ENABLED    = "ENABLE_GAME_LIST";
		static final String GUI_ENABLED     = "ENABLE_GUI_LIST";
	//	static final String SPECIAL_ENABLED = "SPECIAL_LIST";
	//  static final String LOCAL_ENABLED = "LOCAL_LIST";

	/**
	 * Local Enable Key
	 */
	public  static final String PARAMETER_NAME = "¦ LOCAL ENABLE";
	private static final String DEFAULT_VALUE  = "ALL";

	Valid_LocalEnable() {
		super(new T_String(DEFAULT_VALUE));
		setDefaultName(PARAMETER_NAME);
		setHistory(Default, DEFAULT_VALUE);
		setHistory(Current, DEFAULT_VALUE);
		setShowWithOptions(true);
		
		setCriteria(new ValidationCriteria()
				.isRandomAllowed(false)
				.userViewEquals(false)
				.codeViewEquals(false));

		addOption("NO",
				"No actions are allowed in this Setting" ,
				"");
		addOption("ALL",
				"All actions are allowed in this Setting" ,
				LOAD_ENABLED + " " + WRITE_ENABLED + " " + GUI_ENABLED + " " + GAME_ENABLED);
		addOption("SAVE", 
				"Allows actions that change the file" ,
				WRITE_ENABLED);
//		addOption("GUI", 
//				"Allows actions that change GUI and GAMES" ,
//				LOAD_ENABLED + " " + GUI_ENABLED);
//		addOption("GAME", 
//				"Allows actions that change GUI and GAMES" ,
//				LOAD_ENABLED + " " + GAME_ENABLED);
		addOption("LOAD", 
				"Allows actions that change GUI and GAMES" ,
				LOAD_ENABLED + " " + GUI_ENABLED + " " + GAME_ENABLED);
		addOption(clogId,
				"No actions are allowed in this Setting" ,
				"");
	}

	/**
	 * To be notified that config has been updated
	 */
	static void newConfig(PMconfig PM) {
		clogId = PM.getConfig("clogId");
	}
 	// ==========================================================
    // Nested Classes
    //
	/**
	 * Base for every User Entry Lines
	 */
	static class Line_LocalEnable extends Lines<String, Valid_LocalEnable>{

	 	// ==========================================================
	    // Constructors
	    //
		/**
		 * Create a new standard default valued LocalEnable
		 */
		Line_LocalEnable() {
			super(new Valid_LocalEnable());
			setValue(DEFAULT_VALUE);
		}

		/**
		 * Create a new standard LocalEnable with a custom value
		 */
		Line_LocalEnable(String value) {
			super(new Valid_LocalEnable());
			setValue(value);
		}

		// ==========================================================
		// Getters and Setters
		//
		void setValue(String value) {
			setValue(new T_String(value));
		}
		
		boolean isLoadEnabled() {
			return isValueFromCategory(LOAD_ENABLED);
		}
		
		boolean isGuiEnabled() {
			return isValueFromCategory(GUI_ENABLED);
		}
		
		boolean isGameEnabled() {
			return isValueFromCategory(GAME_ENABLED);
		}
		
		boolean isWriteEnabled() {
			return isValueFromCategory(WRITE_ENABLED);
		}
		
//		boolean isLocal() {
//			return isValueFromCategory(LOCAL_ENABLED);
//		}
	}
}
