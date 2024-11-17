package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class ShieldAnimations implements IOptionsSubUI {
	static final String OPTION_ID = SHIELD_ANIMATION_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				beamAnimationFPS,
				shieldEnveloping,
				shieldBorder,
				shieldTransparency,
				shieldFlickering,
				shieldNoisePct,

				headerSpacer50,
				weaponZposition,
				weaponZRandom
				)));
		map.add(new SafeListParam(Arrays.asList(
				shieldType,
				alwaysShowsShield,
				headerSpacer100,
				startShieldDemo
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(OPTION_ID,
				Arrays.asList(
						shieldType,
						alwaysShowsShield, 
						
						headerSpacer50,
						beamAnimationFPS,
						shieldEnveloping,
						shieldBorder,
						shieldTransparency,
						shieldFlickering,
						shieldNoisePct,
						weaponZposition,
						weaponZRandom,
			
						headerSpacer50,
						startShieldDemo
						));
		return majorList;
	}
}
