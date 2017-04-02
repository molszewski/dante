/*
 * Created on 2006-07-07
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.template.types;

/**
 * Names of all fixed attributes of {@link net.java.dante.sim.data.object.agent.ServerAgent} object.
 *
 * @author M.Olszewski
 */
public final class AgentAttributesNames
{
  /** Attribute indicating agent's type. */
  public static final String AGENT_TYPE = "TYPE";
  /** Attribute indicating maximum agent's health. */
  public static final String MAX_HEALTH = "MAX_HEALTH";
  /** Attribute indicating maximum agent's speed. */
  public static final String MAX_SPEED = "MAX_SPEED";
  /** Attribute indicating maximum agent's sight range. */
  public static final String SIGHT_RANGE = "SIGHT_RANGE";
  /** Attribute indicating agent's size. */
  public static final String AGENT_SIZE = "AGENT_SIZE";
  /** Attribute indicating agent's weapon system. */
  public static final String WEAPON_SYSTEM_FILE = "WEAPON_SYSTEM_FILE";
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private AgentAttributesNames()
  {
    // Intentionally left empty.
  }
}