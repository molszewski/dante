/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.client;

import java.awt.Container;

import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.engine.engine2d.Engine2dInitData;


/**
 * Class containing external settings for 
 * {@link net.java.dante.sim.engine.engine2d.client.ClientEngine2d} object.
 *
 * @author M.Olszewski
 */
public class ClientEngine2dInitData extends Engine2dInitData
{
  /** Objects creator. */
  private ObjectsCreator creator;
  /** Friendly agents group's identifier. */
  private int groupId;
  
  
  /**
   * Creates instance of {@link ClientEngine2dInitData} with specified parameters.
   *
   * @param parentContainer - parent's container for engine's graphics context.
   * @param globalData - global data.
   * @param agentsGroupId - identifier of this group.
   * @param objectsCreator - objects creator.
   */
  public ClientEngine2dInitData(Container parentContainer, 
      GlobalData globalData, int agentsGroupId, ObjectsCreator objectsCreator)
  {
    super(parentContainer, globalData);
    
    if (objectsCreator == null)
    {
      throw new NullPointerException("Specified objectsCreator is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }
    
    groupId = agentsGroupId;
    creator = objectsCreator;
  }

  
  /**
   * Gets identifier of this agents group.
   * 
   * @return Returns identifier of this agents group.
   */
  public int getGroupId()
  {
    return groupId;
  }
  
  /**
   * Gets the objects creator.
   * 
   * @return Returns the objects creator.
   */
  public ObjectsCreator getCreator()
  {
    return creator;
  }
}