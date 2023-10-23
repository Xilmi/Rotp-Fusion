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

import static rotp.model.game.IBaseOptsTools.LIVE_OPTIONS_FILE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
import rotp.ui.util.IParam;
import rotp.ui.util.InterfaceOptions;
import rotp.util.FontManager;
import rotp.util.ModifierKeysState;

public class CompactOptionsUI extends BaseModPanel implements MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private String guiTitleID;
	private String GUI_ID;
	
	private static final int rowPad		= s10;
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
	private		   final JTextPane descBox	= new JTextPane();
	private int leftM, rightM;
	private int topM, yTop;
	private int numColumns, numRows;
	private int yTitle;
	private int xSetting, ySetting, columnWidth; // settings var
	private int index, column;
	private int xDesc, yDesc, descWidth;
	
	private LinkedList<Integer>	lastRowList;
	private LinkedList<ModText> btList0; // left part
	private LinkedList<ModText> btList2; // right part
	private LinkedList<ModText> btListBoth;
	private LinkedList<LinkedList<IParam>> optionsList;
	private String parent = "";


	// ========== Constructors and initializers ==========
	//
	public CompactOptionsUI(String guiTitle_ID, String guiId,
			LinkedList<LinkedList<IParam>> paramList) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
		optionsList = paramList;
		init_0();
	}
	public CompactOptionsUI(String guiTitle_ID, String guiId) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
		init_0();
	}
	protected LinkedList<LinkedList<IParam>> getList() { return optionsList; }
	@Override protected void singleInit() {
		optionsList		= getList();
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
		for (LinkedList<IParam> list : optionsList) {
			totalRows += list.size();
			lastRowList.add(totalRows);
			numRows = max(numRows, list.size());
			for (IParam param : list) {
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
	// ========== Optimization Methods ==========
	//
    @Override protected void initBackImg() {
		long timeStart = System.currentTimeMillis();
		backImg = newOpaqueImage(w, h);
		Graphics2D g = (Graphics2D) backImg.getGraphics();
		// modnar: use (slightly) better upsampling
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		// background image
		Image back = GameUI.defaultBackground;
		int imgW = back.getWidth(null);
		int imgH = back.getHeight(null);
		g.drawImage(back, 0, 0, w, h, 0, 0, imgW, imgH, this);

		g.setPaint(bg());
		g.fillRect(leftM, topM, wBG, hBG);

		// Title
		g.setFont(titleFont);
		String title = text(guiTitleID);
		int sw = g.getFontMetrics().stringWidth(title);
		int xTitle = (w-sw)/2;
		drawBorderedString(g, title, 1, xTitle, yTitle, Color.black, Color.white);
   
		// buttons location
		int smallButtonW = scaled(180);
		exitBox.setBounds(w-scaled(189)-rightM, yButton, smallButtonW, smallButtonH);
		smallButtonW = defaultButtonWidth(g);
		defaultBox.setBounds(exitBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		smallButtonW = defaultButtonWidth(g);
		lastBox.setBounds(defaultBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		smallButtonW = userButtonWidth(g);
		userBox.setBounds(lastBox.x-smallButtonW-s30, yButton, smallButtonW, smallButtonH);
		smallButtonW = guideButtonWidth(g);
		guideBox.setBounds(leftM+s9, yButton, smallButtonW, smallButtonH);
    
        initButtonBackImg();
        g.dispose();
		if (showTiming) 
			System.out.println("initBackImg() Time = " + (System.currentTimeMillis()-timeStart));
    }
	// ========== Other Methods ==========
	//
	private ModText newBT(boolean disabled) {
		ModText bt = new ModText(this, settingFont, enabledColor,
				disabledColor, hoverC, depressedC, enabledColor, true);
		bt.disabled(disabled);
		return bt;
	}
	private ModText newBT2(boolean isDefault) {
		ModText bt;
		if (isDefault)
			bt = new ModText(this, settingFont, defaultValuesColor, 
					disabledColor, hoverC, depressedC, disabledColor, true);
		else
			bt = new ModText(this, settingFont, customValuesColor,
					disabledColor, hoverC, depressedC, disabledColor, true);
		return bt;
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
		g.setPaint(bg());
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
					activeList.get(i).toggle(e, GUI_ID, this);
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
	public  void start(String p) { // Called from subUI
		parent = p;
		start();
	}
	private void start() { // Called from subUI
		super.init();
		hoverBox = null;
		prevHover = null;
		descBox.setVisible(true);
		int hSettingTotal = hDistSetting * numRows;
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
		
		guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		enableGlassPane(this);
		refreshGui();
	}
	// ========== Overriders ==========
	//
	@Override protected String exitButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:		 return cancelKey;
		case CTRL_SHIFT: return cancelKey;
		case SHIFT:		 return applyKey;
		default:		 return exitKey;
		}
	}
	@Override protected void close() {
		super.close();
		hoverBox = null;
		prevHover = null;
		switch (parent) {
		case MainOptionsUI.GUI_ID:
			RotPUI.mainOptionsUI().init();
			return;
		case MergedDynamicOptionsUI.GUI_ID:
			RotPUI.mergedDynamicOptionsUI().start(""); // could have been called in setup or in game
			return;
		case DynamicAOptionsUI.GUI_ID:
			RotPUI.modOptionsDynamicA().init();
			return;
		case DynamicBOptionsUI.GUI_ID:
			RotPUI.modOptionsDynamicB().init();
			return;
		case StaticAOptionsUI.GUI_ID:
			RotPUI.modOptionsStaticA().init();
			return;
		case StaticBOptionsUI.GUI_ID:
			RotPUI.modOptionsStaticB().init();
			return;
		case SetupGalaxyUI.GUI_ID:
		case "":
		default: 
	        if (guiOptions().isSetupOption())
	        	RotPUI.setupGalaxyUI().init();
	        else
	        	RotPUI.instance().mainUI().map().resetRangeAreas();
		}
	}
	@Override protected void doExitBoxAction()		{
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:			// Cancel and exit
		case CTRL_SHIFT:	// Cancel and exit
			guiOptions().updateFromFile(LIVE_OPTIONS_FILE);
			UserPreferences.load();
			break;
		case SHIFT:			// Apply
			guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			repaintButtons();
			return; 
		default:			// Exit
			guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			break; 
		}
		close();
	}
	@Override protected void doUserBoxAction()		{
		switch (ModifierKeysState.get()) {
		case CTRL: // saveGlobalUserKey
		case CTRL_SHIFT: // saveLocalUserKey
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
	@Override protected String GUI_ID()		{ return GUI_ID; }
	@Override public void paintComponent(Graphics g0)	{
		// showTiming = true;
		if (showTiming)
			System.out.println("===== Compact PaintComponents =====");
		long timeStart = System.currentTimeMillis();
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		
        // background image
        g.drawImage(backImg(), 0, 0, this);
		// Buttons background image
        drawButtons(g);
		// Tool tip
		paintDescriptions(g);

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
		showGuide(g);
		if (showTiming)
			System.out.println("Compact paintComponent() Time = " + (System.currentTimeMillis()-timeStart));	
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
			case MainOptionsUI.GUI_ID:
				RotPUI.mainOptionsUI().mouseMoved(e);
				return;
			case MergedDynamicOptionsUI.GUI_ID:
				RotPUI.mergedDynamicOptionsUI().mouseMoved(e);
				return;
			case DynamicAOptionsUI.GUI_ID:
				RotPUI.modOptionsDynamicA().mouseMoved(e);
				return;
			case DynamicBOptionsUI.GUI_ID:
				RotPUI.modOptionsDynamicB().mouseMoved(e);
				return;
			case StaticAOptionsUI.GUI_ID:
				RotPUI.modOptionsStaticA().mouseMoved(e);
				return;
			case StaticBOptionsUI.GUI_ID:
				RotPUI.modOptionsStaticB().mouseMoved(e);
				return;
			case "":
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
