/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.client;

import java.awt.Color;
import java.awt.Graphics2D;

import net.java.dante.sim.data.object.agent.ClientFriendlyAgent;
import net.java.dante.sim.data.object.agent.ClientFriendlyAgentState;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.engine.engine2d.Engine2dAnimation;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.event.types.FriendlyAgentEvent;


/**
 * Class represents {@link ClientFriendlyAgent} objects as engine objects with 
 * graphical representations.
 *
 * @author M.Olszewski
 */
class Client2dFriendlyAgent extends Engine2dObject
{
  /** Wrapped {@link ClientFriendlyAgent} object. */
  private ClientFriendlyAgent agent;
  /** Object managing agent's animation. */
  private Engine2dAnimation animation;
  /** Circle defining agent's sight range. */
//  private Circle2d sightCircle;
  /** Circle defining agent's weapon range. */
//  private Circle2d weaponCircle;

  /**
   * Creates instance of {@link Client2dFriendlyAgent} class with specified
   * parameters.
   *
   * @param simAgent - wrapped agent object.
   * @param objSprites - object's graphical representation.
   * @param spritesDelay - time after which sprite will be changed.
   */
  Client2dFriendlyAgent(ClientFriendlyAgent simAgent, 
      Java2dSprite[] objSprites, long spritesDelay)
  {
    super(objSprites[0], 
         ((ClientFriendlyAgentState)simAgent.getData()).getX(), 
         ((ClientFriendlyAgentState)simAgent.getData()).getY(),
         ((ClientFriendlyAgentState)simAgent.getData()).getSize());
    
    agent = simAgent;
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
  }
  

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return ((ClientFriendlyAgentState)agent.getData()).getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return ((ClientFriendlyAgentState)agent.getData()).getY();
  }

  
  /**
   * Updates associated {@link ClientFriendlyAgent} object's state and 
   * animation's frame.
   * 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  public void update(long delta)
  {
    // Update agent's state
    agent.update(delta);

    ClientFriendlyAgentState state = (ClientFriendlyAgentState)agent.getData();
    if (state.isPositionChanged())
    {
      animation.update(delta);
      super.update(delta);
    }
  }
  
  /**
   * Renders agent's identifier in specified color.
   * 
   * @param g2d - graphics context.
   * @param color - agent's identifier color.
   */
  void renderID(Graphics2D g2d, Color color)
  {
    if (g2d != null)
    {
      // Render agents IDs
      g2d.setColor(color);
      g2d.drawString(Integer.toString(agent.getId()), 
          (int)(rectangle.getX() + rectangle.getWidth()),
          (int)(rectangle.getY() - rectangle.getHeight()/4));
    }
  }
  
  /**
   * Updates changes of this friendly agent.
   */
  void changesUpdated()
  {
    // Mark agent state as updated.
    ObjectState agentState = agent.getData();
    if (agentState instanceof Changeable)
    {
      ((Changeable)agentState).changesUpdated();
    }
  }
  
  /**
   * Adds the specified {@link FriendlyAgentEvent} event to wrapped agent.
   * 
   * @param event - the specified {@link FriendlyAgentEvent} event.
   */
  void addEvent(FriendlyAgentEvent event)
  {
    agent.addEvent(event);
  }
  
  /**
   * Shifts time of wrapped {@link ClientFriendlyAgent} by the specified
   * amount of time.
   * 
   * @param time - the specified amount of time.
   */
  void timeShift(long time)
  {
    agent.timeShift(time);
  }
  
  /**
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) + 
        "; agent=" + agent + "]");
  }
}