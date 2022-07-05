
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

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.Validation;
import rotp.ui.UserPreferences;
import br.profileManager.src.main.java.T_Boolean;
import br.profileManager.src.main.java.T_Integer;


class Group_Governor extends AbstractGroup <ClientClasses> {
	
	Group_Governor(ClientClasses go) {
	   super(go, getHeadComments());
	}
	
	private static String getHeadComments() {
		return  " " + NL
				+ "------------- Governor Options -------------" + NL
				+ " ";
	}

	@Override protected void initSettingList(ClientClasses go) {
		addParameter(new GovernorOnByDefault(go));
		addParameter(new AutospendOnByDefault(go));
		addParameter(new DefaultMaxBases(go));
		addParameter(new DivertExcessToResearch(go));
	}

	// ==============================================================
	// GOVERNOR ON BY DEFAULT
	//
	public static class GovernorOnByDefault extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		GovernorOnByDefault(ClientClasses go) { 
			super("GOVERNOR ON BY DEFAULT", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.governorOnByDefault())));
		}
		
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean(UserPreferences.governorOnByDefault());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {}

		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.governorOnByDefault());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setGovernorOn(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// AUTOSPEND ON BY DEFAULT
	//
	static class AutospendOnByDefault extends
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		AutospendOnByDefault(ClientClasses go) {
			super("AUTOSPEND ON BY DEFAULT", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.governorAutoSpendByDefault())));
		}

		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean(UserPreferences.governorAutoSpendByDefault());
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {

		}	

		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.governorAutoSpendByDefault());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setAutoSpendOn(value.getCodeView());
		}

		@Override public void initComments() {}
	}
	// ==============================================================
	// DEFAULT MAX BASES
	//
	static class DefaultMaxBases extends 
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		DefaultMaxBases(ClientClasses go) { 
			super("DEFAULT MAX BASES", 
					new Validation<Integer>(
							new T_Integer(UserPreferences.defaultMaxBases())));
		}

		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(UserPreferences.defaultMaxBases());
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {

		}	

		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(UserPreferences.defaultMaxBases());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			UserPreferences.setDefaultMaxBases(value.getCodeView());
		}

		@Override public void initComments() {}
	}
	// ==============================================================
	// DIVERT EXCESS TO RESEARCH
	//
	static class DivertExcessToResearch extends
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {

		DivertExcessToResearch(ClientClasses go) {
			super("DIVERT EXCESS TO RESEARCH", 
					new Validation<Boolean>(
							new T_Boolean(UserPreferences.divertColonyExcessToResearch())));
		}

		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean(UserPreferences.divertColonyExcessToResearch());
		}

		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {

		}	

		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(UserPreferences.divertColonyExcessToResearch());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			UserPreferences.setDivertColonyExcessToResearch(value.getCodeView());
		}

		@Override public void initComments() {}
	}
}
