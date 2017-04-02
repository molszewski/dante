/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.awt.Container;

import net.java.dante.gui.common.games.GameInitData;
import net.java.dante.receiver.Receiver;

/**
 * Initialization data for game running simulation.
 *
 * @author M.Olszewski
 */
class SimulationGameInitData implements GameInitData
{
  /** External initialization data. */
  ExternalSimulationGameInitData initData;
  /** Parent container. */
  private Container parent;


  /**
   * Creates instance of {@link SimulationGameInitData} class with the
   * specified parameters.
   *
   * @param externalInitData - external initialization data.
   * @param parentContainer - parent container.
   */
  SimulationGameInitData(ExternalSimulationGameInitData externalInitData,
                         Container parentContainer)
  {
    if (externalInitData == null)
    {
      throw new NullPointerException("Specified externalInitData is null!");
    }
    if (parentContainer == null)
    {
      throw new NullPointerException("Specified parentContainer is null!");
    }

    initData = externalInitData;
    parent   = parentContainer;
  }


  /**
   * Gets the name of file with simulation definitions.
   *
   * @return Returns the name of file with simulation definitions.
   */
  public String getDefinitionFile()
  {
    return initData.getDefinitionFile();
  }

  /**
   * Gets the parent container.
   *
   * @return Returns the parent container.
   */
  public Container getParent()
  {
    return parent;
  }

  /**
   * Gets the messages receiver.
   *
   * @return Returns the messages receiver.
   */
  public Receiver getReceiver()
  {
    return initData.getReceiver();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + initData.hashCode();
    result = PRIME * result + parent.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof SimulationGameInitData))
    {
      final SimulationGameInitData other = (SimulationGameInitData) object;
      equal = ((initData.equals(other.initData)) &&
               (parent.equals(other.parent)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[externalInitData=" + initData +
        "; parent=" + parent + "]");
  }
}