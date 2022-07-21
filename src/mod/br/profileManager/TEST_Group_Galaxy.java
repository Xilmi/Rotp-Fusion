package mod.br.profileManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import mod.br.profileManager.Group_Galaxy.GalaxyShape;

class TEST_Group_Galaxy {
	private TEST_ClientClasses cct = new TEST_ClientClasses();
	
	private String galaxyShapePrt = 
			"\r\n"
			+ "¦==== Parameter : GALAXY SHAPE\r\n"
			+ "; Options       : [Rectangle, Ellipse, Spiral, Text, Cluster, Swirlclusters,\r\n"
			+ ";   \" \"         : Grid, Spiralarms, Maze, Shuriken, Bullseye, Lorenz, Fractal]\r\n"
			+ "¦ History       : Current: Rectangle ¦ Last:  ¦ Initial: Rectangle\r\n"
			+ "¦ History       : Default: Rectangle ¦ Game: \r\n"
			+ "¦ LOCAL ENABLE  : All         ; [No, All, Save, Load, Hide, ·]\r\n"
			+ "\r\n"
			+ "Profile 1       :\r\n"
			+ "Random          :\r\n"
			+ "\r\n"
			+ "";
	
	@Test void GalaxyShape_ClientClasses() {
		GalaxyShape param;
		param = new GalaxyShape(cct);
		String out = param.toString(List.of("Profile 1", "Random"));
		String shouldBe = galaxyShapePrt;
		assertEquals(shouldBe, out, "should have been equals");
	}

}
