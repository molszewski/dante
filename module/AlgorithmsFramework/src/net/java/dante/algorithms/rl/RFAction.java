/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

import pl.gdan.elsy.qconf.Action;

/**
 * Base class for all actions that can be made by
 * {@link RFAgent} agent.
 *
 * @author M.Olszewski
 */
public abstract class RFAction extends Action
{
  private RFAgent agent;


  /**
   * Creates instance of {@link RFAction} class.
   *
   * @param learningAgent learning agent.
   */
  public RFAction(RFAgent learningAgent)
  {
    if (learningAgent == null)
    {
      throw new NullPointerException("Specified learningAgent is null!");
    }

    agent = learningAgent;
  }


  /**
   * Gets {@link RFAgent} object connected with this action.
   *
   * @return Returns {@link RFAgent} object connected with this action.
   */
  protected final RFAgent getAgent()
  {
    return agent;
  }
}