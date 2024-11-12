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

package rotp.ui.util;

import static rotp.model.game.IRaceOptions.playerCustomRace;
import static rotp.model.game.IRaceOptions.playerIsCustom;
import static rotp.ui.util.IParam.langLabel;

import rotp.model.empires.Race;
import rotp.model.ships.ShipLibrary;
import rotp.ui.RotPUI;

public class PlayerShipSet extends ParamList {
	
	private static final String FORCED_SHIPSET	= "_FORCED_SHIPSET";
	private static final String CUSTOM_SPECIES	= "_CUSTOM_SPECIES";
	public static final String ORIGINAL			= "Original";
	public static final String DISPLAY_RACE_SET	= "Displayed Race";

	/**
	 * @param gui  The label header
	 * @param name The name
	 */
	public PlayerShipSet(String gui, String name) {
		super(gui, name, "Original");
		refreshLevel(1);
		for (String s : ShipLibrary.current().styles) {
			put(s, s);
		}
//		put(DISPLAY_RACE_SET, DISPLAY_RACE_SET.toUpperCase());
//		put(ORIGINAL, ORIGINAL.toUpperCase());
//		put(DISPLAY_RACE_SET, DISPLAY_RACE_SET);
		put(ORIGINAL, ORIGINAL);
	}
	// ========== Public Getters ==========
	//
	/**
	 * @return Original Status
	 */
	public boolean isOriginal()	  { return get().equalsIgnoreCase(ORIGINAL); }
	public boolean isDisplaySet() { return preferredShipSet().equalsIgnoreCase(DISPLAY_RACE_SET); }
	/**
	 * @return ShipSet Text to display translating Original option
	 */
	public String displaySet() {
		if (playerIsCustom.get() && isOriginal()) {
			String preferredShipSet = preferredShipSet();
		   	if (preferredShipSet.equalsIgnoreCase(DISPLAY_RACE_SET))
		   		return get();
		   	else {
    			String key = getLangLabel() + CUSTOM_SPECIES;
    			String str = langLabel(key, preferredShipSet);
    			return str;
		   	}
	    }
    	else if (isOriginal()) {
			String key = getLangLabel() + "_" + ORIGINAL.toUpperCase();
			String str = langLabel(key);
			return str;
		}
		else {
			String key = getLangLabel() + FORCED_SHIPSET;
			String str = langLabel(key, get());
			return str;
		}
	}
	/**
	 * @return ShipSet index translating Original option
	 */
	public int realShipSetId() {
		int index;
		Race r =  Race.keyed(RotPUI.newOptions().selectedPlayerRace());
		if (playerIsCustom.get() && isOriginal()) {
		   	String preferredShipSet = preferredShipSet();
		   	if (preferredShipSet.equalsIgnoreCase(DISPLAY_RACE_SET))
		   		index = getIndex(r.preferredShipSet);
		   	else
		   		index = getIndex(preferredShipSet);
    	}
		// Standard process
    	else if (isOriginal() || isDisplaySet())
			index = getIndex(r.preferredShipSet);
		else 
			index = getIndex();
		
		if (index == -1) index = 0;
		return index;
	}
	private String preferredShipSet() { return playerCustomRace.getRace().preferredShipSet; }
}
