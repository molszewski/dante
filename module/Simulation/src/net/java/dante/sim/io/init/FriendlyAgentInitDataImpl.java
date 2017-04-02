/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.io.init;

/**
 * Implementation of {@link FriendlyAgentInitData} interface.
 *
 * @author M.Olszewski
 */
class FriendlyAgentInitDataImpl implements FriendlyAgentInitData
{
  /** Name of agent's type. */
  private String type;
  /** Agent's identifier. */
  private int id;
  /** Agent's start 'x' coordinate. */
  private double x;
  /** Agent's start 'y' coordinate. */
  private double y;


  /**
   * Creates instance of {@link FriendlyAgentInitDataImpl} class with specified
   * parameters.
   *
   * @param typeName - name of agent's type.
   * @param agentId - agent's identifier.
   * @param startX - agent's start 'x' coordinate.
   * @param startY - agent's start 'y' coordinate.
   */
  FriendlyAgentInitDataImpl(String typeName, int agentId,
      double startX, double startY)
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
    if (startX < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument startX - it must be positive real number or zero!");
    }
    if (startY < 0.0)
    {
      throw new IllegalArgumentException("Invalid argument startY - it must be positive real number or zero!");
    }

    type = typeName;
    id   = agentId;
    x    = startX;
    y    = startY;
  }

  /**
   * @see net.java.dante.sim.io.init.FriendlyAgentInitData#getType()
   */
  public String getType()
  {
    return type;
  }

  /**
   * @see net.java.dante.sim.io.init.FriendlyAgentInitData#getAgentId()
   */
  public int getAgentId()
  {
    return id;
  }

  /**
   * @see net.java.dante.sim.io.init.FriendlyAgentInitData#getStartX()
   */
  public double getStartX()
  {
    return x;
  }

  /**
   * @see net.java.dante.sim.io.init.FriendlyAgentInitData#getStartY()
   */
  public double getStartY()
  {
    return y;
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
    result = PRIME * result + ((type == null) ? 0 : type.hashCode());
    long temp = Double.doubleToLongBits(x);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof FriendlyAgentInitDataImpl))
    {
      final FriendlyAgentInitDataImpl other = (FriendlyAgentInitDataImpl) object;
      equal = ((id == other.id) && (type.equals(other.type)) &&
               (Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)) &&
               (Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[id=" + id + "; type=" + type +
        "; x=" + x + "; y=" + y + "]");
  }
}