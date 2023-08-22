package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IRandomEvents extends IBaseOptsTools {

	String RANDOM_EVENTS_GUI_ID	= "RANDOM_EVENTS";
	String SPECIAL_DISABLED		= MOD_UI + "RANDOM_EVENT_DISABLED";
	String SPECIAL_UNIQUE		= MOD_UI + "RANDOM_EVENT_UNIQUE";
	Integer MAX_DELAY_TURN	= null; 
	Integer MAX_RETURN_TURN	= null; 
	Integer MAX_SYSTEMS		= null; 

	// ========================================================================
	// BR: RANDOM EVENT GLOBAL PARAMETERS
	ParamBoolean fixedEventsMode	= new ParamBoolean(MOD_UI, "FIXED_EVENTS_MODE", false);
	default boolean selectedFixedEventsMode()	{ return fixedEventsMode.get(); }
	
	ParamInteger eventsStartTurn	= new ParamInteger(MOD_UI, "EVENTS_START_TURN", 50, 1, MAX_DELAY_TURN, 1, 5, 20);
	
	// ========================================================================
	// BR: RANDOM EVENT MONSTERS PARAMETERS
	ParamInteger piratesDelayTurn	= new ParamInteger(MOD_UI, "PIRATES_DELAY_TURN", 25, 0, MAX_DELAY_TURN, 1, 5, 20);
	default int selectedPiratesDelayTurn()		{ return piratesDelayTurn.get(); }

	ParamInteger amoebaDelayTurn	= new ParamInteger(MOD_UI, "AMOEBA_DELAY_TURN", 100, 0, MAX_DELAY_TURN, 1, 5, 20);
	default int selectedAmoebaDelayTurn()		{ return amoebaDelayTurn.get(); }

	ParamInteger crystalDelayTurn	= new ParamInteger(MOD_UI, "CRYSTAL_DELAY_TURN", 100, 0, MAX_DELAY_TURN, 1, 5, 20);
	default int selectedCrystalDelayTurn()		{ return crystalDelayTurn.get(); }

	ParamInteger piratesReturnTurn	= new ParamInteger(MOD_UI, "PIRATES_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20);
	default int selectedPiratesReturnTurn()		{ return piratesReturnTurn.get(); }

	ParamInteger amoebaReturnTurn	= new ParamInteger(MOD_UI, "AMOEBA_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20);
	default int selectedAmoebaReturnTurn()		{ return amoebaReturnTurn.get(); }

	ParamInteger crystalReturnTurn	= new ParamInteger(MOD_UI, "CRYSTAL_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20);
	default int selectedCrystalReturnTurn()		{ return crystalReturnTurn.get(); }

	ParamInteger piratesMaxSystems	= new ParamInteger(MOD_UI, "PIRATES_MAX_SYSTEMS", 0, 0, MAX_SYSTEMS, 1, 5, 20);
	default int selectedPiratesMaxSystems()		{ return piratesMaxSystems.get(); }

	ParamInteger amoebaMaxSystems	= new ParamInteger(MOD_UI, "AMOEBA_MAX_SYSTEMS", 0, 0, MAX_SYSTEMS, 1, 5, 20);
	default int selectedAmoebaMaxSystems()		{ return amoebaMaxSystems.get(); }

	ParamInteger crystalMaxSystems	= new ParamInteger(MOD_UI, "CRYSTAL_MAX_SYSTEMS", 0, 0, MAX_SYSTEMS, 1, 5, 20);
	default int selectedCrystalMaxSystems()		{ return crystalMaxSystems.get(); }

	// ========================================================================
	// BR: RANDOM EVENT OTHER DELAY PARAMETERS
	ParamInteger donationDelayTurn	= new ParamInteger(MOD_UI, "DONATION_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedDonationDelayTurn()		{ return donationDelayTurn.get(); }

	ParamInteger depletedDelayTurn	= new ParamInteger(MOD_UI, "DEPLETED_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedDepletedDelayTurn()		{ return depletedDelayTurn.get(); }

	ParamInteger enrichedDelayTurn	= new ParamInteger(MOD_UI, "ENRICHED_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedEnrichedDelayTurn()		{ return enrichedDelayTurn.get(); }

	ParamInteger fertileDelayTurn	= new ParamInteger(MOD_UI, "FERTILE_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedFertileDelayTurn()		{ return fertileDelayTurn.get(); }

	ParamInteger virusDelayTurn		= new ParamInteger(MOD_UI, "VIRUS_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedVirusDelayTurn()		{ return virusDelayTurn.get(); }

	ParamInteger earthquakeDelayTurn = new ParamInteger(MOD_UI, "EARTHQUAKE_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedEarthquakeDelayTurn()	{ return earthquakeDelayTurn.get(); }

	ParamInteger accidentDelayTurn	= new ParamInteger(MOD_UI, "ACCIDENT_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedAccidentDelayTurn()		{ return accidentDelayTurn.get(); }

	ParamInteger rebellionDelayTurn	= new ParamInteger(MOD_UI, "REBELLION_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedRebellionDelayTurn()	{ return rebellionDelayTurn.get(); }

	ParamInteger derelictDelayTurn	= new ParamInteger(MOD_UI, "DERELICT_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedDerelictDelayTurn()		{ return derelictDelayTurn.get(); }

	ParamInteger assassinDelayTurn	= new ParamInteger(MOD_UI, "ASSASSIN_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedAssassinDelayTurn()		{ return assassinDelayTurn.get(); }

	ParamInteger plagueDelayTurn	= new ParamInteger(MOD_UI, "PLAGUE_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedPlagueDelayTurn()		{ return plagueDelayTurn.get(); }

	ParamInteger supernovaDelayTurn	= new ParamInteger(MOD_UI, "SUPERNOVA_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedSupernovaDelayTurn()	{ return supernovaDelayTurn.get(); }

	ParamInteger piracyDelayTurn	= new ParamInteger(MOD_UI, "PIRACY_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedPiracyDelayTurn()		{ return piracyDelayTurn.get(); }

	ParamInteger cometDelayTurn		= new ParamInteger(MOD_UI, "COMET_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedCometDelayTurn()		{ return cometDelayTurn.get(); }

	ParamInteger relicDelayTurn	= new ParamInteger(MOD_UI, "RELIC_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedRelicDelayTurn()		{ return relicDelayTurn.get(); }

	ParamInteger sizeBoostDelayTurn	= new ParamInteger(MOD_UI, "SIZEBOOST_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedSizeBoostDelayTurn()	{ return sizeBoostDelayTurn.get(); }

	ParamInteger gauntletDelayTurn	= new ParamInteger(MOD_UI, "GAUNTLET_DELAY_TURN", 0, -1, MAX_DELAY_TURN, 1, 5, 20)
			.specialNegative(SPECIAL_DISABLED);
	default int selectedGauntletDelayTurn()		{ return gauntletDelayTurn.get(); }

	// ========================================================================
	// BR: RANDOM EVENT OTHER RETURN PARAMETERS
	ParamInteger donationReturnTurn	= new ParamInteger(MOD_UI, "DONATION_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedDonationReturnTurn()	{ return donationReturnTurn.get(); }

	ParamInteger depletedReturnTurn	= new ParamInteger(MOD_UI, "DEPLETED_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedDepletedReturnTurn()	{ return depletedReturnTurn.get(); }

	ParamInteger enrichedReturnTurn	= new ParamInteger(MOD_UI, "ENRICHED_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedEnrichedReturnTurn()	{ return enrichedReturnTurn.get(); }

	ParamInteger fertileReturnTurn	= new ParamInteger(MOD_UI, "FERTILE_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedFertileReturnTurn()		{ return fertileReturnTurn.get(); }

	ParamInteger virusReturnTurn	= new ParamInteger(MOD_UI, "VIRUS_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedVirusReturnTurn()		{ return virusReturnTurn.get(); }

	ParamInteger earthquakeReturnTurn = new ParamInteger(MOD_UI, "EARTHQUAKE_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedEarthquakeReturnTurn()	{ return earthquakeReturnTurn.get(); }

	ParamInteger accidentReturnTurn	= new ParamInteger(MOD_UI, "ACCIDENT_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedAccidentReturnTurn()	{ return accidentReturnTurn.get(); }

	ParamInteger rebellionReturnTurn = new ParamInteger(MOD_UI, "REBELLION_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedRebellionReturnTurn()	{ return rebellionReturnTurn.get(); }

	ParamInteger derelictReturnTurn	= new ParamInteger(MOD_UI, "DERELICT_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedDerelictReturnTurn()	{ return derelictReturnTurn.get(); }

	ParamInteger assassinReturnTurn	= new ParamInteger(MOD_UI, "ASSASSIN_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedAssassinReturnTurn()	{ return assassinReturnTurn.get(); }

	ParamInteger plagueReturnTurn	= new ParamInteger(MOD_UI, "PLAGUE_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedPlagueReturnTurn()		{ return plagueReturnTurn.get(); }

	ParamInteger supernovaReturnTurn = new ParamInteger(MOD_UI, "SUPERNOVA_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedSupernovaReturnTurn()	{ return supernovaReturnTurn.get(); }

	ParamInteger piracyReturnTurn	= new ParamInteger(MOD_UI, "PIRACY_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedPiracyReturnTurn()		{ return piracyReturnTurn.get(); }

	ParamInteger cometReturnTurn	= new ParamInteger(MOD_UI, "COMET_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedCometReturnTurn()		{ return cometReturnTurn.get(); }

	ParamInteger relicReturnTurn = new ParamInteger(MOD_UI, "RELIC_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedRelicReturnTurn()		{ return relicReturnTurn.get(); }

	ParamInteger sizeBoostReturnTurn = new ParamInteger(MOD_UI, "SIZEBOOST_RETURN_TURN", 1, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedSizeBoostReturnTurn()	{ return sizeBoostReturnTurn.get(); }

	ParamInteger gauntletReturnTurn	= new ParamInteger(MOD_UI, "GAUNTLET_RETURN_TURN", 0, 0, MAX_RETURN_TURN, 1, 5, 20)
			.specialZero(SPECIAL_UNIQUE);
	default int selectedGauntletReturnTurn()	{ return gauntletReturnTurn.get(); }
	

//	ParamInteger maxPlanTime		= new ParamInteger(MOD_UI, "MAX_PLAN_TIME", 20, 0, 1440, 1, 5, 20);
//	default long maxPlanTimeMS()				{
//		final long min2ms = 60000;
//		return min2ms * maxPlanTime.get();
//	}
//
//	ParamList warpDisturbances	= new ParamList( MOD_UI, "WARP_DISTURBANCES", "Off") {
//		{
//			showFullGuide(true);
//			put("Off",		MOD_UI + "WARP_DISTURBANCES_OFF");
//			put("On",		MOD_UI + "WARP_DISTURBANCES_ON");
//			put("Triggered",MOD_UI + "WARP_DISTURBANCES_TRIGGERED");
//		}
//	};
//	default boolean warpDisturbancesTriggered()	{ return warpDisturbances.get().equalsIgnoreCase("Triggered"); }
//	default boolean warpDisturbancesOff()		{ return warpDisturbances.get().equalsIgnoreCase("Off"); }
//	default void resetWarpDisturbances()	 	{ warpDisturbances.set("On"); }
//	default void triggerWarpDisturbances()	 	{ warpDisturbances.set("Triggered"); }
//	default boolean planTimeCheck (long msTime) {
//		if (warpDisturbancesOff())
//			return false;
//		if (msTime < maxPlanTimeMS())
//			return false;
//		triggerWarpDisturbances();
//		return true;
//	}

	// ==================== GUI List Declarations ====================
	LinkedList<LinkedList<IParam>> customRandomEventMap = 
			new LinkedList<LinkedList<IParam>>() { {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_GLOBAL"),
				fixedEventsMode, eventsStartTurn,

				headerSpacer,
				new ParamTitle("RANDOM_EVENTS_MONSTERS"),
				piratesDelayTurn, piratesReturnTurn, piratesMaxSystems,
				amoebaDelayTurn, amoebaReturnTurn, amoebaMaxSystems,
				crystalDelayTurn, crystalReturnTurn, crystalMaxSystems
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_DELAYS"),
				donationDelayTurn,
				depletedDelayTurn,
				enrichedDelayTurn,
				fertileDelayTurn,
				virusDelayTurn,
				earthquakeDelayTurn,
				accidentDelayTurn,
				rebellionDelayTurn,
				derelictDelayTurn,
				assassinDelayTurn,
				plagueDelayTurn,
				supernovaDelayTurn,
				piracyDelayTurn,
				cometDelayTurn,
				relicDelayTurn,
				sizeBoostDelayTurn,
				gauntletDelayTurn
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_RETURNS"),
				donationReturnTurn,
				depletedReturnTurn,
				enrichedReturnTurn,
				fertileReturnTurn,
				virusReturnTurn,
				earthquakeReturnTurn,
				accidentReturnTurn,
				rebellionReturnTurn,
				derelictReturnTurn,
				assassinReturnTurn,
				plagueReturnTurn,
				supernovaReturnTurn,
				piracyReturnTurn,
				cometReturnTurn,
				relicReturnTurn,
				sizeBoostReturnTurn,
				gauntletReturnTurn
				)));
		}
	};
	ParamSubUI customRandomEventUI = new ParamSubUI(MOD_UI, "RANDOM_EVENTS_UI", customRandomEventMap,
			"RANDOM_EVENTS_TITLE", RANDOM_EVENTS_GUI_ID);

	LinkedList<IParam> customRandomEventOptions = customRandomEventUI.optionsList();
}
