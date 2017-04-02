/*
 * Created on 2006-07-24
 *
 * @author M.Olszewski
 */

package net.java.dante.sim.data.template;

/**
 * Implementation of {@link TemplateTypeData} interface.
 *
 * @author M.Olszewski
 */
class TemplateTypeDataImpl implements TemplateTypeData
{
  /** Name of the template type. */
  private String type;
  /** Data for the template type. */
  private TemplateData data;

  /**
   * Creates instance of {@link TemplateTypeDataImpl} with specified arguments.
   *
   * @param typeName - name of the template type.
   * @param templateData - data for the template type.
   */
  TemplateTypeDataImpl(String typeName, TemplateData templateData)
  {
    if (typeName == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (templateData == null)
    {
      throw new NullPointerException("Specified typeName is null!");
    }
    if (typeName.length() <= 0)
    {
      throw new IllegalArgumentException("Invalid argument typeName - it cannot be an empty string!");
    }

    type = typeName;
    data = templateData;
  }

  /**
   * @see net.java.dante.sim.data.template.TemplateTypeData#getType()
   */
  public String getType()
  {
    return type;
  }

  /**
   * @see net.java.dante.sim.data.template.TemplateTypeData#getData()
   */
  public TemplateData getData()
  {
    return data;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + data.hashCode();
    result = PRIME * result + type.hashCode();

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof TemplateTypeData))
    {
      final TemplateTypeData other = (TemplateTypeData) object;
      equal = ((type.equals(other.getType())) &&
               (data.equals(other.getData())));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[type=" + type + "; data=" + data + "]");
  }
}