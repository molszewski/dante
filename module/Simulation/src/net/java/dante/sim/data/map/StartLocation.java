/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

/**
 * Interface for classes that are intended to represent start location
 * of objects on the map.
 *
 * @author M.Olszewski
 */
public interface StartLocation
{
  /**
   * Gets row of this start location.
   * 
   * @return Returns row of this start location.
   */
  int getRow();
  
  /**
   * Gets column of this start location.
   * 
   * @return Returns column of this start location.
   */
  int getColumn();
}