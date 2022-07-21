package mod.br.profileManager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rotp.model.ai.AI;
import rotp.model.empires.Empire;
import rotp.model.empires.Race;
import rotp.model.events.RandomEvent;
import rotp.model.galaxy.GalaxyBullseyeShape;
import rotp.model.galaxy.GalaxyClusterShape;
import rotp.model.galaxy.GalaxyEllipticalShape;
import rotp.model.galaxy.GalaxyFractalShape;
import rotp.model.galaxy.GalaxyGridShape;
import rotp.model.galaxy.GalaxyLorenzShape;
import rotp.model.galaxy.GalaxyMazeShape;
import rotp.model.galaxy.GalaxyRectangularShape;
import rotp.model.galaxy.GalaxyShape;
import rotp.model.galaxy.GalaxyShurikenShape;
import rotp.model.galaxy.GalaxySpiralArmsShape;
import rotp.model.galaxy.GalaxySpiralShape;
import rotp.model.galaxy.GalaxySwirlClustersShape;
import rotp.model.galaxy.GalaxyTextShape;
import rotp.model.galaxy.StarSystem;
import rotp.model.galaxy.StarType;
import rotp.model.game.GameSession;
// import rotp.model.game.GameSession;
import rotp.model.game.IGameOptions;
import rotp.model.game.NewPlayer;
import rotp.model.planet.Planet;
import rotp.model.planet.PlanetType;
import rotp.model.tech.TechEngineWarp;
import rotp.ui.UserPreferences;
import rotp.ui.game.SetupGalaxyUI;
import rotp.util.Base;

/**
 * @author BrokenRegistry
 * Classes that have to be passed thru Profile manager
 * Could be replaced by using {@code Object} and casting the class
 */
public class TEST_ClientClasses extends ClientClasses{
	private IGameOptions options = new Gui();
	private IGameOptions option2 = options;
	private GameSession  session;

	/**
	 * @return the guiObject
	 */
	@Override public IGameOptions newOptions() {
		return options;
	}
	@Override public IGameOptions options() {
		return option2;
	}
	@Override public GameSession session() {
		return session;
	}
	
	// ==============================================================
	// Nested Classes Fake IGameOptions
	//
	static class Gui implements Base, IGameOptions {
	    private static final float BASE_RESEARCH_MOD = 30f;
	    private final String[] opponentRaces = new String[MAX_OPPONENTS];
	    private final List<Integer> colors = new ArrayList<>();
	    private final List<Color> empireColors = new ArrayList<>();
//	    private final NewPlayer player = new NewPlayer();

	    private String selectedGalaxySize;
	    private String selectedGalaxyShape;
	    private String selectedGalaxyShapeOption1;
	    private String selectedGalaxyShapeOption2;
	    
	    private String selectedGalaxyAge;
		// modnar: random tech start
	    private boolean randomTechStart = UserPreferences.randomTechStart();
	    private String selectedGameDifficulty;
	    private String selectedResearchRate;
	    private String selectedTechTradeOption;
	    private String selectedRandomEventOption;
	    private String selectedWarpSpeedOption;
	    private String selectedNebulaeOption;
	    private String selectedCouncilWinOption;
	    private int selectedNumberOpponents;
	    private boolean disableRandomEvents = false;
	    private String selectedStarDensityOption;
	    private String selectedPlanetQualityOption;
	    private String selectedTerraformingOption;
	    private String selectedFuelRangeOption;
	    private String selectedRandomizeAIOption;
	    private String selectedAIHostilityOption;
	    private String selectedColonizingOption;
	    private String selectedOpponentAIOption;
	    private final String[] specificOpponentAIOption = new String[MAX_OPPONENTS+1];
	    private String selectedAutoplayOption;
	    private transient GalaxyShape galaxyShape;
	    
	    private String playerRace = "RACE_HUMAN";
	    private int playerColor   = 3;
	    private String playerLeaderName = "Horace";
	    private String playerHomeWorldName = "Gaia";
	    
	    Gui() {
	    	init();
	    }
	    private void init() {
	        initOpponentRaces();
	        randomizeColors();
	        setDefaultOptionValues();
	    }
	    @Override
	    public int numPlayers()                      { return 1; }
	    @Override
	    public int numColors()                       { return 10; } // modnar: added new colors, but this value should stay == numRaces
	    @Override
	    public NewPlayer selectedPlayer()            { return null; }

	    @Override
	    public boolean disableRandomEvents()         { return disableRandomEvents; }
	    @Override
	    public void disableRandomEvents(boolean b)   { disableRandomEvents = b; }
	    @Override
	    public String selectedGalaxySize()           { return selectedGalaxySize; }
	    @Override
	    public void selectedGalaxySize(String s)     {
	        int prevNumOpp = defaultOpponentsOptions();
	        selectedGalaxySize = s; 
	        if (selectedNumberOpponents() == prevNumOpp)
	            selectedNumberOpponents(defaultOpponentsOptions());
	        generateGalaxy();
	    }
	    @Override
	    public String selectedGalaxyShape()          { return selectedGalaxyShape; }
	    @Override
	    public void selectedGalaxyShape(String s)    { selectedGalaxyShape = s; setGalaxyShape(); generateGalaxy(); }
	    @Override
	    public String selectedGalaxyShapeOption1()       { return selectedGalaxyShapeOption1; }
	    @Override
	    public void selectedGalaxyShapeOption1(String s) { selectedGalaxyShapeOption1 = s; }
	    @Override
	    public String selectedGalaxyShapeOption2()       { return selectedGalaxyShapeOption2; }
	    @Override
	    public void selectedGalaxyShapeOption2(String s) { selectedGalaxyShapeOption2 = s; }
	    @Override
	    public String selectedGalaxyAge()           { return selectedGalaxyAge; }
	    @Override
	    public void selectedGalaxyAge(String s)     { selectedGalaxyAge = s; }
	    @Override
	    public String selectedGameDifficulty()       { return selectedGameDifficulty; }
	    @Override
	    public void selectedGameDifficulty(String s) { selectedGameDifficulty = s; }
	    @Override
	    public String selectedResearchRate()         { return selectedResearchRate == null ? RESEARCH_NORMAL : selectedResearchRate; }
	    @Override
	    public void selectedResearchRate(String s)   { selectedResearchRate = s; }
	    @Override
	    public String selectedTechTradeOption()         { return selectedTechTradeOption == null ? TECH_TRADING_YES : selectedTechTradeOption; }
	    @Override
	    public void selectedTechTradeOption(String s)   { selectedTechTradeOption = s; }
	    @Override
	    public String selectedRandomEventOption()       { return selectedRandomEventOption == null ? RANDOM_EVENTS_NO_MONSTERS : selectedRandomEventOption; }
	    @Override
	    public void selectedRandomEventOption(String s) { selectedRandomEventOption = s; }
	    @Override
	    public String selectedWarpSpeedOption()         { return selectedWarpSpeedOption == null ? WARP_SPEED_NORMAL : selectedWarpSpeedOption; }
	    @Override
	    public void selectedWarpSpeedOption(String s)   { selectedWarpSpeedOption = s; }
	    @Override
	    public String selectedNebulaeOption()           { return selectedNebulaeOption == null ? NEBULAE_NORMAL : selectedNebulaeOption; }
	    @Override
	    public void selectedNebulaeOption(String s)     { selectedNebulaeOption = s; }
	    @Override
	    public String selectedCouncilWinOption()        { return selectedCouncilWinOption == null ? COUNCIL_REBELS : selectedCouncilWinOption; }
	    @Override
	    public void selectedCouncilWinOption(String s)  { selectedCouncilWinOption = s; }
	    @Override
	    public String selectedStarDensityOption()       { return selectedStarDensityOption == null ? STAR_DENSITY_NORMAL : selectedStarDensityOption; }
	    @Override
	    public void selectedStarDensityOption(String s) { selectedStarDensityOption = s; }
	    @Override
	    public String selectedPlanetQualityOption()       { return selectedPlanetQualityOption == null ? PLANET_QUALITY_NORMAL : selectedPlanetQualityOption; }
	    @Override
	    public void selectedPlanetQualityOption(String s) { selectedPlanetQualityOption = s; }
	    @Override
	    public String selectedTerraformingOption()       { return selectedTerraformingOption == null ? TERRAFORMING_NORMAL : selectedTerraformingOption; }
	    @Override
	    public void selectedTerraformingOption(String s) { selectedTerraformingOption = s; }
	    @Override
	    public String selectedColonizingOption()       { return selectedColonizingOption == null ? COLONIZING_NORMAL : selectedColonizingOption; }
	    @Override
	    public void selectedColonizingOption(String s) { selectedColonizingOption = s; }
	    @Override
	    public String selectedFuelRangeOption()       { return selectedFuelRangeOption == null ? FUEL_RANGE_NORMAL : selectedFuelRangeOption; }
	    @Override
	    public void selectedFuelRangeOption(String s) { selectedFuelRangeOption = s; }
	    @Override
	    public String selectedRandomizeAIOption()       { return selectedRandomizeAIOption == null ? RANDOMIZE_AI_NONE : selectedRandomizeAIOption; }
	    @Override
	    public void selectedRandomizeAIOption(String s) { selectedRandomizeAIOption = s; }
	    @Override
	    public String selectedAutoplayOption()          { return selectedAutoplayOption == null ? AUTOPLAY_OFF : selectedAutoplayOption; }
	    @Override
	    public void selectedAutoplayOption(String s)    { selectedAutoplayOption = s; }
	    @Override
	    public String selectedOpponentAIOption()       { return selectedOpponentAIOption == null ? OPPONENT_AI_CRUEL : selectedOpponentAIOption; } // modnar: default to modnar AI
	    @Override
	    public void selectedOpponentAIOption(String s) { selectedOpponentAIOption = s; }
	    @Override
	    public String specificOpponentAIOption(int n)  { 
	            if ((specificOpponentAIOption == null) || (specificOpponentAIOption.length < n))
	                return selectedOpponentAIOption();
	            else
	                return specificOpponentAIOption[n];
	    }
	    @Override
	    public void specificOpponentAIOption(String s, int n) { 
	        if (n < specificOpponentAIOption.length)
	            specificOpponentAIOption[n] = s;
	    }
	    @Override
	    public String selectedAIHostilityOption()       { return selectedAIHostilityOption == null ? AI_HOSTILITY_NORMAL : selectedAIHostilityOption; }
	    @Override
	    public void selectedAIHostilityOption(String s) { selectedAIHostilityOption = s; }
	    @Override
	    public int selectedNumberOpponents()         { return selectedNumberOpponents; }
	    @Override
	    public void selectedNumberOpponents(int i)   { selectedNumberOpponents = i; generateGalaxy(); }
	    @Override
	    public String selectedPlayerRace()           { return playerRace; }
	    @Override
	    public void selectedPlayerRace(String s)     { 
	    	playerRace = s;
    	}
	    @Override public int selectedPlayerColor() { return playerColor; }
	    @Override public void selectedPlayerColor(int i) { playerColor = i; }
	    @Override public String selectedLeaderName() { return playerLeaderName; }
	    @Override public void selectedLeaderName(String s) { playerLeaderName = s.trim(); }
	    @Override public String selectedHomeWorldName() { return playerHomeWorldName; }
	    @Override public void selectedHomeWorldName(String s) { playerHomeWorldName = s.trim(); }
	    @Override
	    public String[] selectedOpponentRaces()      { return opponentRaces; }
	    @Override
	    public String selectedOpponentRace(int i)    { return i >= opponentRaces.length ? null : opponentRaces[i]; }
	    @Override
	    public void selectedOpponentRace(int i, String s) {
	        if (i < opponentRaces.length)
	            opponentRaces[i] = s;
	    }
	    @Override
	    public int maximumOpponentsOptions() {
	        // xilmi: change maxEmpires to be ~3 stars/empire, original ~8 stars/empire
	        int maxEmpires = min(numberStarSystems()/3, colors.size(), MAX_OPPONENT_TYPE*startingRaceOptions().size());
	        int maxOpponents = min(SetupGalaxyUI.MAX_DISPLAY_OPPS);
	        return min(maxOpponents, maxEmpires-1);
	    }
	    @Override
	    public int defaultOpponentsOptions() {
	        int maxEmpires = min((int)Math.ceil(numberStarSystems()/10f), colors.size(), MAX_OPPONENT_TYPE*startingRaceOptions().size());
	        int maxOpponents = min(SetupGalaxyUI.MAX_DISPLAY_OPPS);
	        return min(maxOpponents, maxEmpires-1);
	    }
	    @Override
	    public String name()                 { return "SETUP_RULESET_ORION"; }
	    @Override
	    public void copyOptions(IGameOptions o) { 
	    }
	    @Override
	    public GalaxyShape galaxyShape()   {
	        if (galaxyShape == null)
	            setGalaxyShape();
	        return galaxyShape;
	    }
	    private void setGalaxyShape() {
	        switch(selectedGalaxyShape) {
	            case SHAPE_ELLIPTICAL:
	                galaxyShape = new GalaxyEllipticalShape(this); break;
	            case SHAPE_SPIRAL:
	                galaxyShape = new GalaxySpiralShape(this); break;
	            // modnar: add new map shapes
	            case SHAPE_TEXT:
	                galaxyShape = new GalaxyTextShape(this); break;
	            case SHAPE_CLUSTER:
	                galaxyShape = new GalaxyClusterShape(this); break;
				case SHAPE_SWIRLCLUSTERS:
	                galaxyShape = new GalaxySwirlClustersShape(this); break;
				case SHAPE_GRID:
	                galaxyShape = new GalaxyGridShape(this); break;
				case SHAPE_SPIRALARMS:
	                galaxyShape = new GalaxySpiralArmsShape(this); break;
				case SHAPE_MAZE:
	                galaxyShape = new GalaxyMazeShape(this); break;
				case SHAPE_SHURIKEN:
	                galaxyShape = new GalaxyShurikenShape(this); break;
				case SHAPE_BULLSEYE:
	                galaxyShape = new GalaxyBullseyeShape(this); break;
				case SHAPE_LORENZ:
	                galaxyShape = new GalaxyLorenzShape(this); break;
				case SHAPE_FRACTAL:
	                galaxyShape = new GalaxyFractalShape(this); break;
	            case SHAPE_RECTANGLE:
	            default:
	                galaxyShape = new GalaxyRectangularShape(this);
	        }
	        selectedGalaxyShapeOption1 = galaxyShape.defaultOption1();
	        selectedGalaxyShapeOption2 = galaxyShape.defaultOption2();
	    }
	    @Override
	    public int numGalaxyShapeOption1() {  return galaxyShape.numOptions1(); }
	    @Override
	    public int numGalaxyShapeOption2() {  return galaxyShape.numOptions2(); }
	    @Override
	    public int numberStarSystems() {  // BR: For Profile Manager comments
	    	return numberStarSystems(selectedGalaxySize());
	    }
	    @Override
	    public int numberStarSystems(String size) { // BR: For Profile Manager comments
	        // MOO Strategy Guide, Table 3-2, p.50
	    /*
	    switch (selectedGalaxySize()) {
	            case SIZE_SMALL:  return 24;
	            case SIZE_MEDIUM: return 48;
	            case SIZE_LARGE1:  return 70;
	            case SIZE_HUGE:   return 108;
	            default: return 48;
	    }
	    */
	    switch (size) {
	        case SIZE_TINY:       return 33;
	        case SIZE_SMALL:      return 50;
	        case SIZE_SMALL2:     return 70;
	        case SIZE_MEDIUM:     return 100;
	        case SIZE_MEDIUM2:    return 150;
	        case SIZE_LARGE:      return 225;
	        case SIZE_LARGE2:     return 333;
	        case SIZE_HUGE:       return 500;
	        case SIZE_HUGE2:      return 700;
	        case SIZE_MASSIVE:    return 1000;
	        case SIZE_MASSIVE2:   return 1500;
	        case SIZE_MASSIVE3:   return 2250;
	        case SIZE_MASSIVE4:   return 3333;
	        case SIZE_MASSIVE5:   return 5000;
	        case SIZE_INSANE:     return 10000;
	        case SIZE_LUDICROUS:  return 100000;
	        case SIZE_MAXIMUM:    return maximumSystems();
	    }
	    return 8*(selectedNumberOpponents()+1);
	}
	    @Override
	    public int numberNebula() {
	        if (selectedNebulaeOption().equals(NEBULAE_NONE))
	            return 0;
	        
	        float freq = 1.0f;
	        switch(selectedNebulaeOption()) {
	            case NEBULAE_RARE:     freq = 0.25f; break;
	            case NEBULAE_UNCOMMON: freq = 0.5f; break;
	            case NEBULAE_COMMON:   freq = 2.0f; break;
	            case NEBULAE_FREQUENT: freq = 4.0f; break;
	        }
	        // MOO Strategy Guide, Table 3-3, p.51
	        /*
	        switch (selectedGalaxySize()) {
	        case SIZE_SMALL:     return roll(0,1);
	        case SIZE_MEDIUM:    return roll(1,2);
	        case SIZE_LARGE:     return roll(2,3);
	        case SIZE_HUGE:      return roll(2,4);
	        case SIZE_LUDICROUS: return roll(10,20);
	        default: return roll(1,2);
	        }
	        */
	        int nStars = numberStarSystems();
	        float sizeMult = nebulaSizeMult();
	        int nNeb = (int) nStars/20;
	        
	        return (int) (freq*nNeb/sizeMult/sizeMult);
	    }
	    @Override
	    public float nebulaSizeMult() {
	        int nStars = numberStarSystems();
	        if (nStars < 200)
	            return 1;
	        else 
	            return min(10,sqrt(nStars/200f));
	    }
	    @Override
	    public int selectedAI(Empire e) {
	        if (e.isPlayer()) {
	            switch(selectedAutoplayOption()) {
	                case AUTOPLAY_AI_BASE:   return AI.BASE;
	                case AUTOPLAY_AI_XILMI:  return AI.XILMI;
	                case AUTOPLAY_AI_CRUEL: return AI.CRUEL;
	                case AUTOPLAY_AI_RANDOM:  return AI.RANDOM;
	                case AUTOPLAY_OFF:
	                default:
	                    return AI.XILMI;  // it does matter both for spending reallocation and for ship-captain
	            }
	        }
	        else {
	            switch(selectedOpponentAIOption()) {
	                case OPPONENT_AI_BASE:   return AI.BASE;
	                case OPPONENT_AI_XILMI:  return AI.XILMI;
	                case OPPONENT_AI_CRUEL: return AI.CRUEL;
	                case OPPONENT_AI_UNFAIR: return AI.UNFAIR;
	                case OPPONENT_AI_RANDOM:  return AI.RANDOM;
	                case OPPONENT_AI_ALLRANDOM:  return AI.ALLRANDOM;
	                case OPPONENT_AI_SELECTABLE:
	                    String specificAI = specificOpponentAIOption(e.id);
	                    switch(specificAI) {
	                        case OPPONENT_AI_BASE:   return AI.BASE;
	                        case OPPONENT_AI_XILMI:  return AI.XILMI;
	                        case OPPONENT_AI_CRUEL: return AI.CRUEL;
	                        case OPPONENT_AI_UNFAIR: return AI.UNFAIR;
	                        case OPPONENT_AI_RANDOM:  return AI.RANDOM;
	                        case OPPONENT_AI_ALLRANDOM:  return AI.ALLRANDOM;
	                    }
	            }
	        }
	        return AI.XILMI;
	    }
	    @Override
	    public float hostileTerraformingPct() { 
	        switch(selectedTerraformingOption()) {
	            case TERRAFORMING_NONE:  return 0.0f;
	            case TERRAFORMING_REDUCED: return 0.5f;
	            default:  return 1.0f;
	        }
	    }
	    @Override
	    public float researchCostBase(int techLevel) {
	        // this is a flat research rate adjustment. The method that calls this to calculate
	        // the research cost already factors in the tech level (squared), the map sizes, and
	        // the number of opponents.
	        
	        // the various "slowing" options increase the research cost for higher tech levels
	        
	        // modnar: adjust research costs to asymptotically reach their original scaling
	        // mainly to keep low tech level costs similar to RESEARCH_NORMAL (1.00)
	        // also corrects for old_SLOW's cheaper techLevel==2 and same cost techLevel==3
	        //
	        // techLevel:     2     3     4     5     6     7     8     9     10     20     30     40     50    100
	        // old_SLOW:     0.82  1.00  1.15  1.29  1.41  1.53  1.63  1.73  1.83   2.58   3.16   3.65   4.08   5.77
	        // new_SLOW:     1.15  1.17  1.25  1.34  1.44  1.53  1.62  1.71  1.80   2.53   3.12   3.62   4.06   5.81
	        // old_SLOWER:   1.41  1.73  2.00  2.24  2.45  2.65  2.83  3.00  3.16   4.47   5.48   6.32   7.07  10.00
	        // new_SLOWER:   1.20  1.25  1.40  1.58  1.77  1.96  2.14  2.32  2.49   3.97   5.14   6.14   7.03  10.52
	        // old_SLOWEST:  3.16  3.87  4.47  5.00  5.48  5.92  6.32  6.71  7.07  10.00  12.25  14.14  15.81  22.36
	        // new_SLOWEST:  1.24  1.36  1.75  2.21  2.68  3.15  3.61  4.06  4.49   8.17  11.10  13.60  15.81  24.55
	        
	        float amt = BASE_RESEARCH_MOD;                  // default adjustment
	        switch(selectedResearchRate()) {
	            // mondar: add fast research option
	            case RESEARCH_FAST:
	                return amt*(1.0f/(techLevel+2.0f) + 0.5f);    // mondar: asymptotically approach 2x faster
	            case RESEARCH_SLOW:
	                return amt*((0.6f*techLevel*sqrt(techLevel)+1.0f)/techLevel - 0.2f); // mondar: asymptotically similar
	                //return amt*sqrt(techLevel/3.0f); // approx. 4x slower for level 50
	            case RESEARCH_SLOWER:
	                return amt*((1.2f*techLevel*sqrt(techLevel)+2.0f)/techLevel - 1.5f); // mondar: asymptotically similar
	                //return amt*sqrt(techLevel);   // approx. 7x slower for level 50
	            case RESEARCH_SLOWEST:
	                return amt*((3.0f*techLevel*sqrt(techLevel)+5.0f)/techLevel - 5.5f); // mondar: asymptotically similar
	                //return amt*sqrt(techLevel*5); // approx. 16x slower for level 50
	            default:  
	                return amt;                   // no additional slowing. 
	        }
	    }
	    @Override
	    public  int baseAIRelationsAdj()       { 
	        switch(selectedAIHostilityOption()) {
	            case AI_HOSTILITY_LOWEST:  return 30;
	            case AI_HOSTILITY_LOWER:   return 20;
	            case AI_HOSTILITY_LOW:     return 10;
	            case AI_HOSTILITY_HIGH:    return -10;
	            case AI_HOSTILITY_HIGHER:  return -20;
	            case AI_HOSTILITY_HIGHEST: return -30;
	            default: return 0;
	        } 
	    }

	    @Override
	    public boolean canTradeTechs(Empire e1, Empire e2) {
	        switch(selectedTechTradeOption()) {
	            case TECH_TRADING_YES: return true;
	            case TECH_TRADING_NO:  return false;
	            case TECH_TRADING_ALLIES: return e1.alliedWith(e2.id);
	        }
	        return true;
	    }
	    @Override
	    public boolean allowRandomEvent(RandomEvent ev) {
	        switch(selectedRandomEventOption()) {
	            case RANDOM_EVENTS_ON:  return true;
	            case RANDOM_EVENTS_OFF: return false;
	            case RANDOM_EVENTS_NO_MONSTERS: return !ev.monsterEvent();
	        }
	        return true;
	    }
	    @Override
	    public int warpSpeed(TechEngineWarp tech) {
	        switch(selectedWarpSpeedOption()) {
	            case WARP_SPEED_NORMAL:  return tech.baseWarp();
	            //case WARP_SPEED_FAST: return fibonacci(tech.baseWarp());
	            // modnar: adjust Fast Warp down at advanced Engines
	            //         use [A033638] https://oeis.org/A033638
	            //         a(n) = floor(n^2/4)+1
	            // Normal:     1, 2, 3, 4, 5,  6,  7,  8,  9
	            // FastMOD:    1, 2, 3, 5, 7, 10, 13, 17, 21
	            // Fibonacci:  1, 2, 3, 5, 8, 13, 21, 34, 55
	            case WARP_SPEED_FAST: return quarterSquaresPlusOne(tech.baseWarp());
	        }
	        return tech.baseWarp();
	    }
	    @Override
	    public String randomStarType() {
	        float[] pcts;

	        // normalPcts represents star type distribution per MOO1 Official Strategy Guide
	        //                     RED, ORANG, YELL, BLUE,WHITE, PURP
	        float[] normalPcts = { .30f, .55f, .70f, .85f, .95f, 1.0f };
	        float[] youngPcts  = { .20f, .40f, .55f, .85f, .95f, 1.0f };
	        float[] oldPcts    = { .50f, .65f, .75f, .80f, .85f, 1.0f };

	        int typeIndex = 0;
	        switch(selectedGalaxyAge()) {
	            case GALAXY_AGE_YOUNG:  pcts = youngPcts; break;
	            case GALAXY_AGE_OLD:    pcts = oldPcts; break;
	            default:                pcts = normalPcts; break;
	        }
	        float r = random();
	        for (int i=0;i<pcts.length;i++) {
	            if (r <= pcts[i]) {
	                typeIndex = i;
	                break;
	            }
	        }

	        switch(typeIndex) {
	            case 0:  return StarType.RED;
	            case 1:  return StarType.ORANGE;
	            case 2:  return StarType.YELLOW;
	            case 3:  return StarType.BLUE;
	            case 4:  return StarType.WHITE;
	            case 5:  return StarType.PURPLE;
	            default: return StarType.RED;
	        }
	    }
	    @Override
	    public Planet randomPlanet(StarSystem s) {
	        Planet p = new Planet(s);
	        String[] planetTypes = { "PLANET_NONE", "PLANET_RADIATED", "PLANET_TOXIC", "PLANET_INFERNO",
	                        "PLANET_DEAD", "PLANET_TUNDRA", "PLANET_BARREN", "PLANET_MINIMAL", "PLANET_DESERT",
	                        "PLANET_STEPPE", "PLANET_ARID", "PLANET_OCEAN", "PLANET_JUNGLE", "PLANET_TERRAN" };

	        float[] pcts;

	        float[] redPcts =    { .05f, .10f, .15f, .20f, .25f, .30f, .35f, .40f, .50f, .60f, .75f, .85f, .95f, 1.0f };
	        float[] greenPcts =  { .05f, .10f, .15f, .20f, .25f, .30f, .35f, .40f, .45f, .55f, .65f, .75f, .85f, 1.0f };
	        float[] yellowPcts = { .00f, .00f, .00f, .05f, .05f, .10f, .15f, .20f, .25f, .30f, .40f, .50f, .60f, 1.0f };
	        float[] bluePcts =   { .15f, .25f, .35f, .45f, .55f, .65f, .75f, .85f, .90f, .95f, 1.0f, 1.0f, 1.0f, 1.0f };
	        float[] whitePcts =  { .10f, .15f, .25f, .35f, .45f, .55f, .65f, .75f, .85f, .90f, .95f, 1.0f, 1.0f, 1.0f };
	        float[] purplePcts = { .20f, .45f, .60f, .75f, .85f, .90f, .95f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f };

	        int typeIndex = 0;
	        switch (s.starType().key()) {
	            case StarType.RED:    pcts = redPcts;    break;
	            case StarType.ORANGE:  pcts = greenPcts;  break;
	            case StarType.YELLOW: pcts = yellowPcts; break;
	            case StarType.BLUE:   pcts = bluePcts;   break;
	            case StarType.WHITE:  pcts = whitePcts; break;
	            case StarType.PURPLE: pcts = purplePcts; break;
	            default:
	                pcts = redPcts; break;
	        }

	        float r = random();
	        
	        // modnar: change PLANET_QUALITY settings, comment out poor to great settings
	        /*
	        switch(selectedPlanetQualityOption()) {
	            case PLANET_QUALITY_POOR:     r = random() * 0.8f; break;
	            case PLANET_QUALITY_MEDIOCRE: r = random() * 0.9f; break;
	            case PLANET_QUALITY_NORMAL:   r = random(); break;
	            case PLANET_QUALITY_GOOD:     r = 0.1f + (random() * 0.9f); break;
	            case PLANET_QUALITY_GREAT:    r = 0.2f + (random() * 0.8f); break;
	        }
	        */
	        
	        for (int i=0;i<pcts.length;i++) {
	            if (r <= pcts[i]) {
	                typeIndex = i;
	                break;
	            }
	        }
	        p.initPlanetType(planetTypes[typeIndex]);

	        checkForHostileEnvironment(p, s);

	        checkForPoorResources(p, s);
	        if (p.isResourceNormal())
	            checkForRichResources(p, s);
	        if (p.isResourceNormal())
	            checkForArtifacts(p, s);
	        return p;
	    }
	    @Override
	    public String randomPlayerStarType(Race r)     { return StarType.YELLOW; }
	    @Override
	    public String randomRaceStarType(Race r)       { 
	        List<String> types = new ArrayList<>();
	        types.add(StarType.RED);
	        types.add(StarType.ORANGE);
	        types.add(StarType.YELLOW);

	        return random(types); 
	    }
	    @Override
	    public String randomOrionStarType()       { 
	        List<String> types = new ArrayList<>();
	        types.add(StarType.RED);
	        types.add(StarType.ORANGE);
	        types.add(StarType.YELLOW);

	        return random(types); 
	    }
	    @Override
	    public Planet orionPlanet(StarSystem s) {
	        Planet p = new Planet(s);
	        p.initPlanetType("PLANET_TERRAN");
	        return p;
	    }
	    @Override
	    public Planet randomPlayerPlanet(Race r, StarSystem s) {
	        Planet p = new Planet(s);
	        p.initPlanetType(r.homeworldPlanetType);
	        return p;
	    }
	    @Override
	    public List<String> galaxySizeOptions() {
	        int max = maximumSystems();
	        List<String> list = new ArrayList<>();
	        list.add(SIZE_TINY);
	        if (max > 50)
	            list.add(SIZE_SMALL);
	        if (max > 70)
	            list.add(SIZE_SMALL2);
	        if (max > 100)
	            list.add(SIZE_MEDIUM);
	        if (max > 150)
	            list.add(SIZE_MEDIUM2);
	        if (max > 225)
	            list.add(SIZE_LARGE);
	        if (max > 333)
	            list.add(SIZE_LARGE2);
	        if (max > 500)
	            list.add(SIZE_HUGE);
	        if (max > 700)
	            list.add(SIZE_HUGE2);
	        if (max > 1000)
	            list.add(SIZE_MASSIVE);
	        if (max > 1500)
	            list.add(SIZE_MASSIVE2);
	        if (max > 2250)
	            list.add(SIZE_MASSIVE3);
	        if (max > 3333)
	            list.add(SIZE_MASSIVE4);
	        if (max > 5000)
	            list.add(SIZE_MASSIVE5);
	        if (max > 10000)
	            list.add(SIZE_INSANE);
	        if (max > 100000)
	            list.add(SIZE_LUDICROUS);
	        list.add(SIZE_MAXIMUM);
	        return list;
	    }
	    @Override
	    public List<String> galaxyShapeOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(SHAPE_RECTANGLE);
	        list.add(SHAPE_ELLIPTICAL);
	        list.add(SHAPE_SPIRAL);
	        // mondar: add new map shapes
	        list.add(SHAPE_TEXT);
	        list.add(SHAPE_CLUSTER);
			list.add(SHAPE_SWIRLCLUSTERS);
			list.add(SHAPE_GRID);
			list.add(SHAPE_SPIRALARMS);
			list.add(SHAPE_MAZE);
			list.add(SHAPE_SHURIKEN);
			list.add(SHAPE_BULLSEYE);
			list.add(SHAPE_LORENZ);
			list.add(SHAPE_FRACTAL);
	        return list;
	    }
		
	    @Override
	    public List<String> galaxyShapeOptions1() { return galaxyShape.options1(); }
	    @Override
	    public List<String> galaxyShapeOptions2() { return galaxyShape.options2(); }
	    @Override
	    public List<String> galaxyAgeOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(GALAXY_AGE_YOUNG);
	        list.add(GALAXY_AGE_NORMAL);
	        list.add(GALAXY_AGE_OLD);
	        return list;
	    }
	    @Override
	    public List<String> gameDifficultyOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(DIFFICULTY_EASIEST);
	        list.add(DIFFICULTY_EASIER);
	        list.add(DIFFICULTY_EASY);
	        list.add(DIFFICULTY_NORMAL);
	        list.add(DIFFICULTY_HARD);
	        list.add(DIFFICULTY_HARDER);
	        list.add(DIFFICULTY_HARDEST);
	        // modnar: add custom difficulty level option, set in Remnants.cfg
	        list.add(DIFFICULTY_CUSTOM);
	        return list;
	    }
	    @Override
	    public List<String> researchRateOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(RESEARCH_NORMAL);
	        list.add(RESEARCH_SLOW);
	        list.add(RESEARCH_SLOWER);
	        list.add(RESEARCH_SLOWEST);
	        // mondar: add fast research option
	        list.add(RESEARCH_FAST);
	        return list;
	    }
	    @Override
	    public List<String> techTradingOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(TECH_TRADING_YES);
	        list.add(TECH_TRADING_ALLIES);
	        list.add(TECH_TRADING_NO);
	        return list;
	    }
	    @Override
	    public List<String> randomEventOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(RANDOM_EVENTS_ON);
	        list.add(RANDOM_EVENTS_NO_MONSTERS);
	        list.add(RANDOM_EVENTS_OFF);
	        return list;
	    }
	    @Override
	    public List<String> warpSpeedOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(WARP_SPEED_NORMAL);
	        list.add(WARP_SPEED_FAST);
	        return list;
	    }
	    @Override
	    public List<String> nebulaeOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(NEBULAE_NONE);
	        list.add(NEBULAE_RARE);
	        list.add(NEBULAE_UNCOMMON);
	        list.add(NEBULAE_NORMAL);
	        list.add(NEBULAE_COMMON);
	        list.add(NEBULAE_FREQUENT);
	        return list;
	    }
	    @Override
	    public List<String> councilWinOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(COUNCIL_IMMEDIATE);
	        list.add(COUNCIL_REBELS);
	        list.add(COUNCIL_NONE);
	        return list;
	    }
	    @Override
	    public List<String> starDensityOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(STAR_DENSITY_LOWEST);
	        list.add(STAR_DENSITY_LOWER);
	        list.add(STAR_DENSITY_LOW);
	        list.add(STAR_DENSITY_NORMAL);
	        list.add(STAR_DENSITY_HIGH);
	        list.add(STAR_DENSITY_HIGHER);
	        list.add(STAR_DENSITY_HIGHEST);
	        return list;
	    }
	    @Override
	    public List<String> aiHostilityOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(AI_HOSTILITY_LOWEST);
	        list.add(AI_HOSTILITY_LOWER);
	        list.add(AI_HOSTILITY_LOW);
	        list.add(AI_HOSTILITY_NORMAL);
	        list.add(AI_HOSTILITY_HIGH);
	        list.add(AI_HOSTILITY_HIGHER);
	        list.add(AI_HOSTILITY_HIGHEST);
	        return list;
	    }
	    @Override
	    public List<String> planetQualityOptions() {
	        List<String> list = new ArrayList<>();
	        // modnar: change PLANET_QUALITY settings, add larger and richer, comment out poor to great settings
	        list.add(PLANET_QUALITY_NORMAL);
	        list.add(PLANET_QUALITY_LARGER);
	        list.add(PLANET_QUALITY_RICHER);
	        /*
	        list.add(PLANET_QUALITY_POOR);
	        list.add(PLANET_QUALITY_MEDIOCRE);
	        list.add(PLANET_QUALITY_NORMAL);
	        list.add(PLANET_QUALITY_GOOD);
	        list.add(PLANET_QUALITY_GREAT);
	        */
	        return list;
	    }
	    @Override
	    public List<String> terraformingOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(TERRAFORMING_NORMAL);
	        list.add(TERRAFORMING_REDUCED);
	        list.add(TERRAFORMING_NONE);
	        return list;
	    }
	    @Override
	    public List<String> colonizingOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(COLONIZING_NORMAL);
	        list.add(COLONIZING_RESTRICTED);
	        return list;
	    }
	    @Override
	    public List<String> fuelRangeOptions() {
	        List<String> list = new ArrayList<>();
//	        list.add(FUEL_RANGE_LOCAL);
//	        list.add(FUEL_RANGE_LOW);
	        list.add(FUEL_RANGE_NORMAL);
	        // modnar: comment out fuelRangeOptions from being selected
	        list.add(FUEL_RANGE_HIGH);
	        list.add(FUEL_RANGE_HIGHER);
	        list.add(FUEL_RANGE_HIGHEST);
	        return list;
	    }
	    @Override
	    public List<String> randomizeAIOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(RANDOMIZE_AI_NONE);
	        list.add(RANDOMIZE_AI_PERSONALITY);
	        list.add(RANDOMIZE_AI_ABILITY);
	        list.add(RANDOMIZE_AI_BOTH);
	        return list;
	    }
	    @Override
	    public List<String> autoplayOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(AUTOPLAY_OFF);
	        list.add(AUTOPLAY_AI_BASE);
	        list.add(AUTOPLAY_AI_XILMI);
	        list.add(AUTOPLAY_AI_CRUEL);
	        list.add(AUTOPLAY_AI_RANDOM);
	        return list;
	    }
	    @Override
	    public List<String> opponentAIOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(OPPONENT_AI_BASE);
	        list.add(OPPONENT_AI_XILMI);
	        list.add(OPPONENT_AI_CRUEL);
	        list.add(OPPONENT_AI_UNFAIR);
	        list.add(OPPONENT_AI_RANDOM);
	        list.add(OPPONENT_AI_ALLRANDOM);
	        list.add(OPPONENT_AI_SELECTABLE);
	        return list;
	    }
	    @Override
	    public List<String> specificOpponentAIOptions() {
	        List<String> list = new ArrayList<>();
	        list.add(OPPONENT_AI_BASE);
	        list.add(OPPONENT_AI_XILMI);
	        list.add(OPPONENT_AI_CRUEL);
	        list.add(OPPONENT_AI_UNFAIR);
	        list.add(OPPONENT_AI_RANDOM);
	        list.add(OPPONENT_AI_ALLRANDOM);
	        return list;
	    }
	    @Override
	    public List<String> startingRaceOptions() {
	        List<String> list = new ArrayList<>();
	        list.add("RACE_HUMAN");
	        list.add("RACE_ALKARI");
	        list.add("RACE_SILICOID");
	        list.add("RACE_MRRSHAN");
	        list.add("RACE_KLACKON");
	        list.add("RACE_MEKLAR");
	        list.add("RACE_PSILON");
	        list.add("RACE_DARLOK");
	        list.add("RACE_SAKKRA");
	        list.add("RACE_BULRATHI");
	        return list;
	    }
	    @Override
	    public List<Integer> possibleColors() {
	        return new ArrayList<>(colors);
	    }
	    protected void setDefaultOptionValues() {
	        selectedGalaxySize = SIZE_SMALL;
	        selectedGalaxyShape = galaxyShapeOptions().get(0);
	        selectedGalaxyAge = galaxyAgeOptions().get(1);
	        selectedNumberOpponents = defaultOpponentsOptions();
	        selectedPlayerRace(random(startingRaceOptions()));
	        selectedPlayerRace("RACE_HUMAN");
	        selectedGameDifficulty = DIFFICULTY_NORMAL;
	        selectedOpponentAIOption = OPPONENT_AI_CRUEL;
	        for (int i=0;i<specificOpponentAIOption.length;i++)
	            specificOpponentAIOption[i] = OPPONENT_AI_CRUEL;
	        setToDefault();
	        generateGalaxy();
	    }
	    @Override
	    public void setToDefault() {
	        selectedGalaxyAge = GALAXY_AGE_NORMAL;
	        selectedPlanetQualityOption = PLANET_QUALITY_NORMAL;
	        selectedTerraformingOption = TERRAFORMING_NORMAL;
	        selectedColonizingOption = COLONIZING_NORMAL;
	        selectedResearchRate = RESEARCH_NORMAL;
	        selectedTechTradeOption = TECH_TRADING_YES;
	        selectedRandomEventOption = RANDOM_EVENTS_NO_MONSTERS;
	        selectedWarpSpeedOption = WARP_SPEED_NORMAL;
	        selectedFuelRangeOption = FUEL_RANGE_NORMAL;
	        selectedNebulaeOption = NEBULAE_NORMAL;
	        selectedCouncilWinOption = COUNCIL_REBELS;
	        selectedStarDensityOption = STAR_DENSITY_NORMAL;
	        selectedRandomizeAIOption = RANDOMIZE_AI_NONE;
	        selectedAutoplayOption = AUTOPLAY_OFF;
	        selectedAIHostilityOption = AI_HOSTILITY_NORMAL;
	    }
	    private void generateGalaxy() {
	        galaxyShape().quickGenerate();
	    }
	    @Override
	    public Color color(int i)  { return empireColors.get(i); }
	    @Override
	    public void randomizeColors() {
			// modnar: add new colors
	        empireColors.clear();
			empireColors.add(new Color(237,28,36));   // red
			empireColors.add(new Color(0,166,81));    // green
			empireColors.add(new Color(247,229,60));  // yellow
			empireColors.add(new Color(9,131,214));   // blue
			empireColors.add(new Color(255,127,0));   // orange
			empireColors.add(new Color(145,51,188));  // purple
			empireColors.add(new Color(0,255,255));   // modnar: aqua
			empireColors.add(new Color(255,0,255));   // modnar: fuchsia
			empireColors.add(new Color(132,57,20));   // brown
			empireColors.add(new Color(255,255,255)); // white
			empireColors.add(new Color(0,255,0));     // modnar: lime
			empireColors.add(new Color(128,128,128)); // modnar: grey
			empireColors.add(new Color(220,160,220)); // modnar: plum*
			empireColors.add(new Color(160,220,250)); // modnar: light blue*
			empireColors.add(new Color(170,255,195)); // modnar: mint*
			empireColors.add(new Color(128,128,0));   // modnar: olive**

	        colors.clear();
	        //primary color list
	        List<Integer> list1 = new ArrayList<>();
	        list1.add(0);
	        list1.add(1);
	        list1.add(2);
	        list1.add(3);
	        list1.add(4);
	        list1.add(5);
	        list1.add(6);
	        list1.add(7);
	        list1.add(8);
	        list1.add(9);
			
	        //secondary color list
	        List<Integer> list1a = new ArrayList<>();
	        list1a.add(10);
	        list1a.add(11);
	        list1a.add(12);
	        list1a.add(13);
			list1a.add(14);
	        list1a.add(15);

	        // start repeating the 10-color list for copies of races (up to 5 per race)
	        List<Integer> list2 = new ArrayList<>(list1);
	        list2.addAll(list1a);
	        List<Integer> list3 = new ArrayList<>(list2);
	        List<Integer> list4 = new ArrayList<>(list2);
	        List<Integer> list5 = new ArrayList<>(list2);
	            
	        Collections.shuffle(list1);
	        //Collections.shuffle(list1a); // modnar: no need to shuffle list1a
	        Collections.shuffle(list2);
	        Collections.shuffle(list3);
	        Collections.shuffle(list4);
	        Collections.shuffle(list5);
			// modnar: due to new colors, only add first 10 colors of shuffled lists, subList(0,10)
	        colors.addAll(list1);
	        //colors.addAll(list1a); // modnar: no need to add list1a
	        colors.addAll(list2.subList(0,10));
	        colors.addAll(list3.subList(0,10));
	        colors.addAll(list4.subList(0,10));
	        colors.addAll(list5.subList(0,10));
	    }
	    private void initOpponentRaces() {
	    }
	    private void checkForHostileEnvironment(Planet p, StarSystem s) {
	        // these planet types and no chance for poor resources -- skip
	        switch(p.type().key()) {
	            case PlanetType.NONE:
	                p.makeEnvironmentNone();
	                break;
	            case PlanetType.RADIATED:
	            case PlanetType.TOXIC:
	            case PlanetType.INFERNO:
	            case PlanetType.DEAD:
	            case PlanetType.TUNDRA:
	            case PlanetType.BARREN:
	                p.makeEnvironmentHostile();
	                break;
	            case PlanetType.DESERT:
	            case PlanetType.STEPPE:
	            case PlanetType.ARID:
	            case PlanetType.OCEAN:
	            case PlanetType.JUNGLE:
	            case PlanetType.TERRAN:
	                if (random() < .083333)
	                    p.makeEnvironmentFertile();  // become fertile
	                break;
	        }
	    }
	    private void checkForPoorResources(Planet p, StarSystem s) {
	        // these planet types and no chance for poor resources -- skip
	        switch(p.type().key()) {
	            case PlanetType.NONE:
	            case PlanetType.RADIATED:
	            case PlanetType.TOXIC:
	            case PlanetType.INFERNO:
	            case PlanetType.DEAD:
	            case PlanetType.TUNDRA:
	            case PlanetType.BARREN:
	            case PlanetType.JUNGLE:
	            case PlanetType.TERRAN:
	                return;
	        }

	        float r1 = 0;
	        float r2 = 0;
	        switch(s.starType().key()) {
	            case StarType.BLUE:
	            case StarType.WHITE:
	            case StarType.YELLOW:
	                r1 = .025f; r2 = .10f;
	                break;
	            case StarType.RED:
	                r1 = .06f;  r2 = .20f;
	                break;
	            case StarType.ORANGE:
	                r1 = .135f; r2 = .30f;
	                break;
	            case StarType.PURPLE:
	                // can never have poor/ultrapoor
	                return;
	            default:
	                throw new RuntimeException(concat("Invalid star type for options: ", s.starType().key()));
	        }
	        
	        // modnar: change PLANET_QUALITY settings, 20% more Poor with LARGER, 20% less Poor with RICHER
	        switch(selectedPlanetQualityOption()) {
	            case PLANET_QUALITY_LARGER:   r1 *= 1.2f; r2 *= 1.2f; break;
	            case PLANET_QUALITY_RICHER:   r1 *= 0.8f; r2 *= 0.8f; break;
	            case PLANET_QUALITY_NORMAL:   break;
	            default:    break;
	        }
	        
	        float r = random();
	        if (r <= r1)
	            p.setResourceUltraPoor();
	        else if (r <= r2)
	            p.setResourcePoor();
	    }
	    private void checkForRichResources(Planet p, StarSystem s) {
	        // planet/star ratios per Table 3-9a of Strategy Guide
	        float r1 = 0;
	        float r2 = 0;
	        switch(s.starType().key()) {
	            case StarType.RED:
	            case StarType.WHITE:
	            case StarType.YELLOW:
	            case StarType.ORANGE:
	                switch(p.type().key()) {
	                    case PlanetType.RADIATED:   r1 = .2625f; r2 = .35f; break;
	                    case PlanetType.TOXIC:      r1 = .225f;  r2 = .30f; break;
	                    case PlanetType.INFERNO:    r1 = .1875f; r2 = .25f; break;
	                    case PlanetType.DEAD:       r1 = .15f;   r2 = .20f; break;
	                    case PlanetType.TUNDRA:     r1 = .1125f; r2 = .15f; break;
	                    case PlanetType.BARREN:     r1 = .075f;  r2 = .10f; break;
	                    case PlanetType.MINIMAL:    r1 = .0375f; r2 = .05f; break;
	                }
	                break;
	            case StarType.BLUE:
	                switch(p.type().key()) {
	                    case PlanetType.RADIATED:   r1 = .2925f; r2 = .45f; break;
	                    case PlanetType.TOXIC:      r1 = .26f;   r2 = .40f; break;
	                    case PlanetType.INFERNO:    r1 = .2275f; r2 = .35f; break;
	                    case PlanetType.DEAD:       r1 = .195f;  r2 = .30f; break;
	                    case PlanetType.TUNDRA:     r1 = .1625f; r2 = .25f; break;
	                    case PlanetType.BARREN:     r1 = .13f;   r2 = .20f; break;
	                    case PlanetType.MINIMAL:    r1 = .0975f; r2 = .15f; break;
	                    case PlanetType.DESERT:     r1 = .065f;  r2 = .10f; break;
	                    case PlanetType.STEPPE:     r1 = .0325f; r2 = .05f; break;
	                }
	                break;
	            case StarType.PURPLE:
	                switch(p.type().key()) {
	                    case PlanetType.RADIATED:   r1 = .30f;   r2 = .60f; break;
	                    case PlanetType.TOXIC:      r1 = .275f;  r2 = .55f; break;
	                    case PlanetType.INFERNO:    r1 = .25f;   r2 = .50f; break;
	                    case PlanetType.DEAD:       r1 = .225f;  r2 = .45f; break;
	                    case PlanetType.TUNDRA:     r1 = .20f;   r2 = .40f; break;
	                    case PlanetType.BARREN:     r1 = .175f;  r2 = .35f; break;
	                    case PlanetType.MINIMAL:    r1 = .15f;   r2 = .30f; break;
	                }
	                break;
	            default:
	                throw new RuntimeException(concat("Invalid star type for options: ", s.starType().key()));
	        }

	        // modnar: change PLANET_QUALITY settings, 20% less Rich with LARGER, 50% more Rich with RICHER
	        switch(selectedPlanetQualityOption()) {
	            case PLANET_QUALITY_LARGER:   r1 *= 0.8f; r2 *= 0.8f; break;
	            case PLANET_QUALITY_RICHER:   r1 *= 1.5f; r2 *= 1.5f; break;
	            case PLANET_QUALITY_NORMAL:   break;
	            default:    break;
	        }
	        
	        float r = random();
	        if (r <= r1)
	            p.setResourceRich();
	        else if (r <= r2)
	            p.setResourceUltraRich();
	    }
	    private void checkForArtifacts(Planet p, StarSystem s) {
	        // modnar: no Artifact planets if randomTechStart selected
	        float rArtifact = 1.0f;
	        // modnar: change PLANET_QUALITY settings, 50% more Artifact with RICHER
	        switch(selectedPlanetQualityOption()) {
	            case PLANET_QUALITY_LARGER:   break;
	            case PLANET_QUALITY_RICHER:   rArtifact *= 1.5f; break;
	            case PLANET_QUALITY_NORMAL:   break;
	            default:    break;
	        }
	        if (randomTechStart) {
	            rArtifact *= 0.0f; // modnar: no Artifact planets if randomTechStart selected
	        }
	        switch(p.type().key()) {
	            case PlanetType.STEPPE:
	            case PlanetType.ARID:
	            case PlanetType.OCEAN:
	            case PlanetType.JUNGLE:
	            case PlanetType.TERRAN:
	                if (random() <= 0.10 * rArtifact) // modnar: change artifact ratio for testing, original 0.10
	                    p.setArtifact();
	        }

	    }
	}
}
