/*
 * Created on 2006-08-17
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import java.awt.Container;

import net.java.dante.sim.data.common.InitData;


/**
 * Initialization data for simulations running on clients side.
 *
 * @author M.Olszewski
 */
public class ClientSimulationInitData implements InitData
{
  /** Parent container for graphical output. */
  private Container parent;
  /** Friendly agents group's identifier. */
  private int groupId;

  
  /**
   * Creates object of {@link ClientSimulationInitData} with specified arguments.
   *
   * @param agentsGroupId - identifier of this group.
   * @param parentContainer - parent container for graphical output.
   */
  public ClientSimulationInitData(int agentsGroupId, Container parentContainer)
  {
    if (parentContainer == null)
    {
      throw new NullPointerException("Specified parentContainer is null!");
    }
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }
    
    groupId = agentsGroupId;
    parent = parentContainer;
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
   * Gets parent container for graphical output.
   * 
   * @return Returns parent container for graphical output.
   */
  public Container getParentContainer()
  {
    return parent;
  }
}