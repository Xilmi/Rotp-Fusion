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
import rotp.ui.util.IParam;
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
//	default BufferedImage[] getMugImgArray(int w, int h, int radius, int n1, int n2, float factor) {
//		int   w2 = (int)(factor * w);
//		int   h2 = (int)(factor * h);
//		float r2 = factor * radius;
//
//		BufferedImage[] mug1 = getMugImgArray(w, h, radius, n1);
//		BufferedImage[] mug2 = getMugImgArray(w2, h2, r2, n2-n1);
//		BufferedImage[] mugs = ArrayUtils.addAll(mug1, mug2);
//		return mugs;
//	}
//	default BufferedImage[] getMugImgArray(int boxW, int boxH, float shadeRadius, int num) {
//		BufferedImage[] mugs = new BufferedImage[num];
//		BufferedImage back = getMugBackImg(boxW, boxH, shadeRadius);
//		for (int i=0; i<num; i++) {
//			mugs[i] = new BufferedImage(boxW, boxH, TYPE_INT_ARGB);
//			Graphics2D g = (Graphics2D) mugs[i].getGraphics();
//			g.setComposite(AlphaComposite.SrcOver);
//			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
//			g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//			
//			g.drawImage(back, 0, 0, boxW, boxH, null); // modnar: 80% size box for newRaces
//
//			Image img = Race.keyed(allRaceOptions.get(i)).diploMugshotQuiet();
//			int imgW = img.getHeight(null);
//			int imgH = img.getHeight(null);
//			g.drawImage(img, 0, 0, boxW, boxH, 0, 0, imgW, imgH, null);
//			g.dispose();
//		}
//		return mugs;
//	}

	// ==================== Race Menu addition ====================
	//
   
	PlayerShipSet playerShipSet		= new PlayerShipSet(
			MOD_UI, "PLAYER_SHIP_SET");
	default int selectedPlayerShipSetId()			{ return playerShipSet.realShipSetId(); }

	ParamBoolean playerIsCustom	= new ParamBoolean( BASE_UI, "BUTTON_CUSTOM_PLAYER_RACE", false);
	default ParamBoolean playerIsCustom()			{ return playerIsCustom; }
	default boolean selectedPlayerIsCustom()		{ return playerIsCustom.get(); }
	default void selectedPlayerIsCustom(boolean is)	{ playerIsCustom.set(is); }

	ParamCR		playerCustomRace	= new ParamCR(MOD_UI, defaultRace);
	default ParamCR playerCustomRace()						 { return playerCustomRace; }
	default Serializable selectedPlayerCustomRace()			 { return playerCustomRace.get(); }
	default void selectedPlayerCustomRace(Serializable race) { playerCustomRace.set(race); }
	// Custom Race Menu
	static LinkedList<IParam> optionsCustomRaceBase = new LinkedList<>(
			Arrays.asList(
					playerIsCustom, playerCustomRace
					));
	default LinkedList<IParam> optionsCustomRace() {
		LinkedList<IParam> list = new LinkedList<>();
		list.addAll(optionsCustomRaceBase);
		return list;
	}
	// ==================== GUI List Declarations ====================
	//
	static LinkedList<IParam> optionsRace = new LinkedList<>(
			Arrays.asList(
					playerShipSet, playerIsCustom, playerCustomRace
					));
	default LinkedList<IParam> optionsRace()	{ return optionsRace; }

	LinkedList<IParam> editCustomRace = new LinkedList<>();
	default LinkedList<IParam> editCustomRace()	{ return editCustomRace; }

}
