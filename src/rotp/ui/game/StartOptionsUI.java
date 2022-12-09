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

import static rotp.model.game.MOO1GameOptions.loadAndUpdateFromFileName;
import static rotp.model.game.MOO1GameOptions.saveOptionsToFileName;
import static rotp.model.game.MOO1GameOptions.setBaseAndModSettingsToDefault;
import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.GAME_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LAST_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;
import static rotp.ui.UserPreferences.USER_OPTIONS_FILE;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonKey;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonWidth;
import static rotp.ui.util.AbstractOptionsUI.lastButtonKey;
import static rotp.ui.util.AbstractOptionsUI.okButtonKey;
import static rotp.ui.util.AbstractOptionsUI.userButtonKey;
import static rotp.ui.util.AbstractOptionsUI.userButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.SwingUtilities;

import rotp.mod.br.profiles.Profiles;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.Modifier2KeysState;

public class StartOptionsUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private static final long serialVersionUID	= 1L;
    private static final Color backgroundHaze	= new Color(0,0,0,160);
	private static final String guiTitleID		= "SETTINGS_TITLE";
	public  static final String GUI_ID          = "ADVANCED_OPTIONS";
    
//    public static final Color lightBrown = new Color(178,124,87);
//    public static final Color brown = new Color(141,101,76);
//    public static final Color darkBrown = new Color(112,85,68);
//    public static final Color darkerBrown = new Color(75,55,39);
    
    private Rectangle hoverBox;
    private Rectangle okBox		 = new Rectangle();
    private Rectangle defaultBox = new Rectangle();
	private Rectangle lastBox	 = new Rectangle();
    private Rectangle userBox	 = new Rectangle();
    private BasePanel parent;
    private BaseText galaxyAgeText;
    private BaseText starDensityText;
    private BaseText nebulaeText;
    private BaseText randomEventsText;
    private BaseText planetQualityText;
    private  BaseText terraformingText;
    private BaseText colonizingText;
    private BaseText councilWinText;
    private BaseText randomizeAIText;
    private BaseText autoplayText;
    private BaseText researchRateText;
    private BaseText warpSpeedText;
    private  BaseText fuelRangeText;
    private BaseText techTradingText;
    private BaseText aiHostilityText;
    
    public StartOptionsUI() {
        init0();
    }
    private void init0() {
        setOpaque(false);
        Color textC = SystemPanel.whiteText;
        galaxyAgeText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        starDensityText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        nebulaeText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        randomEventsText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        planetQualityText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        terraformingText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        colonizingText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        councilWinText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        randomizeAIText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        autoplayText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        researchRateText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        warpSpeedText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        fuelRangeText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        techTradingText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        aiHostilityText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }
    private void init() {
        galaxyAgeText.displayText(galaxyAgeStr());
        starDensityText.displayText(starDensityStr());
        nebulaeText.displayText(nebulaeStr());
        randomEventsText.displayText(randomEventsStr());
        planetQualityText.displayText(planetQualityStr());
        terraformingText.displayText(terraformingStr());
        colonizingText.displayText(colonizingStr());
        councilWinText.displayText(councilWinStr());
        randomizeAIText.displayText(randomizeAIStr());
        autoplayText.displayText(autoplayStr());
        fuelRangeText.displayText(fuelRangeStr());
        researchRateText.displayText(researchRateStr());
        techTradingText.displayText(techTradingStr());
        warpSpeedText.displayText(warpSpeedStr());
        aiHostilityText.displayText(aiHostilityStr());
    }
    void open(BasePanel p) {
        parent = p;
		Modifier2KeysState.reset();
		saveOptionsToFileName(guiOptions(), LIVE_OPTIONS_FILE);
        init();
        enableGlassPane(this);
    }
    private void close() {
		Modifier2KeysState.reset();
        disableGlassPane();
		newGameOptions().galaxyShape().quickGenerate(); // BR: to avoid strange galaxy display
    }
//    private void copyOptions(MOO1GameOptions src, MOO1GameOptions dest) {
//    	MOO1GameOptions.copyAdvancedOptions(src, dest);
//    }
//	private void writeOptions(MOO1GameOptions destination) {
//		copyOptions(guiOptions(), destination);
//	}
//	private void readOptions(MOO1GameOptions source) {
//		copyOptions(source, guiOptions());
//	}
    private void doOkBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break;
		default: // Save
			updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break; 
		}
		close();
	}
	private void doDefaultBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // cancelKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);		
			break;
		case SHIFT: // setLocalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), GUI_ID);		
			break; 
		default: // setGlobalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), ALL_GUI_ID);		
			break; 
		}
		init();
		repaint();
	}
	private void doUserBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL: // saveGlobalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			return;
		case CTRL_SHIFT: // saveLocalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID);
			return;
		case SHIFT: // setLocalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID);
			init();
			repaint();
			return;
		default: // setGlobalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			init();
			repaint();
		}
	}
	private void doLastBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL: // setGlobalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, ALL_GUI_ID);
			break;
		case CTRL_SHIFT: // setLocalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, GUI_ID);
			break;
		case SHIFT: // setLocalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, GUI_ID);
			break;
		default: // setGlobalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, ALL_GUI_ID);
		}
		init();
		repaint();
	}
//	private void saveUserOptions() {
//		MOO1GameOptions fileOptions = MOO1GameOptions.loadUserOptions();
//		writeOptions(fileOptions);
//		MOO1GameOptions.updateOptionsAndSaveToUserOptions(fileOptions);
//	}
	private void checkModifierKey(InputEvent e) {
		if (Modifier2KeysState.checkForChange(e)) {
			repaint();
		}
	}
    @Override
    public void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        
        int w = getWidth();
        int h = getHeight();
        Graphics2D g = (Graphics2D) g0;
        
        
        // draw background "haze"
        g.setColor(backgroundHaze);
        g.fillRect(0, 0, w, h);
        
        int numColumns = 3;
        int columnPad = s20;
        int lineH = s17;
        Font descFont = narrowFont(15);
        int leftM = s100;
        int rightM = s100;
        int topM = s45;
        int w1 = w-leftM-rightM;
        int h1 = h-topM-s45;
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(leftM, topM, w1, h1);
        String title = text(guiTitleID);
        g.setFont(narrowFont(30));
        int sw = g.getFontMetrics().stringWidth(title);
        int x1 = leftM+((w1-sw)/numColumns);
        int y1 = topM+s40;
        drawBorderedString(g, title, 1, x1, y1, Color.black, Color.white);
        
        g.setFont(narrowFont(18));
        String expl = text("SETTINGS_DESCRIPTION");
        g.setColor(SystemPanel.blackText);
        drawString(g,expl, leftM+s10, y1+s20);
        
        Stroke prev = g.getStroke();
        g.setStroke(stroke3);

        
        // left column
        int y2 = topM+scaled(110);
        int x2 = leftM+s10;
        int w2 = (w1/numColumns)-columnPad;
        int h2 = s90;
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, galaxyAgeText.stringWidth(g)+s10,s30);
        galaxyAgeText.setScaledXY(x2+s20, y2+s7);
        galaxyAgeText.draw(g);
        String desc = text("SETTINGS_GALAXY_AGE_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        List<String> lines = this.wrappedLines(g,desc, w2-s30);
        int y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, starDensityText.stringWidth(g)+s10,s30);
        starDensityText.setScaledXY(x2+s20, y2+s7);
        starDensityText.draw(g);
        desc = text("SETTINGS_STAR_DENSITY_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }       
       
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, nebulaeText.stringWidth(g)+s10,s30);
        nebulaeText.setScaledXY(x2+s20, y2+s7);
        nebulaeText.draw(g);
        desc = text("SETTINGS_NEBULAE_DESC");
         g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, planetQualityText.stringWidth(g)+s10,s30);
        planetQualityText.setScaledXY(x2+s20, y2+s7);
        planetQualityText.draw(g);
        desc = text("SETTINGS_PLANET_QUALITY_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, terraformingText.stringWidth(g)+s10,s30);
        terraformingText.setScaledXY(x2+s20, y2+s7);
        terraformingText.draw(g);
        desc = text("SETTINGS_TERRAFORMING_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        // middle column
        y2 = topM+scaled(110);
        x2 = x2+w2+s20;
        h2 = s90;
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, randomEventsText.stringWidth(g)+s10,s30);
        randomEventsText.setScaledXY(x2+s20, y2+s7);
        randomEventsText.draw(g);
        desc = text("SETTINGS_RANDOM_EVENTS_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, aiHostilityText.stringWidth(g)+s10,s30);
        aiHostilityText.setScaledXY(x2+s20, y2+s7);
        aiHostilityText.draw(g);
        desc = text("SETTINGS_AI_HOSTILITY_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }       
       

        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, councilWinText.stringWidth(g)+s30,s30);
        councilWinText.setScaledXY(x2+s20, y2+s7);
        councilWinText.draw(g);
        desc = text("SETTINGS_COUNCIL_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
 
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, randomizeAIText.stringWidth(g)+s30,s30);
        randomizeAIText.setScaledXY(x2+s20, y2+s7);
        randomizeAIText.draw(g);
        desc = text("SETTINGS_RANDOMIZE_AI_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, autoplayText.stringWidth(g)+s30,s30);
        autoplayText.setScaledXY(x2+s20, y2+s7);
        autoplayText.draw(g);
        desc = text("SETTINGS_AUTOPLAY_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        // right side
        y2 = topM+scaled(110);
        h2 = s90;
        x2 = x2+w2+s20;
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, researchRateText.stringWidth(g)+s10,s30);
        researchRateText.setScaledXY(x2+s20, y2+s7);
        researchRateText.draw(g);
        desc = text("SETTINGS_RESEARCH_RATE_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
        
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, warpSpeedText.stringWidth(g)+s10,s30);
        warpSpeedText.setScaledXY(x2+s20, y2+s7);
        warpSpeedText.draw(g);
        desc = text("SETTINGS_WARP_SPEED_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }
          
        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, fuelRangeText.stringWidth(g)+s10,s30);
        fuelRangeText.setScaledXY(x2+s20, y2+s7);
        fuelRangeText.draw(g);
        desc = text("SETTINGS_FUEL_RANGE_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }

        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, techTradingText.stringWidth(g)+s10,s30);
        techTradingText.setScaledXY(x2+s20, y2+s7);
        techTradingText.draw(g);
        desc = text("SETTINGS_TECH_TRADING_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }


        y2 += (h2+s20);
        g.setColor(SystemPanel.blackText);
        g.drawRect(x2, y2, w2, h2);
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(x2+s10, y2-s10, colonizingText.stringWidth(g)+s10,s30);
        colonizingText.setScaledXY(x2+s20, y2+s7);
        colonizingText.draw(g);
        desc = text("SETTINGS_COLONIZING_DESC");
        g.setColor(SystemPanel.blackText);
        g.setFont(descFont);
        lines = this.wrappedLines(g,desc, w2-s30);
        y3 = y2+s10;
        for (String line: lines) {
            y3 += lineH;
            drawString(g,line, x2+s20, y3);
        }

        g.setStroke(prev);

        // draw settings button
        int y4 = scaled(690);
        int cnr = s5;
        int smallButtonH = s30;
        int smallButtonW = scaled(180);
        okBox.setBounds(w-scaled(289), y4, smallButtonW, smallButtonH);
        g.setColor(GameUI.buttonBackgroundColor());
        g.fillRoundRect(okBox.x, okBox.y, smallButtonW, smallButtonH, cnr, cnr);
        g.setFont(narrowFont(20));
        String text = text(okButtonKey());
        sw = g.getFontMetrics().stringWidth(text);
        int x = okBox.x+((okBox.width-sw)/2);
        int y = okBox.y+okBox.height-s8;
        Color c = hoverBox == okBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(okBox.x, okBox.y, okBox.width, okBox.height, cnr, cnr);
        g.setStroke(prev);

		text = text(defaultButtonKey());
        sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = defaultButtonWidth(g);
        defaultBox.setBounds(okBox.x-smallButtonW-s30, y4, smallButtonW, smallButtonH);
        g.setColor(GameUI.buttonBackgroundColor());
        g.fillRoundRect(defaultBox.x, defaultBox.y, smallButtonW, smallButtonH, cnr, cnr);
        g.setFont(narrowFont(20));
        x = defaultBox.x+((defaultBox.width-sw)/2);
        y = defaultBox.y+defaultBox.height-s8;
        c = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
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

		text = text(userButtonKey());
        sw 	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = userButtonWidth(g);
		userBox.setBounds(defaultBox.x-smallButtonW-s30, y4, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
    }
    private String galaxyAgeStr() {
        String opt = text(newGameOptions().selectedGalaxyAge());
        return text("SETTINGS_GALAXY_AGE", opt)+"   ";
    }
    private String starDensityStr() {
        String opt = text(newGameOptions().selectedStarDensityOption());
        return text("SETTINGS_STAR_DENSITY", opt)+"   ";
    }
    private String nebulaeStr() {
        String opt = text(newGameOptions().selectedNebulaeOption());
        return text("SETTINGS_NEBULAE", opt)+"   ";
    }
    private String randomEventsStr() {
        String opt = text(newGameOptions().selectedRandomEventOption());
        return text("SETTINGS_RANDOM_EVENTS", opt)+"   ";
    }
    private String planetQualityStr() {
        String opt = text(newGameOptions().selectedPlanetQualityOption());
        return text("SETTINGS_PLANET_QUALITY", opt)+"   ";
    }
    private String terraformingStr() {
        String opt = text(newGameOptions().selectedTerraformingOption());
        return text("SETTINGS_TERRAFORMING", opt)+"   ";
    }
    private String colonizingStr() {
        String opt = text(newGameOptions().selectedColonizingOption());
        return text("SETTINGS_COLONIZING", opt)+"   ";
    }
    private String councilWinStr() {
        String opt = text(newGameOptions().selectedCouncilWinOption());
        return text("SETTINGS_COUNCIL_WIN", opt)+"   ";
    }
    private String randomizeAIStr() {
        String opt = text(newGameOptions().selectedRandomizeAIOption());
        return text("SETTINGS_RANDOMIZE_AI", opt)+" ";
    }
    private String autoplayStr() {
        String opt = text(newGameOptions().selectedAutoplayOption());
        return text("SETTINGS_AUTOPLAY", opt)+" ";
    }
    private String researchRateStr() {
        String opt = text(newGameOptions().selectedResearchRate());
        return text("SETTINGS_RESEARCH_RATE", opt)+"   ";
    }
    private String warpSpeedStr() {
        String opt = text(newGameOptions().selectedWarpSpeedOption());
        return text("SETTINGS_WARP_SPEED", opt)+"   ";
    }
    private String aiHostilityStr() {
        String opt = text(newGameOptions().selectedAIHostilityOption());
        return text("SETTINGS_AI_HOSTILITY", opt)+"   ";
    }
    private String fuelRangeStr() {
        String opt = text(newGameOptions().selectedFuelRangeOption());
        return text("SETTINGS_FUEL_RANGE", opt)+"   ";
    }
    private String techTradingStr() {
        String opt = text(newGameOptions().selectedTechTradeOption());
        return text("SETTINGS_TECH_TRADING", opt)+"   ";
    }
    private void toggleGalaxyAge(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedGalaxyAge(newGameOptions().nextGalaxyAge(up));
        galaxyAgeText.repaint(galaxyAgeStr());
    }
    private void toggleStarDensity(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedStarDensityOption(newGameOptions().nextStarDensityOption(up));
        starDensityText.repaint(starDensityStr());
    }
    private void toggleAIHostility(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedAIHostilityOption(newGameOptions().nextAIHostilityOption(up));
        aiHostilityText.repaint(aiHostilityStr());
    }
    private void toggleNebulae(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedNebulaeOption(newGameOptions().nextNebulaeOption(up));
        nebulaeText.repaint(nebulaeStr());
    }
    private void toggleRandomEvents(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedRandomEventOption(newGameOptions().nextRandomEventOption(up));
        randomEventsText.repaint(randomEventsStr());
    }
    private void togglePlanetQuality(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedPlanetQualityOption(newGameOptions().nextPlanetQualityOption(up));
        planetQualityText.repaint(planetQualityStr());
    }
    private void toggleTerraforming(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedTerraformingOption(newGameOptions().nextTerraformingOption(up));
        terraformingText.repaint(terraformingStr());
    }
    private void toggleColonizing(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedColonizingOption(newGameOptions().nextColonizingOption(up));
        colonizingText.repaint(colonizingStr());
    }
    private void toggleCouncilWin(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedCouncilWinOption(newGameOptions().nextCouncilWinOption(up));
        councilWinText.repaint(councilWinStr());
    }
    private void toggleRandomizeAI(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedRandomizeAIOption(newGameOptions().nextRandomizeAIOption(up));
        randomizeAIText.repaint(randomizeAIStr());
    }
    private void toggleAutoplay(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedAutoplayOption(newGameOptions().nextAutoplayOption(up));
        autoplayText.repaint(autoplayStr());
    }
    private void toggleResearchRate(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedResearchRate(newGameOptions().nextResearchRate(up));
        researchRateText.repaint(researchRateStr());
    }
    private void toggleWarpSpeed(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedWarpSpeedOption(newGameOptions().nextWarpSpeedOption(up));
        warpSpeedText.repaint(warpSpeedStr());
    }
    private void toggleFuelRange(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedFuelRangeOption(newGameOptions().nextFuelRangeOption(up));
        fuelRangeText.repaint(fuelRangeStr());
    }
    private void toggleTechTrading(boolean up) { // BR: added bidirectional
        softClick();
        newGameOptions().selectedTechTradeOption(newGameOptions().nextTechTradeOption(up));
        techTradingText.repaint(techTradingStr());
    } // \ BR:

    // BR:
    private void goToMainMenu() {
        buttonClick();
        doOkBoxAction();
        rotp.ui.RotPUI.instance().selectGamePanel();
    } // \ BR:
	@Override public void keyReleased(KeyEvent e) {
		checkModifierKey(e);		
	}
    @Override
    public void keyPressed(KeyEvent e) {
		checkModifierKey(e);		
        int k = e.getKeyCode(); // BR:
        switch(k) {
            case KeyEvent.VK_ESCAPE:
            	doOkBoxAction();
				return;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                parent.advanceHelp();
                break;
            case KeyEvent.VK_M: // BR: "M" = Go to Main Menu
                goToMainMenu();
                break;
            default: // BR:
            	if (Profiles.processKey(k, e.isShiftDown(), "Advanced", newGameOptions())) {
                    buttonClick();
                    galaxyAgeText.repaint(galaxyAgeStr());
                    starDensityText.repaint(starDensityStr());
                    aiHostilityText.repaint(aiHostilityStr());
                    nebulaeText.repaint(nebulaeStr());
                    randomEventsText.repaint(randomEventsStr());
                    planetQualityText.repaint(planetQualityStr());
                    terraformingText.repaint(terraformingStr());
                    colonizingText.repaint(colonizingStr());
                    councilWinText.repaint(councilWinStr());
                    randomizeAIText.repaint(randomizeAIStr());
                    autoplayText.repaint(autoplayStr());
                    researchRateText.repaint(researchRateStr());
                    warpSpeedText.repaint(warpSpeedStr());
                    fuelRangeText.repaint(fuelRangeStr());
                    techTradingText.repaint(techTradingStr());           		
            	}
            	// Needs to be done twice for the case both Galaxy size
            	// and the number of opponents were changed !?
            	if (Profiles.processKey(k, e.isShiftDown(), "Advanced", newGameOptions())) {
                    galaxyAgeText.repaint(galaxyAgeStr());
                    starDensityText.repaint(starDensityStr());
                    aiHostilityText.repaint(aiHostilityStr());
                    nebulaeText.repaint(nebulaeStr());
                    randomEventsText.repaint(randomEventsStr());
                    planetQualityText.repaint(planetQualityStr());
                    terraformingText.repaint(terraformingStr());
                    colonizingText.repaint(colonizingStr());
                    councilWinText.repaint(councilWinStr());
                    randomizeAIText.repaint(randomizeAIStr());
                    autoplayText.repaint(autoplayStr());
                    researchRateText.repaint(researchRateStr());
                    warpSpeedText.repaint(warpSpeedStr());
                    fuelRangeText.repaint(fuelRangeStr());
                    techTradingText.repaint(techTradingStr());           		
            	}
            return;
       }
    }
    @Override
    public void mouseDragged(MouseEvent e) {  }
    @Override
    public void mouseMoved(MouseEvent e) {
		checkModifierKey(e);		
        int x = e.getX();
        int y = e.getY();
        Rectangle prevHover = hoverBox;
        hoverBox = null;
        if (galaxyAgeText.contains(x,y))
            hoverBox = galaxyAgeText.bounds();
        else if (starDensityText.contains(x,y))
            hoverBox = starDensityText.bounds();
        else if (nebulaeText.contains(x,y))
            hoverBox = nebulaeText.bounds();
        else if (planetQualityText.contains(x,y))
            hoverBox = planetQualityText.bounds();
        else if (terraformingText.contains(x,y))
            hoverBox = terraformingText.bounds();
        else if (colonizingText.contains(x,y))
            hoverBox = colonizingText.bounds();
        else if (randomEventsText.contains(x,y))
            hoverBox = randomEventsText.bounds();
        else if (councilWinText.contains(x,y))
            hoverBox = councilWinText.bounds();
        else if (aiHostilityText.contains(x,y))
            hoverBox = aiHostilityText.bounds();
        else if (randomizeAIText.contains(x,y))
            hoverBox = randomizeAIText.bounds();
        else if (autoplayText.contains(x,y))
            hoverBox = autoplayText.bounds();
        else if (researchRateText.contains(x,y))
            hoverBox = researchRateText.bounds();
        else if (warpSpeedText.contains(x,y))
            hoverBox = warpSpeedText.bounds();
        else if (fuelRangeText.contains(x,y))
            hoverBox = fuelRangeText.bounds();
        else if (techTradingText.contains(x,y))
            hoverBox = techTradingText.bounds();
        else if (okBox.contains(x,y))
            hoverBox = okBox;
        else if (defaultBox.contains(x,y))
            hoverBox = defaultBox;
        else if (userBox.contains(x,y))
            hoverBox = userBox;
        else if (lastBox.contains(x,y))
            hoverBox = lastBox;
		
        if (hoverBox != prevHover) {
            if (prevHover == galaxyAgeText.bounds())
                galaxyAgeText.mouseExit();
            else if (prevHover == starDensityText.bounds())
                starDensityText.mouseExit();
            else if (prevHover == nebulaeText.bounds())
                nebulaeText.mouseExit();
            else if (prevHover == planetQualityText.bounds())
                planetQualityText.mouseExit();
            else if (prevHover == terraformingText.bounds())
                terraformingText.mouseExit();
            else if (prevHover == colonizingText.bounds())
                colonizingText.mouseExit();
            else if (prevHover == randomEventsText.bounds())
                randomEventsText.mouseExit();
            else if (prevHover == aiHostilityText.bounds())
                aiHostilityText.mouseExit();
            else if (prevHover == councilWinText.bounds())
                councilWinText.mouseExit();
            else if (prevHover == randomizeAIText.bounds())
                randomizeAIText.mouseExit();
            else if (prevHover == autoplayText.bounds())
                autoplayText.mouseExit();
            else if (prevHover == researchRateText.bounds())
                researchRateText.mouseExit();
            else if (prevHover == warpSpeedText.bounds())
                warpSpeedText.mouseExit();
            else if (prevHover == fuelRangeText.bounds())
                fuelRangeText.mouseExit();
            else if (prevHover == techTradingText.bounds())
                techTradingText.mouseExit();
            if (hoverBox == galaxyAgeText.bounds())
                galaxyAgeText.mouseEnter();
            else if (hoverBox == starDensityText.bounds())
                starDensityText.mouseEnter();
            else if (hoverBox == nebulaeText.bounds())
                nebulaeText.mouseEnter();
            else if (hoverBox == planetQualityText.bounds())
                planetQualityText.mouseEnter();
            else if (hoverBox == terraformingText.bounds())
                terraformingText.mouseEnter();
            else if (hoverBox == colonizingText.bounds())
                colonizingText.mouseEnter();
            else if (hoverBox == randomEventsText.bounds())
                randomEventsText.mouseEnter();
            else if (hoverBox == aiHostilityText.bounds())
                aiHostilityText.mouseEnter();
            else if (hoverBox == councilWinText.bounds())
                councilWinText.mouseEnter();
            else if (hoverBox == randomizeAIText.bounds())
                randomizeAIText.mouseEnter();
            else if (hoverBox == autoplayText.bounds())
                autoplayText.mouseEnter();
            else if (hoverBox == researchRateText.bounds())
                researchRateText.mouseEnter();
            else if (hoverBox == warpSpeedText.bounds())
                warpSpeedText.mouseEnter();
            else if (hoverBox == fuelRangeText.bounds())
                fuelRangeText.mouseEnter();
            else if (hoverBox == techTradingText.bounds())
                techTradingText.mouseEnter();
            if (prevHover != null)
                repaint(prevHover);
            if (hoverBox != null)
                repaint(hoverBox);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { // BR: added bidirectional
        if (e.getButton() > 3)
            return;
        if (hoverBox == null)
            return;
        boolean up = !SwingUtilities.isRightMouseButton(e);
        mouseCommon(up);
        if (hoverBox == okBox)
        	doOkBoxAction(); // BR: added Ctrl function
        else if (hoverBox == defaultBox)
        	doDefaultBoxAction();
        else if (hoverBox == userBox)
			doUserBoxAction();
        else if (hoverBox == lastBox)
			doLastBoxAction();
    }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) {
        if (hoverBox != null) {
            hoverBox = null;
            repaint();
        }
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) { // BR: 
        boolean up = e.getWheelRotation() < 0;
        mouseCommon(up);
    }
    private void mouseCommon(boolean up) { // BR:
        if (hoverBox == galaxyAgeText.bounds())
            toggleGalaxyAge(up);
        else if (hoverBox == starDensityText.bounds())
            toggleStarDensity(up);
        else if (hoverBox == nebulaeText.bounds())
            toggleNebulae(up);
        else if (hoverBox == planetQualityText.bounds())
            togglePlanetQuality(up);
        else if (hoverBox == terraformingText.bounds())
            toggleTerraforming(up);
        else if (hoverBox == colonizingText.bounds())
            toggleColonizing(up);
        else if (hoverBox == randomEventsText.bounds())
            toggleRandomEvents(up);
        else if (hoverBox == aiHostilityText.bounds())
            toggleAIHostility(up);
        else if (hoverBox == councilWinText.bounds())
            toggleCouncilWin(up);
        else if (hoverBox == randomizeAIText.bounds())
            toggleRandomizeAI(up);
        else if (hoverBox == autoplayText.bounds())
            toggleAutoplay(up);
        else if (hoverBox == researchRateText.bounds())
            toggleResearchRate(up);
        else if (hoverBox == warpSpeedText.bounds())
            toggleWarpSpeed(up);
        else if (hoverBox == fuelRangeText.bounds())
            toggleFuelRange(up);
        else if (hoverBox == techTradingText.bounds())
            toggleTechTrading(up);
    }
    	 
}
