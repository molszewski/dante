/*
 * Created on 2006-08-03
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

/**
 * Idle command - issues agent to do nothing until next command is available.
 * This command is infinite: it cannot be stopped and is never done.
 *
 * @author M.Olszewski
 */
public final class IdleCommand extends AbstractAgentCommand
{
  /** Idle ready state - does nothing. */
  private static final ReadyState READY_STATE = new ReadyState() {
    // Intentionally left empty.
  };
  /** Idle executing state - does nothing. */
  private static final ExecutingState EXECUTING_STATE = new ExecutingState() {
    // Intentionally left empty.
  };
  /** 
   * Idle stopped state - does nothing and always returns <code>false</code>
   * in {@link StoppedState#isStopped()} method.
   */
  private static final StoppedState STOPPED_STATE = new StoppedState() {
    /** 
     * @see net.java.dante.sim.data.object.agent.command.StoppedState#isStopped()
     */
    @Override
    boolean isStopped()
    {
      return false;
    }
  };
  /** 
   * Idle done state - does nothing and always returns <code>false</code>
   * in {@link DoneState#isDone()} method.
   */
  private static final DoneState DONE_STATE = new DoneState() {
    /** 
     * @see net.java.dante.sim.data.object.agent.command.DoneState#isDone()
     */
    @Override
    boolean isDone()
    {
      return false;
    }
  };
  
  
  /**
   * Creates instance of {@link AttackAgentCommand} with specified identifier. 
   */
  IdleCommand()
  {
    stateMachine = new CommandStateMachine(READY_STATE, EXECUTING_STATE, 
        STOPPED_STATE, DONE_STATE);
  }
}