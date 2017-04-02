/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;

import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@link SimulationMap} interface holding start locations
 * groups as the {@link List} and two-dimensional array of map's tiles.
 *
 * @author M.Olszewski
 */
class SimulationMapImpl implements SimulationMap
{
  /** Two-dimensional array of map's tiles. */
  private MapTileType[][] tiles = null;
  /** List of start locations groups. */
  private List<StartLocationGroup> groups = null;


  /**
   * Creates object of {@link SimulationMapImpl} with specified map's tiles and
   * start locations groups.
   *
   * @param mapTiles - two-dimensional array of map's tiles.
   * @param startGroups - list of start locations groups.
   */
  SimulationMapImpl(MapTileType[][] mapTiles, List<StartLocationGroup> startGroups)
  {
    if (mapTiles == null)
    {
      throw new NullPointerException("Specified mapTiles is null!");
    }
    if (startGroups == null)
    {
      throw new NullPointerException("Specified startGroups is null!");
    }

    tiles = mapTiles;
    groups = startGroups;
  }


  /**
   * @see net.java.dante.sim.data.map.SimulationMap#getTile(int, int)
   */
  public MapTileType getTile(int row, int column)
  {
    return tiles[row][column];
  }

  /**
   * @see net.java.dante.sim.data.map.SimulationMap#getRows()
   */
  public int getRows()
  {
    return tiles.length;
  }

  /**
   * @see net.java.dante.sim.data.map.SimulationMap#getColumns()
   */
  public int getColumns()
  {
    return tiles[0].length;
  }

  /**
   * @see net.java.dante.sim.data.map.SimulationMap#getStartLocationGroup(int)
   */
  public StartLocationGroup getStartLocationGroup(int index)
  {
    return groups.get(index);
  }

  /**
   * @see net.java.dante.sim.data.map.SimulationMap#locationGroupsCount()
   */
  public int locationGroupsCount()
  {
    return groups.size();
  }


  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + groups.hashCode();
    result = PRIME * result + Arrays.hashCode(tiles);

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof SimulationMapImpl))
    {
      final SimulationMapImpl other = (SimulationMapImpl) object;

      boolean tilesEqual = true;
      for (int i = 0; i < tiles.length; i++)
      {
        if (!Arrays.equals(tiles[i], other.tiles[i]))
        {
          tilesEqual = false;
          break;
        }
      }

      equal = tilesEqual && groups.equals(other.groups);
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[tiles=" + Arrays.toString(tiles) +
        "; groups=" + groups + "]");
  }
}