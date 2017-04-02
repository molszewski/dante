/*
 * Created on 2006-08-23
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.clients;

import net.java.dante.gui.common.games.FailureReason;
import net.java.dante.gui.common.games.GameData;
import net.java.dante.gui.common.games.GameOutputData;
import net.java.dante.gui.common.games.GameStatusData;

/**
 * Interface representing client connected to server. This interface intends
 * to define set of methods that should be called when connected client
 * should be notified about some action, e.g. creation of new game.
 *
 * @author M.Olszewski
 */
public interface ConnectedClient
{
  /**
   * Gets data of this client's.
   *
   * @return Returns data of this client's.
   */
  ClientData getClientData();

  /**
   * Method invoked if client successfully created game with the specified
   * identifier.
   *
   * @param gameId - the specified game's identifier.
   */
  void gameCreated(int gameId);

  /**
   * Method invoked if client successfully joined game with the specified
   * identifier.
   *
   * @param gameId - the specified game's identifier.
   * @param gameStatuses - game statuses of all clients in the specified game.
   */
  void gameJoined(int gameId, GameStatusData[] gameStatuses);

  /**
   * Method invoked if client successfully sets its status for the game with
   * the specified identifier to ready.
   *
   * @param gameId - the specified game's identifier.
   */
  void clientSetAsReady(int gameId);

  /**
   * Method invoked if client successfully sets its status for the game with
   * the specified identifier to not ready.
   *
   * @param gameId - the specified game's identifier.
   */
  void clientSetAsNotReady(int gameId);

  /**
   * Method invoked if readiness status of any client (with the specified
   * client's identifier) other than this one changed to the specified
   * value within game with the specified identifier.
   *
   * @param clientId - the specified client's identifier.
   * @param gameId - the specified game's identifier.
   * @param status - the specified client's status.
   */
  void clientGameStatusChanged(int clientId, int gameId, boolean status);

  /**
   * Method invoked if data of some game was changed and should be updated.
   * This message does not denote what has changed - it contains whole data
   * which can be read and compared.
   *
   * @param gameData - the specified changed game's data.
   */
  void gameDataChanged(GameData gameData);

  /**
   * Method invoked if client successfully started game with the specified
   * identifier.
   *
   * @param gameId - the specified game's identifier.
   */
  void gameStarted(int gameId);

  /**
   * Method invoked if initialization phase of game with the specified
   * identifier has failed.
   *
   * @param gameId - the specified game's identifier.
   */
  void gameInitializationFailed(int gameId);

  /**
   * Method invoked if client successfully abandoned game with the specified
   * identifier.
   *
   * @param gameId - the specified game's identifier.
   */
  void gameAbandoned(int gameId);

  /**
   * Method invoked if game with the specified identifier in which client
   * was taking part was finished.
   *
   * @param gameId - the specified game's identifier.
   */
//  void gameFinished(int gameId);

  /**
   * Method invoked if game input sent by client was accepted by game
   * with the specified identifier.
   *
   * @param gameId - the specified game's identifier.
   */
  void gameInputAccepted(int gameId);

  /**
   * Method invoked if the specified game output is ready to sent to this client.
   *
   * @param outputData - the specified game output.
   */
  void gameOutputReady(GameOutputData outputData);

  /**
   * Method invoked if client failed to create game because of the specified
   * reason.
   *
   * @param reason - reason why action was failed.
   */
  void gameCreationFailed(FailureReason reason);

  /**
   * Method invoked if client failed to join game because of the specified
   * reason.
   *
   * @param reason - reason why action was failed.
   */
  void gameJoinFailed(FailureReason reason);

  /**
   * Method invoked if client failed to change its status for some game
   * because of the specified reason.
   *
   * @param reason - reason why action was failed.
   */
  void clientStatusChangedFailed(FailureReason reason);

  /**
   * Method invoked if client failed to start game because of the specified
   * reason.
   *
   * @param reason - reason why action was failed.
   */
  void gameStartFailed(FailureReason reason);

  /**
   * Method invoked if client failed to abandon game because of the specified
   * reason.
   *
   * @param reason - reason why action was failed.
   */
  void gameAbandonFailed(FailureReason reason);

  /**
   * Method invoked if game input sent by client was not accepted by game
   * with the specified identifier.
   *
   * @param reason - reason why action was failed.
   */
  void gameInputNotAccepted(FailureReason reason);

  /**
   * Method invoked if any client (with the specified {@link ClientData})
   * other than this one connected to the server.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  void clientConnected(ClientData clientData);

  /**
   * Method invoked if any client (with the specified {@link ClientData})
   * other than this one disconnected from the server.
   *
   * @param clientData - the specified {@link ClientData} object.
   */
  void clientDisconnected(ClientData clientData);

  /**
   * Method invoked if current client was successfully connected to the server
   * and the specified initialization data was generated for him.
   *
   * @param clientInitData - client's initialization data.
   */
  void clientInitialized(ClientInitializationData clientInitData);
}