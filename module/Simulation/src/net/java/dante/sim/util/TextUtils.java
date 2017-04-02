/*
 * Created on 2006-07-13
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.util;


/**
 * Class containing set of useful methods operating on text.
 *
 * @author M.Olszewski
 */
public final class TextUtils
{
  /** DOS and Windows systems file separator. */
  private static final String DOS_SEPARATOR = "\\";
  /** UNIX systems file separator. */
  private static final String UNIX_SEPARATOR = "/";


  /**
   * Private constructor - no external class creation, no inheritance.
   */
  private TextUtils()
  {
    // Intentionally left empty.
  }


  /**
   * Converts specified UNIX file path into DOS file path.
   *
   * @param filePath - UNIX file path.
   *
   * @return Returns converted DOS file path or the same file path if conversion
   *         has not taken place.
   * @throws NullPointerException if <code>filePath</code> is <code>null</code>.
   */
  public static String unix2dosPath(String filePath)
  {
    if (filePath == null)
    {
      throw new NullPointerException("Specified filePath is null!");
    }

    return filePath.replace(UNIX_SEPARATOR, DOS_SEPARATOR);
  }

  /**
   * Converts specified DOS file path into UNIX file path.
   *
   * @param filePath - DOS file path.
   *
   * @return Returns converted UNIX file path or the same file path if conversion
   *         has not taken place.
   * @throws NullPointerException if <code>filePath</code> is <code>null</code>.
   */
  public static String dos2unixPath(String filePath)
  {
    if (filePath == null)
    {
      throw new NullPointerException("Specified filePath is null!");
    }

    return filePath.replace(DOS_SEPARATOR, UNIX_SEPARATOR);
  }
}