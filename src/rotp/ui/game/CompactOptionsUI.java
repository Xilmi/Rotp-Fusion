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

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
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
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;

import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.IParam;
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
	
	private final LinkedList<Integer>	lastRowList	= new LinkedList<>();
	private final LinkedList<ModText>	btListLeft	= new LinkedList<>(); // left part
	private final LinkedList<ModText>	btListRight	= new LinkedList<>(); // right part
	private final LinkedList<ModText>	btListBoth	= new LinkedList<>();
	private final LinkedHashMap<Integer, BufferedImage>	imgList	= new LinkedHashMap<>();
	private LinkedList<LinkedList<IParam>>	optionsList;
	private BaseModPanel parentUI;
	private boolean forceUpdate = true;

	// ========== Constructors and initializers ==========
	//
	public CompactOptionsUI(String guiTitle_ID, String guiId,
			LinkedList<LinkedList<IParam>> paramList) {
		guiTitleID = guiTitle_ID;
		GUI_ID = guiId;
		optionsList = paramList;
		init_0();
	}
	CompactOptionsUI(String guiTitle_ID, String guiId) {
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
					btListLeft.add(newBT(param.isTitle()).initGuide(param));
					btListRight.add(newBT2(param.isDefaultValue()).initGuide(param));
					if (param.isDuplicate())
						duplicateList.add(param);
					else
						paramList.add(param);
				}
			}
		}
		btListBoth.addAll(btListLeft);
		btListBoth.addAll(btListRight);
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
		exitBox.setBounds(w-scaled(189)-rightM, yButton+s2, smallButtonW, smallButtonH);
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
		ModText txt2 = btListRight.get(index);
		if (activeList.get(index).isDefaultValue())
			txt2.enabledC(defaultValuesColor);
		else
			txt2.enabledC(customValuesColor);
	}
	private void paintSetting(Graphics2D g) {
		IParam param = activeList.get(index);
		boolean refresh = forceUpdate || param.updated();
		if (refresh) {
			BufferedImage img = new BufferedImage(retina(columnWidth), retina(textBoxH), TYPE_INT_ARGB);
			Graphics2D gi	 = (Graphics2D) img.getGraphics();
			gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			gi.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY); 
			ModText txtLeft	 = btListLeft.get(index);
			txtLeft.repaint(activeList.get(index).getGuiDisplay(0));
			ModText txtRight = btListRight.get(index);
			txtRight.repaint(activeList.get(index).getGuiDisplay(1));
			if ((txtLeft.box() == hoverBox) || (txtRight.box() == hoverBox)) {
				txtLeft.forceHover  = true;
				txtRight.forceHover = true;
			}
			if (param.isDefaultValue())
				txtRight.enabledC(defaultValuesColor);
			else if (param.isValidValue())
				txtRight.enabledC(customValuesColor);
			else
				txtRight.enabledC(Color.red);
			
			if (retina) {
				txtLeft.fontMult(retinaFactor);
				txtRight.fontMult(retinaFactor);
			}
				
			if (param.isSubMenu()) {
				txtLeft.enabledC(GameUI.textColor());
				txtRight.forceHover = false;				
				int sw	= txtLeft.stringWidth(gi);
				int dx	= (retina(columnWidth) - sw)/2;
				txtLeft.setScaledXY(dx, retina(rowPad+s7));
				txtLeft.draw(gi);
				gi.dispose();
				if (retina) {
					int y = ySetting-rowPad;
					g.drawImage(img, xSetting, y, xSetting+columnWidth, y+textBoxH,
							0, 0, retina(columnWidth), retina(textBoxH), null);
					txtLeft.fontMult(1);
					txtRight.fontMult(1);
				} 
				else
					g.drawImage(img, xSetting, ySetting-rowPad, null);

				param.updated(false);
				imgList.put(index, img);
				txtLeft.setScaledXY(xSetting+invRetina(dx), ySetting+s7);
				txtLeft.updateBounds(g);
				txtLeft.forceHover  = false;
			}
			else {
				int swLeft	= txtLeft.stringWidth(gi);
				int swRight	= txtRight.stringWidth(gi);
				int sw		= swLeft + swRight;
				int dx		= (retina(columnWidth) - sw)/2;
				txtLeft.setScaledXY(dx, retina(rowPad+s7));
				txtRight.setScaledXY(dx + swLeft, retina(rowPad+s7));
				txtLeft.draw(gi);
				txtRight.draw(gi);
				gi.dispose();
				if (retina) {
					int y = ySetting-rowPad;
					g.drawImage(img, xSetting, y, xSetting+columnWidth, y+textBoxH,
							0, 0, retina(columnWidth), retina(textBoxH), null);
					txtLeft.fontMult(1);
					txtRight.fontMult(1);
				} 
				else
					g.drawImage(img, xSetting, ySetting-rowPad, null);

				param.updated(false);
				imgList.put(index, img);
				txtLeft.setScaledXY(xSetting+invRetina(dx), ySetting+s7);
				txtRight.setScaledXY(xSetting+invRetina(dx+swLeft), ySetting+s7);
				txtLeft.updateBounds(g);
				txtRight.updateBounds(g);			
				txtLeft.forceHover  = false;
				txtRight.forceHover = false;				
			}
		}
		else if (retina) {
			BufferedImage img = imgList.get(index);
			int y = ySetting-rowPad;
			int w = img.getWidth();
			int h = img.getHeight();
			g.drawImage(img, xSetting, y, xSetting+invRetina(w), y+invRetina(h), 0, 0, w, h, null);
		}
		else
			g.drawImage(imgList.get(index), xSetting, ySetting-rowPad, null);
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
			if (hoverBox == btListLeft.get(i).box()
					|| hoverBox == btListRight.get(i).box() ) {
				IParam param = activeList.get(i);
				if (param.isSubMenu()) {
					if (e == null)
						return;
					super.close();
			        param.updated(true);
			        btListLeft.get(i).mouseExit();
			        btListRight.get(i).mouseExit();
			        disableGlassPane();
			        param.toggle(e, GUI_ID, this);
					return;
				}			
				forceUpdate |= param.toggle(e, w, this);
				param.updated(true);
				setValueColor(i);
				btListLeft.get(i).repaint(activeList.get(i).getGuiDisplay(0));
				btListRight.get(i).repaint(activeList.get(i).getGuiDisplay(1));
				if (showGuide())
					loadGuide();
				repaint();
				return;
			}			
		}
	}
	private void setLocalToDefault(boolean excludeCfg, boolean excludeSubMenu) {
		for (IParam param : activeList)
			if (!(excludeCfg && param.isCfgFile())
					&& !(excludeSubMenu && param.isSubMenu()))
			param.setFromDefault(excludeCfg, excludeSubMenu);
	}
	public  void start(String p, BaseModPanel ui) { // Called from subUI
		parentUI = ui;
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
		forceUpdate = true;
		refreshGui();
		forceUpdate = true;
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
		

		if (parentUI != null) {
			if(parentUI instanceof CompactOptionsUI)
				((CompactOptionsUI) parentUI).start();
			else
				parentUI.init();
		}
		else
			RotPUI.instance().mainUI().map().resetRangeAreas();
	}
	@Override protected void doExitBoxAction()		{
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:			// Cancel and exit
		case CTRL_SHIFT:	// Cancel and exit
			guiOptions().updateAllNonCfgFromFile(LIVE_OPTIONS_FILE);
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
			setLocalToDefault(false, true);
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
	@Override public void refreshGui()	{
		super.refreshGui();
		for (int i=0; i<activeList.size(); i++) {
			setValueColor(i);
			btListLeft.get(i).displayText(activeList.get(i).getGuiDisplay(0));
			btListRight.get(i).displayText(activeList.get(i).getGuiDisplay(1));
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
		forceUpdate = false;
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
		//super.mouseEntered(e);
		repaint();
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
		for (ModText bt : btListBoth)
			if (bt.box() == prevHover) {
				repaint();
				break;
			}
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
			if (parentUI!=null)
				parentUI.mouseMoved(e);
			return;
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
