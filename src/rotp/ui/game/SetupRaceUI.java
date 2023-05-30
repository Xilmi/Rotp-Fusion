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

import static rotp.model.game.IGameOptions.LIVE_OPTIONS_FILE;
import static rotp.model.game.IGameOptions.optionsRace;
import static rotp.model.game.IGameOptions.playerCustomRace;
import static rotp.model.game.IGameOptions.playerIsCustom;
import static rotp.model.game.IGameOptions.playerShipSet;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import rotp.model.empires.Race;
import rotp.model.ships.ShipImage;
import rotp.model.ships.ShipLibrary;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.game.HelpUI.HelpSpec;
import rotp.ui.main.SystemPanel;
import rotp.util.ModifierKeysState;

public final class SetupRaceUI extends BaseModPanel implements MouseWheelListener {
    private static final long serialVersionUID	= 1L;
	private static final String guiTitleID		= "SETUP_SELECT_RACE";
	public  static final String GUI_ID          = "START_RACE";
	private static final String cancelKey		= "SETUP_BUTTON_CANCEL";
	private static final String customRaceKey	= "SETUP_BUTTON_CUSTOM_PLAYER_RACE";
    private static final int MAX_RACES  = 16; // modnar: increase MAX_RACES to add new Races
    private static final int MAX_COLORS = 16; // modnar: add new colors
    private static final int MAX_SHIP   = ShipLibrary.designsPerSize; // BR:
    private final Color checkBoxC  = new Color(178,124,87);
    private final Color darkBrownC = new Color(112,85,68);
    private int FIELD_W;
    private int FIELD_H;
    private BufferedImage backImg;
    private static BufferedImage raceBackImg;
    private BufferedImage shipBackImg;
    private BufferedImage raceImg;
    private Box	helpBox			= new Box("SETTINGS_BUTTON_HELP");
    private Box	cancelBox		= new Box("SETUP_RACE_CANCEL");
    private Box	nextBox			= new Box("SETUP_RACE_NEXT");
    private Box	leaderBox		= new Box("SETUP_RACE_LEADER");
    private Box	homeWorldBox	= new Box("SETUP_RACE_HOMEWORLD");
    private Box	playerRaceSettingBox = new Box("SETUP_RACE_CUSTOM"); // BR: Player Race Customization
    private Box	checkBox		= new Box("SETUP_RACE_CHECK_BOX"); // BR: Player Race Customization
    private Box	shipSetBox		= new Box("SETUP_RACE_SHIPSET"); // BR: ShipSet Selection
    private Box[] raceBox		= new Box[MAX_RACES];
    private Box[] colorBox		= new Box[MAX_COLORS];
    private Box[] shipBox		= new Box[MAX_SHIP]; // BR: ShipSet Selection

    private static BufferedImage[] racemugs = new BufferedImage[MAX_RACES];
    private JTextField leaderName = new JTextField("");
    private JTextField homeWorld  = new JTextField("");
    private JTextField shipSetTxt = new JTextField(""); // BR: ShipSet Selection
    private int shipSetId = 0; // The index from the list
    private int shipSize = 2;
    private int shipId = 0;
    private int shipW = 0;
    private int shipH = 0;
    @SuppressWarnings("unchecked")
	private List<BufferedImage>[] shipImages = new ArrayList[MAX_SHIP];
    private int bSep = s15;
    private Race dataRace;
 
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
        for (int i=0;i<shipBox.length;i++)
        	shipBox[i] = new Box("SETUP_RACE_SHIPSET");
        for (int i=0;i<shipImages.length;i++)
        	shipImages[i] = new ArrayList<BufferedImage>();
        FIELD_W = scaled(160);
        FIELD_H = s24;
        initTextField(homeWorld);
        initTextField(leaderName);
        initTextField(shipSetTxt); // BR:
    }
	@Override protected void singleInit() { paramList = optionsRace; }
    @Override public void init() {
    	super.init();
    	EditCustomRaceUI.updatePlayerCustomRace();
        leaderName.setFont(narrowFont(20));
        // homeWorld.setFont(narrowFont(20));
        setHomeWorldFont(); // BR: MonoSpaced font for Galaxy
        shipSetTxt.setFont(narrowFont(20)); // BR:
        refreshGui();
        // Save initial options
        guiOptions().updateOptionsAndSaveToFileName(LIVE_OPTIONS_FILE);
    }
	@Override public void showHelp() {
		loadHelpUI();
		repaint();   
	}
    @Override public void advanceHelp() { cancelHelp(); }
	@Override public void cancelHelp() { RotPUI.helpUI().close(); }
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
		nL   = 5;
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
			guiOptions().updateOptionsAndSaveToFileName(LIVE_OPTIONS_FILE);
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
			guiOptions().updateOptionsAndSaveToFileName(LIVE_OPTIONS_FILE);
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
		setFontHints(g);
		drawBackButtons(g);
		drawButtons(g);
		g.dispose();
	}
	private void drawBackButtons(Graphics2D g) {
		int cnr = s5;
        // draw left button
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(cancelBox.x, cancelBox.y, cancelBox.width, cancelBox.height, cnr, cnr);

        // draw right button
        g.setPaint(GameUI.buttonRightBackground());
        g.fillRoundRect(nextBox.x, nextBox.y, nextBox.width, nextBox.height, cnr, cnr);

		// draw DEFAULT button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);

		// draw LAST button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);

		// draw USER button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);

		// draw GUIDE button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);

		// BR: Player Race Customization
        // far left button
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(playerRaceSettingBox.x, playerRaceSettingBox.y,
        		playerRaceSettingBox.width, playerRaceSettingBox.height, cnr, cnr);
	}
    private void drawHelpButton(Graphics2D g) {
        helpBox.setBounds(s20,s20,s20,s25);
        g.setColor(darkBrownC);
        g.fillOval(s20, s20, s20, s25);
        g.setFont(narrowFont(25));
        if (helpBox == hoverBox)
            g.setColor(Color.yellow);
        else
            g.setColor(Color.white);

        drawString(g,"?", s26, s40);
    }
	private void drawButtons(Graphics2D g) {
		// left button
        int cnr = s5;
        g.setFont(narrowFont(30));
        String text1 = text(cancelButtonKey());
        int sw1 = g.getFontMetrics().stringWidth(text1);
        int x1 = cancelBox.x+((cancelBox.width-sw1)/2);
        int y1 = cancelBox.y+cancelBox.height-s12;
        Color c1 = hoverBox == cancelBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text1, 2, x1, y1, GameUI.borderDarkColor(), c1);
        Stroke prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(cancelBox.x, cancelBox.y, cancelBox.width, cancelBox.height, cnr, cnr);
        g.setStroke(prev);

        // right button
        String text2 = text("SETUP_BUTTON_NEXT");
        int sw2= g.getFontMetrics().stringWidth(text2);
        int x2 = nextBox.x+((nextBox.width-sw2)/2);
        int y2 = nextBox.y+nextBox.height-s12;
        Color c2 = hoverBox == nextBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text2, 2, x2, y2, GameUI.borderDarkColor(), c2);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(nextBox.x, nextBox.y, nextBox.width, nextBox.height, cnr, cnr);
        g.setStroke(prev);

        // BR: Player Race Customization
        // far left button
        g.setFont(narrowFont(20));
        String text4 = text(customRaceKey);
        int sw4= g.getFontMetrics().stringWidth(text4);
        int x4 = playerRaceSettingBox.x + ((playerRaceSettingBox.width-sw4)/2);
        int y4 = playerRaceSettingBox.y + playerRaceSettingBox.height-s8;
        Color c4 = hoverBox == playerRaceSettingBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text4, 2, x4, y4, GameUI.borderDarkColor(), c4);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(playerRaceSettingBox.x, playerRaceSettingBox.y, playerRaceSettingBox.width, playerRaceSettingBox.height, cnr, cnr);
        g.setStroke(prev);
        
        // BR: Race customization check box
        int checkW = s16;
        int checkX = playerRaceSettingBox.x + playerRaceSettingBox.width + s10;    
        int checkY = playerRaceSettingBox.y + playerRaceSettingBox.height - s7;
        checkBox.setBounds(checkX, checkY-checkW, checkW, checkW);
        prev = g.getStroke();
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
        g.setStroke(prev);

        g.setFont(narrowFont(20));
        // BR: Default Button 
		String text = text(defaultButtonKey());
        int sw	 = g.getFontMetrics().stringWidth(text);
        int x = defaultBox.x+((defaultBox.width-sw)/2);
        int y = defaultBox.y+defaultBox.height-s8;
        Color c = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
        g.setStroke(prev);

        // BR: Last Button 
		text = text(lastButtonKey());
        sw  = g.getFontMetrics().stringWidth(text);
		x = lastBox.x+((lastBox.width-sw)/2);
		y = lastBox.y+lastBox.height-s8;
		c = hoverBox == lastBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);
		g.setStroke(prev);
 
		// BR: User Button 
		text = text(userButtonKey());
        sw 	 = g.getFontMetrics().stringWidth(text);
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
		g.setFont(narrowFont(20));

		// BR: Guide Button 
		text = text(guideButtonKey());
        sw 	 = g.getFontMetrics().stringWidth(text);
		x = guideBox.x+((guideBox.width-sw)/2);
		y = guideBox.y+guideBox.height-s8;
		c = hoverBox == guideBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	@Override
    public void paintComponent(Graphics g0) {
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
//       leaderName.setLocation(x, y-s53);
        leaderName.setLocation(x, y-s50); // BR: squeezed
//      leaderBox.setBounds(x-s1, y-s53, FIELD_W+s2, FIELD_H+s2);
        leaderBox.setBounds(x-s1, y-s50, FIELD_W+s2, FIELD_H+s2); // BR: squeezed
        homeWorld.setCaretPosition(homeWorld.getText().length());
//      homeWorld.setLocation(x, y-s100-s10);
        homeWorld.setLocation(x, y-s97); // BR: squeezed
		// modnar: test hover text
//		homeWorld.setToolTipText("<html> Homeworld Name is used as <br> the Galaxy Map when selecting <br> Map Shape [Text]. <br><br> (Unicode characters allowed)");
//      homeWorldBox.setBounds(x-s1, y-s100-s10, FIELD_W+s2, FIELD_H+s2);
        homeWorldBox.setBounds(x-s1, y-s97, FIELD_W+s2, FIELD_H+s2); // BR: squeezed

		// modnar: use (slightly) better upsampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth(); // Full screen width
        int h = getHeight(); // Full screen height

        // background image
        g.drawImage(backImg(), 0, 0, w, h, this);

        // selected race center img
        g.drawImage(raceImg(), scaled(425), scaled(108), scaled(385), scaled(489), null);

        // draw Ship frames on the right
        drawShipBoxes(g);

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

        int x0 = colorBox[0].x;

        // race icon
        Race race = Race.keyed(newGameOptions().selectedPlayerRace());
        // int iconH = scaled(115);
        int iconH = scaled(95); // BR: squeezed
        BufferedImage icon = newBufferedImage(race.flagNorm());
        int imgX = scaled(868); // modnar: right side extended, shift race icon
        int imgY = scaled(120);
        //g.drawImage(icon, imgX, imgY, iconH, iconH, null);
        g.drawImage(icon, imgX, imgY, imgX+iconH, imgY+iconH, 0, 0, icon.getWidth(), icon.getHeight(), null);

        // draw race name
        // int y0 = scaled(260);
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
        // y0 += s25;
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
        // y0 += s12;
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
		drawHelpButton(g);
        drawButtons(g);
		showGuide(g);
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
    @Override protected void close() {
    	super.close();
        backImg = null;
        raceImg = null;
        for (int i=0; i<shipImages.length; i++)
        	shipImages[i].clear();
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
    	shipSetId = playerShipSet.realShipSetId();
        for (int i=0; i<shipImages.length; i++)
        	shipImages[i].clear();
    }
    private void checkBoxChanged() { // BR: checkBoxChanged
        repaint();
    }
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
        // value.setBorder(newEmptyBorder(5,5,0,0));
        value.setBorder(newEmptyBorder(3,3,0,0));
        // value.setBorder(null);
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
            String selRace = newGameOptions().selectedPlayerRace();
            raceImg = newBufferedImage(Race.keyed(selRace).setupImage());
        }
        return raceImg;
    }
    private BufferedImage shipBackImg() {
        if (shipBackImg == null)
            initShipBackImg();
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
    private void initShipBackImg() {
        shipH = s65;
        shipW = (int) (shipH * 1.3314f);
        shipBackImg = gc().createCompatibleImage(shipW, shipH);

        Point2D center = new Point2D.Float(shipW/2, shipH/2);
        float radius = s60;
        float[] dist = {0.0f, 0.55f, 0.85f, 1.0f};
        Color[] colors = {GameUI.raceCenterColor(), GameUI.raceCenterColor(), GameUI.raceEdgeColor(), GameUI.raceEdgeColor()};
        RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
        Graphics2D g = (Graphics2D) shipBackImg.getGraphics();
        g.setPaint(p);
        g.fillRect(0, 0, shipW, shipH);
        g.dispose();
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
    private void initBackImg() {
        int w = getWidth();
        int h = getHeight();
        backImg = newOpaqueImage(w, h);
        Graphics2D g = (Graphics2D) backImg.getGraphics();
        setFontHints(g);
		// modnar: use (slightly) better upsampling
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
        g.fillRect(scaled(125), s95, scaled(1040), scaled(515)); // BR: adjusted Shading right position

        // draw race frame
        g.setColor(GameUI.setupFrame());
        g.fillRect(scaled(420), scaled(103), scaled(395), scaled(499));

        // draw race left gradient
		// modnar: extend out for new Races
        g.setPaint(GameUI.raceLeftBackground());
        g.fillRect(scaled(140), scaled(110), scaled(280), scaled(485));

        // draw race right gradient, modnar: extend right side
        g.setPaint(GameUI.raceRightBackground());
        g.fillRect(scaled(815), scaled(110), scaled(335), scaled(485)); // BR: adjusted gradient right position

        int cnr = s5;
        int buttonH = s45;
        int buttonW = scaled(220);

        int xL = scaled(155); // modnar: shift columns over for old Races
		int xCC = scaled(245); // modnar: shift columns over for old Races
        int xR = scaled(340); // modnar: set column for new Races

        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.3f);
        drawRaceBox(g, 0, xL, scaled(122), comp);
        drawRaceBox(g, 1, xCC, scaled(122), comp);
        drawRaceBox(g, 2, xL, scaled(217), comp);
        drawRaceBox(g, 3, xCC, scaled(217), comp);
        drawRaceBox(g, 4, xL, scaled(312), comp);
        drawRaceBox(g, 5, xCC, scaled(312), comp);
        drawRaceBox(g, 6, xL, scaled(407), comp);
        drawRaceBox(g, 7, xCC, scaled(407), comp);
        drawRaceBox(g, 8, xL, scaled(502), comp);
        drawRaceBox(g, 9, xCC, scaled(502), comp);
		drawRaceBox(g, 10, xR, scaled(120), comp); // modnar: add new Races
        drawRaceBox(g, 11, xR, scaled(200), comp); // modnar: add new Races
		drawRaceBox(g, 12, xR, scaled(280), comp); // modnar: add new Races
		drawRaceBox(g, 13, xR, scaled(360), comp); // modnar: add new Races
		drawRaceBox(g, 14, xR, scaled(440), comp); // modnar: add new Races
        drawRaceBox(g, 15, xR, scaled(520), comp); // modnar: add new Races

        // draw color buttons on right panel
        int xC = scaled(830);
        int yC = scaled(555);
        int wC = s21; // modnar: add new colors, change color box sizes
        int hC = s15;
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
        // draw Ship frames on right panel
        drawShipBoxes(g);

        // draw left button
        cancelBox.setBounds(scaled(710), scaled(685), buttonW, buttonH);
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(cancelBox.x, cancelBox.y, buttonW, buttonH, cnr, cnr);

        // draw right button
        nextBox.setBounds(scaled(950), scaled(685), buttonW, buttonH);
        g.setPaint(GameUI.buttonRightBackground());
        g.fillRoundRect(nextBox.x, nextBox.y, buttonW, buttonH, cnr, cnr);

		// draw DEFAULT button
        g.setFont(smallButtonFont);
		buttonH = s30;
		buttonW = defaultButtonWidth(g);
		int xB	= cancelBox.x - (buttonW + bSep);
		int yB	= cancelBox.y + s15;
		defaultBox.setBounds(xB, yB, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(defaultBox.x, defaultBox.y, buttonW, buttonH, cnr, cnr);

		// draw LAST button
		buttonH = s30;
		buttonW = lastButtonWidth(g);
		xB -= (buttonW + bSep);
		lastBox.setBounds(xB, yB, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(lastBox.x, lastBox.y, buttonW, buttonH, cnr, cnr);

		// draw USER button
		buttonW = userButtonWidth(g);
		xB -= (buttonW + bSep);
		userBox.setBounds(xB, yB, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(userBox.x, userBox.y, buttonW, buttonH, cnr, cnr);

		// draw GUIDE button
		buttonW = guideButtonWidth(g);
		xB = s20;
		guideBox.setBounds(xB, yB, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(guideBox.x, guideBox.y, buttonW, buttonH, cnr, cnr);

		// BR: Player Race Customization
        // far left button
        g.setFont(smallButtonFont);
        int smallButtonH = s30;
        int smallButtonW = g.getFontMetrics().stringWidth(text(customRaceKey)) + smallButtonMargin;
        playerRaceSettingBox.setBounds(scaled(140), scaled(615), smallButtonW, smallButtonH);
        g.setPaint(GameUI.buttonLeftBackground());
        g.fillRoundRect(playerRaceSettingBox.x, playerRaceSettingBox.y, smallButtonW, smallButtonH, cnr, cnr);

        g.dispose();
    }
    private void drawRaceBox(Graphics2D g, int num, int x, int y, Composite comp) {
        raceBox[num].setBounds(0,0,0,0);
        // modnar: 80% size box for newRaces
        float raceBoxSize = 1.0f;
        if (num >= 10)
            raceBoxSize = 0.8f;
        BufferedImage back = raceBackImg();
        int w = (int)(raceBoxSize * back.getWidth()); // modnar: 80% size box for newRaces
        int h = (int)(raceBoxSize * back.getHeight()); // modnar: 80% size box for newRaces
        
        g.drawImage(back, x, y, w, h, null); // modnar: 80% size box for newRaces

        List<String> races = newGameOptions().startingRaceOptions();
        if (num >= races.size())
            return;

        raceBox[num].setBounds(x,y,w,h);
        Race r = Race.keyed(races.get(num));
        if (racemugs[num] == null)
            racemugs[num] = newBufferedImage(r.diploMugshotQuiet());

        BufferedImage mug = racemugs[num];

        Composite prevC = g.getComposite();
        if (comp != null)
            g.setComposite(comp);
        g.drawImage(mug, x,y,w,h, null);
        g.setComposite(prevC);
    }
    private void drawShipBoxes(Graphics2D g) {
        int xS = scaled(830) + scaled(220);
        Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 1.0f);
        drawShipBox(g, 0, xS, scaled(120), comp);
        drawShipBox(g, 1, xS, scaled(200), comp);
        drawShipBox(g, 2, xS, scaled(280), comp);
        drawShipBox(g, 3, xS, scaled(360), comp);
        drawShipBox(g, 4, xS, scaled(440), comp);
        drawShipBox(g, 5, xS, scaled(520), comp);
    }
    private void drawShipBox(Graphics2D g, int num, int x, int y, Composite comp) {
        shipBox[num].setBounds(0,0,0,0);
        BufferedImage back = shipBackImg();
        int w = back.getWidth();
        int h = back.getHeight();
        g.drawImage(back, x, y, w, h, null);
        if (num >= MAX_SHIP)
            return;
        loadShipImages(num);
        shipBox[num].setBounds(x,y,w,h);
        g.drawImage(shipImages[num].get(shipId), x,y,w,h, null);
    }
    private void loadShipImages(int num) {
    	if (!shipImages[num].isEmpty())
    		return;
        ShipImage images = ShipLibrary.current().shipImage(shipSetId, shipSize, num);;
        for (String key: images.icons()) {
            Image img = icon(key).getImage();
            int w0 = img.getWidth(null);
            int h0 = img.getHeight(null);
            float scale = min((float)(shipW-s20)/w0, (float)(shipH-s20)/h0);

            int w1 = (int)(scale*w0);
            int h1 = (int)(scale*h0);
            BufferedImage resizedImg = new BufferedImage(w1,h1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImg.createGraphics();
			// modnar: one-step progressive image downscaling, mostly for Sakkra ships (higher-res image files)
			// there should be better methods
			if (scale < 0.5) {
				BufferedImage tmp = new BufferedImage(w0/2, h0/2, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2D = tmp.createGraphics();
				g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				//g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
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
			// should be possible to be even better
            //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            //g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
            //g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.drawImage(img, 0, 0, w1, h1, 0, 0, w0, h0, null);
            //g.drawImage(img, 0, 0, w1, h1, null);
            g.dispose();
            shipImages[num].add(resizedImg);
        }
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
//        	case KeyEvent.VK_F1:
//        		showHelp();
//        		return;
	    	case KeyEvent.VK_R:
	    		playerIsCustom.set(false);
	        	guiOptions().setRandomPlayerRace();
	        	raceChanged();
	        	repaint();
				return;
        	case KeyEvent.VK_ESCAPE:
            	doCancelBoxAction();
                return;
            case KeyEvent.VK_ENTER:
            	doNextBoxAction();
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
        } else if (hoverBox == checkBox) {
            playerIsCustom.toggle(e);
            checkBoxChanged();
        }
    }
}
