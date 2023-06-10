package rotp.ui.main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalButtonUI;

import rotp.Rotp;
import rotp.model.empires.Empire;
import rotp.model.galaxy.StarSystem;
import rotp.model.game.GameSession;
import rotp.model.game.GovernorOptions;
import rotp.ui.RotPUI;
import rotp.ui.races.RacesUI;
import rotp.ui.util.swing.RotpJSpinner;
import rotp.ui.util.swing.RotpJSpinnerButton;
import rotp.util.FontManager;
/**
 * Produced using Netbeans Swing GUI builder.
 */
class GovernorOptionsPanel extends javax.swing.JPanel{
	
	private static final float	valueFontSize		= 14f;
	private static final float	baseFontSize		= 14f;
	private static final float	labelFontSize		= 14f;
	private static final float	buttonFontSize		= 18f;
	private static final float	panelTitleFontSize	= 20f;
	private static final float	baseIconSize		= 16f;
	private static final float	arrowWidthFactor	= 0.8f;
	private static final float	buttonCornerFactor	= 5f/18f;
	private static final int	buttonTopInset		= 6;
	private static final int	buttonSideInset		= 10;
	private static final int	animationStep		= 100; // ms
	private static final int	ANIMATION_STOPPED	= 0;
	private static final int	ANIMATION_ONGOING	= 1;
	private static final int	ANIMATION_CANCELED	= 2;
	private static final int	ANIMATION_RESET		= 3;
	

	private  Font	valueFont, baseFont, labelFont, buttonFont, panelTitleFont;
	private  Color	frameBgColor, panelBgColor, textBgColor, valueBgColor;
	private  Color	textColor, valueTextColor, panelTitleColor;
	private  Color	buttonColor, buttonTextColor, iconBgColor;
	private  Color	hiddenColor, disabledColor, hoverColor, borderColor;
	private  int	iconSize, arrowHeight, buttonCorner;
	private  Icon	iconCheckRadio		= new ScalableCheckBoxAndRadioButtonIcon();
	private  GovButtonUI rotpButtonUI	= new GovButtonUI();
//	private	Inset	iconInset			= new Insets(topInset, 2, 0, 2);
	
	// Display format variable, needed for reset purpose
	//
	private  Boolean	isNewFormat, isCustomSize;
	private  Integer	sizeFactorPct,	brightnessFactorPct;
	private  int		animationLive	= 0;
	private  boolean	updateOngoing	= false;

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(15); // no
    private ScheduledFuture<?> anim;
	private Runnable timedRefresh	= new Runnable() {
	    @Override public void run() {
	    	if (options().resetRequested() && options().isFullRefreshOnLoad() && frame.isVisible()) {
	    		//System.out.println("resetRequested() and executed (FullRefresh)");
	    		optionUpdate();
	    		animate();
	    		return;
	    	}
	    	if (options().resetRequested() && frame.isFocused()) {
	    		//System.out.println("resetRequested() and executed (isFocused)");
	    		optionUpdate();
	    		animate();
	    		return;
	    	}
	    	if (options().refreshRequested()) {
	    		loadDisplayValues();
	    		loadValues();
				options().clearRefresh();
	    		//System.out.println("refreshRequested() and executed");
	    	}
	    	if ((options().isFullRefreshOnLoad() && frame.isVisible())
	    			|| frame.isFocused()) {
	    		animate();
	    	}
	    }
	};
	private final JFrame frame;
	
	// ========== Public Method and Overrider ==========
	//
	private void optionUpdate() {
		if (!updateOngoing) {
			loadDisplayValues();
			loadValues();
			protectedReset();
		}
	} 
	void applyStyle() { protectedUpdatePanel(); }
	
	// ========== Protected initializers ==========
	// Loading and saving values occurring during these call
	// may trigger new-initializations 
	// The first call win!
	//
	private void protectedReset()		{
		if (!updateOngoing) {
			updateOngoing = true;
			if (animationLive != ANIMATION_STOPPED) {
				animationLive = ANIMATION_RESET;
			} 
			else {
				resetPanel();
			}
		}
	}
	private void protectedInitPanel()	{
		if (!updateOngoing) {
			updateOngoing = true;
			initPanel();
			updateOngoing = false;
		}
	}
	private void protectedUpdateColor() {
		if (!updateOngoing) {
			updateOngoing = true;
			initNewColors(true);
			updatePanel(frame, isNewFormat(), false, 0);
			updateOngoing = false;
			setRaceImg(); 	// Pack and set icon
		}
	}
	private void protectedUpdateSize()	{ protectedReset(); }
	private void protectedUpdatePanel() {
		if (!updateOngoing) {
			updateOngoing = true;
			initNewColors(true);
			initNewFonts();
			updatePanel(frame, isNewFormat(), false, 0);
			updateOngoing = false;
			setRaceImg(); 	// Pack and set icon
		}
	}

	// ========== Constructor and initializers ==========
	//
	GovernorOptionsPanel(JFrame frame) {
		this.frame = frame;
		frame.setVisible(true);
		protectedInitPanel();
	}
	private void initNewColors(boolean local) {
		if (isNewFormat()) {
			float brightness;
			if (local)
				brightness = (int)brightnessPct.getValue() /100f;
			else
				brightness = options().getBrightnessPct()/100f;
			frameBgColor	= multColor(new Color(93,  75,  66), brightness);
			panelBgColor	= multColor(new Color(150, 105, 73), brightness);
			textBgColor		= panelBgColor;
			valueBgColor	= multColor(RacesUI.lightBrown, 1.2f * brightness);
			
			buttonColor		= panelBgColor;
			borderColor		= multColor(valueBgColor, 1.2f);
			hiddenColor		= multColor(frameBgColor, 0.8f);
			disabledColor	= multColor(frameBgColor, 1.2f);
			hoverColor		= Color.yellow;
			
			buttonTextColor	= multColor(valueBgColor, 1.2f);
	
			textColor		= SystemPanel.blackText;
			valueTextColor	= SystemPanel.blackText;
			panelTitleColor	= SystemPanel.whiteText;
			iconBgColor		= valueBgColor;
		}
	}
	private void initNewFonts() {
		if (isNewFormat()) {
			iconSize	 	= (int) scaledSize(baseIconSize);
			buttonCorner 	= (int) scaledSize(buttonFontSize * buttonCornerFactor);
			arrowHeight 	= (int) scaledSize((valueFontSize+buttonTopInset) * arrowWidthFactor);
			valueFont		= FontManager.getNarrowFont(scaledSize(valueFontSize));
			baseFont		= FontManager.getNarrowFont(scaledSize(baseFontSize));
			labelFont		= FontManager.getNarrowFont(scaledSize(labelFontSize));
			buttonFont		= FontManager.getNarrowFont(scaledSize(buttonFontSize));
			panelTitleFont	= FontManager.getNarrowFont(scaledSize(panelTitleFontSize));
		}
	}
	private void initPanel() {
		initNewFonts();
		initNewColors(false);
		initComponents();	// Load the form
		loadValues();		// Load User's values
		updatePanel(frame, isNewFormat(), false, 0); // Apply the new formating
		setRaceImg();		// Pack and set icon
	}
	private void resetPanel() {
		updateOngoing = true;
		options().clearReset();

		boolean visible = frame.isVisible();
		frame.setVisible(false);
		//Remove the components before reloading
		Component[] componentList = getComponents();
		for(Component c : componentList){
			remove(c);
		}
		frame.revalidate();
		initPanel();
		frame.revalidate();
		updateOngoing = false;
		frame.setVisible(true);
		frame.setLocation(options().getPosition());
		frame.setVisible(visible);
		startAnimation();
	}
	private void loadDisplayValues() {
		isNewFormat			= !options().isOriginalPanel();
		isCustomSize		= options().isCustomSize();
		sizeFactorPct		= options().getSizeFactorPct();
		brightnessFactorPct	= options().getBrightnessPct();
	}
	// ========== Local tools ==========
	//
	private Color multColor(Color offColor, float factor) {
		factor /= 255f;
		return new Color(Math.min(1f, offColor.getRed()   * factor),
						 Math.min(1f, offColor.getGreen() * factor),
						 Math.min(1f, offColor.getBlue()  * factor));
	}
	private GovernorOptions options()			{ return GameSession.instance().getGovernorOptions(); }
	private boolean	isAutoApply()				{ return (options().isAutoApply()); }
	private boolean	isAnimatedImage()			{ return (options().isAnimatedImage()); }
	private boolean	isCustomSize()				{
		if (isCustomSize == null)
			isCustomSize = options().isCustomSize();
		return isCustomSize;
	}
	private boolean	isNewFormat()				{
		if (isNewFormat == null)
			isNewFormat = !options().isOriginalPanel();
		return isNewFormat;
	}
	private int		getSizeFactor()				{
		if (sizeFactorPct == null)
			sizeFactorPct = options().getSizeFactorPct();
		return sizeFactorPct;
	}
	private int		getBrightnessPct()			{
		if (brightnessFactorPct == null)
			brightnessFactorPct = options().getBrightnessPct();
		return brightnessFactorPct;
	}
	private int		scaledSize(int size)		{ return (int) (size * getFinalSizefactor()); }
	private float 	scaledSize(float size)		{ return size * getFinalSizefactor(); }
	private float 	getFinalSizefactor()		{
		if (isCustomSize())
			return Rotp.resizeAmt() * getSizeFactor()/100f;
		else
			return Rotp.resizeAmt();
	}
	private void	setCustomSize(boolean val)	{
		isCustomSize = val;
		if (isAutoApply())
			options().setIsCustomSize(isCustomSize);
	}
	private void	setBrightnessPct(int val)	{
		brightnessFactorPct = val;
		if (isAutoApply())
			options().setBrightnessPct(brightnessFactorPct);
	}
	private void	setSizeFactorPct(int pct)	{
		sizeFactorPct = pct;
		if (isAutoApply())
			options().setSizeFactorPct(pct);
	}
	private void	setNewFormat(boolean val)	{
		isNewFormat = val;
		if (isAutoApply())
			options().setIsOriginalPanel(!val);
	}

	// ========== Image display and animation ==========
	//
	private void stopAnimation() {
		if (animationLive == ANIMATION_ONGOING)
			animationLive = ANIMATION_CANCELED;
	}
	private void startAnimation() {
		if (anim == null)
			anim = executor.scheduleAtFixedRate(timedRefresh, 0, animationStep, TimeUnit.MILLISECONDS);
		if (updateOngoing)
			return;
		if (isAnimatedImage() && animationLive == ANIMATION_STOPPED) {
			animationLive = ANIMATION_ONGOING;
		}
	}
	private void animate() {
		if (animationLive == ANIMATION_RESET) {
			animationLive = ANIMATION_STOPPED;
			resetPanel(); // called by protected
			return;
		}
		if (isAnimatedImage() && animationLive == ANIMATION_ONGOING) {
			if (frame.isVisible()) {
				updateRaceImage();
				return;
			}
		}
		else {
			animationLive = ANIMATION_STOPPED;
		}
	}
	private void updateRaceImage() {
		BufferedImage raceImg = GameSession.instance().galaxy().player().race().setupImage();
		int srcWidth	= raceImg.getWidth();
		int srcHeight	= raceImg.getHeight();
		int margin		= scaledSize(0);
		int destWidth	= raceImage.getWidth()  - margin;
		int destHeight	= raceImage.getHeight() - margin;
		// Get New Size
		float fW = (float)srcWidth/destWidth;
		float fH = (float)srcHeight/destHeight;
		destWidth *= fW/fH;
		if (destWidth*destHeight == 0)
			return;
		raceImage.setSize(destWidth, destHeight);
		// Flip the Image
		BufferedImage flipped = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = flipped.getGraphics();
		g.drawImage(raceImg, 0, 0, destWidth, destHeight, srcWidth, 0, 0, srcHeight, null);
		raceImage.setIcon(new ImageIcon(flipped));
		repaint(raceImage.getBounds());
	}
	private void setRaceImg() {
		frame.pack(); // Should set a width!
		if (raceImage.getWidth() == 0)
			return;
		raceImage.setOpaque(false);
		updateRaceImage();
		frame.pack();
		startAnimation();
	}
	
	// ========== Update Panel Tools ==========
	//
	private void setBasicArrowButton	(Component c, boolean newFormat, boolean debug) {
		BasicArrowButton button = (BasicArrowButton) c;
		if (newFormat) {
			button.setBackground(frameBgColor);
		}
	}
	private void setRotpSpinnerButton	(Component c, boolean newFormat, boolean debug) {
		RotpJSpinnerButton button = (RotpJSpinnerButton) c;
		button.setFocusPainted(false);
		if (newFormat) {
			button.setBackground(null);
			button.setForeground(buttonTextColor);
			int topInset  = scaledSize(buttonTopInset);
			int sideInset = scaledSize(buttonSideInset);
			button.setMargin(new Insets(topInset, sideInset, -5, sideInset));
			button.setBorderPainted(false);
			button.setFocusPainted(false);
		}
	}
	private void setJButton				(Component c, boolean newFormat, boolean debug) {
		JButton button = (JButton) c;
		button.setFocusPainted(false);
		if (newFormat) {
			button.setUI(rotpButtonUI);
			button.setBackground(null);
			button.setForeground(buttonTextColor);
			int topInset  = scaledSize(buttonTopInset);
			int sideInset = scaledSize(buttonSideInset);
			button.setFont(buttonFont);
			button.setMargin(new Insets(topInset, sideInset, 0, sideInset));
			button.setIcon(new GovButtonIcon());
			button.setOpaque(true);
			button.setContentAreaFilled(false);
			button.setBorderPainted(false);
			button.setFocusPainted(false);
		}
	}
	private void setJCheckBox			(Component c, boolean newFormat, boolean debug) {
		JCheckBox box = (JCheckBox) c;
		box.setFocusPainted(false);
		if (newFormat) {
			box.setBackground(textBgColor);
			box.setForeground(textColor);
			box.setFont(baseFont);
			int topInset  = scaledSize(buttonTopInset);
			box.setMargin(new Insets(topInset, 2, 0, 2));
			box.setIcon(iconCheckRadio);
		}
	}
	private void setJRadioButton		(Component c, boolean newFormat, boolean debug) {
		JRadioButton button = (JRadioButton) c;
		button.setFocusPainted(false);
		if (newFormat) {
			button.setBackground(textBgColor);
			button.setForeground(textColor);
			button.setFont(baseFont);
			int topInset  = scaledSize(buttonTopInset);
			button.setMargin(new Insets(topInset, 2, 0, 2));
			button.setIcon(iconCheckRadio);
		}
	}
	private void setJToggleButton		(Component c, boolean newFormat, boolean debug) { }
	private void setJSpinner			(Component c, boolean newFormat, boolean debug) {
		GovernorJSpinner spinner = (GovernorJSpinner) c;
		if (newFormat) {
			spinner.getLayout();
			spinner.setBackground(valueBgColor);
			spinner.setForeground(textColor);
			spinner.setFont(valueFont);
			spinner.setBorder(null);
			spinner.centerText();
		}	   	
	}
	private void setJLabel				(Component c, boolean newFormat, boolean debug) {
		JLabel label = (JLabel) c;
		if (newFormat) {
			label.setBackground(textBgColor);
			label.setForeground(textColor);
			label.setFont(labelFont);
		}
	}
	private void setJFormattedTextField	(Component c, boolean newFormat, boolean debug) {
		JFormattedTextField txt = (JFormattedTextField) c;
		if (newFormat) {
			txt.setBackground(valueBgColor);
			txt.setForeground(valueTextColor);
			txt.setFont(valueFont);
			
		}
	}
	private void setNumberEditor		(Component c, boolean newFormat, boolean debug) {
		NumberEditor num = (NumberEditor) c;
		if (newFormat) {
			num.setBackground(valueBgColor);
			num.setForeground(valueTextColor);
			int topInset  = scaledSize(6);
			int sideInset = scaledSize(2);
			Border border = BorderFactory.createEmptyBorder(topInset, sideInset, 0, sideInset);
			num.setBorder(border);
		}
	}
	private void setJPanel				(Component c, boolean newFormat, boolean debug) {
		JPanel pane = (JPanel) c;
		if (newFormat) {
			pane.setBackground(panelBgColor);
			pane.setFont(baseFont);
			Border b = pane.getBorder();
			if (b != null && b instanceof TitledBorder) {
				TitledBorder border = (TitledBorder) b;
				border.setTitleColor(panelTitleColor);
				border.setTitleFont(panelTitleFont);
			}
		}
	}
	private void setJLayeredPane		(Component c, boolean newFormat, boolean debug) { }
	private void setJRootPane			(Component c, boolean newFormat, boolean debug) { }

	private	void updatePanel(Container parent, boolean newFormat, boolean debug, int k) {
		autoUpdatePanel(parent, newFormat, debug, k);
		specialUpdatePanel(parent, newFormat, debug, k);
	}
	private	void specialUpdatePanel(Container parent, boolean newFormat, boolean debug, int k) {
		if (newFormat) {
			setBackground(frameBgColor);
		}
		else {
			Component[] componentList = getComponents();
			for(Component c : componentList){
				if (c instanceof JPanel) {
					setBackground(c.getBackground());
					break;
				}
			}
		}
		setRaceImg();
	}
	private	void autoUpdatePanel(Container parent, boolean newFormat, boolean debug, int k) {
		for (Component c : parent.getComponents()) {
			if (c instanceof BasicArrowButton) {
				if (debug) System.out.println("BasicArrowButton : " + k + " -- " + c.toString());
				setBasicArrowButton(c, newFormat, debug);
			} 
			else if (c instanceof RotpJSpinnerButton) {
				if (debug) System.out.println("RotpSpinnerButton : " + k + " -- " + c.toString());
				setRotpSpinnerButton(c, newFormat, debug);
			}
			else if (c instanceof JButton) {
				if (debug) System.out.println("JButton : " + k + " -- " + c.toString());
				setJButton(c, newFormat, debug);
			}
			else if (c instanceof JCheckBox) {
				if (debug) System.out.println("JCheckBox : " + k + " -- " + c.toString());
				setJCheckBox(c, newFormat, debug);
			}
			else if (c instanceof JRadioButton) {
				if (debug) System.out.println("JRadioButton : " + k + " -- " + c.toString());
				setJRadioButton(c, newFormat, debug);
			}
			else if (c instanceof JToggleButton) {
				if (debug) System.out.println("JToggleButton : " + k + " -- " + c.toString());
				setJToggleButton(c, newFormat, debug);
			}
			else if (c instanceof JSpinner) {
				if (debug) System.out.println("JSpinner : " + k + " -- " + c.toString());
				setJSpinner(c, newFormat, debug);
			}
			else if (c instanceof JLabel) {
				if (debug) System.out.println("JLabel : " + k + " -- " + c.toString());
				setJLabel(c, newFormat, debug);
			}
			else if (c instanceof JFormattedTextField) {
				if (debug) System.out.println("JFormattedTextField : " + k + " -- " + c.toString());
				setJFormattedTextField(c, newFormat, debug);
			}
			else if (c instanceof NumberEditor) {
				if (debug) System.out.println("NumberEditor : " + k + " -- " + c.toString());
				setNumberEditor(c, newFormat, debug);
			}
			else if (c instanceof JPanel) {
				if (debug) System.out.println("JPanel : " + k + " -- " + c.toString());
				setJPanel(c, newFormat, debug);
			}
			else if (c instanceof JLayeredPane) {
				if (debug) System.out.println("JLayeredPane : " + k + " -- " + c.toString());
				setJLayeredPane(c, newFormat, debug);
			}
			else if (c instanceof JRootPane) {
				if (debug) System.out.println("JRootPane : " + k + " -- " + c.toString());
				setJRootPane(c, newFormat, debug);
			}
			else {
				if (debug) System.out.println("-- " + k + " -- " + c.toString());
			}
			if (c instanceof Container) {
				autoUpdatePanel((Container)c, newFormat, debug, k+1);
			}
		}
	}

	// ========== Load and save Values ==========
	//
	private void loadValues() {
		GovernorOptions options = options();
		// Other Options and duplicate
		this.governorDefault.setSelected(options.isGovernorOnByDefault());
		this.completionist.setEnabled(isCompletionistEnabled());
		this.autoApplyToggleButton.setSelected(isAutoApply());
		
		// AutoTransport Options
		this.autotransport.setSelected(options.isAutotransport());
		this.autotransportXilmi.setSelected(options.isAutotransportXilmi());
		this.allowUngoverned.setSelected(options.isAutotransportUngoverned());
		this.transportMaxTurns.setValue(options.getTransportMaxTurns());
		this.transportRichDisabled.setSelected(options.isTransportRichDisabled());
		this.transportPoorDouble.setSelected(options.isTransportPoorDouble());

		// StarGates Options
		switch (options().getGates()) {
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

		// Colony Options
		this.missileBases.setValue(options.getMinimumMissileBases());
		this.shieldWithoutBases.setSelected(options.getShieldWithoutBases());
		this.autospend.setSelected(options.isAutospend());
		this.reserve.setValue(options.getReserve());
		this.shipbuilding.setSelected(options.isShipbuilding());
		this.legacyGrowthMode.setSelected(options.legacyGrowthMode());

		// Intelligence Options
		this.autoInfiltrate.setSelected(options.isAutoInfiltrate());
		this.autoSpy.setSelected(options.isAutoSpy());
		this.spareXenophobes.setSelected(options.isSpareXenophobes());

		// Fleet Options
		this.autoScout.setSelected(options.isAutoScout());
		this.autoColonize.setSelected(options.isAutoColonize());
		this.autoAttack.setSelected(options.isAutoAttack());
		this.autoScoutShipCount.setValue(options.getAutoScoutShipCount());
		this.autoColonyShipCount.setValue(options.getAutoColonyShipCount());
		this.autoAttackShipCount.setValue(options.getAutoAttackShipCount());

		// Aspect Options
		this.customSize.setSelected(isCustomSize());
		this.sizePct.setValue(getSizeFactor());
		this.brightnessPct.setValue(getBrightnessPct());
		this.isOriginal.setSelected(!isNewFormat());
	}
	private void applyAction() {// BR: Save Values
		if (!isAutoApply())
			return;
		GovernorOptions options = options();
		
		// AutoTransport Options
		options.setAutotransport(autotransport.isSelected());
		options.setAutotransportXilmi(autotransportXilmi.isSelected());
		options.setAutotransportUngoverned(allowUngoverned.isSelected());
		options.setTransportMaxTurns((Integer)transportMaxTurns.getValue());
		options.setTransportRichDisabled(transportRichDisabled.isSelected());
		options.setTransportPoorDouble(transportPoorDouble.isSelected());

		// StarGates Options
		applyStargates();

		// Colony Options
		options.setMinimumMissileBases((Integer)missileBases.getValue());
		options.setShieldWithoutBases(shieldWithoutBases.isSelected());
		options.setAutospend(autospend.isSelected());
		options.setReserve((Integer)reserve.getValue());
		options.setShipbuilding(shipbuilding.isSelected());
		options.setLegacyGrowthMode(legacyGrowthMode.isSelected());

		// Intelligence Options
		options.setAutoInfiltrate(autoInfiltrate.isSelected());
		options.setAutoSpy(autoSpy.isSelected());
		options.setSpareXenophobes(spareXenophobes.isSelected());

		// Fleet Options
		options.setAutoScout(autoScout.isSelected());
		options.setAutoColonize(autoColonize.isSelected());
		options.setAutoAttack(autoAttack.isSelected());
		options.setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
		options.setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
		options.setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());

		// Aspect Options
		options.setIsOriginalPanel(isOriginal.isSelected());
		options.setIsCustomSize(customSize.isSelected());
		options.setSizeFactorPct((Integer)sizePct.getValue());
		options.setBrightnessPct((Integer)brightnessPct.getValue());
		// Other Options
		options.setGovernorOnByDefault(governorDefault.isSelected());
		options.setIsAnimatedImage(isAnimatedImage());
	}								   
	private void applyStargates() {// BR: 
		if (stargateOff.isSelected())
			options().setGates(GovernorOptions.GatesGovernor.None);
		else if (stargateRich.isSelected())
			options().setGates(GovernorOptions.GatesGovernor.Rich);
		else if (stargateOn.isSelected())
			options().setGates(GovernorOptions.GatesGovernor.All);
	}

	// ========== Completionist tools ==========
	//
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
        transportMaxTurns = new GovernorJSpinner();
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
        jLabel1 = new javax.swing.JLabel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        completionist = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        autoApplyToggleButton = new javax.swing.JCheckBox();
        javax.swing.JPanel fleetPanel = new javax.swing.JPanel();
        autoScout = new javax.swing.JCheckBox();
        autoColonize = new javax.swing.JCheckBox();
        autoAttack = new javax.swing.JCheckBox();
        autoColonyShipCount = new GovernorJSpinner();
        autoColonyShipCountLabel = new javax.swing.JLabel();
        autoScoutShipCount = new GovernorJSpinner();
        autoAttackShipCount = new GovernorJSpinner();
        autoScoutShipCountLabel = new javax.swing.JLabel();
        autoAttackShipCountLabel = new javax.swing.JLabel();
        javax.swing.JPanel colonyPanel = new javax.swing.JPanel();
        autospend = new javax.swing.JCheckBox();
        reserve = new GovernorJSpinner();
        reserveLabel = new javax.swing.JLabel();
        shipbuilding = new javax.swing.JCheckBox();
        shieldWithoutBases = new javax.swing.JCheckBox();
        legacyGrowthMode = new javax.swing.JCheckBox();
        missileBases = new GovernorJSpinner();
        missileBasesLabel = new javax.swing.JLabel();
        javax.swing.JPanel spyPanel = new javax.swing.JPanel();
        spareXenophobes = new javax.swing.JCheckBox();
        autoSpy = new javax.swing.JCheckBox();
        autoInfiltrate = new javax.swing.JCheckBox();
        jPanelAspect = new javax.swing.JPanel();
        isOriginal = new javax.swing.JCheckBox();
        customSize = new javax.swing.JCheckBox();
        sizePct = new GovernorJSpinner();
        sizeFactorLabel = new javax.swing.JLabel();
        brightnessPct = new GovernorJSpinner();
        brightnessLabel = new javax.swing.JLabel();
        raceImage = new javax.swing.JLabel();

        governorDefault.setSelected(true);
        governorDefault.setText("Governor is on by default");
        governorDefault.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                governorDefaultActionPerformed(evt);
            }
        });

        autotransportPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Autotransport Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        autotransport.setText("Population automatically transported from colonies at max production capacity");
        autotransport.setMinimumSize(new java.awt.Dimension(0, 0));
        autotransport.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportActionPerformed(evt);
            }
        });

        transportMaxTurns.setModel(new javax.swing.SpinnerNumberModel(15, 1, 15, 1));
        transportMaxTurns.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                transportMaxTurnsStateChanged(evt);
            }
        });
        transportMaxTurns.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsLabel.setText("Maximum transport distance in turns");
        transportMaxTurnsLabel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                transportMaxTurnsLabelMouseWheelMoved(evt);
            }
        });

        transportMaxTurnsNebula.setText("(1.5x higher distance when transporting to nebulae)");

        transportRichDisabled.setText("Don't send from Rich/Artifacts planets");
        transportRichDisabled.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportRichDisabledActionPerformed(evt);
            }
        });

        transportPoorDouble.setText("Send double from Poor planets");
        transportPoorDouble.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                transportPoorDoubleActionPerformed(evt);
            }
        });

        autotransportXilmi.setText("Let AI handle population transportation");
        autotransportXilmi.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autotransportXilmiActionPerformed(evt);
            }
        });

        allowUngoverned.setText("Allow sending population from ungoverned colonies");
        allowUngoverned.addActionListener(new java.awt.event.ActionListener() {
            @Override
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
                .addGroup(autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(transportPoorDouble)
                    .addComponent(transportMaxTurnsNebula)
                    .addGroup(autotransportPanelLayout.createSequentialGroup()
                        .addComponent(transportMaxTurns, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(transportMaxTurnsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(autotransportXilmi)
                    .addComponent(allowUngoverned)
                    .addComponent(autotransport, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transportRichDisabled))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        autotransportPanelLayout.setVerticalGroup(
            autotransportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(autotransportPanelLayout.createSequentialGroup()
                .addComponent(autotransportXilmi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(allowUngoverned)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autotransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOnActionPerformed(evt);
            }
        });

        allGovernorsOff.setText("All Governors OFF");
        allGovernorsOff.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                allGovernorsOffActionPerformed(evt);
            }
        });

        stargatePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Stargate Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        stargateOptions.add(stargateOff);
        stargateOff.setText("Never build stargates");
        stargateOff.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOffActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateRich);
        stargateRich.setText("Build stargates on Rich");
        stargateRich.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateRichActionPerformed(evt);
            }
        });

        stargateOptions.add(stargateOn);
        stargateOn.setText("Always build stargates");
        stargateOn.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                stargateOnActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("and Ultra Rich planets");

        javax.swing.GroupLayout stargatePanelLayout = new javax.swing.GroupLayout(stargatePanel);
        stargatePanel.setLayout(stargatePanelLayout);
        stargatePanelLayout.setHorizontalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(stargateOff)
                    .addComponent(stargateRich)
                    .addComponent(stargateOn)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        stargatePanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, stargateRich});

        stargatePanelLayout.setVerticalGroup(
            stargatePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(stargatePanelLayout.createSequentialGroup()
                .addComponent(stargateOff)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stargateRich)
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stargateOn)
                .addGap(0, 0, 0))
        );

        okButton.setText("OK");
        okButton.setToolTipText("Apply settings and close the GUI");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        completionist.setText("Completionist Technologies");
        completionist.setToolTipText("<html>\nI like completing games fully. <br/>\nAllow all Empires to Research the following Technologies:<br/>\n<br/>\nControlled Irradiated Environment<br/>\nAtmospheric Terraforming<br/>\nComplete Terraforming<br/>\nAdvanced Soil Enrichment<br/>\nIntergalactic Star Gates<br/>\n<br/>\nMore than 30% of the Galaxy needs to be colonized.<br/>\nPlayer must control more than 50% of colonized systems.<br/>\nPlayer must have completed all Research in their Tech Tree (Future Techs too).<br/>\n</html>");
        completionist.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                completionistActionPerformed(evt);
            }
        });

        applyButton.setText("Apply");
        applyButton.setToolTipText("Apply settings and keep GUI open");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        autoApplyToggleButton.setSelected(true);
        autoApplyToggleButton.setText("Auto Apply");
        autoApplyToggleButton.setToolTipText("For the settings to be applied live.");
        autoApplyToggleButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoApplyToggleButtonActionPerformed(evt);
            }
        });

        fleetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fleet Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        autoScout.setText("Auto Scout");
        autoScout.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoScoutActionPerformed(evt);
            }
        });

        autoColonize.setText("Auto Colonize");
        autoColonize.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoColonizeActionPerformed(evt);
            }
        });

        autoAttack.setText("Auto Attack");
        autoAttack.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoAttackActionPerformed(evt);
            }
        });

        autoColonyShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoColonyShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoColonyShipCountStateChanged(evt);
            }
        });
        autoColonyShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoColonyShipCountMouseWheelMoved(evt);
            }
        });

        autoColonyShipCountLabel.setText("Number of colony ships to send");

        autoScoutShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoScoutShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoScoutShipCountStateChanged(evt);
            }
        });
        autoScoutShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoScoutShipCountMouseWheelMoved(evt);
            }
        });

        autoAttackShipCount.setModel(new javax.swing.SpinnerNumberModel(1, 1, 9999, 1));
        autoAttackShipCount.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                autoAttackShipCountStateChanged(evt);
            }
        });
        autoAttackShipCount.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                autoAttackShipCountMouseWheelMoved(evt);
            }
        });

        autoScoutShipCountLabel.setText("Number of scout ships to send");

        autoAttackShipCountLabel.setText("Number of attack ships to send");

        javax.swing.GroupLayout fleetPanelLayout = new javax.swing.GroupLayout(fleetPanel);
        fleetPanel.setLayout(fleetPanelLayout);
        fleetPanelLayout.setHorizontalGroup(
            fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fleetPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoColonize)
                    .addComponent(autoScout)
                    .addComponent(autoAttack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(autoAttackShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoColonyShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoScoutShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fleetPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(autoColonyShipCountLabel)
                            .addComponent(autoScoutShipCountLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(fleetPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(autoAttackShipCountLabel)))
                .addContainerGap())
        );

        fleetPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {autoAttackShipCount, autoColonyShipCount, autoScoutShipCount});

        fleetPanelLayout.setVerticalGroup(
            fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fleetPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoScoutShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoScoutShipCountLabel)
                    .addComponent(autoScout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoColonize)
                    .addComponent(autoColonyShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoColonyShipCountLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addGroup(fleetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(autoAttackShipCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(autoAttackShipCountLabel)
                    .addComponent(autoAttack))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        colonyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Colony Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        autospend.setText("Autospend");
        autospend.setToolTipText("Automatically spend reserve on planets with lowest production");
        autospend.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autospendActionPerformed(evt);
            }
        });

        reserve.setModel(new javax.swing.SpinnerNumberModel(1000, 0, 100000, 10));
        reserve.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                reserveStateChanged(evt);
            }
        });
        reserve.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                reserveMouseWheelMoved(evt);
            }
        });

        reserveLabel.setText("Keep in reserve");

        shipbuilding.setText("Shipbuilding with Governor enabled");
        shipbuilding.setToolTipText("Divert resources into shipbuilding and not research if planet is already building ships");
        shipbuilding.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                shipbuildingActionPerformed(evt);
            }
        });

        shieldWithoutBases.setText("Allow shields without bases");
        shieldWithoutBases.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                shieldWithoutBasesActionPerformed(evt);
            }
        });

        legacyGrowthMode.setText("Develop colonies as quickly as possible");
        legacyGrowthMode.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                legacyGrowthModeActionPerformed(evt);
            }
        });

        missileBases.setModel(new javax.swing.SpinnerNumberModel(0, 0, 20, 1));
        missileBases.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                missileBasesStateChanged(evt);
            }
        });
        missileBases.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                missileBasesMouseWheelMoved(evt);
            }
        });

        missileBasesLabel.setText("Minimum missile bases");

        javax.swing.GroupLayout colonyPanelLayout = new javax.swing.GroupLayout(colonyPanel);
        colonyPanel.setLayout(colonyPanelLayout);
        colonyPanelLayout.setHorizontalGroup(
            colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colonyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shipbuilding)
                    .addGroup(colonyPanelLayout.createSequentialGroup()
                        .addComponent(missileBases)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(missileBasesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(shieldWithoutBases))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colonyPanelLayout.createSequentialGroup()
                        .addComponent(reserve, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reserveLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(legacyGrowthMode)
                    .addComponent(autospend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        colonyPanelLayout.setVerticalGroup(
            colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(colonyPanelLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(colonyPanelLayout.createSequentialGroup()
                        .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(missileBases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(missileBasesLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(autospend)
                            .addComponent(shieldWithoutBases))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(legacyGrowthMode)
                            .addComponent(shipbuilding))
                        .addContainerGap())
                    .addGroup(colonyPanelLayout.createSequentialGroup()
                        .addGroup(colonyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(reserve, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reserveLabel))
                        .addGap(63, 63, 63))))
        );

        colonyPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {missileBases, missileBasesLabel});

        colonyPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {reserve, reserveLabel});

        spyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Intelligence Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        spareXenophobes.setText("Spare the Xenophobes");
        spareXenophobes.setToolTipText("Once framed by xenophobic empire: stop spying and infiltration to avoid further outrage");
        spareXenophobes.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                spareXenophobesActionPerformed(evt);
            }
        });

        autoSpy.setText("Let AI handle spies");
        autoSpy.setToolTipText("Hand control over spies to AI");
        autoSpy.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoSpyActionPerformed(evt);
            }
        });

        autoInfiltrate.setText("Autoinfiltrate");
        autoInfiltrate.setToolTipText("Automatically sends spies to infiltrate other empires");
        autoInfiltrate.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoInfiltrateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout spyPanelLayout = new javax.swing.GroupLayout(spyPanel);
        spyPanel.setLayout(spyPanelLayout);
        spyPanelLayout.setHorizontalGroup(
            spyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(spyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoInfiltrate)
                    .addComponent(autoSpy)
                    .addComponent(spareXenophobes))
                .addContainerGap())
        );
        spyPanelLayout.setVerticalGroup(
            spyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(spyPanelLayout.createSequentialGroup()
                .addComponent(autoInfiltrate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autoSpy)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spareXenophobes)
                .addContainerGap())
        );

        jPanelAspect.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Aspect", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 13))); // NOI18N

        isOriginal.setText("Original View");
        isOriginal.setName("Original View"); // NOI18N
        isOriginal.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                isOriginalActionPerformed(evt);
            }
        });

        customSize.setText("CustomSize");
        customSize.setName("Custom Size"); // NOI18N
        customSize.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                customSizeActionPerformed(evt);
            }
        });

        sizePct.setModel(new javax.swing.SpinnerNumberModel(100, 20, 200, 1));
        sizePct.setToolTipText("Size Factor");
        sizePct.setName("Size Factor"); // NOI18N
        sizePct.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sizePctStateChanged(evt);
            }
        });
        sizePct.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                sizePctMouseWheelMoved(evt);
            }
        });

        sizeFactorLabel.setText("% Size");

        brightnessPct.setModel(new javax.swing.SpinnerNumberModel(100, 20, 300, 1));
        brightnessPct.setToolTipText("Color Brightness");
        brightnessPct.setName("Color Brightness"); // NOI18N
        brightnessPct.addChangeListener(new javax.swing.event.ChangeListener() {
            @Override
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessPctStateChanged(evt);
            }
        });
        brightnessPct.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                brightnessPctMouseWheelMoved(evt);
            }
        });

        brightnessLabel.setText("% Bright");

        javax.swing.GroupLayout jPanelAspectLayout = new javax.swing.GroupLayout(jPanelAspect);
        jPanelAspect.setLayout(jPanelAspectLayout);
        jPanelAspectLayout.setHorizontalGroup(
            jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAspectLayout.createSequentialGroup()
                .addGroup(jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelAspectLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(sizePct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sizeFactorLabel))
                    .addGroup(jPanelAspectLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isOriginal)
                            .addComponent(customSize)))
                    .addGroup(jPanelAspectLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(brightnessPct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(brightnessLabel)))
                .addGap(0, 0, 0))
        );

        jPanelAspectLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {customSize, isOriginal});

        jPanelAspectLayout.setVerticalGroup(
            jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelAspectLayout.createSequentialGroup()
                .addComponent(isOriginal)
                .addGap(0, 0, 0)
                .addComponent(customSize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizePct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeFactorLabel))
                .addGap(0, 0, 0)
                .addGroup(jPanelAspectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brightnessPct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(brightnessLabel))
                .addGap(0, 0, 0))
        );

        jPanelAspectLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {brightnessLabel, brightnessPct});

        jPanelAspectLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {sizeFactorLabel, sizePct});

        raceImage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        raceImage.setFocusable(false);
        raceImage.setMinimumSize(new java.awt.Dimension(50, 50));
        raceImage.setRequestFocusEnabled(false);
        raceImage.setVerifyInputWhenFocusTarget(false);
        raceImage.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
                raceImageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(applyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(autoApplyToggleButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(completionist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(fleetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jPanelAspect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(2, 2, 2))
                                    .addComponent(autotransportPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(colonyPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(allGovernorsOn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(governorDefault)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(allGovernorsOff)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(stargatePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(spyPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(raceImage, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {raceImage, spyPanel, stargatePanel});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(governorDefault)
                            .addComponent(allGovernorsOff)
                            .addComponent(allGovernorsOn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(autotransportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(raceImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(spyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(colonyPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fleetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelAspect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stargatePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(applyButton)
                    .addComponent(completionist)
                    .addComponent(autoApplyToggleButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {colonyPanel, spyPanel});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {allGovernorsOff, allGovernorsOn, governorDefault});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {applyButton, autoApplyToggleButton, cancelButton, completionist, okButton});

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fleetPanel, jPanelAspect, stargatePanel});

    }// </editor-fold>//GEN-END:initComponents

	private void allGovernorsOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOnActionPerformed
		for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
			if (!ss.isColonized()) {
				// shouldn't happen
				continue;
			}
			ss.colony().setGovernor(true);
			ss.colony().governIfNeeded();
			if (isAutoApply())
				options().setGovernorOnByDefault(governorDefault.isSelected());
		}
	}//GEN-LAST:event_allGovernorsOnActionPerformed

	private void allGovernorsOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allGovernorsOffActionPerformed
		for (StarSystem ss : GameSession.instance().galaxy().player().orderedColonies()) {
			if (!ss.isColonized()) {
				// shouldn't happen
				continue;
			}
			ss.colony().setGovernor(false);
			if (isAutoApply())
				options().setGovernorOnByDefault(governorDefault.isSelected());
		}
		this.allowUngoverned.setSelected(false);
		options().setAutotransportUngoverned(allowUngoverned.isSelected());
		this.autoSpy.setSelected(false);
		options().setAutoSpy(autoSpy.isSelected());
		this.autoInfiltrate.setSelected(false);
		options().setAutoInfiltrate(autoInfiltrate.isSelected());
	}//GEN-LAST:event_allGovernorsOffActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
		applyAction();
		frame.setVisible(false);
	}//GEN-LAST:event_okButtonActionPerformed

	private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
		applyAction();
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
		if (isAutoApply())
			options().setAutotransportXilmi(autotransportXilmi.isSelected());
	}//GEN-LAST:event_autotransportXilmiActionPerformed

	private void transportMaxTurnsLabelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsLabelMouseWheelMoved
		mouseWheel(transportMaxTurns, evt);
	}//GEN-LAST:event_transportMaxTurnsLabelMouseWheelMoved

	private void transportMaxTurnsMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_transportMaxTurnsMouseWheelMoved
		mouseWheel(transportMaxTurns, evt);
	}//GEN-LAST:event_transportMaxTurnsMouseWheelMoved

	private void autoApplyToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoApplyToggleButtonActionPerformed
		options().setAutoApply(autoApplyToggleButton.isSelected());
		applyAction();
	}//GEN-LAST:event_autoApplyToggleButtonActionPerformed

	private void allowUngovernedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allowUngovernedActionPerformed
		if (isAutoApply())
			options().setAutotransportUngoverned(allowUngoverned.isSelected());
	}//GEN-LAST:event_allowUngovernedActionPerformed

	private void autotransportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autotransportActionPerformed
		if (isAutoApply())
			options().setAutotransport(autotransport.isSelected());
	}//GEN-LAST:event_autotransportActionPerformed

	private void transportRichDisabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportRichDisabledActionPerformed
		if (isAutoApply())
			options().setTransportRichDisabled(transportRichDisabled.isSelected());
	}//GEN-LAST:event_transportRichDisabledActionPerformed

	private void transportPoorDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transportPoorDoubleActionPerformed
		if (isAutoApply())
			options().setTransportPoorDouble(transportPoorDouble.isSelected());
	}//GEN-LAST:event_transportPoorDoubleActionPerformed

	private void autoScoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoScoutActionPerformed
		if (isAutoApply())
			options().setAutoScout(autoScout.isSelected());
	}//GEN-LAST:event_autoScoutActionPerformed

	private void autoColonizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoColonizeActionPerformed
		if (isAutoApply())
			options().setAutoColonize(autoColonize.isSelected());
	}//GEN-LAST:event_autoColonizeActionPerformed

	private void autoAttackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoAttackActionPerformed
		if (isAutoApply())
			options().setAutoAttack(autoAttack.isSelected());
	}//GEN-LAST:event_autoAttackActionPerformed

	private void shieldWithoutBasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shieldWithoutBasesActionPerformed
		if (isAutoApply())
			options().setShieldWithoutBases(shieldWithoutBases.isSelected());
	}//GEN-LAST:event_shieldWithoutBasesActionPerformed

	private void autospendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autospendActionPerformed
		if (isAutoApply())
			options().setAutospend(autospend.isSelected());
	}//GEN-LAST:event_autospendActionPerformed

	private void shipbuildingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shipbuildingActionPerformed
		if (isAutoApply())
			options().setShipbuilding(shipbuilding.isSelected());
	}//GEN-LAST:event_shipbuildingActionPerformed

	private void autoInfiltrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoInfiltrateActionPerformed
		if (isAutoApply())
			options().setAutoInfiltrate(autoInfiltrate.isSelected());
	}//GEN-LAST:event_autoInfiltrateActionPerformed

	private void legacyGrowthModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_legacyGrowthModeActionPerformed
		if (isAutoApply())
			options().setLegacyGrowthMode(legacyGrowthMode.isSelected());
	}//GEN-LAST:event_legacyGrowthModeActionPerformed

	private void autoSpyActionPerformed(java.awt.event.ActionEvent evt) {                                               
		if (isAutoApply())
			options().setAutoSpy(autoSpy.isSelected());
	}                                                

	private void spareXenophobesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoSpyActionPerformed
		if (isAutoApply())
			options().setSpareXenophobes(spareXenophobes.isSelected());
	}//GEN-LAST:event_autoSpyActionPerformed

	private void stargateOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOffActionPerformed
		if (isAutoApply()) applyStargates();
	}//GEN-LAST:event_stargateOffActionPerformed

	private void stargateRichActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateRichActionPerformed
		if (isAutoApply()) applyStargates();
	}//GEN-LAST:event_stargateRichActionPerformed

	private void stargateOnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stargateOnActionPerformed
		if (isAutoApply()) applyStargates();
	}//GEN-LAST:event_stargateOnActionPerformed

	private void reserveStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_reserveStateChanged
		if (isAutoApply())
			options().setReserve((Integer)reserve.getValue());
	}//GEN-LAST:event_reserveStateChanged

	private void missileBasesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_missileBasesStateChanged
		if (isAutoApply())
			options().setMinimumMissileBases((Integer)missileBases.getValue());
	}//GEN-LAST:event_missileBasesStateChanged

	private void autoAttackShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoAttackShipCountStateChanged
		if (isAutoApply())
			options().setAutoAttackShipCount((Integer)autoAttackShipCount.getValue());
	}//GEN-LAST:event_autoAttackShipCountStateChanged

	private void autoColonyShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoColonyShipCountStateChanged
		if (isAutoApply())
			options().setAutoColonyShipCount((Integer)autoColonyShipCount.getValue());
	}//GEN-LAST:event_autoColonyShipCountStateChanged

	private void autoScoutShipCountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_autoScoutShipCountStateChanged
		if (isAutoApply())
			options().setAutoScoutShipCount((Integer)autoScoutShipCount.getValue());
   }//GEN-LAST:event_autoScoutShipCountStateChanged

	private void transportMaxTurnsStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_transportMaxTurnsStateChanged
		if (isAutoApply())
			options().setTransportMaxTurns((Integer)transportMaxTurns.getValue());
	}//GEN-LAST:event_transportMaxTurnsStateChanged

	private void governorDefaultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_governorDefaultActionPerformed
		if (isAutoApply())
			options().setGovernorOnByDefault(governorDefault.isSelected());
	}//GEN-LAST:event_governorDefaultActionPerformed

	private void brightnessPctMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_brightnessPctMouseWheelMoved
		mouseWheel(brightnessPct, evt);
	}//GEN-LAST:event_brightnessPctMouseWheelMoved

	private void sizePctMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_sizePctMouseWheelMoved
		mouseWheel(sizePct, evt);
	}//GEN-LAST:event_sizePctMouseWheelMoved

	private void isOriginalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isOriginalActionPerformed
		setNewFormat (!isOriginal.isSelected());
		protectedReset();
	}//GEN-LAST:event_isOriginalActionPerformed

	private void customSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customSizeActionPerformed
		setCustomSize(customSize.isSelected());
		protectedUpdateSize();
	}//GEN-LAST:event_customSizeActionPerformed

	private void raceImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_raceImageMouseClicked
		if (options().toggleAnimatedImage())
			startAnimation();
		else
			stopAnimation();
	}//GEN-LAST:event_raceImageMouseClicked

	private void sizePctStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sizePctStateChanged
		setSizeFactorPct((Integer)sizePct.getValue());
		if (isCustomSize())
			protectedUpdateSize();
	}//GEN-LAST:event_sizePctStateChanged

	private void brightnessPctStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightnessPctStateChanged
		setBrightnessPct((Integer)brightnessPct.getValue());
		protectedUpdateColor();
	}//GEN-LAST:event_brightnessPctStateChanged

	private  void mouseWheel(JSpinner spinner, java.awt.event.MouseWheelEvent evt) {
		if (evt.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			return;
		}
		SpinnerNumberModel model = (SpinnerNumberModel) spinner.getModel();
		// BR: added Shift and Ctrl accelerator
		int inc = (int) Math.signum(evt.getUnitsToScroll()) * model.getStepSize().intValue();
		if (evt.isShiftDown())
			inc *= 5;
		if (evt.isControlDown())
			inc *= 20;
		int value = inc + (int) model.getValue();	   
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
    javax.swing.JButton allGovernorsOff;
    javax.swing.JButton allGovernorsOn;
    javax.swing.JCheckBox allowUngoverned;
    javax.swing.JButton applyButton;
    javax.swing.JCheckBox autoApplyToggleButton;
    javax.swing.JCheckBox autoAttack;
    javax.swing.JSpinner autoAttackShipCount;
    javax.swing.JLabel autoAttackShipCountLabel;
    javax.swing.JCheckBox autoColonize;
    javax.swing.JSpinner autoColonyShipCount;
    javax.swing.JLabel autoColonyShipCountLabel;
    javax.swing.JCheckBox autoInfiltrate;
    javax.swing.JCheckBox autoScout;
    javax.swing.JSpinner autoScoutShipCount;
    javax.swing.JLabel autoScoutShipCountLabel;
    javax.swing.JCheckBox autoSpy;
    javax.swing.JCheckBox autospend;
    javax.swing.JCheckBox autotransport;
    javax.swing.JCheckBox autotransportXilmi;
    javax.swing.JLabel brightnessLabel;
    javax.swing.JSpinner brightnessPct;
    javax.swing.JButton cancelButton;
    javax.swing.JButton completionist;
    javax.swing.JCheckBox customSize;
    javax.swing.JCheckBox governorDefault;
    javax.swing.JCheckBox isOriginal;
    javax.swing.JLabel jLabel1;
    javax.swing.JPanel jPanelAspect;
    javax.swing.JCheckBox legacyGrowthMode;
    javax.swing.JSpinner missileBases;
    javax.swing.JLabel missileBasesLabel;
    javax.swing.JButton okButton;
    javax.swing.JLabel raceImage;
    javax.swing.JSpinner reserve;
    javax.swing.JLabel reserveLabel;
    javax.swing.JCheckBox shieldWithoutBases;
    javax.swing.JCheckBox shipbuilding;
    javax.swing.JLabel sizeFactorLabel;
    javax.swing.JSpinner sizePct;
    javax.swing.JCheckBox spareXenophobes;
    javax.swing.JRadioButton stargateOff;
    javax.swing.JRadioButton stargateOn;
    javax.swing.ButtonGroup stargateOptions;
    javax.swing.JRadioButton stargateRich;
    javax.swing.JSpinner transportMaxTurns;
    javax.swing.JCheckBox transportPoorDouble;
    javax.swing.JCheckBox transportRichDisabled;
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
	
	// ========== Nested Class ==========
	//
	// =============== Check Box and Radio Button ===============
	//
	private class ScalableCheckBoxAndRadioButtonIcon implements Icon {

		private ScalableCheckBoxAndRadioButtonIcon () {  }
		
		protected int dim() { return Math.round(iconSize); }
		@Override public void paintIcon(Component component, Graphics g0, int xi, int yi) {
			ButtonModel buttonModel = ((AbstractButton) component).getModel();
			Graphics2D g = (Graphics2D) g0;
			float y	= (float) (0.5 * (component.getSize().getHeight() - dim()));
			float x	= 2f;
			int corner = 0;
			int border = 1;
			int d2 = (int)(iconSize*0.8f);
			if (component instanceof JRadioButton) {
				corner = dim();
				d2 = (int)(iconSize*0.7f);
			}
			
			if (buttonModel.isRollover()) {
				g.setColor(hoverColor);
				border = 2;
			} else {
				g.setColor(borderColor);
			}
			g.fillRoundRect((int)x, (int)y, dim(), dim(), corner, corner);
			if (buttonModel.isPressed()) {
				g.setColor(Color.GRAY);
			} else {
				g.setColor(iconBgColor);
			}
			g.fillRoundRect(border + (int)x, (int)y + border, dim() - 2*border, dim() - 2*border, corner, corner);
			
			if (buttonModel.isSelected()) {
				Stroke prev = g.getStroke();
				g.setStroke(new BasicStroke(iconSize/5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g.setColor(SystemPanel.whiteText);
				int x0 = (int)(x+iconSize/4);
				int y0 = (int)(3*iconSize/4+y);
				int d1 = (int)(iconSize*0.3f);
				g.drawLine(x0-d1, y0-d1, x0, y0);
				g.drawLine(x0, y0, x0+d2, y0-d2);
				g.setStroke(prev);
			}
		}
		@Override public int getIconWidth()  { return dim(); }
		@Override public int getIconHeight() { return dim(); }
	}
	// ==================== Buttons ====================
	//
	private class GovButtonUI extends MetalButtonUI {
		@Override  protected Color getDisabledTextColor() { return disabledColor; }
	    @Override  protected Color getSelectColor()		  { return hoverColor; }
	}
	// =================================================
	//
	private class GovButtonIcon implements Icon {

		private GovButtonIcon () {  }
		
		@Override public int getIconHeight() { return 2; }
		@Override public int getIconWidth()	 { return 2; }
		@Override public void paintIcon(Component component, Graphics g0, int xi, int yi) {
			Graphics2D g	= (Graphics2D) g0;
			JButton button	= (JButton) component;			
			ButtonModel buttonModel = button.getModel();
			Color borderC = borderColor;
			Color centerC = buttonColor;
			int corner = Math.round(buttonCorner);
			int border = 1;
			int x = 0;
			int y = 0;
			int w = button.getWidth();
			int h = button.getHeight();
			
			if (!buttonModel.isEnabled()) {
				borderC = disabledColor;
				centerC = hiddenColor;
			}
			else if (buttonModel.isRollover()) {
				borderC = hoverColor;
				border = 2;
			}
			// Fill background to go over OS choices... 
			g.setColor(frameBgColor);
			g.fillRect(x, y, w, h);
			
			// Fill the buttons
			g.setColor(centerC);
			g.fillRoundRect(x, y, w, h, corner, corner);
			
			// Draw borders
			Stroke prev = g.getStroke();
			g.setStroke(new BasicStroke(border, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.setColor(borderC);
			g.drawRoundRect(x + border, y + border, w - 2*border, h - 2*border, corner, corner);
			g.setStroke(prev);
		}
	}
	// ==================== Spinners ====================
	//
	private class GovernorJSpinner extends RotpJSpinner {
		@Override public	boolean isNewFormat()	{ return isNewFormat; }
		@Override public	int		arrowSize()		{ return arrowHeight; }
		@Override protected Color	borderColor()	{ return borderColor; }
		@Override protected Color	valueBgColor()	{ return valueBgColor; }
		@Override protected Color	disabledColor()	{ return disabledColor; }
		@Override protected Color	hiddenColor()	{ return hiddenColor; }
		@Override protected Color	hoverColor()	{ return hoverColor; }
		@Override protected Color	panelBgColor()	{ return panelBgColor; }
	}
}
