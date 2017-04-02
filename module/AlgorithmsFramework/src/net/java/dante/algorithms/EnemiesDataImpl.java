/*
 * Created on 2006-09-01
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.java.dante.algorithms.data.EnemyAgentData;
import net.java.dante.algorithms.data.EnemyAgentsData;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventsRepository;
import net.java.dante.sim.event.types.EnemyAgentDestroyedEvent;
import net.java.dante.sim.event.types.EnemyAgentEvent;
import net.java.dante.sim.event.types.EnemyAgentGoneEvent;
import net.java.dante.sim.event.types.EnemyAgentHitEvent;
import net.java.dante.sim.event.types.EnemyAgentMoveEvent;
import net.java.dante.sim.event.types.EnemyAgentSeenEvent;
import net.java.dante.sim.event.types.ObjectMoveEventParams;

/**
 * Implementation of {@link EnemyAgentsData} interface.
 *
 * @author M.Olszewski
 */
class EnemiesDataImpl implements EnemyAgentsData, EventsUpdateable
{
  /** Map between enemy agents identifiers and enemy agents. */
  private Map<Integer, EnemyAgentDataImpl> enemiesMap;
  /** List with all enemy agents. */
  private List<EnemyAgentDataImpl> enemies;
  /** List with visible enemy agents. */
  private List<EnemyAgentDataImpl> visibleEnemies;
  /** List with not visible enemy agents. */
  private List<EnemyAgentDataImpl> notVisibleEnemies;
  /** Count of destroyed enemies */
  private int destroyedEnemies = 0;


  /**
   * Creates instance of {@link EnemiesDataImpl} class.
   *
   * @param enemiesList - list with data of all enemy agents.
   */
  EnemiesDataImpl(List<EnemyAgentDataImpl> enemiesList)
  {
    if (enemiesList == null)
    {
      throw new NullPointerException("Specified enemiesList is null!");
    }
    if (enemiesList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument enemiesList - it must contain at least 1 element!");
    }

    enemies           = enemiesList;

    visibleEnemies    = new ArrayList<EnemyAgentDataImpl>(enemiesList.size());

    notVisibleEnemies = new ArrayList<EnemyAgentDataImpl>(enemiesList.size());
    notVisibleEnemies.addAll(enemiesList);

    // Initialize enemies map
    enemiesMap        = new HashMap<Integer, EnemyAgentDataImpl>(enemiesList.size());
    for (EnemyAgentDataImpl data : enemiesList)
    {
      enemiesMap.put(Integer.valueOf(data.getId()), data);
    }
  }


  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getDestroyedEnemiesCount()
   */
  public int getDestroyedEnemiesCount()
  {
    return destroyedEnemies;
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getEnemiesCount()
   */
  public int getEnemiesCount()
  {
    return enemies.size();
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getEnemiesData()
   */
  public EnemyAgentData[] getEnemiesData()
  {
    return enemies.toArray(new EnemyAgentData[enemies.size()]);
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getNotVisibleEnemiesCount()
   */
  public int getNotVisibleEnemiesCount()
  {
    return notVisibleEnemies.size();
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getNotVisibleEnemiesData()
   */
  public EnemyAgentData[] getNotVisibleEnemiesData()
  {
    return notVisibleEnemies.toArray(new EnemyAgentData[notVisibleEnemies.size()]);
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getVisibleEnemiesCount()
   */
  public int getVisibleEnemiesCount()
  {
    return visibleEnemies.size();
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getVisibleEnemiesData()
   */
  public EnemyAgentData[] getVisibleEnemiesData()
  {
    return visibleEnemies.toArray(new EnemyAgentData[visibleEnemies.size()]);
  }

  /**
   * @see net.java.dante.algorithms.data.EnemyAgentsData#getVisibleEnemyData(int)
   */
  public EnemyAgentData getVisibleEnemyData(int index)
  {
    return visibleEnemies.get(index);
  }

  /**
   * @see net.java.dante.algorithms.EventsUpdateable#update(net.java.dante.sim.event.EventsRepository)
   */
  public void update(EventsRepository repository)
  {
    if (repository == null)
    {
      throw new NullPointerException("Specified repository is null!");
    }

    for (int i = 0, eventsCount = repository.getEventsCount(); i < eventsCount; i++)
    {
      Event event = repository.getEvent(i);
      if (event instanceof EnemyAgentEvent)
      {
        processEnemyEvent((EnemyAgentEvent)event);
      }
    }
  }

  /**
   * Processes the specified enemy's event.
   *
   * @param event the specified enemy's event.
   */
  private void processEnemyEvent(EnemyAgentEvent event)
  {
    EnemyAgentDataImpl enemyData = enemiesMap.get(Integer.valueOf(event.getEnemyAgentId()));

    if (event instanceof EnemyAgentMoveEvent)
    {
      setMovementParameters(enemyData, (ObjectMoveEventParams)event);
    }
    else if (event instanceof EnemyAgentSeenEvent)
    {
      enemyData.inSightRange();
      setMovementParameters(enemyData, (ObjectMoveEventParams)event);

      visibleEnemies.add(enemyData);
      notVisibleEnemies.remove(enemyData);
    }
    else if (event instanceof EnemyAgentGoneEvent)
    {
      enemyData.outOfSightRange();
      setMovementParameters(enemyData, (ObjectMoveEventParams)event);

      visibleEnemies.remove(enemyData);
      notVisibleEnemies.add(enemyData);
    }
    else if (event instanceof EnemyAgentHitEvent)
    {
      enemyData.addHitsCount(1);
    }
    else if (event instanceof EnemyAgentDestroyedEvent)
    {
      enemyData.destroyed();

      destroyedEnemies++;
      visibleEnemies.remove(enemyData);
      notVisibleEnemies.remove(enemyData);
    }
  }

  /**
   * Sets the specified movement parameters of the specified enemy agent's data.
   *
   * @param enemyData the specified enemy agent's data.
   * @param moveParams the specified movement parameters.
   */
  private void setMovementParameters(EnemyAgentDataImpl enemyData,
                                     ObjectMoveEventParams moveParams)
  {
    enemyData.setX(moveParams.getDestinationX());
    enemyData.setY(moveParams.getDestinationY());
    enemyData.setSpeedX(moveParams.getSpeedX());
    enemyData.setSpeedY(moveParams.getSpeedY());
  }
}