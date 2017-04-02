/*
 * Created on 2006-07-05
 *
 * @author M.Olszewski 
 */

package net.java.dante.sim.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.java.dante.sim.common.Dbg;


/**
 * Class providing methods for reading most common types of values from 
 * setting files like:
 * <ol>
 * <li>boolean values,
 * <li>integer values,
 * <li>long integer values,
 * <li>{@link java.io.File} values,
 * </ol>
 *
 * @author M.Olszewski
 */
public class SettingsReader
{
  /** Buffer size used during manipulation with files */
  private static final int DEFAULT_BUFFER_SIZE = 2048;
  
    
  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private SettingsReader()
  {
    //
  }
  
  
  /**
   * Reads boolean value from specified setting. If read operation was
   * failed, default value is used instead.
   * 
   * @param setting - setting with boolean value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read boolean value or default value in case of any
   *         read failures.
   */
  public static boolean readBoolean(String setting, boolean defaultValue)
  {
    boolean value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      String valueStr = ValuesParser.formatPropertyValue(setting).toLowerCase();
      if (valueStr.equals(Boolean.FALSE.toString()) ||
          valueStr.equals(Boolean.TRUE.toString()))
      {
        value = Boolean.parseBoolean(valueStr);
      }
      else
      {
        if (Dbg.DBGW)
        {
          Dbg.warning("Setting: " + setting + " was not read successfully as boolean value - default value: " + defaultValue  + " used instead.");
        }
      }
    }
    
    return value;
  }
  
  /**
   * Reads boolean value from specified setting. If read operation was
   * failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with boolean value.
   * 
   * @return Returns read boolean value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static boolean readBoolean(String setting)
  {
    boolean value = false;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      String valueStr = ValuesParser.formatPropertyValue(setting).toLowerCase();
      if (valueStr.equals(Boolean.FALSE.toString()) ||
          valueStr.equals(Boolean.TRUE.toString()))
      {
        value = Boolean.parseBoolean(valueStr);
      }
      else
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  /**
   * Reads integer value from specified setting. If read operation was
   * failed, default value is used instead.
   * 
   * @param setting - setting with integer value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read integer value or default value in case of any
   *         read failures.
   */
  public static int readInt(String setting, int defaultValue)
  {
    int value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Integer.parseInt(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        if (Dbg.DBGW)
        {
          Dbg.warning("Setting: " + setting + " was not read successfully as integer value - default value: " + defaultValue  + " used instead.");
        }
      }
    }
    
    return value;
  }
  
  /**
   * Reads integer value from specified setting. If read operation was
   * failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with integer value.
   * 
   * @return Returns read integer value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static int readInt(String setting) throws InvalidValueException
  {
    int value = 0;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Integer.parseInt(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  /**
   * Reads long integer value from specified setting. If read operation was
   * failed, default value is used instead.
   * 
   * @param setting - setting with long integer value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read long integer value or default value in case of any
   *         read failures.
   */
  public static long readLong(String setting, long defaultValue)
  {
    long value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Long.parseLong(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        if (Dbg.DBGW)
        {
          Dbg.warning("Setting: " + setting + " was not read successfully as long value - default value: " + defaultValue  + " used instead.");
        }
      }
    }
    
    return value;
  }
  
  /**
   * Reads long integer value from specified setting. If read operation was
   * failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with long integer value.
   * 
   * @return Returns read long integer value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static long readLong(String setting) throws InvalidValueException
  {
    long value = 0;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Long.parseLong(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  /**
   * Reads double value from specified setting. If read operation was
   * failed, default value is used instead.
   * 
   * @param setting - setting with double value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read double value or default value in case of any
   *         read failures.
   */
  public static double readDouble(String setting, double defaultValue)
  {
    double value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Double.parseDouble(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        if (Dbg.DBGW)
        {
          Dbg.warning("Setting: " + setting + " was not read successfully as double value - default value: " + defaultValue  + " used instead.");
        }
      }
    }
    
    return value;
  }
  
  /**
   * Reads double value from specified setting. If read operation was
   * failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with double value.
   * 
   * @return Returns read double value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static double readDouble(String setting) throws InvalidValueException
  {
    double value = 0;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      try
      {
        value = Double.parseDouble(ValuesParser.formatPropertyValue(setting));
      }
      catch (NumberFormatException e)
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  /**
   * Reads {@link String} value from specified setting. If read operation was
   * failed, default value is used instead.
   * 
   * @param setting - setting with {@link String} value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read {@link String} value or default value in case of any
   *         read failures.
   * @throws NullPointerException if <code>defaultValue</code> is <code>null</code>.
   */
  public static String readString(String setting, String defaultValue)
  {
    if (defaultValue == null)
    {
      throw new NullPointerException("Specified defaultValue is null!");
    }
    
    String value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      value = ValuesParser.formatPropertyValue(setting);
    }
    
    return value;
  }
  
  /**
   * Reads {@link String} value from specified setting. If read operation was
   * failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with {@link String} value.
   * 
   * @return Returns read {@link String} value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static String readString(String setting)
  {
    String value = null;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      value = ValuesParser.formatPropertyValue(setting);
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  
  /**
   * Reads {@link java.io.File} value from specified setting. If read operation 
   * was failed, default value is used instead.
   * 
   * @param setting - setting with {@link java.io.File} value.
   * @param defaultValue - default value used in case of read operation failure.
   * 
   * @return Returns read {@link java.io.File} value or default value 
   *         in case of any read failures.
   * @throws NullPointerException if <code>defaultValue</code> is <code>null</code>.
   */
  public static File readFile(String setting, File defaultValue)
  {
    if (defaultValue == null)
    {
      throw new NullPointerException("Specified defaultValue is null!");
    }
    if (!defaultValue.isFile())
    {
      throw new IllegalArgumentException("Specified defaultValue does not refer to file!");
    }
    
    File value = defaultValue;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      File tmpFile = new File(ValuesParser.formatPropertyValue(setting));
      if (tmpFile.isFile())
      {
        value = tmpFile;
      }
      else
      {
        if (Dbg.DBGW)
        {
          Dbg.warning("Setting: " + setting + " was not read successfully as File value - default value: " + defaultValue  + " used instead.");
        }
      }
    }
    
    return value;
  }
  
  /**
   * Reads {@link java.io.File} value from specified setting. If read operation 
   * was failed, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with {@link java.io.File} value.
   * 
   * @return Returns read {@link java.io.File} value.
   * @throws InvalidValueException if read operation was failed.
   */
  public static File readFile(String setting) throws InvalidValueException
  {
    File value = null;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      value = new File(ValuesParser.formatPropertyValue(setting));
      if (!value.isFile())
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return value;
  }
  
  /**
   * Reads array of integer values from specified setting. If read operation
   * fails, {@link InvalidValueException} is being thrown.
   * 
   * @param setting - setting with integer value.
   * @param arraySize - expected size of array. If different array size is found,
   *        {@link InvalidValueException} is being thrown.
   * 
   * @return Returns read array of integer values.
   * @throws InvalidValueException if read operation was failed.
   */
  public static int[] readIntsArray(String setting, int arraySize) throws InvalidValueException
  {
    int[] array = null;
    
    if (ValuesParser.isPropertyValueValid(setting))
    {
      String[] values = ValuesParser.getArrayOfValues(setting, true);
      if (arraySize == values.length)
      {
        array = new int[values.length];
        
        for (int i = 0; i < array.length; i++)
        {
          try
          {
            array[i] = Integer.parseInt(values[i]);
          }
          catch (NumberFormatException e)
          {
            throw new InvalidValueException(setting);
          }
        }
      }
      else
      {
        throw new InvalidValueException(setting);
      }
    }
    else
    {
      throw new InvalidValueException(setting);
    }
    
    return array;
  }
  
  /**
   * Reads settings from specified file and loads it to {@link Properties}
   * object.
   * 
   * @param fileName - name of the file with settings.
   * 
   * @return Returns {@link Properties} object loaded with settings from
   *         specified file name.
   * @throws IOException if any I/O error occurred during reading setting.
   */
  public static Properties readSettings(File fileName) throws IOException
  {
    if (fileName == null)
    {
      throw new NullPointerException("Specified fileName is null!");
    }
    if (!fileName.isFile())
    {
      throw new IllegalArgumentException("Specified fileName does not refer to a file!");
    }
    
    // Open file
    BufferedInputStream bufferedIn = 
        new BufferedInputStream(new FileInputStream(fileName), DEFAULT_BUFFER_SIZE);
    Properties p = null;
    
    try
    {
      // Create properties, fill them
      p = new Properties();
      p.load(bufferedIn);
    }
    finally
    {
      // Close file
      bufferedIn.close();
    }
    
    return p;
  }
  
  /**
   * Reads settings from specified URL and loads it to {@link Properties}
   * object.
   * 
   * @param url - the url specifing file with settings.
   * 
   * @return Returns {@link Properties} object loaded with settings from
   *         specified file name.
   * @throws IOException if any I/O error occurred during reading setting.
   */
  public static Properties readSettings(URL url) throws IOException
  {
    if (url == null)
    {
      throw new NullPointerException("Specified fileName is null!");
    }
    
    // Try to open file
    InputStream input = url.openStream();
    Properties p = null;
    
    try
    {
      // Create properties, fill them
      p = new Properties();
      p.load(input);
    }
    finally
    {
      // Close file
      input.close();
    }
    
    return p;
  }
}