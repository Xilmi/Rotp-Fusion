package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListParam;

final class WeaponAnimation implements IOptionsSubUI {
	static final String OPTION_ID = WEAPON_ANIMATION_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(OPTION_ID,
				Arrays.asList(
						showResultDelay,
						newWeaponSound,
						playerSoundEcho,
						echoSoundDecay,
						echoSoundDelay,
						echoSoundHullDelay,

						headerSpacer50,
						beamWindupFrames,
						beamHoldFrames,
						heavyBeamHoldFrames,
						shieldFadingFrames
						));
		return majorList;
	}
}
