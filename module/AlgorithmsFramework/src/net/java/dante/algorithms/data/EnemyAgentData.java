/*
 * Created on 2006-08-31
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.data;


/**
 * Interface defining enemy agent's data.
 *
 * @author M.Olszewski
 */
public interface EnemyAgentData extends AgentData
{
  /**
   * Gets number of hits that specified enemy agent received.
   *
   * @return Returns number of hits that specified enemy agent received.
   */
  int getHitsCount();

  /**
   * Determines whether specified object is in sight range of controlled agents.
   *
   * @return Returns <code>true</code> if object is in sight range of controlled
   *         agents, <code>false</code> otherwise.
   */
  boolean isVisible();
}