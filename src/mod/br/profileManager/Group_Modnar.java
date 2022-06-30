
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
import br.profileManager.src.main.java.T_Boolean;
import br.profileManager.src.main.java.T_Float;
import br.profileManager.src.main.java.T_Integer;
import br.profileManager.src.main.java.Valid_IntegerWithList;
import br.profileManager.src.main.java.Validation;
import rotp.model.empires.Empire;
import rotp.ui.UserPreferences;
import rotp.ui.game.StartModOptionsUI;

/**
 * @author BrokenRegistry
 * For Parameters in Modnar GUI
 */
class Group_Modnar extends  AbstractGroup <ClientClasses> {

	Group_Modnar(ClientClasses go) {
	   super(go);
	}
	@Override
	protected void initSettingList(ClientClasses go) {
		addParameter(new AlwaysStarGates(go));
		addParameter(new AlwaysThorium(go));
		addParameter(new ChallengeMode(go));
		addParameter(new BattleScouts(go));
		addParameter(new CompanionWorlds(go));
		addParameter(new RandomTechStart(go));
		addParameter(new CustomDifficulty(go));
		addParameter(new DynamicDifficulty(go));
		addParameter(new MissileSizeModifier(go));
		addParameter(new RetreatRestrictions(go));
		addParameter(new RetreatRestrictionTurns(go));
	}

	// ==============================================================
	// ALWAYS STAR GATES
	//
	static class AlwaysStarGates extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		private String StarGateId = "Stargate:0";
		private int StarGateCategory = 4;

		AlwaysStarGates(ClientClasses go) {
			super( "ALWAYS STAR GATES",
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.alwaysStarGates())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			for (Empire empire : go.session().galaxy().empires()) {
				List<String> techList = empire.tech()
						.category(StarGateCategory).possibleTechs();			
				if (!techList.contains(StarGateId)) {
					return new T_Boolean(false);
				}
			}
			return new T_Boolean(true);
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {
			if (value.getCodeView()) {
				for (Empire empire : go.session().galaxy().empires()) {
				empire.tech().category(StarGateCategory).insertPossibleTech(StarGateId);
				}
			}
		}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.alwaysStarGates());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setAlwaysStarGates(value.getCodeView());
		}
		
		@Override public void initComments() {
			setHeadComments(
				" " + NL +
				"------------- Modnar's Options -------------" + NL +
				" ");
			setBottomComments(availableForChange());
		}
	}
	
	// ==============================================================
	// ALWAYS THORIUM
	//
	static class AlwaysThorium extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		private String ThoriumCellId = "FuelRange:8";
		private int ThoriumCellCategory = 4;

		AlwaysThorium(ClientClasses go) { 
			super("ALWAYS THORIUM",
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.alwaysThorium())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			for (Empire empire : go.session().galaxy().empires()) {
				List<String> techList = empire.tech()
						.category(ThoriumCellCategory).possibleTechs();
				if (!techList.contains(ThoriumCellId)) {
					return new T_Boolean(false);
				}
			}
			return new T_Boolean(true);
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {
			if (value.getCodeView()) {
				for (Empire empire : go.session().galaxy().empires()) {
					empire.tech().category(ThoriumCellCategory).insertPossibleTech(ThoriumCellId);
				}
			}
		}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.alwaysThorium());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setAlwaysThorium(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}
	}
	
	// ==============================================================
	// CHALLENGE MODE
	//
	static class ChallengeMode extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		ChallengeMode(ClientClasses go) {
			super("CHALLENGE MODE", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.challengeMode())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean(go.session().galaxy().empire(0).isChallengeMode());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.challengeMode());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setChallengeMode(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	
	// ==============================================================
	// BATTLE SCOUT
	//
	static class BattleScouts extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		BattleScouts(ClientClasses go) {
			super("BATTLE SCOUT", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.battleScout())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return null; // There is no way to know!
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {}		
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.battleScout());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setBattleScout(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	
	// ==============================================================
	// COMPANION WORLDS
	//
	static class CompanionWorlds extends 
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		CompanionWorlds(ClientClasses go) {
			super("COMPANION WORLDS", 
					new Validation<Integer>(
							new T_Integer(UserPreferences.companionWorlds())));

			setHistoryCodeView(Default, 0); // MODNAR DEFAULT
			setLimits(0 , 4);
			setDefaultRandomLimits(0 , 4);
		}
		
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(go.session().galaxy().empire(0).getCompanionWorldsNumber());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(UserPreferences.companionWorlds());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setCompanionWorlds(value.getCodeView());
		}

		@Override public void initComments() {}
	}

	// ==============================================================
	// RANDOM TECH START
	//
	static class RandomTechStart extends 
	AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		RandomTechStart(ClientClasses go) { 
			super("RANDOM TECH START", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.randomTechStart())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return null; // There is no way to know!
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.randomTechStart());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setRandomTechStart(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	
	// ==============================================================
	// CUSTOM DIFFICULTY
	//
	static class CustomDifficulty extends
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		CustomDifficulty(ClientClasses go) { 
			super("CUSTOM DIFFICULTY", 
					new Validation<Integer>(
							new T_Integer(UserPreferences.customDifficulty())));

			setHistoryCodeView(Initial, UserPreferences.customDifficulty());
			setHistoryCodeView(Default, 100); // MODNAR DEFAULT
			setLimits(20 , 500);
			setDefaultRandomLimits(20 , 500);
		}

		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(UserPreferences.customDifficulty()); // Dynamic: Same as UserPreferences
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setCustomDifficulty(value.getCodeView()); // Dynamic: Same as UserPreferences
		}		

		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(UserPreferences.customDifficulty());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setCustomDifficulty(value.getCodeView());
		}

		@Override public void initComments() {
			setBottomComments(dynamicParameter());
		}
	}

	// ==============================================================
	// DYNAMIC DIFFICULTY
	//
   static class DynamicDifficulty extends
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		DynamicDifficulty(ClientClasses go) {
			super("DYNAMIC DIFFICULTY", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.dynamicDifficulty())));

			setHistoryCodeView(Default, false); // MODNAR DEFAULT
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean(UserPreferences.dynamicDifficulty()); // Dynamic: Same as UserPreferences
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setDynamicDifficulty(value.getCodeView()); // Dynamic: Same as UserPreferences
		}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.dynamicDifficulty());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setDynamicDifficulty(value.getCodeView());
		}

		@Override public void initComments() {
			setBottomComments(dynamicParameter());
		}
	}

	// ==============================================================
	// MISSILE SIZE MODIFIER
	//
	static class MissileSizeModifier extends
			AbstractParameter <Float, Validation<Float>, ClientClasses> {

		MissileSizeModifier(ClientClasses go) { 
			super("MISSILE SIZE MODIFIER", 
					new Validation<Float>(
							new T_Float(UserPreferences.missileSizeModifier())));
			
			setHistoryCodeView(Default, 0.66f); // XILMI DEFAULT
			setLimits(0.1f , 1.0f);
			setDefaultRandomLimits(0.1f , 1.0f);
		}

		@Override public AbstractT<Float> getFromGame (ClientClasses go) {
			return new T_Float(UserPreferences.missileSizeModifier()); // Dynamic: Same as UserPreferences
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Float> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView()); // Dynamic: Same as UserPreferences
		}		

		@Override public AbstractT<Float> getFromUI (ClientClasses go) {
			return new T_Float(UserPreferences.missileSizeModifier());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Float> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView());
		}

		@Override public void initComments() {
			setBottomComments(dynamicParameter());
		}
	}
	// ==============================================================
	// RETREAT RESTRICTIONS
	//
	static class RetreatRestrictions extends
			AbstractParameter <Integer, Valid_IntegerWithList, ClientClasses> {

		RetreatRestrictions(ClientClasses go) { 
			super("RETREAT RESTRICTIONS", 
					new Valid_IntegerWithList(
							UserPreferences.retreatRestrictions()
							, StartModOptionsUI.getRetreatRestrictionOptions()));	
			setHistoryCodeView(Initial, UserPreferences.retreatRestrictions());
			setHistoryCodeView(Default, 0);
			setHistory(Current, Initial);
		}

		@Override public AbstractT<Integer> getFromGame (ClientClasses go) { // BR: Validate Dynamic 
			return new T_Integer(UserPreferences.retreatRestrictions()); // Dynamic: Same as UserPreferences
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView()); // Dynamic: Same as UserPreferences
		}		

		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(UserPreferences.retreatRestrictions());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView());
		}

		@Override public void initComments() {
			setBottomComments(dynamicParameter());
		}
	}
	// ==============================================================
	// RETREAT RESTRICTION TURNS
	//
	static class RetreatRestrictionTurns extends
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		RetreatRestrictionTurns(ClientClasses go) { 
			super("RETREAT RESTRICTION TURNS", 
					new Validation<Integer>(
							new T_Integer(UserPreferences.retreatRestrictionTurns())));
			
			setHistoryCodeView(Default, 100); // XILMI DEFAULT
			setLimits(0 , 100);
			setDefaultRandomLimits(0 , 100);
		}

		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(UserPreferences.retreatRestrictionTurns()); // Dynamic: Same as UserPreferences
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView()); // Dynamic: Same as UserPreferences
		}		

		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(UserPreferences.retreatRestrictionTurns());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setMissileSizeModifier(value.getCodeView());
		}

		@Override public void initComments() {
			setBottomComments(dynamicParameter());
		}
	}
}
