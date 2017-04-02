/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.server;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.java.dante.gui.common.LogsManager;
import net.java.dante.gui.common.Utils;
import net.java.dante.gui.common.clients.ClientData;
import net.java.dante.gui.common.games.GameStatusData;




/**
 * Server frame representing server's GUI.
 *
 * @author M.Olszewski
 */
public class ServerFrame extends JFrame
{
  private static final long serialVersionUID = 1L;

  /** Maximum number of log lines. */
  private static final int MAX_LOG_LINES = 5000;

  /** Preferred window's width. */
  private static final int WINDOW_WIDTH  = 1024;
  /** Preferred window's height. */
  private static final int WINDOW_HEIGHT = 768;

  /** Preferred games panel's width. */
  private static final int GAMES_PANEL_WIDTH  = 804;
  /** Preferred games panel's height. */
  private static final int GAMES_PANEL_HEIGHT = 640;

  /** Preferred game panel's width. */
  private static final int GAME_PANEL_WIDTH  = 804;
  /** Preferred game panel's height. */
  private static final int GAME_PANEL_HEIGHT = 620;

  /** Tree nodes manager. */
  private TreeNodesManager nodesMan = new TreeNodesManager();
  /** Logs manager with clear log and scroll lock buttons. */
  LogsManager logsManager = new LogsManager(MAX_LOG_LINES, LogsManager.ALL_BUTTONS);

  /** All games panels. */
  private JPanel[]    gamesPanels = null;
  /** Game panel with tabs. */
  private JTabbedPane gamesPanel  = null;


  /**
   * Creates instance of {@link ServerFrame} class and initializes it.
   *
   * @param maxGames - maximum number of games run simultaneously.
   */
  public ServerFrame(int maxGames)
  {
    super();

    if (maxGames <= 0)
    {
      throw new IllegalArgumentException("Invalid argument maxGames - it must be positive integer!");
    }

    gamesPanels = new JPanel[maxGames];
  }

  /**
   * This method initializes server's frame.
   */
  void initialize()
  {
    setContentPane(initializeMainSplitPane());
    initializeGamesPanels();

    setMinimumSize(new Dimension(WINDOW_WIDTH/2, WINDOW_HEIGHT/2));
    setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
  }

  /**
   * Initializes main split panel.
   *
   * @return Returns initialized {@link javax.swing.JSplitPane} component.
   */
  private JSplitPane initializeMainSplitPane()
  {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    splitPane.setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    splitPane.setTopComponent(initializeCentralSplitPanel());
    splitPane.setBottomComponent(logsManager.getLogPanel());

    return splitPane;
  }

  /**
   * This method initializes bottom split panel.
   *
   * @return Returns initialized {@link javax.swing.JSplitPane} component.
   */
  private JSplitPane initializeCentralSplitPanel()
  {
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                                          initializeTreeData(),
                                          initializeGamePanel());

    return splitPane;
  }

  /**
   * Initializes tree with clients and games data.
   *
   * @return Returns initialized {@link javax.swing.JTree} component.
   */
  private JTree initializeTreeData()
  {
    JTree treeData = new JTree(nodesMan.getTreeModel());
    treeData.setFont(Utils.createDefaultFont(10));
    treeData.setMinimumSize(new Dimension(150, WINDOW_HEIGHT/2));

    return treeData;
  }

  /**
   * Initializes games panel.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JTabbedPane initializeGamePanel()
  {
    gamesPanel = new JTabbedPane();
    Dimension prefDim = new Dimension(GAMES_PANEL_WIDTH, GAMES_PANEL_HEIGHT);
    gamesPanel.setMinimumSize(prefDim);
    gamesPanel.setPreferredSize(prefDim);

    return gamesPanel;
  }

  /**
   * Initialize all game panels (one for one possible game).
   */
  private void initializeGamesPanels()
  {
    for (int i = 0; i < gamesPanels.length; i++)
    {
      JPanel gamePanel = new JPanel(null);

      Dimension prefDim = new Dimension(GAME_PANEL_WIDTH, GAME_PANEL_HEIGHT);
      gamePanel.setMinimumSize(prefDim);
      gamePanel.setPreferredSize(prefDim);

      gamesPanel.addTab(("Game#" + i), gamePanel);
      gamesPanel.setEnabledAt(i, false);

      gamesPanels[i] = gamePanel;
    }
  }

  /**
   * Gets first free panel and marks it as not free.
   *
   * @return Returns first free panel or <code>null</code> if no free panel
   *         was found.
   */
  JPanel getFreePanel()
  {
    JPanel freePanel = null;

    for (int i = 0, count = gamesPanel.getTabCount(); i < count; i++)
    {
      if (!gamesPanel.isEnabledAt(i))
      {
        freePanel = gamesPanels[i];
        gamesPanel.setEnabledAt(i, true);
        break;
      }
    }

    return freePanel;
  }

  /**
   * Sets the specified panel as 'free'.
   *
   * @param panel - the specified panel.
   */
  void freePanel(JPanel panel)
  {
    for (int i = 0; i < gamesPanels.length; i++)
    {
      if ((panel == gamesPanels[i]) && (gamesPanel.isEnabledAt(i)))
      {
        gamesPanels[i].repaint();
        gamesPanel.setEnabledAt(i, false);
      }
    }
  }

  /**
   * Adds the specified client data to tree nodes manager.
   *
   * @param client the specified client data.
   */
  void addClientData(ConnectedClientImpl client)
  {
    nodesMan.addClientData(client);
  }

  /**
   * Modify the specified client data in tree nodes manager.
   *
   * @param client the specified client data.
   */
  void modifyClientData(ConnectedClientImpl client)
  {
    nodesMan.modifyClientData(client);
  }

  /**
   * Removes the specified client data from tree nodes manager.
   *
   * @param client the specified client data.
   */
  void removeClientData(ConnectedClientImpl client)
  {
    nodesMan.removeClientData(client.getClientData().getClientId());
  }

  /**
   * Checks whether tree nodes manager contains the specified game's data.
   *
   * @param gameData the specified game's data.
   *
   * @return Returns <code>true</code> if tree nodes manager contains
   *         the specified game's data, <code>false</code> otherwise.
   */
  boolean containsGameData(GameDataImpl gameData)
  {
    return nodesMan.containsGameData(gameData.getGameId());
  }

  /**
   * Adds the specified game's data to tree nodes manager.
   *
   * @param gameData the specified game's data.
   */
  void addGameData(GameDataImpl gameData)
  {
    nodesMan.addGameData(gameData);
  }

  /**
   * Modifies the specified game's data in tree nodes manager.
   *
   * @param gameData the specified game's data.
   */
  void modifyGameData(GameDataImpl gameData)
  {
    nodesMan.modifyGameData(gameData);
  }

  /**
   * Removes the specified game's data from tree nodes manager.
   *
   * @param gameData the specified game's data.
   */
  void removeGameData(GameDataImpl gameData)
  {
    nodesMan.removeGameData(gameData.getGameId());
  }

  /**
   * Appends the specified text line to log. It should not contain
   * new line character at the end - this character is appended automatically.
   *
   * @param logLine - the specified text line.
   */
  void appendLogLine(String logLine)
  {
    logsManager.appendLogLine(logLine);
  }


  /**
   * @param args
   */
  public static void main(String[] args)
  {
    ServerFrame frame = new ServerFrame(4);
    frame.initialize();

    frame.pack();
    frame.setVisible(true);

    try
    {
      Thread.sleep(2000);
    }
    catch (Exception e)
    {
      // Intentionally left empty.
    }

    JPanel[] panels = new JPanel[5];

    for (int i = 0; i < panels.length; i++)
    {
      panels[i] = frame.getFreePanel();

      try
      {
        Thread.sleep(2000);
      }
      catch (Exception e)
      {
        // Intentionally left empty.
      }
    }

    for (int i = 0; i < panels.length; i++)
    {
      System.out.println("panels["+i+"]:"+panels[i]);
    }

    try
    {
      Thread.sleep(2000);
    }
    catch (Exception e)
    {
      // Intentionally left empty.
    }

    for (int i = 0; i < panels.length; i++)
    {
      frame.freePanel(panels[i]);

      try
      {
        Thread.sleep(2000);
      }
      catch (Exception e)
      {
        // Intentionally left empty.
      }
    }

    frame.dispose();
  }
}

final class TreeNodesManager
{
  private static final String READY_TEXT = "READY";
  private static final String NOT_READY_TEXT = "NOT READY";

  private DefaultTreeModel treeModel;

  private DefaultMutableTreeNode clients = new DefaultMutableTreeNode("Clients");

  private DefaultMutableTreeNode games = new DefaultMutableTreeNode("Games");

  private Map<Integer, DefaultMutableTreeNode> gameIdToNode = new
      HashMap<Integer, DefaultMutableTreeNode>();

  private Map<Integer, DefaultMutableTreeNode> clientIdToNode = new
      HashMap<Integer, DefaultMutableTreeNode>();


  TreeNodesManager()
  {
    //
  }

  DefaultTreeModel getTreeModel()
  {
    if (treeModel == null)
    {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Server Data");
      root.add(clients);
      root.add(games);
      treeModel = new DefaultTreeModel(root);
    }

    return treeModel;
  }

  boolean containsGameData(int checkedGameId)
  {
    return gameIdToNode.containsKey(Integer.valueOf(checkedGameId));
  }

  void addGameData(GameDataImpl addedGameData)
  {
    DefaultMutableTreeNode gameNode = new DefaultMutableTreeNode("Game#" + addedGameData.getGameId());
    buildGameNode(gameNode, addedGameData);

    treeModel.insertNodeInto(gameNode, games, games.getChildCount());
    gameIdToNode.put(Integer.valueOf(addedGameData.getGameId()), gameNode);
  }

  void removeGameData(int removedGameId)
  {
    Integer gameId = Integer.valueOf(removedGameId);

    treeModel.removeNodeFromParent(gameIdToNode.get(gameId));
    gameIdToNode.remove(gameId);
  }

  void modifyGameData(GameDataImpl modifiedGameData)
  {
    Integer gameId = Integer.valueOf(modifiedGameData.getGameId());
    DefaultMutableTreeNode node = gameIdToNode.get(gameId);

    if (node != null)
    {
      node.removeAllChildren();
      buildGameNode(node, modifiedGameData);

      treeModel.nodeStructureChanged(node);
    }
  }

  private void buildGameNode(DefaultMutableTreeNode gameNode, GameDataImpl gameData)
  {
    gameNode.add(new DefaultMutableTreeNode("State=" + gameData.getState()));
    DefaultMutableTreeNode clientsNode = new DefaultMutableTreeNode("Clients[" + gameData.getClientsCount() + "]");
    GameStatusData[] statuses = gameData.getGameClientsData();
    for (int i = 0; i < statuses.length; i++)
    {
      clientsNode.add(new DefaultMutableTreeNode((i+1) +
          ". Client#Id=" + statuses[i].getClientId() +
          ", Status=" + getStringStatus(statuses[i].getGameStatus())));
    }
    gameNode.add(clientsNode);
  }

  private String getStringStatus(boolean status)
  {
    return (status)? READY_TEXT : NOT_READY_TEXT;
  }

  void addClientData(ConnectedClientImpl client)
  {
    ClientData clientData = client.getClientData();
    DefaultMutableTreeNode clientNode = new DefaultMutableTreeNode("Client#" + clientData.getClientId());

    buildClientNode(clientNode, client);

    treeModel.insertNodeInto(clientNode, clients, clients.getChildCount());
    clientIdToNode.put(Integer.valueOf(clientData.getClientId()), clientNode);
  }

  void modifyClientData(ConnectedClientImpl client)
  {
    ClientData clientData = client.getClientData();
    Integer clientId = Integer.valueOf(clientData.getClientId());

    DefaultMutableTreeNode node = clientIdToNode.get(clientId);

    if (node != null)
    {
      node.removeAllChildren();
      buildClientNode(node, client);

      treeModel.nodeStructureChanged(node);
    }
  }

  void removeClientData(int removedGameId)
  {
    Integer gameId = Integer.valueOf(removedGameId);

    treeModel.removeNodeFromParent(clientIdToNode.get(gameId));
    clientIdToNode.remove(gameId);
  }


  private void buildClientNode(DefaultMutableTreeNode clientNode, ConnectedClientImpl client)
  {
    clientNode.add(new DefaultMutableTreeNode("IP: " + client.getSession().getRemoteAddress()));
    clientNode.add(new DefaultMutableTreeNode("Bytes sent: " + client.getSession().getSentBytesCount()));
    clientNode.add(new DefaultMutableTreeNode("Bytes received: " + client.getSession().getReceivedBytesCount()));
    clientNode.add(new DefaultMutableTreeNode("Messages sent: " + client.getSession().getSentMessagesCount()));
    clientNode.add(new DefaultMutableTreeNode("Messages received: " + client.getSession().getReceivedMessagesCount()));
  }
}