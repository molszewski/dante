/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

/**
 * Attacks some enemy agent.
 *
 * @author M.Olszewski
 */
public class AttackAction extends RFAction
{
  private static final long serialVersionUID = 1L;


  /**
   * Creates instance of {@link AttackAction} class.
   *
   * @param learningAgent learning agent.
   */
  public AttackAction(RFAgent learningAgent)
  {
    super(learningAgent);
  }


  /**
   * @see pl.gdan.elsy.qconf.Action#execute()
   */
  @Override
  public int execute()
  {
    return getAgent().attack();
  }
}