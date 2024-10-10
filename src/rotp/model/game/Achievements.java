package rotp.model.game;

import java.io.Serializable;
import java.util.HashMap;

import rotp.util.Base;

public class Achievements implements Base, Serializable {
	private static final long serialVersionUID = 1L;
	HashMap<Long, GameResult> gameResult;
	
	public void addResult(GameSession session) {
		Long achievementId = session.achievementId();
		GameResult results = new GameResult(session);
		gameResult().put(achievementId, results);
		saveResult();
	}
	public HashMap<Long, GameResult> filterResult(HashMap<Long, GameResult> source) {
		return source;
	}

	private HashMap<Long, GameResult> loadResult() { // TODO BR: Achievements loadResult()
		return null;
	}
	private void saveResult() { // TODO BR: Achievements saveResult()
		
	}
	private HashMap<Long, GameResult> gameResult()	{
		if (gameResult == null)
			gameResult = loadResult();
		return gameResult;
	}
	
	class GameResult implements Serializable {
		private static final long serialVersionUID = 1L;
		// Difficulty parameters
		float difficultyLevel;
		float monstersLevel;
		// Size parameters
		int numberOfStars;
		int numberOfAliens;
		// Other Parameters
		String playerRaceName;
		boolean playerIsCustom;
		// Results
		

		GameResult(GameSession session)	{
			IGameOptions opts = session.options();
			difficultyLevel	= opts.aiProductionModifier();
			monstersLevel	= opts.monstersLevel();
			numberOfStars	= opts.numberStarSystems();
			numberOfAliens	= opts.selectedNumberOpponents();
			playerRaceName	= opts.selectedPlayerRace();
			playerIsCustom	= opts.selectedPlayerIsCustom();
		}
	}
}
