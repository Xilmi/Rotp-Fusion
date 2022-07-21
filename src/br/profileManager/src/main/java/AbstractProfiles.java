
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

import static br.profileManager.src.main.java.Valid_ProfileAction.*;
import static br.profileManager.src.main.java.Validation.History.*;
import static br.profileManager.src.main.java.PMutil.containsIgnoreCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @param <C> The class that have to go thru the profile manager
 */
public abstract class AbstractProfiles<C> extends WriteUtil {
	// ========================================================================
	// Variables Properties
	//
	protected static final PMconfig PM = new PMconfig();
	// Keep the initializations for Junit test
	private static String breakLine     = "..";
	private static String commentKey    = ";";
	private static String hiddenKey     = "HIDE";
	private static String hideSeparator = "-";
	
	private List<String> defaultUserSettingKeys = new ArrayList<String>(List.of("User", "LastWord"));
	private boolean firstInit = true;

	private LinkedHashMap<String, AbstractParameter<?, ?, C>> parameterNameMap;
	private LinkedHashMap<String, AbstractGroup<C>> groupNameMap;
	protected LinkedHashMap<String, AbstractGroup<C>> groupMap;
	private Parameter_ProfileAction parameterProfileAction;
	private boolean cleanUserKeys = true;

	private AbstractParameter<?, ?, C> currentParameter;
	private String  currentParameterName;
	private boolean currentParameterIsHidden = false;
	private AbstractGroup<C> currentGroup;
		
	// ==================================================
	// Constructors and helpers
	//
	/**
	 * @param jarPath	Path to the configurations files
	 * @param configFileName Name of the optional (PMconfig) configuration file
	 */
	public AbstractProfiles(String jarPath, String configFileName) {
		PM.loadConfig(jarPath, configFileName, jarPath, getFileName());
		PM.sendInfo();
	}
	/**
	 * To be notified the config has been updated
	 */
	static void newConfig(PMconfig PM) {
		breakLine     = PM.getConfig("breakLine");
		commentKey    = PM.getConfig("commentKey");
		hiddenKey     = PM.getConfig("hiddenKey").toUpperCase();
		hideSeparator = PM.getConfig("hideSeparator");
	}
	// ========================================================================
	//  Abstract Methods
	//
	/**
	 * Add all the groups to the Map with an easy key
	 */
	protected abstract void initGroupMap(C clientObject);

	protected abstract String getFilePath();

	protected abstract String getFileName();
	
	protected abstract void createDefaultUserProfiles();

	// ========================================================================
	//  Protected Getters and Setters
	//
	File getProfilePath() {
		return Paths.get(PM.getConfig("profilePath")
						, PM.getConfig("profileFileName"))
						.toFile();
	}
	protected Parameter_ProfileAction parameterProfileAction() {
		return parameterProfileAction;
	}

	protected LinkedHashMap<String, AbstractParameter<?, ?, C>> parameterNameMap() {
		return parameterNameMap;
	}
	// ========================================================================
	//  Public Methods
	//
	/**
   	 * Load The Profile Manager configuration file,
   	 * and save the current profile with the new configuration.
   	 * Or create it if there is none.
   	 */
	public void loadProfileManagerConfig() {
		if (PM.loadConfig()) {
			PM.sendInfo();
			saveProfilesCfg();
		} else {
			PM.saveConfig();
		}
	}
	/**
   	 * Check if User Settings are already initialized with gameOptions
   	 * @return the initialization status
   	 */
	public boolean isInitialized () {
		return !firstInit;
	}
	/**
   	 * Check if the user has set a valid value for ChangeGame
	 * @param parameter the Parameter to check
   	 * @return the Parameter status
   	 */
	public boolean hasValidUserValueForNewGame(String parameter) {
		return (isInitialized() 
				&& hasValidUserValue(parameter, getReadableProfiles()));
	}
	/**
   	 * Check if the user has set a valid value for ChangeGame
	 * @param parameter the Parameter to check
   	 * @return the Parameter status
   	 */
	public boolean hasValidUserValueForChangeGame(String parameter) {
		return (isInitialized() 
				&& hasValidUserValue(parameter, getGameChangingProfiles()));
	}
	/**
   	 * Check if it's OK to use a parameter
	 * @param parameter the Parameter to check
   	 * @return the Parameter status
   	 */
	public boolean isParameterEnabled (String parameter) {
		if (isInitialized()) {
			AbstractParameter<?, ?, C> param = getParameter(parameter);
			if (param != null) {
				return param.isLoadEnabled();
			}
		}
		return false;
	}
	/**
	 * @param name ID of the group
	 * @return the group instance
	 */
	public AbstractGroup<C> getGroup (String name) {
		return groupNameMap.get(name.toUpperCase());
	}
	/**
	 * @param name ID of the parameter
	 * @return the parameter instance
	 */
	public AbstractParameter<?, ?, C> getParameter (String name) {
		return parameterNameMap.get(PMutil.neverNull(name).toUpperCase());
	}
	/**
   	 * Load the profile file to update the Action
   	 * Update with last options values
   	 * Save the new profile file
	 * @param clientObject The class that manage GUI parameters
   	 */
	public void saveGameToFile(C clientObject) {
		loadProfilesCfg(); // in case the user changed load or save actions
		updateFromGameValue(clientObject);
		updateFromGuiValue(clientObject); // Current value needs to be up to date too
		for (String profile : getGameToFileProfiles()) {
			String action = parameterProfileAction.getProfileCodeView(profile.toUpperCase());
			if (containsIgnoreCase(action, ACTION_GAME_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Game, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_GAME_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Game, profile);
				}
			}
		}
		saveProfilesCfg();
	}
	/**
   	 * Load the profile file and memorize first options
	 * @param clientObject The class that manage GUI parameters
   	 */
	public void initAndLoadProfiles(C clientObject) {
		if (firstInit) {
			parameterProfileAction = new Parameter_ProfileAction();
			initGroupMap(clientObject);
			initGroupNameMap();
			initParameterNameMap();
			firstInit = false;
		}
		loadProfilesCfg();
	}
	/**
	 * @param runObject The class that manage Game parameters
	 */
	public void changeGameSettings(C runObject) {
		loadProfilesCfg();
		List<String> settingKeys = getGameChangingProfiles();
		for (AbstractGroup<C> group : groupMap.values()) {
			group.changeGameFileParameters(runObject, settingKeys);
		}
		//Settings.ChangeGameFile = false;
	}
	/**
	 * Load the profile file to update the Action
   	 * Update with last options values (Asked by user)
   	 * Execute User Actions
   	 * Save the new profile file
	 * @param clientObject The class that manage GUI parameters
   	 */
	public void saveGuiToFile(C clientObject) {
		loadProfilesCfg(); // in case the user changed load or save actions
		updateFromGuiValue(clientObject);
		doUserUpdateActions();
		saveProfilesCfg();
	}
	/**
	 * Load the profile file to update the Action
   	 * Update with last options values
   	 * Save the new profile file (without User Action) (Not asked by user)
	 * @param clientObject The class that manage GUI parameters
   	 */
	public void saveLastGuiToFile(C clientObject) {
		loadProfilesCfg(); // in case the user changed load or save actions
		updateFromGuiValue(clientObject);
		saveProfilesCfg();
	}
	/**
   	 * Load and execute "Surprise" profile file
	 * @param clientObject The class that manage GUI parameters
	 * @param group the local group to load (if local)
	 * @param global if not, the local
   	 */
	protected void loadGroupSettings(C clientObject
						, String group, boolean global) {
		loadProfilesCfg();
		List<String> settingKeys = getReadableProfiles();
		if (global) {
			for ( AbstractGroup<C> g : groupMap.values()) {
				g.overrideGuiParameters(clientObject, settingKeys);
			}
			// return; // Need to be finished with local group!
		}
		currentGroup = groupMap.get(group.toUpperCase());
		currentGroup.overrideGuiParameters(clientObject, settingKeys);
	}
	/**
   	 * Load and execute "Surprise" profile file
	 * @param clientObject The class that manage GUI parameters
	 * @param group the local group to load (if local)
	 * @param global if not, the local
   	 */
	protected void loadSurpriseGroupSettings(C clientObject
								, String group, boolean global) {
		loadProfilesCfg();
		List<String> settingKeys = getSurpriseProfiles();
		if (global) {
			for ( AbstractGroup<C> g : groupMap.values()) {
				g.overrideGuiParameters(clientObject, settingKeys);
			}
			return;
		}
		currentGroup = groupMap.get(group.toUpperCase());
		currentGroup.overrideGuiParameters(clientObject, settingKeys);
	}
	/**
   	 * Reset the game options to the selected history setting,
	 * @param clientObject The class that manage GUI parameters
	 * @param group the local group to load (if local)
	 * @param history the history setting to load
	 * @param global if not, the local
   	 */
	protected void loadHistoryOptions(C clientObject, String group
									, History history, boolean global) {
		if (global) {
			for ( AbstractGroup<C> g : groupMap.values()) {
				g.setGuiParameters(history, clientObject);
			}
			return;
		}
		currentGroup = groupMap.get(group.toUpperCase());
		currentGroup.setGuiParameters(history, clientObject);		
	}

	// ========================================================================
	//  Private Methods
	//
	public boolean hasValidUserValue(String parameter, List<String> Profiles) {
		if (isInitialized()) {
			AbstractParameter<?, ?, C> param = getParameter(parameter);
			if (param != null) {
				return param.hasUserValue(Profiles);
			}
		}
		return false;
	}
	// ========================================================================
	// Initializations Methods
	//
	/**
	 * Key Map the group list 
	 */
	private void initGroupNameMap() {
		groupNameMap = new LinkedHashMap<String, AbstractGroup<C>>();
		for (AbstractGroup<C> group : groupMap.values()) {
			for (String profile : group.profileList()) {
				groupNameMap.put(profile, group);
			}
		}
	}
	
	/**
	 * Add all the Settings to the Map with an easy key
	 */
	private void initParameterNameMap() {
		parameterNameMap = new LinkedHashMap<String, AbstractParameter<?, ?, C>>();
		for (AbstractGroup<C> group : groupMap.values()) {
			for (String profile : group.profileList()) {
				parameterNameMap.put(profile, group.getParameter(profile));
			}
		}
	}
	
	// ========================================================================
	// Other Methods
	//
	/**
	 * @return All the current profiles, loaded from file
	 */
	public List<String> getAllProfiles() {
		return parameterProfileAction.getProfileList();
	}
	
	private List<String> getGameToFileProfiles() {
		return parameterProfileAction.getProfileListForCategory(LAST_ENABLED);
	}
	
	private List<String> getReadableProfiles() {
		return parameterProfileAction.getProfileListForCategory(LOAD_ENABLED);
	}
	
	private List<String> getSurpriseProfiles() {
		return parameterProfileAction.getProfileListForCategory(RANDOM_ENABLED);
	}
	
	private List<String> getGameChangingProfiles() {
		return parameterProfileAction.getProfileListForCategory(GAME_ENABLED);
	}
	
	private void loadProfilesCfg() {
		resetAllUserSettings();
		File profilesCfg = getProfilePath();
		if ( profilesCfg.exists() ) {
			try ( BufferedReader in =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(profilesCfg), "UTF-8"));) {
				String line;
				while ((line = in.readLine()) != null) {
					line = line.trim();
					while (line.endsWith(breakLine)) {
						line = mergeLines(line, in.readLine());
					}
					processLine(line.trim());
				}
			}
			catch (FileNotFoundException e) {
				System.err.println(PM.getConfig("profilePath") 
						+ PM.getConfig("profileFileName") + " not found.");
			}
			catch (IOException e) {
				System.err.println("UserPreferences.load -- IOException: "+ e.toString());
			}
			forceCreationMissingProfile(getAllProfiles());
		}
		else {
			// the file does not exist: create a default one
			createDefaultUserProfiles();
			forceCreationMissingProfile(getAllProfiles());
			doUserUpdateActions();
		}
	}
	
	private String mergeLines(String line1, String line2) {
		if (line1.length() < 3) {
			line1 = "";
		} else {
			line1 = line1.substring(0, line1.length()-3);
		}
		if (line2 == null) {
			return line1;
		}
		if (line2.contains(commentKey)) {
			return line1 + line2.strip().split(commentKey, 2)[1];
		}
		return (line1 + line2).strip();
	}
	
	private void processLine(String line) {
		// Test for Emptiness and ignore
		if (line.isEmpty()) {
			return;
		}
		// test for comment and ignore
		if (isComment(line)) {
			return;
		}
		// Test for new setting
		String key = Lines.getKey(line);
		if (key.isBlank()) {
			return;
		}
		// Test for New Setting Section
		if (AbstractParameter.isHeadOfParameter(key) ) {
			String local = Valid_LocalEnable.DEFAULT_VALUE;
			currentParameterIsHidden = false;
			String[] elements = Lines.getValueAsString(line)
					.toUpperCase().split(hideSeparator, 2);
			currentParameterName = elements[0].strip();
			if (elements.length == 2) {
				local = elements[1].strip();
				if (local.equalsIgnoreCase(hiddenKey)) {
					currentParameterIsHidden = true;
				}
			}
			// Test if initial profile list declaration
			if (parameterProfileAction.getParameterName()
					.equalsIgnoreCase(currentParameterName)) {
				currentParameter = parameterProfileAction;
				currentParameterIsHidden = false; // big problem if hidden!
				currentGroup = null;
				return;
			}
			currentGroup = groupNameMap.get(currentParameterName);
			currentParameter = null;
			if (currentGroup != null) {
				currentParameter = currentGroup.getParameter(currentParameterName);
				currentParameter.setLocalEnable(local);
				return;
			}
			return;
		}
		// it's a setting Line
		if (currentParameter != null
				&& !currentParameterIsHidden) {
			currentParameter.addLine(line);
		}
	}
	
	private int saveProfilesCfg() {
		List<String> settingKeys = getAllProfiles();
		if (settingKeys == null || settingKeys.isEmpty()) {
			settingKeys = defaultUserSettingKeys;
		}
		try (FileOutputStream fout = new FileOutputStream(getProfilePath());
		PrintWriter out = new PrintWriter(new OutputStreamWriter(fout, "UTF-8")); ) {
			// SETTING :PRESET ACTIONS
			out.print(parameterProfileAction.toString(settingKeys));
			// Loop thru settings
			for (AbstractGroup<C> group : groupMap.values()) {
				out.print(group.toString(settingKeys, cleanUserKeys));
			}
			return 0;
		}
		catch (IOException e) {
			System.err.println("ProfileMap.save -- IOException: "+ e.toString());
			return -1;
		}
	}
	
	private void resetAllUserSettings() {
		for (AbstractGroup<C> group : groupMap.values()) {
			group.resetAllUserSettings();
		}
	}
	
	private void forceCreationMissingProfile(List<String> profileList) {
		if (PMutil.getForceCreationMissingProfile()) {
			for (AbstractGroup<C> group : groupMap.values()) {
				group.forceCreationMissingProfile(profileList);
			}
		}
	}
	
	void doUserUpdateActions() {
		// Loop Thru User's Keys and perform requested action
		for (String profile : getAllProfiles()) {
			String action = parameterProfileAction.getProfileUserView(profile.toUpperCase());
			if (containsIgnoreCase(action, ACTION_GUI_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Current, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_GAME_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Game, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_INITIAL_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Initial, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_DEFAULT_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Default, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_LAST_TO_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionToFile(Last, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_GUI_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Current, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_GAME_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Game, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_INITIAL_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Initial, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_DEFAULT_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Default, profile);
				}
			}
			if (containsIgnoreCase(action, ACTION_LAST_UPDATE_FILE)) {
				for (AbstractGroup<C> group : groupMap.values()) {
					group.actionUpdateFile(Last, profile);
				}
			}
		}
		saveProfilesCfg();
	}
	
	void updateFromGuiValue(C clientObject) {
		for (AbstractGroup<C> group : groupMap.values()) {
			group.actionTakeGuiCodeView(clientObject);
		}
	}
	
	void updateFromGameValue(C clientObject) {
		for (AbstractGroup<C> group : groupMap.values()) {
			group.actionTakeGameCodeView(clientObject);
		}
	}
	
	// ========================================================================
	// Nested Classes
	//
	protected class Parameter_ProfileAction extends 
			AbstractParameter<String, Valid_ProfileAction, C> {
		// The Class C is not used, but needed for compatibility

		// ------------------------------------------------------------------------
		// Constructors
		//
		protected Parameter_ProfileAction() { 
			super(PARAMETER_NAME, new Valid_ProfileAction());
		}
		
		//	protected Parameter_ProfileAction(String Name) {
		//		super(Name, new Valid_ProfileAction());
		//	}
			
		@Override public void initComments() {
			setHeadComments (
				"			EXTENDED PLAYER'S SETTINGS" + NL +
				"-------------------------------------------------- " + NL +
				" " + NL
				);
			setBottomComments(
				"(---- The last loaded Win)" );
		  }
		
		@Override public AbstractT<String> getFromGame(C clientObject) { 
			return null; // Should never happen
		}
	
		@Override public void putToGame(C runObject, AbstractT<String> userOption) {}
	
		@Override public AbstractT<String> getFromUI(C gO) { 
			return null; // Should never happen
		}
	
		@Override public void putToGUI(C gO, AbstractT<String> userOption) {}
	}
}
