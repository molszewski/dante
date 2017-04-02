/*
 * Created on 2006-08-20
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.client;

import java.awt.Color;
import java.awt.Graphics2D;

import net.java.dante.sim.data.object.agent.ClientEnemyAgent;
import net.java.dante.sim.data.object.agent.ClientEnemyAgentState;
import net.java.dante.sim.data.object.state.Changeable;
import net.java.dante.sim.data.object.state.ObjectState;
import net.java.dante.sim.engine.engine2d.Engine2dAnimation;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.event.types.EnemyAgentEvent;


/**
 * Class represents {@link ClientEnemyAgent} objects as engine objects with 
 * graphical representations.
 *
 * @author M.Olszewski
 */
class Client2dEnemyAgent extends Engine2dObject
{
  /** Wrapped {@link ClientEnemyAgent} object. */
  private ClientEnemyAgent agent;
  /** Object managing agent's animation. */
  private Engine2dAnimation animation;

  
  /**
   * Creates instance of {@link Client2dEnemyAgent} class with specified
   * parameters.
   *
   * @param simAgent - wrapped agent object.
   * @param objSprites - object's graphical representation.
   * @param spritesDelay - time after which sprite will be changed.
   */
  Client2dEnemyAgent(ClientEnemyAgent simAgent, 
      Java2dSprite[] objSprites, long spritesDelay)
  {
    super(objSprites[0], 
         ((ClientEnemyAgentState)simAgent.getData()).getX(), 
         ((ClientEnemyAgentState)simAgent.getData()).getY(),
         ((ClientEnemyAgentState)simAgent.getData()).getSize());
    
    agent = simAgent;
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
  }
  
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return ((ClientEnemyAgentState)agent.getData()).getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return ((ClientEnemyAgentState)agent.getData()).getY();
  }
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#setActive(boolean)
   */
  @Override
  public void setActive(boolean objectActive)
  {
    super.setActive(objectActive);
    
    ((ClientEnemyAgentState)agent.getData()).destroyed();
  }
  
  /**
   * Updates associated {@link ClientEnemyAgent} object's state and 
   * animation's frame.
   * 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  public void update(long delta)
  {
    // Update agent's state
    agent.update(delta);

    ClientEnemyAgentState state = (ClientEnemyAgentState)agent.getData();
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
   * Adds the specified {@link EnemyAgentEvent} event to wrapped agent.
   * 
   * @param event - the specified {@link EnemyAgentEvent} event.
   */
  void addEvent(EnemyAgentEvent event)
  {
    agent.addEvent(event);
  }
  
  /**
   * Shifts time of wrapped {@link ClientEnemyAgent} by the specified
   * amount of time.
   * 
   * @param time - the specified amount of time.
   */
  void timeShift(long time)
  {
    agent.timeShift(time);
  }
  
  /**
   * Updates changes of this enemy agent.
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
   * Determines whether this enemy agent is visible.
   * 
   * @return Returns <code>true</code> if agent is visible,
   *         <code>false</code> otherwise.
   */
  boolean isVisible()
  {
    return ((ClientEnemyAgentState)agent.getData()).isVisible();
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