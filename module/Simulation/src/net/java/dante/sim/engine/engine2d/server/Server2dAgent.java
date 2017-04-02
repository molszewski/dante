/*
 * Created on 2006-07-19
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d.server;

import java.awt.Color;
import java.awt.Graphics2D;

import net.java.dante.sim.data.object.agent.ServerAgent;
import net.java.dante.sim.data.object.agent.ServerAgentState;
import net.java.dante.sim.engine.engine2d.Engine2dAnimation;
import net.java.dante.sim.engine.engine2d.Engine2dObject;
import net.java.dante.sim.engine.graphics.java2d.Java2dContext;
import net.java.dante.sim.engine.graphics.java2d.Java2dSprite;
import net.java.dante.sim.util.math.Circle2d;


/**
 * Class represents {@link ServerAgent} objects as engine objects with 
 * graphical representations.
 * 
 * @author M.Olszewski
 */
class Server2dAgent extends Engine2dObject
{
  /** Wrapped {@link ServerAgent} object. */
  private ServerAgent agent;
  /** Object managing agent's animation. */
  private Engine2dAnimation animation;
  /** Circle defining agent's sight range. */
  private Circle2d sightCircle;
  /** Circle defining agent's weapon range. */
  private Circle2d weaponCircle;
  
  
  /**
   * Creates instance of {@link Server2dAgent} class with specified
   * parameters.
   *
   * @param simAgent - wrapped agent object.
   * @param objSprites - object's graphical representation.
   * @param spritesDelay - time after which sprite will be changed.
   */
  Server2dAgent(ServerAgent simAgent, Java2dSprite[] objSprites, long spritesDelay)
  {
    super(objSprites[0], ((ServerAgentState)simAgent.getData()).getX(), 
        ((ServerAgentState)simAgent.getData()).getY(),
        ((ServerAgentState)simAgent.getData()).getSize());
    
    
    agent = simAgent;
    animation = new Engine2dAnimation(this, objSprites, spritesDelay);
    initCircles((ServerAgentState)simAgent.getData());
  }
  
  
  /**
   * Initializes sight range and weapon range circles using specified 
   * agent's state.
   * 
   * @param state - the specified agent's state.
   */
  private void initCircles(ServerAgentState state)
  {
    double centerX = state.getX() + (state.getSize().getWidth() >> 1);
    double centerY = state.getY() + (state.getSize().getHeight() >> 1);
    
    sightCircle = new Circle2d(centerX, centerY, state.getSightRange());
    weaponCircle = new Circle2d(centerX, centerY, state.getWeapon().getRange() + 
        (state.getSize().getWidth()/2) + state.getWeapon().getProjectileSize().getWidth());
  }
  
  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getX()
   */
  @Override
  protected double getX()
  {
    return ((ServerAgentState)agent.getData()).getX();
  }

  /** 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#getY()
   */
  @Override
  protected double getY()
  {
    return ((ServerAgentState)agent.getData()).getY();
  }
  
  /**
   * Updates associated {@link ServerAgent} object's state and object bounds.
   * 
   * @see net.java.dante.sim.engine.engine2d.Engine2dObject#update(long)
   */
  public void update(long delta)
  {
    // Update agent's state
    agent.update(delta);

    ServerAgentState state = (ServerAgentState)agent.getData();

    if (state.isPositionChanged())
    {
      animation.update(delta);
      // Update sight & weapon ranges circles
      updateCircles();
      
      // Update bounds
      super.update(delta);
    }
  }
  
  /**
   * Updates circles representing sight range and weapon range.
   */
  private void updateCircles()
  {
    double centerX = getX() + (getObjectBounds().getWidth() >> 1);
    double centerY = getY() + (getObjectBounds().getHeight() >> 1);
    
    sightCircle.setCircle(centerX, centerY, sightCircle.getRadius());
    weaponCircle.setCircle(centerX, centerY, weaponCircle.getRadius());
  }

  /**
   * Method rendering agent's sprite and agent's sight range circle.
   * 
   * @param context - graphics context.
   */
  void render(Java2dContext context)
  {
    Graphics2D g2d = context.getAcceleratedGraphics();
    if (g2d != null)
    {
      renderCircles(g2d);
    }
    
    render();
  }
  
  /**
   * Renders sight range circle and weapon range circle 
   * using the specified graphics context.
   * 
   * @param g2d - graphics context.
   */
  private void renderCircles(Graphics2D g2d)
  {
    g2d.setColor(Color.YELLOW);
    renderCircle(sightCircle, g2d);
    g2d.setColor(Color.GREEN);
    renderCircle(weaponCircle, g2d);
  }
  
  /**
   * Renders the specified circle.
   * 
   * @param circle - the specified circle.
   * @param g2d - graphics context.
   */
  private void renderCircle(Circle2d circle, Graphics2D g2d)
  {
    int startX = (int)(circle.getX() - circle.getRadius());
    int startY = (int)(circle.getY() - circle.getRadius());
    int doubleRadius = ((int)circle.getRadius() << 1);
    g2d.drawOval(startX, startY, doubleRadius, doubleRadius);
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
   * Gets the wrapped simulation's agent.
   *
   * @return Returns the wrapped simulation's agent.
   */
  ServerAgent getAgent()
  {
    return agent;
  }
  
  /**
   * Gets circle representing sight range of this agent.
   * 
   * @return Returns circle representing sight range of this agent.
   */
  Circle2d getSightCircle()
  {
    return sightCircle;
  }
  
  /**
   * Updates old agent's position to current one.
   */
  void updateOldPosition()
  {
    ServerAgentState agentState = (ServerAgentState)agent.getData();
    // Save old position
    agentState.setOldX(agentState.getX());
    agentState.setOldY(agentState.getY());
  }
  
  /**
   * Checks whether coordinates of this agent have been changed
   * since last update.
   * 
   * @return Returns <code>true</code> if coordinates of this agent 
   *         have been changed since last update, <code>false</code> otherwise.
   */
  boolean isPositionChanged()
  {
    return ((ServerAgentState)agent.getData()).isPositionChanged();
  }
  
  /**
   * Reverses agent's position.
   */
  void reversePosition()
  {
    agent.commandInterrupted();
    updateBounds();
    updateCircles();
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