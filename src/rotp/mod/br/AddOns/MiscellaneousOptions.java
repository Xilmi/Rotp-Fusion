
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

package rotp.mod.br.AddOns;

import mod.br.AddOns.Miscellaneous;

/**
 * @author BrokenRegistry
 * Some little Add-ons
 */
public class MiscellaneousOptions {
	
	/**
	 * @param currentColor the current flag color index
	 * @param reverse direction
	 * @return the new flag color index
	 */
	public static int getNextFlagColor(int currentColor, boolean reverse) {
		 return Miscellaneous.getNextFlagColor(currentColor, reverse);
	}
}
