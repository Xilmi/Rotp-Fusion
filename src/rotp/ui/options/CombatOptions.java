package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class CombatOptions implements IOptionsSubUI {
	static final String OPTION_ID = "ZOOM_OPTIONS";

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("WEAPON_ANIMATIONS"),
				showResultDelay,
				newWeaponSound, playerSoundEcho,
				echoSoundDecay, echoSoundDelay, echoSoundHullDelay,

				headerSpacer50,
				beamWindupFrames, beamHoldFrames,
				heavyBeamHoldFrames, shieldFadingFrames
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SHIELD_ANIMATIONS"),
				shieldType, alwaysShowsShield, 
	
				headerSpacer50,
				beamAnimationFPS, shieldEnveloping, shieldBorder,
				shieldTransparency, shieldFlickering, shieldNoisePct,
				weaponZposition, weaponZRandom,
	
				headerSpacer50,
				startShieldDemo
				)));
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
		return map;
	};
}
