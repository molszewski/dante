/*
 * Created on 2006-08-10
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.engine.engine2d;

import java.util.List;

import net.java.dante.sim.engine.EngineObjectsGroup;


/**
 * Class representing group of obstacle tiles. 
 * 
 * @author M.Olszewski
 */
public class Engine2dTilesGroup implements EngineObjectsGroup
{
  /** Obstacle tiles list. */
  private List<Engine2dStillObject> tiles;
  

  /**
   * Creates instance of {@link Engine2dTilesGroup} class.
   * 
   * @param tilesList - list of the tiles.
   */
  public Engine2dTilesGroup(List<Engine2dStillObject> tilesList)
  {
    if (tilesList == null)
    {
      throw new NullPointerException("Specified tilesList is null!");
    }
    if (tilesList.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument tilesList - it must contain at least 1 element!");
    }
    tiles = tilesList;
  }

  
  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#update(long)
   */
  public void update(long delta)
  {
    // Intentionally left empty.
  }

  /**
   * @see net.java.dante.sim.engine.EngineObjectsGroup#render()
   */
  public void render()
  {
    for (Engine2dStillObject tile : tiles)
    {
      tile.render();
    }
  }
}