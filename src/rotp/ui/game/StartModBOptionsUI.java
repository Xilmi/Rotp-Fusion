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

import static rotp.ui.UserPreferences.loadWithNewOptions;
import static rotp.ui.UserPreferences.maximizeSpacing;
import static rotp.ui.UserPreferences.minStarsPerEmpire;
import static rotp.ui.UserPreferences.prefStarsPerEmpire;
import static rotp.ui.UserPreferences.spacingLimit;
import static rotp.ui.UserPreferences.techCloaking;
import static rotp.ui.UserPreferences.techHyperspace;
import static rotp.ui.UserPreferences.techIndustry2;
import static rotp.ui.UserPreferences.techIrradiated;
import static rotp.ui.UserPreferences.techStargate;
import static rotp.ui.UserPreferences.techTerra120;
import static rotp.ui.UserPreferences.techThorium;
import static rotp.ui.UserPreferences.techTransport;
import static rotp.ui.UserPreferences.eventsStartTurn;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import rotp.ui.BaseText;
import rotp.ui.util.AbstractOptionsUI;

// modnar: add UI panel for modnar MOD game options, based on StartOptionsUI.java
public class StartModBOptionsUI extends AbstractOptionsUI {
	private static final long serialVersionUID = 1L;
	public static final String guiTitleID = "SETTINGS_MOD_TITLE_B";
	// First column (left)
	protected BaseText maximizeSpacingText;
	protected BaseText spacingLimitText;
	protected BaseText minStarsPerEmpireText;
	protected BaseText prefStarsPerEmpireText;
	protected BaseText loadWithNewOptionsText;
	// Second column
	protected BaseText techIrradiatedText;
	protected BaseText techCloakingText;
	protected BaseText techStargateText;
	protected BaseText techHyperspaceText;
	// Third column
	protected BaseText techIndustry2Text;
	protected BaseText techThoriumText;
	protected BaseText techTransportText;
	protected BaseText techTerra120Text;
	// Fourth column
	
	public StartModBOptionsUI() {
		super(guiTitleID);
	}
	@Override protected void init0() {
		// For "ParamX" class Settings (Starting with ParamX for the automation to work)
		// Complete this table... Et Voilà!
		// For Mixed Setup keep "paintComponent" up to date
		// First column (left)
		btList.add(loadWithNewOptionsText	= newBT()); paramList.add(loadWithNewOptions);
		btList.add(maximizeSpacingText		= newBT()); paramList.add(maximizeSpacing);
		btList.add(spacingLimitText			= newBT()); paramList.add(spacingLimit);
		btList.add(minStarsPerEmpireText	= newBT()); paramList.add(minStarsPerEmpire);
		btList.add(prefStarsPerEmpireText	= newBT()); paramList.add(prefStarsPerEmpire);
		endOfColumn();
		// Second column
		btList.add(techIrradiatedText		= newBT()); paramList.add(techIrradiated);
		btList.add(techCloakingText			= newBT()); paramList.add(techCloaking);
		btList.add(techStargateText			= newBT()); paramList.add(techStargate);
		btList.add(techHyperspaceText		= newBT()); paramList.add(techHyperspace);
		btList.add(loadWithNewOptionsText	= newBT()); paramList.add(eventsStartTurn);
		endOfColumn();
		// Third column
		btList.add(techIndustry2Text	= newBT()); paramList.add(techIndustry2);
		btList.add(techThoriumText		= newBT()); paramList.add(techThorium);
		btList.add(techTransportText	= newBT()); paramList.add(techTransport);
		btList.add(techTerra120Text		= newBT()); paramList.add(techTerra120);
		endOfColumn();
		// Fourth column
		// endOfColumn();
	}
	@Override protected void initCustom() {}
	@Override protected void paintCustomComponent(Graphics2D g) {}
	@Override protected void repaintCustomComponent() {}
	@Override protected void customMouseCommon(boolean up, boolean mid, boolean shiftPressed,
			boolean ctrlPressed, MouseEvent e, MouseWheelEvent w) {}
}
