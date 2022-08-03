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

package rotp.ui.util;

import static rotp.ui.UserPreferences.MOD_UI;

public class ParamTech extends ParamAAN2 {
	
	public final int techCategory;
	public final String techType;
	public final int techSeqNum;

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param TechCategory The category index from
	 * { "TECH_COMPUTERS", "TECH_CONSTRUCTION", "TECH_FORCE_FIELDS",
	 * "TECH_PLANETOLOGY", "TECH_PROPULSION", "TECH_WEAPONS" }
	 * @param techType   From the third column of rotp/data/techs.txt
	 * @param techType   From the fourth column of rotp/data/techs.txt
	 * @param defaultValue The default value
	 */
	public ParamTech(String gui, String name, int techCategory,
			String techType, int techSeqNum, String defaultValue) {
		super(gui, name, defaultValue);
		this.techCategory = techCategory;
		this.techType	  = techType;
		this.techSeqNum   = techSeqNum;
	}
	/**
	 * GUI = MOD_UI
	 * @param name The name
	 * @param TechCategory The category index from
	 * { "TECH_COMPUTERS", "TECH_CONSTRUCTION", "TECH_FORCE_FIELDS",
	 * "TECH_PLANETOLOGY", "TECH_PROPULSION", "TECH_WEAPONS" }
	 * @param techType   From the third column of rotp/data/techs.txt
	 * @param techType   From the fourth column of rotp/data/techs.txt
	 * @param defaultValue The default value
	 */
	public ParamTech(String name, int techCategory,
			String techType, int techSeqNum, String defaultValue) {
		this(MOD_UI, name, techCategory, techType, techSeqNum, defaultValue);
	}
	/**
	 * GUI = MOD_UI, defaultValue = Auto
	 * @param name The name
	 * @param TechCategory The category index from
	 * { "TECH_COMPUTERS", "TECH_CONSTRUCTION", "TECH_FORCE_FIELDS",
	 * "TECH_PLANETOLOGY", "TECH_PROPULSION", "TECH_WEAPONS" }
	 * @param techType   From the third column of rotp/data/techs.txt
	 * @param techType   From the fourth column of rotp/data/techs.txt
	 */
	public ParamTech(String name, int techCategory,
			String techType, int techSeqNum) {
		this(MOD_UI, name, techCategory, techType, techSeqNum, AUTO);
	}
	// ===== Specific Public Methods =====
	//
	public String techId() {
		return techType + ":" + techSeqNum;
	}
	public boolean isAlways(int techCategory, int techSeqNum, boolean isPlayer) {
		return (techCategory == this.techCategory
				&& techSeqNum == this.techSeqNum)
				&& isAlways(isPlayer) ;
	}
	public boolean isNever(String techId, boolean isPlayer) {
		return techId.equalsIgnoreCase(techId()) 
				&& isNever(isPlayer); 
	}
}
