package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class ShieldAnimations extends AbstractOptionsSubUI {
	static final String OPTION_ID = SHIELD_ANIMATION_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SHIELD_LOOK"),
				beamAnimationFPS,
				shieldEnveloping,
				shieldBorder,
				shieldTransparency,
				shieldFlickering,
				shieldNoisePct
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("WEAPON_POSITION"),
				weaponZposition,
				weaponZRandom
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("SHIELD_ACTIVATION"),
				shieldType,
				alwaysShowsShield,
				HEADER_SPACER_100,
				startShieldDemo
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						shieldType,
						alwaysShowsShield,
						
						HEADER_SPACER_50,
						beamAnimationFPS,
						shieldEnveloping,
						shieldBorder,
						shieldTransparency,
						shieldFlickering,
						shieldNoisePct,
						weaponZposition,
						weaponZRandom,
			
						HEADER_SPACER_50,
						startShieldDemo
						));
		return majorList;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						shieldType,
						alwaysShowsShield
						));
		return minorList;
	}
}
