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
import static rotp.model.game.MOO1GameOptions.loadAndUpdateFromFileName;
import static rotp.model.game.MOO1GameOptions.setBaseAndModSettingsToDefault;
import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;
import static rotp.ui.UserPreferences.playerCustomRace;
import static rotp.ui.UserPreferences.playerIsCustom;

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

import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.empires.CustomRaceDefinitions.RaceList;
import rotp.model.game.DynOptions;
import rotp.model.game.IGameOptions;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.SettingBase;
import rotp.util.LabelManager;
import rotp.util.ModifierKeysState;

public class EditCustomRaceUI extends ShowCustomRaceUI implements MouseWheelListener {
	private static final long serialVersionUID	= 1L;
	public  static final String GUI_ID			= "CUSTOM_RACE";
	private static final String selectKey		= ROOT + "GUI_SELECT";
	private static final String randomKey		= ROOT + "GUI_RANDOM";
	private static final String saveCurrentKey	= ROOT + "GUI_SAVE";
	private static final String loadCurrentKey	= ROOT + "GUI_LOAD";
	private	static final String selectTipKey	= ROOT + "GUI_SELECT_DESC";
	private static final String randomTipKey	= ROOT + "GUI_RANDOM_DESC";
	private static final String saveTipKey		= ROOT + "GUI_SAVE_DESC";
	private static final String loadTipKey		= ROOT + "GUI_LOAD_DESC";
	private	static final EditCustomRaceUI instance = new EditCustomRaceUI();
	
	private final Rectangle selectBox	= new Rectangle();
	private final Rectangle randomBox	= new Rectangle();
	private final Rectangle loadBox		= new Rectangle();

	private LinkedList<SettingBase<?>> guiList;
	private RaceList raceList;
	private static final int raceListW = RotPUI.scaledSize(180);
	
	// ========== Constructors and initializers ==========
	//
	private EditCustomRaceUI() {}
	public static EditCustomRaceUI instance() { return instance.init0(); }
	public EditCustomRaceUI init0() {
		if (initialized)
			return this;
		initialized = true;
		cr = new CustomRaceDefinitions();		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		maxLeftM	= scaled(999);
		guiTitleID	= ROOT + "GUI_TITLE";
	    initGUI();		

		guiList = cr.guiList();
	    for(SettingBase<?> setting : guiList)
	    	setting.settingText(new BaseText(this, false, labelFontSize, 0, 0,
					labelC, labelC, hoverC, depressedC, textC, 0, 0, 0));
	    raceList = cr.initRaceList();
	    initSetting(raceList);

	    commonList = new LinkedList<>();
	    commonList.addAll(settingList);
	    commonList.addAll(guiList);
	    
	    mouseList = new LinkedList<>();
	    mouseList.addAll(commonList);
	    mouseList.add(raceList);
	    
	    cr.setRace(IGameOptions.baseRaceOptions().getFirst());
		return this;
	}
	private void reloadRaceList() {
		raceList.reload();
		int paramIdx	= raceList.index();
		int bulletStart	= raceList.bulletStart();
		int bulletSize	= raceList.bulletBoxSize();
		for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
			int optionIdx = bulletStart + bulletIdx;
			raceList.optionText(optionBT(), bulletIdx);
			raceList.optionText(bulletIdx).disabled(optionIdx == paramIdx);
		}
		init();
	}
	// ========== Other Methods ==========
	//
	public static void updatePlayerCustomRace() {
		if (instance == null)
			return;
		if (instance.cr == null)
			return;
		playerCustomRace.set(instance.cr.getAsOptions());
	}
	private void saveCurrentRace() { cr.saveRace(); }
	private void loadCurrentRace() { cr.loadRace(); }
	private void doLoadBoxAction() { // Local to panel
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Save
			saveCurrentRace();
			break;
		default: // Load
			loadCurrentRace();
			break; 
		}
		reloadRaceList();
		repaint();
	}
	private void doSelectBoxAction() {
		buttonClick();
		playerCustomRace.set(cr.getAsOptions());
		playerIsCustom.set(true);
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		close();
	}
	public void updateCRGui(MOO1GameOptions source) {
        for (InterfaceOptions param : commonList)
			param.setFromOptions(source.dynamicOptions());
        playerIsCustom.setFromOptions(source.dynamicOptions());
		playerCustomRace.setFromOptions(source.dynamicOptions());
		writeLocalOptions(guiOptions());
		init();
	}
	public void writeLocalOptions(MOO1GameOptions destination) {
		for (InterfaceOptions param : commonList)
			param.setOptions(destination.dynamicOptions());
		playerIsCustom.setOptions(destination.dynamicOptions());
		playerCustomRace.setOptions(destination.dynamicOptions());
	}
	private void setToLocalDefault() {
		for (InterfaceOptions param : commonList)
			param.setFromDefault();
		writeLocalOptions(guiOptions());
		init(); // Validate Init
	}
	private void randomizeRace() {
		cr.randomizeRace(true);
		totalCostText.repaint(totalCostStr());
	}
	private String selectButtonDescKey() {
			return selectTipKey;
	}
	private String randomButtonDescKey() {
			return randomTipKey;
	}
	@Override protected String exitButtonDescKey() {
		return super.exitButtonDescKey();
	}
	private String loadButtonDescKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return saveTipKey;
		default:
			return loadTipKey;
		}
	}
	private String loadButtonKey() {
		switch (ModifierKeysState.get()) {
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
				+ smallButtonMargin;
	}
	private void mouseCommon(MouseEvent e, MouseWheelEvent w) {
		for (int settingIdx=0; settingIdx < mouseList.size(); settingIdx++) {
			SettingBase<?> setting = mouseList.get(settingIdx);
			if (setting.isBullet()) {
				if (hoverBox == setting.settingText().bounds()) { // Check Setting
					setting.toggle(e, w, this);
					setting.guiSelect();
					if (raceList.newValue())
						repaint();
					else
						totalCostText.repaint(totalCostStr());
					return;
				} else { // Check options
					int bulletStart	= setting.bulletStart();
					int bulletSize	= setting.bulletBoxSize();
					for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
						int optionIdx = bulletStart + bulletIdx;
						if (hoverBox == setting.optionText(bulletIdx).bounds()) {
							if (setting.toggle(e, w, optionIdx) || raceList.newValue())
								repaint();
							else
								totalCostText.repaint(totalCostStr());
							return;
						}
					}
				}
			} else if (hoverBox == setting.settingText().bounds()) {
				setting.toggle(e, w, this);
				setting.settingText().repaint();
				totalCostText.repaint(totalCostStr());
				return;
			} 
		}
	}
	// ========== Overriders ==========
	//
	@Override protected void refreshGui() {
		System.out.println("===== refreshGui()");
		System.out.println("playerCustomRace: Race Name = " +
				((DynOptions) playerCustomRace.get()).getString("CUSTOM_RACE_RACE_NAME"));
		System.out.println("settingList : Race Name = " + settingList.get(1).guiSettingValue());
		cr.fromOptions((DynOptions) playerCustomRace.get());
		repaint();
	}
	@Override protected void doDefaultBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL: // restoreGlobalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);		
			break;
		case CTRL_SHIFT: // restoreLocalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, GUI_ID());		
			break;
		case SHIFT: // setLocalDefaultKey
			setToLocalDefault();
			break; 
		default: // setGlobalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), ALL_GUI_ID);
			setToLocalDefault();
			break; 
		}
		refreshGui();
	}
	@Override public void open(BasePanel p) {
		enableGlassPane(this);
		ModifierKeysState.reset();
		parent = p;

		cr.fromOptions((DynOptions) playerCustomRace.get());
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		init();
		reloadRaceList();
		repaint();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override protected void close() {
		ModifierKeysState.reset();
		disableGlassPane();
		((SetupRaceUI) parent).raceChanged();		
		RotPUI.instance().returnToSetupRacePanel();
	}
	@Override protected int getBackGroundWidth() {
		return super.getBackGroundWidth() + raceListW + columnPad;
	}
	@Override protected boolean checkForHoveredButtons() {
		if (exitBox.contains(x,y)) {
			hoverBox = exitBox;
			tooltipText = text(exitButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (guideBox.contains(x,y)) {
			hoverBox = guideBox;
			tooltipText = text(guideButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (userBox.contains(x,y)) {
			hoverBox = userBox;
			tooltipText = text(userButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (lastBox.contains(x,y)) {
			hoverBox = lastBox;
			tooltipText = text(lastButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (selectBox.contains(x,y)) {
			hoverBox = selectBox;
			tooltipText = text(selectButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (defaultBox.contains(x,y)) {
			hoverBox = defaultBox;
			tooltipText = text(defaultButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (loadBox.contains(x,y)) {
			hoverBox = loadBox;
			tooltipText = text(loadButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		} else if (randomBox.contains(x,y)) {
			hoverBox = randomBox;
			tooltipText = text(randomButtonDescKey());
			if (hoverBox != prevHover) {
				if (tooltipText.equals(preTipTxt)) {
					repaint(hoverBox);
				} else {
					repaint();
				}
			}
			return true;
		}
		return false;
	}
	@Override protected String raceAIButtonTxt() { return ""; }
	@Override protected void paintButtons(Graphics2D g) {
		int cnr = s5;
		g.setFont(smallButtonFont);

		// Exit Button
		String text = text(exitButtonKey());
		int sw = g.getFontMetrics().stringWidth(text);
		int buttonW	= exitButtonWidth(g);
		xButton = leftM + wBG - buttonW - buttonPad;
		exitBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(exitBox.x, exitBox.y, buttonW, smallButtonH, cnr, cnr);
		int xT = exitBox.x+((exitBox.width-sw)/2);
		int yT = exitBox.y+exitBox.height-s8;
		Color cB = hoverBox == exitBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(exitBox.x, exitBox.y, exitBox.width, exitBox.height, cnr, cnr);
		g.setStroke(prev);

		// Select Button
		text	 = text(selectKey);
		sw = g.getFontMetrics().stringWidth(text);
		buttonW	 = sw + smallButtonMargin;
		xButton -= (buttonW + buttonPad);
		selectBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(selectBox.x, selectBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = selectBox.x + ((selectBox.width-sw)/2);
		yT = selectBox.y + selectBox.height-s8;
		cB = hoverBox == selectBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(selectBox.x, selectBox.y, selectBox.width, selectBox.height, cnr, cnr);
		g.setStroke(prev);

		// Default Button
		text	 = text(defaultButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = defaultButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		defaultBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(defaultBox.x, defaultBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = defaultBox.x + ((defaultBox.width-sw)/2);
		yT = defaultBox.y + defaultBox.height-s8;
		cB = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
		g.setStroke(prev);

		// Last Button
		text	 = text(lastButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW  = lastButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		lastBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(lastBox.x, lastBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = lastBox.x + ((lastBox.width-sw)/2);
		yT = lastBox.y + lastBox.height-s8;
		cB = hoverBox == lastBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);
		g.setStroke(prev);

		// User preference Button
		text	 = text(userButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = userButtonWidth(g);
		xButton -= (buttonW + buttonPad);
		userBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, buttonW, smallButtonH, cnr, cnr);
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
		loadBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(loadBox.x, loadBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = loadBox.x + ((loadBox.width-sw)/2);
		yT = loadBox.y + loadBox.height-s8;
		cB = hoverBox == loadBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(loadBox.x, loadBox.y, loadBox.width, loadBox.height, cnr, cnr);
		g.setStroke(prev);

		// Guide Button
		text	 = text(guideButtonKey());
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW  = g.getFontMetrics().stringWidth(text) + smallButtonMargin;
		xButton -= (buttonW + buttonPad);
		guideBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(guideBox.x, guideBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = guideBox.x+((guideBox.width-sw)/2);
		yT = guideBox.y+guideBox.height-s8;
		cB = hoverBox == guideBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
		g.setStroke(prev);

		// Randomize Button
		text	= text(randomKey);
		xButton = leftM + buttonPad;
		sw		= g.getFontMetrics().stringWidth(text);
		buttonW = g.getFontMetrics().stringWidth(text) + smallButtonMargin;
		randomBox.setBounds(xButton, yButton, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(randomBox.x, randomBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = randomBox.x+((randomBox.width-sw)/2);
		yT = randomBox.y+randomBox.height-s8;
		cB = hoverBox == randomBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(randomBox.x, randomBox.y, randomBox.width, randomBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0); // call ShowUI
		Graphics2D g = (Graphics2D) g0;
		// Custom Race List
		currentWith = raceListW;
		Stroke prev = g.getStroke();
		g.setStroke(stroke2);
		paintSetting(g, raceList);
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
	    //g.dispose();
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
		if (hoverBox == guideBox) {
			doGuideBoxAction();
			return;
		}
		if (hoverBox == userBox) {
			doUserBoxAction();
			return;
		}
		if (hoverBox == lastBox) {
			doLastBoxAction();
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
		mouseCommon(e, null);
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		mouseCommon(null, e);
	}
}
