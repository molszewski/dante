/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski 
 */
package net.java.dante.sim.data.object;

import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.util.RandomIdGenerator;

/**
 * Base class for all simulation's objects.
 * 
 * @author M.Olszewski 
 */
public abstract class SimulationObject
{
  /** Random identifiers generator. */
  private static final RandomIdGenerator idGenerator = new RandomIdGenerator();
  
  /** Pseudo-random unique identifier of an object. */
  private int id;
  /** This object's parent. */
  private SimulationObject parent = null;
  
  
  /**
   * Creates {@link SimulationObject} object with random unique identifier
   * and specified mediator.
   */
  protected SimulationObject()
  {
    if (!idGenerator.canGenerate())
    {
      throw new IllegalStateException("Cannot generate identifier for new object.");
    }
      
    id = idGenerator.generateId();
  }
  
  /**
   * Creates {@link SimulationObject} object with specified identifier.
   * 
   * @param objectId - object's identifier.
   */
  protected SimulationObject(int objectId)
  {
    if (objectId < 0)
    {
      throw new IllegalArgumentException("Invalid argument objectId - it must be an integer greater or equal to zero!");
    }
    
    id = objectId;
  }
  
  
  /**
   * Gets object's identifier.
   * 
   * @return Returns object's identifier.
   */
  public int getId()
  {
    return id;
  }
  
  /**
   * Gets object representing internal data of this {@link SimulationObject} 
   * or <code>null</code> if object does not have internal data.
   * 
   * @return Returns object representing internal data of this 
   *         {@link SimulationObject} or <code>null</code>
   *         if object does not have internal data.
   */
  public abstract ObjectState getData();
  
  /**
   * Gets parent of this {@link SimulationObject} or <code>null</code>
   * if object does not belong to any group.
   * 
   * @return Returns parent of this {@link SimulationObject}.
   */
  public SimulationObject getParent()
  {
    return parent;
  }
  
  /**
   * Sets parent for this object.
   * 
   * @param objectParent - object's parent.
   */
  protected final void setParent(SimulationObject objectParent)
  {
    parent = objectParent;
  }
  
  /**
   * Disposes all resources connected with this object. It should
   * be called whenever object is removed from 
   * {@link net.java.dante.sim.data.SimulationData} instance.<p> 
   * If this method is going to be overridden, it must always call 
   * its super method.
   */
  public void dispose()
  {
    // Dispose identifier
    idGenerator.releaseId(id);
  }
  
  /**
   * Updates state of this {@link SimulationObject} regarding time that has 
   * passed since last update.
   * 
   * @param delta - time that has passed since last update.
   */
  public abstract void update(long delta);
}