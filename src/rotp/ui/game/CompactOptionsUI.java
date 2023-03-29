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

import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.InterfaceParam;
import rotp.util.FontManager;
import rotp.util.ModifierKeysState;

public class CompactOptionsUI extends BaseModPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private String guiTitleID;
	private String GUI_ID;
	
	private static final int rowPad		= s10;
	private	static final int descPadV	= 0;
	private	static final int descPadM	= s10;
	private static final int buttonPadV	= rowPad;
	private static final Color descColor		= SystemPanel.blackText;
	private static final Color disabledColor	= GameUI.textColor();
	private static final Color enabledColor		= GameUI.labelColor();
	private static final Color defaultValuesColor	= SystemPanel.whiteText;
	private static final Color customValuesColor	= Color.orange;
	private static final int descLineH		= 18;
	private	static final Font descFont		= FontManager.current().narrowFont(16);
	private	static final Font titleFont		= FontManager.current().narrowFont(30);
	private static final int titleOffset	= s40; // Offset from Margin
	private static final int titlePad		= s70; // Offset of first setting
	private static final int settingFont	= 20;
	private static final int settingH		= s20;
	private static final int settingpadH	= s5;
	private static final int columnPad		= s12;
	private static final int tooltipLines	= 2;
	private static final int descHeigh		= tooltipLines * descLineH + descPadM;
	private static final int bottomPad		= rowPad;
	private static final int textBoxH		= settingH;
	private static final int hDistSetting	= settingH + settingpadH; // distance between two setting top corner
//	private static final int leftM			= columnPad;
//	private static final int rightM			= leftM;
	private int leftM, rightM;
	private int topM, yTop;
	private int wBG, hBG;
	private int numColumns, numRows;
	private int yTitle, yButton;
	private int xSetting, ySetting, columnWidth; // settings var
	private int index, column;
	private int xDesc, yDesc, descWidth;
	private int x, y; // mouse position
	
	private LinkedList<Integer>	lastRowList;
	private LinkedList<BaseText> btList0;
	private LinkedList<BaseText> btList2;
	private LinkedList<BaseText> btListBoth;
	private static Rectangle hoverBox, prevHover;
	private static final Rectangle exitBox		= new Rectangle();
	private static final Rectangle toolTipBox	= new Rectangle();
	private LinearGradientPaint bg;

	private String tooltipText = "";
	private String preTipTxt   = "";
	private int parent = 0; // 0=Base; 1=Merged; 2=Classic
	
	// ========== Constructors and initializers ==========
	//
	public CompactOptionsUI(String guiTitle_ID, String guiId, LinkedList<LinkedList<InterfaceParam>> paramList) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
		init_Lists(paramList);
		init_0();
	}
	private void init_Lists(LinkedList<LinkedList<InterfaceParam>> optionsList) {
		activeList		= new LinkedList<>();
		duplicateList	= new LinkedList<>();
		paramList		= new LinkedList<>();
		btList0			= new LinkedList<>();
		btList2			= new LinkedList<>();
		btListBoth		= new LinkedList<>();
		lastRowList		= new LinkedList<>();
		int totalRows   = 0;
		numColumns = optionsList.size();
		numRows    = 0;
		for (LinkedList<InterfaceParam> list : optionsList) {
			totalRows += list.size();
			lastRowList.add(totalRows);
			numRows = max(numRows, list.size());
			for (InterfaceParam param : list) {
				if (param != null) {
					activeList.add(param);
					btList0.add(newBT(param.isTitle()));
					btList2.add(newBT2(param.isDefaultValue()));
					if (param.isDuplicate())
						duplicateList.add(param);
					else
						paramList.add(param);
				}
			}
		}
		btListBoth.addAll(btList0);
		btListBoth.addAll(btList2);
	}
	private void init_0() {
		setOpaque(false);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	// ========== Other Methods ==========
	//
	private  BaseText newBT(boolean disabled) {
		BaseText bt = new BaseText(this, false, settingFont, 0, 0,
				enabledColor, disabledColor, hoverC, depressedC, enabledColor, 0, 0, 0);
		bt.disabled(disabled);
		return bt;
	}
	private  BaseText newBT2(boolean isDefault) {
		BaseText bt;
		if (isDefault)
			bt = new BaseText(this, false, settingFont, 0, 0,
				defaultValuesColor, disabledColor, hoverC, depressedC, disabledColor, 0, 0, 0);
		else
			bt = new BaseText(this, false, settingFont, 0, 0,
					customValuesColor, disabledColor, hoverC, depressedC, disabledColor, 0, 0, 0);

		return bt;
	}
	private void drawButtons(Graphics2D g) {
		int cnr = s5;
		// draw settings button
		int smallButtonW = scaled(180);
		exitBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(exitBox.x, exitBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
		String text = text(exitButtonKey());
		int sw = g.getFontMetrics().stringWidth(text);
		int x = exitBox.x+((exitBox.width-sw)/2);
		int y = exitBox.y+exitBox.height-s8;
		Color c = hoverBox == exitBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(exitBox.x, exitBox.y, exitBox.width, exitBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(defaultButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = defaultButtonWidth(g);
		defaultBox.setBounds(exitBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(defaultBox.x, defaultBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
		x = defaultBox.x+((defaultBox.width-sw)/2);
		y = defaultBox.y+defaultBox.height-s8;
		c = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(lastButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = defaultButtonWidth(g);
		lastBox.setBounds(defaultBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(lastBox.x, lastBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
		x = lastBox.x+((lastBox.width-sw)/2);
		y = lastBox.y+lastBox.height-s8;
		c = hoverBox == lastBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);
		g.setStroke(prev);

		text = text(userButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = userButtonWidth(g);
		userBox.setBounds(lastBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private void paintDescriptions(Graphics2D g) {
		List<String> lines = wrappedLines(g, tooltipText, descWidth-2*descPadM);
		g.setFont(descFont);
		toolTipBox.setBounds(xDesc, yDesc, descWidth, descHeigh);
		g.setColor(GameUI.setupFrame());
		g.fill(toolTipBox);
		g.setColor(descColor);
		int xT = xDesc+descPadM;
		int yT = yDesc + s4;
		for (String line: lines) {
			yT += descLineH;
			drawString(g,line, xT, yT);
		}		
	}
	private void repaintTooltip() {
		Graphics2D g = (Graphics2D) getGraphics();
		super.paintComponent(g);
		paintDescriptions(g);
		g.dispose();
	}
	private void setValueColor(int index) {
		BaseText txt2 = btList2.get(index);
		if (activeList.get(index).isDefaultValue())
			txt2.enabledC(defaultValuesColor);
		else
			txt2.enabledC(customValuesColor);
	}
	private void paintSetting(Graphics2D g) { // TODO
		setValueColor(index);
		BaseText txt0 = btList0.get(index);
		BaseText txt2 = btList2.get(index);
		g.setPaint(bg);
		int sw0 = txt0.stringWidth(g);
		int sw2 = txt2.stringWidth(g);
		int sw = sw0 + sw2;
		int dx = (columnWidth - sw)/2;
		g.fillRect(xSetting, ySetting-rowPad, columnWidth, textBoxH);
		txt0.setScaledXY(xSetting+dx, ySetting+s7);
		txt2.setScaledXY(xSetting+dx+sw0, ySetting+s7);
		txt0.draw(g);
		txt2.draw(g);
	}
	private void goToNextSetting() {
		index++;
		if (index >= lastRowList.get(column)) {
			column++;
			xSetting = xSetting + columnWidth + columnPad;
			ySetting = yTop;
		} else
			ySetting += hDistSetting;
	}
	private void mouseCommon(MouseEvent e, MouseWheelEvent w) {
		for (int i=0; i<activeList.size(); i++) {
			if (hoverBox == btList0.get(i).bounds()
					|| hoverBox == btList2.get(i).bounds() ) {
				if (activeList.get(i).isSubMenu()) {
					if (e == null)
						return;
					super.close();
			        disableGlassPane();
					activeList.get(i).toggle(e, 1);
					return;
				}			
				activeList.get(i).toggle(e, w);
				setValueColor(i);
				btList0.get(i).repaint(activeList.get(i).getGuiDisplay(0));
				btList2.get(i).repaint(activeList.get(i).getGuiDisplay(1));
				return;
			}			
		}
	}
	private void setLocalToDefault() {
		for (InterfaceOptions param : activeList)
			param.setFromDefault();
	}
	private boolean checkForHoveredButtons() {
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
		}
		return false;
	}
	private boolean checkForHoveredSettings() {
		BaseText bt0, bt2;
		for (int idx=0; idx<btList0.size(); idx++) {
			bt0 = btList0.get(idx);
			if (bt0.contains(x,y)) {
				hoverBox = bt0.bounds();
				tooltipText = activeList.get(idx).getGuiDescription();
				if (hoverBox != prevHover) {
					bt0.mouseEnter();
					if (tooltipText.equals(preTipTxt)) { 
						repaint(hoverBox);
					} else {
						repaint(hoverBox);
						repaintTooltip();
					}
				}
				return true;
			}
			bt2 = btList2.get(idx);
			if (bt2.contains(x,y)) {
				hoverBox = bt2.bounds();
				tooltipText = activeList.get(idx).getGuiDescription();
				if (hoverBox != prevHover) {
					bt2.mouseEnter();
					setValueColor(idx);
					if (tooltipText.equals(preTipTxt)) { 
						repaint(hoverBox);
					} else {
						repaint(hoverBox);
						repaintTooltip();
					}
				}
				return true;
			}
		}
		return false;
	}
	private void checkExitSettings(LinkedList<BaseText> baseTextList) {
		for (BaseText setting : baseTextList) {
			if (prevHover == setting.bounds()) {
				setting.mouseExit();
				return;
			}
		}
	}
	private void hoverAndTooltip(boolean keyModifierChanged) {
		if (btList0 == null) {
			System.out.println("CompactOptionsUI: btList is null");
			return;
		}
		preTipTxt = tooltipText;
		tooltipText = "";
		prevHover = hoverBox;
		hoverBox = null;
		// Check if cursor is in a box
		boolean onButton = checkForHoveredButtons();
		boolean onBox = onButton || checkForHoveredSettings();

		if (prevHover != hoverBox && prevHover != null) {
			checkExitSettings(btListBoth);
			repaint(prevHover);
		}
		if (keyModifierChanged) {
			repaintButtons();
			if (!tooltipText.equals(preTipTxt))
				repaintTooltip();
		}
		else if (!onBox && prevHover != null) {
			if (!tooltipText.equals(preTipTxt))
				repaintTooltip();
		}
	}
	public void start(boolean callFromGame) { // Called from Base UI
		RotPUI.guiCallFromGame(callFromGame);
		parent = 0;
		start();
	}
	public void start(int p) { // Called from subUI
		parent = p;
		start();
	}
	public void start() { // Called from subUI
		super.init();
		hoverBox = null;
		prevHover = null;
			
		int hSettingTotal = hDistSetting * numRows;
		hBG	= titlePad + hSettingTotal + descPadV + descHeigh + buttonPadV + smallButtonH + bottomPad;
		hBG	= titlePad + hSettingTotal + descHeigh + buttonPadV + smallButtonH + bottomPad;

		leftM  = columnPad;
		rightM = leftM;		
		wBG	= w - (leftM + rightM);
		columnWidth = ((wBG-columnPad)/4) - columnPad; // Max Width allowed
		if (numColumns < 4) { // to adjust the panel width
			wBG = (columnWidth + columnPad) * 3 + columnPad; // below 3 the buttons will be squeezed!
			leftM = (w - wBG)/2;
			rightM = leftM;		
		}
		columnWidth = ((wBG-columnPad)/numColumns) - columnPad;
		
		topM	= (h - hBG) / 2;
		yTitle	= topM + titleOffset;
		yTop	= topM + titlePad; // First setting top position
		yButton	= topM + hBG - (smallButtonH + bottomPad);
		descWidth	= wBG - 2 * columnPad;
		xDesc		= leftM + columnPad;		
		yDesc		= topM + hBG - ( descHeigh + buttonPadV + smallButtonH + bottomPad);
		
		if (bg == null)
			bg = GameUI.settingsSetupBackgroundW(w);
		updateOptionsAndSaveToFileName(RotPUI.mergedGuiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		enableGlassPane(this);
		refreshGui();
	}
	// ========== Overriders ==========
	//
	@Override public boolean checkModifierKey(InputEvent e) {
		boolean change = checkForChange(e);
		hoverAndTooltip(change);
		return change;

	}
	@Override protected void close() {
		super.close();
		hoverBox = null;
		prevHover = null;
        disableGlassPane();
		switch (parent) {
		case 1:
			RotPUI.mergedDynamicOptionsUI().start(RotPUI.guiCallFromGame());
			return;
		case 2:
			RotPUI.modOptionsDynamicA().init();
			return;
		case 0:
		default: 
	        if (!guiCallFromGame())
	        	RotPUI.setupGalaxyUI().init();
	        else
	        	RotPUI.instance().mainUI().map().resetRangeAreas();
		}
	}
	@Override protected void doExitBoxAction() {
		UserPreferences.save();
		// super.doExitBoxAction();
		updateOptionsAndSaveToFileName(RotPUI.mergedGuiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		if (parent == 0) // Sub UI should not change this
			RotPUI.guiCallFromGame(false);
		close();
	}
	@Override protected void doUserBoxAction() {
		switch (ModifierKeysState.get()) {
		case CTRL: // saveGlobalUserKey
		case CTRL_SHIFT: // saveLocalUserKey
			UserPreferences.save();
			break;
		case SHIFT: // setLocalUserKey
		default: // setGlobalUserKey
			UserPreferences.load();
			break; 
		}
		super.doUserBoxAction();
	}
	@Override protected void doDefaultBoxAction() {
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // cancelKey
			UserPreferences.load();
			break;
		default: // setLocalDefaultKey
			setLocalToDefault();
			break; 
		}
		super.doDefaultBoxAction();
	}
	@Override protected void doLastBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case SHIFT: // setLocalLastKey
		default: // setGlobalLastKey
			UserPreferences.load();
		}
		super.doLastBoxAction();
	}
	@Override protected void refreshGui() {
		super.refreshGui();
		for (int i=0; i<activeList.size(); i++) {
			setValueColor(i);
			btList0.get(i).displayText(activeList.get(i).getGuiDisplay(0));
			btList2.get(i).displayText(activeList.get(i).getGuiDisplay(1));
		}
		repaint();
	}
	@Override public void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawButtons(g);
		g.dispose();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		g.setPaint(bg);
		g.fillRect(leftM, topM, wBG, hBG);
		
		// Tool tip
		paintDescriptions(g);

		// Title
		g.setFont(titleFont);
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		Stroke prev = g.getStroke();
		g.setStroke(stroke3);
		// Loop thru the parameters
		index	 = 0;
		column	 = 0;
		xSetting = leftM + columnPad/2;
		ySetting = yTop;
		while (index<activeList.size()) {
			paintSetting(g);
			goToNextSetting();
		}
		g.setStroke(prev);

		drawButtons(g);
		
	}
	@Override public void keyReleased(KeyEvent e) {
		checkModifierKey(e);		
	}
	@Override public void keyPressed(KeyEvent e) {
		checkModifierKey(e);
		int k = e.getKeyCode();  // BR:
		switch(k) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
		}
	}
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		checkModifierKey(e);
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y))
			hoverBox = exitBox;
		else if (defaultBox.contains(x,y))
			hoverBox = defaultBox;
		else if (userBox.contains(x,y))
			hoverBox = userBox;
		else if (lastBox.contains(x,y))
			hoverBox = lastBox;
		else for (BaseText txt : btListBoth) {
			if (txt.contains(x,y)) {
				hoverBox = txt.bounds();
				break;
			}
		}
		if (hoverBox != prevHover) {
			for (BaseText txt : btListBoth) {
				if (prevHover == txt.bounds()) {
					txt.mouseExit();
					break;
				}
			}
			for (BaseText txt : btListBoth) {
				if (hoverBox == txt.bounds()) {
					txt.mouseEnter();
					break;
				}
			}			
			if (prevHover != null) repaint(prevHover);
			if (hoverBox != null)  repaint(hoverBox);
		}
	}
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {
		checkModifierKey(e);
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		mouseCommon(e, null);
		if (hoverBox == exitBox) {
			doExitBoxAction();
			switch (parent) { // To reset the buttons on the parent panel!
			case 1:
				RotPUI.mergedDynamicOptionsUI().mouseMoved(e);
				return;
			case 2:
				RotPUI.modOptionsDynamicA().mouseMoved(e);
				return;
			case 0:
			default:
				return;
			}
		}
		else if (hoverBox == defaultBox)
			doDefaultBoxAction();
		else if (hoverBox == userBox)
			doUserBoxAction();
		else if (hoverBox == lastBox)
			doLastBoxAction();
	}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		checkModifierKey(e);
		mouseCommon(null, e);
	}
}
