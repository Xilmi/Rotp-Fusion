
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

import br.profileManager.src.main.java.AbstractGroup;
import br.profileManager.src.main.java.AbstractParameter;
import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.PMutil;
import br.profileManager.src.main.java.T_String;
import br.profileManager.src.main.java.Validation;
import rotp.model.game.IGameOptions;

/**
 * @author BrokenRegistry
 * For Parameters in Advanced GUI
 */
public class Group_Advanced extends  AbstractGroup <ClientClasses> {

	Group_Advanced(ClientClasses go) {
	   super(go, getHeadComments());
	}
	
	private static String getHeadComments() {
		return  " " + NL
				+ "----------- Advanced Game Options -----------" + NL
				+ "";
	}
	@Override
	protected void initSettingList(ClientClasses go) {
		addParameter(new GalaxyAge(go));
		addParameter(new StarDensity(go));
		addParameter(new Nebulae(go));
		addParameter(new PlanetQuality(go));
		addParameter(new Terraforming(go));
		addParameter(new RandomEventsOptions(go));
		addParameter(new AIHostility(go));
		addParameter(new Council(go));
		addParameter(new RandomizeAI(go));
		addParameter(new AutoPlay(go));
		addParameter(new Research(go));
		addParameter(new WarpSpeed(go));
		addParameter(new FuelRange(go));
		addParameter(new TechTrading(go));
		addParameter(new Colonizing(go));
	}

	// ==============================================================
	// GALAXY AGE
	//
	static class GalaxyAge extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		GalaxyAge(ClientClasses go) { 
			super("GALAXY AGE", 
					new Validation<String>(
							new T_String(go.options().selectedGalaxyAge()),
							go.options().galaxyAgeOptions()));

			setHistory(Default, "Normal"); // Ray
			}

		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedGalaxyAge());
		}

		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}

		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedGalaxyAge());
		}

		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedGalaxyAge(value.getCodeView());
		}

		@Override public void initComments() {}
	}
 	// ==============================================================
	// STAR DENSITY
	//
	static class StarDensity extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		StarDensity(ClientClasses go) {
			super("STAR DENSITY",
					new Validation<String>(
							new T_String(go.options().selectedStarDensityOption()),
							go.options().starDensityOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedStarDensityOption());
		}
		
		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedStarDensityOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedStarDensityOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// NEBULAE
	//
	static class Nebulae extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		Nebulae(ClientClasses go) {
			super("NEBULAE",
					new Validation<String>(
							new T_String(go.options().selectedNebulaeOption()),
							go.options().nebulaeOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedNebulaeOption());
		}
		
		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedNebulaeOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedNebulaeOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// PLANET QUALITY
	//
   static class PlanetQuality extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		PlanetQuality(ClientClasses go) {
			super("PLANET QUALITY",
					new Validation<String>(
							new T_String(go.options().selectedPlanetQualityOption()),
							go.options().planetQualityOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedPlanetQualityOption());
		}
		
		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedPlanetQualityOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedPlanetQualityOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// TERRAFORMING
	//
	static class Terraforming extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		Terraforming(ClientClasses go) {
			super("TERRAFORMING", 
					new Validation<String>(
							new T_String(go.options().selectedTerraformingOption()),
							go.options().terraformingOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedTerraformingOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedTerraformingOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedTerraformingOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedTerraformingOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
	   	}
	}
	// ==============================================================
	// RANDOM EVENTS
	//
   static class RandomEventsOptions extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		RandomEventsOptions(ClientClasses go) {
			super("RANDOM EVENTS", 
					new Validation<String>(
							new T_String(go.options().selectedRandomEventOption()),
							go.options().randomEventOptions()));

			setHistory(Default, "On"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedRandomEventOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			IGameOptions opts = go.options();
			opts.selectedRandomEventOption(value.getCodeView());
			opts.disableRandomEvents(!PMutil.toBoolean(value.getCodeView()));
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedRandomEventOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedRandomEventOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
	   	}
	}
	// ==============================================================
	// AI HOSTILITY
	//
	static class AIHostility extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		AIHostility(ClientClasses go) {
			super("AI HOSTILITY", 
					new Validation<String>(
							new T_String(go.options().selectedAIHostilityOption()),
							go.options().aiHostilityOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedAIHostilityOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedAIHostilityOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedAIHostilityOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedAIHostilityOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
	   	}
	}
	// ==============================================================
	// COUNCIL
	//
	static class Council extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		Council(ClientClasses go) {
			super("COUNCIL", 
					new Validation<String>(
							new T_String(go.options().selectedCouncilWinOption()),
							go.options().councilWinOptions()));

			setHistory(Default, "Rebels"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedCouncilWinOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedCouncilWinOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedCouncilWinOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedCouncilWinOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
	   	}
	}
	// ==============================================================
	// RANDOMIZE AI
	//
	static class RandomizeAI extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		RandomizeAI(ClientClasses go) {
			super("RANDOMIZE AI",
					new Validation<String>(
							new T_String(go.options().selectedRandomizeAIOption()),
							go.options().randomizeAIOptions()));

			setHistory(Default, "None"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedRandomizeAIOption());
		}
		
		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedRandomizeAIOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedRandomizeAIOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// AUTOPLAY
	//
	static class AutoPlay extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		AutoPlay(ClientClasses go) {
			super("AUTOPLAY", 
					new Validation<String>(
							new T_String(go.options().selectedAutoplayOption()),
							go.options().autoplayOptions()));

			setHistory(Default, "Off"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedAutoplayOption());
		}
		
		@Override public void putToGame(ClientClasses gs, AbstractT<String> value) {}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedAutoplayOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedAutoplayOption(value.getCodeView());
		}
		
		@Override public void initComments() {}
	}
	// ==============================================================
	// RESEARCH
	//
	static class Research extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		Research(ClientClasses go) { 
			super("RESEARCH", 
					new Validation<String>(
							new T_String(go.options().selectedResearchRate()),
							go.options().researchRateOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedResearchRate());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedResearchRate(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedResearchRate());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedResearchRate(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}
	}
	// ==============================================================
	// WARP SPEED
	//
	static class WarpSpeed extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		WarpSpeed(ClientClasses go) { 
			super("WARP SPEED", 
					new Validation<String>(
							new T_String(go.options().selectedWarpSpeedOption()),
							go.options().warpSpeedOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedWarpSpeedOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedWarpSpeedOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedWarpSpeedOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedWarpSpeedOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}
	}
	// ==============================================================
	// FUEL RANGE
	//
	static class FuelRange extends
			AbstractParameter <String, Validation<String>, ClientClasses> {

		FuelRange(ClientClasses go) { 
			super("FUEL RANGE", 
					new Validation<String>(
							new T_String(go.options().selectedFuelRangeOption()),
							go.options().fuelRangeOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedFuelRangeOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedFuelRangeOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedFuelRangeOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedFuelRangeOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}
	}
	// ==============================================================
	// TECH TRADING
	//
	static class TechTrading extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		TechTrading(ClientClasses go) { 
			super("TECH TRADING", 
					new Validation<String>(
							new T_String(go.options().selectedTechTradeOption()),
							go.options().techTradingOptions()));

			setHistory(Default, "Yes"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedTechTradeOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedTechTradeOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedTechTradeOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedTechTradeOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
		}
	}
	// ==============================================================
	// COLONIZING
	//
	static class Colonizing extends 
			AbstractParameter <String, Validation<String>, ClientClasses> {

		Colonizing(ClientClasses go) { 
			super("COLONIZING", 
					new Validation<String>(
							new T_String(go.options().selectedColonizingOption()),			
							go.options().colonizingOptions()));

			setHistory(Default, "Normal"); // Ray
		}
		
		@Override public AbstractT<String> getFromGame (ClientClasses go) {
			return new T_String(go.options().selectedColonizingOption());
		}
		
		@Override public void putToGame(ClientClasses go, AbstractT<String> value) {
			go.options().selectedColonizingOption(value.getCodeView());
		}
		
		@Override public AbstractT<String> getFromUI (ClientClasses go) {
			return new T_String(go.options().selectedColonizingOption());
		}
		
		@Override public void putToGUI(ClientClasses go, AbstractT<String> value) {
			go.options().selectedColonizingOption(value.getCodeView());
		}
		
		@Override public void initComments() {
			setBottomComments(availableForChange());
			}
	}
}
