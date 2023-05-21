package rotp.model.game;

import java.util.LinkedList;

import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamTitle;

public interface BaseOptions {
	String BASE_UI				= "SETUP_";
	String GAME_UI				= "GAME_SETTINGS_";
	String ADV_UI				= "SETTINGS_";
	String MOD_UI				= "SETTINGS_MOD_";
	String HEADERS				= "HEADERS_";
	String ALL_GUI_ID			= "ALL_GUI";
	// To be able to identify the current options
	int GAME_ID  = 0;
	int SETUP_ID = 1;
	int UNKNOWN_ID = -1;
	int id();
	void id(int id);
	default void	setAsGame()			{ id(GAME_ID); }
	default void	setAsSetup()		{ id(SETUP_ID); }
	default void	setAsUnknown()		{ id(UNKNOWN_ID); }
	default boolean	isGameOption()		{ return id() == GAME_ID; }
	default boolean	isSetupOption()		{ return id() == SETUP_ID; }
	default boolean	isUnknownOption()	{ return !isGameOption() && !isSetupOption(); }
	default void	showOptionName()	{
		System.out.println("Option name = " + optionName());
	}
	default String	optionName()		{
		switch (id()) {
			case GAME_ID:
				return "Game Options";
			case SETUP_ID:
				return "Setup Options";
			default:
				return "Unknown Options";
		}
	}
	DynOptions dynOpts();

	ParamTitle headerSpacer = new ParamTitle("SPACER");
	
	void updateOptionsAndSaveToFileName (String fileName, LinkedList<InterfaceParam> paramList);
	void loadAndUpdateFromFileName (String fileName, LinkedList<InterfaceParam> paramList);
	void setBaseAndModSettingsToDefault (LinkedList<InterfaceParam> paramList);
	void saveOptionsToFileName (String fileName);
	void setModSettingsFromOptions();
	void writeModSettingsToOptions(LinkedList<InterfaceParam> paramList, boolean call);
	void copyAliensAISettings(IGameOptions dest);
}
