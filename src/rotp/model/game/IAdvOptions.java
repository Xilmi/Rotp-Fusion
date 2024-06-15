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
import static rotp.model.game.IGameOptions.getAiHostilityOptions;
import static rotp.model.game.IGameOptions.getColonizingOptions;
import static rotp.model.game.IGameOptions.getCouncilWinOptions;
import static rotp.model.game.IGameOptions.getFuelRangeOptions;
import static rotp.model.game.IGameOptions.getGalaxyAgeOptions;
import static rotp.model.game.IGameOptions.getNebulaeOptions;
import static rotp.model.game.IGameOptions.getPlanetQualityOptions;
import static rotp.model.game.IGameOptions.getRandomEventOptions;
import static rotp.model.game.IGameOptions.getRandomizeAIOptions;
import static rotp.model.game.IGameOptions.getResearchRateOptions;
import static rotp.model.game.IGameOptions.getStarDensityOptions;
import static rotp.model.game.IGameOptions.getTechTradingOptions;
import static rotp.model.game.IGameOptions.getTerraformingOptions;
import static rotp.model.game.IGameOptions.getWarpSpeedOptions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamList;

// Duplicates Options, Race Menu Options and Galaxy Options
//
public interface IAdvOptions extends IBaseOptsTools {
	
	// ==================== Duplicates for Base Advanced Options ====================
	//
	ParamList galaxyAge			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "GALAXY_AGE", getGalaxyAgeOptions(), GALAXY_AGE_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedGalaxyAge();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedGalaxyAge(newValue);
		}
	}.showFullGuide(true);
	ParamList starDensity		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "STAR_DENSITY", getStarDensityOptions(), STAR_DENSITY_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedStarDensityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedStarDensityOption(newValue);
		}
	}.showFullGuide(true);
	ParamList nebulae			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "NEBULAE", getNebulaeOptions(), NEBULAE_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedNebulaeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedNebulaeOption(newValue);
		}
	}.showFullGuide(true);
	ParamList randomEvents		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RANDOM_EVENTS", getRandomEventOptions(), RANDOM_EVENTS_NO_MONSTERS) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedRandomEventOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedRandomEventOption(newValue);
		}
	}.showFullGuide(true);;
	ParamList planetQuality	= new ParamList( // Duplicate Do not add the list
			ADV_UI, "PLANET_QUALITY", getPlanetQualityOptions(), PLANET_QUALITY_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedPlanetQualityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedPlanetQualityOption(newValue);
		}
	}.showFullGuide(true);
	ParamList terraforming		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "TERRAFORMING", getTerraformingOptions(), TERRAFORMING_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedTerraformingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedTerraformingOption(newValue);
		}
	}.showFullGuide(true);
	ParamList colonizing		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COLONIZING", getColonizingOptions(), COLONIZING_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedColonizingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedColonizingOption(newValue);
		}
	}.showFullGuide(true);
	ParamList councilWin		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COUNCIL_WIN", getCouncilWinOptions(), COUNCIL_REBELS) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedCouncilWinOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedCouncilWinOption(newValue);
		}
		@Override protected String descriptionId() {
			return "SETTINGS_COUNCIL_DESC";
		}
	}.showFullGuide(true);
	ParamList randomizeAI	 	= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RANDOMIZE_AI", getRandomizeAIOptions(), RANDOMIZE_AI_NONE) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedRandomizeAIOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedRandomizeAIOption(newValue);
		}
	};
	ParamList autoplay			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "AUTOPLAY",
			IGameOptions.autoPlayAIset().getAutoPlay(), AUTOPLAY_OFF) {
		@Override public void reInit(List<String> list) {
			if (list == null)
				super.reInit(IGameOptions.autoPlayAIset().getAutoPlay());
			else
				super.reInit(list);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedAutoplayOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedAutoplayOption(newValue);
		}
	}.showFullGuide(false);
	default ParamList autoplay()	{ return autoplay; }
	ParamList researchRate		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RESEARCH_RATE", getResearchRateOptions(), RESEARCH_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedResearchRate();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedResearchRate(newValue);
		}
	}.showFullGuide(true);
	ParamList warpSpeed		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "WARP_SPEED", getWarpSpeedOptions(), WARP_SPEED_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedWarpSpeedOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedWarpSpeedOption(newValue);
		}
	}.showFullGuide(true);
	ParamList fuelRange		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "FUEL_RANGE", getFuelRangeOptions(), FUEL_RANGE_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedFuelRangeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedFuelRangeOption(newValue);
			if (GameSession.instance().status().inProgress())
				GameSession.instance().galaxy().resetAllAI();
		}
	}.showFullGuide(true);
	ParamList techTrading		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "TECH_TRADING", getTechTradingOptions(), TECH_TRADING_YES) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedTechTradeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedTechTradeOption(newValue);
		}
	};
	ParamList aiHostility		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "AI_HOSTILITY", getAiHostilityOptions(), AI_HOSTILITY_NORMAL) {
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedAIHostilityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedAIHostilityOption(newValue);
		}
	}.showFullGuide(true);
	// ==================== GUI List Declarations ====================
	//
//	LinkedList<IParam> advancedOptions	  = new LinkedList<>(
//			Arrays.asList(
//					galaxyAge, starDensity,
//					nebulae, planetQuality,
//					terraforming,
//					null,
//					randomEvents, aiHostility,
//					councilWin, randomizeAI,
//					autoplay,
//					null,
//					researchRate, warpSpeed,
//					fuelRange, techTrading,
//					colonizing,
//					IInGameOptions.inGameOptionsUI,
//					IPreGameOptions.preGameOptionsUI
//					));
	static LinkedList<IParam> advancedOptions() {
		LinkedList<IParam> options  = new LinkedList<>(
				Arrays.asList(
						galaxyAge, starDensity,
						nebulae, planetQuality,
						terraforming,
						null,
						randomEvents, aiHostility,
						councilWin, randomizeAI,
						autoplay,
						null,
						researchRate, warpSpeed,
						fuelRange, techTrading,
						colonizing,
						null,
						IPreGameOptions.preGameOptionsUI(),
						IInGameOptions.inGameOptionsUI(),
						IMainOptions.commonOptionsUI(),
						ICombatOptions.combatOptionsUI(),
						IInGameOptions.baseModOptionsUI()
						));
		return options;
	}
}
