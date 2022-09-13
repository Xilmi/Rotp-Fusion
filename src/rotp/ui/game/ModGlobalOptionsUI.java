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
package rotp.ui.game;

import static rotp.ui.UserPreferences.mapFontFactor;
import static rotp.ui.UserPreferences.menuLoadGame;
import static rotp.ui.UserPreferences.menuStartup;
import static rotp.ui.UserPreferences.showFlagFactor;
import static rotp.ui.UserPreferences.showFleetFactor;
import static rotp.ui.UserPreferences.showInfoFontRatio;
import static rotp.ui.UserPreferences.showNameMinFont;
import static rotp.ui.UserPreferences.showPathFactor;
import static rotp.ui.UserPreferences.showNewRaces;
import static rotp.ui.UserPreferences.showGridCircular;
import static rotp.ui.UserPreferences.showPathFactor;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.BaseText;
import rotp.ui.util.AbstractOptionsUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class ModGlobalOptionsUI extends AbstractOptionsUI {
	private static final long serialVersionUID = 1L;
	public  static final String guiTitleID = "SETTINGS_MOD_TITLE_GLOBAL";
	// First column (left)
	protected BaseText menuStartupText;
	protected BaseText menuLoadGameText;
	protected BaseText newRacesOnByDefaultText;
	protected BaseText gridCircularDisplayText;
	// Second column
	protected BaseText showFleetFactorText;
	protected BaseText showFlagFactorText;
	protected BaseText showPathFactorText;
	// Third column
	protected BaseText showNameMinFontText;
	protected BaseText showInfoFontRatioText;
	protected BaseText mapFontFactorText;
	// Fourth column

	public ModGlobalOptionsUI() {
		super(guiTitleID);
	}

	@Override protected void init0() {
		globalOptions = true; // No preferred button
		// For "ParamX" class Settings (Starting with ParamX for the automation to work)
		// Complete this table... Et Voilà!
		// For Mixed Setup keep "paintComponent" up to date
		// First column (left)
		btList.add(menuStartupText			= newBT()); paramList.add(menuStartup);
		btList.add(menuLoadGameText			= newBT()); paramList.add(menuLoadGame);
		btList.add(newRacesOnByDefaultText	= newBT()); paramList.add(showNewRaces);
		btList.add(gridCircularDisplayText	= newBT()); paramList.add(showGridCircular);
		endOfColumn();
		// Second column
		btList.add(showFleetFactorText	= newBT()); paramList.add(showFleetFactor);
		btList.add(showFlagFactorText	= newBT()); paramList.add(showFlagFactor);
		btList.add(showPathFactorText	= newBT()); paramList.add(showPathFactor);
		endOfColumn();
		// Third column
		btList.add(showNameMinFontText		= newBT()); paramList.add(showNameMinFont);
		btList.add(showInfoFontRatioText	= newBT()); paramList.add(showInfoFontRatio);
		btList.add(mapFontFactorText		= newBT()); paramList.add(mapFontFactor);
		endOfColumn();
		// endOfColumn();
		// Fourth column
		// endOfColumn();
	}
	@Override protected void initCustom() {}
	@Override protected void paintCustomComponent(Graphics2D g) {}
	@Override protected void repaintCustomComponent() {}
	@Override protected void customMouseCommon(boolean up, boolean mid, boolean shiftPressed,
			boolean ctrlPressed, MouseEvent e, MouseWheelEvent w) {}
}
