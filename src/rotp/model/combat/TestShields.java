package rotp.model.combat;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static rotp.model.combat.CombatShield.ABOVE;
import static rotp.model.combat.CombatShield.BELLOW;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import rotp.Rotp;


public class TestShields extends JPanel implements ActionListener {
	
	private final float SCREEN_RATIO	= (float)9/16; 
	private final int SCREEN_WIDTH	= 3840; 
//	private final int SCREEN_WIDTH	= 1024; 
	private final int SCREEN_HEIGHT	= (int) (SCREEN_WIDTH * SCREEN_RATIO + 0.5f);
	private final int COLUMNS_NUM	= 10; 
	private final int ROWS_NUM		= 8; 
	private final int IMAGE_SEP		= 50;
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

	private BufferedImage[][] shieldArray;
	private BufferedImage shipImg;
	private int totalImages, currentImage, animationDelay;
	private Timer animationTimer;
	private CombatShield cs;
	private int[] shieldPos = new int[] {0, 0, 0}; 
	private int[] targetPos = new int[] {0, 0, 0}; 
	private int[] sourcePos = new int[] {1000, 500, 1000}; 
	private int[] semiAxis;
	private Color shieldColor = Color.green;
	private Color beamColor   = Color.red;
	private int beamSize	  = 3;
	private int windUpFramesNum = 6;
	private int holdFramesNum   = 0;
	private int landUpFramesNum = windUpFramesNum;
	private int fadingFramesNum = windUpFramesNum+2;
	private int attacksPerRound = 1;
	private int shieldLevel = 1;
	private float beamForce = 10f;
	private float damage	= 5f;
	private int imageWidth, imageHeight;
	private int shipDX, shipDY;
	private int shipWidth, shipHeight;
	private int boxWidth, boxHeight;
	private int windowWidth,windowHeight;
	private float shipScale;
	
	private void initSizes() {
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
		
		windowWidth  = IMAGE_SEP + 2*imageWidth  + 10;
		windowHeight = IMAGE_SEP + 2*imageHeight + 30;
	}
	private void init() {
		 folderId	= 0;
		 sizeId		= 0;
		 modelId	= 0;
		 monsterId	= 0;
		 loadShields();
	}
	private boolean isMonster() { return folderId == folders.length-1; }
	private void loadShields() {
		long timeStart = System.currentTimeMillis();
		long timeMid = timeStart;
		System.out.println();
		if (isMonster()) {
			imagePath = monsterPath;
			imageName = monsters[monsterId] + ".png";			
		} else {
			imagePath = shipPath + folders[folderId] + "\\";
			imageName = sizes[sizeId] + models[modelId] + ".png";
		}
//		imageName = "A04a2.png";
		initSizes();
		cs = new CombatShield(windUpFramesNum, holdFramesNum, landUpFramesNum, fadingFramesNum,
				shieldPos[0], shieldPos[1], shieldPos[2], shieldColor, scaled(1),
				shieldLevel, shipImg, boxWidth, boxHeight);
		cs.setTarget(targetPos[0], targetPos[1], targetPos[2]);
		cs.setSource(sourcePos[0], sourcePos[1], sourcePos[2], beamColor, scaled(beamSize));
		cs.setWeapons(attacksPerRound, damage, beamForce);

		shieldArray = cs.getShieldArray();
		shipDX		= cs.shieldOffsetX();
		shipDY		= cs.shieldOffsetY();
		totalImages = shieldArray[ABOVE].length;
//		System.out.println("windowWidth: " + windowWidth + "  windowHeight: " + windowHeight);
//		System.out.println("boxWidth: " + boxWidth + "  boxHeight: " + boxHeight);
//		System.out.println("imageWidth: " + imageWidth + "  imageHeight: " + imageHeight);
//		System.out.println("shipWidth: " + shipImg.getWidth() + "  shipHeight: " + shipImg.getHeight());
//		System.out.println("shipDX: " + shipDX + "  shipDY: " + shipDY);
		
		currentImage = 0;
		System.out.println("loadShields: " + folders[folderId] + "\\" + imageName +
				"  Time = " + (System.currentTimeMillis()-timeMid));
//		System.out.println("loadShields() Time = " + (System.currentTimeMillis()-timeMid));
	}
	public TestShields() {
		init();
		animationDelay = 50;
		startAnimation();
	}
	private int scaled(int i) {
		int maxX = SCREEN_HEIGHT*8/5;
		int maxY = SCREEN_WIDTH*5/8;
		if (maxY > SCREEN_HEIGHT)
			maxY = maxX*5/8;
		float resizeAmt = (float) maxY/768;
		return (int) (i * resizeAmt);
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
	private boolean prevMonster() {
		monsterId--;
		System.out.println("prevMonster(): " + monsterId);
		if (monsterId < 0) {
			monsterId = monsters.length-1;
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
	private boolean nextFolder() {
		monsterId=0;
		folderId++;
		if (folderId == folders.length) {
			folderId = 0;
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
	private boolean nextMonster() {
		monsterId++;
		System.out.println("prevMonster(): " + monsterId);
		if (monsterId == monsters.length) {
			monsterId = 0;
			return true;
		}
		return false;
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
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		g.setPaint(spaceBlue);
//		g.setPaint(Color.white);
		g.fillRect (0, 0, windowWidth, windowHeight);

		g.setComposite(AlphaComposite.SrcOver);
		
		int x = IMAGE_SEP;
		int y = IMAGE_SEP;
		BufferedImage aboveImg  = shieldArray[ABOVE][currentImage];
		BufferedImage bellowImg = shieldArray[BELLOW][currentImage];

		g.drawImage(bellowImg, x, y, this);
		
		x = IMAGE_SEP + imageWidth;
		g.drawImage(aboveImg, x, y, this);

		y = IMAGE_SEP + imageHeight;
		g.drawImage(bellowImg, x, y, this);
		g.drawImage(shipImg, x+shipDX, y+shipDY, this);
		g.drawImage(aboveImg, x, y, this);

		x = IMAGE_SEP;
		g.drawImage(bellowImg, x, y, this);
		g.drawImage(shipImg, x+shipDX, y+shipDY, this);

		currentImage = next(currentImage, 1);
	}
	@Override public void actionPerformed(ActionEvent e) { repaint(); }
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
		TestShields anim = new TestShields();
		JFrame app = new JFrame("Animator test");
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
				}
			}
		});
		app.setVisible(true);
	}
}
