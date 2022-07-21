package mod.br.profileManager;

import static mod.br.AddOns.StarsOptions.STAR_TYPES;
import static mod.br.AddOns.StarsOptions.probabilityModifier;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import mod.br.profileManager.Group_BrokenRegistry.BaseProbabilityModifier;

class TEST_Group_BrokenRegistry {
	private TEST_ClientClasses cct = new TEST_ClientClasses();
	
	private String StarProbabilityPrt = 
			"\r\n"
			+ "¦==== Parameter : STAR TYPE PROBABILITY\r\n"
			+ "; Options       : [Red, Orange, Yellow, Blue, White, Purple]\r\n"
			+ "¦ History       : Current: 1.0/1.0/1.0/1.0/1.0/1.0 ¦ Last: \r\n"
			+ "¦ History       : Initial: 1.0/1.0/1.0/1.0/1.0/1.0\r\n"
			+ "¦ History       : Default: 1.0/1.0/1.0/1.0/1.0/1.0 ¦ Game: \r\n"
			+ "¦ LOCAL ENABLE  : All         ; [No, All, Save, Load, Hide, ·]\r\n"
			+ "\r\n"
			+ "Profile 1       :\r\n"
			+ "Random          :\r\n"
			+ "\r\n"
			+ "";

	@Test void StarProbability_ClientClasses() {
		BaseProbabilityModifier param;
		param = new BaseProbabilityModifier(cct, "STAR TYPE PROBABILITY"
				, probabilityModifier("STARS"), STAR_TYPES, "");
		String out = param.toString(List.of("Profile 1", "Random"));
		String shouldBe = StarProbabilityPrt;
		assertEquals(shouldBe, out, "should have been equals");
	}

	@Test void getParameter_String() {
		String param;
		Group_BrokenRegistry group;
		group = new Group_BrokenRegistry(cct);
		param = "STAR TYPE PROBABILITY";
		assertEquals(param,
				group.getParameter(param).getParameterName(),
				"should have retrieved the parameter name");
	}

}
