/*
 * Created on 2006-09-11
 *
 * @author M.Olszewski
 */

package net.java.dante.algorithms.flock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.java.dante.algorithms.data.AlgorithmData;
import net.java.dante.algorithms.data.ControlledAgentData;
import net.java.dante.sim.engine.time.TimeCounter;
import net.java.dante.sim.util.math.MathUtils;
import net.java.dante.sim.util.math.Point2d;

/**
 * Algorithm setting new destination for flock members after travelling distance
 * between old destination and current destination.
 *
 * @author M.Olszewski
 */
public class DestinationChooser
{
  private static final int MAX_DESTINATION_POINTS = 5;

  private static final int CENTRAL_POINT          = 0;
  private static final int LEFT_TOP_QUARTER       = 1;
  private static final int RIGHT_TOP_QUARTER      = 2;
  private static final int RIGHT_BOTTOM_QUARTER   = 3;
  private static final int LEFT_BOTTOM_QUARTER    = 4;

  private Map<Integer, FlockMember> members;
  private Point2d[] destinations;
  private int currentLocation;
  private List<Point2d> points = new ArrayList<Point2d>(MAX_DESTINATION_POINTS - 1);
  private Random rand = new Random();
  private TimeCounter newJourneyTimer = new TimeCounter(0);
  private double avgSpeed;



  /**
   * Creates instance of {@link DestinationChooser} class with the specified
   * parameters.
   *
   * @param flockMembers map containing all flock members.
   * @param algorithmData algorithm's data.
   */
  public DestinationChooser(Map<Integer, FlockMember> flockMembers,
                           AlgorithmData algorithmData)
  {
    if (flockMembers == null)
    {
      throw new NullPointerException("Specified flockMembers is null!");
    }
    if (algorithmData == null)
    {
      throw new NullPointerException("Specified algorithmData is null!");
    }

    members = flockMembers;

    determineDestinations(algorithmData);
  }

  private void determineDestinations(AlgorithmData algorithmData)
  {
    int maxMemberW = 0;
    int maxMemberH = 0;

    Point2d flockCenter = new Point2d(0, 0);

    for (Integer memberId : members.keySet())
    {
      ControlledAgentData memberData = members.get(memberId).getData();

      int memberW = memberData.getSize().getWidth();
      int memberH = memberData.getSize().getHeight();

      if (memberW > maxMemberW)
      {
        maxMemberW = memberW;
      }
      if (memberH > maxMemberH)
      {
        maxMemberH = memberH;
      }

      // Accummulate agent's positions
      flockCenter.setPoint(flockCenter.getX() + memberData.getX(),
                           flockCenter.getY() + memberData.getY());
      // Accumulate speeds
      avgSpeed += memberData.getMaxSpeed();
    }

    // Calculate central point
    flockCenter.setPoint(flockCenter.getX() / members.size(),
                         flockCenter.getY() / members.size());

    // Calculate average speed
    avgSpeed /= members.size();

    int properDeltaW = (maxMemberW << 1);
    int properDeltaH = (maxMemberH << 1);

    int tileW = algorithmData.getTileSize().getWidth();
    int tileH = algorithmData.getTileSize().getHeight();

    int mapWidth = algorithmData.getMap().getColumns() * tileW - (tileW << 1);
    int mapHeight = algorithmData.getMap().getRows() * tileH - (tileH << 1);

    int centerX = (mapWidth >> 1);
    int centerY = (mapHeight >> 1);

    destinations = new Point2d[MAX_DESTINATION_POINTS];
    destinations[CENTRAL_POINT]        = new Point2d(centerX, centerY);
    // Left top quarter
    destinations[LEFT_TOP_QUARTER]     = new Point2d(properDeltaW, properDeltaH);
    // Right top quarter
    destinations[RIGHT_TOP_QUARTER]    = new Point2d(mapWidth - properDeltaW, properDeltaH);
    // Right bottom quarter
    destinations[RIGHT_BOTTOM_QUARTER] = new Point2d(mapWidth - properDeltaW, mapHeight - properDeltaH);
    // Left bottom quarter
    destinations[LEFT_BOTTOM_QUARTER]  = new Point2d(properDeltaW, mapHeight - properDeltaH);

    if (flockCenter.getX() > centerX)
    {
      if (flockCenter.getY() > centerY)
      {
        currentLocation = RIGHT_BOTTOM_QUARTER;
      }
      else
      {
        currentLocation = RIGHT_TOP_QUARTER;
      }
    }
    else if (flockCenter.getX() < centerX)
    {
      if (flockCenter.getY() < centerY)
      {
        currentLocation = LEFT_TOP_QUARTER;
      }
      else
      {
        currentLocation = LEFT_BOTTOM_QUARTER;
      }
    }
    else
    {
      currentLocation = CENTRAL_POINT;
    }

    startNewJourney();
  }

  /**
   * Updates status of cruise algorithm.
   *
   * @param delta time elapsed since the last update.
   */
  public void update(long delta)
  {
    if (newJourneyTimer.isActionTime(delta))
    {
      startNewJourney();
    }
  }

  /**
   * Starts new journey.
   */
  private void startNewJourney()
  {
    Point2d oldP = destinations[currentLocation];
    Point2d p = getNextDestinationPoint();

    double dist = MathUtils.calculateDistance(p.getX(), p.getY(), oldP.getX(), oldP.getY());
    long time = (long)(dist / avgSpeed) * 1000L + 5;

    for (Integer memberId : members.keySet())
    {
      FlockMember member = members.get(memberId);
      member.setDestination(p);
    }

    newJourneyTimer.reset(time);
  }

  private Point2d getNextDestinationPoint()
  {
    points.clear();

    for (int i = 0; i < destinations.length; i++)
    {
      if (i != currentLocation)
      {
        points.add(destinations[i]);
      }
    }

    Collections.shuffle(points, rand);
    currentLocation = rand.nextInt(points.size());

    return points.get(currentLocation);
  }
}