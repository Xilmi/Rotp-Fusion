package rotp.model.game;

import java.util.Arrays;
import java.util.LinkedList;

import rotp.ui.util.IParam;
import rotp.ui.util.ParamBoolean;
import rotp.ui.util.ParamInteger;
import rotp.ui.util.ParamSubUI;
import rotp.ui.util.ParamTitle;

public interface ISystemsOptions extends IBaseOptsTools {

	ParamInteger artifactPlanetMult		= new ParamInteger( MOD_UI, "ARTIFACT_MULT", 100)
			.setLimits(0, 10000)
			.setIncrements(5, 50, 200);
	ParamInteger artifactPlanetOffset	= new ParamInteger( MOD_UI, "ARTIFACT_OFFSET", 0)
			.setLimits(-100, 100)
			.setIncrements(1, 5, 20);
	default float	artifactPlanetProb(float base)	{
		float r = (float) (0.01 * (base * artifactPlanetMult.get() + artifactPlanetOffset.get()));
		// System.out.println("artifactPlanetProb from " + base + " to " + r);
		return r;
	}

	ParamInteger orionPlanetProb		= new ParamInteger( MOD_UI, "ARTIFACT_ORION", 0)
			.setLimits(0, 100)
			.setIncrements(1, 5, 20);
	default float	orionPlanetProb()	{ return 0.01f * orionPlanetProb.get(); }

	ParamInteger ultraPoorPlanetMult	= new ParamInteger( MOD_UI, "ULTRA_POOR_MULT", 100)
			.setLimits(0, 10000)
			.setIncrements(5, 50, 200);
	ParamInteger ultraPoorPlanetOffset	= new ParamInteger( MOD_UI, "ULTRA_POOR_OFFSET", 0)
			.setLimits(-100, 100)
			.setIncrements(1, 5, 20);
	default float	ultraPoorPlanetProb(float base)	{
		float r = (float) (0.01 * (base * ultraPoorPlanetMult.get() + ultraPoorPlanetOffset.get()));
		// System.out.println("ultraPoorPlanetProb from " + base + " to " + r);
		return r;
	}

	ParamInteger poorPlanetMult			= new ParamInteger( MOD_UI, "POOR_MULT", 100)
			.setLimits(0, 10000)
			.setIncrements(5, 50, 200);
	ParamInteger poorPlanetOffset		= new ParamInteger( MOD_UI, "POOR_OFFSET", 0)
			.setLimits(-100, 100)
			.setIncrements(1, 5, 20);
	default float	poorPlanetProb(float base)	{
		float r = (float) (0.01 * (base * poorPlanetMult.get() + poorPlanetOffset.get()));
		// System.out.println("poorPlanetProb from " + base + " to " + r);
		return r;
	}

	ParamInteger richPlanetMult			= new ParamInteger( MOD_UI, "RICH_MULT", 100)
			.setLimits(0, 10000)
			.setIncrements(5, 50, 200);
	ParamInteger richPlanetOffset		= new ParamInteger( MOD_UI, "RICH_OFFSET", 0)
			.setLimits(-100, 100)
			.setIncrements(1, 5, 20);
	default float	richPlanetProb(float base)	{
		float r = (float) (0.01 * (base * richPlanetMult.get() + richPlanetOffset.get()));
		// System.out.println("richPlanetProb from " + base + " to " + r);
		return r;
	}

	ParamInteger ultraRichPlanetMult	= new ParamInteger( MOD_UI, "ULTRA_RICH_MULT", 100)
			.setLimits(0, 10000)
			.setIncrements(5, 50, 200);
	ParamInteger ultraRichPlanetOffset	= new ParamInteger( MOD_UI, "ULTRA_RICH_OFFSET", 0)
			.setLimits(-100, 100)
			.setIncrements(1, 5, 20);
	default float	ultraRichPlanetProb(float base)	{
		float r = (float) (0.01 * (base * ultraRichPlanetMult.get() + ultraRichPlanetOffset.get()));
		// System.out.println("ultraRichPlanetProb from " + base + " to " + r);
		return r;
	}

	ParamBoolean allowRichPoorArtifact	= new ParamBoolean( MOD_UI, "RICH_POOR_ARTIFACT", false);
	default boolean allowRichPoorArtifact()		{ return allowRichPoorArtifact.get(); }

	ParamInteger orionToEmpireModifier	= new ParamInteger( MOD_UI, "ORION_TO_EMPIRE_MODIFIER", 100)
			.setLimits(0, 1000)
			.setIncrements(1, 10, 100);
	default float	orionToEmpireModifier()	{ return (float) (0.01 * orionToEmpireModifier.get()); }
	
	// ==================== GUI List Declarations ====================
	//

	static LinkedList<IParam> systemsOptions() {
		return IBaseOptsTools.getSingleList(systemsOptionsMap());
	}
	static LinkedList<LinkedList<IParam>> systemsOptionsMap()	{
		LinkedList<LinkedList<IParam>> map = new LinkedList<>();
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("ARTIFACT_OPTIONS"),
				artifactPlanetMult, artifactPlanetOffset,
				headerSpacer,
				orionPlanetProb,
				headerSpacer,
				allowRichPoorArtifact
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("RICH_OPTIONS"),
				ultraPoorPlanetMult, ultraPoorPlanetOffset,
				poorPlanetMult, poorPlanetOffset,
				headerSpacer,
				richPlanetMult, richPlanetOffset,
				ultraRichPlanetMult, ultraRichPlanetOffset
				)));
		map.add(new LinkedList<>(Arrays.asList(
				new ParamTitle("GAME_OTHER"),
				orionToEmpireModifier
				)));
		return map;
	};
	String SYSTEMS_GUI_ID	= "SYSTEMS_OPTIONS";
	static ParamSubUI systemsOptionsUI() {
		return new ParamSubUI( MOD_UI, SYSTEMS_GUI_ID, systemsOptionsMap());
	}
	ParamSubUI systemsOptionsUI	= systemsOptionsUI();
}
