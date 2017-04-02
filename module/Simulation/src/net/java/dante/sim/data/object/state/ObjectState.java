/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Common interface for data of all {@link net.java.dante.sim.data.object.SimulationObject} 
 * subclasses.
 *
 * @author M.Olszewski
 */
public interface ObjectState
{
  /**
   * Gets name of object's type.
   * 
   * @return Returns name of object's type.
   */
  String getType();
}