/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */


package net.java.dante.gui.server;


import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.sim.util.SequenceGenerator;

/**
 * Implementation of {@link ClientData} interface.
 *
 * @author M.Olszewski
 */
class ClientDataImpl implements ClientData
{
  /** Sequence numbers generator. */
  private static final SequenceGenerator seqGen = SequenceGenerator.sequenceGenerator();

  /** This client's identifier. */
  private int clientId;


  /**
   * Creates instance of {@link ClientDataImpl} class.
   */
  ClientDataImpl()
  {
    clientId = seqGen.generateId();
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
    final int PRIME = 31;
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
    if (!equal && (object instanceof ClientDataImpl))
    {
      final ClientDataImpl other = (ClientDataImpl) object;
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