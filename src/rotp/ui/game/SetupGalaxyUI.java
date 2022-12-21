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

import static rotp.model.empires.CustomRaceDefinitions.getAllowedAlienRaces;
import static rotp.model.empires.CustomRaceDefinitions.getBaseRacList;
import static rotp.model.game.MOO1GameOptions.loadAndUpdateFromFileName;
import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.GALAXY_TEXT_FILE;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;
import static rotp.ui.UserPreferences.galaxyPreviewColorStarsSize;
import static rotp.ui.UserPreferences.globalCROptions;
import static rotp.ui.UserPreferences.prefStarsPerEmpire;
import static rotp.ui.UserPreferences.shapeOption3;
import static rotp.ui.UserPreferences.showNewRaces;
import static rotp.ui.UserPreferences.useSelectableAbilities;

// modnar: needed for adding RenderingHints
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import rotp.Rotp;
import rotp.mod.br.addOns.RacesOptions;
import rotp.mod.br.profiles.Profiles;
import rotp.model.empires.Race;
import rotp.model.galaxy.GalaxyFactory.GalaxyCopy;
import rotp.model.galaxy.GalaxyShape;
import rotp.model.galaxy.GalaxyShape.EmpireSystem;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.ui.BaseModPanel;
import rotp.ui.NoticeMessage;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.ListDialog;
import rotp.ui.util.Modifier2KeysState;
import rotp.ui.util.SpecificCROption;

public final class SetupGalaxyUI  extends BaseModPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
    // public  static final String guiTitleID	= "SETUP_GALAXY";
	public  static final String GUI_ID       = "START_GALAXY";
	private static final String BACK_KEY	 = "SETUP_BUTTON_BACK";
	private static final String RESTART_KEY	 = "SETUP_BUTTON_RESTART";
	private static final String START_KEY	 = "SETUP_BUTTON_START";
	private static final String SIZE_OPT_KEY = "SETUP_GALAXY_SIZE_STAR_PER_EMPIRE";
	private static final String NO_SELECTION = "SETUP_BITMAP_NO_SELECTION";
	public static int MAX_DISPLAY_OPPS = 49;
	private BufferedImage backImg, playerRaceImg;
	private BufferedImage smBackImg;
    private int bSep = s15;

	private Rectangle modASettingsBox		= new Rectangle(); // modnar: add UI panel for modnar MOD game options
	private Rectangle modBSettingsBox		= new Rectangle(); // BR: Second UI panel for MOD game options
	private Rectangle globalModSettingsBox	= new Rectangle(); // BR: Display UI panel for MOD game options
	private Rectangle backBox		= new Rectangle();
	private Rectangle startBox		= new Rectangle();
	private Rectangle settingsBox	= new Rectangle();
	private Rectangle newRacesBox	= new Rectangle(); // BR:
	private Rectangle showAbilityBox= new Rectangle(); // BR:
	private Rectangle shapeBox		= new Rectangle();
	private Polygon shapeBoxL		= new Polygon();
	private Polygon shapeBoxR		= new Polygon();
	private Rectangle mapOption1Box	= new Rectangle();
	private Polygon mapOption1BoxL	= new Polygon();
	private Polygon mapOption1BoxR	= new Polygon();			 
	private Rectangle mapOption2Box	= new Rectangle();
	private Polygon mapOption2BoxL	= new Polygon();
	private Polygon mapOption2BoxR	= new Polygon();			 
	private Rectangle mapOption3Box	= new Rectangle(); // BR:
	private Rectangle sizeOptionBox	= new Rectangle(); // BR:
	private Polygon sizeOptionBoxL	= new Polygon();   // BR:
	private Polygon sizeOptionBoxR	= new Polygon();   // BR:
	private Rectangle sizeBox	= new Rectangle();
	private Polygon sizeBoxL	= new Polygon();
	private Polygon sizeBoxR	= new Polygon();
	private Rectangle diffBox	= new Rectangle();
	private Polygon diffBoxL	= new Polygon();
	private Polygon diffBoxR	= new Polygon();
	private Rectangle oppBox	= new Rectangle();
	private Polygon  oppBoxU	= new Polygon();
	private Polygon oppBoxD		= new Polygon();
	private Rectangle aiBox		= new Rectangle();
	private Polygon  aiBoxL		= new Polygon();
	private Polygon aiBoxR		= new Polygon();
	private Rectangle crBox		= new Rectangle(); // dataRace selection
	private Polygon  crBoxL		= new Polygon(); // BR:
	private Polygon crBoxR		= new Polygon(); // BR:

	private Rectangle[] oppSet = new Rectangle[MAX_DISPLAY_OPPS];
	private Rectangle[] oppAI = new Rectangle[MAX_DISPLAY_OPPS];
	private Rectangle[] oppCR = new Rectangle[MAX_DISPLAY_OPPS]; // BR: dataRace selection

	private Shape hoverBox;
	private boolean starting = false;
	private int leftBoxX, rightBoxX, boxW, boxY, leftBoxH, rightBoxH;
	private int galaxyX, galaxyY, galaxyW, galaxyH;
	private String[] specificAbilitiesList; 
	private String[] globalAbilitiesList; 
	private String[] galaxyTextList;
    private Font dialogMonoFont;
    private int dialogMonoFontSize = 20;
    private Font boxMonoFont;
    private int boxMonoFontSize = 15;
    
	private Font boxMonoFont() {
    	if (boxMonoFont == null)
			boxMonoFont = galaxyFont(scaled(boxMonoFontSize));
    	return boxMonoFont;
    }
	private Font dialogMonoFont() {
    	if (dialogMonoFont == null)
			dialogMonoFont = galaxyFont(scaled(dialogMonoFontSize));
    	return dialogMonoFont;
    }
	public SetupGalaxyUI() {
		init0();
	}
	private void init0() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		for (int i=0;i<oppSet.length;i++)
			oppSet[i] = new Rectangle();
		for (int i=0;i<oppAI.length;i++)
			oppAI[i] = new Rectangle();
		for (int i=0;i<oppCR.length;i++)
			oppCR[i] = new Rectangle();
	}
	private void initAbilitiesList() {
		// specific
		LinkedList<String> list = new LinkedList<>();
		list.addAll(SpecificCROption.options());
		list.addAll(getAllowedAlienRaces());
		list.addAll(getBaseRacList());
		specificAbilitiesList = list.toArray(new String[list.size()]);
		// global
		list.clear();
		list.addAll(globalCROptions.getBaseOptions());
		list.addAll(getAllowedAlienRaces());
		list.addAll(getBaseRacList());
		globalAbilitiesList = list.toArray(new String[list.size()]);
	}
	public void init() {
		Modifier2KeysState.reset();
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		refreshGui();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override protected void refreshGui() {
        initAbilitiesList();
        guiOptions().setAndGenerateGalaxy();
        backImg = null;
        repaint();
	}

	private void release() {
		backImg = null;
		playerRaceImg  = null;
		boxMonoFont    = null;
		dialogMonoFont = null;
		galaxyTextList = null;
	}
    private void doStartBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			restartGame();
			return;
		default: // Save
			startGame();
			return; 
		}
 	}
    private void doBackBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			// loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			// break;
		default: // Save
			updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break; 
		}
    	// Go back to Race Panel
		RotPUI.instance().returnToSetupRacePanel();
		release();
 	}
	private static String backButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			// return restoreKey;
		default:
			return BACK_KEY;
		}
	}
	private static String startButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return RESTART_KEY;
		default:
			return START_KEY;
		}
	}
	private int currentSpecificAbilityIndex(String s) {
		for (int i=0; i<specificAbilitiesList.length; i++) {
			if (s.equalsIgnoreCase((String) specificAbilitiesList[i]))
				return i;
		}
		return -1;
	}
	private int currentGlobalAbilityIndex(String s) {
		for (int i=0; i<globalAbilitiesList.length; i++) {
			if (s.equalsIgnoreCase((String) globalAbilitiesList[i]))
				return i;
		}
		return -1;
	}
	@SuppressWarnings("rawtypes")
	private void setFileChooserFont(Component[] comp) {
		int topInset  = scaled(6);
		int sideInset = scaled(15);
	    for(int i=0; i<comp.length; i++)  {
	    	if(comp[i] instanceof JPanel){
	            ((JPanel)comp[i]).setBackground(GameUI.borderMidColor());
	            if(((JPanel)comp[i]).getComponentCount() !=0){
	            	setFileChooserFont(((JPanel)comp[i]).getComponents());
	            }
	        }
	        if(comp[i] instanceof JTextField){
	            ((JTextField)comp[i]).setBackground(GameUI.setupFrame());
	        }
	        if(comp[i] instanceof JToggleButton){
	            ((JToggleButton)comp[i]).setBackground(GameUI.setupFrame());
	        }
	        if(comp[i] instanceof JButton){
	            String txt = ((JButton)comp[i]).getText();
	            if (txt!=null && ("Cancel".equals(txt) || "Open".equals(txt))) {
		            ((JButton)comp[i]).setMargin(new Insets(topInset, sideInset, 0, sideInset));
		            ((JButton)comp[i]).setBackground(GameUI.buttonBackgroundColor());
		            ((JButton)comp[i]).setForeground(GameUI.buttonTextColor());
		            ((JButton)comp[i]).setVerticalAlignment(SwingConstants.TOP);
	            }
	        }
	        if(comp[i] instanceof JScrollPane){
	            ((JScrollPane)comp[i]).setBackground(GameUI.borderMidColor());
	        }
	        if(comp[i] instanceof JList){
	            ((JList)comp[i]).setBackground(GameUI.setupFrame());
	            ((JList)comp[i]).setSelectionBackground(GameUI.borderMidColor());
	        }
	        if(comp[i] instanceof JComboBox){
	            ((JComboBox)comp[i]).setBackground(GameUI.setupFrame());
	        }
	        if(comp[i] instanceof Container)
	        	setFileChooserFont(((Container)comp[i]).getComponents());
	        try{comp[i].setFont(narrowFont(15));}
	        catch(Exception e){}//do nothing
	    }
	}
	private String getBitmapFile() {
        String dirPath = Rotp.jarPath();
        File selectedFile = new File(shapeOption3.get());
        if (selectedFile.exists())
        	dirPath = selectedFile.getParentFile().getAbsolutePath();
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			protected JDialog createDialog(Component parent)
	                throws HeadlessException {
	            JDialog dlg = super.createDialog(parent);
	            dlg.setLocation(scaled(300), scaled(200));
	            dlg.setSize(scaled(400), scaled(450));
	            dlg.getContentPane().setBackground(GameUI.borderMidColor());
	            return dlg;
	        }
	    };
	    setFileChooserFont(fileChooser.getComponents());
		fileChooser.setCurrentDirectory(new File(dirPath));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Images (bmp, gif, jpg, png, webp)", "bmp", "gif", "jpg", "jpeg", "png", "webp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Windows Bitmap (bmp)", "bmp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Compuserve GIF (gif)", "gif"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"JPG / JPEG Format (jpg, jpeg)", "jpg", "jpeg"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Portable Network Graphics Files (png)", "png"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"Wippy File Format (webp)", "webp"));
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				"All files (*.*", "*"));
		// Add listener to file picking
		fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
				if (fileChooser.getSelectedFile() != null) {
					preview(fileChooser.getSelectedFile().getPath());
				}
            }
        });
		int result = fileChooser.showOpenDialog(getParent());
		if (result == JFileChooser.APPROVE_OPTION) {
		    // user selects a file
			selectedFile = fileChooser.getSelectedFile();
			return selectedFile.getPath();
		}
		return shapeOption3.defaultValue();
	}
	private void selectBitmapFromList() {
		String filePath = getBitmapFile();
		shapeOption3.set(filePath);
		newGameOptions().galaxyShape().quickGenerate();
		repaint();
	}
	private int currentGalaxyTextIndex(String s) {
		String[] textList = getGalaxyTextList();
		for (int i=0; i<textList.length; i++) {
			if (s.equalsIgnoreCase((String) textList[i]))
				return i;
		}
		return -1;
	}
	private void initGalaxyTextFile(File file) {
		try (FileOutputStream fout = new FileOutputStream(file);
			// modnar: change to OutputStreamWriter, force UTF-8
			PrintWriter out = new PrintWriter(new OutputStreamWriter(fout, "UTF-8")); ) {
			out.println( "	List of customized Text Galaxies");
			out.println( "	Use a \"tab\" as separator to add comments");
			out.println();
			out.println( "ROTP	// Initial options");
			out.println( "ℳo○𝟏	// The precursor!");
			out.println();
			out.println("	A nice selection by U/dweller_below");
			out.println();
			out.println( "∞	Infinity feels good, but gameplay is the same as 8");
			out.println( "☸	The wheel of Dharma also feels appropriate");
			out.println( "༜	The Tibetan Sign Rdel Dkar Gsum gives 3 close rings. And it stacks well in multiple lines");
			out.println( "༶	The Tibetan Mark Caret gives 4 widely spaced star fields");
			out.println( "❖	This one gives 4 star fields with 8 to 13 light year spacing.");
			out.println( "ⵘ	Tifinagh Letter Ayer Yagh gives 5 star fields");
			out.println( "∴∵	You can stack or repeat these 2 characters for multiples of 3 or 6.");
			out.println( "፨	The Ethiopic Paragraph Separator is a nice 7 star fields.");
			out.println( "⁂");
			out.println( "🂓");
			out.println( "░");
			out.println( "▒");
			out.println( "⨌");
			out.println( "🦌");
			out.println( "⛄");
			out.println( "🎅");
			out.println( "🎄");
			out.println();
			out.println("	And more ...");
			out.println();
			out.println( "☃");
			out.println( "👽");
			out.println( "⌨");
			out.println( "⸎");
			out.println( "ꔘ");
			out.println( "꙰");
			out.println( "҈");
			out.println( "҉");
			out.println( "۞");
			out.println( "ꙮ");
			out.println( "𐩕");
			out.println( "֍");
			out.println( "֎");
			out.println( "☷");
			out.println( "❉");
			out.println( "⛆");
			out.println( "⣿");
			out.println( "𓃑");
			out.println( "𖡼");
			out.println( "𖥚");
			out.println( "፠");
			out.println( "⁂");
			out.println( "፤፤");
			out.println( "𐄳");
			out.println( "𐧾");
			out.println( "𐮜");
			out.println( "𑗗");
			out.println( "𝅂");
			out.println( "𞡜");
			out.println();
		}
		catch (IOException e) {
			System.err.println("GalaxyTextFile.save -- IOException: "+ e.toString());
		}
	}
	private String[] getGalaxyTextList() {
		if (galaxyTextList != null)
			return galaxyTextList;
		LinkedList<String> list = new LinkedList<>();
		// list.add(newGameOptions().selectedHomeWorldName());
		String path = Rotp.jarPath();
		String galaxyfile = GALAXY_TEXT_FILE;
		File file = new File(path, galaxyfile);
		if (!file.exists())
			initGalaxyTextFile(file);
			
		try ( BufferedReader in = new BufferedReader( new InputStreamReader( new FileInputStream(file), "UTF-8"));) {
			String input;
			while ((input = in.readLine()) != null) {
				String[] args = input.split("\t");
				String val = args[0].trim();
				if (!val.isEmpty())
					list.add(val);
			}				
		}
		catch (FileNotFoundException e) {
			System.err.println(path+galaxyfile+" not found.");
		}
		catch (IOException e) {
			System.err.println("GalaxyTextFile.load -- IOException: "+ e.toString());
		}

		galaxyTextList = list.toArray(new String[list.size()]);
		return galaxyTextList;
	}
	private String selectGalaxyTextFromList() {
		String initialChoice = newGameOptions().selectedGalaxyShapeOption1();
		String message = "Make your choice, (This list can be edited in the file) " + GALAXY_TEXT_FILE;
	    String input = (String) ListDialog.showDialog(
	    	getParent(),	// Frame component
	    	getParent(),	// Location component
	    	message,		// Message
	        "Galaxy Text selection",	// Title
	        (String[]) getGalaxyTextList(),	// List
	        initialChoice, 				// Initial choice
	        null,	// long Dialogue
	        scaled(400), scaled(300),	// size
	        dialogMonoFont(),		// Font
	        this);	// for listener
	    if (input == null)
	    	return initialChoice;
	    newGameOptions().selectedGalaxyShapeOption1(input);
	    newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	    return input;
	}
	public void preview(String s) {
		if (s == null)
			return;
		if (this.isShapeTextGalaxy())
			newGameOptions().selectedGalaxyShapeOption1(s);
		else {
			shapeOption3.set(s);
		}
	    newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}	
	private String selectSpecificAbilityFromList(int i) {
		String initialChoice = newGameOptions().specificOpponentCROption(i);
	    String input = (String) ListDialog.showDialog(
	    	getParent(),				// Frame component
	    	getParent(),				// Location component
	    	"Select one abilities...",	// Message
	        "Opponent abilities",		// Title
	        (String[]) specificAbilitiesList,	// List
	        initialChoice, 				// Initial choice
	        "XX_RACE_JACKTRADES_XX",	// long Dialogue
	        scaled(400), scaled(300),	// size
	        null, null);	// Font
	    if (input == null)
	    	return initialChoice;
	    newGameOptions().specificOpponentCROption(input, i);
	    return input;
	}
	private String selectGlobalAbilityFromList() {
		String initialChoice = globalCROptions.get();
	    String input = (String) ListDialog.showDialog(
	    	getParent(),				// Frame component
	    	getParent(),				// Location component
	    	"Select one abilities...",	// Message
	        "Opponent abilities",		// Title
	        (String[]) globalAbilitiesList,	// List
	        initialChoice, 				// Initial choice
	        "XX_RACE_JACKTRADES_XX",	// long Dialogue
	        scaled(400), scaled(300),	// size
	        null, null);	// Font
	    if (input == null)
	    	return initialChoice;
	    globalCROptions.set(input);
	    return input;
	}
	@Override
	public void paintComponent(Graphics g0) {
		super.paintComponent(g0);
		Graphics2D g = (Graphics2D) g0;
		// modnar: use (slightly) better upsampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int w = getWidth();
		int h = getHeight();

		for (Rectangle rect: oppSet)
			rect.setBounds(0,0,0,0);
		for (Rectangle rect: oppAI)
			rect.setBounds(0,0,0,0);
		for (Rectangle rect: oppCR)
			rect.setBounds(0,0,0,0);
		// background image
		g.drawImage(backImg(), 0, 0, w, h, this);

		// draw number of opponents
		int maxOpp = newGameOptions().maximumOpponentsOptions();
		int numOpp = newGameOptions().selectedNumberOpponents();
		
		boolean smallImages = maxOpp > 25;
		BufferedImage mugBack = smallImages ? smallRaceBackImg() : SetupRaceUI.raceBackImg();
		int mugW = mugBack.getWidth();
		int mugH = mugBack.getHeight();

		g.setFont(narrowFont(30));
		g.setColor(Color.black);
		String oppStr =str(numOpp);
		int numSW = g.getFontMetrics().stringWidth(oppStr);
		int x0 = oppBox.x + ((oppBox.width-numSW)/2);
		int y0 = oppBox.y + oppBox.height -s5;
		drawString(g,oppStr, x0, y0);

		String randomOppLbl = text("SETUP_OPPONENT_RANDOM");
		int randSW = g.getFontMetrics().stringWidth(randomOppLbl);
		int numRows = smallImages ? 7 : 5;
		int numCols = smallImages ? 7 : 5;
		int fSize	= smallImages ? 12 : 15;
		int offset1	= smallImages ? s4 : s5;
		int offset2	= smallImages ? s12 : s15;
		int boundH	= smallImages ? s17 : s20;
		int spaceW = mugW+(((boxW-s60)-(numCols*mugW))/(numCols-1));
		int spaceH = smallImages ? mugH+s10 : mugH+s15;
		// draw opponent boxes
		Composite raceComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER , 0.5f);
		Composite prevComp = g.getComposite();
		Stroke prevStroke = g.getStroke();
		Color borderC = GameUI.setupFrame();
		boolean selectableAI = newGameOptions().selectableAI();
		boolean selectableCR = useSelectableAbilities.get();
		int maxDraw = min((numRows*numCols), numOpp, MAX_DISPLAY_OPPS);
		for (int i=0;i<maxDraw;i++) {
			int row = i/numCols;
			int col = i%numCols;
			// int y2 = y0+s50+(row*spaceH);
			int y2 = y0+s67+(row*spaceH); // BR: Adjusted for dataRace selection
			int x2 = leftBoxX+s30+(col*spaceW);
			oppSet[i].setBounds(x2,y2,mugW,mugH);
			// oppAI[i].setBounds(x2,y2+mugH-s20,mugW,s20);
			oppAI[i].setBounds(x2,y2+mugH+s1-boundH,mugW,boundH); // BR: Adjusted
			oppCR[i].setBounds(x2,y2-s1,mugW,boundH);
			g.drawImage(mugBack, x2, y2, this);
			String selOpp = newGameOptions().selectedOpponentRace(i);
			if (selOpp == null) {
				int x2b = x2+((mugW-randSW)/2);
				int y2b = smallImages ? y2+mugH-s20 : y2+mugH-s31;
				g.setColor(Color.black);
				g.setFont(narrowFont(30));
				drawString(g,randomOppLbl, x2b, y2b);
			}
			else {
				Race r = Race.keyed(selOpp);
				g.setComposite(raceComp);
				g.drawImage(r.diploMug(), x2, y2, mugW, mugH, this);
				g.setComposite(prevComp);
			}
			if (selectableAI) {
				g.setColor(SystemPanel.whiteText);
				String aiText = text(newGameOptions().specificOpponentAIOption(i+1));
				g.setFont(narrowFont(fSize)); // BR: Adjusted to fit the box
				int aiSW = g.getFontMetrics().stringWidth(aiText);
				int x2b = x2+(mugW-aiSW)/2;
				drawString(g,aiText, x2b, y2+mugH-offset1);
			}
			if (selectableCR) {
				g.setColor(SystemPanel.whiteText);
				String crText = text(newGameOptions().specificOpponentCROption(i+1));
				g.setFont(narrowFont(fSize));
				int crSW = g.getFontMetrics().stringWidth(crText);
				int x2b = x2+(mugW-crSW)/2;
				drawString(g,crText, x2b, y2+offset2);
			}
			g.setStroke(stroke1);
			g.setColor(borderC);
			g.drawRect(x2, y2, mugW, mugH);
			g.setStroke(prevStroke);

		}

		// draw galaxy
		drawGalaxyShape(g, newGameOptions().galaxyShape(), galaxyX, galaxyY, galaxyW, galaxyH);

		// draw info under galaxy map
		g.setColor(Color.black);
		g.setFont(narrowFont(16));
		int galaxyBoxW = boxW-s40;
		int y3 = galaxyY+galaxyH+s16;
		String systemsLbl = text("SETUP_GALAXY_NUMBER_SYSTEMS", newGameOptions().numberStarSystems());
		int sw3 = g.getFontMetrics().stringWidth(systemsLbl);
		int x3 = rightBoxX+s20+((galaxyBoxW/2)-sw3)/2;
		drawString(g,systemsLbl, x3,y3);

		String maxOppsLbl = text("SETUP_GALAXY_MAX_OPPONENTS", newGameOptions().maximumOpponentsOptions());
		int sw4 = g.getFontMetrics().stringWidth(maxOppsLbl);
		int x4 = rightBoxX+s20+(galaxyBoxW/2)+((galaxyBoxW/2)-sw4)/2;
		drawString(g,maxOppsLbl, x4,y3);

		// highlight any controls that are hovered
		if ((hoverBox == shapeBoxL) || (hoverBox == shapeBoxR)
			||  (hoverBox == sizeBoxL)  || (hoverBox == sizeBoxR)
			||  (hoverBox == diffBoxL)  || (hoverBox == diffBoxR)
			||  (hoverBox == aiBoxL)  || (hoverBox == aiBoxR)
			||  (hoverBox == crBoxL)  || (hoverBox == crBoxR)
			||  (hoverBox == mapOption1BoxL)  || (hoverBox == mapOption1BoxR)
			||  (hoverBox == mapOption2BoxL)  || (hoverBox == mapOption2BoxR)
			||  (hoverBox == sizeOptionBoxL)  || (hoverBox == sizeOptionBoxR)
			||  (hoverBox == oppBoxU)   || (hoverBox == oppBoxD)) {
			g.setColor(Color.yellow);
			g.fill(hoverBox);
		}
		else if ((hoverBox == shapeBox) || (hoverBox == sizeBox)
			|| (hoverBox == mapOption1Box) || (hoverBox == mapOption2Box)
			|| (hoverBox == sizeOptionBox) || (hoverBox == crBox)
			|| (hoverBox == aiBox) || (hoverBox == newRacesBox)
			|| (hoverBox == showAbilityBox) || (hoverBox == mapOption3Box)
			|| (hoverBox == diffBox) || (hoverBox == oppBox)) {
			Stroke prev = g.getStroke();
			g.setStroke(stroke2);
			g.setColor(Color.yellow);
			g.draw(hoverBox);
			g.setStroke(prev);
		}
		else {
			if (newGameOptions().selectableAI()) {
				for (int i=0;i<oppAI.length;i++) {
					if (hoverBox == oppAI[i]) {
						Stroke prev = g.getStroke();
						g.setStroke(stroke2);
						g.setColor(Color.yellow);
						g.draw(hoverBox);
						g.setStroke(prev);
						break;
					}
				}
			}
			if (useSelectableAbilities.get()) {
				for (int i=0;i<oppCR.length;i++) {
					if (hoverBox == oppCR[i]) {
						Stroke prev = g.getStroke();
						g.setStroke(stroke2);
						g.setColor(Color.yellow);
						g.draw(hoverBox);
						g.setStroke(prev);
						break;
					}
				}
			}
			for (int i=0;i<oppSet.length;i++) {
				if (hoverBox == oppSet[i]) {
					Stroke prev = g.getStroke();
					g.setStroke(stroke2);
					g.setColor(Color.yellow);
					g.draw(hoverBox);
					g.setStroke(prev);
					break;
				}
			}
		}
		
		// draw top opponents selections options
		g.setColor(Color.black);
		g.setFont(narrowFont(15));

		// draw Opponent CR text
		String crLbl = text(globalCROptions.get());
		int crSW = g.getFontMetrics().stringWidth(crLbl);
		int x4cr = crBox.x+((aiBox.width-crSW)/2);
		int y4cr = crBox.y+crBox.height-s3;
		drawString(g,crLbl, x4cr, y4cr);
		
		// draw Show Abilities Yes/No text
		String showAbilityLbl = showAbilityStr();
		int showAbilitySW = g.getFontMetrics().stringWidth(showAbilityLbl);
		int x4d = showAbilityBox.x+((showAbilityBox.width-showAbilitySW)/2);
		int y4d = showAbilityBox.y+showAbilityBox.height-s3;
		drawString(g, showAbilityLbl, x4d, y4d);

		// draw Opponent AI text
		String aiLbl = text(newGameOptions().selectedOpponentAIOption());
		int aiSW = g.getFontMetrics().stringWidth(aiLbl);
		int x4b = aiBox.x+((aiBox.width-aiSW)/2);
		int y4b = aiBox.y+aiBox.height-s3;
		drawString(g,aiLbl, x4b, y4b);

		// draw New Races ON/OFF text
		String newRacesLbl = newRacesOnStr();
		int newRacesSW = g.getFontMetrics().stringWidth(newRacesLbl);
		int x4c = newRacesBox.x+((newRacesBox.width-newRacesSW)/2);
		int y4c = newRacesBox.y+newRacesBox.height-s3;
		drawString(g, newRacesLbl, x4c, y4c);

		// draw galaxy options text
		int y5 = shapeBox.y+shapeBox.height-s4;
		String shapeLbl = text(newGameOptions().selectedGalaxyShape());
		int shapeSW = g.getFontMetrics().stringWidth(shapeLbl);
		int x5a =shapeBox.x+((shapeBox.width-shapeSW)/2);
		drawString(g,shapeLbl, x5a, y5);
		
		if (newGameOptions().numGalaxyShapeOption1() > 0) {
			if (isShapeTextGalaxy()) {
				String label1 = newGameOptions().selectedGalaxyShapeOption1();
				Font prevFont = g.getFont();
				g.setFont(boxMonoFont());
				int sw1 = g.getFontMetrics().stringWidth(label1);
				int x5d =mapOption1Box.x+((mapOption1Box.width-sw1)/2);
				drawString(g,label1, x5d, y5+s20);
				g.setFont(prevFont);
			}
			else {
				String label1 = text(newGameOptions().selectedGalaxyShapeOption1());
				int sw1 = g.getFontMetrics().stringWidth(label1);
				int x5d =mapOption1Box.x+((mapOption1Box.width-sw1)/2);
				drawString(g,label1, x5d, y5+s20);
			}
			if (newGameOptions().numGalaxyShapeOption2() > 0) {
				if (isShapeBitmapGalaxy()) {
					String label3 = getNameFromPath(shapeOption3.get());
					if (label3.equals(shapeOption3.defaultValue()))
						label3 = text(NO_SELECTION);
					int sw2 = g.getFontMetrics().stringWidth(label3);
					int x5e =mapOption3Box.x+((mapOption3Box.width-sw2)/2);
					drawString(g,label3, x5e, y5+s40);
				}
				String label2 = text(newGameOptions().selectedGalaxyShapeOption2());
				int sw2 = g.getFontMetrics().stringWidth(label2);
				int x5e =mapOption2Box.x+((mapOption2Box.width-sw2)/2);
				drawString(g,label2, x5e, y5+s40);	
			}		 
		}
		
		String sizeLbl = text(newGameOptions().selectedGalaxySize());
		int sizeSW = g.getFontMetrics().stringWidth(sizeLbl);
		int x5b =sizeBox.x+((sizeBox.width-sizeSW)/2);
		drawString(g,sizeLbl, x5b, y5);

		if (isDynamic()) { // BR:
			String label = text(SIZE_OPT_KEY, prefStarsPerEmpire.getGuiValue());
			int sw2 = g.getFontMetrics().stringWidth(label);
			int x5b1 =sizeOptionBox.x+((sizeOptionBox.width-sw2)/2);
			drawString(g,label, x5b1, y5+s20);		   
		}
		
		String diffLbl = text(newGameOptions().selectedGameDifficulty());
		// modnar: add custom difficulty level option, set in Remnants.cfg
		// append this custom difficulty percentage to diffLbl if selected
		if (diffLbl.equals("Custom")) {
			diffLbl = diffLbl + " (" + Integer.toString(UserPreferences.customDifficulty.get()) + "%)";
		} else {
			diffLbl = diffLbl + " (" + Integer.toString(Math.round(100 * newGameOptions().aiProductionModifier())) + "%)";
		}
		
		int diffSW = g.getFontMetrics().stringWidth(diffLbl);
		int x5c =diffBox.x+((diffBox.width-diffSW)/2);
		drawString(g,diffLbl, x5c, y5);
		
		// draw autoplay warning
		if (newGameOptions().isAutoPlay()) {
			g.setFont(narrowFont(16));
			String warning = text("SETTINGS_AUTOPLAY_WARNING");
			List<String> warnLines = this.wrappedLines(g, warning, galaxyW);
			g.setColor(Color.white);
			int warnY = y5+s60;
			for (String line: warnLines) {
				drawString(g,line, galaxyX, warnY);
				warnY += s18;
			}
		}

		drawButtons(g);

		if (starting) {
			NoticeMessage.setStatus(text("SETUP_CREATING_GALAXY"));
			drawNotice(g, 30);
		}
	}
	@Override protected void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawBackButtons(g);		
		drawButtons(g);
		g.dispose();
	}
	private void drawBackButtons(Graphics2D g) {
		int cnr = s5;

		// draw settings button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(settingsBox.x, settingsBox.y, settingsBox.width, settingsBox.height, cnr, cnr);
		
		// draw MOD settings button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(modASettingsBox.x, modASettingsBox.y,
				modASettingsBox.width, modASettingsBox.height, cnr, cnr);

		// draw MOD settings button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(modBSettingsBox.x, modBSettingsBox.y,
				modBSettingsBox.width, modBSettingsBox.height, cnr, cnr);

		// draw MOD settings button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(globalModSettingsBox.x, globalModSettingsBox.y, 
				globalModSettingsBox.width, globalModSettingsBox.height, cnr, cnr);

		// draw START button
		g.setPaint(GameUI.buttonRightBackground());
		g.fillRoundRect(startBox.x, startBox.y, startBox.width, startBox.height, cnr, cnr);

		// draw BACK button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(backBox.x, backBox.y, backBox.width, backBox.height, cnr, cnr);

		// draw DEFAULT button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);

		// draw LAST button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);

		// draw USER button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);

	}
	private void drawButtons(Graphics2D g) {
		// settings button
		int cnr = s5;
		g.setFont(narrowFont(20)); // 18 for 3 buttons
		String text6 = text("SETUP_BUTTON_SETTINGS");
		int sw6 = g.getFontMetrics().stringWidth(text6);
		int x6 = settingsBox.x+((settingsBox.width-sw6)/2);
		int y6 = settingsBox.y+settingsBox.height-s8;
		Color c6 = hoverBox == settingsBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text6, 2, x6, y6, GameUI.borderDarkColor(), c6);
		Stroke prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(settingsBox.x, settingsBox.y, settingsBox.width, settingsBox.height, cnr, cnr);
		g.setStroke(prev);
		
		// modnar: add UI panel for modnar MOD game options
		// MOD settings button
		String textMOD = text("SETUP_BUTTON_MOD_SETTINGS");
		int swMOD = g.getFontMetrics().stringWidth(textMOD);
		int xMOD = modASettingsBox.x+((modASettingsBox.width-swMOD)/2);
		int yMOD = modASettingsBox.y+modASettingsBox.height-s8;
		Color cMOD = hoverBox == modASettingsBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(modASettingsBox.x, modASettingsBox.y, modASettingsBox.width, modASettingsBox.height, cnr, cnr);
		g.setStroke(prev);

		// BR: second UI panel for MOD game options
		// MOD settings button
		String textModB = text("SETUP_BUTTON_MOD_B_SETTINGS");
		int swModB = g.getFontMetrics().stringWidth(textModB);
		int xModB = modBSettingsBox.x+((modBSettingsBox.width-swModB)/2);
		int yModB = modBSettingsBox.y+modBSettingsBox.height-s8;
		Color cModB = hoverBox == modBSettingsBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, textModB, 2, xModB, yModB, GameUI.borderDarkColor(), cModB);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(modBSettingsBox.x, modBSettingsBox.y, modBSettingsBox.width, modBSettingsBox.height, cnr, cnr);
		g.setStroke(prev);
		// BR: Display settings UI panel for MOD game options
		// MOD settings button
		String textModView = text("SETUP_BUTTON_MOD_GLOBAL_SETTINGS");
		int swModView = g.getFontMetrics().stringWidth(textModView);
		int xModView = globalModSettingsBox.x+((globalModSettingsBox.width-swModView)/2);
		int yModView = globalModSettingsBox.y+globalModSettingsBox.height-s8;
		Color cModView = hoverBox == globalModSettingsBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, textModView, 2, xModView, yModView, GameUI.borderDarkColor(), cModView);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(globalModSettingsBox.x, globalModSettingsBox.y,
				globalModSettingsBox.width, globalModSettingsBox.height, cnr, cnr);
		g.setStroke(prev);

		g.setFont(narrowFont(30));
		// left button
		String text = text(backButtonKey());
		int sw = g.getFontMetrics().stringWidth(text);
		int x = backBox.x+((backBox.width-sw)/2);
		int y = backBox.y+backBox.height-s12;
		Color c = hoverBox == backBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(backBox.x, backBox.y, backBox.width, backBox.height, cnr, cnr);
		g.setStroke(prev);

		// middle button
		text = text(startButtonKey());
		sw= g.getFontMetrics().stringWidth(text);
		x = startBox.x+((startBox.width-sw)/2);
		y = startBox.y+startBox.height-s12;
		c = hoverBox == startBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(startBox.x, startBox.y, startBox.width, startBox.height, cnr, cnr);
		g.setStroke(prev);

        g.setFont(narrowFont(20));
        // BR: Default Button 
		text = text(defaultButtonKey());
        sw	 = g.getFontMetrics().stringWidth(text);
        x = defaultBox.x+((defaultBox.width-sw)/2);
        y = defaultBox.y+defaultBox.height-s8;
        c = hoverBox == defaultBox ? Color.yellow : GameUI.borderBrightColor();
        drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
        prev = g.getStroke();
        g.setStroke(stroke1);
        g.drawRoundRect(defaultBox.x, defaultBox.y, defaultBox.width, defaultBox.height, cnr, cnr);
        g.setStroke(prev);

        // BR: Last Button 
		text = text(lastButtonKey());
        sw  = g.getFontMetrics().stringWidth(text);
		x = lastBox.x+((lastBox.width-sw)/2);
		y = lastBox.y+lastBox.height-s8;
		c = hoverBox == lastBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(lastBox.x, lastBox.y, lastBox.width, lastBox.height, cnr, cnr);
		g.setStroke(prev);
 
		// BR: User Button 
		text = text(userButtonKey());
        sw 	 = g.getFontMetrics().stringWidth(text);
		x = userBox.x+((userBox.width-sw)/2);
		y = userBox.y+userBox.height-s8;
		c = hoverBox == userBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(userBox.x, userBox.y, userBox.width, userBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private String newRacesOnStr() {
		if (showNewRaces.get()) return text("SETUP_NEW_RACES_ON");
		else return text("SETUP_NEW_RACES_OFF");
	}
	private String showAbilityStr() {
		return useSelectableAbilities.getGuiValue();
	}
	private boolean isDynamic() {
		return newGameOptions().selectedGalaxySize().equals(IGameOptions.SIZE_DYNAMIC);
	}
	private void drawGalaxyShape(Graphics g, GalaxyShape sh, int x, int y, int w, int h) {
		float factor = min((float)h/sh.height(), (float)w/sh.width());
		int dispH = (int) (sh.height()*factor);
		int dispW = (int) (sh.width()*factor);
		int xOff = x+(w-dispW)/2;
		int yOff = y+(h-dispH)/2;
		int starSize    = s2;
		int worldsSize  = 0;
		int nearSize    = 0;
		int compSize    = 0;
		int starShift   = s1;
		int worldsShift = 0;
		int nearShift   = 0;
		int compShift   = 0;
		boolean colored = galaxyPreviewColorStarsSize.get() != 0;
		if (colored) {
			xOff += starShift;
			yOff += starShift;
			worldsSize  = scaled(galaxyPreviewColorStarsSize.get());
			nearSize    = worldsSize * 3/4;
			compSize    = worldsSize/2;
			worldsShift = worldsSize/2;
			nearShift   = nearSize/2;
			compShift   = compSize/2;
		}

		// Start with lone stars
		Point.Float pt = new Point.Float();
		for (int i=0; i<sh.numberStarSystems();i++) {
			sh.coords(i, pt);
			int x0 = xOff + (int) (pt.x*factor);
			int y0 = yOff + (int) (pt.y*factor);
			g.setColor(starColor(i));
			if (colored)
				g.fillRoundRect(x0-starShift, y0-starShift, starSize, starSize, starSize, starSize);
			else
				g.fillRect(x0, y0, starSize, starSize);
		}
		// Add Orion over the other stars
		if (colored) {
			g.setColor(new Color(64, 64, 255)); // Start with Orion
			sh.coords(0, pt);
			int x0 = xOff + (int) (pt.x*factor);
			int y0 = yOff + (int) (pt.y*factor);
			g.fillRoundRect(x0-worldsShift, y0-worldsShift, worldsSize, worldsSize, worldsSize, worldsSize);
		} 

		// BR: add empires stars to avoid lonely Orion star
		int numCompWorlds = sh.numCompanionWorld();
		int iColor = 0;
		int iEmp   = 0;
		if (colored)
			g.setColor(Color.green); // Start with Player
		for (EmpireSystem emp : sh.empireSystems()) {
			// Home worlds
			int x0 = xOff + (int) (emp.x(0)*factor);
			int y0 = yOff + (int) (emp.y(0)*factor);
			if (colored)
				g.fillRoundRect(x0-worldsShift, y0-worldsShift, worldsSize, worldsSize, worldsSize, worldsSize);
			else {
				g.setColor(starColor(iColor));
				iColor++;					
				g.fillRect(x0, y0, starSize, starSize);
			}
			// Near Stars
			for (int iSys=1; iSys<emp.numSystems();iSys++) {
				x0 = xOff + (int) (emp.x(iSys)*factor);
				y0 = yOff + (int) (emp.y(iSys)*factor);
				if (colored)
					g.fillRoundRect(x0-nearShift, y0-nearShift, nearSize, nearSize, nearSize, nearSize);
				else {
					g.setColor(starColor(iColor));
					iColor++;					
					g.fillRect(x0, y0, starSize, starSize);
				}
			}
			// Companions Worlds
			if (numCompWorlds > 0) {
				for (int iCW=0; iCW<numCompWorlds; iCW++) {
					pt = sh.getCompanion(iEmp, iCW);
					x0 = xOff + (int) (pt.x*factor);
					y0 = yOff + (int) (pt.y*factor);
					if (colored)
						g.fillRoundRect(x0-compShift, y0-compShift, compSize, compSize, compSize, compSize);
					else {
						g.setColor(starColor(iColor));
						iColor++;					
						g.fillRect(x0, y0, starSize, starSize);
					}
				}
			}
			if (colored)
				g.setColor(Color.red); // Start with Player, continue with aliens
			iEmp++;
		}
	}
	private Color starColor(int i) {
		switch(i % 4) {
			case 0:
			case 1:
					return Color.lightGray;
			case 2: return Color.gray;
			case 3: return Color.white;
		}
		return Color.gray;
	}
	private BufferedImage playerRaceImg() {
		if (playerRaceImg == null) {
			String selRace = newGameOptions().selectedPlayerRace();
			playerRaceImg = newBufferedImage(Race.keyed(selRace).diploMug());
		}
		return playerRaceImg;
	}
	private void nextGalaxySize(boolean bounded, boolean click) {
		String nextSize = newGameOptions().nextGalaxySize(bounded);
		if (nextSize.equals(newGameOptions().selectedGalaxySize()))
			return;
		if (click) softClick();
		newGameOptions().selectedGalaxySize(newGameOptions().nextGalaxySize(bounded));
		newGameOptions().galaxyShape().quickGenerate(); // modnar: do a quickgen to get correct map preview
		backImg = null; // BR: to show/hide system per empire
		repaint();
	}
	private void prevGalaxySize(boolean bounded, boolean click) {
		String prevSize = newGameOptions().prevGalaxySize(bounded);
		if (prevSize.equals(newGameOptions().selectedGalaxySize()))
			return;
		if (click) softClick();
		newGameOptions().selectedGalaxySize(newGameOptions().prevGalaxySize(bounded));
		int numOpps = newGameOptions().selectedNumberOpponents();
		if(numOpps<0) {
			newGameOptions().selectedNumberOpponents(0);
			numOpps = 0;
		}
		int maxOpps = newGameOptions().maximumOpponentsOptions();
		if (maxOpps < numOpps) {
			for (int i=maxOpps;i<numOpps;i++)
				newGameOptions().selectedOpponentRace(i,null);
			newGameOptions().selectedNumberOpponents(maxOpps);
		}
		newGameOptions().galaxyShape().quickGenerate(); // modnar: do a quickgen to get correct map preview
		backImg = null; // BR: to show/hide system per empire
		repaint();
	}
	private void nextGalaxyShape(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGalaxyShape(newGameOptions().nextGalaxyShape());
		newGameOptions().galaxyShape().quickGenerate(); 
		backImg = null;
		repaint();
	}
	private void prevGalaxyShape(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGalaxyShape(newGameOptions().prevGalaxyShape());
		newGameOptions().galaxyShape().quickGenerate(); 
		backImg = null;
		repaint();
	}
	private boolean isShapeTextGalaxy() {
		return newGameOptions().selectedGalaxyShape().equals(IGameOptions.SHAPE_TEXT);
	}
	private boolean isShapeBitmapGalaxy() {
		return newGameOptions().selectedGalaxyShape().equals(IGameOptions.SHAPE_BITMAP);
	}
	private String getNameFromPath(String path) {
		File file = new File(path);
		if (file.exists())
			return file.getName();
		return path;
	}
	private void nextMapOption1(boolean click) {
		if (click) softClick();
		if (isShapeTextGalaxy()) {
			String currText = newGameOptions().selectedGalaxyShapeOption1();
			int nextIndex = 0;
			if (currText != null)
				nextIndex = currentGalaxyTextIndex(currText)+1;
			if (nextIndex >= getGalaxyTextList().length)
				nextIndex = 0;
			String nextText = (String) getGalaxyTextList()[nextIndex];
			newGameOptions().selectedGalaxyShapeOption1(nextText);
		} else
			newGameOptions().selectedGalaxyShapeOption1(newGameOptions().nextGalaxyShapeOption1());
		newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}
	private void prevMapOption1(boolean click) {
		if (click) softClick();
		if (isShapeTextGalaxy()) {
			String currText = newGameOptions().selectedGalaxyShapeOption1();
			int prevIndex = 0;
			if (currText != null)
				prevIndex = currentGalaxyTextIndex(currText)-1;
			if (prevIndex < 0)
				prevIndex = getGalaxyTextList().length-1;
			String prevText = (String) getGalaxyTextList()[prevIndex];
			newGameOptions().selectedGalaxyShapeOption1(prevText);
		} else
			newGameOptions().selectedGalaxyShapeOption1(newGameOptions().prevGalaxyShapeOption1());
		newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}
	private void nextMapOption2(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGalaxyShapeOption2(newGameOptions().nextGalaxyShapeOption2());
		newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}
	private void prevMapOption2(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGalaxyShapeOption2(newGameOptions().prevGalaxyShapeOption2());
		newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}
	private void nextGameDifficulty(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGameDifficulty(newGameOptions().nextGameDifficulty());
		repaint();
	}
	private void prevGameDifficulty(boolean click) {
		if (click) softClick();
		newGameOptions().selectedGameDifficulty(newGameOptions().prevGameDifficulty());
		repaint();
	}
	private void nextOpponentAI(boolean click) {
		if (click) softClick();
		newGameOptions().selectedOpponentAIOption(newGameOptions().nextOpponentAI());
		repaint();
	}
	private void prevOpponentAI(boolean click) {
		if (click) softClick();
		newGameOptions().selectedOpponentAIOption(newGameOptions().prevOpponentAI());
		repaint();
	}
	private void nextOpponentCR(boolean click) {
		if (click) softClick();
		if (click || Modifier2KeysState.isCtrlDown())
			selectGlobalAbilityFromList();
		else {
			String currCR = globalCROptions.get();
			int nextIndex = 0;
			if (currCR != null)
				nextIndex = currentGlobalAbilityIndex(currCR)+1;
			if (nextIndex >= globalAbilitiesList.length)
				nextIndex = 0;
			String nextCR = (String) globalAbilitiesList[nextIndex];
			globalCROptions.set(nextCR);
		}
		repaint();
	}
	private void prevOpponentCR(boolean click) {
		if (click) softClick();
		if (click || Modifier2KeysState.isCtrlDown())
			selectGlobalAbilityFromList();
		else {
			String currCR = globalCROptions.get();
			int prevIndex = 0;
			if (currCR != null)
				prevIndex = currentGlobalAbilityIndex(currCR)-1;
			if (prevIndex < 0)
				prevIndex = globalAbilitiesList.length-1;
			String prevCR = (String) globalAbilitiesList[prevIndex];
			globalCROptions.set(prevCR);
		}
		repaint();
	}
	private void toggleNewRaces(boolean click) {
		if (click) softClick();
		showNewRaces.toggle();
		repaint();
	}
	private void toggleShowAbility(boolean click) {
		if (click) softClick();
		if (click && Modifier2KeysState.isCtrlDown()) {
			String defVal = SpecificCROption.defaultSpecificValue().value;
            for (int i=0;i<oppCR.length;i++)
            	newGameOptions().specificOpponentCROption(defVal,i+1);
		}
		else
			useSelectableAbilities.toggle();
		repaint();
	}
	private void increaseOpponents(boolean click) {
		int numOpps = newGameOptions().selectedNumberOpponents();
		if (numOpps >= newGameOptions().maximumOpponentsOptions())
			return;
		if (click) softClick();
		newGameOptions().selectedNumberOpponents(numOpps+1);
		newGameOptions().galaxyShape().quickGenerate(); // modnar: do a quickgen to get correct map preview
		repaint();
	}
	private void decreaseOpponents(boolean click) {
		int numOpps = newGameOptions().selectedNumberOpponents();
		if (numOpps <= 0)
			return;
		if (click) softClick();
		newGameOptions().selectedOpponentRace(numOpps-1,null);
		newGameOptions().selectedNumberOpponents(numOpps-1);
		newGameOptions().galaxyShape().quickGenerate(); // modnar: do a quickgen to get correct map preview
		repaint();
	}
	private void nextSpecificOpponentAI(int i, boolean click) {
		if (click) softClick();
		newGameOptions().nextSpecificOpponentAI(i+1);
		repaint();
	}
	private void prevSpecificOpponentAI(int i, boolean click) {
		if (click) softClick();
		newGameOptions().prevSpecificOpponentAI(i+1);
		repaint();
	}
	private void nextSpecificOpponentCR(int i, boolean click) {
		if (click) softClick();
		if (click || Modifier2KeysState.isCtrlDown())
			selectSpecificAbilityFromList(i+1);
		else {
			String currCR = newGameOptions().specificOpponentCROption(i+1);
			int nextIndex = 0;
			if (currCR != null)
				nextIndex = currentSpecificAbilityIndex(currCR)+1;
			if (nextIndex >= specificAbilitiesList.length)
				nextIndex = 0;
			String nextCR = (String) specificAbilitiesList[nextIndex];
			newGameOptions().specificOpponentCROption(nextCR, i+1);
		}
		repaint();
	}
	private void prevSpecificOpponentCR(int i, boolean click) {
		if (click) softClick();
		if (click || Modifier2KeysState.isCtrlDown())
			selectSpecificAbilityFromList(i+1);
		else {
			String currCR = newGameOptions().specificOpponentCROption(i+1);
			int prevIndex = 0;
			if (currCR != null)
				prevIndex = currentSpecificAbilityIndex(currCR)-1;
	        if (prevIndex < 0)
	        	prevIndex = specificAbilitiesList.length-1;
	        String prevCR = (String) specificAbilitiesList[prevIndex];
	        newGameOptions().specificOpponentCROption(prevCR, i+1);
		}
		repaint();
	}
	private void nextOpponent(int i, boolean click) {
		if (click) softClick();
		newGameOptions().nextOpponent(i);
		repaint();
	}
	private void prevOpponent(int i, boolean click) {
		if (click) softClick();
		newGameOptions().prevOpponent(i);
		repaint();
	}
	private void goToOptions() {
		buttonClick();
		StartOptionsUI optionsUI = RotPUI.startOptionsUI();
		optionsUI.open(this);
		release();
	}
	// modnar: add UI panel for modnar MOD game options
	private void goToModOptions() {
		buttonClick();
		StartModAOptionsUI modOptionsUI = RotPUI.startModAOptionsUI();
		modOptionsUI.open(this);
		release();
	}
	// BR: Second UI panel for MOD game options
	private void goToMod2Options() {
		buttonClick();
		StartModBOptionsUI modBOptionsUI = RotPUI.startModBOptionsUI();
		modBOptionsUI.open(this);
		release();
	}
	// BR: Display UI panel for MOD game options
	private void goToModViewOptions() {
		buttonClick();
		ModGlobalOptionsUI modGlobalOptionsUI = RotPUI.modGlobalOptionsUI();
		modGlobalOptionsUI.open(this);
		release();
	}
	// BR: Add option to return to the main menu
	private void goToMainMenu() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);	
			break;
		default:
			updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break;
		}
		RotPUI.instance().selectGamePanel();
		release();
	}
	// BR: For restarting with new options
	private void restartGame() { 
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		UserPreferences.gamePlayed(true);		
		starting = true;
		buttonClick();
		repaint();
		GalaxyCopy oldGalaxy = new GalaxyCopy(newGameOptions());
		UserPreferences.setForNewGame();
		// Get the old galaxy parameters
        RotPUI.instance().selectRestartGamePanel(oldGalaxy);
		starting = false;
		release();
	}
	private void startGame() {
		updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
		UserPreferences.gamePlayed(true);
		starting = true;
		repaint();
		buttonClick();
		// BR:
		if (Profiles.isStartOpponentRaceListEnabled()) {
			RacesOptions.loadStartingOpponents(newGameOptions());
		}
		if (Profiles.isStartOpponentAIListEnabled()) {
			RacesOptions.loadStartingAIs(newGameOptions());
		}
		GameUI.gameName = generateGameName();
		// \BR:
		UserPreferences.setForNewGame();
		final Runnable save = () -> {
			long start = System.currentTimeMillis();
			GameSession.instance().startGame(newGameOptions());
			RotPUI.instance().mainUI().checkMapInitialized();
			RotPUI.instance().selectIntroPanel();
			log("TOTAL GAME START TIME:" +(System.currentTimeMillis()-start));
			log("Game Name; "+GameUI.gameName);
			starting = false;
			release();
		};
		SwingUtilities.invokeLater(save);
	}
	private BufferedImage backImg() {
		if (backImg == null)
			initBackImg();
		return backImg;
	}
	private void initBackImg() {
		int w = getWidth();
		int h = getHeight();
		backImg = newOpaqueImage(w, h);
		Graphics2D g = (Graphics2D) backImg.getGraphics();
		setFontHints(g);
		// modnar: use (slightly) better upsampling
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		Race race = Race.keyed(newGameOptions().selectedPlayerRace());

		// background image
		Image back = GameUI.defaultBackground;
		int imgW = back.getWidth(null);
		int imgH = back.getHeight(null);
		g.drawImage(back, 0, 0, w, h, 0, 0, imgW, imgH, this);

		// shade Box Dimensions
		leftBoxX = s80;
		rightBoxX = scaled(665);
		boxW = scaled(505);
		boxY = s95;
		leftBoxH = scaled(615);
		rightBoxH = scaled(605); // BR: was 575
		// draw opponents title
		String title1 = text("SETUP_SELECT_OPPONENTS");
		g.setFont(narrowFont(50));
		int sw1 = g.getFontMetrics().stringWidth(title1);
		int x1 = leftBoxX+((boxW-sw1)/2);
		int y0 = s80;
		drawBorderedString(g, title1, 2, x1, y0, Color.darkGray, Color.white);

		// draw galaxy title
		String title2 = text("SETUP_SELECT_GALAXY");
		g.setFont(narrowFont(50));
		int sw1b = g.getFontMetrics().stringWidth(title2);
		int x1b = rightBoxX+((boxW-sw1b)/2);
		drawBorderedString(g, title2, 2, x1b, y0, Color.darkGray, Color.white);

		// draw opponents shading
		g.setColor(GameUI.setupShade());
		g.fillRect(leftBoxX, boxY, boxW, leftBoxH);

		// draw opponents back gradient
		g.setPaint(GameUI.opponentsSetupBackground());
		g.fillRect(leftBoxX+s20, boxY+s20, boxW-s40, s92);

		// draw race box for player
		BufferedImage backimg = SetupRaceUI.raceBackImg();
		int mugW = backimg.getWidth();
		int mugH = backimg.getHeight();
		g.drawImage(backimg, leftBoxX+s25, boxY+s25, this);
		g.drawImage(playerRaceImg(), leftBoxX+s25, boxY+s25, mugW, mugH, this);

		// draw player vs opponent text
		int x2 = leftBoxX+s25+mugW+s15;
		// int y2 = boxY+s25+mugH-s42;
		int y2 = boxY+s25+mugH-s52; // BR: up a little
		int yho = s5; // BR: a little space for new races on/off 
		g.setFont(narrowFont(28));
		String header1 = text("SETUP_OPPONENTS_HEADER_1", race.setupName());
		String header2 = text("SETUP_OPPONENTS_HEADER_2", race.setupName());
		int swHdr = g.getFontMetrics().stringWidth(header1);
		drawBorderedString(g, header1, 1, x2, y2-yho, Color.black, Color.white);

		// draw opponent count box and arrows
		int x2b = x2+swHdr+s5;
		g.setColor(GameUI.setupFrame());
		oppBox.setBounds(x2b,y2-s30,s30,s35);
		g.fill(oppBox);
		int x2c = x2b+s33;
		int y2c = (int)(oppBox.getY()+(oppBox.getHeight()/2));
		oppBoxD.reset();
		oppBoxD.addPoint(x2c,y2c+s2);
		oppBoxD.addPoint(x2c+s13,y2c+s2);
		oppBoxD.addPoint(x2c+s7,y2c+s17);
		g.fill(oppBoxD);
		oppBoxU.reset();
		oppBoxU.addPoint(x2c,y2c-s1);
		oppBoxU.addPoint(x2c+s13,y2c-s1);
		oppBoxU.addPoint(x2c+s7,y2c-s16);
		g.fill(oppBoxU);

		int x2d = x2c+s20;
		drawBorderedString(g, header2, 1, x2d, y2-yho, Color.black, Color.white);
		
		// align AI and CR selection
		String header3   = text("SETUP_OPPONENT_AI");
		String header3cr = text("SETUP_OPPONENT_CR");
		g.setFont(narrowFont(16));
		int swHdr3   = g.getFontMetrics().stringWidth(header3);
		int swHdr3cr = g.getFontMetrics().stringWidth(header3cr);
		swHdr3 = max(swHdr3, swHdr3cr);

		// draw AI selection
		g.setColor(SystemPanel.blackText);
		// int x3 = x2+s20;
		// int y3 = y2+s32;
		int y3 = y2+s47; // BR: up a little
		int x3 = x2;
		drawString(g,header3, x3, y3);
		// int sliderW = s100+s20;
		int sliderW = s100;
		int sliderH = s16;
		int sliderYAI = y3-sliderH+s3;
		int sliderX = x3+swHdr3+s20;
		g.setColor(GameUI.setupFrame());

		aiBoxL.reset();
		aiBoxL.addPoint(sliderX-s4,sliderYAI+s1);
		aiBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2);
		aiBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2));
		g.fill(aiBoxL);
		aiBoxR.reset();
		aiBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1);
		aiBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2);
		aiBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2));
		g.fill(aiBoxR);
		aiBox.setBounds(sliderX, sliderYAI, sliderW, sliderH);
		g.fill(aiBox);

		// draw CR selection
		g.setColor(SystemPanel.blackText);
		int y3cr = y2+s27;
		int x3cr = x2;
		drawString(g,header3cr, x3cr, y3cr);

		int sliderYCR = y3cr-sliderH+s3;
		g.setColor(GameUI.setupFrame());

		crBoxL.reset();
		crBoxL.addPoint(sliderX-s4,sliderYCR+s1);
		crBoxL.addPoint(sliderX-s4,sliderYCR+sliderH-s2);
		crBoxL.addPoint(sliderX-s13,sliderYCR+(sliderH/2));
		g.fill(crBoxL);
		crBoxR.reset();
		crBoxR.addPoint(sliderX+sliderW+s4,sliderYCR+s1);
		crBoxR.addPoint(sliderX+sliderW+s4,sliderYCR+sliderH-s2);
		crBoxR.addPoint(sliderX+sliderW+s13,sliderYCR+(sliderH/2));
		g.fill(crBoxR);
		crBox.setBounds(sliderX, sliderYCR, sliderW, sliderH);
		g.fill(crBox);

		// Align "New Races selection" and "Show Selectable Abilities"
		int margin	= s40;
		int sep		= s10;
		int side	= leftBoxX + boxW - margin;
		int widthNR = s60;
		int widthSA = s40;
		String headerNR = text("SETUP_NEW_RACES_HEADER");
		String headerSA = text("SETUP_SHOW_ABILITIES_HEADER");
		g.setFont(narrowFont(16));
		int swHdrNR = g.getFontMetrics().stringWidth(headerNR);
		int swHdrSA = g.getFontMetrics().stringWidth(headerSA);
		int x4 = side-sep - max(widthNR+swHdrNR, widthSA+swHdrSA);
		
		// draw New Races selection
		g.setColor(SystemPanel.blackText);
		int yNR = y3;
		drawString(g, headerNR, x4, yNR);
		g.setColor(GameUI.setupFrame());
		int bxNR = side-widthNR;
		newRacesBox.setBounds(bxNR , sliderYAI, widthNR, sliderH);
		g.fill(newRacesBox);

		// draw Show Selectable Abilities
		g.setFont(narrowFont(16));
		g.setColor(SystemPanel.blackText);
		int ySA = y3cr;
		drawString(g, headerSA, x4, ySA);
		g.setColor(GameUI.setupFrame());
		int bxSA = side-widthSA;
		showAbilityBox.setBounds(bxSA , sliderYCR, widthSA, sliderH);
		g.fill(showAbilityBox);

		// draw galaxy shading
		g.setColor(GameUI.setupShade());
		g.fillRect(rightBoxX, boxY, boxW, rightBoxH);

		// draw galaxy background gradient
		g.setPaint(GameUI.galaxySetupBackground());
		g.fillRect(rightBoxX+s20, boxY+s20, boxW-s40, rightBoxH-s40);
		g.setColor(Color.black);
		galaxyX = rightBoxX+s40;
		galaxyY = boxY+s40;
		galaxyW = boxW-s80;
		galaxyH = scaled(325);
		g.fillRect(galaxyX, galaxyY, galaxyW, galaxyH);

		// draw 3 galaxy option labels
		int sectionW = (boxW-s40) / 3;
		int y5 = galaxyY+galaxyH+s45;
		g.setFont(narrowFont(24));
		String shapeLbl = text("SETUP_GALAXY_SHAPE_LABEL");
		int shapeSW = g.getFontMetrics().stringWidth(shapeLbl);
		int x5a = rightBoxX+s20+((sectionW-shapeSW)/2);
		drawBorderedString(g, shapeLbl, 1, x5a, y5, Color.black, Color.white);

		String sizeLbl = text("SETUP_GALAXY_SIZE_LABEL");
		int sizeSW = g.getFontMetrics().stringWidth(sizeLbl);
		int x5b = rightBoxX+s20+sectionW+((sectionW-sizeSW)/2);
		drawBorderedString(g, sizeLbl, 1, x5b, y5, Color.black, Color.white);

		String diffLbl = text("SETUP_GAME_DIFFICULTY_LABEL");
		int diffSW = g.getFontMetrics().stringWidth(diffLbl);
		int x5c = rightBoxX+s20+sectionW+sectionW+((sectionW-diffSW)/2);
		drawBorderedString(g, diffLbl, 1, x5c, y5, Color.black, Color.white);

		sliderW = sectionW*2/3;
		sliderH = s18;
		sliderYAI = y5+s10;
		sliderX = rightBoxX+s20+(sectionW/6);
		g.setColor(GameUI.setupFrame());

		shapeBoxL.reset();
		shapeBoxL.addPoint(sliderX-s4,sliderYAI+s1);
		shapeBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2);
		shapeBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2));
		g.fill(shapeBoxL);
		shapeBoxR.reset();
		shapeBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1);
		shapeBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2);
		shapeBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2));
		g.fill(shapeBoxR);
		shapeBox.setBounds(sliderX, sliderYAI, sliderW, sliderH);
		g.fill(shapeBox);
		
	mapOption1BoxL.reset();
		mapOption1BoxR.reset();
		mapOption1Box.setBounds(0,0,0,0);
		if (newGameOptions().numGalaxyShapeOption1() > 0) {
			mapOption1BoxL.addPoint(sliderX-s4,sliderYAI+s1+s20);
			mapOption1BoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2+s20);
			mapOption1BoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2)+s20);
			g.fill(mapOption1BoxL);
			mapOption1BoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1+s20);
			mapOption1BoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2+s20);
			mapOption1BoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2)+s20);
			g.fill(mapOption1BoxR);
			mapOption1Box.setBounds(sliderX, sliderYAI+s20, sliderW, sliderH);
			g.fill(mapOption1Box);
		}

	mapOption2BoxL.reset();
		mapOption2BoxR.reset();
		mapOption2Box.setBounds(0,0,0,0);
		if (newGameOptions().numGalaxyShapeOption2() > 0) {
			if (this.isShapeBitmapGalaxy()) {
				mapOption3Box.setBounds(sliderX+sectionW, sliderYAI+s40, sliderW+sectionW, sliderH);
				g.fill(mapOption3Box);
			}
			mapOption2BoxL.addPoint(sliderX-s4,sliderYAI+s1+s40);
			mapOption2BoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2+s40);
			mapOption2BoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2)+s40);
			g.fill(mapOption2BoxL);
			mapOption2BoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1+s40);
			mapOption2BoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2+s40);
			mapOption2BoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2)+s40);
			g.fill(mapOption2BoxR);
			mapOption2Box.setBounds(sliderX, sliderYAI+s40, sliderW, sliderH);
			g.fill(mapOption2Box);
		}

		sliderX += sectionW;
		sizeBoxL.reset();
		sizeBoxL.addPoint(sliderX-s4,sliderYAI+s1);
		sizeBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2);
		sizeBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2));
		g.fill(sizeBoxL);
		sizeBoxR.reset();
		sizeBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1);
		sizeBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2);
		sizeBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2));
		g.fill(sizeBoxR);
		sizeBox.setBounds(sliderX, sliderYAI, sliderW, sliderH);
		g.fill(sizeBox);

	sizeOptionBoxL.reset();
		sizeOptionBoxR.reset();
		sizeOptionBox.setBounds(0,0,0,0);
		if (isDynamic()) {
			sizeOptionBoxL.addPoint(sliderX-s4,sliderYAI+s1+s20);
			sizeOptionBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2+s20);
			sizeOptionBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2)+s20);
			g.fill(sizeOptionBoxL);
			sizeOptionBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1+s20);
			sizeOptionBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2+s20);
			sizeOptionBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2)+s20);
			g.fill(sizeOptionBoxR);
			sizeOptionBox.setBounds(sliderX, sliderYAI+s20, sliderW, sliderH);
			g.fill(sizeOptionBox);
		}

		sliderX += sectionW;
		diffBoxL.reset();
		diffBoxL.addPoint(sliderX-s4,sliderYAI+s1);
		diffBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2);
		diffBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2));
		g.fill(diffBoxL);
		diffBoxR.reset();
		diffBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1);
		diffBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2);
		diffBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2));
		g.fill(diffBoxR);
		diffBox.setBounds(sliderX, sliderYAI, sliderW, sliderH);
		g.fill(diffBox);

		int cnr = s5;

		// draw settings button
		int smallButtonH = s30; // 27 for 3 buttons
		int smallButtonW = scaled(180); // 150 for 3 buttons
		// int smallButton2W = scaled(135); // for the two smaller buttons when 3 buttons
		// BR: buttons positioning
		int yb = 610; // 615 for 3 buttons (1 row)
		int xb = 960; // 966 for 3 buttons // 960 for 2 buttons 1 row // 948 for centered
		int dx = 200; // 138 for 3 buttons // 200 for 2 buttons 1 row // 241 for centered
		int dy = 35; // 30 for 3 buttons
		settingsBox.setBounds(scaled(xb), scaled(yb), smallButtonW, smallButtonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(settingsBox.x, settingsBox.y, smallButtonW, smallButtonH, cnr, cnr);
		
		// modnar: add UI panel for modnar MOD game options // BR: Squeezed a little
		// draw MOD settings button
		modASettingsBox.setBounds(scaled(xb-dx), scaled(yb), smallButtonW, smallButtonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(modASettingsBox.x, modASettingsBox.y, smallButtonW, smallButtonH, cnr, cnr);
		// BR: second UI panel for MOD game options
		// draw MOD settings button
		modBSettingsBox.setBounds(scaled(xb-dx), scaled(yb+dy), smallButtonW, smallButtonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(modBSettingsBox.x, modBSettingsBox.y, smallButtonW, smallButtonH, cnr, cnr);
		// BR: Display Settings UI panel for MOD game options
		// draw MOD settings button
		globalModSettingsBox.setBounds(scaled(xb), scaled(yb+dy), smallButtonW, smallButtonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(globalModSettingsBox.x, globalModSettingsBox.y, smallButtonW, smallButtonH, cnr, cnr);

		int buttonH = s45;
		int buttonW = scaled(220);
		int yB = 685+10; // 2 Button's Rows Offset, was 685
		xb = 950; // was 950 // 1080 for 3 buttons
		dx = 241;
		// draw START button
		startBox.setBounds(scaled(xb), scaled(yB), buttonW, buttonH);
		g.setPaint(GameUI.buttonRightBackground());
		g.fillRoundRect(startBox.x, startBox.y, buttonW, buttonH, cnr, cnr);

		// draw BACK button
		xb -= dx;
		backBox.setBounds(scaled(xb), scaled(yB), buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(backBox.x, backBox.y, buttonW, buttonH, cnr, cnr);

		// draw DEFAULT button
		buttonH = s30;
		buttonW = defaultButtonWidth(g);
		yb = scaled(yB+15);
		xb = scaled(xb)-buttonW-bSep;
		defaultBox.setBounds(xb, yb, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(defaultBox.x, defaultBox.y, buttonW, buttonH, cnr, cnr);

		// draw LAST button
		buttonW = lastButtonWidth(g);
		xb -= (buttonW + bSep);
		lastBox.setBounds(xb, yb, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(lastBox.x, lastBox.y, buttonW, buttonH, cnr, cnr);

		// draw USER button
		buttonW = userButtonWidth(g);
		xb -= (buttonW + bSep);
		userBox.setBounds(xb, yb, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(userBox.x, userBox.y, buttonW, buttonH, cnr, cnr);

		g.dispose();
	}
	private BufferedImage smallRaceBackImg() {
		if (smBackImg == null)
			initSmallBackImg();
		return smBackImg;
	}
	private void initSmallBackImg() {
		int w = s54;
		int h = s58;
		smBackImg = gc().createCompatibleImage(w, h);

		Point2D center = new Point2D.Float(w/2, h/2);
		float radius = s56;
		float[] dist = {0.0f, 0.1f, 0.5f, 1.0f};
		Color[] colors = {GameUI.raceCenterColor(), GameUI.raceCenterColor(), GameUI.raceEdgeColor(), GameUI.raceEdgeColor()};
		RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
		Graphics2D g = (Graphics2D) smBackImg.getGraphics();
		g.setPaint(p);
		g.fillRect(0, 0, w, h);
		g.dispose();
	}
	@Override
	public String ambienceSoundKey() { 
		return GameUI.AMBIENCE_KEY;
	}
	@Override public void keyReleased(KeyEvent e) {
		checkModifierKey(e);		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		checkModifierKey(e);		
		int k = e.getKeyCode();
		switch(k) {
		case KeyEvent.VK_ESCAPE:
			doBackBoxAction();
			return;
		case KeyEvent.VK_ENTER:
			doStartBoxAction();
			return;
		case KeyEvent.VK_M: // BR: "M" = Go to Main Menu
			goToMainMenu();
			return;
		default: // BR:
			if (Profiles.processKey(k, e.isShiftDown(), "Galaxy", newGameOptions())) {
				buttonClick();
				playerRaceImg = null;
				playerRaceImg = playerRaceImg();
				backImg = null;
				repaint();
			}
			// Needs to be done twice for the case both Galaxy size
			// and the number of opponents were changed !?
			if (Profiles.processKey(k, e.isShiftDown(), "Galaxy", newGameOptions())) {
				playerRaceImg = null;
				playerRaceImg = playerRaceImg();
				backImg = null;
				repaint();
			}
			return;
		}
	}
	// BR:
	/**
	 * Load Profiles with option "Surprise" and start Game
	 */
	public void surpriseStart() {
		Profiles.processKey(KeyEvent.VK_R, true, "Galaxy", newGameOptions());
		buttonClick();
		repaint();
		Profiles.processKey(KeyEvent.VK_R, true, "Galaxy", newGameOptions());
		repaint();
		startGame();
	}
	@Override
	public void mouseDragged(MouseEvent e) {  }
	@Override
	public void mouseMoved(MouseEvent e) {
		checkModifierKey(e);		
		int x = e.getX();
		int y = e.getY();
		Shape prevHover = hoverBox;
		hoverBox = null;
		if (startBox.contains(x,y))
			hoverBox = startBox;
		else if (backBox.contains(x,y))
			hoverBox = backBox;
        else if (defaultBox.contains(x,y))
            hoverBox = defaultBox;
        else if (userBox.contains(x,y))
            hoverBox = userBox;
        else if (lastBox.contains(x,y))
            hoverBox = lastBox;
		else if (settingsBox.contains(x,y))
			hoverBox = settingsBox;
		// modnar: add UI panel for modnar MOD game options
		else if (modASettingsBox.contains(x,y))
			hoverBox = modASettingsBox;
		// BR: Second UI panel for MOD game options
		else if (modBSettingsBox.contains(x,y))
			hoverBox = modBSettingsBox;
		// BR: Display UI panel for MOD game options
		else if (globalModSettingsBox.contains(x,y))
			hoverBox = globalModSettingsBox;
		else if (shapeBoxL.contains(x,y))
			hoverBox = shapeBoxL;
		else if (shapeBoxR.contains(x,y))
			hoverBox = shapeBoxR;
		else if (shapeBox.contains(x,y))
			hoverBox = shapeBox;
		else if (mapOption1BoxL.contains(x,y))
			hoverBox = mapOption1BoxL;
		else if (mapOption1BoxR.contains(x,y))
			hoverBox = mapOption1BoxR;
		else if (mapOption1Box.contains(x,y))
			hoverBox = mapOption1Box;		
		else if (mapOption2BoxL.contains(x,y))
			hoverBox = mapOption2BoxL;
		else if (mapOption2BoxR.contains(x,y))
			hoverBox = mapOption2BoxR;
		else if (mapOption2Box.contains(x,y))
			hoverBox = mapOption2Box;		
		else if (mapOption3Box.contains(x,y))
			hoverBox = mapOption3Box;		
		else if (sizeBoxL.contains(x,y))
			hoverBox = sizeBoxL;
		else if (sizeBoxR.contains(x,y))
			hoverBox = sizeBoxR;
		else if (sizeBox.contains(x,y))
			hoverBox = sizeBox;
		else if (sizeOptionBoxL.contains(x,y))
			hoverBox = sizeOptionBoxL;
		else if (sizeOptionBoxR.contains(x,y))
			hoverBox = sizeOptionBoxR;
		else if (sizeOptionBox.contains(x,y))
			hoverBox = sizeOptionBox;		
		else if (aiBoxL.contains(x,y))
			hoverBox = aiBoxL;
		else if (aiBoxR.contains(x,y))
			hoverBox = aiBoxR;
		else if (aiBox.contains(x,y))
			hoverBox = aiBox;
		else if (crBoxL.contains(x,y))
			hoverBox = crBoxL;
		else if (crBoxR.contains(x,y))
			hoverBox = crBoxR;
		else if (crBox.contains(x,y))
			hoverBox = crBox;
		else if (newRacesBox.contains(x,y))
			hoverBox = newRacesBox;
		else if (showAbilityBox.contains(x,y))
			hoverBox = showAbilityBox;
		else if (diffBoxL.contains(x,y))
			hoverBox = diffBoxL;
		else if (diffBoxR.contains(x,y))
			hoverBox = diffBoxR;
		else if (diffBox.contains(x,y))
			hoverBox = diffBox;
		else if (oppBoxU.contains(x,y))
			hoverBox = oppBoxU;
		else if (oppBoxD.contains(x,y))
			hoverBox = oppBoxD;
		else if (oppBox.contains(x,y))
			hoverBox = oppBox;
		else {
			boolean selectable = newGameOptions().selectableAI();
			boolean selectableCR = useSelectableAbilities.get();
			for (int i=0;i<oppAI.length;i++) {
				if (selectable && oppAI[i].contains(x,y)) {
					hoverBox = oppAI[i];
					break;
				}
				else if (selectableCR && oppCR[i].contains(x,y)) {
					hoverBox = oppCR[i];
					break;
				}
				else if (oppSet[i].contains(x,y)) {
					hoverBox = oppSet[i];
					break;
				}
			}
		}
		if (hoverBox != prevHover) 
			repaint();
	}
	@Override
	public void mouseClicked(MouseEvent e) { }
	@Override
	public void mousePressed(MouseEvent e) { }
	@Override
	public void mouseReleased(MouseEvent e) { // BR: added full mouse control
		if (e.getButton() > 3)
			return;
		if (hoverBox == null)
			return;
		boolean up = !SwingUtilities.isRightMouseButton(e);
		if (hoverBox == backBox)
			doBackBoxAction();
        else if (hoverBox == defaultBox)
        	doDefaultBoxAction();
        else if (hoverBox == userBox)
			doUserBoxAction();
        else if (hoverBox == lastBox)
			doLastBoxAction();
		else if (hoverBox == settingsBox)
			goToOptions();
		// modnar: add UI panel for modnar MOD game options
		else if (hoverBox == modASettingsBox)
			goToModOptions();
		// BR: second UI panel for MOD game options
		else if (hoverBox == modBSettingsBox)
			goToMod2Options();
		// BR: Display UI panel for MOD game options
		else if (hoverBox == globalModSettingsBox)
			goToModViewOptions();
		else if (hoverBox == startBox)
			doStartBoxAction();
		else if (hoverBox == shapeBoxL)
			prevGalaxyShape(true);
		else if (hoverBox == shapeBox)
			if(up) nextGalaxyShape(true);
			else prevGalaxyShape(true);
		else if (hoverBox == shapeBoxR)
			nextGalaxyShape(true);		
		else if (hoverBox == mapOption1BoxL)
			prevMapOption1(true);
		else if (hoverBox == mapOption1Box)
			if (isShapeTextGalaxy())
				selectGalaxyTextFromList();
			else if(up) nextMapOption1(true);
			else prevMapOption1(true);
		else if (hoverBox == mapOption1BoxR)
			nextMapOption1(true);
		else if (hoverBox == mapOption2BoxL)
			prevMapOption2(true);
		else if (hoverBox == mapOption3Box)
			selectBitmapFromList();
		else if (hoverBox == mapOption2Box)
			if(up) nextMapOption2(true);
			else prevMapOption2(true);
		else if (hoverBox == mapOption2BoxR)
			nextMapOption2(true);
		else if (hoverBox == sizeBoxL)
			prevGalaxySize(false, true);
		else if (hoverBox == sizeBox)
			if(up) nextGalaxySize(false, true);
			else prevGalaxySize(false, true);
		else if (hoverBox == sizeBoxR)
			nextGalaxySize(false, true);
		else if (hoverBox == sizeOptionBoxL) {
			prefStarsPerEmpire.prev(e);
			newGameOptions().galaxyShape().quickGenerate();
			repaint();
		}
		else if (hoverBox == sizeOptionBox) {
			softClick();
			prefStarsPerEmpire.toggle(e);
			newGameOptions().galaxyShape().quickGenerate(); 
			repaint();
		}
		else if (hoverBox == sizeOptionBoxR) {
			softClick();
			prefStarsPerEmpire.next(e);
			newGameOptions().galaxyShape().quickGenerate(); 
			repaint();
		}
		else if (hoverBox == aiBoxL)
			prevOpponentAI(true);
		else if (hoverBox == aiBox)
			if(up) nextOpponentAI(true);
			else prevOpponentAI(true);
		else if (hoverBox == aiBoxR)
			nextOpponentAI(true);
		else if (hoverBox == crBoxL)
			prevOpponentCR(true);
		else if (hoverBox == crBox)
			if(up) nextOpponentCR(true);
			else prevOpponentCR(true);
		else if (hoverBox == crBoxR)
			nextOpponentCR(true);
		else if (hoverBox == newRacesBox)
			toggleNewRaces(true);
		else if (hoverBox == showAbilityBox)
			toggleShowAbility(true);
		else if (hoverBox == diffBoxL)
			prevGameDifficulty(true);
		else if (hoverBox == diffBox)
			if(up) nextGameDifficulty(true);
			else prevGameDifficulty(true);
		else if (hoverBox == diffBoxR)
			nextGameDifficulty(true);
		else if (hoverBox == oppBoxU)
			increaseOpponents(true);
		else if (hoverBox == oppBox)
			if(up) increaseOpponents(true);
			else decreaseOpponents(true);
		else if (hoverBox == oppBoxD)
			decreaseOpponents(true);
		else {
			for (int i=0;i<oppSet.length;i++) {
				if (hoverBox == oppSet[i]) {
					if(up) nextOpponent(i, true);
					else prevOpponent(i, true);
					break;
				}
				else if (hoverBox == oppAI[i]) {
					if(up) nextSpecificOpponentAI(i, true);
					else prevSpecificOpponentAI(i, true);
					break;
				}
				else if (hoverBox == oppCR[i]) {
					if(up) nextSpecificOpponentCR(i, true);
					else prevSpecificOpponentCR(i, true);
					break;
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) { }
	@Override
	public void mouseExited(MouseEvent e) {
		if (hoverBox != null) {
			hoverBox = null;
			repaint();
		}
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		boolean up = e.getWheelRotation() > 0;
		if (hoverBox == shapeBox) {
			if (up)
				prevGalaxyShape(false);
			else
				nextGalaxyShape(false);
		}
		else if (hoverBox == mapOption1Box) {
			if (up)
				prevMapOption1(false);
			else
				nextMapOption1(false);
		}
		else if (hoverBox == mapOption2Box) {
			if (up)
				prevMapOption2(false);
			else
				nextMapOption2(false);
		}
		else if (hoverBox == sizeBox) {
			if (up)
				prevGalaxySize(true, false);
			else
				nextGalaxySize(true, false);
		}
		else if (hoverBox == sizeOptionBox) {
			prefStarsPerEmpire.toggle(e);
			newGameOptions().galaxyShape().quickGenerate(); 
			repaint();
		}
		else if (hoverBox == aiBox) {
			if (up)
				prevOpponentAI(false);
			else
				nextOpponentAI(false);
		}
		else if (hoverBox == crBox) {
			if (up)
				prevOpponentCR(false);
			else
				nextOpponentCR(false);
		}
		else if (hoverBox == newRacesBox) {
			toggleNewRaces(false);
		}
		else if (hoverBox == showAbilityBox) {
			toggleShowAbility(false);
		}
		else if (hoverBox == diffBox) {
			if (up)
				prevGameDifficulty(false);
			else
				nextGameDifficulty(false);
		}
		else if (hoverBox == oppBox) {
			if (up)
				decreaseOpponents(false);
			else
				increaseOpponents(false);
		}
		else {
			for (int i=0;i<oppSet.length;i++) {
				if (hoverBox == oppSet[i]) {
					if (up)
						prevOpponent(i, false);
					else
						nextOpponent(i, false);
				}
			}
			for (int i=0;i<oppAI.length;i++) {
				if (hoverBox == oppAI[i]) {
					if (up)
						prevSpecificOpponentAI(i, false);
					else
						nextSpecificOpponentAI(i, false);
				}
			}
			for (int i=0;i<oppCR.length;i++) {
				if (hoverBox == oppCR[i]) {
					if (up)
						prevSpecificOpponentCR(i, false);
					else
						nextSpecificOpponentCR(i, false);
				}
			}
		}
	}
}
