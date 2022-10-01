/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.game;

import static rotp.model.empires.CustomAbilitiesFactory.ROOT;
import static rotp.ui.UserPreferences.customPlayerRace;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonKey;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonWidth;
import static rotp.ui.util.AbstractOptionsUI.userButtonKey;
import static rotp.ui.util.AbstractOptionsUI.userButtonWidth;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.Modifier2KeysState;
import rotp.ui.util.SettingBase;
import rotp.ui.util.SettingBoolean;
import rotp.ui.util.SettingInteger;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public abstract class EditCustomRaceUI extends ShowCustomRaceUI implements MouseWheelListener {
	private static final long serialVersionUID	= 1L;
	private static final String selectKey		= "CUSTOM_RACE_GUI_SELECT";
	private static final String randomKey		= "CUSTOM_RACE_GUI_RANDOM";
	
	private final SettingInteger randomTargetMax = new SettingInteger(ROOT, "RANDOM_TARGET_MAX",
			75, null, null, 1, 5, 20);
	private final SettingInteger randomTargetMin = new SettingInteger(ROOT, "RANDOM_TARGET_MIN",
			0, null, null, 1, 5, 20);
	private final SettingInteger randomMax = new SettingInteger(ROOT, "RANDOM_MAX",
			50, -100, 100, 1, 5, 20);
	private final SettingInteger randomMin = new SettingInteger(ROOT, "RANDOM_MIN",
			-50, -100, 100, 1, 5, 20);
	private final SettingBoolean randomUseTarget = new SettingBoolean(ROOT, "RANDOM_USE_TARGET", false);
	private final SettingBoolean randomSmoothEdges = new SettingBoolean(ROOT, "RANDOM_EDGES", true);
	private final LinkedList<SettingBase<?>> guiList = new LinkedList<>();

	private final Rectangle selectBox	 = new Rectangle();
    private final Rectangle defaultBox = new Rectangle();
    private final Rectangle userBox	 = new Rectangle();
	private final Rectangle randomBox	 = new Rectangle();
	private MOO1GameOptions initialOptions; // To be restored if "cancel"
	
	// ========== Constructors and initializers ==========
	//
	public EditCustomRaceUI() {
		maxLeftM	= scaled(999);
		guiTitleID	= ROOT + "GUI_TITLE";
		init_0();
	}
	private void init_0() {
		setOpaque(false);

	    totalCostText = new BaseText(this, false, costFontSize, 0, 0, 
	    		costC, costC, hoverC, depressedC, costC, 0, 0, 0);

	    // Call for filling the settings
	    if (colSettingsCount == null)
	    	initGUI();

	    guiList.add(randomSmoothEdges);
	    guiList.add(randomMin);
	    guiList.add(randomMax);
	    guiList.add(randomTargetMin);
	    guiList.add(randomTargetMax);
	    guiList.add(randomUseTarget);	    
	    for(SettingBase<?> setting : guiList) {
	    	setting.hasNoCost(true);
	    	setting.settingText(new BaseText(this, false, labelFontSize, 0, 0,
					labelC, labelC, hoverC, depressedC, textC, 0, 0, 0));
	    }
	    
	    commonList.addAll(settingList);
	    commonList.addAll(guiList);
	    cr.setRace(MOO1GameOptions.baseRaceOptions().getFirst());
	    cr.pullSettings();
	    
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	@Override public void open(BasePanel p) {
		parent = p;
		initialOptions = new MOO1GameOptions(); // Any content will do
		saveOptions(initialOptions);
		init();
		enableGlassPane(this);
		repaint();
	}
	// ========== Other Methods ==========
	//
	public void saveOptions(MOO1GameOptions destination) {
		for (InterfaceOptions param : commonList)
			param.setOptions(destination.dynamicOptions());
		customPlayerRace.setOptions(destination.dynamicOptions());
	}
	private void doExitBoxAction() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			getOptions(initialOptions);
			break;
		default: // Save
			saveLastOptions();
			break; 
		}
		close();			
	}
	private void doSelectBoxAction() {
		cr.pushSettings();
		customPlayerRace.set(true);
		saveLastOptions();
		close();
	}
	private void doDefaultBoxAction() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // set to last
			getOptions(MOO1GameOptions.loadLastOptions());
			break;
		case SHIFT: // set to last game options
			if (options() != null)
				getOptions(MOO1GameOptions.loadGameOptions());			
			break;
		default: // set to default
			setToDefault();
			break; 
		}
		repaint();
	}
	private void doUserBoxAction() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Save
			saveUserOptions();
			break;
		default: // Set
			MOO1GameOptions fileOptions = MOO1GameOptions.loadUserOptions();
			getOptions(fileOptions);
			repaint();
		}
	}	
	public void setToDefault() {
		for (InterfaceOptions param : commonList)
			param.setFromDefault();
		init();
	}
	private void saveUserOptions() {
		MOO1GameOptions fileOptions = MOO1GameOptions.loadUserOptions();
		saveOptions(fileOptions);
		MOO1GameOptions.saveUserOptions(fileOptions);
	}
	private void saveLastOptions() {
		MOO1GameOptions fileOptions = MOO1GameOptions.loadLastOptions();
		saveOptions(fileOptions);
		MOO1GameOptions.saveLastOptions(fileOptions);
	}
	private void randomizeRace() {
		cr.randomizeRace(randomMin.settingValue(), randomMax.settingValue(),
				randomTargetMin.settingValue(), randomTargetMax.settingValue(),
				randomUseTarget.settingValue(), randomSmoothEdges.settingValue(), true);
		totalCostText.repaint(totalCostStr());
	}

	// ========== Overriders ==========
	//
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0); // call ShowUI
		Graphics2D g = (Graphics2D) g0;
		int cnr = s5;

		// Select Button
		String text	 = text(selectKey);
		int sw = g.getFontMetrics().stringWidth(text);
		int buttonW	 = sw + buttonMargin;
		xButton -= (buttonW + buttonPad);
		selectBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(selectBox.x, selectBox.y, buttonW, buttonH, cnr, cnr);
		int xT = selectBox.x+((selectBox.width-sw)/2);
		int yT = selectBox.y+selectBox.height-s8;
		Color cB = hoverBox == selectBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(selectBox.x, selectBox.y, selectBox.width, selectBox.height, cnr, cnr);
		g.setStroke(prev);

		// Default Button
		text	 = text(defaultButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = defaultButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		defaultBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(defaultBox.x, defaultBox.y, buttonW, buttonH, cnr, cnr);
		xT = defaultBox.x+((defaultBox.width-sw)/2);
		yT = defaultBox.y+defaultBox.height-s8;
		cB = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
		g.setStroke(prev);

		// User preference Button
		text	 = text(userButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = userButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		userBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, buttonW, buttonH, cnr, cnr);
		xT = userBox.x+((userBox.width-sw)/2);
		yT = userBox.y+userBox.height-s8;
		cB = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);

		// Randomize Button
		text = text(randomKey);
		xButton = leftM + buttonPad;
		sw = g.getFontMetrics().stringWidth(text);
		buttonW = g.getFontMetrics().stringWidth(text) + buttonMargin;
		randomBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(randomBox.x, randomBox.y, buttonW, buttonH, cnr, cnr);
		xT = randomBox.x+((randomBox.width-sw)/2);
		yT = randomBox.y+randomBox.height-s8;
		cB = hoverBox == randomBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(randomBox.x, randomBox.y, randomBox.width, randomBox.height, cnr, cnr);
		g.setStroke(prev);
		
		// Randomize Options
		xLine = xButton + labelPad;
		yLine = yButton - labelPad;
		BaseText bt;
	    for(SettingBase<?> setting : guiList) {
			bt = setting.settingText();
			bt.displayText(setting.guiSettingDisplayStr());
			bt.setScaledXY(xLine, yLine);
			bt.draw(g);
			yLine -= labelH;
	    }
	}
	@Override public void keyPressed(KeyEvent e) {
		checkModifierKey(e);
		int k = e.getKeyCode();  // BR:
		switch(k) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_ENTER:
				parent.advanceHelp();
				return;
			default: // BR:
//				if(Profiles.processKey(k, e.isShiftDown(), guiTitleID, newGameOptions())) {
//				};
				// Needs to be done twice for the case both Galaxy size
				// and the number of opponents were changed !?
//				if(Profiles.processKey(k, e.isShiftDown(), guiTitleID, newGameOptions())) {
//					for (int i=0; i<paramList.size(); i++) {
//						btList.get(i).repaint(paramList.get(i).getGuiDisplay());
//					} TODO BR: processKey
//				};
				return;
		}
	}
	@Override public void mouseMoved(MouseEvent e) {
		checkModifierKey(e);
		int x = e.getX();
		int y = e.getY();
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y))
			hoverBox = exitBox;
		else if (userBox.contains(x,y))
			hoverBox = userBox;

		if (selectBox.contains(x,y))
			hoverBox = selectBox;
		else if (defaultBox.contains(x,y))
			hoverBox = defaultBox;
		else if (randomBox.contains(x,y))
			hoverBox = randomBox;
		else {
			outerLoop1:
			for ( SettingBase<?> setting : commonList) {
				if (setting.settingText().contains(x,y)) {
					hoverBox = setting.settingText().bounds();
					break outerLoop1;
				}
				if (setting.isBullet()) {					
					for (BaseText txt : setting.optionsText()) {
						if (txt.contains(x,y)) {
							hoverBox = txt.bounds();
							break outerLoop1;
						}
					}
				}
			}
		}
		if (hoverBox != prevHover) {
			outerLoop2:
				for ( SettingBase<?> setting : commonList) {
					if (prevHover == setting.settingText().bounds()) {
						setting.settingText().mouseExit();
						break outerLoop2;
					}
					if (setting.isBullet()) {					
						for (BaseText txt : setting.optionsText()) {
							if (prevHover == txt.bounds()) {
								txt.mouseExit();
								break outerLoop2;
							}
						}
					}
				}
			outerLoop3:
				for ( SettingBase<?> setting : commonList) {
					if (hoverBox == setting.settingText().bounds()) {
						setting.settingText().mouseEnter();
						break outerLoop3;
					}
					if (setting.isBullet()) {					
						for (BaseText txt : setting.optionsText()) {
							if (hoverBox == txt.bounds()) {
								txt.mouseEnter();
								break outerLoop3;
							}
						}
					}
				}	
			if (prevHover != null) repaint(prevHover);
			if (hoverBox != null)  repaint(hoverBox);
		}
	}
	@Override public void mouseReleased(MouseEvent e) {
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		if (hoverBox == exitBox) {
			doExitBoxAction();
			return;
		}
		if (hoverBox == selectBox) {
			doSelectBoxAction();
			return;
		}
		if (hoverBox == userBox) {
			doUserBoxAction();
			return;
		}
		if (hoverBox == defaultBox) {
			doDefaultBoxAction();			
			return;
		}
		if (hoverBox == randomBox) {
			randomizeRace();			
			return;
		}
		boolean up	= !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
		boolean mid	= !SwingUtilities.isMiddleMouseButton(e); // BR: added reset click
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed = e.isControlDown();
		mouseCommon(up, mid, shiftPressed, ctrlPressed, e, null);
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed	 = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
