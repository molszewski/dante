/*
 * Created on 2006-08-06
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.object;

/**
 * Class storing width and height of some object.
 *
 * @author M.Olszewski
 */
public final class ObjectSize
{
  /** Width & height. */
  private int w, h;


  /**
   * Creates instance of {@link ObjectSize} class.
   *
   * @param width - the specified width.
   * @param height - the specified height.
   */
  public ObjectSize(int width, int height)
  {
    if (width < 0)
    {
      throw new IllegalArgumentException("Invalid argument width - it must be positive integer or zero!");
    }
    if (height < 0)
    {
      throw new IllegalArgumentException("Invalid argument height - it must be positive integer or zero!");
    }

    w = width;
    h = height;
  }


  /**
   * Gets width stored by this {@link ObjectSize} class.
   *
   * @return Returns width stored by this {@link ObjectSize} class.
   */
  public int getWidth()
  {
    return w;
  }

  /**
   * Gets height stored by this {@link ObjectSize} class.
   *
   * @return Returns height stored by this {@link ObjectSize} class.
   */
  public int getHeight()
  {
    return h;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + h;
    result = PRIME * result + w;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ObjectSize))
    {
      final ObjectSize other = (ObjectSize) object;
      equal = (w == other.w) && (h == other.h);
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[width=" + w + "; height=" + h + "]");
  }
}