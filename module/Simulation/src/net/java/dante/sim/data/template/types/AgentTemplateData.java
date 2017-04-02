/*
 * Created on 2006-07-12
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.template.types;

import net.java.dante.sim.creator.ObjectsCreator;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.TemplateData;

/**
 * Class representing data for one type of agent, used by
 * {@link ObjectsCreator} implementation to create
 * of {@link net.java.dante.sim.data.object.agent.ServerAgent} object with custom data.
 *
 * @author M.Olszewski
 */
public final class AgentTemplateData implements TemplateData
{
  /** Maximum health of this {@link AgentTemplateData}. */
  private int maxHealth;
  /** Maximum speed of this {@link AgentTemplateData}. */
  private double maxSpeed;
  /** Maximum sight range of this {@link AgentTemplateData}. */
  private double sightRange;
  /** Agent's size. */
  private ObjectSize size;
  /** Weapon system used by this {@link AgentTemplateData}. */
  private String weaponSystemType;


  /**
   * Creates object of {@link AgentTemplateData} class with specified data.
   *
   * @param agentMaxHealth - agent's maximum health.
   * @param agentMaxSpeed - agent's maximum speed.
   * @param agentSightRange - agent's maximum sight range.
   * @param agentSize - agent's size.
   * @param agentWeaponSystemType - agent's weapon system.
   */
  public AgentTemplateData(int    agentMaxHealth,
                           double agentMaxSpeed,
                           double agentSightRange,
                           ObjectSize   agentSize,
                           String agentWeaponSystemType)
  {
    if (agentSize == null)
    {
      throw new NullPointerException("Specified agentSize is null!");
    }
    if (agentWeaponSystemType == null)
    {
      throw new NullPointerException("Specified agentSize is null!");
    }
    if (agentWeaponSystemType.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentWeaponSystemType - it cannot be empty string!");
    }
    if (agentSightRange <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument agentSightRange - it must be positive real number!");
    }
    if (agentMaxSpeed <= 0.0)
    {
      throw new IllegalArgumentException("Invalid argument agentMaxSpeed - it must be positive real number!");
    }
    if (agentMaxHealth <= 0)
    {
      throw new IllegalArgumentException("Invalid argument agentMaxHealth - it must be positive integer!");
    }

    maxHealth        = agentMaxHealth;
    maxSpeed         = agentMaxSpeed;
    sightRange       = agentSightRange;
    size             = agentSize;
    weaponSystemType = agentWeaponSystemType;
  }

  /**
   * Gets agent's maximum health.
   *
   * @return Returns agent's maximum health.
   */
  public int getMaxHealth()
  {
    return maxHealth;
  }

  /**
   * Gets agent's maximum speed.
   *
   * @return Returns agent's maximum speed.
   */
  public double getMaxSpeed()
  {
    return maxSpeed;
  }

  /**
   * Gets agent's maximum sight range.
   *
   * @return Returns agent's maximum sight range.
   */
  public double getSightRange()
  {
    return sightRange;
  }

  /**
   * Gets agent's size.
   *
   * @return Returns agent's size.
   */
  public ObjectSize getAgentSize()
  {
    return size;
  }

  /**
   * Gets agent's weapon system.
   *
   * @return Returns agent's weapon system.
   */
  public String getWeaponSystemType()
  {
    return weaponSystemType;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + maxHealth;
    long temp = Double.doubleToLongBits(maxSpeed);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(sightRange);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    result = PRIME * result + ((size == null) ? 0 : size.hashCode());
    result = PRIME * result + ((weaponSystemType == null) ? 0 : weaponSystemType.hashCode());

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (object == this);
    if (!equal && (object instanceof AgentTemplateData))
    {
      AgentTemplateData other = (AgentTemplateData)object;
      equal = (maxHealth == other.maxHealth) &&
              (Double.doubleToLongBits(maxSpeed) == Double.doubleToLongBits(other.maxSpeed)) &&
              (Double.doubleToLongBits(sightRange) == Double.doubleToLongBits(other.sightRange)) &&
              (weaponSystemType.equals(other.weaponSystemType)) &&
              (size.equals(other.size));
    }

    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[maxHealth=" + maxHealth +
        "; maxSpeed=" + maxSpeed +
        "; sightRange=" + sightRange +
        "; weaponSystemType=" + weaponSystemType + "; size=" + size + "]");
  }
}