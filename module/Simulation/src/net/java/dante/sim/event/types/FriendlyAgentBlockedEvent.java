/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.event.types;


/**
 * Interface defining parameters for {@link net.java.dante.sim.event.EventType#FRIENDLY_AGENT_BLOCKED} 
 * event type.
 *
 * @author M.Olszewski
 */
public interface FriendlyAgentBlockedEvent extends FriendlyAgentEvent
{
  /**
   * Gets blocked agent's 'x' coordinate.
   * 
   * @return Returns blocked agent's  'x' coordinate.
   */
  double getBlockedX();
  
  /**
   * Gets blocked agent's 'y' coordinate.
   * 
   * @return Returns blocked agent's 'y' coordinate.
   */
  double getBlockedY();
}