/*
 * Created on 2006-07-16
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;

import java.util.Arrays;

/**
 * Utility class containing set of useful constants and methods creating
 * instances of {@link StartLocation}, {@link StartLocationGroup} and
 * {@link SimulationMap} classes.
 *
 * @author M.Olszewski
 */
public final class MapUtils
{
  /** Minimum map's width. */
  public static final int MINIMUM_MAP_WIDTH = 8;
  /** Minimum map's height. */
  public static final int MINIMUM_MAP_HEIGHT = 4;
  /** Minimum number of start locations groups on the map. */
  public static final int MINIMUM_GROUPS_COUNT = 2;
  /** Maximum number of start locations groups on the map. */
  public static final int MAXIMUM_GROUPS_COUNT = 16;
  /** Minimum number of start locations in one group on the map. */
  public static final int MINIMUM_LOCATIONS_IN_GROUPS_COUNT = 1;


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private MapUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Creates instance of {@link StartLocation} class with specified arguments.
   *
   * @param locationRow - location's row.
   * @param locationColumn - location's column.
   *
   * @return Returns instance of {@link StartLocation} class with specified
   *         arguments.
   */
  public static StartLocation createStartLocation(int locationRow, int locationColumn)
  {
    return new StartLocationImpl(locationRow, locationColumn);
  }

  /**
   * Creates instance of this {@link StartLocationGroup} with specified
   * arguments.
   *
   * @param startLocations - list with start locations.
   *
   * @return Returns instance of {@link StartLocationGroup} class with specified
   *         arguments.
   */
  public static StartLocationGroup createStartLocationGroup(StartLocation[] startLocations)
  {
    return new StartLocationGroupImpl(Arrays.asList(startLocations));
  }

  /**
   * Creates instance of {@link SimulationMap} class with specified map's
   * tiles and start locations groups.
   *
   * @param mapTiles - two-dimensional array of integers.
   * @param startGroups - array with start locations groups.
   *
   * @return Returns instance of {@link SimulationMap} class with specified
   *         map's tiles and start locations groups.
   */
  public static SimulationMap createSimulationMap(int[][] mapTiles,
      StartLocationGroup[] startGroups)
  {
    if (mapTiles == null)
    {
      throw new NullPointerException("Specified mapTiles is null!");
    }
    if (startGroups == null)
    {
      throw new NullPointerException("Specified startGroups is null!");
    }
    if (startGroups.length < MINIMUM_GROUPS_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument startGroups - it must contain at least " + MapUtils.MINIMUM_GROUPS_COUNT + " start locations!");
    }
    if (startGroups.length > MAXIMUM_GROUPS_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument startGroups - it cannot contain more than " + MapUtils.MAXIMUM_GROUPS_COUNT + " start locations!");
    }

    return new SimulationMapImpl(createMapTiles(mapTiles), Arrays.asList(startGroups));
  }

  /**
   * Creates two-dimensional array of {@link net.java.dante.sim.data.map.MapTileType} objects from
   * two-dimensional array of integers.
   *
   * @param mapTiles - two-dimensional array of integers.
   *
   * @return Returns two-dimensional array of {@link net.java.dante.sim.data.map.MapTileType}
   *         objects.
   */
  private static MapTileType[][] createMapTiles(int[][] mapTiles)
  {
    if (mapTiles == null)
    {
      throw new NullPointerException("Specified mapTiles is null!");
    }
    if (mapTiles.length < MINIMUM_MAP_HEIGHT)
    {
      throw new IllegalArgumentException("Invalid argument mapTiles - its height is not equal to or greater than minimum=" + MINIMUM_MAP_HEIGHT + "!");
    }
    if (mapTiles[0].length < MINIMUM_MAP_WIDTH)
    {
      throw new IllegalArgumentException("Invalid argument mapTiles - its width is not equal to or greater than minimum=" + MINIMUM_MAP_WIDTH + "!");
    }

    if (!checkObstacleLine(mapTiles[0]) || !checkObstacleLine(mapTiles[mapTiles.length - 1]))
    {
      throw new IllegalArgumentException("Invalid argument mapTiles - it is not fully surrounded with obstacles!");
    }

    final int obstacleTileType = MapTileType.OBSTACLE.ordinal();

    MapTileType[][] tiles = new MapTileType[mapTiles.length][mapTiles[0].length];
    for (int i = 0; i < mapTiles.length; i++)
    {
      if (mapTiles[0].length != mapTiles[i].length)
      {
        throw new IllegalArgumentException("Invalid argument mapTiles - it contains different lengths of tiles columns in row " + i + "!");
      }
      if ((mapTiles[i][0] != obstacleTileType) || (mapTiles[i][mapTiles[i].length - 1] != obstacleTileType))
      {
        throw new IllegalArgumentException("Invalid argument mapTiles - it is not fully surrounded with obstacles!");
      }

      for (int j = 0; j < mapTiles[i].length; j++)
      {
        tiles[i][j] = MapTileType.values()[mapTiles[i][j]];
      }
    }

    return tiles;
  }


  /**
   * Checks whether whole specified line with tiles contains only obstacles or not.
   *
   * @param tilesLine - line with tiles, should contain only obstacles.
   *
   * @return Returns <code>true</code> if line contains only obstacles,
   *         <code>false</code> otherwise.
   */
  private static boolean checkObstacleLine(int[] tilesLine)
  {
    final int obtacleTileType = MapTileType.OBSTACLE.ordinal();
    boolean onlyObstacles = true;
    for (int i = 0; i < tilesLine.length; i++)
    {
      if (tilesLine[i] != obtacleTileType)
      {
        onlyObstacles = false;
        break;
      }
    }

    return onlyObstacles;
  }
}