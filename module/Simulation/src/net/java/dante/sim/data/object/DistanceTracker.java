/*
 * Created on 2006-07-27
 *
 * @author M.Olszewski
 */


package net.java.dante.sim.data.object;


/**
 * Class responsible for tracking distance that some object has moved.
 * It determines whether destination point was reached or not
 * by taking into consideration elapsing time.
 *
 * @author M.Olszewski
 */
public final class DistanceTracker
{
  /** Total amount of way that is step. */
  private final double totalWay;
  /** Speed of tracked object. */
  private final double objectSpeed;
  /** Time counted so far. */
  private long timeCount;


  /**
   * Creates instance of {@link DistanceTracker} class with specified parameters.
   *
   * @param trackedObjectSpeed - speed of an object.
   * @param beginX - start 'x' coordinate.
   * @param beginY - start 'y' coordinate.
   * @param endX - end 'x' coordinate.
   * @param endY - end 'y' coordinate.
   */
  public DistanceTracker(double trackedObjectSpeed, double beginX, double beginY,
      double endX, double endY)
  {
    double differenceX = endX - beginX;
    double differenceY = endY - beginY;

    totalWay = Math.sqrt((differenceX * differenceX) + (differenceY * differenceY));
    // Store speed in [pixels/millisecond]
    objectSpeed = trackedObjectSpeed / 1000.0;
  }

  /**
   * Creates instance of {@link DistanceTracker} class with specified parameters.
   *
   * @param trackedObjectSpeed - speed of an object.
   * @param beginX - start 'x' coordinate.
   * @param beginY - start 'y' coordinate.
   * @param objectTotalWay - total way that object should move.
   */
  public DistanceTracker(double trackedObjectSpeed, double beginX, double beginY,
      double objectTotalWay)
  {
    totalWay = objectTotalWay;
    // Store speed in [pixels/millisecond]
    objectSpeed = trackedObjectSpeed / 1000.0;
  }


  /**
   * Checks whether destination point is reached.
   *
   * @param elapsedTime - time elapsed between two updates.
   *
   * @return Returns <code>true</code> if destination point was reached,
   *         <code>false</code> otherwise.
   */
  public boolean isDestinationReached(long elapsedTime)
  {
    timeCount += elapsedTime;
    return isReached();
  }

  /**
   * Reverses time that has passed by specified delta and checks whether
   * destination point was reached.
   *
   * @param delta - time for which we do reversal procedure.
   *
   * @return Returns <code>true</code> if destination point was reached,
   *         <code>false</code> otherwise.
   */
  public boolean reverse(long delta)
  {
    timeCount -= delta;
    return isReached();
  }

  /**
   * Checks whether destination point is reached.
   *
   * @return Returns <code>true</code> if destination point was reached,
   *         <code>false</code> otherwise.
   */
  private boolean isReached()
  {
    return ((timeCount * objectSpeed) > totalWay);
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[objectSpeed=" + objectSpeed + "; totalWay=" + totalWay + "]");
  }
}