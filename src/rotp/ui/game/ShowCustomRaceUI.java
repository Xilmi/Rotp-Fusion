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
import static rotp.ui.UserPreferences.playerIsCustom;
import static rotp.ui.UserPreferences.showTooltips;
import static rotp.ui.util.AbstractOptionsUI.exitButtonKey;
import static rotp.ui.util.AbstractOptionsUI.exitButtonWidth;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.main.SystemPanel;
import rotp.ui.races.RacesUI;
import rotp.ui.util.AbstractOptionsUI;
import rotp.ui.util.InterfaceOptions;
import rotp.ui.util.Modifier2KeysState;
import rotp.ui.util.SettingBase;
import rotp.util.FontManager;

public class ShowCustomRaceUI extends BasePanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID	= 1L;
	private static final Color  backgroundHaze	= new Color(0,0,0,160);
	private static final String totalCostKey	= ROOT + "GUI_COST";
	protected static final String exitTipKey	= ROOT + "EXIT_TIP";
	
	private	static final int tooltipPadV	= s10;
	private	static final int tooltipPadM	= s10;
	private static final Color tooltipC		= SystemPanel.blackText;
	private static final int tooltipLineH	= 18;
	private	static final Font tooltipFont	= FontManager.current().narrowFont(14);

	protected static final Color textC		= SystemPanel.whiteText;
	protected static final Font buttonFont	= FontManager.current().narrowFont(20);
	protected static final int buttonH		= s30;
	protected static final int buttonMargin	= AbstractOptionsUI.smallButtonM;
	protected static final int buttonPadV	= tooltipPadV;
	protected static final int buttonPad	= s15;
	private static final int xButtonOffset	= s30;
	protected static final Color labelC		= SystemPanel.orangeText;
	protected static final int labelFontSize= 14;
	protected static final int labelH		= s16;
	protected static final int labelPad		= s8;

	private static final Color costC		= SystemPanel.blackText;
	private static final int costFontSize	= 18;
	protected static final Font raceNameFont= FontManager.current().narrowFont(16);
	protected static final int raceNameH	= s20;
	protected static final int raceNameW	= RotPUI.scaledSize(150);
	protected static final int columnPad	= s20;
	private	static final Font titleFont		= FontManager.current().narrowFont(30);
	private static final int titleOffset	= s30; // Offset from Margin
	private static final int costOffset		= s25; // Offset from title
	private static final int titlePad		= s80; // Offset of first setting
	private static final int raceAIW		= RotPUI.scaledSize(120);
	private static final int raceAIH		= s18;
	private static final int raceAIFontSize	= 16;
	
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
	private static final int wFirstColumn	= RotPUI.scaledSize(150);
	private static final int wSetting		= RotPUI.scaledSize(220);
	protected int currentWith = wFirstColumn;

	private static final Color optionC		= SystemPanel.blackText; // Unselected option Color
	private static final Color selectC		= SystemPanel.whiteText;  // Selected option color
	private static final int optionFont		= 13;
	private static final int optionH		= s15;
	private static final int optionIndent	= s15;

	// This should be the last static to be initialized
	public static final ShowCustomRaceUI instance = new ShowCustomRaceUI().init0();

	private LinkedList<Integer> colSettingsCount;
	private	LinkedList<Integer> spacerList;
	private LinkedList<Integer> columnList;
	public  LinkedList<SettingBase<?>> commonList;
	protected LinkedList<SettingBase<?>> settingList;
	protected String guiTitleID;

	private int numColumns	= 0;
	private int columnsMaxH	= 0;
	private int columnH		= 0;
	private int numSettings	= 0;

	private int yTitle;
	private int xCost, yCost, xRace, yRace;
	private int w;
	private int h;
	private int hBG;
	private int settingSize;
	private int settingBoxH;
	private int topM;
	protected int yTop;
	protected int xButton, yButton, xTT, yTT, wTT;
	protected int wBG;
	protected int leftM;
	protected int xLine, yLine; // settings var
	protected int x, y; // mouse position

	protected BasePanel parent;
	protected Rectangle hoverBox;
	protected final Rectangle exitBox = new Rectangle();
	protected String tooltipText = "";
	protected BaseText totalCostText;
	protected BaseText raceAI;
	private	  RacesUI  raceUI; // Parent panel
	protected int maxLeftM;
	public final CustomRaceDefinitions cr;
	
	// ========== Constructors and initializers ==========
	//
	protected ShowCustomRaceUI() {
		cr = new CustomRaceDefinitions();
		setOpaque(false);
	    totalCostText = new BaseText(this, false, costFontSize, 0, 0, 
	    		costC, costC, hoverC, depressedC, costC, 0, 0, 0);
	    raceAI = new BaseText(this, false, raceAIFontSize, 0, 0, 
	    		costC, costC, hoverC, depressedC, costC, 0, 0, 0);
	}
	private ShowCustomRaceUI init0() {
		maxLeftM	= scaled(50);
		guiTitleID	= ROOT + "SHOW_TITLE";
	    commonList	= settingList;
	    cr.setRace(MOO1GameOptions.baseRaceOptions().getFirst());
	    cr.pullSettings();
		addMouseListener(this);
		addMouseMotionListener(this);
	    initGUI();		
		return this;
	}
	public void loadRace() { // For Race Diplomatic UI Panel
		cr.setFromRaceToShow(raceUI.selectedEmpire().dataRace());
	}
	public void init(RacesUI p) { // For Race Diplomatic UI Panel
		raceUI = p;
	}
	protected void initGUI() {
		colSettingsCount = new LinkedList<>();
		columnList	= cr.columnList();
		spacerList	= cr.spacerList();
		settingList	= cr.settingList();
		settingSize	= settingList.size();
		
		for (int i=0; i<settingSize; i++) {
			if (spacerList.contains(i))
				columnH += settingH;
			if (columnList.contains(i))
				endOfColumn();
			initSetting(settingList.get(i));
			numSettings++;
		}
		endOfColumn();
	}
	protected void initSetting(SettingBase<?> setting) {
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
			columnH += frameEndPad;
		} else {
			setting.settingText(settingBT());			
			columnH += settingH;
		}		
	}
	public void open(BasePanel p) {
		parent = p;
		init();
		enableGlassPane(this);
		repaint();
	}
	protected void init() {
//		if (cr.race() == null) {
//			cr.setFromOptions((DynOptions) playerCustomRace.get());
//			cr.setRace(newGameOptions().selectedPlayerRace());
//			cr.pullSettings();
//		}
		for (SettingBase<?> setting : commonList) {
			if (setting.isBullet()) {
				setting.settingText().displayText(setting.guiSettingDisplayStr()); // The setting
				int optionCount = setting.boxSize();
				for (int optionIdx=0; optionIdx <  optionCount; optionIdx++) { // The options
					setting.optionText(optionIdx).displayText(setting.guiCostOptionStr(optionIdx));
				}
			} else {
				setting.settingText().displayText(setting.guiSettingDisplayStr());			
			}
		}
		raceAI.setBounds(0, 0, raceAIW, raceAIH);
		raceAI.displayText(raceAITxt());
		totalCostText.displayText(totalCostStr());
		totalCostText.disabled(true);
		tooltipText = "This is where Tool tips are displayed";
	}
	// ========== Other Methods ==========
	//
	public void getOptions(MOO1GameOptions source) {
		for (InterfaceOptions param : commonList)
			param.setFromOptions(source.dynamicOptions());
		playerIsCustom.setFromOptions(source.dynamicOptions());
		init();
	}
	private  BaseText settingBT() {
		return new BaseText(this, false, settingFont, 0, 0,
				settingC, settingNegC, hoverC, depressedC, textC, 0, 0, 0);
	}
	protected  BaseText optionBT() {
		return new BaseText(this, false, optionFont, 0, 0,
				optionC, selectC, hoverC, depressedC, textC, 0, 0, 0);
	}
	private void endOfColumn() {
		columnH += settingH;
		columnsMaxH = max(columnsMaxH, columnH);
		colSettingsCount.add(numSettings);
		numColumns++;
		numSettings	= 0;
		columnH		= 0;
	}
	private String exitButtonTipKey() { return exitTipKey; }
	private void doExitBoxAction() { close(); }
	protected void paintSetting(Graphics2D g, SettingBase<?> setting) {
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
		g.drawRect(xLine, yLine - frameShift, currentWith, settingBoxH);
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
		setting.formatData(g, wSetting - 2*optionIndent);
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
	protected void displayTooltip(String tip) {
		if (!tooltipText.equals(tip)) {
			tooltipText = tip;
			repaint();
		}
	}
	protected void close() { disableGlassPane(); }
	protected void checkModifierKey(InputEvent e) {
		hoverAndTooltip(Modifier2KeysState.checkForChange(e));
	}
	protected  String totalCostStr() {
		return text(totalCostKey, Math.round(cr.getTotalCost()));
	}
	protected void hoverAndTooltip(boolean repaint) {
		String tip = tooltipText;
		Rectangle prevHover = hoverBox;
		hoverBox = null;
		if (exitBox.contains(x,y)) {
			hoverBox = exitBox;
			tooltipText = text(exitButtonTipKey());
		}
		
		if (repaint || !tooltipText.equals(tip))
			repaint();
		else if (hoverBox != prevHover) {
			if (prevHover != null) repaint(prevHover);
			if (hoverBox != null)  repaint(hoverBox);
		}
	}
	protected String raceAITxt() {
		return raceUI.selectedEmpire().getAiName();
	}
	protected int getBackGroundWidth() {
		return wFirstColumn+columnPad + (wSetting+columnPad) * (numColumns-1);
	}
	// ========== Overriders ==========
	//
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		currentWith	 = wFirstColumn;
		w	= getWidth();
		h	= getHeight();
		wBG	= getBackGroundWidth();
		wTT	= wBG - 2 * columnPad;

		g.setFont(tooltipFont);
		List<String> lines = wrappedLines(g, tooltipText, wTT-2*tooltipPadM);
		int tooltipH = 0;
		if (showTooltips.get()) {
			// Set the base top Margin
			tooltipH = 2 * tooltipLineH + tooltipPadM;		
			hBG	 = titlePad + columnsMaxH + buttonPadV + buttonH + tooltipPadV + tooltipH + tooltipPadV;
			topM = (h - hBG)/2;
			// Set the final High
			tooltipH = max(1, lines.size()) * tooltipLineH + tooltipPadM;
			hBG		= titlePad + columnsMaxH + buttonPadV + buttonH + tooltipPadV + tooltipH + tooltipPadV;
			yTT		= topM + hBG - tooltipPadV - tooltipH;
		} else {
			// Set the final High
			hBG	 = titlePad + columnsMaxH + buttonPadV + buttonH + buttonPadV;
			topM = (h - hBG)/2;
			yTT		= topM + hBG;
		}
		
		yTop	= topM + titlePad; // First setting top position
		leftM	= Math.min((w - wBG)/2, maxLeftM);
		yTitle	= topM + titleOffset;
		yButton	= yTT - tooltipPadV - buttonH;
		yCost 	= yTitle + costOffset;
		yRace	= yCost - raceNameH + s6;
		xRace	= leftM + columnPad/2;
		xLine	= leftM + columnPad/2;
		yLine	= yTop;
		xTT		= leftM + columnPad;

		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		
		g.setPaint(GameUI.settingsSetupBackground(w));
		g.fillRect(leftM, topM, wBG, hBG);
		
		// Tool tip
		if (showTooltips.get()) {
			g.setColor(tooltipC);
			g.drawRect(xTT, yTT, wTT, tooltipH);
			g.setFont(tooltipFont);
			yTT += s4;
			for (String line: lines) {
				yTT += tooltipLineH;
				drawString(g,line, xTT+tooltipPadM, yTT);
			}		
		}
		
		// Title
		g.setFont(titleFont);
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = leftM +(wBG-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		// Race Name
//		g.setFont(narrowFont(costFontSize));
//		g.setColor(costC);
//		sw = g.getFontMetrics().stringWidth(raceKeyTxt) + s4;
//		drawString(g, raceKeyTxt, xRace, yCost); // Yes yCost!
//        xRace += sw;
//        raceKey.setCaretPosition(raceKey.getText().length());
//		raceKey.setLocation(xRace, yRace);

		// Total cost
		xCost = xRace + raceNameW + columnPad;
		totalCostText.displayText(totalCostStr());
		totalCostText.setScaledXY(xCost, yCost);
		totalCostText.draw(g);
		
		// Race AI
		g.setFont(raceNameFont);
		g.setColor(costC);
		raceAI.displayText(raceAITxt());
		int xRaceAI = leftM + wBG - columnPad - raceAI.stringWidth(g);
		raceAI.setScaledXY(xRaceAI, yRace + raceAIH);
		raceAI.draw(g);

		// Loop thru the parameters
		xLine = leftM+s10;
		yLine = yTop;
		// First column (left)
		// Loop thru parameters

		Stroke prev = g.getStroke();
		g.setStroke(stroke2);
		for (int i=0; i<settingSize; i++) {
			if (spacerList.contains(i))
				yLine += spacerH;
			if (columnList.contains(i)) {
				xLine = xLine + currentWith + columnPad;
				currentWith = wSetting;
				yLine = yTop;
			}
			paintSetting(g, settingList.get(i));
			yLine += settingHPad;
		}
		g.setStroke(prev);
		// ready for extension
		xLine = xLine + currentWith + columnPad;
		yLine = yTop;

		int cnr = s5;
		g.setFont(buttonFont);
		// Exit Button
		String text = text(exitButtonKey());
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
			case KeyEvent.VK_SPACE:
			case KeyEvent.VK_ENTER:
				parent.advanceHelp();
				return;
			default:
				return;
		}
	}
	@Override public void mouseDragged(MouseEvent e) {  }
	@Override public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		checkModifierKey(e);
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
	}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}
