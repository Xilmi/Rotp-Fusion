package rotp.model.galaxy;

import java.awt.Rectangle;
import java.io.Serializable;

import rotp.ui.main.GalaxyMapPanel;

public abstract class FleetBase implements Ship, Serializable {
	private static final long serialVersionUID = 1L;
	private float arrivalTime = Float.MAX_VALUE;

	private transient boolean displayed;
	private transient Rectangle selectBox;
	private transient boolean hovering;

	@Override
	public float arrivalTime() {
		if (arrivalTime == Float.MAX_VALUE) {
	    	throw new RuntimeException("Something has gone terribly wrong: it appears setArrivalTime() was never called.");
	    }
	    return arrivalTime;
	}
	protected abstract float calculateArrivalTime();
	public void setArrivalTime() {
		arrivalTime = calculateArrivalTime();
		if (arrivalTime == Float.MAX_VALUE) {
			throw new RuntimeException("Something has gone terribly wrong: calculateArrivalTime() returned Float.MAX_VALUE.");
		}
	}

	@Override
	public boolean displayed() { return displayed; }
	protected abstract boolean decideWhetherDisplayed(GalaxyMapPanel map);
	@Override
	public void setDisplayed(GalaxyMapPanel map) {
		displayed = decideWhetherDisplayed(map);
	}

	public Rectangle selectBox() {
		if (selectBox == null)
			selectBox = new Rectangle();
		return selectBox;
	}

	@Override
	public boolean hovering()                   { return hovering; }
	@Override
	public void hovering(boolean b)             { hovering = b; }
}
