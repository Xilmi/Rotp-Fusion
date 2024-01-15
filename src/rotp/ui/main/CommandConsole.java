/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *	 notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *	 notice, this list of conditions and the following disclaimer in the
 *	 documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *	 contributors may be used to endorse or promote products derived
 *	 from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package rotp.ui.main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import rotp.ui.RotPUI;
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
//	private final List<IParam> speciesPanel = new ArrayList<>();
//	private final List<IParam> galaxyPanel  = new ArrayList<>();
//	private final LinkedHashMap<String, List<IParam>> panelList  = new LinkedHashMap<>();

	private JLabel cmdLabel, optLabel, resultLabel;
	private JTextField cmdField, optField;
	private JTextPane resultPane;
	private JScrollPane scrollPane;
//	private IParam currentSetting;
//	private List<IParam> currentPanel = speciesPanel;
//	private int lastList = NULL_ID;


	private final List<Panel> panels = new ArrayList<>();
	private Panel livePanel;
//	private IParam liveSetting;
	private Panel mainPanel, setupPanel, gamePanel;

	// ##### INITIALIZERS #####
	private void initPanels() {
		mainPanel = initMainPanel();
		livePanel = mainPanel;
		panels.clear();
		panels.add(mainPanel);
	}
	private Panel initMainPanel() { // TODO BR: initMainPanel
		Panel main = new Panel("Main Panel") {
			@Override protected String open(String out) {
				RotPUI.instance().selectGamePanel();
				return super.open(out);
			}
		};
		setupPanel = initSetupPanels(main);
		main.addPanel(setupPanel);
		gamePanel = initGamePanels(main);
		main.addPanel(gamePanel);
		return main;
	}
	private Panel initSetupPanels(Panel parent) { // TODO BR: initSetupPanels
		Panel panel = new Panel("New Setup Panel", parent) {
			@Override protected String open(String out) {
				RotPUI.instance().selectGamePanel();
				RotPUI.instance().selectSetupRacePanel();
				return super.open(out);
			}
		};
		panel.addPanel(initSpeciePanel(parent));
		panel.addPanel(initGalaxyPanel(parent));
		return panel;
	}
	private Panel initGamePanels(Panel parent)	{ // TODO BR: initGamePanels
		Panel panel = new Panel("Game Panel", parent);
		return panel;
	}
	private Panel initSpeciePanel(Panel parent) { // TODO BR: initSpeciePanel
		Panel panel = new Panel("Player Species Panel", parent);
		panel.addSetting(RotPUI.setupRaceUI().playerSpecies());
		panel.addSetting(RotPUI.setupRaceUI().playerHomeWorld());
		panel.addSetting(RotPUI.setupRaceUI().playerLeader());
		return panel;
	}
	private Panel initGalaxyPanel(Panel parent) { // TODO BR: initGalaxyPanel
		Panel panel = new Panel("Galaxy Panel", parent);
		panel.addSetting(rotp.model.game.IGalaxyOptions.sizeSelection);
		panel.addSetting(rotp.model.game.IPreGameOptions.dynStarsPerEmpire);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeSelection);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeOption1);
		panel.addSetting(rotp.model.game.IGalaxyOptions.shapeOption2);
		panel.addSetting(rotp.model.game.IGalaxyOptions.difficultySelection);
		panel.addSetting(rotp.model.game.IInGameOptions.customDifficulty);
		panel.addSetting(rotp.model.game.IGalaxyOptions.aliensNumber);
		panel.addSetting(RotPUI.setupGalaxyUI().opponentAI);
		panel.addSetting(RotPUI.setupGalaxyUI().globalAbilities);
		return panel;
	}


//	private void initSpeciePanel() {
//		speciesPanel.clear();
//		speciesPanel.add(RotPUI.setupRaceUI().playerSpecies());
//		speciesPanel.add(RotPUI.setupRaceUI().playerHomeWorld());
//		speciesPanel.add(RotPUI.setupRaceUI().playerLeader());
//		panelList.put("Species settings", speciesPanel);
//	}
//	private void initGalaxyPanel() {
//		galaxyPanel.clear();
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.sizeSelection);
//		galaxyPanel.add(rotp.model.game.IPreGameOptions.dynStarsPerEmpire);
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.shapeSelection);
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.shapeOption1);
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.shapeOption2);
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.difficultySelection);
//		galaxyPanel.add(rotp.model.game.IInGameOptions.customDifficulty);
//		galaxyPanel.add(rotp.model.game.IGalaxyOptions.aliensNumber);
//		galaxyPanel.add(RotPUI.setupGalaxyUI().opponentAI);
//		galaxyPanel.add(RotPUI.setupGalaxyUI().globalAbilities);
//		panelList.put("Galaxy settings", galaxyPanel);
//	}
//	private void initLists() {
//		panelList.clear();
//		initSpeciePanel();
//		initGalaxyPanel();
//	}
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
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
//				newOptionEntry(evt);
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
//		initLists();
		initPanels();
		resultPane.setText(cmdGuide());
		cmdField.requestFocusInWindow();
	}

	// ##### EVENTS METHODES #####
	@Override public void actionPerformed(ActionEvent evt) { }
	private void optionEntry(ActionEvent evt) { livePanel.newEntry(evt); }

	private String optsGuide() {
//		String out = clsHelp();
		String out = "";
		out += newline + "Empty: list availble settings";
		out += newline + "o: list all available options";
		out += newline + "o+: select next option";
		out += newline + "o-: select previous option";
		out += newline + "o CHOICE: select chosen option";
		out += newline + "s: list all available settings";
		out += newline + "s+: next setting";
		out += newline + "s-: previous setting";
		out += newline + "s CHOICE: select chosen setting";
		out += newline + "p: list all available panels";
		out += newline + "p+: next panel";
		out += newline + "p-: previous panel";
		out += newline + "p CHOICE: select chosen panel";

//		out += newline + "*: show all choices";
//		out += newline + "= VALUE: Set the option";
		return out;
	}
//	private void newOptionEntry(ActionEvent evt) {
//		List<String> param = new ArrayList<>();
//		String txt = optField.getText();
//		String cmd = getParam(txt, param);
//		String out = "Command = " + txt + newline;
//		switch (cmd.toUpperCase()) {
//			case ""		: out += settingList();	break;
//			case "?"	: out += optsGuide();	break;
//			case "CLS"	: out = "";				break;
//			case "O"	: out += optionEntry(param.remove(0), param);	break;
//			case "O+"	: out += optionEntry("+", param);	break;
//			case "O-"	: out += optionEntry("-", param);	break;
//			case "O*"	: out += optionEntry("*", param);	break;
//			case "O="	: out += optionEntry("=", param);	break;
//			case "S"	: out += settingEntry(param.remove(0), param);	break;
//			case "S+"	: out += settingEntry("+", param);	break;
//			case "S-"	: out += settingEntry("-", param);	break;
//			case "S*"	: out += settingEntry("*", param);	break;
//			case "S="	: out += settingEntry("=", param);	break;
//			case "P"	: out += panelEntry(param.remove(0), param);	break;
//			case "P+"	: out += panelEntry("+", param);	break;
//			case "P-"	: out += panelEntry("-", param);	break;
//			case "P*"	: out += panelEntry("*", param);	break;
//			case "P="	: out += panelEntry("=", param);	break;
//			default	:
//				Integer number = getInteger(cmd);
//				if (number != null && lastList != NULL_ID) {
//					switch (lastList) {
//					case OPTION_ID	: out += optionSel(cmd);	break;
//					case SETTING_ID	: out += settingSel(cmd);	break;
//					case PANEL_ID	: out += panelSel(cmd);		break;
//					}
//				} else {
//					out += "? unrecognised command";
//					resultPane.setText(out);
//					return;
//				}
//		}
//		optField.setText("");
//		resultPane.setText(out);
//	}
	private String cmdGuide() {
//		String out = clsHelp();
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
//			case "NEW"	: out += newGame();		break;
			case "NEW"	: out = setupPanel.open(out);		break;
			default:
				out += "? unrecognised command";
				resultPane.setText(out);
				return;
		}
		cmdField.setText("");
		resultPane.setText(out);
	}
//		String out = clsHelp();
	String out = "";
//	private String clsHelp() { return ("cls: Clear screen"); }

//	private String panelEntry(String cmd, List<String> p) {
//		String out = "";
//		switch (cmd.toUpperCase()) {
//			case ""	:
//			case "?":
//			case "*": out += panelList();	break;
//			case "+": out += panelNext();	break;
//			case "-": out += panelPrev();	break;
//			case "=": out += panelSel(p.get(0));	break;
//			default	:
//				out += panelSel(cmd);
//		}
//		return out;
//	}
//	private String settingEntry(String cmd, List<String> p) {
//		String out = "";
//		switch (cmd.toUpperCase()) {
//			case ""	:
//			case "?":
//			case "*": out += settingList();	break;
//			case "+": out += settingNext();	break;
//			case "-": out += settingPrev();	break;
//			case "=": out += settingSel(p.get(0));	break;
//			default	:
//				out += settingSel(cmd);
//		}
//		return out;
//	}
//	private String optionEntry(String cmd, List<String> p) {
//		String out = "";
//		switch (cmd.toUpperCase()) {
//			case ""	:
//			case "?":
//			case "*": out += optionList();	break;
//			case "+": out += optionNext();	break;
//			case "-": out += optionPrev();	break;
//			case "=": out += optionSel(p.get(0));	break;
//			default	:
//				out += optionSel(cmd);
//		}
//		return out;
//	}


//	// Options methods
//	private String optionSel(String param) {
//		if (param == null || param.isEmpty())
//			return optionGuide();
//    	if (currentSetting instanceof ParamList) {
//    		Integer number = getInteger(param);
//    		if (number != null)
//    			((ParamList)currentSetting).setFromIndex(number);
//    		else
//    			((ParamList)currentSetting).set(param);
//    	}
//    	else if (currentSetting instanceof ParamInteger) {
//    		Integer number = getInteger(param);
//    		if (number != null)
//    			((ParamInteger)currentSetting).set(number);
//    		else
//    			return "";
//    	}
//    	else if (currentSetting instanceof ParamFloat) {
//    		Float number = getFloat(param);
//    		if (number != null)
//    			((ParamFloat)currentSetting).set(number);
//    		else
//    			return "? Float expected";
//    	}
//    	else if (currentSetting instanceof ParamString) {
//    		((ParamString)currentSetting).set(param);
//    	}
//    	else if (currentSetting instanceof ParamBoolean) {
//   			currentSetting.setFromCfgValue(param);
//    	}
//    	else
//    		return "? Something wrong";
//		return optionGuide();
//	}
//	private String optionPrev() {
//		if (currentSetting == null)
//			return "? No selected option";
//		currentSetting.prev();
//		return optionGuide();
//	}
//	private String optionNext() {
//		if (currentSetting == null)
//			return "? No selected option";
//		currentSetting.next();
//		return optionGuide();
//	}
//	private String optionList() {
//		if (currentSetting == null)
//			return "? No selected option";
//		lastList = OPTION_ID;
//		return currentSetting.getFullHelp();
//	}
//	private String optionGuide() {
//		if (currentSetting == null)
//			return "? No selected option";
//		String out = "Current Setting: " + newline;
//		out += currentSetting.getHelp();
//		// out += newline + "Current Setting Value:" +  newline;
//		out += newline + currentSetting.selectionGuide();
//		out += newline + newline;
//		//out += newline + newline + "Possible Values" + newline;
//		out += optionList();
//		return out;
//	}
//	// Settings methods
//	private String settingPrev() {
//		if (currentPanel == null)
//			return "? No selected panel";
//		int index = currentPanel.indexOf(currentSetting) - 1;
//		if (index < 0)
//			index = currentPanel.size()-1;
//		return settingSel(index);
//	}
//	private String settingNext() {
//		if (currentPanel == null)
//			return "? No selected panel";
//		int index = currentPanel.indexOf(currentSetting) + 1;
//		if (index >= currentPanel.size())
//			index = 0;
//		return settingSel(index);
//	}
//	private String settingSel(String param) {
//		if (param == null || param.isEmpty())
//			return settingGuide();
//		Integer number = getInteger(param);
//		if (number == null)
//			return "? Invalid parameter" + newline + settingGuide();
//		return settingSel(number);
//	}
//	private String settingSel(IParam option) {
//		currentSetting = option;
//		return optionGuide();
//	}
//	private String settingSel(int index) {
//		if (currentPanel.isEmpty())
//			return "";
//		index = bounds(0, index, currentPanel.size()-1);
//		return settingSel(currentPanel.get(index));
//	}
//	private String settingList() {
//		if (currentPanel == null || currentPanel.size() == 0)
//			return "? No setting list available";
//		String out =  "Setting list:";
//		for (int i=0; i<currentPanel.size(); i++) {
//			IParam setting = currentPanel.get(i);
//			out += newline + "(" + i + ") ";
//			out += setting.getGuiDisplay();
//			out += ": ";
//			out += setting.getDescription();
//		}
//		lastList = SETTING_ID;
//		return out;
//	}
//	private String settingGuide() { return settingList(); }
//	// Panels methods
//	private String panelPrev() {
//		if (panelList == null)
//			return "? No panel list available";
//		int index = currentPanel.indexOf(currentSetting) - 1;
//		if (index < 0)
//			index = currentPanel.size()-1;
//		return settingSel(index);
//	}
//	private String panelNext() {
//		if (panelList == null)
//			return "? No panel list availablel";
//		int index = currentPanel.indexOf(currentSetting) + 1;
//		if (index >= currentPanel.size())
//			index = 0;
//		return settingSel(index);
//	}
//	private String panelSel(int index) {
//		int i=0;
//		for (Entry<String, List<IParam>> entry: panelList.entrySet()) {
//			if (i == index) {
//				currentPanel = entry.getValue();
//			}
//			i++;
//		}
//		settingSel(0);
//		return panelGuide();
//	}
//	private String panelSel(String param) {
//		if (param == null || param.isEmpty())
//			return panelGuide();
//		Integer number = getInteger(param);
//		if (number == null)
//			return "? Invalid parameter" + newline + panelGuide();
//		return panelSel(number);
//	}
//	private String panelList() {
//		if (panelList == null || panelList.size() == 0)
//			return "? No panel list available";
//		String out = "";
//		int i=0;
//		for (String panel: panelList.keySet()) {
//			out += newline + "(" + i + ") ";
//			out += panel;
//			i++;
//		}
//		lastList = PANEL_ID;
//		return out;
//	}
//	private String panelGuide() {
//		if (currentPanel == null)
//			return "? No selected panel";
//		String out = "Current Panel: ";
//		for (Entry<String, List<IParam>> entry: panelList.entrySet()) {
//			if (entry.getValue().equals(currentPanel)) {
//				out += entry.getKey();
//				out += newline;
//				break;
//			}
//		}
//		return out + settingGuide();
//	}

//	// Commands
//	private String newGame() {
//		//resultPane.setContentType("text/html");
//		RotPUI.instance().selectGamePanel();
//		RotPUI.instance().selectSetupRacePanel();
//		return settingSel(RotPUI.setupRaceUI().playerSpecies());
//	}

	// ##### Tools
	private String getParam (String input, List<String> param) {
		String[] text = input.trim().split("\\s+");
		for (int i=1; i<text.length; i++)
			param.add(text[i]);
		param.add(""); // to avoid empty list!
		return text[0];
	}
	private Integer getInteger(String text) {
        try {
        	return Integer.parseInt(text);
        } catch (NumberFormatException e) {
        	return null;
        }
	}
	private Float getFloat(String text) {
        try {
        	return Float.parseFloat(text);
        } catch (NumberFormatException e) {
        	return null;
        }
	}
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event dispatch thread.
	 */
	private static void createAndShowGUI(boolean show) {
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
	public static void showConsole(boolean show)	{
		if (frame == null) {
			if (show)
				createAndShowGUI( show);
			return;
		}
		else
			frame.setVisible(show);
	}
	public static void hideConsole()	{ showConsole(false); }
	public static void main(String[] args) {
		//Schedule a job for the event dispatch thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override public void run() {
				createAndShowGUI(true);
			}
		});
	}
	// ################### SUB CLASS ######################
	private class Panel {
		private final String panelName;
		private final Panel parent;
		private List<IParam> settings = new ArrayList<>();
		private List<Panel> subPanels = new ArrayList<>();
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
					if (p.isSubMenu())
						subPanels.add(new Panel(name, this, (ParamSubUI) p));
					else
						settings.add(p);
		}
		// #####  #####
		protected String open(String out)	{
			livePanel = this;
			return panelGuide(out);
		}
		protected String close(String out)		{ return parent.open(out); }
		private void addPanel(Panel panel)		{ subPanels.add(panel); }
		private void addSetting(IParam setting)	{ settings.add(setting); }
		private void newEntry(ActionEvent evt) {
			List<String> param = new ArrayList<>();
			String txt = ((JTextField) evt.getSource()).getText();
			String cmd = getParam(txt, param);
			String out = "Command = " + txt + newline;
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
				out += "(" + i + ") " + p.panelName + newline;
				i++;
			}
			lastList = PANEL_ID;
			return out;
		}
		private String panelGuide(String out) {
			out += "Current Panel: ";
			out += panelName + newline;
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
				out += newline + "(" + i + ") ";
				out += setting.getGuiDisplay();
				out += ": ";
				out += setting.getDescription();
			}
			lastList = SETTING_ID;
			return out;
		}
		private String settingGuide(String out) { return settingList(out); }
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
	}
}
