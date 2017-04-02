/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.object.ChangeableData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectBaseState;
import net.java.dante.sim.data.object.state.ObjectBaseStateHolder;
import net.java.dante.sim.data.object.state.ObservedProjectileState;
import net.java.dante.sim.data.object.state.ObservedProjectileStateHolder;

/**
 * Class containing only part of projectile state - part available for clients.
 *
 * @author M.Olszewski
 */
public class ClientProjectileState implements ObjectBaseState, ObservedProjectileState, Changeable
{
  /** Number of parameters that can be changed during updating state. */
  private static final int CHANGEABLE_PARAMETERS_COUNT = 2;
  /** Index in changes record for 'x' coordinate. */
  private static final int POSITION_X_INDEX = 0;
  /** Index in changes record for 'y' coordinate. */
  private static final int POSITION_Y_INDEX = 1;
  
  /** Reference to object holding basic information about this projectile (coordinates, speeds). */
  private ObjectBaseState baseState;
  /** Reference to object holding specific information about this projectile (explosion range, maximum speed). */
  private ObservedProjectileState projectileState;
  /** Record with changed parameters since last update. */
  private ChangeableData changesRecord = new ChangeableData(CHANGEABLE_PARAMETERS_COUNT);
  

  /**
   * Creates object of {@link ClientProjectileState} with specified type.
   * 
   * @param typeName - name of projectile's type.
   * @param projectileSize - projectile's size.
   * @param projectileExplosionRange - projectile's explosion range.
   * @param projectileMaxSpeed - projectile's maximum speed.
   */
  public ClientProjectileState(String typeName, ObjectSize projectileSize, 
      double projectileExplosionRange, double projectileMaxSpeed)
  {
    baseState = new ObjectBaseStateHolder(typeName, projectileSize);
    projectileState = new ObservedProjectileStateHolder(
        projectileExplosionRange, projectileMaxSpeed);
  }
  
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getType()
   */
  public String getType()
  {
    return baseState.getType();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getX()
   */
  public double getX()
  {
    return baseState.getX();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setX(double)
   */
  public void setX(double newX)
  {
    if (newX != baseState.getX())
    {
      changesRecord.markAsChanged(POSITION_X_INDEX);
      baseState.setX(newX);
    }
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getY()
   */
  public double getY()
  {
    return baseState.getY();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setY(double)
   */
  public void setY(double newY)
  {
    if (newY != baseState.getY())
    {
      changesRecord.markAsChanged(POSITION_Y_INDEX);
      baseState.setY(newY);
    }
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSize()
   */
  public ObjectSize getSize()
  {
    return baseState.getSize();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedX()
   */
  public double getSpeedX()
  {
    return baseState.getSpeedX();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedX(double)
   */
  public void setSpeedX(double newSpeedX)
  {
    baseState.setSpeedX(newSpeedX);
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getSpeedY()
   */
  public double getSpeedY()
  {
    return baseState.getSpeedY();
  }
  
  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#setSpeedY(double)
   */
  public void setSpeedY(double newSpeedY)
  {
    baseState.setSpeedY(newSpeedY);
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedProjectileState#getExplosionRange()
   */
  public double getExplosionRange()
  {
    return projectileState.getExplosionRange();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return projectileState.getMaxSpeed();
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#isVisible()
   */
  public boolean isVisible()
  {
    return projectileState.isVisible();
  }
  
  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#inSightRange()
   */
  public void inSightRange()
  {
    projectileState.inSightRange();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedObjectState#outOfSightRange()
   */
  public void outOfSightRange()
  {
    projectileState.outOfSightRange();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedProjectileState#destroyed()
   */
  public void destroyed()
  {
    projectileState.destroyed();
  }

  /** 
   * @see net.java.dante.sim.data.object.state.ObservedProjectileState#isAlive()
   */
  public boolean isAlive()
  {
    return projectileState.isAlive();
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
    return (getClass() + "[base=" + baseState + "]");
  }
}