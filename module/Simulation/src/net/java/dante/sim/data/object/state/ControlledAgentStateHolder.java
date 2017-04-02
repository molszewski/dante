/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.state;


/**
 * Class holding data defined in {@link ControlledAgentState} interface, allowing
 * to modify number of current agent's hit points.
 * 
 * @author M.Olszewski
 */
public class ControlledAgentStateHolder extends AgentStateHolder
    implements ControlledAgentState
{
  /** Agent's hit points. */
  private int hitPoints;


  /**
   * Creates instance of {@link ControlledAgentStateHolder} class holding data 
   * specified in {@link ControlledAgentState} interface.
   *
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   */
  public ControlledAgentStateHolder(double agentSightRange, 
      int agentMaxHitPoints, double agentMaxSpeed)
  {
    super(agentSightRange, agentMaxHitPoints, agentMaxSpeed);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#getCurrentHitPoints()
   */
  public int getCurrentHitPoints()
  {
    return hitPoints;
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#setCurrentHitPoints(int)
   */
  public void setCurrentHitPoints(int currentHitPoints)
  {
    if (currentHitPoints <= getMaxHitPoints())
    {
      hitPoints = currentHitPoints;
    }
    else
    {
      hitPoints = getMaxHitPoints();
    }
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; hitPoints=" + hitPoints + "]");
  }
}