
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
import static rotp.ui.UserPreferences.eventsStartTurn;
import static rotp.ui.UserPreferences.maximizeSpacing;
import static rotp.ui.UserPreferences.minStarsPerEmpire;
import static rotp.ui.UserPreferences.prefStarsPerEmpire;
import static rotp.ui.UserPreferences.spacingLimit;
import static rotp.ui.UserPreferences.techCloaking;
import static rotp.ui.UserPreferences.techHyperspace;
import static rotp.ui.UserPreferences.techIndustry2;
import static rotp.ui.UserPreferences.techIrradiated;
import static rotp.ui.UserPreferences.techStargate;
import static rotp.ui.UserPreferences.techThorium;
import static rotp.ui.UserPreferences.techTransport;

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.T_Boolean;
import br.profileManager.src.main.java.T_Float;
import br.profileManager.src.main.java.T_Integer;
import br.profileManager.src.main.java.T_String;
import br.profileManager.src.main.java.Validation;
import rotp.model.empires.Empire;
import rotp.ui.util.ParamTech;

/**
 * @author BrokenRegistry
 * For Parameters in MOD Options B GUI
 */
class Group_Mod_B extends  AbstractGroup <ClientClasses> {

	Group_Mod_B(ClientClasses go) {
		super(go, getHeadComments());
	}
	
	private static String getHeadComments() {
		return  " " + NL
				+ "------------- Modder's Options B -------------" + NL
				+ "";
	}

	@Override protected void initSettingList(ClientClasses go) {
		addParameter(new RandomEventsStartingYear(go));
		addParameter(new MaximizeEmpiresSpacing(go));
		addParameter(new MaxSpacingLimit(go));
		addParameter(new PreferredStarsPerEmpire(go));
		addParameter(new MinStarsPerEmpire(go));
		addParameter(new Tech(go, techIrradiated));
		addParameter(new Tech(go, techCloaking));
		addParameter(new Tech(go, techStargate));
		addParameter(new Tech(go, techHyperspace));
		addParameter(new Tech(go, techIndustry2));
		addParameter(new Tech(go, techThorium));
		addParameter(new Tech(go, techTransport));
	}
	// ==============================================================
	// RANDOM EVENTS STARTING YEAR
	//
	static class RandomEventsStartingYear extends 
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		RandomEventsStartingYear(ClientClasses go) {
			super("RANDOM EVENTS STARTING YEAR", 
					new Validation<Integer>(
							new T_Integer(eventsStartTurn.get())));

			setHistoryCodeView(Default, eventsStartTurn.defaultValue());
			setHistory(Default, new T_Integer(eventsStartTurn.get()));
			setLimits(1 , 1000000);
			setDefaultRandomLimits(50 , 200);
		}
		
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer(eventsStartTurn.get());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(eventsStartTurn.get());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			eventsStartTurn.setAndSave(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ========================================================================
	// MAXIMIZE EMPIRES SPACING
	//
	static class MaximizeEmpiresSpacing extends 
			AbstractParameter <Boolean, Validation<Boolean>, ClientClasses> {
	 
		MaximizeEmpiresSpacing(ClientClasses go) {
			super( "MAXIMIZE EMPIRES SPACING",
					new Validation<Boolean>(
							new T_Boolean(maximizeSpacing.defaultValue())));

			setHistoryCodeView(Default, false); // BR DEFAULT
		}
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Boolean> getFromGame (ClientClasses go) {
			return new T_Boolean();
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Boolean> value) {}
		
		@Override public AbstractT<Boolean> getFromUI (ClientClasses go) {
			return new T_Boolean(maximizeSpacing.get());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Boolean> value) {
			maximizeSpacing.setAndSave(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "I don’t like being squeezed in a corned in big map with few opponents..." + NL
					+ "With this option activated, the space between every empire will be maximized."
					+ NL);
		}
	}
	// ========================================================================
	// MAXIMIZE SPACING LIMIT
	//
	static class MaxSpacingLimit extends 
			AbstractParameter <Float, Validation<Float>, ClientClasses> {

		MaxSpacingLimit(ClientClasses go) { 
			super( "MAXIMIZE SPACING LIMIT",
					new Validation<Float>(
							new T_Float(spacingLimit.get())));

			setHistoryCodeView(Default, spacingLimit.defaultValue());
			setLimits(3f , 1000000f);
			setDefaultRandomLimits(10f , 30f);
		}
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Float> getFromGame (ClientClasses go) {
			return new T_Float();
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Float> value) {}
		
		@Override public AbstractT<Float> getFromUI (ClientClasses go) {
			return new T_Float(spacingLimit.get());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Float> value) {
			spacingLimit.setAndSave(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "Maximal Spacing factor when activating the \"MAXIMIZE EMPIRES SPACING\""
					+ " Setting. To high values are not very compatible with improbable galaxies ex: text galaxy." + NL
					+ NL);
		}
	}
	// ========================================================================
	// PREF STARS PER EMPIRE
	//
	static class PreferredStarsPerEmpire extends 
			AbstractParameter <Float, Validation<Float>, ClientClasses> {

		PreferredStarsPerEmpire(ClientClasses go) { 
			super( "PREF STARS PER EMPIRE",
					new Validation<Float>(
							new T_Float(prefStarsPerEmpire.get())));

			setHistoryCodeView(Default, prefStarsPerEmpire.defaultValue());
			setLimits(3f , 1000000f);
			setDefaultRandomLimits(10f , 30f);
		}
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Float> getFromGame (ClientClasses go) {
			return new T_Float();
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Float> value) {}
		
		@Override public AbstractT<Float> getFromUI (ClientClasses go) {
			return new T_Float(prefStarsPerEmpire.get());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Float> value) {
			prefStarsPerEmpire.setAndSave(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "Preferred number of stars around every empires."
					+ " This parameter will affect the default selected number of opponents,"
					+ " also depend on the size of the galaxy." + NL
					+ "This parameter will be disabled as soon as a number of opponents is chosen."
					+ NL);
		}
	}
	// ========================================================================
	// MIN STARS PER EMPIRE
	//
	static class MinStarsPerEmpire extends 
			AbstractParameter <Integer, Validation<Integer>, ClientClasses> {

		MinStarsPerEmpire(ClientClasses go) {
			super( "MIN STARS PER EMPIRE",
					new Validation<Integer>(
							new T_Integer(minStarsPerEmpire.get())));

			setHistoryCodeView(Default, minStarsPerEmpire.defaultValue());
			setLimits(0 , 1000000);
			setDefaultRandomLimits(3 , 20);
		}
	    // ========== Overriders ==========
	    //
		@Override public AbstractT<Integer> getFromGame (ClientClasses go) {
			return new T_Integer();
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<Integer> value) {}
		
		@Override public AbstractT<Integer> getFromUI (ClientClasses go) {
			return new T_Integer(minStarsPerEmpire.get());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<Integer> value) {
			minStarsPerEmpire.setAndSave(value.getCodeView());
		}
		
		@Override public void initComments() {
			setSettingComments(" " + NL
					+ "Minimum number of stars around every empires." + NL
					+ " This parameter will affect the maximum number of allowed opponents,"
					+ " also depend on the size of the galaxy."
					+ NL);
		}
	}
	// ==============================================================
	// TECH
	//
	static class Tech extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		private final ParamTech tech;
		
		Tech(ClientClasses go, ParamTech tech) {
			super( tech.getCfgLabel(),
					new Validation<String>(
							new T_String(tech.get()),
							tech.getOptions()));
			this.tech = tech;
			setHistoryCodeView(Default, tech.defaultValue());
			setDefaultRandomLimits(ParamTech.ALWAYS, ParamTech.NEVER);
		}

		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			boolean all = true;
			boolean none = true;
			for (Empire empire : go.session().galaxy().empires()) {
				if (empire.tech().category(tech.techCategory)
						.possibleTechs().contains(tech.techId()))
					none = false;
				else 
					all = false;
			}
			if (all) return new T_String(ParamTech.ALWAYS);
			if (none) return new T_String(ParamTech.NEVER);
			return new T_String(ParamTech.AUTO);
		}
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) { 
			if (value.getCodeView().equalsIgnoreCase(ParamTech.PLAYER)
					|| value.getCodeView().equalsIgnoreCase(ParamTech.SELFISH)) {
				go.session().galaxy().player().tech()
						.category(tech.techCategory)
						.insertPossibleTech(tech.techId());
			}
			else if (value.getCodeView().equalsIgnoreCase(ParamTech.ALWAYS)) {
				for (Empire empire : go.session().galaxy().empires()) {
					empire.tech().category(tech.techCategory)
							.insertPossibleTech(tech.techId());
				}
			}
		}
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(tech.get());
		}
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			tech.set(value.getCodeView());
		}
		@Override public void initComments() {
			setBottomComments(availableForChange() + " (Add only)");
		}
	}
}
