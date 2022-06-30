
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
 * For the validation of the profiles Action
 */

public class Valid_ProfileAction extends Validation<String> {
	static final String LOAD_ENABLED    = "ENABLE_LOAD_LIST";
	static final String RANDOM_ENABLED  = "ENABLE_RANDOM_LIST";
	static final String WRITE_ENABLED   = "ENABLE_WRITE_LIST";
	static final String LAST_ENABLED    = "ENABLE_LAST_LIST"; // Update last Game State
	static final String GAME_ENABLED    = "ENABLE_GAME_LIST";
	static final String SPECIAL_ENABLED = "SPECIAL_LIST";
	/**
	 * User view association with Action Random
	 */
	public static final String ACTION_RANDOM       = "Surprise";
	/**
	 * User view association with Action Get from file and put to GUI
	 */
	public static final String ACTION_FILE_TO_GUI  = "Load";
	/**
	 * User view association with Action Get from file and put to GAME
	 */
	public static final String ACTION_FILE_TO_GAME = "Change";
	/**
	 * User view association with Action Get from GUI and put to File
	 */
	public static final String ACTION_GUI_TO_FILE  = "SaveGui";
	/**
	 * User view association with Action Get from Game and put to File
	 */
	public static final String ACTION_GAME_TO_FILE = "SaveGame";
	/**
	 * User view association with Action Get from Initial and put to File
	 */
	public static final String ACTION_INITIAL_TO_FILE  = "SaveInitial";
	/**
	 * User view association with Action Get from Default and put to File
	 */
	public static final String ACTION_DEFAULT_TO_FILE  = "SaveDefault";
	/**
	 * User view association with Action Get from Default and put to File
	 */
	public static final String ACTION_LAST_TO_FILE  = "SaveLast";
	/**
	 * User view association with Action Get from GUI and update File
	 */
	public static final String ACTION_GUI_UPDATE_FILE  = "GetGUI";
	/**
	 * User view association with Action Get from Game and update File
	 */
	public static final String ACTION_GAME_UPDATE_FILE = "GetGame";
	/**
	 * User view association with Action Get from Initial and update File
	 */
	public static final String ACTION_INITIAL_UPDATE_FILE = "GetInitial";
	/**
	 * User view association with Action Get from Default and update File
	 */
	public static final String ACTION_DEFAULT_UPDATE_FILE = "GetDefault";
	/**
	 * User view association with Action Get from Default and update File
	 */
	public static final String ACTION_LAST_UPDATE_FILE = "GetLast";
	/**
	 * User view association with Parameter Name
	 */
	public  static final String PARAMETER_NAME = "PROFILES ACTIONS";
	private static final String DEFAULT_VALUE  = "";

	Valid_ProfileAction() {
		super(new T_String(DEFAULT_VALUE));
		setDefaultName("None!");
		setHistory(Default, DEFAULT_VALUE);
		setShowHistory(false);
		setShowLocalEnable(false);
		
		setCriteria(new ValidationCriteria()
				.isRandomAllowed(false)
				.userViewEquals(false)
				.codeViewEquals(false));
		
		addOption(ACTION_FILE_TO_GUI,
				"If the key \"L\" is pressed, this profile will change the GUI" ,
				LOAD_ENABLED);
		addOption(ACTION_RANDOM,
				"If the key \"R\" is pressed, this profile will change the GUI..." + NL
				+ "I use it to Randomize, but could be alternate load!" ,
				RANDOM_ENABLED);
		addOption(ACTION_FILE_TO_GAME, 
				"If the key \"X\" is pressed in Load Menu, the loaded Game will be changed" ,
				GAME_ENABLED);
		addOption(ACTION_GUI_TO_FILE, 
				"When a Game is started or if the key \"U\" is pressed, this profile will save the GUI settings" ,
				WRITE_ENABLED);
		addOption(ACTION_GAME_TO_FILE,
				"When a Game is started, this profile will save the Game settings" ,
				WRITE_ENABLED + " " + LAST_ENABLED);
		addOption(ACTION_INITIAL_TO_FILE,
				"When a Game is started or if the key \"U\" is pressed, this profile will save the initial settings" ,
				WRITE_ENABLED);
		addOption(ACTION_DEFAULT_TO_FILE,
				"When a Game is started or if the key \"U\" is pressed, this profile will save the default settings" ,
				WRITE_ENABLED);
		addOption(ACTION_GUI_UPDATE_FILE,
				"When a Game is started or if the key \"U\" is pressed, non empty parameters of this profile will save the GUI settings" ,
				WRITE_ENABLED);
		addOption(ACTION_GAME_UPDATE_FILE, 
				"When a Game is started, non empty parameters of this profile will save the Game settings" ,
				WRITE_ENABLED + " " + LAST_ENABLED);
		addOption(ACTION_INITIAL_UPDATE_FILE,
				"When a Game is started or if the key \"U\" is pressed, non empty parameters of this profile will save the initial settings" ,
				WRITE_ENABLED);
		addOption(ACTION_DEFAULT_UPDATE_FILE,
				"When a Game is started or if the key \"U\" is pressed, non empty parameters of this profile will save the default settings" ,
				WRITE_ENABLED);
//		list.addElement("CLEAR",
//				"Remove every occurrence of this setting" ,
//				"SPECIAL_ENABLED");
	}

 	// --------------------------------------------------------------
    // Nested Class
    //
	/**
	 * Base for every profile line Action declaration
	 */
	static class Line_ProfileAction extends Lines<String, Valid_ProfileAction>{

	 	// --------------------------------------------------------------
	    // Constructors
	    //
		/**
		 * Create a new standard default valued ProfileAction
		 */
		Line_ProfileAction() {
			super(new Valid_ProfileAction());
		}

		// --------------------------------------------------------------
		// Getters and Setters
		//
		boolean isLoadEnabled() {
			return isValueFromCategory(LOAD_ENABLED);
		}
		
		boolean isWriteEnabled() {
			return isValueFromCategory(WRITE_ENABLED);
		}
	}
}
