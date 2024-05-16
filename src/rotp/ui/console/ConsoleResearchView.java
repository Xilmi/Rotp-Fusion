package rotp.ui.console;

import rotp.ui.console.CommandConsole.CommandMenu;

public class ConsoleResearchView implements IConsole {
//	private Empire player;
//	private boolean isPlayer;
	
	void initId(int empId)	{
		
	}
	// ##### ResearchView Command
	CommandMenu initTechMenu(CommandMenu parent)	{
		CommandMenu menu = new CommandMenu("Tech Menu", parent) {
			
		};
		return menu;
	}

}
