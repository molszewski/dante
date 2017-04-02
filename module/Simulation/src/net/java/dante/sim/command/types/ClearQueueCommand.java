/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski 
 */


package net.java.dante.sim.command.types;

/**
 * Special command for clearing agent's queue. If any agent receives this 
 * command, it must clear its commands queue immediately.
 * 
 * @author M.Olszewski
 */
public interface ClearQueueCommand extends Command
{
  // Intentionally left empty.
}