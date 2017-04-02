/*
 * Created on 2006-08-10
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.data.object.projectile.ServerProjectileState;
import net.java.dante.sim.event.types.EventTypesUtils;


/**
 * Visibility manager, updating visibility records and generating
 * proper events.
 *
 * @author M.Olszewski
 */
class VisibilityManager
{
  /** All visibility records registered in this {@link VisibilityManager}. */
  private Set<VisibilityRecord> records = new HashSet<VisibilityRecord>();
  /** Current time holder. */
  private CurrentTimeHolder timeHolder;


  /**
   * Creates instance of {@link VisibilityManager} class.
   *
   * @param currentTimeHolder - current time holder.
   */
  VisibilityManager(CurrentTimeHolder currentTimeHolder)
  {
    if (currentTimeHolder == null)
    {
      throw new NullPointerException("Specified currentTimeHolder is null!");
    }

    timeHolder = currentTimeHolder;
  }


  /**
   * Registers the specified {@link VisibilityRecord} record in
   * this {@link VisibilityManager}.
   *
   * @param record - the specified specified {@link VisibilityRecord} record
   *        to register.
   */
  void registerRecord(VisibilityRecord record)
  {
    if (record == null)
    {
      throw new NullPointerException("Specified record is null!");
    }

    records.add(record);
  }

  /**
   * Unregisters the specified {@link VisibilityRecord} record from
   * this {@link VisibilityManager}. If  the specified {@link VisibilityRecord}
   * record is not registered, this method returns immediately.
   *
   * @param record - the specified specified {@link VisibilityRecord} record
   *        to unregister.
   */
  void unregisterRecord(VisibilityRecord record)
  {
    if (record == null)
    {
      throw new NullPointerException("Specified record is null!");
    }

    records.remove(record);
  }

  /**
   * Initializes visibility records by contents of the specified agents group.
   * It detects still enemy agents and adds proper 'seen' events if necessary.
   *
   * @param group - the specified agents group.
   */
  void initVisibilityRecords(Server2dAgentsGroup group)
  {
    Collection<Server2dAgent> agents = group.getAgents();

    for (VisibilityRecord record : records)
    {
      // Filter records - do not update the same visibility record.
      if (record.getGroupId() != group.getGroupId())
      {
        for (Server2dAgent agent : agents)
        {
          if (record.update(agent))
          {
            generateInitEventsForStillAgent(record, agent);
          }
        }
      }
    }
  }

  private void generateInitEventsForStillAgent(VisibilityRecord record, Server2dAgent stillAgent)
  {
    ServerAgentState state = (ServerAgentState)stillAgent.getAgent().getData();
    int agentId = stillAgent.getAgent().getId();

    if (record.isVisible(stillAgent) && !record.wasVisible(stillAgent))
    {
      record.getEventsBuilder().addEvent(
          EventTypesUtils.createEnemyAgentSeenEvent(
              timeHolder.getCurrentTime(), agentId,
              state.getX(), state.getY(),
              state.getSpeedX(), state.getSpeedY()));
    }
  }

  /**
   * This method should be invoked for each moved agent, after its movement.
   * It updates the specified {@link net.java.dante.sim.engine.engine2d.Engine2dObject}
   * object in all registered visibility records.
   *
   * @param groupId - agent's group identifier.
   * @param movedAgent - the specified agent.
   */
  void updateRecordsByMovedAgent(int groupId, Server2dAgent movedAgent)
  {
    for (VisibilityRecord record : records)
    {
      if ((record.getGroupId() != groupId) &&
          (record.update(movedAgent)))
      {
        generateEventsForMovedAgent(record, movedAgent);
      }
    }
  }

  private void generateEventsForMovedAgent(
      VisibilityRecord record, Server2dAgent movedAgent)
  {
    ServerAgentState state = (ServerAgentState)movedAgent.getAgent().getData();
    int agentId = movedAgent.getAgent().getId();

    if (record.isVisible(movedAgent))
    {
      record.getStatistics().enemyAgentVisible();

      if (record.wasVisible(movedAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentMoveEvent(
                timeHolder.getCurrentTime(), agentId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
      else
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentSeenEvent(
                timeHolder.getCurrentTime(), agentId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
    else
    {
      if (record.wasVisible(movedAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentGoneEvent(
                timeHolder.getCurrentTime(), agentId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
  }

  /**
   * This method should be invoked for each moved projectile, after its movement.
   * It updates the specified {@link Server2dProjectile} object in all registered
   * visibility records.
   *
   * @param movedProjectile - the specified projectile.
   */
  void updateRecordsByMovedProjectile(Server2dProjectile movedProjectile)
  {
    for (VisibilityRecord record : records)
    {
      if (record.update(movedProjectile))
      {
        generateEventsForMovedProjectile(record, movedProjectile);
      }
    }
  }

  private void generateEventsForMovedProjectile(
      VisibilityRecord record, Server2dProjectile movedProjectile)
  {
    ServerProjectileState state = (ServerProjectileState)movedProjectile.getProjectile().getData();
    int projectileId = movedProjectile.getProjectile().getId();

    if (record.isVisible(movedProjectile))
    {
      if (record.wasVisible(movedProjectile))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileMoveEvent(
                timeHolder.getCurrentTime(), projectileId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
      else
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileSeenEvent(
                timeHolder.getCurrentTime(), state.getType(), projectileId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
    else
    {
      if (record.wasVisible(movedProjectile))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileGoneEvent(
                timeHolder.getCurrentTime(), projectileId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
  }

  /**
   * This method should be invoked after movement of all objects.
   * It updates visibility status of all agents from the specified
   * agents group in all visibility records (except the one owned by
   * the specified {@link Server2dAgentsGroup} object).
   *
   * @param group - the specified agents group.
   */
  void updateRecordsForAgentsGroups(Server2dAgentsGroup group)
  {
    Collection<Server2dAgent> agents = group.getAgents();

    for (VisibilityRecord record : records)
    {
      // Filter records - do not update the same visibility record.
      if (record.getGroupId() != group.getGroupId())
      {
        for (Server2dAgent agent : agents)
        {
          if (record.update(agent))
          {
            generateEventsForStillAgent(record, agent);
          }
        }
      }
    }
  }

  private void generateEventsForStillAgent(
      VisibilityRecord record, Server2dAgent stillAgent)
  {
    ServerAgentState state = (ServerAgentState)stillAgent.getAgent().getData();
    int agentId = stillAgent.getAgent().getId();

    if (record.isVisible(stillAgent))
    {
      record.getStatistics().enemyAgentVisible();

      if (!record.wasVisible(stillAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentSeenEvent(
                timeHolder.getCurrentTime(), agentId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
    else
    {
      if (record.wasVisible(stillAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentGoneEvent(
                timeHolder.getCurrentTime(), agentId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
  }

  /**
   * This method should be invoked after movement of all objects.
   * It updates visibility status of all projectiles from the specified
   * projectiles group in all visibility records.
   *
   * @param group - the specified projectiles group.
   */
  void updateRecordsForProjectilesGroups(Server2dProjectilesGroup group)
  {
    Collection<Server2dProjectile> projectiles = group.getProjectiles();

    for (VisibilityRecord record : records)
    {
      for (Server2dProjectile projectile : projectiles)
      {
        if (record.update(projectile))
        {
          generateEventsForStillProjectile(record, projectile);
        }
      }
    }
  }

  private void generateEventsForStillProjectile(
      VisibilityRecord record, Server2dProjectile stillProjectile)
  {
    ServerProjectileState state = (ServerProjectileState)stillProjectile.getProjectile().getData();
    int projectileId = stillProjectile.getProjectile().getId();

    if (record.isVisible(stillProjectile))
    {
      if (!record.wasVisible(stillProjectile))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileSeenEvent(
                timeHolder.getCurrentTime(), state.getType(), projectileId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
    else
    {
      if (record.wasVisible(stillProjectile))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileGoneEvent(
                timeHolder.getCurrentTime(), projectileId,
                state.getX(), state.getY(),
                state.getSpeedX(), state.getSpeedY()));
      }
    }
  }

  void generateEnemyAgentHitEvents(int groupId, Server2dAgent hitAgent, int shootingAgentId)
  {
    int agentId = hitAgent.getAgent().getId();
    for (VisibilityRecord record : records)
    {
      if ((groupId != record.getGroupId()) && record.isVisible(hitAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentHitEvent(
                timeHolder.getCurrentTime(), agentId, shootingAgentId));
      }
    }
  }

  void generateEnemyAgentKilledEvents(int groupId, Server2dAgent killedAgent, int shootingAgentId)
  {
    int agentId = killedAgent.getAgent().getId();
    for (VisibilityRecord record : records)
    {
      if ((groupId != record.getGroupId()) && record.isVisible(killedAgent))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createEnemyAgentDestroyedEvent(
                timeHolder.getCurrentTime(), agentId, shootingAgentId));
      }
    }
  }

  void generateProjectileDestroyedEvents(Server2dProjectile destroyedProjectile)
  {
    int projectileId = destroyedProjectile.getProjectile().getId();
    ServerProjectileState state = (ServerProjectileState)destroyedProjectile.getProjectile().getData();

    for (VisibilityRecord record : records)
    {
      if (record.isVisible(destroyedProjectile))
      {
        record.getEventsBuilder().addEvent(
            EventTypesUtils.createProjectileDestroyedEvent(
                timeHolder.getCurrentTime(), projectileId,
                state.getX(), state.getY()));

      }
    }
  }
}