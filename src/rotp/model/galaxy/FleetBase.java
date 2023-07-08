package rotp.model.galaxy;

import java.awt.Rectangle;
import rotp.ui.main.GalaxyMapPanel;
import rotp.util.Base;

public abstract class FleetBase implements Base {
  private static final long serialVersionUID = 1L;
  private float arrivalTime = Float.MAX_VALUE;

  private transient boolean displayed;
  private transient Rectangle selectBox;
  private transient boolean hovering;

  public float arrivalTime() {
    if (arrivalTime == Float.MAX_VALUE) {
      throw new RuntimeException("Something has gone terribly wrong: setArrivalTime() was never called, or it returned Float.MAX_VALUE.");
    }
    return arrivalTime;
  }
  protected abstract float calculateArrivalTime();
  public void setArrivalTime() {
    arrivalTime = calculateArrivalTime();
  }

  public boolean displayed() { return displayed; }
  protected abstract boolean decideWhetherDisplayed(GalaxyMapPanel map);
  public void setDisplayed(GalaxyMapPanel map) {
    displayed = decideWhetherDisplayed(map);
  }

  public Rectangle selectBox() {
    if (selectBox == null)
      selectBox = new Rectangle();
    return selectBox;
  }

  public boolean hovering()                   { return hovering; }
  public void hovering(boolean b)             { hovering = b; }
}
