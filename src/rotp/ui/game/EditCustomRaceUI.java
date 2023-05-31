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
import static rotp.model.game.IGameOptions.LIVE_OPTIONS_FILE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.ParamButtonHelp;
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
	private static final int	raceListW		= RotPUI.scaledSize(180);
	private static final ParamButtonHelp loadButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"CUSTOM_RACE_BUTTON_LOAD",
			loadCurrentKey,
			"",
			saveCurrentKey,
			"");
	private	static final EditCustomRaceUI instance		= new EditCustomRaceUI();
	
	private final Box selectBox	= new Box(selectKey);
	private final Box randomBox	= new Box(randomKey);
	private final Box loadBox	= new Box(loadButtonHelp);

	private LinkedList<SettingBase<?>> guiList;
	private RaceList raceList;
	private int yRandB;
	
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
	    	setting.settingText(new ModText(this, labelFontSize,
					labelC, labelC, hoverC, depressedC, textC, false));
	    raceList = cr.initRaceList();
	    initSetting(raceList);

	    commonList = new LinkedList<>();
	    commonList.addAll(settingList);
	    commonList.addAll(guiList);
	    
	    mouseList = new LinkedList<>();
	    mouseList.addAll(commonList);
	    mouseList.add(raceList);
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
		instance.guiOptions().selectedPlayerCustomRace(instance.cr.getAsOptions());
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
		guiOptions().selectedPlayerCustomRace(cr.getAsOptions());
		guiOptions().selectedPlayerIsCustom(true);
		guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		close();
	}
	public void updateCRGui(IGameOptions source) {
        for (InterfaceOptions param : commonList)
			param.updateOptionTool(source.dynOpts());
		writeLocalOptions(guiOptions());
	}
	public void writeLocalOptions(IGameOptions destination) {
		for (InterfaceOptions param : commonList)
			param.updateOption(destination.dynOpts());
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
				if (hoverBox == setting.settingText().box()) { // Check Setting
					setting.toggle(e, w, this);
					setting.guiSelect();
					if (autoGuide) {
						loadGuide();
						repaint();
					}
					else if (raceList.newValue())
						repaint();
					else
						totalCostText.repaint(totalCostStr());
					return;
				}
				else { // Check options
					int bulletStart	= setting.bulletStart();
					int bulletSize	= setting.bulletBoxSize();
					for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
						int optionIdx = bulletStart + bulletIdx;
						if (hoverBox == setting.optionText(bulletIdx).box()) {
							if (setting.toggle(e, w, optionIdx) || raceList.newValue()) {
								repaint();
							}
							else
								totalCostText.repaint(totalCostStr());
							return;
						}
					}
				}
			}
			else if (hoverBox == setting.settingText().box()) {
				setting.toggle(e, w, this);
				setting.settingText().repaint();
				totalCostText.repaint(totalCostStr());
				if (autoGuide) {
					loadGuide();
					repaint();
				}
				return;
			} 
		}
	}
	// ========== Overriders ==========
	//
	@Override protected void refreshGui() {
		System.out.println("===== refreshGui()");
		System.out.println("playerCustomRace: Race Name = " +
				((DynOptions) guiOptions().selectedPlayerCustomRace()).getString("CUSTOM_RACE_RACE_NAME"));
		System.out.println("settingList : Race Name = " + settingList.get(1).guideValue());
		cr.setSettingTools((DynOptions) guiOptions().selectedPlayerCustomRace());
		repaint();
	}
	@Override protected void doDefaultBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL: // restoreGlobalKey
			guiOptions().updateFromFile(LIVE_OPTIONS_FILE);		
			break;
		case CTRL_SHIFT: // restoreLocalKey
			guiOptions().updateFromFile(LIVE_OPTIONS_FILE, localOptions());		
			break;
		case SHIFT: // setLocalDefaultKey
			setToLocalDefault();
			break; 
		default: // setGlobalDefaultKey
			guiOptions().resetToDefault();
			setToLocalDefault();
			break; 
		}
		refreshGui();
	}
	@Override public void open(BasePanel p) {
		enableGlassPane(this);
		ModifierKeysState.reset();
		parent = p;

		cr.setSettingTools((DynOptions) guiOptions().selectedPlayerCustomRace());
		guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		init();
		reloadRaceList();
		repaint();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override protected void close() {
		ModifierKeysState.reset();
		disableGlassPane();
		((SetupRaceUI) parent).raceChanged();		
		RotPUI.instance().selectSetupRacePanel();
	}
	@Override protected int getBackGroundWidth() {
		return super.getBackGroundWidth() + raceListW + columnPad;
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
		text	= text(guideButtonKey());
		sw		= g.getFontMetrics().stringWidth(text);
		buttonW = g.getFontMetrics().stringWidth(text) + smallButtonMargin;
		xButton	= leftM + buttonPad;
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
		yRandB  = yDesc - buttonPadV - smallButtonH;
		sw		= g.getFontMetrics().stringWidth(text);
		buttonW = g.getFontMetrics().stringWidth(text) + smallButtonMargin;
		randomBox.setBounds(xButton, yRandB, buttonW, smallButtonH);
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
		xLine = xDesc  + labelPad;
		yLine = yRandB - labelPad;
		ModText bt;
	    for(SettingBase<?> setting : guiList) {
			bt = setting.settingText();
			bt.displayText(setting.guiSettingDisplayStr());
			bt.setScaledXY(xLine, yLine);
			bt.draw(g);
			yLine -= labelH;
	    }
	}
	@Override public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
		}
		super.keyPressed(e);
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
