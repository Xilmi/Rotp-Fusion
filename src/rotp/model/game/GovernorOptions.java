package rotp.model.game;

import static rotp.model.game.BaseOptions.headerSpacer;

import java.awt.Point;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.UserPreferences;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

/**
 * Governor options.
 */
public class GovernorOptions implements Serializable {
    private static final long serialVersionUID = 1l;
    public	static final String GOVERNOR_GUI_ID	= "GOVERNOR";
    public	static final String GOV_UI			= "GOVERNOR_";

    public enum GatesGovernor {
        None,
        Rich,
        All
    }
    // The old options are kept Active for compatibility
    // The new dynamic options are needed for multiple access
    // Remnant.cfg options will be read once, the ignored.
    // keep backwards compatibility with system properties
    private boolean governorOnByDefault = UserPreferences.governorOnByDefault();
    private boolean legacyGrowthMode = UserPreferences.legacyGrowth(); // BR: moved to remnant.cfg
    private boolean autotransport = "true".equalsIgnoreCase(System.getProperty("autotransport", "false"));
    private boolean autotransportXilmi = "true".equalsIgnoreCase(System.getProperty("autotransportXilmi", "false"));
    private boolean autotransportUngoverned = "true".equalsIgnoreCase(System.getProperty("autotransportUngoverned", "false"));
    private GatesGovernor gates = "false".equalsIgnoreCase(System.getProperty("autogate", "true")) ? GatesGovernor.None : GatesGovernor.Rich;

    // 1.5x for destinations inside nebulae
    private int transportMaxTurns = 5;
    private boolean transportRichDisabled = true;
    private boolean transportPoorDouble = true;

    private int minimumMissileBases = 0;
    private boolean shieldWithoutBases = false;
    private boolean autospend = UserPreferences.governorAutoSpendByDefault();
    private boolean autoApply = UserPreferences.governorAutoApply();
    private boolean autoInfiltrate = "true".equalsIgnoreCase(System.getProperty("autoInfiltrate", "true"));
    private boolean autoSpy = "true".equalsIgnoreCase(System.getProperty("autoSpy", "false"));
    private int reserve = 1000;
 
    // if true, new colonies will have auto ship building set to "on"
    // Converted use: true = not yet transfered.
    // The autoShipsByDefault original function will be implemented
    // Using the news parameters
    private boolean autoShipsByDefault = true;
    private boolean shipbuilding = true;

    // if true, automatically scout new planets
    private boolean autoScout = true;
    // if true, automatically colonize new planets
    private boolean autoColonize = true;
    // if true, send ships to enemy colonies
    private boolean autoAttack = false;
    // How many ships should Auto* missions send?
    private int autoScoutShipCount  = 1;
    private int autoColonyShipCount = 1;
    private int autoAttackShipCount = 1;

    // BR: For the future parameters, to keep save files compatibility
    // Will be saved in the dynamic options list.
	// AutoTransport Options
	private	final static ParamBoolean	autoTransport		= new ParamBoolean(
			GOV_UI, "AUTO_TRANSPORT", true);
	private	final static ParamBoolean	autotransportAtMax	= new ParamBoolean(
			GOV_UI, "TRANSPORT_XILMI", true);
	private	final static ParamBoolean	autotransportAll	= new ParamBoolean(
			GOV_UI, "TRANSPORT_UNGOVERNED", true);
	private	final static ParamBoolean	transportNoRich		= new ParamBoolean(
			GOV_UI, "TRANSPORT_RICH_OFF", true);
	private	final static ParamBoolean	transportPoorX2		= new ParamBoolean(
			GOV_UI, "TRANSPORT_POOR_DBL", true);
	private	static final ParamInteger	transportMaxDist	= new ParamInteger(
			GOV_UI, "TRANSPORT_MAX_TURNS", 5, 1, 15, 1, 3, 5);

	// StarGates Options
	// Using an Enum object instead of a list will break the game save if the enum is changed! 
	private	final static ParamList		starGateOption		= new ParamList(
			GOV_UI, "STARGATES_OPTIONS", GatesGovernor.Rich.name()) {
		{
			showFullGuide(true);
			for (GatesGovernor value: GatesGovernor.values())
				put(value.name(), GOV_UI + "STARGATES_" + value.name().toUpperCase());
		}
	};

	// Colony Options
	private	static final ParamInteger	missileBasesMin		= new ParamInteger(
			GOV_UI, "MIN_MISSILE_BASES", 0, 0, 20, 1, 3, 5);
	private	final static ParamBoolean	shieldAlones		= new ParamBoolean(
			GOV_UI, "SHIELD_WITHOUT_BASES", false);
	private	final static ParamBoolean	autoSpend			= new ParamBoolean(
			GOV_UI, "AUTOSPEND", false);
	private	static final ParamInteger	reserveForSlow		= new ParamInteger(
			GOV_UI, "RESERVE", 0, 0, 100000, 10, 50, 200);
	private	final static ParamBoolean	shipBuilding		= new ParamBoolean(
			GOV_UI, "SHIP_BUILDING", true);
	private	final static ParamBoolean	maxGrowthMode		= new ParamBoolean(
			GOV_UI, "LEGACY_GROWTH_MODE", true);

	// Intelligence Options
	private	final static ParamBoolean	auto_Infiltrate		= new ParamBoolean(
			GOV_UI, "AUTO_INFILTRATE", true);
	private	final static ParamBoolean	auto_Spy			= new ParamBoolean(
			GOV_UI, "AUTO_SPY", true);
	private	final static ParamBoolean	spareXenophobes		= new ParamBoolean(
			GOV_UI, "SPARE_XENOPHOBES", false);

	// Aspect Options
	private	final static ParamBoolean	originalPanel		= new ParamBoolean(
			GOV_UI, "ORIGINAL_PANEL", false);
	private	final static ParamBoolean	customSize			= new ParamBoolean(
			GOV_UI, "CUSTOM_SIZE", true);
	private	static final ParamInteger	brightnessPct		= new ParamInteger(
			GOV_UI, "BRIGHTNESS",	100, 20, 300, 1, 5, 20);
	private	static final ParamInteger	sizeFactorPct		= new ParamInteger(
			GOV_UI, "SIZE_FACTOR",	100, 20, 200, 1, 5, 20);
	private	static final ParamInteger	horizontalPosition	= new ParamInteger(
			GOV_UI, "POSITION_X",	0, null, null, 1, 5, 20);
	private	static final ParamInteger	verticalPosition	= new ParamInteger(
			GOV_UI, "POSITION_Y",	0, null, null, 1, 5, 20);

	// Fleet Options
	private	final static ParamBoolean	auto_Scout			= new ParamBoolean(
			GOV_UI, "AUTO_SCOUT", true);
	private	static final ParamInteger	autoScoutCount		= new ParamInteger(
			GOV_UI, "AUTO_SCOUT_COUNT",	1, 1, 9999, 1, 5, 20);
	public	final static ParamBoolean	auto_Colonize		= new ParamBoolean(
			GOV_UI, "AUTO_COLONIZE", true);
	private	static final ParamInteger	autoColonyCount		= new ParamInteger(
			GOV_UI, "AUTO_COLONY_COUNT", 1, 1, 9999, 1, 5, 20);
	private	final static ParamBoolean	auto_Attack			= new ParamBoolean(
			GOV_UI, "AUTO_ATTACK", false);
	private	static final ParamInteger	autoAttackCount		= new ParamInteger(
			GOV_UI, "AUTO_ATTACK_COUNT", 1, 1, 9999, 1, 5, 20);
    // if true, new colonies will have auto ship building set to "on"
	private	final static ParamBoolean	autoShipsDefault	= new ParamBoolean(
			GOV_UI, "AUTOSHIPS_BY_DEFAULT", true);

	// Other Options
	private	final static ParamBoolean	animatedImage		= new ParamBoolean(
			GOV_UI, "ANIMATED_IMAGE", true);
	private final static ParamBoolean	auto_Apply			= new ParamBoolean(
			GOV_UI, "AUTO_APPLY", true);
	private final static ParamBoolean	governorByDefault	= new ParamBoolean(
			GOV_UI, "ON_BY_DEFAULT", true);

	private static final LinkedList<LinkedList<InterfaceParam>> governorOptionsMap = 
			new LinkedList<LinkedList<InterfaceParam>>();
	static {
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(
				new ParamTitle(GOV_UI + "TRANSPORT_OPTIONS"),
				autoTransport, autotransportAtMax, autotransportAll,
				transportNoRich, transportPoorX2, transportMaxDist,

				headerSpacer,
				new ParamTitle(GOV_UI + "COLONY_OPTIONS"),
				missileBasesMin, shieldAlones,
				autoSpend, reserveForSlow, shipBuilding,
				maxGrowthMode
				)));
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(				
				new ParamTitle(GOV_UI + "INTELLIGENCE_OPTIONS"),
				auto_Infiltrate, auto_Spy, spareXenophobes,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "FLEET_OPTIONS"),
				// autoShipsByDefault,	// TODO: for future use
				auto_Scout, autoScoutCount,
				auto_Colonize, autoColonyCount,
				auto_Attack, autoAttackCount,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "STARGATES_OPTIONS"),
				starGateOption
				)));
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(
				new ParamTitle(GOV_UI + "ASPECT_OPTIONS"),
				originalPanel, customSize, animatedImage,
				brightnessPct, sizeFactorPct,
				horizontalPosition, verticalPosition,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "OTHER_OPTIONS"),
				governorByDefault, auto_Apply
				)));
	};
	private	static final String		GOV_GUI_ID	= "GOV_2";
	public	static final ParamSubUI	governorOptionsUI	= new ParamSubUI(
			GOV_UI, "SETUP_MENU", governorOptionsMap,
			"SETUP_TITLE", GOV_GUI_ID);
	public	static final LinkedList<InterfaceParam> governorOptions = governorOptionsUI.optionsList();

	private transient boolean localSave = false;

	// ========== Constructor And Initializers ==========
    public GovernorOptions() {  }
    public void gameLoaded() {
    	if (autoShipsByDefault) {
    		autoTransport.set(autotransport);
    		autotransportAtMax.set(autotransportXilmi);
    		autotransportAll.set(autotransportUngoverned);
    		transportNoRich.set(transportRichDisabled);
    		transportPoorX2.set(transportPoorDouble);
    		transportMaxDist.set(transportMaxTurns);
    		starGateOption.set(gates.name());
    		missileBasesMin.set(minimumMissileBases);
    		shieldAlones.set(shieldWithoutBases);
    		autoSpend.set(autospend);
    		reserveForSlow.set(reserve);
    		shipBuilding.set(shipbuilding);
    		maxGrowthMode.set(legacyGrowthMode);
    		auto_Infiltrate.set(autoInfiltrate);
    		auto_Spy.set(autoSpy);
    		auto_Scout.set(autoScout);
    		autoScoutCount.set(autoScoutShipCount);
    		auto_Colonize.set(autoColonize);
    		autoColonyCount.set(autoColonyShipCount);
    		auto_Attack.set(autoAttack);
    		autoAttackCount.set(autoAttackShipCount);
    		auto_Apply.set(autoApply);
    		governorByDefault.set(governorOnByDefault);
    		save();
    	}
    	autoShipsByDefault = false;
        // Converted use of autoShipsByDefault: true = not yet transfered.
        // The autoShipsByDefault original function will be implemented using the new parameters
        // if true, new colonies will have auto ship building set to "on"
    }

    public boolean isLocalSave() {
    	if (localSave) {
    		localSave = false;
    		return true;
    	}
    	return false;
    }
    public void save() { // update Quietly
    	localSave = true;
    	MOO1GameOptions opts = (MOO1GameOptions) GameSession.instance().options();
    	opts.writeModSettingsToOptions(governorOptions, false);
//    	MOO1GameOptions.writeModSettingsToOptions(
//    			(MOO1GameOptions) GameSession.instance().options(), GOVERNOR_GUI_ID, false);
    }
    public boolean isOriginalPanel()			{ return originalPanel.get(); }
    public void setIsOriginalPanel(boolean newValue, boolean save) {
    	originalPanel.set(newValue);
        if(save) save();
    }
    public boolean isCustomSize()				{ return customSize.get(); }
    public void setIsCustomSize(boolean newValue, boolean save) {
    	customSize.set(newValue);
        if(save) save();
    }
    public boolean isAnimatedImage()			{ return animatedImage.get(); }
    public void setIsAnimatedImage(boolean newValue, boolean save) {
    	animatedImage.set(newValue);
        if(save) save();
    }
    public boolean toggleAnimatedImage() {
    	animatedImage.toggle();
    	save();
    	return animatedImage.get();
    }
    public int  getBrightnessPct()				{ return brightnessPct.get(); }
    public void setBrightnessPct(int newValue, boolean save) {
    	brightnessPct.set(newValue);
        if(save) save();
    }
    public int  getSizeFactorPct()				{ return sizeFactorPct.get(); }
    public void setSizeFactorPct(int newValue, boolean save) {
    	sizeFactorPct.set(newValue);
        if(save) save();
    }
    public int  getPositionX()					{ return horizontalPosition.get(); }
    public void setPositionX(int newValue, boolean save) {
    	horizontalPosition.set(newValue);
        if(save) save();
    }
    public int  getPositionY()					{ return verticalPosition.get(); }
    public void setPositionY(int newValue, boolean save) {
    	verticalPosition.set(newValue);
        if(save) save();
    }
    public Point getPosition()					{ 
    	Point pt = new Point();
	    	pt.x = horizontalPosition.get();
	    	pt.y = verticalPosition.get();
    	return pt;
    }
    public void setPosition(Point pt) {
    	horizontalPosition.set(pt.x);
    	verticalPosition.set(pt.y);
    }
   
    public boolean isAutoApply()				{ return auto_Apply.get(); }
    public void setAutoApply(boolean newValue, boolean save) {
        auto_Apply.set(newValue);
        if(save) save();
    }
    public boolean isGovernorOnByDefault()		{ return governorByDefault.get(); }
    public void setGovernorOnByDefault(boolean newValue, boolean save) {
    	governorByDefault.set(newValue);
        if(save) save();
    }
    public boolean isAutotransport()			{ return autoTransport.get(); }
    public void setAutotransport(boolean newValue, boolean save) {
    	autoTransport.set(newValue);
        if(save) save();
    }
    public boolean isAutotransportXilmi()		{ return autotransportAtMax.get(); }
    public void setAutotransportXilmi(boolean newValue, boolean save) {
    	autotransportAtMax.set(newValue);
        if(save) save();
    }
    public boolean isAutotransportUngoverned()	{ return autotransportAll.get(); }
    public void setAutotransportUngoverned(boolean newValue, boolean save) {
    	autotransportAll.set(newValue);
        if(save) save();
    }
    public boolean isTransportRichDisabled()	{ return transportNoRich.get(); }
    public void setTransportRichDisabled(boolean newValue, boolean save) {
    	transportNoRich.set(newValue);
        if(save) save();
    }
    public boolean isTransportPoorDouble()		{ return transportPoorX2.get(); }
    public void setTransportPoorDouble(boolean newValue, boolean save) {
    	transportPoorX2.set(newValue);
        if(save) save();
    }
    public int  getTransportMaxTurns()			{ return transportMaxDist.get(); }
    public void setTransportMaxTurns(int newValue, boolean save) {
    	transportMaxDist.set(newValue);
        if(save) save();
    }
    public GatesGovernor getGates()				{
    	String gate = starGateOption.get();
		for (GatesGovernor value: GatesGovernor.values())
			if (gate.equalsIgnoreCase(value.name()))
				return value;
		return GatesGovernor.Rich; // Default Value
    }
    public void setGates(GatesGovernor gates, boolean save) {
    	starGateOption.set(gates.name());
        if(save) save();
    }
    public boolean legacyGrowthMode()			{ return maxGrowthMode.get(); }
    public void setLegacyGrowthMode(boolean newValue, boolean save) {
    	maxGrowthMode.set(newValue);
        if(save) save();
    }
    public int  getMinimumMissileBases()		{ return missileBasesMin.get(); }
    public void setMinimumMissileBases(int newValue, boolean save) {
    	missileBasesMin.set(newValue);
        if(save) save();
    }
    public boolean getShieldWithoutBases()		{ return shieldAlones.get(); }
    public void setShieldWithoutBases(boolean newValue, boolean save) {
    	shieldAlones.set(newValue);
        if(save) save();
    }
    public boolean isAutospend()				{ return autoSpend.get(); }
    public void setAutospend(boolean newValue, boolean save) {
    	autoSpend.set(newValue);
        if(save) save();
    }
    public int  getReserve()					{ return reserveForSlow.get(); }
    public void setReserve(int newValue, boolean save) {
    	reserveForSlow.set(newValue);
        if(save) save();
    }
    public boolean isShipbuilding()				{ return shipBuilding.get(); }
    public void setShipbuilding(boolean newValue, boolean save) {
    	shipBuilding.set(newValue);
        if(save) save();
    }
    public boolean isAutoInfiltrate()			{ return auto_Infiltrate.get(); }
    public void setAutoInfiltrate(boolean newValue, boolean save) {
    	auto_Infiltrate.set(newValue);
        if(save) save();
    }
    public boolean isAutoSpy()					{ return auto_Spy.get(); }
    public void setAutoSpy(boolean newValue, boolean save) {
    	auto_Spy.set(newValue);
        if(save) save();
    }
    public boolean isSpareXenophobes()			{ return spareXenophobes.get(); }
    public void setSpareXenophobes(boolean newValue, boolean save) {
        spareXenophobes.set(newValue);
        if(save) save();
    }
    public boolean isAutoScout()				{ return auto_Scout.get(); }
    public void setAutoScout(boolean newValue, boolean save) {
    	auto_Scout.set(newValue);
        if(save) save();
    }
    public boolean isAutoColonize()				{ return auto_Colonize.get(); }
    public void setAutoColonize(boolean newValue, boolean save) {
    	auto_Colonize.set(newValue);
        if(save) save();
    }
    public boolean isAutoAttack()				{ return auto_Attack.get(); }
    public void setAutoAttack(boolean newValue, boolean save) {
    	auto_Attack.set(newValue);
        if(save) save();
    }
    public int  getAutoScoutShipCount()			{ return autoScoutCount.get(); }
    public void setAutoScoutShipCount(int newValue, boolean save) {
    	autoScoutCount.set(newValue);
        if(save) save();
    }
    public int  getAutoColonyShipCount()		{ return autoColonyCount.get(); }
    public void setAutoColonyShipCount(int newValue, boolean save) {
    	autoColonyCount.set(newValue);
        if(save) save();
    }
    public int  getAutoAttackShipCount()		{ return autoAttackCount.get(); }
    public void setAutoAttackShipCount(int newValue, boolean save) {
    	autoAttackCount.set(newValue);
        if(save) save();
    }
    public boolean isAutoShipsByDefault()		{ return autoShipsDefault.get(); }
    public void setAutoShipsByDefault(boolean newValue, boolean save) {
    	autoShipsDefault.set(newValue);
        if(save) save();
    }
}
