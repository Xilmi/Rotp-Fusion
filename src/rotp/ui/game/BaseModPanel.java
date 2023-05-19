/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	   https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.game;

import static rotp.model.game.MOO1GameOptions.loadAndUpdateFromFileName;
import static rotp.model.game.MOO1GameOptions.setBaseAndModSettingsToDefault;
import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.model.game.BaseOptions.*;

import static rotp.ui.UserPreferences.GAME_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LAST_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;
import static rotp.ui.UserPreferences.USER_OPTIONS_FILE;
import static rotp.ui.util.InterfaceParam.LABEL_DESCRIPTION;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;

import rotp.Rotp;
import rotp.model.game.MOO1GameOptions;
import rotp.ui.BasePanel;
import rotp.ui.BaseText;
import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamButtonHelp;
import rotp.util.LabelManager;
import rotp.util.ModifierKeysState;

public abstract class BaseModPanel extends BasePanel
		implements MouseListener, MouseMotionListener {
 
	private static final String setGlobalDefaultKey	= "SETTINGS_GLOBAL_DEFAULT";
	private static final String setLocalDefaultKey	= "SETTINGS_LOCAL_DEFAULT";
	private static final String setGlobalGameKey	= "SETTINGS_GLOBAL_LAST_GAME";
	private static final String setLocalGameKey		= "SETTINGS_LOCAL_LAST_GAME";
	private static final String setGlobalLastKey	= "SETTINGS_GLOBAL_LAST_SET";
	private static final String setLocalLastKey		= "SETTINGS_LOCAL_LAST_SET";
	private static final String setGlobalUserKey	= "SETTINGS_GLOBAL_USER_SET";
	private static final String setLocalUserKey		= "SETTINGS_LOCAL_USER_SET";
	private static final String saveGlobalUserKey	= "SETTINGS_GLOBAL_USER_SAVE";
	private static final String saveLocalUserKey	= "SETTINGS_LOCAL_USER_SAVE";
	private static final String restoreGlobalKey	= "SETTINGS_GLOBAL_RESTORE";
	private static final String restoreLocalKey		= "SETTINGS_LOCAL_RESTORE";
	private static final String guideKey			= "SETTINGS_GUIDE";
	protected static final String exitKey		 	= "SETTINGS_EXIT";
	protected static final String cancelKey		 	= "SETTINGS_CANCEL";
	protected static final String applyKey		 	= "SETTINGS_APPLY";
	
	private	  static int	 exitButtonWidth, guideButtonWidth,
							 userButtonWidth, defaultButtonWidth, lastButtonWidth;
	protected static int	 mX, mY, w, h;
	protected static int	 smallButtonMargin;
	protected static int	 smallButtonH;
	public	  static int	 guideFontSize;
	public	  static boolean autoGuide	= false; // To disable automated Guide
	public	  static boolean dialGuide	= false; // To disable automated Guide on dialog list
	private	  static boolean contextHlp	= false; // The time to show  the contextual help

	private	  final LinkedList<PolyBox>	polyBoxList	= new LinkedList<>();
	protected final LinkedList<Box>		boxBaseList	= new LinkedList<>();
	protected final LinkedList<Box>		boxHelpList	= new LinkedList<>();
	protected Box	  hoverBox;
	protected Box	  prevHover;
	protected PolyBox hoverPolyBox;
	private	  PolyBox prevPolyBox;
	protected boolean hoverChanged;

	LinkedList<InterfaceParam> paramList;
	
	private boolean initialised = false;
	LinkedList<InterfaceParam> duplicateList;
	LinkedList<InterfaceParam> activeList;
	
	protected void singleInit() {} // To avoid call to options during class creation
//	protected abstract void singleInit(); // To avoid call to options during class creation
	
	public GuidePopUp guidePopUp;
	
	protected static final ParamButtonHelp exipButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"SETTINGS_BUTTON_EXIT",
			exitKey,
			applyKey,
			cancelKey,
			"");
	private static final ParamButtonHelp userButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"SETTINGS_BUTTON_USER",
			setGlobalUserKey,
			setLocalUserKey,
			saveGlobalUserKey,
			saveLocalUserKey);
	private static final ParamButtonHelp lastButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"SETTINGS_BUTTON_LAST",
			setGlobalLastKey,
			setLocalLastKey,
			setGlobalGameKey,
			setLocalGameKey);
	private static final ParamButtonHelp defaultButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"SETTINGS_BUTTON_DEFAULT",
			setGlobalDefaultKey,
			setLocalDefaultKey,
			restoreGlobalKey,
			restoreLocalKey);

	protected Font smallButtonFont	= narrowFont(20);
	protected Box defaultBox		= new Box(defaultButtonHelp);
	protected Box lastBox			= new Box(lastButtonHelp);
	protected Box userBox			= new Box(userButtonHelp);
	protected Box guideBox			= new Box(guideKey);

	protected boolean globalOptions	= false; // No preferred button and Saved to remnant.cfg

	protected BaseModPanel () {
		if (guidePopUp == null)
			guidePopUp = new GuidePopUp();
		guidePopUp.init();
	}
	protected abstract String GUI_ID();
	private void localInit(Graphics2D g) {
		Font prevFont = g.getFont();
		g.setFont(smallButtonFont);

		initExitButtonWidth(g);
		initGuideButtonWidth(g);
		initUserButtonWidth(g);
		initDefaultButtonWidth(g);
		initLastButtonWidth(g);

		g.setFont(prevFont);
		guidePopUp.init();
	}
	private int stringWidth(Graphics2D g, String key) {
		return g.getFontMetrics().stringWidth(LabelManager.current().label(key));
	}
	private int buttonWidth(Graphics2D g, String[] keys) {
		int result = 0;
		for (String key : keys)
			result = max(result, stringWidth(g, key));
		return smallButtonMargin + result;
	}
	
	protected void refreshGui() {}
	protected MOO1GameOptions guiOptions() { return RotPUI.mergedGuiOptions(); }

	protected boolean guiCallFromGame() { return RotPUI.guiCallFromGame(); }
	@Override public void repaintButtons() { repaint(); }
	protected void init() {
		if (!initialised) {
			singleInit();
			initialised = true;
		}
		ModifierKeysState.reset();
		w = RotPUI.setupRaceUI().getWidth();
		h = RotPUI.setupRaceUI().getHeight();
		smallButtonMargin = s30;
		smallButtonH	  = s30;
	}
	protected void close() { 
		ModifierKeysState.reset();
		disableGlassPane();
	}

	// ---------- Exit Button
	protected String exitButtonKey() { return exitKey;}
	private void initExitButtonWidth(Graphics2D g) {
		exitButtonWidth = buttonWidth(g, new String[] {exitKey});
	}
	protected int exitButtonWidth(Graphics2D g) {
		if (exitButtonWidth == 0)
			localInit(g);
		return exitButtonWidth;
	}
	protected void doExitBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			// loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			// break;
		default: // Save
			updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break; 
		}
		close();
	}
	protected String exitButtonDescKey() {
		return exitButtonKey() + LABEL_DESCRIPTION;
	}

	// ---------- Guide Button
	protected String guideButtonKey() { return guideKey; }
	private void initGuideButtonWidth(Graphics2D g) {
		guideButtonWidth = buttonWidth(g, new String[] {guideKey});
	}
	protected int guideButtonWidth(Graphics2D g) {
		if (guideButtonWidth == 0) 
			localInit(g);
		return guideButtonWidth;
	}
	protected void doGuideBoxAction() {
		buttonClick();
		autoGuide = !autoGuide;
		dialGuide &= autoGuide;
		if (autoGuide)
			loadGuide();
		else
			clearGuide();
		paintComponent(getGraphics());
	}	
	protected String guideButtonDescKey() {
		return guideButtonKey() + LABEL_DESCRIPTION;
	}

	// ---------- User Button
	protected String userButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:		 return saveGlobalUserKey;
		case CTRL_SHIFT: return saveLocalUserKey;
		case SHIFT:		 return setLocalUserKey;
		default:		 return setGlobalUserKey;
		}
	}
	private void initUserButtonWidth(Graphics2D g) {
		userButtonWidth = buttonWidth(g, new String[] {
				saveGlobalUserKey, saveLocalUserKey, setLocalUserKey, setGlobalUserKey});
	}
	protected int userButtonWidth(Graphics2D g) {
		if (userButtonWidth == 0) 
			localInit(g);
		return userButtonWidth;
	}
	protected void doUserBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL: // saveGlobalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			return;
		case CTRL_SHIFT: // saveLocalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID());
			return;
		case SHIFT: // setLocalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID());
			refreshGui();
			return;
		default: // setGlobalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			refreshGui();
		}
	}	
	protected String userButtonDescKey() {
		return userButtonKey() + LABEL_DESCRIPTION;
	}

	// ---------- Default Button
	protected String defaultButtonKey() {
		if (globalOptions)  // The old ways
			switch (ModifierKeysState.get()) {
			case CTRL:
			case CTRL_SHIFT: return restoreLocalKey;
			default:		 return setLocalDefaultKey;
			}
		else
			switch (ModifierKeysState.get()) {
			case CTRL:		 return restoreGlobalKey;
			case CTRL_SHIFT: return restoreLocalKey;
			case SHIFT:		 return setLocalDefaultKey;
			default:		 return setGlobalDefaultKey;
			}
	}
	private void initDefaultButtonWidth(Graphics2D g) {
		defaultButtonWidth = buttonWidth(g, new String[] {
				restoreGlobalKey, restoreLocalKey, setLocalDefaultKey, setGlobalDefaultKey});
	}
	protected int defaultButtonWidth(Graphics2D g) {
		if (defaultButtonWidth == 0) 
			localInit(g);
		return defaultButtonWidth;
	}
	protected void doDefaultBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL: // restoreGlobalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);		
			break;
		case CTRL_SHIFT: // restoreLocalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, GUI_ID());		
			break;
		case SHIFT: // setLocalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), GUI_ID());		
			break; 
		default: // setGlobalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), ALL_GUI_ID);		
			break; 
		}
		refreshGui();
	}
	protected String defaultButtonDescKey() {
		return defaultButtonKey() + LABEL_DESCRIPTION;
	}

	// ---------- Last Button
	protected String lastButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:		 return setGlobalGameKey;
		case CTRL_SHIFT: return setLocalGameKey;
		case SHIFT:		 return setLocalLastKey;
		default:		 return setGlobalLastKey;
		}
	}
	private void initLastButtonWidth(Graphics2D g) {
		lastButtonWidth = buttonWidth(g, new String[] {
				setGlobalGameKey, setLocalGameKey, setLocalLastKey, setGlobalLastKey});
	}
	protected int lastButtonWidth(Graphics2D g) {
		if (lastButtonWidth == 0) 
			localInit(g);
		return lastButtonWidth;
	}
	protected void doLastBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL: // setGlobalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, ALL_GUI_ID);
			break;
		case CTRL_SHIFT: // setLocalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, GUI_ID());
			break;
		case SHIFT: // setLocalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, GUI_ID());
			break;
		default: // setGlobalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, ALL_GUI_ID);
		}
		refreshGui();
	}
	protected String lastButtonDescKey() {
		return lastButtonKey() + LABEL_DESCRIPTION;
	}

	// ---------- Events management
	@Override public void mouseClicked(MouseEvent e) {  }
	@Override public void mousePressed(MouseEvent e) {  }
	@Override public void mouseEntered(MouseEvent e) {
		ModifierKeysState.reset();
		repaintButtons();
	}
	@Override public void mouseExited(MouseEvent e)	 { clearGuide(); }
	@Override public void mouseDragged(MouseEvent e) {  }
	@Override public void mouseMoved(MouseEvent e)	 {
		mX = e.getX();
		mY = e.getY();
		if (hoverBox != null && hoverBox.contains(mX,mY)) {
			hoverChanged = false;
			return;
		}
		hoverChanged = true;
		prevHover	 = hoverBox;
		prevPolyBox	 = hoverPolyBox;
		hoverPolyBox = null;
		hoverBox	 = null;

		for (Box box : boxBaseList)
			if (box.contains(mX,mY)) {
				hoverBox = box;
				break;
			}
		if (hoverBox != prevHover) {
			loadGuide();
			repaint();
			return;
		}
		for (PolyBox box : polyBoxList)
				if (box.contains(mX,mY)) {
					hoverPolyBox = box;
					break;
				}
		if (hoverPolyBox != prevPolyBox) {
			repaint();
		}
	}
	@Override public void keyReleased(KeyEvent e)	 { checkModifierKey(e); }
	@Override public void keyPressed(KeyEvent e)	 {
		checkModifierKey(e);
		switch(e.getKeyCode()) {
			case KeyEvent.VK_F1:
				if (showContextualHelp())
					return;
				showHelp(); // Panel Help
				return;
			case KeyEvent.VK_G:
				doGuideBoxAction();
				return;
		}
	}
	// ---------- Help management
	protected void loadGuide()						 {
		if (hoverBox == null) {
			clearGuide();
			return;
		}
		if (!(autoGuide || dialGuide))
			return;
		guidePopUp.setDest(hoverBox, false, getGraphics());
	}
	private boolean showContextualHelp()			 { // Following "F1!
		if (hoverBox == null)
			return false; // ==> panel help
		
	  	if (!guidePopUp.setDest(hoverBox, true, getGraphics()))
	  		return false; // ==> panel help
	  	contextHlp = true;
	  	return true;
	}
	protected void showGuide(Graphics g)			 {
		if (!(autoGuide || dialGuide || contextHlp))
			return;
		guidePopUp.paintGuide(g);
	}
	private void clearGuide()						 {
		guidePopUp.clear();
		contextHlp = false;
	}
	// ========== Sub Classes ==========
	//
	class Box extends Rectangle {
		private InterfaceParam	param;
		private String			label;
		private ModText         modText;
		private int 			mouseBoxIndex;
		// ========== Constructors ==========
		//
		public Box()				{ addToList(); }
		private Box(boolean add)	{ if (add) addToList(); }
		private Box(ModText modText, boolean addToList) {
			this(addToList);
//			boxHelpList.add(this);
			this.modText = modText;
		}
		Box(String label)			{
			this();
			boxHelpList.add(this);
			this.label = label;
		}
		Box(InterfaceParam param)	{
			this();
			boxHelpList.add(this);
			this.param = param;
		}
		Box(InterfaceParam param, int mouseBoxIndex) {
			this(param);
			mouseBoxIndex(mouseBoxIndex);
		}
		public	void removeFromList()				 { boxBaseList.remove(this); }
		private void addToList() 					 { boxBaseList.add(this); }
		private void initGuide(String label)		 { this.label = label; }
		private void initGuide(InterfaceParam param) { this.param = param; }
		InterfaceParam param()	 					 { return param; }
		private void mouseBoxIndex(int idx)			 { mouseBoxIndex = idx; }
		// ========== Doers ==========
		//
		boolean checkIfHovered() { return checkIfHovered(null); }
		boolean checkIfHovered(JTextPane descBox) {
			if (contains(mX,mY)) {
				hoverBox = this;
				if (descBox != null)
					descBox.setText(getDescription());
				hoverChanged = (hoverBox != prevHover);
				if (hoverChanged) {
					mouseEnter();
					if (descBox != null)
						descBox.setText(getDescription());
					loadGuide();
					if (prevHover != null) {
						prevHover.mouseExit();
						repaint(prevHover);
					}
					repaint();					
				}
				return true;
			}
			return false;
		}
		void mouseEnter() {
			if (modText != null)
				modText.mouseEnter();
		}
		void mouseExit() {
			if (modText != null)
				modText.mouseExit();
		}
		// ========== Getters ==========
		//
		public String getDescription()		 {
			String desc = getParamDescription();
			if (desc == null || desc.isEmpty()) {
				desc = getLabelDescription();
				if (desc == null)
					return "";
			}
			return desc;
		}
		private String getFullHelp()		 {
			String help = getParamFullHelp();
			if (help == null || help.isEmpty()) {
				help = getLabelHelp();
				if (help == null)
					return "";
			}
			return help;
		}
		String getHelp()					 {
			String help = getParamHelp();
			if (help == null || help.isEmpty()) {
				help = getLabelHelp();
				if (help == null)
					return "";
			}
			return help;
		}
		private String getGuide()					 {
			String guide = getParamGuide();
			if (guide == null || guide.isEmpty()) {
				guide = getLabelHelp();
				if (guide == null)
					return "";
			}
			return guide;
		}
		public 	int	   mouseBoxIndex()		 { return mouseBoxIndex; }
		String getLabelDescription()		 { return InterfaceParam.langDesc(label); }
		private String getLabelHelp()		 { return InterfaceParam.langHelp(label); }
		private String getParamDescription() {
			if (param == null)
				return "";
			String desc = param.getGuiDescription();
			if (desc == null || desc.isEmpty())
				return param.getToolTip();
			return desc;
		}
		private String getParamHelp()	 {
			if (param == null)
				return "";
			return param.getHelp();
		}
		private String getParamFullHelp()	 {
			if (param == null)
				return "";
			return param.getFullHelp();
		}
		private String getParamGuide()		 {
			if (param == null)
				return "";
			return param.getGuide();
		}
	}
	class PolyBox extends Polygon {
		// ========== Constructors ==========
		//
		PolyBox() { polyBoxList.add(this); }
	}
	public class ModText extends BaseText {

		private final Box box;

		/**
		* @param p		BasePanel
		* @param logo	logoFont
		* @param fSize	fontSize
		* @param x1	xOrig
		* @param y1	yOrig
		* @param c1	enabledC
		* @param c2	disabledC
		* @param c3	hoverC
		* @param c4	depressedC
		* @param c5	shadeC
		* @param i1	bdrStep
		* @param i2	topLBdr
		* @param i3	btmRBdr
		*/
//		public ModText(BasePanel p, boolean logo, int fSize, int x1, int y1, Color c1, Color c2, Color c3, Color c4,
//				Color c5, int i1, int i2, int i3) {
//			super(p, logo, fSize, x1, y1, c1, c2, c3, c4, c5, i1, i2, i3);
//			box = new Box(this, true);
//		}
		
		/**
		* @param p		BasePanel
		* @param fSize	fontSize
		* @param c1		enabledC
		* @param c2		disabledC
		* @param c3		hoverC
		* @param c4		depressedC
		* @param c5		shadeC
		* @param add	add to box list
		*/
		public ModText(BasePanel p, int fSize, Color c1, Color c2, Color c3, Color c4, Color c5, boolean add) {
			super(p, false, fSize, 0, 0, c1, c2, c3, c4, c5, 0, 0, 0);
			box = new Box(this, add);
		}
		public void	   removeBoxFromList()				{ box.removeFromList(); }
		public ModText initGuide(InterfaceParam param)	{ box.initGuide(param); return this; }
		public ModText initGuide(String label)			{ box.initGuide(label); return this; }
		Box box() {
			box.setBounds(bounds());
			return box;
		}
	}
	// ===============================================================================
	public class GuidePopUp {
		private static final int FONT_SIZE	= 16;
		private final int maxWidth      = scaled(400);
		private final Color guideColor	= GameUI.setupFrame();
		private final Color helpColor	= new Color(240,240,240);
		private final Color lineColor	= Color.white;
		private final JTextPane border	= new JTextPane();
		private final JTextPane	margin	= new JTextPane();
		private final JTextPane pane	= new JTextPane();
		private Rectangle dest			= new Rectangle(0,0,0,0);
		private String text;
		private int x, y, w, h;
		private int[] lineArr;
		private boolean fullHelp;
		private Color bgC  = guideColor;
		private Color bdrC = new Color(bgC.getRed(), bgC.getGreen(), bgC.getBlue(), 160);

		// ========== Constructors and initializers ==========
		//	
		GuidePopUp()		{ }
		private void init() {
			add(border, 0);
			add(margin, 0);
			add(pane, 0);
			border.setOpaque(true);
			margin.setOpaque(true);
			pane.setOpaque(true);
			pane.setContentType("text/html");
			pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
			hide();
			guideFontSize = FONT_SIZE;
		}
		private void setText(String newText)	{ text = newText; }
		private void setDest(Rectangle newDest)	{
			dest = newDest;
			setVisible();
			init(dest);
		}
		private void setFullHelp(boolean full)	{ fullHelp = full; }
		public  void setDest(Rectangle dest, String text, Graphics g0)	{
			setFullHelp(false);
			setText(text);
			setDest(dest);
		}
		private boolean setDest(Box dest, boolean fullHelp, Graphics g0){
			if (dest == null)
				return false;
			String txt;
			if (fullHelp)
			  	txt = dest.getFullHelp();
			else
			  	txt = dest.getGuide();
			if (txt == null || txt.isEmpty())
		  		return false;
			setFullHelp(fullHelp);
			setText(txt);
			setDest(dest);
			return true;
		}
		// ========== Shared Methods ==========
		//
	    private void setVisible()		{
	    	if(pane.isVisible())
	    		return;
			border.setVisible(true);
			margin.setVisible(true);
	    	pane.setVisible(true);
	    }
	    private void hide()				{
	    	border.setVisible(false);
	    	margin.setVisible(false);
	    	pane.setVisible(false);
	    }
	    public void clear()		{ hide(); }
		// ========== Private Methods ==========
		//
		private void paintGuide(Graphics g0)	{
			if (dest == null)
				return;
			if (!pane.isVisible())
				return;
			Graphics2D g = (Graphics2D) g0;
			setVisible();
			border.setBackground(bdrC);
			border.setBounds(x-s8, y-s8, w+s16, h+s16);
			margin.setBackground(bgC);
			margin.setBounds(x-s3, y-s3, w+s6, h+s6);
			pane.setFont(plainFont(guideFontSize));
			pane.setBackground(bgC);
			pane.setBounds(x, y, w, h);
			drawLines(g);
		}
        private void setLineArr(int... arr)		{ lineArr = arr; }
		private void drawLines(Graphics2D g)	{
			if (lineArr != null) {
				Stroke prev = g.getStroke();
				g.setStroke(stroke2);
				g.setColor(lineColor);
				int size = lineArr.length/2 - 1;
				for (int i=0; i<size; i++) {
					int k = 2*i;
					g.drawLine(lineArr[k], lineArr[k+1], lineArr[k+2], lineArr[k+3]);
				}
				g.setStroke(prev);
			}			
		}
		private void autoSize(int width)		{
    		int iW = scaled(Rotp.IMG_W - 20);
    		int iH = scaled(Rotp.IMG_H - 20);
    		int testW, preTest;
			bgC  = fullHelp ? helpColor : guideColor;
			bdrC = new Color(bgC.getRed(), bgC.getGreen(), bgC.getBlue(), 160);
			w = Short.MAX_VALUE;
			guideFontSize = FONT_SIZE+1;
			guideFontSize = FONT_SIZE;
			boolean go = true;
			while (go) {
//				System.out.println("guideFontSize = " + guideFontSize);
				pane.setFont(plainFont(guideFontSize));
				h = Short.MAX_VALUE;
				preTest = -1;
				testW = width;
				while (h > iH && preTest != testW && testW < iW) {
					preTest = testW;
		    		pane.setSize(new Dimension(testW, Short.MAX_VALUE));
		    		pane.setText(text);
		    		w = min(testW, pane.getPreferredSize().width);
		    		h = pane.getPreferredSize().height;
		    		testW *= (float) h /iH;
//					System.out.println("iW " + iW + " w " + w + " testW " + testW + " iH " + iH+ "  h " + h);
				}
				go = (w > iW || h > iH);
				if (go) {
					guideFontSize = max (1, min(guideFontSize-1,
												(int)(guideFontSize * (float)iH/h -1)));
					go = guideFontSize > 1;
//					System.out.println("iW " + iW + " w " + w + " iH " + iH+ "  h " + h);
				}
			}
    		margin.setSize(new Dimension(w+s6, h+s6));
    		border.setSize(new Dimension(w+s16, h+s16));
    		pane.setSize(new Dimension(w, h));
		}
		private void init(Rectangle dest)		{ init(dest, s20, s20); }
		private void init(Rectangle dest, int xShift, int yShift) {
			init(dest, xShift, yShift, s10, s10); }
        private void init(Rectangle dest, int xShift, int yShift, int xCover, int yCover) {
        	init(dest, xShift, yShift, xCover, yCover, s10, s10); }
        private void init(Rectangle dest,
        		int xShift, int yShift, int xCover, int yCover, int xMargin, int yMargin) {
    		int xb, xd, yb, yd;
    		int iW = scaled(Rotp.IMG_W);
    		int iH = scaled(Rotp.IMG_H);
    		autoSize(maxWidth);
    		// relative position
    		// find X location
     		if (2*dest.x + dest.width  > iW) { // put box to the left
    			x = dest.x - w - xShift;
    			if (x < xMargin)
    				x = xMargin;
    			xb = x + w;
	   			xd = dest.x + xCover;
	   			if (xd < xb)
	   				xd = xb + s10;
    		}
    		else { // put box to the right
    			x = dest.x + dest.width + xShift;
    			if (x+w > iW-xMargin)
    				x = iW-xMargin - w;
	   			xb = x;
	   			xd = dest.x + dest.width - xCover;
	   			if (xd > xb)
	   				xd = xb - s10;
    		}
    		// find Y location
     		if (2*dest.y + dest.width  > iH) { // put box to the top
    			y = dest.y - h - yShift;
    			if (y < yMargin)
    				y = yMargin;
    			yb = y + h;
	   			yd = dest.y + yCover;
	   			if (yd < yb)
	   				yb = yd + s10;
    		}
    		else { // put box to the bottom
    			y = dest.y + dest.height + yShift;
    			if (y+h > iH-yMargin)
    				y = iH-yMargin - h;
	   			yb = y;
	   			yd = dest.y + dest.height - yCover;
	   			if (yd > yb)
	   				yb = yd - s10;
    		}
    		setLineArr(xb, yb, xd, yd);
        }
	}
}
