/*
 * Created on 2006-08-28
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

/**
 * Enumeration defining possible types of commands.
 *
 * @author M.Olszewski
 */
public enum CommandType
{
  /** 'Clear agent's queue' command. */
  CLEAR_QUEUE,
  /** 'Resume last agent's command' command. */
  RESUME_LAST_COMMAND,
  /** 'Attack target' command. */
  ATTACK,
  /** 'Move to destination' command. */
  MOVE
}