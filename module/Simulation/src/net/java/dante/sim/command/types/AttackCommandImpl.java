/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;


/**
 * Implementation of {@link AttackCommand} interface.
 *
 * @author M.Olszewski
 */
final class AttackCommandImpl extends AbstractCommand implements AttackCommand
{
  /** Projectile destination's coordinates ('x','y'). */
  private double x, y;


  /**
   * Creates instance of {@link AttackCommandImpl} class.
   *
   * @param commandId - command's identifier.
   * @param targetX - projectile's destination 'x' coordinate.
   * @param targetY - projectile's destination 'y' coordinate.
   */
  AttackCommandImpl(int commandId, double targetX, double targetY)
  {
    super(commandId, CommandType.ATTACK);

    x = targetX;
    y = targetY;
  }


  /**
   * @see net.java.dante.sim.command.types.AttackCommand#getTargetX()
   */
  public double getTargetX()
  {
    return x;
  }

  /**
   * @see net.java.dante.sim.command.types.AttackCommand#getTargetY()
   */
  public double getTargetY()
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
    if (equal && (object instanceof AttackCommand))
    {
      final AttackCommand other = (AttackCommand) object;
      equal = ((Double.doubleToLongBits(x) == Double.doubleToLongBits(other.getTargetX())) &&
               (Double.doubleToLongBits(y) == Double.doubleToLongBits(other.getTargetY())));
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
        "; targetX=" + x + "; targetY=" + y + "]");
  }
}