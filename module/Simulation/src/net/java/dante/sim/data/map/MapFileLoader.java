/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.dante.sim.data.common.FileLoader;


/**
 * Class responsible for reading contents of map and converting it to
 * simulation map format.
 *
 * Format of the map file is following:<br>
 * <pre>
 * ##########################<br>
 * #              #       22#<br>
 * #     ###              22#<br>
 * #11         ###          #<br>
 * #11               #      #<br>
 * ##########################</pre>
 * Where:<br>
 * <ul>
 * <li><code>#</code> indicates fixed, invincible obstacles.
 * <li>Spaces marks empty space, where agents can move.
 * <li>Hexadecimal numbers (from <code>0</code> to <code>F</code>)
 *     indicates possible start location for different agents group.
 * </ul>
 *
 * @author M.Olszewski
 */
class MapFileLoader extends FileLoader implements MapLoader
{
  /**
   * Creates instance of {@link MapFileLoader}.
   *
   * @param mapFileName - file name with map's definition.
   */
  MapFileLoader(String mapFileName)
  {
    super(mapFileName);
  }


  /**
   * Loads map from file specified in this object's constructor
   * and converts it into proper {@link SimulationMap}.
   *
   * @return Returns read map in format specified by implementations of
   *         {@link SimulationMap}.
   * @throws MapLoadingFailedException if any error occurred during
   *         reading map definition file or parsing it.
   */
  public SimulationMap loadMap() throws MapLoadingFailedException
  {
    SimulationMap map = null;
    try
    {
      map = Parser.parseMapFile(openFile());
    }
    catch (IOException e)
    {
      throw new MapLoadingFailedException("IOException thrown during reading specified file: '" + getFileName() + "'!", e);
    }

    return map;
  }


  /**
   * Parser class responsible for parsing map definition class, checking
   * its format, collecting data from it and converting extracted data into
   * object of class implementing {@link SimulationMap} interface.
   *
   * @author M.Olszewski
   */
  static class Parser
  {
    /** String representing obstacle tile. */
    private static final String OBSTACLE = "#";
    /** Character representing obstacle tile. */
    private static final char OBSTACLE_CHAR = '#';
    /** Character representing free tile. */
    private static final char SPACE_CHAR = ' ';

    /**
     * Pattern necessary to check whether first and last line of map contains
     * only {@link #OBSTACLE_CHAR} characters.
     */
    private static final Pattern dashLine = Pattern.compile(OBSTACLE + "+");


    /**
     * Parses specified map definition file, checks its validity (size,
     * characters etc.) and returns instance of class implementing
     * {@link SimulationMap} interface, containing all necessary data
     * to run simulation on defined map.
     *
     * @param inputStream - {@link InputStream} object representing file
     *        with map's definition.
     *
     * @return Returns instance of class implementing {@link SimulationMap}
     *         interface, containing all necessary data
     *         to run simulation on defined map.
     *
     * @throws IOException if any I/O error occurred during reading contents
     *         of map's definition file.
     */
    static SimulationMap parseMapFile(InputStream inputStream) throws IOException
    {
      List<String> lines = collectLines(inputStream);
      checkSize(lines);
      return parseLines(lines);
    }

    /**
     * Buffers contents of map definition file into {@link List} object.
     *
     * @param inputStream - {@link InputStream} object representing file
     *        with map's definition.
     *
     * @return Returns {@link List} object containing buffered contents of
     *         map definition file.
     *
     * @throws IOException if any I/O error occurred during reading contents
     *         of map's definition file.
     */
    static private List<String> collectLines(InputStream inputStream) throws IOException
    {
      List<String> lines = null;
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream));

      try
      {
        // Skip blank lines - file can start with them
        String line = skipBlankLines(reader);

        if (line != null)
        {
          lines = new ArrayList<String>();
          // Read _only_ trimmed lines starting and ending with '#'
          while (line != null)
          {
            String trimmedLine = line.trim();
            if (!trimmedLine.startsWith(OBSTACLE) || !trimmedLine.endsWith(OBSTACLE))
            {
              break;
            }

            lines.add(trimmedLine);

            line = reader.readLine();
          }
        }
      }
      finally
      {
        // In any case - try to close BufferedReader
        reader.close();
      }

      return lines;
    }

    /**
     * Reads all lines until non-blank line will be found. Line is considered
     * as blank one if it contains no characters after calling
     * {@link String#trim()} method. Method returns first non-blank line
     * or <code>null</code> if end of file was encountered.
     *
     * @param r - {@link BufferedReader} used to read lines from file.
     *
     * @return Returns first non-blank line or <code>null</code> if
     *         end of file was encountered.
     * @throws IOException if any I/O error occurred during reading lines from
     *         file.
     */
    static private String skipBlankLines(BufferedReader r) throws IOException
    {
      String line = r.readLine();
      while (line != null)
      {
        String trimmedLine = line.trim();
        if (trimmedLine.length() > 0)
        {
          break;
        }
        line = r.readLine();
      }

      return line;
    }

    /**
     * Checks whether buffered contents of map definition file contains correct
     * size:
     * <ul>
     * <li>height must be greater than minimum map's height,
     * <li>width must  be greater than minimum map's width,
     * <li>all widths must be the same.
     * </ul>
     *
     * @param lines - buffered contents of map definition file.
     *
     * @throws MapLoadingFailedException if any size is incorrect.
     */
    static private void checkSize(List<String> lines)
    {
      // Check height
      if (lines.size() <= 0)
      {
        throw new MapLoadingFailedException("Invalid file - no map definition detected!");
      }
      if (lines.size() < MapUtils.MINIMUM_MAP_HEIGHT)
      {
        throw new MapLoadingFailedException("Map height is below minimum value: " + MapUtils.MINIMUM_MAP_HEIGHT + "!");
      }

      int firstLineWidth = lines.get(0).length();
      // Check width of first line
      if (firstLineWidth < MapUtils.MINIMUM_MAP_WIDTH)
      {
        throw new MapLoadingFailedException("Map width is below minimum value: " + MapUtils.MINIMUM_MAP_WIDTH + "!");
      }

      checkWidths(lines, firstLineWidth);
    }

    /**
     * Checks width of all lines from buffered contents of map definition
     * file against specified 'reference' width.
     *
     * @param lines - buffered contents of map definition file to check.
     * @param referenceWidth - width against which all lines will be checked.
     *
     * @throws MapLoadingFailedException if any width is different than
     *         <code>referenceWidth</code>.
     */
    static private void checkWidths(List<String> lines, int referenceWidth)
    {
      for (int i = 0, size = lines.size(); i < size; i++)
      {
        // Each line must have the same width
        if (lines.get(i).length() != referenceWidth)
        {
          throw new MapLoadingFailedException("Not matching map width was found - all lines of map should have the same width!");
        }
      }
    }

    /**
     * Parses lines buffered contents of map definition file and extracts
     * from it object of class implementing {@link SimulationMap} interface
     * and containing locations of all map tiles and start locations
     * of each group.
     *
     * @param lines - buffered contents of map definition file.
     *
     * @return Returns object of {@link SimulationMap} class.
     * @throws MapLoadingFailedException if any error occurred during parsing
     *         read lines.
     */
    static private SimulationMap parseLines(List<String> lines)
    {
      final String firstLine = lines.get(0);
      // First and last line - only dashes are acceptable
      checkDashLine(firstLine);
      checkDashLine(lines.get(lines.size() - 1));

      MapTileType[][] mapData = new MapTileType[lines.size()][firstLine.length()];
      List<StartLocationGroup> startLocations = new ArrayList<StartLocationGroup>();

      // Fill created structures
      collectMapData(lines, mapData, startLocations);

      if (startLocations.size() > MapUtils.MAXIMUM_GROUPS_COUNT)
      {
        throw new MapLoadingFailedException("Map definition file contains more than allowed amount of start locations groups (" + MapUtils.MAXIMUM_GROUPS_COUNT + ")!");
      }

      // Remove 'unnecessary' locations.
      removeUnnecessaryLocations(startLocations);

      return new SimulationMapImpl(mapData, startLocations);
    }

    /**
     * Checks whether line contains only dash (<code>'#'</code>) characters.
     *
     * @param line - line to check.
     *
     * @throws MapLoadingFailedException if line contains any other character.
     */
    static private void checkDashLine(String line)
    {
      Matcher m = dashLine.matcher(line);
      if (!m.matches())
      {
        throw new MapLoadingFailedException("First and last lines of map definition must contain only '" + OBSTACLE_CHAR + "' characters!");
      }
    }

    /**
     * Collect map data (tiles and start locations) from buffered
     * contents of map definition file held in {@link List} object.
     *
     * @param lines - buffered contents of map definition file (input).
     * @param mapTiles - to fill with locations of tiles representing
     *        obstacles/start locations/free tiles (output).
     * @param startLocations - to fill with start locations of all groups (output).
     *
     * @throws MapLoadingFailedException if invalid character was found during
     *         collecting map's data.
     */
    static private void collectMapData(List<String> lines, MapTileType[][] mapTiles,
        List<StartLocationGroup> startLocations)
    {
      // Map containing association between integer
      Map<Integer, List<StartLocation>> startLocationsMap = new HashMap<Integer, List<StartLocation>>();

      // Fill first and last ones
      Arrays.fill(mapTiles[0], MapTileType.OBSTACLE);
      Arrays.fill(mapTiles[mapTiles.length - 1], MapTileType.OBSTACLE);

      // Collect data from lines from second to last but one line
      for (int i = 1, size = (lines.size() - 1); i < size; i++)
      {
        String line = lines.get(i);

        for (int j = 0, length = line.length(); j < length; j++)
        {
          char character = line.charAt(j);
          if (character == SPACE_CHAR)
          {
            mapTiles[i][j] = MapTileType.FREE_TILE;
          }
          else if (character == OBSTACLE_CHAR)
          {
            mapTiles[i][j] = MapTileType.OBSTACLE;
          }
          else
          {
            int digit = Character.digit(character, 16);

            if (digit != -1)
            {
              List<StartLocation> group = startLocationsMap.get(Integer.valueOf(digit));
              if (group == null)
              {
                group = new ArrayList<StartLocation>();
                startLocationsMap.put(Integer.valueOf(digit), group);
              }
              group.add(new StartLocationImpl(i, j));

              mapTiles[i][j] = MapTileType.START_LOCATION;
            }
            else
            {
              throw new MapLoadingFailedException("Found invalid character in map definition file: " + character + " at [row: " + i + "][column: " + j + "]!");
            }
          }
        }
      }

      for (Iterator<Integer> it = startLocationsMap.keySet().iterator(); it.hasNext(); )
      {
        startLocations.add(new StartLocationGroupImpl(startLocationsMap.get(it.next())));
      }
    }

    /**
     * Obtains minimum number of locations from list of {@link StartLocationGroup}
     * and removes all locations from other groups that have more of them
     * (all groups must have the same number of start locations).
     *
     * @param startGroups - list with {@link StartLocationGroup} objects that
     *        will checked whether all groups contains the same number of
     *        start locations.
     *
     * @throws MapLoadingFailedException if list does not contain any
     *         start location group.
     */
    static private void removeUnnecessaryLocations(List<StartLocationGroup> startGroups)
    {
      if (startGroups.size() < MapUtils.MINIMUM_GROUPS_COUNT)
      {
        throw new MapLoadingFailedException("Map definition file does not contain minimum amount of start locations groups (" + MapUtils.MINIMUM_GROUPS_COUNT + ")!");
      }

      int minSize = getMinGroupsCount(startGroups);
      if (minSize != -1)
      {
        for (int i = 0, size = startGroups.size(); i < size; i++)
        {
          StartLocationGroupImpl group = (StartLocationGroupImpl)startGroups.get(i);
          int locationCount = group.locationsCount();
          if (locationCount > minSize)
          {
            group.cutTo(minSize);
          }
        }
      }
    }

    /**
     * Gets size of {@link StartLocationGroup} that contains the smallest number
     * of {@link StartLocation} objects.
     *
     * @param startGroups - list with {@link StartLocationGroup} objects from
     *        which minimum size will be obtained.
     *
     * @return Returns size of {@link StartLocationGroup} that contains
     *         the smallest number of {@link StartLocation} objects.
     */
    static private int getMinGroupsCount(List<StartLocationGroup> startGroups)
    {
      boolean changed = false;
      int minSize = Integer.MAX_VALUE;

      for (int i = 0, size = startGroups.size(); i < size; i++)
      {
        int locationCount = startGroups.get(i).locationsCount();
        if (locationCount < minSize)
        {
          minSize = locationCount;
          changed = true;
        }
      }

      return (changed ? minSize : -1);
    }
  }
}