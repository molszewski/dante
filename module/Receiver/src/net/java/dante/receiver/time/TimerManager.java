/*
 * Created on 2006-09-02
 *
 * @author M.Olszewski
 */

package net.java.dante.receiver.time;

import java.util.Date;
import java.util.TimerTask;

import net.java.dante.receiver.Receiver;



/**
 * Timer manager, creating implementation of {@link Timer} interface that
 * sends {@link TimerMessage} messages when they goes off.
 *
 * @author M.Olszewski
 */
public class TimerManager
{
  /** The only existing instance of {@link TimerManager}. */
  private static final TimerManager instance = new TimerManager();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private TimerManager()
  {
    // Intentionally left empty.
  }


  /**
   * Gets the only instance of this singleton class.
   *
   * @return Returns the only instance of this singleton class.
   */
  public static TimerManager getInstance()
  {
    return instance;
  }


  /**
   * Creates timer for the specified receiver and returns handle
   * to it.
   *
   * @param receiver - the specified receiver.
   *
   * @return Returns handle object to the specified timer.
   */
  public Timer createTimer(Receiver receiver)
  {
    return new TimerImpl(receiver);
  }

  /**
   * Implementation of {@link Timer} interface using {@link java.util.Timer}
   * class to perform all tasks.
   *
   * @author M.Olszewski
   */
  class TimerImpl implements Timer
  {
    /** Messages receiver. */
    final Receiver receiver;
    /** Timer used by this implementation. */
    private java.util.Timer extTimer;


    /**
     * Creates instance of {@link TimerImpl} class.
     *
     * @param messagesReceiver - messages receiver.
     */
    public TimerImpl(Receiver messagesReceiver)
    {
      receiver = messagesReceiver;
    }

    /**
     * @see net.java.dante.receiver.time.Timer#scheduleAfter(long)
     */
    public void scheduleAfter(long delay)
    {
      if (extTimer == null)
      {
        extTimer = new java.util.Timer();

        extTimer.schedule(generateTask(), delay);
      }
    }

    /**
     * @see net.java.dante.receiver.time.Timer#scheduleAt(long)
     */
    public void scheduleAt(long delay)
    {
      if (extTimer == null)
      {
        extTimer = new java.util.Timer();

        extTimer.schedule(generateTask(), new Date(delay));
      }
    }

    /**
     * Creates timer task posting message to the specified receiver
     * when timer time went off.
     *
     * @return Returns created timer task.
     */
    private TimerTask generateTask()
    {
      return new TimerTask() {
        /**
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run()
        {
          receiver.postMessage(new TimerMessage(TimerImpl.this));

          TimerImpl.this.cancel();
        }
      };
    }

    /**
     * @see net.java.dante.receiver.time.Timer#cancel()
     */
    public void cancel()
    {
      if (extTimer != null)
      {
        extTimer.cancel();
        extTimer = null;
      }
    }
  }
}