/*
 * Created on 2006-08-24
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.messages.net.client.update;

import net.java.dante.gui.common.clients.ClientData;

/**
 * Representation of {@link ClientData} sent to clients.
 *
 * @author M.Olszewski
 */
class ConnectedClientData implements ClientData
{
  /** Client's identifier. */
  int clientId;


  /**
   * Creates instance of {@link ConnectedClientData} class from the specified
   * {@link ClientData} object.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  ConnectedClientData(ClientData clientData)
  {
    clientId = clientData.getClientId();
  }

  /**
   * Creates instance of {@link ConnectedClientData} class from the specified
   * parameters.
   *
   * @param clientIdentifier - client's identifier.
   */
  ConnectedClientData(int clientIdentifier)
  {
    clientId = clientIdentifier;
  }


  /**
   * @see net.java.dante.gui.common.clients.ClientData#getClientId()
   */
  public int getClientId()
  {
    return clientId;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + clientId;

    return result;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof ConnectedClientData))
    {
      final ConnectedClientData other = (ConnectedClientData) object;
      equal = (clientId == other.clientId);
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[clientId=" + clientId + "]");
  }
}