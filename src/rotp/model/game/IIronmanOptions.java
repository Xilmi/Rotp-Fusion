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

	ParamList ironmanMode	= new ParamList( MOD_UI, "IRONMAN_MODE", "Off") {
		{
			showFullGuide(true);
			put("Off",			MOD_UI + "IRONMAN_OFF");
			put("NoOptions",	MOD_UI + "IRONMAN_NO_OPTIONS");
		}
	};
	default boolean isGameOptionsAllowed()	{ return ironmanMode.get().equalsIgnoreCase("Off"); }
	default boolean isSaveOptionsAllowed()	{ return !ironmanMode.get().equalsIgnoreCase("NoSave"); }

	ParamBoolean ironmanNoLoad		= new ParamBoolean(MOD_UI, "IRONMAN_NO_LOAD", false);
	default boolean selectedIronmanLoad()	{ return ironmanNoLoad.get(); }
	default boolean ironmanLocked()			{ return ironmanNoLoad.get() && GameSession.ironmanLocked(); }

	ParamInteger ironmanLoadDelay	= new ParamInteger( MOD_UI, "IRONMAN_LOAD_DELAY", 10, 1, 500, 1, 5, 20);
	default int selectedIronmanLoadDelay()	{ return ironmanLoadDelay.get(); }

	ParamBoolean repeatableArtifact	= new ParamBoolean(MOD_UI, "REPEATABLE_ARTIFACT", false);
	default boolean selectedRepeatableArtifact()	{ return repeatableArtifact.get(); }

	// ==================== GUI List Declarations ====================
	//

	LinkedList<LinkedList<IParam>> ironmanOptionsMap = new LinkedList<LinkedList<IParam>>()
	{ {
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("IRONMAN_MAIN"),
				ironmanMode
				
//				headerSpacer,
//				new ParamTitle("IRONMAN_FORMER"),
				)));
		add(new LinkedList<>(Arrays.asList(
				new ParamTitle("IRONMAN_CUSTOM"),
				IGameOptions.fixedEventsMode,
				repeatableArtifact,
				ironmanNoLoad, ironmanLoadDelay
				)));
		}
	};
	ParamSubUI ironmanOptionsUI = new ParamSubUI( MOD_UI, "IRONMAN_OPTIONS_UI", ironmanOptionsMap,
			"IRONMAN_OPTIONS_TITLE", IRONMAN_GUI_ID)
	{ { isCfgFile(true); } };


	LinkedList<IParam> ironmanOptions = ironmanOptionsUI.optionsList();
}
