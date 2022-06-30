//
///**
// * Licensed under the GNU General Public License, Version 3 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *	 https://www.gnu.org/licenses/gpl-3.0.html
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
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.file.Paths;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Properties;
//
////import java.io.File;
////import java.io.IOException;
////import java.util.HashMap;
////import java.util.Map;
////import org.codehaus.jackson.JsonGenerationException;
////import org.codehaus.jackson.map.JsonMappingException;
////import org.codehaus.jackson.map.ObjectMapper;
////import org.codehaus.jackson.map.ObjectWriter;
////import org.codehaus.jackson.util.DefaultPrettyPrinter;
////public class JavaMapToJsonFile {
////  public static void main(String[] args) throws JsonGenerationException,
////      JsonMappingException, IOException {
////    Map<String, Object> person = new HashMap<String, Object>();
////    Map<String, String> address = new HashMap<String, String>();
////    address.put("Vill.", "Dhananjaypur");
////    address.put("Dist.", "Varanasi");
////    address.put("State", "UP");
////    person.put("id", "1");
////    person.put("name", "Arvind");
////    person.put("address", address);
////    ObjectMapper mapper = new ObjectMapper();
////    ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
////    mapper.writeValue(new File("JacksonFile/dataTwo.json"), person);
////    System.out.println("--Done--");
////  }
//
///**
// * @author BrokenRegistry
// * Global parameters, open for configuration
// */
//public class PMconfig2 extends Properties {
//
//	private static final String defaultProfileName = "Profile.cfg";
//	private static final String defaultConfigName  = "PMConfig.xml";
//	
//	interface NewConfig {
//		void newConfig();
//	}
//
//	/**
//	 * Original Version 2020-06-23
//	 */
//	private static final long serialVersionUID = LocalDate
//			.parse("2022-06-23")
//			.toEpochDay();
//	private static List<NewConfig> listeners = new ArrayList<NewConfig>();
//	private static final PMconfig2 config = new PMconfig2();
//
//	PMconfig2() {
//		setProperty("commentKey",		";");
//		setProperty("commentSpacer",	 " ");
//		setProperty("keyValueSeparator", ":");
//		setProperty("valueSpacer",	     " ");
//		setProperty("separatorSymbol",   "=");
//		setProperty("separatorSpacer",   " ");
//		setProperty("parameterKey",	     "¦ Parameter");
//		setProperty("historyKey",		 "¦ History");
//		setProperty("optionsKey",		 "Options");
//		setProperty("optionsSubKey",	 "  \" \" ");
//		setProperty("historyNameValueSeparator", ": ");
//		setProperty("historyElementsSeparator",  " ¦ ");
//		setProperty("parametersSeparator", ",");
//		setProperty("listSeparator",	   "/");
//		setProperty("randomId",	"RANDOM");
//		setProperty("clogId",   "·");
//		
//		setProperty("availableForChange", "---- Available for changes in game saves");
//		setProperty("dynamicParameter", "---- Follow the GUI, not stored in game");
//
//		setProperty("lineSplitPosition",  "16");
//		setProperty("commentEndPosition", "30");
//		setProperty("maxLineLength",	  "80");
//
//		setProperty("configPath",      "");
//		setProperty("configFileName",  defaultConfigName);
//		setProperty("profilePath",     "");
//		setProperty("profileFileName", defaultProfileName);
//	}
//
//	/**
//	 * @return the config
//	 */
//	public PMconfig2 config() {
//		return config;
//	}
//	/**
//	 * To add a listener to NewConfig
//	 * @param toAdd
//	 */
//	public static void addListener(NewConfig toAdd) {
//		listeners.add(toAdd);
//	}
//	/**
//	 * To notify all config user to update
//	 */
//	public static void sendInfo() {
//		// Notify everybody that may be interested.
//		for (NewConfig hl : listeners) {
//			hl.newConfig();
//		}
//		// For Static Field in abstract classes...
//		// No needs to update them for every child!
//		WriteUtil.newConfig();
//		AbstractT.newConfig();
//		LineString.newConfig();
//		Options.newConfig();
//		Validation.newConfig();
//		Valid_LocalEnable.newConfig();
//		Lines.newConfig();
//	}
//	
//	/**
//	 * @return the file is loaded
//	 */
//	public static boolean loadConfig() {
//		return loadConfig(
//				config.getProperty("configPath"),
//				config.getProperty("configFileName"),
//				config.getProperty("profilePath"),
//				config.getProperty("profileFileName"));
//	}
//	/**
//	 * @param configPath
//	 * @param configFileName
//	 * @return the file is loaded
//	 */
//	public static boolean loadConfig(String configPath
//									, String configFileName) {
//		return loadConfig(
//				configPath,
//				configFileName,
//				config.getProperty("profilePath"),
//				config.getProperty("profileFileName"));
//	}
//	/**
//	 * @param configPath
//	 * @param configFileName
//	 * @param profilePath
//	 * @param profileFileName
//	 * @return the file is loaded
//	 */
//	public static boolean loadConfig(
//			  String configPath
//			, String configFileName
//			, String profilePath
//			, String profileFileName) 
//	{
//		// update the Files Path
//		setConfigFilePath(configPath, configFileName);
//		setProfileFilePath(profilePath, profileFileName);
//		File file = Paths.get(configPath, configFileName).toFile();
//
//		if (file.exists()) {
//			// Try to load the file
//			if (isXML(file)) {
//				try {
//					config.loadFromXML(new FileInputStream(file));
//					return true;
//				} catch (IOException e) {
//					e.printStackTrace();
//				}				
//			} else {
//				try(FileReader fileReader = new FileReader(file)){
//				    config.load(fileReader);
//				    return true;
//				} catch (IOException e) {
//				    e.printStackTrace();
//				}				
//			}
//		}
//		return false;
//	}
//	/**
//	 * Save the current configuration
//	 */
//	public static void saveConfig() {
//		saveConfig(config.getProperty("configPath")
//				 , config.getProperty("configFileName"));
//	}
//	/**
//	 * Save the current configuration
//	 * @param configPath
//	 * @param configFileName
//	 */
//	public static void saveConfig(String configPath, String configFileName) {
//		File file = Paths.get(configPath, configFileName).toFile();
//		if (isXML(file)) {
//			try {
//				config.storeToXML(new FileOutputStream(file), "Testing now!");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}			
//		} else {
//			try(FileWriter output = new FileWriter(file, Charset.forName("UTF-8"))){
//			    config.store(output, "These are properties");
//			} catch (IOException e) {
//			    e.printStackTrace();
//			}			
//		}
//	}
//	/**
//	 * @param profilePath
//	 * @param profileFileName
//	 */
//	public static void setProfileFilePath(String profilePath, String profileFileName) {
//		config.setProperty("profilePath", profilePath);
//		config.setProperty("profileFileName", profileFileName);
//	}
//	/**
//	 * @param configPath
//	 * @param configFileName
//	 */
//	public static void setConfigFilePath(String configPath, String configFileName) {
//		config.setProperty("configPath", configPath);
//		config.setProperty("configFileName", configFileName);
//	}
//	/**
//	 * @param key the property to retrieve
//	 * @return The String property
//	 */
//	public static String getConfig(String key) {
//		return config.getProperty(key);
//	}
//	/**
//	 * @param key the property to retrieve
//	 * @return The Integer property
//	 */
//	public static Integer getIntConfig(String key) {
//		return Integer.decode(config.getProperty(key));
//	}
//	/**
//	 * @param key the property to retrieve
//	 * @param property The String property
//	 */
//	public static void set(String key, String property) {
//		config.setProperty(key, property);
//	}
//	/**
//	 * @param key the property to retrieve
//	 * @param property The String property
//	 */
//	public static void set(String key, Integer property) {
//		config.setProperty(key, property.toString());
//	}
//	/**
//	 * Test if the file has an extention xml compatible
//	 * @param file
//	 */
//	private static boolean isXML(File file) {
//		return getExtension(file.toString()).get().equalsIgnoreCase("xml");
//	}
//	/**
//	 * Extract the filename extension
//	 * @param filename
//	 */
//	private static Optional<String> getExtension(String filename) {
//	    return Optional.ofNullable(filename)
//	      .filter(f -> f.contains("."))
//	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
//	}
//}
