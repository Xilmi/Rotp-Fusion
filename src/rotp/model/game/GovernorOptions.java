package rotp.model.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.UserPreferences;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;

/**
 * Governor options.
 */
public class GovernorOptions implements Serializable {
    private static final long serialVersionUID = 1l;

    public enum GatesGovernor {
        None,
        Rich,
        All
    }
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
    private boolean shipbuilding = true;

    // if true, new colonies will have auto ship building set to "on"
    // TODO: for future use
    private boolean autoShipsByDefault = true;
    // if true, automatically scout new planets
    private boolean autoScout = true;
    // if true, automatically colonize new planets
    private boolean autoColonize = true;
    // if true, send ships to enemy colonies
    private boolean autoAttack = false;
    // How many ships should Auto* missions send?
    private int autoScoutShipCount = 1;
    private int autoColonyShipCount = 1;
    private int autoAttackShipCount = 1;

    // BR: For the future parameters, to keep save files compatibility
    // Will be saved in the dynamic options list.
	public final static String GOVERNOR_GUI_ID	= "GOVERNOR";
	public final static String GOV_UI			= "GOVERNOR_";
	public final static ParamBoolean spareXenophobes = new ParamBoolean(
			GOV_UI, "SPARE_XENOPHOBES", false);
	public final static LinkedList<InterfaceParam> governorOptions = new LinkedList<>(
			Arrays.asList(
					spareXenophobes
					));

    public GovernorOptions() {
    }

    private void save() {
    	MOO1GameOptions.writeModSettingsToOptions(
    			(MOO1GameOptions) GameSession.instance().options(), GOVERNOR_GUI_ID);
    }

    public boolean isSpareXenophobes() {
        return spareXenophobes.get();
    }

    public void setSpareXenophobes(boolean newValue) {
        spareXenophobes.set(newValue);
        save();
    }

    public boolean isGovernorOnByDefault() {
        return governorOnByDefault;
    }
    
    public boolean legacyGrowthMode() {
        return legacyGrowthMode;
    }

    public boolean isAutotransport() {
        return autotransport;
    }
    
    public boolean isAutotransportXilmi() {
        return autotransportXilmi;
    }
    
    public boolean isAutotransportUngoverned() {
        return autotransportUngoverned;
    }

    public GatesGovernor getGates() {
        return gates;
    }

    public void setGovernorOnByDefault(boolean governorOnByDefault) {
        this.governorOnByDefault = governorOnByDefault;
        UserPreferences.setGovernorOn(governorOnByDefault);
    }
    
    public void setLegacyGrowthMode(boolean legacyGrowthMode) {
        this.legacyGrowthMode = legacyGrowthMode;
        UserPreferences.setLegacyGrowth(legacyGrowthMode);
    }

    public void setAutotransport(boolean autotransport) {
        this.autotransport = autotransport;
    }
    
    public void setAutotransportXilmi(boolean autotransportXilmi) {
        this.autotransportXilmi = autotransportXilmi;
    }
    
    public void setAutotransportUngoverned(boolean autotransportUngoverned) {
        this.autotransportUngoverned = autotransportUngoverned;
    }

    public void setGates(GatesGovernor gates) {
        this.gates = gates;
    }

    public int getTransportMaxTurns() {
        return transportMaxTurns;
    }

    public void setTransportMaxTurns(int transportMaxTurns) {
        this.transportMaxTurns = transportMaxTurns;
    }

    public int getMinimumMissileBases() {
        return minimumMissileBases;
    }
    
    public void setMinimumMissileBases(int minimumMissileBases) {
        this.minimumMissileBases = minimumMissileBases;
    }

    public boolean getShieldWithoutBases() {
        return shieldWithoutBases;
    }
    
    public void setShieldWithoutBases(boolean shieldWithoutBases) {
        this.shieldWithoutBases = shieldWithoutBases;
    }
    
    public boolean isAutospend() {
        return autospend;
    }

    public boolean isAutoApply() {
        return autoApply;
    }

    public boolean isAutoInfiltrate() {
        return autoInfiltrate;
    }
    
    public boolean isAutoSpy() {
        return autoSpy;
    }

    public void setAutoApply(boolean autoApply) {
        this.autoApply = autoApply;
        UserPreferences.setGovernorAutoApply(autoApply);
    }

    public void setAutospend(boolean autospend) {
        this.autospend = autospend;
        UserPreferences.setAutoSpendOn(autospend);
    }
    
    public void setAutoInfiltrate(boolean autoInfiltrate) {
        this.autoInfiltrate = autoInfiltrate;
    }
        
    public void setAutoSpy(boolean autoSpy) {
        this.autoSpy = autoSpy;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public boolean isShipbuilding() {
        return shipbuilding;
    }

    public void setShipbuilding(boolean shipbuilding) {
        this.shipbuilding = shipbuilding;
    }

    public boolean isAutoShipsByDefault() {
        return autoShipsByDefault;
    }

    public void setAutoShipsByDefault(boolean autoShipsByDefault) {
        this.autoShipsByDefault = autoShipsByDefault;
    }

    public boolean isAutoScout() {
        return autoScout;
    }

    public void setAutoScout(boolean autoScout) {
        this.autoScout = autoScout;
    }

    public boolean isAutoColonize() {
        return autoColonize;
    }

    public void setAutoColonize(boolean autoColonize) {
        this.autoColonize = autoColonize;
    }

    public boolean isTransportRichDisabled() {
        return transportRichDisabled;
    }

    public void setTransportRichDisabled(boolean transportRichDisabled) {
        this.transportRichDisabled = transportRichDisabled;
    }

    public boolean isTransportPoorDouble() {
        return transportPoorDouble;
    }

    public void setTransportPoorDouble(boolean transportPoorDouble) {
        this.transportPoorDouble = transportPoorDouble;
    }

    public boolean isAutoAttack() {
        return autoAttack;
    }

    public void setAutoAttack(boolean autoAttack) {
        this.autoAttack = autoAttack;
    }

    public int getAutoScoutShipCount() {
        return autoScoutShipCount;
    }

    public void setAutoScoutShipCount(int autoScoutShipCount) {
        this.autoScoutShipCount = autoScoutShipCount;
    }

    public int getAutoColonyShipCount() {
        return autoColonyShipCount;
    }

    public void setAutoColonyShipCount(int autoColonyShipCount) {
        this.autoColonyShipCount = autoColonyShipCount;
    }

    public int getAutoAttackShipCount() {
        return autoAttackShipCount;
    }

    public void setAutoAttackShipCount(int autoAttackShipCount) {
        this.autoAttackShipCount = autoAttackShipCount;
    }
}
