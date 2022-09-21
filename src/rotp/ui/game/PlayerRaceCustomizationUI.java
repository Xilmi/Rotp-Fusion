/*
 * Copyright 2015-2020 Ray Fowler
 * 
 * Licensed under the GNU General Public License, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 https://www.gnu.org/licenses/gpl-3.0.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rotp.ui.game;

import static rotp.model.empires.CustomRaceFactory.ROOT;

import rotp.ui.util.AbstractCRUI;

public class PlayerRaceCustomizationUI extends AbstractCRUI {
	private static final long serialVersionUID = 1L;
	private static final PlayerRaceCustomizationUI playerInstance  = new PlayerRaceCustomizationUI();
	private static final PlayerRaceCustomizationUI displayInstance = new PlayerRaceCustomizationUI();
	public  static final String guiTitleID = ROOT + "GUI_TITLE";
	
	public PlayerRaceCustomizationUI() {
		super(guiTitleID);
	}
	public static PlayerRaceCustomizationUI playerInstance()  { return playerInstance; }
	public static PlayerRaceCustomizationUI displayInstance() { return displayInstance; }
	
	@Override protected void init0() {
		// First column (left)
		newSetting(cr.new BaseDataRace());
		endOfColumn(); // ====================

		// Second column
		newSetting(cr.new RacePlanetType());
		newSetting(cr.new HomeworldSize());
//		newSetting(cr.new SpeciesType()); // Not used in Game
		newSetting(cr.new PopGrowRate());
		newSetting(cr.new IgnoresEco());
		newSetting(cr.new Spacer()); // ----------
		newSetting(cr.new ShipAttack());
		newSetting(cr.new ShipDefense());
		newSetting(cr.new ShipInitiative());
		newSetting(cr.new GroundAttack());
		newSetting(cr.new Spacer()); // ----------
		newSetting(cr.new SpyCost());
		newSetting(cr.new SpySecurity());
		newSetting(cr.new SpyInfiltration());
//		newSetting(cr.new SpyTelepathy()); // Not used in Game
		newSetting(cr.new Spacer()); // ----------
		newSetting(cr.new DiplomacyTrade());
// 		newSetting(cr.new DiploPosDP()); // Not used in Game
		newSetting(cr.new DiplomacyBonus());
		newSetting(cr.new DiplomacyCouncil());
		newSetting(cr.new RelationDefault());	// BR: Maybe All the races
		endOfColumn(); // ====================

		// Third column
		newSetting(cr.new ProdWorker());
		newSetting(cr.new ProdControl());
		newSetting(cr.new IgnoresFactoryRefit());
		newSetting(cr.new TechDiscovery());
		newSetting(cr.new TechResearch());
		newSetting(cr.new Spacer()); // ----------
		newSetting(cr.new ResearchComputer());
		newSetting(cr.new ResearchConstruction());
		newSetting(cr.new ResearchForceField());
		newSetting(cr.new ResearchPlanet());
		newSetting(cr.new ResearchPropulsion());
		newSetting(cr.new ResearchWeapon());
		newSetting(cr.new Spacer()); // ----------
		
		endOfColumn(); // ====================
		// Fourth column
		newSetting(cr.new PlanetRessources());
		newSetting(cr.new PlanetEnvironment());
		newSetting(cr.new CreditsBonus());
		newSetting(cr.new HitPointsBonus());
		newSetting(cr.new MaintenanceBonus());
		newSetting(cr.new ShipSpaceBonus());
		endOfColumn(); // ====================
		// Fifth column
		// endOfColumn(); // ====================

	}
}
