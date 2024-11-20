package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class WeaponAnimation extends AbstractOptionsSubUI {
	static final String OPTION_ID = WEAPON_ANIMATION_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				showResultDelay,
				newWeaponSound,
				playerSoundEcho,
				echoSoundDecay,
				echoSoundDelay,
				echoSoundHullDelay
				)));
		map.add(new SafeListParam(Arrays.asList(
				beamWindupFrames,
				beamHoldFrames,
				heavyBeamHoldFrames,
				shieldFadingFrames
				)));
		return map;
	}

	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						showResultDelay,
						newWeaponSound,
						playerSoundEcho,
						echoSoundDecay,
						echoSoundDelay,
						echoSoundHullDelay,

						HEADER_SPACER_50,
						beamWindupFrames,
						beamHoldFrames,
						heavyBeamHoldFrames,
						shieldFadingFrames
						));
		return majorList;
	}
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						newWeaponSound
						));
		return minorList;
	}
}
