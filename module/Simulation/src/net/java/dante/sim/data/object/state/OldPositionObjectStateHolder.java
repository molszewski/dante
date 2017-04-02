/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Class holding data defined in {@link OldPositionObjectState} interface, allowing
 * to modify object's old coordinates.
 * 
 * @author M.Olszewski
 */
public class OldPositionObjectStateHolder implements OldPositionObjectState
{
  /** Object's old 'x' coordinate. */
  private double oldX;
  /** Object's old 'y' coordinate. */
  private double oldY;

  
  /**
   * Creates instance of {@link OldPositionObjectStateHolder} class holding data 
   * specified in {@link OldPositionObjectState} interface.
   */
  public OldPositionObjectStateHolder()
  {
    // Intentionally left empty.
  }

  
  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#getOldX()
   */
  public double getOldX()
  {
    return oldX;
  }
  
  /**
   * Sets object's old 'x' coordinate.
   * 
   * @param newOldX - new value of object's old 'x' coordinate.
   */
  public void setOldX(double newOldX)
  {
    oldX = newOldX;
  }

  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#getOldY()
   */
  public double getOldY()
  {
    return oldY;
  }
  
  /**
   * Sets object's old 'y' coordinate.
   * 
   * @param newOldY - new value of object's old 'y' coordinate.
   */
  public void setOldY(double newOldY)
  {
    oldY = newOldY;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[oldX=" + oldX + "; oldY=" + oldY + "]");
  }
}