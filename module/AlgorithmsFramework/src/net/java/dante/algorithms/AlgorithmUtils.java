/*
 * Created on 2006-09-03
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.java.dante.algorithms.data.AgentData;
import net.java.dante.algorithms.data.ObjectData;
import net.java.dante.algorithms.data.SimpleObjectData;
import net.java.dante.algorithms.data.TileData;
import net.java.dante.sim.data.map.MapTileType;
import net.java.dante.sim.data.map.SimulationMap;
import net.java.dante.sim.data.object.ObjectSize;
import net.java.dante.sim.data.template.types.WeaponTemplateData;
import net.java.dante.sim.util.math.Circle2d;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Point2d;
import net.java.dante.sim.util.math.Vector2d;

/**
 * Class containing useful methods for algorithms.
 *
 * @author M.Olszewski
 */
public class AlgorithmUtils
{
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private AlgorithmUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Checks whether the specified object is within range of weapon that belongs
   * to the specified agent.
   *
   * @param agent the specified agent.
   * @param targetObject the specified target object.
   *
   * @return Returns <code>true</code> if object is within weapon range of the
   *         specified agent, <code>false</code> otherwise.
   */
  public static boolean isObjectWithinAttackRange(AgentData agent,
                                                  ObjectData targetObject)
  {
    Point2d point = new Point2d(targetObject.getX() + (targetObject.getSize().getWidth() >> 1),
                                targetObject.getY() + (targetObject.getSize().getHeight() >> 1));
    Circle2d circle = new Circle2d(agent.getX(), agent.getY(), agent.getWeapon().getRange());

    return circle.contains(point);
  }

  /**
   * Checks whether the specified object is within sight range of the specified
   * agent.
   *
   * @param agent the specified agent.
   * @param object the specified object.
   *
   * @return Returns <code>true</code> if the specified object is within
   *         sight range of the specified agent, <code>false</code> otherwise.
   */
  public static boolean isObjectWithinSightRange(AgentData agent,
                                                 SimpleObjectData object)
  {
    Circle2d circle = new Circle2d(agent.getX(), agent.getY(), agent.getSightRange());

    return circle.contains(object.getX() + (object.getSize().getWidth() >> 1),
                           object.getY() + (object.getSize().getHeight() >> 1));
  }

  /**
   * Checks whether the specified object is within the specified range
   * calculated from the specified point.
   *
   * @param pointX the specified point's 'x' coordinates.
   * @param pointY the specified point's 'y' coordinates.
   * @param range the specified range.
   * @param object the specified object.
   *
   * @return Returns <code>true</code> if the specified object is within
   *         range from the specified point, <code>false</code> otherwise.
   */
  public static boolean isObjectWithinRange(double pointX,
                                            double pointY,
                                            double range,
                                            SimpleObjectData object)
  {
    Circle2d circle = new Circle2d(pointX, pointY, range);

    return circle.contains(object.getX() + (object.getSize().getWidth() >> 1),
                           object.getY() + (object.getSize().getHeight() >> 1));
  }

  /**
   * Calculates distance between the specified objects.
   *
   * @param object1 the first object.
   * @param object2 the second object.
   *
   * @return Returns calculated distance between the specified objects.
   */
  public static double calculateDistance(ObjectData object1,
                                         ObjectData object2)
  {
    return MathUtils.calculateDistance(object1.getX() + object1.getSize().getWidth(),
                                       object1.getY() + object1.getSize().getHeight(),
                                       object2.getX() + object1.getSize().getWidth(),
                                       object2.getY() + object2.getSize().getHeight());
  }

  /**
   * Checks whether the specified object is on the way of the moving object
   * (so it checks whether there will occur some collision in future).
   * This method checks all bounds of the specified object.
   *
   * @param objectX the specified object's 'x' coordinate.
   * @param objectY the specified object's 'y' coordinate.
   * @param objectSize the specified object's size.
   * @param movingX the specified moving object's 'x' coordinate.
   * @param movingY the specified moving object's 'y' coordinate.
   * @param movingSpeed the specified moving object's speed.
   * @param movingSize the specified moving object's size.
   *
   * @return Returns <code>true</code> if the specified object is on the way
   *         of moving object, <code>false</code> otherwise.
   */
  public static boolean isObjectOnObjectWay(double objectX, double objectY,
                                            ObjectSize objectSize,
                                            double movingX, double movingY,
                                            Vector2d movingSpeed,
                                            ObjectSize movingSize)
  {
    boolean collision = !movingSpeed.hasZeroLength();

    if (collision)
    {
      int w = objectSize.getWidth();
      int h = objectSize.getHeight();

      int movHalfW = (movingSize.getWidth() >> 1);
      int movHalfH = (movingSize.getHeight() >> 1);

      Vector2d objectsVect = new Vector2d(objectX - movingX - movHalfW,
                                          objectY - movingY - movHalfH);
      Vector2d boundsVect  = new Vector2d(w + (movHalfW << 1), 0);

      // Top bounds

      if (!checkVectorsCollision(movingSpeed, boundsVect, objectsVect))
      {
        // Left bounds
        boundsVect.setVector(0, h + (movHalfH << 1));
        if (!checkVectorsCollision(movingSpeed, boundsVect, objectsVect))
        {
          // Change objects vector
          objectsVect.setVector(objectX + w - movingX + movHalfW,
                                objectY + h - movingY + movHalfH);

          // Right bounds
          boundsVect.setVector(0, -h - (movHalfH << 1));
          if (!checkVectorsCollision(movingSpeed, boundsVect, objectsVect))
          {
            // Bottom bounds
            boundsVect.setVector(-w - (movHalfW << 1), 0);
            if (!checkVectorsCollision(movingSpeed, boundsVect, objectsVect))
            {
              collision = false;
            }
          }
        }
      }
    }

    return collision;
  }
  
  /**
   * Checks whether the specified object is on the way of the moving object
   * (so it checks whether there will occur some collision in future).
   * This method checks all bounds of the specified object.
   *
   * @param objectX the specified object's 'x' coordinate.
   * @param objectY the specified object's 'y' coordinate.
   * @param objectSize the specified object's size.
   * @param movingObject the specified moving object.
   *
   * @return Returns <code>true</code> if the specified object is on the way
   *         of moving object, <code>false</code> otherwise.
   */
  public static boolean isObjectOnObjectWay(double objectX, double objectY,
                                            ObjectSize objectSize,
                                            ObjectData movingObject)
  {
    return isObjectOnObjectWay(objectX, objectY, objectSize,
                               movingObject.getX(), movingObject.getY(),
                               new Vector2d(movingObject.getSpeedX(), movingObject.getSpeedY()),
                               movingObject.getSize());
  }

  /**
   * Checks whether the specified object is on the way of the moving object
   * (so it checks whether there will occur some collision in future).
   * This method checks all bounds of the specified object.
   *
   * @param object the specified object.
   * @param movingObject the specified moving object.
   *
   * @return Returns <code>true</code> if the specified object is on the way
   *         of moving object, <code>false</code> otherwise.
   */
  public static boolean isObjectOnObjectWay(SimpleObjectData object,
                                            ObjectData movingObject)
  {
    return isObjectOnObjectWay(object.getX(), object.getY(), object.getSize(),
                               movingObject.getX(), movingObject.getY(),
                               new Vector2d(movingObject.getSpeedX(), movingObject.getSpeedY()),
                               movingObject.getSize());
  }

  private static boolean checkVectorsCollision(Vector2d v1, Vector2d v2, Vector2d v3)
  {
    boolean collision = false;

    double mx = Vector2d.perpDotProduct(v1, v2);
    if (mx != 0.0)
    {
      double insideVector1 = Vector2d.perpDotProduct(v3, v2) / mx;

      if (insideVector1 >= 0.0)
      {
        Vector2d v4 = new Vector2d(-v3.getX(), -v3.getY());

        double insideVector2 = Vector2d.perpDotProduct(v4, v1) / Vector2d.perpDotProduct(v2, v1);

        if ((insideVector2 >= 0.0) && (insideVector2 <= 1.0))
        {
          collision = true;
        }
      }
    }

    return collision;
  }

  /**
   * Create array containing all tiles from the specified map.
   *
   * @param map the specified map.
   * @param tileSize size of each tile.
   *
   * @return Returns array containing all map tiles.
   */
  public static TileData[] createTilesData(SimulationMap map, ObjectSize tileSize)
  {
    if (map == null)
    {
      throw new NullPointerException("Specified map is null!");
    }
    if (tileSize == null)
    {
      throw new NullPointerException("Specified tileSize is null!");
    }

    final int rowsCount = map.getRows();
    final int colsCount = map.getColumns();

    // Assumed list size: at least 25% of map are tiles
    final int assumedListSize = ((map.getColumns() * map.getRows()) >> 2);
    List<TileData> tilesList = new ArrayList<TileData>(assumedListSize);

    int tileW = tileSize.getWidth();
    int tileH = tileSize.getHeight();
    for (int i = 0; i < rowsCount; i++)
    {
      for (int j = 0; j < colsCount; j++)
      {
        MapTileType tileType = map.getTile(i, j);
        if (tileType == MapTileType.OBSTACLE)
        {
          tilesList.add(new TileDataImpl(j * tileW, i * tileH, tileSize));
        }
      }
    }

    return tilesList.toArray(new TileData[tilesList.size()]);
  }

  /**
   * Contains test methods.
   *
   * @param args not used.
   */
  public static void main(String[] args)
  {
    WeaponTemplateData wtd = new WeaponTemplateData(64.0, 50.0, 8.0, 10, 15, 2000, new ObjectSize(8,8));


    ControlledAgentDataImpl object1 = new ControlledAgentDataImpl("raz",
                                                                  1,
                                                                  new ObjectSize(32, 16),
                                                                  25.0,
                                                                  100,
                                                                  96.0,
                                                                  new WeaponDataImpl(wtd));

    EnemyAgentDataImpl object2 = new EnemyAgentDataImpl("dwa",
                                                        2,
                                                        new ObjectSize(32, 16),
                                                        25.0,
                                                        100,
                                                        96.0,
                                                        new WeaponDataImpl(wtd));

    // Left future collision
    object1.setX(32.0);
    object1.setY(32.0);
    object2.setX(128.0);
    object2.setY(32.0);

    Vector2d vel = getVelocity(object1, object2);
    object1.setSpeedX(vel.getX());
    object1.setSpeedY(vel.getY());

    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(16.1);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(48.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(15.99);
    assertFalse(isObjectOnObjectWay(object2, object1));

    object2.setY(48.01);
    assertFalse(isObjectOnObjectWay(object2, object1));

    // Right future collision
    object1.setX(128.0);
    object1.setY(32.0);
    object2.setX(32.0);
    object2.setY(32.0);

    vel = getVelocity(object1, object2);
    object1.setSpeedX(vel.getX());
    object1.setSpeedY(vel.getY());

    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(16.1);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(48.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setY(15.99);
    assertFalse(isObjectOnObjectWay(object2, object1));

    object2.setY(48.01);
    assertFalse(isObjectOnObjectWay(object2, object1));

    // Bottom future collision
    object1.setX(32.0);
    object1.setY(128.0);
    object2.setX(32.0);
    object2.setY(32.0);

    vel = getVelocity(object1, object2);
    object1.setSpeedX(vel.getX());
    object1.setSpeedY(vel.getY());

    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(64.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(0.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(-0.01);
    assertFalse(isObjectOnObjectWay(object2, object1));

    object2.setX(64.1);
    assertFalse(isObjectOnObjectWay(object2, object1));

    // Top future collision
    object1.setX(32.0);
    object1.setY(32.0);
    object2.setX(32.0);
    object2.setY(128.0);

    vel = getVelocity(object1, object2);
    object1.setSpeedX(vel.getX());
    object1.setSpeedY(vel.getY());

    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(64.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(0.0);
    assertTrue(isObjectOnObjectWay(object2, object1));

    object2.setX(-0.01);
    assertFalse(isObjectOnObjectWay(object2, object1));

    object2.setX(64.1);
    assertFalse(isObjectOnObjectWay(object2, object1));

    object1.setX(32.0);
    object1.setY(32.0);
    object2.setX(0.0);
    object2.setY(0.0);

    vel = getVelocity(object1, object2);
    object1.setSpeedX(vel.getX());
    object1.setSpeedY(vel.getY());

    assertTrue(isObjectOnObjectWay(object2, object1));

    final int maxW = 10000;
    final int maxH = 10000;
    final Random r = new Random();

    int w1 = object1.getSize().getWidth();
    int h1 = object1.getSize().getHeight();

    int w2 = object2.getSize().getWidth();
    int h2 = object2.getSize().getHeight();

    for (int i = 0; i < 100000; i++)
    {
      object1.setX(r.nextInt(maxW));
      object1.setY(r.nextInt(maxH));
      object2.setX(r.nextInt(maxW));
      object2.setY(r.nextInt(maxH));

      double destX = object2.getX() + r.nextInt(w1 + w2 - 1) + 1 - (w1 >> 1);
      double destY = object2.getY() + r.nextInt(h1 + h2 - 1) + 1 - (h1 >> 1);

      vel = MathUtils.calculateVelocity(object1.getMaxSpeed(),
                                        object1.getX() + (object1.getSize().getWidth() >> 1),
                                        object1.getY() + (object1.getSize().getHeight() >> 1),
                                        destX, destY);
      object1.setSpeedX(vel.getX());
      object1.setSpeedY(vel.getY());

      assertTrue(isObjectOnObjectWay(object2, object1), "object1[x=" + object1.getX() + "; y=" + object1.getY() + "; dx=" + object1.getSpeedX() + "; dy=" + object1.getSpeedY() +
                 "] object2[x=" + object2.getX() + "; y=" + object2.getY() + "] destX=" + destX + "  destY=" + destY);
    }
  }

  private static Vector2d getVelocity(ObjectData o1, ObjectData o2)
  {
    return MathUtils.calculateVelocity(o1.getMaxSpeed(),
                                       o1.getX() + (o1.getSize().getWidth() >> 1),
                                       o1.getY() + (o1.getSize().getHeight() >> 1),
                                       o2.getX() + (o2.getSize().getWidth() >> 1),
                                       o2.getY() + (o2.getSize().getHeight() >> 1));
  }


  private static void assertTrue(boolean exp)
  {
    if (!exp)
    {
      System.err.println("isObjectOnObjectWay() FAILED at:" + new Throwable().getStackTrace()[1].toString());
    }
  }

  private static void assertTrue(boolean exp, String msg)
  {
    if (!exp)
    {
      System.err.println("isObjectOnObjectWay() FAILED: " + msg + " at:"+ new Throwable().getStackTrace()[1].toString());
    }
  }

  private static void assertFalse(boolean exp)
  {
    if (exp)
    {
      System.err.println("isObjectOnObjectWay() FAILED at:" + new Throwable().getStackTrace()[1].toString());
    }
  }
}