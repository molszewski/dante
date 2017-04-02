/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command.types;

/**
 * Interface for move commands.
 * 
 * @author M.Olszewski
 */
public interface MoveCommand extends Command
{
  /**
   * Gets 'x' coordinate of movement destination.
   * 
   * @return Returns 'x' coordinate of movement destination.
   */
  double getDestinationX();
  
  /**
   * Gets 'y' coordinate of movement destination.
   * 
   * @return Returns 'y' coordinate of movement destination.
   */
  double getDestinationY();
}