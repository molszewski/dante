/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

/**
 * Moves forward.
 *
 * @author M.Olszewski
 */
public class MoveForwardAction extends RFAction
{
  private static final long serialVersionUID = 1L;


  /**
   * Creates instance of {@link MoveForwardAction} class.
   *
   * @param learningAgent learning agent.
   */
  public MoveForwardAction(RFAgent learningAgent)
  {
    super(learningAgent);
  }


  /**
   * @see pl.gdan.elsy.qconf.Action#execute()
   */
  @Override
  public int execute()
  {
    return getAgent().moveForward();
  }
}