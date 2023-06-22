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

import static rotp.model.empires.CustomRaceDefinitions.BASE_RACE_MARKER;
import static rotp.model.empires.CustomRaceDefinitions.fileToAlienRace;
import static rotp.model.empires.CustomRaceDefinitions.getAllowedAlienRaces;
import static rotp.model.empires.CustomRaceDefinitions.getBaseRaceList;
import static rotp.model.game.IBaseOptsTools.BASE_UI;
import static rotp.model.game.IBaseOptsTools.LIVE_OPTIONS_FILE;
import static rotp.model.game.IGalaxyOptions.aliensNumber;
import static rotp.model.game.IGalaxyOptions.bitmapGalaxyLastFolder;
import static rotp.model.game.IGalaxyOptions.difficultySelection;
import static rotp.model.game.IGalaxyOptions.galaxyRandSource;
import static rotp.model.game.IGalaxyOptions.globalCROptions;
import static rotp.model.game.IGalaxyOptions.optionsGalaxy;
import static rotp.model.game.IGalaxyOptions.shapeOption1;
import static rotp.model.game.IGalaxyOptions.shapeOption2;
import static rotp.model.game.IGalaxyOptions.shapeOption3;
import static rotp.model.game.IGalaxyOptions.shapeSelection;
import static rotp.model.game.IGalaxyOptions.showNewRaces;
import static rotp.model.game.IGalaxyOptions.sizeSelection;
import static rotp.model.game.IGalaxyOptions.useSelectableAbilities;
import static rotp.model.game.IGameOptions.OPPONENT_AI_HYBRID;
import static rotp.model.game.IMainOptions.compactOptionOnly;
import static rotp.model.game.IMainOptions.galaxyPreviewColorStarsSize;
import static rotp.model.game.IMainOptions.minListSizePopUp;
import static rotp.model.game.IPreGameOptions.dynStarsPerEmpire;
import static rotp.ui.UserPreferences.GALAXY_TEXT_FILE;
import static rotp.ui.util.IParam.LABEL_DESCRIPTION;
import static rotp.ui.util.IParam.labelFormat;
import static rotp.ui.util.IParam.langLabel;
import static rotp.ui.util.IParam.realLangLabel;
import static rotp.ui.util.IParam.rowFormat;
import static rotp.ui.util.IParam.tableFormat;

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
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
import rotp.model.ai.AIList;
import rotp.model.empires.Race;
import rotp.model.galaxy.GalaxyFactory.GalaxyCopy;
import rotp.model.galaxy.GalaxyShape;
import rotp.model.galaxy.GalaxyShape.EmpireSystem;
import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.game.IInGameOptions;
import rotp.ui.NoticeMessage;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.HelpUI.HelpSpec;
import rotp.ui.main.SystemPanel;
import rotp.ui.util.ListDialog;
import rotp.ui.util.ParamButtonHelp;
import rotp.ui.util.ParamList;
import rotp.ui.util.SpecificCROption;
import rotp.util.ModifierKeysState;

public final class SetupGalaxyUI  extends BaseModPanel implements MouseWheelListener {
	private static final long serialVersionUID = 1L;
    // public  static final String guiTitleID	= "SETUP_GALAXY";
	public  static final String GUI_ID       = "START_GALAXY";
	private static final String BACK_KEY	 = "SETUP_BUTTON_BACK";
	private static final String RESTART_KEY	 = "SETUP_BUTTON_RESTART";
	private static final String START_KEY	 = "SETUP_BUTTON_START";
	private static final String SIZE_OPT_KEY = "SETUP_GALAXY_SIZE_STAR_PER_EMPIRE";
	private static final String NO_SELECTION = "SETUP_BITMAP_NO_SELECTION";
	private static final String SPECIFIC_AI  = "SETUP_SPECIFIC_AI";
	private static final String GLOBAL_AI    = "SETUP_GLOBAL_AI";
	private static final String SPECIFIC_ABILITY = "SETUP_SPECIFIC_ABILITY";
	private static final String GLOBAL_ABILITIES = "SETUP_GLOBAL_ABILITY";
	private static final String OPPONENT_RANDOM	 = "SETUP_OPPONENT_RANDOM";
	public  static final int	MAX_DISPLAY_OPPS = 49;
	private static String opponentRandom = "???";
	private static SetupGalaxyUI instance;
    private final Color darkBrownC = new Color(112,85,68);
	private BufferedImage backImg, playerRaceImg;
	private BufferedImage smBackImg;
    private int bSep = s15;

	public static final ParamButtonHelp startButtonHelp = new ParamButtonHelp( // For Help Do not add the list
			"SETUP_START",
			START_KEY,
			"",
			RESTART_KEY,
			"");
	private final ParamList opponentAI			= new ParamList( // For Guide
			BASE_UI, "OPPONENT_AI",
			IGameOptions.globalAIset().getAliens(),
			IGameOptions.defaultAI.aliensKey) {
		@Override public String	getOptionValue(IGameOptions options)	{
			return options.selectedOpponentAIOption();
		}
		@Override public String	guideValue()	{ return langLabel(get()); }
		@Override public void reInit(List<String> list) {
			if (list == null)
				super.reInit(IGameOptions.globalAIset().getAliens());
			else
				super.reInit(list);
		}
	};
	private final ParamList specificAI			= new ParamList( // For Guide
			BASE_UI, "SPECIFIC_AI",
			IGameOptions.specificAIset().getAliens(),
			IGameOptions.defaultAI.aliensKey) {
		@Override public String	getOptionValue(IGameOptions options)	{
			return options.specificOpponentAIOption(mouseBoxIndex()+1);
		}
		@Override public String	guideValue()	{ return langLabel(get()); }
		@Override public void reInit(List<String> list) {
			if (list == null)
				super.reInit(IGameOptions.specificAIset().getAliens());
			else
				super.reInit(list);
		}
	};
	private final ParamList specificOpponent	= new ParamList( // For Guide
			BASE_UI, "SPECIFIC_OPPONENT", guiOptions().allRaceOptions(), opponentRandom) {
		@Override public void reInit(List<String> list) {
			if (list == null)
				super.reInit(guiOptions().allRaceOptions());
			else
				super.reInit(list);
		}
		@Override public String	getOptionValue(IGameOptions options)	{
			String val = options.selectedOpponentRace(mouseBoxIndex());
			if (val == null)
				return opponentRandom;
			return val;
		}
		@Override public String	guideValue()	{
			String key = get();
			if (key == null || key.equals(opponentRandom))
				return "Random";
			System.out.println("key = " + key);
			Race race = Race.keyed(key);
			String name = race.setupName();
			return name; 
		}
		@Override public String getRowGuide(int id)	{
			// System.out.println("id = " + id + " " + this.getGuiValue(id));
			String key, help;
			key = getGuiValue(id);
			if (key == null || key.equals(opponentRandom))
				help = labelFormat(opponentRandom) + "Surprise me!";
			else {
				Race   race		= Race.keyed(key);
				String raceName = race.setupName();
				help = labelFormat(raceName) + race.description1
						+ "<br>" + race.description2
						+ "&ensp /&ensp " + race.description3.replace("[race]", raceName)
						+ "&ensp /&ensp " + race.description4;
			}
			return help;
		}
		@Override public String valueGuide(int id)	{
			// System.out.println("id = " + id + " " + this.getGuiValue(id));
			String key, help;
			key = getGuiValue(id);
			if (key == null || key.equals(opponentRandom))
				help = "Surprise me!";
			else {
				Race   race		= Race.keyed(key);
				String raceName = race.setupName();
				System.out.println("key = " + key);
				help = labelFormat(raceName) + race.description1
						+ "<br>" + race.description2
						+ "<br>" + race.description3.replace("[race]", raceName)
						+ "<br>" + race.description4;
			}
			return tableFormat(help);
		}
	};
    private final ParamList globalAbilities		= new ParamList( // For Guide
			BASE_UI, "GLOBAL_ABILITY", globalAbilitiesList,
			SpecificCROption.BASE_RACE.value)	{
		@Override public String	getOptionValue(IGameOptions options) {
			return globalCROptions.get();
		}
		@Override public String	guideValue()	{ return text(get()); }
		@Override public String getRowGuide(int id)	{
			String key  = getGuiValue(id);
			String help = realLangLabel(key+LABEL_DESCRIPTION);
			if (help != null)
				return rowFormat(labelFormat(name(id)), help);

			Race   race		= fileToAlienRace(key);
			String raceName = race.setupName;
			if (key.startsWith(BASE_RACE_MARKER))
				help = labelFormat(name(id)) + "<i>(Base race)</i>&nbsp " + race.description1;
			else
				help = labelFormat(raceName) + race.description1;
			help += "<br>" + race.description2
					+ "&ensp /&ensp " + race.description3.replace("[race]", raceName)
					+ "&ensp /&ensp " + race.description4;
			return help;
		}
	};
	private final ParamList specificAbilities	= new ParamList( // For Guide
			BASE_UI, "SPECIFIC_ABILITY", specificAbilitiesList,
			SpecificCROption.defaultSpecificValue().value) {
		@Override public String	getOptionValue(IGameOptions options)	{
			return options.specificOpponentCROption(mouseBoxIndex()+1);
		}
		@Override public String	guideValue()	{ return text(get()); }
		@Override public String getRowGuide(int id)	{
			String key  = getGuiValue(id);
			String help = realLangLabel(key+LABEL_DESCRIPTION);
			if (help != null)
				return rowFormat(labelFormat(name(id)), help);

			Race   race		= fileToAlienRace(key);
			String raceName = race.setupName;
			if (key.startsWith(BASE_RACE_MARKER))
				help = labelFormat(name(id)) + "<i>(Base race)</i>&nbsp " + race.description1;
			else
				help = labelFormat(raceName) + race.description1;
			help += "<br>" + race.description2
					+ "&ensp /&ensp " + race.description3.replace("[race]", raceName)
					+ "&ensp /&ensp " + race.description4;
			return help;
		}
	};

	private Box mergedStaticBox		= new Box("SETUP_GALAXY_COMPACT_OPTIONS"); // BR add UI panel for MOD game options
	private Box mergedDynamicBox	= new Box("SETUP_GALAXY_COMPACT_OPTIONS"); // BR add UI panel for MOD game options
	private Box modStaticABox		= new Box("SETUP_GALAXY_CLASSIC_OPTIONS"); // BR add UI panel for MOD game options
	private Box modStaticBBox		= new Box("SETUP_GALAXY_CLASSIC_OPTIONS"); // BR add UI panel for MOD game options
	private Box modDynamicABox		= new Box("SETUP_GALAXY_CLASSIC_OPTIONS"); // BR add UI panel for MOD game options
	private Box modDynamicBBox		= new Box("SETUP_GALAXY_CLASSIC_OPTIONS"); // BR add UI panel for MOD game options
	private Box globalModSettingsBox= new Box("SETUP_GALAXY_CLASSIC_OPTIONS"); // BR add UI panel for MOD game options
	private Box	settingsBox			= new Box("SETUP_GALAXY_CLASSIC_OPTIONS");
	private Box	helpBox   			= new Box("SETTINGS_BUTTON_HELP");
	private Box	backBox				= new Box("SETUP_GALAXY_BACK");
	private Box	startBox;
	private Box	newRacesBox			= new Box("SETUP_GALAXY_RACE_LIST"); // BR:
	private Box	showAbilitiesBox; // BR:
	private Box		shapeBox;
	private Polygon	shapeBoxL		= new PolyBox();
	private Polygon	shapeBoxR		= new PolyBox();
	private Box		mapOption1Box;
	private Polygon	mapOption1BoxL	= new PolyBox();
	private Polygon	mapOption1BoxR	= new PolyBox();			 
	private Box		mapOption2Box;
	private Polygon	mapOption2BoxL	= new PolyBox();
	private Polygon	mapOption2BoxR	= new PolyBox();			 
	private Box		mapOption3Box; // BR:
	private Box		sizeOptionBox; // BR:
	private Polygon	sizeOptionBoxL	= new PolyBox();   // BR:
	private Polygon	sizeOptionBoxR	= new PolyBox();   // BR:
	private Box		sizeBox;
	private Polygon	sizeBoxL		= new PolyBox();
	private Polygon	sizeBoxR		= new PolyBox();
	private Box		diffBox;
	private Polygon	diffBoxL		= new PolyBox();
	private Polygon	diffBoxR		= new PolyBox();
	private Box		wysiwygBox; // BR:
	private Polygon	wysiwygBoxL		= new PolyBox();   // BR:
	private Polygon	wysiwygBoxR		= new PolyBox();   // BR:
	private Box		oppBox;
	private Polygon	oppBoxU			= new PolyBox();
	private Polygon	oppBoxD			= new PolyBox();
	private Box		aiBox			= new Box(opponentAI);
	private Polygon	aiBoxL			= new PolyBox();
	private Polygon	aiBoxR			= new PolyBox();
	private Box		abilitiesBox	= new Box(globalAbilities); // dataRace selection
	private Polygon	abilitiesBoxL	= new PolyBox(); // BR:
	private Polygon	abilitiesBoxR	= new PolyBox(); // BR:

	private Box[]	oppSet			= new Box[MAX_DISPLAY_OPPS];
	private Box[]	oppAI			= new Box[MAX_DISPLAY_OPPS];
	private Box[]	oppAbilities	= new Box[MAX_DISPLAY_OPPS]; // BR: dataRace selection

	private boolean starting = false;
	private int leftBoxX, rightBoxX, boxW, boxY, leftBoxH, rightBoxH;
	private int galaxyX, galaxyY, galaxyW, galaxyH;
	private static final LinkedList<String> specificAbilitiesList = new LinkedList<>();; 
	private static final LinkedList<String> globalAbilitiesList   = new LinkedList<>();; 
	private String[] specificAbilitiesArray; 
	private String[] globalAbilitiesArray; 
	private String[] galaxyTextArray;
    private Font dialogMonoFont;
    private int  dialogMonoFontSize = 20;
    private Font boxMonoFont;
    private int  boxMonoFontSize  = 15;

    public static ParamList specificAI() { return instance.specificAI; }
    public static ParamList opponentAI() { return instance.opponentAI; }
    public static int mouseBoxIndex() { return instance.hoverBox.mouseBoxIndex(); }

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
		instance = this;
		init0();
	}
	private void init0() {
		isSubMenu = false;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		
	}
    private void initOpponentGuide() {
		opponentRandom = text(OPPONENT_RANDOM);
		LinkedList<String> list = new LinkedList<>();
		list.addAll(guiOptions().getNewRacesOnOffList());
		list.add(opponentRandom); // For Random (???)
		specificOpponent.reInit(list);
		specificOpponent.defaultValue(opponentRandom);
	}
	private void initAIandAbilitiesList() {
		initOpponentGuide();
		// specific Abilities
		specificAbilitiesList.clear();
		specificAbilitiesList.addAll(SpecificCROption.options());
		specificAbilitiesList.removeLast(); // The blank one (USER_CHOICE)
		specificAbilitiesList.addAll(getAllowedAlienRaces());
		specificAbilitiesList.addAll(getBaseRaceList());
		specificAbilities.reInit(specificAbilitiesList);
		specificAbilitiesArray = specificAbilitiesList.toArray(new String[specificAbilitiesList.size()]);
		// global Abilities
		globalAbilitiesList.clear();
		globalAbilitiesList.addAll(globalCROptions.getBaseOptions());
		globalAbilitiesList.addAll(getAllowedAlienRaces());
		globalAbilitiesList.addAll(getBaseRaceList());
		globalAbilities.reInit(globalAbilitiesList);
		globalAbilitiesArray = globalAbilitiesList.toArray(new String[globalAbilitiesList.size()]);
	}


	@Override protected void singleInit() {
		startBox			= new Box(startButtonHelp);
		showAbilitiesBox	= new Box(useSelectableAbilities);
		shapeBox			= new Box(shapeSelection);
		mapOption1Box		= new Box(shapeOption1);
		mapOption2Box		= new Box(shapeOption2);
		mapOption3Box		= new Box(shapeOption3);
		sizeOptionBox		= new Box(dynStarsPerEmpire);
		sizeBox				= new Box(sizeSelection);
		diffBox				= new Box(difficultySelection);
		wysiwygBox			= new Box(galaxyRandSource);
		oppBox				= new Box(aliensNumber);
		
		
		
		paramList = optionsGalaxy;
		for (int i=0;i<oppSet.length;i++)
			oppSet[i] = new Box(specificOpponent, i);
		for (int i=0;i<oppAbilities.length;i++)
			oppAbilities[i] = new Box(specificAbilities, i);
		for (int i=0;i<oppAI.length;i++)
			oppAI[i] = new Box(specificAI, i);
		duplicateList = new LinkedList<>();
		duplicateList.add(difficultySelection);
		duplicateList.add(shapeSelection);
		duplicateList.add(sizeSelection);
		duplicateList.add(shapeOption1);
		duplicateList.add(shapeOption2);
		duplicateList.add(aliensNumber);
	}
	@Override public void init() {
		super.init();
		boxMonoFont    = null;
		dialogMonoFont = null;
        initAIandAbilitiesList();
        guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		refreshGui();
	}
	@Override protected String GUI_ID() { return GUI_ID; }
	@Override public void refreshGui() {
        guiOptions().setAndGenerateGalaxy();
        backImg = null;
        repaint();
	}
	@Override protected void close() {
		super.close();
		backImg = null;
		playerRaceImg  = null;
		boxMonoFont    = null;
		dialogMonoFont = null;
		galaxyTextArray = null;
	}
	@Override public void showHelp() {
		loadHelpUI();
		repaint();   
	}
    @Override public void advanceHelp() { cancelHelp(); }
	@Override public void cancelHelp() { RotPUI.helpUI().close(); }
	private void loadHelpUI() {
		int xBox, yBox, wBox;
		int x1, y1, x2, y2;
		int xb, xe, yb, ye;
		int nL, hBox;
		String txt;
		HelpSpec sp;
		Box dest;
		int w = getWidth();
		HelpUI helpUI = RotPUI.helpUI();
		helpUI.clear();

		// Overview = Top, Center
		txt  = text("SETUP_GALAXY_MAIN_DESC");
		nL   = 4;
		wBox = scaled(400);
		xBox = w/2 - wBox/2;
		xBox = rightBoxX;
		yBox = s10;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		int hShift = s20;
		int xTab   = s15;

		// Small Buttons at the bottom
		wBox = scaled(200);

		// Default button: Touch Galaxy
		dest = defaultBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		x1	 = rightBoxX - wBox;
		y1	 = dest.y - hBox - hShift;
		xBox = x1;
		yBox = y1;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*3/4;
		yb   = yBox + sp.height();
		xe   = dest.x + dest.width/2;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);
		
		// Back button; left Galaxy
		dest = backBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		y2	 = y1 - hBox - hShift;
		xBox = x1;
		yBox = y2;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox;
		yb   = yBox + sp.height()/2;
		xe   = dest.x - s25;
		ye   = dest.y;
		sp.setLineArr(new int[] {xb, yb, xb+s15, yb, xe, ye, dest.x, dest.y+s10});
		
		// User button: Left of Last button
		dest = userBox;
		txt  = dest.getDescription();
		nL   = 3;
		xBox = x1 - wBox*3/2 - xTab;
		yBox = y1;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*3/4;
		yb   = yBox + sp.height();
		xe   = dest.x + dest.width/2;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);


		// Last button: Left of Default button
		dest = lastBox;
		txt  = dest.getDescription();
		nL   = 3;
		x2	 = x1 - wBox - xTab;
		xBox = x2;
		yBox = y2;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*3/4;
		yb   = yBox + sp.height();
		xe   = dest.x + dest.width/4;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);

		// Guide button: Left of User button
		dest = guideBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		xBox = x2 - wBox - xTab;
		yBox = y2;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*1/4;
		yb   = y2 + sp.height();
		xe   = dest.x + dest.width*1/2;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);

		// Big buttons, bottom up

		// Start button; right Galaxy
		dest = startBox;
		txt  = dest.getHelp();
		nL   = 11;
		wBox = scaled(300);
		hBox = HelpUI.height(nL);
		xBox = rightBoxX + boxW + s50 - wBox;
		yBox = dest.y - hBox - scaled(150);
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*5/6;
		yb   = yBox + sp.height();
		xe   = dest.x + dest.width - s5;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);
		
		// Options Buttons
		txt  = text("");
		int margin = s3;
		// Box around buttons
 		if (compactOptionOnly.get()) {
			hBox = mergedStaticBox.height + 2*margin;
			wBox = mergedStaticBox.x + mergedDynamicBox.width - mergedDynamicBox.x + 2*margin;
			xBox = mergedDynamicBox.x - margin;
			yBox = mergedStaticBox.y - margin;
			nL   = 7;
			txt  = text("SETUP_GALAXY_COMPACT_OPTIONS_HELP");
		} else {
 			hBox = modDynamicBBox.y + modDynamicBBox.height - modStaticBBox.y + 2*margin;
			wBox = settingsBox.x + settingsBox.width - modStaticBBox.x + 2*margin;
			xBox = modStaticBBox.x - margin;
			yBox = modStaticBBox.y - margin;
			nL   = 9;
			txt  = text("SETUP_GALAXY_CLASSIC_OPTIONS_HELP");
		}
		int[] lineArr = sp.rect(xBox, yBox, wBox, hBox);
		ye = yBox;
		yb = yBox - s80;
	   
		hBox = HelpUI.height(nL);
		wBox = scaled(360);
		//xBox = rightBoxX + xTab;
		//yBox = boxY + rightBoxH - hBox - scaled(170);
		xBox = rightBoxX - scaled(130);
		yBox = y2 - hBox - hShift;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*5/6;
		yb   = yBox + sp.height();
		xe   = xb+s10;
		sp.setLine(xb, yb, xe, ye);
		sp.setLineArr(lineArr);

		// Opponents parameters
		wBox   = scaled(250);
		hShift = s60;  

		txt  = text("SETUP_GALAXY_AGAINST_DESC");
		dest = aiBox;
//		txt  = dest.getGuide();
		nL   = 5;
		hBox = HelpUI.height(nL);
		xBox = dest.x + dest.width/2 - wBox*3/4;
		yBox = dest.y + hShift;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*3/4;
		yb   = yBox;
		xe   = dest.x + dest.width/2;
		ye   = dest.y + dest.height;
		sp.setLine(xb, yb, xe, ye);

		dest = newRacesBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		xBox = dest.x + dest.width/2 - wBox/4;
		yBox = dest.y + hShift-s10;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox+s55, nL, txt);
		xb   = xBox + wBox/4;
		yb   = yBox;
		xe   = dest.x + dest.width/2;
		ye   = dest.y + dest.height;
		sp.setLine(xb, yb, xe, ye);

		dest = showAbilitiesBox;
		txt  = dest.getDescription();
		nL   = 3;
		hBox = HelpUI.height(nL);
		xBox = dest.x + dest.width + s40;
		yBox = dest.y + dest.height/2 - hBox/2 - s10;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox;
		yb   = yBox + hBox/2 + s10;
		xe   = dest.x + dest.width;
		ye   = dest.y + dest.height/2;
		sp.setLine(xb, yb, xe, ye);

		wBox = scaled(450);
		dest = abilitiesBox;
		txt  = dest.getDescription();
		nL   = 5;
		hBox = HelpUI.height(nL);
		xBox = dest.x + dest.width + s70 - wBox;
		xBox = leftBoxX;
		yBox = dest.y - hBox - s40;
		sp   = helpUI.addBrownHelpText(xBox, yBox, wBox, nL, txt);
		xb   = xBox + wBox*3/4;
		yb   = yBox + hBox;
		xe   = dest.x + dest.width*3/4;
		ye   = dest.y;
		sp.setLine(xb, yb, xe, ye);

		helpUI.open(this);
	}
	private void doStartBoxAction() {
		buttonClick();
		switch (ModifierKeysState.get()) {
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
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			// loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			// break;
		default: // Save
			guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			break; 
		}
		// Go back to Race Panel
		close();
		RotPUI.instance().selectSetupRacePanel();
 	}
	private static String backButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			// return restoreKey;
		default:
			return BACK_KEY;
		}
	}
	private static String startButtonKey() {
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT:
			return RESTART_KEY;
		default:
			return START_KEY;
		}
	}
	private int currentSpecificAbilityIndex(String s) {
		for (int i=0; i<specificAbilitiesArray.length; i++) {
			if (s.equalsIgnoreCase((String) specificAbilitiesArray[i]))
				return i;
		}
		return -1;
	}
	private int currentGlobalAbilityIndex(String s) {
		for (int i=0; i<globalAbilitiesArray.length; i++) {
			if (s.equalsIgnoreCase((String) globalAbilitiesArray[i]))
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
        String dirPath = bitmapGalaxyLastFolder.get();
        File selectedFile = new File(shapeOption3.get());
        if (selectedFile.exists()) {
        	dirPath = selectedFile.getParentFile().getAbsolutePath();
        	bitmapGalaxyLastFolder.set(dirPath);
        	UserPreferences.save();
        }
		JFileChooser fileChooser = new JFileChooser() {
			@Override
			protected JDialog createDialog(Component parent)
	                throws HeadlessException {
	            JDialog dlg = super.createDialog(parent);
	            dlg.setLocation(scaled(300), scaled(200));
	            dlg.setSize(scaled(420), scaled(470));
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
			dirPath = selectedFile.getParentFile().getAbsolutePath();
        	bitmapGalaxyLastFolder.set(dirPath);
        	UserPreferences.save();
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
		if (galaxyTextArray != null)
			return galaxyTextArray;
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

		galaxyTextArray = list.toArray(new String[list.size()]);
		return galaxyTextArray;
	}
	private String selectGalaxyTextFromList() {
		String initialChoice = newGameOptions().selectedGalaxyShapeOption1();
		String message = "Make your choice, (This list can be edited in the file) " + GALAXY_TEXT_FILE;
		ListDialog dialog = new ListDialog(
		    	this,							// Frame component
		    	getParent(),					// Location component
		    	message,						// Message
		        "Galaxy Text selection",		// Title
		        (String[]) getGalaxyTextList(),	// List
		        initialChoice, 					// Initial choice
		        null,							// long Dialogue
		        false,							// isVerticalWrap
		        scaled(420), scaled(320),		// size
		        dialogMonoFont(),				// Font
		        this,							// for listener
		        null,							// Alternate return
		        null); 							// Help parameter

		String input = (String) dialog.showDialog();
	    if (input == null)
	    	return initialChoice;
	    newGameOptions().selectedGalaxyShapeOption1(input);
	    newGameOptions().galaxyShape().quickGenerate();
		repaint();
	    return input;
	}
	@Override public void preview(String s) {
		if (s == null)
			return;
		if (this.isShapeTextGalaxy())
			newGameOptions().selectedGalaxyShapeOption1(s);
		else if (this.isShapeBitmapGalaxy()){
			shapeOption3.set(s);
		}
	    newGameOptions().galaxyShape().quickGenerate(); 
		repaint();
	}	
	private String selectSpecificAIFromList(int i) {
		String title			= text(SPECIFIC_AI);
		String message			= text(SPECIFIC_AI + LABEL_DESCRIPTION);
		String initialChoice	= text(newGameOptions().specificOpponentAIOption(i+1));
		AIList list				= IGameOptions.specificAIset();
		List<String> returnList = list.getAliens();
		String[] choiceArray	= list.getNames().toArray(new String[list.size()]);;
		ListDialog dialog		= new ListDialog(
		    	this, getParent(),			// Frame & Location component
		    	message, title,				// Message, Title
		    	choiceArray,				// List
		        initialChoice, 				// Initial choice
		        "XX_AI: Character_XX",		// long Dialogue
		        true,						// isVerticalWrap
		        scaled(325), scaled(185),	// size Width, Height
				null, null,					// Font, Preview
				returnList,					// Alternate return
				specificAI);				// help parameter
		String input = (String) dialog.showDialog();
		ModifierKeysState.reset();
		repaint();
	    if (input == null)
	    	return initialChoice;

	    newGameOptions().specificOpponentAIOption(input, i+1);
	    return input;
	}
	private String selectGlobalAIFromList() {
		String title			= text(GLOBAL_AI);
		String message			= text(GLOBAL_AI + LABEL_DESCRIPTION);
		String initialChoice	= text(newGameOptions().selectedOpponentAIOption());
		AIList list				= IGameOptions.globalAIset();
		List<String> returnList = list.getAliens();
		String[] choiceArray	= list.getNames().toArray(new String[list.size()]);;
		ListDialog dialog		= new ListDialog(
		    	this, getParent(),			// Frame & Location component
		    	message, title,				// Message, Title
		    	choiceArray,				// List
		        initialChoice, 				// Initial choice
		        "XX_AI:Character_XX",		// long Dialogue
		        true,						// isVerticalWrap
		        scaled(225), scaled(250),	// size Width, Height
				null, null,					// Font, Preview
				returnList,					// Alternate return
				opponentAI);				// help parameter

		String input = (String) dialog.showDialog();
		ModifierKeysState.reset();
		repaint();
	    if (input == null)
	    	return initialChoice;

	    newGameOptions().selectedOpponentAIOption(input);
	    return input;
	}
	private String selectSpecificAbilityFromList(int i) {
		String title   = text(SPECIFIC_ABILITY);
		String message = text(SPECIFIC_ABILITY + LABEL_DESCRIPTION);
		String initialChoice = newGameOptions().specificOpponentCROption(i);
		ListDialog dialog = new ListDialog(
		    	this, getParent(),	// Frame & Location component
		    	message, title,				// Message, Title
		        specificAbilitiesArray,		// List
		        initialChoice, 				// Initial choice
		        "XX_RACE_JACKTRADES_XX",	// long Dialogue
		        false,						// isVerticalWrap
		        scaled(400), scaled(420),	// size
				null, null, null,			// Font, Preview, Alternate return
				specificAbilities); // help parameter

		String input = (String) dialog.showDialog();
		ModifierKeysState.reset();
		repaint();
	    if (input == null)
	    	return initialChoice;
	    newGameOptions().specificOpponentCROption(input, i);
	    return input;
	}
	private String selectAlienAbilityFromList() {
		String title   = text(GLOBAL_ABILITIES);
		String message = text(GLOBAL_ABILITIES + LABEL_DESCRIPTION);
		String initialChoice = globalCROptions.get();
		ListDialog dialog = new ListDialog(
			    this, getParent(),	// Frame & Location component
		    	message, title,				// Message, Title
		        globalAbilitiesArray,		// List
		        initialChoice, 				// Initial choice
		        "XX_RACE_JACKTRADES_XX",	// long Dialogue
		        false,						// isVerticalWrap
		        scaled(400), scaled(420),	// size
				null, null, null,			// Font, Preview, Alternate return
				globalAbilities); // help parameter

		String input = (String) dialog.showDialog();
		ModifierKeysState.reset();
		ModifierKeysState.reset();
		repaint();
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

		for (Box box: oppSet)
			box.setBounds(0,0,0,0);
		for (Box box: oppAI)
			box.setBounds(0,0,0,0);
		for (Box box: oppAbilities)
			box.setBounds(0,0,0,0);
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
			int y2o = y2;
			int mugHo = mugH;
			if (selectableCR) {
				y2o   += boundH;
				mugHo -= boundH;
			}
			if (selectableAI) {
				mugHo -= boundH;
			}
			oppSet[i].setBounds(x2,y2o,mugW,mugHo);
			// oppAI[i].setBounds(x2,y2+mugH-s20,mugW,s20);
			oppAI[i].setBounds(x2,y2+mugH-boundH,mugW,boundH); // BR: Adjusted
			oppAbilities[i].setBounds(x2,y2,mugW,boundH);
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
		if ((	 hoverPolyBox == shapeBoxL)		 || (hoverPolyBox == shapeBoxR)
			||  (hoverPolyBox == sizeBoxL)		 || (hoverPolyBox == sizeBoxR)
			||  (hoverPolyBox == diffBoxL)		 || (hoverPolyBox == diffBoxR)
			||  (hoverPolyBox == wysiwygBoxL)	 || (hoverPolyBox == wysiwygBoxR)
			||  (hoverPolyBox == aiBoxL)		 || (hoverPolyBox == aiBoxR)
			||  (hoverPolyBox == abilitiesBoxL)		 || (hoverPolyBox == abilitiesBoxR)
			||  (hoverPolyBox == mapOption1BoxL) || (hoverPolyBox == mapOption1BoxR)
			||  (hoverPolyBox == mapOption2BoxL) || (hoverPolyBox == mapOption2BoxR)
			||  (hoverPolyBox == sizeOptionBoxL) || (hoverPolyBox == sizeOptionBoxR)
			||  (hoverPolyBox == oppBoxU)		 || (hoverPolyBox == oppBoxD)) {
			g.setColor(Color.yellow);
			g.fill(hoverPolyBox);
		}
		else if ((hoverBox == shapeBox)		|| (hoverBox == sizeBox)
			|| (hoverBox == mapOption1Box)	|| (hoverBox == mapOption2Box)
			|| (hoverBox == sizeOptionBox)	|| (hoverBox == abilitiesBox)
			|| (hoverBox == aiBox)			|| (hoverBox == newRacesBox)
			|| (hoverBox == showAbilitiesBox)	|| (hoverBox == mapOption3Box)
			|| (hoverBox == diffBox)		|| (hoverBox == wysiwygBox)
			|| (hoverBox == oppBox)) {
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
				for (int i=0;i<oppAbilities.length;i++) {
					if (hoverBox == oppAbilities[i]) {
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
		int x4cr = abilitiesBox.x+((aiBox.width-crSW)/2);
		int y4cr = abilitiesBox.y+abilitiesBox.height-s3;
		drawString(g,crLbl, x4cr, y4cr);
		
		// draw Show Abilities Yes/No text
		String showAbilityLbl = showAbilityStr();
		int showAbilitySW = g.getFontMetrics().stringWidth(showAbilityLbl);
		int x4d = showAbilitiesBox.x+((showAbilitiesBox.width-showAbilitySW)/2);
		int y4d = showAbilitiesBox.y+showAbilitiesBox.height-s3;
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
				int x5d = mapOption1Box.x+((mapOption1Box.width-sw1)/2);
				drawString(g,label1, x5d, y5+s20);
				g.setFont(prevFont);
			}
			else {
				String label1 = text(newGameOptions().selectedGalaxyShapeOption1());
				int sw1 = g.getFontMetrics().stringWidth(label1);
				int x5d = mapOption1Box.x+((mapOption1Box.width-sw1)/2);
				drawString(g,label1, x5d, y5+s20);
			}
			if (newGameOptions().numGalaxyShapeOption2() > 0) {
				if (isShapeBitmapGalaxy()) {
					Font prevFont = g.getFont();
					String label3 = getNameFromPath(shapeOption3.get());
					if (label3.equals(shapeOption3.defaultValue()))
						label3 = text(NO_SELECTION);
			        int fs  = scaledFont(g, label3, mapOption3Box.width, 15, 10);
					int sw2 = g.getFontMetrics().stringWidth(label3);
					g.setFont(narrowFont(fs));
					int x5e =mapOption3Box.x+((mapOption3Box.width-sw2)/2);
					if (compactOptionOnly.get())
						drawString(g,label3, x5e, y5+s60);
					else
						drawString(g,label3, x5e, y5+s40);
					g.setFont(prevFont);
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
			String label = text(SIZE_OPT_KEY, dynStarsPerEmpire.guideValue());
			int sw2 = g.getFontMetrics().stringWidth(label);
			int x5b1 =sizeOptionBox.x+((sizeOptionBox.width-sw2)/2);
			drawString(g,label, x5b1, y5+s20);		   
		}
		
		String diffLbl = text(newGameOptions().selectedGameDifficulty());
		// modnar: add custom difficulty level option, set in Remnants.cfg
		// append this custom difficulty percentage to diffLbl if selected
		if (diffLbl.equals("Custom")) {
			diffLbl = diffLbl + " (" + Integer.toString(IInGameOptions.customDifficulty.get()) + "%)";
		} else {
			diffLbl = diffLbl + " (" + Integer.toString(Math.round(100 * newGameOptions().aiProductionModifier())) + "%)";
		}
		
		int diffSW = g.getFontMetrics().stringWidth(diffLbl);
		int x5c =diffBox.x+((diffBox.width-diffSW)/2);
		drawString(g,diffLbl, x5c, y5);
		
		String wysiwygLbl;
		if (galaxyRandSource.get() == 0)
			wysiwygLbl = "Random";
		else
			wysiwygLbl = "Wysiwyg " + galaxyRandSource.guideValue();
		int wysiwygSW = g.getFontMetrics().stringWidth(wysiwygLbl);
		if (wysiwygSW > wysiwygBox.width) {
			wysiwygLbl = galaxyRandSource.guideValue();
			wysiwygSW  = g.getFontMetrics().stringWidth(wysiwygLbl);
		}
		int x5d =wysiwygBox.x+((wysiwygBox.width-wysiwygSW)/2);
		drawString(g,wysiwygLbl, x5d, y5+s20);
		
		// draw autoplay warning
		if (newGameOptions().isAutoPlay()) {
			g.setFont(narrowFont(16));
			String warning = text("SETTINGS_AUTOPLAY_WARNING");
			List<String> warnLines = this.wrappedLines(g, warning, galaxyW);
			g.setColor(Color.white);
			int warnY = y5+s60;
			if (compactOptionOnly.get())
				warnY += s25;

			for (String line: warnLines) {
				drawString(g,line, galaxyX, warnY);
				warnY += s18;
			}
		}

		drawHelpButton(g);
		drawButtons(g);
		showGuide(g);

		if (starting) {
			NoticeMessage.setStatus(text("SETUP_CREATING_GALAXY"));
			drawNotice(g, 30);
		}
	}
	@Override public void repaintButtons() {
		Graphics2D g = (Graphics2D) getGraphics();
		setFontHints(g);
		drawBackButtons(g);		
		drawButtons(g);
		g.dispose();
	}
	private void drawBackButtons(Graphics2D g) {
		int cnr = s5;

		
		if (!compactOptionOnly.get()) {
			// draw Advanced settings button
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(settingsBox.x, settingsBox.y, settingsBox.width, settingsBox.height, cnr, cnr);

			// draw MOD settings buttons
			g.fillRoundRect(modStaticABox.x, modStaticABox.y,
					modStaticABox.width, modStaticABox.height, cnr, cnr);

			g.fillRoundRect(modStaticBBox.x, modStaticBBox.y,
					modStaticBBox.width, modStaticBBox.height, cnr, cnr);

			g.fillRoundRect(modDynamicABox.x, modDynamicABox.y,
					modDynamicABox.width, modDynamicABox.height, cnr, cnr);

			g.fillRoundRect(modDynamicBBox.x, modDynamicBBox.y,
					modDynamicBBox.width, modDynamicBBox.height, cnr, cnr);

			g.fillRoundRect(globalModSettingsBox.x, globalModSettingsBox.y, 
					globalModSettingsBox.width, globalModSettingsBox.height, cnr, cnr);			
		} else {
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(mergedStaticBox.x, mergedStaticBox.y,
					mergedStaticBox.width, mergedStaticBox.height, cnr, cnr);

			g.fillRoundRect(mergedDynamicBox.x, mergedDynamicBox.y,
					mergedDynamicBox.width, mergedDynamicBox.height, cnr, cnr);
			
		}

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

		// draw GUIDE button
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
	}
    private void drawHelpButton(Graphics2D g) {
        helpBox.setBounds(s20,s20,s20,s25);
        g.setColor(darkBrownC);
        g.fillOval(s20, s20, s20, s25);
        g.setFont(narrowFont(25));
        if (helpBox == hoverBox)
            g.setColor(Color.yellow);
        else
            g.setColor(Color.white);

        drawString(g,"?", s26, s40);
    }
	private void drawButtons(Graphics2D g) {
		int cnr = s5;
		Stroke prev;
		
		if (!compactOptionOnly.get()) {
			// Advanced settings button
			g.setFont(narrowFont(18)); // 18 for 3 buttons// 20 for 2 buttons
			String text6 = text("SETUP_BUTTON_SETTINGS");
			int sw6 = g.getFontMetrics().stringWidth(text6);
			int x6 = settingsBox.x+((settingsBox.width-sw6)/2);
			int y6 = settingsBox.y+settingsBox.height-s8;
			Color c6 = hoverBox == settingsBox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, text6, 2, x6, y6, GameUI.borderDarkColor(), c6);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(settingsBox.x, settingsBox.y, settingsBox.width, settingsBox.height, cnr, cnr);
			g.setStroke(prev);

			// modnar: add UI panel for modnar MOD game options
			// MOD settings button
			String textMOD = text("SETUP_BUTTON_STATIC_A_SETTINGS");
			int swMOD = g.getFontMetrics().stringWidth(textMOD);
			int xMOD = modStaticABox.x+((modStaticABox.width-swMOD)/2);
			int yMOD = modStaticABox.y+modStaticABox.height-s8;
			Color cMOD = hoverBox == modStaticABox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(modStaticABox.x, modStaticABox.y, modStaticABox.width, modStaticABox.height, cnr, cnr);
			g.setStroke(prev);

			// BR: second UI panel for MOD game options
			// MOD settings button
			textMOD = text("SETUP_BUTTON_STATIC_B_SETTINGS");
			swMOD = g.getFontMetrics().stringWidth(textMOD);
			xMOD = modStaticBBox.x+((modStaticBBox.width-swMOD)/2);
			yMOD = modStaticBBox.y+modStaticBBox.height-s8;
			cMOD = hoverBox == modStaticBBox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(modStaticBBox.x, modStaticBBox.y, modStaticBBox.width, modStaticBBox.height, cnr, cnr);
			g.setStroke(prev);

			// BR: second UI panel for MOD game options
			// MOD settings button
			textMOD = text("SETUP_BUTTON_DYNAMIC_A_SETTINGS");
			swMOD = g.getFontMetrics().stringWidth(textMOD);
			xMOD = modDynamicABox.x+((modDynamicABox.width-swMOD)/2);
			yMOD = modDynamicABox.y+modDynamicABox.height-s8;
			cMOD = hoverBox == modDynamicABox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(modDynamicABox.x, modDynamicABox.y, modDynamicABox.width, modDynamicABox.height, cnr, cnr);
			g.setStroke(prev);

			// BR: second UI panel for MOD game options
			// MOD settings button
			textMOD = text("SETUP_BUTTON_DYNAMIC_B_SETTINGS");
			swMOD = g.getFontMetrics().stringWidth(textMOD);
			xMOD = modDynamicBBox.x+((modDynamicBBox.width-swMOD)/2);
			yMOD = modDynamicBBox.y+modDynamicBBox.height-s8;
			cMOD = hoverBox == modDynamicBBox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(modDynamicBBox.x, modDynamicBBox.y, modDynamicBBox.width, modDynamicBBox.height, cnr, cnr);
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
		} else {
			g.setFont(narrowFont(20)); // 18 for 3 buttons// 20 for 2 buttons
			// BR: second UI panel for MOD game options
			// MOD settings button
			String textMOD = text("SETUP_BUTTON_MERGED_STATIC_SETTINGS");
			int swMOD = g.getFontMetrics().stringWidth(textMOD);
			int xMOD = mergedStaticBox.x+((mergedStaticBox.width-swMOD)/2);
			int yMOD = mergedStaticBox.y+mergedStaticBox.height-s8;
			Color cMOD = hoverBox == mergedStaticBox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(mergedStaticBox.x, mergedStaticBox.y, mergedStaticBox.width, mergedStaticBox.height, cnr, cnr);
			g.setStroke(prev);

			// BR: second UI panel for MOD game options
			// MOD settings button
			textMOD = text("SETUP_BUTTON_MERGED_DYNAMIC_SETTINGS");
			swMOD = g.getFontMetrics().stringWidth(textMOD);
			xMOD = mergedDynamicBox.x+((mergedDynamicBox.width-swMOD)/2);
			yMOD = mergedDynamicBox.y+mergedDynamicBox.height-s8;
			cMOD = hoverBox == mergedDynamicBox ? Color.yellow : GameUI.borderBrightColor();
			drawShadowedString(g, textMOD, 2, xMOD, yMOD, GameUI.borderDarkColor(), cMOD);
			prev = g.getStroke();
			g.setStroke(stroke1);
			g.drawRoundRect(mergedDynamicBox.x, mergedDynamicBox.y, mergedDynamicBox.width, mergedDynamicBox.height, cnr, cnr);
			g.setStroke(prev);
		}


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
		 
		// BR: Guide Button 
		text = text(guideButtonKey());
        sw 	 = g.getFontMetrics().stringWidth(text);
		x = guideBox.x+((guideBox.width-sw)/2);
		y = guideBox.y+guideBox.height-s8;
		c = hoverBox == guideBox ? Color.yellow : GameUI.borderBrightColor();
		drawShadowedString(g, text, 2, x, y, GameUI.borderDarkColor(), c);
		prev = g.getStroke();
		g.setStroke(stroke1);
		g.drawRoundRect(guideBox.x, guideBox.y, guideBox.width, guideBox.height, cnr, cnr);
		g.setStroke(prev);
	}
	private String newRacesOnStr() {
		if (showNewRaces.get()) return text("SETUP_NEW_RACES_ON");
		else return text("SETUP_NEW_RACES_OFF");
	}
	private String showAbilityStr() {
		return useSelectableAbilities.guideValue();
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
	private boolean isDynamic() {
		return newGameOptions().selectedGalaxySize().equals(IGameOptions.SIZE_DYNAMIC);
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
	private void postGalaxySizeSelection(boolean click) {
		if (click) softClick();
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
		postSelectionLight(false);
	}
	private void postSelectionFull(boolean click) {
		if (click) softClick();
		newGameOptions().galaxyShape().quickGenerate();
		backImg = null;
		postSelectionLight(false);
	}
	private void postSelectionMedium(boolean click) {
		if (click) softClick();
		newGameOptions().galaxyShape().quickGenerate();
		postSelectionLight(false);
	}
	private void postSelectionLight(boolean click) {
		if (click) softClick();
		loadGuide();
		repaint();
	}
	private void nextMapOption1(boolean click) {
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
			shapeOption1.next();
		postSelectionMedium(click);
	}
	private void prevMapOption1(boolean click) {
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
			shapeOption1.prev();
		postSelectionMedium(click);
	}
	private void nextOpponentAI(boolean click) {
		newGameOptions().selectedOpponentAIOption(newGameOptions().nextOpponentAI());
		postSelectionLight(click);
	}
	private void prevOpponentAI(boolean click) {
		newGameOptions().selectedOpponentAIOption(newGameOptions().prevOpponentAI());
		postSelectionLight(click);
	}
	private void toggleOpponentAI(MouseEvent e) {
		softClick();
		boolean up  = !SwingUtilities.isRightMouseButton(e);
		boolean mid = SwingUtilities.isMiddleMouseButton(e);
		if (mid)
			newGameOptions().selectedOpponentAIOption(opponentAI.defaultValue());
		else if (newGameOptions().opponentAIOptions().size() >= minListSizePopUp.get()
					|| ModifierKeysState.isCtrlDown())
			selectGlobalAIFromList();
		else if (up)
			newGameOptions().selectedOpponentAIOption(newGameOptions().nextOpponentAI());
		else
			newGameOptions().selectedOpponentAIOption(newGameOptions().prevOpponentAI());
		postSelectionLight(false);
	}
	private void nextGlobalAbilities(boolean click) {
		if (click) softClick();
			String currCR = globalCROptions.get();
		int nextIndex = 0;
		if (currCR != null)
			nextIndex = currentGlobalAbilityIndex(currCR)+1;
		if (nextIndex >= globalAbilitiesArray.length)
			nextIndex = 0;
		String nextCR = (String) globalAbilitiesArray[nextIndex];
		globalCROptions.set(nextCR);
		postSelectionLight(false);
	}
	private void prevGlobalAbilities(boolean click) {
		if (click) softClick();
			String currCR = globalCROptions.get();
		int prevIndex = 0;
		if (currCR != null)
			prevIndex = currentGlobalAbilityIndex(currCR)-1;
		if (prevIndex < 0)
			prevIndex = globalAbilitiesArray.length-1;
		String prevCR = (String) globalAbilitiesArray[prevIndex];
		globalCROptions.set(prevCR);
		postSelectionLight(false);
	}
	private void toggleGlobalAbilities(MouseEvent e) {
		softClick();
		boolean up  = !SwingUtilities.isRightMouseButton(e);
		boolean mid = SwingUtilities.isMiddleMouseButton(e);
		if (mid)
			globalCROptions.setFromDefault();
		else if (globalAbilitiesArray.length >= minListSizePopUp.get()
				|| ModifierKeysState.isCtrlDown())
			selectAlienAbilityFromList();
		else if (up)
			nextGlobalAbilities(false);
		else
			prevGlobalAbilities(false);
		postSelectionLight(false);
	}
	private void toggleNewRaces(boolean click) {
		if (click) softClick();
		showNewRaces.toggle();
		initOpponentGuide();
		postSelectionLight(false);
	}
	private void nextSpecificOpponentAI(int i, boolean click) {
		if (click) softClick();
		if (click || ModifierKeysState.isCtrlDown())
			selectSpecificAIFromList(i);
		else
			newGameOptions().nextSpecificOpponentAI(i+1);
	}
	private void prevSpecificOpponentAI(int i, boolean click) {
		if (click) softClick();
		if (click || ModifierKeysState.isCtrlDown())
			selectSpecificAIFromList(i);
		else
			newGameOptions().prevSpecificOpponentAI(i+1);
	}
	private void toggleSpecificOpponentAI(int i, boolean click, boolean up, boolean mid) {
		if (click) softClick();
		if (mid)
			newGameOptions().specificOpponentAIOption(specificAI.defaultValue(), i+1);
		else if (up)
			nextSpecificOpponentAI(i, false);
		else
			prevSpecificOpponentAI(i, false);
		postSelectionLight(false);
	}
	private void toggleShowAbility(boolean click) {
		if (click) softClick();
		if (click && ModifierKeysState.isCtrlDown()) {
			String defVal = SpecificCROption.defaultSpecificValue().value;
            for (int i=0;i<oppAbilities.length;i++)
            	newGameOptions().specificOpponentCROption(defVal,i+1);
		}
		else
			useSelectableAbilities.toggle();
		postSelectionLight(false);
	}
	private void nextSpecificOpponentAbilities(int i, boolean click) {
		if (click) softClick();
		if (click || ModifierKeysState.isCtrlDown())
			selectSpecificAbilityFromList(i+1);
		else {
			String currCR = newGameOptions().specificOpponentCROption(i+1);
			int nextIndex = 0;
			if (currCR != null)
				nextIndex = currentSpecificAbilityIndex(currCR)+1;
			if (nextIndex >= specificAbilitiesArray.length)
				nextIndex = 0;
			String nextCR = (String) specificAbilitiesArray[nextIndex];
			newGameOptions().specificOpponentCROption(nextCR, i+1);
		}
	}
	private void prevSpecificOpponentAbilities(int i, boolean click) {
		if (click) softClick();
		if (click || ModifierKeysState.isCtrlDown())
			selectSpecificAbilityFromList(i+1);
		else {
			String currCR = newGameOptions().specificOpponentCROption(i+1);
			int prevIndex = 0;
			if (currCR != null)
				prevIndex = currentSpecificAbilityIndex(currCR)-1;
	        if (prevIndex < 0)
	        	prevIndex = specificAbilitiesArray.length-1;
	        String prevCR = (String) specificAbilitiesArray[prevIndex];
	        newGameOptions().specificOpponentCROption(prevCR, i+1);
		}
	}
	private void toggleSpecificOpponentAbilities(int i, boolean click, boolean up, boolean mid) {
		if (click) softClick();
		if (mid)
			newGameOptions().specificOpponentCROption(specificAbilities.defaultValue(), i+1);
		else if (up)
			nextSpecificOpponentAbilities(i, false);
		else
			prevSpecificOpponentAbilities(i, false);
		postSelectionLight(false);
	}
	private void toggleOpponent(int i, boolean click, boolean up, boolean mid) {
		if (click) softClick();
		if (mid)
			newGameOptions().selectedOpponentRace(i, null);
		else if (up)
			newGameOptions().nextOpponent(i);
		else
			newGameOptions().prevOpponent(i);
		postSelectionLight(false);
	}
	private void goToOptions() {
		buttonClick();
		AdvancedOptionsUI optionsUI = RotPUI.advancedOptionsUI();
		close();
		optionsUI.init();
	}
	// BR: add UI panel for MOD game options
	private void goToMergedStatic() {
		buttonClick();
		MergedStaticOptionsUI modOptionsUI = RotPUI.mergedStaticOptionsUI();
		close();
		modOptionsUI.start(0);
	}
	private void goToMergedDynamic() {
		buttonClick();
		MergedDynamicOptionsUI modOptionsUI = RotPUI.mergedDynamicOptionsUI();
		close();
		modOptionsUI.start(0);
	}
	private void goToModStaticA() {
		buttonClick();
		StaticAOptionsUI modOptionsUI = RotPUI.modOptionsStaticA();
		close();
		modOptionsUI.init();
	}
	private void goToModStaticB() {
		buttonClick();
		StaticBOptionsUI modBOptionsUI = RotPUI.modOptionsStaticB();
		close();
		modBOptionsUI.init();
	}
	private void goToModDynamicA() {
		buttonClick();
		DynamicAOptionsUI modOptionsUI = RotPUI.modOptionsDynamicA();
		close();
		modOptionsUI.init();
	}
	private void goToModDynamicB() {
		buttonClick();
		DynamicBOptionsUI modBOptionsUI = RotPUI.modOptionsDynamicB();
		close();
		modBOptionsUI.init();
	}
	// BR: Display UI panel for MOD game options
	private void goToModGlobalOptions() {
		buttonClick();
		ModGlobalOptionsUI modGlobalOptionsUI = RotPUI.modGlobalOptionsUI();
		close();
		modGlobalOptionsUI.init();
	}
	// BR: Add option to return to the main menu
	private void goToMainMenu() {
		buttonClick();
		switch (ModifierKeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			guiOptions().updateFromFile(LIVE_OPTIONS_FILE);	
			break;
		default:
			guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
			break;
		}
		close();
		RotPUI.instance().selectGamePanel();
	}
	// BR: For restarting with new options
	private void restartGame() { 
		guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		starting = true;
		buttonClick();
		repaint();
		GalaxyCopy oldGalaxy = new GalaxyCopy(newGameOptions());
		UserPreferences.setForNewGame();
		// Get the old galaxy parameters
		close();
        RotPUI.instance().selectRestartGamePanel(oldGalaxy);
		starting = false;
	}
	private void startGame() {
		guiOptions().saveOptionsToFile(LIVE_OPTIONS_FILE);
		starting = true;
		repaint();
		buttonClick();
		GameUI.gameName = generateGameName();
		UserPreferences.setForNewGame();
		close();
		final Runnable save = () -> {
			long start = System.currentTimeMillis();
			GameSession.instance().startGame(newGameOptions());
			RotPUI.instance().mainUI().checkMapInitialized();
			RotPUI.instance().selectIntroPanel();
			log("TOTAL GAME START TIME:" +(System.currentTimeMillis()-start));
			log("Game Name; "+GameUI.gameName);
			starting = false;
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

		abilitiesBoxL.reset();
		abilitiesBoxL.addPoint(sliderX-s4,sliderYCR+s1);
		abilitiesBoxL.addPoint(sliderX-s4,sliderYCR+sliderH-s2);
		abilitiesBoxL.addPoint(sliderX-s13,sliderYCR+(sliderH/2));
		g.fill(abilitiesBoxL);
		abilitiesBoxR.reset();
		abilitiesBoxR.addPoint(sliderX+sliderW+s4,sliderYCR+s1);
		abilitiesBoxR.addPoint(sliderX+sliderW+s4,sliderYCR+sliderH-s2);
		abilitiesBoxR.addPoint(sliderX+sliderW+s13,sliderYCR+(sliderH/2));
		g.fill(abilitiesBoxR);
		abilitiesBox.setBounds(sliderX, sliderYCR, sliderW, sliderH);
		g.fill(abilitiesBox);

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
		showAbilitiesBox.setBounds(bxSA , sliderYCR, widthSA, sliderH);
		g.fill(showAbilitiesBox);

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
		mapOption3Box.setBounds(0,0,0,0);
		if (newGameOptions().numGalaxyShapeOption2() > 0) {
			if (this.isShapeBitmapGalaxy()) {
				if (compactOptionOnly.get())
					// mapOption3Box.setBounds(sliderX-s13, sliderYAI+s60, sliderW+2*sectionW+s26, sliderH);
					mapOption3Box.setBounds(sliderX, sliderYAI+s60, sliderW+2*sectionW, sliderH);
				else
					mapOption3Box.setBounds(sliderX+sectionW-s13, sliderYAI+s40, sliderW+sectionW+s26, sliderH);
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

		wysiwygBoxL.reset();
		wysiwygBoxL.addPoint(sliderX-s4,sliderYAI+s1+s20);
		wysiwygBoxL.addPoint(sliderX-s4,sliderYAI+sliderH-s2+s20);
		wysiwygBoxL.addPoint(sliderX-s13,sliderYAI+(sliderH/2)+s20);
		g.fill(wysiwygBoxL);
		wysiwygBoxR.reset();
		wysiwygBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+s1+s20);
		wysiwygBoxR.addPoint(sliderX+sliderW+s4,sliderYAI+sliderH-s2+s20);
		wysiwygBoxR.addPoint(sliderX+sliderW+s13,sliderYAI+(sliderH/2+s20));
		g.fill(wysiwygBoxR);
		wysiwygBox.setBounds(sliderX, sliderYAI+s20, sliderW, sliderH);
		g.fill(wysiwygBox);
		
		int cnr = s5;
		// draw settings button
		int smallButtonH = s27; // 27 for 3 buttons // 30 for 2 buttons
		int smallButtonW = scaled(150); // 150 for 3 buttons // 180 for 2 buttons
		int smallButton2W = scaled(135); // for the two smaller buttons when 3 buttons
		// BR: buttons positioning
		int yb = 615; // 615 for 3 buttons (1 row) // 610 for 2 buttons
		int xb = 989; // 989 for 3 buttons // 960 for 2 buttons 1 row // 948 for centered
		int dx = 145; // 145 for 3 buttons // 200 for 2 buttons 1 row // 241 for centered
		int dy = 30; // 30 for 3 buttons // 35 for 2 buttons
		
		if (!compactOptionOnly.get()) {
			settingsBox.setBounds(scaled(xb), scaled(yb), smallButtonW, smallButtonH);
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(settingsBox.x, settingsBox.y, smallButtonW, smallButtonH, cnr, cnr);
			// modnar: add UI panel for modnar MOD game options // BR: Squeezed a little
			// draw MOD settings button
			modStaticABox.setBounds(scaled(xb-dx), scaled(yb), smallButton2W, smallButtonH);
			g.setPaint(GameUI.buttonRightBackground());
			g.fillRoundRect(modStaticABox.x, modStaticABox.y, smallButton2W, smallButtonH, cnr, cnr);
			// BR: second UI panel for MOD game options
			// draw MOD settings button
			modStaticBBox.setBounds(scaled(xb-2*dx), scaled(yb), smallButton2W, smallButtonH);
			g.setPaint(GameUI.buttonRightBackground());
			g.fillRoundRect(modStaticBBox.x, modStaticBBox.y, smallButton2W, smallButtonH, cnr, cnr);
			// BR: second UI panel for MOD game options
			// draw MOD settings button
			modDynamicABox.setBounds(scaled(xb-dx), scaled(yb+dy), smallButton2W, smallButtonH);
			g.setPaint(GameUI.buttonRightBackground());
			g.fillRoundRect(modDynamicABox.x, modDynamicABox.y, smallButton2W, smallButtonH, cnr, cnr);
			// BR: second UI panel for MOD game options
			// draw MOD settings button
			modDynamicBBox.setBounds(scaled(xb-2*dx), scaled(yb+dy), smallButton2W, smallButtonH);
			g.setPaint(GameUI.buttonRightBackground());
			g.fillRoundRect(modDynamicBBox.x, modDynamicBBox.y, smallButton2W, smallButtonH, cnr, cnr);
			// BR: Display Settings UI panel for MOD game options
			// draw MOD settings button
			globalModSettingsBox.setBounds(scaled(xb), scaled(yb+dy), smallButtonW, smallButtonH);
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(globalModSettingsBox.x, globalModSettingsBox.y, smallButtonW, smallButtonH, cnr, cnr);			
			mergedStaticBox.setBounds(0, 0, 0, 0);
			mergedDynamicBox.setBounds(0, 0, 0, 0);
		} else {
			yb = 610; // 615 for 3 buttons (1 row) // 610 for 2 buttons
			xb = 960; // 984 for 3 buttons // 960 for 2 buttons 1 row // 948 for centered
			dx = 200; // 145 for 3 buttons // 200 for 2 buttons 1 row // 241 for centered
			dy = 30; // for 1 row
			smallButtonW = scaled(180); // 150 for 3 buttons // 180 for 2 buttons
			smallButtonH = s30; // 27 for 3 buttons // 30 for 2 buttons
			// draw Merged settings button
			mergedStaticBox.setBounds(scaled(xb), scaled(yb+dy), smallButtonW, smallButtonH);
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(mergedStaticBox.x, mergedStaticBox.y, smallButtonW, smallButtonH, cnr, cnr);
			// draw Merged settings button
			mergedDynamicBox.setBounds(scaled(xb-dx), scaled(yb+dy), smallButtonW, smallButtonH);
			g.setPaint(GameUI.buttonLeftBackground());
			g.fillRoundRect(mergedDynamicBox.x, mergedDynamicBox.y, smallButtonW, smallButtonH, cnr, cnr);
			settingsBox.setBounds(0, 0, 0, 0);
			modStaticABox.setBounds(0, 0, 0, 0);
			modStaticBBox.setBounds(0, 0, 0, 0);
			modDynamicABox.setBounds(0, 0, 0, 0);
			modDynamicBBox.setBounds(0, 0, 0, 0);
			globalModSettingsBox.setBounds(0, 0, 0, 0);
		}

		int buttonH = s45;
		int buttonW = scaled(220);
		int yB = 685+10; // 2 Button's Rows Offset, was 685
		xb = 950; // was 950
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

		// draw GUIDE button
		buttonW = guideButtonWidth(g);
		xb = s20;
		guideBox.setBounds(xb, yb, buttonW, buttonH);
		g.setPaint(GameUI.buttonLeftBackground());
		g.fillRoundRect(guideBox.x, guideBox.y, buttonW, buttonH, cnr, cnr);

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
	@Override public String ambienceSoundKey()		{ return GameUI.AMBIENCE_KEY; }
	@Override public void keyPressed(KeyEvent e)	{
		super.keyPressed(e);
		switch(e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			doBackBoxAction();
			return;
		case KeyEvent.VK_ENTER:
			doStartBoxAction();
			return;
		case KeyEvent.VK_M: // BR: "M" = Go to Main Menu
			goToMainMenu();
			return;
		default:
			return;
		}
	}
	@Override public void mouseReleased(MouseEvent e) { // BR: added full mouse control
		if (e.getButton() > 3)
			return;
		if (hoverBox == null && hoverPolyBox == null)
			return;
		if (hoverBox == helpBox) {
			showHelp();
			return;
		}
		boolean up  = !SwingUtilities.isRightMouseButton(e);
		boolean mid = SwingUtilities.isMiddleMouseButton(e);
		if (hoverBox == backBox)
			doBackBoxAction();
        else if (hoverBox == defaultBox)
        	doDefaultBoxAction();
        else if (hoverBox == guideBox)
			doGuideBoxAction();
        else if (hoverBox == userBox)
			doUserBoxAction();
        else if (hoverBox == lastBox)
			doLastBoxAction();
		else if (hoverBox == settingsBox)
			goToOptions();
		// modnar: add UI panel for modnar MOD game options
		else if (hoverBox == modStaticABox)
			goToModStaticA();
		// BR: Merged UI panel for MOD game options
		else if (hoverBox == mergedStaticBox)
			goToMergedStatic();
		else if (hoverBox == mergedDynamicBox)
			goToMergedDynamic();
		// BR: second UI panel for MOD game options
		else if (hoverBox == modStaticBBox)
			goToModStaticB();
		else if (hoverBox == modDynamicABox)
			goToModDynamicA();
		else if (hoverBox == modDynamicBBox)
			goToModDynamicB();
		// BR: Display UI panel for MOD game options
		else if (hoverBox == globalModSettingsBox)
			goToModGlobalOptions();
		else if (hoverBox == startBox)
			doStartBoxAction();
		else if (hoverPolyBox == shapeBoxL) {
			shapeSelection.prev();
			postSelectionFull(true);
		}
		else if (hoverBox == shapeBox) {
			shapeSelection.toggle(e,  this);
			postSelectionFull(true);
		}
		else if (hoverPolyBox == shapeBoxR) {
			shapeSelection.next();
			postSelectionFull(true);
		}
		else if (hoverPolyBox == mapOption1BoxL)
			prevMapOption1(true);
		else if (hoverBox == mapOption1Box)
			if (isShapeTextGalaxy())
				selectGalaxyTextFromList();
			else {
				shapeOption1.toggle(e, this);
				postSelectionMedium(true);
			}
		else if (hoverPolyBox == mapOption1BoxR)
			nextMapOption1(true);
		else if (hoverPolyBox == mapOption2BoxL) {
			shapeOption2.prev();
			postSelectionMedium(true);
		}
		else if (hoverBox == mapOption2Box) {
			shapeOption2.toggle(e, this);
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == mapOption2BoxR) {
			shapeOption2.next();
			postSelectionMedium(true);
		}
		else if (hoverBox == mapOption3Box)
			selectBitmapFromList();
		else if (hoverPolyBox == sizeBoxL) {
			sizeSelection.prev();
			postGalaxySizeSelection(true);
		}
		else if (hoverBox == sizeBox) {
			sizeSelection.toggle(e, this);
			postGalaxySizeSelection(true);
		}
		else if (hoverPolyBox == sizeBoxR) {
			sizeSelection.next();
			postGalaxySizeSelection(true);
		}
		else if (hoverPolyBox == sizeOptionBoxL) {
			dynStarsPerEmpire.prev(e);
			postSelectionMedium(true);
		}
		else if (hoverBox == sizeOptionBox) {
			dynStarsPerEmpire.toggle(e, this);
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == sizeOptionBoxR) {
			dynStarsPerEmpire.next(e);
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == aiBoxL)
			prevOpponentAI(true);
		else if (hoverBox == aiBox)
			toggleOpponentAI(e);
		else if (hoverPolyBox == aiBoxR)
			nextOpponentAI(true);
		else if (hoverPolyBox == abilitiesBoxL)
			prevGlobalAbilities(true);
		else if (hoverBox == abilitiesBox)
			toggleGlobalAbilities(e);
		else if (hoverPolyBox == abilitiesBoxR)
			nextGlobalAbilities(true);
		else if (hoverBox == newRacesBox)
			toggleNewRaces(true);
		else if (hoverBox == showAbilitiesBox)
			toggleShowAbility(true);
		else if (hoverPolyBox == diffBoxL) {
			difficultySelection.prev();
			postSelectionLight(true);
		}
		else if (hoverBox == diffBox) {
			difficultySelection.toggle(e, this);
			postSelectionLight(true);
		}
		else if (hoverPolyBox == diffBoxR) {
			difficultySelection.next();
			postSelectionLight(true);
		}
		else if (hoverPolyBox == wysiwygBoxL) {
			galaxyRandSource.prev();
			postSelectionMedium(true);
		}
		else if (hoverBox == wysiwygBox) {
			galaxyRandSource.toggle(e, this);
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == wysiwygBoxR) {
			galaxyRandSource.next();
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == oppBoxU) {
			aliensNumber.next();
			postSelectionMedium(true);
		}
		else if (hoverBox == oppBox)
		 {
			aliensNumber.toggle(e, this);
			postSelectionMedium(true);
		}
		else if (hoverPolyBox == oppBoxD)
		 {
			aliensNumber.prev();
			postSelectionMedium(true);
		}
		else {
			for (int i=0;i<oppSet.length;i++) {
				if (hoverBox == oppAI[i]) {
					toggleSpecificOpponentAI(i, true, up, mid);
					break;
				}
				else if (hoverBox == oppAbilities[i]) {
					toggleSpecificOpponentAbilities(i, true, up, mid);
					break;
				}
				else if (hoverBox == oppSet[i]) {
					toggleOpponent(i, true, up, mid);
					break;
				}
			}
		}
	}
	@Override public void mouseWheelMoved(MouseWheelEvent e) {
		boolean up = e.getWheelRotation() > 0;
		if (hoverBox == shapeBox)  {
			shapeSelection.toggle(e);
			postSelectionFull(false);
		}
		else if (hoverBox == mapOption1Box) {
			shapeOption1.toggle(e);
			postSelectionMedium(false);
		}
		else if (hoverBox == mapOption2Box) {
			shapeOption2.toggle(e);
			postSelectionMedium(false);
		}
		else if (hoverBox == sizeBox) {
			sizeSelection.toggle(e);
			postGalaxySizeSelection(false);
		}
		else if (hoverBox == sizeOptionBox) {
			dynStarsPerEmpire.toggle(e);
			postSelectionMedium(false);
		}
		else if (hoverBox == aiBox) {
			if (up)
				prevOpponentAI(false);
			else
				nextOpponentAI(false);
		}
		else if (hoverBox == abilitiesBox) {
			if (up)
				prevGlobalAbilities(false);
			else
				nextGlobalAbilities(false);
		}
		else if (hoverBox == newRacesBox) {
			toggleNewRaces(false);
		}
		else if (hoverBox == showAbilitiesBox) {
			toggleShowAbility(false);
		}
		else if (hoverBox == diffBox)
		 {
			difficultySelection.toggle(e);
			postSelectionLight(false);
		}
		else if (hoverBox == wysiwygBox)
		 {
			galaxyRandSource.toggle(e);
			postSelectionMedium(false);
		}
		else if (hoverBox == oppBox) {
			aliensNumber.toggle(e);
			postSelectionMedium(false);
		}
		else {
			for (int i=0;i<oppAI.length;i++) {
				if (hoverBox == oppAI[i]) {
					toggleSpecificOpponentAI(i, false, up, false);
					return;
				}
			}
			for (int i=0;i<oppAbilities.length;i++) {
				if (hoverBox == oppAbilities[i]) {
					toggleSpecificOpponentAbilities(i, false, up, false);
					return;
				}
			}
			for (int i=0;i<oppSet.length;i++) {
				if (hoverBox == oppSet[i]) {
					toggleOpponent(i, false, up, false);
					return;
				}
			}
		}
	}
}
 
