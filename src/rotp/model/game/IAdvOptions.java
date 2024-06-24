package rotp.model.game;

import static rotp.model.game.DefaultValues.MOO1_DEFAULT;
import static rotp.model.game.IGameOptions.AUTOPLAY_OFF;
import static rotp.model.game.IGameOptions.AI_HOSTILITY_NORMAL;
import static rotp.model.game.IGameOptions.COLONIZING_NORMAL;
import static rotp.model.game.IGameOptions.COUNCIL_IMMEDIATE;
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
import static rotp.model.game.ISystemsOptions.firstRingRadius;
import static rotp.model.game.ISystemsOptions.firstRingSystemNumber;
import static rotp.model.game.ISystemsOptions.radiusToNumStars;
import static rotp.model.game.ISystemsOptions.secondRingRadius;
import static rotp.model.game.ISystemsOptions.secondRingSystemNumber;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.util.IParam;
import rotp.ui.util.LinkData;
import rotp.ui.util.LinkValue;
import rotp.ui.util.ParamList;

// Duplicates Options, Race Menu Options and Galaxy Options
//
public interface IAdvOptions extends IBaseOptsTools {
	
	// ==================== Duplicates for Base Advanced Options ====================
	//
	ParamList galaxyAge = new GalaxyAge(); // Duplicate Do not add the list
	class GalaxyAge extends ParamList {
		GalaxyAge() {
			super(ADV_UI, "GALAXY_AGE", getGalaxyAgeOptions(), GALAXY_AGE_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedGalaxyAge();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedGalaxyAge(newValue);
		}
	}

	default float densitySizeFactor(String density)	{ return getDensitySizeFactor(density); }
	static  float getDensitySizeFactor(String densityOption) {
        switch (densityOption) {
	        case IGameOptions.STAR_DENSITY_LONELY:  return 1.3f;
	        case IGameOptions.STAR_DENSITY_U_WIDE:  return 1.3f;
	        case IGameOptions.STAR_DENSITY_V_WIDE:  return 1.3f;
	        case IGameOptions.STAR_DENSITY_WIDER:   return 1.3f;
	        case IGameOptions.STAR_DENSITY_WIDE:    return 1.3f;
	        case IGameOptions.STAR_DENSITY_LOWEST:  return 1.3f;
            case IGameOptions.STAR_DENSITY_LOWER:   return 1.2f;
            case IGameOptions.STAR_DENSITY_LOW:     return 1.1f;
            case IGameOptions.STAR_DENSITY_HIGH:    return 0.9f;
            case IGameOptions.STAR_DENSITY_HIGHER:  return 0.8f;
            case IGameOptions.STAR_DENSITY_HIGHEST: return 0.7f;
        }
        return 1.0f;
    }
	default float systemBuffer(String density)	{ return getSystemBuffer(density); }
	public static float getSystemBuffer(String densityOption) { 
//		switch (densityOption) {
//			case IGameOptions.STAR_DENSITY_LOWEST:  return 2.5f;
//			case IGameOptions.STAR_DENSITY_LOWER:   return 2.3f;
//			case IGameOptions.STAR_DENSITY_LOW:		return 2.1f;
//			case IGameOptions.STAR_DENSITY_HIGH:	return 1.7f;
//			case IGameOptions.STAR_DENSITY_HIGHER:  return 1.5f;
//			case IGameOptions.STAR_DENSITY_HIGHEST: return 1.3f;
//		}
		return 1.9f * getDensitySizeFactor(densityOption);
	}
	float densitySizeFactor();
	default ParamList starDensity()	{ return starDensity; }
	ParamList starDensity = new StarDensity(); // Duplicate Do not add the list
	class StarDensity extends ParamList {
		StarDensity() {
			super(ADV_UI, "STAR_DENSITY", getStarDensityOptions(), STAR_DENSITY_NORMAL);
			showFullGuide(true);
		}
		@Override public void lateInit(int level)	{
			if (level == 0) {
				resetLinks();
				// Density numerical value = spreading
				// When spreading goes system number should go down.
				addLink(firstRingSystemNumber,  DO_LOCK, GO_UP, GO_DOWN, "Ring 1");
				addLink(secondRingSystemNumber, DO_LOCK, GO_UP, GO_DOWN, "Ring 2");
			}
			else
				super.lateInit(level);
		}
		@Override public boolean isValidValue()	{ return isValidDoubleCheck(); }
		@Override protected void convertValueToLink(LinkData rec)	{
			// Convert the current state
			switch (rec.key) {
				case "Ring 1":
					rec.aimValue = new LinkValue(radiusToNumStars(firstRingRadius.get()));
					return;
				case "Ring 2":
					rec.aimValue = new LinkValue(radiusToNumStars(secondRingRadius.get()));
					return;
				default:
					super.convertValueToLink(rec);
			}
		}
		@Override protected Boolean getDirectionOfChange(String before, String after) {
			float valBefore	= getDensitySizeFactor(before);
			float valAfter	= getDensitySizeFactor(after);
			if (valAfter > valBefore)
				return GO_UP;
			if (valAfter < valBefore)
				return GO_DOWN;
			return null;
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedStarDensityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedStarDensityOption(newValue);
		}
	}

	ParamList nebulae = new Nebulae(); // Duplicate Do not add the list
	class Nebulae extends ParamList {
		Nebulae() {
			super(ADV_UI, "NEBULAE", getNebulaeOptions(), NEBULAE_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedNebulaeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedNebulaeOption(newValue);
		}
	}

	ParamList randomEvents = new RandomEvents(); // Duplicate Do not add the list
	class RandomEvents extends ParamList {
		RandomEvents() {
			super(ADV_UI, "RANDOM_EVENTS", getRandomEventOptions(), RANDOM_EVENTS_NO_MONSTERS);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedRandomEventOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedRandomEventOption(newValue);
		}
	}

	ParamList planetQuality	= new PlanetQuality(); // Duplicate Do not add the list
	class PlanetQuality extends ParamList {
		PlanetQuality() {
			super(ADV_UI, "PLANET_QUALITY", getPlanetQualityOptions(), PLANET_QUALITY_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedPlanetQualityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedPlanetQualityOption(newValue);
		}
	}

	ParamList terraforming		= new Terraforming(); // Duplicate Do not add the list
	class Terraforming extends ParamList {
		Terraforming() {
			super(ADV_UI, "TERRAFORMING", getTerraformingOptions(), TERRAFORMING_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedTerraformingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedTerraformingOption(newValue);
		}
	}

	ParamList colonizing		= new Colonizing(); // Duplicate Do not add the list
	class Colonizing extends ParamList {
		Colonizing() {
			super(ADV_UI, "COLONIZING", getColonizingOptions(), COLONIZING_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedColonizingOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedColonizingOption(newValue);
		}
	}

	ParamList councilWin = new CouncilWin(); // Duplicate Do not add the list
	class CouncilWin extends ParamList {
		CouncilWin() {
			super(ADV_UI, "COUNCIL_WIN", getCouncilWinOptions(), COUNCIL_REBELS);
			this.setDefaultValue(MOO1_DEFAULT, COUNCIL_IMMEDIATE);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedCouncilWinOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedCouncilWinOption(newValue);
		}
		@Override protected String descriptionId() {
			return "SETTINGS_COUNCIL_DESC";
		}
	}

	ParamList randomizeAI = new RandomizeAI(); // Duplicate Do not add the list
	class RandomizeAI extends ParamList {
		RandomizeAI() {
			super(ADV_UI, "RANDOMIZE_AI", getRandomizeAIOptions(), RANDOMIZE_AI_NONE);
			showFullGuide(false);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedRandomizeAIOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedRandomizeAIOption(newValue);
		}
	}

	ParamList autoplay = new Autoplay(); // Duplicate Do not add the list
	default ParamList autoplay()	{ return autoplay; }
	class Autoplay extends ParamList {
		Autoplay() {
			super(ADV_UI, "AUTOPLAY",
					IGameOptions.autoPlayAIset().getAutoPlay(), AUTOPLAY_OFF);
			showFullGuide(false);
		}
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
	}

	ParamList researchRate		= new ResearchRate(); // Duplicate Do not add the list
	class ResearchRate extends ParamList {
		ResearchRate() {
			super(ADV_UI, "RESEARCH_RATE", getResearchRateOptions(), RESEARCH_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedResearchRate();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedResearchRate(newValue);
		}
	}

	ParamList warpSpeed	= new WarpSpeed(); // Duplicate Do not add the list
	class WarpSpeed extends ParamList {
		WarpSpeed() {
			super(ADV_UI, "WARP_SPEED", getWarpSpeedOptions(), WARP_SPEED_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedWarpSpeedOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedWarpSpeedOption(newValue);
		}
	}

	ParamList fuelRange	= new FuelRange(); // Duplicate Do not add the list
	class FuelRange extends ParamList {
		FuelRange() {
			super(ADV_UI, "FUEL_RANGE", getFuelRangeOptions(), FUEL_RANGE_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedFuelRangeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedFuelRangeOption(newValue);
			if (GameSession.instance().status().inProgress())
				GameSession.instance().galaxy().resetAllAI();
		}
	}

	ParamList techTrading = new TechTrading(); // Duplicate Do not add the list
	class TechTrading extends ParamList {
		TechTrading() {
			super(ADV_UI, "TECH_TRADING", getTechTradingOptions(), TECH_TRADING_YES);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedTechTradeOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedTechTradeOption(newValue);
		}
	}

	ParamList aiHostility	= new AiHostility(); // Duplicate Do not add the list
	class AiHostility extends ParamList {
		AiHostility() {
			super(ADV_UI, "AI_HOSTILITY", getAiHostilityOptions(), AI_HOSTILITY_NORMAL);
			showFullGuide(true);
		}
		@Override public String getOptionValue(IGameOptions options) {
			return options.selectedAIHostilityOption();
		}
		@Override public void setOptionValue(IGameOptions options, String newValue) {
			options.selectedAIHostilityOption(newValue);
		}
	}
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
