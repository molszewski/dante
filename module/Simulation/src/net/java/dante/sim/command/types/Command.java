/*
 * Created on 2006-07-31
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;


/**
 * Interface determining minimum interface for commands.
 *
 * @author M.Olszewski
 */
public interface Command
{
  /**
   * Gets command's identifier.
   *
   * @return Returns command's identifier.
   */
  int getCommandId();

  /**
   * Gets command's type.
   *
   * @return Returns command's type.
   */
  CommandType getCommandType();
}