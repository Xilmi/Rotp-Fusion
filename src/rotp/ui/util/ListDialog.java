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

package rotp.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import rotp.ui.BasePanel;
import rotp.ui.RotPUI;
import rotp.ui.game.GameUI;
import rotp.ui.game.HelpUI;
import rotp.ui.game.HelpUI.HelpSpec;
import rotp.util.Base;

/*
 * ListDialog.java is meant to be used by programs such as
 * ListDialogRunner.  It requires no additional files.
 */

/**
 * Use this modal dialog to let the user choose one string from a long
 * list.  See ListDialogRunner.java for an example of using ListDialog.
 * The basics:
 * <pre>
	String[] choices = {"A", "long", "array", "of", "strings"};
	String selectedName = ListDialog.showDialog(
								componentInControllingFrame,
								locatorComponent,
								"A description of the list:",
								"Dialog Title",
								choices,
								choices[0]);
 * </pre>
 */
public class ListDialog extends JDialog
						implements ActionListener, Base {

	private String value = null;
	private int index    = -1;
	private JList<Object> list;
	private HelpUI  helpUI;
	private List<String> alternateReturn;
	private Frame frame;
	private InterfaceParam param;
	private boolean showHelp = false;

	/**
	 * Set up the dialog.  The first Component argument
	 * determines which frame the dialog depends on; it should be
	 * a component in the dialog's controlling frame. The second
	 * Component argument should be null if you want the dialog
	 * to come up with its left corner in the center of the screen;
	 * otherwise, it should be the component on top of which the
	 * dialog should appear.
	 */	
	public ListDialog(
			BasePanel frameComp,
			Component locationComp,
			String labelText,
			String title,
			String[] possibleValues,
			String initialValue,
			String longValue,
			int width, int height) { 
		this(frameComp,  locationComp, labelText, title, possibleValues,
				 initialValue, longValue, false, width, height, null, null, null, null);
	}
	public String showDialog() { // Can only be called once.
		value = null;
		index = -1;
		setVisible(true);
		if (alternateReturn != null && index >= 0) {
			index = Math.max(0,  index);
			value = alternateReturn.get(index);
		}
		return value;
	}
	private void setValue(String newValue) {
		value = newValue;
		list.setSelectedValue(value, true);
		index = Math.max(0, list.getSelectedIndex());
	}

	public ListDialog(BasePanel frameComp,
					   Component locationComp,
					   String labelText,
					   String title,
					   Object[] data,
					   String initialValue,
					   String longValue,
					   boolean isVerticalWrap,
					   int width, int height,
					   Font listFont,
					   InterfacePreview panel,
					   List<String> alternateReturn,
					   InterfaceParam param) {

		super(JOptionPane.getFrameForComponent(frameComp.getParent()), title, true);
		frame = JOptionPane.getFrameForComponent(frameComp.getParent());
		this.alternateReturn = alternateReturn;
		this.param = param;

		int topInset  = RotPUI.scaledSize(6);
		int sideInset = RotPUI.scaledSize(15);
		//Create and initialize the buttons.
		final JButton helpButton = new JButton("Help");
		helpButton.setMargin(new Insets(topInset, sideInset, 0, sideInset));
		helpButton.setFont(narrowFont(15));
		helpButton.setVerticalAlignment(SwingConstants.TOP);
		helpButton.setBackground(GameUI.buttonBackgroundColor());
		helpButton.setForeground(GameUI.buttonTextColor());
		helpButton.setActionCommand("Help");
		helpButton.addActionListener(this);
		//
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setMargin(new Insets(topInset, sideInset, 0, sideInset));
		cancelButton.setFont(narrowFont(15));
		cancelButton.setVerticalAlignment(SwingConstants.TOP);
		cancelButton.setBackground(GameUI.buttonBackgroundColor());
		cancelButton.setForeground(GameUI.buttonTextColor());
		cancelButton.addActionListener(this);
		//
		final JButton setButton = new JButton("Set");
		setButton.setMargin(new Insets(topInset, sideInset, 0, sideInset));
		setButton.setFont(narrowFont(15));
		setButton.setVerticalAlignment(SwingConstants.TOP);
		setButton.setBackground(GameUI.buttonBackgroundColor());
		setButton.setForeground(GameUI.buttonTextColor());
		setButton.setActionCommand("Set");
		setButton.addActionListener(this);
		getRootPane().setDefaultButton(setButton);

		//main part of the dialog
		list = new JList<Object>(data) {
			//Subclass JList to workaround bug 4832765, which can cause the
			//scroll pane to not let the user easily scroll up to the beginning
			//of the list.  An alternative would be to set the unitIncrement
			//of the JScrollBar to a fixed value. You wouldn't get the nice
			//aligned scrolling, but it should work.
			@Override
			public int getScrollableUnitIncrement(Rectangle visibleRect,
												  int orientation,
												  int direction) {
				int row;
				if (orientation == SwingConstants.VERTICAL &&
					  direction < 0 && (row = getFirstVisibleIndex()) != -1) {
					Rectangle r = getCellBounds(row, row);
					if ((r.y == visibleRect.y) && (row != 0))  {
						Point loc = r.getLocation();
						loc.y--;
						int prevIndex = locationToIndex(loc);
						Rectangle prevR = getCellBounds(prevIndex, prevIndex);

						if (prevR == null || prevR.y >= r.y) {
							return 0;
						}
						return prevR.height;
					}
				}
				return super.getScrollableUnitIncrement(
								visibleRect, orientation, direction);
			}
		};

		if (listFont == null)
			list.setFont(narrowFont(14));
		else {
			list.setFont(listFont);
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
		}
		list.addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		    	if (list.getSelectedValue() != null) {
	    			index = Math.max(0, list.getSelectedIndex());
		    		if (panel != null) { // For Preview
		    			if (alternateReturn != null) {
			    			value = alternateReturn.get(index);
			    			panel.preview(value);
			    		}
			    		else
			    			panel.preview((String) list.getSelectedValue());
		    		}
		    		if (showHelp && param != null) { // For Help
		    			showHelp(index);
		    		}
		    	}
		    }
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		if (longValue != null)
			list.setPrototypeCellValue(longValue); //get extra space
		if (isVerticalWrap)
			list.setLayoutOrientation(JList.VERTICAL_WRAP);
		else
			list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		list.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					setButton.doClick(); //emulate button click
				}
			}
		});
		list.setBackground(GameUI.setupFrame());
		list.setForeground(Color.BLACK);
		list.setSelectionBackground(GameUI.borderMidColor());
		list.setSelectionForeground(Color.WHITE);

		if (width<=0)
			width = 300;
		if (height<=0)
			height = 150;
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(width, height));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		listScroller.getVerticalScrollBar().setBackground(GameUI.borderMidColor());
		listScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = GameUI.borderDarkColor();
		    }
		});
		listScroller.getHorizontalScrollBar().setBackground(GameUI.borderMidColor());
		listScroller.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = GameUI.borderDarkColor();
		    }
		});
		
		//Create a container so that we can add a title around
		//the scroll pane.  Can't add a title directly to the
		//scroll pane because its background would be white.
		//Lay out the label and scroll pane from top to bottom.
		JPanel listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel(labelText);
		label.setFont(narrowFont(14));
		label.setLabelFor(list);
		label.setForeground(Color.BLACK);

		//listPane.setFont(narrowFont(20));
		listPane.setBackground(GameUI.borderMidColor());
		//listPane.setForeground(Color.BLACK);
		listPane.add(label);
		listPane.add(Box.createRigidArea(new Dimension(0,5)));
		listPane.add(listScroller);
		listPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		//Lay out the buttons from left to right.
		JPanel buttonPane = new JPanel();
		buttonPane.setFont(narrowFont(15));
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.setBackground(GameUI.borderMidColor());
		buttonPane.setForeground(Color.WHITE);

		buttonPane.add(helpButton);
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(setButton);

		//Put everything together, using the content pane's BorderLayout.
		Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);

		//Initialize values.
		setValue(initialValue);
		pack();

		setSize(width, height);
		if (listFont != null)
			setLocation(RotPUI.scaledSize(250), RotPUI.scaledSize(200));
		else
			setLocationRelativeTo(locationComp);			
	}

	//Handle clicks on the Set and Cancel buttons.
	@Override public void actionPerformed(ActionEvent e) {
		if ("Help".equals(e.getActionCommand())) {
			showHelp = !showHelp;
			if (showHelp)
				showHelp(index);
			else
				clearHelp();
			return;
		}		
		if ("Set".equals(e.getActionCommand())) {
			index = list.getSelectedIndex();
			value = (String)(list.getSelectedValue());
			clearHelp();
			dispose();
			frame.repaint();
			return;
		}
		if ("Cancel".equals(e.getActionCommand())) {
			clearHelp();
			dispose();
			frame.repaint();
			return;
		}
	}
	private void clearHelp() {
		if (helpUI != null) {
			helpUI.clear();
			helpUI = null;
		}
		frame.paintComponents(frame.getGraphics());
	}
	private void showHelp(int idx) {
		clearHelp();
		Rectangle dest	= getBounds();
		int maxWidth	= scaled(300);
		String text		= "No Help Yet";
		if (param != null)
			text = param.dialogHelp(list.getSelectedIndex());
		helpUI = RotPUI.helpUI();
		helpUI.clear();
		HelpSpec sp = helpUI.addBrownHelpText(0, 0, maxWidth, 1, text);
		sp.autoSize(frame);
		sp.autoPosition(dest);
		helpUI.paintComponent(frame.getGraphics());
	}
	private void showHelp() {
		clearHelp();
		Rectangle dest	= getBounds();
		int	  maxWidth	= scaled(300);
		String	  text	= "None";
		if (param != null)
			text = param.getFullHelp();
		helpUI	= RotPUI.helpUI();
		helpUI.clear();
		HelpSpec sp = helpUI.addBrownHelpText(0, 0, maxWidth, 1, text);
		sp.autoSize(frame);
		sp.autoPosition(dest);
		helpUI.paintComponent(frame.getGraphics());
	}
}