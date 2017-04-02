/*
 * Created on 2006-08-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

/**
 * Implementation of {@link EnemyAgentInitData} interface.
 *
 * @author M.Olszewski
 */
class EnemyAgentInitDataImpl implements EnemyAgentInitData
{
  /** Name of agent's type. */
  private String type;
  /** Agent's identifier. */
  private int id;


  /**
   * Creates instance of {@link EnemyAgentInitDataImpl} class with specified
   * parameters.
   *
   * @param typeName - name of agent's type.
   * @param agentId - agent's identifier.
   */
  EnemyAgentInitDataImpl(String typeName, int agentId)
  {
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() < 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it must be non-empty string!");
    }
    if (agentId < 0)
    {
      throw new IllegalArgumentException("Invalid argument agentId - it must be positive integer or zero!");
    }

    type = typeName;
    id   = agentId;
  }


  /**
   * @see net.java.dante.sim.io.init.EnemyAgentInitData#getType()
   */
  public String getType()
  {
    return type;
  }

  /**
   * @see net.java.dante.sim.io.init.EnemyAgentInitData#getAgentId()
   */
  public int getAgentId()
  {
    return id;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + id;
    result = PRIME * result + type.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof EnemyAgentInitDataImpl))
    {
      final EnemyAgentInitDataImpl other = (EnemyAgentInitDataImpl) object;
      equal = ((id == other.id) && (type.equals(other.type)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[id=" + id + "; type=" + type + "]");
  }
}