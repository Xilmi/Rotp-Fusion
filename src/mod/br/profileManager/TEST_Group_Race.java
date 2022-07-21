package mod.br.profileManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import mod.br.profileManager.Group_Race.PlayerColor;
import mod.br.profileManager.Group_Race.PlayerRace;

class TEST_Group_Race {
	private TEST_ClientClasses cct = new TEST_ClientClasses();
	
	private String racePrt = 
			"\r\n"
			+ "¦==== Parameter : PLAYER RACE\r\n"
			+ "; Options       : [Human, Alkari, Silicoid, Mrrshan, Klackon, Meklar, Psilon,\r\n"
			+ ";   \" \"         : Darlok, Sakkra, Bulrathi]\r\n"
			+ "¦ History       : Current: Human ¦ Last:  ¦ Initial: Human ¦ Default: Human\r\n"
			+ "¦ History       : Game: \r\n"
			+ "¦ LOCAL ENABLE  : All         ; [No, All, Save, Load, Hide, ·]\r\n"
			+ "\r\n"
			+ "Profile 1       :\r\n"
			+ "Random          :\r\n"
			+ "; ---- Available for changes in game saves\r\n"
			+ "\r\n"
			+ "";

	private String colorPrt = 
			"\r\n"
			+ "¦==== Parameter : PLAYER COLOR\r\n"
			+ "; Options       : [Red, Green, Yellow, Blue, Orange, Purple, Aqua, Fuchsia,\r\n"
			+ ";   \" \"         : Brown, White, Lime, Grey, Plum, Light Blue, Mint, Olive]\r\n"
			+ "¦ History       : Current: Blue ¦ Last:  ¦ Initial: Blue ¦ Default: Red\r\n"
			+ "¦ History       : Game: \r\n"
			+ "¦ LOCAL ENABLE  : All         ; [No, All, Save, Load, Hide, ·]\r\n"
			+ "\r\n"
			+ "Profile 1       :\r\n"
			+ "Random          :\r\n"
			+ "; ---- Available for changes in game saves\r\n"
			+ "\r\n"
			+ "";
	
	@Test void PlayerRace_ClientClasses() {
		PlayerRace param;
		param = new PlayerRace(cct);
		String out = param.toString(List.of("Profile 1", "Random"));
		String shouldBe = racePrt;
		assertEquals(shouldBe, out, "should have been equals");
	}

	@Test void PlayerColor_ClientClasses() {
		PlayerColor param;
		param = new PlayerColor(cct);
		String out = param.toString(List.of("Profile 1", "Random"));
		String shouldBe = colorPrt;
		assertEquals(shouldBe, out, "should have been equals");
	}

	@Test void profileList_None() {
		Group_Race group;
		group = new Group_Race(cct);
		assertEquals("[PLAYER RACE, PLAYER COLOR, PLAYER HOMEWORLD, PLAYER NAME]",
				group.profileList().toString(),
				"should have been \"[PLAYER RACE, PLAYER COLOR]\"");
	}

	@Test void getParameter_String() {
		String param;
		Group_Race group;
		group = new Group_Race(cct);
		param = "PLAYER RACE";
		assertEquals(param,
				group.getParameter(param).getParameterName(),
				"should have retrieved the parameter name");
		param = "PLAYER COLOR";
		assertEquals(param,
				group.getParameter(param).getParameterName(),
				"should have retrieved the parameter name");
	}

}
