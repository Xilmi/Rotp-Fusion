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

import static rotp.model.game.IBaseOptsTools.*;


import java.util.Arrays;
import java.util.List;

public class ParamFlagColor extends ParamList {
	
	public static final String FLAG_COLOR_NONE   = "FLAG_COLOR_NONE";
    public static final String FLAG_COLOR_WHITE  = "FLAG_COLOR_WHITE";
    public static final String FLAG_COLOR_RED    = "FLAG_COLOR_RED";
    public static final String FLAG_COLOR_BLUE   = "FLAG_COLOR_BLUE";
    public static final String FLAG_COLOR_GREEN  = "FLAG_COLOR_GREEN";
    public static final String FLAG_COLOR_DKGREEN= "FLAG_COLOR_DKGREEN";
    public static final String FLAG_COLOR_YELLOW = "FLAG_COLOR_YELLOW";
    public static final String FLAG_COLOR_AQUA   = "FLAG_COLOR_AQUA";
    public static final String FLAG_COLOR_ORANGE = "FLAG_COLOR_ORANGE";
    public static final String FLAG_COLOR_LTBLUE = "FLAG_COLOR_LTBLUE";
    public static final String FLAG_COLOR_PURPLE = "FLAG_COLOR_PURPLE";
    public static final String FLAG_COLOR_PINK   = "FLAG_COLOR_PINK";
    public static final int flagCount;
	
    private static final IndexableMap flagColorMap = new IndexableMap();
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
			    FLAG_COLOR_PINK,
			    FLAG_COLOR_DKGREEN
				);
		flagCount = flagColorList.size();
		for (String element : flagColorList)
			flagColorMap.put(element, element); // Temporary; needs to be further initialized
	}

	// ===== Constructors =====
	//
	/**
	 * GUI = MOD_UI
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamFlagColor(String name, String defaultValue) {
		super(MOD_UI, name, defaultValue, flagColorMap);
		showFullGuide(false);
	}
}
