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

import rotp.model.empires.Race;
import rotp.util.LanguageManager;
import rotp.util.sound.SoundManager;

public class ParamSpeciesName extends ParamString {
	
	boolean isValidValue = false;
	
	// ===== Constructors =====
	//
	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public ParamSpeciesName(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue);
		isCfgFile(true);
	}
	// ===== Public Methods =====
	//
	public String[] getValid()		{
		String[] strArray = getArray();
		if (strArray.length != 3)
			return null;
		for (int i=0; i<3; i++) {
			String s = strArray[i].trim();
			if (s.isEmpty())
				return null;
			strArray[i] = s;
		}
		return strArray;
	}
	public boolean newName()		{ return getValid() != null; }
	public String getEmpire()		{
		String[] strArray = getValid();
		if (strArray == null)
			return null;
		return strArray[0];
	}
	public String getRace()			{
		String[] strArray = getValid();
		if (strArray == null)
			return null;
		return strArray[1];
	}
	public String getRacePlural()	{
		String[] strArray = getValid();
		if (strArray == null)
			return null;
		return strArray[2];
	}
	// ===== Overriders =====
	//
	@Override public boolean isValidValue()	{ return isValidValue; }

	@Override public String	set(String val)	{
		super.set(val);
		String[] strArray = getArray();
		if (strArray.length == 0 || newName(strArray)) {
			Race race = Race.keyed(getCfgLabel());
			LanguageManager.current().reloadRace(race);
			isValidValue = true;
		}
		else {
			isValidValue = false;
			misClick();
		}
		return val;
	}
	// ===== Private Methods =====
	//
	private void misClick()		{ SoundManager.current().playAudioClip("MisClick"); }
	private String[] getArray()	{ return get().split(","); }
	private boolean newName(String[] strArray)	{
		if (strArray.length != 3)
			return false;
		for (int i=0; i<3; i++)
			if (strArray[i].trim().isEmpty())
				return false;
		return true;
	}

}
