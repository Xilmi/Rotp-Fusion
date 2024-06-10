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

import java.io.Serializable;
import static rotp.model.tech.Tech.miniFastRate;
import rotp.util.Base;

public class EmpireStatus implements Base, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int TURNS = 100;
    final public static int FLEET = 0;
    final public static int POPULATION = 1;
    final public static int TECHNOLOGY = 2;
    final public static int PLANETS = 3;
    final public static int PRODUCTION = 4;
    final public static int POWER = 5;
    final public static int TECHNOLOGY_MAX = 6;

    private final Empire empire;
    // BR: converted to float to avoid power overload!
    private int[] fleetStrength;
    private int[] population;
    private int[] technology;
    private int[] planets;
    private int[] production;
    private int[] power;
    private float[] fleetStrengthF = new float[TURNS];
    private float[] populationF    = new float[TURNS];
    private float[] technologyF    = new float[TURNS];
    private float[] planetsF       = new float[TURNS];
    private float[] productionF    = new float[TURNS];
    private float[] powerF         = new float[TURNS];
    private float[] technologyMaxF = new float[TURNS];

    public EmpireStatus (Empire e) {
        empire = e;
    }
    private float[] convertArray(int[] src) {
    	float[] out = new float[src.length];
    	for (int i=0; i<src.length; i++)
    		out[i] = (float) src[i];
    	return out;
    }
    private float[] fleetStrength() {
    	if (fleetStrengthF == null) {
    		fleetStrengthF = convertArray(fleetStrength);
    		fleetStrength  = null;
    	}
    	return fleetStrengthF;
    }
    private float[] population() {
    	if (populationF == null) {
    		populationF = convertArray(population);
    		population  = null;
    	}
    	return populationF;
    }
    private float[] technology() {
    	if (technologyF == null) {
    		technologyF = convertArray(technology);
    		technology  = null;
    	}
    	return technologyF;
    }
    private float[] planets() {
    	if (planetsF == null) {
    		planetsF = convertArray(planets);
    		planets  = null;
    	}
    	return planetsF;
    }
    private float[] production() {
    	if (productionF == null) {
    		productionF = convertArray(production);
    		production  = null;
    	}
    	return productionF;
    }
    private float[] power() {
    	if (powerF == null) {
    		powerF = convertArray(power);
    		power  = null;
    	}
    	return powerF;
    }
    private float[] technologyMax() {
    	if (technologyMaxF == null)
    		technologyMaxF = technology().clone();
    	return technologyMaxF;
    }
    public String title(int cat) {
        switch (cat) {
            case EmpireStatus.FLEET      : return text("RACES_STATUS_FLEET_STRENGTH");
            case EmpireStatus.POPULATION : return text("RACES_STATUS_POPULATION");
            case EmpireStatus.TECHNOLOGY : return text("RACES_STATUS_TECHNOLOGY");
            case EmpireStatus.PLANETS    : return text("RACES_STATUS_PLANETS");
            case EmpireStatus.PRODUCTION : return text("RACES_STATUS_PRODUCTION");
            case EmpireStatus.POWER      : return text("RACES_STATUS_TOTAL_POWER");
            case EmpireStatus.TECHNOLOGY_MAX : return text("RACES_STATUS_TECHNOLOGY_MAX");
       }
        return "";
    }
    public void assessTurn() {
        int turn = galaxy().numberTurns();
        if (turn >= fleetStrength().length)
            growLists();
        
        fleetStrength()[turn] = currentFleetStrengthValue();
        population()[turn] = currentPopulationValue();
        technology()[turn] = currentTechnologyValue();
        planets()[turn] = currentPlanetsValue();
        production()[turn] = currentProductionValue();
        power()[turn] = currentPowerValue();
        technologyMax()[turn] = currentMaxTechnologyValue();
    }
    public float[] values(int cat) {
        switch (cat) {
            case EmpireStatus.FLEET      : return fleetStrength();
            case EmpireStatus.POPULATION : return population();
            case EmpireStatus.TECHNOLOGY : return technology();
            case EmpireStatus.PLANETS    : return planets();
            case EmpireStatus.PRODUCTION : return production();
            case EmpireStatus.POWER      : return power();
            case EmpireStatus.TECHNOLOGY_MAX : return technologyMax();
        }
        return null;
    }
    public int age(Empire viewer) {
            return galaxy().numberTurns() - lastViewTurn(viewer);
    }
    public float lastViewValue(Empire viewer, int cat) {
        switch(cat) {
            case FLEET:      return valueFor(fleetStrength(), lastViewTurn(viewer));
            case POPULATION: return valueFor(population(), lastViewTurn(viewer));
            case TECHNOLOGY: return valueFor(technology(), lastViewTurn(viewer));
            case PLANETS:    return valueFor(planets(), lastViewTurn(viewer));
            case PRODUCTION: return valueFor(production(), lastViewTurn(viewer));
            case POWER:      return valueFor(power(), lastViewTurn(viewer));
            case TECHNOLOGY_MAX: return valueFor(technologyMax(), lastViewTurn(viewer));
        }
        return 0;
    }
    public int lastTurnAlive() {
    		for (int i=1; i<populationF.length; i++)
    			if(populationF[i] == 0)
    				return i-1;
    		return galaxy().currentTurn();
    }
    private float valueFor(float[] vals, int turn) {
        if (turn < 0)
            return -1;
        if (turn >= vals.length)
            return vals[vals.length-1];
        return vals[turn];
    }
    public int lastViewTurn(Empire viewer) {
        if (empire == viewer)
            return galaxy().numberTurns();

        int lastSpyDate = viewer.viewForEmpire(empire).spies().lastSpyDate();

        return lastSpyDate < 0 ? -1 : lastSpyDate - galaxy().beginningYear();
    }
    private void growLists() {
        fleetStrengthF = larger(fleetStrength());
        populationF = larger(population());
        technologyF = larger(technology());
        planetsF = larger(planets());
        productionF = larger(production());
        powerF = larger(power());
        technologyMaxF = larger(technologyMax());
    }
    private float[] larger(float[] list) {
        float[] newList = new float[list.length+100];
        System.arraycopy(list, 0, newList, 0, list.length);
        return newList;
    }
    private float currentFleetStrengthValue() {
        return (float)Math.ceil(empire.totalFleetSize()); // BR: kept ceil to avoid compatibility issues
    }
    private float currentPlanetsValue() {
        return empire.allColonizedSystems().size();
    }
    private float currentPopulationValue() {
        return (float)Math.ceil(empire.totalEmpirePopulation()); // BR: kept ceil to avoid compatibility issues
    }
    private float currentProductionValue() {
        return (float)Math.ceil(empire.totalPlanetaryProduction()); // BR: kept ceil to avoid compatibility issues
    }
    private float currentTechnologyValue() {
        return (float)Math.ceil(empire.tech().avgTechLevel()); // BR: kept ceil to avoid compatibility issues
    }
    private float currentMaxTechnologyValue() {
        return empire.tech().maxTechLevel();
    }
    public float currentPowerValue() {
        float tech = (float)Math.pow(1 / miniFastRate, empire.tech().avgTechLevel());
        float industrialPower = tech * empire.totalPlanetaryProduction();
        float militaryPower = tech * empire.totalFleetSize();
        return (float) Math.ceil(industrialPower+militaryPower); // BR: kept ceil to avoid compatibility issues
    }
}
