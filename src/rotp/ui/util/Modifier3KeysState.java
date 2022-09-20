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

import java.awt.event.InputEvent;

public enum Modifier3KeysState {
	NONE, SHIFT, CTRL, ALT, CTRL_SHIFT, ALT_SHIFT, ALT_CTRL, ALT_CTRL_SHIFT;

	private static Modifier3KeysState lastState = Modifier3KeysState.NONE;

	public static Modifier3KeysState set(InputEvent e) {
		lastState = analyze(e);
		return lastState;
	}
	public static Modifier3KeysState get() {
		return lastState;
	}
	public static boolean checkForChange(InputEvent e) {
		Modifier3KeysState newState = analyze(e);
		if (newState == lastState)
			return false;
		lastState = newState;
		return true;
	}

	private static Modifier3KeysState analyze(InputEvent e) {
		if (e == null)
			return Modifier3KeysState.NONE;
		if (e.isShiftDown())
			if (e.isControlDown())
				if (e.isAltDown())
					return Modifier3KeysState.ALT_CTRL_SHIFT;
				else
					return Modifier3KeysState.CTRL_SHIFT;
			else if (e.isAltDown())
				return Modifier3KeysState.ALT_SHIFT;
			else
				return Modifier3KeysState.SHIFT;
		if (e.isControlDown())
			if (e.isAltDown())
				return Modifier3KeysState.ALT_CTRL;
			else
				return Modifier3KeysState.CTRL;
		if (e.isAltDown())
			return Modifier3KeysState.ALT;
		return Modifier3KeysState.NONE;
	}
}
