/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.event.types;

import net.java.dante.sim.event.EventType;


/**
 * Implementation of {@link EnemyAgentMoveEvent} interface.
 *
 * @author M.Olszewski
 */
class EnemyAgentMoveEventImpl extends AbstractEnemyAgentEvent implements EnemyAgentMoveEvent
{
  /** Object holding data for performing movement. */
  private ObjectMovement movement;


  /**
   * Creates instance of {@link EnemyAgentMoveEventImpl} class.
   *
   * @param eventId event's identifier.
   * @param eventTime event's time.
   * @param enemyAgentId enemy agent's identifier.
   * @param enemyDestinationX enemy agent's destination 'x' coordinate.
   * @param enemyDestinationY enemy agent's destination 'y' coordinate.
   * @param enemySpeedX enemy agent's horizontal speed.
   * @param enemySpeedY enemy agent's vertical speed.
   */
  EnemyAgentMoveEventImpl(int eventId, long eventTime, int enemyAgentId,
                          double enemyDestinationX, double enemyDestinationY,
                          double enemySpeedX, double enemySpeedY)
  {
    super(eventId, eventTime, EventType.ENEMY_AGENT_MOVE, enemyAgentId);

    movement = new ObjectMovement(enemyDestinationX, enemyDestinationY,
        enemySpeedX, enemySpeedY);
  }


  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getDestinationX()
   */
  public double getDestinationX()
  {
    return movement.getDestinationX();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getDestinationY()
   */
  public double getDestinationY()
  {
    return movement.getDestinationY();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getSpeedX()
   */
  public double getSpeedX()
  {
    return movement.getSpeedX();
  }

  /**
   * @see net.java.dante.sim.event.types.ObjectMoveEventParams#getSpeedY()
   */
  public double getSpeedY()
  {
    return movement.getSpeedY();
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = super.hashCode();

    result = PRIME * result + movement.hashCode();

    return result;
  }

  /**
   * @see net.java.dante.sim.event.types.AbstractEnemyAgentEvent#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof EnemyAgentMoveEvent))
    {
      final EnemyAgentMoveEvent other = (EnemyAgentMoveEvent) object;
      equal = ((Double.doubleToLongBits(getDestinationX()) == Double.doubleToLongBits(other.getDestinationX())) &&
               (Double.doubleToLongBits(getDestinationY()) == Double.doubleToLongBits(other.getDestinationY())) &&
               (Double.doubleToLongBits(getSpeedX()) == Double.doubleToLongBits(other.getSpeedX())) &&
               (Double.doubleToLongBits(getSpeedY()) == Double.doubleToLongBits(other.getSpeedY())));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; movement=" + movement + "]");
  }
}