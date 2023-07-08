package rotp.model.galaxy;

public abstract class FleetBase {
  private static final long serialVersionUID = 1L;
  private float arrivalTime = Float.MAX_VALUE;

  public float arrivalTime() { return arrivalTime; }
  abstract float calculateArrivalTime();
  public void setArrivalTime() {
    arrivalTime = calculateArrivalTime();
  }
}
