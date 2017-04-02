/*
 * Created on 2006-07-15
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data;

import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.settings.SimulationSettings;

/**
 * Class representing global data of {@link net.java.dante.sim.Simulation} implementations.
 *
 * @author M.Olszewski
 */
public class GlobalData
{
  /** Indicates whether battle is recorded. */
  private boolean recordBattle;
  /** Indicates whether battle will be evaluated. */
  private boolean evaluateBattle;
  /** Duration of battle, in milliseconds. */
  private long battleDuration;
  /** Interval between two updates, in milliseconds. */
  private long updatesInterval;
  /** Interval between sending update messages, in milliseconds. */
  private long sendUpdateMessagesInterval;
  /** Real number of groups taking part in battle. */
  private int groupsCount;

  /** File with background's image. */
  private String backgroundImageFile;
  /** File with map's tile image. */
  private String tileImageFile;

  /** Size of agent's commands queue. */
  private int agentQueueSize;
  /** Agent's movement animation. */
  private AnimationFramesData agentMoveAnim;
  /** Agent's explosion animation. */
  private AnimationFramesData agentExplosionAnim;
  /** Projectile's movement animation. */
  private AnimationFramesData projectileMoveAnim;
  /** Projectile's explosion animation. */
  private AnimationFramesData projectileExplosionAnim;

  /** Map tile's size. */
  private ObjectSize mapTileSize;


  /**
   * Creates instance of {@link GlobalData} with specified arguments.
   *
   * @param settings - {@link SimulationSettings} object containing all data
   *        read from files.
   * @param agentsGroupsCount - real number of players taking part in battle.
   */
  GlobalData(SimulationSettings settings, int agentsGroupsCount)
  {
    if (settings == null)
    {
      throw new NullPointerException("Specified settings is null!");
    }
    if (agentsGroupsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument groupsCountData - it must be positive integer!");
    }

    setParameters(settings.isRecordBattle(),
                  settings.isEvaluateBattle(),
                  settings.getBattleDuration(),
                  settings.getUpdatesInterval(),
                  settings.getSendUpdateMessagesInterval(),
                  agentsGroupsCount,
                  settings.getBackgroundImageFile(),
                  settings.getTileImageFile(),
                  settings.getAgentQueueMaxSize(),
                  settings.getAgentMoveAnimation(),
                  settings.getAgentExplosionAnimation(),
                  settings.getProjectileMoveAnimation(),
                  settings.getProjectileExplosionAnimation(),
                  settings.getMapTileSize());
  }

  /**
   * Creates instance of {@link GlobalData} with specified arguments.
   *
   * @param battleRecorded - indicates whether battle will be recorded or not.
   * @param battleEvaluated - indicates whether battle will be evaluated or not.
   * @param durationOfBattle - maximum duration of battle.
   * @param intervalBetweenUpdates - interval between two updates, in milliseconds.
   * @param intervalBetweenSendingUpdateMessages - interval between sending update messages, in milliseconds.
   * @param agentsGroupsCount - real number of players taking part in battle.
   * @param backgroundImageFileName - the name of file with map's tile image.
   * @param tileImageFileName - the file with map's tile image.
   * @param agentCommandsQueueMaxSize - the agent's commands queue size.
   * @param agentMoveAnimationData - the agent's movement animation data.
   * @param agentExplosionAnimationData - the agent's explosion animation data.
   * @param projectileMoveAnimationData - the projectile's movement animation data.
   * @param projectileExplosionAnimationData - the projectile's explosion animation data.
   * @param sizeOfMapTile - map tile's size.
   */
  public GlobalData(boolean             battleRecorded,
                    boolean             battleEvaluated,
                    long                durationOfBattle,
                    long                intervalBetweenUpdates,
                    long                intervalBetweenSendingUpdateMessages,
                    int                 agentsGroupsCount,
                    String              backgroundImageFileName,
                    String              tileImageFileName,
                    int                 agentCommandsQueueMaxSize,
                    AnimationFramesData agentMoveAnimationData,
                    AnimationFramesData agentExplosionAnimationData,
                    AnimationFramesData projectileMoveAnimationData,
                    AnimationFramesData projectileExplosionAnimationData,
                    ObjectSize          sizeOfMapTile)
  {
    setParameters(battleRecorded,
                  battleEvaluated,
                  durationOfBattle,
                  intervalBetweenUpdates,
                  intervalBetweenSendingUpdateMessages,
                  agentsGroupsCount,
                  backgroundImageFileName,
                  tileImageFileName,
                  agentCommandsQueueMaxSize,
                  agentMoveAnimationData,
                  agentExplosionAnimationData,
                  projectileMoveAnimationData,
                  projectileExplosionAnimationData,
                  sizeOfMapTile);
  }


  /**
   * Sets all parameters of this {@link GlobalData} object.
   *
   * @param battleRecorded - indicates whether battle will be recorded or not.
   * @param battleEvaluated - indicates whether battle will be evaluated or not.
   * @param durationOfBattle - maximum duration of battle.
   * @param intervalBetweenUpdates - interval between two updates, in milliseconds.
   * @param intervalBetweenSendingUpdateMessages - interval between sending update messages, in milliseconds.
   * @param agentsGroupsCount - real number of players taking part in battle.
   * @param backgroundImageFileName - the name of file with map's tile image.
   * @param tileImageFileName - the file with map's tile image.
   * @param agentCommandsQueueMaxSize - the agent's commands queue size.
   * @param agentMoveAnimationData - the agent's movement animation data.
   * @param agentExplosionAnimationData - the agent's explosion animation data.
   * @param projectileMoveAnimationData - the projectile's movement animation data.
   * @param projectileExplosionAnimationData - the projectile's explosion animation data.
   * @param sizeOfMapTile - map tile's size.
   */
  private void setParameters(boolean             battleRecorded,
                             boolean             battleEvaluated,
                             long                durationOfBattle,
                             long                intervalBetweenUpdates,
                             long                intervalBetweenSendingUpdateMessages,
                             int                 agentsGroupsCount,
                             String              backgroundImageFileName,
                             String              tileImageFileName,
                             int                 agentCommandsQueueMaxSize,
                             AnimationFramesData agentMoveAnimationData,
                             AnimationFramesData agentExplosionAnimationData,
                             AnimationFramesData projectileMoveAnimationData,
                             AnimationFramesData projectileExplosionAnimationData,
                             ObjectSize          sizeOfMapTile)
  {
    if (backgroundImageFileName == null)
    {
      throw new NullPointerException("Specified backgroundImageFileName is null!");
    }
    if (tileImageFileName == null)
    {
      throw new NullPointerException("Specified tileImageFileName is null!");
    }
    if (agentMoveAnimationData == null)
    {
      throw new NullPointerException("Specified agentMoveAnimationData is null!");
    }
    if (agentExplosionAnimationData == null)
    {
      throw new NullPointerException("Specified agentExplosionAnimationData is null!");
    }
    if (projectileMoveAnimationData == null)
    {
      throw new NullPointerException("Specified projectileMoveAnimationData is null!");
    }
    if (projectileExplosionAnimationData == null)
    {
      throw new NullPointerException("Specified projectileExplosionAnimationData is null!");
    }
    if (sizeOfMapTile == null)
    {
      throw new NullPointerException("Specified sizeOfMapTile is null!");
    }
    if (agentsGroupsCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument groupsCountData - it must be positive integer!");
    }
    if (agentCommandsQueueMaxSize <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentCommandsQueueSize - it must be positive integer!");
    }
    if (durationOfBattle <= 0)
    {
      throw new IllegalArgumentException("Invalid argument durationOfBattle - it must be positive long integer!");
    }
    if (intervalBetweenUpdates <= 0)
    {
      throw new IllegalArgumentException("Invalid argument intervalBetweenUpdates - it must be positive long integer!");
    }
    if (intervalBetweenSendingUpdateMessages <= 0)
    {
      throw new IllegalArgumentException("Invalid argument intervalBetweenSendingUpdateMessages - it must be positive long integer!");
    }

    recordBattle   = battleRecorded;
    evaluateBattle = battleEvaluated;
    battleDuration = durationOfBattle;
    groupsCount    = agentsGroupsCount;

    sendUpdateMessagesInterval = intervalBetweenSendingUpdateMessages;
    updatesInterval            = intervalBetweenUpdates;

    backgroundImageFile = backgroundImageFileName;
    tileImageFile       = tileImageFileName;

    agentQueueSize          = agentCommandsQueueMaxSize;
    agentMoveAnim           = agentMoveAnimationData;
    agentExplosionAnim      = agentExplosionAnimationData;
    projectileMoveAnim      = projectileMoveAnimationData;
    projectileExplosionAnim = projectileExplosionAnimationData;

    mapTileSize    = sizeOfMapTile;
  }

  /**
   * Gets battle's duration.
   *
   * @return Returns battle's duration.
   */
  public long getBattleDuration()
  {
    return battleDuration;
  }

  /**
   * Checks whether battle will be evaluated.
   *
   * @return Returns <code>true</code> if battle will be evaluated, <code>false</code>
   *         otherwise.
   */
  public boolean isEvaluateBattle()
  {
    return evaluateBattle;
  }

  /**
   * Checks whether battle will be recorded.
   *
   * @return Returns <code>true</code> if battle will be recorded, <code>false</code>
   *         otherwise.
   */
  public boolean isRecordBattle()
  {
    return recordBattle;
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
   * Gets the interval between sending update messages.
   *
   * @return Returns the interval between sending update messages.
   */
  public long getSendUpdateMessagesInterval()
  {
    return sendUpdateMessagesInterval;
  }

  /**
   * Gets real number of groups taking part in the battle.
   *
   * @return Returns real number of groups taking part in the battle.
   */
  public int getGroupsCount()
  {
    return groupsCount;
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
   * Gets maximum size of agent's commands queue.
   *
   * @return Returns maximum size of agent's commands queue.
   */
  public int getAgentQueueMaxSize()
  {
    return agentQueueSize;
  }

  /**
   * Gets the agent's movement animation data.
   *
   * @return Returns the agent's movement animation data.
   */
  public AnimationFramesData getAgentMoveAnimation()
  {
    return agentMoveAnim;
  }

  /**
   * Gets the projectile's movement animation data.
   *
   * @return Returns the projectile's movement animation data.
   */
  public AnimationFramesData getProjectileMoveAnimation()
  {
    return projectileMoveAnim;
  }

  /**
   * Gets the agent's explosion animation data.
   *
   * @return Returns the agent's explosion animation data.
   */
  public AnimationFramesData getAgentExplosionAnimation()
  {
    return agentExplosionAnim;
  }

  /**
   * Gets the projectile's explosion animation data.
   *
   * @return Returns the projectile's explosion animation data.
   */
  public AnimationFramesData getProjectileExplosionAnimation()
  {
    return projectileExplosionAnim;
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
   * Gets map tile's size.
   *
   * @return Returns map tile's size.
   */
  public ObjectSize getMapTileSize()
  {
    return mapTileSize;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + (recordBattle ? 1231 : 1237);
    result = PRIME * result + (evaluateBattle ? 1231 : 1237);
    result = PRIME * result + (int) (battleDuration ^ (battleDuration >>> 32));
    result = PRIME * result + (int) (updatesInterval ^ (updatesInterval >>> 32));
    result = PRIME * result + (int) (sendUpdateMessagesInterval ^ (sendUpdateMessagesInterval >>> 32));
    result = PRIME * result + backgroundImageFile.hashCode();
    result = PRIME * result + tileImageFile.hashCode();
    result = PRIME * result + groupsCount;
    result = PRIME * result + agentMoveAnim.hashCode();
    result = PRIME * result + agentExplosionAnim.hashCode();
    result = PRIME * result + projectileMoveAnim.hashCode();
    result = PRIME * result + projectileExplosionAnim.hashCode();
    result = PRIME * result + mapTileSize.hashCode();
    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GlobalData))
    {
      final GlobalData other = (GlobalData) object;
      equal = ((recordBattle == other.recordBattle) &&
               (evaluateBattle == other.evaluateBattle) &&
               (battleDuration == other.battleDuration) &&
               (updatesInterval == other.updatesInterval) &&
               (sendUpdateMessagesInterval == other.sendUpdateMessagesInterval) &&
               (groupsCount == other.groupsCount) &&
               (backgroundImageFile.equals(other.backgroundImageFile)) &&
               (tileImageFile.equals(other.tileImageFile)) &&
               (agentMoveAnim.equals(other.agentMoveAnim)) &&
               (agentExplosionAnim.equals(other.agentExplosionAnim)) &&
               (projectileMoveAnim.equals(other.projectileMoveAnim)) &&
               (projectileExplosionAnim.equals(other.projectileExplosionAnim)) &&
               (mapTileSize.equals(other.mapTileSize)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[recordBattle=" + recordBattle +
        "; evaluateBattle=" + evaluateBattle +
        "; battleDuration=" + battleDuration +
        "; updatesInterval=" + updatesInterval +
        "; sendUpdateMessagesInterval=" + sendUpdateMessagesInterval +
        "; groupsCount=" + groupsCount +
        "; backgroundImageFile=" + backgroundImageFile +
        "; tileImageFile=" + tileImageFile +
        "; agentMoveAnim=" + agentMoveAnim +
        "; agentExplosionAnim=" + agentExplosionAnim +
        "; projectileMoveAnim=" + projectileMoveAnim +
        "; projectileExplosionAnim=" + projectileExplosionAnim +
        "; mapTileSize=" + mapTileSize +"]");
  }
}