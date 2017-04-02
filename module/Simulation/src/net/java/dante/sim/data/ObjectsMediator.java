/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.object.SimulationObject;

/**
 * Interface for classes that mediates between external environment and
 * some {@link net.java.dante.sim.data.SimulationData} object to which mediator is bound.
 * Mediator's methods are called at the moment of simulation objects
 * creation or destruction and notifies {@link net.java.dante.sim.data.SimulationData} bound
 * object about this events.
 *
 * @author M.Olszewski
 */
public interface ObjectsMediator
{
  /**
   * This method should be invoked when {@link SimulationObject}
   * was destroyed externally and main {@link net.java.dante.sim.data.SimulationData} 
   * object should be notified about this event.
   * 
   * @param object - object that was destroyed.
   */
  void objectDestroyed(SimulationObject object);
  
  /**
   * This method should be invoked when new {@link SimulationObject}
   * was created externally and main {@link net.java.dante.sim.data.SimulationData} 
   * object should be notified about this event.
   * 
   * @param object - object that was created.
   */
  void objectCreated(SimulationObject object);
}