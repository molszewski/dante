/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */


package net.java.dante.algorithms.flock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.java.dante.algorithms.AlgorithmUtils;
import net.java.dante.algorithms.common.Dbg;
import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.algorithms.data.EnemyAgentData;
import net.java.dante.algorithms.data.ProjectileData;
import net.java.dante.algorithms.data.TileData;
import net.java.dante.sim.command.CommandsRepositoryBuilder;
import net.java.dante.sim.command.types.CommandTypesUtils;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Point2d;
import net.java.dante.sim.util.math.Vector2d;



/**
 * A member of flock class.
 *
 * @author M.Olszewski
 */
public class FlockMember
{
  /** Interval between two flock member updates. */
  private static final int UPDATE_INTERVAL = 170;
  /** Initial size of list with projectiles. */
  private static final int PROJECTILES_LIST_SIZE = 30;
  /** Minimum urgency of vector change. */
  private static final double MINIMUM_URGENCY = 0.05;
  /** Maximum urgency of vector change. */
  private static final double MAXIMUM_URGENCY = 0.2;
  /** Maximum total change of vector. */
  private static final double MAXIMUM_CHANGE = 0.6;
  /** Preferred separation distance. */
  private static final double PREFERRED_SEPARATION_DISTANCE = 90.0;
  /** Preferred keep away distance. */
  private static final double PREFERRED_KEEP_AWAY_DISTANCE = 64.0;
  /** Time of the movement. */
  private static final long MOVEMENT_TIME = 200;

  /* Data common for the whole flock. */
  private AlgorithmData flockData;

  /* Commands builder. */
  private CommandsRepositoryBuilder builder;

  /* Data of this flock's member. */
  private ControlledAgentData memberData;
  private Point2d destination = new Point2d(0, 0);
  private Point2d position = new Point2d(0, 0);
  private Vector2d speed = new Vector2d(0, 0);
  private Map<Integer, FlockMember> members;

  /* Reload weapon timer. */
  private TimeCounter reloadTimer;
  /* Update flock member timer. */
  private TimeCounter updateTimer = new TimeCounter(UPDATE_INTERVAL);

  /* Data of visible flock members. */
  private List<FlockMember> visibleMembers;
  private double nearestMemberDistance = Double.POSITIVE_INFINITY;
  private FlockMember nearestMember;

  /* Data of obstacles. */
  private TileData[] obstacles;
  private List<TileData> visibleObstacles;
  private double nearestObstacleDistance = Double.POSITIVE_INFINITY;
  private TileData nearestObstacle;

  /* Data of visible projectiles. */
  private List<ProjectileData> visibleProjectiles = new ArrayList<ProjectileData>(PROJECTILES_LIST_SIZE);
  private double nearestProjectileDistance = Double.POSITIVE_INFINITY;
  private ProjectileData nearestProjectile;

  /* Data of visible enemies. */
  private List<EnemyAgentData> visibleEnemies;
  private double nearestEnemyDistance = Double.POSITIVE_INFINITY;
  private EnemyAgentData nearestEnemy;


  /**
   * Creates instance of {@link FlockMember} class.
   *
   * @param flockMemberData data of this member of the flock.
   * @param flockCommonData common data for flock.
   * @param commandsBuilder command's builder.
   * @param initialDestination initial destination for this member.
   * @param obstaclesData array containing all obstacles in the world.
   */
  public FlockMember(ControlledAgentData flockMemberData,
                     AlgorithmData flockCommonData,
                     CommandsRepositoryBuilder commandsBuilder,
                     Point2d initialDestination,
                     TileData[] obstaclesData)
  {
    if (flockMemberData == null)
    {
      throw new NullPointerException("Specified flockMemberData is null!");
    }
    if (flockCommonData == null)
    {
      throw new NullPointerException("Specified flockCommonData is null!");
    }
    if (commandsBuilder == null)
    {
      throw new NullPointerException("Specified commandsBuilder is null!");
    }
    if (initialDestination == null)
    {
      throw new NullPointerException("Specified initialDestination is null!");
    }
    if (obstaclesData == null)
    {
      throw new NullPointerException("Specified obstaclesData is null!");
    }

    memberData = flockMemberData;
    flockData  = flockCommonData;

    position.setPoint(memberData.getX() + (memberData.getSize().getWidth() >> 1),
                      memberData.getY() + (memberData.getSize().getHeight() >> 1));

    builder    = commandsBuilder;
    destination = initialDestination;

    obstacles        = obstaclesData;
    visibleMembers   = new ArrayList<FlockMember>(flockData.getControlledData().getAgentsCount() - 1);
    visibleObstacles = new ArrayList<TileData>(obstaclesData.length);
    // Assume the worst
    visibleEnemies   = new ArrayList<EnemyAgentData>(flockData.getEnemiesData().getEnemiesCount());

    // Timer counters
    reloadTimer = new TimeCounter(memberData.getWeapon().getReloadTime());
    reloadTimer.refresh(memberData.getWeapon().getReloadTime());
    updateTimer.refresh(UPDATE_INTERVAL);
  }


  /**
   * Sets flock repository for this flock's member.
   *
   * @param flockMembers flock's repository.
   */
  public void setFlockRepository(Map<Integer, FlockMember> flockMembers)
  {
    if (flockMembers == null)
    {
      throw new NullPointerException("Specified flockMembers is null!");
    }

    members = flockMembers;
  }

  /**
   * Updates this flock member position using flock algorithm.
   *
   * @param delta time elapsed since last update.
   */
  public void update(long delta)
  {
    reloadTimer.refresh(delta);

    if (updateTimer.isActionTime(delta))
    {
      update0();

      updateTimer.reset();
    }
  }

  /**
   * Method invoked if member of the flock was blocked at the specified location.
   *
   * @param blockedX blocking location.
   * @param blockedY blocking location.
   */
  public void blocked(double blockedX, double blockedY)
  {
    if (Dbg.DBG1)
    {
      Dbg.write("############ flock member " + memberData.getId() + " BLOCKED!");
      Dbg.write("blocked at:" + blockedX + ", " + blockedY);
    }

    speed.setVector(0, 0);
    position.setPoint(blockedX + (memberData.getSize().getWidth() >> 1),
                      blockedY + (memberData.getSize().getHeight() >> 1));

    update0();

    updateTimer.reset();
  }

  /**
   * Forces this flock member's update.
   */
  public void forceUpdate()
  {
    update0();
  }

  private void update0()
  {
    if (Dbg.DBG1)
    {
      Dbg.write("ID=" + memberData.getId());
      Dbg.write("last update speed: dx=" + memberData.getSpeedX() + ", dy=" + memberData.getSpeedY());
      Dbg.write("last update pos: dx=" + memberData.getX() + ", dy=" + memberData.getY());
      Dbg.write("real speed: dx=" + speed.getX() + ", dy=" + speed.getY());
      Dbg.write("real x=" + position.getX() + ", y=" + position.getY());
    }

    updateVisibleMembersList();

    updateVisibleObstaclesList();

    updateVisibleHeadingProjectilesList();

    updateVisibleEnemiesList();

    if (Dbg.DBG1)
    {
      Dbg.write("Sees: " + visibleMembers.size() + " flock members, " +
                visibleObstacles.size() + " walls, " +
                visibleEnemies.size() + " enemies, " +
                visibleProjectiles.size() + " projectiles");
    }

    Vector2d acceleration = new Vector2d(0, 0);

    if (Dbg.DBG1) Dbg.write("Acceleration at start: " + acceleration);

    // Apply three first rules of flocking algorithm if any other member of
    // the flock is seen
    if (visibleMembers.size() > 0)
    {
      if (Dbg.DBG1) Dbg.write("Sees members - updates acc!");

      // Rule #1: Separation
      acceleration.addVector(applySeparationRule());
      if (Dbg.DBG1) Dbg.write("Acceleration after separation: " + acceleration);

      // Rule #2: Alignment
      acceleration.addVector(applyAlignmentRule());
      if (Dbg.DBG1) Dbg.write("Acceleration after alignment: " + acceleration);

      // Rule #3: Cohesion
      acceleration.addVector(applyCohesionRule());
      if (Dbg.DBG1) Dbg.write("Acceleration after cohesion: " + acceleration);
    }

    // Rule #4: avoid visible obstacles
    if (visibleObstacles.size() > 0)
    {
      acceleration.addVector(avoidObstacles());
      if (Dbg.DBG1) Dbg.write("Acceleration after avoiding obstacles: " + acceleration);
    }

    // Rule #4 also: avoid visible projectiles heading towards this member
    if (visibleProjectiles.size() > 0)
    {
      acceleration.addVector(avoidProjectiles());
      if (Dbg.DBG1) Dbg.write("Acceleration after avoiding projectiles: " + acceleration);
    }

    // Extra rule: keep distance between you and member of enemy flock
    if (visibleEnemies.size() > 0)
    {
      acceleration.addVector(keepAwayFromEnemies());
      if (Dbg.DBG1) Dbg.write("Acceleration after keeping distance from enemies: " + acceleration);
    }

    acceleration.addVector(moveToDestination());
    if (Dbg.DBG1) Dbg.write("Acceleration after moving towards destination: " + acceleration);

    if (acceleration.magnitude() > MAXIMUM_CHANGE)
    {
      acceleration.adjustVectorToMagnitude(MAXIMUM_CHANGE);
      if (Dbg.DBG1) Dbg.write("Acceleration after adjusting it to maximum change: " + acceleration);
    }

    speed.setVector(speed.getX() + acceleration.getX(),
                    speed.getY() + acceleration.getY());

    if (Dbg.DBG1) Dbg.write("Speed changed to: " + speed);

    // Adjust to max speed
    speed.adjustVectorToMagnitude(memberData.getMaxSpeed());
    if (Dbg.DBG1) Dbg.write("Speed after applying max speed: " + speed);

    // Calculate point which we'll reach if we move with the calculated speed
    // in the defined time
    position.setPoint(getPosition().getX() + (speed.getX() * MOVEMENT_TIME / 1000),
                      getPosition().getY() + (speed.getY() * MOVEMENT_TIME / 1000));

    if (Dbg.DBG1) Dbg.write("Current destination: " + position);

    createCommands(position);
  }

  /**
   * Updates list with visible flock members.
   */
  private void updateVisibleMembersList()
  {
    clearVisibleMembers();

    for (Integer memberId : members.keySet())
    {
      FlockMember flockMember = members.get(memberId);
      if ((flockMember != this) && (AlgorithmUtils.isObjectWithinRange(getPosition().getX(),
                                                                            getPosition().getY(),
                                                                            memberData.getSightRange(),
                                                                            flockMember.getData())))
      {
        visibleMembers.add(flockMember);

        double distance = MathUtils.calculateDistance(getPosition().getX(),
                                                      getPosition().getY(),
                                                      flockMember.getPosition().getX(),
                                                      flockMember.getPosition().getY());

        if (distance < nearestMemberDistance)
        {
          nearestMemberDistance = distance;
          nearestMember         = flockMember;
        }
      }
    }
  }

  private void clearVisibleMembers()
  {
    visibleMembers.clear();
    nearestMember = null;
    nearestMemberDistance = Double.POSITIVE_INFINITY;
  }

  /**
   * Updates list with visible obstacles.
   */
  private void updateVisibleObstaclesList()
  {
    clearVisibleObstacles();

    for (int i = 0; i < obstacles.length; i++)
    {
      TileData obstacle = obstacles[i];

      if (AlgorithmUtils.isObjectWithinRange(getPosition().getX(),
                                                  getPosition().getY(),
                                                  memberData.getSightRange(),
                                                  obstacle))
      {
        visibleObstacles.add(obstacle);

        double distance = MathUtils.calculateDistance(getPosition().getX(),
                                                      getPosition().getY(),
                                                      obstacle.getX(),
                                                      obstacle.getY());

        if (distance < nearestObstacleDistance)
        {
          nearestObstacleDistance = distance;
          nearestObstacle         = obstacle;
        }
      }
    }
  }

  private void clearVisibleObstacles()
  {
    visibleObstacles.clear();
    nearestObstacle = null;
    nearestObstacleDistance = Double.POSITIVE_INFINITY;
  }

  /**
   * Updates list of visible projectiles heading towards this flock's member.
   */
  private void updateVisibleHeadingProjectilesList()
  {
    clearVisibleProjectiles();

    if (flockData.getProjectilesData().getProjectilesCount() > 0)
    {
      ProjectileData[] projectileData = flockData.getProjectilesData().getProjectilesData();

      for (int i = 0; i < projectileData.length; i++)
      {
        ProjectileData projectile = projectileData[i];

        if (AlgorithmUtils.isObjectWithinRange(getPosition().getX(),
                                                    getPosition().getY(),
                                                    memberData.getSightRange(),
                                                    projectile))
        {
          if (AlgorithmUtils.isObjectOnObjectWay(getPosition().getX(),
                                                 getPosition().getY(),
                                                 memberData.getSize(),
                                                 projectile))
          {
            visibleProjectiles.add(projectile);

            double distance = MathUtils.calculateDistance(getPosition().getX(),
                                                          getPosition().getY(),
                                                          projectile.getX(),
                                                          projectile.getY());

            if (distance < nearestProjectileDistance)
            {
              nearestProjectileDistance = distance;
              nearestProjectile         = projectile;
            }
          }
        }
      }
    }
  }

  private void clearVisibleProjectiles()
  {
    visibleProjectiles.clear();
    nearestProjectile = null;
    nearestProjectileDistance = Double.POSITIVE_INFINITY;
  }

  /**
   * Updates list of visible enemies.
   */
  private void updateVisibleEnemiesList()
  {
    clearVisibleEnemies();

    int size = flockData.getEnemiesData().getVisibleEnemiesCount();
    if (size > 0)
    {
      for (int i = 0; i < size; i++)
      {
        EnemyAgentData enemy = flockData.getEnemiesData().getVisibleEnemyData(i);

        if (AlgorithmUtils.isObjectWithinRange(getPosition().getX(),
                                               getPosition().getY(),
                                               memberData.getSightRange(),
                                               enemy))
        {
          visibleEnemies.add(enemy);

          double distance = MathUtils.calculateDistance(getPosition().getX(),
                                                        getPosition().getY(),
                                                        enemy.getX(),
                                                        enemy.getY());

          if (distance < nearestEnemyDistance)
          {
            nearestEnemyDistance = distance;
            nearestEnemy         = enemy;
          }
        }
      }
    }
  }

  private void clearVisibleEnemies()
  {
    visibleEnemies.clear();
    nearestEnemy = null;
    nearestEnemyDistance = Double.POSITIVE_INFINITY;
  }

  private Vector2d applySeparationRule()
  {
    double ratio = nearestMemberDistance / PREFERRED_SEPARATION_DISTANCE;

    // Adjust ratio
    if (ratio < MINIMUM_URGENCY) ratio = MINIMUM_URGENCY;
    if (ratio > MAXIMUM_URGENCY) ratio = MAXIMUM_URGENCY;

    // Compute vector towards nearest flock member
    Vector2d change = new Vector2d(nearestMember.getPosition().getX() - getPosition().getX(),
                                   nearestMember.getPosition().getY() - getPosition().getY());

    if (nearestMemberDistance < PREFERRED_SEPARATION_DISTANCE)
    {
      change.adjustVectorToMagnitude(-ratio);
    }
    else if (nearestMemberDistance > PREFERRED_SEPARATION_DISTANCE)
    {
      change.adjustVectorToMagnitude(ratio);
    }
    else
    {
      change.setVector(0, 0);
    }

    return change;
  }

  private Vector2d applyAlignmentRule()
  {
    Vector2d change = new Vector2d(nearestMember.getSpeed().getX(),
                                   nearestMember.getSpeed().getY());

    change.adjustVectorToMagnitude(MINIMUM_URGENCY);

    return change;
  }

  private Vector2d applyCohesionRule()
  {
    Vector2d center = new Vector2d(0, 0);

    for (int i = 0, size = visibleMembers.size(); i < size; i++)
    {
      FlockMember member = visibleMembers.get(i);
      center.addVector(member.getPosition().getX(), member.getPosition().getY());
    }

    center.divideBy(visibleMembers.size());

    Vector2d change = new Vector2d(center.getX() - getPosition().getX(),
                                   center.getY() - getPosition().getY());

    change.adjustVectorToMagnitude(MINIMUM_URGENCY);

    return change;
  }

  private Vector2d avoidObstacles()
  {
    Vector2d change = new Vector2d(getPosition().getX() - (nearestObstacle.getX() + (nearestObstacle.getSize().getWidth() >> 1)),
                                   getPosition().getY() - (nearestObstacle.getY() + (nearestObstacle.getSize().getHeight() >> 1)));

    change.adjustVectorToMagnitude(MAXIMUM_URGENCY);

    return change;
  }

  private Vector2d avoidProjectiles()
  {
    Vector2d change = new Vector2d(getPosition().getX() - (nearestProjectile.getX() + (nearestProjectile.getSize().getWidth() >> 1)),
                                   getPosition().getY() - (nearestProjectile.getY() + (nearestProjectile.getSize().getHeight() >> 1)));

    change.adjustVectorToMagnitude(MAXIMUM_URGENCY);

    return change;
  }

  private Vector2d keepAwayFromEnemies()
  {
    double ratio = nearestEnemyDistance / PREFERRED_KEEP_AWAY_DISTANCE;

    // Adjust ratio
    if (ratio < MINIMUM_URGENCY) ratio = MINIMUM_URGENCY;
    if (ratio > MAXIMUM_URGENCY) ratio = MAXIMUM_URGENCY;

    // Compute vector towards nearest enemy flock member
    Vector2d change = new Vector2d((nearestEnemy.getX() + (nearestEnemy.getSize().getWidth() >> 1)) - getPosition().getX(),
                                   (nearestEnemy.getY() + (nearestEnemy.getSize().getHeight() >> 1)) - getPosition().getY());

    if (nearestEnemyDistance < PREFERRED_KEEP_AWAY_DISTANCE)
    {
      change.adjustVectorToMagnitude(-ratio);
    }
    else if (nearestEnemyDistance > PREFERRED_KEEP_AWAY_DISTANCE)
    {
      change.adjustVectorToMagnitude(ratio);
    }
    else
    {
      change.setVector(0, 0);
    }

    return change;
  }

  private Vector2d moveToDestination()
  {
    // Compute vector towards destination
    Vector2d change = new Vector2d(destination.getX() - getPosition().getX(),
                                   destination.getY() - getPosition().getY());

    if (Dbg.DBG1) Dbg.write("Change vector towards destination: " + change);

    change.adjustVectorToMagnitude(MAXIMUM_CHANGE);

    if (Dbg.DBG1) Dbg.write("Change vector towards destination after altering it: " + change);

    return change;
  }

  private void createCommands(Point2d currentDestination)
  {
    builder.addCommand(memberData.getId(), CommandTypesUtils.createClearQueueCommand());

    if (isAttackPossible())
    {
      builder.addCommand(memberData.getId(), CommandTypesUtils.createAttackCommand(nearestEnemy.getX() + (nearestEnemy.getSize().getWidth() >> 1),
                                                                                   nearestEnemy.getY() + (nearestEnemy.getSize().getHeight() >> 1)));
      reloadTimer.reset();
    }

    builder.addCommand(memberData.getId(), CommandTypesUtils.createMoveCommand(currentDestination.getX(),
                                                                               currentDestination.getY()));
  }

  private boolean isAttackPossible()
  {
    return reloadTimer.isActionTime() && (visibleEnemies.size() > 0) &&
           AlgorithmUtils.isObjectWithinRange(position.getX(), position.getY(),
                                              memberData.getWeapon().getRange(),
                                              nearestEnemy);
  }

  /**
   * Sets the specified destination for this flock member.
   *
   * @param newDestination the specified destination.
   */
  public void setDestination(Point2d newDestination)
  {
    if (newDestination == null)
    {
      throw new NullPointerException("Specified newDestination is null!");
    }

    destination = newDestination;
  }

  /**
   * Gets this member's data.
   *
   * @return Returns this member's data.
   */
  public ControlledAgentData getData()
  {
    return memberData;
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
    position.setPoint(positionX + (memberData.getSize().getWidth() >> 1),
                      positionY + (memberData.getSize().getHeight() >> 1));
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

  /**
   * Gets position of this flock member.
   *
   * @return Returns position of this flock member.
   */
  private Point2d getPosition()
  {
    return position;
  }

  /**
   * Gets speed of this flock member.
   *
   * @return Returns speed of this flock member.
   */
  private Vector2d getSpeed()
  {
    return speed;
  }

  // Alternative method of dealing with the obstacles
//  private void bounce()
//  {
//    updateVisibleMembersList();
//
//    updateVisibleObstaclesList();
//
//    updateVisibleEnemiesList();
//
//    double nearestX = 0;
//    double nearestY = 0;
//
//    if (nearestEnemyDistance < nearestMemberDistance)
//    {
//      if (nearestEnemyDistance < nearestObstacleDistance)
//      {
//        if (visibleEnemies.size() > 0)
//        {
//          nearestX = nearestEnemy.getX();
//          nearestY = nearestEnemy.getY();
//
//          Dbg.write("nearest is enemy with x,y=" + nearestX + ", " + nearestY);
//        }
//        else
//        {
//          Dbg.error("No enemies!!!");
//        }
//      }
//      else
//      {
//        if (visibleObstacles.size() > 0)
//        {
//          nearestX = nearestObstacle.getX();
//          nearestY = nearestObstacle.getY();
//
//          Dbg.write("nearest is obstacle with x,y=" + nearestX + ", " + nearestY);
//        }
//        else
//        {
//          Dbg.error("No obstacles!!!");
//        }
//      }
//    }
//    else
//    {
//      if (nearestMemberDistance < nearestObstacleDistance)
//      {
//        if (visibleMembers.size() > 0)
//        {
//          nearestX = nearestMember.getPosition().getX();
//          nearestY = nearestMember.getPosition().getY();
//
//          Dbg.write("nearest is member with x,y=" + nearestX + ", " + nearestY);
//        }
//        else
//        {
//          Dbg.error("No members!");
//        }
//      }
//      else
//      {
//        if (visibleObstacles.size() > 0)
//        {
//          nearestX = nearestObstacle.getX();
//          nearestY = nearestObstacle.getY();
//
//          Dbg.write("nearest is obstacle with x,y=" + nearestX + ", " + nearestY);
//        }
//        else
//        {
//          Dbg.error("No obstacles!!!");
//        }
//      }
//    }
//
//
//    speed.setVector(position.getX() - nearestX,
//                    position.getY() - nearestY);
//
//    Dbg.write("vector against obstacle is: " + speed.getX() + ", " + speed.getY());
//
//    speed.adjustVectorToMagnitude(memberData.getMaxSpeed());
//
//    position.setPoint(getPosition().getX() + (speed.getX() * MOVEMENT_TIME / 1000),
//                      getPosition().getY() + (speed.getY() * MOVEMENT_TIME / 1000));
//
//    Dbg.write("new position is: " + position.getX() + ", " + position.getY());
//
//    createCommands(position);
//  }
}