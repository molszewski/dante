/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Minimum nterface for classes which are holding information about changes in their
 * parameters between two updates.
 * 
 * @author M.Olszewski
 */
public interface Changeable
{
  /**
   * Checks whether any of object's parameters has been changed since last update.
   * 
   * @return Returns <code>true</code> if any parameter has been changed since 
   *         last update, <code>false</code> otherwise.
   */
  public boolean isChanged();
  
  /**
   * Updates all changes.
   */
  public void changesUpdated();
}