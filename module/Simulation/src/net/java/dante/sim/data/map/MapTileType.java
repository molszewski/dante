/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.map;

/**
 * Enumeration representing type of tile on the map. 
 *
 * @author M.Olszewski
 */
public enum MapTileType
{
  /** Free space tile, objects can move freely through it. */
  FREE_TILE,
  /** 
   * Almost the same as free tile - the only difference is that it is start 
   * point for object.
   */
  START_LOCATION,
  /** Obstacle tile cannot be destroyed and objects cannot move through it. */
  OBSTACLE;
}