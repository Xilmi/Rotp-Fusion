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

public class OpponentCROptions extends ParamList {
	
//	public static final String BASE_RACE	= "Base Race";
//	public static final String SELECTION	= "Selection";
//	public static final String REWORKED		= "Reworked";
//	public static final String PLAYER		= "Player";
//	public static final String RANDOM		= "Random";
//	public static final String FILES_FLT	= "Files";
//	public static final String FILES_NO_FLT	= "Files No Filter";
//	public static final String FILES_RACES	= "Files Races";
//	public static final String ALL			= "All";

	/**
	 * @param gui  The label header
	 * @param name The name
	 * @param defaultValue The default value
	 */
	public OpponentCROptions(String gui, String name, String defaultValue) {
		super(gui, name, defaultValue);
		for (SpecificCROption opt : SpecificCROption.values()) {
			if(!opt.isSelection() && !opt.isUserChoice())
				put(opt.value, gui + name + "_" + opt.name());
		}
//		put(BASE_RACE,		gui + name + "_BASE_RACE");
//		put(SELECTION,		gui + name + "_SELECTION");
//		put(REWORKED,		gui + name + "_REWORKED");
//		put(PLAYER,			gui + name + "_PLAYER");
//		put(RANDOM,			gui + name + "_RANDOM");
//		put(FILES_FLT,		gui + name + "_FILES_FLT");
//		put(FILES_NO_FLT,	gui + name + "_FILES_NO_FLT");
//		put(FILES_RACES,	gui + name + "_FILES_RACES");
//		put(ALL,	 		gui + name + "_ALL");
	}
	public SpecificCROption getEnu() {
		return SpecificCROption.set(get());
	}

	public boolean isBaseRace() {
		return SpecificCROption.isBaseRace(get());
	}
	public boolean isSelection() {
		return SpecificCROption.isSelection(get());
	}
	public boolean isReworked() {
		return SpecificCROption.isReworked(get());
	}
	public boolean isPlayer() {
		return SpecificCROption.isPlayer(get());
	}
	public boolean isRandom() {
		return SpecificCROption.isRandom(get());
	}
	public boolean isFilteredFiles() {
		return SpecificCROption.isFilteredFiles(get());
	}
	public boolean isAllFiles() {
		return SpecificCROption.isAllFiles(get());
	}
	public boolean isFilesAndRaces() {
		return SpecificCROption.isFilesAndRaces(get());
	}
	public boolean isAll() {
		return SpecificCROption.isAll(get());
	}
}
