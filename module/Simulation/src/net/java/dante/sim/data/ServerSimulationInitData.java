/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data;

import java.awt.Container;

import net.java.dante.sim.data.common.InitData;


/**
 * Initialization data for simulations running on server side.
 *
 * @author M.Olszewski
 */
public class ServerSimulationInitData implements InitData
{
  /** Minimum number of groups taking part in simulation. */
  public final static int MINIMUM_GROUPS_NUMBER = 2;
  
  /** Number of groups taking part in simulation. */
  private int groupsCount;
  /** Parent container for graphical output. */
  private Container parent;
  
  
  /**
   * Creates object of {@link ServerSimulationInitData} with specified arguments.
   * 
   * @param simGroupsCount - number of groups taking part in simulation.
   * @param parentContainer - parent container for graphical output.
   * 
   * @throws IllegalArgumentException if <code>simGroupsCount</code> is lesser
   *         than {@link #MINIMUM_GROUPS_NUMBER}.
   */
  public ServerSimulationInitData(int simGroupsCount, Container parentContainer)
  {
    if (parentContainer == null)
    {
      throw new NullPointerException("Specified parentContainer is null!");
    }
    if (simGroupsCount < MINIMUM_GROUPS_NUMBER)
    {
      throw new IllegalArgumentException("Invalid argument simGroupsCount - it must be equal or greater than " + MINIMUM_GROUPS_NUMBER + "!");
    }
    
    groupsCount = simGroupsCount;
    parent = parentContainer;
  }


  /**
   * Gets number of groups taking part in simulation.
   * 
   * @return Returns number of groups taking part in simulation.
   */
  public int getGroupsCount()
  {
    return groupsCount;
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