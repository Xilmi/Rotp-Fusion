package rotp.model.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamCR;
import rotp.ui.util.PlayerShipSet;

public interface IRaceOptions extends IBaseOptsTools {

	String defaultRace = "RACE_HUMAN";
	default String defaultRace() { return defaultRace; }
	
	static LinkedList<String> getBaseRaceOptions() {
    	LinkedList<String> list = new LinkedList<>();
        list.add(defaultRace);
        list.add("RACE_ALKARI");
        list.add("RACE_SILICOID");
        list.add("RACE_MRRSHAN");
        list.add("RACE_KLACKON");
        list.add("RACE_MEKLAR");
        list.add("RACE_PSILON");
        list.add("RACE_DARLOK");
        list.add("RACE_SAKKRA");
        list.add("RACE_BULRATHI");
        return list;
    }
	LinkedList<String> baseRaceOptions = getBaseRaceOptions(); 
	default LinkedList<String> baseRaceOptions() { return getBaseRaceOptions(); } 

	static LinkedList<String> getAllRaceOptions() {
    	LinkedList<String> list = getBaseRaceOptions();
        list.add("RACE_NEOHUMAN");   // modnar: add races
		list.add("RACE_MONOCLE");    // modnar: add races
		list.add("RACE_JACKTRADES"); // modnar: add races
		list.add("RACE_EARLYGAME");  // modnar: add races
		list.add("RACE_WARDEMON");   // modnar: add races
        list.add("RACE_GEARHEAD");   // modnar: add races
        return list;
    }
	LinkedList<String> allRaceOptions = getAllRaceOptions(); 
    default LinkedList<String> allRaceOptions() { return getAllRaceOptions(); } 

	// ==================== Race Menu addition ====================
	//
	PlayerShipSet playerShipSet		= new PlayerShipSet(
			MOD_UI, "PLAYER_SHIP_SET");
	default int selectedPlayerShipSetId()			{ return playerShipSet.realShipSetId(); }

	ParamBoolean  playerIsCustom	= new ParamBoolean( BASE_UI, "BUTTON_CUSTOM_PLAYER_RACE", false);
	default ParamBoolean playerIsCustom()			{ return playerIsCustom; }
	default boolean selectedPlayerIsCustom()		{ return playerIsCustom.get(); }
	default void selectedPlayerIsCustom(boolean is)	{ playerIsCustom.set(is); }

	ParamCR       playerCustomRace	= new ParamCR(MOD_UI, defaultRace);
	default ParamCR playerCustomRace()						 { return playerCustomRace; }
	default Serializable selectedPlayerCustomRace()			 { return playerCustomRace.get(); }
	default void selectedPlayerCustomRace(Serializable race) { playerCustomRace.set(race); }
	// Custom Race Menu
	static LinkedList<InterfaceParam> optionsCustomRaceBase = new LinkedList<>(
			Arrays.asList(
					playerIsCustom, playerCustomRace
					));
	default LinkedList<InterfaceParam> optionsCustomRace() {
		LinkedList<InterfaceParam> list = new LinkedList<>();
		list.addAll(optionsCustomRaceBase);
		return list;
	}
	// ==================== GUI List Declarations ====================
	//
	static LinkedList<InterfaceParam> optionsRace = new LinkedList<>(
			Arrays.asList(
					playerShipSet, playerIsCustom, playerCustomRace
					));
	default LinkedList<InterfaceParam> optionsRace()	{ return optionsRace; }

	LinkedList<InterfaceParam> editCustomRace = new LinkedList<>(); // TODO BR: Fake list
	default LinkedList<InterfaceParam> editCustomRace()	{ return editCustomRace; }

}
