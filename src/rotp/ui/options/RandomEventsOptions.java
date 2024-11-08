package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.IAdvOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamTitle;

final class RandomEventsOptions implements IOptionsSubUI {
	static final String OPTION_ID = "RANDOM_EVENTS";

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_GLOBAL"),
				IAdvOptions.randomEvents,
				eventsStartTurn, eventsPace,
				eventsFavorWeak, fixedEventsMode,
				monstersGiveLoots, monstersLevel,
				monstersGNNNotification,

				headerSpacer50,
				guardianMonstersLevel,
				isMoO1Monster,
				
				headerSpacer50,
				new ParamTitle("RANDOM_EVENTS_MONSTERS"),
				piratesDelayTurn, piratesReturnTurn, piratesMaxSystems,
				headerSpacer50,
				amoebaDelayTurn, amoebaReturnTurn, amoebaMaxSystems,
				headerSpacer50,
				crystalDelayTurn, crystalReturnTurn, crystalMaxSystems
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_DELAYS"),
				donationDelayTurn,
				depletedDelayTurn,
				enrichedDelayTurn,
				fertileDelayTurn,
				virusDelayTurn,
				earthquakeDelayTurn,
				accidentDelayTurn,
				rebellionDelayTurn,
				derelictDelayTurn,
				assassinDelayTurn,
				plagueDelayTurn,
				supernovaDelayTurn,
				piracyDelayTurn,
				cometDelayTurn,
				relicDelayTurn,
				sizeBoostDelayTurn,
				gauntletDelayTurn
				)));
		map.add(new SafeListParam(Arrays.asList(
				new ParamTitle("RANDOM_EVENTS_RETURNS"),
				donationReturnTurn,
				depletedReturnTurn,
				enrichedReturnTurn,
				fertileReturnTurn,
				virusReturnTurn,
				earthquakeReturnTurn,
				accidentReturnTurn,
				rebellionReturnTurn,
				derelictReturnTurn,
				assassinReturnTurn,
				plagueReturnTurn,
				supernovaReturnTurn,
				piracyReturnTurn,
				cometReturnTurn,
				relicReturnTurn,
				sizeBoostReturnTurn,
				gauntletReturnTurn
				)));
		return map;
	}
}
