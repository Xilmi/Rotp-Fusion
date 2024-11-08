package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class ShipCombatRules implements IOptionsSubUI {
	static final String OPTION_ID = SHIP_COMBAT_RULES_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return false; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("ASTEROIDS"),
				moo1PlanetLocation, moo1AsteroidsLocation,
				asteroidsVanish, moo1AsteroidsProperties,
				headerSpacer50,
				minLowAsteroids, maxLowAsteroids,
				minHighAsteroids, maxHighAsteroids,
				headerSpacer50,
				baseNoAsteroidsProbPct, stepNoAsteroidsProbPct,
				baseLowAsteroidsProbPct, stepLowAsteroidsProbPct,
				richNoAsteroidsModPct, ultraRichNoAsteroidsModPct
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("XILMI_AI_OPTIONS"),
				playerAttackConfidence, playerDefenseConfidence,
				aiAttackConfidence, aiDefenseConfidence,

				headerSpacer50,
				new ParamTitle("GAME_OTHER"),
				maxCombatTurns,
				retreatRestrictions, retreatRestrictionTurns
				)));
		map.add(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		map.add(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());

		return map;
	};
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey());
		majorList.addAll(AllSubUI.getHandle(WEAPON_ANIMATION_UI_KEY).getUiExt());
		majorList.add(headerSpacer50);
		majorList.addAll(AllSubUI.getHandle(SHIELD_ANIMATION_UI_KEY).getUiExt());
		return majorList;
	}

}
