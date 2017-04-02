/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

/**
 * Interface for classes that are intended to store {@link StartLocation} 
 * objects.
 *  
 * @author M.Olszewski
 */
public interface StartLocationGroup
{
  /**
   * Gets {@link StartLocation} specified by <code>index</code>.
   * 
   * @param index - index of {@link StartLocation} to obtain.
   * 
   * @return Returns {@link StartLocation} specified by <code>index</code>.
   */
  StartLocation getLocation(int index);
  
  /**
   * Gets number of locations stored by this {@link StartLocationGroup}.
   * 
   * @return Returns number of locations stored by this {@link StartLocationGroup}.
   */
  int locationsCount();
}