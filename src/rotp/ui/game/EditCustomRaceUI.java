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

import static rotp.model.empires.CustomRaceDefinitions.ROOT;
import static rotp.ui.UserPreferences.customPlayerRace;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonKey;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonWidth;
import static rotp.ui.util.AbstractOptionsUI.userButtonKey;
import static rotp.ui.util.AbstractOptionsUI.userButtonWidth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.Modifier2KeysState;
import rotp.ui.util.SettingBase;
import rotp.util.LabelManager;

public class EditCustomRaceUI extends ShowCustomRaceUI implements MouseWheelListener {
	private static final long serialVersionUID	= 1L;
	private static final String selectKey		= ROOT + "GUI_SELECT";
	private static final String randomKey		= ROOT + "GUI_RANDOM";
	private static final String saveCurrentKey	= ROOT + "GUI_SAVE";
	private static final String loadCurrentKey	= ROOT + "GUI_LOAD";
	private static final String setDefaultTipKey= ROOT + "DEFAULT_TIP";
	private static final String setGameTipKey	= ROOT + "LAST_GAME_TIP";
	private static final String setLastTipKey	= ROOT + "LAST_SET_TIP";
	private	static final String setUserTipKey	= ROOT + "USER_SET_TIP";
	private	static final String saveUserTipKey	= ROOT + "USER_SAVE_TIP";
	private	static final String selectTipKey	= ROOT + "GUI_SELECT_TIP";
	private static final String randomTipKey	= ROOT + "GUI_RANDOM_TIP";
	private static final String saveTipKey		= ROOT + "GUI_SAVE_TIP";
	private static final String loadTipKey		= ROOT + "GUI_LOAD_TIP";
	private static final String cancelTipKey	= ROOT + "CANCEL_TIP";

	public	static final EditCustomRaceUI instance = new EditCustomRaceUI().init0();
	
	private final Rectangle selectBox	= new Rectangle();
    private final Rectangle defaultBox	= new Rectangle();
    private final Rectangle userBox		= new Rectangle();
	private final Rectangle randomBox	= new Rectangle();
	private final Rectangle loadBox		= new Rectangle();

	private LinkedList<SettingBase<?>> guiList;
	private MOO1GameOptions initialOptions; // To be restored if "cancel"
	
	// ========== Constructors and initializers ==========
	//
	private EditCustomRaceUI() {}

	private void initTextField(JTextField value) {
		value.setBackground(GameUI.setupFrame());
		value.setBorder(newEmptyBorder(3,3,0,0));
		value.setPreferredSize(new Dimension(raceNameW, raceNameH));
		value.setFont(raceNameFont);
		value.setForeground(Color.black);
		value.setCaretColor(Color.black);
		value.putClientProperty("caretWidth", s3);
		value.setVisible(true);
		value.addMouseListener(this);
		add(value);
	}
	private EditCustomRaceUI init0() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		maxLeftM	= scaled(999);
		guiTitleID	= ROOT + "GUI_TITLE";
	    initTextField(raceName);
	    initGUI();		

		guiList = cr.guiList();
	    for(SettingBase<?> setting : guiList)
	    	setting.settingText(new BaseText(this, false, labelFontSize, 0, 0,
					labelC, labelC, hoverC, depressedC, textC, 0, 0, 0));

	    commonList = new LinkedList<>();
	    commonList.addAll(settingList);
	    commonList.addAll(guiList);
	    cr.setRace(MOO1GameOptions.baseRaceOptions().getFirst());
	    cr.pullSettings();
		return this;
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

	private void saveCurrentRace() { cr.saveRace(); }
	private void loadCurrentRace() { cr.loadRace(); }
	private void doLoadBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Save
			saveCurrentRace();
			break;
		default: // Load
			loadCurrentRace();
			repaint();
			break; 
		}
	}
	private void doExitBoxAction() {
		buttonClick();
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
		buttonClick();
		cr.pushSettings();
		customPlayerRace.set(true);
		saveLastOptions();
		close();
	}
	private void doDefaultBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // set to last
			getOptions(MOO1GameOptions.loadLastOptions());
			break;
		case SHIFT: // set to last game options
			getOptions(MOO1GameOptions.loadGameOptions());
			break;
		default: // set to default
			setToDefault();
			break; 
		}
		repaint();
	}
	private void doUserBoxAction() {
		buttonClick();
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
		cr.randomizeRace(true);
		totalCostText.repaint(totalCostStr());
	}
	private String selectButtonTipKey() {
			return selectTipKey;
	}
	private String randomButtonTipKey() {
			return randomTipKey;
	}
	private String exitButtonTipKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			return cancelTipKey;
		default: // Save
			return exitTipKey;
		}
	}
	private String userButtonTipKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return saveUserTipKey;
		default:
			return setUserTipKey;
		}
	}
	private String defaultButtonTipKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return setLastTipKey;
		case SHIFT:
			return setGameTipKey;
		default:
			return setDefaultTipKey;
		}
	}
	private String loadButtonTipKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return saveTipKey;
		default:
			return loadTipKey;
		}
	}
	private String loadButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return saveCurrentKey;
		default:
			return loadCurrentKey;
		}
	}
	private int loadButtonWidth(Graphics2D g) {
		return Math.max(g.getFontMetrics().stringWidth(LabelManager.current().label(saveCurrentKey)),
						g.getFontMetrics().stringWidth(LabelManager.current().label(loadCurrentKey)))
				+ buttonMargin;
	}
	private void updateBulletSetting(SettingBase<?> setting) {
		setting.guiSelect();
		totalCostText.repaint(totalCostStr());
	}
	private void mouseCommon(boolean up, boolean mid, boolean shiftPressed, boolean ctrlPressed
			, MouseEvent e, MouseWheelEvent w) {
		for (int settingIdx=0; settingIdx < commonList.size(); settingIdx++) {
			SettingBase<?> setting = commonList.get(settingIdx);
			if (setting.isBullet()) {
				if (hoverBox == setting.settingText().bounds()) { // Check Setting
					setting.toggle(e, w);
					updateBulletSetting(setting);
					return;
				} else { // Check options
					int optionCount	= setting.boxSize(); // 1 for the setting
					for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
						if (hoverBox == setting.optionText(optionIdx).bounds()) {
							setting.index(optionIdx);
							updateBulletSetting(setting);
							return;
						}
					}
				}
			} else if (hoverBox == setting.settingText().bounds()) {
				setting.toggle(e, w);
				setting.settingText().repaint();
				totalCostText.repaint(totalCostStr());
				return;
			}
		}
	}
	@Override protected void hoverAndTooltip(boolean repaint) {
		String tip = tooltipText;
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y)) {
			hoverBox = exitBox;
			tooltipText = text(exitButtonTipKey());
		}
		else if (userBox.contains(x,y)) {
			hoverBox = userBox;
			tooltipText = text(userButtonTipKey());
		}
		else if (selectBox.contains(x,y)) {
			hoverBox = selectBox;
			tooltipText = text(selectButtonTipKey());
		}
		else if (defaultBox.contains(x,y)) {
			hoverBox = defaultBox;
			tooltipText = text(defaultButtonTipKey());
		}
		else if (loadBox.contains(x,y)) {
			hoverBox = loadBox;
			tooltipText = text(loadButtonTipKey());
		}
		else if (randomBox.contains(x,y)) {
			hoverBox = randomBox;
			tooltipText = text(randomButtonTipKey());
		}
		else {
			outerLoop1:
			for (SettingBase<?> setting : commonList) {
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
			outerLoop2: // Check exit settings
			for ( SettingBase<?> setting : commonList) {
				if (setting.isSpacer())
					continue;
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
			
			outerLoop3: // Check enter settings
			for ( SettingBase<?> setting : commonList) {
				if (setting.isSpacer())
					continue;
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
			if (repaint)
				repaint();
			else if (!tooltipText.equals(tip))
				repaint();
			else {
				if (prevHover != null) repaint(prevHover);
				if (hoverBox != null)  repaint(hoverBox);
			}
		}
		else if (repaint || !tooltipText.equals(tip))
			repaint();		
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
		int xT = selectBox.x + ((selectBox.width-sw)/2);
		int yT = selectBox.y + selectBox.height-s8;
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
		xT = defaultBox.x + ((defaultBox.width-sw)/2);
		yT = defaultBox.y + defaultBox.height-s8;
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
		xT = userBox.x + ((userBox.width-sw)/2);
		yT = userBox.y + userBox.height-s8;
		cB = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);

		// Load / Save Button
		text	 = text(loadButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = loadButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		loadBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(loadBox.x, loadBox.y, buttonW, buttonH, cnr, cnr);
		xT = loadBox.x + ((loadBox.width-sw)/2);
		yT = loadBox.y + loadBox.height-s8;
		cB = hoverBox == loadBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(loadBox.x, loadBox.y, loadBox.width, loadBox.height, cnr, cnr);
		g.setStroke(prev);

		// Randomize Button
		text	= text(randomKey);
		xButton = leftM + buttonPad;
		sw		= g.getFontMetrics().stringWidth(text);
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
	    
		g.setFont(raceNameFont);
		g.setColor(Color.black);
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
		if (hoverBox == loadBox) {
			doLoadBoxAction();			
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
	@Override public void mouseEntered(MouseEvent e) { // TODO
		if (e.getComponent() == raceName) {
			raceName.requestFocus();
        }
	}
	@Override public void mouseExited(MouseEvent e) {
		if (e.getComponent() == raceName) {
			cr.raceName().set(raceName.getText());
			RotPUI.instance().requestFocus();
		}
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed	 = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
