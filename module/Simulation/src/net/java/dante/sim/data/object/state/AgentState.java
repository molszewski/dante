/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski
 */


package net.java.dante.sim.data.object.state;

/**
 * Interface defining set of fundamental agent attributes: sight range,
 * maximum hit points and maximum speed.
 *
 * @author M.Olszewski
 */
public interface AgentState extends ObjectMaxSpeedState
{
  /**
   * Gets object's maximum speed.
   *
   * @return Returns object's maximum speed.
   */
  double getMaxSpeed();

  /**
   * Gets maximum number of controlled agent's hit points.
   *
   * @return Returns maximum number of controlled agent's hit points.
   */
  int getMaxHitPoints();

  /**
   * Gets controlled agent's sight range.
   *
   * @return Returns controlled agent's sight range.
   */
  double getSightRange();
}