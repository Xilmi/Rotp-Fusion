package rotp.model.game;

import static rotp.ui.UserPreferences.headerSpacer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

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
//    public enum GatesGovernor {
//        None ("None",	GOV_UI + "GATE_NONE"),
//        Rich ("Rich",	GOV_UI + "GATE_RICH"),
//        All  ("All",	GOV_UI + "GATE_ALL");
//    	public final String name, label;
//    	private GatesGovernor(String name, String label) {
//    		this.name	= name;
//    		this.label	= label;
//    	}
//    	public static GatesGovernor set(String name) {
//    		for (GatesGovernor value: values())
//    			if (name.equalsIgnoreCase(value.label))
//    				return value;
//    		return Rich; // Default Value
//
//    }
    // keep backwards compatibility with system properties
//    private boolean governorOnByDefault = UserPreferences.governorOnByDefault();
//    private boolean legacyGrowthMode = UserPreferences.legacyGrowth(); // BR: moved to remnant.cfg
//    private boolean autotransport = "true".equalsIgnoreCase(System.getProperty("autotransport", "false"));
//    private boolean autotransportXilmi = "true".equalsIgnoreCase(System.getProperty("autotransportXilmi", "false"));
//    private boolean autotransportUngoverned = "true".equalsIgnoreCase(System.getProperty("autotransportUngoverned", "false"));
//    private GatesGovernor gates = "false".equalsIgnoreCase(System.getProperty("autogate", "true")) ? GatesGovernor.None : GatesGovernor.Rich;

    // 1.5x for destinations inside nebulae
//    private int transportMaxTurns = 5;
//    private boolean transportRichDisabled = true;
//    private boolean transportPoorDouble = true;

//    private int minimumMissileBases = 0;
//    private boolean shieldWithoutBases = false;
//    private boolean autospend = UserPreferences.governorAutoSpendByDefault();
//    private boolean autoApply = UserPreferences.governorAutoApply();
//    private boolean autoInfiltrate = "true".equalsIgnoreCase(System.getProperty("autoInfiltrate", "true"));
//    private boolean autoSpy = "true".equalsIgnoreCase(System.getProperty("autoSpy", "false"));
//    private int reserve = 1000;
//    private boolean shipbuilding = true;

    // if true, new colonies will have auto ship building set to "on"
    // TODO: for future use
//    private boolean autoShipsByDefault = true;
//    // if true, automatically scout new planets
//    private boolean autoScout = true;
//    // if true, automatically colonize new planets
//    private boolean autoColonize = true;
//    // if true, send ships to enemy colonies
//    private boolean autoAttack = false;
//    // How many ships should Auto* missions send?
//    private int autoScoutShipCount = 1;
//    private int autoColonyShipCount = 1;
//    private int autoAttackShipCount = 1;

    // BR: For the future parameters, to keep save files compatibility
    // Will be saved in the dynamic options list.
    
	// AutoTransport Options
	private	final static ParamBoolean	autotransport			= new ParamBoolean(
			GOV_UI, "AUTO_TRANSPORT", true);
	private	final static ParamBoolean	autotransportXilmi		= new ParamBoolean(
			GOV_UI, "TRANSPORT_XILMI", true);
	private	final static ParamBoolean	autotransportUngoverned	= new ParamBoolean(
			GOV_UI, "TRANSPORT_UNGOVERNED", true);
	private	final static ParamBoolean	transportRichDisabled	= new ParamBoolean(
			GOV_UI, "TRANSPORT_RICH_OFF", true);
	private	final static ParamBoolean	transportPoorDouble		= new ParamBoolean(
			GOV_UI, "TRANSPORT_POOR_DBL", true);
	private	static final ParamInteger	transportMaxTurns		= new ParamInteger(
			GOV_UI, "TRANSPORT_MAX_TURNS", 5, 1, 15, 1, 3, 5);

	// StarGates Options
	// Using an Enum object instead of a list will break the game save if the enum is changed! 
	private	final static ParamList	starGateOption				= new ParamList(
			GOV_UI, "STARGATES_OPTIONS", GatesGovernor.Rich.name()) {
		{
			showFullGuide(true);
			for (GatesGovernor value: GatesGovernor.values())
				put(value.name(), GOV_UI + "STARGATES_" + value.name().toUpperCase());
		}
	};

	// Colony Options
	private	static final ParamInteger	minimumMissileBases		= new ParamInteger(
			GOV_UI, "MIN_MISSILE_BASES", 0, 0, 20, 1, 3, 5);
	private	final static ParamBoolean	shieldWithoutBases		= new ParamBoolean(
			GOV_UI, "SHIELD_WITHOUT_BASES", false);
	private	final static ParamBoolean	autospend				= new ParamBoolean(
			GOV_UI, "AUTOSPEND", false);
	private	static final ParamInteger	reserve					= new ParamInteger(
			GOV_UI, "RESERVE", 0, 0, 20, 1, 5, 20);
	private	final static ParamBoolean	shipbuilding			= new ParamBoolean(
			GOV_UI, "SHIP_BUILDING", true);
	private	final static ParamBoolean	legacyGrowthMode		= new ParamBoolean(
			GOV_UI, "LEGACY_GROWTH_MODE", true);

	// Intelligence Options
	private	final static ParamBoolean	autoInfiltrate			= new ParamBoolean(
			GOV_UI, "AUTO_INFILTRATE", true);
	private	final static ParamBoolean	autoSpy					= new ParamBoolean(
			GOV_UI, "AUTO_SPY", true);
	private	final static ParamBoolean	spareXenophobes			= new ParamBoolean(
			GOV_UI, "SPARE_XENOPHOBES", false);

	// Aspect Options
	private	final static ParamBoolean	originalPanel			= new ParamBoolean(
			GOV_UI, "ORIGINAL_PANEL", false);
	private	final static ParamBoolean	customSize				= new ParamBoolean(
			GOV_UI, "CUSTOM_SIZE", true);
	private	static final ParamInteger	brightnessPct			= new ParamInteger(
			GOV_UI, "BRIGHTNESS",	100, 20, 300, 1, 5, 20);
	private	static final ParamInteger	sizeFactorPct			= new ParamInteger(
			GOV_UI, "SIZE_FACTOR",	100, 20, 200, 1, 5, 20);

	// Fleet Options
	private	final static ParamBoolean	autoScout				= new ParamBoolean(
			GOV_UI, "AUTO_SCOUT", true);
	private	static final ParamInteger	autoScoutShipCount		= new ParamInteger(
			GOV_UI, "AUTO_SCOUT_COUNT",	1, 1, 9999, 1, 5, 20);
	private	final static ParamBoolean	autoColonize			= new ParamBoolean(
			GOV_UI, "AUTO_COLONIZE", true);
	private	static final ParamInteger	autoColonyShipCount		= new ParamInteger(
			GOV_UI, "AUTO_COLONY_COUNT", 1, 1, 9999, 1, 5, 20);
	private	final static ParamBoolean	autoAttack				= new ParamBoolean(
			GOV_UI, "AUTO_ATTACK", true);
	private	static final ParamInteger	autoAttackShipCount		= new ParamInteger(
			GOV_UI, "AUTO_ATTACK_COUNT", 1, 1, 9999, 1, 5, 20);
    // if true, new colonies will have auto ship building set to "on"
	private	final static ParamBoolean	autoShipsByDefault		= new ParamBoolean(
			GOV_UI, "AUTOSHIPS_BY_DEFAULT", true);

	// Other Options
	private	final static ParamBoolean	animatedImage			= new ParamBoolean(
			GOV_UI, "ANIMATED_IMAGE", true);
	private final static ParamBoolean	autoApply				= new ParamBoolean(
			GOV_UI, "AUTO_APPLY", true);
	private final static ParamBoolean	governorOnByDefault		= new ParamBoolean(
			GOV_UI, "ON_BY_DEFAULT", true);

	private static final LinkedList<LinkedList<InterfaceParam>> governorOptionsMap = 
			new LinkedList<LinkedList<InterfaceParam>>();
	static {
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(
				new ParamTitle(GOV_UI + "TRANSPORT_OPTIONS"),
				autotransport, autotransportXilmi, autotransportUngoverned,
				transportRichDisabled, transportPoorDouble, transportMaxTurns,

				headerSpacer,
				new ParamTitle(GOV_UI + "STARGATES_OPTIONS"),
				starGateOption,

				headerSpacer,
				new ParamTitle(GOV_UI + "COLONY_OPTIONS"),
				minimumMissileBases, shieldWithoutBases,
				autospend, reserve, shipbuilding,
				legacyGrowthMode
				
				)));
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(				
				new ParamTitle(GOV_UI + "INTELLIGENCE_OPTIONS"),
				autoInfiltrate, autoSpy, spareXenophobes,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "FLEET_OPTIONS"),
				// autoShipsByDefault,	// TODO: for future use
				autoScout, autoScoutShipCount,
				autoColonize, autoColonyShipCount,
				autoAttack, autoAttackShipCount
				
				)));
		governorOptionsMap.add(new LinkedList<>(Arrays.asList(
				new ParamTitle(GOV_UI + "ASPECT_OPTIONS"),
				originalPanel, customSize, animatedImage,
				brightnessPct, sizeFactorPct,
				
				headerSpacer,
				new ParamTitle(GOV_UI + "OTHER_OPTIONS"),
				governorOnByDefault, autoApply
				)));
	};
	private	static final String		GOV_GUI_ID	= "GOV_2";
	public	static final ParamSubUI	governorOptionsUI	= new ParamSubUI(
			GOV_UI, "SETUP_MENU", governorOptionsMap,
			"SETUP_TITLE", GOV_GUI_ID);
	public	static final LinkedList<InterfaceParam> governorOptions = governorOptionsUI.optionsList();

	private boolean localSave = false;

	// ========== Constructor ==========
    public GovernorOptions() { }

    public boolean isLocalSave() {
    	if (localSave) {
    		localSave = false;
    		return true;
    	}
    	return false;
    }
    public void save() {
    	localSave = true;
    	MOO1GameOptions.writeModSettingsToOptions(
    			(MOO1GameOptions) GameSession.instance().options(), GOVERNOR_GUI_ID);
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
    public boolean isAutoApply()				{ return autoApply.get(); }
    public void setAutoApply(boolean newValue, boolean save) {
        autoApply.set(newValue);
        if(save) save();
    }
    public boolean isGovernorOnByDefault()		{ return governorOnByDefault.get(); }
    public void setGovernorOnByDefault(boolean newValue, boolean save) {
    	governorOnByDefault.set(newValue);
        if(save) save();
    }
    public boolean isAutotransport()			{ return autotransport.get(); }
    public void setAutotransport(boolean newValue, boolean save) {
    	governorOnByDefault.set(newValue);
        if(save) save();
    }
    public boolean isAutotransportXilmi()		{ return autotransportXilmi.get(); }
    public void setAutotransportXilmi(boolean newValue, boolean save) {
    	governorOnByDefault.set(newValue);
        if(save) save();
    }
    public boolean isAutotransportUngoverned()	{ return autotransportUngoverned.get(); }
    public void setAutotransportUngoverned(boolean newValue, boolean save) {
    	autotransportUngoverned.set(newValue);
        if(save) save();
    }
    public boolean isTransportRichDisabled()	{ return transportRichDisabled.get(); }
    public void setTransportRichDisabled(boolean newValue, boolean save) {
    	transportRichDisabled.set(newValue);
        if(save) save();
    }
    public boolean isTransportPoorDouble()		{ return transportPoorDouble.get(); }
    public void setTransportPoorDouble(boolean newValue, boolean save) {
    	transportPoorDouble.set(newValue);
        if(save) save();
    }
    public int  getTransportMaxTurns()			{ return transportMaxTurns.get(); }
    public void setTransportMaxTurns(int newValue, boolean save) {
    	transportMaxTurns.set(newValue);
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
    public boolean legacyGrowthMode()			{ return legacyGrowthMode.get(); }
    public void setLegacyGrowthMode(boolean newValue, boolean save) {
    	legacyGrowthMode.set(newValue);
        if(save) save();
    }
    public int  getMinimumMissileBases()		{ return minimumMissileBases.get(); }
    public void setMinimumMissileBases(int newValue, boolean save) {
    	minimumMissileBases.set(newValue);
        if(save) save();
    }
    public boolean getShieldWithoutBases()		{ return shieldWithoutBases.get(); }
    public void setShieldWithoutBases(boolean newValue, boolean save) {
    	shieldWithoutBases.set(newValue);
        if(save) save();
    }
    public boolean isAutospend()				{ return autospend.get(); }
    public void setAutospend(boolean newValue, boolean save) {
    	autospend.set(newValue);
        if(save) save();
    }
    public int  getReserve()					{ return reserve.get(); }
    public void setReserve(int newValue, boolean save) {
    	reserve.set(newValue);
        if(save) save();
    }
    public boolean isShipbuilding()				{ return shipbuilding.get(); }
    public void setShipbuilding(boolean newValue, boolean save) {
    	shipbuilding.set(newValue);
        if(save) save();
    }
    public boolean isAutoInfiltrate()			{ return autoInfiltrate.get(); }
    public void setAutoInfiltrate(boolean newValue, boolean save) {
    	autoInfiltrate.set(newValue);
        if(save) save();
    }
    public boolean isAutoSpy()					{ return autoSpy.get(); }
    public void setAutoSpy(boolean newValue, boolean save) {
    	autoSpy.set(newValue);
        if(save) save();
    }
    public boolean isSpareXenophobes()			{ return spareXenophobes.get(); }
    public void setSpareXenophobes(boolean newValue, boolean save) {
        spareXenophobes.set(newValue);
        if(save) save();
    }
    public boolean isAutoScout()				{ return autoScout.get(); }
    public void setAutoScout(boolean newValue, boolean save) {
    	autoScout.set(newValue);
        if(save) save();
    }
    public boolean isAutoColonize()				{ return autoColonize.get(); }
    public void setAutoColonize(boolean newValue, boolean save) {
    	autoColonize.set(newValue);
        if(save) save();
    }
    public boolean isAutoAttack()				{ return autoAttack.get(); }
    public void setAutoAttack(boolean newValue, boolean save) {
    	autoAttack.set(newValue);
        if(save) save();
    }
    public int  getAutoScoutShipCount()			{ return autoScoutShipCount.get(); }
    public void setAutoScoutShipCount(int newValue, boolean save) {
    	autoScoutShipCount.set(newValue);
        if(save) save();
    }
    public int  getAutoColonyShipCount()		{ return autoColonyShipCount.get(); }
    public void setAutoColonyShipCount(int newValue, boolean save) {
    	autoColonyShipCount.set(newValue);
        if(save) save();
    }
    public int  getAutoAttackShipCount()		{ return autoAttackShipCount.get(); }
    public void setAutoAttackShipCount(int newValue, boolean save) {
    	autoAttackShipCount.set(newValue);
        if(save) save();
    }
    public boolean isAutoShipsByDefault()		{ return autoShipsByDefault.get(); }
    public void setAutoShipsByDefault(boolean newValue, boolean save) {
    	autoShipsByDefault.set(newValue);
        if(save) save();
    }
}
