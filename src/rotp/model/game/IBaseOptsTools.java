package rotp.model.game;

import java.util.LinkedList;
import rotp.ui.util.IParam;
import rotp.ui.util.ParamTitle;

public interface IBaseOptsTools {
	String BASE_UI				= "SETUP_";
	String GAME_UI				= "GAME_SETTINGS_";
	String ADV_UI				= "SETTINGS_";
	String MOD_UI				= "SETTINGS_MOD_";
	String HEADERS				= "HEADERS_";	
	String OPTIONFILE_EXTENSION	= ".options";
	String GAME_OPTIONS_FILE	= "Game" + OPTIONFILE_EXTENSION;
	String LAST_OPTIONS_FILE	= "Last" + OPTIONFILE_EXTENSION;
	String LIVE_OPTIONS_FILE	= "Live" + OPTIONFILE_EXTENSION;
	String USER_OPTIONS_FILE	= "User" + OPTIONFILE_EXTENSION;
	String NEGATIVE_DISABLED	= "SETTINGS_MOD_NEGATIVE_DISABLED";
	// To be able to identify the current options
	int UNKNOWN_ID = 0;
	int GAME_ID  = 1;
	int SETUP_ID = 2;

	ParamTitle headerSpacer = new ParamTitle("SPACER");

	static LinkedList<IParam> getSingleList(LinkedList<LinkedList<IParam>> listList) {
		LinkedList<IParam> paramList = new LinkedList<>();
		for ( LinkedList<IParam> list : listList ) {
			for (IParam param : list) {
				if (param != null && !param.isTitle())
					paramList.add(param);
			}
		}

		return paramList;
	}
}
