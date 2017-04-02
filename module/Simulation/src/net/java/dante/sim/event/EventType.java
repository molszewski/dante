/*
 * Created on 2006-07-25
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event;


/**
 * Enumeration defining possible types of events.
 *
 * @author M.Olszewski
 */
public enum EventType
{
  /** Friendly agent move. */
  FRIENDLY_AGENT_MOVE,
  /** Friendly agent movement was blocked. */
  FRIENDLY_AGENT_BLOCKED,
  /** Friendly agent movement was finished. */
  FRIENDLY_AGENT_MOVE_FINISHED,
  /** Friendly agent was hit. */
  FRIENDLY_AGENT_HIT,
  /** Friendly agent was destroyed. */
  FRIENDLY_AGENT_DESTROYED,

  /** Enemy agent move. */
  ENEMY_AGENT_MOVE,
  /** Enemy agent was seen by friendly agent. */
  ENEMY_AGENT_SEEN,
  /** Enemy agent is no longer seen by friendly agent. */
  ENEMY_AGENT_GONE,
  /** Enemy agent was hit. */
  ENEMY_AGENT_HIT,
  /** Enemy agent was destroyed. */
  ENEMY_AGENT_DESTROYED,

  /** Projectile move. */
  PROJECTILE_MOVE,
  /** Projectile was seen by friendly agent. */
  PROJECTILE_SEEN,
  /** Projectile is no longer seen by friendly agent. */
  PROJECTILE_GONE,
  /** Projectile was shot by friendly agent. */
  PROJECTILE_SHOT,
  /** Projectile was destroyed and explosion was seen by friendly agent. */
  PROJECTILE_DESTROYED;


  /**
   * Gets {@link EventType} enumeration for the specified ordinal
   * or <code>null</code> if state for the specified ordinal does not
   * exist.
   *
   * @param ordinal - the specified ordinal.
   *
   * @return Returns {@link EventType} enumeration for the specified ordinal
   *         or <code>null</code> if state for the specified ordinal does not
   *         exist.
   */
  public static EventType getEventType(int ordinal)
  {
    EventType eventType = null;

    for (EventType e : values())
    {
      if (e.ordinal() == ordinal)
      {
        eventType = e;
        break;
      }
    }

    return eventType;
  }
}