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

import javax.swing.SwingUtilities;

import rotp.mod.br.profiles.Profiles;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.main.SystemPanel;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public abstract class AbstractConfigUI extends BasePanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static final Color backgroundHaze = new Color(0,0,0,160);
	private final String guiTitleID;
	private final int SETTING_IDX	= 0;
	
	private final Color textC		= SystemPanel.whiteText;
	private final Font buttonFont	= narrowFont(20);
	private final int buttonW		= scaled(180);
	private final int buttonH		= s30;
	private final int xButtonOffset	= s30;
	private final int yButtonOffset	= s40;

	private final Color costC		= SystemPanel.blackText; // Setting name color
	private final Font titleFont	= narrowFont(30);
	private final Font costFont		= narrowFont(18);
	private final int titleOffset	= s30; // Offset from Margin
	private final int costOffset	= s30; // Offset from title
	private final int titlePad		= scaled(90);
	private final int bottomPad		= scaled(50);
	private final int columnPad		= s20;
	
	private final Color frameC		= SystemPanel.blackText; // Setting frame color
	private final Color settingC	= SystemPanel.whiteText; // Setting name color
	private final Font settingFont	= narrowFont(14);
	private final int settingH		= s16;
	private final int settingHPad	= s4;
	private final int frameShift	= s5;
	private final int frameTopPad	= 0;
	private final int frameEndPad	= s4;
	private final int settingIndent	= s10;
	private final int wSetting		= scaled(200);

	private final Color optionC		= SystemPanel.blackText; // Unselected option Color
	private final Color selectC		= SystemPanel.whiteText;  // Selected option color
	private final Font optionFont	= narrowFont(12);
	private final int optionH		= s14;
	private final int optionIndent	= s15;

	private int xButton, yButton;
	private int yTitle;
	private int xCost, yCost;
	private int w, wBG, h, hBG;
	private int numColumns	= 0;
	private int columnH		= 0;
	private int columnsMaxH	= 0;
	private int numSettings	= 0;
	private int settingBoxH;
	private int leftM, topM, yTop;
	private int xLine, yLine; // settings var
	private int paramIndex, columnIndex, rowIndex;
	
	
	private LinkedList<Integer> colSettingsCount	= new LinkedList<>();
	protected LinkedList<BaseText[]> btList			= new LinkedList<>();
	protected LinkedList<AbstractParam<?>> paramList= new LinkedList<>();
	private Rectangle hoverBox;
	private Rectangle okBox 		= new Rectangle();
	private Rectangle defaultBox	= new Rectangle();
	private BasePanel parent;
	
	private BaseText totalCostText;
	private String totalCostKey = "CUSTOM_RACE_GUI_COST";
	private float totalCost = 0; 
	
	// ========== Constructors and initializers ==========
	//
	public AbstractConfigUI(String guiTitle_ID) {
		guiTitleID = guiTitle_ID;
		init_0();
	}
	private void init_0() {
		setOpaque(false);
	    totalCostText = new BaseText(this, false, 18, 15, -78, costC, costC, hoverC, depressedC, costC, 0, 0, 0);
		// Call for filling the settings
		init0();

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	private void init() {
		// Display text Initialization
		for (int settingIdx=0; settingIdx<paramList.size(); settingIdx++) { // Loop thru the setting list
			BaseText[] btA = btList.get(settingIdx);
			AbstractParam<?> param = paramList.get(settingIdx);
			if (param.isBullet()) {
				btA[SETTING_IDX].displayText(param.getGuiSettingLabelValueCostStr()); // The setting
				int optionCount = btA.length-1;
				for (int optionIdx=0; optionIdx <  optionCount; optionIdx++) {
					btA[optionIdx+1].displayText(param.getGuiCostOptionStr(optionIdx)); // The options
				}
			} else {
				btA[SETTING_IDX].displayText(param.getGuiSettingLabelValueCostStr());			
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
	private  float updateTotalCost() {
		totalCost = 0;
		for (AbstractParam<?> param : paramList) {
			totalCost += param.getCost();
		}
		return totalCost;
	}
	private  String totalCostStr() {
		return text(totalCostKey, (int)updateTotalCost());
	}
	private  BaseText settingBT() {
		return new BaseText(this, false, 14, 15, -78, settingC, settingC, hoverC, depressedC, settingC, 0, 0, 0);
	}
	private  BaseText optionBT() {
		return new BaseText(this, false, 12, 15, -78, optionC, selectC, hoverC, depressedC, textC, 0, 0, 0);
	}
	protected  BaseText[] newBT(AbstractParam<?> param) {
		columnH += settingHPad;
		if (param.isBullet()) {
			int optionCount = param.getBoxSize(); // +1 for the setting
			int paramIdx	= param.getIndex();
			BaseText[] btA	= new BaseText[optionCount+1];
			btA[SETTING_IDX]= settingBT();
			columnH += settingH;
			columnH += frameTopPad;
			for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
				btA[optionIdx+1] = optionBT();
				btA[optionIdx+1].disabled(optionIdx == paramIdx);
				columnH	+= optionH;
			}
			numSettings++;
			columnH += frameEndPad;
			return btA;			
		} else {
			BaseText[] btA = new BaseText[1];
			btA[0] = settingBT();			
			numSettings++;
			columnH += settingH;
			return btA;
		}
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
		init();
		enableGlassPane(this);
	}
	private void close() {
		disableGlassPane();
	}
	private void setToDefault() {
		UserPreferences.setToDefault(guiTitleID);
		init();
		repaint();
	}
	private void paintSetting(Graphics2D g, BaseText[] btA, AbstractParam<?> param) {
		if (param.isBullet()) {
			int bulletSize	= btA.length;
			int optionCount	= bulletSize-1;
			String txt	= param.getGuiSettingLabelCostStr();
			// frame
			settingBoxH	= bulletSize * optionH;
			g.setColor(frameC);
			g.drawRect(xLine, yLine - frameShift, wSetting, settingBoxH);
			g.setPaint(GameUI.settingsSetupBackground(w));
			g.setFont(settingFont);
			int sw = g.getFontMetrics().stringWidth(txt);
			g.fillRect(xLine + settingIndent/2, yLine -s18 + frameShift, sw + settingIndent, s18);
			g.setColor(settingC);
			
			BaseText bt = btA[SETTING_IDX];
			bt.displayText(txt);
			bt.setScaledXY(xLine + settingIndent, yLine);
			bt.draw(g);
			yLine += settingH;
			yLine += frameTopPad;

			// Options
			g.setFont(optionFont);
			for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
				bt = btA[optionIdx+1];
				bt.displayText(param.getGuiCostOptionStr(optionIdx));
				bt.setScaledXY(xLine + optionIndent, yLine);
				bt.draw(g);
				yLine	+= optionH;
			}				
			yLine += frameEndPad;
		} else {
			g.setColor(settingC);
			g.setFont(settingFont);
			BaseText bt = btA[SETTING_IDX];
			bt.displayText(param.getGuiSettingLabelValueCostStr());
			bt.setScaledXY(xLine + settingIndent, yLine);
			bt.draw(g);
			yLine += settingH;
		}
	}
	private void goToNextSetting() {
		paramIndex++;
		rowIndex++;
		if (rowIndex >= colSettingsCount.get(columnIndex)) {
			rowIndex = 0;
			columnIndex++;
			xLine = xLine + wSetting + columnPad;
			yLine = yTop;
		} else
			yLine += settingHPad;
	}
	private void updateBulletSetting(AbstractParam<?> param, BaseText[] btA) {
		int optionCount	= param.getBoxSize(); // +1 for the setting
		int paramIdx	= param.getIndex();
		btA[SETTING_IDX].repaint();
		for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
			btA[optionIdx+1].disabled(optionIdx == paramIdx);
			btA[optionIdx+1].repaint();
		}
		totalCostText.repaint(totalCostStr());
	}
	private void mouseCommon(boolean up, boolean mid, boolean shiftPressed, boolean ctrlPressed
			, MouseEvent e, MouseWheelEvent w) {
		for (int settingIdx=0; settingIdx < paramList.size(); settingIdx++) {
			BaseText[] btA = btList.get(settingIdx);
			AbstractParam<?> param = paramList.get(settingIdx);
			if (param.isBullet()) {
				if (hoverBox == btA[SETTING_IDX].bounds()) { // Check Setting
					param.toggle(e, w);
					updateBulletSetting(param, btA);
					return;
				} else { // Check options
					int optionCount	= btA.length-1; // 1 for the setting
					for (int optionIdx=0; optionIdx < optionCount; optionIdx++) {
						if (hoverBox == btA[optionIdx+1].bounds()) {
							param.setFromIndexAndSave(optionIdx);
							updateBulletSetting(param, btA);
							return;
						}
					}
				}
			} else if (hoverBox == btA[SETTING_IDX].bounds()) {
				param.toggle(e, w);
				btA[SETTING_IDX].repaint();
				totalCostText.repaint(totalCostStr());
				return;
			}
		}
	}
	// ========== Overriders ==========
	//
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		w		= getWidth();
		h		= getHeight();
		hBG		= titlePad + columnsMaxH + bottomPad;
		topM	= (h - hBG)/2;
		yTop	= topM + titlePad; // First setting top position
		wBG		= (wSetting + columnPad) * numColumns;
		leftM	= (w - wBG)/2;
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
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		g.setFont(costFont);
		g.setColor(costC);
		totalCostText.setScaledXY(xCost, yCost);
		totalCostText.draw(g);
		
		// Loop thru the parameters
		paramIndex	 = 0;
		columnIndex	 = 0;
		rowIndex	 = 0;
		xLine = leftM+s10;
		yLine = yTop;
		// First column (left)
		// Loop thru parameters

		Stroke prev = g.getStroke();
		g.setStroke(stroke2);
		while (paramIndex < paramList.size()) {
			paintSetting(g, btList.get(paramIndex), paramList.get(paramIndex));
			goToNextSetting();
		}
		g.setStroke(prev);

		// draw settings button
		int cnr = s5;
		xButton = leftM + wBG - buttonW - xButtonOffset;
		okBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(okBox.x, okBox.y, buttonW, buttonH, cnr, cnr);
		g.setFont(buttonFont);
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
		xButton -= (buttonW+s30);
		int sw7 = g.getFontMetrics().stringWidth(text7);
		defaultBox.setBounds(xButton, yButton, buttonW, buttonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(defaultBox.x, defaultBox.y, buttonW, buttonH, cnr, cnr);
		g.setFont(buttonFont);
		int x7 = defaultBox.x+((defaultBox.width-sw7)/2);
		int y7 = defaultBox.y+defaultBox.height-s8;
		Color c7 = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text7, 2, x7, y7, GameUI.borderDarkColor(), c7);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	@Override public void keyPressed(KeyEvent e) {
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
//					for (int i=0; i<paramList.size(); i++) {
//						btList.get(i).repaint(paramList.get(i).getGuiDisplay());
//					} TODO BR: processKey
				};
				return;
		}
	}
	@Override public void mouseDragged(MouseEvent e) {  }
	@Override public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (okBox.contains(x,y))
			hoverBox = okBox;
		else if (defaultBox.contains(x,y))
			hoverBox = defaultBox;
		else {
			outerLoop1:
			for (BaseText[] btA : btList) {
				for (BaseText txt : btA) {
					if (txt.contains(x,y)) {
						hoverBox = txt.bounds();
						break outerLoop1;
					}
				}
			}
		}
		if (hoverBox != prevHover) {
			outerLoop2:
			for (BaseText[] btA : btList) {
				for (BaseText txt : btA) {
					if (prevHover == txt.bounds()) {
						txt.mouseExit();
						break outerLoop2;
					}
				}
			}
			outerLoop3:
			for (BaseText[] btA : btList) {
				for (BaseText txt : btA) {
					if (hoverBox == txt.bounds()) {
						txt.mouseEnter();
						break outerLoop3;
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
		boolean up	= !SwingUtilities.isRightMouseButton(e); // BR: added bidirectional
		boolean mid	= !SwingUtilities.isMiddleMouseButton(e); // BR: added reset click
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed = e.isControlDown();
		mouseCommon(up, mid, shiftPressed, ctrlPressed, e, null);
		if (hoverBox == okBox)
			close();
		else if (hoverBox == defaultBox)
			setToDefault();
	}
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) {
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		boolean shiftPressed = e.isShiftDown();
		boolean ctrlPressed = e.isControlDown();
		boolean up = e.getWheelRotation() < 0;
		mouseCommon(up, false, shiftPressed, ctrlPressed, null, e);
	}
}
