/*
 * Created on 2006-07-13
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

import junit.framework.TestCase;

/**
 * Test suite for {@link SimulationMapImpl} class.
 * 
 * @author M.Olszewski
 */
public class SimulationMapImplTest extends TestCase
{
  /** Number of map rows from valid map files. */
  private static final int MAP_ROWS = 6;
  /** Number of map columns from valid map files. */
  private static final int MAP_COLUMNS = 26;
  /** Obstacles locations from valid map files. */
  private static final int[][] MAP_OBSTACLES = 
      new int[][] { {1, 4},  {1, 15}, 
                    {2,6},   {2, 7},  {2, 8},
                    {3, 12}, {3, 13}, {3, 14},
                    {4, 18} };
  /** All start agents locations. */
  private static final int[][] MAP_START_LOCATIONS = 
      new int[][] { {1, 23}, {1, 24}, 
                    {2, 23}, {2, 24}, 
                    {3, 1},  {3, 2}, 
                    {4, 1},  {4, 2}};
  /** All groups of start agents locations. */
  private static final int[][][] MAP_GROUPS_START_LOCATIONS = 
      new int[][][] {{ {1, 23}, {1, 24}, {2, 23}, {2, 24} }, 
                     { {3, 1},  {3, 2},  {4, 1},  {4, 2}  }};
  /** Counts of agents start locations groups. */
  private static final int GROUPS_COUNT = 2;
  /** Counts of start locations in start locations groups. */
  private static final int LOCATIONS_IN_GROUPS_COUNT = 4;
  
  
  /**
   * Test method for 
   * {@link net.java.dante.sim.data.map.SimulationMapImpl#SimulationMapImpl(net.java.dante.sim.data.map.MapTileType[][], java.util.List)}.
   */
  public final void testSimulationMapImpl()
  {
    try
    {
      new SimulationMapImpl(
          TestMapUtils.createValidMap(MAP_ROWS, MAP_COLUMNS, MAP_OBSTACLES, MAP_START_LOCATIONS), 
          null);
    }
    catch (NullPointerException e) 
    {
      // Intentionally left empty.
    }
    try
    {
      new SimulationMapImpl(
          null,
          TestMapUtils.createStartLocationGroups(MAP_GROUPS_START_LOCATIONS));
    }
    catch (NullPointerException e) 
    {
      // Intentionally left empty.
    }
    try
    {
      new SimulationMapImpl(null, null);
    }
    catch (NullPointerException e) 
    {
      // Intentionally left empty.
    }
  }

  /**
   * Test method for following methods:
   * <ul>
   * <li>{@link net.java.dante.sim.data.map.SimulationMapImpl#getColumns()}
   * <li>{@link net.java.dante.sim.data.map.SimulationMapImpl#getTile(int, int)}
   * <li>{@link net.java.dante.sim.data.map.SimulationMapImpl#getRows()}
   * <li>{@link net.java.dante.sim.data.map.SimulationMapImpl#getStartLocationGroup(int)}
   * <li>{@link net.java.dante.sim.data.map.SimulationMapImpl#locationGroupsCount()}
   * </ul>
   */
  public final void testPublicInterface()
  {
    MapTileType[][] tiles = TestMapUtils.createValidMap(MAP_ROWS, MAP_COLUMNS, 
        MAP_OBSTACLES, MAP_START_LOCATIONS);
    SimulationMap map = new SimulationMapImpl(
        tiles, 
        TestMapUtils.createStartLocationGroups(MAP_GROUPS_START_LOCATIONS));
    assertEquals(map.getColumns(), MAP_COLUMNS);
    assertEquals(map.getRows(), MAP_ROWS);
    TestMapUtils.checkTiles(map, tiles);
    TestMapUtils.checkGroups(map, tiles, GROUPS_COUNT, LOCATIONS_IN_GROUPS_COUNT);
  }
}