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
package rotp.ui.main.overlay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import rotp.model.Sprite;
import rotp.model.empires.Empire;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.ui.BasePanel;
import rotp.ui.main.GalaxyMapPanel;
import rotp.ui.main.MainUI;
import rotp.ui.main.SystemPanel;
import rotp.ui.sprites.BombardNoSprite;
import rotp.ui.sprites.BombardTargetSprite;
import rotp.ui.sprites.BombardYesSprite;
import rotp.ui.sprites.ClickToContinueSprite;
import rotp.ui.sprites.MapSprite;
import rotp.ui.vipconsole.IVIPConsole;
import rotp.ui.vipconsole.IVIPListener;

public class MapOverlayBombardPrompt extends MapOverlay implements IVIPListener {
    static final Color destroyedTextC = new Color(255,32,32,192);
    static final Color destroyedMaskC = new Color(0,0,0,160);
    Color maskC  = new Color(40,40,40,160);
    Area mask;
    BufferedImage planetImg;
    MainUI parent;
    boolean bombarded = false;
    int sysId;
    ShipFleet fleet;
    int pop, endPop, estKills, estFactoryKills, bases, endBases, fact, endFact, shield, transports;
    boolean drawSprites = false;
    ClickToContinueSprite clickSprite;
    BombardNoSprite noButton = new BombardNoSprite();
    BombardYesSprite yesButton = new BombardYesSprite();
    BombardTargetSprite targetButton = new BombardTargetSprite();
    SystemFlagSprite flagButton = new SystemFlagSprite();
    public MapOverlayBombardPrompt(MainUI p) {
        parent = p;
        clickSprite = new ClickToContinueSprite(parent);
    }
    public void releaseObjects() {
    	fleet = null;
    }
    public void init(int systemId, ShipFleet fl) {
        drawSprites = true;
        planetImg = null;
        Empire pl = player();
        flagButton.reset();
        StarSystem sys = galaxy().system(systemId);
        sysId = systemId;
        fleet = fl;
        bombarded = false;
        pl.sv.refreshFullScan(sysId);
        pop = endPop = pl.sv.population(sysId);
        bases = endBases = pl.sv.bases(sysId);
        fact = endFact = pl.sv.factories(sysId);
        shield = sys.colony().defense().shieldLevel();
        transports = player().transportsInTransit(sys);
        noButton.reset();
        yesButton.reset();
        targetButton.reset();
        parent.hideDisplayPanel();
        parent.map().setScale(20);
        parent.map().recenterMapOn(sys);
        parent.mapFocus(sys);
        parent.clickedSprite(sys);
        parent.repaint();
        estKills = Math.round(fl.expectedBombardDamage(false) / 200f);
        estFactoryKills = Math.round(fl.expectedBombardDamage(true) / 50f);
        initConsoleSelection("Bombard Planet", false);
    }
    private StarSystem starSystem() {
        return galaxy().system(sysId);
    }
    private void toggleFlagColor(boolean reverse) {
        player().sv.toggleFlagColor(sysId, reverse);
        parent.repaint();
    }
    private void resetFlagColor() {
        player().sv.resetFlagColor(sysId);
        parent.repaint();
    }
    public void bombardTarget() {
        if (drawSprites) {
            drawSprites = false;
            mask = null;
            targetBombard();
            bombard();
            parent.map().repaint();
        }
    }
    public void bombardYes() {
        if (drawSprites) {
            drawSprites = false;
            mask = null;
            softClick();
            bombard();
            parent.map().repaint();
        }
    }
    public void bombardCancel() {
        if (drawSprites) {
            drawSprites = false;
            mask = null;
            advanceMap();
        }
    }
    private void targetBombard() { // BR:
        // avoid multiple bombings triggered by
        // repaints from animation
        if (!bombarded) {
            bombarded = true;
            fleet.targetBombard(0.5f + options().selectedBombingTarget());
            Empire pl = player();
            endPop = pl.sv.population(sysId);
            endBases = pl.sv.bases(sysId);
            endFact = pl.sv.factories(sysId);
            initConsoleReport("Bombardement Report", true);
        }
    }
    private void bombard() {
        // avoid multiple bombings triggered by
        // repaints from animation
        if (!bombarded) {
            bombarded = true;
            fleet.bombard();
            Empire pl = player();
            endPop = pl.sv.population(sysId);
            endBases = pl.sv.bases(sysId);
            endFact = pl.sv.factories(sysId);
            initConsoleReport("Bombardement Report", true);
        }
    }
    @Override
    public boolean drawSprites()   { return drawSprites; }
    @Override
    public boolean masksMouseOver(int x, int y)   { return true; }
    @Override
    public boolean hoveringOverSprite(Sprite o) { return false; }
    @Override
    public void advanceMap() {
        parent.resumeTurn();
    }
    @Override
    public void paintOverMap(MainUI parent, GalaxyMapPanel ui, Graphics2D g) {
        StarSystem sys = galaxy().system(sysId);
        Empire pl = player();

        int s7 = BasePanel.s7;
        int s10 = BasePanel.s10;
        int s15 = BasePanel.s15;
        int s20 = BasePanel.s20;
        int s25 = BasePanel.s25;
        int s30 = BasePanel.s30;
        int s35 = BasePanel.s35;
        int s40 = BasePanel.s40;
        int s50 = BasePanel.s50;
        int s60 = BasePanel.s60;

        int w = ui.getWidth();
        int h = ui.getHeight();

        int transportH = transports > 0 ? s20 : 0;
        boolean targetOK = options().targetBombardAllowedForPlayer();
        int bdrW = s7;
        int buttonOffset = targetOK? s35 : 0; // BR: adjusted for target
        int boxW = scaled(540);
        int boxH = scaled(245)+transportH+buttonOffset; // BR: adjusted for target
        int boxH1 = BasePanel.s73+transportH+buttonOffset; // BR: adjusted for target

        int boxX = -s40+(w/2);
        int boxY = s40+(h-boxH)/2;

        // draw map mask
        if (mask == null) {
            int r = s60;
            int centerX = w*2/5;
            int centerY = h*2/5;
            Ellipse2D window = new Ellipse2D.Float();
            window.setFrame(centerX-r, centerY-r, r+r, r+r);
            Area st1 = new Area(window);
            Rectangle blackout  = new Rectangle();
            blackout.setFrame(0,0,w,h);
            mask = new Area(blackout);
            mask.subtract(st1);
        }
        g.setColor(maskC);
        g.fill(mask);
        // draw border
        g.setColor(MainUI.paneShadeC);
        g.fillRect(boxX-bdrW, boxY-bdrW, boxW+bdrW+bdrW, boxH+bdrW+bdrW);

        // draw Box
        g.setColor(MainUI.paneBackground);
        g.fillRect(boxX, boxY, boxW, boxH1);

        String sysName = player().sv.name(sys.id);
        // draw planet image
        if (planetImg == null) {
            if (sys.planet().type().isAsteroids()) {
                planetImg = newBufferedImage(boxW, boxH-boxH1);
                Graphics imgG = planetImg.getGraphics();
                imgG.setColor(Color.black);
                imgG.fillRect(0, 0, boxW, boxH-boxH1);
                drawBackgroundStars(imgG, boxW, boxH-boxH1);
                parent.drawStar((Graphics2D) imgG, sys.starType(), s60, boxW*4/5, (boxH-boxH1)/3);
                imgG.dispose();
            }
            else {
                planetImg = sys.planet().type().panoramaImage();
                int planetW = planetImg.getWidth();
                int planetH = planetImg.getHeight();
                Graphics imgG = planetImg.getGraphics();
                Empire emp = pl.sv.empire(sysId);
                if (emp != null) {
                    BufferedImage fortImg = emp.fortress(sys.colony().fortressNum());
                    int fortW = scaled(fortImg.getWidth());
                    int fortH = scaled(fortImg.getHeight());
                    int fortScaleW = fortW*planetW/w;
                    int fortScaleH = fortH*planetW/w;
                    int fortX = planetImg.getWidth()-fortScaleW;
                    int fortY = planetImg.getHeight()-fortScaleH+(planetH/5);
                    imgG.drawImage(fortImg, fortX, fortY, fortX+fortScaleW, fortY+fortScaleH, 0, 0, fortImg.getWidth(), fortImg.getHeight(), null);
                    imgG.dispose();
                }
            }
        }
        g.drawImage(planetImg, boxX, boxY+boxH1, boxW, boxH-boxH1, null);

        // draw header info
        int leftW = boxW * 35/100;
        String yearStr = displayYearOrTurn();
        g.setFont(narrowFont(40));
        int sw = g.getFontMetrics().stringWidth(yearStr);
        int x0 = boxX+((leftW-sw)/2);
        int ya = boxY+boxH1-s20-(transportH/2)-(targetOK? s20 : 0); // BR: adjusted for target (-s20)
        drawBorderedString(g, yearStr, 2, x0, ya, SystemPanel.textShadowC, SystemPanel.orangeText);

        if (bombarded) {
            String titleStr = text("MAIN_BOMBARD_COMPLETE");
            g.setFont(narrowFont(24));
            drawShadowedString(g, titleStr, 4, boxX+leftW, boxY+s30, SystemPanel.textShadowC, Color.white);
            String contStr = text("CLICK_CONTINUE");
            g.setColor(Color.black);
            g.setFont(narrowFont(14));
            drawString(g,contStr, boxX+leftW, boxY+s50);
            // click to continue sprite
            parent.addNextTurnControl(clickSprite);
        }
        else {
            String titleStr = text("MAIN_BOMBARD_TITLE", sysName);
            if(!fleet.empire().atWarWith(sys.empId()))
                titleStr = text("NEUTRAL_BOMBARD_TITLE", sysName);
            titleStr = sys.empire().replaceTokens(titleStr, "alien");
            int titleFontSize = scaledFont(g, titleStr, boxW-leftW, 20, 14);
            g.setFont(narrowFont(titleFontSize));
            drawString(g,titleStr, boxX+leftW, boxY+s25);

            if (transports > 0) {
                String subtitleStr = text("MAIN_BOMBARD_TROOPS", str(transports));
                subtitleStr = player().replaceTokens(subtitleStr, "alien");
                g.setColor(Color.black);
                int subtitleFontSize = min(titleFontSize-2, scaledFont(g, subtitleStr, boxW-leftW, 20, 14));
                g.setFont(narrowFont(subtitleFontSize));
                drawString(g,subtitleStr, boxX+leftW, boxY+s25+transportH);         
            }
            
            // calc width needed for yes/no buttons
            g.setFont(narrowFont(20));
            String yesStr;
            if (targetOK)
            	yesStr = text("MAIN_BOMBARD_DROP_ALL");
            else
            	yesStr = text("MAIN_BOMBARD_YES");
            String targetStr = text("MAIN_BOMBARD_TARGET", options().selectedBombingTarget());
            String noStr = text("MAIN_BOMBARD_NO");
            int swYes = g.getFontMetrics().stringWidth(yesStr);
            int swLim = g.getFontMetrics().stringWidth(targetStr);
            int swNo = g.getFontMetrics().stringWidth(noStr);
            // int buttonW = Math.max(swYes, swNo);
            int bwYes, bwNo, bwLim;
            if (targetOK) { // BR: adjusted for target
            	bwYes = swYes + s20;
            	bwNo  = bwYes;
            	bwLim = swLim + s20;
            } else {
            	bwYes = Math.max(swYes, swNo) + s20;
            	bwNo  = bwYes;
            	bwLim = bwYes;
            }

            // print prompt string
            String promptStr = text("MAIN_BOMBARD_PROMPT");
            int promptFontSize = scaledFont(g, promptStr, boxW-leftW-bwYes-bwNo-s30, 24, 20);
            g.setFont(narrowFont(promptFontSize));
            int swPrompt = g.getFontMetrics().stringWidth(promptStr);
            int promptY = boxY+s35+transportH;
            if(fleet.empire().atWarWith(sys.empId()))
                drawShadowedString(g, promptStr, 4, boxX+leftW, promptY+s20, SystemPanel.textShadowC, Color.white);
            else
                drawShadowedString(g, promptStr, 4, boxX+leftW, promptY+s20, SystemPanel.textShadowC, Color.red);

            // draw yes/no buttons
            g.setFont(narrowFont(20));
            int buttonY = promptY+buttonOffset; // BR: adjusted for target
            int buttonH = s30;
            // int x2 = boxX+leftW+swPrompt+s10;
            int xYes = boxX+leftW+ (targetOK? 0 : swPrompt+s10); // BR: adjusted for target
            int xLim = xYes+bwYes+s10;
            // int x4 = x3+buttonW+s10;
            int xNo = targetOK? xLim+bwLim+s10 : xLim; // BR: adjusted for target
            // yes button
            parent.addNextTurnControl(yesButton);
            yesButton.parent(this);
            yesButton.setBounds(xYes, buttonY, bwYes, buttonH);
            yesButton.draw(parent.map(), g);
            // BR: Target button
            if (targetOK) {
	            parent.addNextTurnControl(targetButton);
	            targetButton.parent(this);
	            targetButton.setBounds(xLim, buttonY, bwLim, buttonH);
	            targetButton.draw(parent.map(), g);
            }
            // no button
            parent.addNextTurnControl(noButton);
            noButton.parent(this);
            noButton.setBounds(xNo, buttonY, bwNo, buttonH);
            noButton.draw(parent.map(), g);
         }

        // draw top data line
        int y0a = boxY+boxH1+s20;
        int x0a = boxX+s10;

        int pad = s30;
        int p1 = BasePanel.s5;
        String dmgStr = text("MAIN_BOMBARD_DMG", "-99");
        String popStr = text("MAIN_BOMBARD_POPULATION", endPop);
        String factStr = text("MAIN_BOMBARD_FACTORIES", endFact);
        String baseStr = text("MAIN_BOMBARD_BASES", endBases);
        String shieldStr = text("MAIN_BOMBARD_SHIELD", shield);

        String allText = concat(popStr,dmgStr,factStr,dmgStr,baseStr,dmgStr,shieldStr);
        int fontSize1 = scaledFont(g, allText, boxW-s10-s10-(3*pad)-(3*p1), 20, 13);
        g.setFont(narrowFont(fontSize1));
        int allsw = g.getFontMetrics().stringWidth(allText);
        pad = (boxW-allsw-(3*p1)-s10-s10)/3;
        int dmgW = g.getFontMetrics().stringWidth(dmgStr)+p1;

        drawBorderedString(g, popStr, 1, x0a, y0a, Color.black, Color.white);
        x0a += g.getFontMetrics().stringWidth(popStr);
        if (endPop < pop) {
            dmgStr = text("MAIN_BOMBARD_DMG", str(endPop-pop));
            drawBorderedString(g, dmgStr, 1, x0a+p1, y0a, Color.black, Color.red);
        } else if(!bombarded)
        {
            dmgStr = text("MAIN_BOMBARD_DMG", -estKills);
            drawBorderedString(g, dmgStr, 1, x0a+p1, y0a, Color.black, Color.yellow);
        }
        x0a += dmgW;
        x0a += pad;

        drawBorderedString(g, factStr, 1, x0a, y0a, Color.black, Color.white);
        x0a += g.getFontMetrics().stringWidth(factStr);
        if (endFact < fact) {
            dmgStr = text("MAIN_BOMBARD_DMG", str(endFact-fact));
            drawBorderedString(g, dmgStr, 1, x0a+p1, y0a, Color.black, Color.red);
        } else if(!bombarded)
        {
            dmgStr = text("MAIN_BOMBARD_DMG", -estFactoryKills);
            drawBorderedString(g, dmgStr, 1, x0a+p1, y0a, Color.black, Color.yellow);
        }
        x0a += dmgW;
        x0a += pad;

        drawBorderedString(g, baseStr, 1, x0a, y0a, Color.black, Color.white);
        x0a += g.getFontMetrics().stringWidth(baseStr);
        if (endBases < bases) {
            dmgStr = text("MAIN_BOMBARD_DMG", str(endBases-bases));
            drawBorderedString(g, dmgStr, 1, x0a+p1, y0a, Color.black, Color.red);
        }
        x0a += dmgW;
        x0a += pad;

        drawBorderedString(g, shieldStr, 1, x0a, y0a, Color.black, Color.white);

        // draw planet info, from bottom up
        int x1 = boxX+s15;
        int y1 = boxY+boxH-s10;
        int lineH = s20;
        int desiredFont = 18;

        if (pl.sv.isUltraPoor(sys.id)) {
            g.setColor(SystemPanel.redText);
            String s1 = text("MAIN_SCOUT_ULTRA_POOR_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.sv.isPoor(sys.id)) {
            g.setColor(SystemPanel.redText);
            String s1 = text("MAIN_SCOUT_POOR_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.sv.isRich(sys.id)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_RICH_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.sv.isUltraRich(sys.id)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_ULTRA_RICH_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }

        if (pl.sv.isOrionArtifact(sys.id)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_ANCIENTS_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.sv.isArtifact(sys.id)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_ARTIFACTS_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }

        if (pl.isEnvironmentHostile(sys)) {
            g.setColor(SystemPanel.redText);
            String s1 = text("MAIN_SCOUT_HOSTILE_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.isEnvironmentFertile(sys)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_FERTILE_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else if (pl.isEnvironmentGaia(sys)) {
            g.setColor(SystemPanel.greenText);
            String s1 = text("MAIN_SCOUT_GAIA_DESC");
            int fontSize = scaledFont(g, s1, boxW-s25, desiredFont, 15);
            g.setFont(narrowFont(fontSize));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }

        // classification line
        if (sys.planet().type().isAsteroids()) {
            String s1 = text("MAIN_SCOUT_NO_PLANET");
            g.setFont(narrowFont(desiredFont+3));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }
        else {
            String s1 = text("MAIN_SCOUT_TYPE", text(sys.planet().type().key()), (int)sys.planet().maxSize());
            g.setFont(narrowFont(desiredFont+3));
            drawBorderedString(g, s1, 1, x1, y1, Color.black, Color.white);
            y1 -= lineH;
        }

        // planet name
        y1 -= scaled(5);
        g.setFont(narrowFont(32));
        drawBorderedString(g, sysName, 1, x1, y1, Color.darkGray, SystemPanel.orangeText);

        // planet flag
        parent.addNextTurnControl(flagButton);
        flagButton.init(this, g);
        flagButton.mapX(boxX+boxW-flagButton.width()+s10);
        flagButton.mapY(boxY+boxH-flagButton.height()+s10);
        flagButton.draw(parent.map(), g);

        if (sys.empire() == null) {
            g.setColor(destroyedMaskC);
            g.fillRect(boxX, boxY+boxH1, boxW, boxH-boxH1);
            String s = text("MAIN_BOMBARD_DESTROYED");
            int fontSize = scaledFont(g, s, boxW-s10, 50, 30);
            g.setFont(narrowFont(fontSize));
            sw = g.getFontMetrics().stringWidth(s);
            x0 = boxX+((boxW-sw)/2);
            int y0 = boxY+boxH1+scaled(fontSize+20);
            this.drawBorderedString(g, s, 2, x0, y0, Color.black, destroyedTextC);
        }
        parent.map().updateHover();  // BR: To remove the need of moving the mouse
    }
    @Override
    public boolean handleKeyPress(KeyEvent e) {
    	setModifierKeysState(e); // BR: For the Flag color selection
        boolean shift = e.isShiftDown();
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                if (bombarded)
                    advanceMap();
                else 
                    bombardCancel();
            case KeyEvent.VK_N:
                bombardCancel();
                break;
            case KeyEvent.VK_L:
            	if (e.isAltDown()) {
            		debugReloadLabels(parent);
            		break;
            	}
                bombardTarget();
                break;
            case KeyEvent.VK_Y:
                bombardYes();
                break;
            case KeyEvent.VK_F:
                toggleFlagColor(shift);
                break;
            default:
            	if (!shift) // BR to avoid noise when changing flag color
            		misClick();
                break;
        }
        return true;
    }
    class SystemFlagSprite extends MapSprite {
        private int mapX, mapY, buttonW, buttonH;
        private int selectX, selectY, selectW, selectH;

        private MapOverlayBombardPrompt parent;

        protected int mapX()      { return mapX; }
        protected int mapY()      { return mapY; }
        public void mapX(int i)   { selectX = mapX = i; }
        public void mapY(int i)   { selectY = mapY = i; }

        public int width()        { return buttonW; }
        public int height()       { return buttonH; }
        public void reset()       {  }

        public void init(MapOverlayBombardPrompt p, Graphics2D g)  {
            parent = p;
            buttonW = BasePanel.s70;
            buttonH = BasePanel.s70;
            selectW = buttonW;
            selectH = buttonH;
        }
        public void setSelectionBounds(int x, int y, int w, int h) {
            selectX = x;
            selectY = y;
            selectW = w;
            selectH = h;
        }
        @Override
        public boolean acceptDoubleClicks()         { return true; }
        @Override
        public boolean acceptWheel()                { return true; }
        @Override
        public boolean isSelectableAt(GalaxyMapPanel map, int x, int y) {
            hovering = x >= selectX
                        && x <= selectX+selectW
                        && y >= selectY
                        && y <= selectY+selectH;
            return hovering;
        }
        @Override
        public void draw(GalaxyMapPanel map, Graphics2D g) {
            if (!parent.drawSprites())
                return;
            StarSystem sys = parent.starSystem();
            Image flagImage = parent.parent.flagImage(sys);
            Image flagHaze = parent.parent.flagHaze(sys);
            g.drawImage(flagHaze, mapX, mapY, buttonW, buttonH, null);
            if (hovering) {
                Image flagHover = parent.parent.flagHover(sys);
                g.drawImage(flagHover, mapX, mapY, buttonW, buttonH, null);
            }
            g.drawImage(flagImage, mapX, mapY, buttonW, buttonH, null);
        }
        @Override
        public void click(GalaxyMapPanel map, int count, boolean rightClick, boolean click, boolean middleClick, MouseEvent e) {
        	// BR: if 3 buttons:
        	//   - Middle click = Reset
        	//   - Right click = Reverse
            if (middleClick)
            	parent.resetFlagColor();
            else if (rightClick)
            	if (has3Buttons())
            		parent.toggleFlagColor(true);
            	else
            		parent.resetFlagColor();
            else
            	parent.toggleFlagColor(false);
        };
        @Override
        public void wheel(GalaxyMapPanel map, int rotation, boolean click) {
            if (rotation < 0)
                parent.toggleFlagColor(true);
            else
                parent.toggleFlagColor(false);
        };
    }

    // ##### Console Tools
    @Override public void consoleEntry()				{ advanceMap(); }
    @Override public List<ConsoleOptions> getOptions()	{
		List<ConsoleOptions> options = new ArrayList<>();
        boolean targetOK = options().targetBombardAllowedForPlayer();
		if (bombarded)
			options.add(new ConsoleOptions(KeyEvent.VK_ESCAPE, "C", "Continue"));
		else {
			if (targetOK) {
				options.add(new ConsoleOptions(KeyEvent.VK_Y, "Y", "Yes, Drop all the bombs."));
				String targetStr = text("MAIN_BOMBARD_TARGET", options().selectedBombingTarget());
				options.add(new ConsoleOptions(KeyEvent.VK_L, "L", "Limited, " + targetStr));
			}
			else
				options.add(new ConsoleOptions(KeyEvent.VK_Y, "Y", "Yes, Drop the bombs."));
			options.add(new ConsoleOptions(KeyEvent.VK_N, "N", "No, do not bombard the planet."));
		}
		return options;
	}
	@Override public String getMessage()				{
        StarSystem sys = galaxy().system(sysId);
        Empire player = player();

        //boolean targetOK = options().targetBombardAllowedForPlayer();
        String sysName = player().sv.name(sys.id);

        // draw header info
        String message = displayYearOrTurn();
        message += IVIPConsole.SPACER + sysName;

        if (bombarded) {
        	message += IVIPConsole.SPACER + text("MAIN_BOMBARD_COMPLETE");
        }
        else {
	    	String titleStr = text("MAIN_BOMBARD_TITLE", sysName);
	        if(!fleet.empire().atWarWith(sys.empId()))
	            titleStr = text("NEUTRAL_BOMBARD_TITLE", sysName);
	        titleStr = sys.empire().replaceTokens(titleStr, "alien");
	        message += IVIPConsole.SPACER + titleStr;
	        if (transports > 0) {
	            String subtitleStr = text("MAIN_BOMBARD_TROOPS", str(transports));
	            subtitleStr = player().replaceTokens(subtitleStr, "alien");
	            message += subtitleStr;
	        }
        }
        // planet name
        message += NEWLINE + sysName;

        // draw planet info, from bottom up
        message += NEWLINE;
        if (player.sv.isUltraPoor(sys.id))
        	message += " " + text("MAIN_SCOUT_ULTRA_POOR_DESC");
        else if (player.sv.isPoor(sys.id))
        	message += " " + text("MAIN_SCOUT_POOR_DESC");
        else if (player.sv.isRich(sys.id))
        	message += " " + text("MAIN_SCOUT_RICH_DESC");
        else if (player.sv.isUltraRich(sys.id))
        	message += " " + text("MAIN_SCOUT_ULTRA_RICH_DESC");

        if (player.sv.isOrionArtifact(sys.id))
        	message += " " + text("MAIN_SCOUT_ANCIENTS_DESC");
        else if (player.sv.isArtifact(sys.id))
        	message += " " + text("MAIN_SCOUT_ARTIFACTS_DESC");

        if (player.isEnvironmentHostile(sys)) 
        	message += " " + text("MAIN_SCOUT_HOSTILE_DESC");
        else if (player.isEnvironmentFertile(sys))
        	message += " " + text("MAIN_SCOUT_FERTILE_DESC");
        else if (player.isEnvironmentGaia(sys))
        	message += " " + text("MAIN_SCOUT_GAIA_DESC");

        // classification line
        if (sys.planet().type().isAsteroids())
        	message += " " + text("MAIN_SCOUT_NO_PLANET");
        else
        	message += " " + text("MAIN_SCOUT_TYPE", text(sys.planet().type().key()), (int)sys.planet().maxSize());

    	// draw top data line
        String popStr = text("MAIN_BOMBARD_POPULATION", endPop);
        String factStr = text("MAIN_BOMBARD_FACTORIES", endFact);
        String baseStr = text("MAIN_BOMBARD_BASES", endBases);
        String shieldStr = text("MAIN_BOMBARD_SHIELD", shield);

        message += NEWLINE + popStr;
        if (endPop < pop)
        	 message += " Casualties " + text("MAIN_BOMBARD_DMG", str(pop-endPop));
        else if (!bombarded)
        	 message += " Estimated kill " + text("MAIN_BOMBARD_DMG", estKills);

        message += NEWLINE + factStr + " ";
        if (endFact < fact)
        	message += " Damage " + text("MAIN_BOMBARD_DMG", str(fact-endFact));
        else if(!bombarded)
        	message += " Estimated Damage " + text("MAIN_BOMBARD_DMG", estFactoryKills);

        message += NEWLINE + baseStr + " ";
        if (endBases < bases)
        	message += " Damage " + text("MAIN_BOMBARD_DMG", str(bases-endBases));
        message += NEWLINE + shieldStr + " ";

        // print prompt string
    	if (sys.empire() == null)
            message += NEWLINE + text("MAIN_BOMBARD_DESTROYED");

        if (bombarded) {
        	if (sys.empire() == null)
                message += NEWLINE + text("MAIN_BOMBARD_DESTROYED");
        	message += NEWLINE + IVIPConsole.PRESS_ANY_KEY;
        }
        else {
            String promptStr = text("MAIN_BOMBARD_PROMPT");
            if(fleet.empire().atWarWith(sys.empId()))
            	message += NEWLINE + "We are at war with this empire";
            else
            	message += NEWLINE + "We are not at war with this empire";
        	message += NEWLINE + promptStr;
        	message += NEWLINE + getMessageOption();
        }
 
		return message;
	}
}
