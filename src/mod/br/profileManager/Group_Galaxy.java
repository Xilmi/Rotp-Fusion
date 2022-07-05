
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

import static br.profileManager.src.main.java.Validation.History.Default;
import static br.profileManager.src.main.java.Validation.History.Initial;
import static rotp.model.game.IGameOptions.SHAPE_BULLSEYE;
import static rotp.model.game.IGameOptions.SHAPE_CLUSTER;
import static rotp.model.game.IGameOptions.SHAPE_ELLIPTICAL;
import static rotp.model.game.IGameOptions.SHAPE_FRACTAL;
import static rotp.model.game.IGameOptions.SHAPE_GRID;
import static rotp.model.game.IGameOptions.SHAPE_LORENZ;
import static rotp.model.game.IGameOptions.SHAPE_MAZE;
import static rotp.model.game.IGameOptions.SHAPE_RECTANGLE;
import static rotp.model.game.IGameOptions.SHAPE_SHURIKEN;
import static rotp.model.game.IGameOptions.SHAPE_SPIRAL;
import static rotp.model.game.IGameOptions.SHAPE_SPIRALARMS;
import static rotp.model.game.IGameOptions.SHAPE_SWIRLCLUSTERS;
import static rotp.model.game.IGameOptions.SHAPE_TEXT;

import java.util.ArrayList;
import java.util.List;

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.T_Integer;
import br.profileManager.src.main.java.T_String;
import br.profileManager.src.main.java.Validation;
import mod.br.AddOns.RaceFilter;
import rotp.model.empires.Empire;
import rotp.model.galaxy.GalaxyBullseyeShape;
import rotp.model.galaxy.GalaxyClusterShape;
import rotp.model.galaxy.GalaxyEllipticalShape;
import rotp.model.galaxy.GalaxyFractalShape;
import rotp.model.galaxy.GalaxyGridShape;
import rotp.model.galaxy.GalaxyLorenzShape;
import rotp.model.galaxy.GalaxyMazeShape;
import rotp.model.galaxy.GalaxyRectangularShape;
import rotp.model.galaxy.GalaxyShurikenShape;
import rotp.model.galaxy.GalaxySpiralArmsShape;
import rotp.model.galaxy.GalaxySpiralShape;
import rotp.model.galaxy.GalaxySwirlClustersShape;
import rotp.model.galaxy.GalaxyTextShape;
import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * For Parameters in Galaxy GUI
 */
public class Group_Galaxy extends  AbstractGroup <ClientClasses> {

	Group_Galaxy(ClientClasses go) {
	   super(go, getHeadComments());
	}
	private static String getHeadComments() {
		return  " " + NL
				+ "------------- Galaxy Options -------------" + NL
				+ "";
	}

	@Override protected void initSettingList(ClientClasses go) {
		addParameter(new GalaxyShape(go));
		addParameter(new GalaxySize(go));
		addParameter(new ShapeOption(go, "SHAPE RECTANGLE OPTION 1"
				, SHAPE_RECTANGLE, 1, GalaxyRectangularShape.options1));
		addParameter(new ShapeOption(go, "SHAPE RECTANGLE OPTION 2"
				, SHAPE_RECTANGLE, 2, GalaxyRectangularShape.options2));
		addParameter(new ShapeOption(go, "SHAPE ELLIPTICAL OPTION 1"
				, SHAPE_ELLIPTICAL, 1, GalaxyEllipticalShape.options1));
		addParameter(new ShapeOption(go, "SHAPE ELLIPTICAL OPTION 2"
				, SHAPE_ELLIPTICAL, 2, GalaxyEllipticalShape.options2));
		addParameter(new ShapeOption(go, "SHAPE SPIRAL OPTION 1"
				, SHAPE_SPIRAL, 1, GalaxySpiralShape.options1));
		addParameter(new ShapeOption(go, "SHAPE SPIRAL OPTION 2"
				, SHAPE_SPIRAL, 2, GalaxySpiralShape.options2));
		addParameter(new ShapeOption(go, "SHAPE TEXT OPTION 1"
				, SHAPE_TEXT, 1, GalaxyTextShape.options1));
		addParameter(new ShapeOption(go, "SHAPE TEXT OPTION 2"
				, SHAPE_TEXT, 2, GalaxyTextShape.options2));
		addParameter(new ShapeOption(go, "SHAPE LORENZ OPTION 1"
				, SHAPE_LORENZ, 1, GalaxyLorenzShape.options1));
		addParameter(new ShapeOption(go, "SHAPE LORENZ OPTION 2"
				, SHAPE_LORENZ, 2, GalaxyLorenzShape.options2));
		addParameter(new ShapeOption(go, "SHAPE FRACTAL OPTION 1"
				, SHAPE_FRACTAL, 1, GalaxyFractalShape.options1));
		addParameter(new ShapeOption(go, "SHAPE FRACTAL OPTION 2"
				, SHAPE_FRACTAL, 2, GalaxyFractalShape.options2));
		addParameter(new ShapeOption(go, "SHAPE MAZE OPTION 1"
				, SHAPE_MAZE, 1, GalaxyMazeShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE MAZE OPTION 2"
//				, SHAPE_MAZE, 2, GalaxyMazeShape.options2));
		addParameter(new ShapeOption(go, "SHAPE SHURIKEN OPTION 1"
				, SHAPE_SHURIKEN, 1, GalaxyShurikenShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE SHURIKEN OPTION 2"
//				, SHAPE_SHURIKEN, 2, GalaxyShurikenShape.options2));
		addParameter(new ShapeOption(go, "SHAPE BULLSEYE OPTION 1"
				, SHAPE_BULLSEYE, 1, GalaxyBullseyeShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE BULLSEYE OPTION 2"
//				, SHAPE_BULLSEYE, 2, GalaxyBullseyeShape.options2));
		addParameter(new ShapeOption(go, "SHAPE GRID OPTION 1"
				, SHAPE_GRID, 1, GalaxyGridShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE GRID OPTION 2"
//				, SHAPE_GRID, 2, GalaxyGridShape.options2));
		addParameter(new ShapeOption(go, "SHAPE CLUSTER OPTION 1"
				, SHAPE_CLUSTER, 1, GalaxyClusterShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE CLUSTER OPTION 2"
//				, SHAPE_CLUSTER, 2, GalaxyClusterShape.options2));
		addParameter(new ShapeOption(go, "SHAPE SWIRLCLUSTERS OPTION 1"
				, SHAPE_SWIRLCLUSTERS, 1, GalaxySwirlClustersShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE SWIRLCLUSTERS OPTION 2"
//				, SHAPE_SWIRLCLUSTERS, 2, GalaxySwirlClustersShape.options2));
		addParameter(new ShapeOption(go, "SHAPE SPIRALARMS OPTION 1"
				, SHAPE_SPIRALARMS, 1, GalaxySpiralArmsShape.options1));
//		addParameter(new ShapeOption(go, "SHAPE SPIRALARMS OPTION 2"
//				, SHAPE_SPIRALARMS, 2, GalaxySpiralArmsShape.options2));
		addParameter(new Difficulty(go));
		addParameter(new OpponentAI(go));
		addParameter(new NbOpponent(go));	
		addParameter(new GuiRaceFilter(go));
		addParameter(new GameRaceFilter(go));
		addParameter(new GuiPresetOpponent(go));
		addParameter(new StartPresetOpponent(go));
		addParameter(new GuiAIFilter(go));  
		addParameter(new GameAIFilter(go)); 
		addParameter(new GuiPresetAI(go));  
		addParameter(new StartPresetAI(go)); 
	}

	// ========== Common Methods ==========
	//
	private static List<String> getRaceOptionList(ClientClasses go) {
		List<String> list = go.newOptions().startingRaceOptions();
		list.add("null");
		list.add("gui");
		list.add("game");
		return list;
	}

	private static List<String> getAIOptionList(ClientClasses go) {
		List<String> list = go.newOptions().specificOpponentAIOptions();
		list.add("gui");
		list.add("game");
		return list;
	}

	private static String getInitialAI(ClientClasses go) {
		List<String> list = go.newOptions().specificOpponentAIOptions();
		return list.get(list.size()-2);
	}

	private static List<String> getRaceFromGUI(IGameOptions options) {
		List<String> list = new ArrayList<String>() ;
		String race;
		int lim = options.selectedNumberOpponents();
		for (int i=0; i<lim; i++) {
			race = options.selectedOpponentRace(i);
			if (race == null) {
				race = IGameOptions.OPPONENT_AI_BASE;
			}
			list .add(race);
		}
		return list;
	}

	private static List<String> getAIFromGUI(IGameOptions options) {
		List<String> list = new ArrayList<String>() ;
		String ai;
		int lim = options.selectedNumberOpponents();
		for (int i=0; i<lim; i++) {
			ai = options.specificOpponentAIOption(i);
			if (ai == null) {
				ai = "";
			}
			list .add(ai);
		}
		return list;
	}

	// ==============================================================
	// SHAPE OPTION
	//
	static class ShapeOption extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		private final String shapeCodeView;
		private final int optionLevel;
		// ========== Constructors and initializer ==========
		//
		ShapeOption(ClientClasses go, String name, String codeView
				, int level, List<String> options) { 
			super(name, new Validation<String>(new T_String(options.get(0)), options));
			shapeCodeView = codeView;
			optionLevel = level;
			setHistory(Default, options.get(0));
		}
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(""); // No way to know
		}
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			if (go.newOptions().selectedGalaxyShape() == shapeCodeView) {
				switch (optionLevel) {
				case 1:
					return new T_String(go.newOptions().selectedGalaxyShapeOption1());
				case 2:
					return new T_String(go.newOptions().selectedGalaxyShapeOption2());
				}
			}
			return new T_String("");
		}
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			if (go.newOptions().selectedGalaxyShape() == shapeCodeView) {
				switch (optionLevel) {
				case 1:
					go.newOptions().selectedGalaxyShapeOption1(value.getCodeView());
					go.options().selectedGalaxyShapeOption1(value.getCodeView());
					return;
				case 2:
					go.newOptions().selectedGalaxyShapeOption2(value.getCodeView());
					go.options().selectedGalaxyShapeOption2(value.getCodeView());
					return;
				}
			}
		}
		@Override public void initComments() {}
	}
	// ==============================================================
	// GALAXY SHAPE
	//
	static class GalaxyShape extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ========== Constructors and initializer ==========
		//
		GalaxyShape(ClientClasses go) { 
			super("GALAXY SHAPE",
					new Validation<String>(
							new T_String(go.newOptions().selectedGalaxyShape()),
							go.newOptions().galaxyShapeOptions()));			

			setHistory(Default, "Rectangle"); // Ray Choice
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.newOptions().selectedGalaxyShape());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {

		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedGalaxyShape());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedGalaxyShape(value.getCodeView());
			go.options().selectedGalaxyShape(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// GALAXY SIZE
	//
	static class GalaxySize extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ========== Constructors and initializer ==========
		//
		GalaxySize(ClientClasses go) {
			super("GALAXY SIZE",
					new Validation<String>(
							new T_String(go.newOptions().selectedGalaxySize()),
							go.newOptions().galaxySizeOptions()));			

		setHistory(Default, "Small"); // Ray Choice
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.newOptions().selectedGalaxySize());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {

		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedGalaxySize());
		}
		@Override
		public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedGalaxySize(value.getCodeView());
			go.options().selectedGalaxySize(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	 
	// ==============================================================
	// DIFFICULTY
	//
	static class Difficulty extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ========== Constructors and initializer ==========
		//
		Difficulty(ClientClasses go) {
			super("DIFFICULTY",
					new Validation<String>(
							new T_String(go.newOptions().selectedGameDifficulty()),
							go.newOptions().gameDifficultyOptions()));			

		setHistory(Default, "Easy"); // Ray Choice
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.newOptions().selectedGameDifficulty());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
//			go.getGuiObject().selectedGameDifficulty(codeView);
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedGameDifficulty());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedGameDifficulty(value.getCodeView());
			go.options().selectedGameDifficulty(value.getCodeView());
		}
		
		@Override public void initComments() {
//					setBottomComments(AVAILABLE_FOR_CHANGE);
			}
	}
	// ==============================================================
	// OPPONENT AI
	//
	static class OpponentAI extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ========== Constructors and initializer ==========
		//
		OpponentAI(ClientClasses go) {
			super("OPPONENT AI", 
					new Validation<String>(
							new T_String(go.newOptions().selectedOpponentAIOption()),
							go.newOptions().opponentAIOptions()));			

			setHistory(Default, "Base"); // Ray Choice
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.newOptions().selectedOpponentAIOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {

		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.newOptions().selectedOpponentAIOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.newOptions().selectedOpponentAIOption(value.getCodeView());
			go.options().selectedOpponentAIOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	
	// ==============================================================
	// NB OPPONENTS
	//
	static class NbOpponent extends
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		// ========== Constructors and initializer ==========
		//
		NbOpponent(ClientClasses go) {
			super("NB OPPONENTS", 
					new Validation<Integer>(
							new T_Integer(go.newOptions().selectedNumberOpponents())));			

			setHistoryCodeView(Default, 3); // Ray Choice
			Integer min = 0;
			Integer max = go.newOptions().maximumOpponentsOptions();
			setLimits(min, max);
			setDefaultRandomLimits(1, max);
		}

		// ========== Overriders ==========
		//
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(go.newOptions().selectedNumberOpponents());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(go.newOptions().selectedNumberOpponents());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			// the limits may have changed from previous settings
			Integer min = 0;
			Integer max = go.newOptions().maximumOpponentsOptions();
			setLimits(min, max);
			setDefaultRandomLimits(1, max);
			go.newOptions().selectedNumberOpponents(Math.min(max, value.getCodeView()));
			go.options().selectedNumberOpponents(Math.min(max, value.getCodeView()));
		}

		@Override public void initComments() {}
	}
   
	// ==============================================================
	// GUI RACE FILTER
	//
	static class GuiRaceFilter extends
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		GuiRaceFilter(ClientClasses go) { 
			super("GUI RACE FILTER",
					new Validation<String>(
							new T_String(go.newOptions().selectedPlayerRace()), 
							go.newOptions().startingRaceOptions()));
			
			List<String> defaultValue = go.newOptions().startingRaceOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			RaceFilter.defaultRaceList(defaultValue);
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(); // Not really possible
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(RaceFilter.selectedGuiRaceFilter());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			RaceFilter.selectedGuiRaceFilter(value.getCodeList());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "If you don’t like to have some races as opponent,"
					+ " or if your planetary distribution affect a race too much,"
					+ " you are able to remove them form the pool of selectable opponents." + NL
					+ "Only the opponents on the list will be shown when you click on the selection rectangle." + NL
					+ "The race list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}

	// ==============================================================
	// GAME RACE FILTER
	//
	static class GameRaceFilter extends
			AbstractParameter <String, Validation<String>, ClientClasses> {

	    // ==================================================
	    // Constructors and initializers
	    //
		GameRaceFilter(ClientClasses go) { 
			super("GAME RACE FILTER",
					new Validation<String>(
							new T_String(go.newOptions().selectedPlayerRace()), 
							go.newOptions().startingRaceOptions()));
			
			List<String> defaultValue = go.newOptions().startingRaceOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			RaceFilter.defaultRaceList(defaultValue);
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(); // Not really possible
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(RaceFilter.selectedGameRaceFilter());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			RaceFilter.selectedGameRaceFilter(value.getCodeList());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "If you don’t like to have some races as opponent,"
					+ " or if your planetary distribution affect a race too much,"
					+ " you are able to remove them form the pool of selectable opponents." + NL
					+ "Only the opponents on the list will be used by the random generator when starting a new game." + NL
					+ "The race list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}
	// ==============================================================
	// GUI PRESET OPPONENT
	//
	// GuiRaceFilter is required
	// 
	static class GuiPresetOpponent extends
			AbstractParameter <String, Valid_RaceList, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		GuiPresetOpponent(ClientClasses go) { 
			super("GUI PRESET OPPONENT",
					new Valid_RaceList(
							new T_String(go.newOptions().selectedPlayerRace())
							, getRaceOptionList(go)
					)
			);
			
			List<String> defaultValue = go.newOptions().startingRaceOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			// remove the "null" from randomize
			setDefaultRandomLimits(defaultValue.get(0)
					, defaultValue.get(defaultValue.size()-1));
			setLimits(defaultValue.get(0)
					, defaultValue.get(defaultValue.size()-1));
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			List<String> list = new ArrayList<String>();
			for (Empire empire : go.session().galaxy().empires()) {
				list.add(empire.raceName());
			}
			list.remove(0); // remove player
			return new T_String().setFromCodeView(list);
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(getRaceFromGUI(go.newOptions()));
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			String[] selectedOpponents = ((Valid_RaceList) getValidation())
										.analyze(go, value.getUserList()
												, IGameOptions.MAX_OPPONENT_TYPE
												, false);
			int i=0;
			for (String race : selectedOpponents) {
				go.newOptions().selectedOpponentRace(i, race);
				go.options().selectedOpponentRace(i, race);
				i++;
			}
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "To fill the opponent list or suggest random opponents from a list." + NL
					+ "If option is empty, the opponent is not changed." + NL
					+ "If the option is \"null\" the result is an empty rectangle." + NL
					+ "If the option is \"random\" the opponent will be selected from the full race list." + NL
					+ "If the option is \"GUI\" the opponent will be selected from the GUI RACE FILTER list." + NL
					+ "If the option is \"GAME\" the opponent will be selected from the GAME RACE FILTER list." + NL
					+ "If the option is \"random race_1, race_2, race_N\"  the opponent will be selected from the given list." + NL
					+ "If the list is shorter than the number of opponents and the last option is random:"
					+ " this last option will be applied to the remaining opponents."
					+ " Otherwise the remaining opponents aren’t changed." + NL
					+ "The race list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}
	// ==============================================================
	// START PRESET OPPONENT
	//
	// GuiRaceFilter is required
	// 
	/**
	 * Management of random opponent filling at start
	 */
	public static class StartPresetOpponent extends
			AbstractParameter <String, Valid_RaceList, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		StartPresetOpponent(ClientClasses go) { 
			super("START PRESET OPPONENT",
					new Valid_RaceList(
							new T_String(go.newOptions().selectedPlayerRace())
							, getRaceOptionList(go)
					)
			);
			
			List<String> defaultValue = go.newOptions().startingRaceOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			// remove the "null" from randomize
			setDefaultRandomLimits(defaultValue.get(0)
					, defaultValue.get(defaultValue.size()-1));
			setLimits(defaultValue.get(0)
					, defaultValue.get(defaultValue.size()-1));
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			List<String> list = new ArrayList<String>();
			for (Empire empire : go.session().galaxy().empires()) {
				list.add(empire.raceName());
			}
			list.remove(0); // remove player
			return new T_String().setFromCodeView(list);
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(getRaceFromGUI(go.newOptions()));
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			String[] selectedOpponents = ((Valid_RaceList) getValidation())
										.analyze(go, value.getUserList()
												, IGameOptions.MAX_OPPONENT_TYPE
												, true);
			RaceFilter.startOpponentRace(selectedOpponents);
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "To replace the random opponent generation when starting a new game." + NL
					+ "If option is empty or null, the opponent will be randomly selected." + NL
					+ "If the option is \"random\" the opponent will be selected from the full race list." + NL
					+ "If the option is \"GUI\" the opponent will be selected from the GUI RACE FILTER list." + NL
					+ "If the option is \"GAME\" the opponent will be selected from the GAME RACE FILTER list." + NL
					+ "If the option is \"random race_1, race_2, race_N\""
					+ " the opponent will be selected from the given list." + NL
					+ "If the list is shorter than the number of opponents,"
					+ " this last option will be applied to the remaining opponents."
					+ "When the maximum number of a type of opponent (5) is reached,"
					+ " it’ll be removed from the list of allowed opponents."
					+ " When this list is empty, sorry, a forbidden race will be chosen." + NL
					+ "The race list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
		// ========== Other Methods ==========
		//
		/**
		 * @param go the ClientClass
		 */
		public void loadOpponents(ClientClasses go) {
			String[] selectedOpponents = RaceFilter.startOpponentRace();
			if (selectedOpponents != null) {
				int i=0;
				for (String race : selectedOpponents) {
					go.newOptions().selectedOpponentRace(i, race);
					go.options().selectedOpponentRace(i, race);
					i++;
				}
			}
		}
	}
	// ==============================================================
	// GUI AI FILTER
	//
	static class GuiAIFilter extends
			AbstractParameter <String, Validation<String>, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		GuiAIFilter(ClientClasses go) { 
			super("GUI AI FILTER",
					new Validation<String>(
							new T_String(getInitialAI(go))
							, go.newOptions().specificOpponentAIOptions()));
			
			List<String> defaultValue = go.newOptions().specificOpponentAIOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			RaceFilter.defaultGuiAIList(defaultValue);
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(); // Not really possible
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromUserView(RaceFilter.selectedGuiAIFilter());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			RaceFilter.selectedGuiAIFilter(value.getUserList());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "If you don’t like to have some AI as opponent,"
					+ " or if your planetary distribution affect an AI too much,"
					+ " you are able to remove them form the pool of selectable AI." + NL
					+ "Only the AI on the list will toggle when you click on the selection rectangle." + NL
					+ "The AI list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}

	// ==============================================================
	// GAME AI FILTER
	//
	static class GameAIFilter extends
			AbstractParameter <String, Validation<String>, ClientClasses> {

	    // ==================================================
	    // Constructors and initializers
	    //
		GameAIFilter(ClientClasses go) { 
			super("GAME AI FILTER",
					new Validation<String>(
							new T_String(getInitialAI(go))
							, go.newOptions().specificOpponentAIOptions()));
			
			List<String> defaultValue = go.newOptions().specificOpponentAIOptions();
//			.specificOpponentAIOptions().subList(0, AI.AI_LAST_ID);
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
			RaceFilter.defaultGameAIList(defaultValue);
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(); // Not really possible
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromUserView(RaceFilter.selectedGameAIFilter());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			RaceFilter.selectedGameAIFilter(value.getUserList());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "If you don’t like to have some AI as opponent,"
					+ " or if your planetary distribution affect an AI too much,"
					+ " you are able to remove them form the pool of selectable AI." + NL
					+ "Only the AI on the list will be used by the random generator when starting a new game." + NL
					+ "The AI list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}
	// ==============================================================
	// GUI PRESET AI
	//
	// GuiAIFilter is required
	// 
	static class GuiPresetAI extends
			AbstractParameter <String, Valid_AIList, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		GuiPresetAI(ClientClasses go) { 
			super("GUI PRESET AI",
					new Valid_AIList(
							new T_String(getInitialAI(go))
							, getAIOptionList(go)
					)
			);
			
			List<String> defaultValue = go.newOptions().specificOpponentAIOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			List<String> list = new ArrayList<String>();
			for (Empire empire : go.session().galaxy().empires()) {
				list.add(empire.raceName());
			}
			list.remove(0); // remove player
			return new T_String().setFromCodeView(list);
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(getAIFromGUI(go.newOptions()));
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			String[] selectedAIs = ((Valid_AIList) getValidation())
										.analyze(go, value.getUserList(), false);
			int i=0;
			for (String ai : selectedAIs) {
				go.newOptions().specificOpponentAIOption(ai, i);
				go.options().specificOpponentAIOption(ai, i);
				i++;
			}
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "To fill the AI opponent list or suggest random opponents AI from a list." + NL
					+ "If option is empty, the opponent AI is not changed." + NL
					+ "If the option is \"random\" the opponent will be selected from the full race list." + NL
					+ "If the option is \"GUI\" the opponent will be selected from the GUI AI FILTER list." + NL
					+ "If the option is \"GAME\" the opponent will be selected from the GAME AI FILTER list." + NL
					+ "If the option is \"random AI_1, AI_2, AI_N\"  the AI will be selected from the given list." + NL
					+ "If the list is shorter than the number of opponents,"
					+ " the last option will be applied to the remaining AI."
					+ "The AI list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
	}
	// ==============================================================
	// START PRESET AI
	//
	// GuiAIFilter is required
	// 
	/**
	 * Management of random opponent AI filling at start
	 */
	public static class StartPresetAI extends
			AbstractParameter <String, Valid_AIList, ClientClasses> {

		// ==================================================
		// Constructors and initializers
		//
		StartPresetAI(ClientClasses go) { 
			super("START PRESET AI",
					new Valid_AIList(
							new T_String(getInitialAI(go))
							, getAIOptionList(go)
					)
			);
			
			List<String> defaultValue = go.newOptions().specificOpponentAIOptions();
			setHistoryCodeView(Initial, defaultValue); // set Current too
			setHistoryCodeView(Default, defaultValue);
		}
		
		// ========== Overriders ==========
		//
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			List<String> ai = go.newOptions().specificOpponentAIOptions();
			List<String> list = new ArrayList<String>();
			for (Empire empire : go.session().galaxy().empires()) {
				list.add(ai.get(empire.selectedAI));
			}
			list.remove(0); // remove player
			return new T_String().setFromCodeView(list);
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String().setFromCodeView(getAIFromGUI(go.newOptions()));
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			String[] selectedAIs = ((Valid_AIList) getValidation())
										.analyze(go, value.getUserList(), true);
			RaceFilter.startOpponentAI(selectedAIs);
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "To replace the random opponent AI generation when starting a new game." + NL
					+ "If option is empty, the opponent AI will be randomly selected." + NL
					+ "If the option is \"random\" the opponent AI will be selected from the full AI list." + NL
					+ "If the option is \"GUI\" the opponent AI will be selected from the GUI AI FILTER list." + NL
					+ "If the option is \"GAME\" the opponent AI will be selected from the GAME AI FILTER list." + NL
					+ "If the option is \"random AI_1, AI_2, AI_N"
					+ " the opponent AI will be selected from the given list." + NL
					+ "If the list is shorter than the number of AI,"
					+ " the last option will be applied to the remaining AI."
					+ "The AI list must be written using \"/\" as separator." + NL
					+ "!!! Don’t break the lines !!! even if they become very long..."
					+ " There is no multi-line analysis."
					+ NL);
		}
		// ========== Other Methods ==========
		//
		/**
		 * @param go the ClientClass
		 */
		public void loadAIs(ClientClasses go) {
			String[] selectedAIs = RaceFilter.startOpponentAI();
			if (selectedAIs != null) {
				int i=0;
				for (String ai : selectedAIs) {
					go.newOptions().specificOpponentAIOption(ai, i);
					go.options().specificOpponentAIOption(ai, i);
					i++;
				}
			}
		}
	}
}
