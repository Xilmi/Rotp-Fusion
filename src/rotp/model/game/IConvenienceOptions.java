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
	{
		{ isCfgFile(true); }
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getBoolean(getCfgLabel()) == null) {
				setOptionValue(opts, get());
			}
			isCfgFile(false);
		}
	};

	ParamInteger showLimitedWarnings	= new ParamInteger(MOD_UI, "SHOW_LIMITED_WARNINGS" , -1, -1, 49, 1, 2, 5)
	{
		{ isCfgFile(true); }
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getInteger(getCfgLabel()) == null) {
				setOptionValue(opts, get());
			}
			isCfgFile(false);
		}
	}	.loop(true)
		.specialNegative(MOD_UI + "SHOW_LIMITED_WARNINGS_ALL");

	ParamBoolean showAlliancesGNN		= new ParamBoolean(MOD_UI, "SHOW_ALLIANCES_GNN", true)
	{	
		{ isCfgFile(true); }
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getBoolean(getCfgLabel()) == null) {
				setOptionValue(opts, get());
			}
			isCfgFile(false);
		}
	};

	ParamBoolean techExchangeAutoRefuse = new ParamBoolean(MOD_UI, "TECH_EXCHANGE_AUTO_NO", false)
	{
		{ isCfgFile(true); }
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getBoolean(getCfgLabel()) == null) {
				setOptionValue(opts, get());
			}
			isCfgFile(false);
		}
	};

	ParamBoolean autoColonize_	= new ParamBoolean( GAME_UI, "AUTOCOLONIZE", false) {
//		{ isDuplicate(true); isCfgFile(true); }
//		@Override public Boolean getOption()		{ return autoColonize(); }
//		@Override public void setOption(Boolean b)	{ autoColonize(b); }
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getBoolean(getCfgLabel()) == null) {
				if (set)
					set(UserPreferences.getAutoColonize());
				setOptionValue(opts, get());
			}
		}
	};
	default boolean autoColonize()				{ return autoColonize_.get(); }

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
		@Override public void transfert (IGameOptions opts, boolean set)	{
			if (opts.dynOpts().getString(getCfgLabel()) == null) {
				if (set)
					set(UserPreferences.autoBombardMode());
				setOptionValue(opts, get());
			}
		}
	};
	default boolean autoBombardNever()		{ return autoBombard_.get().equals(AUTOBOMBARD_NEVER); }
	default boolean autoBombardYes()		{ return autoBombard_.get().equals(AUTOBOMBARD_YES); }
	default boolean autoBombardWar()		{ return autoBombard_.get().equals(AUTOBOMBARD_WAR); }
	default boolean autoBombardInvading()	{ return autoBombard_.get().equals(AUTOBOMBARD_INVADE); }

	// ==================== GUI List Declarations ====================
	LinkedList<IParam> convenienceOptions = new LinkedList<>(
			Arrays.asList(
				systemNameDisplay, shipDisplay, flightPathDisplay, showGridCircular, showShipRanges,
				null,
				autoColonize_, autoBombard_, divertExcessToResearch, defaultMaxBases, IMainOptions.compactOptionOnly,
				null,
				displayYear, showNextCouncil, showAlliancesGNN, showLimitedWarnings, techExchangeAutoRefuse
			));
}
