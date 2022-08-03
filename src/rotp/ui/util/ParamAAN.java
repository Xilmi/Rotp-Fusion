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

public class ParamAAN extends ParamList {
	
	public static final String NEVER	= "Never";
	public static final String AUTO		= "Auto";
	public static final String ALWAYS	= "Always";
	
	private static final LinkedHashMap<String, String> labelsAlwaysAutoNever =
							new LinkedHashMap<String, String>();
	static {
		labelsAlwaysAutoNever.put(NEVER,	MOD_UI + "OPTION_NEVER");
		labelsAlwaysAutoNever.put(AUTO,		MOD_UI + "OPTION_AUTO");
		labelsAlwaysAutoNever.put(ALWAYS,	MOD_UI + "OPTION_ALWAYS");
	}

	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamAAN(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue, labelsAlwaysAutoNever);
	}
	/**
	 * GUI = MOD_UI
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamAAN(String name, String defaultValue) {
		this(MOD_UI, name, defaultValue);
	}
	/**
	 * GUI = MOD_UI, defaultValue = Auto
	 * @param name The name
	 * @param TechCategory The category index from
	 */
	public ParamAAN(String name) {
		this(MOD_UI, name, AUTO);
	}
	// ===== Specific Public Methods =====
	//
	public boolean isAlways() {
		return value.equalsIgnoreCase(ALWAYS);
	}
	public boolean isNever() {
		return value.equalsIgnoreCase(NEVER);

	}
	public boolean isAuto() {
		return value.equalsIgnoreCase(AUTO);
	}
}
