/*
 * Created on 2006-07-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link StartLocationGroup} interface using
 * {@link ArrayList} as a storage for {@link StartLocation} objects.
 *
 * @author M.Olszewski
 */
final class StartLocationGroupImpl implements StartLocationGroup
{
  /** Locations storage. */
  private List<StartLocation> locations;


  /**
   * Creates instance of this {@link StartLocationGroupImpl}.
   *
   * @param startLocations - list with start locations.
   */
  StartLocationGroupImpl(List<StartLocation> startLocations)
  {
    if (startLocations == null)
    {
      throw new NullPointerException("Specified startLocations is null!");
    }
    if (startLocations.size() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument startLocations - it must contain at least 1 element!");
    }

    locations = startLocations;
  }


  /**
   * @see net.java.dante.sim.data.map.StartLocationGroup#getLocation(int)
   */
  public StartLocation getLocation(int index)
  {
    return locations.get(index);
  }

  /**
   * @see net.java.dante.sim.data.map.StartLocationGroup#locationsCount()
   */
  public int locationsCount()
  {
    return locations.size();
  }

  /**
   * Removes calculated number of locations so locations storage will contain
   * only specified number of locations. Locations are removed from the end
   * of locations storage to its beginning. At least one location must
   * remain in location storage.
   *
   * @param newSize - new size of location storage. Locations above this number
   *        will be removed.
   *
   * @throws IllegalArgumentException if new size is lesser/equal to zero or
   *         greater/equal to locations storage size.
   */
  void cutTo(int newSize)
  {
    if (newSize <= 0)
    {
      throw new IllegalArgumentException("Invalid argument minSize - it must be positive integer!");
    }
    if (newSize >= locations.size())
    {
      throw new IllegalArgumentException("Invalid argument minSize - it must be lesser than amount of locations!");
    }

    // MIN=1, MAX=(locations.size() - 1)
    for (int i = (locations.size() - 1), lastLocToCutIdx = locations.size() - newSize; i >= lastLocToCutIdx; i--)
    {
      locations.remove(i);
    }
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + locations.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof StartLocationGroupImpl))
    {
      final StartLocationGroupImpl other = (StartLocationGroupImpl) object;
      equal = (locations.equals(other.locations));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[locations=" + locations + "]");
  }
}