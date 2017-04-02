/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command.types;

/**
 * Interface for attack commands.
 * 
 * @author M.Olszewski
 */
public interface AttackCommand extends Command
{
  /**
   * Gets 'x' coordinate of attacked target.
   * 
   * @return Returns 'x' coordinate of attacked target.
   */
  double getTargetX();
  
  /**
   * Gets 'y' coordinate of attacked target.
   * 
   * @return Returns 'y' coordinate of attacked target.
   */
  double getTargetY();
}