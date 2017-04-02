/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;


/**
 * Interface representing map on which simulation takes place. Interface allows
 * to obtain type of each tile on the map, number of rows and columns of the map
 * and groups of object's start locations. Each start locations group contains
 * the same amount of start locations.
 *
 * @author M.Olszewski
 */
public interface SimulationMap
{
  /**
   * Gets type of tile specified by column and row.
   *
   * @param row row's column.
   * @param column tile's column.
   *
   * @return Returns type of tile specified by column and row.
   */
  MapTileType getTile(int row, int column);

  /**
   * Gets total number of map's rows.
   *
   * @return Returns total number of map's rows.
   */
  int getRows();

  /**
   * Gets total number of map's columns.
   *
   * @return Returns total number of map's columns.
   */
  int getColumns();

  /**
   * Gets {@link StartLocationGroup} object at specified index.
   *
   * @param index index of {@link StartLocationGroup} object.
   *
   * @return Returns {@link StartLocationGroup} object at specified index.
   */
  StartLocationGroup getStartLocationGroup(int index);

  /**
   * Gets total number of start locations groups.
   *
   * @return Returns total number of start locations groups.
   */
  int locationGroupsCount();
}