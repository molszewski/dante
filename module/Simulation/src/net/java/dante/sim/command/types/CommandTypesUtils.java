/*
 * Created on 2006-07-28
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.command.types;

import net.java.dante.sim.util.SequenceGenerator;


/**
 * Utility class providing implementations for following interfaces,
 * representing commands:
 * <ol>
 * <li>{@link net.java.dante.sim.command.types.MoveCommand},
 * <li>{@link net.java.dante.sim.command.types.AttackCommand}
 * <li>{@link net.java.dante.sim.command.types.ClearQueueCommand}
 * <li>{@link net.java.dante.sim.command.types.ResumeLastCommand}
 * </ol>
 *
 * @author M.Olszewski
 */
public final class CommandTypesUtils
{
  /** Identifiers generator. */
  private static final SequenceGenerator idGen = SequenceGenerator.synchronizedSequenceGenerator();


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private CommandTypesUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Creates object of class implementing {@link MoveCommand} interface
   * representing movement command.
   *
   * @param destinationX destination's 'x' coordinate.
   * @param destinationY destination's 'y' coordinate.
   *
   * @return Returns object of class implementing {@link MoveCommand} interface
   *         representing movement command.
   */
  public static MoveCommand createMoveCommand(double destinationX,
                                              double destinationY)
  {
    return new MoveCommandImpl(idGen.generateId(), destinationX, destinationY);
  }

  /**
   * Creates object of class implementing {@link MoveCommand} interface
   * representing movement command.
   *
   * @param commandId command's identifier.
   * @param destinationX destination's 'x' coordinate.
   * @param destinationY destination's 'y' coordinate.
   *
   * @return Returns object of class implementing {@link MoveCommand} interface
   *         representing movement command.
   */
  public static MoveCommand createMoveCommand(int commandId,
                                              double destinationX,
                                              double destinationY)
  {
    return new MoveCommandImpl(commandId, destinationX, destinationY);
  }

  /**
   * Creates object of class implementing {@link AttackCommand} interface
   * representing attack command.
   *
   * @param destinationX attack's 'x' coordinate.
   * @param destinationY attack's 'y' coordinate.
   *
   * @return Returns object of class implementing {@link AttackCommand} interface
   *         representing attack command.
   */
  public static AttackCommand createAttackCommand(double destinationX,
                                                  double destinationY)
  {
    return new AttackCommandImpl(idGen.generateId(), destinationX, destinationY);
  }

  /**
   * Creates object of class implementing {@link AttackCommand} interface
   * representing attack command.
   *
   * @param commandId command's identifier.
   * @param destinationX attack's 'x' coordinate.
   * @param destinationY attack's 'y' coordinate.
   *
   * @return Returns object of class implementing {@link AttackCommand} interface
   *         representing attack command.
   */
  public static AttackCommand createAttackCommand(int commandId,
                                                  double destinationX,
                                                  double destinationY)
  {
    return new AttackCommandImpl(commandId, destinationX, destinationY);
  }

  /**
   * Creates object of class implementing {@link AttackCommand} interface
   * representing clearing queue command.
   *
   * @return Returns object of class implementing {@link AttackCommand} interface
   *         representing clearing queue command.
   */
  public static ClearQueueCommand createClearQueueCommand()
  {
    return new ClearQueueCommandImpl(idGen.generateId());
  }

  /**
   * Creates object of class implementing {@link ClearQueueCommand} interface
   * representing clearing queue command.
   *
   * @param commandId command's identifier.
   *
   * @return Returns object of class implementing {@link ClearQueueCommand} interface
   *         representing clearing queue command.
   */
  public static ClearQueueCommand createClearQueueCommand(int commandId)
  {
    return new ClearQueueCommandImpl(commandId);
  }

  /**
   * Creates object of class implementing {@link ResumeLastCommand} interface
   * representing command resuming last command from queue.
   *
   * @return Returns object of class implementing {@link ResumeLastCommand} interface
   *         representing command resuming last command from queue.
   */
  public static ResumeLastCommand createResumeLastCommand()
  {
    return new ResumeLastCommandImpl(idGen.generateId());
  }

  /**
   * Creates object of class implementing {@link ResumeLastCommand} interface
   * representing command resuming last command from queue.
   *
   * @param commandId command's identifier.
   *
   * @return Returns object of class implementing {@link ResumeLastCommand} interface
   *         representing command resuming last command from queue.
   */
  public static ResumeLastCommand createResumeLastCommand(int commandId)
  {
    return new ResumeLastCommandImpl(commandId);
  }
}