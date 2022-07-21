package mod.br.profileManager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import mod.br.profileManager.Group_Advanced.*;



class TEST_Group_Advanced {
	private TEST_ClientClasses cct = new TEST_ClientClasses();

	
	private String galaxyAgePrt = 
			"\r\n"
			+ "¦==== Parameter : GALAXY AGE\r\n"
			+ "; Options       : [Young, Normal, Old]\r\n"
			+ "¦ History       : Current: Normal ¦ Last:  ¦ Initial: Normal ¦ Default: Normal\r\n"
			+ "¦ History       : Game: \r\n"
			+ "¦ LOCAL ENABLE  : All         ; [No, All, Save, Load, Hide, ·]\r\n"
			+ "\r\n"
			+ "Profile 1       :\r\n"
			+ "Random          :\r\n"
			+ "\r\n"
			+ "";
	
	@Test void GalaxyAge_ClientClasses() {
		GalaxyAge param;
		param = new GalaxyAge(cct);
		String out = param.toString(List.of("Profile 1", "Random"));
		String shouldBe = galaxyAgePrt;
		assertEquals(shouldBe, out, "should have been equals");
	}
}
