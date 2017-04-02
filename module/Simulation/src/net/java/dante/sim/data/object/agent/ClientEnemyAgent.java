/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.EventDrivenSimulationObject;
import net.java.dante.sim.data.object.MovementStateMachine;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventType;
import net.java.dante.sim.event.types.EnemyAgentDestroyedEvent;
import net.java.dante.sim.event.types.EnemyAgentGoneEvent;
import net.java.dante.sim.event.types.EnemyAgentHitEvent;
import net.java.dante.sim.event.types.EnemyAgentMoveEvent;
import net.java.dante.sim.event.types.EnemyAgentSeenEvent;

/**
 * Representation of enemy agent. It is used by client side for all enemy 
 * agents.
 * 
 * @author M.Olszewski
 */
public class ClientEnemyAgent extends EventDrivenSimulationObject
{
  /** Agent's state. */
  private ClientEnemyAgentState state;
  /** Objects mediator. */
  private ObjectsMediator mediator;
  /** State machine responsible for moving agent. */
  private MovementStateMachine stateMachine;

  
  /**
   * Creates instance of {@link ClientEnemyAgent} class with specified agent's state.
   * 
   * @param enemyId - enemy agent's identifier.
   * @param enemyState - state of this agent.
   * @param objectsMediator - objects mediator.
   * @param currentTime - current simulation's time.
   */
  public ClientEnemyAgent(int enemyId, ClientEnemyAgentState enemyState, 
      ObjectsMediator objectsMediator, long currentTime)
  {
    super(enemyId, currentTime);
    
    if (enemyState == null)
    {
      throw new NullPointerException("Specified enemyState is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }
    
    state = enemyState;
    mediator = objectsMediator;
    stateMachine = new MovementStateMachine(enemyState);
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
  public void update(long delta)
  {
    super.update(delta);
    
    stateMachine.update(delta);
  }

  /** 
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#filterEvent(net.java.dante.sim.event.Event)
   */
  @Override
  protected boolean filterEvent(Event event)
  {
    return (state.isAlive() && 
           ((event.getEventType() == EventType.ENEMY_AGENT_SEEN) ||
            (event.getEventType() == EventType.ENEMY_AGENT_GONE) ||
            (event.getEventType() == EventType.ENEMY_AGENT_MOVE) ||
            (event.getEventType() == EventType.ENEMY_AGENT_HIT)  ||
            (event.getEventType() == EventType.ENEMY_AGENT_DESTROYED)));
  }

  /** 
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#processEvent(net.java.dante.sim.event.Event)
   */
  @Override
  protected void processEvent(Event event)
  {
    if (event instanceof EnemyAgentMoveEvent)
    {
      agentMoved((EnemyAgentMoveEvent)event);
    }
    else if (event instanceof EnemyAgentSeenEvent)
    {
      agentSeen((EnemyAgentSeenEvent)event);
    }
    else if (event instanceof EnemyAgentGoneEvent)
    {
      agentGone((EnemyAgentGoneEvent)event);
    }
    else if (event instanceof EnemyAgentHitEvent)
    {
      state.addHitsCount(1);
    }
    else if (event instanceof EnemyAgentDestroyedEvent)
    {
      agentDestroyed();
    }
    else
    {
      throw new IllegalArgumentException("Invalid argument event - not supported Event interface!");
    }
  }
  
  /**
   * Method invoked when enemy agent was moved.
   * 
   * @param movedEvent - the {@link EnemyAgentMoveEvent} event.
   */
  private void agentMoved(EnemyAgentMoveEvent movedEvent)
  {
    state.setSpeedX(movedEvent.getSpeedX());
    state.setSpeedY(movedEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
    stateMachine.startMovement(movedEvent.getDestinationX(), movedEvent.getDestinationY(),
        movedEvent.getSpeedX(), movedEvent.getSpeedY());
  }
  
  /**
   * Method invoked when enemy agent is gone.
   * 
   * @param seenEvent - the {@link EnemyAgentSeenEvent} event.
   */
  private void agentSeen(EnemyAgentSeenEvent seenEvent)
  {
    state.inSightRange();
    state.setX(seenEvent.getDestinationX());
    state.setY(seenEvent.getDestinationY());
    state.setSpeedX(seenEvent.getSpeedX());
    state.setSpeedY(seenEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
  }
  
  /**
   * Method invoked when enemy agent is gone.
   * 
   * @param goneEvent - the {@link EnemyAgentGoneEvent} event.
   */
  private void agentGone(EnemyAgentGoneEvent goneEvent)
  {
    state.outOfSightRange();
    state.setX(goneEvent.getDestinationX());
    state.setY(goneEvent.getDestinationY());
    state.setSpeedX(goneEvent.getSpeedX());
    state.setSpeedY(goneEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
  }
  
  /**
   * Method invoked when enemy agent is destroyed.
   */
  private void agentDestroyed()
  {
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    
    stateMachine.setNotMovingState();
    state.destroyed();
    mediator.objectDestroyed(this);
  }
}