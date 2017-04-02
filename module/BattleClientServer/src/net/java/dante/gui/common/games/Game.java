/*
 * Created on 2006-08-21
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common.games;

/**
 * Interface representing game.
 *
 * @author M.Olszewski
 */
public interface Game
{
  /**
   * Gets this game's data.
   *
   * @return Returns this game's data.
   */
  GameData getData();

  /**
   * This method should be invoked if game is meant to be created.
   */
  void create();

  /**
   * This method should be invoked if game is meant to be ready to start.
   */
  void ready();

  /**
   * This method should be invoked if game is meant to be not ready to start.
   */
  void notReady();

  /**
   * This method should be invoked if game is meant to be initialized.
   *
   * @param initData - initialization data for this game.
   */
  void init(GameInitData initData);

  /**
   * This method should be invoked if game is meant to be started.
   */
  void start();

  /**
   * This method should be invoked if game is meant to be paused.
   */
  void pause();

  /**
   * This method should be invoked if game is meant to be resumed.
   */
  void resume();

  /**
   * This method should be invoked if game is meant to be finished.
   */
  void finish();

  /**
   * This method should be invoked if game should accept the specified input.
   *
   * @param inputData - input to accept by this game.
   */
  void acceptInput(GameInputData inputData);
}