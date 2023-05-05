package rotp.ui.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseWheelEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import rotp.model.empires.Empire;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.game.GovernorOptions;
import rotp.model.game.MOO1GameOptions;
import rotp.model.game.MOO1GameOptions.NewOptionsListener;
import rotp.ui.RotPUI;
import rotp.ui.game.GameUI;
import rotp.ui.races.RacesUI;
import rotp.util.FontManager;

/**
 * Produced using Netbeans Swing GUI builder.
 */
public class GovernorOptionsPanel extends javax.swing.JPanel implements NewOptionsListener{

	private final Font	valueFont		= FontManager.current().narrowFont(13);
	private final Font	baseFont		= FontManager.current().narrowFont(14);
	private final Font	labelFont		= FontManager.current().narrowFont(14);
	private final Font	buttonFont		= FontManager.current().narrowFont(15);
	private final Font	titleFont		= FontManager.current().narrowFont(21);
	private Color	buttonColor			= new Color(93,75,66);
	private Color	buttonOffColor		= buttonColor;
	private Color	buttonOnColor		= new Color(131,95,70);
	private Color	buttonTextColor		= SystemPanel.whiteText;
	private Color	buttonTextOnColor	= SystemPanel.blackText;
	private Color	buttonBorderColor	= SystemPanel.whiteText;;
	private Color	valueBgColor		= multColor(RacesUI.lightBrown, 1.2f);
	private Color	paneColor			= new Color(150,105,73);
	private Color	textBgColor			= paneColor;
	private Color	textColor			= SystemPanel.blackText;
	private Color	titleColor			= SystemPanel.whiteText;
	private int		buttonBorder		= 2;
	private int		buttonTopInset		= 6;
	private int		buttonSideInset		= 10;
	private int		buttonWidth			= 150;

	private final JFrame frame;
	private boolean autoApply = true;
    private void testColor() {
    	buttonColor		= new Color(93,75,66);
    	buttonOffColor	= new Color(93,75,66);
    	buttonOnColor	= new Color(131,95,70);
    	buttonTextColor	= SystemPanel.whiteText;
    	valueBgColor	= multColor(RacesUI.lightBrown, 1.2f);
    	paneColor		= new Color(150,105,73);
    	textBgColor		= paneColor;
    	textColor		= SystemPanel.blackText;
    	titleColor		= SystemPanel.whiteText;
    	buttonTextOnColor	= SystemPanel.blackText;
    	updatePanel(frame, true, true, false, 0);
    }

	public GovernorOptionsPanel(JFrame frame) {
        this.frame = frame;
        initComponents();
        setPanelSize(717, 575);
        updatePanel(frame, true, true, false, 0);
        // initial values
        loadValues();
        updatePanel(frame, true, true, false, 0);
        MOO1GameOptions.addListener(this);
    }
    @Override public void optionLoaded() { loadValues();  } 
    public void newStyle() { updatePanel(frame, true, true, false, 0); } 
    private static Color multColor		(Color offColor, float factor) {
    	factor /= 255f;
    	return new Color(Math.min(1f, offColor.getRed()   * factor),
    					 Math.min(1f, offColor.getGreen() * factor),
    					 Math.min(1f, offColor.getBlue()  * factor));
    }
    private void setBasicArrowButton	(Component c, boolean color, boolean font, boolean debug) {
       	
    }
    private void setJButton				(Component c, boolean color, boolean font, boolean debug) {
    	JButton button = (JButton) c;
    	if (color) {
    		button.setBackground(buttonColor);
    		button.setForeground(buttonTextColor);
    	}
    	if (font) {
    		int topInset  = RotPUI.scaledSize(buttonTopInset);
    		int sideInset = RotPUI.scaledSize(buttonSideInset);
    		setBorder(new LineBorder(buttonBorderColor, RotPUI.scaledSize(buttonBorder), true));
    		button.setMargin(new Insets(topInset, sideInset, 0, sideInset));
    		button.setFont(buttonFont);
    	}
    }
    private void setJCheckBox			(Component c, boolean color, boolean font, boolean debug) {
    	JCheckBox box = (JCheckBox) c;
    	if (color) {
    		box.setBackground(textBgColor);
    		box.setForeground(textColor);
    	}
    	if (font)
    		box.setFont(baseFont);
    }
    private void setJRadioButton		(Component c, boolean color, boolean font, boolean debug) {
    	JRadioButton button = (JRadioButton) c;
    	if (color) {
    		button.setBackground(textBgColor);
    		button.setForeground(textColor);
    	}
    	if (font)
    		button.setFont(baseFont);
    }
    private void setJToggleButton		(Component c, boolean color, boolean font, boolean debug) {
    	JToggleButton button = (JToggleButton) c;
    	if (color) {
    		button.setBackground(buttonColor);
    		button.setForeground(buttonTextColor);
    	}
    	if (font) {
    		int topInset  = RotPUI.scaledSize(6);
    		int sideInset = RotPUI.scaledSize(10);
    		button.setMargin(new Insets(topInset, sideInset, 0, sideInset));
    		button.setFont(buttonFont);
    	}
    }
    private void setJSpinner			(Component c, boolean color, boolean font, boolean debug) {
    	JSpinner spinner = (JSpinner) c;
    	if (color) {
    		spinner.setBackground(valueBgColor);
    		spinner.setForeground(textColor);
    	}
    	if (font) {
    		int topInset  = RotPUI.scaledSize(4);
    		int sideInset = RotPUI.scaledSize(6);
    		//spinner.getBorder().getBorderInsets(c).set(topInset, sideInset, 0, sideInset);
    		spinner.setFont(valueFont);
    	}       	
    }
    private void setJLabel				(Component c, boolean color, boolean font, boolean debug) {
    	JLabel label = (JLabel) c;
    	if (color) {
    		label.setBackground(textBgColor);
    		label.setForeground(textColor);
    	}
    	if (font)
    		label.setFont(labelFont);
    }
    private void setJFormattedTextField	(Component c, boolean color, boolean font, boolean debug) {
    	JFormattedTextField txt = (JFormattedTextField) c;
    	if (color) {
    		txt.setBackground(valueBgColor);
    		txt.setForeground(SystemPanel.blackText);
    	}
    	if (font) {
    		int topInset  = RotPUI.scaledSize(6);
    		int sideInset = RotPUI.scaledSize(10);
    		txt.setMargin(new Insets(topInset, sideInset, 0, sideInset));
    		txt.setFont(valueFont);
    	}
    }
    private void setNumberEditor		(Component c, boolean color, boolean font, boolean debug) {
       	
    }
    private void setJPanel				(Component c, boolean color, boolean font, boolean debug) {
    	JPanel pane = (JPanel) c;
    	if (color)
    		pane.setBackground(paneColor);
    	if (font)
    		pane.setFont(baseFont);
    	Border b = pane.getBorder();
    	if (b != null && b instanceof TitledBorder) {
        	TitledBorder border = (TitledBorder) b;
        	if (color)
        		border.setTitleColor(titleColor);
        	if (font)
        		border.setTitleFont(titleFont);
    	}
    }
    private void setJLayeredPane		(Component c, boolean color, boolean font, boolean debug) {
//    	JLayeredPane pane = (JLayeredPane) c;
//    	if (color)
//    		pane.setBackground(PaneColor);
//    	if (font)
//    		pane.setFont(baseFont);
     }
    private void setJRootPane			(Component c, boolean color, boolean font, boolean debug) {
//    	JRootPane pane = (JRootPane) c;
//    	if (color)
//    		pane.setBackground(PaneColor);
//    	if (font)
//    		pane.setFont(baseFont);
    }
    private	void updatePanel(Container parent, boolean color, boolean font, boolean debug, int k) {
    	for (Component c : parent.getComponents()) {
    		if (c instanceof BasicArrowButton) {
    			if (debug) System.out.println("BasicArrowButton : " + k + " -- " + c.toString());
    			setBasicArrowButton(c, color, font, debug);
        	} 
        	else if (c instanceof JButton) {
        		if (debug) System.out.println("JButton : " + k + " -- " + c.toString());
        		setJButton(c, color, font, debug);
        	}
        	else if (c instanceof JCheckBox) {
        		if (debug) System.out.println("JCheckBox : " + k + " -- " + c.toString());
        		setJCheckBox(c, color, font, debug);
        	}
        	else if (c instanceof JRadioButton) {
        		if (debug) System.out.println("JRadioButton : " + k + " -- " + c.toString());
        		setJRadioButton(c, color, font, debug);
        	}
        	else if (c instanceof JToggleButton) {
        		if (debug) System.out.println("JToggleButton : " + k + " -- " + c.toString());
        		setJToggleButton(c, color, font, debug);
        	}
        	else if (c instanceof JSpinner) {
        		if (debug) System.out.println("JSpinner : " + k + " -- " + c.toString());
        		setJSpinner(c, color, font, debug);
        	}
        	else if (c instanceof JLabel) {
        		if (debug) System.out.println("JLabel : " + k + " -- " + c.toString());
        		setJLabel(c, color, font, debug);
        	}
        	else if (c instanceof JFormattedTextField) {
        		if (debug) System.out.println("JFormattedTextField : " + k + " -- " + c.toString());
        		setJFormattedTextField(c, color, font, debug);
        	}
        	else if (c instanceof NumberEditor) {
        		if (debug) System.out.println("NumberEditor : " + k + " -- " + c.toString());
        		setNumberEditor(c, color, font, debug);
        	}
        	else if (c instanceof JPanel) {
        		if (debug) System.out.println("JPanel : " + k + " -- " + c.toString());
        		setJPanel(c, color, font, debug);
        	}
        	else if (c instanceof JLayeredPane) {
        		if (debug) System.out.println("JLayeredPane : " + k + " -- " + c.toString());
        		setJLayeredPane(c, color, font, debug);
        	}
        	else if (c instanceof JRootPane) {
        		if (debug) System.out.println("JRootPane : " + k + " -- " + c.toString());
        		setJRootPane(c, color, font, debug);
        	}
        	else {
        		if (debug) System.out.println("-- " + k + " -- " + c.toString());
        	}
            if (c instanceof Container) {
            	updatePanel((Container)c, color, font, debug, k+1);
            }
        }
    	setAutoApplyColors();
    	repaint();
    }
    private void setPanelSize(int width, int height) {
    	setPreferredSize(new Dimension(RotPUI.scaledSize(width), RotPUI.scaledSize(height)));
    }
    private void setAutoApplyColors() {
    	if (autoApply) {
    		autoApplyToggleButton.setBackground(buttonOnColor);
    		autoApplyToggleButton.setForeground(buttonTextOnColor);
    	}
    	else {
    		autoApplyToggleButton.setBackground(buttonOffColor);
    		autoApplyToggleButton.setForeground(buttonTextColor);
       }
    }
    private void loadValues() {
        GovernorOptions options = GameSession.instance().getGovernorOptions();
        this.autoApply = options.isAutoApply();
        this.governorDefault.setSelected(options.isGovernorOnByDefault());
        this.legacyGrowthMode.setSelected(options.legacyGrowthMode());
        this.autotransport.setSelected(options.isAutotransport());
        this.autotransportXilmi.setSelected(options.isAutotransportXilmi());
        this.autoInfiltrate.setSelected(options.isAutoInfiltrate());
        this.autoSpy.setSelected(options.isAutoSpy());
        this.spareXenophobes.setSelected(options.isSpareXenophobes());
        this.allowUngoverned.setSelected(options.isAutotransportUngoverned());
        this.transportMaxTurns.setValue(options.getTransportMaxTurns());
        this.transportRichDisabled.setSelected(options.isTransportRichDisabled());
        this.transportPoorDouble.setSelected(options.isTransportPoorDouble());
        switch (GameSession.instance().getGovernorOptions().getGates()) {
            case None:
                this.stargateOff.setSelected(true);
                break;
            case Rich:
                this.stargateRich.setSelected(true);
                break;
            case All:
                this.stargateOn.setSelected(true);
                break;
        }
        this.missileBases.setValue(options.getMinimumMissileBases());
        this.shieldWithoutBases.setSelected(options.getShieldWithoutBases());
        this.autospend.setSelected(options.isAutospend());
        this.reserve.setValue(options.getReserve());
        this.shipbuilding.setSelected(options.isShipbuilding());
        this.autoScout.setSelected(options.isAutoScout());
        this.autoColonize.setSelected(options.isAutoColonize());
        this.completionist.setEnabled(isCompletionistEnabled());
        this.autoAttack.setSelected(options.isAutoAttack());
        this.autoScoutShipCount.setValue(options.getAutoScoutShipCount());
        this.autoColonyShipCount.setValue(options.getAutoColonyShipCount());
        this.autoAttackShipCount.setValue(options.getAutoAttackShipCount());
        setAutoApplyColors();
    }
    private boolean isCompletionistEnabled() {
        if (GameSession.instance().galaxy() == null) {
            return false;
        }
        float colonized = GameSession.instance().galaxy().numColonizedSystems() / (float)GameSession.instance().galaxy().numStarSystems();
        float controlled = GameSession.instance().galaxy().player().numColonies() / GameSession.instance().galaxy().numColonizedSystems();
        boolean completed = GameSession.instance().galaxy().player().tech().researchCompleted();
        // System.out.format("Colonized %.2f galaxy, controlled %.2f galaxy, completed research %s%n", colonized, controlled, completed);
        if (colonized >= 0.3 && controlled >= 0.5 && completed) {
            return true;
        } else {
            return false;
        }
    }
    private void performCompletionist() {
        // game not in session
        if (GameSession.instance().galaxy() == null) {
            return;
        }
        // Techs to give
        String techs[] = {
                "ImprovedTerraforming:8",
                "SoilEnrichment:1",
                "AtmosphereEnrichment:0",
                "ControlEnvironment:6",
                "Stargate:0"
        };
        for (Empire e: GameSession.instance().galaxy().empires()) {
            if (e.extinct()) {
                continue;
            }
            for (String t: techs) {
                e.tech().allowResearch(t);
            }
        }
    }
    private void applyAction() {// BR: 
    	GovernorOptions options = GameSession.instance().getGovernorOptions();
        options.setGovernorOnByDefault(governorDefault.isSelected());
        options.setLegacyGrowthMode(legacyGrowthMode.isSelected());
        options.setAutotransport(autotransport.isSelected());
        options.setAutotransportXilmi(autotransportXilmi.isSelected());
        options.setAutotransportUngoverned(allowUngoverned.isSelected());
        options.setTransportMaxTurns((Integer)transportMaxTurns.getValue());
        options.setTransportRichDisabled(transportRichDisabled.isSelected());
        options.setTransportPoorDouble(transportPoorDouble.isSelected());
        applyStargates();
        options.setMinimumMissileBases((Integer)missileBases.getValue());
        options.setShieldWithoutBases(shieldWithoutBases.isSelected());
        options.setAutospend(autospend.isSelected());
        options.setAutoInfiltrate(autoInfiltrate.isSelected());
        options.setAutoSpy(autoSpy.isSelected());
        options.setSpareXenophobes(spareXenophobes.isSelected());
        options.setReserve((Integer)reserve.getValue());
        options.setShipbuilding(shipbuilding.isSelected());
        options.setAutoScout(autoScout.isSelected());
        options.setAutoColonize(autoColonize.isSelected());
        options.setAutoAttack(autoAttack.isSelected());
        options.setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
        options.setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
        options.setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());
    }                                   
    private void applyStargates() {// BR: 
    	GovernorOptions options = GameSession.instance().getGovernorOptions();
        if (stargateOff.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.None);
        } else if (stargateRich.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.Rich);
        } else if (stargateOn.isSelected()) {
            options.setGates(GovernorOptions.GatesGovernor.All);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stargateOptions = new javax.swing.ButtonGroup();
        governorDefault = new javax.swing.JCheckBox();
        javax.swing.JPanel autotransportPanel = new javax.swing.JPanel();
        autotransport = new javax.swing.JCheckBox();
        transportMaxTurns = new javax.swing.JSpinner();
        javax.swing.JLabel transportMaxTurnsLabel = new javax.swing.JLabel();
        javax.swing.JLabel transportMaxTurnsNebula = new javax.swing.JLabel();
        transportRichDisabled = new javax.swing.JCheckBox();
        transportPoorDouble = new javax.swing.JCheckBox();
        autotransportXilmi = new javax.swing.JCheckBox();
        allowUngoverned = new javax.swing.JCheckBox();
        allGovernorsOn = new javax.swing.JButton();
        allGovernorsOff = new javax.swing.JButton();
        javax.swing.JPanel stargatePanel = new javax.swing.JPanel();
        stargateOff = new javax.swing.JRadioButton();
        stargateRich = new javax.swing.JRadioButton();
        stargateOn = new javax.swing.JRadioButton();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        missileBases = new javax.swing.JSpinner();
        missileBasesLabel = new javax.swing.JLabel();
        autospend = new javax.swing.JCheckBox();
        reserve = new javax.swing.JSpinner();
        resrveLabel = new javax.swing.JLabel();
        shipbuilding = new javax.swing.JCheckBox();
        autoScout = new javax.swing.JCheckBox();
        autoColonize = new javax.swing.JCheckBox();
        completionist = new javax.swing.JButton();
        autoAttack = new javax.swing.JCheckBox();
        autoColonyShipCount = new javax.swing.JSpinner();
        autoColonyShipCountLabel = new javax.swing.JLabel();
        autoScoutShipCount = new javax.swing.JSpinner();
        autoAttackShipCount = new javax.swing.JSpinner();
        autoScoutShipCountLabel = new javax.swing.JLabel();
        autoAttackShipCountLabel = new javax.swing.JLabel();
        shieldWithoutBases = new javax.swing.JCheckBox();
        legacyGrowthMode = new javax.swing.JCheckBox();
        autoSpy = new javax.swing.JCheckBox();
        autoInfiltrate = new javax.swing.JCheckBox();
        applyButton = new javax.swing.JButton();
        autoApplyToggleButton = new javax.swing.JButton();
        spareXenophobes = new javax.swing.JCheckBox();

        governorDefault.setText("Governor is on by default");

        autotransportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Autotransport Options"));

        autotransport.setText("Population automatically transported from colonies at max production capacity");
        autotransport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportActionPerformed(evt);
            }
        });

        transportMaxTurns.setModel(new javax.swing.SpinnerNumberModel(15, 1, 15, 1));
        transportMaxTurns.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transportMaxTurnsStateChanged(evt);
            }
        });
        transportMaxTurns.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsLabel.setText("Maximum transport distance in turns");
        transportMaxTurnsLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsLabelMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsNebula.setText("(1.5x higher distance when transporting to nebulae)");

        transportRichDisabled.setText("Don't send from Rich/Artifacts planets");
        transportRichDisabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportRichDisabledActionPerformed(evt);
            }
        });

        transportPoorDouble.setText("Send double from Poor planets");
        transportPoorDouble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportPoorDoubleActionPerformed(evt);
            }
        });

        autotransportXilmi.setText("Let AI handle population transportation");
        autotransportXilmi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportXilmiActionPerformed(evt);
            }
        });

        allowUngoverned.setText("allow sending population from ungoverned colonies");
        allowUngoverned.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allowUngovernedActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout autotransportPanelLayout = new javax.swing.GroupLayout(autotransportPanel);
        autotransportPanel.setLayout(autotransportPanelLayout);
        autotransportPanelLayout.setHorizontalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transportMaxTurnsLabel))
                    .addComponent(transportMaxTurnsNebula)
                    .addComponent(transportRichDisabled)
                    .addComponent(transportPoorDouble)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autotransport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(autotransportPanelLayout.createSequentialGroup()
                                .addComponent(autotransportXilmi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(187, 187, 187))
                            .addGroup(autotransportPanelLayout.createSequentialGroup()
                                .addComponent(allowUngoverned, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(187, 187, 187)))
                .addContainerGap(86, Short.MAX_VALUE))
        );
        autotransportPanelLayout.setVerticalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addComponent(autotransportXilmi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allowUngoverned)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transportMaxTurns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportMaxTurnsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportMaxTurnsNebula)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportRichDisabled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transportPoorDouble))
        );

        allGovernorsOn.setText("All Governors ON");
        allGovernorsOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOnActionPerformed(evt);
            }
        });

        allGovernorsOff.setText("All Governors OFF");
        allGovernorsOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOffActionPerformed(evt);
            }
        });

        stargatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stargate Options"));

        stargateOptions.add(stargateOff);
        stargateOff.setText("Never build stargates");
        stargateOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOffActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateRich);
        stargateRich.setText("Build stargates on Rich and Ultra Rich planets");
        stargateRich.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateRichActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateOn);
        stargateOn.setText("Always build stargates");
        stargateOn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout stargatePanelLayout = new javax.swing.GroupLayout(stargatePanel);
        stargatePanel.setLayout(stargatePanelLayout);
        stargatePanelLayout.setHorizontalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stargateOff)
                    .addComponent(stargateRich)
                    .addComponent(stargateOn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        stargatePanelLayout.setVerticalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addComponent(stargateOff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateRich)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stargateOn))
        );

        okButton.setText("OK");
        okButton.setToolTipText("Apply settings and close the GUI");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        missileBases.setModel(new javax.swing.SpinnerNumberModel(0, 0, 20, 1));
        missileBases.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                missileBasesStateChanged(evt);
            }
        });
        missileBases.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                missileBasesMouseWheelMoved(evt);
            }
        });

        missileBasesLabel.setText("Minimum missile bases");

        autospend.setText("Autospend");
        autospend.setToolTipText("Automatically spend reserve on planets with lowest production");
        autospend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autospendActionPerformed(evt);
            }
        });

        reserve.setModel(new javax.swing.SpinnerNumberModel(1000, 0, 100000, 10));
        reserve.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                reserveStateChanged(evt);
            }
        });
        reserve.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                reserveMouseWheelMoved(evt);
            }
        });

        resrveLabel.setText("Keep in reserve");

        shipbuilding.setText("Shipbuilding with Governor enabled");
        shipbuilding.setToolTipText("Divert resources into shipbuilding and not research if planet is already building ships");
        shipbuilding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shipbuildingActionPerformed(evt);
            }
        });

        autoScout.setText("Auto Scout");
        autoScout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoScoutActionPerformed(evt);
            }
        });

        autoColonize.setText("Auto Colonize");
        autoColonize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoColonizeActionPerformed(evt);
            }
        });

        completionist.setText("Completionist Technologies");
        completionist.setToolTipText("<html>\nI like completing games fully. <br/>\nAllow all Empires to Research the following Technologies:<br/>\n<br/>\nControlled Irradiated Environment<br/>\nAtmospheric Terraforming<br/>\nComplete Terraforming<br/>\nAdvanced Soil Enrichment<br/>\nIntergalactic Star Gates<br/>\n<br/>\nMore than 30% of the Galaxy needs to be colonized.<br/>\nPlayer must control more than 50% of colonized systems.<br/>\nPlayer must have completed all Research in their Tech Tree (Future Techs too).<br/>\n</html>");
        completionist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                completionistActionPerformed(evt);
            }
        });

        autoAttack.setText("Auto Attack");
        autoAttack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAttackActionPerformed(evt);
            }
        });

        autoColonyShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoColonyShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoColonyShipCountStateChanged(evt);
            }
        });
        autoColonyShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoColonyShipCountMouseWheelMoved(evt);
            }
        });

        autoColonyShipCountLabel.setText("Number of colony ships to send");

        autoScoutShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoScoutShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoScoutShipCountStateChanged(evt);
            }
        });
        autoScoutShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoScoutShipCountMouseWheelMoved(evt);
            }
        });

        autoAttackShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoAttackShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoAttackShipCountStateChanged(evt);
            }
        });
        autoAttackShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoAttackShipCountMouseWheelMoved(evt);
            }
        });

        autoScoutShipCountLabel.setText("Number of scout ships to send");

        autoAttackShipCountLabel.setText("Number of attack ships to send");

        shieldWithoutBases.setText("Allow shields without bases");
        shieldWithoutBases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shieldWithoutBasesActionPerformed(evt);
            }
        });

        legacyGrowthMode.setText("Develop colonies as quickly as possible");
        legacyGrowthMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                legacyGrowthModeActionPerformed(evt);
            }
        });

        autoSpy.setText("Let AI handle spies");
        autoSpy.setToolTipText("Hand control over spies to AI");
        autoSpy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSpyActionPerformed(evt);
            }
        });

        autoInfiltrate.setText("Autoinfiltrate");
        autoInfiltrate.setToolTipText("Automatically sends spies to infiltrate other empires");
        autoInfiltrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoInfiltrateActionPerformed(evt);
            }
        });

        applyButton.setText("Apply");
        applyButton.setToolTipText("Apply settings and keep GUI open");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        autoApplyToggleButton.setText("Auto Apply");
        autoApplyToggleButton.setToolTipText("For the settings to be applied live.");
        autoApplyToggleButton.setFocusPainted(false);
        autoApplyToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoApplyToggleButtonActionPerformed(evt);
            }
        });

        spareXenophobes.setText("Spare the Xenophobes");
        spareXenophobes.setToolTipText("Once framed by xenophobic empire: stop spying and infiltration to avoid further outrage");
        spareXenophobes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spareXenophobesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autotransportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stargatePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(allGovernorsOn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(allGovernorsOff))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(governorDefault)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(autoColonize)
                                    .addComponent(autoScout)
                                    .addComponent(autoAttack))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(autoAttackShipCount)
                                    .addComponent(autoScoutShipCount)
                                    .addComponent(autoColonyShipCount))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(autoColonyShipCountLabel)
                                    .addComponent(autoScoutShipCountLabel)
                                    .addComponent(autoAttackShipCountLabel))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(shipbuilding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(autoInfiltrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(legacyGrowthMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(193, 193, 193))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(autoSpy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(9, 9, 9)
                                        .addComponent(spareXenophobes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(completionist)
                                        .addGap(8, 8, 8))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(autospend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(resrveLabel))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(missileBasesLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(shieldWithoutBases)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(okButton)
                        .addGap(18, 18, 18)
                        .addComponent(applyButton)
                        .addGap(18, 18, 18)
                        .addComponent(autoApplyToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(governorDefault)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allGovernorsOn)
                    .addComponent(allGovernorsOff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stargatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoScout)
                    .addComponent(autoScoutShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoScoutShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoColonize)
                    .addComponent(autoColonyShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoColonyShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoAttack)
                    .addComponent(autoAttackShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoAttackShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(missileBasesLabel)
                    .addComponent(shieldWithoutBases))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autospend)
                    .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(resrveLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shipbuilding)
                    .addComponent(legacyGrowthMode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(completionist)
                    .addComponent(autoSpy)
                    .addComponent(autoInfiltrate)
                    .addComponent(spareXenophobes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton)
                    .addComponent(applyButton)
                    .addComponent(autoApplyToggleButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void allGovernorsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOnActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(true);
            ss.colony().governIfNeeded();
            if (autoApply) { // BR:
                GovernorOptions options = GameSession.instance().getGovernorOptions();
                options.setGovernorOnByDefault(governorDefault.isSelected());
            }
        }
    }//GEN-LAST:event_allGovernorsOnActionPerformed

    private void allGovernorsOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOffActionPerformed
        for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
            if (!ss.isColonized()) {
                // shouldn't happen
                continue;
            }
            ss.colony().setGovernor(false);
            if (autoApply) { // BR:
                GovernorOptions options = GameSession.instance().getGovernorOptions();
                options.setGovernorOnByDefault(governorDefault.isSelected());
            }
        }
    }//GEN-LAST:event_allGovernorsOffActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
    	applyAction();
    	frame.setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
    	applyAction();
    	testColor();
    }//GEN-LAST:event_applyButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        frame.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void missileBasesMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_missileBasesMouseWheelMoved
        mouseWheel(missileBases, evt);
    }//GEN-LAST:event_missileBasesMouseWheelMoved

    private void reserveMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_reserveMouseWheelMoved
        mouseWheel(reserve, evt);
    }//GEN-LAST:event_reserveMouseWheelMoved

    private void completionistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_completionistActionPerformed
        performCompletionist();
    }//GEN-LAST:event_completionistActionPerformed

    private void autoColonyShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoColonyShipCountMouseWheelMoved
        mouseWheel(autoColonyShipCount, evt);
    }//GEN-LAST:event_autoColonyShipCountMouseWheelMoved

    private void autoScoutShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoScoutShipCountMouseWheelMoved
        mouseWheel(autoScoutShipCount, evt);
    }//GEN-LAST:event_autoScoutShipCountMouseWheelMoved

    private void autoAttackShipCountMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_autoAttackShipCountMouseWheelMoved
        mouseWheel(autoAttackShipCount, evt);
    }//GEN-LAST:event_autoAttackShipCountMouseWheelMoved

    private void autotransportXilmiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autotransportXilmiActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransportXilmi(autotransportXilmi.isSelected());
        }
    }//GEN-LAST:event_autotransportXilmiActionPerformed

    private void transportMaxTurnsLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsLabelMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsLabelMouseWheelMoved

    private void transportMaxTurnsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsMouseWheelMoved
        mouseWheel(transportMaxTurns, evt);
    }//GEN-LAST:event_transportMaxTurnsMouseWheelMoved

    private void autoApplyToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoApplyToggleButtonActionPerformed
        autoApply = !autoApply;
    	GovernorOptions options = GameSession.instance().getGovernorOptions();
        options.setAutoApply(autoApply);
        if (autoApply) // BR:
        	applyAction();
        setAutoApplyColors();
    }//GEN-LAST:event_autoApplyToggleButtonActionPerformed

    private void allowUngovernedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowUngovernedActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransportUngoverned(allowUngoverned.isSelected());
        }
    }//GEN-LAST:event_allowUngovernedActionPerformed

    private void autotransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autotransportActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutotransport(autotransport.isSelected());
        }
    }//GEN-LAST:event_autotransportActionPerformed

    private void transportRichDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportRichDisabledActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportRichDisabled(transportRichDisabled.isSelected());
        }
    }//GEN-LAST:event_transportRichDisabledActionPerformed

    private void transportPoorDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportPoorDoubleActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportPoorDouble(transportPoorDouble.isSelected());
        }
    }//GEN-LAST:event_transportPoorDoubleActionPerformed

    private void autoScoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoScoutActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoScout(autoScout.isSelected());
        }
    }//GEN-LAST:event_autoScoutActionPerformed

    private void autoColonizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoColonizeActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoColonize(autoColonize.isSelected());
        }
    }//GEN-LAST:event_autoColonizeActionPerformed

    private void autoAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAttackActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoAttack(autoAttack.isSelected());
        }
    }//GEN-LAST:event_autoAttackActionPerformed

    private void shieldWithoutBasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shieldWithoutBasesActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setShieldWithoutBases(shieldWithoutBases.isSelected());
        }
    }//GEN-LAST:event_shieldWithoutBasesActionPerformed

    private void autospendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autospendActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutospend(autospend.isSelected());
        }
    }//GEN-LAST:event_autospendActionPerformed

    private void shipbuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipbuildingActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setShipbuilding(shipbuilding.isSelected());
        }
    }//GEN-LAST:event_shipbuildingActionPerformed

    private void autoInfiltrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoInfiltrateActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoInfiltrate(autoInfiltrate.isSelected());
        }
    }//GEN-LAST:event_autoInfiltrateActionPerformed

    private void legacyGrowthModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legacyGrowthModeActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setLegacyGrowthMode(legacyGrowthMode.isSelected());
        }
    }//GEN-LAST:event_legacyGrowthModeActionPerformed

    private void autoSpyActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoSpy(autoSpy.isSelected());
        }
    }                                       

    private void spareXenophobesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSpyActionPerformed
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setSpareXenophobes(spareXenophobes.isSelected());
        }
    }//GEN-LAST:event_autoSpyActionPerformed

    private void stargateOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOffActionPerformed
        if (autoApply) applyStargates(); // BR:
    }//GEN-LAST:event_stargateOffActionPerformed

    private void stargateRichActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateRichActionPerformed
        if (autoApply) applyStargates(); // BR:
    }//GEN-LAST:event_stargateRichActionPerformed

    private void stargateOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOnActionPerformed
        if (autoApply) applyStargates(); // BR:
    }//GEN-LAST:event_stargateOnActionPerformed

    private void reserveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_reserveStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setReserve((Integer)reserve.getValue());
        }
    }//GEN-LAST:event_reserveStateChanged

    private void missileBasesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_missileBasesStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setMinimumMissileBases((Integer)missileBases.getValue());
        }
    }//GEN-LAST:event_missileBasesStateChanged

    private void autoAttackShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoAttackShipCountStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());
        }
    }//GEN-LAST:event_autoAttackShipCountStateChanged

    private void autoColonyShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoColonyShipCountStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
        }
    }//GEN-LAST:event_autoColonyShipCountStateChanged

    private void autoScoutShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoScoutShipCountStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
        }
   }//GEN-LAST:event_autoScoutShipCountStateChanged

    private void transportMaxTurnsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transportMaxTurnsStateChanged
        if (autoApply) { // BR:
            GovernorOptions options = GameSession.instance().getGovernorOptions();
            options.setTransportMaxTurns((Integer)transportMaxTurns.getValue());
        }
    }//GEN-LAST:event_transportMaxTurnsStateChanged

    private static void mouseWheel(JSpinner spinner, java.awt.event.MouseWheelEvent evt) {
        if (evt.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
            return;
        }
        SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
        int value = (int) model.getValue();
        // always scroll integers by 1
        value -= Math.signum(evt.getUnitsToScroll()) * model.getStepSize().intValue();
        int minimum = ((Number)model.getMinimum()).intValue();
        int maximum = ((Number)model.getMaximum()).intValue();
        if (value < minimum) {
            value = minimum;
        }
        if (value > maximum) {
            value = maximum;
        }
        spinner.setValue(value);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allGovernorsOff;
    private javax.swing.JButton allGovernorsOn;
    private javax.swing.JCheckBox allowUngoverned;
    private javax.swing.JButton applyButton;
    private javax.swing.JButton autoApplyToggleButton;
    private javax.swing.JCheckBox autoAttack;
    private javax.swing.JSpinner autoAttackShipCount;
    private javax.swing.JLabel autoAttackShipCountLabel;
    private javax.swing.JCheckBox autoColonize;
    private javax.swing.JSpinner autoColonyShipCount;
    private javax.swing.JLabel autoColonyShipCountLabel;
    private javax.swing.JCheckBox autoInfiltrate;
    private javax.swing.JCheckBox autoScout;
    private javax.swing.JSpinner autoScoutShipCount;
    private javax.swing.JLabel autoScoutShipCountLabel;
    private javax.swing.JCheckBox autoSpy;
    private javax.swing.JCheckBox autospend;
    private javax.swing.JCheckBox autotransport;
    private javax.swing.JCheckBox autotransportXilmi;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton completionist;
    private javax.swing.JCheckBox governorDefault;
    private javax.swing.JCheckBox legacyGrowthMode;
    private javax.swing.JSpinner missileBases;
    private javax.swing.JLabel missileBasesLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JSpinner reserve;
    private javax.swing.JLabel resrveLabel;
    private javax.swing.JCheckBox shieldWithoutBases;
    private javax.swing.JCheckBox shipbuilding;
    private javax.swing.JCheckBox spareXenophobes;
    private javax.swing.JRadioButton stargateOff;
    private javax.swing.JRadioButton stargateOn;
    private javax.swing.ButtonGroup stargateOptions;
    private javax.swing.JRadioButton stargateRich;
    private javax.swing.JSpinner transportMaxTurns;
    private javax.swing.JCheckBox transportPoorDouble;
    private javax.swing.JCheckBox transportRichDisabled;
    // End of variables declaration//GEN-END:variables

    // Just test the layout
    public static void main(String arg[]) {
        // initialize everything
        RotPUI.instance();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
			public void run() {
                JFrame frame = new JFrame("GovernorOptions");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                //Create and set up the content pane.
                GovernorOptionsPanel newContentPane = new GovernorOptionsPanel(frame);
                newContentPane.setOpaque(true); //content panes must be opaque
                frame.setContentPane(newContentPane);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });

    }
}
