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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;

import rotp.model.empires.CustomRaceDefinitions;
import rotp.model.game.IGameOptions;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.main.SystemPanel;
import rotp.ui.races.RacesUI;
import rotp.ui.util.ListDialog;
import rotp.ui.util.SettingBase;
import rotp.util.FontManager;
import rotp.util.ModifierKeysState;

public class ShowCustomRaceUI extends BaseModPanel {
	private static final long serialVersionUID	= 1L;
	private static final Color  backgroundHaze	= new Color(0,0,0,160);
	private static final String playerAIOffKey	= "SETUP_OPPONENT_AI_PLAYER";
	private static final String totalCostKey	= ROOT + "GUI_COST";
	
	private	static final int  tooltipPadV	= s10;
	private	static final int  descPadM		= s5;
	private static final int  descLineH		= s18;
	private	static final Font descFont		= FontManager.current().narrowFont(14);

	protected static final Color textC		= SystemPanel.whiteText;
	protected static final int buttonPad	= s15;
	private   static final int buttonPadV	= tooltipPadV;
	protected static final int xButtonOffset= s30;
	protected static final Color labelC		= SystemPanel.orangeText;
	protected static final int labelFontSize= 14;
	protected static final int labelH		= s16;
	protected static final int labelPad		= s8;

	protected static final int columnPad	= s12;
	private static final Color costC		= SystemPanel.blackText;
	private static final int costFontSize	= 18;
	private	static final Font titleFont		= FontManager.current().narrowFont(30);
	private static final int titleOffset	= s30; // Offset from Margin
	private static final int costOffset		= s25; // Offset from title
	private static final int titlePad		= s80; // Offset of first setting
	private static final int raceAIH		= s18;
	
	private static final Color frameC		= SystemPanel.blackText; // Setting frame color
	private static final Color settingNegC	= SettingBase.settingNegC; // Setting name color
	private static final Color settingC		= SettingBase.settingC; // Setting name color
	private static final int settingFont	= 15;
	private static final int settingH		= s16;
	private static final int spacerH		= s10;
	private static final int settingHPad	= s4;
	private static final int frameShift		= s5;
	private static final int frameTopPad	= 0;
	private static final int frameSizePad	= s10;
	private static final int frameEndPad	= s4;
	private static final int settingIndent	= s10;
	private static final int wFirstColumn	= RotPUI.scaledSize(200);
	private static final int wSetting		= RotPUI.scaledSize(200);
	protected int currentWith = wFirstColumn;

	private static final Color optionC		= SystemPanel.blackText; // Unselected option Color
	private static final Color selectC		= SystemPanel.whiteText;  // Selected option color
	private static final int   optionFont	= 13;
	private static final int   optionH		= s15;
	private static final int   optionIndent	= s15;

	// This should be the last static to be initialized
	private static final ShowCustomRaceUI instance = new ShowCustomRaceUI();

	private LinkedList<Integer> colSettingsCount;
	private	LinkedList<Integer> spacerList;
	private LinkedList<Integer> columnList;
	LinkedList<SettingBase<?>>  commonList;
	protected LinkedList<SettingBase<?>> settingList;
	protected LinkedList<SettingBase<?>> mouseList;
	
	protected String guiTitleID;

	private int numColumns	= 0;
	private int columnsMaxH	= 0;
	private int columnH		= RotPUI.scaledSize(60); // For the Random options
	private int numSettings	= 0;
	private	int descLines	= 2;
	private	int descHeigh	= descLines * descLineH + descPadM;

	private int yTitle;
	private int xCost, yCost;
	private int w;
	private int h;
	private int hBG;
	protected int wBG;
	private int settingSize;
	private int settingBoxH;
	private int topM;
	private int yTop;
	private int xDesc, yDesc, descWidth;
	protected int xButton, yButton;
	protected int leftM;
	protected int xLine, yLine; // settings var

	protected BasePanel parent;
	protected final Box exitBox			= new Box("SETTINGS_EXIT");
	private	  final Box raceAIBox		= new Box(ROOT + "RACE_AI");
	private	  final JTextPane descBox	= new JTextPane();
	protected ModText totalCostText;
	private	  RacesUI  raceUI; // Parent panel
	protected int maxLeftM;
	CustomRaceDefinitions cr;
	protected boolean initialized = false;

	// ========== Constructors and initializers ==========
	//
	protected ShowCustomRaceUI() {
		setOpaque(false);
		add(descBox);
		descBox.setOpaque(true);
		descBox.setContentType("text/html");
		descBox.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
	    totalCostText = new ModText(this, costFontSize, 
	    		costC, costC, hoverC, depressedC, costC, false);
	}
	public static ShowCustomRaceUI instance() {
		return instance.init0();
	}
	private ShowCustomRaceUI init0() {
		if (initialized)
			return this;
		initialized = true;
		cr = new CustomRaceDefinitions();		
		maxLeftM	= scaled(80);
		guiTitleID	= ROOT + "SHOW_TITLE";
	    commonList	= settingList;
	    mouseList	= settingList;
	    cr.setRace(IGameOptions.baseRaceOptions().getFirst());
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
	    mouseList	= settingList;

		for (int i=0; i<settingSize; i++) {
			if (spacerList.contains(i))
				columnH += spacerH;
			if (columnList.contains(i))
				endOfColumn();
			initSetting(settingList.get(i));
			numSettings++;
		}
		endOfColumn();
	}
	protected void initSetting(SettingBase<?> setting) {
		if (setting.isBullet()) {
			setting.settingText(settingBT());
			columnH += settingH + frameSizePad;
			columnH += frameTopPad;
			int paramIdx	= setting.index();
			int bulletStart	= setting.bulletStart();
			int bulletSize	= setting.bulletBoxSize();
			for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
				int optionIdx = bulletStart + bulletIdx;
				setting.optionText(optionBT(), bulletIdx);
				setting.optionText(bulletIdx).disabled(optionIdx == paramIdx);
				columnH	+= optionH;
			}
			columnH += frameEndPad;
		} else {
			setting.settingText(settingBT());			
			columnH += settingH;
		}
		columnH += settingHPad;
	}
	public void open(BasePanel p) {
		enableGlassPane(this);
		ModifierKeysState.reset();
		parent = p;
		init();
		repaint();
	}
	// ========== Other Methods ==========
	//
	protected void setToolTip(String tt) {
		descBox.setText(tt);
		loadGuide();
		if (hoverBox != null && hoverBox != prevHover)
			repaint(hoverBox);
	}
	private  ModText settingBT() {
		return new ModText(this, settingFont, settingC, settingNegC, hoverC, depressedC, textC, false);
	}
	protected  ModText optionBT() {
		return new ModText(this, optionFont, optionC, selectC, hoverC, depressedC, textC, false);
	}
	private void endOfColumn() {
		columnH -= 2 * settingH; // to fit the reality... Debug needed
		columnsMaxH = max(columnsMaxH, columnH);
		colSettingsCount.add(numSettings);
		numColumns++;
		numSettings	= 0;
		columnH		= 0;
	}
	private boolean isPlayer()			{ return raceUI.selectedEmpire().isPlayer(); }
	private String selectAIFromList(String[] aiArray, String initialChoice) {
		String message = "Make your choice";
		ListDialog dialog = new ListDialog(
		    	this,					// Frame component
		    	getParent(),			// Location component
		    	message,				// Message
		        "Empire AI selection",	// Title
		        aiArray,				// List
		        initialChoice, 			// Initial choice
		        "XXXXXXXXXXXXXXXX",		// long Dialogue
				false,					// isVertical
		        scaled(220), scaled(220),	// size
				null, null, null,		// Font, Preview, Alternate return
				null); 					// TODO BR: add help parameter

		String input = (String) dialog.showDialog();
	    if (input == null)
	    	return initialChoice;
	    return input;
	}
	private void raceAIBoxAction() {
		if (isPlayer()) { // Change Player AI
			IGameOptions opts   	= raceUI.options();
			List<String> aiKeys		= MOO1GameOptions.autoplayBaseOptions();
			List<String> aiNameList = new ArrayList<>(); // Get language version
			for (String key : aiKeys)
				aiNameList.add(text(key));
			String[] aiNameArray = aiNameList.toArray(new String[aiNameList.size()]);

			String currentAI = opts.selectedAutoplayOption();
			int currentIndex = aiKeys.indexOf(currentAI);
			String aiNewName = selectAIFromList(aiNameArray, aiNameArray[currentIndex]);
			int aiNewIndex   = aiNameList.indexOf(aiNewName);
			String aiNewKey  = aiKeys.get(aiNewIndex);
			
			opts.selectedAutoplayOption(aiNewKey);
			raceUI.selectedEmpire().changePlayerAI(aiNewKey);
		}
		else { // Change Opponent AI
			List<String> aiNameList = MOO1GameOptions.sortedOpponentAINames();
			String[] aiNameArray    = aiNameList.toArray(new String[aiNameList.size()]);

			String currentAI = raceUI.selectedEmpire().getAiName();
			String aiNewName = selectAIFromList(aiNameArray, currentAI);
			String aiNewKey  = MOO1GameOptions.getOpponentAIKey(aiNewName);

			raceUI.selectedEmpire().changeOpponentAI(aiNewKey);
		}
		repaint();
	}
	protected  String totalCostStr() {
		return text(totalCostKey, Math.round(cr.getTotalCost()));
	}
	private boolean checkForHoveredSettings(LinkedList<SettingBase<?>> settings) {
		for (SettingBase<?> setting : settings) {
			if (setting.settingText().contains(mX,mY)) {
				hoverBox = setting.settingText().box();
				setToolTip(setting.getToolTip());
				if (hoverBox != prevHover) {
					setting.settingText().mouseEnter();
					repaint(hoverBox);
				}
				return true;
			}
			if (setting.isBullet()) {
				int idx = 0;
				for (ModText txt : setting.optionsText()) {
					if (txt.contains(mX,mY)) {
						hoverBox = txt.box();
						setToolTip(setting.getToolTip(idx));
						if (hoverBox != prevHover) {
							txt.mouseEnter();
							repaint(hoverBox);
						}
						return true;
					}
					idx++;
				}
			}
		}
		return false;
	}
	protected String raceAIButtonTxt() {
		if (isPlayer())
			if (raceUI.selectedEmpire().isAIControlled())
				return raceUI.selectedEmpire().getAiName();
			else
				return text(playerAIOffKey);
		else
			return raceUI.selectedEmpire().getAiName();
	}
	protected int getBackGroundWidth() {
		return columnPad+wFirstColumn+columnPad + (wSetting+columnPad) * (numColumns-1);
	}
	protected void paintSetting(Graphics2D g, SettingBase<?> setting) {
		int sizePad	= frameSizePad;
		int endPad 	= frameEndPad;
		int optNum	= setting.bulletBoxSize();;
		float cost 	= setting.settingCost();
		ModText bt	= setting.settingText();
		int paramId	= setting.index();

		if (optNum == 0) {
			endPad	= 0;
			sizePad	= 0;
		}
		settingBoxH	= optNum * optionH + sizePad;
		// frame
		g.setColor(frameC);
		g.drawRect(xLine, yLine - frameShift, currentWith, settingBoxH);
		g.setPaint(GameUI.settingsSetupBackgroundW(w));
		bt.displayText(setting.guiSettingDisplayStr());
		g.fillRect(xLine + settingIndent/2, yLine -s12 + frameShift,
				bt.stringWidth(g) + settingIndent, s12);
		setting.enabledColor(cost);
		bt.setScaledXY(xLine + settingIndent, yLine);
		bt.draw(g);
		yLine += settingH;
		yLine += frameTopPad;
		// Options
		setting.formatData(g, wSetting - 2*optionIndent);
		int bulletStart	= setting.bulletStart();
		int bulletSize	= setting.bulletBoxSize();
		for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
			int optionIdx = bulletStart + bulletIdx;
			bt = setting.optionText(bulletIdx);
			bt.disabled(optionIdx == paramId);
			bt.displayText(setting.guiCostOptionStr(optionIdx));
			bt.setScaledXY(xLine + optionIndent, yLine);
			bt.setFixedWidth(true, currentWith-2*optionIndent);
			bt.draw(g);
			yLine += optionH;
		}				
		yLine += endPad;
	}
	private void paintDescriptions(Graphics2D g) {
		descBox.setFont(descFont);
		descBox.setBackground(GameUI.setupFrame());
		descBox.setBounds(xDesc, yDesc, descWidth, descHeigh);
		descBox.setVisible(true);
	}
	protected void paintButtons(Graphics2D g) {
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

		// Guide Button
		text	= text(guideButtonKey());
		xButton = leftM + buttonPad;
		sw		= g.getFontMetrics().stringWidth(text);
		buttonW = g.getFontMetrics().stringWidth(text) + smallButtonMargin;
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

		// RaceUI Button
		text = raceAIButtonTxt();
		sw = g.getFontMetrics().stringWidth(text);
		buttonW	= sw + smallButtonMargin;
		int xAI = leftM + wBG - columnPad - buttonW;
		int yAI	= yCost - raceAIH - s10;
		raceAIBox.setBounds(xAI, yAI, buttonW, smallButtonH);
		g.setColor(GameUI.buttonBackgroundColor());
		g.fillRoundRect(raceAIBox.x, raceAIBox.y, buttonW, smallButtonH, cnr, cnr);
		xT = raceAIBox.x+((raceAIBox.width-sw)/2);
		yT = raceAIBox.y+raceAIBox.height-s8;
		cB = hoverBox == raceAIBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, xT, yT, GameUI.borderDarkColor(), cB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(raceAIBox.x, raceAIBox.y, raceAIBox.width, raceAIBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	// ========== Overriders ==========
	//
	@Override protected void init() {
		super.init();
		for (SettingBase<?> setting : commonList) {
			if (setting.isBullet()) {
				setting.settingText().displayText(setting.guiSettingDisplayStr()); // The setting
				int bulletStart	= setting.bulletStart();
				int bulletSize	= setting.bulletBoxSize();
				for (int bulletIdx=0; bulletIdx < bulletSize; bulletIdx++) {
					int optionIdx = bulletStart + bulletIdx;
					setting.optionText(bulletIdx).displayText(setting.guiCostOptionStr(optionIdx));
				}
			}
			else {
				setting.settingText().displayText(setting.guiSettingDisplayStr());			
			}
		}
		totalCostText.displayText(totalCostStr());
		totalCostText.disabled(true);
		descBox.setText("<b>Shift</b>&nbsp and <b>Ctrl</b>&nbsp can be used to change buttons, click and scroll functions");
	}
	@Override public boolean checkModifierKey(InputEvent e) {
		boolean change = checkForChange(e);
//		hoverAndTooltip(false);
		return change;
	}
	@Override public void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		super.paintComponent(g);
		paintButtons(g);
		g.dispose();
	}
	@Override protected String GUI_ID() { return ""; }
	@Override public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		currentWith	 = wFirstColumn;
		w	= getWidth();
		h	= getHeight();
		wBG	= getBackGroundWidth();
		descWidth	= wBG - 2 * columnPad;

		g.setFont(descFont);
		// Set the base top Margin
		// Set the final High
		hBG	 = titlePad + columnsMaxH + buttonPadV + smallButtonH + tooltipPadV + descHeigh + tooltipPadV;
		topM = (h - hBG)/2;
		yDesc	 = topM + hBG - tooltipPadV - descHeigh;
		
		yTop	= topM + titlePad; // First setting top position
		leftM	= Math.min((w - wBG)/2, maxLeftM);
		yTitle	= topM + titleOffset;
		yButton	= yDesc - tooltipPadV - smallButtonH;
		yCost 	= yTitle + costOffset;
		xCost	= leftM + columnPad/2;
		xLine	= leftM + columnPad/2;
		yLine	= yTop;
		xDesc		= leftM + columnPad;

		// draw background "haze"
		g.setColor(backgroundHaze);
		g.fillRect(0, 0, w, h);
		
		g.setPaint(GameUI.settingsSetupBackgroundW(w));
		g.fillRect(leftM, topM, wBG, hBG);
		
		// Tool tip
		paintDescriptions(g);
		
		// Title
		g.setFont(titleFont);
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = leftM +(wBG-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
		
		// Total cost
		totalCostText.displayText(totalCostStr());
		totalCostText.setScaledXY(xCost, yCost);
		totalCostText.draw(g);
		
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

		paintButtons(g);
		showGuide(g);
		
		// ready for extension
		xLine = xLine + currentWith + columnPad;
		yLine = yTop;
	}
	@Override public void keyReleased(KeyEvent e) {
		if(checkModifierKey(e)) {
			if(hoverBox != null)
				descBox.setText(hoverBox.getDescription());
			repaintButtons();
		}
	}
	@Override public void keyPressed(KeyEvent e) {
		if(checkModifierKey(e)) {
			if(hoverBox != null)
				descBox.setText(hoverBox.getDescription());
			repaintButtons();
		}
		switch(e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				close();
				return;
			default:
				break;
		}
		super.keyPressed(e);
	}
	@Override public void mouseMoved(MouseEvent e) {
		mX = e.getX();
		mY = e.getY();
		if (hoverBox != null && hoverBox.contains(mX,mY)) {
			hoverChanged = false;
			return;
		}
		descBox.setText("");
		hoverChanged = true;
		prevHover	 = hoverBox;
		hoverBox	 = null;
		// Go thru the buttons and restore the boxes
		for (Box box : boxHelpList)
			if (box.checkIfHovered(descBox))
				break;
		if (prevHover != null) {
			prevHover.mouseExit();
			if (hoverBox == null)
				descBox.setText("");
			loadGuide();
			repaint();
		}
		// Check if cursor is in a box
		checkForHoveredSettings(mouseList);;

	}
	@Override public void mouseReleased(MouseEvent e) {
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		if (hoverBox == exitBox) {
			close();
			return;
		}
		if (hoverBox == guideBox) {
			doGuideBoxAction();
			return;
		}
		if (hoverBox == raceAIBox) {
			raceAIBoxAction();
			return;
		}
	}
}
