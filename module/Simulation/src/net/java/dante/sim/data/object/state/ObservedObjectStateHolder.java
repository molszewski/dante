/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Class holding data defined in {@link ObservedObjectState} interface, allowing
 * to modify visibility and operational statuses.
 *
 * @author M.Olszewski
 */
public class ObservedObjectStateHolder implements ObservedObjectState
{
  /** Visibility status of observed object. */
  private boolean inRange;
  /** 
   * Operational status of observed object: it is set to <code>true</code>
   * by default.
   */
  private boolean alive = true;
  

  /**
   * Creates instance of {@link ObservedObjectStateHolder} class with specified
   * parameters.
   * 
   * @param visibilityStatus - initial value of visibility status.
   */
  public ObservedObjectStateHolder(boolean visibilityStatus)
  {
    inRange = visibilityStatus;
  }
  

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isAlive()
   */
  public boolean isAlive()
  {
    return alive;
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#destroyed()
   */
  public void destroyed()
  {
    alive = false;
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isVisible()
   */
  public boolean isVisible()
  {
    return inRange;
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#inSightRange()
   */
  public void inSightRange()
  {
    inRange = true;
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#outOfSightRange()
   */
  public void outOfSightRange()
  {
    inRange = false;
  }
  
  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[inSightRange=" + inRange + "; alive=" + alive + "]");
  }
}