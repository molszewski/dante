/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

/**
 * Changes agent's movement direction to right.
 *
 * @author M.Olszewski
 */
public class TurnRightAction extends RFAction
{
  private static final long serialVersionUID = 1L;


  /**
   * Creates instance of {@link TurnRightAction} class.
   *
   * @param learningAgent learning agent.
   */
  public TurnRightAction(RFAgent learningAgent)
  {
    super(learningAgent);
  }


  /**
   * @see pl.gdan.elsy.qconf.Action#execute()
   */
  @Override
  public int execute()
  {
    return getAgent().turnRight();
  }
}
