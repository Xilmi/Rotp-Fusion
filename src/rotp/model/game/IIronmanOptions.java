package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface IIronmanOptions extends IBaseOptsTools {
	String IRONMAN_GUI_ID	= "IRONMAN_OPTIONS";

	ParamList ironmanMode	= new ParamList( MOD_UI, "IRONMAN_MODE", "Off")
			.showFullGuide(true)
			.isValueInit(false)
			.put("Off",			MOD_UI + "IRONMAN_OFF")
			.put("NoOptions",	MOD_UI + "IRONMAN_NO_OPTIONS");
	default boolean isGameOptionsAllowed()	{ return ironmanMode.get().equalsIgnoreCase("Off"); }
	default boolean isSaveOptionsAllowed()	{ return !ironmanMode.get().equalsIgnoreCase("NoSave"); }

	ParamBoolean ironmanNoLoad		= new ParamBoolean(MOD_UI, "IRONMAN_NO_LOAD", false)
			.isValueInit(false);
	default boolean selectedIronmanLoad()	{ return ironmanNoLoad.get(); }
	default boolean ironmanLocked()			{ return ironmanNoLoad.get() && GameSession.ironmanLocked(); }

	ParamInteger ironmanLoadDelay	= new ParamInteger( MOD_UI, "IRONMAN_LOAD_DELAY", 10)
			.setLimits(1, 500)
			.setIncrements(1, 5, 20);
	default int selectedIronmanLoadDelay()	{ return ironmanLoadDelay.get(); }

	ParamBoolean persistentArtifact	= new ParamBoolean(MOD_UI, "REPEATABLE_ARTIFACT", false)
			.isValueInit(false);
	default boolean isPersistentArtifact()	{ return persistentArtifact.get(); }
	
	ParamBoolean researchMoo1		= new ParamBoolean(MOD_UI, "RESEARCH_MOO1", false);
	default boolean researchMoo1()			{ return researchMoo1.get(); }

	ParamBoolean persistentRNG		= new ParamBoolean(MOD_UI, "PERSISTENT_RNG", false);
	default boolean persistentRNG()			{ return persistentRNG.get(); }

	ParamBoolean allowSpeciesDetails	= new ParamBoolean(MOD_UI, "ALLOW_SPECIES_DETAILS", true);
	default boolean allowSpeciesDetails()	{ return allowSpeciesDetails.get(); }


	// ==================== GUI List Declarations ====================
	//
	ParamSubUI ironmanOptionsUI = ironmanOptionsUI();

	static LinkedList<LinkedList<IParam>> ironmanOptionsMap() {
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("IRONMAN_MAIN"),
				ironmanMode
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("IRONMAN_CUSTOM"),
				IGameOptions.fixedEventsMode,
				persistentArtifact,
				ironmanNoLoad, ironmanLoadDelay,
				researchMoo1, persistentRNG,
				allowSpeciesDetails
				)));
		return map;
	};
	static ParamSubUI ironmanOptionsUI() {
		return new ParamSubUI( MOD_UI, "IRONMAN_OPTIONS_UI", ironmanOptionsMap(),
				"IRONMAN_OPTIONS_TITLE", IRONMAN_GUI_ID).isCfgFile(true);
	}
	static LinkedList<IParam> ironmanOptions() {
		return IBaseOptsTools.getSingleList(ironmanOptionsMap());
	}
}
