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
package rotp.ui.game;

import static rotp.model.game.MOO1GameOptions.loadAndUpdateFromFileName;
import static rotp.model.game.MOO1GameOptions.setBaseAndModSettingsToDefault;
import static rotp.model.game.MOO1GameOptions.updateOptionsAndSaveToFileName;
import static rotp.ui.UserPreferences.ALL_GUI_ID;
import static rotp.ui.UserPreferences.GAME_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LAST_OPTIONS_FILE;
import static rotp.ui.UserPreferences.LIVE_OPTIONS_FILE;
import static rotp.ui.UserPreferences.USER_OPTIONS_FILE;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.util.LinkedList;

import rotp.ui.BasePanel;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.Modifier2KeysState;
import rotp.util.LabelManager;

public abstract class BaseModPanel extends BasePanel {
 
	private static final String setGlobalDefaultKey	= "SETTINGS_GLOBAL_DEFAULT";
	private static final String setLocalDefaultKey	= "SETTINGS_LOCAL_DEFAULT";
	private static final String setGlobalGameKey	= "SETTINGS_GLOBAL_LAST_GAME";
	private static final String setLocalGameKey		= "SETTINGS_LOCAL_LAST_GAME";
	private static final String setGlobalLastKey	= "SETTINGS_GLOBAL_LAST_SET";
	private static final String setLocalLastKey		= "SETTINGS_LOCAL_LAST_SET";
	private static final String setGlobalUserKey	= "SETTINGS_GLOBAL_USER_SET";
	private static final String setLocalUserKey		= "SETTINGS_LOCAL_USER_SET";
	private static final String saveGlobalUserKey	= "SETTINGS_GLOBAL_USER_SAVE";
	private static final String saveLocalUserKey	= "SETTINGS_LOCAL_USER_SAVE";
	private static final String restoreGlobalKey	= "SETTINGS_GLOBAL_RESTORE";
	private static final String restoreLocalKey		= "SETTINGS_LOCAL_RESTORE";
	private static final String exitKey		 		= "SETTINGS_EXIT";

	private static int exitButtonWidth, userButtonWidth, defaultButtonWidth, lastButtonWidth;

	protected static int  smallButtonMargin, smallButtonH;
	protected static Font smallButtonFont;

	LinkedList<InterfaceParam> paramList = new LinkedList<>();

	//	protected Font smallButtonFont	= FontManager.current().narrowFont(20);
	protected Rectangle defaultBox	= new Rectangle();
	protected Rectangle lastBox		= new Rectangle();
	protected Rectangle userBox		= new Rectangle();

	protected boolean globalOptions	= false; // No preferred button and Saved to remnant.cfg

	private void localInit(Graphics2D g) {
		smallButtonMargin	= s30;
		smallButtonH		= s30;
		smallButtonFont		= narrowFont(20);
		
		Font prevFont = g.getFont();
		g.setFont(smallButtonFont);

		initExitButtonWidth(g);
		initUserButtonWidth(g);
		initDefaultButtonWidth(g);
		initLastButtonWidth(g);

		g.setFont(prevFont);
	}
	private int stringWidth(Graphics2D g, String key) {
		return g.getFontMetrics().stringWidth(LabelManager.current().label(key));
	}
	private int buttonWidth(Graphics2D g, String[] keys) {
		int result = 0;
		for (String key : keys)
			result = max(result, stringWidth(g, key));
		return smallButtonMargin + result;
	}
	
	protected abstract String GUI_ID();
	protected void refreshGui() {}

	protected void repaintButtons() { repaint(); }
	protected void checkModifierKey(InputEvent e) {
		if (Modifier2KeysState.checkForChange(e)) {
			repaintButtons();
		}
	}
	protected void init() {
		Modifier2KeysState.reset();
		if (paramList != null)
			for (InterfaceParam param : paramList)
				param.setPanel(this);

	}

	protected void close() {
		Modifier2KeysState.reset();
		if (paramList != null)
			for (InterfaceParam param : paramList)
				param.setPanel(null);
    }

	// ---------- Exit Button
	protected String exitButtonKey() {
		switch (Modifier2KeysState.get()) {
		// case CTRL:
		// case CTRL_SHIFT: return cancelKey;
		default: return exitKey;
		}
	}
	private void initExitButtonWidth(Graphics2D g) {
		exitButtonWidth = buttonWidth(g, new String[] {exitKey});
		// cancelKey, exitKey});
	}
	protected int exitButtonWidth(Graphics2D g) {
		if (exitButtonWidth == 0)
			localInit(g);
		return exitButtonWidth;
	}
    protected void doExitBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL:
		case CTRL_SHIFT: // Restore
			// loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			// break;
		default: // Save
			updateOptionsAndSaveToFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);
			break; 
		}
		close();
	}

	// ---------- User Button
	protected String userButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:		 return saveGlobalUserKey;
		case CTRL_SHIFT: return saveLocalUserKey;
		case SHIFT:		 return setLocalUserKey;
		default:		 return setGlobalUserKey;
		}
	}
	private void initUserButtonWidth(Graphics2D g) {
		userButtonWidth = buttonWidth(g, new String[] {
				saveGlobalUserKey, saveLocalUserKey, setLocalUserKey, setGlobalUserKey});
	}
	protected int userButtonWidth(Graphics2D g) {
		if (userButtonWidth == 0) 
			localInit(g);
		return userButtonWidth;
	}
	protected void doUserBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL: // saveGlobalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			return;
		case CTRL_SHIFT: // saveLocalUserKey
			updateOptionsAndSaveToFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID());
			return;
		case SHIFT: // setLocalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, GUI_ID());
			refreshGui();
			return;
		default: // setGlobalUserKey
			loadAndUpdateFromFileName(guiOptions(), USER_OPTIONS_FILE, ALL_GUI_ID);
			refreshGui();
		}
	}	

	// ---------- Default Button
	protected String defaultButtonKey() {
		if (globalOptions)  // The old ways
			switch (Modifier2KeysState.get()) {
			case CTRL:
			case CTRL_SHIFT: return restoreLocalKey;
			default:		 return setLocalDefaultKey;
			}
		else
			switch (Modifier2KeysState.get()) {
			case CTRL:		 return restoreGlobalKey;
			case CTRL_SHIFT: return restoreLocalKey;
			case SHIFT:		 return setLocalDefaultKey;
			default:		 return setGlobalDefaultKey;
			}
	}
	private void initDefaultButtonWidth(Graphics2D g) {
		defaultButtonWidth = buttonWidth(g, new String[] {
				restoreGlobalKey, restoreLocalKey, setLocalDefaultKey, setGlobalDefaultKey});
	}
	protected int defaultButtonWidth(Graphics2D g) {
		if (defaultButtonWidth == 0) 
			localInit(g);
		return defaultButtonWidth;
	}
	protected void doDefaultBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL: // restoreGlobalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, ALL_GUI_ID);		
			break;
		case CTRL_SHIFT: // restoreLocalKey
			loadAndUpdateFromFileName(guiOptions(), LIVE_OPTIONS_FILE, GUI_ID());		
			break;
		case SHIFT: // setLocalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), GUI_ID());		
			break; 
		default: // setGlobalDefaultKey
			setBaseAndModSettingsToDefault(guiOptions(), ALL_GUI_ID);		
			break; 
		}
		refreshGui();
	}

	// ---------- Last Button
	protected String lastButtonKey() {
		switch (Modifier2KeysState.get()) {
		case CTRL:		 return setGlobalGameKey;
		case CTRL_SHIFT: return setLocalGameKey;
		case SHIFT:		 return setLocalLastKey;
		default:		 return setGlobalLastKey;
		}
	}
	private void initLastButtonWidth(Graphics2D g) {
		lastButtonWidth = buttonWidth(g, new String[] {
				setGlobalGameKey, setLocalGameKey, setLocalLastKey, setGlobalLastKey});
	}
	protected int lastButtonWidth(Graphics2D g) {
		if (lastButtonWidth == 0) 
			localInit(g);
		return lastButtonWidth;
	}
	protected void doLastBoxAction() {
		buttonClick();
		switch (Modifier2KeysState.get()) {
		case CTRL: // setGlobalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, ALL_GUI_ID);
			break;
		case CTRL_SHIFT: // setLocalGameKey
			loadAndUpdateFromFileName(guiOptions(), GAME_OPTIONS_FILE, GUI_ID());
			break;
		case SHIFT: // setLocalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, GUI_ID());
			break;
		default: // setGlobalLastKey
			loadAndUpdateFromFileName(guiOptions(), LAST_OPTIONS_FILE, ALL_GUI_ID);
		}
		refreshGui();
	}
}
