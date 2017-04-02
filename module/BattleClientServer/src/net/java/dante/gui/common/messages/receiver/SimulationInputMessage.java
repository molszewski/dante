/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;


import net.java.dante.gui.common.games.GameInputData;
import net.java.dante.receiver.ReceiverMessage;
import net.java.dante.sim.io.InputData;

/**
 * Class wrapping {@link net.java.dante.sim.io.InputData} object
 * ({@link net.java.dante.sim.Simulation} output) for the game with 
 * specified identifier.
 *
 * @author M.Olszewski
 */
public class SimulationInputMessage implements ReceiverMessage, GameInputData
{
  /** Game's identifier for which input data was generated. */
  private Integer gameId;
  /** Input data for simulation denoted by the specified identifier. */
  private InputData data;


  /**
   * Creates instance of {@link SimulationInputMessage} class with the specified
   * parameters.
   *
   * @param gameIdentifier - game's identifier for which input data was generated.
   * @param inputData - input data for simulation denoted the by
   *        the specified identifier.
   */
  public SimulationInputMessage(int gameIdentifier, InputData inputData)
  {
    if (inputData == null)
    {
      throw new NullPointerException("Specified inputData is null!");
    }

    gameId = Integer.valueOf(gameIdentifier);
    data   = inputData;
  }


  /**
   * @see net.java.dante.gui.common.games.GameInputData#getGameId()
   */
  public Integer getGameId()
  {
    return gameId;
  }

  /**
   * Gets input data wrapped by this message.
   *
   * @return Returns input data wrapped by this message.
   */
  public InputData getInputData()
  {
    return data;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + data.hashCode();
    result = PRIME * result + gameId.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof SimulationInputMessage))
    {
      final SimulationInputMessage other = (SimulationInputMessage) object;
      equal = ((gameId.equals(other.gameId)) &&
               (data.equals(other.data)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[gameId=" + gameId + "; data=" + data + "]");
  }
}