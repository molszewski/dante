/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;


/**
 * Implementation of {@link StartLocation} interface.
 *
 * @author M.Olszewski
 */
final class StartLocationImpl implements StartLocation
{
  /** Column of this start location. */
  private int column;
  /** Row of this start location. */
  private int row;


  /**
   * Creates instance of this {@link StartLocationImpl}.
   *
   * @param locationRow - location's row.
   * @param locationColumn - location's column.
   */
  StartLocationImpl(int locationRow, int locationColumn)
  {
    if (locationRow < 0)
    {
      throw new IllegalArgumentException("Invalid argument locationRow - it cannot be negative integer!");
    }
    if (locationColumn < 0)
    {
      throw new IllegalArgumentException("Invalid argument locationColumn cannot be negative integer!");
    }

    row = locationRow;
    column = locationColumn;
  }


  /**
   * @see net.java.dante.sim.data.map.StartLocation#getRow()
   */
  public int getRow()
  {
    return row;
  }

  /**
   * @see net.java.dante.sim.data.map.StartLocation#getColumn()
   */
  public int getColumn()
  {
    return column;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + column;
    result = PRIME * result + row;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof StartLocationImpl))
    {
      final StartLocationImpl other = (StartLocationImpl) object;
      equal = ((column == other.column) && (row == other.row));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[row=" + row + "; column=" + column + "]");
  }
}