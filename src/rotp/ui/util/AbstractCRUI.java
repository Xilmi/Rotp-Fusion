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
package rotp.ui.util;

import static rotp.model.empires.CustomRaceFactory.ROOT;
import static rotp.ui.UserPreferences.customPlayerRace;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonKey;
import static rotp.ui.util.AbstractOptionsUI.defaultButtonWidth;
import static rotp.ui.util.AbstractOptionsUI.exitButtonKey;
import static rotp.ui.util.AbstractOptionsUI.exitButtonWidth;
import static rotp.ui.util.AbstractOptionsUI.userButtonKey;
import static rotp.ui.util.AbstractOptionsUI.userButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import rotp.model.empires.CustomRaceFactory;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.game.GameUI;
import rotp.ui.main.SystemPanel;
import rotp.ui.races.RacesUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public abstract class AbstractCRUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID	= 1L;
	private static final Color  backgroundHaze	= new Color(0,0,0,160);
	private static final String totalCostKey	= "CUSTOM_RACE_GUI_COST";
	private static final String selectKey		= "CUSTOM_RACE_GUI_SELECT";
	private static final String randomKey		= "CUSTOM_RACE_GUI_RANDOM";
	private static final LinkedList<Integer> colSettingsCount	= new LinkedList<>();
	private static final LinkedList<SettingBase<?>> settingList	= new LinkedList<>();
	private static final LinkedList<SettingBase<?>> guiList		= new LinkedList<>();
	private static final LinkedList<SettingBase<?>> commonList	= new LinkedList<>();
	public	static final CustomRaceFactory cr = new CustomRaceFactory();
	private static final String initialRace	= MOO1GameOptions.baseRacesOptions().getFirst();
	
	private static final Color textC		= SystemPanel.whiteText;
	private		   final Font buttonFont	= narrowFont(20);
	private static final int buttonH		= s30;
	private static final int buttonMargin	= AbstractOptionsUI.smallButtonM;
	private static final int buttonPad		= s15;
	private static final int xButtonOffset	= s30;
	private static final int yButtonOffset	= s40;
	private static final Color labelC		= SystemPanel.orangeText;
	private	static final int labelFontSize	= 14;
	private static final int labelH			= s16;
	private static final int labelPad		= s8;

	private static final Color costC		= SystemPanel.blackText;
	private		   final String guiTitleID;
	private		   final String showTitleID;
	private		   final Font titleFont		= narrowFont(30);
	private	static final int costFontSize	= 18;
	private static final int titleOffset	= s30; // Offset from Margin
	private static final int costOffset		= s25; // Offset from title
	private static final int titlePad		= s75;
	private static final int bottomPad		= s40;
	private static final int columnPad		= s20;
	
	private static final Color frameC		= SystemPanel.blackText; // Setting frame color
	private static final Color settingPosC	= SystemPanel.limeText; // Setting name color
	private static final Color settingNegC	= SystemPanel.redText; // Setting name color
	private static final Color settingC		= SystemPanel.whiteText; // Setting name color
	private static final int settingFont	= 16;
	private static final int settingH		= s16;
	private static final int spacerH		= s10;
	private static final int settingHPad	= s4;
	private static final int frameShift		= s5;
	private static final int frameTopPad	= 0;
	private static final int frameSizePad	= s10;
	private static final int frameEndPad	= s4;
	private static final int settingIndent	= s10;
	private static final int wSetting		= s100+s100+s20;

	private static final Color optionC		= SystemPanel.blackText; // Unselected option Color
	private static final Color selectC		= SystemPanel.whiteText;  // Selected option color
	private static final int optionFont		= 13;
	private static final int optionH		= s15;
	private static final int optionIndent	= s15;

	private static final SettingInteger randomTargetMax = new SettingInteger(ROOT, "RANDOM_TARGET_MAX",
			75, null, null, 1, 5, 20);
	private static final SettingInteger randomTargetMin = new SettingInteger(ROOT, "RANDOM_TARGET_MIN",
			0, null, null, 1, 5, 20);
	private static final SettingInteger randomMax = new SettingInteger(ROOT, "RANDOM_MAX",
			50, -100, 100, 1, 5, 20);
	private static final SettingInteger randomMin = new SettingInteger(ROOT, "RANDOM_MIN",
			-50, -100, 100, 1, 5, 20);
	private static final SettingBoolean randomUseTarget	  = new SettingBoolean(ROOT, "RANDOM_USE_TARGET", false);
	private static final SettingBoolean randomSmoothEdges = new SettingBoolean(ROOT, "RANDOM_EDGES", true);

	private static int numColumns	= 0;
	private static int columnsMaxH	= 0;

	private int xButton, yButton;
	private int yTitle;
	private int xCost, yCost;
	private int w, wBG, h, hBG;
	private int columnH		= 0;
	private int numSettings	= 0;
	private int settingBoxH;
	private int leftM, topM, yTop;
	private int xLine, yLine; // settings var
	private int columnIndex, rowIndex;

	private BasePanel parent;
	private Rectangle hoverBox;
	private Rectangle exitBox 	 = new Rectangle();
	private Rectangle selectBox	 = new Rectangle();
    private Rectangle defaultBox = new Rectangle();
    private Rectangle userBox	 = new Rectangle();
	private Rectangle randomBox	 = new Rectangle();
	private boolean ctrlPressed	 = false;
	private static BaseText totalCostText;
	private RacesUI  raceUI;
	private boolean  showOnly = false;
	private static boolean initialized = false;
	private MOO1GameOptions initialOptions; // To be restored if "cancel"
	
	// ========== Constructors and initializers ==========
	//

	public AbstractCRUI(String guiTitle_ID) {
		guiTitleID = guiTitle_ID;
		showTitleID = ROOT + "SHOW_TITLE";
		showOnly   = false;
		init_0();
	}
	private void init_0() {
		setOpaque(false);
		if (!initialized ) {
			cr.init(settingList);
		    totalCostText = new BaseText(this, false, costFontSize, 0, 0, 
		    		costC, costC, hoverC, depressedC, costC, 0, 0, 0);

		    // Call for filling the settings
		    if (settingList.size() == 0)
		    	init0();

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
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		initialized = true;
	}
	public void loadRace() {
		showOnly = true;
		cr.initShowRace(raceUI.selectedEmpire().abilitiesKey());
	}
	public void init(RacesUI p) {
		raceUI     = p;
		showOnly   = true;
		cr.initShowRace(initialRace);
	}
	private void init() {
		showOnly = false;
		if (cr.getRace() == null) {
			cr.setRace(newGameOptions().selectedPlayerRace());
			cr.pullSettings();
		}
//		// Display text Initialization
//		for (SettingBase<?> setting : settingList) { // Loop thru the setting list
//			if (setting.isSpacer())
//				continue;
//			if (setting.isBullet()) {
//				setting.settingText().displayText(setting.guiSettingDisplayStr()); // The setting
//				int optionCount = setting.boxSize();
//				for (int optionIdx=0; optionIdx <  optionCount; optionIdx++) {
//					setting.optionText(optionIdx).displayText(setting.guiCostOptionStr(optionIdx)); // The options
//				}
//			} else {
//				setting.settingText().displayText(setting.guiSettingDisplayStr());			
//			}
//		}
		// TODO BR: Mix the two list
		for (SettingBase<?> setting : commonList) { // Loop thru the setting list
			if (setting.isSpacer())
				continue;
			if (setting.isBullet()) {
				setting.settingText().displayText(setting.guiSettingDisplayStr()); // The setting
				int optionCount = setting.boxSize();
				for (int optionIdx=0; optionIdx <  optionCount; optionIdx++) {
					setting.optionText(optionIdx).displayText(setting.guiCostOptionStr(optionIdx)); // The options
				}
			} else {
				setting.settingText().displayText(setting.guiSettingDisplayStr());			
			}
		}
		totalCostText.displayText(totalCostStr());
		totalCostText.disabled(true);
	}
	// ========== Abstract Methods Request ==========
	//
	protected abstract void init0();
	// ========== Other Methods ==========
	//
	private  String totalCostStr() {
		return text(totalCostKey, Math.round(cr.getTotalCost()));
	}
	private  BaseText settingBT() {
		return new BaseText(this, false, settingFont, 0, 0,
				settingC, settingNegC, hoverC, depressedC, textC, 0, 0, 0);
	}
	private  BaseText optionBT() {
		return new BaseText(this, false, optionFont, 0, 0,
				optionC, selectC, hoverC, depressedC, textC, 0, 0, 0);
	}
	protected void newSetting(SettingBase<?> setting) {
		columnH += settingHPad;
		if (setting.isBullet()) {
			int optionCount = setting.boxSize(); // +1 for the setting
			int paramIdx	= setting.index();
			setting.settingText(settingBT());
			columnH += settingH;
			columnH += frameTopPad;
			for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
				setting.optionText(optionBT(), optionIdx);
				setting.optionText(optionIdx).disabled(optionIdx == paramIdx);
				columnH	+= optionH;
			}
			numSettings++;
			columnH += frameEndPad;
		} else {
			setting.settingText(settingBT());			
			numSettings++;
			columnH += settingH;
		}
		settingList.add(setting);
	}
	protected void endOfColumn() {
		columnsMaxH = max(columnsMaxH, columnH);
		colSettingsCount.add(numSettings);
		numColumns++;
		numSettings	= 0;
		columnH		= 0;
	}
	public void open(BasePanel p) {
		parent = p;
		initialOptions = new MOO1GameOptions(); // Any content will do
		saveOptions(initialOptions);
		init();
		enableGlassPane(this);
	}
	private void close() {
		disableGlassPane();
	}
	private void saveOptions(MOO1GameOptions destination) {
//		for (InterfaceOptions param : settingList)
//			param.setOptions(destination);
		for (InterfaceOptions param : commonList)
			param.setOptions(destination);
		customPlayerRace.setOptions(destination);
	}
	private void getOptions(MOO1GameOptions source) {
//		for (InterfaceOptions param : settingList)
//			param.setFromOptions(source);
		for (InterfaceOptions param : commonList)
			param.setFromOptions(source);
		customPlayerRace.setFromOptions(source);
	}
	private void doExitBoxAction() {
		if (showOnly) {
			close();
			return;
		}
		if (ctrlPressed) // cancel = set to initial and close
			getOptions(initialOptions);
		else // save and exit
			saveLastOptions();
		close();			
	}
	private void doSelectBoxAction() {
		cr.pushSettings();
		customPlayerRace.set(true);
		saveLastOptions();
		close();
	}
	private void doDefaultBoxAction() {
		if (ctrlPressed) { // set to last
			MOO1GameOptions fileOptions = MOO1GameOptions.loadLastOptions();
			getOptions(fileOptions);
		} else
			setToDefault();
		init();
		repaint();
	}
	private void doUserBoxAction() {
		if (ctrlPressed)
			saveUserOptions();
		else { // Set
			MOO1GameOptions fileOptions = MOO1GameOptions.loadUserOptions();
			getOptions(fileOptions);
			init();
			repaint();
		}
	}	
	public void setToDefault() {
//		for (InterfaceOptions param : settingList)
//			param.setFromDefault();
		for (InterfaceOptions param : commonList)
			param.setFromDefault();
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
	private void checkCtrlKey(boolean pressed) {
		if (pressed != ctrlPressed) {
			ctrlPressed = pressed;
			repaint();
		}
	}
	private void randomizeRace() {
		cr.randomizeRace(randomMin.settingValue(), randomMax.settingValue(),
				randomTargetMin.settingValue(), randomTargetMax.settingValue(),
				randomUseTarget.settingValue(), randomSmoothEdges.settingValue(), true);
		totalCostText.repaint(totalCostStr());
	}
	private void paintSetting(Graphics2D g, SettingBase<?> setting) {
		if (setting.isSpacer()) {
			yLine += spacerH;
			return;
		}
		int sizePad	= frameSizePad;
		int endPad 	= frameEndPad;
		int optNum	= setting.boxSize();;
		float cost 	= setting.settingCost();
		BaseText bt	= setting.settingText();
		int paramId	= setting.index();

		if (optNum == 0) {
			endPad	= 0;
			sizePad	= 0;
		}
		settingBoxH	= optNum * optionH + sizePad;
		// frame
		g.setColor(frameC);
		g.drawRect(xLine, yLine - frameShift, wSetting, settingBoxH);
		g.setPaint(GameUI.settingsSetupBackground(w));
		bt.displayText(setting.guiSettingDisplayStr());
		g.fillRect(xLine + settingIndent/2, yLine -s12 + frameShift,
				bt.stringWidth(g) + settingIndent, s12);
		if (cost == 0) 
			bt.enabledC(settingC);
		else if (cost > 0)
			bt.enabledC(settingPosC);
		else
			bt.enabledC(settingNegC);		
		bt.setScaledXY(xLine + settingIndent, yLine);
		bt.draw(g);
		yLine += settingH;
		yLine += frameTopPad;
		// Options
		for (int optionId=0; optionId < optNum; optionId++) {
			bt = setting.optionText(optionId);
			bt.disabled(optionId == paramId);
			bt.displayText(setting.guiCostOptionStr(optionId));
			bt.setScaledXY(xLine + optionIndent, yLine);
			bt.draw(g);
			yLine += optionH;
		}				
		yLine += endPad;
	}
	private void goToNextSetting() {
		rowIndex++;
		if (rowIndex >= colSettingsCount.get(columnIndex)) {
			rowIndex = 0;
			columnIndex++;
			xLine = xLine + wSetting + columnPad;
			yLine = yTop;
		} else
			yLine += settingHPad;
	}
	private void updateBulletSetting(SettingBase<?> setting) {
		setting.guiSelect();
		totalCostText.repaint(totalCostStr());
	}
	private void mouseCommon(boolean up, boolean mid, boolean shiftPressed, boolean ctrlPressed
			, MouseEvent e, MouseWheelEvent w) {
//		for (int settingIdx=0; settingIdx < settingList.size(); settingIdx++) {
//			SettingBase<?> setting = settingList.get(settingIdx);
		for (int settingIdx=0; settingIdx < commonList.size(); settingIdx++) {
			SettingBase<?> setting = commonList.get(settingIdx);
			if (setting.isSpacer())
				continue;
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
//		for (SettingBase<?> setting : guiList) {
//			if (hoverBox == setting.settingText().bounds()) {
//				setting.toggle(e, w);
//				setting.settingText().repaint();
//				return;
//			}
//		}
	}
	// ========== Overriders ==========
	//
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		w	  = getWidth();
		h	  = getHeight();
		hBG	  = titlePad + columnsMaxH + bottomPad;
		topM  = (h - hBG)/2;
		yTop  = topM + titlePad; // First setting top position
		wBG	  = (wSetting + columnPad) * numColumns;
		leftM = (w - wBG)/2;
		if (showOnly) {
			leftM = Math.min(leftM, scaled(100));
		}
		yTitle	= topM + titleOffset;
		yButton	= topM + hBG - yButtonOffset;
		yCost	= yTitle + costOffset;
		xCost	= leftM + columnPad/2;
		xLine	= leftM + columnPad/2;
		yLine	= yTop;

		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		
		g.setPaint(GameUI.settingsSetupBackground(w));
		g.fillRect(leftM, topM, wBG, hBG);
		g.setFont(titleFont);
		String title;
		if (showOnly)
			title = text(showTitleID);
		else
			title = text(guiTitleID);

		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = leftM +(wBG-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		totalCostText.displayText(totalCostStr());
		totalCostText.setScaledXY(xCost, yCost);
		totalCostText.draw(g);
		
		// Loop thru the parameters
		columnIndex	 = 0;
		rowIndex	 = 0;
		xLine = leftM+s10;
		yLine = yTop;
		// First column (left)
		// Loop thru parameters

		Stroke prev = g.getStroke();
		g.setStroke(stroke2);
		for (SettingBase<?> setting : settingList) {
			paintSetting(g, setting);
			goToNextSetting();
		}
		g.setStroke(prev);

		int cnr = s5;
		g.setFont(buttonFont);
		// Exit Button
		String text = text(exitButtonKey(ctrlPressed));
		sw = g.getFontMetrics().stringWidth(text);
		int buttonW	= exitButtonWidth(g);
		xButton = leftM + wBG - buttonW - xButtonOffset;
		exitBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(exitBox.x, exitBox.y, buttonW, buttonH, cnr, cnr);
		int xT = exitBox.x+((exitBox.width-sw)/2);
		int yT = exitBox.y+exitBox.height-s8;
		Color cB = hoverBox == exitBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(exitBox.x, exitBox.y, exitBox.width, exitBox.height, cnr, cnr);
		g.setStroke(prev);

		// Select Button
		if (showOnly)
			return;
		text	 = text(selectKey);
		sw		 = g.getFontMetrics().stringWidth(text);
		buttonW	 = sw + buttonMargin;
		xButton -= (buttonW + buttonPad);
		selectBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(selectBox.x, selectBox.y, buttonW, buttonH, cnr, cnr);
		xT = selectBox.x+((selectBox.width-sw)/2);
		yT = selectBox.y+selectBox.height-s8;
		cB = hoverBox == selectBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(selectBox.x, selectBox.y, selectBox.width, selectBox.height, cnr, cnr);
		g.setStroke(prev);

		// Default Button
		text	 = text(defaultButtonKey(ctrlPressed));
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
        text	 = text(userButtonKey(ctrlPressed));
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
	@Override public void keyReleased(KeyEvent e) {
		checkCtrlKey(e.isControlDown());		
	}
	@Override public void keyPressed(KeyEvent e) {
		checkCtrlKey(e.isControlDown());		
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
	@Override public void mouseDragged(MouseEvent e) {  }
	@Override public void mouseMoved(MouseEvent e) {
		checkCtrlKey(e.isControlDown());		
		int x = e.getX();
		int y = e.getY();
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y))
			hoverBox = exitBox;
		else if (userBox.contains(x,y))
			hoverBox = userBox;

		if (!showOnly) {
			if (selectBox.contains(x,y))
				hoverBox = selectBox;
			else if (defaultBox.contains(x,y))
				hoverBox = defaultBox;
			else if (randomBox.contains(x,y))
				hoverBox = randomBox;
			else {
//				for (SettingBase<?> setting : guiList)
//					if (setting.settingText().contains(x,y))
//						hoverBox = setting.settingText().bounds();
				outerLoop1:
//				for ( SettingBase<?> setting : settingList) {
				for ( SettingBase<?> setting : commonList) {
					if (setting.isSpacer())
						continue;
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
		}

		if (hoverBox != prevHover) {
			if (!showOnly) {
//				for (SettingBase<?> setting : guiList)
//					if (prevHover == setting.settingText().bounds())
//						setting.settingText().mouseExit();
				outerLoop2:
				for ( SettingBase<?> setting : commonList) {
//				for ( SettingBase<?> setting : settingList) {
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
//				for (SettingBase<?> setting : guiList)
//					if (hoverBox == setting.settingText().bounds())
//						setting.settingText().mouseEnter();
				outerLoop3:
				for ( SettingBase<?> setting : commonList) {
//				for ( SettingBase<?> setting : settingList) {
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
			}
			if (prevHover != null) repaint(prevHover);
			if (hoverBox != null)  repaint(hoverBox);
		}
	}
	@Override public void mouseClicked(MouseEvent e) { }
	@Override public void mousePressed(MouseEvent e) { }
	@Override public void mouseReleased(MouseEvent e) {
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		if (hoverBox == exitBox) {
			doExitBoxAction();
			return;
		}
		if (showOnly)
			return;
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
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) {
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		if (showOnly)
			return;
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
