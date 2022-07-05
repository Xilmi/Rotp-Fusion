
/**
 * Licensed under the GNU General Public License, Version 3 (the "License");
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author BrokenRegistry
 * Global parameters, open for configuration
 */
public class PMconfig {

	interface NewConfig {
		void newConfig();
	}
	private static final String defaultProfileName = "Profile.cfg";
	private static final String defaultConfigName  = "ProfileManager.json";
	private static List<NewConfig> listeners = new ArrayList<NewConfig>();
	private final LinkedHashMap<String, String> config = new LinkedHashMap<String, String>();

	// ==================================================
    // Constructors
    //
	PMconfig() {
		config.put("commentKey",		";");
		config.put("commentSpacer",	    " ");
		config.put("breakLine",         "..");
		config.put("keyValueSeparator", ":");
		config.put("valueSpacer",	    " ");
		config.put("separatorSymbol",   "=");
		config.put("separatorSpacer",   " ");
		config.put("optionsSubKey",	    "  \" \" ");
		config.put("historyNameValueSeparator", ": ");
		config.put("historyElementsSeparator",  " ¦ ");
		config.put("parametersSeparator", ",");
		config.put("listSeparator",	      "/");
		config.put("====================", "==========");
		config.put("clogId",    "·");
		config.put("randomId",	"RANDOM");
		config.put("parameterKey",	    "¦==== Parameter");
		config.put("hiddenKey",	        "Hide");
		config.put("hideSeparator",     "-");
		config.put("historyKey",		"¦ History");
		config.put("optionsKey",		"Options");
		config.put("--------------------", "----------");
		
		config.put("availableForChange", "---- Available for changes in game saves");
		config.put("dynamicParameter",   "---- Follow the GUI, not stored in game");
		config.put("++++++++++++++++++++", "++++++++++");

		config.put("lineSplitPosition",  "16");
		config.put("commentEndPosition", "30");
		config.put("maxLineLength",	     "80");
		config.put("####################", "##########");

		config.put("configPath",      "");
		config.put("configFileName",  defaultConfigName);
		config.put("profilePath",     "");
		config.put("profileFileName", defaultProfileName);
	}
	// ==================================================
    // Initializations Methods
    //
	/**
	 * To notify all config user to update
	 */
	public void sendInfo() {
		// Notify everybody that may be interested.
		for (NewConfig hl : listeners) {
			hl.newConfig();
		}
		// For Static Field in abstract classes...
		// No needs to update them for every child!
		// Be careful about the sequence!
		WriteUtil.newConfig(this);
		AbstractT.newConfig(this);
		LineString.newConfig(this);
		Lines.newConfig(this);
		Options.newConfig(this);
		Validation.newConfig(this);
		Valid_LocalEnable.newConfig(this);
		AbstractParameter.newConfig(this);
		AbstractProfiles.newConfig(this);
	}
	/**
	 * @return the file is loaded
	 */
	public boolean loadConfig() {
		return loadConfig(
				config.get("configPath"),
				config.get("configFileName"),
				config.get("profilePath"),
				config.get("profileFileName"));
	}
	/**
	 * @param configPath
	 * @param configFileName
	 * @return the file is loaded
	 */
	public boolean loadConfig(String configPath
									, String configFileName) {
		return loadConfig(
				configPath,
				configFileName,
				config.get("profilePath"),
				config.get("profileFileName"));
	}
	/**
	 * @param configPath
	 * @param configFileName
	 * @param profilePath
	 * @param profileFileName
	 * @return the file is loaded
	 */
	@SuppressWarnings("unchecked")
	public boolean loadConfig(
			  String configPath
			, String configFileName
			, String profilePath
			, String profileFileName) 
	{
		// First update the Files Path
		setConfigFilePath(configPath, configFileName);
		setProfileFilePath(profilePath, profileFileName);
		File file = new File(configPath, configFileName);

		if (file.exists()) {
			// Try to load the file
		    ObjectMapper mapper = new ObjectMapper();
		    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    LinkedHashMap<String, String> newConfig;
		    try {
		    	newConfig = mapper.readValue(file, LinkedHashMap.class);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		    // Then it's OK for the user to remove parameter from the config file!
		    // And new parameters may be added without breaking compatibility
		    config.putAll(newConfig);
	    	return true;			
		}
		return false;
	}
	/**
	 * Save the current configuration
	 */
	public void saveConfig() {
		saveConfig(config.get("configPath")
				 , config.get("configFileName"));
	}
	/**
	 * Save the current configuration
	 * @param configPath
	 * @param configFileName
	 */
	public void saveConfig(String configPath, String configFileName) {
		File file = new File(configPath, configFileName);
	    ObjectMapper mapper = new ObjectMapper();
	    String json="";
	    
	    try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		try (FileOutputStream fout = new FileOutputStream(file);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(fout, "UTF-8")); ) {
			out.print(json);
		}
		catch (IOException e) {
			System.err.println("ProfileMap.save -- IOException: "+ e.toString());
		}
	}

	// ==================================================
    // Setters
    //
	/**
	 * To add a listener to NewConfig
	 * @param toAdd
	 */
	public static void addListener(NewConfig toAdd) {
		listeners.add(toAdd);
	}
	/**
	 * @param profilePath
	 * @param profileFileName
	 */
	public void setProfileFilePath(String profilePath, String profileFileName) {
		config.put("profilePath", profilePath);
		config.put("profileFileName", profileFileName);
	}
	/**
	 * @param configPath
	 * @param configFileName
	 */
	public void setConfigFilePath(String configPath, String configFileName) {
		config.put("configPath", configPath);
		config.put("configFileName", configFileName);
	}
	/**
	 * @param key the property to retrieve
	 * @param property The String property
	 */
	public void set(String key, String property) {
		config.put(key, property);
	}
	/**
	 * @param key the property to retrieve
	 * @param property The String property
	 */
	public void set(String key, Integer property) {
		config.put(key, property.toString());
	}
	// ==================================================
    // Getters
    //
	/**
	 * @param key the property to retrieve
	 * @return The String property
	 */
	public String getConfig(String key) {
		return config.get(key);
	}
	/**
	 * @param key the property to retrieve
	 * @return The Integer property
	 */
	public Integer getIntConfig(String key) {
		return Integer.decode(config.get(key));
	}
}
