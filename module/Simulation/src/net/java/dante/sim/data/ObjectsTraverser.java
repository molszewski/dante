/*
 * Created on 2006-07-14
 *
 * @author M.Olszewski 
 */


package net.java.dante.sim.data;

import net.java.dante.sim.data.object.SimulationObject;

/**
 * Abstract class defining interface for subclasses providing algorithm for
 * traversing all {@link net.java.dante.sim.data.object.SimulationObject} instances
 * stored inside {@link net.java.dante.sim.data.SimulationData} implementation.
 * 
 * @author M.Olszewski
 */
public abstract class ObjectsTraverser
{
  /** Instance of default implementation. */
  private static ObjectsTraverser defaultImpl = new ObjectsTraverserImpl();
  
  
  /**
   * Protected default constructor - only for inheritance.
   */
  protected ObjectsTraverser()
  {
    // Intentionally left empty.
  }

  
  /**
   * Gets default implementation of {@link ObjectsTraverser}.
   * 
   * @return Returns default implementation of {@link ObjectsTraverser}.
   */
  public static ObjectsTraverser getDefault()
  {
    return defaultImpl;
  }
  
  /**
   * Performs tree traverse procedure, executing actions for each tree node 
   * before traversing further and after traversing through all the children.
   * 
   * @param startObject - object from which traverse should be started.
   * @param action - action listener, callback object.
   */
  public abstract void traverse(SimulationObject startObject, ActionListener action);
}