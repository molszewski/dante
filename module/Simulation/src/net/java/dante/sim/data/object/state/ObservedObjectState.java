/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.state;

/**
 * Interface defining set of attributes for observed object: visibility
 * status (in sight range or not) and operational status (alive or destroyed).
 *
 * @author M.Olszewski
 */
public interface ObservedObjectState
{
  /**
   * Determines whether specified object is alive or destroyed.
   *
   * @return Returns <code>true</code> if object is alive or <code>false</code>
   *         if it is destroyed.
   */
  boolean isAlive();

  /**
   * Sets operational status of object to destroyed. Further calls to
   * {@link #isAlive()} must always return <code>false</code>.
   */
  void destroyed();

  /**
   * Determines whether specified object is in sight range of some agents
   * group.
   *
   * @return Returns <code>true</code> if object is in sight range of some
   *         agents group, <code>false</code> otherwise.
   */
  boolean isVisible();

  /**
   * Sets status of specified object to 'in sight range' of some
   * agents group.
   */
  void inSightRange();

  /**
   * Sets status of specified object to 'out of sight range' of some
   * agents group.
   */
  void outOfSightRange();
}