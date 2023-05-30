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

package rotp.util;

import java.awt.event.InputEvent;

public enum ModifierKeysState {
	NONE			(""),
	SHIFT			("(Shift) "),
	CTRL			("(Ctrl) "),
	//ALT				("(Alt) "),
	CTRL_SHIFT		("(Ctrl)+(Shift) "),
	//ALT_SHIFT		("(Alt)+(Shift)) "),
	//ALT_CTRL		("(Alt)+(Ctrl) "),
	//ALT_CTRL_SHIFT	("(Alt)+(Ctrl)+(Shift) ")
	;

	public final String helpLine;
	private ModifierKeysState(String opt) { helpLine = opt; }

	private static ModifierKeysState lastState = ModifierKeysState.NONE;
	private static boolean isShiftDown;
	private static boolean isCtrlDown;
	private static boolean isAltDown;

	public static boolean isShiftDown()	{ return isShiftDown; }
	public static boolean isCtrlDown()	{ return isCtrlDown; }
	public static boolean isAltDown()	{ return isAltDown; }
	public static boolean isShiftOrCtrlDown()	{ return isShiftDown || isCtrlDown; }

	public static void set(InputEvent e) {
		setKeysState(e);
//		lastState = analyze(e);
//		return lastState;
	}
	public static ModifierKeysState get() {
		return lastState;
	}
	public static void reset() {
		lastState	= ModifierKeysState.NONE;
		isShiftDown	= false;
		isCtrlDown	= false;
		isAltDown	= false;
	}
	public static boolean checkForChange(InputEvent e) {
		ModifierKeysState newState = analyze(e);
		if (newState == lastState)
			return false;
		lastState = newState;
		return true;
	}
	private static boolean setKeysState(InputEvent e) {
		if (e == null) {
			reset();
			return true;
		}
		isShiftDown	= e.isShiftDown();
		isCtrlDown	= e.isControlDown();
		isAltDown	= e.isAltDown();
		return false;
	}
	
	private static ModifierKeysState analyze(InputEvent e) {
		setKeysState(e);
		if (setKeysState(e))
			return lastState;
		if (isShiftDown)
			if (isCtrlDown)
				return ModifierKeysState.CTRL_SHIFT;
			else
				return ModifierKeysState.SHIFT;
		if (isCtrlDown)
			return ModifierKeysState.CTRL;
		return ModifierKeysState.NONE;
	}
// !!! Middle-click wrongly generate alt alt action !!!
//
//	private static ModifierKeysState analyze(InputEvent e) {
//		setKeysState(e);
//		if (setKeysState(e))
//			return lastState;
//		if (isShiftDown)
//			if (isCtrlDown)
//				if (isAltDown)
//					return ModifierKeysState.ALT_CTRL_SHIFT;
//				else
//					return ModifierKeysState.CTRL_SHIFT;
//			else if (isAltDown)
//				return ModifierKeysState.ALT_SHIFT;
//			else
//				return ModifierKeysState.SHIFT;
//		if (isCtrlDown)
//			if (isAltDown)
//				return ModifierKeysState.ALT_CTRL;
//			else
//				return ModifierKeysState.CTRL;
//		if (isAltDown)
//			return ModifierKeysState.ALT;
//		return ModifierKeysState.NONE;
//	}
}
