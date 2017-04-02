/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

import net.java.dante.sim.util.TempFilesList;

import junit.framework.TestCase;

/**
 * Test suite for {@link MapFileLoader} class loading invalid and
 * valid maps from files.
 *
 * @author M.Olszewski
 */
public class MapFileLoaderTest extends TestCase
{
  /** List with all valid configuration files. */
  private TempFilesList validFiles = new TempFilesList();
  /** List with all invalid configuration files. */
  private TempFilesList invalidFiles = new TempFilesList();
  
  /** Number of map rows from valid map files. */
  private static final int VALID_MAP_ROWS = 6;
  /** Number of map columns from valid map files. */
  private static final int VALID_MAP_COLUMNS = 26;
  /** Obstacles locations from valid map files. */
  private static final int[][] VALID_MAP_OBSTACLES = 
      new int[][] { {1, 4},  {1, 15}, 
                    {2,6},   {2, 7},  {2, 8},
                    {3, 12}, {3, 13}, {3, 14},
                    {4, 18} };
  /** Start agents locations from 1st valid map file. */
  private static final int[][] VALID_MAP1_START_LOCATIONS = 
      new int[][] { {1, 23}, {1, 24}, 
                    {2, 23}, {2, 24}, 
                    {3, 1},  {3, 2}, 
                    {4, 1},  {4, 2}};
  /** Start agents locations from 2nd valid map file. */
  private static final int[][] VALID_MAP2_START_LOCATIONS = 
      new int[][] { {1, 1}, {1, 2}, {1, 16}, 
                    {2, 5}, {2, 24}, 
                    {4, 1}, {4, 2}, {4, 19}};
  /** Start agents locations from 3rd valid map file. */
  private static final int[][] VALID_MAP3_START_LOCATIONS = 
      new int[][] { {1, 1}, {1, 5},  {1, 16}, {1, 19}, {1, 24},
                    {2, 9}, {2, 21}, 
                    {3, 6}, {3, 11}, {3, 15}, {3, 18},
                    {4, 1}, {4, 3},  {4, 17}, {4, 19}, {4, 24} };
  /** All start agents locations from valid map files. */
  private static final int[][][] VALID_MAPS_START_LOCATIONS = new int[][][] {
    VALID_MAP1_START_LOCATIONS, 
    VALID_MAP2_START_LOCATIONS,
    VALID_MAP3_START_LOCATIONS
  };
  /** Counts of agents start locations groups from valid map files. */
  private static final int[] VALID_GROUPS_COUNTS = new int[] { 2, 2, 16 };
  /** Counts of start locations in start locations groups from valid map files. */
  private static final int[] VALID_LOCATIONS_IN_GROUPS_COUNTS = new int[] { 4, 3, 1 };
    
  /** Content of files with valid maps. */
  private static final String[] VALID_MAPS = new String[] { 
      "##########################\n" +
      "#   #          #       22#\n" + 
      "#     ###              22#\n" +
      "#11         ###          #\n" +
      "#11               #      #\n" +
      "##########################\n",
      "##########################\n" +
      "#AA #          #2        #\n" + 
      "#    A###               2#\n" +
      "#           ###          #\n" +
      "#AA               #2     #\n" +
      "##########################\n",
      "##########################\n" +
      "#1  #3         #5  a    2#\n" + 
      "#     ###4           B   #\n" +
      "#     F    7###6  0      #\n" +
      "#e D             8#9    C#\n" +
      "##########################\n"
  };

  /** Content of files with invalid maps. */
  private static final String[] INVALID_MAPS = new String[] {
    "123 random content :)",
    "#11         22#\n",
    "##########################\n" +
    "#11                    22#\n" +
    "##########################\n",
    "######\n" +
    "#11  #\n" +
    "#    #\n" +
    "#    #\n" +
    "#  22#\n" +
    "######\n",
    "############################\n" +
    " ##11        ##          ## \n" +
    "  #          ##          #  \n" +
    "  #       ##             #  \n" +
    " ##       ##           22## \n" +
    "############################\n",
    "#11                    22#\n" +
    "#       ##               #\n" +
    "#       ##               #\n" +
    "##########################\n",
    "##########################\n" +
    "#11        ##          22#\n" +
    "#       ##               #\n" +
    "#       ##   ##          #\n",
    "###  #####################\n" +
    "#11        ##            #\n" +
    "#       ##               #\n" +
    "#       ##              2#\n" +
    "##########################\n",
    "##########################\n" +
    "#11        ##            #\n" +
    "#       ##               #\n" +
    "#       ##              2#\n" +
    "#####  ###################\n",
    "##########################\n" +
    "#11        ##            #\n" +
    "#       ##               #\n" +
    "#       ##             g2#\n" +
    "##########################\n",
    "##########################\n" +
    "#11        ##            #\n" +
    "#       ##     P         #\n" +
    "#       ##             22#\n" +
    "##########################\n",
    "##########################\n" +
    "#11        ##            #\n" +
    "#       #?               #\n" +
    "#       ##             22#\n" +
    "##########################\n",
  };
  
  
  /**
   * @see TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
    validFiles.fillList(VALID_MAPS);
    invalidFiles.fillList(INVALID_MAPS);
  }

  /**
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    validFiles.clear(true);
    invalidFiles.clear(true);
  }
  
  /**
   * Test method for {@link net.java.dante.sim.data.map.MapFileLoader#loadMap()} loading
   * valid maps.
   */
  public final void testLoadFile()
  {
    // Precondition - must be true.
    assertEquals(VALID_MAPS_START_LOCATIONS.length, validFiles.size());
    
    for (int i = 0; i < VALID_MAPS_START_LOCATIONS.length; i++)
    {
      MapLoader loader = new MapFileLoader(validFiles.get(i).getName());
      SimulationMap map = loader.loadMap();
      int row = map.getRows();
      assertEquals(row, VALID_MAP_ROWS);
      int col = map.getColumns();
      assertEquals(col, VALID_MAP_COLUMNS);
      
      // Create valid map with proper tiles and add start locations
      MapTileType[][] tiles = TestMapUtils.createValidMap(VALID_MAP_ROWS, 
          VALID_MAP_COLUMNS, VALID_MAP_OBSTACLES, VALID_MAPS_START_LOCATIONS[i]);
      
      TestMapUtils.checkTiles(map, tiles);
      TestMapUtils.checkGroups(map, tiles, VALID_GROUPS_COUNTS[i], VALID_LOCATIONS_IN_GROUPS_COUNTS[i]);
    }
  }
  
  /**
   * Test method for {@link net.java.dante.sim.data.map.MapFileLoader#loadMap()} loading
   * invalid maps.
   */
  public final void testLoadFileInvalid()
  {
    try
    {
      new MapFileLoader("./probably-non-existing-file-but-if-not-this-test-may-fail.but-i-hope-not");
      fail("IllegalArgumentException was expected!");
    }
    catch (IllegalArgumentException e)
    {
      // Intentionally left empty.
    }
    
    for (int i = 0, size = invalidFiles.size(); i < size; i++)
    {
      try
      {
        MapLoader loader = new MapFileLoader(invalidFiles.get(i).getName());
        loader.loadMap();
        fail("MapLoadingFailedException was expected!");
      }
      catch (MapLoadingFailedException e)
      {
        // Intentionally left empty.
      }
    }
    
  }
}