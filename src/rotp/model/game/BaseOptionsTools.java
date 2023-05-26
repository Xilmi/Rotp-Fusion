package rotp.model.game;

import java.util.LinkedList;

import rotp.ui.RotPUI;
import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamTitle;

public interface BaseOptionsTools {
	String BASE_UI				= "SETUP_";
	String GAME_UI				= "GAME_SETTINGS_";
	String ADV_UI				= "SETTINGS_";
	String MOD_UI				= "SETTINGS_MOD_";
	String HEADERS				= "HEADERS_";	
	// To be able to identify the current options
	int UNKNOWN_ID = 0;
	int GAME_ID  = 1;
	int SETUP_ID = 2;

	ParamTitle headerSpacer = new ParamTitle("SPACER");

	int id();
	void id(int id);
	DynOptions dynOpts();
	IGameOptions opts();
	
	default void	setAsGame()				{ id(GAME_ID); }
	default void	setAsSetup()			{ id(SETUP_ID); }
	default void	setAsUnknown()			{ id(UNKNOWN_ID); }
	default boolean	isGameOption()			{ return id() == GAME_ID; }
	default boolean	isSetupOption()			{ return id() == SETUP_ID; }
	default boolean	isUnknownOption()		{ return !isGameOption() && !isSetupOption(); }
	default void	updateGuiOptionsId()	{
		RotPUI.currentOptions(id());
		writeModSettingsToOptions(true);
	}
	
	void loadStartupOptions();
	void saveOptionsToFile (String fileName);
	/**
	 * Load the file and update the listed parameters
	 * (Options and options' tools)
	 * @param fileName
	 * @param paramList
	 */
	void updateFromFile (String fileName, LinkedList<InterfaceParam> paramList);
	/**
	 * update the listed parameters From their default values
	 * (Options and options' tools)
	 * @param paramList
	 */
	void updateFromDefault (LinkedList<InterfaceParam> paramList);
	

	
	
	// Tools For Debug
	default void	showOptionName()		{
		System.out.println("Option name = " + optionName());
	}
	default void	showOptionName(String header)	{
		System.out.println(header + " Option name = " + optionName());
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
	/**
	 * TODO BR: will have to be removed
	 * 			Use Save (String fileName) instead, the options should be up to date
	 * Update listed options and save to file
	 * @param fileName
	 * @param paramList
	 */
	void updateOptionsAndSaveToFileName (String fileName, LinkedList<InterfaceParam> paramList);

	/**
	 * TODO BR: will have to be removed, the options should be up to date
	 * updated governor options will have to "call"
	 * @param paramList
	 * @param call
	 */
	void writeModSettingsToOptions(boolean call);
	void writeModSettingsToOptions(LinkedList<InterfaceParam> paramList, boolean call);
	void copyAliensAISettings(IGameOptions dest);
}
