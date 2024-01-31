package rotp.ui.console;

import static rotp.model.game.IBaseOptsTools.GAME_OPTIONS_FILE;
import static rotp.model.game.IBaseOptsTools.LIVE_OPTIONS_FILE;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import rotp.model.game.IInGameOptions;
import rotp.model.game.IMainOptions;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.main.MainUI;
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
	private static final int MENU_ID	= 2;
	private static JFrame frame;
	private static CommandConsole instance;
	public	static Menu introMenu, loadMenu, saveMenu;

	private final JLabel commandLabel, resultLabel;
	private final JTextField commandField;
	private final JTextPane resultPane;
	private final JScrollPane scrollPane;
	private final List<Menu>		menus		= new ArrayList<>();
	private final List<Transport>	transports	= new ArrayList<>();
	private final List<ShipFleet>	fleets		= new ArrayList<>();
	private final List<StarSystem>	systems		= new ArrayList<>();
	private final LinkedList<String> lastCmd	= new LinkedList<>();
	private Menu liveMenu;
	private Menu mainMenu, setupMenu, gameMenu, speciesMenu;
	int selectedStar; //, destStar, selectedEmpire, selectedFleet, selectedTransport, selectedDesign;
	private HashMap<Integer, Integer> altIndex2SystemIndex = new HashMap<>();
//	private Menu stars, fleet, ships, opponents;
//	private final List<SystemView> starList = new ArrayList<>();
	private StarView starView;
	
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
	private Command initContinue()	{
		Command cmd = new Command("Continue", "C") {
			@Override protected String execute(List<String> param) {
				if (RotPUI.gameUI().canContinue()) {
					RotPUI.gameUI().continueGame();
					return gameMenu.open("");
				}
				else
					return "Nothing to continue" + newline;
			}
		};
		cmd.cmdHelp("Continue the current game. If none are started, continue with the last saved game.");
		return cmd;
	}
	private Command initLoadFile()	{
		Command cmd = new Command("Load File", "L") {
			@Override protected String execute(List<String> param) {
				return loadMenu.open("");
			}
		};
		cmd.cmdHelp("Open a standard file chooser to load a previously saved game.");
		return cmd;
	}
	private Command initSaveFile()	{
		Command cmd = new Command("Save File", "S") {
			@Override protected String execute(List<String> param) {
				return saveMenu.open("");
			}
		};
		cmd.cmdHelp("Open a standard file chooser to save the current game.");
		return cmd;
	}
	private Command initStartGame()	{
		Command cmd = new Command("Start Game", "go", "start") {
			@Override protected String execute(List<String> param) {
				RotPUI.setupGalaxyUI().startGame();
				return "";
			}
		};
		cmd.cmdHelp(text("SETUP_BUTTON_START_DESC"));
		return cmd;
	}
	private Command initNextTurn()	{
		Command cmd = new Command("Next Turn", "N") {
			@Override protected String execute(List<String> param) {
				session().nextTurn();
				return "Performing Next Turn...";
			}
		};
		cmd.cmdHelp("Next Turn");
		return cmd;
	}
	private Command initView()		{
		Command cmd = new Command("View planets & fleets", "V") {
			@Override protected String execute(List<String> param) {
				ViewFilter filter = new ViewFilter(this, param);
				return filter.getResult("");
			}
		};
		cmd.cmdParam(" [SHip|SCout|Rxx|Dxx] [Y|O|Oxx|W] [A|U|X|N|C] [P|F|T]");
		cmd.cmdHelp("Loop thru all the given filters and return the result"
				+ newline + "Distance filters:"
				+ newline + "[SH | Ship] : filter in ship range of the player empire"
				+ newline + "[SC | Scout] : filter in scout range of the player empire"
				+ newline + "[Rxx] : filter in xx light years Range of the player empire"
				+ newline + "[Dxx] : filter in xx light years Distance of the selected star system"
				+ newline + "Owner filters: if none all are displayed"
				+ newline + "[Y] : add plaYer"
				+ newline + "[O] : add all Opponents"
				+ newline + "[Oxx] : add Opponent xx (Index)"
				+ newline + "[W | OW] : add Opponents at War with the player"
				+ newline + "Category filters:"
				+ newline + "[A] : planets under Attack"
				+ newline + "[U] : Unexplored star system only"
				+ newline + "[X] : eXplored star system only"
				+ newline + "[N] : uNcolonized star system only"
				+ newline + "[C] : Colonized star system only"
				+ newline + "List filters: if none, all three lists are shown"
				+ newline + "[P] : add Planet list (star systems)"
				+ newline + "[F] : add Fleet list"
				+ newline + "[T] : add Transport list"
				);
		return cmd;
	}
	private Command initPlanet()	{ // TODO BR: Display planet
		Command cmd = new Command("select Planet from index", "P") {
			@Override protected String execute(List<String> param) {
				if (param.isEmpty())
					return getLineGuide();
				String s = param.get(0);
				Integer p = getInteger(s);
				if (p == null)
					return getLineGuide();
				selectedStar = validPlanet(p);
				StarSystem sys = getSys(selectedStar);
				mainUI().selectSystem(sys);
				return starView.planetInfo(selectedStar, "");
				//return planetInfo(selectedStar, "");
			}
		};
		cmd.cmdParam(" Index");
		cmd.cmdHelp("Select current Planet from planet index and show info");
		return cmd;		
	}

	private	void reInit()			{ initAltIndex(); }
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
	private void initMenus()		{
		mainMenu = initMainMenu();
		liveMenu = mainMenu;
		menus.clear();
		menus.add(mainMenu);
		loadMenu = initLoadMenu();
		saveMenu = initSaveMenu();
	}
	private Menu initSaveMenu()		{
		Menu menu = new Menu("Save Menu") {
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
					return mainMenu.open(out);
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
						return mainMenu.open(out);
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
							resultPane.setText(mainMenu.open(str));
							return;
						}
					};
					SwingUtilities.invokeLater(save);
					out +=  "Saved to File: " + file.getAbsolutePath() + newline;
					return mainMenu.open(out);
				}
				out +=  "No file selected" + newline + newline;
				return mainMenu.open(out);
			}
		};
		return menu;
	}
	private Menu initLoadMenu()		{
		Menu menu = new Menu("Load Menu") {
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
						return mainMenu.open(out);
					}
					GameUI.gameName = fileBaseName(file.getName());
					String dirName = file.getParent();
					final Runnable load = () -> {
						GameSession.instance().loadSession(dirName, file.getName(), false);
					};
					SwingUtilities.invokeLater(load);
					out +=  "File: " + GameUI.gameName + newline;
					return gameMenu.open(out);
				}
				out +=  "No file selected" + newline + newline;
				return mainMenu.open(out);
			}
		};
		return menu;
	}
	private Menu initMainMenu()		{
		Menu main = new Menu("Main Menu") {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return super.open(out);
			}
		};
		main.addMenu(new Menu("Global Settings Menu", main, IMainOptions.commonOptions()));
		speciesMenu = initSpecieMenu(main);
		setupMenu = initSetupMenus(main);
		main.addMenu(setupMenu);
		gameMenu = initGameMenus(main);
		main.addMenu(gameMenu);
		main.addCommand(initContinue()); // C
		main.addCommand(initLoadFile()); // L
		main.addCommand(initSaveFile()); // S
		return main;
	}
	private Menu initSetupMenus(Menu parent) {
		Menu menu = new Menu("New Setup Menu", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return speciesMenu.open(out);
			}
		};
		menu.addMenu(speciesMenu);
		return menu;
	}
	private Menu initGameMenus(Menu parent)	 { // TODO BR: initGameMenus
		Menu menu = new Menu("Game Menu", parent);
		menu.addMenu(new Menu("In Game Settings Menu", menu, IInGameOptions.inGameOptions()));
		menu.addCommand(initNextTurn());	// N
		menu.addCommand(initView());		// V
		menu.addCommand(initPlanet());		// P
		introMenu = initIntroMenu(menu);
		return menu;
	}
	private Menu initSpecieMenu(Menu parent) {
		Menu menu = new Menu("Player Species Menu", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectSetupRacePanel();
				return super.open(out);
			}
		};
		menu.addMenu(initGalaxyMenu(menu));
		menu.addSetting(RotPUI.setupRaceUI().playerSpecies());
		menu.addSetting(RotPUI.setupRaceUI().playerHomeWorld());
		menu.addSetting(RotPUI.setupRaceUI().playerLeader());
		return menu;
	}
	private Menu initGalaxyMenu(Menu parent) {
		Menu menu = new Menu("Galaxy Menu", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectSetupGalaxyPanel();
				return super.open(out);
			}
		};
		menu.addMenu(new Menu("Advanced Options Menu", menu, IAdvOptions.advancedOptions()));
		menu.addSetting(rotp.model.game.IGalaxyOptions.sizeSelection);
		menu.addSetting(rotp.model.game.IPreGameOptions.dynStarsPerEmpire);
		menu.addSetting(rotp.model.game.IGalaxyOptions.shapeSelection);
		menu.addSetting(rotp.model.game.IGalaxyOptions.shapeOption1);
		menu.addSetting(rotp.model.game.IGalaxyOptions.shapeOption2);
		menu.addSetting(rotp.model.game.IGalaxyOptions.difficultySelection);
		menu.addSetting(rotp.model.game.IInGameOptions.customDifficulty);
		menu.addSetting(rotp.model.game.IGalaxyOptions.aliensNumber);
		menu.addSetting(rotp.model.game.IGalaxyOptions.showNewRaces);
		menu.addSetting(RotPUI.setupGalaxyUI().opponentAI);
		menu.addSetting(RotPUI.setupGalaxyUI().globalAbilities);
		menu.addCommand(initStartGame());
		return menu;
	}
	private Menu initIntroMenu(Menu parent)	 {
		Menu menu = new Menu("Intro Menu", parent) {
			@Override public String open(String out) {
				reInit();
				Empire pl = player();
				List<String> text = pl.race().introduction();
				out = "Intro Menu" + newline + newline;
				for (int i=0; i<text.size(); i++)  {
					String paragraph = text.get(i).replace("[race]", pl.raceName());
					out += paragraph + newline;
				}
				out += newline + "Enter any command to continue";
				liveMenu = this;
				resultPane.setText(out);
				return "";
			}
			@Override protected String close(String out) {
				RotPUI.raceIntroUI().finish();
				return gameMenu.open(out);
			}
			@Override protected void newEntry(String entry)	{
				resultPane.setText(close(""));
			}
		};
		return menu;
	}

	private void previousCmd() 	{
		if (lastCmd.isEmpty())
			return;
		String txt = commandField.getText().toUpperCase();
		int idx = lastCmd.indexOf(txt)-1;
		if (idx<0)
			idx = lastCmd.size()-1;
		commandField.setText(lastCmd.get(idx));
	}
	private void nextCmd()		{
		if (lastCmd.isEmpty())
			return;
		String txt = commandField.getText().toUpperCase();
		int idx = lastCmd.indexOf(txt);
		if (idx<0)
			return;
		idx = min(idx+1, lastCmd.size()-1);
		commandField.setText(lastCmd.get(idx));
	}
	public CommandConsole()		{
		super(new GridBagLayout());
		commandLabel = new JLabel("Options: ");
		commandField = new JTextField(80);
		commandField.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) { }
			@Override public void keyReleased(KeyEvent e) {
				int code = e.getKeyCode();
				switch(code) {
					case KeyEvent.VK_UP:
						previousCmd();
						break;
					case KeyEvent.VK_DOWN:
						nextCmd();
						break;
				}
			}
			@Override public void keyPressed(KeyEvent e) { }
		});
		commandLabel.setLabelFor(commandField);
		commandField.addActionListener(new java.awt.event.ActionListener() {
			@Override public void actionPerformed(java.awt.event.ActionEvent evt) {
				commandEntry(evt);
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

		//Add Components to this menu.
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = GridBagConstraints.REMAINDER;

		c.fill = GridBagConstraints.HORIZONTAL;
		add(commandLabel);
		add(commandField, c);

		add(resultLabel);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);

		setPreferredSize(new Dimension(800, 600));
		resultPane.setContentType("text/html");
		resultPane.setText("<html>");
		initMenus();
		resultPane.setText(liveMenu.menuGuide(""));

		starView = new StarView(this);
		instance = this;
	}

	private MainUI mainUI()	  { return RotPUI.instance().mainUI(); }
	// ##### EVENTS METHODES #####
	@Override public void actionPerformed(ActionEvent evt)	{ }
	private void commandEntry(ActionEvent evt)	{ liveMenu.newEntry(((JTextField) evt.getSource()).getText()); }

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
		out += newline + "M: list all available menus";
		out += newline + "M INDEX: select chosen menu";
		out += newline + "M+: next menu";
		out += newline + "M-: previous menu";
		return out;
	}
	// ##### Tools
//	private String cLn(String s)	{ return s.isEmpty() ? "" : (newline + s); }
	private int validPlanet(int p)	{ return bounds(0, p, galaxy().systemCount-1); }
	private void sortSystems()		{ systems.sort((s1, s2) -> s1.altId-s2.altId); }
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
		fleets.addAll(player().getVisibleFleet());
	}
	private String getParam (String input, List<String> param) {
		String[] text = input.trim().split("\\s+");
		for (int i=1; i<text.length; i++)
			param.add(text[i]);
		//param.add(""); // to avoid empty list!
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
	StarSystem getSys(int altIdx)	{ return galaxy().system(altIndex2SystemIndex.get(altIdx)); }
	SystemView getView(int altIdx)	{ return player().sv.view(altIndex2SystemIndex.get(altIdx)); }
	private String ly (float dist)	{ return text("SYSTEMS_RANGE", df1.format(Math.ceil(10*dist)/10)); }
	private static void createAndShowGUI(boolean show)	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (frame == null) {
					//Create and set up the window.
					frame = new JFrame("Command Console");
					frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

					//Add contents to the window.
					frame.add(new CommandConsole());

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
	// ################### SUB CLASS MENU ######################
	public class Menu {
		private final String menuName;
		private final Menu parent;
		private final List<IParam> settings	 = new ArrayList<>();
		private final List<Menu>   subMenus	 = new ArrayList<>();
		private final List<Command> commands = new ArrayList<>();
		private int lastList = NULL_ID;
		private IParam liveSetting;

		// ##### CONSTRUCTORS #####
		Menu(String name)	{
			parent = this;
			menuName = name;
		} // For the main menu
		Menu(String name, Menu parent)	{
			this.parent = parent;
			menuName = name;
			addMenu(parent);
		}
		Menu(String name, Menu parent, ParamSubUI ui)		{ this(name, parent, ui.optionsList); }
		Menu(String name, Menu parent, List<IParam> src)	{
			this(name, parent);
			for (IParam p : src)
				if (p != null)
					if (p.isSubMenu()) {
						ParamSubUI ui = (ParamSubUI) p;
						String uiName = text(ui.titleId());
						subMenus.add(new Menu(uiName, this, ui));
					}
					else
						settings.add(p);
		}
		// #####  #####
		public String open(String out)		{
			liveMenu = this;
			return menuGuide(out);
		}
		protected String close(String out)		{ return parent.open(out); }
		private void addMenu(Menu menu)			{ subMenus.add(menu); }
		private void addSetting(IParam setting)	{ settings.add(setting); }
		private void addCommand(Command cmd)	{ commands.add(cmd); }
		protected void newEntry(String entry)	{
			List<String> param = new ArrayList<>();
			String txt = entry.trim().toUpperCase();
			lastCmd.remove(txt); // To keep unique and at last position
			if (!txt.isEmpty())
				lastCmd.add(txt);
			String cmd = getParam(txt, param); // this will remove the cmd from param list
			String out = "Command = " + txt + newline;
			for (Command c : commands) {
				if (c.isKey(cmd)) {
					if (param.contains("?"))
						out += c.cmdHelp();
					else
						out += c.execute(param);
					commandField.setText("");
					resultPane.setText(out);
					return;
				}
			}
			switch (cmd) {
				case ""		: out = menuGuide(out);	break;
				case "?"	: out = optsGuide();	break;
				case "CLS"	: out = "";				break;
				case "UP"	:
					commandField.setText("");
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
				case "M"	: out = menuEntry(out, param.remove(0), param);	break;
				case "M+"	: out = menuEntry(out, "+", param);	break;
				case "M-"	: out = menuEntry(out, "-", param);	break;
				case "M*"	: out = menuEntry(out, "*", param);	break;
				case "M="	: out = menuEntry(out, "=", param);	break;
				default	:
					switch (lastList) {
					case OPTION_ID	: out = optionSelect(out, cmd);	break;
					case SETTING_ID	: out = settingSelect(out, cmd);	break;
					case MENU_ID	: out = menuSelect(out, cmd);		break;
					case NULL_ID	:
					default	:
						out += "? unrecognised command";
						resultPane.setText(out);
						return;
					}
			}
			commandField.setText("");
			resultPane.setText(out);
		}
		private String menuEntry(String out, String cmd, List<String> p) {
			switch (cmd) {
				case ""	:
				case "?":
				case "*": return menuList(out);
				case "+": return menuNext(out);
				case "-": return menuPrev(out);
				case "=": return menuSelect(out, p.get(0));
				default	:
					return menuSelect(out, cmd);
			}
		}
		private String settingEntry(String out, String cmd, List<String> p) {
			switch (cmd	) {
				case ""	:
				case "?":
				case "*": return settingList(out);
				case "+": return settingNext(out);
				case "-": return settingPrev(out);
				case "=": return settingSelect(out, p.get(0));
				default	:
					return settingSelect(out, cmd);
			}
		}
		private String optionEntry(String out, String cmd, List<String> p) {
			switch (cmd	) {
				case ""	:
				case "?":
				case "*": return optionList(out);
				case "+": return optionNext(out);
				case "-": return optionPrev(out);
				case "=": return optionSelect(out, p.get(0));
				default	:
					return optionSelect(out, cmd);
			}
		}
		// Menus methods
		private String menuPrev(String out) { return close(out); }
		private String menuNext(String out) {
			List<Menu> list = parent.subMenus;
			int index = list.indexOf(this) + 1;
			if (index >= list.size())
				return close(out); // Return to parent
			return list.get(index).open(out);
		}
		private String menuSelect(String out, int index) {
			if (index < 0)
				return out + " ? Should not be negative";
			if (index >= subMenus.size())
				return out + " ? Index to high! max = " + (subMenus.size()-1);
			return subMenus.get(index).open(out);
		}
		private String menuSelect(String out, String param) {
			if (param == null || param.isEmpty())
				return menuGuide(out); // No parameters = ask for help
			Integer number = getInteger(param);
			if (number == null)
				return "? Invalid parameter" + newline + menuGuide(out);
			return menuSelect(out, number);
		}
		private String menuList(String out) {
			if (subMenus.size() == 0)
				return "? No menu list available";
			out += "Menu List: " + newline;
			int i=0;
			for (Menu p: subMenus) {
				out += "(M " + i + ") " + p.menuName + newline;
				i++;
			}
			lastList = MENU_ID;
			return out;
		}
		private String menuGuide(String out) {
			out += "Current Menu: ";
			out += menuName + newline;
			out = commandGuide(out);			
			out = menuList(out);
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
		private String settingSelect(String out, String param) {
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
				return out + " ? Index to high! max = " + (subMenus.size()-1);
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
		private String optionSelect(String out, String param) {
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
				out += cmd.getLineGuide() + newline;
			}
			return out;
		}
	}
	// ################### SUB CLASS COMMAND ######################
	class Command {
		private final List <String> keyList = new ArrayList<>();
		private final String description;
		private String cmdHelp	= "";
		private String cmdParam	= "";
		protected String execute(List<String> param) { return "Unimplemented command!" + newline; }
		Command(String descr, String... keys) {
			description = descr;
			for (String key : keys)
				keyList.add(key.toUpperCase());
		}
		private boolean isKey(String str)	{ return keyList.contains(str); }
		private void cmdHelp(String help)	{ cmdHelp = help;}
		private void cmdParam(String p)		{ cmdParam = p;}
		private String cmdHelp()			{ return cmdHelp;}
		private String getKey()				{ return keyList.get(0);}
		protected String getLineGuide()		{
			String out = "(";
			out += getKey();
			out += cmdParam + ") ";
			out += description;
			return out;
		}
	}
	// ################### SUB CLASS VIEW FILTER ######################
	class ViewFilter {
		private List<Integer> empires = new ArrayList<>();
		private Boolean colonized	= null;
		private Boolean explored	= null;
		private boolean attacked	= false;
		private boolean allList		= true;
		private boolean planetList	= false;
		private boolean fleetList	= false;
		private boolean transList	= false;
		private Float dist	= null;
		private Float range	= null;
		private	Empire pl	= player();
		private String result	= "";

		String getResult(String out)	{ return out + result; } 

		ViewFilter(Command cmd, List<String> filters)	{
			if (filters.contains("?")) {
				result = cmd.description + newline + cmd.cmdHelp();
				return;
			}
			for (String filter : filters)
				filterMOList(filter);
			if (allList) {
				filterSystems();
				filterFleets();
				filterTransports();
				return;
			}
			if (planetList)
				filterSystems();
			if (fleetList)
				filterFleets();
			if (transList)
				filterTransports();
		}
		private void filterMOList(String filter)	{
			if (filter.isEmpty())
				return;
			String c1, c2, se;
			c1 = filter.substring(0, 1)	;
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
				empires.add(0);
				break;
			case "O": // Selected Opponent
				if (se.isEmpty()) { // All opponents
					for (Empire e : galaxy().activeEmpires())
						if (!e.isPlayer())
							empires.add(e.id);
				}
				else if (se.equals("W")) { // At war with
					for (Empire e : pl.warEnemies())
						empires.add(e.id);
				}
				else { // Specified opponents
					Integer opp = getInteger(se);
					if (opp != null)
						empires.add(opp);
				}
				break;			
			case "W": // At war with
				for (Empire e : pl.warEnemies())
					empires.add(e.id);
				break;			
			case "A": // Unexplored
				attacked = true;
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
			case "P": // Planets
				allList = false;
				planetList = true;
				break;			
			case "F": // Fleets
				allList = false;
				fleetList = true;
				break;			
			case "T": // Transports
				allList = false;
				transList = true;
				break;			
			}
		}
		private void filterSystems()	{
			resetSystems();
			filterMOList(systems);
			if (!empires.isEmpty()) {
				List<StarSystem> copy = new ArrayList<>(systems);
				for (StarSystem sys : copy)
					if (!empires.contains(sys.empId()))
						systems.remove(sys);
			}
			List<StarSystem> copy = new ArrayList<>(systems);
			for (StarSystem sys : copy) {
				SystemView sv = pl.sv.view(sys.id);
				if (attacked && !sv.attackTarget())
					systems.remove(sys);
				if (colonized != null && colonized && !sv.isColonized())
					systems.remove(sys);
				else if (colonized != null && !colonized && sv.isColonized())
					systems.remove(sys);
				else if (explored != null && explored && !sv.scouted())
					systems.remove(sys);
				else if (explored != null && !explored && sv.scouted())
					systems.remove(sys);
			}
			sortSystems();
			result = viewSystems(result);
		}
		private void filterFleets()	{
			resetFleets();
			filterMOList(fleets);
			if (!empires.isEmpty()) {
				List<ShipFleet> copy = new ArrayList<>(fleets);
				for (ShipFleet sf : copy)
					if (!empires.contains(sf.empId()))
						fleets.remove(sf);
			}
			result = viewFleets(result);
		}
		private void filterTransports()	{
			resetTransports();
			filterMOList(transports);
			if (!empires.isEmpty()) {
				List<Transport> copy = new ArrayList<>(transports);
				for (Transport tr : copy)
					if (!empires.contains(tr.empId()))
						transports.remove(tr);
			}
			result = viewTransports(result);
		}
		private String viewSystems(String out)	{
			if (systems.isEmpty())
				return out + "Empty Star System List" + newline;
			StarSystem ref = getSys(selectedStar);
			Empire pl = player();
			SystemInfo sv = pl.sv;
			for (StarSystem sys : systems) {
				SystemView view = sv.view(sys.id);
				Empire emp = sys.empire();
				out += "(P " + sys.altId + ")";
				if (dist == null && emp != null && !emp.isPlayer())
					out += " Dist player = " + ly(pl.distanceTo(sys));
				else
					out +=  " Dist P" + selectedStar + " = " + ly(ref.distanceTo(sys));
				if (pl.knowsOf(emp))
					out += ", Empire = " + emp.id;
				String s = view.name();
				if (!s.isEmpty())
					out += ", " + view.name();
				out += ", " + view.descriptiveName(); 
				out += newline;	
			}
			return out;
		}
		private String viewFleets(String out)	{
			if (fleets.isEmpty())
				return out + "Empty Fleet List" + newline;
			StarSystem ref = getSys(selectedStar);
			int idx = 0;
			for (ShipFleet fleet : fleets) {
				out += "(F " + idx + ")";
				Empire emp = fleet.empire();
				if (pl.knowsOf(emp))
					out += " Empire = " + emp.id;
				else
					out += " Empire = ?";
				if (fleet.isOrbiting())
					out += ", Orbit P" + fleet.system().altId;
				else if (pl.knowETA(fleet)) {
					int destination = fleet.destination().altId;
					int eta = fleet.travelTurnsRemaining();
					out += ", ETA P" + destination + " = " + eta + " year";
					if (eta>1)
						out += "s";
				}
				else
					out += ", Dist P" + selectedStar + " = " + ly(ref.distanceTo(fleet));
				out += newline;
				idx++;
			}
			return out;
		}
		private String viewTransports(String out)	{ // TODO BR: String viewTransports(String out)
			if (transports.isEmpty())
				return out + "Empty Transport List" + newline;
			StarSystem ref = getSys(selectedStar);
			int idx = 0;
			for (Transport transport : transports) {
				out += "(T " + idx + ")";
				Empire emp = transport.empire();
				if (pl.knowsOf(emp))
					out += " Empire = " + emp.id;
				else
					out += " Empire = ?";
				if (pl.knowETA(transport)) {
					int destination = transport.destination().altId;
					int eta = transport.travelTurnsRemaining();
					out += ", ETA P" + destination + " = " + eta + " year";
					if (eta>1)
						out += "s";
				}
				else
					out += ", Dist P" + selectedStar + " = " + ly(ref.distanceTo(transport));
				out += newline;
				idx++;
			}
			return out;
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
