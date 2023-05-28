package rotp.model.game;

import java.awt.Point;
import java.io.Serializable;

import rotp.ui.UserPreferences;
import rotp.ui.util.AbstractParam;
import rotp.ui.util.InterfaceParam;

/**
 * Governor options.
 */
public class GovernorOptions implements Serializable, IGovOptions {
    private static final long serialVersionUID = 1l;
    public	static final String GOVERNOR_GUI_ID	= "GOVERNOR";
//    public	static final String GOV_UI			= "GOVERNOR_";
    private static boolean callForRefresh		= false;
    private static boolean callForReset			= false;

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

	private transient boolean localUpdate = false;

	// ========== Constructor And Initializers ==========AbstractParam <T>
    public GovernorOptions() {  
    	for (InterfaceParam param : governorOptions)
    		((AbstractParam <?>) param).isGovernor(GOV_REFRESH);

    	auto_Apply.isGovernor(GOV_RESET);
    	customSize.isGovernor(GOV_RESET);
    	animatedImage.isGovernor(GOV_RESET);
    	brightnessPct.isGovernor(GOV_RESET);
    	originalPanel.isGovernor(GOV_RESET);
    	sizeFactorPct.isGovernor(GOV_RESET);
    	verticalPosition.isGovernor(GOV_RESET);
    	horizontalPosition.isGovernor(GOV_RESET);
    }
    public void gameLoaded() {
    	if (autoShipsByDefault) {
    		autoTransport.silentSet(autotransport);
    		autotransportAtMax.silentSet(autotransportXilmi);
    		autotransportAll.silentSet(autotransportUngoverned);
    		transportNoRich.silentSet(transportRichDisabled);
    		transportPoorX2.silentSet(transportPoorDouble);
    		transportMaxDist.silentSet(transportMaxTurns);
    		starGateOption.silentSet(gates.name());
    		missileBasesMin.silentSet(minimumMissileBases);
    		shieldAlones.silentSet(shieldWithoutBases);
    		autoSpend.silentSet(autospend);
    		reserveForSlow.silentSet(reserve);
    		shipBuilding.silentSet(shipbuilding);
    		maxGrowthMode.silentSet(legacyGrowthMode);
    		auto_Infiltrate.silentSet(autoInfiltrate);
    		auto_Spy.silentSet(autoSpy);
    		auto_Scout.silentSet(autoScout);
    		autoScoutCount.silentSet(autoScoutShipCount);
    		auto_Colonize.silentSet(autoColonize);
    		autoColonyCount.silentSet(autoColonyShipCount);
    		auto_Attack.silentSet(autoAttack);
    		autoAttackCount.silentSet(autoAttackShipCount);
    		auto_Apply.silentSet(autoApply);
    		governorByDefault.silentSet(governorOnByDefault);
    		save();
    	}
    	autoShipsByDefault = false;
        // Converted use of autoShipsByDefault: true = not yet transfered.
        // The autoShipsByDefault original function will be implemented using the new parameters
        // if true, new colonies will have auto ship building set to "on"
    }
    public static void callForRefresh(int call)	{
    	callForRefresh	= callForRefresh || (call == GOV_REFRESH);
    	callForReset	= callForReset   || (call == GOV_RESET);
    }
    public void		clearRefresh()		{ callForRefresh = false; }
    public void		clearReset()		{ clearRefresh(); callForReset = false; }
    public boolean	refreshRequested()	{ return callForRefresh; }
    public boolean	resetRequested()	{ return callForReset; }
    
    public boolean isLocalUpdate() {// TODO BR: REMOVE 
    	if (localUpdate) {
    		localUpdate = false;
    		return true;
    	}
    	return false;
    }
    public void save() { // update Quietly // TODO BR: REMOVE all saves
    	localUpdate = true;
//    	IGameOptions opts = GameSession.instance().options();
//    	opts.writeModSettingsToOptions(governorOptions, false);
//    	MOO1GameOptions.writeModSettingsToOptions(
//    			(MOO1GameOptions) GameSession.instance().options(), GOVERNOR_GUI_ID, false);
    }
    public boolean isOriginalPanel()			{ return originalPanel.get(); }
    public void setIsOriginalPanel(boolean newValue, boolean save) {
    	originalPanel.silentSet(newValue);
        if(save) save();
    }
    public boolean isCustomSize()				{ return customSize.get(); }
    public void setIsCustomSize(boolean newValue, boolean save) {
    	customSize.silentSet(newValue);
        if(save) save();
    }
    public boolean isAnimatedImage()			{ return animatedImage.get(); }
    public void setIsAnimatedImage(boolean newValue, boolean save) {
    	animatedImage.silentSet(newValue);
        if(save) save();
    }
    public boolean toggleAnimatedImage() {
    	animatedImage.toggle();
    	save();
    	return animatedImage.get();
    }
    public int  getBrightnessPct()				{ return brightnessPct.get(); }
    public void setBrightnessPct(int newValue, boolean save) {
    	brightnessPct.silentSet(newValue);
        if(save) save();
    }
    public int  getSizeFactorPct()				{ return sizeFactorPct.get(); }
    public void setSizeFactorPct(int newValue, boolean save) {
    	sizeFactorPct.silentSet(newValue);
        if(save) save();
    }
    public int  getPositionX()					{ return horizontalPosition.get(); }
    public void setPositionX(int newValue, boolean save) {
    	horizontalPosition.silentSet(newValue);
        if(save) save();
    }
    public int  getPositionY()					{ return verticalPosition.get(); }
    public void setPositionY(int newValue, boolean save) {
    	verticalPosition.silentSet(newValue);
        if(save) save();
    }
    public Point getPosition()					{ 
    	Point pt = new Point();
	    	pt.x = horizontalPosition.get();
	    	pt.y = verticalPosition.get();
    	return pt;
    }
    public void setPosition(Point pt) {
    	horizontalPosition.silentSet(pt.x);
    	verticalPosition.silentSet(pt.y);
    }
   
    public boolean isAutoApply()				{ return auto_Apply.get(); }
    public void setAutoApply(boolean newValue, boolean save) {
        auto_Apply.silentSet(newValue);
        if(save) save();
    }
    public boolean isGovernorOnByDefault()		{ return governorByDefault.get(); }
    public void setGovernorOnByDefault(boolean newValue, boolean save) {
    	governorByDefault.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutotransport()			{ return autoTransport.get(); }
    public void setAutotransport(boolean newValue, boolean save) {
    	autoTransport.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutotransportXilmi()		{ return autotransportAtMax.get(); }
    public void setAutotransportXilmi(boolean newValue, boolean save) {
    	autotransportAtMax.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutotransportUngoverned()	{ return autotransportAll.get(); }
    public void setAutotransportUngoverned(boolean newValue, boolean save) {
    	autotransportAll.silentSet(newValue);
        if(save) save();
    }
    public boolean isTransportRichDisabled()	{ return transportNoRich.get(); }
    public void setTransportRichDisabled(boolean newValue, boolean save) {
    	transportNoRich.silentSet(newValue);
        if(save) save();
    }
    public boolean isTransportPoorDouble()		{ return transportPoorX2.get(); }
    public void setTransportPoorDouble(boolean newValue, boolean save) {
    	transportPoorX2.silentSet(newValue);
        if(save) save();
    }
    public int  getTransportMaxTurns()			{ return transportMaxDist.get(); }
    public void setTransportMaxTurns(int newValue, boolean save) {
    	transportMaxDist.silentSet(newValue);
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
    	starGateOption.silentSet(gates.name());
        if(save) save();
    }
    public boolean legacyGrowthMode()			{ return maxGrowthMode.get(); }
    public void setLegacyGrowthMode(boolean newValue, boolean save) {
    	maxGrowthMode.silentSet(newValue);
        if(save) save();
    }
    public int  getMinimumMissileBases()		{ return missileBasesMin.get(); }
    public void setMinimumMissileBases(int newValue, boolean save) {
    	missileBasesMin.silentSet(newValue);
        if(save) save();
    }
    public boolean getShieldWithoutBases()		{ return shieldAlones.get(); }
    public void setShieldWithoutBases(boolean newValue, boolean save) {
    	shieldAlones.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutospend()				{ return autoSpend.get(); }
    public void setAutospend(boolean newValue, boolean save) {
    	autoSpend.silentSet(newValue);
        if(save) save();
    }
    public int  getReserve()					{ return reserveForSlow.get(); }
    public void setReserve(int newValue, boolean save) {
    	reserveForSlow.silentSet(newValue);
        if(save) save();
    }
    public boolean isShipbuilding()				{ return shipBuilding.get(); }
    public void setShipbuilding(boolean newValue, boolean save) {
    	shipBuilding.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoInfiltrate()			{ return auto_Infiltrate.get(); }
    public void setAutoInfiltrate(boolean newValue, boolean save) {
    	auto_Infiltrate.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoSpy()					{ return auto_Spy.get(); }
    public void setAutoSpy(boolean newValue, boolean save) {
    	auto_Spy.silentSet(newValue);
        if(save) save();
    }
    public boolean isSpareXenophobes()			{ return spareXenophobes.get(); }
    public void setSpareXenophobes(boolean newValue, boolean save) {
        spareXenophobes.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoScout()				{ return auto_Scout.get(); }
    public void setAutoScout(boolean newValue, boolean save) {
    	auto_Scout.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoColonize()				{ return auto_Colonize.get(); }
    public void setAutoColonize(boolean newValue, boolean save) {
    	auto_Colonize.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoAttack()				{ return auto_Attack.get(); }
    public void setAutoAttack(boolean newValue, boolean save) {
    	auto_Attack.silentSet(newValue);
        if(save) save();
    }
    public int  getAutoScoutShipCount()			{ return autoScoutCount.get(); }
    public void setAutoScoutShipCount(int newValue, boolean save) {
    	autoScoutCount.silentSet(newValue);
        if(save) save();
    }
    public int  getAutoColonyShipCount()		{ return autoColonyCount.get(); }
    public void setAutoColonyShipCount(int newValue, boolean save) {
    	autoColonyCount.silentSet(newValue);
        if(save) save();
    }
    public int  getAutoAttackShipCount()		{ return autoAttackCount.get(); }
    public void setAutoAttackShipCount(int newValue, boolean save) {
    	autoAttackCount.silentSet(newValue);
        if(save) save();
    }
    public boolean isAutoShipsByDefault()		{ return autoShipsDefault.get(); }
    public void setAutoShipsByDefault(boolean newValue, boolean save) {
    	autoShipsDefault.silentSet(newValue);
        if(save) save();
    }
}
