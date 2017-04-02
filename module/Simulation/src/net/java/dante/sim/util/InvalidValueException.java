/*
 * Created on 2005-11-01
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util;

import java.util.Properties;

import net.java.dante.sim.common.SimulationException;


/**
 * Unchecked exception caused by an error during parsing property value obtained 
 * from {@link Properties} object (e.g. array of values structure is invalid, 
 * there are no integer values in string representing minimum and maximum values)
 *
 * @author M.Olszewski
 */
public class InvalidValueException extends SimulationException
{
  private static final long serialVersionUID = 1L;
  

  /**
   * Constructs a new {@link InvalidValueException} with message containing 
   * string that caused exception.<p> 
   * The cause is not initialized, and may subsequently be initialized by 
   * a call to {@link Throwable#initCause(java.lang.Throwable)}.
   * 
   * @param parsedString {@link String} that caused exception
   */
  public InvalidValueException(String parsedString)
  {
    super("Invalid property value in parsed text: " + parsedString);
  }
  
  /**
   * Constructs a new {@link InvalidValueException} with message containing 
   * string that caused exception.<p> 
   * Note that the detail message associated with <code>cause</code> is <i>not</i> 
   * automatically incorporated in this exception's detail message.
  
   * @param parsedString {@link String} that caused exception
   * @param cause - the cause (which is saved for later retrieval by 
   *        the {@link Throwable#getCause()} method). (A <code>null</code> value 
   *        is permitted, and indicates that the cause is nonexistent 
   *        or unknown.)
   */
  public InvalidValueException(String parsedString, Throwable cause)
  {
    super("Invalid property value in parsed text: " + parsedString, 
          cause);
  }
}