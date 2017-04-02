/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.DistanceTracker;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.state.ObjectState;

/**
 * Class representing projectiles on server side of simulation.
 *
 * @author M.Olszewski
 */
public class ServerProjectile extends SimulationObject
{
  /** State of this projectile. */
  private ServerProjectileState state;
  /** Tracks whether destination point was reached. */
  private DistanceTracker tracker;
  /** Objects mediator. */
  private ObjectsMediator mediator;

  
  /**
   * Creates instance of {@link ServerProjectile} class.
   * 
   * @param projectileState - projectile's state.
   * @param objectsMediator - objects mediator.
   */
  public ServerProjectile(ServerProjectileState projectileState, 
      ObjectsMediator objectsMediator)
  {
    if (projectileState == null)
    {
      throw new NullPointerException("Specified projectilesState is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }
    
    state = projectileState;
    mediator = objectsMediator;
    tracker = new DistanceTracker(projectileState.getMaxSpeed(), 
                                  projectileState.getX(), 
                                  projectileState.getY(), 
                                  projectileState.getDistance());
  }
  

  /** 
   * @see net.java.dante.sim.data.object.SimulationObject#getData()
   */
  @Override
  public ObjectState getData()
  {
    return state;
  }

  /** 
   * @see net.java.dante.sim.data.object.SimulationObject#update(long)
   */
  @Override
  public void update(long elapsedTime)
  {
    double dx = (elapsedTime * state.getSpeedX()) / 1000;
    double dy = (elapsedTime * state.getSpeedY()) / 1000;
    
    state.setX(state.getX() + dx);
    state.setY(state.getY() + dy);
    
    if (tracker.isDestinationReached(elapsedTime))
    {
      mediator.objectDestroyed(this);
    }
  }

  /** 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[state=" + state + "; tracker=" + tracker + "]");
  }
}