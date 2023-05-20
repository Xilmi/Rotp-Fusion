package rotp.model.game;

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
	int id();
	void id(int id);
	DynOptions dynOpts();

	ParamTitle headerSpacer = new ParamTitle("SPACER");

}
