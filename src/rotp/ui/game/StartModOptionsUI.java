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
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class StartModOptionsUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private static final long serialVersionUID = 1L;
    private static final Color backgroundHaze = new Color(0,0,0,160);
    
    public static final Color lightBrown = new Color(178,124,87);
    public static final Color brown = new Color(141,101,76);
    public static final Color darkBrown = new Color(112,85,68);
    public static final Color darkerBrown = new Color(75,55,39);
    
    Rectangle hoverBox;
    Rectangle okBox = new Rectangle();
    Rectangle defaultBox = new Rectangle();
    BasePanel parent;
    BaseText alwaysStarGatesText;
    BaseText alwaysThoriumText;
    BaseText challengeModeText;
    BaseText battleScoutText;
    BaseText companionWorldsText;
    BaseText randomTechStartText;
    BaseText customDifficultyText;
    BaseText dynamicDifficultyText;
    BaseText missileSizeModifierText;
    BaseText retreatRestrictionsText;
    BaseText retreatRestrictionTurnsText;
    
    public StartModOptionsUI() {
        init0();
    }
    private void init0() {
        setOpaque(false);
        Color textC = SystemPanel.whiteText;
        alwaysStarGatesText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        alwaysThoriumText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        challengeModeText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        battleScoutText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        companionWorldsText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        randomTechStartText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        customDifficultyText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        dynamicDifficultyText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        missileSizeModifierText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        retreatRestrictionsText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        retreatRestrictionTurnsText = new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }
    public void init() {
        alwaysStarGatesText.displayText(alwaysStarGatesStr());
        alwaysThoriumText.displayText(alwaysThoriumStr());
        challengeModeText.displayText(challengeModeStr());
        battleScoutText.displayText(battleScoutStr());
        companionWorldsText.displayText(companionWorldsStr());
        randomTechStartText.displayText(randomTechStartStr());
        customDifficultyText.displayText(customDifficultyStr());
        dynamicDifficultyText.displayText(dynamicDifficultyStr());
        missileSizeModifierText.displayText(missileSizeModifierStr());
        retreatRestrictionsText.displayText(retreatRestrictionsStr());
        retreatRestrictionTurnsText.displayText(retreatRestrictionTurnsStr());
    }
    public void open(BasePanel p) {
        parent = p;
        init();
        enableGlassPane(this);
    }
    public void close() {
        disableGlassPane();
    }
    public void setToDefault() {
        UserPreferences.setModToDefault();
        init();
        repaint();
    }
    private static String yesOrNo(boolean b) {
        return b ? "YES" : "NO";
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
        int h1 = h-topM-s45-s100; // modnar: adjust panel vertical extent
        g.setPaint(GameUI.settingsSetupBackground(w));
        g.fillRect(leftM, topM, w1, h1);
        String title = text("SETTINGS_MOD_TITLE");
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
        g.fillRect(x2+s10, y2-s10, alwaysStarGatesText.stringWidth(g)+s10,s30);
        alwaysStarGatesText.setScaledXY(x2+s20, y2+s7);
        alwaysStarGatesText.draw(g);
        String desc = text("SETTINGS_MOD_ALWAYS_STARGATES_DESC");
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
        g.fillRect(x2+s10, y2-s10, alwaysThoriumText.stringWidth(g)+s10,s30);
        alwaysThoriumText.setScaledXY(x2+s20, y2+s7);
        alwaysThoriumText.draw(g);
        desc = text("SETTINGS_MOD_ALWAYS_THORIUM_DESC");
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
        g.fillRect(x2+s10, y2-s10, challengeModeText.stringWidth(g)+s10,s30);
        challengeModeText.setScaledXY(x2+s20, y2+s7);
        challengeModeText.draw(g);
        desc = text("SETTINGS_MOD_CHALLENGE_MODE_DESC");
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
        g.fillRect(x2+s10, y2-s10, retreatRestrictionsText.stringWidth(g)+s10,s30);
        retreatRestrictionsText.setScaledXY(x2+s20, y2+s7);
        retreatRestrictionsText.draw(g);
        desc = text("SETTINGS_MOD_RETREAT_RESTRICTIONS_DESC");
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
        g.fillRect(x2+s10, y2-s10, battleScoutText.stringWidth(g)+s10,s30);
        battleScoutText.setScaledXY(x2+s20, y2+s7);
        battleScoutText.draw(g);
        desc = text("SETTINGS_MOD_BATTLE_SCOUT_DESC");
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
        g.fillRect(x2+s10, y2-s10, companionWorldsText.stringWidth(g)+s10,s30);
        companionWorldsText.setScaledXY(x2+s20, y2+s7);
        companionWorldsText.draw(g);
        desc = text("SETTINGS_MOD_COMPANION_WORLDS_DESC");
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
        g.fillRect(x2+s10, y2-s10, randomTechStartText.stringWidth(g)+s30,s30);
        randomTechStartText.setScaledXY(x2+s20, y2+s7);
        randomTechStartText.draw(g);
        desc = text("SETTINGS_MOD_RANDOM_TECH_START_DESC");
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
        g.fillRect(x2+s10, y2-s10, retreatRestrictionTurnsText.stringWidth(g)+s30,s30);
        retreatRestrictionTurnsText.setScaledXY(x2+s20, y2+s7);
        retreatRestrictionTurnsText.draw(g);
        desc = text("SETTINGS_MOD_RETREAT_RESTRICTION_TURNS_DESC");
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
        g.fillRect(x2+s10, y2-s10, customDifficultyText.stringWidth(g)+s10,s30);
        customDifficultyText.setScaledXY(x2+s20, y2+s7);
        customDifficultyText.draw(g);
        desc = text("SETTINGS_MOD_CUSTOM_DIFFICULTY_DESC");
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
        g.fillRect(x2+s10, y2-s10, dynamicDifficultyText.stringWidth(g)+s10,s30);
        dynamicDifficultyText.setScaledXY(x2+s20, y2+s7);
        dynamicDifficultyText.draw(g);
        desc = text("SETTINGS_MOD_DYNAMIC_DIFFICULTY_DESC");
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
        g.fillRect(x2+s10, y2-s10, missileSizeModifierText.stringWidth(g)+s10,s30);
        missileSizeModifierText.setScaledXY(x2+s20, y2+s7);
        missileSizeModifierText.draw(g);
        desc = text("SETTINGS_MOD_MISSILE_SIZE_MODIFIER_DESC");
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
        int y4 = scaled(480)+s100; // modnar: adjust button y position, related to panel vertical extent
        int cnr = s5;
        int smallButtonH = s30;
        int smallButtonW = scaled(180);
        okBox.setBounds(w-scaled(289), y4, smallButtonW, smallButtonH);
        g.setColor(GameUI.buttonBackgroundColor());
        g.fillRoundRect(okBox.x, okBox.y, smallButtonW, smallButtonH, cnr, cnr);
        g.setFont(narrowFont(20));
        String text6 = text("SETTINGS_EXIT");
        int sw6 = g.getFontMetrics().stringWidth(text6);
        int x6 = okBox.x+((okBox.width-sw6)/2);
        int y6 = okBox.y+okBox.height-s8;
        Color c6 = hoverBox == okBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text6, 2, x6, y6, GameUI.borderDarkColor(), c6);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(okBox.x, okBox.y, okBox.width, okBox.height, cnr, cnr);
        g.setStroke(prev);

        String text7 = text("SETTINGS_DEFAULT");
        int sw7 = g.getFontMetrics().stringWidth(text7);
        smallButtonW = sw7+s30;
        defaultBox.setBounds(okBox.x-smallButtonW-s30, y4, smallButtonW, smallButtonH);
        g.setColor(GameUI.buttonBackgroundColor());
        g.fillRoundRect(defaultBox.x, defaultBox.y, smallButtonW, smallButtonH, cnr, cnr);
        g.setFont(narrowFont(20));
        int x7 = defaultBox.x+((defaultBox.width-sw7)/2);
        int y7 = defaultBox.y+defaultBox.height-s8;
        Color c7 = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text7, 2, x7, y7, GameUI.borderDarkColor(), c7);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
        g.setStroke(prev);
    }
    private String alwaysStarGatesStr() {
        String opt = text(yesOrNo(UserPreferences.alwaysStarGates()));
        return text("SETTINGS_MOD_ALWAYS_STARGATES", opt)+"   ";
    }
    private String alwaysThoriumStr() {
        String opt = text(yesOrNo(UserPreferences.alwaysThorium()));
        return text("SETTINGS_MOD_ALWAYS_THORIUM", opt)+"   ";
    }
    private String challengeModeStr() {
        String opt = text(yesOrNo(UserPreferences.challengeMode()));
        return text("SETTINGS_MOD_CHALLENGE_MODE", opt)+"   ";
    }
    private String battleScoutStr() {
        String opt = text(yesOrNo(UserPreferences.battleScout()));
        return text("SETTINGS_MOD_BATLLE_SCOUT", opt)+"   ";
    }
    private String companionWorldsStr() {
        String opt = String.format("%d",UserPreferences.companionWorldsSigned()); // BR:
        return text("SETTINGS_MOD_COMPANION_WORLDS", opt)+"   ";
    }
    private String randomTechStartStr() {
        String opt = text(yesOrNo(UserPreferences.randomTechStart()));
        return text("SETTINGS_MOD_RANDOM_TECH_START", opt)+"   ";
    }
    private String customDifficultyStr() {
        String opt = String.format("%d",UserPreferences.customDifficulty());
        return text("SETTINGS_MOD_CUSTOM_DIFFICULTY", opt)+"   ";
    }
    private String dynamicDifficultyStr() {
        String opt = text(yesOrNo(UserPreferences.dynamicDifficulty()));
        return text("SETTINGS_MOD_DYNAMIC_DIFFICULTY", opt)+"   ";
    }
    private String missileSizeModifierStr() {
        String opt = String.format("%d",(int)(UserPreferences.missileSizeModifier() * 100));
        return text("SETTINGS_MOD_MISSILE_SIZE_MODIFIER", opt)+"   ";
    }
    private String retreatRestrictionsStr() {
        String opt = "";
        switch(UserPreferences.retreatRestrictions())
        {
            case 0:
                opt = text("SETTINGS_MOD_RETREAT_NONE");
                break;
            case 1:
                opt = text("SETTINGS_MOD_RETREAT_AI");
                break;
            case 2:
                opt = text("SETTINGS_MOD_RETREAT_PLAYER");
                break;
            case 3:
                opt = text("SETTINGS_MOD_RETREAT_BOTH");
                break;
            default:
                opt = text("SETTINGS_MOD_RETREAT_NONE");
                break;
        }
        return text("SETTINGS_MOD_RETREAT_RESTRICTIONS", opt)+"   ";
    }
    /**
     * @return Retreat Restriction Option List // BR:
     */
    public static List<String> getRetreatRestrictionOptions() {
    	return List.of(
    			"SETTINGS_MOD_RETREAT_NONE"
    			, "SETTINGS_MOD_RETREAT_AI"
    			, "SETTINGS_MOD_RETREAT_PLAYER"
    			, "SETTINGS_MOD_RETREAT_BOTH");
    } // \BR:
    private String retreatRestrictionTurnsStr() {
        String opt = String.format("%d",UserPreferences.retreatRestrictionTurns());
        return text("SETTINGS_MOD_RETREAT_RESTRICTION_TURNS", opt)+"   ";
    }

    private void toggleAlwaysStarGates() {
        softClick();
        UserPreferences.toggleAlwaysStarGates();
        alwaysStarGatesText.repaint(alwaysStarGatesStr());
    }
    private void toggleAlwaysThorium() {
        softClick();
        UserPreferences.toggleAlwaysThorium();
        alwaysThoriumText.repaint(alwaysThoriumStr());
    }
    private void toggleChallengeMode() {
        softClick();
        UserPreferences.toggleChallengeMode();
        challengeModeText.repaint(challengeModeStr());
    }
    private void toggleBattleScout() {
        softClick();
        UserPreferences.toggleBattleScout();
        battleScoutText.repaint(battleScoutStr());
    }
    private void toggleCompanionWorlds(boolean up) { // BR: added bidirectional
        softClick();
        UserPreferences.toggleCompanionWorlds(up);
        companionWorldsText.repaint(companionWorldsStr());
    }
    private void toggleRandomTechStart() {
        softClick();
        UserPreferences.toggleRandomTechStart();
        randomTechStartText.repaint(randomTechStartStr());
    }
    private void toggleCustomDifficulty(boolean up, // BR: added bidirectional
    		boolean shiftPressed, boolean ctrlPressed) {
        softClick();
        if (up) {
            if (shiftPressed) 
                scrollCustomDifficulty(5);
            else if (ctrlPressed)
                scrollCustomDifficulty(20);
            else
                scrollCustomDifficulty(1); 
        }
        else {
            if (shiftPressed) 
                scrollCustomDifficulty(-5);
            else if (ctrlPressed)
                scrollCustomDifficulty(-20);
            else
                scrollCustomDifficulty(-1); 
        }
        customDifficultyText.repaint(customDifficultyStr());
    }
    private void scrollCustomDifficulty(int i) {
        UserPreferences.toggleCustomDifficulty(i);
        customDifficultyText.repaint(customDifficultyStr());
    }
    private void toggleDynamicDifficulty() {
        softClick();
        UserPreferences.toggleDynamicDifficulty();
        dynamicDifficultyText.repaint(dynamicDifficultyStr());
    }
    private void scrollMissileSizeModifier(float f) {
        UserPreferences.toggleMissileSizeModifier(f);
        missileSizeModifierText.repaint(missileSizeModifierStr());
    }
    private void scrollRetreatRestrictions(int i) {
        softClick();
        UserPreferences.toggleRetreatRestrictions(i);
        retreatRestrictionsText.repaint(retreatRestrictionsStr());
    }
    private void scrollRetreatRestrictionTurns(int i) {
        UserPreferences.toggleRetreatRestrictionTurns(i);
        retreatRestrictionTurnsText.repaint(retreatRestrictionTurnsStr());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();  // BR:
        switch(k) {
            case KeyEvent.VK_ESCAPE:
                close();
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_ENTER:
                parent.advanceHelp();
                break;
            default: // BR:
            	if(Profiles.processKey(k, e.isShiftDown(), "Modnar",
            			options(), newGameOptions())) {
            		alwaysStarGatesText.repaint(alwaysStarGatesStr());
            		alwaysThoriumText.repaint(alwaysThoriumStr());
            		challengeModeText.repaint(challengeModeStr());
                    battleScoutText.repaint(battleScoutStr());
                    companionWorldsText.repaint(companionWorldsStr());
                    randomTechStartText.repaint(randomTechStartStr());
                    customDifficultyText.repaint(customDifficultyStr());
                    customDifficultyText.repaint(customDifficultyStr());
                    dynamicDifficultyText.repaint(dynamicDifficultyStr());
                    missileSizeModifierText.repaint(missileSizeModifierStr());
            	};
            	// Needs to be done twice for the case both Galaxy size
            	// and the number of opponents were changed !?
            	if(Profiles.processKey(k, e.isShiftDown(), "Modnar",
            			options(), newGameOptions())) {
            		alwaysStarGatesText.repaint(alwaysStarGatesStr());
            		alwaysThoriumText.repaint(alwaysThoriumStr());
            		challengeModeText.repaint(challengeModeStr());
                    battleScoutText.repaint(battleScoutStr());
                    companionWorldsText.repaint(companionWorldsStr());
                    randomTechStartText.repaint(randomTechStartStr());
                    customDifficultyText.repaint(customDifficultyStr());
                    customDifficultyText.repaint(customDifficultyStr());
                    dynamicDifficultyText.repaint(dynamicDifficultyStr());
                    missileSizeModifierText.repaint(missileSizeModifierStr());
            	};
                buttonClick();
                return;
        }
    }
    @Override
    public void mouseDragged(MouseEvent e) {  }
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Rectangle prevHover = hoverBox;
        hoverBox = null;
        if (alwaysStarGatesText.contains(x,y))
            hoverBox = alwaysStarGatesText.bounds();
        else if (alwaysThoriumText.contains(x,y))
            hoverBox = alwaysThoriumText.bounds();
        else if (challengeModeText.contains(x,y))
            hoverBox = challengeModeText.bounds();
        else if (battleScoutText.contains(x,y))
            hoverBox = battleScoutText.bounds();
        else if (companionWorldsText.contains(x,y))
            hoverBox = companionWorldsText.bounds();
        else if (randomTechStartText.contains(x,y))
            hoverBox = randomTechStartText.bounds();
        else if (customDifficultyText.contains(x,y))
            hoverBox = customDifficultyText.bounds();
        else if (dynamicDifficultyText.contains(x,y))
            hoverBox = dynamicDifficultyText.bounds();
        else if (missileSizeModifierText.contains(x, y))
            hoverBox = missileSizeModifierText.bounds();
        else if (retreatRestrictionsText.contains(x, y))
            hoverBox = retreatRestrictionsText.bounds();
        else if (retreatRestrictionTurnsText.contains(x, y))
            hoverBox = retreatRestrictionTurnsText.bounds();
        else if (okBox.contains(x,y))
            hoverBox = okBox;
        else if (defaultBox.contains(x,y))
            hoverBox = defaultBox;
		
        if (hoverBox != prevHover) {
            if (prevHover == alwaysStarGatesText.bounds())
                alwaysStarGatesText.mouseExit();
            else if (prevHover == alwaysThoriumText.bounds())
                alwaysThoriumText.mouseExit();
            else if (prevHover == challengeModeText.bounds())
                challengeModeText.mouseExit();
            else if (prevHover == battleScoutText.bounds())
                battleScoutText.mouseExit();
            else if (prevHover == companionWorldsText.bounds())
                companionWorldsText.mouseExit();
            else if (prevHover == randomTechStartText.bounds())
                randomTechStartText.mouseExit();
            else if (prevHover == customDifficultyText.bounds())
                customDifficultyText.mouseExit();
            else if (prevHover == dynamicDifficultyText.bounds())
                dynamicDifficultyText.mouseExit();
            else if (prevHover == missileSizeModifierText.bounds())
                missileSizeModifierText.mouseExit();
            else if (prevHover == retreatRestrictionsText.bounds())
                retreatRestrictionsText.mouseExit();
            else if (prevHover == retreatRestrictionTurnsText.bounds())
                retreatRestrictionTurnsText.mouseExit();
            if (hoverBox == alwaysStarGatesText.bounds())
                alwaysStarGatesText.mouseEnter();
            else if (hoverBox == alwaysThoriumText.bounds())
                alwaysThoriumText.mouseEnter();
            else if (hoverBox == challengeModeText.bounds())
                challengeModeText.mouseEnter();
            else if (hoverBox == battleScoutText.bounds())
                battleScoutText.mouseEnter();
            else if (hoverBox == companionWorldsText.bounds())
                companionWorldsText.mouseEnter();
            else if (hoverBox == randomTechStartText.bounds())
                randomTechStartText.mouseEnter();
            else if (hoverBox == customDifficultyText.bounds())
                customDifficultyText.mouseEnter();
            else if (hoverBox == dynamicDifficultyText.bounds())
                dynamicDifficultyText.mouseEnter();
            else if (hoverBox == missileSizeModifierText.bounds())
                missileSizeModifierText.mouseEnter();
            else if (hoverBox == retreatRestrictionsText.bounds())
                retreatRestrictionsText.mouseEnter();
            else if (hoverBox == retreatRestrictionTurnsText.bounds())
                retreatRestrictionTurnsText.mouseEnter();
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
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() > 3)
            return;
        if (hoverBox == null)
            return;
        boolean up = !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
        boolean shiftPressed = e.isShiftDown();
        boolean ctrlPressed = e.isControlDown();
        mouseCommon(up, shiftPressed, ctrlPressed);
        if (hoverBox == okBox)
            close();
        else if (hoverBox == defaultBox)
            setToDefault();
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
    public void mouseWheelMoved(MouseWheelEvent e) {
        // modnar: mouse scroll for custom difficulty, with Shift/Ctrl modifiers
        boolean shiftPressed = e.isShiftDown(); // BR: updated deprecated method
        boolean ctrlPressed = e.isControlDown();
        boolean up = e.getWheelRotation() < 0;
        mouseCommon(up, shiftPressed, ctrlPressed);
    }
    private void mouseCommon(boolean up, boolean shiftPressed, boolean ctrlPressed) { // BR:
        if (hoverBox == alwaysStarGatesText.bounds())
            toggleAlwaysStarGates();
        else if (hoverBox == alwaysThoriumText.bounds())
            toggleAlwaysThorium();
        else if (hoverBox == challengeModeText.bounds())
            toggleChallengeMode();
        else if (hoverBox == battleScoutText.bounds())
            toggleBattleScout();
        else if (hoverBox == companionWorldsText.bounds())
            toggleCompanionWorlds(up);
        else if (hoverBox == randomTechStartText.bounds())
            toggleRandomTechStart();
        else if (hoverBox == customDifficultyText.bounds())
            toggleCustomDifficulty(up, shiftPressed, ctrlPressed);
        else if (hoverBox == dynamicDifficultyText.bounds())
            toggleDynamicDifficulty();
        else if (hoverBox == customDifficultyText.bounds()) {
            if (up) {
                if (shiftPressed) 
                    scrollCustomDifficulty(5);
                else if (ctrlPressed)
                    scrollCustomDifficulty(20);
                else
                    scrollCustomDifficulty(1); 
                return;
            }
            else {
                if (shiftPressed) 
                    scrollCustomDifficulty(-5);
                else if (ctrlPressed)
                    scrollCustomDifficulty(-20);
                else
                    scrollCustomDifficulty(-1); 
                return;
            }
        } else if (hoverBox == missileSizeModifierText.bounds()) {
            if (up) {
                if (shiftPressed) 
                    scrollMissileSizeModifier(0.05f);
                else if (ctrlPressed)
                    scrollMissileSizeModifier(0.2f);
                else
                    scrollMissileSizeModifier(0.01f); 
                return;
            }
            else {
                if (shiftPressed) 
                    scrollMissileSizeModifier(-0.05f);
                else if (ctrlPressed)
                    scrollMissileSizeModifier(-0.2f);
                else
                    scrollMissileSizeModifier(-0.01f); 
                return;
            }
        } else if (hoverBox == retreatRestrictionsText.bounds()) {
            if (up) {
                scrollRetreatRestrictions(1); 
                return;
            }
            else {
                scrollRetreatRestrictions(-1); 
                return;
            }
        } else if (hoverBox == retreatRestrictionTurnsText.bounds()) {
            if (up) {
                if (shiftPressed) 
                    scrollRetreatRestrictionTurns(5);
                else if (ctrlPressed)
                    scrollRetreatRestrictionTurns(20);
                else
                    scrollRetreatRestrictionTurns(1); 
                return;
            }
            else {
                if (shiftPressed) 
                    scrollRetreatRestrictionTurns(-5);
                else if (ctrlPressed)
                    scrollRetreatRestrictionTurns(-20);
                else
                    scrollRetreatRestrictionTurns(-1); 
                return;
            }
        }
    }
}
