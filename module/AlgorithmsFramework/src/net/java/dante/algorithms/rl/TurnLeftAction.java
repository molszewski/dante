/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

/**
 * Changes agent's movement direction to left.
 *
 * @author M.Olszewski
 */
public class TurnLeftAction extends RFAction
{
  private static final long serialVersionUID = 1L;


  /**
   * Creates instance of {@link TurnLeftAction} class.
   *
   * @param learningAgent learning agent.
   */
  public TurnLeftAction(RFAgent learningAgent)
  {
    super(learningAgent);
  }


  /**
   * @see pl.gdan.elsy.qconf.Action#execute()
   */
  @Override
  public int execute()
  {
    return getAgent().turnLeft();
  }
}