/*
 * Created on 2006-07-15
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.common;


/**
 * Class containing data about position in Cartesian system (only 'x' and 'y'
 * coordinates are supported).
 *
 * @author M.Olszewski
 */
public class PositionData
{
  /** 'x' coordinate of start position. */
  private double x;
  /** 'y' coordinate of start position. */
  private double y;
  
  
  /**
   * Creates object of {@link PositionData} with specified starting position.
   * 
   * @param startX - 'x' coordinate of start position.
   * @param startY - 'y' coordinate of start position.
   * 
   * @throws IllegalArgumentException if <code>startX</code>/<code>startY</code> 
   *         are not a positive real numbers.
   */
  public PositionData(double startX, double startY)
  {
    x = startX;
    y = startY;
  }
  
  
  /**
   * Gets 'x' coordinate of start position.
   * 
   * @return Returns 'x' coordinate of start position.
   */
  public double getStartX()
  {
    return x;
  }
  
  /**
   * Gets 'y' coordinate of start position.
   * 
   * @return Returns 'y' coordinate of start position.
   */
  public double getStartY()
  {
    return y;
  }
}