package rotp.model.combat;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static rotp.model.combat.CombatShield.ABOVE;
import static rotp.model.combat.CombatShield.BELLOW;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import rotp.Rotp;
import rotp.util.Base;


public class TestShields extends JPanel implements Base, ActionListener {
//public class TestShields extends JPanel implements ActionListener {
	private final float SCREEN_RATIO	= (float)9/16; 
	private final int SCREEN_WIDTH	= 3840; 
//	private final int SCREEN_WIDTH	= 1920; 
//	private final int SCREEN_WIDTH	= 1024; 
	private final int SCREEN_HEIGHT	= (int) (SCREEN_WIDTH * SCREEN_RATIO + 0.5f);
	private final int COLUMNS_NUM	= 10; 
	private final int ROWS_NUM		= 8; 
	private final int IMAGE_SEP		= 50;
	private final int s10			= scaled(10);

	private final Color spaceBlue = new Color(32,32,64);

	private String imagePath, imageName;
	private final String shipPath = Rotp.jarPath() + "\\..\\src\\rotp\\images\\ships\\";
	private final String monsterPath = Rotp.jarPath() + "\\..\\src\\rotp\\images\\missiles\\";
	private final String[] folders = new String[] {"Alkari", "Bulrathi", "Darlok", "Human",
			"Klackon", "Meklar", "Mrrshan", "Psilon", "Sakkra", "Silicoid", "Monsters"};
	private final String[] sizes = new String[] {"A", "B", "C", "D"};
	private final String[] models = new String[] {"01a", "02a", "03a", "04a", "05a", "06a"};
	private final String[] monsters = new String[] {"OrionGuardian", "SpaceCrystal", "SpacePirates", "SpaceAmoeba"};
	private int folderId, sizeId, modelId, monsterId;

	private BufferedImage[][][] shieldArray;
	private BufferedImage shipImg;
	private int totalImages, currentImage, animationDelay;
	private Timer animationTimer;
	private CombatShield cs;
	private int[] targetCtr = new int[] {0, 0, 0};
	private int[] sourcePos = new int[] {1000, 500, 100};
	private int[] srcLocX = new int[] {-1,  0,  1, 1, 1, 0, -1, -1};
	private int[] srcLocY = new int[] {-1, -1, -1, 0, 1, 1,  1,  0};
	private int srcLocation = 0;
	private Color shieldColor = Color.green;
	private int beamSize	  = 3;
	private int windUpFramesNum = 6;
	private int holdFramesNum   = 0;
	private int heavyHoldFramesNum   = 0;
	private int landUpFramesNum = windUpFramesNum;
	private int fadingFramesNum = windUpFramesNum+2;
	private int shieldLevel = 1;
	private float beamForce = 10f;
	private float damage	= 5f;
	private int imageWidth, imageHeight;
	private int shieldDX, shieldDY, ctrDX, ctrDY;
	private int shipWidth, shipHeight;
	private int boxWidth, boxHeight;
	private int windowWidth,windowHeight;
	private Rectangle winRec; 
	private float shipScale;
	private int sourceDiameter, sourceDelta;
	private int winTarX, winTarY;
	private int currentAttack = 0;
	private int sourceSize = 3;
	private int wpnCount = 10;
	private boolean isHeavy = false;
	private int shieldBorders;
	private boolean beamView = true;
	private boolean holdTimer = false;

	// Weapon parameters
	int weaponMaxId = 23;
	float cost, size, power;
	boolean restricted = false;
	int weaponId = 1;
	int damageLow = 0;
	int damageHigh = 0;
	int range = 1;
	boolean heavyAllowed = false;
	int heavyDamageLow = 0;
	int heavyDamageHigh = 0;
	int heavyRange = 2;
	int attacksPerRound = 1;
	int computer = 0;
	float enemyShieldMod = 1;
	boolean streaming = false;
	String soundEffect = "ShipLaser";
	int weaponSpread = 1; // 1, 2, 4, 7
	int windUpFrames = 3;
	int holdFrames = 0;
	int beamStroke, dashStroke;
	Color beamColor = Color.red;
	Color beamColor2;
	Color cycleColor;
	Color cycleColor2;
	Stroke weaponStroke;
	// \Weapon parameters
	
	private void initSizes() {
		shieldArray = new BufferedImage[attacksPerRound][][];
		windUpFramesNum = 6;
		holdFramesNum   = 6;
		landUpFramesNum = windUpFramesNum;
		fadingFramesNum = windUpFramesNum+2;
		// Box size
		boxWidth  = (SCREEN_WIDTH-scaled(20))/COLUMNS_NUM;
		boxHeight = (SCREEN_HEIGHT-scaled(65))/ROWS_NUM;
		// ship size
		BufferedImage baseShipImg = loadImage(imagePath + imageName);
		int baseShipWidth  = baseShipImg.getWidth();
		int baseShipHeight = baseShipImg.getHeight();
		shipScale  = Math.min((float)boxWidth/baseShipWidth, (float)boxHeight/baseShipHeight)*9/10;
		shipWidth  = (int) (baseShipWidth  * shipScale);
		shipHeight = (int) (baseShipHeight * shipScale);

		shipImg = new BufferedImage(shipWidth, shipHeight, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) shipImg.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(baseShipImg, 0, 0, shipWidth, shipHeight, 0, 0, baseShipWidth, baseShipHeight, null);
		g.dispose();
//		semiAxis = initSemiAxis(shipScale);
		imageWidth  = boxWidth + IMAGE_SEP;
		imageHeight = boxHeight + IMAGE_SEP;
		
		windowWidth  = imageWidth * 3 + 10; // + IMAGE_SEP;
		windowHeight = imageHeight * 3  + 30; // + IMAGE_SEP;
		winRec	= new Rectangle(windowWidth, windowHeight);
		winTarX = IMAGE_SEP + imageWidth  + boxWidth/2 ;
		winTarY = IMAGE_SEP + imageHeight + boxHeight/2;
		sourceDiameter = scaled(3);
		sourceDelta = scaled((sourceSize + 1) * 5);
	}
	private void init() {
		 folderId	= 0;
		 sizeId		= 0;
		 modelId	= 0;
		 monsterId	= 0;
		 loadShields();
	}
	private boolean isMonster() { return folderId == folders.length-1; }
	private int[] newSourcePos(int dx, int dy, int dz) {
		return new int[] {
				targetCtr[0] + dx * boxWidth - boxWidth/6,
				targetCtr[1] - dy * boxHeight,
				targetCtr[2] + dz
			};
	}
	private void loadShields() {
		// long timeStart = System.currentTimeMillis();
		// long timeMid = timeStart;
		System.out.println();
		if (isMonster()) {
			imagePath = monsterPath;
			imageName = monsters[monsterId] + ".png";			
		} else {
			imagePath = shipPath + folders[folderId] + "\\";
			imageName = sizes[sizeId] + models[modelId] + ".png";
		}
		initWeapon();
		attacksPerRound	= 1;
		currentAttack	= 0;
		shieldBorders	= scaled(1);
		initSizes();
		targetCtr = new int[] {winTarX, winTarY, 0};
		sourcePos = newSourcePos(1, 1, scaled(200));

		cs = new CombatShield(holdFramesNum, landUpFramesNum, fadingFramesNum,
				targetCtr[0], targetCtr[1], targetCtr[2], shieldColor, shieldBorders,
				shieldLevel, shipImg, boxWidth, boxHeight);
		cs.setNoise(0);
		cs.setImpact(0, 0, 0); // offset to ship center
		cs.setSource(sourcePos[0], sourcePos[1], sourcePos[2], beamColor, scaled(beamSize));
		cs.setWeapons(damage, beamForce);

		shieldArray[currentAttack] = cs.getShieldArray();
		shieldDX	= cs.shieldOffsetX();
		shieldDY	= cs.shieldOffsetY();
		ctrDX		= cs.centerOffsetX();
		ctrDY		= cs.centerOffsetY();
		totalImages = shieldArray[currentAttack][ABOVE].length;
		
		currentImage = 0;
//		System.out.println("loadShields: " + folders[folderId] + "\\" + imageName +
//				"  Time = " + (System.currentTimeMillis()-timeMid));
//		System.out.println("loadShields() Time = " + (System.currentTimeMillis()-timeMid));
	}
	private void paintLines(SortedMap<Integer, ArrayList<Line2D.Double>> lines, Graphics2D g) {
		for(ArrayList<Line2D.Double> lineList : lines.values()) {
			for(Line2D.Double line : lineList) {
				g.draw(line);
			}
		}
	}
	private void paintLines(ArrayList<Line2D.Double> lines, Graphics2D g) {
		for(Line2D.Double line : lines) {
			g.draw(line);
		}
	}
	private ArrayList<Line2D.Double> addMultiLines(int size, int weapons, int sx, int sy, int tx, int ty) {
		// Direct copy; avoid changes
		int offSet = scaled((size + 1) * 5);
		ArrayList<Line2D.Double> lines = new ArrayList<>();
		if(weapons < 3)
			lines.add(new Line2D.Double(sx, sy, tx, ty));
		else if(weapons < 6) {
			lines.add(new Line2D.Double(sx, sy + offSet, tx, ty));
			lines.add(new Line2D.Double(sx, sy - offSet, tx, ty));
		} else {
			lines.add(new Line2D.Double(sx, sy, tx, ty));
			lines.add(new Line2D.Double(sx, sy + offSet, tx, ty));
			lines.add(new Line2D.Double(sx, sy - offSet, tx, ty));
		}
		return lines;
	}
	private void initWeapon() {
		dashStroke = 0;
		restricted = false;
		damageLow = 0;
		damageHigh = 0;
		range = 1;
		heavyAllowed = false;
		heavyDamageLow = 0;
		heavyDamageHigh = 0;
		heavyRange = 2;
		attacksPerRound = 1;
		computer = 0;
		enemyShieldMod = 1;
		streaming = false;
		soundEffect = "ShipLaser";
		weaponSpread = 1; // 1, 2, 4, 7
		windUpFrames = 3;
		holdFrames = 0;
		beamStroke = 0;
		beamColor = Color.red;
		beamColor2 = null;
		cycleColor = null;
		cycleColor2 = null;
		weaponStroke = null;
		switch(weaponId) {
			case 0: // LASER
				damageLow = 1;
				damageHigh = 4;
				heavyAllowed = true;
				heavyDamageLow = 1;
				heavyDamageHigh = 7;
				cost = 8;
				size = 10;
				power = 25;
				beamColor = new Color(0x9f,0x33,0x35);
				beamStroke = 1;
				soundEffect = "ShipLaser";
				break;
			case 1: // GATLING LASER
				damageLow = 1;
				damageHigh = 4;
				attacksPerRound = 4;
				cost = 20;
				size = 20;
				power = 70;
				beamColor = new Color(0xbd,0x23,0x3a);
				beamColor2 = new Color(0x72,0x10,0x10);
				beamStroke = 1;
				soundEffect = "ShipMultiLaser";
				break;
			case 2: // NEUTRON PELLET GUN
				damageLow = 2;
				damageHigh = 5;
				enemyShieldMod = .5f;
				cost = 7.5f;
				size = 15;
				power = 25;
				beamColor = new Color(0xa8,0xb4,0x85);
				beamStroke = 1;
				dashStroke = 3;
				soundEffect = "ShipNeutronPelletGun";
				break;
			case 3: // ION CANNON
				damageLow = 3;
				damageHigh = 8;
				heavyAllowed = true;
				heavyDamageLow = 3;
				heavyDamageHigh = 15;
				cost = 10;
				size = 15;
				power = 35;
				beamColor = new Color(0xa4,0x7b,0x56);
				beamStroke = 1;
				soundEffect = "ShipIonCannon";
				break;
			case 4: // MASS DRIVER
				weaponSpread = 1;
				damageLow = 5;
				damageHigh = 8;
				enemyShieldMod = .5f;
				cost = 18;
				size = 55;
				power = 50;
				beamColor = new Color(0xac,0xac,0xac);
				beamStroke = 1;
				dashStroke = 3;
				soundEffect = "ShipMassDriver";
				break;
			case 5: // NEUTRON BLASTER
				damageLow = 3;
				damageHigh = 12;
				heavyAllowed = true;
				heavyDamageLow = 3;
				heavyDamageHigh = 24;
				cost = 15;
				size = 20;
				power = 60;
				beamColor = new Color(0x3c,0x03,0x78);
				beamColor2 = new Color(0xcd,0xb1,0xe8);
				beamStroke = 1;
				soundEffect = "ShipNeutronBlaster";
				break;
			case 6: // GRAVITON BEAM
				damageLow = 1;
				damageHigh = 15;
				streaming = true;
				weaponSpread = 4;
				holdFrames = 6;
				cost = 12;
				size = 30;
				power = 60;
				beamColor = new Color(0x28,0x00,0x7e);
				beamColor2 = new Color(0xf5,0xb7,0xf3);
				beamStroke = 1;
				soundEffect = "ShipGravitonBeam";
				break;
			case 7: // HARD BEAM
				weaponSpread = 1;
				damageLow = 8;
				damageHigh = 12;
				enemyShieldMod = .5f;
				cost = 25;
				size = 50;
				power = 100;
				beamColor = new Color(0xf0,0xb5,0x6e);
				beamColor2 = new Color(0xcb,0x81,0x29);
				beamStroke = 1;
				soundEffect = "ShipHardBeam";
				break;
			case 8: // FUSION BEAM
				damageLow = 4;
				damageHigh = 16;
				heavyAllowed = true;
				heavyDamageLow = 4;
				heavyDamageHigh = 30;
				cost = 13;
				size = 20;
				power = 75;
				beamColor = new Color(0x0c,0x56,0x0c);
				beamColor2 = new Color(0x82,0xc8,0x82);
				beamStroke = 1;
				soundEffect = "ShipFusionBeam";
				break;
			case 9: // MEGABOLT CANNON
				damageLow = 2;
				damageHigh = 20;
				weaponSpread = 4;
				holdFrames = 6;
				computer = 3;
				cost = 16;
				size = 30;
				power = 65;
				beamColor = new Color(0xe5,0xee,0xbe);
				cycleColor = new Color(0xce,0xe2,0x89);
				cycleColor2 = new Color(0xea,0xbb,0xea);
				beamStroke = 1;
				soundEffect = "ShipMegaBoltCannon";
				break;
			case 10: // PHASOR
				damageLow = 5;
				damageHigh = 20;
				heavyAllowed = true;
				heavyDamageLow = 5;
				heavyDamageHigh = 40;
				cost = 18;
				size = 20;
				power = 90;
				beamColor = new Color(0xb6,0x07,0x5a);
				beamColor2 = new Color(0xde,0x8d,0xb3);
				beamStroke = 1;
				soundEffect = "ShipPhasor";
				break;
			case 11: // AUTO-BLASTER
				damageLow = 4;
				damageHigh = 16;
				attacksPerRound = 3;
				cost = 24;
				size = 30;
				power = 90;
				beamColor = new Color(0x24,0xbe,0x93);
				beamColor2 = new Color(0x03,0x25,0x1d);
				beamStroke = 1;
				soundEffect = "ShipAutoBlaster";
				break;
			case 12: // TACHYON BEAM
				damageLow = 1;
				damageHigh = 25;
				weaponSpread = 4;
				holdFrames = 6;
				streaming = true;
				cost = 18;
				size = 30;
				power = 80;
				beamColor = new Color(0x36,0x06,0x00);
				beamColor2 = new Color(0xe1,0xa3,0x8d);
				beamStroke = 1;
				soundEffect = "ShipTachyonBeam";
				break;
			case 13: // GAUSS AUTO-CANNON
				damageLow = 7;
				damageHigh = 10;
				enemyShieldMod = .5f;
				attacksPerRound = 4;
				cost = 40;
				size = 105;
				power = 105;
				beamColor = new Color(0xac,0xac,0xac);
				beamStroke = 3;
				dashStroke = 1;
				soundEffect = "ShipGaussAutoCannon";
				break;
			case 14: // PARTICLE BEAM
				damageLow = 10;
				damageHigh = 20;
				enemyShieldMod = .5f;
				cost = 26;
				size = 90;
				power = 75;
				beamColor = new Color(0x3b,0x39,0x48);
				beamColor2 = new Color(0x95,0x94,0x9d);
				dashStroke = 1;
				beamStroke = 3;
				soundEffect = "ShipParticleBeam";
				break;
			case 15: // PLASMA CANNON
				damageLow = 6;
				damageHigh = 30;
				weaponSpread = 4;
				cost = 24;
				size = 30;
				power = 110;
				beamColor = new Color(0xfe,0x29,0x28);
				beamColor2 = new Color(0xce,0x1f,0x1e);
				beamStroke = 1;
				soundEffect = "ShipPlasmaCannon";
				break;
			case 16: // DEATH RAY
				range = 1;
				damageLow = 200;
				damageHigh = 1000;
				weaponSpread = 7;
				holdFrames = 6;
				restricted = true;
				cost = 120;
				size = 2000;
				power = 2000;
				beamColor = new Color(0x56,0x02,0xc2);
				beamColor2 = new Color(0xcb,0x33,0x5e);
				beamStroke = 1;
				soundEffect = "ShipDeathRay";
				break;
			case 17: // DISRUPTOR
				damageLow = 10;
				damageHigh = 40;
				range = 2;
				cost = 100;
				size = 70;
				power = 160;
				beamColor = new Color(0xa4,0x7b,0x56);
				beamColor2 = new Color(0x82,0xc8,0x82);
				beamStroke = 1;
				soundEffect = "ShipDisruptor";
				break;
			case 18: // PULSE PHASOR
				damageLow = 5;
				damageHigh = 20;
				attacksPerRound = 3;
				holdFrames = 4;
				cost = 42;
				size = 40;
				power = 120;
				beamColor = new Color(0xb6,0x07,0x5a);
				cycleColor = new Color(0xde,0x8d,0xb3);
				beamStroke = 1;
				soundEffect = "ShipPulsePhasor";
				break;
			case 19: // TRI-FOCUS PLASMA CANNON
				damageLow = 20;
				damageHigh = 50;
				weaponSpread = 2;
				cost = 55;
				size = 65;
				power = 180;
				beamColor = new Color(0xfe,0x29,0x28);
				beamColor2 = new Color(0xce,0x1f,0x1e);
				beamStroke = 1;
				soundEffect = "ShipTriFocusPlasmaCannon";
				break;
			case 20: // STELLAR CONVERTOR
				damageLow = 10;
				damageHigh = 35;
				attacksPerRound = 4;
				weaponSpread = 4;
				holdFrames = 4;
				range = 3;
				cost = 105;
				size = 200;
				power = 300;
				beamColor = new Color(0xff,0xff,0xb0);
				cycleColor = new Color(0xff,0xff,0xff);
				beamStroke = 1;
				soundEffect = "ShipStellarConvertor";
				break;
			case 21: // MAULER DEVICE
				damageLow = 20;
				damageHigh = 100;
				weaponSpread = 7;
				holdFrames = 4;
				cost = 120;
				size = 150;
				power = 300;
				beamColor = new Color(0x00,0xaf,0x7d);
				beamColor2 = new Color(0x4c,0xd0,0xab);
				beamStroke = 1;
				soundEffect = "ShipMauler";
				break;
			case 22: // AMOEBA STREAM
				damageLow = 250;
				damageHigh = 1000;
				range = 3;
				streaming = true;
				restricted = true;
				beamColor = Color.green;
				beamStroke = 5;
				soundEffect = "ShipAmoebaStream";
				break;
			case 23: // CRYSTAL RAY
				damageLow = 100;
				damageHigh = 300;
				range = 3;
				attacksPerRound = 4;
				restricted = true;
				beamColor = Color.white;
				beamStroke = 4;
				soundEffect = "ShipMultiLaser";
				break;
		}
		System.out.println("##### New Weapon : " + soundEffect);
	}
	private void setPaint(Graphics2D g, int i, int weaponX, int weaponY, int weaponDx, int weaponDy) {
		if(beamColor2 != null) {
			GradientPaint gp = new GradientPaint(weaponX+i*s10, weaponY+i*s10, beamColor,
					weaponX+i*s10+weaponDx, weaponY+i*s10+weaponDy, beamColor2, true);
			g.setPaint(gp);
		} else
			g.setColor(beamColor);
			
		if(cycleColor != null) {
			if(i%2 == 0) {
				g.setColor(cycleColor);
			} else if (cycleColor2 != null && i%3 == 0) {
				g.setColor(cycleColor2);
			} else {
				g.setColor(beamColor);
			}
		}
	}
	private void drawAttack(Graphics2D g, int weaponX, int weaponY, int impactX, int impactY,
			int wpnNum, float dmg, int count, int boxW, int boxH, float force) {

		count =20;
		int wpnCount = count / attacksPerRound;
		int targetTLx = targetCtr[0]-boxW/2; // Top Left points
		int targetTLy = targetCtr[1]-boxH/2;
		int sourceTLx = sourcePos[0]-boxW/2;
		int sourceTLy = sourcePos[1]-boxH/2;
		int shipTLx	  = targetCtr[0]-shipImg.getWidth()/2;
		int shipTLy	  = targetCtr[1]-shipImg.getHeight()/2;
		
		int dist = (int) Math.round(Math.sqrt (
				Math.pow(sourceTLx-targetTLx, 2) +
				Math.pow(sourceTLy-targetTLy, 2)));
		int distFactor	= 8*dist;
		int weaponDx	= (impactX-weaponX)/distFactor;
		int weaponDy	= (impactY-weaponY)/distFactor;

		int w = scaled(3);
		if ((dashStroke > 0) && (weaponStroke == null)) {
			float dash = scaled(w*dashStroke);
			weaponStroke = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{0, dash}, 0);
		}
		if (weaponStroke != null)
			g.setStroke(weaponStroke);
		else {
			int strokeSize = beamStroke*2;
			if (isHeavy)
				strokeSize += 1;
			g.setStroke(new BasicStroke(scaled(strokeSize), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		}
		windUpFrames = windUpFramesNum;
		if (isHeavy)
			holdFrames = heavyHoldFramesNum;
		else
			holdFrames = holdFramesNum;
		int fadingFrames = fadingFramesNum;
		int landUpFrames = windUpFrames;
		long sleepTime	 = 20; // Original = 50;
		int targetZ  = 0;
		int weaponZ  = scaled(200);
		int weaponDz = scaled(50);	
		weaponZ += roll(-weaponDz,weaponDz); // Already scaled
		
		BufferedImage[][][] shieldArray = new BufferedImage[attacksPerRound][][];
		CombatShield cs = new CombatShield(holdFrames, landUpFrames, fadingFrames,
				targetCtr[0], targetCtr[1], targetZ, shieldColor, shieldBorders,
				shieldLevel, shipImg, boxW, boxH);
		
		cs.setSource(sourceTLx, sourceTLy, weaponZ, beamColor, w);
		cs.setWeapons(dmg, force);
		cs.setImpact(targetTLx, targetTLy, targetZ);
		int shieldTLx = cs.shieldTopLeftX();
		int shieldTLy = cs.shieldTopLeftY();

		// Full Beam trajectory generation
		ArrayList<Line2D.Double> lines = new ArrayList<>();
		double insideRatio = 0;
		for(int i = 0; i < attacksPerRound; ++i) {
			int xAdj = scaled(roll(-4,4)*2);
			int yAdj = scaled(roll(-4,4)*2);
			int[] shipImpact	= cs.setImpact(xAdj, yAdj, 0);
			shieldArray[i]		= cs.getShieldArray();
			insideRatio += cs.insideRatio();
			if (weaponSpread > 1) {
				int xMod = (sourceTLy == targetTLy) ? 0 : 1;
				int yMod = (sourceTLx == targetTLx) ? 0 : 1;
				if ((sourceTLx < targetTLx) && (sourceTLy < targetTLy))
					xMod = -1;
				else if ((sourceTLx > targetTLx) && (sourceTLy > targetTLy))
					xMod = -1;
				for (int n = -1 * weaponSpread; n <= weaponSpread; n++) {
					int adj = scaled(n);
					lines.addAll(addMultiLines(sourceSize, wpnCount, weaponX, weaponY,
							shipImpact[0]+(xMod*adj), shipImpact[1]+(yMod*adj)));
				}
			} else {
				lines.addAll(addMultiLines(sourceSize, wpnCount, weaponX, weaponY, shipImpact[0], shipImpact[1]));
			}
		}
		double fraction =  (1-insideRatio) / (windUpFrames*attacksPerRound);
		//
		// Animations start Here
		//
		if(beamColor2 == null && cycleColor == null)
			beamColor2 = multColor(beamColor, 0.75f);
		setPaint(g, 0, weaponX, weaponY, weaponDx, weaponDy);
		// Beams Progression toward target
		SortedMap<Integer, ArrayList<Line2D.Double>> partLines = new TreeMap<>();
		for(int i = 0; i < windUpFrames; ++i) {
			ArrayList<Line2D.Double> pl = new ArrayList<>();
			for(Line2D.Double line : lines) {
				double newX1 = line.getX1() + (line.getX2() - line.getX1()) * i * fraction;
				double newY1 = line.getY1() + (line.getY2() - line.getY1()) * i * fraction;
				double newX2 = line.getX1() + (line.getX2() - line.getX1()) * (i + 1) * fraction;
				double newY2 = line.getY1() + (line.getY2() - line.getY1()) * (i + 1) * fraction;
				pl.add(new Line2D.Double(newX1, newY1, newX2, newY2));
			}
			partLines.put(i, pl);
			paintLines(pl, g);
			paintImmediately(winRec);
			sleep(sleepTime);
		}

		// Beams reach the shield generation
		ArrayList<Line2D.Double> hitLines = new ArrayList<>(); // The ones that go thru the shield
		clearWindow(g);
		setPaint(g, windUpFrames, weaponX, weaponY, weaponDx, weaponDy);
		for(Line2D.Double line : lines) {
			double newX1 = line.getX1() + (line.getX2() - line.getX1()) * windUpFrames * fraction;
			double newY1 = line.getY1() + (line.getY2() - line.getY1()) * windUpFrames * fraction;
			double newX2 = line.getX2();
			double newY2 = line.getY2();
			hitLines.add(new Line2D.Double(newX1, newY1, newX2, newY2));
		}
		for(int k = 0; k < attacksPerRound; k++)
			g.drawImage(shieldArray[k][BELLOW][0], shieldTLx, shieldTLy, null);
		g.drawImage(shipImg, shipTLx, shipTLy, null);
		paintLines(hitLines, g); // hitting lines are in-between
		for(int k = 0; k < attacksPerRound; k++)
			g.drawImage(shieldArray[k][ABOVE][0], shieldTLx, shieldTLy, null);
	
		paintLines(partLines, g); // visible line are above
		paintImmediately(winRec);
		sleep(sleepTime);

		// Show Continuous
		for(int i = 0; i < holdFrames; i++) {
			clearWindow(g);
			setPaint(g, i+1+windUpFrames, weaponX, weaponY, weaponDx, weaponDy);
			int shieldIdx = i+1;
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][BELLOW][shieldIdx], shieldTLx, shieldTLy, null);
			g.drawImage(shipImg, shipTLx, shipTLy, null);
			paintLines(hitLines, g); // hitting lines are in-between
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][ABOVE][shieldIdx], shieldTLx, shieldTLy, null);

			paintLines(partLines, g);
			paintImmediately(winRec);
			sleep(sleepTime);
		}
		// Show end of beam
		for(int i = 0; i < windUpFrames; ++i) {
			clearWindow(g);
			setPaint(g, i+1+windUpFrames+holdFrames, weaponX, weaponY, weaponDx, weaponDy);
			int shieldIdx = i+1+holdFrames;
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][BELLOW][shieldIdx], shieldTLx, shieldTLy, null);
			g.drawImage(shipImg, shipTLx, shipTLy, null);
			paintLines(hitLines, g); // hitting lines are in-between
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][ABOVE][shieldIdx], shieldTLx, shieldTLy, null);

			partLines.get(i).clear();
			paintLines(partLines, g);
			paintImmediately(winRec);
			sleep(sleepTime);
		}
		// Show shield fading
		for(int i = 0; i < fadingFrames; ++i) {
			clearWindow(g);
			int shieldIdx = i+1+holdFrames+windUpFrames;
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][BELLOW][shieldIdx], shieldTLx, shieldTLy, null);
			g.drawImage(shipImg, shipTLx, shipTLy, null);
			for(int k = 0; k < attacksPerRound; k++)
				g.drawImage(shieldArray[k][ABOVE][shieldIdx], shieldTLx, shieldTLy, null);
			paintImmediately(winRec);
			sleep(sleepTime);
		}
	}
//	private void waitForCapsLockPressed() {
//		if (!debugWait)
//			return;
//		System.out.println("##### PRESS CAPS LOCK #####");
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		tk.setLockingKeyState(KeyEvent.VK_CAPS_LOCK, Boolean.FALSE);
//		while(!tk.getLockingKeyState(KeyEvent.VK_CAPS_LOCK))
//			sleep(100);
//		tk.setLockingKeyState(KeyEvent.VK_CAPS_LOCK, Boolean.FALSE);
//	}
	public TestShields() {
		init();
		animationDelay = 50;
		startAnimation();
	}
	@Override public int scaled(int i) {
		int maxX = SCREEN_HEIGHT*8/5;
		int maxY = SCREEN_WIDTH*5/8;
		if (maxY > SCREEN_HEIGHT)
			maxY = maxX*5/8;
		float resizeAmt = (float) maxY/768;
        if (i < 1)
            return (int) Math.ceil(resizeAmt*i);
        else if (i > 1)
            return (int) Math.floor(resizeAmt*i);
        else
            return i;
	}
	private boolean prevFolder() {
		monsterId=monsters.length-1;
		folderId--;
		if (folderId < 0) {
			folderId = folders.length-1;
			return true;
		}
		return false;
	}
	private boolean nextFolder() {
		monsterId=0;
		folderId++;
		if (folderId == folders.length) {
			folderId = 0;
			return true;
		}
		return false;
	}
	private boolean prevSize() {
		if (isMonster())
			return false;
		sizeId--;
		if (sizeId < 0) {
			sizeId = sizes.length-1;
			return true;
		}
		return false;
	}
	private boolean nextSize() {
		if (isMonster())
			return false;
		sizeId++;
		if (sizeId == sizes.length) {
			sizeId = 0;
			return true;
		}
		return false;
	}
	private boolean prevModel() {
		if (isMonster())
			return false;
		modelId--;
		if (modelId < 0) {
			modelId = models.length-1;
			return true;
		}
		return false;
	}
	private boolean nextModel() {
		if (isMonster())
			return false;
		modelId++;
		if (modelId == models.length) {
			modelId = 0;
			return true;
		}
		return false;
	}
	private boolean prevMonster() {
		monsterId--;
		System.out.println("prevMonster(): " + monsterId);
		if (monsterId < 0) {
			monsterId = monsters.length-1;
			return true;
		}
		return false;
	}
	private boolean nextMonster() {
		monsterId++;
		System.out.println("prevMonster(): " + monsterId);
		if (monsterId == monsters.length) {
			monsterId = 0;
			return true;
		}
		return false;
	}
	private void prevShip() {
		if (isMonster()) {
			if (prevMonster())
				prevFolder();
			return;
		}
		if (prevModel())
			if (prevSize())
				prevFolder();
	}
	private void nextShip() {
		if (isMonster()) {
			if (nextMonster())
				nextFolder();
			return;
		}
		if (nextModel())
			if (nextSize())
				nextFolder();
	}
	private void prevWeapon() {
		weaponId--;
		if (weaponId < 0)
			weaponId = weaponMaxId;
	}
	private void nextWeapon() {
		weaponId++;
		if (weaponId > weaponMaxId)
			weaponId = 0;
	}
	private void drawBoxes(Graphics2D g, int x, int y) {
		g.drawRect(x, y, boxWidth, boxHeight);
		g.drawLine(x, y+boxHeight/2, x+boxWidth, y+boxHeight/2);
		g.drawLine(x+boxWidth/2, y, x+boxWidth/2, y+boxHeight);
	}
	private void drawSourceAndTarget(Graphics2D g) {
		g.setPaint(Color.white);
		int x = sourcePos[0]-sourceDiameter/2;
		int y = sourcePos[1]-sourceDiameter/2-sourceDelta;
		g.fillOval(x, y, sourceDiameter, sourceDiameter);
		y += sourceDelta;
		g.fillOval(x, y, sourceDiameter, sourceDiameter);
		y += sourceDelta;
		g.fillOval(x, y, sourceDiameter, sourceDiameter);
		g.drawLine(winTarX+ctrDX, 0, winTarX+ctrDX, windowHeight);
		g.drawLine(0, winTarY+ctrDY, windowWidth, winTarY+ctrDY);
	}
	private void clearWindow(Graphics2D g) {
		g.setPaint(spaceBlue);
//		g.setPaint(Color.white);
		g.fillRect (0, 0, windowWidth, windowHeight);
	}
	private void paintBeamView(Graphics2D g) {
		holdTimer = true;
		clearWindow(g);
		int dx = (boxWidth-shipWidth)/2;
		int dy = (boxHeight-shipHeight)/2;
		int x = IMAGE_SEP + imageWidth;
		int y = IMAGE_SEP + imageHeight;
		g.drawImage(shipImg, x+dx, y+dy, this);

		sourcePos = newSourcePos(srcLocX[srcLocation], srcLocY[srcLocation], scaled(200));
		srcLocation++;
		if (srcLocation >= srcLocX.length)
			srcLocation = 0;

		int srcX	= sourcePos[0];
		int srcY	= sourcePos[1];
		int impactX	= targetCtr[0];
		int impactY	= targetCtr[1];
		int wpnNum	= weaponId;
		float dmg	= damage;
		int count	= wpnCount;
		float force	= beamForce;
		
		drawAttack(g, srcX, srcY, impactX, impactY,	wpnNum, dmg, count, boxWidth, boxHeight, force);
		holdTimer = false;
	}
	private void paintQuadView(Graphics2D g) {
		clearWindow(g);
		g.setComposite(AlphaComposite.SrcOver);
		g.setPaint(Color.blue);

		int x = IMAGE_SEP;
		int y = IMAGE_SEP;
		int dx = (boxWidth-shipWidth)/2;
		int dy = (boxHeight-shipHeight)/2;
		
		BufferedImage aboveImg  = shieldArray[currentAttack][ABOVE][currentImage];
		BufferedImage bellowImg = shieldArray[currentAttack][BELLOW][currentImage];

		g.drawImage(bellowImg, x+dx+shieldDX, y+dy+shieldDY, this);
		drawBoxes(g, x, y);
		
		x = IMAGE_SEP + imageWidth;
		g.drawImage(aboveImg, x+dx+shieldDX, y+dy+shieldDY, this);
		drawBoxes(g, x, y);

		y = IMAGE_SEP + imageHeight;
		g.drawImage(bellowImg, x+dx+shieldDX, y+dy+shieldDY, this);
		g.drawImage(shipImg, x+dx, y+dy, this);
		g.drawImage(aboveImg, x+dx+shieldDX, y+dy+shieldDY, this);
		drawBoxes(g, x, y);
		
		x = IMAGE_SEP;
		g.drawImage(bellowImg, x+dx+shieldDX, y+dy+shieldDY, this);
		g.drawImage(shipImg, x+dx, y+dy, this);
		drawBoxes(g, x, y);

		drawSourceAndTarget(g);
		currentImage = next(currentImage, 1);		
	}
	@Override public void paintComponent(Graphics g0) {
		if (holdTimer)
			return;
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
//		System.out.println("beamView = " + beamView);
		if (beamView) {
			paintBeamView(g);
		}
		else
			paintQuadView(g);
	}
	@Override public void actionPerformed(ActionEvent e) {
		if (holdTimer)
			return;		
		repaint();
	}
	private int next(int val, int incr) { return (val + incr) % totalImages; }
	private BufferedImage loadImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return img;
	}
	public void startAnimation() {
		if (animationTimer == null) {
			currentImage = 0;
			animationTimer = new Timer(animationDelay, this);
			animationTimer.start();
		}
		else if (!animationTimer.isRunning())
			animationTimer.restart();
	}
	public void stopAnimation() { animationTimer.stop(); }
	public static void main(String args[]) {
//		anim = new TestShields();
		TestShields anim = new TestShields();
		JFrame app = new JFrame("Animator test");
//		app = new JFrame("Animator test");
		app.add(anim, BorderLayout.CENTER);
		app.setSize(anim.windowWidth, anim.windowHeight);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//app.setSize(anim.getPreferredSize().width + 10, anim.getPreferredSize().height + 30);
		app.setLocation(600, 350);
		app.addKeyListener(new KeyListener(){
			@Override public void keyPressed(KeyEvent e) {}
			@Override  public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_B:
						anim.beamView = !anim.beamView;
						anim.holdTimer = false;
						//anim.loadShields();
						return;
					case KeyEvent.VK_M:
						if (e.isShiftDown())
							anim.prevModel();
						else
							anim.nextModel();
						anim.loadShields();
						return;
					case KeyEvent.VK_N:
					case KeyEvent.VK_RIGHT:
						anim.nextShip();
						anim.loadShields();
						return;
					case KeyEvent.VK_LEFT:
						anim.prevShip();
						anim.loadShields();
						return;
					case KeyEvent.VK_DOWN:
						anim.loadShields();
						return;
					case KeyEvent.VK_F:
					case KeyEvent.VK_R:
						if (e.isShiftDown())
							anim.prevFolder();
						else
							anim.nextFolder();
						anim.loadShields();
						return;
					case KeyEvent.VK_S:
						if (e.isShiftDown())
							anim.prevSize();
						else
							anim.nextSize();
						anim.loadShields();
						return;
					case KeyEvent.VK_W:
						if (e.isShiftDown())
							anim.prevWeapon();
						else
							anim.nextWeapon();
						anim.loadShields();
						return;
				}
			}
		});
		app.setVisible(true);
	}
}
