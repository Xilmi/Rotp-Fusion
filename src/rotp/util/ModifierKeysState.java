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
	private static boolean isAltDown;
	private static boolean isAltGrDown;
	private static boolean isCtrlDown;
	private static boolean isMetaDown;
	private static boolean isShiftDown;

	public static boolean isAltDown()	{ return isAltDown; }
	public static boolean isAltGrDown()	{ return isAltGrDown; }
	public static boolean isCtrlDown()	{ return isCtrlDown; }
	public static boolean isMetaDown()	{ return isMetaDown; }
	public static boolean isShiftDown()	{ return isShiftDown; }
	public static boolean isShiftOrCtrlDown()	{ return isShiftDown || isCtrlDown; }

	public static void set(InputEvent e)	{ setKeysState(e); }
	public static ModifierKeysState get()	{ return lastState; }
	public static void reset() {
		lastState	= ModifierKeysState.NONE;
		isAltDown	= false;
		isAltGrDown	= false;
		isCtrlDown	= false;
		isMetaDown	= false;
		isShiftDown	= false;
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
		isAltDown	= e.isAltDown();
		isAltGrDown	= e.isAltGraphDown();
		isCtrlDown	= e.isControlDown();
		isMetaDown	= e.isMetaDown();
		isShiftDown	= e.isShiftDown();
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
}
