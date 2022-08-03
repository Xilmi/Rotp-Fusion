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

import java.util.LinkedHashMap;

public class ParamAAN2 extends ParamList {
	
	public static final String HUMBLE	= "Never Always";
	public static final String RIVALS	= "Never Auto";
	public static final String NEVER	= "Never Never";
	public static final String MAYBE	= "Auto Never";
	public static final String AUTO		= "Auto Auto";
	public static final String RISKY	= "Auto Always";
	public static final String ALWAYS	= "Always Always";
	public static final String PLAYER	= "Always Auto";
	public static final String SELFISH  = "Always Never";
	
	private static final LinkedHashMap<String, String> labelsAlwaysAutoNever =
							new LinkedHashMap<String, String>();
	static {
		labelsAlwaysAutoNever.put(HUMBLE,	MOD_UI + "OPTION_NEVER_ALWAYS");
		labelsAlwaysAutoNever.put(RIVALS,	MOD_UI + "OPTION_NEVER_AUTO");
		labelsAlwaysAutoNever.put(NEVER,	MOD_UI + "OPTION_NEVER_NEVER");
		labelsAlwaysAutoNever.put(MAYBE,	MOD_UI + "OPTION_AUTO_NEVER");
		labelsAlwaysAutoNever.put(AUTO,		MOD_UI + "OPTION_AUTO_AUTO");
		labelsAlwaysAutoNever.put(RISKY,	MOD_UI + "OPTION_AUTO_ALWAYS");
		labelsAlwaysAutoNever.put(ALWAYS,	MOD_UI + "OPTION_ALWAYS_ALWAYS");
		labelsAlwaysAutoNever.put(PLAYER,	MOD_UI + "OPTION_ALWAYS_AUTO");
		labelsAlwaysAutoNever.put(SELFISH,	MOD_UI + "OPTION_ALWAYS_NEVER");
	}

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamAAN2(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue, labelsAlwaysAutoNever);
	}
	/**
	 * GUI = MOD_UI
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamAAN2(String name, String defaultValue) {
		this(MOD_UI, name, defaultValue);
	}
	/**
	 * GUI = MOD_UI, defaultValue = Auto
	 * @param name The name
	 * @param TechCategory The category index from
	 */
	public ParamAAN2(String name) {
		this(MOD_UI, name, AUTO);
	}
	// ===== Specific Public Methods =====
	//
	public boolean isAlways(boolean isPlayer) {
		if (isPlayer) 
			switch(value) {
				case ALWAYS: case PLAYER: case SELFISH:
					return true;
				default:
					return false;
			}
		else switch(value) {
				case HUMBLE: case RISKY: case ALWAYS:
					return true;
				default:
					return false;
			}
	}
	public boolean isNever(boolean isPlayer) {
		if (isPlayer) 
			switch(value) {
				case HUMBLE: case RIVALS: case NEVER:
					return true;
				default:
					return false;
			}
		else switch(value) {
				case NEVER: case MAYBE: case SELFISH:
					return true;
				default:
					return false;
			}
	}
	public boolean isAuto(boolean isPlayer) {
		if (isPlayer) 
			switch(value) {
				case MAYBE: case AUTO: case RISKY:
					return true;
				default:
					return false;
			}
		else switch(value) {
				case RIVALS: case AUTO: case PLAYER:
					return true;
				default:
					return false;
			}
	}
}
