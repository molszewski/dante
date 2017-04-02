/*
 * Created on 2006-09-02
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver.time;

import net.java.dante.receiver.ReceiverMessage;

/**
 * Timer message, containing reference to a timer which fired
 * it.
 *
 * @author M.Olszewski
 */
public class TimerMessage implements ReceiverMessage
{
  /** Fired timer. */
  private Timer timer;


  /**
   * Creates instance of {@link TimerMessage} class.
   *
   * @param firedTimer - timer which fired this message.
   */
  TimerMessage(Timer firedTimer)
  {
    if (firedTimer == null)
    {
      throw new NullPointerException("Specified firedTimer is null!");
    }

    timer = firedTimer;
  }


  /**
   * Gets timer which fired this message.
   *
   * @return Returns timer which fired this message.
   */
  public Timer getTimer()
  {
    return timer;
  }
}