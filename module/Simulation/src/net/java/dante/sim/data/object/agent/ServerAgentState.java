/*
 * Created on 2006-05-02
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.object.ChangeableData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.ServerWeaponSystem;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectBaseState;
import net.java.dante.sim.data.object.state.ObjectBaseStateHolder;
import net.java.dante.sim.data.object.state.OldPositionObjectState;
import net.java.dante.sim.data.object.state.OldPositionObjectStateHolder;
import net.java.dante.sim.data.object.state.ServerControlledAgentState;
import net.java.dante.sim.data.object.state.ServerControlledAgentStateHolder;


/**
 * Agent's state - available on server side for all agents.
 *
 * @author M.Olszewski
 */
public class ServerAgentState implements ObjectBaseState, OldPositionObjectState,
    ServerControlledAgentState, Changeable
{
  /** Number of parameters that can be changed during updating state. */
  private static final int CHANGEABLE_PARAMETERS_COUNT = 2;
  /** Index in changes record for 'x' coordinate. */
  private static final int POSITION_X_INDEX = 0;
  /** Index in changes record for 'y' coordinate. */
  private static final int POSITION_Y_INDEX = 1;

  /** Reference to object holding basic information about this agent (coordinates, speeds). */
  private ObjectBaseState baseHolder;
  /**
   * Reference to object holding basic information about old coordinates of
   * this agent.
   */
  private OldPositionObjectState oldPositionHolder;
  /** Reference to object holding specific information about this agent (range, damage etc.). */
  private ServerControlledAgentStateHolder agentHolder;
  /** Record with changed parameters since last update. */
  private ChangeableData changesRecord = new ChangeableData(CHANGEABLE_PARAMETERS_COUNT);


  /**
   * Creates object of {@link ServerAgentState} with specified type.
   *
   * @param agentType - name of agent's type.
   * @param agentSightRange - agent's sight range.
   * @param agentMaxHitPoints - agent's max hit points.
   * @param agentMaxSpeed - agent's max speed.
   * @param agentSize - agent's size.
   * @param weaponSystem - agent's weapon system.
   */
  public ServerAgentState(String agentType, double agentSightRange,
      int agentMaxHitPoints, double agentMaxSpeed, ObjectSize agentSize,
      ServerWeaponSystem weaponSystem)
  {
    baseHolder = new ObjectBaseStateHolder(agentType, agentSize);
    oldPositionHolder = new OldPositionObjectStateHolder();
    agentHolder = new ServerControlledAgentStateHolder(agentSightRange,
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
      baseHolder.setX(newX);
      changesRecord.markAsChanged(POSITION_X_INDEX);
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
      baseHolder.setY(newY);
      changesRecord.markAsChanged(POSITION_Y_INDEX);
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
    if (newSpeedX != baseHolder.getSpeedX())
    {
      baseHolder.setSpeedX(newSpeedX);
    }
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
    if (newSpeedY != baseHolder.getSpeedY())
    {
      baseHolder.setSpeedY(newSpeedY);
    }
  }

  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#getOldX()
   */
  public double getOldX()
  {
    return oldPositionHolder.getOldX();
  }

  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#setOldX(double)
   */
  public void setOldX(double newOldX)
  {
    oldPositionHolder.setOldX(newOldX);
  }

  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#getOldY()
   */
  public double getOldY()
  {
    return oldPositionHolder.getOldY();
  }

  /**
   * @see net.java.dante.sim.data.object.state.OldPositionObjectState#setOldY(double)
   */
  public void setOldY(double newOldY)
  {
    oldPositionHolder.setOldY(newOldY);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledAgentState#getSightRange()
   */
  public double getSightRange()
  {
    return agentHolder.getSightRange();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ServerControlledAgentState#getWeapon()
   */
  public ServerWeaponSystem getWeapon()
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
    if (currentHitPoints != agentHolder.getCurrentHitPoints())
    {
      agentHolder.setCurrentHitPoints(currentHitPoints);
    }
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
   * Checks whether any parameter has been changed since last update.
   *
   * @return Returns <code>true</code> if any parameter has been changed since
   *         last update, <code>false</code> otherwise.
   */
  public boolean isChanged()
  {
    return changesRecord.isAnythingChanged();
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
   * Checks whether 'x' coordinate parameter has been changed since last update.
   *
   * @return Returns <code>true</code> if 'x' coordinate parameter
   *         has been changed since last update, <code>false</code> otherwise.
   */
  public boolean isChangedX()
  {
    return changesRecord.isChanged(POSITION_X_INDEX);
  }

  /**
   * Checks whether 'y' coordinate parameter has been changed since last update.
   *
   * @return Returns <code>true</code> if 'y' coordinate parameter
   *         has been changed since last update, <code>false</code> otherwise.
   */
  public boolean isChangedY()
  {
    return changesRecord.isChanged(POSITION_Y_INDEX);
  }

  /**
   * Reverses current agent's coordinates to old ones.
   */
  public void reversePosition()
  {
    baseHolder.setX(oldPositionHolder.getOldX());
    baseHolder.setY(oldPositionHolder.getOldY());
    changesRecord.unmarkAsChanged(POSITION_X_INDEX);
    changesRecord.unmarkAsChanged(POSITION_Y_INDEX);
  }

  /**
   * @see net.java.dante.sim.data.object.state.Changeable#changesUpdated()
   */
  public void changesUpdated()
  {
    changesRecord.updateCompleted();
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[base=" + baseHolder + "; oldPosition=" +
        oldPositionHolder + "; agent=" + agentHolder +
        "; changes=" + changesRecord + "]");
  }
}