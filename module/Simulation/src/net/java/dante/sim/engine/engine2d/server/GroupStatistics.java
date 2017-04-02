/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.engine.engine2d.server;

/**
 * Class holding statistics for the specified agents group.
 *
 * @author M.Olszewski
 */
public class GroupStatistics
{
  /** Weight used to calculate points for accuracy. */
  public final static double ACCURACY_WEIGHT = 100.0;
  /** Weight used to calculate points for spotted enemy agents. */
  public final static double ENEMY_AGENTS_VISIBILITY_WEIGHT = 0.001;
  /** Weight used to calculate points for hit enemy agents. */
  public final static double ENEMY_AGENTS_HIT_WEIGHT = 10.0;
  /** Weight used to calculate points for destroyed enemy agents. */
  public final static double ENEMY_AGENTS_DESTROYED_WEIGHT = 100.0;
  /**
   * Weight used to calculate points for friendly agents hit by
   * its agents group.
   */
  public final static double FRIENDLY_FIRE_HIT_WEIGHT = -10.0;
  /** Weight used to calculate points for damage taken by this group. */
  public final static double DAMAGE_TAKEN_WEIGHT = -0.1;
  /**
   * Weight used to calculate points for friendly agents destroyed by
   * its agents group.
   */
  public final static double FRIENDLY_FIRE_DESTROYED_WEIGHT = -200.0;
  /** Weight used to calculate points for destroyed friendly agents. */
  public final static double FRIENDLY_AGENTS_DESTROYED_WEIGHT = -50.0;


  /** Agents group's identifier. */
  private int groupId;
  /** Number of enemy agents spotted. */
  private long enemyAgentsVisible = 0;
  /** Number of updates. */
  private long updatesNumber = 0;
  /** Number of enemy agents hit. */
  private int enemyAgentsHit = 0;
  /** Number of enemy agents destroyed. */
  private int enemyAgentsDestroyed = 0;
  /** Number of damage received by this group. */
  private int damageTaken = 0;
  /** Number of destroyed friendly agents by other groups. */
  private int friendlyAgentsDestroyed = 0;
  /** Number of hits received from friendly agents. */
  private int friendlyFireHits = 0;
  /** Number of friendly agents destroyed by this group. */
  private int friendlyFireDestroyed = 0;
  /** Number of shot projectiles. */
  private int projectilesShot = 0;
  /** Number of total points. */
  private long totalPoints = 0;


  /**
   * Creates instance of {@link GroupStatistics} class with blank
   * statistics.
   *
   * @param agentsGroupId agents group's identifier.
   */
  GroupStatistics(int agentsGroupId)
  {
    if (agentsGroupId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentsGroupId - it must be an integer greater or equal to zero!");
    }

    groupId = agentsGroupId;
  }



  /**
   * Creates instance of {@link GroupStatistics} class with the specified
   * statistics.
   *
   * @param agentsGroupId agents group's identifier.
   * @param enemiesVisible number of enemies spotted in all updates.
   * @param updatesCount number of updates.
   * @param enemiesHit number of enemies hit.
   * @param enemiesDestroyed number of enemies destroyed.
   * @param friendlyFireAgentsHits number of friendly agent hits by this group.
   * @param friendlyFireAgentsDestroyed number of friendly agents hit by this group.
   * @param friendlyDestroyed number of friendly agents destroyed by enemies groups.
   * @param groupDamageTaken damage taken by this group.
   * @param shotProjectiles number of projectiles shot.
   * @param totalAmountOfPoints total amount of points for this group.
   */
  public GroupStatistics(int agentsGroupId, long enemiesVisible, long updatesCount,
                         int enemiesHit, int enemiesDestroyed,
                         int friendlyFireAgentsHits, int friendlyFireAgentsDestroyed,
                         int friendlyDestroyed, int groupDamageTaken,
                         int shotProjectiles, long totalAmountOfPoints)
  {
    groupId                 = agentsGroupId;
    enemyAgentsVisible      = enemiesVisible;
    updatesNumber           = updatesCount;
    enemyAgentsHit          = enemiesHit;
    enemyAgentsDestroyed    = enemiesDestroyed;
    friendlyFireHits        = friendlyFireAgentsHits;
    friendlyFireDestroyed   = friendlyFireAgentsDestroyed;
    friendlyAgentsDestroyed = friendlyDestroyed;
    damageTaken             = groupDamageTaken;
    projectilesShot         = shotProjectiles;
    totalPoints             = totalAmountOfPoints;
  }


  /**
   * This method should be invoked if any enemy agent is visible now by
   * some agents group.
   */
  void enemyAgentVisible()
  {
    enemyAgentsVisible++;
  }

  /**
   * This method should be invoked if any enemy agent was hit by
   * some agents group.
   */
  void enemyAgentHit()
  {
    enemyAgentsHit++;
  }

  /**
   * This method should be invoked if any enemy agent was destroyed by
   * some agents group.
   */
  void enemyAgentDestroyed()
  {
    enemyAgentsDestroyed++;
  }

  /**
   * This method should be invoked if any friendly agent was hit by
   * some agents group.
   */
  void friendlyFireHit()
  {
    friendlyFireHits++;
  }

  /**
   * This method should be invoked if any friendly agent received some
   * amount of damage.
   *
   * @param damage amount of received damage.
   */
  void damageReceived(int damage)
  {
    damageTaken += damage;
  }

  /**
   * This method should be invoked if any friendly agent was destroyed by
   * enemy agents group.
   */
  void friendlyAgentDestroyed()
  {
    friendlyAgentsDestroyed++;
  }

  /**
   * This method should be invoked if any friendly agent was destroyed by
   * its agents group.
   */
  void friendlyFireDestroyed()
  {
    friendlyFireDestroyed++;
  }

  /**
   * This method should be invoked if any projectile was shot by
   * some agents group.
   */
  void projectileShot()
  {
    projectilesShot++;
  }

  /**
   * This method should be performed after each update in which agents
   * group taken part.
   */
  void updateCompleted()
  {
    updatesNumber++;
  }

  /**
   * Calculates total points.
   */
  void calculateTotalPoints()
  {
    double result = getAccuracy() * ACCURACY_WEIGHT;
    result       += enemyAgentsVisible * ENEMY_AGENTS_VISIBILITY_WEIGHT;
    result       += enemyAgentsHit * ENEMY_AGENTS_HIT_WEIGHT;
    result       += enemyAgentsDestroyed * ENEMY_AGENTS_DESTROYED_WEIGHT;
    result       += friendlyFireHits * FRIENDLY_FIRE_HIT_WEIGHT;
    result       += friendlyFireDestroyed * FRIENDLY_FIRE_DESTROYED_WEIGHT;
    result       += friendlyAgentsDestroyed * FRIENDLY_AGENTS_DESTROYED_WEIGHT;
    result       += damageTaken * DAMAGE_TAKEN_WEIGHT;

    totalPoints = (long)result;
  }

  /**
   * Gets identifier of this agents group.
   *
   * @return Returns identifier of this agents group.
   */
  public int getGroupId()
  {
    return groupId;
  }

  /**
   * Gets the total number of enemy agents visible during all updates.
   *
   * @return Returns the total number of enemy agents visible during all updates.
   */
  public long getEnemyAgentsVisible()
  {
    return enemyAgentsVisible;
  }

  /**
   * Gets the count of enemy agents hits.
   *
   * @return Returns the count of enemy agents hits.
   */
  public int getEnemyAgentsHit()
  {
    return enemyAgentsHit;
  }

  /**
   * Gets the count of enemy agents destroyed.
   *
   * @return Returns the count of enemy agents destroyed.
   */
  public int getEnemyAgentsDestroyed()
  {
    return enemyAgentsDestroyed;
  }

  /**
   * Gets the count of friendly agents hits.
   *
   * @return Returns the count of friendly agents hits.
   */
  public int getFriendlyFireHits()
  {
    return friendlyFireHits;
  }

  /**
   * Gets the amount of damage taken by this group.
   *
   * @return Returns the amount of damage taken by this group.
   */
  public int getDamageTaken()
  {
    return damageTaken;
  }

  /**
   * Gets the count of friendly agents destroyed by friendly agents.
   *
   * @return Returns the count of friendly agents destroyed by friendly agents.
   */
  public int getFriendlyFireDestroyed()
  {
    return friendlyFireDestroyed;
  }

  /**
   * Gets the number of destroyed friendly agents.
   *
   * @return Returns the number of destroyed friendly agents.
   */
  public int getFriendlyAgentsDestroyed()
  {
    return friendlyAgentsDestroyed;
  }

  /**
   * Gets the count of shot projectiles.
   *
   * @return Returns the count of shot projectiles.
   */
  public int getProjectilesShot()
  {
    return projectilesShot;
  }

  /**
   * Gets the count of updates.
   *
   * @return Returns the count of updates.
   */
  public long getUpdatesCount()
  {
    return updatesNumber;
  }

  /**
   * Gets the average number of enemy agents seen in each update.
   *
   * @return Returns the average number of enemy agents seen in each update.
   */
  public double getEnemyAgentsAverageVisibility()
  {
    double average = 0;
    if (updatesNumber != 0)
    {
      average = ((double)(enemyAgentsVisible >> 1)) / ((double)updatesNumber);
    }

    return average;
  }

  /**
   * Calculates accuracy of this enemy agents group.
   *
   * @return Returns accuracy of this enemy agents group.
   */
  public double getAccuracy()
  {
    double accuracy = 0;
    if (projectilesShot != 0)
    {
      accuracy = ((double)enemyAgentsHit) / ((double)projectilesShot);
    }

    return accuracy;
  }

  /**
   * Gets the total points.
   *
   * @return Returns the total points.
   */
  public long getTotalPoints()
  {
    return totalPoints;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + groupId;
    result = PRIME * result + enemyAgentsDestroyed;
    result = PRIME * result + enemyAgentsHit;
    result = PRIME * result + damageTaken;
    result = PRIME * result + friendlyFireHits;
    result = PRIME * result + friendlyFireDestroyed;
    result = PRIME * result + friendlyAgentsDestroyed;
    result = PRIME * result + projectilesShot;
    result = PRIME * result + (int) (enemyAgentsVisible ^ (enemyAgentsVisible >>> 32));
    result = PRIME * result + (int) (updatesNumber ^ (updatesNumber>>> 32));
    result = PRIME * result + (int) (totalPoints ^ (totalPoints >>> 32));

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GroupStatistics))
    {
      final GroupStatistics other = (GroupStatistics) object;
      equal = ((groupId == other.groupId) &&
               (enemyAgentsVisible == other.enemyAgentsVisible) &&
               (updatesNumber == other.updatesNumber) &&
               (enemyAgentsHit == other.enemyAgentsHit) &&
               (enemyAgentsDestroyed == other.enemyAgentsDestroyed) &&
               (friendlyFireHits == other.friendlyFireHits) &&
               (friendlyFireDestroyed == other.friendlyFireDestroyed) &&
               (friendlyAgentsDestroyed == other.friendlyAgentsDestroyed) &&
               (damageTaken == other.damageTaken) &&
               (projectilesShot == other.projectilesShot) &&
               (totalPoints == other.totalPoints));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[groupId=" + groupId +
        "; enemyAgentsVisible=" + enemyAgentsVisible +
        "; updatesNumber=" + updatesNumber +
        "; enemyAgentsHit=" + enemyAgentsHit +
        "; enemyAgentsDestroyed=" + enemyAgentsDestroyed +
        "; friendlyFireHits=" + friendlyFireHits +
        "; friendlyFireDestroyed=" + friendlyFireDestroyed +
        "; friendlyAgentsDestroyed=" + friendlyAgentsDestroyed +
        "; damageTaken=" + damageTaken +
        "; projectilesShot=" + projectilesShot +
        "; totalPoints=" + totalPoints + "]");
  }
}