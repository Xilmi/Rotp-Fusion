
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

package mod.br.profileManager;
import static br.profileManager.src.main.java.Valid_ProfileAction.*;
import static br.profileManager.src.main.java.Validation.History.*;

import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractProfiles;
import br.profileManager.src.main.java.Valid_LocalEnable;
import rotp.Rotp;
import rotp.ui.UserPreferences;

/**
 * <ClientClass> The class that have to go thru the profile manager
 */
public class UserProfiles extends AbstractProfiles<ClientClasses> {
    
	static enum BaseMod {
		Original, Governor, Modnar, Xilmi, BrokenRegistry
	}
	
	private static final String BR_GROUP_NAME = "BR";
	static final BaseMod baseMod = BaseMod.BrokenRegistry;
	
	// ==================================================
	// Constructors and helpers
	//
	/**
	 * @param jarPath	Path to the configurations files
	 * @param configFileName Name of the optional (PMconfig) configuration file
	 */
	public UserProfiles(String jarPath, String configFileName) {
		super(jarPath, configFileName);
	}
	// ========================================================================
	//  Public Methods
	//
	/**
   	 * Load the configuration file to update the Action
   	 * Update with last Loaded Game options values
   	 * Save the new configuration file
	 * @param key the key to process
	 * @param global Global or Local ?
	 * @param group group name
	 * @param clientObject The class that manage GUI parameters
	 * @return key to local remaining processing
   	 */
	public boolean processKey(int key, boolean global,
			String group, ClientClasses clientObject) {
		boolean refresh = false;
		switch (key) {
		case KeyEvent.VK_B: // "B" = Load Broken Registry User Profiles
			if(global) { // For Random
				loadSurpriseGroupSettings(clientObject, group, false);
			} else {
				loadGroupSettings(clientObject, group, false);
			}
			refresh = true;
			break;
		case KeyEvent.VK_D: // "D" = Reload Default Profiles
			loadHistoryOptions(clientObject, group, Default, global);
			refresh = true;
			break;
		case KeyEvent.VK_F: // "F" = Reload History Former (Last) Profiles
			loadHistoryOptions(clientObject, group, Last, global);
			refresh = true;
			break;
		case KeyEvent.VK_G: // "G" = Reload History Game Profiles
			loadHistoryOptions(clientObject, group, Game, global);
			refresh = true;
			break;
		case KeyEvent.VK_I: // "I" = Reload Initial Profiles
			loadHistoryOptions(clientObject, group, Initial, global);
			refresh = true;
			break;
		case KeyEvent.VK_L: // "L" = Load GUI User Profiles
		case KeyEvent.VK_P: // "P" = Load GUI User Profiles
			loadGroupSettings(clientObject, group, global);
			refresh = true;
			break;
		case KeyEvent.VK_R: // "R" = Load GUI Surprise Profiles
			loadSurpriseGroupSettings(clientObject, group, global);
			refresh = true;
			break;
		case KeyEvent.VK_S: // "S = Save Remnant.cfg
			UserPreferences.save();
			break;
		case KeyEvent.VK_U: // "U" = Update User Presets
			saveGuiToFile(clientObject);
			break;
		}
		if (refresh) {
			UserPreferences.save();
		}
		return refresh;
	}	
	// ========================================================================
	//  Abstract Methods
	//
	@Override protected String getFilePath () {
		return Rotp.jarPath(); 
	}

	@Override protected String getFileName () {
		return "Profiles.cfg";
	}

	/*
	 * Add all the groups to the Map with an easy key
	 */
	@Override protected void initGroupMap(ClientClasses options) {
      groupMap = new LinkedHashMap<String, AbstractGroup<ClientClasses>>();
      groupMap.put("RACE",        new Group_Race(options));
      groupMap.put("GALAXY",      new Group_Galaxy(options));
      groupMap.put("ADVANCED",    new Group_Advanced(options));
      groupMap.put("MODNAR",      new Group_Modnar(options));
      groupMap.put("GOVERNOR",    new Group_Governor(options));
      groupMap.put(BR_GROUP_NAME, new Group_BrokenRegistry(options));
	}

	@Override protected void createDefaultUserProfiles() {
		String random = PM.getConfig("randomId").toLowerCase();
		parameterProfileAction().addLine("Continuation"
				, ACTION_GUI_TO_FILE + " " + ACTION_FILE_TO_GUI
				, "To retrieve the last session configuration. Press \"L\" to load this profile");

		parameterProfileAction().addLine("MyConfig"
				, ACTION_FILE_TO_GUI
				, "Adjust this manually, with your preferences");

		parameterProfileAction().addLine("ChangeGame"
				, ACTION_FILE_TO_GAME
				, "This profile change Game When Loaded by pressing \"X\", add "
						+ ACTION_GUI_TO_FILE
						+ " to be able to update this profile from the GUI");

		parameterProfileAction().addLine("FullRandom"
				, ACTION_RANDOM
				, "All parameter preset to random! Load by pressing \"R\", add or replace by "
						+ ACTION_FILE_TO_GUI 
						+ " to also allow it to be loaded with \"L\"");

		parameterProfileAction().addLine("MyRandom"
				, ACTION_RANDOM
				, "For your customized random! Load by pressing \"R\", add or replace by "
						+ ACTION_FILE_TO_GUI 
						+ " to also allow it to be loaded with \"L\"");

		parameterProfileAction().addLine("Vanilla"
				, ACTION_DEFAULT_TO_FILE
				, "remove the settings you don't want vanilla, replace "
				+ ACTION_DEFAULT_TO_FILE + " by " + ACTION_DEFAULT_UPDATE_FILE
				+ " and " + ACTION_FILE_TO_GUI
				+ " to keep some vanilla configuration");

		parameterProfileAction().addLine("LastWord·"
				, ACTION_FILE_TO_GUI + " " + ACTION_RANDOM
				, "For the parameters you never want to be changed."
						+ " Keep at the end of the list. The \"" 
						+ PM.getConfig("clogId")
						+ "\" prevent it to be mistakenly changed");
		
		// Fill the Random
		for (AbstractParameter<?, ?, ClientClasses> parameter : parameterNameMap().values()) {
			parameter.addLine("FullRandom", random);
		}
		// Some specialized parameters
		getParameter("AUTOPLAY").addLine("LastWord", "Off", "Only activated thru GUI");
		getParameter("PLAYER HOMEWORLD").addLine(
				Valid_LocalEnable.PARAMETER_NAME, "LOAD" );
		getParameter("PLAYER NAME").addLine(
				Valid_LocalEnable.PARAMETER_NAME, "LOAD" );
		
		getParameter("MAXIMIZE EMPIRES SPACING").addLine("Random", "NO",
				"Not Random, not yet");
		
		// Special random with comments
		getParameter("PLAYER RACE").addLine("MyRandom", random,
				"Full random");
		getParameter("PLAYER COLOR").addLine("MyRandom"
				, random + " Green, Lime",
				"2 values = a range from option list");
		getParameter("GALAXY SHAPE").addLine("MyRandom",
				random + " Rectangle, Ellipse, Spiral, Spiralarms",
				"a limited choice");
		getParameter("GALAXY SIZE").addLine("MyRandom", "",
				"Nothing changed by this profile");
		getParameter("DIFFICULTY").addLine("MyRandom"
				, random + " 1, 4",
				"a range from option list");
		getParameter("OPPONENT AI").addLine("MyRandom"
				, random + " Base, Xilmi, Xilmi"
				, "2 chances to have Xilmi vs Base");
		getParameter("NB OPPONENTS").addLine("MyRandom"
				, random + " 3, 6"
				, "a custom range");
		getParameter("GALAXY AGE").addLine("MyRandom",
				random + " Young, Young, Old, Old",
				"Only 2 choices... Not a range");
		getParameter("NEBULAE").addLine("MyRandom"
				, random + " 1, 4"
				, "Range = Rare .. Common (first option = 0)");	
		getParameter("AI HOSTILITY").addLine("MyRandom"
				, random + " 0, 3");
		getParameter("ALWAYS STAR GATES").addLine("MyRandom", "Yes", "Not Random!");
	}
}
