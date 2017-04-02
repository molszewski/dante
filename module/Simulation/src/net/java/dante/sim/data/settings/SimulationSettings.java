/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.settings;

import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.data.object.ObjectSize;


/**
 * Class represents settings of {@link net.java.dante.sim.Simulation} read from file
 * with main attributes. This class objects are immutable, so they can be
 * used safely in different threads.
 *
 * @author M.Olszewski
 */
public class SimulationSettings implements Settings
{
  /** Minimum duration of battle in milliseconds. */
  public static final long MINIMUM_BATTLE_DURATION_MS = 60000;

  // Main settings:
  /** Indicates whether battle will be recorded. */
  private boolean recordBattle;
  /** Indicates whether battle will be evaluated. */
  private boolean evaluateBattle;
  /** Duration of battle, in milliseconds. */
  private long battleDuration;
  /** Interval between two updates, in milliseconds. */
  private long updatesInterval;
  /** Interval between sending update messages, in milliseconds. */
  private long sendUpdateMessagesInterval;


  // Map settings:
  /** File with battlefield's map definition. */
  private String mapFile;
  /** File with background's image. */
  private String backgroundImageFile;
  /** File with map's tile image. */
  private String tileImageFile;
  /** Size of battlefield map's tile. */
  private ObjectSize mapTileSize;

  // Agents settings:
  /** File with agent's description. */
  private String agentFile;
  /** Size of agent's commands queue. */
  private int agentQueueSize;
  /** Agent's movement animation. */
  private AnimationFramesData agentMoveAnimation;
  /** Agent's explosion animation. */
  private AnimationFramesData agentExplosionAnimation;

  // Projectiles settings:
  /** Projectile's movement animation. */
  private AnimationFramesData projectileMoveAnimation;
  /** Projectile's explosion animation. */
  private AnimationFramesData projectileExplosionAnimation;



  /**
   * Default constructor with package access.
   */
  SimulationSettings()
  {
    // Intentionally left empty.
  }


  /**
   * Checks whether battle will be recorded.
   *
   * @return Returns <code>true</code> if battle will be recorded or
   *         <code>false</code> otherwise.
   */
  public boolean isRecordBattle()
  {
    return recordBattle;
  }

  /**
   * Sets setting indicating whether battle will be recorded or not to specified
   * value.
   *
   * @param battleRecorded - indicates whether battle will be recorded or not.
   */
  void setRecordBattle(boolean battleRecorded)
  {
    recordBattle = battleRecorded;
  }

  /**
   * Checks whether battle will be evaluated.
   *
   * @return Returns <code>true</code> if battle will be evaluated or
   *         <code>false</code> otherwise.
   */
  public boolean isEvaluateBattle()
  {
    return evaluateBattle;
  }

  /**
   * Sets setting indicating whether battle will be evaluated or not to specified
   * value.
   *
   * @param battleEvaluated - indicates whether battle will be evaluated or not.
   */
  void setEvaluateBattle(boolean battleEvaluated)
  {
    evaluateBattle = battleEvaluated;
  }

  /**
   * Gets the duration of battle
   *
   * @return Returns the duration of battle.
   */
  public long getBattleDuration()
  {
    return battleDuration;
  }

  /**
   * Sets maximum duration of battle to specified value.
   *
   * @param durationOfBattle - indicates maximum duration of battle.
   */
  void setBattleDuration(long durationOfBattle)
  {
    if (durationOfBattle < 0)
    {
      throw new IllegalArgumentException("Invalid argument durationOfBattle - it must be positive long integer or zero!");
    }

    battleDuration = durationOfBattle;
  }

  /**
   * Gets the interval between two updates.
   *
   * @return Returns the interval between two updates.
   */
  public long getUpdatesInterval()
  {
    return updatesInterval;
  }

  /**
   * Sets interval between two updates.
   *
   * @param newUpdatesInterval - indicates interval between two updates.
   */
  void setUpdatesInterval(long newUpdatesInterval)
  {
    if (newUpdatesInterval <= 0)
    {
      throw new IllegalArgumentException("Invalid argument newUpdatesInterval - it must be positive long integer!");
    }

    updatesInterval = newUpdatesInterval;
  }

  /**
   * Gets the interval between sending update messages.
   *
   * @return Returns the interval between sending update messages.
   */
  public long getSendUpdateMessagesInterval()
  {
    return sendUpdateMessagesInterval;
  }

  /**
   * Sets interval between sending update messages.
   *
   * @param newSendUpdateMessagesInterval - indicates interval between sending update messages.
   */
  void setSendUpdateMessagesInterval(long newSendUpdateMessagesInterval)
  {
    if (newSendUpdateMessagesInterval <= 0)
    {
      throw new IllegalArgumentException("Invalid argument newSendUpdateMessagesInterval - it must be positive long integer!");
    }

    sendUpdateMessagesInterval = newSendUpdateMessagesInterval;
  }

  /**
   * Gets the file with battlefield's definition.
   *
   * @return Returns the file with battlefield's definition.
   */
  public String getMapFile()
  {
    return mapFile;
  }

  /**
   * Sets the name of file with map's definition.
   *
   * @param mapFileName - the file name of map's definition.
   */
  void setMapFile(String mapFileName)
  {
     mapFile = mapFileName;
  }

  /**
   * Gets the name of file with background's image.
   *
   * @return Returns the file with background's image.
   */
  public String getBackgroundImageFile()
  {
    return backgroundImageFile;
  }

  /**
   * Sets the name of file with map's tile image.
   *
   * @param backgroundImageFileName - the name of file with map's tile image.
   */
  void setBackgroundImageFile(String backgroundImageFileName)
  {
    backgroundImageFile = backgroundImageFileName;
  }

  /**
   * Gets the name of file with map's tile image.
   *
   * @return Returns the file with map's tile image.
   */
  public String getTileImageFile()
  {
    return tileImageFile;
  }

  /**
   * Sets the name of file with map's tile image.
   *
   * @param tileImageFileName - the file with map's tile image.
   */
  void setTileImageFile(String tileImageFileName)
  {
    tileImageFile = tileImageFileName;
  }

  /**
   * Gets map tile's size.
   *
   * @return Returns map tile's size.
   */
  public ObjectSize getMapTileSize()
  {
    return mapTileSize;
  }

  /**
   * Sets map tile's size.
   *
   * @param newMapTileSize - new map tile's size.
   */
  void setMapTileSize(ObjectSize newMapTileSize)
  {
    mapTileSize = newMapTileSize;
  }

  /**
   * Gets the name of file with agent's definition.
   *
   * @return Returns the name of file with agent's definition.
   */
  public String getAgentFile()
  {
    return agentFile;
  }

  /**
   * Sets the name of file with agent's definition.
   *
   * @param agentFileName - the name of file with agent's definition.
   */
  void setAgentFile(String agentFileName)
  {
    agentFile = agentFileName;
  }

  /**
   * Gets size of agent's commands queue.
   *
   * @return Returns size of agent's commands queue.
   */
  public int getAgentQueueMaxSize()
  {
    return agentQueueSize;
  }

  /**
   * Sets the size of agent's commands queue.
   *
   * @param agentCommandsQueueSize - size of agent's commands queue.
   */
  void setAgentQueueMaxSize(int agentCommandsQueueSize)
  {
    agentQueueSize = agentCommandsQueueSize;
  }

  /**
   * Gets the agent's movement animation data.
   *
   * @return Returns the agent's movement animation data.
   */
  public AnimationFramesData getAgentMoveAnimation()
  {
    return agentMoveAnimation;
  }

  /**
   * Sets the agent's movement animation data.
   *
   * @param agentMoveAnimationData - the agent's movement animation data.
   */
  void setAgentMoveAnimation(AnimationFramesData agentMoveAnimationData)
  {
    agentMoveAnimation = agentMoveAnimationData;
  }

  /**
   * Gets the projectile's movement animation data.
   *
   * @return Returns the projectile's movement animation data.
   */
  public AnimationFramesData getProjectileMoveAnimation()
  {
    return projectileMoveAnimation;
  }

  /**
   * Sets the projectile's movement animation data.
   *
   * @param projectileMoveAnimationData - the projectile's movement animation data.
   */
  void setProjectileMoveAnimation(AnimationFramesData projectileMoveAnimationData)
  {
    projectileMoveAnimation = projectileMoveAnimationData;
  }

  /**
   * Gets the agent's explosion animation data.
   *
   * @return Returns the agent's explosion animation data.
   */
  public AnimationFramesData getAgentExplosionAnimation()
  {
    return agentExplosionAnimation;
  }

  /**
   * Sets the agent's explosion animation data.
   *
   * @param agentExplosionAnimationData - the agent's explosion animation data.
   */
  void setAgentExplosionAnimation(AnimationFramesData agentExplosionAnimationData)
  {
    agentExplosionAnimation = agentExplosionAnimationData;
  }

  /**
   * Gets the projectile's explosion animation data.
   *
   * @return Returns the projectile's explosion animation data.
   */
  public AnimationFramesData getProjectileExplosionAnimation()
  {
    return projectileExplosionAnimation;
  }

  /**
   * Sets the projectile's explosion animation data.
   *
   * @param projectileExplosionAnimationData - the projectile's explosion animation data.
   */
  void setProjectileExplosionAnimation(AnimationFramesData projectileExplosionAnimationData)
  {
    projectileExplosionAnimation = projectileExplosionAnimationData;
  }
}