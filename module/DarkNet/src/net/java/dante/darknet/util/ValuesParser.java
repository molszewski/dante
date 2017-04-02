package net.java.dante.darknet.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.java.dante.darknet.common.Dbg;


/**
 * Class contains set of useful methods that should be used while parsing
 * properties values of any game object.  
 * 
 * @author M.Olszewski
 */
public final class ValuesParser
{
  /** String representing beginning of the array of values  */
  public static final String ARRAY_START = "{";
  /** String representing end of the array of values */
  public static final String ARRAY_END   = "}";
  
  /** Index of minimum value obtained by call to method 
   * {@link #loadMinMax(String) loadMinMax()}*/
  public static final int MIN_VALUE_INDEX      = 0;
  /** Index of maximum value obtained by call to method 
   * {@link #loadMinMax(String) loadMinMax()}*/
  public static final int MAX_VALUE_INDEX      = 1;
  
  /** String representing single quote character: <code>'</code> */ 
  private static final String QUOTE         = "'"; 
  /** String representing double quote character: <code>"</code> */
  private static final String DOUBLE_QUOTE  = "\"";
  
  /** Expected number of values in <code>String</code> representing minimum
   *  and maximum values */
  private static final int MIN_MAX_VALUES_COUNT = 2;
  
  /* Patterns used during parsing array of values */
  
  /** Pattern defining beginning of array of values  */
  private static final Pattern arrayOfValuesBegin = 
      Pattern.compile("^\\{(\\s)*");
  /** Pattern defining end of array of values  */
  private static final Pattern arrayOfValuesEnd = 
      Pattern.compile("^(\\s)*\\}(\\s)*");
  /** Pattern defining one array value - quoted (enclosed by single/double quotes) 
   *  string */
  private static final Pattern arrayValue = 
      Pattern.compile("^(?m)(('((\\\\')|[^'])*')|(\"((\\\\\")|[^\"])*\"))");
  /** Pattern defining separator (comma) between two array values plus leading and 
   * trailing spaces */
  private static final Pattern arrayValuesSeparator = 
      Pattern.compile("(?m)^(\\s)*(,)(\\s)*");
  
  
  /**
   * Checks whether property value is valid. Valid value is different than 
   * <code>null</code> and it's lengthh is greater than zero.
   * 
   * @param value property value to check 
   * 
   * @return <code>true</code> if property value is valid. 
   */
  public static final boolean isPropertyValueValid(String value)
  {
    return ((value != null) && (value.length() > 0));
  }
  
  
  /**
   * Creates new <code>String</code> object from a <code>value</code>
   * representing property value obtained from <code>Properties</code> object.
   * Format operation includes removing trailing and leading spaces and quotes 
   * (single or double) from <code>value</code>.
   * 
   * @param value property value that will be formatted
   * 
   * @return new formatted <code>String</code>
   */
  public static String formatPropertyValue(String value)
  {
    return removeQuotes(value.trim());
  }
  
  
  /**
   * Checks whether string is enclosed by single or double quotes.<p> 
   * Valid quotes are characters: <code>"</code> or <code>'</code>. 
   * Leading or trailing spaces are skipped during the check. 
   * Method returns <code>true</code> if string is between quotes of one kind.<p>
   * Examples of valid quoted strings: 
   * <ul>
   * <li><code>"Valid example 1"</code><br>
   * <li><code>'Valid example 2'</code><br>
   * </ul><p>  
   * Examples of invalid quoted strings:
   * <ul>
   * <li><code>"Invalid example 1'</code><br>
   * <li><code>'Invalid example 2</code><br>
   * <li><code>Invalid example 3"</code><br>
   * </ul>
   * 
   * @param str string that will be checked
   * 
   * @return <code>true</code> if <code>str</code> is enclosed by quotes of one 
   *         kind 
   */
  public static final boolean isQuotedString(String str)
  {
    String s = str.trim();
    
    return ((s.startsWith(QUOTE) && s.endsWith(QUOTE)) ||
            (s.startsWith(DOUBLE_QUOTE) && s.endsWith(DOUBLE_QUOTE)));
  }
  
  
  /**
   * Creates new <code>String</code> object without leading and trailing 
   * single (<code>'</code>) or double quotes (<code>"</code>).
   * Type of quote (single/double) at beginning and at the end of the specified
   * string should be the same. If not, or quotes were not found, the same 
   * object is returned. 
   * 
   * @param s <code>String</code> with quotes to be removed
   *  
   * @return new <code>String</code> object without quotes or the same 
   *         <code>String</code> object if  quotes were not found
   */
  private static String removeQuotes(String s)
  {
    String resultStr = s.trim();
    if (isQuotedString(resultStr))
    {
      resultStr = resultStr.substring(1, resultStr.length() - 1);
    }
    
    return resultStr;
  }
  
  
  /**
   * Checks whether value obtained from <code>Properties</code> object is 
   * representing an array of values. <p> 
   * Array of values begins with <code>{</code> character and ends with 
   * <code>}</code> character. Leading or trailing spaces are skipped during 
   * the check.<p>
   * <b>NOTE:</b><br> 
   * This method will not check whether array of values structure is valid.
   * Use {@link #getArrayOfValues(String, boolean) getArrayOfValues()} to obtain array of 
   * values with full check - this method will check array structure and
   * throw <code>IllegalArgumentException</code> in case of any error 
   * in array of values structure. 
   * 
   * @param value string that will be checked
   * 
   * @return <code>true</code> if value is representing an array of values  
   */
  public static final boolean isArrayOfValues(String value)
  {
    String s = value.trim();
    
    return ((s.startsWith(ARRAY_START) && s.endsWith(ARRAY_END)));
  }
  
  /**
   * Parses given value holding array of integers and returns parsed
   * array of integers.
   * 
   * @param value - value holding array of integers.
   * 
   * @return Returns parsed array of integers.
   */
  public static final int[] getIntegerArray(String value)
  {
    String[] values = getArrayOfValues(value, true);
    int[] array = new int[values.length];
    
    for (int i = 0; i < array.length; i++)
    {
      try
      {
        array[i] = Integer.parseInt(values[i]);
      }
      catch (NumberFormatException e)
      {
        throw new InvalidValueException("Specified value does not contain integer array!");
      }
    }
    
    return array;
  }
  
  
  /**
   * Parses specified string holding array of values.<p> 
   * Array of values has following format:
   * <ul>
   * <li> array starts with <code>{</code> and ends with <code>}</code> characters
   * <li> each array value starts with <code>'</code> or <code>"</code> character
   *      and ends with the same character
   * <li> array values are separated by comma (<code>,</code>)
   * </ul>
   * Method returns array of strings with all obtained values. All obtained values
   * are formatted (trailing and leading spaces and quotes are removed) if 
   * <code>formatValues</code> argument is set to <code>true</code>.<p>
   *  
   * This method support a case of empty array of values - then empty array is 
   * returned. If any error occurred during parsing the specified array of values, 
   * <code>InvalidValueException</code> is thrown.
   * 
   * @param value <code>String</code> representing array of values
   * @param formatValues specifies whether values should be formatted
   * 
   * @return array of strings with all obtained values.
   * 
   * @throws InvalidValueException if an error occurred during parsing array
   *         of values
   */
  public static final String[] getArrayOfValues(String value, boolean formatValues) 
      throws InvalidValueException
  {
    /* Store all parsed values here */
    ArrayList<String> arrayOfValues = new ArrayList<String>();
    
    String workingStr = value.trim();
    Matcher arrayStart = arrayOfValuesBegin.matcher(workingStr);
    
    /* Check whether array starts with '{' character */
    if (arrayStart.find())
    {
      workingStr = workingStr.substring(arrayStart.end());
      
      /* Check whether array does not end immediately - case of empty array */
      if (!isEndOfArray(workingStr))
      {
        boolean gotEndOfArray = false;
        
        while (!gotEndOfArray)
        {
          /* Always match remaining string */
          Matcher valuesFinder = arrayValue.matcher(workingStr);
          
          /* Next value from array should be found */
          if (valuesFinder.find())
          {
            if (formatValues)
            {
              arrayOfValues.add(formatPropertyValue(valuesFinder.group()));
            }
            else
            {
              arrayOfValues.add(valuesFinder.group());
            }
            
            workingStr = workingStr.substring(valuesFinder.end());
            
            /* Check whether array does not end */
            if (isEndOfArray(workingStr))
            {
              gotEndOfArray = true;
            }
            else
            {
              /* If array does not end, then comma must be found */
              Matcher commaMatch = arrayValuesSeparator.matcher(workingStr);
              
              if (commaMatch.find())
              {
                /* Cut string */
                workingStr = workingStr.substring(commaMatch.end());
              }
              else
              {
                if (Dbg.DBGE) Dbg.error("ValuesParser.getArrayOfValues(): Error: Expected comma was not found after the array value! Remaining string: " + workingStr);
                throw new InvalidValueException(workingStr);
              }
            }
          }
          else
          {
            if (Dbg.DBGE) Dbg.error("ValuesParser.getArrayOfValues(): Error: expected array value was not found! Remaining string: " + workingStr);
            throw new InvalidValueException(workingStr);
          }
        }
      }
      else
      {
        if (Dbg.DBGW) Dbg.warning("ValuesParser.getArrayOfValues(): Array of values is empty.");
      }
    }
    else
    {
      if (Dbg.DBGE) Dbg.error("ValuesParser.getArrayOfValues(): String: " + value + " is not representing array of values!");
      throw new InvalidValueException(workingStr);
    }
    
    return arrayOfValues.toArray(new String[0]);
  }
  
  
  /**
   * Checks whether specified string starts with end-of-array character and 
   * if there are any non-whitespace characters after it. 
   * 
   * @param s <code>String</code> that will be checked
   * 
   * @return <code>true</code> if end-of-array character is found at beginning
   *         of the specified string
   * 
   * @throws InvalidValueException if there are any characters after end-of-array character
   */
  private static boolean isEndOfArray(String s) throws InvalidValueException
  {
    boolean arrayEndFound = false;
    
    Matcher arrayEnd = arrayOfValuesEnd.matcher(s);
    /* Check whether next character is '{' - equal to 'end of array' */
    if (arrayEnd.find())
    {
      if (arrayEnd.hitEnd())
      {
        arrayEndFound = true;
      }
      else
      {
        /* Any non-whitespace character after end-of-array-character is an error */
        if (Dbg.DBGE) Dbg.error("ValuesParser.isEndOfArray(): Error - there are some characters after end-of-array character!"); 
        throw new InvalidValueException(s);
      }
    }
    
    return arrayEndFound;
  }
  
  
  /**
   * Loads minimum and maximum values from specified string <code>value</code>,
   * which should have following format:<br>
   * <code>[IntegerValue] - [IntegerValue]</code><p>
   * First integer value represents minimum value, second one represent maximum
   * value.<p>
   * If only one integer value was found in <code>value</code> then it is 
   * stored on first ([0]) and second index ([1]) of array. 
   * 
   * @param values <code>String</code> object representing minimum and maximum 
   *        values
   * 
   * @return array with two integer numbers holding minimum and maximum values
   * 
   * @throws InvalidValueException if minimum and/or maximum values are not integer
   *         values.
   */
  public static int[] loadMinMax(String values) throws InvalidValueException
  {
    int[]    resultValues = new int[2]; 
    String[] minMax       = values.split("-");
    
    /* Check whether values is splitted between min and max values */
    if (minMax.length == MIN_MAX_VALUES_COUNT)
    {
      try
      {
        resultValues[MIN_VALUE_INDEX] = Integer.parseInt(formatPropertyValue(minMax[MIN_VALUE_INDEX]));
        resultValues[MAX_VALUE_INDEX] = Integer.parseInt(formatPropertyValue(minMax[MAX_VALUE_INDEX]));
      }
      catch (NumberFormatException e)
      {
        if (Dbg.DBGE)
        {
          Dbg.error("ValuesParser.loadMinMax(): MINIMUM or MAXIMUM value is not valid integer value!");
          e.printStackTrace();
        }
        
        throw new InvalidValueException(values, e);
      }
    }
    else
    {
      /* If value is not splitted then it must be single integer value */
      try
      {
        resultValues[MIN_VALUE_INDEX] = resultValues[MAX_VALUE_INDEX] = Integer.parseInt(values);
      }
      catch (NumberFormatException e)
      {
        if (Dbg.DBGE)
        {
          Dbg.error("ValuesParser.loadMinMax(): values string doesn't contain integer value!");
          e.printStackTrace();
        }
        
        throw new InvalidValueException(values, e);
      }
    }
    
    return resultValues;
  }
  
  
  /**
   * Method with simple tests.
   * 
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    /* REMOVE QUOTES TEST */
    String testStr = "Alibaba";
    String[] tests = new String[] {
        "    \"Alibaba\"      ", "'Alibaba' ", " \"Alibaba'",
        "  'Alibaba\" ", "\"\"Alibaba\"\"  ", "  ''Alibaba''" 
    };
    String test5 = "\"Alibaba\"";
    String test6 = "'Alibaba'";
    String[] expected = new String[] {
        testStr, testStr, tests[2].trim(), tests[3].trim(), test5, test6
    };
    
    for (int i = 0; i < tests.length; i++)
    {
      String res = removeQuotes(tests[i]);
      if (res.equals(expected[i]))
      {
        Dbg.info(i + " - OK: " + res + " == " + expected[i]);
      }
      else
      {
        Dbg.error(i + " - FAILED: " + res + " != " + expected[i]);
      }
    }
    
    /* ARRAY OF VALUES METHODS TESTS  */
    String arrayV = "{ 'Carbonara', 'Once upon a time', \"Last week\" }";
    Dbg.info("Res: " + isArrayOfValues(arrayV));
    try
    {
      String[] r = getArrayOfValues(arrayV, false);
      for (int i = 0; i < r.length; i++)
      {
        Dbg.info(i + ". " + formatPropertyValue(r[i]));
      }
    }
    catch (InvalidValueException e)
    {
      e.printStackTrace();
    }
    
    try
    {
      String[] r = getArrayOfValues(" { } ", false);
      Dbg.write("Values count: " + r.length);
      for (int i = 0; i < r.length; i++)
      {
        Dbg.info(i + ". " + formatPropertyValue(r[i]));
      }
    }
    catch (InvalidValueException e)
    {
      e.printStackTrace();
    }
    
    /* LOAD MIN MAX TESTS  */
    String minMax1 = "100-150";
    int[] expectedInts1 = {100, 150};
    
    String minMax2 = "100-100";
    int expectedInt = 100;
    
    int[] firstMinMax = loadMinMax(minMax1);
    if ((firstMinMax[MIN_VALUE_INDEX] == expectedInts1[MIN_VALUE_INDEX]) &&
        (firstMinMax[MAX_VALUE_INDEX] == expectedInts1[MAX_VALUE_INDEX]))
    {
      Dbg.write("MIN-MAX Test1 - OK: got min=" + firstMinMax[MIN_VALUE_INDEX] +
          " max=" + firstMinMax[MAX_VALUE_INDEX]);
    }
    
    int[] secondMinMax = loadMinMax(minMax2);
    if ((secondMinMax[MIN_VALUE_INDEX] == expectedInt) &&
        (secondMinMax[MAX_VALUE_INDEX] == expectedInt))
    {
      Dbg.write("MIN-MAX Test2 - OK: got min=" + secondMinMax[MIN_VALUE_INDEX] +
          " max=" + secondMinMax[MAX_VALUE_INDEX]);
    }
  }
  
  
}
