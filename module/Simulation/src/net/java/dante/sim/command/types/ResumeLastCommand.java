/*
 * Created on 2006-08-08
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.command.types;

/**
 * Special command for resuming current command that was stopped.
 * This action can be executed only when any command was interrupted and should
 * be resumed.
 *
 * @author M.Olszewski
 */
public interface ResumeLastCommand extends Command
{
  // Intentionally left empty.
}