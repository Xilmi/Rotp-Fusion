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

import static rotp.ui.UserPreferences.randomAlienRacesMax;
import static rotp.ui.UserPreferences.randomAlienRacesMin;
import static rotp.ui.UserPreferences.randomAlienRacesSmoothEdges;
import static rotp.ui.UserPreferences.randomAlienRacesTargetMax;
import static rotp.ui.UserPreferences.randomAlienRacesTargetMin;
import static rotp.ui.UserPreferences.randomAlienRaces;
import static rotp.ui.util.SettingBase.CostFormula.DIFFERENCE;
import static rotp.util.Base.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import br.profileManager.src.main.java.PMutil;
import rotp.model.game.MOO1GameOptions;
import rotp.model.planet.PlanetType;
import rotp.ui.util.SettingBase;
import rotp.ui.util.SettingBoolean;
import rotp.ui.util.SettingInteger;

public class CustomRaceFactory {
	
	public static final String ROOT = "CUSTOM_RACE_";
	public static final String PLANET = "PLANET_";
	private static final boolean booleansAreBullet = true;
	private static final boolean saveNotAllowed = false;

	private Race race;
	private LinkedList<SettingBase<?>> settingList;
	private LinkedHashMap<String, String> settingsValues = new LinkedHashMap<>();
	
	public CustomRaceFactory() {}
	public void init(LinkedList<SettingBase<?>> newSettingList) {
		settingList = newSettingList;
	}
	public boolean isEmpty() {
		return settingList == null;
	}
	/**
	 * @param raceKey the new race
	 * @return this for chaining purpose
	 */
	public void setRace(String raceKey) {
		if (raceKey == null)
			race = null;
		else if (race == null
				|| !race.name().equalsIgnoreCase(raceKey)) {
			race = Race.keyed(raceKey).copy();
		}
	}
	public void initShowRace(String raceKey) {
		race = Race.keyed(raceKey);
		pullSettings();
	}
	public Race getRace() {
		return race;
	}
	private void setKey(String keyString) {
		String[] list = keyString.split(",");
		for (int i=0; i<list.length; i+=2) {
			settingsValues.put(list[i], list[i+1]);
		}
		setValues();
	}
	public String getKey() {
		LinkedList<String> list = new LinkedList<>();
		for (SettingBase<?> setting : settingList) {
			if (!setting.isSpacer()) {
				list.add(setting.cfgName());
				list.add(setting.cfgValue());
			}
		}
		return String.join(",", list);
	}
	public int getCount() {
		int count = 0;
		for (SettingBase<?> setting : settingList) {
			if (!setting.isSpacer()
					&& !setting.hasNoCost())
				count++;
		}
		return count;
	}
	private void randomizeRace(float min, float max,
			float targetMin, float targetMax, boolean gaussian, boolean updateGui) {
		float target	= (targetMax + targetMin)/2;
		float maxDiff	= Math.max(0.04f, Math.abs(targetMax-targetMin)/2);
		float maxChange	= Math.max(0.1f, Math.abs(max-min));
		
		List<SettingBase<?>> shuffledSettingList = new ArrayList<>(settingList);
		// first pass full random
		float cost = randomizeRace(min, max, gaussian, updateGui);
		
		// second pass going smoothly to the target
		for (int i=0; i<10; i++) {
			Collections.shuffle(shuffledSettingList);
			for (SettingBase<?> setting : shuffledSettingList) {
				if (!setting.isSpacer() &&!setting.hasNoCost()) {
					float difference = target - cost;
					if (Math.abs(difference) <= maxDiff)
						return;
					float costFactor = setting.costFactor();
					float changeRequest = difference/costFactor;
					cost -= setting.settingCost();
					if (Math.abs(changeRequest) <= maxChange) {
						float maxRequest = changeRequest + maxDiff/costFactor;
						if (maxRequest > 0)
							maxRequest = Math.min(maxChange, maxRequest);
						else
							maxRequest =  Math.max(-maxChange, maxRequest);						
						float minRequest = changeRequest - maxDiff/costFactor;
						if (maxRequest > 0)
							minRequest = Math.min(maxChange, minRequest);
						else
							minRequest =  Math.max(-maxChange, minRequest);
						float request = minRequest + random.nextFloat() * (maxRequest-minRequest);

						setting.setValueFromCost(setting.settingCost()+request*costFactor);
					} else {
						setting.setRandom(setting.lastRandomSource()
								+ maxChange*Math.signum(changeRequest));
					}
					cost += setting.settingCost();
					if (updateGui)
						setting.guiSelect();
				}
			}				
		}
		// third pass forcing the target
		for (int i=0; i<5; i++) {
			Collections.shuffle(shuffledSettingList);
			for (SettingBase<?> setting : shuffledSettingList) {
				if (!setting.isSpacer() &&!setting.hasNoCost()) {
					cost -= setting.settingCost();
					setting.setValueFromCost(target - cost);
					cost += setting.settingCost();
					if (updateGui)
						setting.guiSelect();
					if (Math.abs(cost-target) <= maxDiff)
						return;
				}
			}				
		}
	}
	private float randomizeRace(float min, float max, boolean gaussian, boolean updateGui) {
		for (SettingBase<?> setting : settingList) {
			if (!setting.isSpacer()) {
				setting.setRandom(min, max, gaussian);
				if (updateGui)
					setting.guiSelect();
			}
		}
		return getTotalCost();
	}
	public void randomizeRace(float min, float max, float targetMin, float targetMax, 
			boolean useTarget, boolean gaussian, boolean updateGui) {
		if (useTarget)
			randomizeRace(min, max, targetMin, targetMax, gaussian, updateGui);
		else
			randomizeRace(min, max, gaussian, updateGui);
	}
	public  float getTotalCost() {
		float totalCost = 0;
		for (SettingBase<?> setting : settingList) {
			if (setting.isSpacer())
				continue;
			totalCost += setting.settingCost();
		}
		return totalCost;
	}
	public void pushSettings() {
		for (SettingBase<?> setting : settingList) {
			setting.pushSetting();
		}
	}
	public void pullSettings() {
		for (SettingBase<?> setting : settingList) {
			setting.pullSetting();
		}
	}
	private float updateSettings() {
		for (SettingBase<?> setting : settingList) {
			setting.updateGui();
		}
		return getTotalCost();
	}
	private void setValues() {
		for (SettingBase<?> setting : settingList) {
			if (!setting.isSpacer()) {
				String value = settingsValues.get(setting.cfgName());
				if (value != null)
					setting.setFromCfgValue(value);
			}
		}
		pushSettings();
	}
	private void getFullList() {
		settingList = new LinkedList<>();
		settingList.add(new BaseDataRace());
		settingList.add(new RacePlanetType());
		settingList.add(new HomeworldSize());
//		settingList.add(new SpeciesType()); // Not used in Game
		settingList.add(new PopGrowRate());
		settingList.add(new IgnoresEco());
		settingList.add(new ShipAttack());
		settingList.add(new ShipDefense());
		settingList.add(new ShipInitiative());
		settingList.add(new GroundAttack());
		settingList.add(new SpyCost());
		settingList.add(new SpySecurity());
		settingList.add(new SpyInfiltration());
//		settingList.add(new SpyTelepathy()); // Not used in Game
		settingList.add(new DiplomacyTrade());
// 		settingList.add(new DiploPosDP()); // Not used in Game
		settingList.add(new DiplomacyBonus());
		settingList.add(new DiplomacyCouncil());
		settingList.add(new RelationDefault());	// BR: Maybe All the races
		settingList.add(new ProdWorker());
		settingList.add(new ProdControl());
		settingList.add(new IgnoresFactoryRefit());
		settingList.add(new TechDiscovery());
		settingList.add(new TechResearch());
		settingList.add(new ResearchComputer());
		settingList.add(new ResearchConstruction());
		settingList.add(new ResearchForceField());
		settingList.add(new ResearchPlanet());
		settingList.add(new ResearchPropulsion());
		settingList.add(new ResearchWeapon());
		settingList.add(new PlanetRessources());
		settingList.add(new PlanetEnvironment());
		settingList.add(new CreditsBonus());
		settingList.add(new HitPointsBonus());
		settingList.add(new MaintenanceBonus());
		settingList.add(new ShipSpaceBonus());
	}
	// -------------------- Static Methods --------------------
	// 
	public static String getRandomAlienRaceKey() {
		CustomRaceFactory cr = new CustomRaceFactory();
		cr.getFullList();
		cr.randomizeRace(randomAlienRacesMin.get(), randomAlienRacesMax.get(),
				randomAlienRacesTargetMin.get(), randomAlienRacesTargetMax.get(),
				randomAlienRacesSmoothEdges.get(), randomAlienRaces.isTarget(), false);
		return cr.getKey();
	}
	public static String raceToKey(Race race) {
		CustomRaceFactory cr = new CustomRaceFactory();
		cr.getFullList();
		cr.setRace(race.name());
		cr.pullSettings();
		return cr.getKey();
	}
	public static Race keyToRace(String raceKey) {
		CustomRaceFactory cr = new CustomRaceFactory();
		cr.getFullList();
		cr.setKey(raceKey);
		return cr.race;
	}
	public static int keyToValue(String raceKey) {
		CustomRaceFactory cr = new CustomRaceFactory();
		cr.getFullList();
		cr.setRace(raceKey);
		cr.pullSettings();
		return Math.round(cr.getTotalCost()); 
	}
	// ==================== Nested Classes ====================
	//
	// ==================== Spacer ====================
	//
	public class Spacer extends SettingBase<String> {
		
		public Spacer() {
			super("", "");
			isSpacer(true);
			saveAllowed(false);
		}
	}
	// ==================== BaseDataRace ====================
	//
	public class BaseDataRace extends SettingBase<String> {
		private boolean updateAllowed	= true;
		private boolean pullAllowed		= true;
		private boolean costInitialized	= false;
		public BaseDataRace() {
			super(ROOT, "BASE_DATARACE");
			isBullet(true);
			saveAllowed(false);
			labelsAreFinals(true);
			hasNoCost(true);
			for (String race : MOO1GameOptions.allRaceOptions()) {
				Race r = Race.keyed(race);
				String name = r.setupName();
				put(PMutil.suggestedUserViewFromCodeView(race), name, 0f, race);
			}
			defaultIndex(0);
			initOptionsText();
		}
		@Override public void updateGui() {
			if (updateAllowed) {
				String raceKey = settingValue();
				setRace(raceKey);
				pullSettings();
				super.updateGui();
				updateAllowed = false;
				pullSettings();
				updateSettings();
				updateAllowed = true;
			}
		}
		@Override public void pushSetting() {
			setRace(settingValue());
		}
		@Override public void pullSetting() {
			if (!pullAllowed)
				return;
			if(!costInitialized) {
				String raceKey = race.name();
				pullAllowed = false;
				for (int i=0; i<boxSize(); i++) {
					index(i);
					race = Race.keyed(settingValue()).copy();
					pullSettings();
					newCost(i, getTotalCost());
				}
				set(raceKey);
				race = Race.keyed(settingValue()).copy();
				pullSettings();
				pullAllowed = true;
				costInitialized = true;
			}
			set(race.name());
		}
	}
	// ==================== CreditsBonus ====================
	//
	public class CreditsBonus extends SettingInteger {
		// big = good
		public CreditsBonus() {
			super(ROOT, "CREDIT", 0, 0, 35, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .8f}, new float[]{0f, .8f});
		}
		@Override public void pushSetting() {
			race.bCBonus((float) settingValue()/100);
		}
		@Override public void pullSetting() {
			set(Math.round(race.bCBonus() * 100));
		}
	}
	// ==================== HitPointsBonus ====================
	//
	public class HitPointsBonus extends SettingInteger {
		// big = good
		public HitPointsBonus() {
			super(ROOT, "HIT_POINTS", 100, 50, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .4f}, new float[]{0f, .6f});
		}
		@Override public void pushSetting() {
			race.hPFactor((float) settingValue()/100);
		}
		@Override public void pullSetting() {
			set(Math.round(race.hPFactor() * 100));
		}
	}
	// ==================== ShipSpaceBonus ====================
	//
	// Absolute min = ? .75 not OK for colony building!
	public class ShipSpaceBonus extends SettingInteger {
		// big = good
		public ShipSpaceBonus() {
			super(ROOT, "SHIP_SPACE", 100, 80, 175, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 1f}, new float[]{0f, 2f});
		}
		@Override public void pushSetting() {
			race.shipSpaceFactor((float) settingValue()/100);
		}
		@Override public void pullSetting() {
			set(Math.round(race.shipSpaceFactor() * 100));
		}
	}
	// ==================== MaintenanceBonus ====================
	//
	public class MaintenanceBonus extends SettingInteger {
		// Big = bad
		public MaintenanceBonus() {
			super(ROOT, "MAINTENANCE", 100, 50, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, -.2f}, new float[]{0f, -.4f});
		}
		@Override public void pushSetting() {
			race.maintenanceFactor((float) settingValue()/100);
		}
		@Override public void pullSetting() {
			set(Math.round(race.maintenanceFactor() * 100));
		}
	}
	// ==================== PlanetRessources ====================
	//
	public class PlanetRessources extends SettingBase<String> {
		private static final String defaultValue = "Normal";
		
		public PlanetRessources() {
			super(ROOT, "HOME_RESOURCES");
			isBullet(true);
			saveAllowed(false);
			labelsAreFinals(true);
			put("UltraPoor",	PLANET + "ULTRA_POOR",		-50f, "UltraPoor");
			put("Poor",			PLANET + "POOR",			-25f, "Poor");
			put("Normal",		ROOT   + "RESOURCES_NORMAL",  0f, "Normal");
			put("Rich",			PLANET + "RICH",			 30f, "Rich");
			put("UltraRich",	PLANET + "ULTRA_RICH",		 50f, "UltraRich");
			put("Artifacts",	PLANET + "ARTIFACTS",		 40f, "Artifacts");
			defaultCfgValue(defaultValue);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.planetRessource(settingValue());
		}
		@Override public void pullSetting() {
			set(race.planetRessource());
		}
	}
	// ==================== PlanetEnvironment ====================
	//
	public class PlanetEnvironment extends SettingBase<String> {
		private static final String defaultValue = "Normal";
		
		public PlanetEnvironment() {
			super(ROOT, "HOME_ENVIRONMENT");
			isBullet(true);
			saveAllowed(false);
			labelsAreFinals(true);
			put("Hostile", PLANET + "HOSTILE",			  -20f, "Hostile");
			put("Normal",  ROOT   + "ENVIRONMENT_NORMAL",	0f, "Normal");
			put("Fertile", PLANET + "FERTILE",			   15f, "Fertile");
			put("Gaia",	   PLANET + "GAIA",				   30f, "Gaia");
			defaultCfgValue(defaultValue);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.planetEnvironment(settingValue());
		}
		@Override public void pullSetting() {
			set(race.planetEnvironment());
		}
	}
	// ==================== PlanetType ====================
	//
	public class RacePlanetType extends SettingBase<String> {
		private static final String defaultValue = "Terran";
		
		public RacePlanetType() {
			super(ROOT, "HOME_TYPE");
			isBullet(true);
			saveAllowed(false);
			labelsAreFinals(true);
//			put("Ocean",	PlanetType.OCEAN,	-8f, PlanetType.OCEAN);
//			put("Jungle",	PlanetType.JUNGLE,	-5f, PlanetType.JUNGLE);
//			put("Terran",	PlanetType.TERRAN,	 0f, PlanetType.TERRAN);
			put("Ocean",	PlanetType.OCEAN,	0f, PlanetType.OCEAN);
			put("Jungle",	PlanetType.JUNGLE,	0f, PlanetType.JUNGLE);
			put("Terran",	PlanetType.TERRAN,	0f, PlanetType.TERRAN);
			defaultCfgValue(defaultValue);
			initOptionsText();
			hasNoCost(true); // to be removed
		}
		@Override public void pushSetting() {
			race.homeworldPlanetType = settingValue();
		}
		@Override public void pullSetting() {
			set(race.homeworldPlanetType);
		}
	}
	// ==================== HomeworldSize ====================
	//
	public class HomeworldSize extends SettingInteger {
		
		public HomeworldSize() {
			super(ROOT, "HOME_SIZE", 100, 70, 150, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .4f}, new float[]{0f, .7f});
		}
		@Override public void pushSetting() {
			race.homeworldSize = settingValue();
		}
		@Override public void pullSetting() {
			set(race.homeworldSize);
		}
	}
	// ==================== SpeciesType ====================
	//
	public class SpeciesType extends SettingBase<Integer> {
		private static final String defaultValue = "Terran";
		
		public SpeciesType() {
			super(ROOT, "RACE_TYPE");
			isBullet(true);
			saveAllowed(false);
			labelsAreFinals(true);
			put("Terran",	"RACE_TERRAN",   0f, 1);
			put("Aquatic",	"RACE_AQUATIC",  2f, 2);
			put("Silicate",	"RACE_SILICATE", 4f, 3);
			put("Robotic",	"RACE_ROBOTIC",	 4f, 4);
			defaultCfgValue(defaultValue);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.speciesType = settingValue();
		}
		@Override public void pullSetting() {
			set(race.speciesType);
		}
	}
	// ==================== IgnoreEco ====================
	//
	public class IgnoresEco extends SettingBoolean {
		private static final boolean defaultValue = false;
		
		public IgnoresEco() {
			super(ROOT, "IGNORES_ECO", defaultValue, 50f, 0f);
			isBullet(booleansAreBullet);
			saveAllowed(false);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.ignoresPlanetEnvironment = settingValue();
		}
		@Override public void pullSetting() {
			set(race.ignoresPlanetEnvironment);
		}
	}
	// ==================== PopGrowRate ====================
	//
	public class PopGrowRate extends SettingInteger {
		
		public PopGrowRate() {
			super(ROOT, "POP_GROW_RATE", 100, 50, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .2f, .003f}, new float[]{0f, .3f});
		}
		@Override public void pushSetting() {
			race.growthRateMod = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round (race.growthRateMod * 100));
		}
	}
	// ==================== ShipAttack ====================
	//
	public class ShipAttack extends SettingInteger {
		
		public ShipAttack() {
			super(ROOT, "SHIP_ATTACK", 0, -1, 5, 1, 1, 1, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 3f}, new float[]{0f, 5f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.shipAttackBonus(settingValue());
		}
		@Override public void pullSetting() {
			set(race.shipAttackBonus());
		}
	}
	// ==================== ShipDefense ====================
	//
	public class ShipDefense extends SettingInteger {
		
		public ShipDefense() {
			super(ROOT, "SHIP_DEFENSE", 0, -1, 5, 1, 1, 1, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 1.5f, 1.5f}, new float[]{0f, 6f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.shipDefenseBonus(settingValue());
		}
		@Override public void pullSetting() {
			set(race.shipDefenseBonus());
		}
	}
	// ==================== ShipInitiative ====================
	//
	public class ShipInitiative extends SettingInteger {
		
		public ShipInitiative() {
			super(ROOT, "SHIP_INITIATIVE", 0, -1, 5, 1, 1, 1, saveNotAllowed,
					DIFFERENCE, new float[]{5f, 1f}, new float[]{0f, 6f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.shipInitiativeBonus(settingValue());
		}
		@Override public void pullSetting() {
			set(race.shipInitiativeBonus());
		}
	}
	// ==================== GroundAttack ====================
	//
	public class GroundAttack extends SettingInteger {
		
		public GroundAttack() {
			super(ROOT, "GROUND_ATTACK", 0, -20, 30, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 1.25f}, new float[]{0f, 0.75f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.groundAttackBonus(settingValue());
		}
		@Override public void pullSetting() {
			set(race.groundAttackBonus());
		}
	}
	// ==================== SpyCost ====================
	//
	public class SpyCost extends SettingInteger {
		
		public SpyCost() {
			super(ROOT, "SPY_COST", 100, 50, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, -.1f}, new float[]{0f, -.2f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.spyCostMod = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.spyCostMod * 100));
		}
	}
	// ==================== SpySecurity ====================
	//
	public class SpySecurity extends SettingInteger {
		
		public SpySecurity() {
			super(ROOT, "SPY_SECURITY", 0, -20, 40, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 1f}, new float[]{0f, 2f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.internalSecurityAdj = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.internalSecurityAdj * 100));
		}
	}
	// ==================== SpyInfiltration ====================
	//
	public class SpyInfiltration extends SettingInteger {
		
		public SpyInfiltration() {
			super(ROOT, "SPY_INFILTRATION", 0, -20, 40, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 1.25f}, new float[]{0f, 2.5f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.spyInfiltrationAdj = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.spyInfiltrationAdj * 100));
		}
	}
	// ==================== SpyTelepathy ====================
	//
	public class SpyTelepathy extends SettingBoolean {
		private static final boolean defaultValue = false;
		
		public SpyTelepathy() {
			super(ROOT, "SPY_TELEPATHY", defaultValue, 20f, 0f);
			isBullet(booleansAreBullet);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.telepathic = settingValue();
		}
		@Override public void pullSetting() {
			set(race.telepathic);
		}
	}
	// ==================== DiplomacyTrade ====================
	//
	public class DiplomacyTrade extends SettingInteger {
		
		public DiplomacyTrade() {
			super(ROOT, "DIPLOMACY_TRADE", 0, -30, 30, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .4f}, new float[]{0f, .3f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.tradePctBonus = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.tradePctBonus * 100));
		}
	}
	// ==================== DiploPosDP ====================
	//
	public class DiploPosDP extends SettingInteger {
		
		public DiploPosDP() {
			super(ROOT, "DIPLO_POS_DP", 100, 70, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .3f}, new float[]{0f, .8f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.positiveDPMod = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.positiveDPMod * 100));
		}
	}
	// ==================== DiplomacyBonus ====================
	//
	public class DiplomacyBonus extends SettingInteger {
		
		public DiplomacyBonus() {
			super(ROOT, "DIPLOMACY_BONUS", 0, -50, 100, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .1f}, new float[]{0f, .2f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.diplomacyBonus = settingValue();
		}
		@Override public void pullSetting() {
			set(race.diplomacyBonus);
		}
	}
	// ==================== DiplomacyCouncil ====================
	//
	public class DiplomacyCouncil extends SettingInteger {
		
		public DiplomacyCouncil() {
			super(ROOT, "DIPLOMACY_COUNCIL", 0, -25, 25, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .2f}, new float[]{0f, .2f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.councilBonus = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.councilBonus * 100));
		}
	}
	// ==================== RelationDefault ====================
	//
	public class RelationDefault extends SettingInteger {
		
		public RelationDefault() {
			super(ROOT, "RELATION_DEFAULT", 0, -10, 10, 1, 2, 4, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .4f}, new float[]{0f, .4f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.defaultRaceRelations(settingValue());
		}
		@Override public void pullSetting() {
			set((int)race.defaultRaceRelations());
		}
	}
	// ==================== ProdWorker ====================
	//
	public class ProdWorker extends SettingInteger {
		// bigger = better
		public ProdWorker() {
			super(ROOT, "PROD_WORKER", 100, 70, 200, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .3f, 0.003f}, new float[]{0f, 0.8f, 0.006f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.workerProductivityMod = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.workerProductivityMod * 100));
		}
	}
	// ==================== ProdControl ====================
	//
	public class ProdControl extends SettingInteger {
		
		public ProdControl() {
			super(ROOT, "PROD_CONTROL", 0, -1, 4, 1, 1, 1, saveNotAllowed,
					DIFFERENCE, new float[]{0f, 15f}, new float[]{0f, 30f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.robotControlsAdj = settingValue();
		}
		@Override public void pullSetting() {
			set(race.robotControlsAdj);
		}
	}
	// ==================== IgnoresFactoryRefit ====================
	//
	public class IgnoresFactoryRefit extends SettingBoolean {
		private static final boolean defaultValue = false;
		
		public IgnoresFactoryRefit() {
			super(ROOT, "PROD_REFIT_COST", defaultValue, 20f, 0f);
			isBullet(booleansAreBullet);
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.ignoresFactoryRefit = settingValue();
		}
		@Override public void pullSetting() {
			set(race.ignoresFactoryRefit());
		}
	}
	// ==================== TechDiscovery ====================
	//
	public class TechDiscovery extends SettingInteger {
		// bigger = better
		public TechDiscovery() {
			super(ROOT, "TECH_DISCOVERY", 50, 30, 100, 1, 5, 20, saveNotAllowed,
					DIFFERENCE, new float[]{0f, .5f}, new float[]{0f, 0.5f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techDiscoveryPct = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techDiscoveryPct * 100));
		}
	}
	// ==================== TechResearch ====================
	//
	public class TechResearch extends SettingInteger {
		// bigger = better
		public TechResearch() {
			super(ROOT, "TECH_RESEARCH", 100, 60, 200, 1, 5, 20, saveNotAllowed, DIFFERENCE,
					new float[]{0f, 0.7f, 0.004f},
					new float[]{0f, 1.0f, 0.006f});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.researchBonusPct = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.researchBonusPct * 100));
		}
	}
	// ==================== ResearchComputer ====================
	//
	private static final int studyCostMin = 50;
	private static final int studyCostMax = 200;
	private static final float researchC1pos = -.0f;
	private static final float researchC1neg = -.0f;
	private static final float researchC2pos = -.0012f;
	private static final float researchC2neg = -.005f;

	public class ResearchComputer extends SettingInteger {
		// smaller = better
		public ResearchComputer() {
			super(ROOT, "RESEARCH_COMPUTER", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[0] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[0] * 100));
		}
	}
	// ==================== ResearchConstruction ====================
	//
	public class ResearchConstruction extends SettingInteger {
		
		public ResearchConstruction() {
			super(ROOT, "RESEARCH_CONSTRUCTION", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[1] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[1] * 100));
		}
	}
	// ==================== ResearchForceField ====================
	//
	public class ResearchForceField extends SettingInteger {
		
		public ResearchForceField() {
			super(ROOT, "RESEARCH_FORCEFIELD", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[2] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[2] * 100));
		}
	}
	// ==================== ResearchPlanet ====================
	//
	public class ResearchPlanet extends SettingInteger {
		
		public ResearchPlanet() {
			super(ROOT, "RESEARCH_PLANET", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[3] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[3] * 100));
		}
	}
	// ==================== ResearchPropulsion ====================
	//
	public class ResearchPropulsion extends SettingInteger {
		
		public ResearchPropulsion() {
			super(ROOT, "RESEARCH_PROPULSION", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[4] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[4] * 100));
		}
	}
	// ==================== ResearchWeapon ====================
	//
	public class ResearchWeapon extends SettingInteger {
		
		public ResearchWeapon() {
			super(ROOT, "RESEARCH_WEAPON", 100, studyCostMin, studyCostMax,
					1, 5, 20, saveNotAllowed, DIFFERENCE, 
					new float[]{0f, researchC1pos, researchC2pos},
					new float[]{0f, researchC1neg, researchC2neg});
			initOptionsText();
		}
		@Override public void pushSetting() {
			race.techMod[5] = (float) settingValue()/100;
		}
		@Override public void pullSetting() {
			set(Math.round(race.techMod[5] * 100));
		}
	}
}
