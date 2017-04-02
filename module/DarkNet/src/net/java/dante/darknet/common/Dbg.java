/*
 * Created on 2005-09-27
 * @author M.Olszewski 
 */
package net.java.dante.darknet.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.util.Date;


/**
 * Class provides easy-to-control debug output.
 * 
 * @author M.Olszewski
 */
public class Dbg
{
  /* Store standard and error outputs */
  private static PrintStream standardOut = System.out;
  private static PrintStream standardErr = System.err;
  
  private static PrintStream lastLogFile = null;
  
  // Debug prefixes
  private static final String debugPrefix   = "[DEBUG]";
  private static final String errorPrefix   = debugPrefix + "[ ERROR   ] ";
  private static final String warningPrefix = debugPrefix + "[ WARNING ] ";
  private static final String infoPrefix    = debugPrefix + "[ INFO    ] ";
  
  // Debug flags
  private static final int DBGS_NONE    = 0x0000;
  private static final int DBGW_ENABLED = 0x0001;
  private static final int DBGE_ENABLED = 0x0002;
  
  // Debug level 
  private static final int DBG_LEVEL = 2;
  
  private static final DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.LONG);
  
  /** Enables/disables time stamps in debug messages. */
  private static final boolean TIME_STAMP = true;
  
  // Debug options - should contain all required flags
  private static final int DEBUG_OPTIONS = DBGS_NONE | DBGW_ENABLED | DBGE_ENABLED;
  
  /**
   * Indicates whether debug output is enabled.
   */
  public static final boolean DEBUG = false;
  
  /**
   * Indicates whether warning debug output is enabled.
   */
  public static final boolean DBGW = (DEBUG && ((DEBUG_OPTIONS & DBGW_ENABLED) == DBGW_ENABLED));
  
  /**
   * Indicates whether error debug output is enabled.
   */
  public static final boolean DBGE = (DEBUG && ((DEBUG_OPTIONS & DBGE_ENABLED) == DBGE_ENABLED));
  
  /**
   * Indicates whether level 1 debug output is enabled.
   */
  public static final boolean DBG1 = (DEBUG && (DBG_LEVEL >= 1));
  
  /**
   * Indicates whether level 2 debug output is enabled.
   */
  public static final boolean DBG2 = (DEBUG && (DBG_LEVEL >= 2));
  
  /**
   * Indicates whether level 3 debug output is enabled.
   */
  public static final boolean DBG3 = (DEBUG && (DBG_LEVEL >= 3));
  
  
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private Dbg()
  {
    // Intentionally left empty.
  }
  
  
  /**
   * Prints <code>string</code> to console with debug prefix.
   * 
   * @param string - string to be printed.
   */
  public static void write(String string)
  {
    if (TIME_STAMP)
    {
      System.err.println(createTimeStamp() + " " + debugPrefix + " " + string);
    }
    else
    {
      System.err.println(debugPrefix + " " + string);
    }
  }
  
  
  /**
   * Prints <code>string</code> to console with warning prefix.
   * 
   * @param string - warning string to be printed.
   */
  public static void warning(String string)
  {
    if (TIME_STAMP)
    {
      System.err.println(createTimeStamp() + " " + warningPrefix + " " + string);
    }
    else
    {
      System.err.println(warningPrefix + string);
    }
  }
  
  
  /**
   * Prints <code>string</code> to console with error prefix.
   * 
   * @param string - error string to be printed.
   */
  public static void error(String string)
  {
    if (TIME_STAMP)
    {
      System.err.println(createTimeStamp() + " " + errorPrefix + " " + string);
    }
    else
    {
      System.err.println(errorPrefix + string);
    }
  }
  
  /**
   * Prints <code>string</code> on console with information prefix
   * 
   * @param string - information string to be printed
   */
  public static void info(String string)
  {
    if (TIME_STAMP)
    {
      System.err.println(createTimeStamp() + " " + infoPrefix + " " + string);
    }
    else
    {
      System.err.println(infoPrefix + string);
    }
  }
  
  
  /**
   * Throws {@link AssertionError} if specified <code>statement</code> is 
   * <code>false</code>.
   * 
   * @param statement - statement to check.
   * @param string - error with this message is thrown if 
   *        <code>statement</code> is <code>false</code>
   */
  public static void assertion(boolean statement, String string)
  {
    if (!statement)
    {
      throw new AssertionError(string);
    }
  }
  
  
  /**
   * Enables logging and stores all standard and error output in specified file.
   * 
   * @param filePath - path to a file where results sent to standard out and 
   *        error output will be stored.
   */
  public static void enableLogging(File filePath)
  {
    try
    {
      PrintStream logFile = new PrintStream(filePath);
      lastLogFile = logFile;
      System.setOut(logFile);
      System.setErr(logFile);
    }
    catch (FileNotFoundException e)
    {
      if (Dbg.DBGE) 
      {
        Dbg.error("Exception in enableLogging() - printing stack trace.");
      }
      e.printStackTrace();
    }
  }
  
  
  /**
   * Enables logging and stores all standard and error output in specified file.
   * 
   * @param filePath - path to a file where results sent to standard out and 
   *        error output will be stored.
   */
  public static void enableLogging(String filePath)
  {
    enableLogging(new File(filePath));
  }
  
  
  /**
   * Disables logging.
   */
  public static void disableLogging()
  {
    System.setOut(standardOut);
    System.setErr(standardErr);
    
    if (lastLogFile != null)
    {
      lastLogFile.close();
      lastLogFile = null;
    }
  }
  
  private static synchronized String createTimeStamp()
  {
    return timeFormat.format(new Date());
  }
}
