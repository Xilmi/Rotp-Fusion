package rotp.model.game;

import rotp.ui.util.ParamTitle;

public interface IBaseOptsTools {
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

}
