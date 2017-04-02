/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.game.sim;

import java.util.Arrays;
import java.util.Random;

import net.java.dante.darknet.messaging.NetworkMessage;
import net.java.dante.darknet.test.MessagesProvider;
import net.java.dante.darknet.test.SimpleNetMessagesTester;
import net.java.dante.sim.command.CommandUtils;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.command.types.CommandType;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.data.GlobalData;
import net.java.dante.sim.data.common.AnimationFramesData;
import net.java.dante.sim.data.map.MapTileType;
import net.java.dante.sim.data.map.MapUtils;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.map.StartLocation;
import net.java.dante.sim.data.map.StartLocationGroup;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.TemplateDataUtils;
import net.java.dante.sim.data.template.TemplateTypeData;
import net.java.dante.sim.data.template.types.AgentTemplateData;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.engine.engine2d.server.GroupStatistics;
import net.java.dante.sim.event.EventType;
import net.java.dante.sim.event.EventUtils;
import net.java.dante.sim.event.EventsRepositoryBuilder;
import net.java.dante.sim.event.types.EventTypesUtils;
import net.java.dante.sim.io.CommandsData;
import net.java.dante.sim.io.StatisticsData;
import net.java.dante.sim.io.TimeSyncData;
import net.java.dante.sim.io.UpdateData;
import net.java.dante.sim.io.init.AgentInitDataUtils;
import net.java.dante.sim.io.init.AgentsInitData;
import net.java.dante.sim.io.init.EnemyAgentInitData;
import net.java.dante.sim.io.init.EnemyAgentsGroupInitData;
import net.java.dante.sim.io.init.FriendlyAgentInitData;
import net.java.dante.sim.io.init.InitializationData;

/**
 * Class containing set of tests for all network messages regarding
 * simulation.
 *
 * @author M.Olszewski
 */
public class SimulationMessagesTest
{
  /**
   * Method running tests.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    SimpleNetMessagesTester tester = new SimpleNetMessagesTester();
    tester.registerMessagesProvider(new SimulationMessagesProvider());

    tester.test();
  }
}

/**
 * Provider of network simulation messages.
 *
 * @author M.Olszewski
 */
class SimulationMessagesProvider implements MessagesProvider
{
  private Random rand = new Random();

  
  /**
   * @see net.java.dante.darknet.test.MessagesProvider#provide()
   */
  public NetworkMessage[] provide()
  {
    final int maxMessages = 10000;
    final int NUMBER_OF_TYPES = 6;

    NetworkMessage[] msg = new NetworkMessage[maxMessages];

    for (int i = 0; i < maxMessages; i++)
    {
      switch (rand.nextInt(NUMBER_OF_TYPES))
      {
        case 0:
        {
          msg[i] = buildInitializationMessage();
          break;
        }
        case 1:
        {
          msg[i] = buildFinishMessage();
          break;
        }
        case 2:
        {
          msg[i] = buildTimeSyncMessage();
          break;
        }
        case 3:
        {
          msg[i] = buildCommandsNetworkMessage();
          break;
        }
        case 4:
        {
          msg[i] = buildUpdateNetworkMessage();
          break;
        }
        case 5:
        {
          msg[i] = buildStatisticsNetworkMessage();
          break;
        }
      }
    }

    return msg;
  }

  private FinishDataNetworkMessage buildFinishMessage()
  {
    return new FinishDataNetworkMessage(rand.nextInt(Integer.MAX_VALUE));
  }

  private TimeSyncNetworkMessage buildTimeSyncMessage()
  {
    return new TimeSyncNetworkMessage(rand.nextInt(Integer.MAX_VALUE),
                                      new TimeSyncData(rand.nextInt(Integer.MAX_VALUE), rand.nextInt(Integer.MAX_VALUE)));
  }

  private InitializationNetworkMessage buildInitializationMessage()
  {
    GlobalData globalData = new GlobalData(rand.nextBoolean(), rand.nextBoolean(),
                                           rand.nextInt(1000000) + 1, rand.nextInt(1000) + 1,
                                           rand.nextInt(20000) + 1, 2,
                                           "map", "tile",
                                           rand.nextInt(200) + 1,
                                           new AnimationFramesData("agentMove", rand.nextInt(40) + 1, rand.nextInt(1000) + 1),
                                           new AnimationFramesData("agentExplo", rand.nextInt(40) + 1, rand.nextInt(1000) + 1),
                                           new AnimationFramesData("projMove", rand.nextInt(40) + 1, rand.nextInt(1000) + 1),
                                           new AnimationFramesData("projExplo", rand.nextInt(40) + 1, rand.nextInt(1000) + 1),
                                           new ObjectSize(rand.nextInt(100), rand.nextInt(100)));

    StartLocation[] group1 = new StartLocation[] {
        MapUtils.createStartLocation(rand.nextInt(12), rand.nextInt(12)),
        MapUtils.createStartLocation(rand.nextInt(12), rand.nextInt(12))
    };

    StartLocation[] group2 = new StartLocation[] {
        MapUtils.createStartLocation(rand.nextInt(12), rand.nextInt(12)),
        MapUtils.createStartLocation(rand.nextInt(12), rand.nextInt(12))
    };

    StartLocationGroup[] groups = new StartLocationGroup[] {
        MapUtils.createStartLocationGroup(group1),
        MapUtils.createStartLocationGroup(group2),
    };

    int[][] tiles = new int[12][12];

    for (int i = 0; i < tiles.length; i++)
    {
      for (int j = 0; j < tiles[i].length; j++)
      {
        if ((j == 0) || (j == (tiles[i].length - 1)))
        {
          tiles[i][j] = MapTileType.OBSTACLE.ordinal();
        }
        else
        {
          if (rand.nextInt(100) > 25)
          {
            tiles[i][j] = MapTileType.FREE_TILE.ordinal();
          }
          else
          {
            tiles[i][j] = MapTileType.OBSTACLE.ordinal();
          }
        }
      }
    }

    Arrays.fill(tiles[0], MapTileType.OBSTACLE.ordinal());
    Arrays.fill(tiles[tiles.length - 1], MapTileType.OBSTACLE.ordinal());


    SimulationMap simMap = MapUtils.createSimulationMap(tiles, groups);

    TemplateTypeData agentData =
        TemplateDataUtils.createTemplateTypeData("KRAJNIAK",
            new AgentTemplateData(rand.nextInt(1000) + 1, rand.nextInt(500) + 1,
                                  rand.nextInt(500) + 1,
                                  new ObjectSize(rand.nextInt(100), rand.nextInt(100)),
                                  "ROSE"));
    TemplateTypeData weaponData =
        TemplateDataUtils.createTemplateTypeData("ROSE",
            new WeaponTemplateData(rand.nextInt(500) + 1, rand.nextInt(500) + 1,
                                   rand.nextInt(500) + 1, rand.nextInt(50) + 1,
                                   rand.nextInt(50) + 50,
                                   rand.nextInt(50000),
                                   new ObjectSize(rand.nextInt(100), rand.nextInt(100))));


    FriendlyAgentInitData[] friendlyData = new FriendlyAgentInitData[] {
        AgentInitDataUtils.createFriendlyAgentInitData("KRAJNIAK", rand.nextInt(Integer.MAX_VALUE), group1[0].getColumn(), group1[0].getRow()),
        AgentInitDataUtils.createFriendlyAgentInitData("KRAJNIAK", rand.nextInt(Integer.MAX_VALUE), group1[1].getColumn(), group1[1].getRow()),
    };

    EnemyAgentInitData[] enemiesData = new EnemyAgentInitData[] {
        AgentInitDataUtils.createEnemyAgentInitData("KRAJNIAK", rand.nextInt(Integer.MAX_VALUE)),
        AgentInitDataUtils.createEnemyAgentInitData("KRAJNIAK", rand.nextInt(Integer.MAX_VALUE))
    };

    AgentsInitData agentsInitData = AgentInitDataUtils.createAgentsGroupsInitData(friendlyData,
        new EnemyAgentsGroupInitData[] { AgentInitDataUtils.createEnemyAgentsGroupInitData(enemiesData) });


    return new InitializationNetworkMessage(rand.nextInt(Integer.MAX_VALUE),
               InitializationData.createInitializationData(rand.nextInt(Integer.MAX_VALUE),
                                                           simMap, agentData,
                                                           weaponData, agentsInitData, globalData));
  }

  private CommandsNetworkMessage buildCommandsNetworkMessage()
  {
    final int MAX_COMMANDS = rand.nextInt(200) + 10;
    final int MAX_COMMANDS_TYPE = CommandType.values().length;

    CommandsRepositoryBuilder builder = CommandUtils.createDefaultBuilder(rand.nextInt(Integer.MAX_VALUE));
    int agentId = rand.nextInt(Integer.MAX_VALUE);

    for (int i = 0; i < MAX_COMMANDS; i++)
    {
      if (rand.nextInt(100) > 75)
      {
        agentId = rand.nextInt(Integer.MAX_VALUE);
      }

      CommandType type = CommandType.values()[rand.nextInt(MAX_COMMANDS_TYPE)];
      switch (type)
      {
        case CLEAR_QUEUE:
        {
          builder.addCommand(agentId, CommandTypesUtils.createClearQueueCommand());
          break;
        }
        case RESUME_LAST_COMMAND:
        {
          builder.addCommand(agentId, CommandTypesUtils.createResumeLastCommand());
          break;
        }
        case MOVE:
        {
          builder.addCommand(agentId,
                             CommandTypesUtils.createMoveCommand(rand.nextInt(2000),
                                                                 rand.nextInt(2000)));
          break;
        }
        case ATTACK:
        {
          builder.addCommand(agentId,
                             CommandTypesUtils.createAttackCommand(rand.nextInt(2000),
                                                                   rand.nextInt(2000)));
          break;
        }
      }
    }

    return new CommandsNetworkMessage(rand.nextInt(Integer.MAX_VALUE),
                                      new CommandsData(builder.build()));
  }

  UpdateNetworkMessage buildUpdateNetworkMessage()
  {
    final int MAX_EVENTS = rand.nextInt(500) + 10;
    final int MAX_EVENTS_TYPE = EventType.values().length;

    EventsRepositoryBuilder builder = EventUtils.createDefaultBuilder(rand.nextInt(Integer.MAX_VALUE));

    int enemyAgentId = rand.nextInt(Integer.MAX_VALUE);
    int friendlyAgentId = rand.nextInt(Integer.MAX_VALUE);
    int projectileId = rand.nextInt(Integer.MAX_VALUE);

    for (int i = 0; i < MAX_EVENTS; i++)
    {
      if (rand.nextInt(100) > 75)
      {
        enemyAgentId = rand.nextInt(Integer.MAX_VALUE);
      }
      if (rand.nextInt(100) > 75)
      {
        friendlyAgentId = rand.nextInt(Integer.MAX_VALUE);
      }
      if (rand.nextInt(100) > 75)
      {
        projectileId = rand.nextInt(Integer.MAX_VALUE);
      }

      EventType type = EventType.values()[rand.nextInt(MAX_EVENTS_TYPE)];
      switch (type)
      {
        case ENEMY_AGENT_DESTROYED:
        {
          builder.addEvent(
              EventTypesUtils.createEnemyAgentDestroyedEvent(rand.nextInt(1000000),
                                                             enemyAgentId,
                                                             rand.nextInt(1000000)));
          break;
        }
        case ENEMY_AGENT_GONE:
        {
          builder.addEvent(
              EventTypesUtils.createEnemyAgentGoneEvent(rand.nextInt(1000000),
                                                        enemyAgentId,
                                                        rand.nextInt(2000),
                                                        rand.nextInt(2000),
                                                        rand.nextInt(50),
                                                        rand.nextInt(50)));
          break;
        }
        case ENEMY_AGENT_SEEN:
        {
          builder.addEvent(
              EventTypesUtils.createEnemyAgentSeenEvent(rand.nextInt(1000000),
                                                        enemyAgentId,
                                                        rand.nextInt(2000),
                                                        rand.nextInt(2000),
                                                        rand.nextInt(50),
                                                        rand.nextInt(50)));
          break;
        }
        case ENEMY_AGENT_MOVE:
        {
          builder.addEvent(
              EventTypesUtils.createEnemyAgentMoveEvent(rand.nextInt(1000000),
                                                        enemyAgentId,
                                                        rand.nextInt(2000),
                                                        rand.nextInt(2000),
                                                        rand.nextInt(50),
                                                        rand.nextInt(50)));
          break;
        }
        case ENEMY_AGENT_HIT:
        {
          builder.addEvent(
              EventTypesUtils.createEnemyAgentHitEvent(rand.nextInt(1000000),
                                                       enemyAgentId,
                                                       rand.nextInt(1000000)));
          break;
        }
        case FRIENDLY_AGENT_BLOCKED:
        {
          builder.addEvent(
              EventTypesUtils.createFriendlyAgentBlockedEvent(rand.nextInt(1000000),
                                                              friendlyAgentId,
                                                              rand.nextInt(2000),
                                                              rand.nextInt(2000)));
          break;
        }
        case FRIENDLY_AGENT_DESTROYED:
        {
          builder.addEvent(
              EventTypesUtils.createFriendlyAgentDestroyedEvent(rand.nextInt(1000000),
                                                                friendlyAgentId));
          break;
        }
        case FRIENDLY_AGENT_HIT:
        {
          builder.addEvent(
              EventTypesUtils.createFriendlyAgentHitEvent(rand.nextInt(1000000),
                                                          friendlyAgentId,
                                                          rand.nextInt(200) + 1));
          break;
        }
        case FRIENDLY_AGENT_MOVE:
        {
          builder.addEvent(
              EventTypesUtils.createFriendlyAgentMoveEvent(rand.nextInt(1000000),
                                                           friendlyAgentId,
                                                           rand.nextInt(2000),
                                                           rand.nextInt(2000),
                                                           rand.nextInt(50),
                                                           rand.nextInt(50)));
          break;
        }
        case FRIENDLY_AGENT_MOVE_FINISHED:
        {
          builder.addEvent(
              EventTypesUtils.createFriendlyAgentMoveFinishedEvent(rand.nextInt(1000000),
                                                                   friendlyAgentId));
          break;
        }
        case PROJECTILE_DESTROYED:
        {
          builder.addEvent(EventTypesUtils.createProjectileDestroyedEvent(rand.nextInt(1000000),
                                                                          projectileId,
                                                                          rand.nextInt(2000),
                                                                          rand.nextInt(2000)));
          break;
        }
        case PROJECTILE_GONE:
        {
          builder.addEvent(EventTypesUtils.createProjectileGoneEvent(rand.nextInt(1000000),
                                                                     projectileId,
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(50),
                                                                     rand.nextInt(50)));
          break;
        }
        case PROJECTILE_MOVE:
        {
          builder.addEvent(EventTypesUtils.createProjectileMoveEvent(rand.nextInt(1000000),
                                                                     projectileId,
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(50),
                                                                     rand.nextInt(50)));
          break;
        }
        case PROJECTILE_SEEN:
        {
          builder.addEvent(EventTypesUtils.createProjectileSeenEvent(rand.nextInt(1000000),
                                                                     "nie wiem co psize lae jest spook",
                                                                     projectileId,
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(50),
                                                                     rand.nextInt(50)));
          break;
        }
        case PROJECTILE_SHOT:
        {
          builder.addEvent(EventTypesUtils.createProjectileShotEvent(rand.nextInt(1000000),
                                                                     "raz dwa trzysta dwiwescscsieeee osiem",
                                                                     projectileId,
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(2000),
                                                                     rand.nextInt(50),
                                                                     rand.nextInt(50),
                                                                     rand.nextInt(1000000)));
          break;
        }
      }
    }

    return new UpdateNetworkMessage(rand.nextInt(Integer.MAX_VALUE),
                                    new UpdateData(rand.nextInt(1000000) + 1,
                                                   builder.build()));
  }

  StatisticsNetworkMessage buildStatisticsNetworkMessage()
  {
    GroupStatistics groupStats = new GroupStatistics(rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE),
                                                     rand.nextInt(Integer.MAX_VALUE));
    return new StatisticsNetworkMessage(rand.nextInt(Integer.MAX_VALUE),
                                     new StatisticsData(groupStats));
  }
}