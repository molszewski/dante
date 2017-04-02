/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;


/**
 * Interface defining set of controlled agent attributes: sight range,
 * number of current and maximum hit points and maximum speed. 
 * Interface allows to modify number of current agent's hit points.
 * 
 * @author M.Olszewski
 */
public interface ControlledAgentState extends AgentState
{
  /**
   * Gets current number of controlled agent's hit points.
   * 
   * @return Returns current number of controlled agent's hit points.
   */
  int getCurrentHitPoints();
  
  /**
   * Sets number of agent's current hit points. If specified value exceeds
   * agent's maximum hit points (which can be obtained by call to 
   * {@link #getMaxHitPoints()} method), agent's current hit points 
   * must be set to value of maximum hit points.
   * 
   * @param currentHitPoints - new number of agent's current hit points to set.
   */
  void setCurrentHitPoints(int currentHitPoints);
}