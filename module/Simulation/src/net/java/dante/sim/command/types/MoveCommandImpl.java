/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

/**
 * Implementation of {@link MoveCommand} interface.
 *
 * @author M.Olszewski
 */
final class MoveCommandImpl extends AbstractCommand implements MoveCommand
{
  /** Coordinates ('x', 'y') of movement destination. */
  double x, y;


  /**
   * Creates instance of {@link MoveCommandImpl} class.
   *
   * @param commandId - command's identifier.
   * @param destinationX - projectile's destination 'x' coordinate.
   * @param destinationY - projectile's destination 'y' coordinate.
   */
  MoveCommandImpl(int commandId, double destinationX, double destinationY)
  {
    super(commandId, CommandType.MOVE);

    x = destinationX;
    y = destinationY;
  }


  /**
   * @see net.java.dante.sim.command.types.MoveCommand#getDestinationX()
   */
  public double getDestinationX()
  {
    return x;
  }

  /**
   * @see net.java.dante.sim.command.types.MoveCommand#getDestinationY()
   */
  public double getDestinationY()
  {
    return y;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;

    int result = super.hashCode();

    long temp = Double.doubleToLongBits(x);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = PRIME * result + (int) (temp ^ (temp >>> 32));

    return result;
  }

  /**
   * @see net.java.dante.sim.command.types.AbstractCommand#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = super.equals(object);
    if (equal && (object instanceof MoveCommand))
    {
      final MoveCommand other = (MoveCommand)object;
      equal = ((Double.doubleToLongBits(x) == Double.doubleToLongBits(other.getDestinationX())) &&
               (Double.doubleToLongBits(y) == Double.doubleToLongBits(other.getDestinationY())));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String superString = super.toString();
    return (superString.substring(0, superString.length() - 1) +
        "; destinationX=" + x + "; destinationY=" + y + "]");
  }
}