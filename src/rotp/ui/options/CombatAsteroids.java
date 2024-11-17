package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class CombatAsteroids implements IOptionsSubUI {
	static final String OPTION_ID = COMBAT_ASTEROID_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }
	@Override public boolean hasExtraParam()	{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				moo1PlanetLocation,
				moo1AsteroidsLocation,
				asteroidsVanish,
				moo1AsteroidsProperties
				)));
		map.add(new SafeListParam(Arrays.asList(
				minLowAsteroids,
				maxLowAsteroids,
				minHighAsteroids,
				maxHighAsteroids
				)));
		map.add(new SafeListParam(Arrays.asList(
				baseNoAsteroidsProbPct,
				stepNoAsteroidsProbPct,
				baseLowAsteroidsProbPct,
				stepLowAsteroidsProbPct,
				richNoAsteroidsModPct,
				ultraRichNoAsteroidsModPct
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(OPTION_ID,
				Arrays.asList(
						moo1PlanetLocation,
						moo1AsteroidsLocation,
						asteroidsVanish,
						moo1AsteroidsProperties
						));
		return majorList;
	}
}
