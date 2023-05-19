package rotp.model.game;

import static rotp.model.game.IGameOptions.AI_HOSTILITY_NORMAL;
import static rotp.model.game.IGameOptions.AUTOPLAY_OFF;
import static rotp.model.game.IGameOptions.COLONIZING_NORMAL;
import static rotp.model.game.IGameOptions.COUNCIL_REBELS;
import static rotp.model.game.IGameOptions.FUEL_RANGE_NORMAL;
import static rotp.model.game.IGameOptions.GALAXY_AGE_NORMAL;
import static rotp.model.game.IGameOptions.NEBULAE_NORMAL;
import static rotp.model.game.IGameOptions.PLANET_QUALITY_NORMAL;
import static rotp.model.game.IGameOptions.RANDOMIZE_AI_NONE;
import static rotp.model.game.IGameOptions.RANDOM_EVENTS_NO_MONSTERS;
import static rotp.model.game.IGameOptions.RESEARCH_NORMAL;
import static rotp.model.game.IGameOptions.STAR_DENSITY_NORMAL;
import static rotp.model.game.IGameOptions.TECH_TRADING_YES;
import static rotp.model.game.IGameOptions.TERRAFORMING_NORMAL;
import static rotp.model.game.IGameOptions.WARP_SPEED_NORMAL;
import static rotp.model.game.MOO1GameOptions.getAiHostilityOptions;
import static rotp.model.game.MOO1GameOptions.getColonizingOptions;
import static rotp.model.game.MOO1GameOptions.getCouncilWinOptions;
import static rotp.model.game.MOO1GameOptions.getFuelRangeOptions;
import static rotp.model.game.MOO1GameOptions.getGalaxyAgeOptions;
import static rotp.model.game.MOO1GameOptions.getNebulaeOptions;
import static rotp.model.game.MOO1GameOptions.getPlanetQualityOptions;
import static rotp.model.game.MOO1GameOptions.getRandomEventOptions;
import static rotp.model.game.MOO1GameOptions.getRandomizeAIOptions;
import static rotp.model.game.MOO1GameOptions.getResearchRateOptions;
import static rotp.model.game.MOO1GameOptions.getStarDensityOptions;
import static rotp.model.game.MOO1GameOptions.getTechTradingOptions;
import static rotp.model.game.MOO1GameOptions.getTerraformingOptions;
import static rotp.model.game.MOO1GameOptions.getWarpSpeedOptions;
import static rotp.ui.UserPreferences.AUTOBOMBARD_INVADE;
import static rotp.ui.UserPreferences.AUTOBOMBARD_NEVER;
import static rotp.ui.UserPreferences.AUTOBOMBARD_NO;
import static rotp.ui.UserPreferences.AUTOBOMBARD_WAR;
import static rotp.ui.UserPreferences.AUTOBOMBARD_YES;
import static rotp.ui.UserPreferences.autoBombardMode;
import static rotp.ui.UserPreferences.autoColonize;
import static rotp.ui.UserPreferences.save;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamList;

// Duplicates Options, Race Menu Options and Galaxy Options
//
public interface DuplicateOptions extends BaseOptions {
	
	// ==================== Duplicates for Base Advanced Options ====================
	//
	ParamList galaxyAge			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "GALAXY_AGE", getGalaxyAgeOptions(), GALAXY_AGE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedGalaxyAge();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedGalaxyAge(newValue);
		}
	};
	ParamList starDensity		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "STAR_DENSITY", getStarDensityOptions(), STAR_DENSITY_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedStarDensityOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedStarDensityOption(newValue);
		}
	};
	ParamList nebulae			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "NEBULAE", getNebulaeOptions(), NEBULAE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedNebulaeOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedNebulaeOption(newValue);
		}
	};
	ParamList randomEvents		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RANDOM_EVENTS", getRandomEventOptions(), RANDOM_EVENTS_NO_MONSTERS) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedRandomEventOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedRandomEventOption(newValue);
		}
	};
	ParamList planetQuality	= new ParamList( // Duplicate Do not add the list
			ADV_UI, "PLANET_QUALITY", getPlanetQualityOptions(), PLANET_QUALITY_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedPlanetQualityOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedPlanetQualityOption(newValue);
		}
	};
	ParamList terraforming		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "TERRAFORMING", getTerraformingOptions(), TERRAFORMING_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedTerraformingOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedTerraformingOption(newValue);
		}
	};
	ParamList colonizing		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COLONIZING", getColonizingOptions(), COLONIZING_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedColonizingOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedColonizingOption(newValue);
		}
	};
	ParamList councilWin		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COUNCIL_WIN", getCouncilWinOptions(), COUNCIL_REBELS) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedCouncilWinOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedCouncilWinOption(newValue);
		}
		@Override protected String descriptionId() {
			return "SETTINGS_COUNCIL_DESC";
		}
	};
	ParamList randomizeAI	 	= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RANDOMIZE_AI", getRandomizeAIOptions(), RANDOMIZE_AI_NONE) {
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedRandomizeAIOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedRandomizeAIOption(newValue);
		}
	};
	ParamList autoplay			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "AUTOPLAY",
			IGameOptions.autoPlayAIset().getAutoPlay(), AUTOPLAY_OFF) {
		{ showFullGuide(false); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedAutoplayOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedAutoplayOption(newValue);
		}
	};
	default ParamList autoplay()	{ return autoplay(); }
	ParamList researchRate		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RESEARCH_RATE", getResearchRateOptions(), RESEARCH_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedResearchRate();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedResearchRate(newValue);
		}
	};
	ParamList warpSpeed		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "WARP_SPEED", getWarpSpeedOptions(), WARP_SPEED_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedWarpSpeedOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedWarpSpeedOption(newValue);
		}
	};
	ParamList fuelRange		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "FUEL_RANGE", getFuelRangeOptions(), FUEL_RANGE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedFuelRangeOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedFuelRangeOption(newValue);
			if (GameSession.instance().status().inProgress())
				GameSession.instance().galaxy().resetAllAI();
		}
	};
	ParamList techTrading		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "TECH_TRADING", getTechTradingOptions(), TECH_TRADING_YES) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedTechTradeOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedTechTradeOption(newValue);
		}
	};
	ParamList aiHostility		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "AI_HOSTILITY", getAiHostilityOptions(), AI_HOSTILITY_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getFromOption() {
			return RotPUI.mergedGuiOptions().selectedAIHostilityOption();
		}
		@Override public void setOption(String newValue) {
			RotPUI.mergedGuiOptions().selectedAIHostilityOption(newValue);
		}
	};
	// ==================== Duplicates for Main Settings Options ====================
	//
	ParamBoolean autoColonize_	= new ParamBoolean( // Duplicate Do not add the list
			GAME_UI, "AUTOCOLONIZE", false) {
		{ isDuplicate(true); }
		@Override public Boolean get() { return autoColonize(); }
		@Override public Boolean set(Boolean newValue) {
			autoColonize(newValue);
			save();
			return autoColonize();
		}
	};
	ParamList autoBombard_		= new ParamList( // Duplicate Do not add the list
			GAME_UI, "AUTOBOMBARD",
			Arrays.asList(
					AUTOBOMBARD_NO,
					AUTOBOMBARD_NEVER,
					AUTOBOMBARD_YES,
					AUTOBOMBARD_WAR,
					AUTOBOMBARD_INVADE
					),
			AUTOBOMBARD_NO) {
		{ showFullGuide(true); }
		@Override public String get() { return autoBombardMode(); }
		@Override public String set(String newValue) {
			autoBombardMode(newValue);
			save();
			return autoBombardMode();
		}
	};

	// ==================== GUI List Declarations ====================
	//
	LinkedList<InterfaceParam> advancedOptions	  = new LinkedList<>(
			Arrays.asList(
					galaxyAge, starDensity, nebulae, planetQuality, terraforming,
					null,
					randomEvents, aiHostility, councilWin, randomizeAI, autoplay,
					null,
					researchRate, warpSpeed, fuelRange, techTrading, colonizing
					));

}
