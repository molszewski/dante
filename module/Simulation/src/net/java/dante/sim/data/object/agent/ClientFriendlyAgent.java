/*
 * Created on 2006-08-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent;

import net.java.dante.sim.data.ObjectsMediator;
import net.java.dante.sim.data.object.ClientWeaponSystem;
import net.java.dante.sim.data.object.EventDrivenSimulationObject;
import net.java.dante.sim.data.object.MovementStateMachine;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.object.SimulationObject;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.event.Event;
import net.java.dante.sim.event.EventType;
import net.java.dante.sim.event.types.EventTypesUtils;
import net.java.dante.sim.event.types.FriendlyAgentBlockedEvent;
import net.java.dante.sim.event.types.FriendlyAgentDestroyedEvent;
import net.java.dante.sim.event.types.FriendlyAgentHitEvent;
import net.java.dante.sim.event.types.FriendlyAgentMoveEvent;

/**
 * Representation of agent used by client side for agents controlled by client.
 *
 * @author M.Olszewski 
 */
public class ClientFriendlyAgent extends EventDrivenSimulationObject
{
  /** Agent's state. */
  ClientFriendlyAgentState state;
  /** Objects mediator. */
  private ObjectsMediator mediator;
  /** State machine responsible for moving agent. */
  private MovementStateMachine stateMachine;
  /** Alive status - if agent is destroyed, no events will be processed. */
  private boolean agentAlive = true;
  
  
  /**
   * Creates instance of {@link ClientFriendlyAgent} class.
   *
   * @param objectId - agent's identifier.
   * @param agentState - agent's state.
   * @param objectsMediator - objects mediator.
   * @param currentTime - current simulation's time.
   */
  public ClientFriendlyAgent(int objectId, ClientFriendlyAgentState agentState, 
      ObjectsMediator objectsMediator, long currentTime)
  {
    super(objectId, currentTime);

    if (agentState == null)
    {
      throw new NullPointerException("Specified agentState is null!");
    }
    if (objectsMediator == null)
    {
      throw new NullPointerException("Specified objectsMediator is null!");
    }
    
    state = agentState;
    stateMachine = new MovementStateMachine(agentState);
    mediator = objectsMediator;
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
    return (agentAlive && 
            ((event.getEventType() == EventType.FRIENDLY_AGENT_HIT) ||
             (event.getEventType() == EventType.FRIENDLY_AGENT_BLOCKED) ||
             (event.getEventType() == EventType.FRIENDLY_AGENT_MOVE) ||
             (event.getEventType() == EventType.FRIENDLY_AGENT_DESTROYED)));
  }

  /**
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#processEvent(net.java.dante.sim.event.Event)
   */
  @Override
  protected void processEvent(Event event)
  {
    if (event instanceof FriendlyAgentMoveEvent)
    {
      agentMoved((FriendlyAgentMoveEvent)event);
    }
    else if (event instanceof FriendlyAgentBlockedEvent)
    {
      agentBlocked((FriendlyAgentBlockedEvent)event);
    }
    else if (event instanceof FriendlyAgentHitEvent)
    {
      agentHit((FriendlyAgentHitEvent)event);
    }
    else if (event instanceof FriendlyAgentDestroyedEvent)
    {
      agentDestroyed();
    }
    else
    {
      throw new IllegalArgumentException("Invalid argument event - not supported Event interface!");
    }
  }
  
  /**
   * Method invoked when this agent was moved.
   * 
   * @param moveEvent - the {@link FriendlyAgentMoveEvent} event.
   */
  private void agentMoved(FriendlyAgentMoveEvent moveEvent)
  {
    state.setSpeedX(moveEvent.getSpeedX());
    state.setSpeedY(moveEvent.getSpeedY());
    
    stateMachine.setNotMovingState();
    stateMachine.startMovement(
        moveEvent.getDestinationX(), moveEvent.getDestinationY(),
        moveEvent.getSpeedX(), moveEvent.getSpeedY());
  }
  
  /**
   * Method invoked when this agent is blocked.
   * 
   * @param blockedEvent - the {@link FriendlyAgentBlockedEvent} event.
   */
  private void agentBlocked(FriendlyAgentBlockedEvent blockedEvent)
  {
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    state.setX(blockedEvent.getBlockedX());
    state.setY(blockedEvent.getBlockedY());
  }
  
  /**
   * Method invoked when this agent is hit.
   * 
   * @param hitEvent - the {@link FriendlyAgentHitEvent} event.
   */
  private void agentHit(FriendlyAgentHitEvent hitEvent)
  {
    state.setCurrentHitPoints(state.getCurrentHitPoints() - hitEvent.getDamage());
  }
  
  /**
   * Method invoked when this agent is destroyed.
   */
  private void agentDestroyed()
  {
    state.setSpeedX(0.0);
    state.setSpeedY(0.0);
    
    stateMachine.setNotMovingState();
    agentAlive = false;
    mediator.objectDestroyed(this);
  }

  long counter = 0;
  
  /** 
   * @see net.java.dante.sim.data.object.EventDrivenSimulationObject#update(long)
   */
  @Override
  public void update(long delta)
  {
    super.update(delta);
    
    stateMachine.update(delta);
  }
  
  
  /**
   * Tests.
   * 
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    ObjectsMediator om = new ObjectsMediator() {
      /** 
       * @see net.java.dante.sim.data.ObjectsMediator#objectCreated(net.java.dante.sim.data.object.SimulationObject)
       */
      public void objectCreated(SimulationObject object){ /* Intentionally left empty. */ }
      /** 
       * @see net.java.dante.sim.data.ObjectsMediator#objectDestroyed(net.java.dante.sim.data.object.SimulationObject)
       */
      public void objectDestroyed(SimulationObject object) { /* Intentionally left empty. */ }
    };
    
    ClientFriendlyAgentState state = new ClientFriendlyAgentState("micro", 25.0, 100, 15.0, new ObjectSize(32,32),
        new ClientWeaponSystem("hitter", new WeaponTemplateData(10, 10, 10, 10, 10, 10, new ObjectSize(8,8))));
    
    state.setX(0.0);
    state.setY(0.0);
    
    ClientFriendlyAgent agent = new ClientFriendlyAgent(11, state, om, 0);
    agent.addEvent(EventTypesUtils.createFriendlyAgentMoveEvent(2, 11, 5.0, 0.0, 500.0, 0.0));
    
    for (int i = 0; i < 20; i++)
    {
      agent.update(1);
    }
  }
}