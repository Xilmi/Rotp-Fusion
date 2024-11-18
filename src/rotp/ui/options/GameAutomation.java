package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class GameAutomation implements IOptionsSubUI {
	static final String OPTION_ID = GAME_AUTOMATION_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }
	@Override public boolean isCfgFile()		{ return true; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				AllSubUI.governorSubUI(),
				
				headerSpacer50,
				transportAutoEco,
				autoTerraformEnding,
				divertExcessToResearch,
				techExchangeAutoRefuse
				)));
		map.add(new SafeListParam(Arrays.asList(
				showAlliancesGNN,
				hideMinorReports,
				showAllocatePopUp,
				showLimitedWarnings,

				lineSpacer25,
				autoBombard_,
				autoColonize_
				)));
		map.add(new SafeListParam(Arrays.asList(
				defaultForwardRally,
				defaultChainRally,
				chainRallySpeed,
				
				headerSpacer50,
				trackUFOsAcrossTurns
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						autoBombard_,
						autoColonize_,

						lineSpacer25,
						defaultForwardRally,
						defaultChainRally,
						chainRallySpeed,
						
						lineSpacer25,
						transportAutoEco,
						autoTerraformEnding,
						showAlliancesGNN,
						hideMinorReports,
						showAllocatePopUp,
						showLimitedWarnings,

						lineSpacer25,
						techExchangeAutoRefuse,

						lineSpacer25,
						divertExcessToResearch,
						trackUFOsAcrossTurns,
						
						lineSpacer25,
						AllSubUI.governorSubUI()
						));
		return majorList;
	}
}
