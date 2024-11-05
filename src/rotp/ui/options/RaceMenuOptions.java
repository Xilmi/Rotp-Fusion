package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

public final class RaceMenuOptions implements IOptionsSubUI {
	static final		String OPTION_ID = "RACE_SUBUI";
	public static final String CUSTOM_ID = "CUSTOM_LIST";
	public static final String RACE_ID	 = "RACE_LIST";
	
	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean noPanel()			{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(optionsCustomRace());
		map.add(optionsRace());
		return map;
	}
	static SafeListParam optionsCustomRace() {
		return new SafeListParam(CUSTOM_ID,
				Arrays.asList(playerIsCustom, playerCustomRace));
	}
	static SafeListParam optionsRace() {
		return new SafeListParam(RACE_ID,
				Arrays.asList(playerShipSet, playerIsCustom, playerCustomRace));
	}
}
