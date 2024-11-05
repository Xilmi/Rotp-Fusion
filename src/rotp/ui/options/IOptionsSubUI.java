package rotp.ui.options;

import rotp.model.game.IModOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamSubUI;

public interface IOptionsSubUI extends IModOptions {
	
	SafeListPanel	optionsMap();
	String optionId();

	default ParamSubUI getUI()		{
		return new ParamSubUI(
				headId(),
				uiNameKey(),
				optionsMap(),
				uiTitleKey(),
				optionId()
				).isCfgFile(isCfgFile());
	}
	default boolean noPanel()		{ return false; }
	default boolean isCfgFile()		{ return false; }
	default String headId()			{ return MOD_UI; }
	default SafeListParam getList()	{ return optionsMap().getList(); }
	default String uiNameKey()		{ return optionId() + "_UI"; }
	default String uiTitleKey()		{ return optionId() + "_TITLE"; }
}
