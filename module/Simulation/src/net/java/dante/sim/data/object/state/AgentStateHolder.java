/*
 * Created on 2006-08-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;

/**
 * Class holding data defined in {@link AgentState} interface.
 * 
 * @author M.Olszewski
 */
public class AgentStateHolder implements AgentState
{
  /** Agent's sight range. */
  private double sightRange;
  /** Agent's maximum hit points. */
  private int maxHitPoints;
  /** Agent's maximum speed. */
  private double maxSpeed;

  
  /**
   * Creates instance of {@link AgentStateHolder} class holding data 
   * specified in {@link AgentState} interface.
   *
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   */
  public AgentStateHolder(double agentSightRange, 
      int agentMaxHitPoints, double agentMaxSpeed)
  {
    if (agentSightRange <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument agentSightRange - it must be positive real number!");
    }
    if (agentMaxSpeed <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument agentMaxSpeed - it must be positive real number!");
    }
    if (agentMaxHitPoints <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentMaxHitPoints - it must be positive integer!");
    }
    
    sightRange = agentSightRange;
    maxHitPoints = agentMaxHitPoints;
    maxSpeed = agentMaxSpeed;
  }

  
  /**
   * @see net.java.dante.sim.data.object.state.AgentState#getSightRange()
   */
  public double getSightRange()
  {
    return sightRange;
  }

  /**
   * @see net.java.dante.sim.data.object.state.AgentState#getMaxHitPoints()
   */
  public int getMaxHitPoints()
  {
    return maxHitPoints;
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return maxSpeed;
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[sightRange=" + sightRange +  
         "; maxHitPoints=" + maxHitPoints + "; maxSpeed=" + maxSpeed + "]");
  }
}