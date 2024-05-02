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

import rotp.Rotp;
import rotp.model.empires.Empire;
import rotp.model.empires.SystemView;
import rotp.model.galaxy.Galaxy;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.galaxy.Transport;
import rotp.model.game.GameSession;
import rotp.model.game.GovernorOptions;
import rotp.model.game.IAdvOptions;
import rotp.model.game.IGameOptions;
import rotp.model.game.IInGameOptions;
import rotp.model.game.IMainOptions;
import rotp.ui.RotPUI;
import rotp.ui.UserPreferences;
import rotp.ui.game.GameUI;
import rotp.ui.main.FleetPanel;
import rotp.ui.main.MainUI;
import rotp.ui.tech.DiplomaticMessageUI;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamFloat;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamString;
import rotp.ui.util.ParamSubUI;

public class CommandConsole extends JPanel  implements IConsole, ActionListener {
	private static JFrame frame;
	private static CommandConsole instance;
	private static boolean errorDisplayed = false;
	public	static CommandMenu introMenu, loadMenu, saveMenu;
	public	static ReportMenu reportMenu;
	public	static ColonizeMenu colonizeMenu;
	public	static DiplomaticMessageMenu diplomaticMessageMenu;

	private final JLabel commandLabel, resultLabel;
	private final JTextField commandField;
	private final JTextPane resultPane;
	private final JScrollPane scrollPane;
	private final List<CommandMenu>	menus		= new ArrayList<>();
	private final List<Empire>		empires		= new ArrayList<>();
	private final List<Transport>	transports	= new ArrayList<>();
	private final List<ShipFleet>	fleets		= new ArrayList<>();
	private final List<StarSystem>	systems		= new ArrayList<>();
	private final LinkedList<String> lastCmd	= new LinkedList<>();
	private CommandMenu liveMenu;
	private CommandMenu mainMenu, setupMenu, gameMenu, speciesMenu;
	private int selectedStar, aimedStar, selectedFleet, selectedTransport, selectedEmpire; // ,, selectedDesign;
	private HashMap<Integer, Integer> altIndex2SystemIndex = new HashMap<>();
//	private Menu stars, fleet, ships, opponents;
//	private final List<SystemView> starList = new ArrayList<>();
	private ConsoleStarView		starView;
	private ConsoleFleetView	fleetView;
	private ConsoleEmpireView	empireView;
	
	// ##### STATIC METHODS #####
	public static CommandConsole cc()				{ return instance; }
	public static void updateConsole()				{ instance.reInit(); }
	public static void turnCompleted(int turn)		{
		instance.resultPane.setText("Current turn: " + turn + NEWLINE);
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
	public static void showConsole(boolean show)	{
		if(!Rotp.isIDE())
			Rotp.setVisible(!show);
		if (frame == null) {
			if (show)
				createAndShowGUI(show);
			return;
		}
		else
			frame.setVisible(show);
	}
	public static void hideConsole()				{ showConsole(false); }
	// variables control
	void liveMenu(CommandMenu menu)	{ liveMenu = menu; }
	CommandMenu liveMenu()			{ return liveMenu; }
	int selectedStar()				{ return selectedStar; }
	int aimedStar()					{ return aimedStar; }
	void aimedStar(int id)			{ aimedStar = id; }
	StarSystem aimedSystem()		{
		if (aimedStar() < 0 || aimedStar() >= galaxy().systemCount)
			return null;
		return console().getSys(aimedStar());
	}
	public static void throwError(Throwable e) {
		StackTraceElement[] trace = e.getStackTrace();
		String out = e.toString();
		for (StackTraceElement ste : trace)
			out += NEWLINE + ste.toString();
		instance.resultPane.setText(out);
		errorDisplayed = true;
	}
	private void testError() {
		boolean allowTest = false;
		int location = allowTest? -2 : 0;
		String test = "Test";
		test.charAt(location);
		try {
			test.charAt(location);
		} catch (Exception e) {
			throwError(e);
		}
	}
	// ##### CONSTRUCTOR #####
	private static void createAndShowGUI(boolean show)	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				if (frame == null) {
					//Create and set up the window.
					frame = new JFrame("Command Console");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					//frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

					//Add contents to the window.
					frame.add(new CommandConsole());

					//Display the window.
					frame.pack();
					frame.setVisible(show);
				}
			}
		});
	}
	public CommandConsole()			{
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
					case KeyEvent.VK_PAGE_UP:
						testError();
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

		resultLabel	= new JLabel("Result: ");
		resultPane	= new JTextPane() {
			@Override public void setText(String t) { if (!errorDisplayed) super.setText(t); }
		};
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
		resultPane.setText(liveMenu().menuGuide(""));

		starView	= new ConsoleStarView();
		fleetView	= new ConsoleFleetView();
		empireView	= new ConsoleEmpireView();
		instance	= this;
		if(!Rotp.isIDE())
			IMainOptions.graphicsMode.set(IMainOptions.GRAPHICS_LOW);
	}
	// ##### INITIALIZERS #####
	private	void reInit()			{ initAltIndex(); }
	private Command initContinue()	{
		Command cmd = new Command("Continue", "C") {
			@Override protected String execute(List<String> param) {
				if (RotPUI.gameUI().canContinue()) {
					RotPUI.gameUI().continueGame();
					return gameMenu.open("");
				}
				else
					return "Nothing to continue" + NEWLINE;
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
		cmd.cmdParam( " " + optional("SHip", "SCout", "Rxx", "Dxx")
			 		+ " " + optional("Y", "O", "Oxx", "W")
			 		+ " " + optional("A", "U", "X", "N", "C")
			 		+ " " + optional(SYSTEM_KEY, FLEET_KEY, TRANSPORT_KEY, EMPIRE_KEY)
			 		);
		cmd.cmdHelp("Loop thru all the given filters and return the result"
				+ NEWLINE + "Distance filters:"
				+ NEWLINE + optional("SH", "Ship") + " : filter in ship range of the player empire"
				+ NEWLINE + optional("SC", "Scout") + " : filter in scout range of the player empire"
				+ NEWLINE + optional("Rxx") + " : filter in xx light years Range of the player empire"
				+ NEWLINE + optional("Dxx") + " : filter in xx light years Distance of the selected star system"
				+ NEWLINE + "Owner filters: if none all are displayed"
				+ NEWLINE + optional("Y") + " : add plaYer"
				+ NEWLINE + optional("O") + " : add all Opponents"
				+ NEWLINE + optional("Oxx") + " : add Opponent xx (Index)"
				+ NEWLINE + optional("W", "OW") + " : add Opponents at War with the player"
				+ NEWLINE + "Category filters:"
				+ NEWLINE + optional("A") + " : planets under Attack"
				+ NEWLINE + optional("U") + " : Unexplored star system only"
				+ NEWLINE + optional("X") + " : eXplored star system only"
				+ NEWLINE + optional("N") + " : uNcolonized star system only"
				+ NEWLINE + optional("C") + " : Colonized star system only"
				+ NEWLINE + "List filters: if none, all three lists are shown"
				+ NEWLINE + optional(SYSTEM_KEY)	+ " : add Planet list (star systems)"
				+ NEWLINE + optional(FLEET_KEY)		+ " : add Fleet list"
				+ NEWLINE + optional(TRANSPORT_KEY)	+ " : add Transport list"
				+ NEWLINE + optional(EMPIRE_KEY)	+ " : add Empire list"
				);
		return cmd;
	}
	private Command initSelectPlanet()		{
		Command cmd = new Command("Select Planet from index and gives Info", SYSTEM_KEY) {
			@Override protected String execute(List<String> param) {
				if (param.isEmpty())
					return getShortGuide();
				String s = param.remove(0);
				Integer p = getInteger(s);
				if (p == null)
					return getShortGuide();

				String out	= "";
				selectedStar	= validPlanet(p);
				StarSystem sys	= getSys(selectedStar);
				mainUI().selectSystem(sys);
				starView.initAltId(selectedStar);

				// Action are reserved to player colonies
				if (!isPlayer(sys.empire()))
					return starView.getInfo(out);

				while (!param.isEmpty()) {
					s = param.remove(0);
					switch (s.toUpperCase()) {
					case TOGGLE_GOV:
						out = starView.toggleGovernor(out) + NEWLINE;
						break;
					case SHIP_SPENDING:
						out = starView.shipSpending(param, out) + NEWLINE;
						break;
					case IND_SPENDING:
						out = starView.indSpending(param, out) + NEWLINE;
						break;
					case DEF_SPENDING:
						out = starView.defSpending(param, out) + NEWLINE;
						break;
					case ECO_SPENDING:
						out = starView.ecoSpending(param, out) + NEWLINE;
						break;
					case TECH_SPENDING:
						out = starView.techSpending(param, out) + NEWLINE;
						break;
					case SHIP_BUILDING:
						out = starView.shipBuilding(param, out) + NEWLINE;
						break;
					case SHIP_LIMIT:
						out = starView.shipLimit(param, out) + NEWLINE;
						break;
					case BASE_LIMIT:
						out = starView.missBuilding(param, out) + NEWLINE;
						break;
					case TROOP_SEND:
						out = starView.sendPopulation(param, out) + NEWLINE;
						break;
					case ABANDON:
						out = starView.abandonColony(param, out) + NEWLINE;
						break;
					case CANCEL_SEND:
						out = starView.cancelSend(param, out) + NEWLINE;
						break;
					default:
						out += "Don't understand parameter " + s;
						break;
					}
				}
				return starView.getInfo(out);
			}
		};
		cmd.cmdParam(" Index " + optional(TOGGLE_GOV)
						+ optional(SHIP_SPENDING + " %")
						+ optional(DEF_SPENDING + " %")
						+ optional(IND_SPENDING + " %")
						+ optional(ECO_SPENDING + " %")
						+ optional(TECH_SPENDING + " %")
						+ optional(SHIP_BUILDING + " val")
						+ optional(SHIP_LIMIT + " max")
						+ optional(BASE_LIMIT + " max")
						);
		cmd.cmdHelp("Additionnal sequentially processed requests:"
				+ NEWLINE + optional(TOGGLE_GOV) + " To Toggle Governo on/off"
				+ NEWLINE + optional(SHIP_SPENDING + " " + TOGGLE_LOCK) + " To lock/unlock Ship spending"
				+ NEWLINE + optional(SHIP_SPENDING + " %") + " To set Ship spending percentage"
				+ NEWLINE + optional(SHIP_SPENDING + " " + SMART_ECO_MAX) + " To maximize Ship spending, while keeping ECO clean"
				+ NEWLINE + optional(SHIP_SPENDING + " " + SMOOTH_MAX) + " To smart maximize Ship spending to reach target, while keeping ECO clean"
				+ NEWLINE + optional(DEF_SPENDING + " " + TOGGLE_LOCK) + " To lock/unlock Defense spending"
				+ NEWLINE + optional(DEF_SPENDING + " %") + " To set Defense spending percentage"
				+ NEWLINE + optional(DEF_SPENDING + " " + SMART_ECO_MAX) + " To maximize Defense spending, while keeping ECO clean"
				+ NEWLINE + optional(DEF_SPENDING + " " + SMOOTH_MAX) + " To smart maximize Defense spending to reach target, while keeping ECO clean"
				+ NEWLINE + optional(IND_SPENDING + " " + TOGGLE_LOCK) + " To lock/unlock Industry spending"
				+ NEWLINE + optional(IND_SPENDING + " %") + " To set Industry spending percentage"
				+ NEWLINE + optional(IND_SPENDING + " " + SMART_ECO_MAX) + " To maximize Industry spending, while keeping ECO clean"
				+ NEWLINE + optional(ECO_SPENDING + " " + TOGGLE_LOCK) + " To lock/unlock Ecology spending"
				+ NEWLINE + optional(ECO_SPENDING + " %") + " To set Ecology spending percentage"
				+ NEWLINE + optional(ECO_SPENDING + " " + ECO_CLEAN) + " To set Ecology spending to clean"
				+ NEWLINE + optional(ECO_SPENDING + " " + ECO_GROWTH) + " To set Ecology spending to grow population"
				+ NEWLINE + optional(ECO_SPENDING + " " + ECO_TERRAFORM) + " To set Ecology spending to terraform planet"
				+ NEWLINE + optional(TECH_SPENDING + " " + TOGGLE_LOCK) + " To lock/unlock Research spending"
				+ NEWLINE + optional(TECH_SPENDING + " %") + " To set Research spending percentage"
				+ NEWLINE + optional(TECH_SPENDING + " " + SMART_ECO_MAX) + " To maximize Research spending, while keeping ECO clean"
				+ NEWLINE + optional(TROOP_SEND + " " + SYSTEM_KEY) + " destId amount : To send transport to another planet"
				+ NEWLINE + optional(ABANDON + " " + SYSTEM_KEY) + " destId amount : To abandon the planet"
				+ NEWLINE + optional(CANCEL_SEND) + " To cancel all transports from this planet"
				);
		return cmd;		
	}
	private Command initAimedPlanet()		{
		Command cmd = new Command("select Aimed planet from index, or Selected planet", AIMED_KEY) {
			@Override protected String execute(List<String> param) {
				String out = getShortGuide() + NEWLINE;
				if (!param.isEmpty()) {
					String s = param.get(0);
					if (s.equalsIgnoreCase("S"))
						aimedStar(selectedStar);
					else {
						Integer p = getInteger(s);
						if (p != null) {
							aimedStar(p);
							out = "";
						}
					}
				}
				StarSystem sys	= getSys(aimedStar());
				out += viewTargetSystemInfo(sys, true);
				return out;
			}
		};
		cmd.cmdParam(" " + optional("Index", "S"));
		cmd.cmdHelp("select Aimed planet from planet index, or from Selected planet if \"S\", and gives Destination info");
		return cmd;		
	}
	private Command initSelectFleet()		{
		Command cmd = new Command("select Fleet from index and gives fleet info", FLEET_KEY) {
			@Override protected String execute(List<String> param) {
				if (param.isEmpty())
					return cmdHelp();

				String  str  = param.remove(0);
				Integer flId = getInteger(str);
				ShipFleet fleet;
				if (flId == null) { // select a new fleet
					return "??? parameter " + str + NEWLINE + cmdHelp();
				}

				FleetPanel panel = RotPUI.instance().mainUI().displayPanel().fleetPane();
				String out = getShortGuide() + NEWLINE;
				// select a new fleet
				selectedFleet = validFleet(flId);
				if (selectedFleet == flId)
					out = "";
				else
					return "Invalid Fleet selection";
				fleet = fleets.get(selectedFleet);
				mainUI().selectSprite(fleet, 1, false, true, false);
				mainUI().map().recenterMapOn(fleet);
				mainUI().repaint();
				fleetView.init(selectedFleet);
				out = fleetView.getInfo(out);

				if (!param.isEmpty()) { // Do something with selected fleet
					str = param.remove(0);
					if (str.equalsIgnoreCase(FLEET_SEND)) { // Send Fleet
						out = fleetView.sendFleet(param, out);
					}
					else if (str.equalsIgnoreCase(FLEET_UNDEPLOY)) {
						panel.undeployFleet();
					}
					else
						out += NEWLINE + "Wrong parameter " + str;
				}
				return out;
			}
		};
		cmd.cmdParam(" Index " + optional(FLEET_UNDEPLOY) + OR_SEP
					+ optional(FLEET_SEND + " " + SYSTEM_KEY + " destId [n] [n] [n] [n] [n]"));

		cmd.cmdHelp("Additionnal requests:"
				 + NEWLINE + "Optional "+ optional(FLEET_UNDEPLOY) + " to Undeploy fleet"
				 + NEWLINE + "Optional "+ optional(FLEET_SEND) + " to Star System " + SYSTEM_KEY + "x"
				 + NEWLINE + "Optional select sub fleet by adding the number of each listed design");
		return cmd;		
	}
	private Command initSelectTransport()	{
		Command cmd = new Command("select Transport and gives Transport info", TRANSPORT_KEY) {
			@Override protected String execute(List<String> param) {
				String out = getShortGuide() + NEWLINE;
				if (!param.isEmpty()) {
					String s = param.get(0);
					Integer f = getInteger(s);
					if (f != null) {
						selectedTransport = bounds(0, f, transports.size()-1);
						Transport transport = transports.get(selectedTransport);
						mainUI().selectSprite(transport, 1, false, true, false);
						mainUI().map().recenterMapOn(transport);
						mainUI().repaint();
						out = transportInfo(transport, NEWLINE);
					}
				}
				return out;
			}
		};
		cmd.cmdParam(" Index");
		cmd.cmdHelp("No secondary options");
		return cmd;		
	}
	private Command initSelectEmpire()		{
		Command cmd = new Command("select Empire from index and gives Info", EMPIRE_KEY) {
			@Override protected String execute(List<String> param) {
				String out = getShortGuide() + NEWLINE;
				// If no parameters, then return player contact info
				if (param.isEmpty()) {
					out += empireContactInfo(player(), NEWLINE);
					out += NEWLINE + viewEmpiresContactInfo();
					return out;
				}
				// Empire selection
				String str = param.get(0);
				Integer empId = getInteger(str);
				if (empId == null)
					selectedEmpire = player().id;
				else {
					selectedEmpire = bounds(0, empId, galaxy().numEmpires()-1);
					param.remove(0);
				}
				//empireView.initId(selectedEmpire);
				Empire empire = galaxy().empire(selectedEmpire);
				
				// Info selection
				if (param.isEmpty()) // No param, then basic contact info
					return empireView.contactInfo(empire, "");

				str = param.get(0);
				switch (str) {
				case EMP_DIPLOMACY:
					return empireView.diplomacyInfo(empire, "");
				case EMP_INTELLIGENCE:
					return empireView.intelligenceInfo(empire, "");
				case EMP_MILITARY:
					return empireView.militaryInfo(empire, "");
				case EMP_STATUS:
					return empireView.statusInfo(empire, "");
				}
				return out + " Unknown Parameter " + str;
			}
		};
		cmd.cmdParam(" " + optional("Index")
				+ optional("EMP_DIPLOMACY")
				);
		cmd.cmdHelp("Select Empire from index, and gives Empire contact info; Player empire will be selected when no index is given."
				+ NEWLINE + optional(EMP_DIPLOMACY)		+ " To get Empire diplomatic info"
				+ NEWLINE + optional(EMP_INTELLIGENCE)	+ " To get Empire intelligence info"
				+ NEWLINE + optional(EMP_MILITARY)		+ " To get Empire military info"
				+ NEWLINE + optional(EMP_STATUS)		+ " To get Empire status info"
				);
		return cmd;		
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
	private void initMenus()		{
		mainMenu = initMainMenu();
		liveMenu(mainMenu);
		menus.clear();
		menus.add(mainMenu);
		loadMenu = initLoadMenu();
		saveMenu = initSaveMenu();
	}
	private CommandMenu initSaveMenu()		{
		CommandMenu menu = new CommandMenu("Save Menu") {
			private String fileBaseName(String fn)		{
				String ext = GameSession.SAVEFILE_EXTENSION;
				if (fn.endsWith(ext)) {
					List<String> parts = substrings(fn, '.');
					if (!parts.get(0).trim().isEmpty()) 
						return fn.substring(0, fn.length()-ext.length());
				}
				return fn;
			}
			@Override public String open(String out)	{
				if (!session().status().inProgress()) {
					out += "No game in progress" + NEWLINE;
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
						out +=  "No file selected" + NEWLINE;
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
							String str = "Save unsuccessful: " + file.getAbsolutePath() + NEWLINE;
							resultPane.setText(mainMenu.open(str));
							return;
						}
					};
					SwingUtilities.invokeLater(save);
					out +=  "Saved to File: " + file.getAbsolutePath() + NEWLINE;
					return mainMenu.open(out);
				}
				out +=  "No file selected" + NEWLINE + NEWLINE;
				return mainMenu.open(out);
			}
		};
		return menu;
	}
	private CommandMenu initLoadMenu()		{
		CommandMenu menu = new CommandMenu("Load Menu") {
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
						out +=  "No file selected" + NEWLINE + NEWLINE;
						return mainMenu.open(out);
					}
					GameUI.gameName = fileBaseName(file.getName());
					String dirName = file.getParent();
					final Runnable load = () -> {
						GameSession.instance().loadSession(dirName, file.getName(), false);
					};
					SwingUtilities.invokeLater(load);
					out +=  "File: " + GameUI.gameName + NEWLINE;
					return gameMenu.open(out);
				}
				out +=  "No file selected" + NEWLINE + NEWLINE;
				return mainMenu.open(out);
			}
		};
		return menu;
	}
	private CommandMenu initMainMenu()		{
		CommandMenu main = new CommandMenu("Main Menu") {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return super.open(out);
			}
		};
		main.addMenu(new CommandMenu("Global Settings Menu", main, IMainOptions.commonOptions()));
		speciesMenu = initSpecieMenu(main);
		setupMenu = initSetupMenus(main);
		main.addMenu(setupMenu);
		gameMenu = initGameMenus(main);
		main.addCommand(initContinue()); // C
		main.addCommand(initLoadFile()); // L
		main.addCommand(initSaveFile()); // S
		return main;
	}
	private CommandMenu initSetupMenus(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("New Setup Menu", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectGamePanel();
				return speciesMenu.open(out);
			}
		};
		menu.addMenu(speciesMenu);
		return menu;
	}
	private CommandMenu initGameMenus(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("Game Menu", parent);
		menu.addMenu(new CommandMenu("In Game Settings Menu", menu, IInGameOptions.inGameOptions()));
		menu.addMenu(new CommandMenu("Governor Menu", menu, GovernorOptions.governorOptionsUI));
		menu.addCommand(initNextTurn());		// N
		menu.addCommand(initView());			// V
		menu.addCommand(initSelectPlanet());	// P
		menu.addCommand(initAimedPlanet());		// A
		menu.addCommand(initSelectFleet());		// F
		menu.addCommand(initSelectTransport());	// T
		menu.addCommand(initSelectEmpire());	// E
		introMenu				= initIntroMenu(menu);
		reportMenu				= new ReportMenu("Report Menu", menu);
		colonizeMenu			= new ColonizeMenu("Colonize Menu", menu);
		diplomaticMessageMenu	= new DiplomaticMessageMenu("Diplomatic Messages Menu", menu);
		return menu;
	}
	private CommandMenu initSpecieMenu(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("Player Species Menu", parent) {
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
	private CommandMenu initGalaxyMenu(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("Galaxy Menu", parent) {
			@Override public String open(String out) {
				RotPUI.instance().selectSetupGalaxyPanel();
				return super.open(out);
			}
		};
		menu.addMenu(new CommandMenu("Advanced Options Menu", menu, IAdvOptions.advancedOptions()));
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
	private CommandMenu initIntroMenu(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("Intro Menu", parent) {
			@Override public String open(String out) {
				reInit();
				Empire pl = player();
				List<String> text = pl.race().introduction();
				out = "Intro Menu" + NEWLINE + NEWLINE;
				for (int i=0; i<text.size(); i++)  {
					String paragraph = text.get(i).replace("[race]", pl.raceName());
					out += paragraph + NEWLINE;
				}
				out += NEWLINE + "Enter any command to continue";
				liveMenu(this);
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

	private MainUI mainUI()	  { return RotPUI.instance().mainUI(); }
	// ##### EVENTS METHODES #####
	@Override public void actionPerformed(ActionEvent evt)	{ }
	private void commandEntry(ActionEvent evt)	{ liveMenu().newEntry(((JTextField) evt.getSource()).getText()); }

	private String optsGuide()	{
		String out = "";
		out += NEWLINE + "Empty: list availble settings";
		out += NEWLINE + "O: list all available options";
		out += NEWLINE + "O INDEX: select chosen option";
		out += NEWLINE + "O+: select next option";
		out += NEWLINE + "O-: select previous option";
		out += NEWLINE + "S: list all available settings";
		out += NEWLINE + "S INDEX: select chosen setting";
		out += NEWLINE + "S+: next setting";
		out += NEWLINE + "S-: previous setting";
		out += NEWLINE + "M: list all available menus";
		out += NEWLINE + "M INDEX: select chosen menu";
		out += NEWLINE + "M+: next menu";
		out += NEWLINE + "M-: previous menu";
		return out;
	}
	// ##### Tools
	private int validFleet(int idx)		{ return bounds(0, idx, fleets.size()-1); }
	private int validTransport(int idx)	{ return bounds(0, idx, transports.size()-1); }
	private void sortSystems()			{ systems.sort((s1, s2) -> s1.altId-s2.altId); }
	private void resetSystems()			{
		systems.clear();
		systems.addAll(Arrays.asList(galaxy().starSystems()));
	}
	private void resetEmpires()			{
		empires.clear();
		for (Empire emp : galaxy().empires())
			if (!emp.extinct() && player().hasContacted(emp.id))
				empires.add(emp);
	}
	private void resetTransports()		{
		transports.clear();
		transports.addAll(player().opponentsTransports());
	}
	private void resetFleets()			{
		fleets.clear();
		fleets.addAll(player().getVisibleFleets());
	}
	private String getParam (String input, List<String> param) {
		String[] text = input.trim().split("\\s+");
		for (int i=1; i<text.length; i++)
			param.add(text[i]);
		//param.add(""); // to avoid empty list!
		return text[0];
	}
	StarSystem getSys(int altIdx)		{ return galaxy().system(altIndex2SystemIndex.get(altIdx)); }
	SystemView getView(int altIdx)		{ return player().sv.view(altIndex2SystemIndex.get(altIdx)); }
	ShipFleet  getFleet(int idx)		{
		int validIdx = validFleet(idx);
		if (idx == validIdx)
			return fleets.get(validFleet(idx));
		return null;
	}
	Transport  getTransport(int idx)	{ return transports.get(validTransport(idx)); }
	int getSysId(int altIdx)			{ return altIndex2SystemIndex.get(altIdx); }
	int getFleetIndex(ShipFleet fl)		{ return fleets.indexOf(fl); }
	int getTransportIndex(Transport tr)	{ return transports.indexOf(tr); }
	// ################### SUB CLASS REPORT MENU ######################
	public class ReportMenu extends CommandMenu {
		ReportMenu(String name)	{ super(name); }
		ReportMenu(String name, CommandMenu parent)		{ super(name, parent); }
		@Override protected String close(String out)	{
			session().resumeNextTurnProcessing();
			liveMenu(gameMenu);
			return out;
		}
		@Override protected void newEntry(String entry)	{ resultPane.setText(close("")); }
		public String openScoutReport(HashMap<String, List<StarSystem>> newSystems) {
			if (newSystems.isEmpty())
				return close("Scout Report: Empty List!");

			String out ="";
			List<StarSystem> scoutSystems		= newSystems.get("Scouts");
			List<StarSystem> allySystems		= newSystems.get("Allies");
			List<StarSystem> astronomerSystems	= newSystems.get("Astronomers");

			if (!scoutSystems.isEmpty()) {
				scoutSystems.sort((s1, s2) -> s1.altId-s2.altId);
				if (scoutSystems.size() == 1)
					out += "Our ships have scouted this system" + NEWLINE;
				else
					out += "Our ships have scouted these systems" + NEWLINE;
				for (StarSystem sys : scoutSystems)
					out += viewSystemInfo(sys, true) + NEWLINE;
			}
			if (!allySystems.isEmpty()) {
				allySystems.sort((s1, s2) -> s1.altId-s2.altId);
				out += "Our allies have shared these data" + NEWLINE;
				for (StarSystem sys : allySystems)
					out += viewSystemInfo(sys, true) + NEWLINE;
			}
			if (!astronomerSystems.isEmpty()) {
				astronomerSystems.sort((s1, s2) -> s1.altId-s2.altId);
				out += "Our astronomers have collected these data" + NEWLINE;
				for (StarSystem sys : astronomerSystems)
					out += viewSystemInfo(sys, true) + NEWLINE;
			}

			out += NEWLINE + "Enter any command to continue";
			liveMenu(this);
			resultPane.setText(out);
			return "";
		}
		public String acknowledgeMessage(String message) {
			String out = message + NEWLINE + NEWLINE + "Enter any command to continue";
			liveMenu(this);
			resultPane.setText(out);
			return "";
		}
		public String openTemplate() {
			String out = "";
			liveMenu(this);
			resultPane.setText(out);
			return out;
		}
	}
	// ################### SUB CLASS DIPLOMATIC MESSAGE MENU ######################
	public class DiplomaticMessageMenu extends CommandMenu {
		private DiplomaticMessageUI parentUI;
		private String message;

		DiplomaticMessageMenu(String name, CommandMenu parent)	{ super(name, parent); }
		@Override protected String close(String out) {
			session().resumeNextTurnProcessing();
			parentUI = null;
			message	 = null;
			liveMenu(gameMenu);
			return out;
		}
		@Override protected void newEntry(String entry)	{
			boolean exited = parentUI.consoleResponse(entry);
			commandField.setText("");
			if (exited)
				close("");
			else {
				misClick();
				String out = "Invalid Answer: " + entry + NEWLINE;
				out += NEWLINE + message;
				resultPane.setText(out);
			}
		}
		public String openDiplomaticMessagPrompt(DiplomaticMessageUI ui) {
			parentUI = ui;
			message = parentUI.getConsoleMessage(NEWLINE);
			liveMenu(this);
			resultPane.setText(message);
			return "";
		}
	}
	// ################### SUB CLASS COLONIZE MENU ######################
	public class ColonizeMenu extends CommandMenu {
	    int sysId;
	    private ShipFleet fleet;
		private String message;
		
		ColonizeMenu(String name)	{ super(name); }
		ColonizeMenu(String name, CommandMenu parent)	{ super(name, parent); }
		@Override protected String close(String out) {
			session().resumeNextTurnProcessing();
			fleet  = null;
			liveMenu(gameMenu);
			return out;
		}
		@Override protected void newEntry(String entry)	{
			switch (entry.toUpperCase()) {
				case "N":
					commandField.setText("");
					resultPane.setText(close(""));
					break;
				case "Y":
					fleet.colonizeSystem(galaxy().system(sysId));
					String title = text("MAIN_COLONIZE_ANIMATION_TITLE", str(galaxy().currentYear()));
					title = player().replaceTokens(title, "player");
					commandField.setText("");
					//resultPane.setText(close(title));
					reportMenu.acknowledgeMessage(title);
					break;
				default:
					misClick();
					String out = "Invalid Answer: " + entry + NEWLINE;
					out += NEWLINE + message;
					resultPane.setText(out);
			}
			commandField.setText("");
		}
		public String openColonyPrompt(int systemId, ShipFleet fl) {
	        sysId  = systemId;
	        fleet  = fl;
			String sysName = player().sv.name(sysId);
			message = text("MAIN_COLONIZE_TITLE", sysName) + NEWLINE;
			String yearStr = displayYearOrTurn();
			message += yearStr + NEWLINE;
			starView.initId(sysId);
			message = starView.getInfo(message) + NEWLINE + NEWLINE;
			String promptStr = text("MAIN_COLONIZE_PROMPT");
			message += promptStr + " Y or N";
			liveMenu(this);
			resultPane.setText(message);
			return "";
		}
	}
	// ################### SUB CLASS COMMAND MENU ######################
	public class CommandMenu {
		private final String menuName;
		private final CommandMenu parent;
		private final List<IParam> settings	 = new ArrayList<>();
		private final List<CommandMenu>   subMenus	 = new ArrayList<>();
		private final List<Command> commands = new ArrayList<>();
		private int lastList = NULL_ID;
		private IParam liveSetting;

		// ##### CONSTRUCTORS #####
		protected CommandMenu(String name)	{
			parent = this;
			menuName = name;
		} // For the main menu
		protected CommandMenu(String name, CommandMenu parent)	{
			this.parent = parent;
			menuName = name;
			addMenu(parent);
		}
		CommandMenu(String name, CommandMenu parent, ParamSubUI ui)		{
			this(name, parent, ui.optionsList);
		}
		CommandMenu(String name, CommandMenu parent, List<IParam> src)	{
			this(name, parent);
			for (IParam p : src)
				if (p != null)
					if (p.isSubMenu()) {
						ParamSubUI ui = (ParamSubUI) p;
						String uiName = text(ui.titleId());
						subMenus.add(new CommandMenu(uiName, this, ui));
					}
					else
						settings.add(p);
		}
		// #####  #####
		public String open(String out)		{
			liveMenu(this);
			return menuGuide(out);
		}
		protected String close(String out)		{ return parent.open(out); }
		private void addMenu(CommandMenu menu)	{ subMenus.add(menu); }
		private void addSetting(IParam setting)	{ settings.add(setting); }
		private void addCommand(Command cmd)	{ commands.add(cmd); }
		protected void newEntry(String entry)	{
			List<String> param = new ArrayList<>();
			String txt = entry.trim().toUpperCase();
			err("Console Command = " + txt);
			// For debug purpose only
			if (txt.equals("SHOW MAIN")) {
				Rotp.setVisible(true);
				frame.setVisible(true);
				return;
			}
			if (txt.equals("HIDE MAIN")) {
				Rotp.setVisible(false);
				return;
			}
			// \debug
			lastCmd.remove(txt); // To keep unique and at last position
			if (!txt.isEmpty())
				lastCmd.add(txt);
			String cmd = getParam(txt, param); // this will remove the cmd from param list
			String out = "Command = " + txt + NEWLINE;
			boolean hasDigit = cmd.matches(".*\\d.*");
			String cmd0 = "";
			String cmd1 = "";
			if (hasDigit) {
				cmd0 = cmd.replaceAll("[^a-zA-Z]", "");
				cmd1 = cmd.replaceAll("[*a-zA-Z]", "");
			}
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
				else if (hasDigit && c.isKey(cmd0)) {
					param.add(0, cmd1);
					if (param.contains("?"))
						out += c.cmdHelp();
					else
						out += c.execute(param);
					commandField.setText("");
					resultPane.setText(out);
					return;
				}
			}
			otherCase(cmd, out, param, hasDigit, cmd0, cmd1);
		}
		private String safeRemove(List<String> param, int id) {
			if (param == null)
				return "";
			if (param.size() <= id)
				return "";
			return param.remove(id);
		}
		private void otherCase(String cmd, String out, List<String> param,
								boolean hasDigit, String cmd0, String cmd1) {
			switch (cmd) {
				case ""		: out = menuGuide(out);	break;
				case "?"	: out = optsGuide();	break;
				case "CLS"	: out = "";				break;
				case "UP"	:
					commandField.setText("");
					close(out);
					return;
				case OPTION_KEY			: out = optionEntry(out, safeRemove(param, 0), param);	break;
				case OPTION_KEY + "+"	: out = optionEntry(out, "+", param);	break;
				case OPTION_KEY + "-"	: out = optionEntry(out, "-", param);	break;
				case OPTION_KEY + "*"	: out = optionEntry(out, "*", param);	break;
				case OPTION_KEY + "="	: out = optionEntry(out, "=", param);	break;
				case SETTING_KEY		: out = settingEntry(out, safeRemove(param, 0), param);	break;
				case SETTING_KEY + "+"	: out = settingEntry(out, "+", param);	break;
				case SETTING_KEY + "-"	: out = settingEntry(out, "-", param);	break;
				case SETTING_KEY + "*"	: out = settingEntry(out, "*", param);	break;
				case SETTING_KEY + "="	: out = settingEntry(out, "=", param);	break;
				case MENU_KEY			: out = menuEntry(out, safeRemove(param, 0), param);	break;
				case MENU_KEY + "+"		: out = menuEntry(out, "+", param);	break;
				case MENU_KEY + "-"		: out = menuEntry(out, "-", param);	break;
				case MENU_KEY + "*"		: out = menuEntry(out, "*", param);	break;
				case MENU_KEY + "="		: out = menuEntry(out, "=", param);	break;
				default	:
					if (hasDigit && !cmd0.isEmpty()) {
						param.add(0, cmd1);
						otherCase(cmd0, out, param, false, "", "");
						return;
					}
					else {
						switch (lastList) {
							case OPTION_ID	: out = optionSelect(out, cmd);		break;
							case SETTING_ID	: out = settingSelect(out, cmd);	break;
							case MENU_ID	: out = menuSelect(out, cmd);		break;
							case NULL_ID	:
							default	:
								out += "? unrecognised command";
								resultPane.setText(out);
								return;
						}
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
			List<CommandMenu> list = parent.subMenus;
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
				return "? Invalid parameter" + NEWLINE + menuGuide(out);
			return menuSelect(out, number);
		}
		private String menuList(String out) {
			if (subMenus.size() == 0)
				return "? No menu list available";
			out += "Menu List: " + NEWLINE;
			int i=0;
			for (CommandMenu p: subMenus) {
				out += "(M " + i + ") " + p.menuName + NEWLINE;
				i++;
			}
			lastList = MENU_ID;
			return out;
		}
		private String menuGuide(String out) {
			out += "Current Menu: ";
			out += menuName + NEWLINE;
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
				return settingGuide(out+ "? Invalid parameter" + NEWLINE);
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
					out += NEWLINE + "( S " + i + ") ";
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
			out += "Current Setting: " + NEWLINE;
			out += liveSetting.getHelp();
			out += NEWLINE + liveSetting.selectionGuide();
			out += NEWLINE + NEWLINE;
			return optionList(out);
		}
		// Command methods
		private String commandGuide(String out) {
			for (Command cmd : commands) {
				out += cmd.getShortGuide() + NEWLINE;
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
		protected String execute(List<String> param) { return "Unimplemented command!" + NEWLINE; }
		Command(String descr, String... keys) {
			description = descr;
			for (String key : keys)
				keyList.add(key.toUpperCase());
		}
		private boolean isKey(String str)	{ return keyList.contains(str); }
		private void cmdHelp(String help)	{ cmdHelp = help;}
		private void cmdParam(String p)		{ cmdParam = p;}
		protected String cmdHelp()			{ return getShortGuide() + NEWLINE + cmdHelp;}
		private String getKey()				{ return keyList.get(0);}
		protected String getShortGuide()		{
			String out = "(";
			out += getKey();
			out += cmdParam + ") ";
			out += description;
			return out;
		}
	}
	// ################### SUB CLASS VIEW FILTER ######################
	class ViewFilter {
		private List<Integer> empireFilter = new ArrayList<>();
		private Boolean colonized	= null;
		private Boolean explored	= null;
		private boolean attacked	= false;
		private boolean allList		= true;
		private boolean planetList	= false;
		private boolean fleetList	= false;
		private boolean transpList	= false;
		private boolean empireList	= false;
		private Float dist	= null;
		private Float range	= null;
		private	Empire pl	= player();
		private String result	= "";

		String getResult(String out)	{ return out + result; } 

		ViewFilter(Command cmd, List<String> filters)	{
			if (filters.contains("?")) {
				result = cmd.description + NEWLINE + cmd.cmdHelp();
				return;
			}
			for (String filter : filters)
				filterMOList(filter);
			if (allList) {
				filterSystems();
				filterFleets();
				filterTransports();
				filterEmpires();
				return;
			}
			if (planetList)
				filterSystems();
			if (fleetList)
				filterFleets();
			if (transpList)
				filterTransports();
			if (empireList)
				filterEmpires();
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
				empireFilter.add(0);
				break;
			case "O": // Selected Opponent
				if (se.isEmpty()) { // All opponents
					for (Empire e : galaxy().activeEmpires())
						if (!e.isPlayer())
							empireFilter.add(e.id);
				}
				else if (se.equals("W")) { // At war with
					for (Empire e : pl.warEnemies())
						empireFilter.add(e.id);
				}
				else { // Specified opponents
					Integer opp = getInteger(se);
					if (opp != null)
						empireFilter.add(opp);
				}
				break;			
			case "W": // At war with
				for (Empire e : pl.warEnemies())
					empireFilter.add(e.id);
				break;			
			case "A": // Attacked
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
			case EMPIRE_KEY: // Planets
				allList = false;
				empireList = true;
				break;			
			case SYSTEM_KEY: // Planets
				allList = false;
				planetList = true;
				break;			
			case FLEET_KEY: // Fleets
				allList = false;
				fleetList = true;
				break;			
			case TRANSPORT_KEY: // Transports
				allList = false;
				transpList = true;
				break;			
			}
		}
		private void filterSystems()	{
			resetSystems();
			filterMOList(systems);
			if (!empireFilter.isEmpty()) {
				List<StarSystem> copy = new ArrayList<>(systems);
				for (StarSystem sys : copy)
					if (!empireFilter.contains(sys.empId()))
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
			if (!empireFilter.isEmpty()) {
				List<ShipFleet> copy = new ArrayList<>(fleets);
				for (ShipFleet sf : copy)
					if (!empireFilter.contains(sf.empId()))
						fleets.remove(sf);
			}
			result = viewFleets(result);
		}
		private void filterTransports()	{
			resetTransports();
			filterMOList(transports);
			if (!empireFilter.isEmpty()) {
				List<Transport> copy = new ArrayList<>(transports);
				for (Transport tr : copy)
					if (!empireFilter.contains(tr.empId()))
						transports.remove(tr);
			}
			result = viewTransports(result);
		}
		private void filterEmpires()	{
			resetEmpires();
			if (range != null) {
				List<Empire> copy = new ArrayList<>(empires);
				for (Empire emp : copy)
					if (pl.distanceToEmpire(emp) > range)
						empires.remove(emp);
			}
			if (dist != null) {
				StarSystem ref = getSys(selectedStar);
				List<Empire> copy = new ArrayList<>(empires);
				for (Empire emp : copy)
					if (emp.distanceTo(ref) > dist)
						empires.remove(emp);
			}
			if (!empireFilter.isEmpty()) {
				List<Empire> copy = new ArrayList<>(empires);
				for (Empire emp : copy)
					if (!empireFilter.contains(emp.id))
						empires.remove(emp);
			}
			//result += viewEmpiresContactInfo(empires);
			result = viewEmpires(result);
		}
		private String viewSystems(String out)	{
			if (systems.isEmpty())
				return out + "Empty Star System List" + NEWLINE;
			for (StarSystem sys : systems)
				out += viewSystemInfo(sys, dist!=null) + NEWLINE;
			return out;
		}
		private String viewFleets(String out)	{
			if (fleets.isEmpty())
				return out + "Empty Fleet List" + NEWLINE;
			int idx = 0;
			for (ShipFleet fleet : fleets) {
				out += bracketed(FLEET_KEY, idx) + " ";
				out += viewFleetInfo(fleet);
				out += NEWLINE;
				idx++;
			}
			return out;
		}
		private String viewTransports(String out)	{
			if (transports.isEmpty())
				return out + "Empty Transport List" + NEWLINE;
			for (Transport transport : transports) {
				out += transportInfo(transport, SPACER);
				out += NEWLINE;
			}
			return out;
		}
		private String viewEmpires(String out)	{
			if (empires.isEmpty())
				return out + "Empty Empire List" + NEWLINE;
			for (Empire empire : empires) {
				out += empireContactInfo(empire, SPACER);
				out += NEWLINE;
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
