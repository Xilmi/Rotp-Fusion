package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.UserPreferences;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamList;

// Options saved in Remnants.cfg
public interface IConvenienceOptions extends IMapOptions {
	// ==================== Parameters saved in Remnant.cfg ====================
	String AUTOBOMBARD_NO		= "GAME_SETTINGS_AUTOBOMBARD_NO";
	String AUTOBOMBARD_NEVER	= "GAME_SETTINGS_AUTOBOMBARD_NEVER";
	String AUTOBOMBARD_YES		= "GAME_SETTINGS_AUTOBOMBARD_YES";
	String AUTOBOMBARD_WAR		= "GAME_SETTINGS_AUTOBOMBARD_WAR";
	String AUTOBOMBARD_INVADE	= "GAME_SETTINGS_AUTOBOMBARD_INVADE";

	ParamBoolean showNextCouncil		= new ParamBoolean(MOD_UI, "SHOW_NEXT_COUNCIL", false) // Show years left until next council
	{	{ isCfgFile(true); }	};

	ParamInteger showLimitedWarnings	= new ParamInteger(MOD_UI, "SHOW_LIMITED_WARNINGS" , -1, -1, 49, 1, 2, 5)
	{	{ isCfgFile(false); }	}
		.loop(true)
		.specialNegative(MOD_UI + "SHOW_LIMITED_WARNINGS_ALL");
	default int selectedMaxWarnings()				{
		int max = showLimitedWarnings.get();
		if (max < 0)
			max=100;
		return max;
	}

	ParamBoolean showAlliancesGNN	= new ParamBoolean(MOD_UI, "SHOW_ALLIANCES_GNN", true)
	{	{ isCfgFile(false); }	};
	default boolean hideAlliancesGNN()		{ return !showAlliancesGNN.get(); }

	ParamBoolean hideMinorReports	= new ParamBoolean(MOD_UI, "HIDE_MINOR_REPORTS", false)
	{	{ isCfgFile(false); }	};
	default boolean hideMinorReports()		{ return hideMinorReports.get(); }

	ParamBoolean showAllocatePopUp	= new ParamBoolean(MOD_UI, "SHOW_ALLOCATE_POPUP", true)
	{	{ isCfgFile(false); }	};
	default boolean showAllocatePopUp()		{ return showAllocatePopUp.get(); }

	ParamBoolean techExchangeAutoRefuse = new ParamBoolean(MOD_UI, "TECH_EXCHANGE_AUTO_NO", false)
	{	{ isCfgFile(false); }	};

	ParamBoolean autoColonize_	= new ParamBoolean( GAME_UI, "AUTOCOLONIZE", false);
	default boolean autoColonize()			{ return autoColonize_.get(); }

	ParamList autoBombard_		= new ParamList( GAME_UI, "AUTOBOMBARD",
			Arrays.asList(
					AUTOBOMBARD_NO,
					AUTOBOMBARD_NEVER,
					AUTOBOMBARD_YES,
					AUTOBOMBARD_WAR,
					AUTOBOMBARD_INVADE
					),
			AUTOBOMBARD_NO) {
		{
//			isDuplicate(true); isCfgFile(true);
			showFullGuide(true);
		}
		@Override public String getCfgValue() { return UserPreferences.autoBombardToSettingName(get()); }
//		@Override public String getOption()			{ return autoBombardMode(); }
//		@Override public void setOption(String s)	{ autoBombardMode(s); }
	};
	default boolean autoBombardNever()		{ return autoBombard_.get().equals(AUTOBOMBARD_NEVER); }
	default boolean autoBombardYes()		{ return autoBombard_.get().equals(AUTOBOMBARD_YES); }
	default boolean autoBombardWar()		{ return autoBombard_.get().equals(AUTOBOMBARD_WAR); }
	default boolean autoBombardInvading()	{ return autoBombard_.get().equals(AUTOBOMBARD_INVADE); }

	// ==================== GUI List Declarations ====================
	LinkedList<IParam> convenienceOptions = new LinkedList<>(
			Arrays.asList(
				systemNameDisplay, shipDisplay,
				flightPathDisplay, showGridCircular,
				showShipRanges,
				null,
				autoColonize_, autoBombard_,
				divertExcessToResearch, defaultMaxBases,
				IMainOptions.compactOptionOnly,
				null,
				displayYear, showNextCouncil,
				showAlliancesGNN, showLimitedWarnings,
				techExchangeAutoRefuse,
				null,
				hideMinorReports, showAllocatePopUp
			));
}
