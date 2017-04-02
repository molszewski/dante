/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.object.ChangeableData;
import net.java.dante.sim.data.object.ClientWeaponSystem;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.FriendlyAgentState;
import net.java.dante.sim.data.object.state.FriendlyAgentStateHolder;
import net.java.dante.sim.data.object.state.ObjectBaseState;
import net.java.dante.sim.data.object.state.ObjectBaseStateHolder;

/**
 * Agent's state available on client side for all agents controlled by client.
 *
 * @author M.Olszewski 
 */
public class ClientFriendlyAgentState implements ObjectBaseState, FriendlyAgentState, Changeable
{
  /** Number of parameters that can be changed during updating state. */
  private static final int CHANGEABLE_PARAMETERS_COUNT = 2;
  /** Index in changes record for 'x' coordinate. */
  private static final int POSITION_X_INDEX = 0;
  /** Index in changes record for 'y' coordinate. */
  private static final int POSITION_Y_INDEX = 1;
  
  /** Reference to object holding basic information about this agent (coordinates, speeds). */
  private ObjectBaseStateHolder baseHolder;
  /** 
   * Reference to object holding specific information about this agent 
   * sight range, weapon system, maximum speed etc.). 
   */
  private FriendlyAgentStateHolder agentHolder;
  /** Record with changed parameters since last update. */
  private ChangeableData changesRecord = new ChangeableData(CHANGEABLE_PARAMETERS_COUNT);
  
  
  /**
   * Creates object of {@link ClientFriendlyAgentState} with specified type.
   * 
   * @param agentType - name of agent's type.
   * @param agentSightRange - agent's sight range.
   * @param weaponSystem - agent's weapon system.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   * @param agentSize - agent's size.
   */
  public ClientFriendlyAgentState(String agentType, double agentSightRange,  
      int agentMaxHitPoints, double agentMaxSpeed, ObjectSize agentSize, 
      ClientWeaponSystem weaponSystem)
  {
    baseHolder = new ObjectBaseStateHolder(agentType, agentSize);
    agentHolder = new FriendlyAgentStateHolder(agentSightRange, 
        agentMaxHitPoints, agentMaxSpeed, weaponSystem);
  }
  
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getType()
   */
  public String getType()
  {
    return baseHolder.getType();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getX()
   */
  public double getX()
  {
    return baseHolder.getX();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setX(double)
   */
  public void setX(double newX)
  {
    if (newX != baseHolder.getX())
    {
      changesRecord.markAsChanged(POSITION_X_INDEX);
      baseHolder.setX(newX);
    }
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getY()
   */
  public double getY()
  {
    return baseHolder.getY();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setY(double)
   */
  public void setY(double newY)
  {
    if (newY != baseHolder.getY())
    {
      changesRecord.markAsChanged(POSITION_Y_INDEX);
      baseHolder.setY(newY);
    }
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSize()
   */
  public ObjectSize getSize()
  {
    return baseHolder.getSize();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedX()
   */
  public double getSpeedX()
  {
    return baseHolder.getSpeedX();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedX(double)
   */
  public void setSpeedX(double newSpeedX)
  {
    baseHolder.setSpeedX(newSpeedX);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedY()
   */
  public double getSpeedY()
  {
    return baseHolder.getSpeedY();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedY(double)
   */
  public void setSpeedY(double newSpeedY)
  {
    baseHolder.setSpeedY(newSpeedY);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#getSightRange()
   */
  public double getSightRange()
  {
    return agentHolder.getSightRange();
  }

  /**
   * @see net.java.dante.sim.data.object.state.FriendlyAgentState#getWeapon()
   */
  public ClientWeaponSystem getWeapon()
  {
    return agentHolder.getWeapon();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#getCurrentHitPoints()
   */
  public int getCurrentHitPoints()
  {
    return agentHolder.getCurrentHitPoints();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#setCurrentHitPoints(int)
   */
  public void setCurrentHitPoints(int currentHitPoints)
  {
    agentHolder.setCurrentHitPoints(currentHitPoints);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#getMaxHitPoints()
   */
  public int getMaxHitPoints()
  {
    return agentHolder.getMaxHitPoints();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return agentHolder.getMaxSpeed();
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.Changeable#changesUpdated()
   */
  public void changesUpdated()
  {
    changesRecord.updateCompleted();
  }
  
  /**
   * Checks whether 'x' or 'y' coordinates parameters have been changed
   * since last update.
   * 
   * @return Returns <code>true</code> if 'x' or 'y' coordinates parameters
   *         have been changed since last update, <code>false</code> otherwise.
   */
  public boolean isPositionChanged()
  {
    return (changesRecord.isChanged(POSITION_X_INDEX) || 
            changesRecord.isChanged(POSITION_Y_INDEX));
  }

  /** 
   * @see net.java.dante.sim.data.object.state.Changeable#isChanged()
   */
  public boolean isChanged()
  {
    return changesRecord.isAnythingChanged();
  }
  
  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[base=" + baseHolder + "; agent=" + agentHolder + "]"); 
  }
}