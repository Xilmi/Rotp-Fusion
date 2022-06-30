
/*
 * Copyright 2015-2020 Ray Fowler
 * 
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

package mod.br.profileManager;

import static br.profileManager.src.main.java.Validation.History.Current;
import static br.profileManager.src.main.java.Validation.History.Default;
import static br.profileManager.src.main.java.Validation.History.Initial;

import java.util.List;

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.PMutil;
import br.profileManager.src.main.java.T_Integer;
import br.profileManager.src.main.java.T_String;
import br.profileManager.src.main.java.Valid_IntegerWithList;
import br.profileManager.src.main.java.Validation;

/**
 * @author BrokenRegistry
 * For Parameters in Race GUI
 */
public class Group_Race extends AbstractGroup <ClientClasses> {

	Group_Race(ClientClasses go) {
		super(go);
	}
	
	@Override protected void initSettingList(ClientClasses go) {
			addParameter(new PlayerRace(go));
			addParameter(new PlayerColor(go));
			addParameter(new PlayerHomeWorld(go));
			addParameter(new PlayerName(go));
	}

	// ==============================================================
	// PLAYER RACE
	//
	static class PlayerRace extends
			AbstractParameter<String, Validation<String>, ClientClasses> {

	    // ========== Constructors and initializer ==========
	    // startingRaceOptions
		PlayerRace(ClientClasses go) { 
			super("PLAYER RACE", 
					new Validation<String>(
							new T_String(go.newOptions().selectedPlayerRace()),
							go.newOptions().startingRaceOptions()));			
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.newOptions().selectedPlayerRace());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedPlayer().race = value.getCodeView(); // Direct to avoid reseting opponents
			go.session().galaxy().empire(0).setRace(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedPlayerRace());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedPlayerRace(value.getCodeView());
			go.options().selectedPlayerRace(value.getCodeView());
		}
		
		@Override public void initComments() {
			setHeadComments(
				" " + NL +
				"--------- Races Game Options ---------" + NL +
				" ");
			setBottomComments(availableForChange());
		}
	}

	// ==============================================================
	// PLAYER COLOR
	//
	static class Valid_Color extends Validation<Integer> {

		static final List<String> EMPIRECOLORS = getEmpireColors();

		private static List<String> getEmpireColors() {
			switch(UserProfiles.baseMod) {
			case BrokenRegistry:
			case Modnar:
			case Xilmi:
				return List.of ("Red", "Green", "Yellow", "Blue", "Orange", "Purple",
								"Aqua", "Fuchsia", "Brown", "White", "Lime", "Grey",
								"Plum", "Light Blue", "Mint", "Olive");
			default:
				return List.of ("Blue", "Brown", "Green",  "Orange", "Pink",
								"Purple", "Red", "Teal", "Yellow", "White");
			}
		}

		Valid_Color(int initial) {
			super(new T_Integer(initial));
			init();
		}

		private void initCriteria() {
			getCriteria().isNullAllowed(false);
		}
		
		private void init() {
			initCriteria();
			for (String color : EMPIRECOLORS) {
				addOption(EMPIRECOLORS.indexOf(color), color);
			}
			setLimits(0 , EMPIRECOLORS.size());
			setDefaultRandomLimits(0 , EMPIRECOLORS.size());
			setHistory(Default, getDefaultColor());
		}

		private String getDefaultColor() {
			switch(UserProfiles.baseMod) {
			case Modnar:
			case Xilmi:
				return "Red";
			default:
				return "Blue";
			}
		}
				
		/**
		 * Generate UserViewList and convert it to capitalized String
		 * @return UserView List in capitalized String
		 */
		@Override public String getOptionsRange() {
			return PMutil.capitalize(getOptionsStringList().toString());
		}
	}

	// ========== Parameter Section ==========
	//
	static class PlayerColor extends 
			AbstractParameter <Integer, Valid_IntegerWithList, ClientClasses> {

		private static List<String> getEmpireColors() {
			switch(UserProfiles.baseMod) {
			case BrokenRegistry:
			case Modnar:
			case Xilmi:
				return List.of ("Red", "Green", "Yellow", "Blue", "Orange", "Purple",
								"Aqua", "Fuchsia", "Brown", "White", "Lime", "Grey",
								"Plum", "Light Blue", "Mint", "Olive");
			default:
				return List.of ("Blue", "Brown", "Green",  "Orange", "Pink",
								"Purple", "Red", "Teal", "Yellow", "White");
			}
		}

	    // ========== Constructors and initializer ==========
	    //
		PlayerColor(ClientClasses go) {
			super("PLAYER COLOR"
					, new Valid_IntegerWithList(
							go.newOptions().selectedPlayerColor()
							, getEmpireColors()));
			setHistoryCodeView(Initial, go.newOptions().selectedPlayerColor());
			setHistoryCodeView(Default, 0);
			setHistory(Current, Initial);
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(go.newOptions().selectedPlayerColor());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {
			go.newOptions().selectedPlayerColor(value.getCodeView());
			go.session().galaxy().empire(0).changeColorId(value.getCodeView());
		}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return getValidation().newValue(go.newOptions().selectedPlayerColor());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			go.newOptions().selectedPlayerColor(value.getCodeView());
			go.options().selectedPlayerColor(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}	
	}

	// ==============================================================
	// PLAYER HOMEWORLD
	//
	static class PlayerHomeWorld extends
	AbstractParameter<String, Validation<String>, ClientClasses> {

	    // ========== Constructors and initializer ==========
	    // startingRaceOptions
		PlayerHomeWorld(ClientClasses go) { 
			super("PLAYER HOMEWORLD", 
					new Validation<String>(
							new T_String(go.newOptions().selectedHomeWorldName())));			
		
			getValidation().getCriteria().isRandomAllowed(false);
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.session().galaxy().empire(0).getHomeWorldName());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedHomeWorldName(value.getCodeView());
			go.session().galaxy().empire(0).setHomeWorldName(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedHomeWorldName());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedHomeWorldName(value.getCodeView());
			go.options().selectedHomeWorldName(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(NL 
					+ "The best would be to let the game choose this value!"
					+ NL);
			setBottomComments(availableForChange());
		}
	}

	// ==============================================================
	// PLAYER NAME
	//
	static class PlayerName extends
	AbstractParameter<String, Validation<String>, ClientClasses> {

	    // ========== Constructors and initializer ==========
	    // startingRaceOptions
		PlayerName(ClientClasses go) { 
			super("PLAYER NAME", 
					new Validation<String>(
							new T_String(go.newOptions().selectedLeaderName())));			

			getValidation().getCriteria().isRandomAllowed(false);
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.session().galaxy().empire(0).leader().name());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedLeaderName(value.getCodeView());
			go.session().galaxy().empire(0).leader().setName(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedLeaderName());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedLeaderName(value.getCodeView());
			go.options().selectedLeaderName(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(NL 
					+ "The best would be to let the game choose this value!"
					+ NL);
			setBottomComments(availableForChange());
		}
	}
}