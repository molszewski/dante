/*
 * Created on 2006-07-13
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

/**
 * Utility class for tests written with JUnit framework with set of methods
 * for creating structures connected with maps (two dimensional arrays
 * with {@link MapTileType}, lists with {@link StartLocationGroup} objects)
 * and checking their integrity.
 *
 * @author M.Olszewski
 */
final class TestMapUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private TestMapUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Creates valid map as two dimensional array of {@link MapTileType}
   * with specified obstacles and start locations.
   *
   * @param rows - number of map rows.
   * @param columns - number of map columns.
   * @param internalObstacles - two dimensional array with internal
   *        obstacles locations (x, y).
   * @param startLocations -  - two dimensional array with start locations (x, y).
   *
   * @return Returns two dimensional array of {@link MapTileType} representing
   *         valid map with obstacles.
   */
  static MapTileType[][] createValidMap(int rows, int columns,
      int[][] internalObstacles, int[][] startLocations)
  {
    if (internalObstacles == null)
    {
      throw new NullPointerException("Specified internalObstacles is null!");
    }
    if (startLocations == null)
    {
      throw new NullPointerException("Specified startLocations is null!");
    }
    if (rows <= 0)
    {
      throw new IllegalArgumentException("Invalid argument rows - it must be positive integer!");
    }
    if (columns <= 0)
    {
      throw new IllegalArgumentException("Invalid argument columns - it must be positive integer!");
    }

    MapTileType[][] tiles = new MapTileType[rows][columns];

    for (int i = 0; i < tiles.length; i++)
    {
      for (int j = 0; j < tiles[i].length; j++)
      {
        if ((j == 0) || (j == (tiles[i].length - 1)))
        {
          tiles[i][j] = MapTileType.OBSTACLE;
        }
        else
        {
          tiles[i][j] = MapTileType.FREE_TILE;
        }
      }
    }

    Arrays.fill(tiles[0], MapTileType.OBSTACLE);
    Arrays.fill(tiles[tiles.length - 1], MapTileType.OBSTACLE);

    addObstacles(tiles, internalObstacles);
    addStartLocations(tiles, startLocations);

    return tiles;
  }

  /**
   * Adds start locations to specified map representation.
   *
   * @param tiles - two dimensional array of {@link MapTileType} representing
   *         valid map.
   * @param startLocations - two dimensional array with start locations (x, y).
   */
  private static void addStartLocations(MapTileType[][] tiles, int[][] startLocations)
  {
    for (int i = 0; i < startLocations.length; i++)
    {
      tiles[startLocations[i][0]][startLocations[i][1]] = MapTileType.START_LOCATION;
    }
  }

  /**
   * Adds obstacles locations to specified map representation.
   *
   * @param tiles - two dimensional array of {@link MapTileType} representing
   *        valid map.
   * @param obstacles - two dimensional array with internal
   *        obstacles locations (x, y).
   */
  private static void addObstacles(MapTileType[][] tiles, int[][] obstacles)
  {
    for (int i = 0; i < obstacles.length; i++)
    {
      tiles[obstacles[i][0]][obstacles[i][1]] = MapTileType.OBSTACLE;
    }
  }

  /**
   * Checks whether all tiles held inside specified {@link SimulationMap} object
   * are equal to ones from two dimensional array of {@link MapTileType}
   * representing valid map.
   *
   * @param map - {@link SimulationMap} object with tiles.
   * @param tiles - two dimensional array of {@link MapTileType} representing
   *        valid map.
   */
  static void checkTiles(SimulationMap map, MapTileType[][] tiles)
  {
    if (map == null)
    {
      throw new NullPointerException("Specified map is null!");
    }
    if (tiles == null)
    {
      throw new NullPointerException("Specified tiles is null!");
    }

    for (int i = 0, rows = map.getRows(); i < rows; i++)
    {
      for (int j = 0, cols = map.getColumns(); j < cols; j++)
      {
        Assert.assertEquals(map.getTile(i,j), tiles[i][j]);
      }
    }
  }

  /**
   * Checks whether all start locations groups contains valid data (group counts,
   * locations count in single groups, locations refers to proper tiles).
   *
   * @param map - {@link SimulationMap} object with tiles.
   * @param tiles - two dimensional array of {@link MapTileType} representing
   *        valid map.
   * @param locationsGroupCount - valid number of start location groups.
   * @param locationsForSingleGroup  - valid number of locations in each
   *        start location group.
   */
  static void checkGroups(SimulationMap map, MapTileType[][] tiles,
      int locationsGroupCount, int locationsForSingleGroup)
  {
    if (map == null)
    {
      throw new NullPointerException("Specified map is null!");
    }
    if (tiles == null)
    {
      throw new NullPointerException("Specified tiles is null!");
    }
    if (locationsGroupCount <= 0)
    {
      throw new IllegalArgumentException("Invalid argument locationsGroupCount - it must be positive integer!");
    }
    if (locationsForSingleGroup <= 0)
    {
      throw new IllegalArgumentException("Invalid argument locationsForSingleGroup - it must be positive integer!");
    }

    Assert.assertEquals(map.locationGroupsCount(), locationsGroupCount);
    for (int i = 0, size = map.locationGroupsCount(); i < size; i++)
    {
      StartLocationGroup group = map.getStartLocationGroup(i);
      Assert.assertEquals(group.locationsCount(), locationsForSingleGroup);
      for (int j = 0, groupSize = group.locationsCount(); j < groupSize; j++)
      {
        StartLocation loc = group.getLocation(j);
        Assert.assertEquals(tiles[loc.getRow()][loc.getColumn()], MapTileType.START_LOCATION);
      }
    }
  }

  /**
   * Creates list with start locations groups from three dimensional array.
   *
   * @param startLocations - three dimensional array definining groups of start locations.
   *
   * @return Returns newly created list with start locations groups.
   */
  static List<StartLocationGroup> createStartLocationGroups(int[][][] startLocations)
  {
    if (startLocations == null)
    {
      throw new NullPointerException("Specified startLocations is null!");
    }

    List<StartLocationGroup> list = new ArrayList<StartLocationGroup>();

    for (int i = 0; i < startLocations.length; i++)
    {
      List<StartLocation> group = new ArrayList<StartLocation>();
      for (int j = 0; j < startLocations[i].length; j++)
      {
        group.add(new StartLocationImpl(startLocations[i][j][0],
            startLocations[i][j][1]));
      }
      list.add(new StartLocationGroupImpl(group));
    }

    return list;
  }
}