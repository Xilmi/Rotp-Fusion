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
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;

import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.InterfaceParam;
import rotp.util.FontManager;
import rotp.util.ModifierKeysState;

public class CompactOptionsUI extends BaseModPanel implements MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private String guiTitleID;
	private String GUI_ID;
	
	private static final int rowPad		= s10;
	private	static final int descPadV	= 0;
	private	static final int descPadM	= s5;
	private static final int buttonPadV	= rowPad;
	private static final Color disabledColor		= GameUI.textColor();
	private static final Color enabledColor			= GameUI.labelColor();
	private static final Color defaultValuesColor	= SystemPanel.whiteText;
	private static final Color customValuesColor	= Color.orange;
	private static final int descLineH		= s18;
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
	private		   final Box exitBox		= new Box(exitKey);
	private		   final JTextPane descBox	= new JTextPane();
	private int leftM, rightM;
	private int topM, yTop;
	private int wBG, hBG;
	private int numColumns, numRows;
	private int yTitle, yButton;
	private int xSetting, ySetting, columnWidth; // settings var
	private int index, column;
	private int xDesc, yDesc, descWidth;
	
	private LinkedList<Integer>	lastRowList;
	private LinkedList<ModText> btList0; // left part
	private LinkedList<ModText> btList2; // right part
	private LinkedList<ModText> btListBoth;
	private LinearGradientPaint bg;

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
					btList0.add(newBT(param.isTitle()).initGuide(param));
					btList2.add(newBT2(param.isDefaultValue()).initGuide(param));
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
		add(descBox);
		descBox.setOpaque(true);
		descBox.setContentType("text/html");
		descBox.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	// ========== Other Methods ==========
	//
	private  ModText newBT(boolean disabled) {
//		ModText bt = new ModText(this, false, settingFont, 0, 0,
//				enabledColor, disabledColor, hoverC, depressedC, enabledColor, 0, 0, 0);
		ModText bt = new ModText(this, settingFont, enabledColor,
				disabledColor, hoverC, depressedC, enabledColor, true);
		bt.disabled(disabled);
		return bt;
	}
	private  ModText newBT2(boolean isDefault) {
		ModText bt;
		if (isDefault)
			bt = new ModText(this, settingFont, defaultValuesColor, 
					disabledColor, hoverC, depressedC, disabledColor, true);
//			bt = new ModText(this, false, settingFont, 0, 0,
//				defaultValuesColor, disabledColor, hoverC, depressedC, disabledColor, 0, 0, 0);
		else
			bt = new ModText(this, settingFont, customValuesColor,
					disabledColor, hoverC, depressedC, disabledColor, true);
//			bt = new ModText(this, false, settingFont, 0, 0,
//					customValuesColor, disabledColor, hoverC, depressedC, disabledColor, 0, 0, 0);
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

		text = text(guideButtonKey());
		sw	 = g.getFontMetrics().stringWidth(text);
		smallButtonW = guideButtonWidth(g);
		guideBox.setBounds(leftM+s9, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(guideBox.x, guideBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(smallButtonFont);
		x = guideBox.x+((guideBox.width-sw)/2);
		y = guideBox.y+guideBox.height-s8;
		c = hoverBox == guideBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private void paintDescriptions(Graphics2D g) {
		descBox.setFont(descFont);
		descBox.setBackground(GameUI.setupFrame());
		descBox.setBounds(xDesc, yDesc, descWidth, descHeigh);
	}
	private void setValueColor(int index) {
		ModText txt2 = btList2.get(index);
		if (activeList.get(index).isDefaultValue())
			txt2.enabledC(defaultValuesColor);
		else
			txt2.enabledC(customValuesColor);
	}
	private void paintSetting(Graphics2D g) {
		setValueColor(index);
		ModText txt0 = btList0.get(index);
		ModText txt2 = btList2.get(index);
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
			if (hoverBox == btList0.get(i).box()
					|| hoverBox == btList2.get(i).box() ) {
				if (activeList.get(i).isSubMenu()) {
					if (e == null)
						return;
					super.close();
			        disableGlassPane();
					activeList.get(i).toggle(e, 1, this);
					return;
				}			
				activeList.get(i).toggle(e, w, this);
				setValueColor(i);
				btList0.get(i).repaint(activeList.get(i).getGuiDisplay(0));
				btList2.get(i).repaint(activeList.get(i).getGuiDisplay(1));
				if (autoGuide) {
					loadGuide();
					repaint();
				}
				return;
			}			
		}
	}
	private void setLocalToDefault() {
		for (InterfaceOptions param : activeList)
			param.setFromDefault();
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
	private void start() { // Called from subUI
		super.init();
		hoverBox = null;
		prevHover = null;
		descBox.setVisible(true);
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
	@Override protected void doExitBoxAction()		{
		UserPreferences.save();
		updateOptionsAndSaveToFileName(RotPUI.mergedGuiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		if (parent == 0) // Sub UI should not change this
			RotPUI.guiCallFromGame(false);
		close();
	}
	@Override protected void doUserBoxAction()		{
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
	@Override protected void doDefaultBoxAction()	{
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
	@Override protected void doLastBoxAction()		{
		buttonClick();
		switch (ModifierKeysState.get()) {
		case SHIFT: // setLocalLastKey
		default: // setGlobalLastKey
			UserPreferences.load();
		}
		super.doLastBoxAction();
	}
	@Override protected void refreshGui()	{
		super.refreshGui();
		for (int i=0; i<activeList.size(); i++) {
			setValueColor(i);
			btList0.get(i).displayText(activeList.get(i).getGuiDisplay(0));
			btList2.get(i).displayText(activeList.get(i).getGuiDisplay(1));
		}
		repaint();
	}
	@Override public void repaintButtons()	{
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawButtons(g);
		g.dispose();
	}
	@Override protected String GUI_ID()		{ return GUI_ID; }
	@Override public void paintComponent(Graphics g0)	{
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
		showGuide(g);		
	}
	@Override public void keyReleased(KeyEvent e)		{
		if(checkModifierKey(e))
			if(hoverBox != null)
				descBox.setText(hoverBox.getDescription());
	}
	@Override public void keyPressed(KeyEvent e)		{
		if(checkModifierKey(e))
			if(hoverBox != null)
				descBox.setText(hoverBox.getDescription());
		super.keyPressed(e);
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				doExitBoxAction();
				return;
		}
	}
	@Override public void mouseEntered(MouseEvent e)	{
		for (int i=0; i<activeList.size(); i++) {
			if (hoverBox == btList0.get(i).box()
					|| hoverBox == btList2.get(i).box() ) {
				setValueColor(i);
				btList0.get(i).repaint(activeList.get(i).getGuiDisplay(0));
				btList2.get(i).repaint(activeList.get(i).getGuiDisplay(1));
				if (autoGuide) {
					loadGuide();
					repaint();
				}
				return;
			}			
		}
	}
	@Override public void mouseMoved(MouseEvent e)		{
		mX = e.getX();
		mY = e.getY();
		if (hoverBox != null && hoverBox.contains(mX,mY)) {
			hoverChanged = false;
			return;
		}
		prevHover = hoverBox;
		hoverBox = null;
		hoverChanged = true;
		for (Box box : boxBaseList)
			if (box.checkIfHovered(descBox))
				break;
		if (prevHover != null) {
			prevHover.mouseExit();
			if (hoverBox == null)
				descBox.setText("");
			loadGuide();
			repaint();
		}
	}
	@Override public void mouseReleased(MouseEvent e)	{
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
		else if (hoverBox == guideBox)
			doGuideBoxAction();
		else if (hoverBox == lastBox)
			doLastBoxAction();
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) { mouseCommon(null, e); }
}
