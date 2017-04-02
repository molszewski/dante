/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.util.SequenceGenerator;


/**
 * Utility class delivering following instances of {@link net.java.dante.sim.event.Event}
 * implementations:
 * <ol>
 * <li>{@link EnemyAgentGoneEvent},
 * <li>{@link EnemyAgentHitEvent},
 * <li>{@link EnemyAgentDestroyedEvent},
 * <li>{@link EnemyAgentMoveEvent},
 * <li>{@link EnemyAgentSeenEvent},
 * <li>{@link FriendlyAgentHitEvent},
 * <li>{@link FriendlyAgentDestroyedEvent},
 * <li>{@link FriendlyAgentMoveEvent},
 * <li>{@link FriendlyAgentMoveFinishedEvent},
 * <li>{@link ProjectileGoneEvent},
 * <li>{@link ProjectileMoveEvent},
 * <li>{@link ProjectileSeenEvent},
 * <li>{@link ProjectileShotEvent},
 * <li>{@link ProjectileDestroyedEvent}
 * </ol>
 *
 * @author M.Olszewski
 */
public final class EventTypesUtils
{
  /** Identifiers generator. */
  private static final SequenceGenerator idGen = SequenceGenerator.synchronizedSequenceGenerator();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private EventTypesUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Creates {@link EnemyAgentGoneEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentGoneEvent} object.
   */
  public static EnemyAgentGoneEvent createEnemyAgentGoneEvent(
      long eventTime, int enemyId,
      double enemyDestinationX, double enemyDestinationY,
      double enemySpeedX, double enemySpeedY)
  {
    return createEnemyAgentGoneEvent(idGen.generateId(), eventTime, enemyId,
                                     enemyDestinationX, enemyDestinationY,
                                     enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link EnemyAgentGoneEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentGoneEvent} object.
   */
  public static EnemyAgentGoneEvent createEnemyAgentGoneEvent(
      int eventId, long eventTime, int enemyId,
      double enemyDestinationX, double enemyDestinationY,
      double enemySpeedX, double enemySpeedY)
  {
    return new EnemyAgentGoneEventImpl(eventId, eventTime, enemyId,
                                       enemyDestinationX, enemyDestinationY,
                                       enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link EnemyAgentHitEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link EnemyAgentHitEvent} object.
   */
  public static EnemyAgentHitEvent createEnemyAgentHitEvent(long eventTime,
                                                            int enemyId,
                                                            int shooterId)
  {
    return createEnemyAgentHitEvent(idGen.generateId(), eventTime, enemyId, shooterId);
  }

  /**
   * Creates {@link EnemyAgentHitEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link EnemyAgentHitEvent} object.
   */
  public static EnemyAgentHitEvent createEnemyAgentHitEvent(int eventId,
                                                            long eventTime,
                                                            int enemyId,
                                                            int shooterId)
  {
    return new EnemyAgentHitEventImpl(eventId, eventTime, enemyId, shooterId);
  }

  /**
   * Creates {@link EnemyAgentDestroyedEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link EnemyAgentDestroyedEvent} object.
   */
  public static EnemyAgentDestroyedEvent createEnemyAgentDestroyedEvent(long eventTime,
                                                                        int enemyId,
                                                                        int shooterId)
  {
    return createEnemyAgentDestroyedEvent(idGen.generateId(),
                                          eventTime,
                                          enemyId,
                                          shooterId);
  }

  /**
   * Creates {@link EnemyAgentDestroyedEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link EnemyAgentDestroyedEvent} object.
   */
  public static EnemyAgentDestroyedEvent createEnemyAgentDestroyedEvent(int eventId,
                                                                        long eventTime,
                                                                        int enemyId,
                                                                        int shooterId)
  {
    return new EnemyAgentDestroyedEventImpl(eventId, eventTime, enemyId, shooterId);
  }

  /**
   * Creates {@link EnemyAgentMoveEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentMoveEvent} object.
   */
  public static EnemyAgentMoveEvent createEnemyAgentMoveEvent(long eventTime, int enemyId,
                                                               double enemyDestinationX, double enemyDestinationY,
                                                               double enemySpeedX, double enemySpeedY)
  {
    return createEnemyAgentMoveEvent(idGen.generateId(), eventTime, enemyId,
                                     enemyDestinationX, enemyDestinationY,
                                     enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link EnemyAgentMoveEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentMoveEvent} object.
   */
  public static EnemyAgentMoveEvent createEnemyAgentMoveEvent(int eventId, long eventTime, int enemyId,
                                                               double enemyDestinationX, double enemyDestinationY,
                                                               double enemySpeedX, double enemySpeedY)
  {
    return new EnemyAgentMoveEventImpl(eventId, eventTime, enemyId,
                                       enemyDestinationX, enemyDestinationY,
                                       enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link EnemyAgentSeenEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentSeenEvent} object.
   */
  public static EnemyAgentSeenEvent createEnemyAgentSeenEvent(long eventTime, int enemyId,
                                                              double enemyDestinationX, double enemyDestinationY,
                                                              double enemySpeedX, double enemySpeedY)
  {
    return createEnemyAgentSeenEvent(idGen.generateId(), eventTime, enemyId,
                                     enemyDestinationX, enemyDestinationY,
                                     enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link EnemyAgentSeenEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   *
   * @return Returns created {@link EnemyAgentSeenEvent} object.
   */
  public static EnemyAgentSeenEvent createEnemyAgentSeenEvent(int eventId, long eventTime, int enemyId,
                                                              double enemyDestinationX, double enemyDestinationY,
                                                              double enemySpeedX, double enemySpeedY)
  {
    return new EnemyAgentSeenEventImpl(eventId, eventTime, enemyId,
                                       enemyDestinationX, enemyDestinationY,
                                       enemySpeedX, enemySpeedY);
  }

  /**
   * Creates {@link FriendlyAgentBlockedEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param blockedX blocked agent's 'x' coordinate.
   * @param blockedY blocked agent's 'y' coordinate.
   *
   * @return Returns created {@link FriendlyAgentBlockedEvent} object.
   */
  public static FriendlyAgentBlockedEvent createFriendlyAgentBlockedEvent(long eventTime, int friendlyAgentId,
                                                                          double blockedX, double blockedY)
  {
    return createFriendlyAgentBlockedEvent(idGen.generateId(), eventTime, friendlyAgentId,
                                           blockedX, blockedY);
  }

  /**
   * Creates {@link FriendlyAgentBlockedEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param blockedX blocked agent's 'x' coordinate.
   * @param blockedY blocked agent's 'y' coordinate.
   *
   * @return Returns created {@link FriendlyAgentBlockedEvent} object.
   */
  public static FriendlyAgentBlockedEvent createFriendlyAgentBlockedEvent(int eventId, long eventTime, int friendlyAgentId,
                                                                          double blockedX, double blockedY)
  {
    return new FriendlyAgentBlockedEventImpl(eventId, eventTime, friendlyAgentId,
                                             blockedX, blockedY);
  }

  /**
   * Creates {@link FriendlyAgentHitEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param damageAmount amount of received damage.
   *
   * @return Returns created {@link FriendlyAgentHitEvent} object.
   */
  public static FriendlyAgentHitEvent createFriendlyAgentHitEvent(long eventTime,
                                                                  int friendlyAgentId,
                                                                  int damageAmount)
  {
    return createFriendlyAgentHitEvent(idGen.generateId(), eventTime,
                                       friendlyAgentId, damageAmount);
  }

  /**
   * Creates {@link FriendlyAgentHitEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param damageAmount amount of received damage.
   *
   * @return Returns created {@link FriendlyAgentHitEvent} object.
   */
  public static FriendlyAgentHitEvent createFriendlyAgentHitEvent(int eventId, long eventTime,
                                                                  int friendlyAgentId, int damageAmount)
  {
    return new FriendlyAgentHitEventImpl(eventId, eventTime,
                                         friendlyAgentId, damageAmount);
  }

  /**
   * Creates {@link FriendlyAgentDestroyedEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   *
   * @return Returns created {@link FriendlyAgentDestroyedEvent} object.
   */
  public static FriendlyAgentDestroyedEvent createFriendlyAgentDestroyedEvent(long eventTime,
                                                                              int friendlyAgentId)
  {
    return createFriendlyAgentDestroyedEvent(idGen.generateId(),
                                             eventTime,
                                             friendlyAgentId);
  }

  /**
   * Creates {@link FriendlyAgentDestroyedEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   *
   * @return Returns created {@link FriendlyAgentDestroyedEvent} object.
   */
  public static FriendlyAgentDestroyedEvent createFriendlyAgentDestroyedEvent(int eventId,
                                                                              long eventTime,
                                                                              int friendlyAgentId)
  {
    return new FriendlyAgentDestroyedEventImpl(eventId,
                                               eventTime,
                                               friendlyAgentId);
  }

  /**
   * Creates {@link FriendlyAgentMoveEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param destinationX agent's destination 'x' coordinate.
   * @param destinationY agent's destination 'y' coordinate.
   * @param agentSpeedX agent's horizontal speed.
   * @param agentSpeedY agent's vertical speed.
   *
   * @return Returns created {@link FriendlyAgentMoveEvent} object.
   */
  public static FriendlyAgentMoveEvent createFriendlyAgentMoveEvent(long eventTime, int friendlyAgentId,
                                                                     double destinationX, double destinationY,
                                                                     double agentSpeedX, double agentSpeedY)
  {
    return createFriendlyAgentMoveEvent(idGen.generateId(), eventTime, friendlyAgentId,
                                        destinationX, destinationY,
                                        agentSpeedX, agentSpeedY);
  }

  /**
   * Creates {@link FriendlyAgentMoveEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   * @param destinationX agent's destination 'x' coordinate.
   * @param destinationY agent's destination 'y' coordinate.
   * @param agentSpeedX agent's horizontal speed.
   * @param agentSpeedY agent's vertical speed.
   *
   * @return Returns created {@link FriendlyAgentMoveEvent} object.
   */
  public static FriendlyAgentMoveEvent createFriendlyAgentMoveEvent(int eventId, long eventTime, int friendlyAgentId,
                                                                    double destinationX, double destinationY,
                                                                    double agentSpeedX, double agentSpeedY)
  {
    return new FriendlyAgentMoveEventImpl(eventId, eventTime, friendlyAgentId,
                                          destinationX, destinationY,
                                          agentSpeedX, agentSpeedY);
  }

  /**
   * Creates {@link FriendlyAgentMoveFinishedEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   *
   * @return Returns created {@link FriendlyAgentMoveFinishedEvent} object.
   */
  public static FriendlyAgentMoveFinishedEvent createFriendlyAgentMoveFinishedEvent(long eventTime,
                                                                                    int friendlyAgentId)
  {
    return createFriendlyAgentMoveFinishedEvent(idGen.generateId(),
                                                eventTime,
                                                friendlyAgentId);
  }

  /**
   * Creates {@link FriendlyAgentMoveFinishedEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param friendlyAgentId friendly agent's identifier.
   *
   * @return Returns created {@link FriendlyAgentDestroyedEvent} object.
   */
  public static FriendlyAgentMoveFinishedEvent createFriendlyAgentMoveFinishedEvent(int eventId,
                                                                                    long eventTime,
                                                                                    int friendlyAgentId)
  {
    return new FriendlyAgentMoveFinishedEventImpl(eventId,
                                                  eventTime,
                                                  friendlyAgentId);
  }

  /**
   * Creates {@link ProjectileGoneEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileGoneEvent} object.
   */
  public static ProjectileGoneEvent createProjectileGoneEvent(long eventTime, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return createProjectileGoneEvent(idGen.generateId(), eventTime, projectileId,
                                     projectileDestinationX, projectileDestinationY,
                                     projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileGoneEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileGoneEvent} object.
   */
  public static ProjectileGoneEvent createProjectileGoneEvent(int eventId, long eventTime, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return new ProjectileGoneEventImpl(eventId, eventTime, projectileId,
                                       projectileDestinationX, projectileDestinationY,
                                       projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileMoveEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileMoveEvent} object.
   */
  public static ProjectileMoveEvent createProjectileMoveEvent(long eventTime, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return createProjectileMoveEvent(idGen.generateId(), eventTime, projectileId,
                                     projectileDestinationX, projectileDestinationY,
                                     projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileMoveEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileMoveEvent} object.
   */
  public static ProjectileMoveEvent createProjectileMoveEvent(int eventId, long eventTime, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return new ProjectileMoveEventImpl(eventId, eventTime, projectileId,
                                       projectileDestinationX, projectileDestinationY,
                                       projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileSeenEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param projectileType projectile's type.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileSeenEvent} object.
   */
  public static ProjectileSeenEvent createProjectileSeenEvent(long eventTime, String projectileType, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return createProjectileSeenEvent(idGen.generateId(), eventTime,
                                     projectileType, projectileId,
                                     projectileDestinationX, projectileDestinationY,
                                     projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileSeenEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileType projectile's type.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   *
   * @return Returns created {@link ProjectileSeenEvent} object.
   */
  public static ProjectileSeenEvent createProjectileSeenEvent(int eventId, long eventTime,
                                                              String projectileType, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY)
  {
    return new ProjectileSeenEventImpl(eventId, eventTime,
                                       projectileType, projectileId,
                                       projectileDestinationX, projectileDestinationY,
                                       projectileSpeedX, projectileSpeedY);
  }

  /**
   * Creates {@link ProjectileShotEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param projectileType projectile's type.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link ProjectileShotEvent} object.
   */
  public static ProjectileShotEvent createProjectileShotEvent(long eventTime, String projectileType, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY,
                                                              int shooterId)
  {
    return createProjectileShotEvent(idGen.generateId(), eventTime,
                                     projectileType, projectileId,
                                     projectileDestinationX, projectileDestinationY,
                                     projectileSpeedX, projectileSpeedY,
                                     shooterId);
  }

  /**
   * Creates {@link ProjectileShotEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileType projectile's type.
   * @param projectileId projectile's identifier.
   * @param projectileDestinationX projectile's 'x' coordinate.
   * @param projectileDestinationY projectile's 'y' coordinate.
   * @param projectileSpeedX projectile's horizontal speed.
   * @param projectileSpeedY projectile's vertical speed.
   * @param shooterId shooter's identifier.
   *
   * @return Returns created {@link ProjectileShotEvent} object.
   */
  public static ProjectileShotEvent createProjectileShotEvent(int eventId, long eventTime,
                                                              String projectileType, int projectileId,
                                                              double projectileDestinationX, double projectileDestinationY,
                                                              double projectileSpeedX, double projectileSpeedY,
                                                              int shooterId)
  {
    return new ProjectileShotEventImpl(eventId, eventTime,
                                       projectileType, projectileId,
                                       projectileDestinationX, projectileDestinationY,
                                       projectileSpeedX, projectileSpeedY,
                                       shooterId);
  }

  /**
   * Creates {@link ProjectileDestroyedEvent} object with specified parameters.
   *
   * @param eventTime event's time.
   * @param projectileId destroyed projectile's identifier.
   * @param projectileX last projectile's 'x' coordinate.
   * @param projectileY last projectile's 'y' coordinate.
   *
   * @return Returns created {@link ProjectileDestroyedEvent} object.
   */
  public static ProjectileDestroyedEvent createProjectileDestroyedEvent(long eventTime, int projectileId,
                                                                        double projectileX, double projectileY)
  {
    return createProjectileDestroyedEvent(idGen.generateId(), eventTime, projectileId,
                                          projectileX, projectileY);
  }

  /**
   * Creates {@link ProjectileDestroyedEvent} object with specified parameters.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param projectileId destroyed projectile's identifier.
   * @param projectileX last projectile's 'x' coordinate.
   * @param projectileY last projectile's 'y' coordinate.
   *
   * @return Returns created {@link ProjectileDestroyedEvent} object.
   */
  public static ProjectileDestroyedEvent createProjectileDestroyedEvent(int eventId, long eventTime, int projectileId,
                                                                        double projectileX, double projectileY)
  {
    return new ProjectileDestroyedEventImpl(eventId, eventTime, projectileId,
                                            projectileX, projectileY);
  }
}