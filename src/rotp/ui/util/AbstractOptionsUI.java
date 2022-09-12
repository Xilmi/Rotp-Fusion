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
import java.awt.Rectangle;
import java.awt.Stroke;
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
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.main.SystemPanel;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public abstract class AbstractOptionsUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private static final String exitKey		  = "SETTINGS_EXIT";
	public  static final String cancelKey	  = "SETTINGS_CANCEL";
	public  static final String setDefaultKey = "SETTINGS_DEFAULT";
	public  static final String setLastKey	  = "SETTINGS_LAST_SET";
	public  static final String setUserKey	  = "SETTINGS_USER_SET";
	public  static final String saveUserKey	  = "SETTINGS_USER_SAVE";
	private final String guiTitleID;
	
	private Font descFont    = narrowFont(15);
	private static int columnPad    = s20;
	private static int smallButtonH = s30;
	private static int smallButtonM = s30; // Margin for all GUI
	private static int hSetting 	 = s90;
	private static int lineH		 = s17;
	private static int rowPad		 = s20;
	private static int hDistSetting = hSetting + rowPad; // distance between two setting top corner
	private int leftM, rightM,topM, yTop;
	private int w, wBG, h, hBG;
	private int numColumns, numRows;
	private int yTitle, xDesc, yDesc, yButton;
	private int xSetting, ySetting, wSetting; // settings var
	private int index, column;
	
	private Color textC = SystemPanel.whiteText;
	private LinkedList<Integer>	lastRowList = new LinkedList<Integer>();
	protected LinkedList<BaseText>	btList		= new LinkedList<BaseText>();
	protected LinkedList<AbstractParam<?>> paramList = new LinkedList<AbstractParam<?>>();
	protected Rectangle hoverBox;
	private   Rectangle okBox 		= new Rectangle();
	private   Rectangle defaultBox	= new Rectangle();
	private   Rectangle userBox		= new Rectangle();
	private   BasePanel parent;
	private   boolean ctrlPressed	= false;
	protected boolean global		= false; // No preferred button
	
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
			topM 		-= s30; // Margin reduction
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
	// ========== Abstract Methods Request ==========
	//
	protected abstract void init0();
	// These may be left empty by full Auto GUI
	protected abstract void initCustom();
	protected abstract void paintCustomComponent(Graphics2D g);
	protected abstract void repaintCustomComponent();
	protected abstract void customMouseCommon(boolean up, boolean mid,
			boolean shiftPressed, boolean ctrlPressed, MouseEvent e, MouseWheelEvent w);
	// ========== Other Methods ==========
	//
	protected  BaseText newBT() { 
		return new BaseText(this, false, 20, 20,-78,  textC, textC, hoverC, depressedC, textC, 0, 0, 0);
	}
	protected  void endOfColumn() {
		numColumns++;
		lastRowList.add(btList.size());
	}
	public void open(BasePanel p) {
		parent = p;
		init();
		enableGlassPane(this);
	}
	private void close() {
		disableGlassPane();
	}
	private void doOkBoxAction() {
		if (ctrlPressed) { // Exit without saving
			close();
		} else { // save and exit
			newGameOptions().saveLastOptions(paramList); // TODO BR: Non param options
			close();
		}
	}
	private void doDefaultBoxAction() {
		if (ctrlPressed) { // set to last
			newGameOptions().setUserOptions(paramList); // TODO BR: Non param options
			init();
			repaint();
		} else { // set to default
			UserPreferences.setFromDefault(paramList); // TODO BR: Non param options
			init();
			repaint();
		}
	}
	private void doUserBoxAction() {
		if (ctrlPressed) { // set
			newGameOptions().setUserOptions(paramList); // TODO BR: Non param options
			init();
			repaint();
		} else { // Save
			newGameOptions().saveUserOptions(paramList); // TODO BR: Non param options
		}
	}
	public static String okButtonKey(boolean ctrlPressed) {
		if (ctrlPressed)
			return cancelKey;
		else
			return exitKey;			
	}
	public static String userButtonKey(boolean ctrlPressed) {
		if (ctrlPressed)
			return saveUserKey;
		else
			return setUserKey;			
	}
	public static String defaultButtonKey(boolean ctrlPressed) {
		if (ctrlPressed)
			return setLastKey;
		else
			return setDefaultKey;			
	}
	public static int userButtonWidth(Graphics2D g) {
        return Math.max(g.getFontMetrics().stringWidth(saveUserKey),
        				g.getFontMetrics().stringWidth(setUserKey))
        		+ smallButtonM;
	}
	public static int defaultButtonWidth(Graphics2D g) {
        return Math.max(g.getFontMetrics().stringWidth(setDefaultKey),
        				g.getFontMetrics().stringWidth(setLastKey))
        		+ smallButtonM;
	}
	private void checkCtrlKey(boolean pressed) {
		if (pressed != ctrlPressed) {
			ctrlPressed = pressed;
			repaint();
		}
	}
	protected void paintSetting(Graphics2D g, BaseText txt, String desc) {
		g.setColor(SystemPanel.blackText);
		g.drawRect(xSetting, ySetting, wSetting, hSetting);
		g.setPaint(GameUI.settingsSetupBackground(w));
		g.fillRect(xSetting+s10, ySetting-s10, txt.stringWidth(g)+s10,s30);
		txt.setScaledXY(xSetting+columnPad, ySetting+s7);
		txt.draw(g);
		g.setColor(SystemPanel.blackText);
		g.setFont(descFont);
		List<String> lines = this.wrappedLines(g, desc, wSetting-s30);
		int y3 = ySetting+s10;
		for (String line: lines) {
			y3 += lineH;
			drawString(g,line, xSetting+columnPad, y3);
		}		
	}
	protected void goToNextSetting() {
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
		w	= getWidth();
		h	= getHeight();
		wBG	= w - (leftM + rightM);
		wSetting = (wBG/numColumns)-columnPad;
		
		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		
		g.setPaint(GameUI.settingsSetupBackground(w));
		g.fillRect(leftM, topM, wBG, hBG);
		g.setFont(narrowFont(30));
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2; // BR: put it in the center
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

		// draw settings button
		int cnr = s5;
		int smallButtonW = scaled(180);
		okBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(okBox.x, okBox.y, smallButtonW, smallButtonH, cnr, cnr);
		g.setFont(narrowFont(20));
		String text6 = text(okButtonKey(ctrlPressed));
		int sw6 = g.getFontMetrics().stringWidth(text6);
		int x6 = okBox.x+((okBox.width-sw6)/2);
		int y6 = okBox.y+okBox.height-s8;
		Color c6 = hoverBox == okBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text6, 2, x6, y6, GameUI.borderDarkColor(), c6);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(okBox.x, okBox.y, okBox.width, okBox.height, cnr, cnr);
		g.setStroke(prev);

		String text7 = text(defaultButtonKey(ctrlPressed));
        int sw7		 = g.getFontMetrics().stringWidth(text7);
		smallButtonW = defaultButtonWidth(g) + s30;
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

		if (global)
			return;  // No preferred button
		String text8 = text(userButtonKey(ctrlPressed));
        int sw8 	 = g.getFontMetrics().stringWidth(text8);
		smallButtonW = userButtonWidth(g) + s30;
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
	@Override public void keyReleased(KeyEvent e) {
		checkCtrlKey(e.isControlDown());		
	}
	@Override public void keyPressed(KeyEvent e) {
		checkCtrlKey(e.isControlDown());
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
		checkCtrlKey(e.isControlDown());
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
		checkCtrlKey(e.isControlDown());
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		boolean up	= !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
		boolean mid	= !SwingUtilities.isMiddleMouseButton(e); // BR: added reset click
		boolean shiftPressed = e.isShiftDown();
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
//		checkCtrlKey(e.isControlDown());
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		checkCtrlKey(e.isControlDown());
		// modnar: mouse scroll for custom difficulty, with Shift/Ctrl modifiers
		boolean shiftPressed = e.isShiftDown(); // BR: updated deprecated method
//		boolean ctrlPressed = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
