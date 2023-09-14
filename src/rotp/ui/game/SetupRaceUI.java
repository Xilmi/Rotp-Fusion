/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.game;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints; // modnar: needed for adding RenderingHints
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import rotp.model.empires.Race;
import rotp.model.game.IRaceOptions;
import rotp.model.ships.ShipImage;
import rotp.model.ships.ShipLibrary;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.game.HelpUI.HelpSpec;
import rotp.ui.main.SystemPanel;
import rotp.util.FontManager;
import rotp.util.ModifierKeysState;

public final class SetupRaceUI extends BaseModPanel implements MouseWheelListener, IRaceOptions {
    private static final long serialVersionUID	= 1L;
	private static final String guiTitleID		= "SETUP_SELECT_RACE";
	private static final String GUI_ID          = "START_RACE";
	private static final String cancelKey		= "SETUP_BUTTON_CANCEL";
	private static final String customRaceKey	= "SETUP_BUTTON_CUSTOM_PLAYER_RACE";
    private static final int    MAX_RACES  		= 16; // modnar: increase MAX_RACES to add new Races
    private static final int    MAX_COLORS 		= 16; // modnar: add new colors
    private static final int    MAX_SHIP   		= ShipLibrary.designsPerSize; // BR:
	private static final int    settingFont		= 20;
	private	static final Font   labelFont		= FontManager.current().narrowFont(settingFont);
	
    private final Color checkBoxC  = new Color(178,124,87);
    private final Color darkBrownC = new Color(112,85,68);

    private int w10Or = scaled(200);
    private int w6Ext = scaled(80);
    private int w6Ext() { return isOS()? 0 : w6Ext; }
    
    // Left Frame
    private int xLeftFrame() { return scaled(220) - w6Ext; }
    private int yLeftFrame = scaled(110);
    private int wLeftFrame() { return w10Or + w6Ext(); }
    private int hLeftFrame = scaled(485);

    // Center Frame
    private int xCtrFrame() { return xLeftFrame() + wLeftFrame(); };
    private int yCtrFrame = scaled(103);
    private int wCtrFrame = scaled(395);
    private int hCtrFrame = scaled(499);

    // Right Frame
    private int xRightFrame() { return xCtrFrame() + wCtrFrame; }
    private int yRightFrame = yLeftFrame;
    private int wRightFrame = scaled(335);
    private int hRightFrame = hLeftFrame;

    // Shading BG
    private int wShEdge = s15;
    private int xShading() { return xLeftFrame() - wShEdge; } 
    private int yShading = yLeftFrame - wShEdge;
    private int wShading() { return xRightFrame() + wRightFrame + wShEdge - xShading(); }
    private int hShading = hLeftFrame + 2 * wShEdge;

    // Whole page Parameters
    private BufferedImage backImg; // the full background
    
    // Colors
    private int xColors() { return xRightFrame() + wShEdge; };
    private int yColors = yRightFrame + hRightFrame - scaled(40);
    private int wColors = s21;  // modnar: add new colors, change color box sizes
    private int hColors = s15;
    
    // Races Parameters
    private int iconSize = s95;
    private int yIcon    = yRightFrame + s10;
    private int xIcon() { return xRightFrame() + s6 + iconSize/2; }
    private Box	playerRaceSettingBox = new Box("SETUP_RACE_CUSTOM"); // BR: Player Race Customization
    private Box	checkBox			 = new Box("SETUP_RACE_CHECK_BOX"); // BR: Player Race Customization
    private Box[] raceBox			 = new Box[MAX_RACES];
    private BufferedImage raceImg;
    private static BufferedImage raceIconImg; // For the little icon
    private static BufferedImage raceBackImg; // For race Mug
    private static BufferedImage[] racemugs = new BufferedImage[MAX_RACES];

    // Other Parameters
    private final int FIELD_W		= scaled(160);
    private final int FIELD_H		= s24;
    private JTextField leaderName	= new JTextField("");
    private Box	leaderBox			= new Box("SETUP_RACE_LEADER");
    private JTextField homeWorld 	= new JTextField("");
    private Box	homeWorldBox		= new Box("SETUP_RACE_HOMEWORLD");
    private Box	noFogBox			= new Box(noFogOnIcons);
    private Box[] colorBox			= new Box[MAX_COLORS];

    // Fleet Parameters
    private final int shipNum     = 6;
    private final int shipSize    = 2; // Large
    private final int shipDist    = s80;
    private final int shipHeight  = s65;
    private final int shipWidth   = (int) (shipHeight * 1.3314f);
    private final int fleetWidth  = shipWidth;
    private final int fleetHeight = shipHeight + shipDist * (shipNum-1);
    private int xFleet() { return xRightFrame() + scaled(235); }
    private final int yFleet      = yRightFrame + s10;
    private Box		  shipSetBox  = new Box("SETUP_RACE_SHIPSET"); // BR: ShipSet Selection
    private Box[]	  shipBox	  = new Box[MAX_SHIP]; // BR: ShipSet Selection
    private JTextField shipSetTxt = new JTextField(""); // BR: ShipSet Selection
    private int shipStyle		  = 0; // The index that define the shape
    private BufferedImage shipBackImg, fleetBackImg;

    // Buttons Parameters
    private int xButton, yButton, wButton, hButton;
    private int bSep = s15;
	private BufferedImage[] fleetImages = new BufferedImage[MAX_SHIP];
    private Race dataRace;
    private Box	helpBox		= new Box("SETTINGS_BUTTON_HELP");
    private Box	cancelBox	= new Box("SETUP_RACE_CANCEL");
    private Box	nextBox		= new Box("SETUP_RACE_NEXT");
    private BufferedImage buttonBackImg;

    // Debug Parameter
    private static boolean showTiming = false;
 
    private boolean isOS() { return newGameOptions().originalSpeciesOnly(); }
    public SetupRaceUI() {
        init0();
    }
    private void init0() {
		isSubMenu = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        for (int i=0;i<raceBox.length;i++)
            raceBox[i] = new Box("SETUP_RACE_RACES");
        for (int i=0;i<colorBox.length;i++)
            colorBox[i] = new Box();
        for (int i=0; i<shipBox.length; i++)
        	shipBox[i] = new Box("SETUP_RACE_SHIPSET");
        initTextField(homeWorld);
        initTextField(leaderName);
        initTextField(shipSetTxt); // BR:
    }
    public void resetRaceMug() {
    	for (int i=0;i<racemugs.length;i++)
    		racemugs[i] = null;
    }
    private void resetFleetImages() {
    	for (int i=0; i<fleetImages.length; i++)
    		fleetImages[i] = null;
    }
    private void initShipBoxBounds() {
    	int xFleet = xFleet(); 
        for (int i=0; i<shipBox.length; i++)
        	shipBox[i].setBounds(xFleet, yFleet + i * shipDist, shipWidth, shipHeight);
    }
	@Override protected void singleInit() {
		paramList = optionsRace;
	}
    @Override public void init() {
    	super.init();
    	EditCustomRaceUI.updatePlayerCustomRace();
        leaderName.setFont(labelFont);
        setHomeWorldFont(); // BR: MonoSpaced font for Galaxy
        shipSetTxt.setFont(labelFont); // BR:
        initShipBoxBounds();
        refreshGui();
        // Save initial options
        newGameOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
    }
	@Override public void showHelp() {
		loadHelpUI();
		repaint();   
	}
	@Override public void showHotKeys() {
		loadHotKeysUI();
		repaint();   
	}
	@Override protected void loadHotKeysUI() {
    	HelpUI helpUI = RotPUI.helpUI();
        helpUI.clear();
        int xHK = scaled(100);
        int yHK = scaled(70);
        int wHK = scaled(360);
        helpUI.addBrownHelpText(xHK, yHK, wHK, 10, text("SETUP_RACE_HELP_HK"));
        helpUI.open(this);
	}
	private void loadHelpUI() {
		int xBox, yBox, wBox;
		int xb, xe, yb, ye;
		int nL, hBox, lH;
		String txt;
		HelpSpec sp;
		Box dest;
		HelpUI helpUI = RotPUI.helpUI();
		helpUI.clear();

		txt  = text("SETUP_RACE_MAIN_DESC");
		nL   = 6;
		xBox = s50;
		wBox = scaled(350);
		yBox = s20;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		lH   = HelpUI.lineH();
		
		int yShift = s40;
		int xShift = s40;
		dest = nextBox;
		txt  = dest.getDescription();
        nL   = 2;
        wBox = dest.width - s20;
        hBox = nL*lH;
        xBox = dest.x+s10;
        yBox = dest.y-hBox-yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox/2;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width/2;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        
		dest = cancelBox;
		txt  = dest.getDescription();
        nL   = 2;
        hBox = nL*lH;
        xBox = dest.x+s10;
        yBox = dest.y-hBox-yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox/2;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width/2;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        
		dest = defaultBox;
		txt  = dest.getDescription();
        nL   = 3;
        hBox = nL*lH;
        xBox -= 2*xShift;
        yBox -= hBox+yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+xShift;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width - xShift;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        
		dest = lastBox;
		txt  = dest.getDescription();
        nL   = 3;
        hBox = nL*lH;
        xBox -= wBox;
        yBox = dest.y-hBox-yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox/4;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width/2;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        
		dest = userBox;
		txt  = dest.getDescription();
        nL   = 2;
        hBox = nL*lH;
        xBox -= xShift;
        yBox -= hBox+yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+xShift/2;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width*3/4;;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);

		dest = checkBox;
		txt  = dest.getDescription();
        nL   = 2;
        hBox = nL*lH;
        xBox -= xShift;
        yBox -= hBox+yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, scaled(170), nL, txt);
        xb   = xBox+xShift/2;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width/2;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);

		dest = guideBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		xBox = dest.x;
		yBox = playerRaceSettingBox.y - hBox - yShift/2;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*1/4;
		yb   = yBox + sp.height();
		xe   = dest.x + dest.width*1/2;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);
		
		dest = playerRaceSettingBox;
		txt  = dest.getDescription();
        nL   = 3;
        hBox = nL*lH;
//        yBox = dest.y-hBox-hShift;
		xBox += xShift*3;
        yBox -= hBox+yShift;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox*3/4;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width*3/4;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        
		dest = shipSetBox;
		txt  = dest.getDescription();
        nL   = 3;
        wBox = scaled(225);
        hBox = nL*lH;
        xBox = dest.x - s15;
        yBox = dest.y - hBox - s60;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox/2;
        yb   = yBox+sp.height();
        xe   = dest.x + dest.width/2;
        ye   = dest.y;
        sp.setLine(xb, yb, xe, ye);
        int margin = s3;
		dest = shipBox[0];
		int lx = dest.x - margin;
		int ty = dest.y - margin;
		int rx = lx + dest.width + margin;
		int by = shipBox[MAX_SHIP-1].y + dest.height + margin;
        
        sp.setLineArr(xb+s30, yBox,
        		lx, ty + scaled(120),
        		lx, ty,
        		rx, ty,
        		rx, by,
        		lx, by,
        		lx, ty + s100
           	    );

		txt  = text("SETUP_RACE_RACES_DESC");
		Rectangle dst = new Rectangle(scaled(425), scaled(108), scaled(385), scaled(489));
        nL   = 4;
        wBox = scaled(300);
        hBox = nL*lH;
        xBox = dst.x - wBox - s25;
        yBox = dst.y + dst.height/4;
        sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
        xb   = xBox+wBox;
        yb   = yBox+sp.height()/2;
        xe   = dst.x + s20;
        ye   = dst.y + dest.height/2;
        sp.setLine(xb, yb, xe, ye);

        helpUI.open(this);
    }
    private void setHomeWorldFont() { // BR: MonoSpaced font for Galaxy
   		homeWorld.setFont(narrowFont(20));
    }
    private void doCancelBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: 
		default: // Save
			newGameOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			break; 
		}
    	goToMainMenu();
 	}
    private void doNextBoxAction() { // save and continue
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
		default: // Save
			newGameOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			break; 
		}
 		goToGalaxySetup();
 	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override protected void refreshGui() {
		raceChanged();
		repaint();
	}
	private static String cancelButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			// return restoreKey;
		default:
			return cancelKey;
		}
	}
	@Override public void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		initButtonBackImg();
        g.drawImage(buttonBackImg(), xButton, yButton, wButton, hButton, null);
		g.dispose();
	}
    private void drawButton(Graphics2D g, boolean all, Box box, String text) {
        if (hoverBox == box || all) {
        	String s = text(text);
        	int cnr  = s5;
	        int sw	 = g.getFontMetrics().stringWidth(s);
	        int x    = box.x + ((box.width-sw)/2);
	        int y    = box.y + box.height-s8;
	        if (all) {
	        	Color c1 = GameUI.borderBrightColor();
		        drawShadowedString(g, s, 2, x-xButton, y-yButton, GameUI.borderDarkColor(), c1);
		        g.setStroke(stroke1);
		        g.drawRoundRect(box.x-xButton, box.y-yButton, box.width, box.height, cnr, cnr);	        	
	        } else {
	        	Color c1 = Color.yellow;
		        drawShadowedString(g, s, 2, x, y, GameUI.borderDarkColor(), c1);
		        g.setStroke(stroke1);
		        g.drawRoundRect(box.x, box.y, box.width, box.height, cnr, cnr);
	        }
        }
    }
	private void drawButtons(Graphics2D g, boolean all) {
        Stroke prev = g.getStroke();
        g.setFont(narrowFont(20));

        drawButton(g, all, defaultBox, defaultButtonKey());
        drawButton(g, all, lastBox,    lastButtonKey());
        drawButton(g, all, userBox,    userButtonKey());
        drawButton(g, all, guideBox,   guideButtonKey());
        g.setStroke(prev);
	}
	private void drawFixButtons(Graphics2D g, boolean all) {
        int cnr = s5;
        Stroke prev = g.getStroke();

        // Help button
        if (hoverBox == helpBox || all) {
	        g.setFont(narrowFont(25));
	        if (helpBox == hoverBox)
	            g.setColor(Color.yellow);
	        else
	            g.setColor(Color.white);
	        drawString(g,"?", s26, s40);
	        g.setStroke(stroke1);
	        g.drawOval(helpBox.x, helpBox.y, helpBox.width, helpBox.height);
    	}
	
        g.setFont(narrowFont(30));
		// left button
        if (hoverBox == cancelBox || all) {
	        String text1 = text(cancelButtonKey());
	        int sw1  = g.getFontMetrics().stringWidth(text1);
	        int x1   = cancelBox.x+((cancelBox.width-sw1)/2);
	        int y1   = cancelBox.y+cancelBox.height-s12;
	        Color c1 = hoverBox == cancelBox ? Color.yellow : GameUI.borderBrightColor();
	        drawShadowedString(g, text1, 2, x1, y1, GameUI.borderDarkColor(), c1);
	        g.setStroke(stroke1);
	        g.drawRoundRect(cancelBox.x, cancelBox.y, cancelBox.width, cancelBox.height, cnr, cnr);
        }
        
        // right button
        if (hoverBox == nextBox || all) {
	        String text2 = text("SETUP_BUTTON_NEXT");
	        int sw2  = g.getFontMetrics().stringWidth(text2);
	        int x2   = nextBox.x+((nextBox.width-sw2)/2);
	        int y2   = nextBox.y+nextBox.height-s12;
	        Color c2 = hoverBox == nextBox ? Color.yellow : GameUI.borderBrightColor();
	        drawShadowedString(g, text2, 2, x2, y2, GameUI.borderDarkColor(), c2);
	        g.setStroke(stroke1);
	        g.drawRoundRect(nextBox.x, nextBox.y, nextBox.width, nextBox.height, cnr, cnr);
    	}
	
        // BR: No Fog on Icons
        if (hoverBox == noFogBox || all) {
	        int noFogW = s16;
	        int noFogX = playerRaceSettingBox.x + s5 ;
	        int noFogY = playerRaceSettingBox.y - s15;
	        noFogBox.setBounds(noFogX, noFogY-noFogW, noFogW, noFogW);
	        g.setStroke(stroke3);
			g.setColor(GameUI.setupFrame());
	        g.fill(noFogBox);
	        if (hoverBox == noFogBox) {
	            g.setColor(Color.yellow);
	            g.draw(noFogBox);
	        }
	        if (noFogOnIcons()) {
	            g.setColor(SystemPanel.blackText);
	            g.drawLine(noFogX-s1, noFogY-s8, noFogX+s4, noFogY-s4);
	            g.drawLine(noFogX+s4, noFogY-s4, noFogX+noFogW, noFogY-s16);
	        }
		}
	        
        // BR: Player Race Customization
        // far left button
        if (hoverBox == playerRaceSettingBox || all) {
	        g.setFont(narrowFont(20));
	        String text4 = text(customRaceKey);
	        int sw4  = g.getFontMetrics().stringWidth(text4);
	        int x4   = playerRaceSettingBox.x + ((playerRaceSettingBox.width-sw4)/2);
	        int y4   = playerRaceSettingBox.y + playerRaceSettingBox.height-s8;
	        Color c4 = hoverBox == playerRaceSettingBox ? Color.yellow : GameUI.borderBrightColor();
	        drawShadowedString(g, text4, 2, x4, y4, GameUI.borderDarkColor(), c4);
	        g.setStroke(stroke1);
	        g.drawRoundRect(playerRaceSettingBox.x, playerRaceSettingBox.y, playerRaceSettingBox.width, playerRaceSettingBox.height, cnr, cnr);
        }
	        
        // BR: Race customization check box
        if (hoverBox == checkBox || all) {
	        int checkW = s16;
	        int checkX = playerRaceSettingBox.x + playerRaceSettingBox.width + s10;    
	        int checkY = playerRaceSettingBox.y + playerRaceSettingBox.height - s7;
	        checkBox.setBounds(checkX, checkY-checkW, checkW, checkW);
	        g.setStroke(stroke3);
	        g.setColor(checkBoxC);
	        g.fill(checkBox);
	        if (hoverBox == checkBox) {
	            g.setColor(Color.yellow);
	            g.draw(checkBox);
	        }
	        if (playerIsCustom.get()) {
	            g.setColor(SystemPanel.whiteText);
	            g.drawLine(checkX-s1, checkY-s8, checkX+s4, checkY-s4);
	            g.drawLine(checkX+s4, checkY-s4, checkX+checkW, checkY-s16);
	        }
        }
        g.setStroke(prev);
	}
	@Override public void paintComponent(Graphics g0) {
		// System.out.println("===== PaintComponents =====");
		// showTiming = false;
		long timeStart = System.currentTimeMillis();
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
 
        int x = colorBox[0].x;
        int y = colorBox[0].y;

        shipSetTxt.setCaretPosition(shipSetTxt.getText().length());
        shipSetTxt.getCaret().setVisible(false);
        shipSetTxt.setFocusable(false);
        shipSetTxt.setLocation(x, y-s100-s44);
        shipSetBox.setBounds(x-s1, y-s100-s44, FIELD_W+s2, FIELD_H+s2);
        leaderName.setCaretPosition(leaderName.getText().length());
        leaderName.setLocation(x, y-s50); // BR: squeezed
        leaderBox.setBounds(x-s1, y-s50, FIELD_W+s2, FIELD_H+s2); // BR: squeezed
        homeWorld.setCaretPosition(homeWorld.getText().length());
        homeWorld.setLocation(x, y-s97); // BR: squeezed
		// modnar: test hover text
        // homeWorld.setToolTipText("<html> Homeworld Name is used as <br> the Galaxy Map when selecting <br> Map Shape [Text]. <br><br> (Unicode characters allowed)");
        homeWorldBox.setBounds(x-s1, y-s97, FIELD_W+s2, FIELD_H+s2); // BR: squeezed

		// modnar: use (slightly) better upsampling
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // background image
        g.drawImage(backImg(), 0, 0, this);

		// Buttons background image
        g.drawImage(buttonBackImg(), xButton, yButton, null);

        // selected race center img
		g.drawImage(raceImg(), xCtrFrame(), yCtrFrame, null);

        // draw Ship frames on the right
        g.drawImage(fleetBackImg(), xFleet(), yFleet, null);

        // selected race box
        List<String> races = newGameOptions().startingRaceOptions();
        String selRace = newGameOptions().selectedPlayerRace();
        for (int i=0;i<races.size();i++) {
            if (races.get(i).equals(selRace)) {
                Rectangle box = raceBox[i];
                drawRaceBox(g, i, box.x, box.y, null);
                Stroke prev = g.getStroke();
                g.setStroke(stroke2);
                g.setColor(GameUI.setupFrame());
                g.draw(raceBox[i]);
                g.setStroke(prev);
                break;
            }
        }

        // hovering race box outline
        for (int i=0;i<raceBox.length;i++) {
            if (raceBox[i] == hoverBox) {
                Stroke prev = g.getStroke();
                g.setStroke(stroke2);
                g.setColor(Color.yellow);
                g.draw(raceBox[i]);
                g.setStroke(prev);
                break;
            }
        }


        // race icon
        //BufferedImage icon = newBufferedImage(race.flagNorm());
        g.drawImage(raceIcon(), xIcon(), yIcon, null);

        // draw race name
        Race race = Race.keyed(newGameOptions().selectedPlayerRace());
        int x0 = colorBox[0].x;
        int y0 = scaled(240); // BR: squeezed
        // BR: show custom race name and descriptions
        String raceName, desc1, desc2, desc3, desc4;
        if (playerIsCustom.get()) {
        	raceName = dataRace.setupName;
        	desc1 = dataRace.description1;
        	desc2 = dataRace.description2;
        	desc3 = dataRace.description3.replace("[race]", raceName);
        	desc4 = dataRace.description4;
        }
        else {
        	raceName = race.setupName();
        	desc1 = race.description1;
        	desc2 = race.description2;
        	desc3 = race.description3.replace("[race]", raceName);
        	desc4 = race.description4;
        }
        // \BR:
        int fs = scaledFontSize(g, raceName, scaled(200), 30, 10);
        g.setFont(font(fs));
        drawBorderedString(g0, raceName, 1, x0, y0, Color.black, Color.white);

        // draw race desc #1
        int maxLineW = scaled(185); // modnar: right side extended, increase maxLineW
        y0 += s20; // BR: squeezed
        g.setFont(narrowFont(16));
        g.setColor(Color.black);
        List<String> desc1Lines = wrappedLines(g, desc1, maxLineW); // BR:
        g.fillOval(x0, y0-s8, s5, s5);
        for (String line: desc1Lines) {
            drawString(g,line, x0+s8, y0);
            y0 += s16;
        }

        // draw race desc #2
        y0 += s3;
        List<String> desc2Lines = wrappedLines(g, desc2, maxLineW); // BR:
        g.fillOval(x0, y0-s8, s5, s5);
        for (String line: desc2Lines) {
            drawString(g,line, x0+s8, y0);
            y0 += s16;
        }

        // modnar: draw race desc #4, with 'if' check
        if (desc4 != null) {
            y0 += s3;
            List<String> desc4Lines = wrappedLines(g, desc4, maxLineW); // BR:
            g.fillOval(x0, y0-s8, s5, s5);
            for (String line: desc4Lines) {
                drawString(g,line, x0+s8, y0);
                y0 += s18;
            }
        }
        
        // draw race desc #3
        y0 += s3;  // BR: squeezed
        // String desc3 = race.description3.replace("[race]", race.setupName());
        List<String> desc3Lines = scaledNarrowWrappedLines(g0, desc3, maxLineW+s8, 5, 16, 13);
        for (String line: desc3Lines) {
            drawString(g,line, x0, y0);
            y0 += s16;
        }

        // BR: draw Ship Set label
        String shipSetLbl = text("SETUP_SHIP_SET_LABEL");
        int x3 = colorBox[0].x;
        int y3 = colorBox[0].y-scaled(148);
        g.setFont(narrowFont(20));
        g.setColor(Color.black);
        drawString(g,shipSetLbl, x, y3);

        if (hoverBox == shipSetBox) {
            Stroke prev = g.getStroke();
            g.setStroke(stroke4);
            g.setColor(Color.yellow);
            g.draw(hoverBox);
            g.setStroke(prev);
        }

        // draw homeworld label
        String homeLbl = text("SETUP_HOMEWORLD_NAME_LABEL");
        x3 = colorBox[0].x;
        // y3 = colorBox[0].y-s100-s14;
        y3 = colorBox[0].y-scaled(101); // BR: squeezed
        g.setFont(narrowFont(20));
        g.setColor(Color.black);
        drawString(g,homeLbl, x3, y3);

        if (hoverBox == homeWorldBox) {
            Stroke prev = g.getStroke();
            g.setStroke(stroke4);
            g.setColor(Color.yellow);
            g.draw(hoverBox);
            g.setStroke(prev);
        }

        // draw leader name label
        String nameLbl = text("SETUP_LEADER_NAME_LABEL");
        x3 = colorBox[0].x;
        // y3 = colorBox[0].y-s60;
        y3 = colorBox[0].y-s54; // BR: squeezed
        g.setFont(narrowFont(20));
        g.setColor(Color.black);
        drawString(g,nameLbl, x3, y3);

        if (hoverBox == leaderBox) {
            Stroke prev = g.getStroke();
            g.setStroke(stroke4);
            g.setColor(Color.yellow);
            g.draw(hoverBox);
            g.setStroke(prev);
        }

        // draw empire color label
        String colorLbl = text("SETUP_RACE_COLOR");
        x3 = colorBox[0].x;
        y3 = colorBox[0].y-s7;
        g.setFont(narrowFont(20));
        g.setColor(Color.black);
        drawString(g,colorLbl, x3, y3);

        // draw selected & hovering colors
        for (int i=0;i<colorBox.length;i++) {
            int xC = colorBox[i].x;
            int yC = colorBox[i].y;
            int wC = colorBox[i].width;
            int hC = colorBox[i].height;
            Color c = newGameOptions().color(i);
            if (hoverBox == colorBox[i]) {
                Stroke prev = g.getStroke();
                g.setStroke(BasePanel.stroke2);
                g.setColor(Color.yellow);
                g.drawRect(xC, yC, wC, hC);
                g.setStroke(prev);
            }
            if (newGameOptions().selectedPlayerColor() == i) {
                g.setColor(c);
                g.fillRect(xC, yC, wC, hC);
                Stroke prev = g.getStroke();
                g.setStroke(BasePanel.stroke2);
                g.setColor(GameUI.setupFrame());
                g.drawRect(xC, yC, wC, hC);
                g.setStroke(prev);
            }
        }

        drawFixButtons(g, false);
        drawButtons(g, false);
		showGuide(g);

		if (showTiming)
			System.out.println("paintComponent() Time = " + (System.currentTimeMillis()-timeStart));
	}
    private void goToMainMenu() {
        buttonClick();
        RotPUI.instance().selectGamePanel();
        close();
    }
    private void goToGalaxySetup() {
        buttonClick();
        RotPUI.instance().selectSetupGalaxyPanel();
        close();
    }
    private void clearImages() {
        backImg			= null;
        raceImg			= null;
        raceIconImg		= null;
        raceBackImg		= null;
        shipBackImg		= null;
        fleetBackImg	= null;
        buttonBackImg	= null;
        resetFleetImages();
        resetRaceMug();
    }
    @Override protected void close() {
    	super.close();
    	clearImages();
    }
    private void selectRace(int i) {
        String selRace = newGameOptions().selectedPlayerRace();
        List<String> races = newGameOptions().startingRaceOptions();
        if (i <= races.size()) {
            if (!selRace.equals(races.get(i))) {
                newGameOptions().selectedPlayerRace(races.get(i));
                raceChanged();
                repaint();
            }
        }
    }
    private void shipSetChanged() {
    	shipSetTxt.setText(playerShipSet.displaySet());
    	shipStyle = playerShipSet.realShipSetId();
        fleetBackImg = null;
        resetFleetImages();
    }
    private void noFogBoxChanged() {
    	resetRaceMug();
    	backImg = null;
        repaint();
    }
    private void checkBoxChanged() { repaint(); }
    void raceChanged() {
        Race r   =  Race.keyed(newGameOptions().selectedPlayerRace());
      	dataRace = playerCustomRace.getRace(); // BR:
        r.resetSetupImage();
        r.resetMugshot();
        shipSetChanged();
        leaderName.setText(r.randomLeaderName());
        newGameOptions().selectedLeaderName(leaderName.getText());
        homeWorld.setText(r.defaultHomeworldName());
        newGameOptions().selectedHomeWorldName(homeWorld.getText());
        raceImg = null;
        raceIconImg = null;
    }
    private void selectColor(int i) {
        int selColor = newGameOptions().selectedPlayerColor();
        if (selColor != i) {
            newGameOptions().selectedPlayerColor(i);
            repaint();
        }
    }
    private void initTextField(JTextField value) {
        value.setBackground(GameUI.setupFrame());
        value.setBorder(newEmptyBorder(3,3,0,0));
        value.setPreferredSize(new Dimension(FIELD_W, FIELD_H));
        value.setFont(narrowFont(20));
        value.setForeground(Color.black);
        value.setCaretColor(Color.black);
        value.putClientProperty("caretWidth", s3);
        value.setVisible(true);
        value.addMouseListener(this);
        add(value);
    }
    private BufferedImage raceImg() {
        if (raceImg == null) {
            int newW = wCtrFrame;
            int newH = hCtrFrame;
            raceImg = new BufferedImage(newW, newH, TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) raceImg.getGraphics();            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    		
            String selRace = newGameOptions().selectedPlayerRace();
    		BufferedImage img = newBufferedImage(Race.keyed(selRace).setupImage());
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);
    		g.drawImage(img, 0, 0, newW, newH, 0, 0, imgW, imgH, null);
    		g.dispose();
        }
        return raceImg;
    }
    private BufferedImage raceIcon() {
        if (raceIconImg == null) {
            int newW = iconSize;
            int newH = iconSize;
            raceIconImg = new BufferedImage(newW, newH, TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) raceIconImg.getGraphics();            
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    		
            String selRace    = newGameOptions().selectedPlayerRace();
    		BufferedImage img = newBufferedImage(Race.keyed(selRace).flagNorm());
            int imgW = img.getWidth(null);
            int imgH = img.getHeight(null);
    		g.drawImage(img, 0, 0, newW, newH, 0, 0, imgW, imgH, null);
    		g.dispose();
        }
        return raceIconImg;
    }
    private BufferedImage fleetBackImg() {
        if (fleetBackImg == null)
            initFleetBackImg();
        return fleetBackImg;
    }
    private BufferedImage shipBackImg() {
        if (shipBackImg == null)
        	shipBackImg = getShipBackImg();
        return shipBackImg;
    }
    static BufferedImage raceBackImg() {
        if (raceBackImg == null)
            initRaceBackImg();
        return raceBackImg;
    }
    private BufferedImage backImg() {
        if (backImg == null)
            initBackImg();
        return backImg;
    }
    private BufferedImage buttonBackImg() {
        if (buttonBackImg == null)
            initButtonBackImg();
        return buttonBackImg;
    }
    private void initFleetBackImg() {
		long timeStart = System.currentTimeMillis();
		fleetBackImg = new BufferedImage(fleetWidth, fleetHeight, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) fleetBackImg.getGraphics();
        g.setComposite(AlphaComposite.SrcOver);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		BufferedImage shipBack = shipBackImg();
        for(int i=0; i<shipNum; i++) {
            int x = 0;
            int y = i * shipDist;
            g.drawImage(shipBack, x, y, shipWidth, shipHeight, null);
            g.drawImage(getShipImage(i), x, y, shipWidth, shipHeight, null);
        }
        g.dispose();
		if (showTiming)
			System.out.println("initFleetBackImg() Time = " + (System.currentTimeMillis()-timeStart));
    }
    private BufferedImage getShipBackImg() {
		long timeStart = System.currentTimeMillis();
		BufferedImage shipBackImg = gc().createCompatibleImage(shipWidth, shipHeight);
        Point2D center = new Point2D.Float(shipWidth/2f, shipHeight/2f);
        float radius = s60;
        float[] dist = {0.0f, 0.55f, 0.85f, 1.0f};
        Color[] colors = {GameUI.raceCenterColor(), GameUI.raceCenterColor(), GameUI.raceEdgeColor(), GameUI.raceEdgeColor()};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) shipBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, shipWidth, shipHeight);
        g.dispose();
		if (showTiming)
			System.out.println("initShipBackImg() Time = " + (System.currentTimeMillis()-timeStart));
		return shipBackImg;
    }
    private static void initRaceBackImg() {
        int w = s76;
        int h = s82;
        raceBackImg = gc().createCompatibleImage(w, h);

        Point2D center = new Point2D.Float(w/2, h/2);
        float radius = s78;
        float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
        Color[] colors = {GameUI.raceCenterColor(), GameUI.raceCenterColor(), GameUI.raceEdgeColor(), GameUI.raceEdgeColor()};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) raceBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, w, h);
        g.dispose();
    }
    private void initButtonBackImg() {
		int cnr = s5;
		int xMin = guideBox.x;
		int yMin = guideBox.y;
		int xMax = defaultBox.x + defaultBox.width;
		int yMax = defaultBox.y + defaultBox.height;
		xButton = xMin-s2;
		yButton = yMin-s2;
		wButton = xMax - xMin + s4;
		hButton = yMax - yMin + s4;
		
		buttonBackImg = new BufferedImage(wButton, hButton, TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) buttonBackImg.getGraphics();
		setFontHints(g);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.setFont(smallButtonFont);
		// draw DEFAULT button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(defaultBox.x-xButton, defaultBox.y-yButton, defaultBox.width, defaultBox.height, cnr, cnr);

		// draw LAST button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(lastBox.x-xButton, lastBox.y-yButton, lastBox.width, lastBox.height, cnr, cnr);

		// draw USER button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(userBox.x-xButton, userBox.y-yButton, userBox.width, userBox.height, cnr, cnr);

		// draw GUIDE button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(guideBox.x-xButton, guideBox.y-yButton, guideBox.width, guideBox.height, cnr, cnr);

		drawButtons(g, true);
    }
    private void initBackImg() {
		long timeStart = System.currentTimeMillis();
        int w = getWidth();
        int h = getHeight();
        backImg = newOpaqueImage(w, h);
        Graphics2D g = (Graphics2D) backImg.getGraphics();
        setFontHints(g);
        
		// modnar: use (slightly) better upsampling
        // BR: Even better for unique rendering
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // background image
        Image back = GameUI.defaultBackground;
        int imgW = back.getWidth(null);
        int imgH = back.getHeight(null);
        g.drawImage(back, 0, 0, w, h, 0, 0, imgW, imgH, this);

        // draw title
        String title = text(guiTitleID);
        g.setFont(narrowFont(50));
        int sw = g.getFontMetrics().stringWidth(title);
        int x0 = (w - sw) / 2;
        int y0 = s80;
        drawBorderedString(g, title, 2, x0, y0, Color.darkGray, Color.white);

        // draw shading, modnar: extend right side
		// modnar: extend out for new Races
        g.setColor(GameUI.setupShade());
        g.fillRect(xShading(), yShading, wShading(), hShading); // BR: adjusted Shading right position
        // g.fillRect(scaled(125), s95, scaled(1040), scaled(515));

        // draw race frame
        g.setColor(GameUI.setupFrame());
        g.fillRect(xCtrFrame(), yCtrFrame, wCtrFrame, hCtrFrame);
        // g.fillRect(scaled(420), raceFrameY, scaled(395), raceFrameH);

        // draw race left gradient
		// modnar: extend out for new Races
        g.setPaint(GameUI.raceLeftBackground());
        g.fillRect(xLeftFrame(), yLeftFrame, wLeftFrame(), hLeftFrame);
        // g.fillRect(scaled(140);, scaled(110), scaled(280), scaled(485));

        // draw race right gradient, modnar: extend right side
        g.setPaint(GameUI.raceRightBackground(xRightFrame()));
        g.fillRect(xRightFrame(), yRightFrame, wRightFrame, hRightFrame);
        // g.fillRect(scaled(815), scaled(110), scaled(335), scaled(485)); // BR: adjusted gradient right position

        int cnr = s5;
        int buttonH = s45;
        int buttonW = scaled(220);

        int xL = xLeftFrame() + s15;
		int xM = xL + s90;
        int xR = xM + s95; // modnar: set column for new Races

        float fog = noFogOnIcons()? 1.0f : 0.3f;
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , fog);
        
        for (int i=0; i<5; i++) {
        	int y = yLeftFrame + scaled(12 + i * 95);
        	drawRaceBox(g, 2*i,   xL, y, comp);
        	drawRaceBox(g, 1+2*i, xM, y, comp);
        }
        if(!isOS())  // For modnar new Races
            for (int i=0; i<6; i++) {
            	int y = yLeftFrame + scaled(10 + i * 80);
            	drawRaceBox(g, i+10,   xR, y, comp);
            }
        
        // draw color buttons on right panel
        int xC = xColors();
        int yC = yColors;
        int wC = wColors;
        int hC = hColors;
        for (int i=0;i<MAX_COLORS;i++) {
            int yC1 = i%2 == 0 ? yC : yC+hC+s5;
            Color c = options().color(i);
            Color c0 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 160); // modnar: less transparent unselected color
            g.setColor(c0);
            g.fillRect(xC, yC1, wC, hC);
            colorBox[i].setBounds(xC, yC1, wC, hC);
            if (i%2 == 1)
                xC += (wC+s5); // modnar: add new colors, less separation between color boxes
        }

        // draw Help Button
        helpBox.setBounds(s20,s20,s20,s25);
        g.setColor(darkBrownC);
        g.fillOval(helpBox.x, helpBox.y, helpBox.width, helpBox.height);
        
        // draw left button
        cancelBox.setBounds(scaled(710), scaled(685), buttonW, buttonH);
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(cancelBox.x, cancelBox.y, buttonW, buttonH, cnr, cnr);

        // draw right button
        nextBox.setBounds(scaled(950), scaled(685), buttonW, buttonH);
        g.setPaint(GameUI.buttonRightBackground());
        g.fillRoundRect(nextBox.x, nextBox.y, buttonW, buttonH, cnr, cnr);

		// setBounds DEFAULT button
        g.setFont(smallButtonFont);
		buttonH = s30;
		buttonW = defaultButtonWidth(g);
		int xB	= cancelBox.x - (buttonW + bSep);
		int yB	= cancelBox.y + s15;
		defaultBox.setBounds(xB, yB, buttonW, buttonH);

		// setBounds LAST button
		buttonH = s30;
		buttonW = lastButtonWidth(g);
		xB -= (buttonW + bSep);
		lastBox.setBounds(xB, yB, buttonW, buttonH);

		// setBounds USER button
		buttonW = userButtonWidth(g);
		xB -= (buttonW + bSep);
		userBox.setBounds(xB, yB, buttonW, buttonH);

		// setBounds GUIDE button
		buttonW = guideButtonWidth(g);
		xB = s20;
		guideBox.setBounds(xB, yB, buttonW, buttonH);

		// BR: No Fog on Icons
		int noFogX = xLeftFrame();
		int noFogY = yCtrFrame + hCtrFrame + s5;
		String label = noFogOnIcons.getLabel();
		sw = g.getFontMetrics().stringWidth(label);
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRect(noFogX, noFogY, sw + s40, s25);
        g.setColor(GameUI.borderBrightColor());
        g.setFont(labelFont);
        g.drawString(label, noFogX + s30, noFogY + s19);
		
		// BR: Player Race Customization
        // far left button
        g.setFont(smallButtonFont);
        int smallButtonH = s30;
        int smallButtonW = g.getFontMetrics().stringWidth(text(customRaceKey)) + smallButtonMargin;
        xB = noFogX;
        yB = noFogY + s35;
        playerRaceSettingBox.setBounds(xB, yB, smallButtonW, smallButtonH);
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(playerRaceSettingBox.x, playerRaceSettingBox.y, smallButtonW, smallButtonH, cnr, cnr);

        drawFixButtons(g, true);
        initButtonBackImg();
        
        g.dispose();
		if (showTiming) 
			System.out.println("initBackImg() Time = " + (System.currentTimeMillis()-timeStart));
    }
    private void initRaceMugImg() {
		long timeStart = System.currentTimeMillis();
		
        List<String> races = newGameOptions().startingRaceOptions();
		BufferedImage back = raceBackImg();
		int bW = back.getWidth();
		int bH = back.getHeight();
		for (int num=0; num<MAX_RACES; num++) {
			// modnar: 80% size box for newRaces
	        float raceBoxSize = num >= 10? 0.8f : 1.0f;
	        int  rbW = (int)(raceBoxSize * bW);
	        int  rbH = (int)(raceBoxSize * bH);

	        racemugs[num] = new BufferedImage(rbW, rbH, TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) racemugs[num].getGraphics();
            g.setComposite(AlphaComposite.SrcOver);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
            g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        
	        g.drawImage(back, 0, 0, rbW, rbH, null); // modnar: 80% size box for newRaces

	        BufferedImage img = newBufferedImage(Race.keyed(races.get(num)).diploMugshotQuiet());
            int imgW = img.getWidth();
            int imgH = img.getHeight();
    		g.drawImage(img, 0, 0, rbW, rbH, 0, 0, imgW, imgH, null);
    		g.dispose();
		}
		if (showTiming)
			System.out.println("initFleetBackImg() Time = " + (System.currentTimeMillis()-timeStart));
    }
    private void drawRaceBox(Graphics2D g, int num, int x, int y, Composite comp) {
        if (racemugs[num] == null)
        	initRaceMugImg();
        BufferedImage mug = racemugs[num];
        raceBox[num].setBounds(x, y, mug.getWidth(), mug.getHeight());
        
        Composite prevC = g.getComposite();
        if (comp != null)
            g.setComposite(comp);
        g.drawImage(mug, x, y, null);
        g.setComposite(prevC);
    }
    private BufferedImage getShipImage(int shapeId) {
    	if (fleetImages[shapeId] != null)
    		return fleetImages[shapeId];
    	fleetImages = new BufferedImage[MAX_SHIP];
        ShipImage images = ShipLibrary.current().shipImage(shipStyle, shipSize, shapeId);
        Image img = icon(images.baseIcon()).getImage();
        int w0 = img.getWidth(null);
        int h0 = img.getHeight(null);
        float scale = min((float)shipWidth/w0, (float)shipHeight/h0);
        int w1 = (int)(scale*w0);
        int h1 = (int)(scale*h0);
        BufferedImage resizedImg = new BufferedImage(w1,h1, TYPE_INT_ARGB);
        Graphics2D g = resizedImg.createGraphics();
		// modnar: one-step progressive image downscaling, mostly for Sakkra ships (higher-res image files)
		// there should be better methods
		if (scale < 0.5) {
			BufferedImage tmp = new BufferedImage(w0/2, h0/2, TYPE_INT_ARGB);
			Graphics2D g2D = tmp.createGraphics();
			// BR: Maximized Quality
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
	        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			//g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2D.drawImage(img, 0, 0, w0/2, h0/2, 0, 0, w0, h0, this);
			g2D.dispose();
			img = tmp;
			w0 = img.getWidth(null);
			h0 = img.getHeight(null);
			scale = scale*2;
		}
		// modnar: use (slightly) better downsampling
		// NOTE: drawing current ship design on upper-left of Design screen
		// https://docs.oracle.com/javase/tutorial/2d/advanced/quality.html
		// BR: Set to the best using modnar recommendations
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.drawImage(img, 0, 0, w1, h1, 0, 0, w0, h0, null);
        g.dispose();
        fleetImages[shapeId] = resizedImg;
		return fleetImages[shapeId];
    }
    // BR: Display UI panel for Player Race Customization
    private void goToPlayerRaceCustomization() {
        buttonClick();
        EditCustomRaceUI.instance().open(this);
		setVisible(false);      
    }
    @Override
    public String ambienceSoundKey() { 
        return GameUI.AMBIENCE_KEY;
    }
    @Override
    public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
    	checkModifierKey(e);
    	int k = e.getKeyCode();
        switch(k) {
	    	case KeyEvent.VK_R:
	    		playerIsCustom.set(false);
	        	newGameOptions().setRandomPlayerRace();
	        	raceChanged();
	        	repaint();
				return;
        	case KeyEvent.VK_ESCAPE:
            	doCancelBoxAction();
                return;
            case KeyEvent.VK_ENTER:
            	doNextBoxAction();
                return;
            case KeyEvent.VK_HOME:
            	int st = 1500;
            	System.out.println("");
            	System.out.println("ShipLaser");
            	playAudioClip("ShipLaser");
            	sleep(st);
            	System.out.println("ShipMultiLaser");
            	playAudioClip("ShipMultiLaser");
            	sleep(st);
            	System.out.println("ShipNeutronPelletGun");
            	playAudioClip("ShipNeutronPelletGun");
            	sleep(st);
            	System.out.println("ShipIonCannon");
            	playAudioClip("ShipIonCannon");
            	sleep(st);
            	System.out.println("ShipMassDriver");
            	playAudioClip("ShipMassDriver");
            	sleep(st);
            	System.out.println("ShipNeutronBlaster");
            	playAudioClip("ShipNeutronBlaster");
            	sleep(st);
            	System.out.println("ShipGravitonBeam");
            	playAudioClip("ShipGravitonBeam");
            	sleep(st);
            	System.out.println("ShipHardBeam");
            	playAudioClip("ShipHardBeam");
            	sleep(st);
            	System.out.println("ShipFusionBeam");
            	playAudioClip("ShipFusionBeam");
            	sleep(st);
            	System.out.println("ShipMegaBoltCannon");
            	playAudioClip("ShipMegaBoltCannon");
            	sleep(st);
            	System.out.println("ShipPhasor");
            	playAudioClip("ShipPhasor");
            	sleep(st);
            	System.out.println("ShipAutoBlaster");
            	playAudioClip("ShipAutoBlaster");
            	sleep(st);
            	System.out.println("ShipTachyonBeam");
            	playAudioClip("ShipTachyonBeam");
            	sleep(st);
            	System.out.println("ShipGaussAutoCannon");
            	playAudioClip("ShipGaussAutoCannon");
            	sleep(st);
            	System.out.println("ShipParticleBeam");
            	playAudioClip("ShipParticleBeam");
            	sleep(st);
            	System.out.println("ShipPlasmaCannon");
            	playAudioClip("ShipPlasmaCannon");
            	sleep(st);
            	System.out.println("ShipDeathRay");
            	playAudioClip("ShipDeathRay");
            	sleep(st);
            	System.out.println("ShipDisruptor");
            	playAudioClip("ShipDisruptor");
            	sleep(st);
            	System.out.println("ShipPulsePhasor");
            	playAudioClip("ShipPulsePhasor");
            	sleep(st);
            	System.out.println("ShipTriFocusPlasmaCannon");
            	playAudioClip("ShipTriFocusPlasmaCannon");
            	sleep(st);
                return;
            default:
                return;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 3)
            return;
        if (hoverBox == null)
            return;
        if (hoverBox == helpBox) {
			if (SwingUtilities.isRightMouseButton(e))
				showHotKeys();
			else
				showHelp();
            return;
        }
        search:
        if (hoverBox == cancelBox)
        	doCancelBoxAction();
        else if (hoverBox == nextBox)
        	doNextBoxAction();
        else if (hoverBox == defaultBox)
        	doDefaultBoxAction();
        else if (hoverBox == guideBox)
			doGuideBoxAction();
        else if (hoverBox == userBox)
			doUserBoxAction();
        else if (hoverBox == lastBox)
			doLastBoxAction();
        // BR: Player Race customization
        else if (hoverBox == playerRaceSettingBox)
            goToPlayerRaceCustomization();
        else if (hoverBox == noFogBox) {
        	noFogOnIcons.toggle(e, this);
            noFogBoxChanged();
        }
        else if (hoverBox == checkBox) {
            playerIsCustom.toggle(e, this);
            checkBoxChanged();
        }
        // BR: Player Ship Set Selection
        else if (hoverBox == shipSetBox) {
        	playerShipSet.toggle(e, this);
        	shipSetChanged();
        	repaint();
        }
        else {
            for (int i=0;i<raceBox.length;i++) {
                if (hoverBox == raceBox[i]) {
                    selectRace(i);
                    shipSetChanged();
                    break search;
                }
            }
            for (int i=0;i<colorBox.length;i++) {
                if (hoverBox == colorBox[i]) {
                    selectColor(i);
                    break search;
                }
            }
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.getComponent() == leaderName) {
            leaderName.requestFocus();
            hoverBox = leaderBox;
            repaint();
        }
        else if (e.getComponent() == homeWorld) {
            homeWorld.requestFocus();
            hoverBox = homeWorldBox;
            repaint();
        }
        else if (e.getComponent() == shipSetTxt) {
        	//shipSetTxt.requestFocus();
            hoverBox = shipSetBox;
            repaint();
        }
    }
    @Override
    public void mouseExited(MouseEvent e) {
    	super.mouseExited(e);
        if (e.getComponent() == leaderName) {
            newGameOptions().selectedLeaderName(leaderName.getText());
            RotPUI.instance().requestFocus();
        }
        else if (e.getComponent() == homeWorld) {
            newGameOptions().selectedHomeWorldName(homeWorld.getText());
            RotPUI.instance().requestFocus();
        }
        if (hoverBox != null) {
            hoverBox = null;
            repaint();
        }
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (hoverBox == shipSetBox) {
        	playerShipSet.toggle(e);
        	shipSetChanged();
        	repaint();
        }
        else if (hoverBox == noFogBox) {
        	noFogOnIcons.toggle(e);
            noFogBoxChanged();
        }
        else if (hoverBox == checkBox) {
            playerIsCustom.toggle(e);
            checkBoxChanged();
        }
    }
}
