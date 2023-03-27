/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.model.empires;

import static rotp.ui.UserPreferences.flagColorCount;
import static rotp.ui.UserPreferences.*;
import static rotp.model.planet.PlanetType.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import rotp.mod.br.addOns.MiscellaneousOptions;
import rotp.mod.br.profiles.Profiles;
import rotp.model.ai.FleetPlan;
import rotp.model.colony.Colony;
import rotp.model.events.SystemScoutedEvent;
import rotp.model.galaxy.IMappedObject;
import rotp.model.galaxy.ShipFleet;
import rotp.model.galaxy.StarSystem;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetType;
import rotp.ui.util.ParamList;
import rotp.ui.util.ParamList.IndexableMap;
import rotp.util.Base;
import rotp.util.ImageManager;
import rotp.util.ModifierKeysState;

public class SystemView implements IMappedObject, Base, Serializable {
    private static final long serialVersionUID = 1L;
    protected static final int UNIMPORTANT   = 0;
    protected static final int INNER_SYSTEM  = 1;
    protected static final int BORDER_SYSTEM = 2;
    protected static final int ATTACK_TARGET = 3;
    
    public  static final String AUTO_FLAG_NOT   = "SETTINGS_MOD_AUTO_FLAG_NO_AUTOMATION";
    private static final String AUTO_FLAG_TYPE  = "SETTINGS_MOD_AUTO_FLAG_TYPE";
    private static final String AUTO_FLAG_ENV   = "SETTINGS_MOD_AUTO_FLAG_ENVIRONMENT";
    private static final String AUTO_FLAG_ASSET = "SETTINGS_MOD_AUTO_FLAG_RESSOURCES";

    public static final String FLAG_COLOR_NONE   = "FLAG_COLOR_NONE";
    public static final String FLAG_COLOR_WHITE  = "FLAG_COLOR_WHITE";
    public static final String FLAG_COLOR_RED    = "FLAG_COLOR_RED";
    public static final String FLAG_COLOR_BLUE   = "FLAG_COLOR_BLUE";
    public static final String FLAG_COLOR_GREEN  = "FLAG_COLOR_GREEN";
    public static final String FLAG_COLOR_YELLOW = "FLAG_COLOR_YELLOW";
    public static final String FLAG_COLOR_AQUA   = "FLAG_COLOR_AQUA";
    public static final String FLAG_COLOR_ORANGE = "FLAG_COLOR_ORANGE";
    public static final String FLAG_COLOR_LTBLUE = "FLAG_COLOR_LTBLUE";
    public static final String FLAG_COLOR_PURPLE = "FLAG_COLOR_PURPLE";
    public static final String FLAG_COLOR_PINK   = "FLAG_COLOR_PINK";
	private static int flagNumber;
	
	public static final IndexableMap flagAssignationMap = new IndexableMap();
	static {
		List<String> flagAssignationList = Arrays.asList (
	    		AUTO_FLAG_NOT,
	    		AUTO_FLAG_TYPE,
	    		AUTO_FLAG_ENV,
	    		AUTO_FLAG_ASSET
				);
		for (String element : flagAssignationList)
			flagAssignationMap.put(element, element); // Temporary; needs to be further initialized
	}
	public static final IndexableMap flagColorMap = new IndexableMap();
	static {
		List<String> flagColorList = Arrays.asList (
			    FLAG_COLOR_NONE, // Don't move this one!
			    FLAG_COLOR_WHITE,
			    FLAG_COLOR_RED,
			    FLAG_COLOR_BLUE,
			    FLAG_COLOR_GREEN,
			    FLAG_COLOR_YELLOW,
			    FLAG_COLOR_AQUA,
			    FLAG_COLOR_ORANGE,
			    FLAG_COLOR_LTBLUE,
			    FLAG_COLOR_PURPLE,
			    FLAG_COLOR_PINK
				);
		flagNumber = flagColorList.size();
		for (String element : flagColorList)
			flagColorMap.put(element, element); // Temporary; needs to be further initialized
	}
    private static final List<String> flagImageNameList = Arrays.asList (
			"Flag_None",
			"Flag_White",
			"Flag_Red",
			"Flag_Blue",
			"Flag_Green",
			"Flag_Yellow",
			"Flag_Aqua",
			"Flag_Orange",
			"Flag_LtBlue",
			"Flag_Purple",
			"Flag_Pink"
			);
    private static final List<String> mapFlagImageNameList = Arrays.asList (
			"Flag_NoneM",
			"Flag_WhiteM",
			"Flag_RedM",
			"Flag_BlueM",
			"Flag_GreenM",
			"Flag_YellowM",
			"Flag_AquaM",
			"Flag_OrangeM",
			"Flag_LtBlueM",
			"Flag_PurpleM",
			"Flag_Pink"
			);

    public  static final int FLAG_NONE = 0;

    // BR: flagColorCount
    private static final int BASE_MASK_COLOR = 127; // "only" 128 colors to avoid sign management
    private static final int[] COLOR_SHIFT = {1, 256, 65536, 16777216};
    private static final int[] COLOR_MASK = { 
    		BASE_MASK_COLOR * COLOR_SHIFT[0],
    		BASE_MASK_COLOR * COLOR_SHIFT[1],
    		BASE_MASK_COLOR * COLOR_SHIFT[2],
    		BASE_MASK_COLOR * COLOR_SHIFT[3]};
    private static final int[] COLOR_IMASK = { // Inverted Mask
    		-1 - COLOR_MASK[0],
    		-1 - COLOR_MASK[1],
    		-1 - COLOR_MASK[2],
    		-1 - COLOR_MASK[3]};

    public static SystemView create(int sysId, int empId) {
        return new SystemView(sysId,empId);
    }

    public final int ownerId;
    public final int sysId;
    private StarSystem relocationSystem;
    private float hostilityLevel = 0;
    private int locationSecurity = INNER_SYSTEM;
    private float scoutTime = 0;
    private float spyTime = 0;

    // viewed variables
    private Empire vEmpire;
    private String vName = "";
    private Planet vPlanet;
    private String vPlanetTypeKey;
    private final List<ShipFleet> vOrbitingFleets = new ArrayList<>();
    private final List<ShipFleet> vExitingFleets = new ArrayList<>();
    private boolean vGuarded = false;
    private int vBases = 0;
    private int vShieldLevel = 0;
    private int vFactories = 0;
    private int vPopulation = 0;
    private int vCurrentSize = 0;
    private int vArtifacts = 0;
    private boolean vStargate = false;
    private int flagColor = FLAG_NONE;
    private boolean forwardRallies = false;

    private transient Empire owner;
    private transient PlanetType vPlanetType;
    private transient FleetPlan fleetPlan;

    /**
     * @return Ordered List of Flag Colors // BR:
     */
    public static List<String> getFlagList() {
    	return Arrays.asList("NONE", "WHITE", "RED", "BLUE", "GREEN", "YELLOW"
    				 , "AQUA", "ORANGE", "LIGHT BLUE", "PURPLE", "PINK");
    }
    public List<ShipFleet> orbitingFleets()  { return vOrbitingFleets; }
    public List<ShipFleet> exitingFleets()   { return vExitingFleets; }
    public StarSystem system()               { return galaxy().system(sysId); }
    public String name()                     { return vName; }
    public void name(String s) {
        system().name(s);
        vName = s;
    }
    public Empire owner() { 
        if (owner == null)
            owner = galaxy().empire(ownerId);
        return owner; 
    }
    public Empire empire()                   { return vEmpire; }
    public int empId()                       { return id(vEmpire); }
    public int population()                  { return vPopulation; }
    public int bases()                       { return vBases; }
    public int shieldLevel()                 { return vShieldLevel; }
    public int factories()                   { return vFactories; }
    public int currentSize()                 { return vCurrentSize; }
    public int artifacts()                   { return vArtifacts; }
    public boolean stargate()                { return vStargate; }
    public void removeStargate()             { vStargate = false;
        if (isColonized())
            colony().removeStargate();
    }
    public Planet planet()                   { return vPlanet; }
    public int locationSecurity()            { return locationSecurity; }
    public float hostilityLevel()            { return hostilityLevel; }
    public float spyTime()                   { return spyTime; }
    public float scoutTime()                 { return scoutTime; }
    public boolean isGuarded()               { return vGuarded; }
    public StarSystem rallySystem()          { return relocationSystem; }
    public float spyTurn()                   { return spyTime - galaxy().beginningYear(); }
    public float scoutTurn()                 { return scoutTime - galaxy().beginningYear(); }
    public void rallySystem(StarSystem sys)  {
        if (canRallyTo(sys)) {
            relocationSystem = (sys == system()) ? null : sys;
            system().rallySprite().clear();
        }
    }
    public boolean forwardRallies()          { return forwardRallies; }
    public void toggleForwardRallies()       { forwardRallies = !forwardRallies; }
    public void stopRally() { 
        relocationSystem = null;
        system().rallySprite().stop(); 
    }
    public void goExtinct() {
        vEmpire = null;
        vBases = 0;
        vShieldLevel = 0;
        vFactories = 0;
        vPopulation = 0;
        vStargate = false;
        clearHostility();
    }
    public int flagColorId()  { return flagColor; }
//    public Color flagColor() { // BR: not used!
//        switch(flagColor) {
//            case FLAG_RED:    return Color.red;
//            case FLAG_WHITE:  return Color.white;
//            case FLAG_BLUE:   return Color.blue;
//            case FLAG_GREEN:  return Color.green;
//            case FLAG_YELLOW: return Color.yellow;
//        }
//        return null; 
//    }
    private void setResourceFlagColor(int id) { // TODO BR: setAssetFlagColor(int id)
    	int color = 0;
    	Planet planet = system().planet();
    	if (planet.type().key() == NONE)
    		color = flagNoneColor.getIndex();
    	else if (planet.isResourceUltraPoor())
    		color = flagUltraPoorColor.getIndex();
    	else if (planet.isResourcePoor())
    		color = flagPoorColor.getIndex();
    	else if (planet.isResourceNormal())
    		color = flagAssetNormalColor.getIndex();
    	else if (planet.isResourceRich())
    		color = flagRichColor.getIndex();
    	else if (planet.isResourceUltraRich())
    		color = flagUltraRichColor.getIndex();
    	else if (planet.isAntaran())
    		color = flagAntaranColor.getIndex();
    	else if (planet.isOrionArtifact())
    		color = flagOrionColor.getIndex();
		setFlagColor(color, id);
    }
    private void setEnvFlagColor(int id) { // TODO BR: setEnvFlagColor(int id)
    	int color = 0;
    	Planet planet = system().planet();
    	if (planet.type().key() == NONE)
    		color = flagEnvNoneColor.getIndex();
    	else if (planet.isEnvironmentNone())
    		color = flagEnvNoneColor.getIndex();
    	else if (planet.isEnvironmentHostile())
    		color = flagEnvHostileColor.getIndex();
    	else if (planet.isEnvironmentNormal())
    		color = flagEnvNormalColor.getIndex();
    	else if (planet.isEnvironmentFertile())
    		color = flagEnvFertileColor.getIndex();
    	else if (planet.isEnvironmentGaia())
    		color = flagEnvGaiaColor.getIndex();
		setFlagColor(color, id);
    }
    private void setTypeFlagColor(int id) {
    	int color;
		switch (system().planet().type().key()) {
	    case NONE:
	    	color = flagAsteroidColor.getIndex();
	    	break;
	    case RADIATED:
	    	color = flagRadiatedColor.getIndex();
	    	break;
	    case TOXIC:
	    	color = flagToxicColor.getIndex();
	    	break;
	    case INFERNO:
	    	color = flagInfernoColor.getIndex();
	    	break;
	    case DEAD :
	    	color = flagDeadColor.getIndex();
	    	break;
	    case TUNDRA:
	    	color = flagTundraColor.getIndex();
	    	break;
	    case BARREN:
	    	color = flagBarrenColor.getIndex();
	    	break;
	    case MINIMAL:
	    	color = flagMinimalColor.getIndex();
	    	break;
	    case DESERT:
	    	color = flagDesertColor.getIndex();
	    	break;
	    case STEPPE:
	    	color = flagSteppeColor.getIndex();
	    	break;
	    case ARID:
	    	color = flagAridColor.getIndex();
	    	break;
	    case OCEAN:
	    	color = flagOceanColor.getIndex();
	    	break;
	    case JUNGLE:
	    	color = flagJungleColor.getIndex();
	    	break;
	    case TERRAN:	
		default:
			color = flagTerranColor.getIndex();
			break;
		}
		setFlagColor(color, id);
    }
    private void setFlagColor(int color, int id) { // BR: flagColorCount
    	flagColor = getMixedColor(flagColor, color, id);
    }
    private int getFlagColor(int id) { // BR: flagColorCount
    	return getBaseColor(flagColor, id);
    }
    private int getMixedColor(int mixedColor, int color, int id) { // BR: flagColorCount
    	return getFilteredColor(mixedColor, id) | getShiftedColor(color, id);
    }
    
    private int getFilteredColor(int mixedColor, int id) { // BR: flagColorCount
    	int idx = min(4, max(1, id))-1;
    	return (mixedColor & COLOR_IMASK[idx]);
    }
    private int getBaseColor(int mixedColor, int id) { // BR: flagColorCount
    	int idx = min(4, max(1, id))-1;
    	return (mixedColor & COLOR_MASK[idx]) / COLOR_SHIFT[idx];
    }
    private int getShiftedColor(int baseColor, int id) { // BR: flagColorCount
    	int idx = min(4, max(1, id))-1;
    	return (baseColor & COLOR_MASK[0]) * COLOR_SHIFT[idx];
    }
    private static Image joinImage(Image left, Image right) { // BR: flagColorCount
    	int side  = 133;
    	int end  = 133;
    	int mid1 = end/2+8;
    	int mid2 = end - mid1;
    	int mast = 97;
    	int fw = mid1;
    	BufferedImage result = new BufferedImage(
                side, side,
                BufferedImage.TYPE_INT_ARGB);
    	Graphics g = result.getGraphics();
    	g.drawImage(right, mid2, 0,  end, side, mast, 0, mast-fw, side, null);
    	g.drawImage(left,  0,    0, mid1, side, mast-fw, 0, mast, side, null);
    	return new ImageIcon(result).getImage();
    }
    public static Image flagBackGround(String name) { // BR: flagColorCount
    	Image fh = ImageManager.current().image(name);
    	switch (flagColorCount.get()) {
    	case 2:
    		return joinImage(fh, fh);
    	default:
    		return fh;
    	}
    }
    public Image flagImage() { // BR: flagColorCount
    	switch (flagColorCount.get()) {
    	case 2:
    		return joinImage(flagImage(getFlagColor(1)), flagImage(getFlagColor(2)));
    	default:
    		return flagImage(flagColor);
    	}
    }
    public Image mapFlagImage() { // BR: flagColorCount
    	switch (flagColorCount.get()) {
    	case 2:
    		return joinImage(mapFlagImage(getFlagColor(1)), mapFlagImage(getFlagColor(2)));
    	default:
    		return mapFlagImage(getFlagColor(1));
    	}
    }
    private Image flagImage(int flagColor) { // BR: flagColorCount
    	if (flagColor < 0 || flagColor >= flagImageNameList.size())
    		return null;
    	return image(flagImageNameList.get(flagColor));
    }
    private Image mapFlagImage(int flagColor) { // BR: flagColorCount
    	if (flagColor <= 0 || flagColor >= mapFlagImageNameList.size())
    		return null;
    	return image(mapFlagImageNameList.get(flagColor));
    }
    // BR: Fix for "Precursor Relic" event; Now every one see the same thing!
    public PlanetType planetType() {
    	if (scouted())
    		return system().planet().type();
    	else
    		return planetType_(); // BR: former call
    }
    // BR: Fix for "Precursor Relic" event; Now everyone sees the same thing!
    private PlanetType planetType_() {
        if (vPlanetTypeKey == null)
            return null;
        if (vPlanetType == null)
            vPlanetType = PlanetType.keyed(vPlanetTypeKey);
        return vPlanetType;
    }
    public void clearFleetPlan() {
        if (fleetPlan != null)
            fleetPlan.clear();
    }
    public boolean hasFleetPlan() {
        return (fleetPlan != null) 
           && (fleetPlan.needsShips() || fleetPlan.isRetreating());
    }
    public FleetPlan fleetPlan()  {
        if (fleetPlan == null)
            fleetPlan = new FleetPlan(ownerId, sysId);
        return fleetPlan;
    }
    public void raiseHostility()                   { hostilityLevel++; }
    public void resetSystemData()                  { setLocationSecurity(); }
    public void refreshSystemEntryScan() {
        vGuarded = system().hasMonster();      
        if (vGuarded)
            setName();
    }
    private void autoFlagAssignation(ParamList assignation, int id) {
    	switch (assignation.get()) {
	    	case AUTO_FLAG_TYPE:
	    		setTypeFlagColor(id);
	    		return;
	    	case AUTO_FLAG_ENV:
	    		setEnvFlagColor(id);
	    		return;
	    	case AUTO_FLAG_ASSET:
	    		setResourceFlagColor(id);
	    		return;
	    	case AUTO_FLAG_NOT:
			default:
				return;
    	}
    }
    public void refreshFullScan() {
        if (!scouted()) {
            log("Orbital scan scouts new system: ", system().name());
            owner().shareSystemInfoWithAllies(this);
            if (owner().isPlayer()) { // TODO BR: Add auto Flag
            	autoFlagAssignation(autoFlagAssignation1, 1);
            	autoFlagAssignation(autoFlagAssignation2, 2);
//            	switch (autoFlagAssignation1.get()) {
//	            	case AUTO_FLAG_TYPE:
//	            		setTypeFlagColor(1);
//	            		break;
//	            	case AUTO_FLAG_ENV:
//	            		setEnvFlagColor(1);
//	            		break;
//	            	case AUTO_FLAG_ASSET:
//	            		setResourceFlagColor(1);
//	            		break;
//	            	case AUTO_FLAG_NOT:
//	        		default:
//	        			break;
//            	}
//            	switch (autoFlagAssignation2.get()) {
//	            	case AUTO_FLAG_TYPE:
//	            		setTypeFlagColor(2);
//	            		break;
//	            	case AUTO_FLAG_ENV:
//	            		setEnvFlagColor(2);
//	            		break;
//	            	case AUTO_FLAG_ASSET:
//	            		setResourceFlagColor(2);
//	            		break;
//	            	case AUTO_FLAG_NOT:
//	        		default:
//	        			break;
//            	}
 // TODO BR:           	System.out.println("System Scouted ready for auto Flag");
            }
            if (owner().isPlayerControlled()) {
                session().addSystemScouted(system());
                if (system().empire() != player())
                    system().addEvent(new SystemScoutedEvent(player().id));
            }
        }
        scoutTime = galaxy().currentYear();
        spyTime = galaxy().currentYear();
        setName();
        setEmpire();
        setPlanetData();
        setColonyData();
        setOrbitingFleets();
        refreshSystemEntryScan();
                
        if (system().hasBonusTechs())
            owner().plunderAncientTech(system());
    }
    public void refreshAllySharingScan() {
        if (owner().isPlayerControlled() && !scouted()) {
            log("Ally shares new system data: ", system().name());
            session().addSystemScoutedByAllies(system());
        }

        scoutTime = galaxy().currentYear();
        setName();
        setEmpire();
        setPlanetData();
    }
    public void refreshLongRangePlanetScan() {
        if (!scouted()) {
            log("Long range planet scan scouts new system: ", system().name());
            owner().shareSystemInfoWithAllies(this);
            if (owner().isPlayerControlled())
                session().addSystemScoutedByAstronomers(system());
        }

        scoutTime = galaxy().currentYear();
        setName();
        setEmpire();
        setPlanetData();
        setOrbitingFleets();
    }
    public void refreshLongRangeShipScan() {
        if (!scouted()) {
            log("Long range ship scan scouts new system: ", system().name());
            owner().shareSystemInfoWithAllies(this);
            if (owner().isPlayer())
                session().addSystemScouted(system());
        }

        scoutTime = galaxy().currentYear();
        setName();
        setEmpire();
        setPlanetData();
        setOrbitingFleets();
    }
    public void refreshSpyScan() {
        setName();
        setEmpire();
        setColonyData();
        spyTime = galaxy().currentYear();
    }
    public void setEmpire() {
        // if the empire has changed, reset the spy time
        Empire prevEmpire = vEmpire;
        vEmpire = system().empire();
        if (vEmpire != prevEmpire)
            spyTime = 0;
        
        if (!owner().aggressiveWith(id(vEmpire)))
            clearHostility();
    }
    private void setPlanetData() {
        vArtifacts = system().planet().artifacts();
        vPlanet = system().planet();
        // BR: Fix for "Precursor Relic" event
        // vPlanetTypeKey = system().planet().type().key();
        if (vPlanetTypeKey != system().planet().type().key()) {
        	vPlanetTypeKey = system().planet().type().key();
        	// BR: Force actualization
        	vPlanetType = null;
        	planetType_();
        }
        vCurrentSize = (int) system().planet().currentSize();
    }
    private void setColonyData() {
        Colony col = system().colony();
        vStargate = system().isColonized() && col.hasStargate();
        vBases = 0;
        vFactories = 0;
        vPopulation = 0;
        vShieldLevel = 0;
        if (isColonized()) {
            float actualPop = col.population();
            vBases = col.defense().missileBases();
            vFactories = (int) col.industry().factories();
            vPopulation = actualPop < 1 ? (int) Math.ceil(actualPop) : (int) actualPop;
            vShieldLevel = col.defense().shieldLevel();
            vCurrentSize = (int) system().planet().currentSize();
        }
    }
    private void setOrbitingFleets() {
        orbitingFleets().clear();
        exitingFleets().clear();
        vGuarded = system().hasMonster();
        vOrbitingFleets.clear();
        vOrbitingFleets.addAll(system().orbitingFleets());
        vExitingFleets.clear();
        vExitingFleets.addAll(system().exitingFleets());
    }
    public void clearFleetInfo() {
        orbitingFleets().clear();
        exitingFleets().clear();
    }

    public float distance()                 { return owner().sv.distance(system().id); }
    public boolean hasRallyPoint()           { return rallySystem() != null; }
    public Colony colony()                   { return system() == null ? null : system().colony(); }
    public boolean abandoned()               { return system() == null ? false : system().abandoned(); }
    public Integer deltaPopulation()         { return isColonized() ? colony().deltaPopulation() : 0; }
    public Integer deltaFactories()          { return isColonized() ? colony().industry().deltaFactories() : 0; }
    public Integer deltaBases()              { return isColonized() ? colony().defense().deltaBases() : 0; }
    public BufferedImage planetTerrain()     { return planetType() == null ? null : planetType().terrainImage(); }

    public boolean resourceUltraRich()       { return (planet() != null) && planet().isResourceUltraRich(); }
    public boolean resourceRich()            { return (planet() != null) && planet().isResourceRich(); }
    public boolean resourceNormal()          { return (planet() != null) && planet().isResourceNormal(); }
    public boolean resourcePoor()            { return (planet() != null) && planet().isResourcePoor(); }
    public boolean resourceUltraPoor()       { return (planet() != null) && planet().isResourceUltraPoor(); }
    public boolean artifact()                { return (planet() != null) && planet().isArtifact(); }
    public boolean orionArtifact()           { return (planet() != null) && planet().isOrionArtifact(); }
    
    public boolean environmentHostile()     { return (planet() != null) && planet().isEnvironmentHostile(); }
    public boolean environmentFertile()     { return (planet() != null) && planet().isEnvironmentFertile(); }
    public boolean environmentGaia()        { return (planet() != null) && planet().isEnvironmentGaia(); }

    public void resetFlagColor()            { flagColor = FLAG_NONE; }

    public void toggleFlagColor(boolean reverse) { // BR: flagColorCount
    	int id = 1;
    	if(ModifierKeysState.isShiftDown())
    		id = 2;
    	setFlagColor(toggleFlagColor(reverse, getFlagColor(id)), id);
    }
    private int toggleFlagColor(boolean reverse, int flagColor) { // BR: flagColorCount
    	if (Profiles.isFlagColorOrderEnabled()) { // BR:
    		return MiscellaneousOptions.getNextFlagColor(flagColor, reverse);
    	}
		if (reverse) {
			flagColor--;
			if (flagColor < 0)
				return flagNumber-1;
			return flagColor;
		} else {
			flagColor++;
			if (flagColor >= flagNumber)
				return 0;
			return flagColor;
		}
    }
    public String resourceType() {
        if (artifact() || orionArtifact())
            return "PLANET_ARTIFACTS";
        if (resourceUltraPoor())
            return "PLANET_ULTRA_POOR";
        else if (resourcePoor())
            return "PLANET_POOR";
        else if (resourceUltraRich())
            return "PLANET_ULTRA_RICH";
        else if (resourceRich())
            return "PLANET_RICH";
        else
            return "";
    }
    public String ecologyType() {
        if (environmentHostile())
            return "PLANET_HOSTILE";
        else if (environmentFertile())
            return "PLANET_FERTILE";
        if (environmentGaia())
            return "PLANET_GAIA";
        else
            return "";
    }
    public boolean canSabotageBases()        { return bases() > 0; }
    public boolean canSabotageFactories()    { return factories() > 0; }
    public boolean canInciteRebellion()      { 
        if (!isColonized())
            return false;
        if (colony().inRebellion())
            return false;
        if (colony().isCapital())
            return false;
        // we cannot incite a rebellion in an enemy system that the enemy cannot
        // currently colonize (to send transports to stop rebellion). This can
        // occur if, for example, the system is degraded with a random event
        if (!empire().canColonize(system()))
            return false;
        // special case: we cannot incite rebellion against a final war enemy 
        // if we are rebelling against the New Republic
        Empire leader = galaxy().council().leader();
        if (leader != null) {
            if (owner().viewForEmpire(vEmpire).embassy().finalWar()) {                   
                if ((vEmpire == leader) || vEmpire.viewForEmpire(leader).embassy().unity())
                    return false;
            }
        }
        return true;
    }

    public boolean scouted()                 { return (owner() == empire()) || (scoutTime() > 0); }
    public boolean spied()                   { return (owner() == empire()) || (spyTime() > 0); }
    public int lastReportYear()              { return (owner() == empire()) ? galaxy().currentYear() : (int) spyTime(); }
    public int lastReportTurn()              { return (int) max(spyTurn(), scoutTurn()); }
    public int spyReportAge()                { return galaxy().currentYear() - lastReportYear(); }

    public boolean canRallyTo(StarSystem sys) { return sys.empire() == owner(); }

    public float distanceTo(SystemView v)   { return system().distanceTo(v.system()); }

    public int desiredMissileBases() {
        return (empire() == owner()) ? colony().defense().maxBases() : 0;
    }
    public boolean innerSystem()             { return locationSecurity() == INNER_SYSTEM; }
    public boolean supportSystem()           { return false; }
    public boolean borderSystem()            { return locationSecurity() == BORDER_SYSTEM; }
    public boolean attackTarget()            { return locationSecurity() == ATTACK_TARGET; }

    public boolean isColonized()             { return (empire() != null) && (colony() != null); }
    public boolean isInEmpire()              { return owner() == system().empire();}
    
    public boolean isAlert() {
        if (vName.isEmpty())
            return false;
        if (scouted() && system().hasEvent())
            return true;
        if(system().empire() == player())
        {
            for (ShipFleet fl: orbitingFleets()) {
                if (fl.isPotentiallyArmed(player())) {
                    if (player().atWarWith(fl.empId())) { 
                        return true;
                    }  
                }
            }
        }
        return (scouted() && isColonized() && colony().inRebellion());
    }
    public String descriptiveName() {
        if (!isColonized()) {
            if (!scouted()) 
                return text("MAIN_UNSCOUTED");
            else if (system().planet().isEnvironmentNone())
                return text("MAIN_NO_PLANETS");
            else if (abandoned())
                return text("MAIN_ABANDONED");
            else
                return text("MAIN_NO_COLONIES");
        }
        String name;
        if (!scouted())
            name = text("PLANET_WORLD",empire().raceName());
        else if (empire().isHomeworld(system()))
            name = text("PLANET_HOMEWORLD",empire().raceName());
        else if (empire().isColony(system()))
            name = text("PLANET_COLONY",empire().raceName());
        else
            name = text("PLANET_WORLD",empire().raceName());
        
        name = empire().replaceTokens(name, "alien");
        return name;
    }

    @Override
    public String toString()       { return concat("View: ", name()); }
    @Override
    public float x()               	  			{ return system().x();  }
    @Override
    public float y()             				  { return system().y();  }

    public boolean hasActiveTransport()        { return isColonized() && colony().transport().isActive(); }
    public boolean hasFleetForCiv (Empire c) {
        return system().hasFleetForEmpire(c);
    }
    public boolean inRange(float range) {
        if (distance() <= range)
            return true;
        for (Empire ally: owner().allies()) {
            if (ally.sv.withinRange(system().id, range))
                return true;
        }
        return false;
    }
    public boolean inShipRange()  { return inRange(owner().tech().shipRange()); }
    public int maxPopToGive(float targetPopPct) {
        if (!colony().canTransport())
            return 0;

        int p1 = colony().maxTransportsAllowed();
        int p2 = (int) (colony().population() - (targetPopPct * system().planet().currentSize()));
        return Math.min(p1,p2);
    }
    public float defenderCombatAdj() {
        if (empire() == owner())
            return colony().defenderCombatAdj();

        EmpireView cv = owner().viewForEmpire(empire());

        if (cv == null)
            return 0;

        return cv.spies().tech().troopCombatAdj(true);
    }
    public int rallyTurnsTo(StarSystem dest) {
            return (int) Math.ceil(system().rallyTimeTo(dest));
    }
    public int maxTransportsToReceive() {
        if (isColonized()) {
            if (owner() == empire())
                return currentSize() - (int) colony().workingPopulation();
            else
                return currentSize();
        }
        return 0;
    }
    public int maxTransportsToSend() {
        return population() <= 1 ? 0 : population() / 2;
    }
    private SystemView(int sId, int empId) {
        ownerId = empId;
        sysId = sId;
    }
    private void setLocationSecurity() {
        if (distance() > owner().scoutRange()) {
            locationSecurity = UNIMPORTANT;
            return;
        }
        locationSecurity = INNER_SYSTEM;
        float dangerRange = owner().shipRange() + 1;
        for (int i=0; i< system().numNearbySystems();i++) {
            StarSystem sys = system().nearbySystem(i);
            if (distanceTo(sys) > dangerRange)
                break;
            int empId = owner().sv.empId(sys.id);
            if (empId != Empire.NULL_ID) {
                if (owner().atWarWith(empId)) {
                    locationSecurity = ATTACK_TARGET;
                    break;
                }
                if (!owner().friendlyWith(empId))
                    locationSecurity = BORDER_SYSTEM;
            }
        }
    }
    private void setName() {
        if (!system().unnamed())
            vName = system().name();
        else if (vName.isEmpty()) {  // BR: for custom Races
        	vName = owner().dataRace().worldsPrefix
        			+ owner().race().randomSystemName(owner())
        			+ owner().dataRace().worldsSuffix;
        }
    }
    private void clearHostility()                   { hostilityLevel = 0; }
}
