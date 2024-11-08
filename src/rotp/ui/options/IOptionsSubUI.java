package rotp.ui.options;

import java.awt.image.BufferedImage;

import rotp.model.game.IModOptions;
import rotp.model.game.SafeListPanel;
import rotp.model.game.SafeListParam;
import rotp.ui.util.ParamSubUI;

public interface IOptionsSubUI extends IModOptions {
	
	//SafeListParam	majorList();
	//default SafeListParam	majorList() { return new SafeListParam(uiMajorKey()); }
	default SafeListParam	majorList() { return null; }
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

	default boolean noPanel()			{ return false; }
	default boolean isCfgFile()			{ return false; }
	default boolean hasExtraParam()		{ return false; }

	default String headId()				{ return MOD_UI; }
	default String uiNameKey()			{ return optionId() + "_UI"; }
	default String uiTitleKey()			{ return optionId() + "_TITLE"; }
	default String uiMajorKey()			{ return optionId() + "_MAJOR"; }
	default String uiCallKey()			{ return optionId() + "_MAJOR2"; }

	default BufferedImage getIcon()		{ return null; }
	default SafeListParam getList()		{ return optionsMap().getList(); }
	default SafeListParam getUiExt()	{
		SafeListParam callList = new SafeListParam(uiCallKey());
		callList.add(getUI());
		callList.addAll(majorList());
		return callList;
	}
	default SafeListPanel optionsMap()	{
		SafeListPanel map = new SafeListPanel(optionId());
		map.add(majorList());
		return map;
	};
}
