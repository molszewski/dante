/*
 * Created on 2006-09-03
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.receiver;


import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.receiver.ReceiverMessage;

/**
 * Message sent when algorithm class was selected.
 *
 * @author M.Olszewski
 */
public class AlgorithmSelectedMessage implements ReceiverMessage
{
  /** Selected algorithm. */
  private BaseAlgorithmImpl algorithm;


  /**
   * Creates instance of {@link AlgorithmSelectedMessage} class.
   *
   * @param selectedAlgorithm selected algorithm.
   */
  public AlgorithmSelectedMessage(BaseAlgorithmImpl selectedAlgorithm)
  {
    if (selectedAlgorithm == null)
    {
      throw new NullPointerException("Specified selectedAlgorithm is null!");
    }

    algorithm = selectedAlgorithm;
  }


  /**
   * Gets selected algorithm.
   *
   * @return Returns selected algorithm.
   */
  public BaseAlgorithmImpl getAlgorithm()
  {
    return algorithm;
  }
}