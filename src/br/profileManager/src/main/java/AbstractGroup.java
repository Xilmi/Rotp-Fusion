
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @param <C> The class that have to go thru the profile manager
 */
public abstract class AbstractGroup<C> extends WriteUtil {

	//  ========================================================================
	// Variables Properties
	//
	LinkedHashMap<String, AbstractParameter<?, ?, C>>
		parameterNameMap = new LinkedHashMap<String, 
								AbstractParameter<?, ?, C>>();

	//  ========================================================================
	// Constructor
	//
	protected AbstractGroup(C clientObject) {
		initSettingList(clientObject);
	}
	// ========================================================================
	// Abstract Methods
	//
	protected abstract void initSettingList(C clientObject);

	// ========================================================================
	// Public and packages Methods
	//
	/**
	 * @param clientObject The class that manage GUI parameters
	 */
	public void actionTakeGuiCodeView(C clientObject) {
		for (AbstractParameter<?, ?, C>
					parameter : parameterNameMap.values() ) {
			parameter.setFromGuiCodeView(clientObject); 
		}
	}
	/**
	 * @param clientObject The class that manage GUI parameters
	 */
	public void actionTakeGameCodeView(C clientObject) {
		for (AbstractParameter<?, ?, C> 
					parameter : parameterNameMap.values() ) {
			parameter.setFromGameCodeView(clientObject);
		}
	}
	/**
	 * @param history  Field to get from
	 * @param profileName the profile name
	 */
	public void actionToFile(Validation.History history, String profileName) {
		for (AbstractParameter<?, ?, C> 
					parameter : parameterNameMap.values() ) {
			parameter.actionToFile(history, profileName);
		}
	}
	/**
	 * @param history  Field to get from
	 * @param profileName the profile name
	 */
	public void actionUpdateFile(Validation.History history, String profileName) {
		for (AbstractParameter<?, ?, C>
					parameter : parameterNameMap.values() ) {
			parameter.actionUpdateFile(history, profileName);
		}
	}
	/**
	 * @return the profile name list
	 */
	public List<String> profileList() {
		return new ArrayList<String>(parameterNameMap.keySet());
	 }
	/**
	 * @param profileName the profile name
	 * @return Selected parameter
	 */
	public AbstractParameter<?, ?, C> getParameter(String profileName) {
		return parameterNameMap.get(profileName);
	}
	/**
	 * @param profileList the profile name
	 * @param cleanProfiles to remove the unlisted profiles
	 * @return parameters group as String, ready to be printed
	 */
	public String toString(List<String> profileList, boolean cleanProfiles) {
		String out = "";
		for (AbstractParameter<?, ?, C>
					parameter : parameterNameMap.values() ) {
			if (cleanProfiles) {
				out += parameter.toString(profileList);
			}
			else {
				List<String> keySet = new ArrayList<String>(profileList);
				keySet.addAll(parameter.getProfileList());
				out += parameter.toString(keySet);
			}
		}
		return out;
	}
	/**
	 * @param profileList the profile name
	 * @return parameters group as String, ready to be printed
	 */
	public String toString(List<String> profileList) {
		return toString(profileList, false);
	}

	void forceCreationMissingProfile(List<String> profileList) {
		if (PMutil.getForceCreationMissingProfile()) {
			for (AbstractParameter<?, ?, C>
						parameter : parameterNameMap.values() ) {
				parameter.forceCreationMissingProfile(profileList);
			}
		}
	}
	// ========================================================================
	// Initialization Methods
	//
	/**
	 * @param parameter the parameter block to process
	 */
	protected void addParameter(
			AbstractParameter<?, ?, C> parameter) {
		parameterNameMap.put(parameter.getParameterName(), parameter);
	}
	/**
	 * Partial reset, values only
	 */
	void resetAllUserSettings() {
		for (AbstractParameter<?, ?, C>
					parameter : parameterNameMap.values() ) {
			parameter.resetUserProfiles();
		}
	}
	/**
	 * @param clientObject The class that manage GUI parameters
	 * @param profileList the profile name
	 */
	public void overrideGuiParameters(
				C clientObject, List<String> profileList) {
		// Loop thru settings
		for (AbstractParameter<?, ?, C>
						parameter : parameterNameMap.values() ) {
			parameter.overrideGuiParameters(clientObject, profileList);
		}
	}
	/**
	 * @param runObject The class that manage Game parameters
	 * @param profileList the profile name
	 */
	public void changeGameFileParameters(
				C runObject, List<String> profileList) {
		// Loop thru settings
		for (AbstractParameter<?, ?, C> 
					parameter : parameterNameMap.values() ) {
			parameter.changeGameFileParameters(runObject, profileList);
		}
	}
	/**
	 * @param history  Field to get from
	 * @param clientObject The class that manage GUI parameters
	 */
	public void setGuiParameters(Validation.History history, C clientObject) {
		for (AbstractParameter<?, ?, C> 
					parameter : parameterNameMap.values() ) {
			parameter.putHistoryToGUI(history, clientObject);
		}
	}
}
