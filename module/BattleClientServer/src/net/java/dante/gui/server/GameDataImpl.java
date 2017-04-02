/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.java.dante.gui.common.Const;
import net.java.dante.gui.common.clients.ConnectedClient;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameState;
import net.java.dante.gui.common.games.GameStatusData;
import net.java.dante.sim.util.SequenceGenerator;



/**
 * Class representing game's data for {@link GameImpl} class.
 *
 * @author M.Olszewski
 */
class GameDataImpl implements GameData
{
  /** Sequence numbers generator. */
  private static final SequenceGenerator seqGen = SequenceGenerator.sequenceGenerator();

  /** This game's identifier. */
  private int gameId;
  /** This game's state. */
  private GameState state = GameState.FREE;
  /** Clients connected with this game. */
  private InternalClientsList clients;


  /**
   * Creates instance of {@link GameDataImpl} with the specified parameters.
   *
   * @param maxClientsCount - maximum number of clients in this game.
   */
  GameDataImpl(final int maxClientsCount)
  {
    if (maxClientsCount < Const.MINIMUM_CLIENTS_IN_GAME_COUNT)
    {
      throw new IllegalArgumentException("Invalid argument maxClientsCount - it must be greater than or equal to MINIMUM_CLIENTS_COUNT=" + Const.MINIMUM_CLIENTS_IN_GAME_COUNT + "!");
    }

    gameId = seqGen.generateId();
    clients = new InternalClientsList(maxClientsCount);
  }


  /**
   * @see net.java.dante.gui.common.games.GameData#getGameId()
   */
  public int getGameId()
  {
    return gameId;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getState()
   */
  public GameState getState()
  {
    return state;
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getClientsCount()
   */
  public int getClientsCount()
  {
    return clients.clientsCount();
  }

  /**
   * Gets array with clients data for this game.
   *
   * @return Returns array with clients data for this game.
   */
  GameStatusData[] getGameClientsData()
  {
    return clients.getGameClientsData();
  }

  /**
   * @see net.java.dante.gui.common.games.GameData#getClientId(int)
   */
  public int getClientId(int index)
  {
    return clients.getGameClientData(index).getClientId();
  }


  /**
   * @see net.java.dante.gui.common.games.GameData#getMaxClientsCount()
   */
  public int getMaxClientsCount()
  {
    return clients.maxClientsCount();
  }

  /**
   * Adds the specified client.
   *
   * @param client - the specified client.
   */
  void addClient(ConnectedClient client)
  {
    clients.addClient(client);
  }

  /**
   * Removes the specified client.
   *
   * @param client - the specified client.
   */
  void removeClient(ConnectedClient client)
  {
    clients.removeClient(client);
  }

  /**
   * Removes all clients from this game.
   */
  void removeAllClients()
  {
    clients.clear();
  }

  /**
   * Add mapping between the specified agents group identifier
   * and some client taking part in this game.
   *
   * @param agentsGroupId - the specified agents group identifier.
   *
   * @return Returns <code>true</code> if all mappings were added,
   *         <code>false</code> otherwise.
   */
  boolean addAgentsGroupMapping(int agentsGroupId)
  {
    return clients.addMapping(agentsGroupId);
  }

  /**
   * Gets client that was mapped with the specified agents group identifier.
   *
   * @param agentsGroupId - the specified agents group identifier.
   *
   * @return Returns client that was mapped with the specified
   *         agents group identifier.
   */
  ConnectedClient getClientWithGroupId(int agentsGroupId)
  {
    return clients.getClientWithGroupId(agentsGroupId);
  }

  /**
   * Gets agents group's identifier for the specified
   * {@link ConnectedClient} object.
   *
   * @param client - the specified {@link ConnectedClient} object.
   *
   * @return Returns agents group's identifier for the specified
   *         {@link ConnectedClient} object.
   */
  Integer getAgentsGroupId(ConnectedClient client)
  {
    return clients.getAgentsGroupId(client);
  }

  /**
   * This method should be invoked if game was created.
   */
  void gameCreated()
  {
    clients.clear();
    state = GameState.CREATED;
  }

  /**
   * Marks the specified client as not ready to start game.
   *
   * @param client - the specified client.
   *
   * @return Returns <code>true</code> if client's data was changed,
   *         <code>false</code> otherwise.
   */
  boolean clientReady(ConnectedClient client)
  {
    return clients.markClientAsReady(client);
  }

  /**
   * Marks the specified client as not ready to start game.
   *
   * @param client - the specified client.
   *
   * @return Returns <code>true</code> if client's data was changed,
   *         <code>false</code> otherwise.
   */
  boolean clientNotReady(ConnectedClient client)
  {
    return clients.markClientAsNotReady(client);
  }

  /**
   * Checks whether all condition that are required to start game
   * are met.
   *
   * @return Returns <code>true</code> if game is ready to be started,
   *         <code>false</code> otherwise.
   */
  boolean isReadyState()
  {
    return clients.isReadyState();
  }

  /**
   * This method should be invoked if game was marked as ready to start.
   */
  void gameReady()
  {
    state = GameState.READY_TO_RUN;
  }

  /**
   * This method should be invoked if game was marked as not ready to start.
   */
  void gameNotReady()
  {
    state = GameState.CREATED;
  }

  /**
   * This method should be invoked if game was marked as initializing.
   */
  void gameInitializing()
  {
    state = GameState.INITIALIZING;
  }

  /**
   * This method should be invoked if game was started or resumed.
   */
  void gameStarted()
  {
    state = GameState.RUNNING;
  }

  /**
   * This method should be invoked if game was paused.
   */
  void gamePaused()
  {
    state = GameState.PAUSED;
  }

  /**
   * This method should be invoked if game was finished.
   */
  void gameFinished()
  {
    clients.clear();
    state = GameState.FINISHED;
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int PRIME = 37;
    int result = 17;

    result = PRIME * result + clients.hashCode();
    result = PRIME * result + gameId;
    result = PRIME * result + state.hashCode();

    return result;
  }


  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object)
  {
    boolean equal = (this == object);
    if (!equal && (object instanceof GameDataImpl))
    {
      final GameDataImpl other = (GameDataImpl) object;
      equal = ((gameId == other.gameId) &&
               (state == other.state) &&
               (clients.equals(other.clients)));
    }
    return equal;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return (getClass() + "[gameId=" + gameId + "; state=" + state +
        "; clients=" + clients + "]");
  }


  /**
   * Class representing internal data for clients (identifiers and statuses).
   *
   * @author M.Olszewski
   */
  final class InternalClientData implements GameStatusData
  {
    /** Client's identifier. */
    private int clientId;
    /** Client's status. */
    private boolean status = false;


    /**
     * Creates instance of {@link InternalClientData} class with the
     * specified parameters.
     *
     * @param clientIdentifier - client's identifier.
     */
    InternalClientData(int clientIdentifier)
    {
      clientId = clientIdentifier;
    }


    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getClientId()
     */
    public int getClientId()
    {
      return clientId;
    }

    /**
     * @see net.java.dante.gui.common.games.GameStatusData#getGameStatus()
     */
    public boolean getGameStatus()
    {
      return status;
    }

    /**
     * Sets this client's status to ready.
     */
    void ready()
    {
      status = true;
    }

    /**
     * Sets this client's status to not ready.
     */
    void notReady()
    {
      status = false;
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
      result = PRIME * result + (status ? 1231 : 1237);

      return result;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object)
    {
      boolean equal = (this == object);
      if (!equal && (object instanceof InternalClientData))
      {
        final InternalClientData other = (InternalClientData) object;
        equal = ((clientId == other.clientId) &&
                 (status == other.status));
      }
      return equal;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[clientId=" + clientId + "; status=" + status + "]");
    }
  }

  /**
   * Class maintaining list of clients in the specified game.
   *
   * @author M.Olszewski
   */
  final class InternalClientsList
  {
    /** Maximum allowed number of clients. */
    private final int maxClients;
    /** Ready clients count */
    private int readyClientsCount = 0;
    /** Clients connected with this game. */
    private Map<ConnectedClient, InternalClientData> clientsMap;
    /** List with clients identifiers connected with this game. */
    private List<InternalClientData> clientsData;
    /** Storage containing mapping between agents groups identifiers and clients. */
    private GroupIdsStorage idsStorage = new GroupIdsStorage();


    /**
     * Creates instance of {@link InternalClientsList} class with specified
     * parameters.
     *
     * @param maxClientsCount - maximum count of clients that can be stored
     *        by this list.
     */
    InternalClientsList(final int maxClientsCount)
    {
      maxClients  = maxClientsCount;
      clientsMap  = new HashMap<ConnectedClient, InternalClientData>(maxClientsCount);
      clientsData = new ArrayList<InternalClientData>(maxClientsCount);
    }


    /**
     * Adds the specified client to this list.
     *
     * @param client - the specified client.
     */
    void addClient(ConnectedClient client)
    {
      if ((clientsMap.size() < maxClients) && (!clientsMap.containsKey(client)))
      {
        InternalClientData data = new InternalClientData(client.getClientData().getClientId());
        clientsMap.put(client, data);
        clientsData.add(data);
      }
    }

    /**
     * Removes the specified from this list.
     *
     * @param client - the specified client.
     */
    void removeClient(ConnectedClient client)
    {
      InternalClientData data = clientsMap.get(client);
      if (data.getGameStatus())
      {
        readyClientsCount--;
      }
      clientsMap.remove(client);
      clientsData.remove(data);
      idsStorage.removeMapping(client);
    }

    /**
     * Add mapping between the specified agents group identifier
     * and some client taking part in this game.
     *
     * @param agentsGroupId - the specified agents group identifier.
     *
     * @return Returns <code>true</code> if all mappings were added,
     *         <code>false</code> otherwise.
     */
    boolean addMapping(int agentsGroupId)
    {
      return (idsStorage.addMapping(clientsMap.keySet(), agentsGroupId) == 0);
    }

    /**
     * Gets client that was mapped with the specified agents group identifier.
     *
     * @param agentsGroupId - the specified agents group identifier.
     *
     * @return Returns client that was mapped with the specified
     *         agents group identifier.
     */
    ConnectedClient getClientWithGroupId(int agentsGroupId)
    {
      return idsStorage.getClient(agentsGroupId);
    }

    /**
     * Gets agents group's identifier for the specified
     * {@link ConnectedClient} object.
     *
     * @param client - the specified {@link ConnectedClient} object.
     *
     * @return Returns agents group's identifier for the specified
     *         {@link ConnectedClient} object.
     */
    Integer getAgentsGroupId(ConnectedClient client)
    {
      return idsStorage.getAgentsGroupId(client);
    }

    /**
     * Marks the specified client as not ready to start game.
     *
     * @param client - the specified client.
     *
     * @return Returns <code>true</code> if client's data was changed,
     *         <code>false</code> otherwise.
     */
    boolean markClientAsReady(ConnectedClient client)
    {
      boolean changed = false;

      InternalClientData clientData = clientsMap.get(client);
      if (clientData != null)
      {
        if (!clientData.getGameStatus())
        {
          readyClientsCount++;
          clientData.ready();

          changed = true;
        }
      }

      return changed;
    }

    /**
     * Marks the specified client as not ready to start game.
     *
     * @param client - the specified client.
     *
     * @return Returns <code>true</code> if client's data was changed,
     *         <code>false</code> otherwise.
     */
    boolean markClientAsNotReady(ConnectedClient client)
    {
      boolean changed = false;

      InternalClientData clientData = clientsMap.get(client);
      if (clientData != null)
      {
        if (clientData.getGameStatus())
        {
          readyClientsCount--;
          clientData.notReady();

          changed = true;
        }
      }

      return changed;
    }

    /**
     * Checks whether there are enough clients to start game.
     *
     * @return Returns <code>true</code> if there are enough clients
     *         to start game, <code>false</code> otherwise.
     */
    boolean isEnoughClients()
    {
      return (clientsMap.size() >= Const.MINIMUM_CLIENTS_IN_GAME_COUNT);
    }

    /**
     * Checks whether clients are ready to start game.
     *
     * @return Returns <code>true</code> if clients are ready,
     *         <code>false</code> otherwise.
     */
    boolean areClientsReady()
    {
      return (clientsMap.size() == readyClientsCount);
    }

    /**
     * Checks whether all condition that are required to start game
     * are met.
     *
     * @return Returns <code>true</code> if game is ready to be started,
     *         <code>false</code> otherwise.
     */
    boolean isReadyState()
    {
      return (isEnoughClients() && areClientsReady());
    }

    /**
     * Gets count of clients stored by this list.
     *
     * @return Returns count of clients stored by this list.
     */
    int clientsCount()
    {
      return clientsMap.size();
    }

    /**
     * Gets client's data for this game from the specified index.
     *
     * @param index - the specified index.
     *
     * @return Returns client's data for this game from the specified index.
     */
    GameStatusData getGameClientData(int index)
    {
      return clientsData.get(index);
    }

    /**
     * Gets array with clients data for this game.
     *
     * @return Returns array with clients data for this game.
     */
    GameStatusData[] getGameClientsData()
    {
      return clientsData.toArray(new GameStatusData[clientsData.size()]);
    }

    /**
     * Gets array with all clients stored by this list.
     *
     * @return Returns array with all clients stored by this list.
     */
    ConnectedClient[] getClients()
    {
      Set<ConnectedClient> conClients = clientsMap.keySet();
      return conClients.toArray(new ConnectedClient[conClients.size()]);
    }

    /**
     * Gets maximum count of clients that can be stored by this list.
     *
     * @return Returns maximum count of clients that can be stored by this list.
     */
    int maxClientsCount()
    {
      return maxClients;
    }

    /**
     * Clears this list.
     */
    void clear()
    {
      clientsMap.clear();
      clientsData.clear();
      idsStorage.clear();
      readyClientsCount = 0;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      final int PRIME = 37;
      int result = 17;

      result = PRIME * result + clientsData.hashCode();
      result = PRIME * result + clientsMap.hashCode();
      result = PRIME * result + idsStorage.hashCode();
      result = PRIME * result + maxClients;
      result = PRIME * result + readyClientsCount;

      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object)
    {
      boolean equal = (this == object);
      if (!equal && (object instanceof InternalClientsList))
      {
        final InternalClientsList other = (InternalClientsList) object;
        equal = ((maxClients == other.maxClients) &&
                 (readyClientsCount == other.readyClientsCount) &&
                 (clientsData.equals(other.clientsData)) &&
                 (clientsMap.equals(other.clientsMap)) &&
                 (idsStorage.equals(other.idsStorage)));
      }
      return equal;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[maxClients=" + maxClients +
          "; readyClientsCount=" + readyClientsCount +
          "; clientsData=" + clientsData +
          "; clientsMap=" + clientsMap +
          "; idsStorage=" + idsStorage + "]");
    }
  }

  /**
   * Storage for mapping between agents group identifiers and clients.
   *
   * @author M.Olszewski
   */
  final class GroupIdsStorage
  {
    /** Mapping between agents groups identifiers and clients. */
    private Map<Integer, ConnectedClient> groupIdToClientMap =
        new HashMap<Integer, ConnectedClient>();
    /** Mapping between clients and agents groups identifiers . */
    private Map<ConnectedClient, Integer> clientToGroupIdMap =
        new HashMap<ConnectedClient, Integer>();
    /** Random generator making mapping creation more interesting. */
    private Random rand = new Random();


    /**
     * Gets {@link ConnectedClient} object for the specified agents group's
     * identifier.
     *
     * @param groupId - the specified agents group's identifier.
     *
     * @return Returns {@link ConnectedClient} object for the specified
     *         agents group's identifier.
     */
    ConnectedClient getClient(int groupId)
    {
      return groupIdToClientMap.get(Integer.valueOf(groupId));
    }

    /**
     * Gets agents group's identifier for the specified
     * {@link ConnectedClient} object.
     *
     * @param client - the specified {@link ConnectedClient} object.
     *
     * @return Returns agents group's identifier for the specified
     *         {@link ConnectedClient} object.
     */
    Integer getAgentsGroupId(ConnectedClient client)
    {
      return clientToGroupIdMap.get(client);
    }

    /**
     * Adds mapping between the specified agents groups identifier and
     * one of clients selected randomly from the specified array of clients
     * taking part in one game. Mapping is added only for client that it is
     * not already mapped.
     *
     * @param clientsCollection - list with clients taking part in this game.
     * @param groupId - the specified agents group's identifier.
     *
     * @return Returns number of not mapped clients left.
     */
    int addMapping(Collection<ConnectedClient> clientsCollection, int groupId)
    {
      List<ConnectedClient> clientsList = new ArrayList<ConnectedClient>(clientsCollection.size());
      for (ConnectedClient client : clientsCollection)
      {
        clientsList.add(client);
      }

      clientsList.removeAll(groupIdToClientMap.values());

      int clientsLeft = 0;
      if (clientsList.size() > 0)
      {
        Integer groupIdObj = Integer.valueOf(groupId);
        int index = rand.nextInt(clientsList.size());
        // Create mapping
        groupIdToClientMap.put(groupIdObj, clientsList.get(index));
        clientToGroupIdMap.put(clientsList.get(index), groupIdObj);

        clientsLeft = clientsList.size() - 1;
      }

      return clientsLeft;
    }

    /**
     * Removes mapping for the specified client.
     *
     * @param client - the specified client.
     */
    void removeMapping(ConnectedClient client)
    {
      Integer groupId = clientToGroupIdMap.get(client);
      if (groupId != null)
      {
        clientToGroupIdMap.remove(client);
        groupIdToClientMap.remove(groupId);
      }
    }

    /**
     * Clears this storage.
     */
    void clear()
    {
      groupIdToClientMap.clear();
      clientToGroupIdMap.clear();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      final int PRIME = 37;
      int result = 17;

      result = PRIME * result + clientToGroupIdMap.hashCode();
      result = PRIME * result + groupIdToClientMap.hashCode();

      return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object)
    {
      boolean equal = (this == object);
      if (!equal && (object instanceof GroupIdsStorage))
      {
        final GroupIdsStorage other = (GroupIdsStorage) object;
        equal = ((clientToGroupIdMap.equals(other.clientToGroupIdMap)) &&
                 (groupIdToClientMap.equals(other.groupIdToClientMap)));
      }
      return equal;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
      return (getClass() + "[clientToGroupIdMap=" + clientToGroupIdMap +
          "; groupIdToClientMap=" + groupIdToClientMap + "]");
    }
  }
}