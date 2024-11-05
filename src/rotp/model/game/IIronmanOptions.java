package rotp.model.game;

import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

public interface IIronmanOptions extends IBaseOptsTools {
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
			.isValueInit(false)
			.setDefaultValue(MOO1_DEFAULT, true);
	default boolean isPersistentArtifact()	{ return persistentArtifact.get(); }
	
	ParamBoolean researchMoo1		= new ParamBoolean(MOD_UI, "RESEARCH_MOO1", false)
			.setDefaultValue(MOO1_DEFAULT, true);
	default boolean researchMoo1()			{ return researchMoo1.get(); }

	ParamBoolean persistentRNG		= new ParamBoolean(MOD_UI, "PERSISTENT_RNG", false)
			.setDefaultValue(MOO1_DEFAULT, true);
	default boolean persistentRNG()			{ return persistentRNG.get(); }

	ParamBoolean allowSpeciesDetails	= new ParamBoolean(MOD_UI, "ALLOW_SPECIES_DETAILS", true);
	default boolean allowSpeciesDetails()	{ return allowSpeciesDetails.get(); }
}
