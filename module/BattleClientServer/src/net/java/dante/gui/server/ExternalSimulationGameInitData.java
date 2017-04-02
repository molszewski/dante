/*
 * Created on 2006-09-03
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;


import net.java.dante.gui.common.games.GameInitData;
import net.java.dante.receiver.Receiver;

/**
 * External initialization data for simulation.
 *
 * @author M.Olszewski
 */
public class ExternalSimulationGameInitData implements GameInitData
{
  /** Messages receiver. */
  private Receiver  receiver;
  /** File with simulation definitions. */
  private String    definitionFile;


  /**
   * Creates instance of {@link SimulationGameInitData} class with the
   * specified parameters.
   *
   * @param messagesReceiver - messages receiver.
   * @param definitionFileName - name of file with simulation definitions.
   */
  ExternalSimulationGameInitData(Receiver  messagesReceiver,
                                 String    definitionFileName)
  {
    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }
    if (definitionFileName == null)
    {
      throw new NullPointerException("Specified definitionFileName is null!");
    }
    if (definitionFileName.length() < 0)
    {
      throw new IllegalArgumentException("Invalid argument definitionFileName - it cannot be an empty string!");
    }

    receiver       = messagesReceiver;
    definitionFile = definitionFileName;
  }


  /**
   * Gets the name of file with simulation definitions.
   *
   * @return Returns the name of file with simulation definitions.
   */
  public String getDefinitionFile()
  {
    return definitionFile;
  }

  /**
   * Gets the messages receiver.
   *
   * @return Returns the messages receiver.
   */
  public Receiver getReceiver()
  {
    return receiver;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + definitionFile.hashCode();
    result = PRIME * result + receiver.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ExternalSimulationGameInitData))
    {
      final ExternalSimulationGameInitData other = (ExternalSimulationGameInitData) object;
      equal = ((definitionFile.equals(other.definitionFile)) &&
               (receiver.equals(other.receiver)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[definitionFile=" + definitionFile +
        "; receiver=" + receiver + "]");
  }
}