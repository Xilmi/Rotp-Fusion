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
import rotp.util.Base;

public class CommandConsole extends JPanel  implements Base, ActionListener {

	private final static String newline = "<br>";
	private static JFrame frame;

	private JLabel cmdLabel, optLabel, resLabel;
	private JTextField cmdField, optField;
	private JTextPane resultPane;
	private JScrollPane scrollPane;
	private IParam currentOption;

	public CommandConsole() {
		super(new GridBagLayout());

		cmdLabel = new JLabel("Command: ");
		cmdField = new JTextField(80);
		cmdField.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newCommandEntry(evt);
			}
		});

		optLabel = new JLabel("Options: ");
		optField = new JTextField(80);
		optField.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newOptionEntry(evt);
			}
		});

		resLabel = new JLabel("Result: ");
		resultPane = new JTextPane();
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

		add(resLabel);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(scrollPane, c);
		
		setPreferredSize(new Dimension(800, 600));
		resultPane.setContentType("text/html");
		resultPane.setText("<html>");
		//out("<html>");
	}

	@Override public void actionPerformed(ActionEvent evt) { }

	private void newOptionEntry(ActionEvent evt) {
		List<String> param = new ArrayList<>();
		String text = getParam(optField.getText(), param);
		String out = "Command = " + text + newline;
		switch (text.toUpperCase()) {
			case "?"	: out += optsHelp();	break;
			case "CLS"	: out = "";				break;
			case "+"	: out += next();		break;
			case "-"	: out += prev();		break;
			case "*"	: out += allOptions();	break;
			default	:
				out += parseOption(text);	break;
		}
		optField.setText("");
		resultPane.setText(out);
	}
	private void newCommandEntry(ActionEvent evt) {
		List<String> param = new ArrayList<>();
		String text = getParam(cmdField.getText(), param);
		String out = "Command = " + text + newline;
		switch (text.toUpperCase()) {
			case "?"	: out += cmdHelp();		break;
			case "CLS"	: out = "";				break;
			case "NEW"	: out += newGame();		break;
			default:
				out = ("Command = " + text + " ?");
				resultPane.setText(out);
				return;
		}
		cmdField.setText("");
		resultPane.setText(out);
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
	private String parseOption(String text) {
    	if (currentOption instanceof ParamList) {
    		Integer number = getInteger(text);
    		if (number != null)
    			((ParamList)currentOption).setFromIndex(number);
    		else 
    			((ParamList)currentOption).set(text);
    	}
    	else if (currentOption instanceof ParamInteger) {
    		Integer number = getInteger(text);
    		if (number != null)
    			((ParamInteger)currentOption).set(number);
    		else
    			return "";
    	}
    	else if (currentOption instanceof ParamFloat) {
    		Float number = getFloat(text);
    		if (number != null)
    			((ParamFloat)currentOption).set(number);
    		else
    			return "? Float expected";
    	}
    	else if (currentOption instanceof ParamString) {
    		((ParamString)currentOption).set(text);
    	}
    	else if (currentOption instanceof ParamBoolean) {
   			currentOption.setFromCfgValue(text);
    	}
    	else
    		return "? Something wrong";
		return OptionHelp();
	}
	private String optsHelp() {
		String out = clsHelp();
		out += newline + "+: next selection";
		out += newline + "-: previous selection";
		out += newline + "*: show all choices";
		return out;
	}
	private String cmdHelp() {
		String out = clsHelp();
		out += newline + "new: create a new setup";
		return out;
	}
	private String clsHelp() { return ("cls: Clear screen" + System.lineSeparator()); }
	
	private String getParam (String input, List<String> param) {
		String[] text = input.trim().split("\\s+");
		for (int i=1; i<text.length; i++)
			param.add(text[i]);
		return text[0];
	}
	private String allOptions() {
		if (currentOption == null)
			return "? No selected option";
		return currentOption.getFullHelp();
	}
	private String prev() {
		if (currentOption == null)
			return "? No selected option";
		currentOption.prev();
		return OptionHelp();
	}
	private String next() {
		if (currentOption == null)
			return "? No selected option";
		currentOption.next();
		return OptionHelp();
	}
	private String newGame() {
		//resultPane.setContentType("text/html");
		RotPUI.instance().selectGamePanel();
		RotPUI.instance().selectSetupRacePanel();
		currentOption = RotPUI.setupRaceUI().playerSpecies();
		return OptionHelp();
	}
	private String OptionHelp() {
		String out = "";
		out = currentOption.getHelp();
		out += newline;
		out += currentOption.selectionGuide();
		return out;
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
}
