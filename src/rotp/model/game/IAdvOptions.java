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

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamList;

// Duplicates Options, Race Menu Options and Galaxy Options
//
public interface IAdvOptions extends IBaseOptsTools {
	
	// ==================== Duplicates for Base Advanced Options ====================
	//
	ParamList galaxyAge			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "GALAXY_AGE", getGalaxyAgeOptions(), GALAXY_AGE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedGalaxyAge();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedGalaxyAge(newValue);
		}
	};
	ParamList starDensity		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "STAR_DENSITY", getStarDensityOptions(), STAR_DENSITY_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedStarDensityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedStarDensityOption(newValue);
		}
	};
	ParamList nebulae			= new ParamList( // Duplicate Do not add the list
			ADV_UI, "NEBULAE", getNebulaeOptions(), NEBULAE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedNebulaeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedNebulaeOption(newValue);
		}
	};
	ParamList randomEvents		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RANDOM_EVENTS", getRandomEventOptions(), RANDOM_EVENTS_NO_MONSTERS) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedRandomEventOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedRandomEventOption(newValue);
		}
	};
	ParamList planetQuality	= new ParamList( // Duplicate Do not add the list
			ADV_UI, "PLANET_QUALITY", getPlanetQualityOptions(), PLANET_QUALITY_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedPlanetQualityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedPlanetQualityOption(newValue);
		}
	};
	ParamList terraforming		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "TERRAFORMING", getTerraformingOptions(), TERRAFORMING_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedTerraformingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedTerraformingOption(newValue);
		}
	};
	ParamList colonizing		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COLONIZING", getColonizingOptions(), COLONIZING_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedColonizingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedColonizingOption(newValue);
		}
	};
	ParamList councilWin		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "COUNCIL_WIN", getCouncilWinOptions(), COUNCIL_REBELS) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedCouncilWinOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedCouncilWinOption(newValue);
		}
		@Override protected String descriptionId() {
			return "SETTINGS_COUNCIL_DESC";
		}
	};
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
		{ showFullGuide(false); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedAutoplayOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedAutoplayOption(newValue);
		}
	};
	default ParamList autoplay()	{ return autoplay; }
	ParamList researchRate		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "RESEARCH_RATE", getResearchRateOptions(), RESEARCH_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedResearchRate();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedResearchRate(newValue);
		}
	};
	ParamList warpSpeed		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "WARP_SPEED", getWarpSpeedOptions(), WARP_SPEED_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedWarpSpeedOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedWarpSpeedOption(newValue);
		}
	};
	ParamList fuelRange		= new ParamList( // Duplicate Do not add the list
			ADV_UI, "FUEL_RANGE", getFuelRangeOptions(), FUEL_RANGE_NORMAL) {
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedFuelRangeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedFuelRangeOption(newValue);
			if (GameSession.instance().status().inProgress())
				GameSession.instance().galaxy().resetAllAI();
		}
	};
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
		{ showFullGuide(true); }
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedAIHostilityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedAIHostilityOption(newValue);
		}
	};
	// ==================== GUI List Declarations ====================
	//
	LinkedList<IParam> advancedOptions	  = new LinkedList<>(
			Arrays.asList(
					galaxyAge, starDensity, nebulae, planetQuality, terraforming,
					null,
					randomEvents, aiHostility, councilWin, randomizeAI, autoplay,
					null,
					researchRate, warpSpeed, fuelRange, techTrading, colonizing
					));

}
