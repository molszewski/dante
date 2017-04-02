/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */


package net.java.dante.algorithms;


import net.java.dante.algorithms.data.TileData;
import net.java.dante.sim.data.object.ObjectSize;

/**
 * Implementation of {@link TileData} interface.
 *
 * @author M.Olszewski
 */
class TileDataImpl implements TileData
{
  private double x;
  private double y;
  private ObjectSize size;

  /**
   * Creates instance of {@link TileDataImpl} class with the specified
   * parameters.
   *
   * @param tileX tile's 'x' coordinate.
   * @param tileY tile's 'y' coordinate.
   * @param tileSize tile's size.
   */
  TileDataImpl(double tileX, double tileY, ObjectSize tileSize)
  {
    if (tileSize == null)
    {
      throw new NullPointerException("Specified tileSize is null!");
    }

    x = tileX;
    y = tileY;
    size = tileSize;
  }


  /**
   * @see net.java.dante.algorithms.data.TileData#getSize()
   */
  public ObjectSize getSize()
  {
    return size;
  }

  /**
   * @see net.java.dante.algorithms.data.TileData#getX()
   */
  public double getX()
  {
    return x;
  }

  /**
   * @see net.java.dante.algorithms.data.TileData#getY()
   */
  public double getY()
  {
    return y;
  }
}