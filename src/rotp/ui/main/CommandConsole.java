package rotp.ui.main;

import static rotp.model.game.IBaseOptsTools.GAME_OPTIONS_FILE;
import static rotp.model.game.IBaseOptsTools.LIVE_OPTIONS_FILE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import rotp.model.empires.Empire;
import rotp.model.empires.SystemInfo;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.galaxy.Transport;
import rotp.model.game.GameSession;
import rotp.model.game.IAdvOptions;
import rotp.model.game.IGameOptions;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamString;
import rotp.ui.util.ParamSubUI;
import rotp.util.Base;

public class CommandConsole extends JPanel  implements Base, ActionListener {

	private static final String newline = "<br>";
	private static final int NULL_ID	= -1;
	private static final int OPTION_ID	= 0;
	private static final int SETTING_ID	= 1;
	private static final int PANEL_ID	= 2;
	private static JFrame frame;
	private static CommandConsole instance;
	public	static Panel introPanel, loadPanel, savePanel;

	private final JLabel cmdLabel, optLabel, resultLabel;
	private final JTextField cmdField, optField;
	private final JTextPane resultPane;
	private final JScrollPane scrollPane;
	private final List<Panel>		panels		= new ArrayList<>();
	private final List<Transport>	transports	= new ArrayList<>();
	private final List<ShipFleet>	fleets		= new ArrayList<>();
	private final List<StarSystem>	systems		= new ArrayList<>();
	private Panel livePanel;
	private Panel mainPanel, setupPanel, gamePanel, speciesPanel;
	private int selectedStar, destStar, selectedEmpire, selectedFleet, selectedTransport, selectedDesign;
	private HashMap<Integer, Integer> altIndex2SystemIndex = new HashMap<>();
//	private Panel stars, fleet, ships, opponents;
//	private final List<SystemView> starList = new ArrayList<>();
	
	// ##### STATIC METHODS #####
	public static void updateConsole()			{ instance.reInit(); }
	public static void turnCompleted(int turn)	{
		instance.resultPane.setText("Current turn: " + turn + newline);
		
	}
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());

		Map<K, V> result = new LinkedHashMap<>();
		for (Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}
	// ##### INITIALIZERS #####
	private	void reInit()			{
		initAltIndex();
	}
	private void initAltIndex()		{
		// Alternative index built from distance to the original player homeworld.
		// The original index gives too much info on opponents home world and is too random for other systems.
		HashMap<Integer, Float> homeDistances = new HashMap<>();
		Galaxy gal = galaxy();
		StarSystem home = gal.system(0);
		for (int i=0; i<gal.systemCount; i++)
			homeDistances.put(i, home.distanceTo(gal.system(i)));
		List<Entry<Integer, Float>> list = new ArrayList<>(homeDistances.entrySet());
		list.sort(Entry.comparingByValue());
		altIndex2SystemIndex.clear();
		Integer altId = 0;
		for (Entry<Integer, Float> entry : list) {
			Integer key = entry.getKey();
			altIndex2SystemIndex.put(altId, key);
			gal.system(key).altId = altId;
			altId++;
		}
	}
	private void initPanels()		{
		mainPanel = initMainPanel();
		livePanel = mainPanel;
		panels.clear();
		panels.add(mainPanel);
		loadPanel = initLoadPanel();
		savePanel = initSavePanel();
	}
	private Panel initSavePanel()	{
		Panel panel = new Panel("Save Panel") {
			private String fileBaseName(String fn) {
				String ext = GameSession.SAVEFILE_EXTENSION;
				if (fn.endsWith(ext)) {
					List<String> parts = substrings(fn, '.');
					if (!parts.get(0).trim().isEmpty()) 
						return fn.substring(0, fn.length()-ext.length());
				}
				return fn;
			}
			@Override public String open(String out) {
				if (!session().status().inProgress()) {
					out += "No game in progress" + newline;
					return mainPanel.open(out);
				}
				String dirPath = UserPreferences.saveDirectoryPath();
				String fileName = GameUI.gameName + GameSession.SAVEFILE_EXTENSION;
				JFileChooser chooser = new JFileChooser();
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(
						"RotP", "rotp"));
				chooser.setCurrentDirectory(new File(dirPath));
				chooser.setSelectedFile(new File(dirPath, fileName));
				int status = chooser.showSaveDialog(null);
				if (status == JFileChooser.APPROVE_OPTION) {
					File rawFile = chooser.getSelectedFile();
					if (rawFile == null) {
						out +=  "No file selected" + newline;
						return mainPanel.open(out);
					}
					GameUI.gameName = fileBaseName(rawFile.getName());
					dirPath = rawFile.getParent();
					fileName = GameUI.gameName + GameSession.SAVEFILE_EXTENSION;
					File file = new File(dirPath, fileName); // Force the correct extension
					// Remove sensitive info that should not be shared in game file
					// (May contains player name)
					RotPUI.currentOptions(IGameOptions.GAME_ID);
					options().prepareToSave(true);
					options().saveOptionsToFile(GAME_OPTIONS_FILE);
					options().saveOptionsToFile(LIVE_OPTIONS_FILE);
					final Runnable save = () -> {
						try {
							GameSession.instance().saveSession(file);
							RotPUI.instance().selectGamePanel();
						}
						catch(Exception e) {
							String str = "Save unsuccessful: " + file.getAbsolutePath() + newline;
							resultPane.setText(mainPanel.open(str));
							return;
						}
					};
					SwingUtilities.invokeLater(save);
					out +=  "Saved to File: " + file.getAbsolutePath() + newline;
					return mainPanel.open(out);
				}
				out +=  "No file selected" + newline + newline;
				return mainPanel.open(out);
			}
		};
		return panel;
	}
	private Panel initLoadPanel()	{
		Panel panel = new Panel("Load Panel") {
			private String fileBaseName(String fn) {
				String ext = GameSession.SAVEFILE_EXTENSION;
				if (fn.endsWith(ext)) {
					List<String> parts = substrings(fn, '.');
					if (!parts.get(0).trim().isEmpty()) 
						return fn.substring(0, fn.length()-ext.length());
				}
				return "";
			}
			@Override public String open(String out) {
				String dirPath = UserPreferences.saveDirectoryPath();
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(dirPath));
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter(
						"RotP", "rotp"));
				int status = chooser.showOpenDialog(null);
				if (status == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					if (file == null) {
						out +=  "No file selected" + newline + newline;
						return mainPanel.open(out);
					}
					GameUI.gameName = fileBaseName(file.getName());
					String dirName = file.getParent();
					final Runnable load = () -> {
						GameSession.instance().loadSession(dirName, file.getName(), false);
					};
					SwingUtilities.invokeLater(load);
					out +=  "File: " + GameUI.gameName + newline;
					return gamePanel.open(out);
				}
				out +=  "No file selected" + newline + newline;
				return mainPanel.open(out);
			}
		};
		return panel;
	}
	private Panel initMainPanel()	{ // TODO BR: complete initMainPanel
		Panel main = new Panel("Main Panel") {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return super.open(out);
			}
		};
		speciesPanel = initSpeciePanel(main);
		setupPanel = initSetupPanels(main);
		main.addPanel(setupPanel);
		gamePanel = initGamePanels(main);
		main.addPanel(gamePanel);
		main.addCommand(new Command("Continue", "C") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Continue"
				if (RotPUI.gameUI().canContinue()) {
					RotPUI.gameUI().continueGame();
					return gamePanel.open("");
				}
				else
					return "Nothing to continue" + newline;
			}
		});
		main.addCommand(new Command("Load File", "L") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Load"
				return loadPanel.open("");
			}
		});
		main.addCommand(new Command("Save File", "S") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Load"
				return savePanel.open("");
			}
		});
		return main;
	}
	private Panel initSetupPanels(Panel parent) {
		Panel panel = new Panel("New Setup Panel", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return speciesPanel.open(out);
			}
		};
		panel.addPanel(speciesPanel);
		return panel;
	}
	private Panel initGamePanels(Panel parent)	{ // TODO BR: initGamePanels
		Panel panel = new Panel("Game Panel", parent);
		panel.addCommand(new Command("Next Turn", "N") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Load"
				session().nextTurn();
				return "Performing Next Turn...";
			}
		});
		panel.addCommand(new Command("View Star Systems [Ship | Scout | Distance]", "VS") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Load"
				resetSystems();
				if (!param.isEmpty()) {
					String cmd = param.remove(0).toUpperCase();
					filterMOList(systems, cmd);
//					Float dist;
//
//					if (cmd.equals("SHIP"))
//						dist = player().shipRange();
//					else if (cmd.equals("SCOUT"))
//						dist = player().scoutRange();
//					else
//						dist = getFloat(cmd);
//					if (dist != null) {
//						filterMOList(starList, dist);
//					}
				}
				sortSystems();
				return viewSystems("");
			}
		});
		introPanel	= initIntroPanel(panel);
		return panel;
	}
	private Panel initSpeciePanel(Panel parent) {
		Panel panel = new Panel("Player Species Panel", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectSetupRacePanel();
				return super.open(out);
			}
		};
		panel.addPanel(initGalaxyPanel(panel));
		panel.addSetting(RotPUI.setupRaceUI().playerSpecies());
		panel.addSetting(RotPUI.setupRaceUI().playerHomeWorld());
		panel.addSetting(RotPUI.setupRaceUI().playerLeader());
		return panel;
	}
	private Panel initGalaxyPanel(Panel parent) {
		Panel panel = new Panel("Galaxy Panel", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectSetupGalaxyPanel();
				return super.open(out);
			}
		};
		panel.addPanel(new Panel("Advanced Options Panel", panel, IAdvOptions.advancedOptions()));
		panel.addSetting(rotp.model.game.IGalaxyOptions.sizeSelection);
		panel.addSetting(rotp.model.game.IPreGameOptions.dynStarsPerEmpire);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeSelection);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeOption1);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeOption2);
		panel.addSetting(rotp.model.game.IGalaxyOptions.difficultySelection);
		panel.addSetting(rotp.model.game.IInGameOptions.customDifficulty);
		panel.addSetting(rotp.model.game.IGalaxyOptions.aliensNumber);
		panel.addSetting(rotp.model.game.IGalaxyOptions.showNewRaces);
		panel.addSetting(RotPUI.setupGalaxyUI().opponentAI);
		panel.addSetting(RotPUI.setupGalaxyUI().globalAbilities);
		panel.addCommand(new Command("Start Game", "go", "start") {
			@Override protected String execute(List<String> param) { // TODO BR: new Command("Start Game", "go", "start")
				RotPUI.setupGalaxyUI().startGame();
				return "";
			}
		});
		return panel;
	}
	private Panel initIntroPanel(Panel parent)	{
		Panel panel = new Panel("Intro Panel", parent) {
			@Override public String open(String out) {
				reInit();
				Empire pl = player();
				List<String> text = pl.race().introduction();
				out = "Intro Panel" + newline + newline;
				for (int i=0; i<text.size(); i++)  {
					String paragraph = text.get(i).replace("[race]", pl.raceName());
					out += paragraph + newline;
				}
				out += newline + "Enter any command to continue";
				livePanel = this;
				resultPane.setText(out);
				return "";
			}
			@Override protected String close(String out) {
				RotPUI.raceIntroUI().finish();
				return gamePanel.open(out);
			}
			@Override protected void newEntry(ActionEvent evt)	{
				resultPane.setText(close(""));
			}
		};
		return panel;
	}

	public CommandConsole() {
		super(new GridBagLayout());
		cmdLabel = new JLabel("Command: ");
		cmdField = new JTextField(80);
		cmdLabel.setLabelFor(cmdField);
		cmdField.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newCommandEntry(evt);
			}
		});
		optLabel = new JLabel("Options: ");
		optField = new JTextField(80);
		optLabel.setLabelFor(optField);
		optField.addActionListener(new java.awt.event.ActionListener() {
			@Override public void actionPerformed(java.awt.event.ActionEvent evt) {
				optionEntry(evt);
			}
		});
		resultLabel = new JLabel("Result: ");
		resultPane = new JTextPane();
		resultLabel.setLabelFor(resultPane);
		resultPane.setEditable(false);
		resultPane.setOpaque(true);
		resultPane.setContentType("text/html");
		resultPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		scrollPane = new JScrollPane(resultPane);

		//Add Components to this panel.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(optLabel);
		add(optField, c);
		add(cmdLabel);
		add(cmdField, c);

		add(resultLabel);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		setPreferredSize(new Dimension(800, 600));
		resultPane.setContentType("text/html");
		resultPane.setText("<html>");
		initPanels();
		resultPane.setText(livePanel.panelGuide(""));
	}

	// ##### EVENTS METHODES #####
	@Override public void actionPerformed(ActionEvent evt)	{ }
	private void optionEntry(ActionEvent evt)	{ livePanel.newEntry(evt); }

	private String optsGuide()	{
		String out = "";
		out += newline + "Empty: list availble settings";
		out += newline + "O: list all available options";
		out += newline + "O INDEX: select chosen option";
		out += newline + "O+: select next option";
		out += newline + "O-: select previous option";
		out += newline + "S: list all available settings";
		out += newline + "S INDEX: select chosen setting";
		out += newline + "S+: next setting";
		out += newline + "S-: previous setting";
		out += newline + "P: list all available panels";
		out += newline + "P INDEX: select chosen panel";
		out += newline + "P+: next panel";
		out += newline + "P-: previous panel";
		return out;
	}
	private String cmdGuide()	{
		String out = "";
		out += newline + "new: create a new setup";
		return out;
	}
	private void newCommandEntry(ActionEvent evt) {
		List<String> param = new ArrayList<>();
		String txt = cmdField.getText();
		String cmd = getParam(txt, param);
		String out = "Command = " + txt + newline;
		switch (cmd.toUpperCase()) {
			case ""		:
			case "?"	: out += cmdGuide();	break;
			case "CLS"	: out = "";				break;
			case "NEW"	: out = setupPanel.open(out);		break;
			default:
				out += "? unrecognised command";
				resultPane.setText(out);
				return;
		}
		cmdField.setText("");
		resultPane.setText(out);
	}
	// ##### Tools
//	private void newStarList()	{
//		starList.clear();
//		SystemInfo sv = player().sv;
//		int size = galaxy().systemCount;
//		for(int i=0; i<size; i++) {
//			starList.add(sv.view(altIndex2SystemIndex.get(i)));
//		}
//	}
	private void sortSystems()	{ systems.sort((s1, s2) -> s1.altId-s2.altId); }
//	private void sortStarList()	{ starList.sort((s1, s2) -> s1.system().altId-s2.system().altId); }
//	private void filterMOList(List<? extends IMappedObject> list, List<String> filters)	{
//		if (list.isEmpty() || filters.isEmpty())
//			return;
//		for (String filter : filters)
//			filterMOList(list, filter);
//	}
	private void filterMOList(List<? extends IMappedObject> list, String filter)	{
		if (list.isEmpty() || filter.isEmpty())
			return;
		Integer opponent = null; // none
		String c1, c2, se;
		Float dist	= null;
		Float range	= null;
		Empire pl = player();
		c1 = filter.substring(0, 1).toUpperCase();
		if (filter.length()>1) {
			c2 = filter.substring(1, 2);
			se = filter.substring(1);
		} else {
			c2 = "";
			se = "";			
		}
		switch (c1) {
		case "S": // Ship or scout range
			if (c2.equals("H"))
				range = pl.shipRange();
			else
				range = pl.scoutRange();
			break;
		case "R": // Range value
			range = getFloat(se);
			break;
		case "D": // Distance value
			dist = getFloat(se);
			break;
		case "P": // Player only
			opponent = 0;
			break;
		case "O": // Selected Opponent
			opponent = getInteger(se);
			break;			
		}
		if (range != null) {
			List<? extends IMappedObject> copy = new ArrayList<>(list);
			for (IMappedObject imo : copy) {
				if (pl.distanceTo(imo) > range)
					list.remove(imo);
			}
			return;
		}
		else if (dist != null) {
			StarSystem ref = getSys(selectedStar);
			List<? extends IMappedObject> copy = new ArrayList<>(list);
			for (IMappedObject imo : copy) {
				if (ref.distanceTo(imo) > dist)
					list.remove(imo);
			}
			return;
		}
		else if (opponent != null) {
			StarSystem ref = getSys(selectedStar);
			List<? extends IMappedObject> copy = new ArrayList<>(list);
			for (IMappedObject imo : copy) {
				if (ref.distanceTo(imo) > dist)
					list.remove(imo);
			}
			return;
		}
	}
//	private String viewStarList(String out)	{
//		if (starList.isEmpty())
//			return out + "Empty Star System List" + newline;
//		StarSystem ref = getSys(selectedStar);
//		for (SystemView s : starList) {
//			StarSystem sys = s.system();
//			out += "(" + sys.altId + ")";
//			out += " " + ly(ref.distanceTo(sys));
//			out += " " + s.descriptiveName(); 
//			out += newline;	
//		}
//		return out;
//	}
	private String viewSystems(String out)	{
		if (systems.isEmpty())
			return out + "Empty Star System List" + newline;
		StarSystem ref = getSys(selectedStar);
		Empire pl = player();
		SystemInfo sv = pl.sv;
		for (StarSystem sys : systems) {
			SystemView view = sv.view(sys.id);
			out += "(" + sys.altId + ")";
			out += " " + ly(ref.distanceTo(sys));
			out += " " + view.descriptiveName(); 
			out += newline;	
		}
		return out;
	}
	private void resetSystems()		{
		systems.clear();
		systems.addAll(Arrays.asList(galaxy().starSystems()));
	}
	private void resetTransports()	{
		transports.clear();
		transports.addAll(player().opponentsTransports());
	}
	private void resetFleets()		{
		fleets.clear();
		fleets.addAll(player().allFleets());
	}

	private String getParam (String input, List<String> param) {
		String[] text = input.trim().split("\\s+");
		for (int i=1; i<text.length; i++)
			param.add(text[i]);
		param.add(""); // to avoid empty list!
		return text[0];
	}
	private Integer getInteger(String text)	{
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private Float getFloat(String text)		{
		try {
			return Float.parseFloat(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	private StarSystem getSys(int altIdx)	{ return galaxy().system(altIndex2SystemIndex.get(altIdx)); }
	private SystemView getView(int altIdx)	{ return player().sv.view(altIndex2SystemIndex.get(altIdx)); }
	private String ly (float dist)			{ return String.format("%.1f ly", dist); }

	private static void createAndShowGUI(boolean show)	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (frame == null) {
					//Create and set up the window.
					frame = new JFrame("Command Console");
					frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

					//Add contents to the window.
					instance = new CommandConsole();
					frame.add(instance);

					//Display the window.
					frame.pack();
					frame.setVisible(show);
				}
			}
		});
	}
	public static void showConsole(boolean show)		{
		if (frame == null) {
			if (show)
				createAndShowGUI(show);
			return;
		}
		else
			frame.setVisible(show);
	}
	public static void hideConsole()		{ showConsole(false); }
	public static void main(String[] args)	{
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				createAndShowGUI(true);
			}
		});
	}
	// ################### SUB CLASS PANEL ######################
	public class Panel {
		private final String panelName;
		private final Panel parent;
		private final List<IParam> settings	 = new ArrayList<>();
		private final List<Panel> subPanels	 = new ArrayList<>();
		private final List<Command> commands = new ArrayList<>();
		private int lastList = NULL_ID;
		private IParam liveSetting;

		// ##### CONSTRUCTORS #####
		Panel(String name)	{
			parent = this;
			panelName = name;
		} // For the main panel
		Panel(String name, Panel parent)	{
			this.parent = parent;
			panelName = name;
			addPanel(parent);
		}
		Panel(String name, Panel parent, ParamSubUI ui)		{ this(name, parent, ui.optionsList); }
		Panel(String name, Panel parent, List<IParam> src)	{
			this(name, parent);
			for (IParam p : src)
				if (p != null)
					if (p.isSubMenu()) {
						ParamSubUI ui = (ParamSubUI) p;
						String uiName = text(ui.titleId());
						subPanels.add(new Panel(uiName, this, ui));
					}
					else
						settings.add(p);
		}
		// #####  #####
		public String open(String out)		{
			livePanel = this;
			return panelGuide(out);
		}
		protected String close(String out)		{ return parent.open(out); }
		private void addPanel(Panel panel)		{ subPanels.add(panel); }
		private void addSetting(IParam setting)	{ settings.add(setting); }
		private void addCommand(Command cmd)	{ commands.add(cmd); }
		protected void newEntry(ActionEvent evt)	{
			List<String> param = new ArrayList<>();
			String txt = ((JTextField) evt.getSource()).getText();
			String cmd = getParam(txt, param);
			String out = "Command = " + txt + newline;
			for (Command c : commands) {
				if (c.isKey(cmd)) {
					out += c.execute(param);
					optField.setText("");
					resultPane.setText(out);
					return;
				}
			}
			switch (cmd.toUpperCase()) {
				case ""		: out = panelGuide(out);	break;
				case "?"	: out = optsGuide();	break;
				case "CLS"	: out = "";				break;
				case "UP"	:
					optField.setText("");
					close(out);
					return;
				case "O"	: out = optionEntry(out, param.remove(0), param);	break;
				case "O+"	: out = optionEntry(out, "+", param);	break;
				case "O-"	: out = optionEntry(out, "-", param);	break;
				case "O*"	: out = optionEntry(out, "*", param);	break;
				case "O="	: out = optionEntry(out, "=", param);	break;
				case "S"	: out = settingEntry(out, param.remove(0), param);	break;
				case "S+"	: out = settingEntry(out, "+", param);	break;
				case "S-"	: out = settingEntry(out, "-", param);	break;
				case "S*"	: out = settingEntry(out, "*", param);	break;
				case "S="	: out = settingEntry(out, "=", param);	break;
				case "P"	: out = panelEntry(out, param.remove(0), param);	break;
				case "P+"	: out = panelEntry(out, "+", param);	break;
				case "P-"	: out = panelEntry(out, "-", param);	break;
				case "P*"	: out = panelEntry(out, "*", param);	break;
				case "P="	: out = panelEntry(out, "=", param);	break;
				default	:
					switch (lastList) {
					case OPTION_ID	: out = optionSel(out, cmd);	break;
					case SETTING_ID	: out = settingSel(out, cmd);	break;
					case PANEL_ID	: out = panelSel(out, cmd);		break;
					case NULL_ID	:
					default	:
						out += "? unrecognised command";
						resultPane.setText(out);
						return;
					}
			}
			optField.setText("");
			resultPane.setText(out);
		}
		private String panelEntry(String out, String cmd, List<String> p) {
			switch (cmd.toUpperCase()) {
				case ""	:
				case "?":
				case "*": return panelList(out);
				case "+": return panelNext(out);
				case "-": return panelPrev(out);
				case "=": return panelSel(out, p.get(0));
				default	:
					return panelSel(out, cmd);
			}
		}
		private String settingEntry(String out, String cmd, List<String> p) {
			switch (cmd.toUpperCase()) {
				case ""	:
				case "?":
				case "*": return settingList(out);
				case "+": return settingNext(out);
				case "-": return settingPrev(out);
				case "=": return settingSel(out, p.get(0));
				default	:
					return settingSel(out, cmd);
			}
		}
		private String optionEntry(String out, String cmd, List<String> p) {
			switch (cmd.toUpperCase()) {
				case ""	:
				case "?":
				case "*": return optionList(out);
				case "+": return optionNext(out);
				case "-": return optionPrev(out);
				case "=": return optionSel(out, p.get(0));
				default	:
					return optionSel(out, cmd);
			}
		}
		// Panels methods
		private String panelPrev(String out) { return close(out); }
		private String panelNext(String out) {
			List<Panel> list = parent.subPanels;
			int index = list.indexOf(this) + 1;
			if (index >= list.size())
				return close(out); // Return to parent
			return list.get(index).open(out);
		}
		private String panelSel(String out, int index) {
			if (index < 0)
				return out + " ? Should not be negative";
			if (index >= subPanels.size())
				return out + " ? Index to high! max = " + (subPanels.size()-1);
			return subPanels.get(index).open(out);
		}
		private String panelSel(String out, String param) {
			if (param == null || param.isEmpty())
				return panelGuide(out); // No parameters = ask for help
			Integer number = getInteger(param);
			if (number == null)
				return "? Invalid parameter" + newline + panelGuide(out);
			return panelSel(out, number);
		}
		private String panelList(String out) {
			if (subPanels.size() == 0)
				return "? No panel list available";
			out += "Panel List: " + newline;
			int i=0;
			for (Panel p: subPanels) {
				out += "(P " + i + ") " + p.panelName + newline;
				i++;
			}
			lastList = PANEL_ID;
			return out;
		}
		private String panelGuide(String out) {
			out += "Current Panel: ";
			out += panelName + newline;
			out = commandGuide(out);			
			out = panelList(out);
			return settingGuide(out);
		}
		// Settings methods
		private String settingPrev(String out) {
			int index = settings.indexOf(liveSetting) - 1;
			if (index < 0)
				index = settings.size()-1;
			return settingSel(out, index);
		}
		private String settingNext(String out) {
			int index = settings.indexOf(liveSetting) + 1;
			if (index >= settings.size())
				index = 0;
			return settingSel(out, index);
		}
		private String settingSel(String out, String param) {
			if (param == null || param.isEmpty())
				return settingGuide(out);
			Integer number = getInteger(param);
			if (number == null)
				return settingGuide(out+ "? Invalid parameter" + newline);
			return settingSel(out, number);
		}
		protected String settingSel(String out, IParam option) {
			liveSetting = option;
			return optionGuide(out);
		}
		private String settingSel(String out, int index) {
			if (index < 0)
				return out + " ? Should not be negative";
			if (index >= settings.size())
				return out + " ? Index to high! max = " + (subPanels.size()-1);
			return settingSel(out, settings.get(index));
		}
		private String settingList(String out) {
			out +=  "Setting list:";
			for (int i=0; i<settings.size(); i++) {
				IParam setting = settings.get(i);
				if (setting.isActive()) {
					out += newline + "( S " + i + ") ";
					out += setting.getGuiDisplay();
					out += ": ";
					out += setting.getDescription();					
				}
			}
			lastList = SETTING_ID;
			return out;
		}
		protected String settingGuide(String out) { return settingList(out); }
		// Options methods
		private String optionSel(String out, String param) {
			if (param == null || param.isEmpty())
				return optionGuide(out);
			if (liveSetting instanceof ParamList) {
				Integer number = getInteger(param);
				if (number != null)
					((ParamList)liveSetting).setFromIndex(number);
				else
					((ParamList)liveSetting).set(param);
			}
			else if (liveSetting instanceof ParamInteger) {
				Integer number = getInteger(param);
				if (number != null)
					((ParamInteger)liveSetting).set(number);
				else
					return out;
			}
			else if (liveSetting instanceof ParamFloat) {
				Float number = getFloat(param);
				if (number != null)
					((ParamFloat)liveSetting).set(number);
				else
					return out + "? Float expected";
			}
			else if (liveSetting instanceof ParamString) {
				((ParamString)liveSetting).set(param);
			}
			else if (liveSetting instanceof ParamBoolean) {
				liveSetting.setFromCfgValue(param);
			}
			else
				return out + "? Something wrong";
			return optionGuide(out);
		}
		private String optionPrev(String out) {
			if (liveSetting == null)
				return "? No selected option";
			liveSetting.prev();
			return optionGuide(out);
		}
		private String optionNext(String out) {
			if (liveSetting == null)
				return "? No selected option";
			liveSetting.next();
			return optionGuide(out);
		}
		private String optionList(String out) {
			if (liveSetting == null)
				return "? No selected option";
			lastList = OPTION_ID;
			return out + liveSetting.getFullHelp();
		}
		private String optionGuide(String out) {
			if (liveSetting == null)
				return "? No selected option";
			out += "Current Setting: " + newline;
			out += liveSetting.getHelp();
			out += newline + liveSetting.selectionGuide();
			out += newline + newline;
			return optionList(out);
		}
		// Command methods
		private String commandGuide(String out) {
			for (Command cmd : commands) {
				out += cmd.getGuide() + newline;
			}
			return out;
		}
	}
	// ################### SUB CLASS COMMAND ######################
	private class Command {
		private final List <String> keyList = new ArrayList<>();
		private final String description;
		protected String execute(List<String> param) { return "Unimplemented command!" + newline; }
		private Command(String descr, String... keys) {
			description = descr;
			for (String key : keys)
				keyList.add(key.toUpperCase());
		}
		private boolean isKey(String str) { return keyList.contains(str.toUpperCase()); }
		private String getKey() { return keyList.get(0);}
		private String getGuide() { 
			String out = "(";
			out += getKey();
			out += ") ";
			out += description;
			return out;
		}
	}
	// ################### SUB CLASS COMMAND ######################
	private class MO_Filter {
		private List<Integer> opponent = new ArrayList<>();
		private Boolean colonized = null;
		private Boolean explored  = null;
		private Float dist	= null;
		private Float range	= null;
		private	Empire pl	= player();
		private List<String> filterList;

		private MO_Filter(List<String> filters) {
			filterList = filters;
			if (filters.isEmpty())
				return;
			for (String filter : filters)
				filterMOList(filter);
		}
		private void filterMOList(String filter)	{
			String c1, c2, se;
			c1 = filter.substring(0, 1).toUpperCase();
			if (filter.length()>1) {
				c2 = filter.substring(1, 2);
				se = filter.substring(1);
			} else {
				c2 = "";
				se = "";			
			}
			switch (c1) {
			case "S": // Ship or scout range
				if (c2.equals("H"))
					range = pl.shipRange();
				else
					range = pl.scoutRange();
				break;
			case "R": // Range value
				range = getFloat(se);
				break;
			case "D": // Distance value
				dist = getFloat(se);
				break;
			case "Y": // Player only
				opponent.add(0);
				break;
			case "O": // Selected Opponent
				if (se.isEmpty()) { // All opponents
					for (Empire e : galaxy().activeEmpires())
						if (!e.isPlayer())
							opponent.add(e.id);
				}
				else if (se.equals("W")) { // At war with
					for (Empire e : pl.warEnemies())
						opponent.add(e.id);
				}
				else { // Specified opponents
					Integer opp = getInteger(se);
					if (opp != null)
						opponent.add(opp);
				}
				break;			
			case "W": // At war with
				for (Empire e : pl.warEnemies())
					opponent.add(e.id);
				break;			
			case "U": // Unexplored
				explored = false;
				break;			
			case "X": // explored
				explored = true;
				break;			
			case "N": // Uncolonized
				colonized = false;
				break;			
			case "C": // colonized
				colonized = true;
				break;			
			}
		}
		private void filterSystems()	{
			resetSystems();
			filterMOList(systems);
			if (!opponent.isEmpty()) {
				List<StarSystem> copy = new ArrayList<>(systems);
				for (StarSystem sys : copy)
					if (!opponent.contains(sys.empId()))
						systems.remove(sys);
			}
		}
		private void filterFleets()	{
			resetFleets();
			filterMOList(fleets);
			if (!opponent.isEmpty()) {
				List<ShipFleet> copy = new ArrayList<>(fleets);
				for (ShipFleet sf : copy)
					if (!opponent.contains(sf.empId()))
						fleets.remove(sf);
			}
		}
		private void filterTransports()	{
			resetTransports();
			filterMOList(transports);
			if (!opponent.isEmpty()) {
				List<Transport> copy = new ArrayList<>(transports);
				for (Transport tr : copy)
					if (!opponent.contains(tr.empId()))
						transports.remove(tr);
			}
		}

		private void filterMOList(List<? extends IMappedObject> list)	{
			if (range != null) {
				List<? extends IMappedObject> copy = new ArrayList<>(list);
				for (IMappedObject imo : copy)
					if (pl.distanceTo(imo) > range)
						list.remove(imo);
			}
			if (dist != null) {
				StarSystem ref = getSys(selectedStar);
				List<? extends IMappedObject> copy = new ArrayList<>(list);
				for (IMappedObject imo : copy)
					if (ref.distanceTo(imo) > dist)
						list.remove(imo);
			}
		}
	}
}

