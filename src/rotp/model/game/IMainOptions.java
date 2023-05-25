package rotp.model.game;

import static rotp.ui.UserPreferences.AUTOBOMBARD_INVADE;
import static rotp.ui.UserPreferences.AUTOBOMBARD_NEVER;
import static rotp.ui.UserPreferences.AUTOBOMBARD_NO;
import static rotp.ui.UserPreferences.AUTOBOMBARD_WAR;
import static rotp.ui.UserPreferences.AUTOBOMBARD_YES;
import static rotp.ui.UserPreferences.autoBombardMode;
import static rotp.ui.UserPreferences.autoColonize;
import static rotp.ui.UserPreferences.save;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.InterfaceParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamList;

public interface IMainOptions extends BaseOptionsTools {
	
	// ==================== Duplicates for Base Main Settings Options ====================
	//
	ParamBoolean autoColonize_	= new ParamBoolean( // Duplicate Do not add the list
			GAME_UI, "AUTOCOLONIZE", false) {
		{ isDuplicate(true); }
		@Override public Boolean get() { return autoColonize(); }
		@Override public Boolean set(Boolean newValue) {
			autoColonize(newValue);
			save();
			return autoColonize();
		}
	};
	ParamList autoBombard_		= new ParamList( // Duplicate Do not add the list
			GAME_UI, "AUTOBOMBARD",
			Arrays.asList(
					AUTOBOMBARD_NO,
					AUTOBOMBARD_NEVER,
					AUTOBOMBARD_YES,
					AUTOBOMBARD_WAR,
					AUTOBOMBARD_INVADE
					),
			AUTOBOMBARD_NO) {
		{ showFullGuide(true); }
		@Override public String get() { return autoBombardMode(); }
		@Override public String set(String newValue) {
			autoBombardMode(newValue);
			save();
			return autoBombardMode();
		}
	};

	// ==================== GUI List Declarations ====================
	//
	LinkedList<InterfaceParam> mainOptions	  = new LinkedList<>(
			Arrays.asList(
					autoColonize_, autoBombard_
					));

}
