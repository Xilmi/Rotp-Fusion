package rotp.model.game;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.game.GameUI;
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
		list.add("RACE_MONOCLE");	// modnar: add races
		list.add("RACE_JACKTRADES"); // modnar: add races
		list.add("RACE_EARLYGAME");  // modnar: add races
		list.add("RACE_WARDEMON");   // modnar: add races
		list.add("RACE_GEARHEAD");   // modnar: add races
		return list;
	}
	LinkedList<String> allRaceOptions = getAllRaceOptions(); 
	default LinkedList<String> allRaceOptions() { return getAllRaceOptions(); } 

	default int numberSpecies() { return allRaceOptions.size(); }; // BR: This value have to be easily available

	default BufferedImage getMugBackImg(int w, int h, float radius) {
		BufferedImage backImg = new BufferedImage(w, h, TYPE_INT_ARGB);
		Point2D center = new Point2D.Float(w/2f, h/2f);
		float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
		Color[] colors = {GameUI.raceCenterColor(), GameUI.raceCenterColor(), GameUI.raceEdgeColor(), GameUI.raceEdgeColor()};
		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
		Graphics2D g = (Graphics2D) backImg.getGraphics();
		g.setPaint(p);
		g.fillRect(0, 0, w, h);
		g.dispose();
		return backImg;
	}
	// ==================== Race Menu addition ====================
	//
   
	PlayerShipSet playerShipSet		= new PlayerShipSet(
			MOD_UI, "PLAYER_SHIP_SET");
	default int selectedPlayerShipSetId()			{ return playerShipSet.realShipSetId(); }

	ParamBoolean playerIsCustom	= new ParamBoolean( BASE_UI, "BUTTON_CUSTOM_PLAYER_RACE", false, false);
	
	default ParamBoolean playerIsCustom()			{ return playerIsCustom; }
	default boolean selectedPlayerIsCustom()		{ return playerIsCustom.get(); }
	default void selectedPlayerIsCustom(boolean is)	{ playerIsCustom.set(is); }

	ParamCR		playerCustomRace	= new ParamCR(MOD_UI, defaultRace);
	default ParamCR playerCustomRace()						 { return playerCustomRace; }
	default Serializable selectedPlayerCustomRace()			 { return playerCustomRace.get(); }
	default void selectedPlayerCustomRace(Serializable race) { playerCustomRace.set(race); }
	// Custom Race Menu
	static SafeListParam optionsCustomRaceBase = new SafeListParam(
			Arrays.asList(
					playerIsCustom, playerCustomRace
					));
	default SafeListParam optionsCustomRace() {
		SafeListParam list = new SafeListParam();
		list.addAll(optionsCustomRaceBase);
		return list;
	}
	// ==================== GUI List Declarations ====================
	//
	static SafeListParam optionsRace = new SafeListParam(
			Arrays.asList(
					playerShipSet, playerIsCustom, playerCustomRace
					));
	default SafeListParam optionsRace()	{ return optionsRace; }

	SafeListParam editCustomRace = new SafeListParam();
	default SafeListParam editCustomRace()	{ return editCustomRace; }

}
