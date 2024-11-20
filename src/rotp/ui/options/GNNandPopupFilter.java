package rotp.ui.options;

import java.util.Arrays;

import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;

final class GNNandPopupFilter extends AbstractOptionsSubUI {
	static final String OPTION_ID = GNN_AND_POPUP_FILTER_UI_KEY;

	@Override public String optionId()			{ return OPTION_ID; }

	@Override public SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(OPTION_ID);
		map.add(new SafeListParam(Arrays.asList(
				showAlliancesGNN,
				hideMinorReports,
				showLimitedWarnings,
				showAllocatePopUp
				)));
		map.add(new SafeListParam(Arrays.asList(
				RELEVANT_TITLE,
				autoBombard_,
				autoColonize_,
				techExchangeAutoRefuse
				)));
		return map;
	}
	@Override public SafeListParam majorList()	{
		SafeListParam majorList = new SafeListParam(uiMajorKey(),
				Arrays.asList(
						showAlliancesGNN,
						hideMinorReports,
						showLimitedWarnings,
						showAllocatePopUp
						));
		return majorList;
	}
}
