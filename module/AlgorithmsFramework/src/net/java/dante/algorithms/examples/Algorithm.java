/*
 * Created on 2006-09-03
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.java.dante.algorithms.AlgorithmUtils;
import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.algorithms.data.AgentData;
import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.ControlledAgentsData;
import net.java.dante.algorithms.data.EnemyAgentData;
import net.java.dante.algorithms.data.EnemyAgentsData;
import net.java.dante.sim.command.CommandUtils;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.engine.time.SystemTimer;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.engine.time.TimeStopper;

/**
 * Example of an algorithm.
 *
 * @author M.Olszewski
 */
public class Algorithm extends BaseAlgorithmImpl
{
  Random r  = new Random();

  /**
   * Creates instance of {@link Algorithm} class.
   */
  public Algorithm()
  {
    super(false);
  }


  /**
   * @see net.java.dante.algorithms.BaseAlgorithmImpl#runAlgorithm()
   */
  @Override
  protected void runAlgorithm()
  {
    AlgorithmData data = getAlgorithmData();

    SystemTimer timer = new SystemTimer();
    TimeCounter refreshCounter = new TimeCounter(data.getBestRefreshRate()/2);
    TimeCounter sendCommandsCounter = new TimeCounter(1000);

    AgentData[] agents = data.getControlledData().getActiveAgentsData();

    int activeAgentsCount = data.getControlledData().getActiveAgentsCount();
    Map<Integer, TimeCounter> reloadTimers = new HashMap<Integer, TimeCounter>(activeAgentsCount);
    List<Integer> commandIssued = new ArrayList<Integer>(activeAgentsCount);

    for (int i = 0; i < activeAgentsCount; i++)
    {
      long reloadT = agents[i].getWeapon().getReloadTime()*3;
      TimeCounter tc = new TimeCounter(reloadT);
      reloadTimers.put(Integer.valueOf(agents[i].getId()), tc);
    }

    final int INTERVAL = 50;

    long delta = INTERVAL;

    timer.start();
    TimeStopper stopper = new TimeStopper(timer, INTERVAL);
    CommandsRepositoryBuilder builder = CommandUtils.createDefaultBuilder(data.getGroupId());

    while (isRunning())
    {
      stopper.startPoint();

      EnemyAgentsData enemiesData = data.getEnemiesData();
      ControlledAgentsData agentsData = data.getControlledData();
      ControlledAgentData[] activeAgents = agentsData.getActiveAgentsData();


      for (Integer agentId : reloadTimers.keySet())
      {
        TimeCounter tc = reloadTimers.get(agentId);
        tc.refresh(delta);
      }


      if (sendCommandsCounter.isActionTime(delta))
      {
        if (enemiesData.getVisibleEnemiesCount() <= 0)
        {
          for (int i = 0; i < activeAgents.length; i++)
          {
            ControlledAgentData activeAgent = activeAgents[i];

            Integer agentId = Integer.valueOf(activeAgent.getId());
            if (!commandIssued.contains(agentId))
            {
              builder.addCommand(activeAgent.getId(), CommandTypesUtils.createClearQueueCommand());
              builder.addCommand(activeAgent.getId(), CommandTypesUtils.createMoveCommand(r.nextInt(736) + 32,
                                                                                          r.nextInt(736) + 32));
              commandIssued.add(agentId);
            }
          }
        }
        else
        {
          EnemyAgentData[] visibleEnemies = enemiesData.getVisibleEnemiesData();

          for (int i = 0; i < activeAgents.length; i++)
          {
            ControlledAgentData activeAgent = activeAgents[i];

            Integer agentId = Integer.valueOf(activeAgent.getId());
            if (!commandIssued.contains(agentId))
            {
              boolean enemyFound = false;
              for (int j = 0; j < visibleEnemies.length; j++)
              {
                if (reloadTimers.get(agentId).isActionTime())
                {
                  if (AlgorithmUtils.isObjectWithinAttackRange(activeAgent, visibleEnemies[j]))
                  {
                    if (r.nextBoolean())
                    {
                      EnemyAgentData enemyData = visibleEnemies[j];
                      builder.addCommand(activeAgent.getId(), CommandTypesUtils.createClearQueueCommand());
                      builder.addCommand(activeAgent.getId(), CommandTypesUtils.createAttackCommand(enemyData.getX() + enemyData.getSize().getWidth()/2,
                                                                                                    enemyData.getY() + enemyData.getSize().getHeight()/2));
                      builder.addCommand(activeAgent.getId(), CommandTypesUtils.createMoveCommand(r.nextInt(736) + 32,
                                                                                                  r.nextInt(736) + 32));
                      commandIssued.add(agentId);
                      reloadTimers.get(agentId).reset();
                      enemyFound = true;
                      break;
                    }
                  }
                }
              }

              if (!enemyFound)
              {
                builder.addCommand(activeAgent.getId(), CommandTypesUtils.createClearQueueCommand());
                builder.addCommand(activeAgent.getId(), CommandTypesUtils.createMoveCommand(r.nextInt(736) + 32,
                                                                                            r.nextInt(736) + 32));
                commandIssued.add(agentId);
              }
            }
          }
        }


        sendCommands(builder.build());
        commandIssued.clear();

        sendCommandsCounter.reset();

      }

      if (refreshCounter.isActionTime(delta))
      {
        data.refresh();

        refreshCounter.reset();
      }

      stopper.endPoint();
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "Simple algorithm.";
  }
}