/*
 * Created on 2006-07-28
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.data.object.agent.command;

import net.java.dante.sim.data.object.agent.ServerAgentState;

/**
 * Class defining base for subclasses implementing 
 * {@link net.java.dante.sim.data.object.agent.command.AgentCommand} interface, using 
 * 'State' pattern managed by {@link CommandStateMachine} object.
 * All subclasses must create appropriate {@link CommandStateMachine} object
 * in their constructors.
 * 
 * @author M.Olszewski
 */
abstract class AbstractAgentCommand implements AgentCommand
{
  /** State machine for this command. */
  protected CommandStateMachine stateMachine;

  
  /**
   * Creates instance of {@link AbstractAgentCommand} class.
   */
  AbstractAgentCommand()
  {
    // Intentionally left empty.
  }
  
  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#execute(int, int, net.java.dante.sim.data.object.agent.ServerAgentState, long)
   */
  public void execute(int agentId, int groupId, ServerAgentState agentState, long elapsedTime)
  {
    stateMachine.execute(agentId, groupId, agentState, elapsedTime);
  }

  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#stop()
   */
  public void stop()
  {
    stateMachine.stop();
  }

  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#restart()
   */
  public void restart()
  {
    stateMachine.restart();
  }
  
  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#reverse(ServerAgentState)
   */
  public void reverse(ServerAgentState agentState)
  {
    stateMachine.reverse(agentState);
  }

  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#isDone()
   */
  public boolean isDone()
  {
    return stateMachine.isDone();
  }
  
  /**
   * @see net.java.dante.sim.data.object.agent.command.AgentCommand#isStopped()
   */
  public boolean isStopped()
  {
    return stateMachine.isStopped();
  }
  
  /**
   * Base class defining fundamental behaviour of {@link #restart()} method.
   * 
   * @author M.Olszewski
   */
  protected class BaseStoppedState extends StoppedState
  {
    /**
     * @see net.java.dante.sim.data.object.agent.command.CommandState#restart()
     */
    @Override
    void restart()
    {
      stateMachine.setExecutingState();
    }
  }
}