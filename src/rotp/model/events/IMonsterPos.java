package rotp.model.events;

import java.awt.Point;
import java.util.HashMap;

public interface IMonsterPos {

	HashMap<Integer, Point.Float> wanderPath();
	Point.Float pos();
	int targetTurnCount();
	boolean notified();
}
