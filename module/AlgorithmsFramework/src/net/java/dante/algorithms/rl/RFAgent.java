/*
 * Created on 2006-09-10
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.rl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.java.dante.algorithms.AlgorithmUtils;
import net.java.dante.algorithms.common.Dbg;
import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.ControlledAgentsData;
import net.java.dante.algorithms.data.EnemyAgentData;
import net.java.dante.algorithms.data.EnemyAgentsData;
import net.java.dante.algorithms.data.ProjectileData;
import net.java.dante.algorithms.data.ProjectilesData;
import net.java.dante.algorithms.data.SimpleObjectData;
import net.java.dante.algorithms.data.TileData;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.util.math.Point2d;
import net.java.dante.sim.util.math.Vector2d;

import pl.gdan.elsy.qconf.Action;
import pl.gdan.elsy.qconf.Brain;


/**
 * Reinforcement learning agent.
 *
 * @author M.Olszewski
 */
public class RFAgent
{
  /** Interval between two updates of learning agent. */
  private static final int UPDATE_INTERVAL = 200;
  
  private static final double AVG_FORGET = 0.01;
  
  private static final String STORED_BRAIN_FILE_PREFIX = RFAgent.class.getSimpleName() + "___";
  private static final int MOVEMENT_TIME = 200;
  private static final int DEFAULT_LIST_SIZE = 100;
  private static final double TURN_ANGLE = 1;

  enum CommandType
  {
    /** Command was not issued. */
    NO_COMMAND,
    /** Turn command type. */
    TURN_COMMAND,
    /** Movement command type. */
    MOVE_COMMAND,
    /** Attack command type. */
    ATTACK_COMMAND
  }

  private AlgorithmData data;
  private CommandsRepositoryBuilder builder;
  private TileData[] obstacles;

  private ControlledAgentData agentData;
  private Point2d position;
  private Vector2d speed = new Vector2d(0, 0);
  private final double maxDistance;
  private final double checkDistance;
  private double angle = 0;
  private double avgReward = 0;
  private List<SimpleObjectData> objectsWithinMovement = new ArrayList<SimpleObjectData>(DEFAULT_LIST_SIZE);
  
  /* Reload weapon timer. */
  private TimeCounter reloadTimer;
  /* Update learning agent timer. */
  private TimeCounter updateTimer = new TimeCounter(UPDATE_INTERVAL);

  private CommandType lastCommandType = CommandType.NO_COMMAND;

  private int agentIdx;

  private RFAgentPerception perception;
  private Brain brain;

  /**
   * Creates instance of {@link RFAgent} class.
   *
   * @param algorithmData algorithm's data.
   * @param controlledAgentData this agent's data.
   * @param commandsBuilder commands builder.
   * @param obstaclesArray obstacle's array.
   * @param agentIndex agent's agent's index in group.
   */
  public RFAgent(AlgorithmData algorithmData,
                 ControlledAgentData controlledAgentData,
                 CommandsRepositoryBuilder commandsBuilder,
                 TileData[] obstaclesArray,
                 int agentIndex)
  {
    if (controlledAgentData == null)
    {
      throw new NullPointerException("Specified controlledAgentData is null!");
    }
    if (algorithmData == null)
    {
      throw new NullPointerException("Specified algorithmData is null!");
    }
    if (commandsBuilder == null)
    {
      throw new NullPointerException("Specified commandsBuilder is null!");
    }
    if (obstaclesArray == null)
    {
      throw new NullPointerException("Specified obstaclesArray is null!");
    }

    data        = algorithmData;
    builder     = commandsBuilder;
    agentData   = controlledAgentData;
    obstacles   = obstaclesArray;
    agentIdx    = agentIndex;
    maxDistance = (agentData.getMaxSpeed() * MOVEMENT_TIME) / 1000.0;
    
    double halfDiagonal = Math.sqrt((agentData.getSize().getWidth() * agentData.getSize().getWidth()) +
                                    (agentData.getSize().getWidth() * agentData.getSize().getWidth())) * 0.5;
    
    checkDistance = maxDistance + halfDiagonal;

    position    = new Point2d(agentData.getX() + (agentData.getSize().getWidth() >> 1),
                              agentData.getY() + (agentData.getSize().getHeight() >> 1));
    
    // Timer counters
    reloadTimer = new TimeCounter(agentData.getWeapon().getReloadTime());
    reloadTimer.refresh(agentData.getWeapon().getReloadTime());
    updateTimer.refresh(UPDATE_INTERVAL);

    initializeQFramework();
  }

  private void initializeQFramework()
  {
    perception = new RFAgentPerception(this);

    Action actionArray[] = new Action[4];
    actionArray[0] = new MoveForwardAction(this);
    actionArray[1] = new TurnLeftAction(this);
    actionArray[2] = new TurnRightAction(this);
    actionArray[3] = new AttackAction(this);

    brain = new Brain(perception, actionArray);
    brain.setAlpha(0.1);
    brain.setGamma(0.8);
    brain.setLambda(0.8);
    brain.setUseBoltzmann(true);
    brain.setTemperature(0.02);

    // Try to load brain stored in a file
    tryLoadBrain();
  }

  private boolean tryLoadBrain()
  {
    boolean loaded = false;
    File f = new File(STORED_BRAIN_FILE_PREFIX + agentIdx);
    if (f.exists())
    {
      try
      {
        brain.load(f.getName());
        loaded = true;
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (ClassNotFoundException e)
      {
        e.printStackTrace();
      }
    }

    return loaded;
  }

  /**
   * Turns agent's movement left.
   *
   * @return Always returns 0.
   */
  int turnLeft()
  {
    if (Dbg.DBG1) Dbg.write("Turn left action.");
    
    angle -= TURN_ANGLE;
    lastCommandType = CommandType.TURN_COMMAND;

    return 0;
  }

  /**
   * Turns agent's movement right.
   *
   * @return Always returns 0.
   */
  int turnRight()
  {
    if (Dbg.DBG1) Dbg.write("Turn right action.");
    
    angle += TURN_ANGLE;
    lastCommandType = CommandType.TURN_COMMAND;

    return 0;
  }

  /**
   * Starts a movement to some destination in agent's sight range.
   *
   * @return Always returns 1.
   */
  int moveForward()
  {
    if (Dbg.DBG1) Dbg.write("Move forward action.");
    
    calculateDestination();

    builder.addCommand(agentData.getId(), CommandTypesUtils.createClearQueueCommand());
    builder.addCommand(agentData.getId(), CommandTypesUtils.createMoveCommand(position.getX(),
                                                                              position.getY()));
    lastCommandType = CommandType.MOVE_COMMAND;

    int retVal = 1;

    if (!checkPossibleCollisions())
    {
      retVal = 0;
    }

    return retVal;
  }

  /**
   * Calculates agent's destination.
   */
  private void calculateDestination()
  {
    if (Dbg.DBG1)
    {
      Dbg.write("old point=" + position.getX() + ", " + position.getY());
      Dbg.write("angle=" + angle);
    }
    
    // Choose distance
    double dx = maxDistance * Math.cos(angle);
    double dy = maxDistance * Math.sin(angle);
    
    position.setPoint(position.getX() + dx, position.getY() + dy);
    
    speed.setVector(dx / MOVEMENT_TIME * 1000.0, dy / MOVEMENT_TIME * 1000.0);

    if (Dbg.DBG1)
    {
      Dbg.write("dx=" + dx + ", dy=" + dy);
      Dbg.write("new point=" + position.getX() + ", " + position.getY());
      Dbg.write("new speed=" + speed.getX() + ", " + speed.getY());
    }
  }

  /**
   * Force enemy attack which is in group's range.
   *
   * @return Returns: '1' if attack was performed on enemy agent in range,
   *         or '0' if no enemy agent was in agent's weapon range.
   */
  int attack()
  {
    if (Dbg.DBG1) Dbg.write("Attack action.");
    int retVal = 0;

    EnemyAgentData enemy = chooseEnemy();

    if (enemy != null)
    {
      if (reloadTimer.isActionTime())
      {
        builder.addCommand(agentData.getId(),
                           CommandTypesUtils.createClearQueueCommand());
        
        builder.addCommand(agentData.getId(),
                           CommandTypesUtils.createAttackCommand(enemy.getX() + (enemy.getSize().getWidth() >> 1),
                                                                 enemy.getY() + (enemy.getSize().getHeight() >> 1)));
        
        reloadTimer.reset();
        if (Dbg.DBG1) Dbg.write("Attack successful!");
      }
      else
      {
        if (Dbg.DBG1) Dbg.write("Attack failed: weapon not reloaded!");
        retVal = 1;
      }
    }
    else
    {
      if (Dbg.DBG1) Dbg.write("Attack failed: no enemies in sight range!");
      retVal = 2;
    }

    lastCommandType = CommandType.ATTACK_COMMAND;

    return retVal;
  }

  /**
   * Chooses enemy agent to attack.
   *
   * @return Returns attacked enemy agent's data or <code>null</code> if no
   *         enemies are within group's range.
   */
  private EnemyAgentData chooseEnemy()
  {
    EnemyAgentData enemy = null;
    int visibleEnemiesCount = data.getEnemiesData().getVisibleEnemiesCount();

    if (visibleEnemiesCount > 0)
    {
      // Choose the first one in sight's range
      for (int i = 0; i < visibleEnemiesCount; i++)
      {
        EnemyAgentData enemyInRange = data.getEnemiesData().getVisibleEnemyData(i);
        if (AlgorithmUtils.isObjectWithinAttackRange(agentData,
                                                     enemyInRange))
        {
          enemy = enemyInRange;
          break;
        }
      }
    }

    return enemy;
  }

  /**
   * Updates this agent.
   * 
   * @param delta time elapsed since the last update.
   */
  public void update(long delta)
  {
    reloadTimer.refresh(delta);
    
    if (updateTimer.isActionTime(delta))
    {
      if (Dbg.DBG1) Dbg.write("Update for agent=" + agentData.getId());
      updateCollisionsData();
  
      perception.perceive();
      
      avgReward = avgReward * (1 - AVG_FORGET) + perception.getReward() * AVG_FORGET;
//      if (Dbg.DBG1) 
      Dbg.write("Avg reward=" + avgReward);
      
      brain.count();
      brain.executeAction();
      
      updateTimer.reset();
    }
  }

  /**
   * Gets the lastCommandType.
   *
   * @return Returns the lastCommandType.
   */
  public CommandType getLastCommandType()
  {
    return lastCommandType;
  }

  boolean attackedEnemyInRange()
  {
    assert lastCommandType == CommandType.ATTACK_COMMAND : "lastCommandType != CommandType.ATTACK_COMMAND !!!";

    return (brain.getExecutionResult() == 0);
  }
  
  boolean weaponNotRealoaded()
  {
    assert lastCommandType == CommandType.ATTACK_COMMAND : "lastCommandType != CommandType.ATTACK_COMMAND !!!";

    return (brain.getExecutionResult() == 1);
  }
  
  boolean noEnemyInRange()
  {
    assert lastCommandType == CommandType.ATTACK_COMMAND : "lastCommandType != CommandType.ATTACK_COMMAND !!!";
    
    return (brain.getExecutionResult() == 2);
  }

  boolean movedWell()
  {
    assert lastCommandType == CommandType.MOVE_COMMAND : "lastCommandType != CommandType.MOVE_COMMAND !!!";

    return (brain.getExecutionResult() == 0);
  }

  private void updateCollisionsData()
  {
    objectsWithinMovement.clear();

    collectOtherAgents();
    collectObstacles();
    collectEnemies();
    collectProjectiles();

    if (Dbg.DBG1) Dbg.write("updated collision data, found " + objectsWithinMovement.size() + " objects");
  }
  
  private boolean checkObject(SimpleObjectData object)
  {
    double distanceToCheck = checkDistance + 0.5 * Math.sqrt(object.getSize().getWidth() * object.getSize().getWidth() + 
                                                             object.getSize().getHeight() * object.getSize().getHeight());
    
    boolean result = (AlgorithmUtils.isObjectWithinRange(position.getX(), position.getY(), distanceToCheck, object) &&
        AlgorithmUtils.isObjectOnObjectWay(object.getX(), object.getY(), object.getSize(),
                                           position.getX(), position.getY(),
                                           speed, agentData.getSize()));
    
    if (Dbg.DBG1)
    {
      Dbg.write("distance to check=" + distanceToCheck);
      Dbg.write("Agent data: x=" + position.getX() + ", y=" + position.getY() + 
                ", size=" + agentData.getSize() + " speed=" + speed);
      Dbg.write("Object data: x=" + object.getX() + ", y=" + object.getY() + 
                ", size=" + object.getSize());
      Dbg.write("result=" + result);
    }
    
    return result;
  }

  boolean checkPossibleCollisions()
  {
    return objectsWithinMovement.size() > 0;
  }

  private void collectOtherAgents()
  {
    ControlledAgentsData agents = data.getControlledData();
    for (int i = 0, size = agents.getActiveAgentsCount(); i < size; i++)
    {
      ControlledAgentData otherAgentData = agents.getActiveAgentData(i);

      if (otherAgentData != agentData)
      {
        if (checkObject(otherAgentData))
        {
          objectsWithinMovement.add(otherAgentData);
        }
      }
    }
  }

  private void collectObstacles()
  {
    for (int i = 0; i < obstacles.length; i++)
    {
      if (checkObject(obstacles[i]))
      {
        objectsWithinMovement.add(obstacles[i]);
      }
    }
  }

  private void collectEnemies()
  {
    EnemyAgentsData agents = data.getEnemiesData();
    for (int i = 0, size = agents.getVisibleEnemiesCount(); i < size; i++)
    {
      EnemyAgentData enemyAgentData = agents.getVisibleEnemyData(i);

      if (checkObject(enemyAgentData))
      {
        objectsWithinMovement.add(enemyAgentData);
      }
    }
  }

  private void collectProjectiles()
  {
    ProjectilesData projectilesData = data.getProjectilesData();
    if (projectilesData.getProjectilesCount() > 0)
    {
      ProjectileData[] projectiles = projectilesData.getProjectilesData();
      for (int i = 0; i < projectiles.length; i++)
      {
        ProjectileData projectile = projectiles[i];

        if (checkObject(projectile))
         {
           objectsWithinMovement.add(projectile);
         }
      }
    }
  }
  
  boolean isWeaponReady()
  {
    return reloadTimer.isActionTime();
  }

  boolean isEnemyInRange()
  {
    boolean inRange = false;
    
    EnemyAgentData[] visibleEnemies = data.getEnemiesData().getVisibleEnemiesData();
    for (int i = 0; i < visibleEnemies.length; i++)
    {
      if (AlgorithmUtils.isObjectWithinAttackRange(agentData, visibleEnemies[i]))
      {
        inRange = true;
        break;
      }
    }

    return inRange;
  }

  /**
   * Stores agent's brain.
   */
  public void storeBrain()
  {
    try
    {
      brain.save(STORED_BRAIN_FILE_PREFIX + agentIdx);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Updates position of blocked agent.
   *
   * @param blockedX blocked agent's coordinates.
   * @param blockedY blocked agent's coordinates.
   */
  public void blocked(double blockedX, double blockedY)
  {
    position.setPoint(blockedX + (agentData.getSize().getWidth() >> 1),
                      blockedY + (agentData.getSize().getHeight() >> 1));
    speed.setVector(0, 0);
    
    if (Dbg.DBG1) Dbg.write("blocked at point=" + position.getX() + ", " + position.getY());
  }
  
  /**
   * Updates speed and position of moved agent.
   * 
   * @param positionX blocked agent's coordinates.
   * @param positionY blocked agent's coordinates.
   * @param speedX blocked agent's speed.
   * @param speedY blocked agent's speed.
   */
  public void moved(double positionX, double positionY, double speedX, double speedY)
  {
    position.setPoint(positionX + (agentData.getSize().getWidth() >> 1),
                      positionY + (agentData.getSize().getHeight() >> 1));
    speed.setVector(speedX, speedY);
    
    if (Dbg.DBG1) Dbg.write("movement finished at point=" + position.getX() + ", " + position.getY());
  }
  
  /**
   * Updates speed of agent whose movement was finished.
   */
  public void moveFinished()
  {
    speed.setVector(0, 0);
    
    if (Dbg.DBG1) Dbg.write("movement finished at point=" + position.getX() + ", " + position.getY());
  }
}