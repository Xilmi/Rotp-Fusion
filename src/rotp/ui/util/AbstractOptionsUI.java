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

import javax.swing.SwingUtilities;

import rotp.mod.br.profiles.Profiles;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.main.SystemPanel;
import rotp.util.LabelManager;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public abstract class AbstractOptionsUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private static final String exitKey		  = "SETTINGS_EXIT";
	private static final String cancelKey	  = "SETTINGS_CANCEL";
	private static final String setDefaultKey = "SETTINGS_DEFAULT";
	private static final String setGameKey	  = "SETTINGS_LAST_GAME";
	private static final String setLastKey	  = "SETTINGS_LAST_SET";
	public	static final String setUserKey	  = "SETTINGS_USER_SET";
	public	static final String saveUserKey	  = "SETTINGS_USER_SAVE";
	private final String guiTitleID;
	
	private Font descFont	= narrowFont(15);
	private static int columnPad	= s20;
	private static int smallButtonH = s30;
	public  static int smallButtonM = s30; // Margin for all GUI
	private static int hSetting	= s90;
	private static int lineH		= s17;
	private static int rowPad		= s20;
	private static int hDistSetting = hSetting + rowPad; // distance between two setting top corner
	private int leftM, rightM,topM, yTop;
	private int w, wBG, h, hBG;
	private int numColumns, numRows;
	private int yTitle, xDesc, yDesc, yButton;
	private int xSetting, ySetting, wSetting; // settings var
	private int index, column;
	
	private Color textC = SystemPanel.whiteText;
	private LinkedList<Integer>	lastRowList = new LinkedList<>();
	private LinkedList<BaseText> btList		= new LinkedList<>();
	public	LinkedList<InterfaceParam> paramList = new LinkedList<>();
	private Rectangle hoverBox;
	private Rectangle okBox		= new Rectangle();
	private Rectangle defaultBox= new Rectangle();
	private Rectangle userBox	= new Rectangle();
	private BasePanel parent;
	protected boolean globalOptions	= false; // No preferred button and Saved to remnant.cfg
	private MOO1GameOptions initialOptions; // To be restored if "cancel"
	private LinearGradientPaint bg;
	
	// ========== Constructors and initializers ==========
	//
	public AbstractOptionsUI(String guiTitle_ID) {
		guiTitleID = guiTitle_ID;
		init_0();
	}
	private void init_0() {
		setOpaque(false);
		textC = SystemPanel.whiteText;
		numColumns = 0;
		// Call for filling the settings
		init0();
		for (int i=0; i<paramList.size(); i++) {
			btList.add(newBT());
		}

		// numRows = Max column length
		numRows	 = lastRowList.getFirst();
		for (int i=1; i<lastRowList.size(); i++) {
			numRows = max(numRows, lastRowList.get(i)-lastRowList.get(i-1));
		}
		// Elements positioning
		int shiftTitle	= s40;
		int shiftButton	= s15;
		int topPad		= hSetting;
		int hSettings	= hDistSetting * numRows;

		leftM	= scaled(max(columnPad, 100 + (3-numColumns) * 150));
		rightM	= leftM;
		topM	= s45;
		yTitle	= topM + shiftTitle;
		yButton	= topM + topPad + hSettings - shiftButton;
		xDesc	= leftM + columnPad/2;
		yDesc	= yTitle + s20;
		
		// Special positioning for 6 rows
		if (numRows == 6) {
			// Shift the top
			topM		-= s30; // Margin reduction
			topPad		-= s40; // Push the settings up
			shiftTitle	-= s10; // Shift the title a little
			// Move the description to the buttons level
			yTitle	= topM + shiftTitle;
			yButton	= topM + topPad + hSettings - shiftButton;
			yDesc	= yButton + s20;
		}
		yTop	= topM + topPad; // First setting top position
		hBG		= topPad + hSettings + smallButtonH - s10;

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	private void init() {
		for (int i=0; i<paramList.size(); i++)
			btList.get(i).displayText(paramList.get(i).getGuiDisplay());
		initCustom();
	}
	protected void numColumns(int num) { numColumns = num; }
	protected void rowCountList(Integer... rows) {
		Integer id = 0;
		for (Integer row : rows) {
			id+= row;
			lastRowList.add(id);
		}
	}
	// ========== Abstract Methods Request ==========
	//
	protected abstract void init0();
	// ========== Former Abstract Methods Request ==========
	//
	// These may be left empty by full Auto GUI
	private void initCustom() {}
	private void paintCustomComponent(Graphics2D g) {}
	private void repaintCustomComponent() {}
	private void customMouseCommon(boolean up, boolean mid,
			boolean shiftPressed, boolean ctrlPressed, MouseEvent e, MouseWheelEvent w) {}
	// ========== Other Methods ==========
	//
	private  BaseText newBT() { 
		return new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
	}
	protected  void endOfColumn() {
		numColumns++;
		lastRowList.add(paramList.size());
	}
	public void open(BasePanel p) {
		parent = p;
		Modifier2KeysState.reset();
		w	= p.getWidth();
		h	= p.getHeight();
		wBG	= w - (leftM + rightM);
		wSetting = (wBG/numColumns)-columnPad;
		if (bg == null)
			if (numColumns>3)
				bg = GameUI.settingsSetupBackgroundW(w);
			else
				bg = GameUI.settingsSetupBackground(w);

		initialOptions = new MOO1GameOptions(); // Any content will do
		updateOptions(initialOptions);
		init();
		enableGlassPane(this);
	}
	private void close() {
		newGameOptions().galaxyShape().quickGenerate(); // BR: to get correct map preview 
		initialOptions = null;
		Modifier2KeysState.reset();
		disableGlassPane();
	}
	public void updateOptions(MOO1GameOptions destination) {
		for (InterfaceOptions param : paramList)
			param.setOptions(destination.dynamicOptions());
	}
	public void getOptions(MOO1GameOptions source) {
		for (InterfaceOptions param : paramList)
			param.setFromOptions(source.dynamicOptions());
	}
	private void doOkBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			getOptions(initialOptions);
			break;
		default: // Save
			if (globalOptions) // The old ways
				UserPreferences.save();
			else // The new ways
				saveLastOptions();
			break; 
		}
		close();			
	}
	private void doDefaultBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // set to last
			if (globalOptions) // The old ways
				UserPreferences.load();
			else // The new ways
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
		init();
		repaint();
	}
	public void setToDefault() {
		for (InterfaceOptions param : paramList)
			param.setFromDefault();
	}
	private void doUserBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Save
			saveUserOptions();
			break;
		default: // Set
			getOptions(MOO1GameOptions.loadUserOptions());
			init();
			repaint();
		}
	}
	private void saveUserOptions() {
		MOO1GameOptions fileOptions = MOO1GameOptions.loadUserOptions();
		updateOptions(fileOptions);
		MOO1GameOptions.saveUserOptions(fileOptions);
	}
	private void saveLastOptions() {
		MOO1GameOptions fileOptions = MOO1GameOptions.loadLastOptions();
		updateOptions(fileOptions);
		MOO1GameOptions.saveLastOptions(fileOptions);
		initialOptions = null;
	}
	public static String exitButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return cancelKey;
		default:
			return exitKey;	
		}
	}
	public static String okButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return cancelKey;
		default:
			return exitKey;
		}
	}
	public static String userButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return saveUserKey;
		default:
			return setUserKey;
		}
	}
	public static String defaultButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return setLastKey;
		case SHIFT:
			return setGameKey;
		default:
			return setDefaultKey;
		}
	}
	public static int exitButtonWidth(Graphics2D g) {
		return Math.max(g.getFontMetrics().stringWidth(LabelManager.current().label(cancelKey)),
						g.getFontMetrics().stringWidth(LabelManager.current().label(exitKey)))
				+ smallButtonM;
	}
	public static int userButtonWidth(Graphics2D g) {
		return Math.max(g.getFontMetrics().stringWidth(LabelManager.current().label(saveUserKey)),
						g.getFontMetrics().stringWidth(LabelManager.current().label(setUserKey)))
				+ smallButtonM;
	}
	public static int defaultButtonWidth(Graphics2D g) {
		return Math.max(g.getFontMetrics().stringWidth(LabelManager.current().label(setDefaultKey)),
						g.getFontMetrics().stringWidth(LabelManager.current().label(setLastKey)))
				+ smallButtonM;
	}
	private void checkModifierKey(InputEvent e) {
		if (Modifier2KeysState.checkForChange(e)) {
			repaintButtons();
		}
	}
	private void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawButtons(g);
		g.dispose();
	}
	private void drawButtons(Graphics2D g) {
		int cnr = s5;
		// draw settings button
		int smallButtonW = scaled(180);
		okBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(okBox.x, okBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		String text6 = text(okButtonKey());
		int sw6 = g.getFontMetrics().stringWidth(text6);
		int x6 = okBox.x+((okBox.width-sw6)/2);
		int y6 = okBox.y+okBox.height-s8;
		Color c6 = hoverBox == okBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text6, 2, x6, y6, GameUI.borderDarkColor(), c6);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(okBox.x, okBox.y, okBox.width, okBox.height, cnr, cnr);
		g.setStroke(prev);

		String text7 = text(defaultButtonKey());
		int sw7		 = g.getFontMetrics().stringWidth(text7);
		smallButtonW = defaultButtonWidth(g);
		defaultBox.setBounds(okBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
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

		if (globalOptions)
			return;  // No preferred button
		String text8 = text(userButtonKey());
		int sw8		 = g.getFontMetrics().stringWidth(text8);
		smallButtonW = userButtonWidth(g);
		userBox.setBounds(defaultBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(userBox.x, userBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		int x8 = userBox.x+((userBox.width-sw8)/2);
		int y8 = userBox.y+userBox.height-s8;
		Color c8 = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text8, 2, x8, y8, GameUI.borderDarkColor(), c8);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private void paintSetting(Graphics2D g, BaseText txt, String desc) {
		g.setColor(SystemPanel.blackText);
		g.drawRect(xSetting, ySetting, wSetting, hSetting);
		g.setPaint(bg);
		g.fillRect(xSetting+s10, ySetting-s10, txt.stringWidth(g)+s10,s30);
		txt.setScaledXY(xSetting+columnPad, ySetting+s7);
		txt.draw(g);
		g.setColor(SystemPanel.blackText);
		g.setFont(descFont);
		List<String> lines = wrappedLines(g, desc, wSetting-s30);
		int y3 = ySetting+s10;
		for (String line: lines) {
			y3 += lineH;
			drawString(g,line, xSetting+columnPad, y3);
		}		
	}
	private void goToNextSetting() {
		index++;
		if (index >= lastRowList.get(column)) {
			column++;
			xSetting = xSetting + wSetting + columnPad;
			ySetting = yTop;
		} else
			ySetting += hDistSetting;
	}
	private void mouseCommon(boolean up, boolean mid, boolean shiftPressed, boolean ctrlPressed
			, MouseEvent e, MouseWheelEvent w) {
		for (int i=0; i<paramList.size(); i++) {
			if (hoverBox == btList.get(i).bounds()) {
				paramList.get(i).toggle(e, w);
				btList.get(i).repaint(paramList.get(i).getGuiDisplay());
				return;
			}			
		}
		customMouseCommon(up, mid, shiftPressed, ctrlPressed, e, w);
	}
	// ========== Overriders ==========
	//
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		g.setPaint(bg);
		g.fillRect(leftM, topM, wBG, hBG);
		g.setFont(narrowFont(30));
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		g.setFont(narrowFont(18));
		String expl = text("SETTINGS_DESCRIPTION");
		g.setColor(SystemPanel.blackText);
		drawString(g, expl, xDesc, yDesc);
		
		Stroke prev = g.getStroke();
		g.setStroke(stroke3);
		
		// Loop thru the parameters
		index	 = 0;
		column	 = 0;
		xSetting = leftM+s10;
		ySetting = yTop;
		// First column (left)
		while (index<paramList.size()) {
			paintSetting(g, btList.get(index), paramList.get(index).getGuiDescription());
			goToNextSetting();
		}
		paintCustomComponent(g);

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
				doOkBoxAction();
				return;
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_ENTER:
				parent.advanceHelp();
				return;
			default: // BR:
				if(Profiles.processKey(k, e.isShiftDown(), guiTitleID, newGameOptions())) {
				};
				// Needs to be done twice for the case both Galaxy size
				// and the number of opponents were changed !?
				if(Profiles.processKey(k, e.isShiftDown(), guiTitleID, newGameOptions())) {
					for (int i=0; i<paramList.size(); i++) {
						btList.get(i).repaint(paramList.get(i).getGuiDisplay());
					}
					repaintCustomComponent();
				};
				return;
		}
	}
	@Override public void mouseDragged(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {
		checkModifierKey(e);
		int x = e.getX();
		int y = e.getY();
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (okBox.contains(x,y))
			hoverBox = okBox;
		else if (defaultBox.contains(x,y))
			hoverBox = defaultBox;
		else if (userBox.contains(x,y))
			hoverBox = userBox;
		else for (BaseText txt : btList) {
			if (txt.contains(x,y)) {
				hoverBox = txt.bounds();
				break;
			}
		}
		if (hoverBox != prevHover) {
			for (BaseText txt : btList) {
				if (prevHover == txt.bounds()) {
					txt.mouseExit();
					break;
				}
			}
			for (BaseText txt : btList) {
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
		boolean up	= !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
		boolean mid	= !SwingUtilities.isMiddleMouseButton(e); // BR: added reset click
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed  = e.isControlDown();
		mouseCommon(up, mid, shiftPressed, ctrlPressed, e, null);
		if (hoverBox == okBox)
			doOkBoxAction();
		else if (hoverBox == defaultBox)
			doDefaultBoxAction();
		else if (hoverBox == userBox)
			doUserBoxAction();
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
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed  = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
