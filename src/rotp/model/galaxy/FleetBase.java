package rotp.model.galaxy;

public abstract class FleetBase {
  private static final long serialVersionUID = 1L;
  private float arrivalTime = Float.MAX_VALUE;

  private transient boolean displayed;

  public float arrivalTime() { return arrivalTime; }
  protected abstract float calculateArrivalTime();
  public void setArrivalTime() {
    arrivalTime = calculateArrivalTime();
  }

  public boolean displayed() { return displayed; }
  protected abstract boolean decideWhetherDisplayed();
  public void setDisplayed() {
    displayed = decideWhetherDisplayed();
  }
}
