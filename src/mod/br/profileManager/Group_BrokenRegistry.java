
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
import static mod.br.AddOns.StarsOptions.ALL_PLANETS_KEY;
import static mod.br.AddOns.StarsOptions.PLANET_TYPES;
import static mod.br.AddOns.StarsOptions.STARS_KEY;
import static mod.br.AddOns.StarsOptions.STAR_TYPES;
import static mod.br.AddOns.StarsOptions.probabilityModifier;

import java.util.List;

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.T_Float;
import br.profileManager.src.main.java.Valid_IntegerWithList;
import mod.br.AddOns.Miscellaneous;
import mod.br.AddOns.StarsOptions.ProbabilityModifier;


/**
 * @author BrokenRegistry
 * For Parameters without GUI from BrokenRegistry Mods
 */
public class Group_BrokenRegistry extends  AbstractGroup <ClientClasses> {
	
	Group_BrokenRegistry(ClientClasses go) {
	   super(go, getHeadComments());
	}
	
	private static String getHeadComments() {
		return  " " + NL
				+ "------------- Broken Registry Options -------------" + NL
				+ "";
	}
	
	@Override protected void initSettingList(ClientClasses go) {
		addParameter(new FlagColorOrder(go));
		addParameter(new BaseProbabilityModifier(go, "STAR TYPE PROBABILITY"
				, probabilityModifier(STARS_KEY), STAR_TYPES
				, " " + NL
				+ "Modify the probability of appearance of each star colour." + NL
				+ "A positive value will multiply the base probability." + NL
				+ "A negative values replace it (after the sign is changed, of course!)." + NL
				+ "Be careful... This could terribly affect the game!"
				+ NL));
		addParameter(new BaseProbabilityModifier(go
				, "PLANET TYPE PROBABILITY " + ALL_PLANETS_KEY
				, probabilityModifier(ALL_PLANETS_KEY), PLANET_TYPES
				, " " + NL
				+ "Modify the probability of appearance of each planet type, globally for all star colour." + NL
				+ "A positive value will multiply the base probability." + NL
				+ "A negative values replace it (after the sign is changed, of course!)." + NL
				+ "Be careful... This could terribly affect the game!"
				+ NL));
		for (String color : STAR_TYPES) {
			addParameter(new BaseProbabilityModifier(go
					, "PLANET TYPE PROBABILITY " + color
					, probabilityModifier(color), PLANET_TYPES
					, " " + NL
					+ "Modify the probability of appearance of each planet type, globally this specific star colour." + NL
					+ "A positive value will multiply the base probability." + NL
					+ "A negative values replace it (after the sign is changed, of course!)." + NL
					+ "This parameter is applied after the global one." + NL
					+ "Be careful... This could terribly affect the game!"
					+ NL));
		}

	}
	// ========================================================================
	// FLAG COLOR ORDER
	//
	static class FlagColorOrder extends 
			AbstractParameter <Integer, Valid_IntegerWithList, ClientClasses> {

	    // ========== Constructors and initializer ==========
	    //
		FlagColorOrder(ClientClasses go) {
			super("FLAG COLOR ORDER"
					, new Valid_IntegerWithList(0
							, Miscellaneous.defaultFlagColorOrder().getUserList()));
			
			getValidation().setHistory(Initial, Miscellaneous.defaultFlagColorOrder());
			getValidation().setHistory(Default, Miscellaneous.defaultFlagColorOrder());
			getValidation().setHistory(Current, Miscellaneous.defaultFlagColorOrder());
		}
		
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return Miscellaneous.selectedFlagColorOrder();
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {
			Miscellaneous.selectedFlagColorOrder(value);
		}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return Miscellaneous.selectedFlagColorOrder();
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			Miscellaneous.selectedFlagColorOrder(value);

			go.options().selectedPlayerColor(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(dynamicParameter());
			setSettingComments(" " + NL
					+ "This setting will change scrolling order of the star flags in the galaxy map" + NL
					+ "List lenght may be shortened. by removing some colors" + NL
					+ "If you remove the \"None\" one, it will still be available on reset"
					+ NL);
		}	
	}

	// ==============================================================
	// BASE PROBABILITY MODIFIER CLASS
	//
	/**
	 *  Base class for star and planets probability modifier
	 */
	static class BaseProbabilityModifier extends 
			AbstractParameter <Float, Valid_ProbabilityDensity, ClientClasses> {

		private final ProbabilityModifier pMod;
	    // ========== Constructors and initializer ==========
	    //
		BaseProbabilityModifier(ClientClasses go
				, String Name
				, ProbabilityModifier modifier
				, List<String> options
				, String settingComment)
		{
			super(Name, new Valid_ProbabilityDensity(
					ProbabilityModifier.DefaultProbabilityModifier, options)
					, settingComment);

			pMod = modifier;
			setHistoryCodeView(Initial, pMod.defaultModifierList());
			setHistoryCodeView(Default, pMod.defaultModifierList());
			setHistoryCodeView(Current, pMod.defaultModifierList());
		}
				
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Float> getFromGame (ClientClasses go) {
			return null; // Too complicated to only guess!
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Float> value) {}
		
		@Override public AbstractT<Float> getFromUI (ClientClasses go) {
			return new T_Float().setFromCodeView(
					pMod.selectedModifierList());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Float> value) {
			pMod.selectedModifierList(value.getCodeList());
		}
		
		@Override public void initComments() {}
	}
}
