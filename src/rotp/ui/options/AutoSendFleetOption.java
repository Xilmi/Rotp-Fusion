package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class AutoSendFleetOption extends AbstractOptionsSubUI {
	static final String OPTION_ID = AUTO_SEND_FLEET_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return false; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GOVERNOR_AUTO_SEND"),
				fleetAutoScoutMode,
				fleetAutoColonizeMode,
				fleetAutoAttackMode
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("GOVERNOR_TARGET"),
				autoAttackEmpire
				)));
		return map;
	};
	@Override public SafeListParam minorList()	{
		SafeListParam minorList = new SafeListParam(uiMinorKey(),
				Arrays.asList(
						fleetAutoScoutMode,
						fleetAutoColonizeMode,
						fleetAutoAttackMode
						));
		return minorList;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						fleetAutoScoutMode,
						fleetAutoColonizeMode,
						LINE_SPACER_25,
						fleetAutoAttackMode,
						autoAttackEmpire
						));
		return majorList;
	}

}
