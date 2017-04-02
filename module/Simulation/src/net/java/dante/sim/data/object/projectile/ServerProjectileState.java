/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.object.ChangeableData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ControlledProjectileState;
import net.java.dante.sim.data.object.state.ControlledProjectileStateHolder;
import net.java.dante.sim.data.object.state.ObjectBaseState;
import net.java.dante.sim.data.object.state.ObjectBaseStateHolder;


/**
 * Class containing full information about projectile, that allows to control it
 * - only available on server side.
 *
 * @author M.Olszewski
 */
public class ServerProjectileState implements ObjectBaseState, ControlledProjectileState, Changeable
{
  /** Number of parameters that can be changed during updating state. */
  private static final int CHANGEABLE_PARAMETERS_COUNT = 2;
  /** Index in changes record for 'x' coordinate. */
  private static final int POSITION_X_INDEX = 0;
  /** Index in changes record for 'y' coordinate. */
  private static final int POSITION_Y_INDEX = 1;

  /** Identifier of projectile owner's group. */
  private int ownerGroupId;
  /** Identifier of projectile's owner. */
  private int ownerId;
  /** Reference to object holding basic information about this projectile (coordinates, speeds). */
  private ObjectBaseStateHolder baseHolder;
  /** Reference to object holding specific information about this projectile (range, damage etc.). */
  private ControlledProjectileStateHolder projectileHolder;
  /** Record with changed parameters since last update. */
  private ChangeableData changesRecord = new ChangeableData(CHANGEABLE_PARAMETERS_COUNT);


  /**
   * Creates object of {@link ServerAgentState} with specified type.
   *
   * @param typeName - name of projectile's type.
   * @param projectileOwnerGroupId - identifier of projectile owner's group.
   * @param projectileOwnerId - identifier of projecitle's owner.
   * @param projectileRange - range of the projectile.
   * @param projectileExplosionRange - projectile's explosion range.
   * @param projectileDamage - damage that will be inflicted by this projectile.
   * @param projectileMaxSpeed - projectile's maximum speed.
   * @param projectileSize - projectile's size.
   */
  public ServerProjectileState(String typeName,
                               int projectileOwnerGroupId, int projectileOwnerId,
                               double projectileRange, double projectileExplosionRange,
                               int projectileDamage, double projectileMaxSpeed,
                               ObjectSize projectileSize)
  {
    if (projectileOwnerGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileOwnerGroupId - it must be positive integer or zero!");
    }
    if (projectileOwnerId < 0)
    {
      throw new IllegalArgumentException("Invalid argument projectileOwnerId - it must be positive integer or zero!");
    }

    ownerGroupId = projectileOwnerGroupId;
    ownerId      = projectileOwnerId;
    baseHolder = new ObjectBaseStateHolder(typeName, projectileSize);
    projectileHolder = new ControlledProjectileStateHolder(projectileRange,
        projectileExplosionRange, projectileDamage, projectileMaxSpeed);
  }


  /**
   * @see net.java.dante.sim.data.object.state.ObjectBaseState#getType()
   */
  public String getType()
  {
    return baseHolder.getType();
  }

  /**
   * Gets identifier of group to which belongs owner of this projectile.
   * 'Owner' means here an object which created the projectile.
   *
   * @return Returns the identifier of projectile owner's group.
   */
  public int getOwnerGroupId()
  {
    return ownerGroupId;
  }

  /**
   * Gets the projectile's owner identifier.
   *
   * @return Returns the projectile's owner identifier.
   */
  public int getOwnerId()
  {
    return ownerId;
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
    if (newSpeedY  != baseHolder.getSpeedY())
    {
      baseHolder.setSpeedY(newSpeedY);
    }
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getDistance()
   */
  public double getDistance()
  {
    return projectileHolder.getDistance();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getDamage()
   */
  public int getDamage()
  {
    return projectileHolder.getDamage();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ControlledProjectileState#getExplosionRange()
   */
  public double getExplosionRange()
  {
    return projectileHolder.getExplosionRange();
  }

  /**
   * @see net.java.dante.sim.data.object.state.ObjectMaxSpeedState#getMaxSpeed()
   */
  public double getMaxSpeed()
  {
    return projectileHolder.getMaxSpeed();
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
    return (getClass() + "[base=" + baseHolder + "; projectile=" +
        projectileHolder + "; changes=" + changesRecord + "]");
  }
}