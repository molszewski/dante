/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.object.SimulationObject;

/**
 * Interface for listeners notified about changes in states of
 * {@link SimulationObject} objects from {@link SimulationData} instance
 * which invokes methods from this listener.<p>
 * There are two possible changes in states: object added
 * or object removed.
 *
 * @author M.Olszewski
 */
public interface ObjectStateChangedListener
{
  /**
   * Method invoked when {@link SimulationObject} object was added
   * to {@link SimulationData} instance which invokes this method.
   *
   * @param object - added object.
   */
  void objectAdded(SimulationObject object);

  /**
   * Method invoked when {@link SimulationObject} object was removed
   * from {@link SimulationData} instance which invokes this method.
   *
   * @param object - removed object.
   */
  void objectRemoved(SimulationObject object);
}