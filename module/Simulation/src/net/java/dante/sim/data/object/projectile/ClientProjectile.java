/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.projectile;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.EventDrivenSimulationObject;
import net.java.dante.sim.data.object.MovementStateMachine;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventType;
import net.java.dante.sim.event.types.ProjectileDestroyedEvent;
import net.java.dante.sim.event.types.ProjectileGoneEvent;
import net.java.dante.sim.event.types.ProjectileMoveEvent;


/**
 * Class representing projectiles on client side of simulation.
 * 
 * @author M.Olszewski
 */
public class ClientProjectile extends EventDrivenSimulationObject
{
  /** State of this projectile. */
  private ClientProjectileState state;
  /** Objects mediator. */
  private ObjectsMediator mediator;
  /** State machine responsible for moving agent. */
  private MovementStateMachine stateMachine;

  
  /**
   * Creates instance of {@link ClientProjectile} class with specified 
   * parameters.
   *
   * @param objectId - projectile's identifier.
   * @param objectsMediator - objects mediator.
   * @param projectileState - projectile's state.
   * @param currentTime - current simulation's time.
   */
  public ClientProjectile(int objectId, ClientProjectileState projectileState, 
      ObjectsMediator objectsMediator, long currentTime)
  {
    super(objectId, currentTime);
    
    if (projectileState == null)
    {
      throw new NullPointerException("Specified projectileState is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }
    
    state = projectileState;
    mediator = objectsMediator;
    stateMachine = new MovementStateMachine(projectileState);
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
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#filterEvent(net.java.dante.sim.event.Event)
   */
  @Override
  protected boolean filterEvent(Event event)
  {
    return (event.getEventType() == EventType.PROJECTILE_MOVE);
  }
  
  /**
   * @see net.java.dante.sim.data.object.SimulationObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    super.update(delta);
    
    stateMachine.update(delta);
  }

  /**
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#processEvent(net.java.dante.sim.event.Event)
   */
  @Override
  protected void processEvent(Event event)
  {
    if (event instanceof ProjectileMoveEvent)
    {
      projectileMoved((ProjectileMoveEvent)event);
    }
    else
    {
      throw new IllegalArgumentException("Invalid argument event - not supported Event interface found!");
    }
  }
  
  /**
   * Method invoked when this projectile was moved.
   * 
   * @param moveEvent - the {@link ProjectileMoveEvent} event.
   */
  private void projectileMoved(ProjectileMoveEvent moveEvent)
  {
    state.setSpeedX(moveEvent.getSpeedX());
    state.setSpeedY(moveEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
    stateMachine.startMovement(moveEvent.getDestinationX(), moveEvent.getDestinationY(),
        moveEvent.getSpeedX(), moveEvent.getSpeedY());
  }
  
  /**
   * Method invoked when this projectile is gone.
   * 
   * @param goneEvent - the {@link ProjectileGoneEvent} event.
   */
  public void projectileGone(ProjectileGoneEvent goneEvent)
  {
    state.setSpeedX(goneEvent.getSpeedX());
    state.setSpeedY(goneEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
    mediator.objectDestroyed(this);
  }
  
  /**
   * Method invoked when this projectile is destroyed.
   * 
   * @param destroyedEvent - the {@link ProjectileDestroyedEvent} event.
   */
  public void projectileDestroyed(ProjectileDestroyedEvent destroyedEvent)
  {
    state.setX(destroyedEvent.getProjectileX());
    state.setY(destroyedEvent.getProjectileY());
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    
    stateMachine.setNotMovingState();
    state.destroyed();
    mediator.objectDestroyed(this);
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[state=" + state + "; stateMachine=" + stateMachine + "]");
  }
}