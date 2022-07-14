
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

package mod.br.AddOns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.profileManager.src.main.java.AbstractT;
import br.profileManager.src.main.java.T_Integer;
import rotp.model.empires.SystemView;

/**
 * @author BrokenRegistry
 * Control several little add-ons
 */
public class Miscellaneous {

	private static List<Integer> selectedFlagOrder;
	private static List<String>  selectedFlagColor;
	private static T_Integer     selectedFlagColorOrder;
	private static Integer       selectedSize;
	private static List<Integer> converter;
	private static List<String>  defaultFlagColor;
	private static T_Integer     defaultFlagColorOrder = new T_Integer();

		static {
			defaultFlagColorOrder.setUserViewOnly(SystemView.getFlagList());
			List<Integer> codeView = new ArrayList<Integer>();
			int size = defaultFlagColorOrder.getUserList().size();
			for(int i=0; i<size; i++) {
				codeView.add(i);
			}
			defaultFlagColorOrder.setCodeViewOnly(codeView);
			defaultFlagColor = defaultFlagColorOrder.getUserList();
			selectedFlagColorOrder(defaultFlagColorOrder);
		}
	
		static String asString(List<Integer> value) {
			if (value == null) {
				return "Null";
			}
			Integer[] array = value.toArray(new Integer[0]);
			return asString(array);
		}
		static String asString(Integer[] probabilityDensity) {
			if (probabilityDensity == null) {
				return "Null";
			}
			return Arrays.toString(probabilityDensity);
		}
	// ========== Setters ==========
	//
	/**
	 * @param colorOrder the new flag color order to set
	 */
	public static void selectedFlagColorOrder(AbstractT<Integer> colorOrder) {
		selectedFlagColorOrder = (T_Integer) colorOrder;
		selectedFlagOrder = selectedFlagColorOrder.getCodeList();
		selectedFlagColor = selectedFlagColorOrder.getUserList();
		selectedSize      = selectedFlagOrder.size();

		String color;
		int selectedIndex;
		converter = new ArrayList<Integer>();
		for (int i=0; i<selectedSize; i++) {
			color = selectedFlagColor.get(i);
			selectedIndex = defaultFlagColor.indexOf(color);
			converter.add(selectedIndex);
		}
	}

	// ========== Getters ==========
	//
	/**
	 * @return the default flag color order
	 */
	public static T_Integer defaultFlagColorOrder() {
		return defaultFlagColorOrder;
	}
	/**
	 * @return the selected flag color order
	 */
	public static T_Integer selectedFlagColorOrder() {
		if (selectedFlagColorOrder == null 
				|| selectedFlagColorOrder.toString().isBlank()) {
			return defaultFlagColorOrder;
		}
		return selectedFlagColorOrder;
	}
	/**
	 * @param colorIndex the current flag color index
	 * @param reverse direction
	 * @return the new flag color index
	 */
	public static int getNextFlagColor(int colorIndex, boolean reverse) {
		int selectedIndex = converter.indexOf(colorIndex); // -1 if none... OK!
		if (reverse) {		 
			selectedIndex -= 1;
			if (selectedIndex < 0) {
				selectedIndex = selectedSize-1;
			}
			return converter.get(selectedIndex);
		} 
		selectedIndex += 1;	
		if (selectedIndex > selectedSize-1) {
			selectedIndex = 0;
		}
		return converter.get(selectedIndex);
	}
}
