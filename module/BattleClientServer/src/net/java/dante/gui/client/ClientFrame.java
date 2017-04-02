/*
 * Created on 2006-08-30
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.client;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import net.java.dante.algorithms.BaseAlgorithm;
import net.java.dante.algorithms.BaseAlgorithmImpl;
import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.gui.common.LogsManager;
import net.java.dante.gui.common.messages.receiver.AlgorithmSelectedMessage;
import net.java.dante.gui.common.messages.receiver.ClientConnectionRequestMessage;
import net.java.dante.gui.common.messages.receiver.ClientDisconnectionRequestMessage;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.RunnableReceiver;

/**
 * Client frame.
 *
 * @author M.Olszewski
 */
class ClientFrame extends JFrame
{
  private static final long serialVersionUID = 1L;

  /** Default path to algorithm. */
  private static final String DEFAULT_ALGORITHM_PATH = ".";
//  private static final String DEFAULT_ALGORITHM_PATH = "./../AlgorithmsFramework/bin/rl";

  /** Maximum number of log lines. */
  private static final int MAX_LOG_LINES = 5000;

  /** Preferred window's width. */
  private static final int WINDOW_WIDTH  = 1024;
  /** Preferred window's height. */
  private static final int WINDOW_HEIGHT = 768;

  /** Preferred game's panel width. */
  private static final int PREFERRED_GAME_PANEL_WIDTH  = 804;

  /** Preferred game's panel height. */
  private static final int PREFERRED_GAME_PANEL_HEIGHT  = 612;
  
  private static final String CLASS_FILE_EXT = ".class";
  private static final char BINARY_CLASS_NAME_SEPARATOR_CHAR = '.';


  /** Messages receiver. */
  final Receiver receiver;

  /** Logs manager with clear log and scroll lock buttons. */
  LogsManager logsManager = new LogsManager(MAX_LOG_LINES, LogsManager.ALL_BUTTONS);

  /** Connection configuration dialog. */
  ConnectionConfigDialog connectionConfDialog   = null;
  /** 'Load algorithm' button. */
  JButton   loadAlgorithmButton                 = null;
  /** 'Configure connection' button. */
  JButton   configConnectionButton              = null;
  /** 'Connect' button. */
  JButton   connectButton                       = null;
  /** 'Disconnect' button. */
  JButton   disconnectButton                    = null;
  /** Game panel. */
  JPanel    gamePanel                           = null;



  /**
   * Creates instance of {@link ClientFrame} class and initializes it.
   *
   * @param messagesReceiver messages receiver.
   */
  ClientFrame(Receiver messagesReceiver)
  {
    super();

    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }
    receiver = messagesReceiver;
  }


  /**
   * This method initializes client's frame.
   */
  void initialize()
  {
    initializeConfigConnectionDialog();
    setContentPane(initializeMainSplitPane());
    setMinimumSize(new Dimension(WINDOW_WIDTH/2, WINDOW_HEIGHT/2));
    setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

    pack();
  }

  /**
   * Initializes configuration connection dialog.
   */
  private void initializeConfigConnectionDialog()
  {
    connectionConfDialog = new ConnectionConfigDialog(this, receiver);
    connectionConfDialog.initialize();
  }

  /**
   * Initializes main split panel.
   *
   * @return Returns initialized {@link javax.swing.JSplitPane} component.
   */
  private JSplitPane initializeMainSplitPane()
  {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setDividerSize(1);
    splitPane.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    splitPane.setMaximumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    splitPane.setTopComponent(initializeTopPanel());
    splitPane.setBottomComponent(initializeBottomPanel());

    return splitPane;
  }

  /**
   * Initializes top panel.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeTopPanel()
  {
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout());
    topPanel.setMinimumSize(new Dimension(WINDOW_WIDTH/2, 50));

    topPanel.add(initializeLoadAlgorithmButton());
    topPanel.add(initializeConfigureNetworkButton());
    topPanel.add(initializeConnectButton());
    topPanel.add(initializeDisconnectButton());

    loadAlgorithmButton.setEnabled(true);
    configConnectionButton.setEnabled(true);
    connectButton.setEnabled(false);
    disconnectButton.setEnabled(false);

    return topPanel;
  }

  /**
   * Initializes 'load algorithm' button.
   *
   * @return Returns initialized {@link javax.swing.JButton} component.
   */
  private JButton initializeLoadAlgorithmButton()
  {
    final JFileChooser pathsFC   = initializeAlgorithmPathChooser();
    final JFileChooser classesFC = initializeClassFilesChooser();

    loadAlgorithmButton = new JButton("Setup algorithm");

    loadAlgorithmButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        int retVal = pathsFC.showDialog(connectButton, "Set algorithm's lookup path");
        if (retVal == JFileChooser.APPROVE_OPTION)
        {
          classesFC.setFileFilter(new RestrictedFileFilter(pathsFC.getSelectedFile().getAbsolutePath()));
          classesFC.setCurrentDirectory(pathsFC.getSelectedFile());
          retVal = classesFC.showDialog(connectButton, "Load algorithm");
          if (retVal == JFileChooser.APPROVE_OPTION)
          {
            loadClassFromFile(classesFC.getSelectedFile(),
                              pathsFC.getSelectedFile());
          }
        }
      }
    });

    return loadAlgorithmButton;
  }

  /**
   * Initializes file chooser for '.class' files.
   *
   * @return Returns initialized {@link javax.swing.JFileChooser} component.
   */
  private JFileChooser initializeClassFilesChooser()
  {
    final JFileChooser fc = new JFileChooser(DEFAULT_ALGORITHM_PATH);

    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setAcceptAllFileFilterUsed(false);

    return fc;
  }
  
  private static class RestrictedFileFilter extends FileFilter
  {
    String restrictingDir;
    
    
    RestrictedFileFilter(String restrictingDirectory)
    {
      restrictingDir = restrictingDirectory;
    }
    
    
    /**
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File f)
    {
      String absPath = f.getAbsolutePath();
      return (f.isDirectory() ||
             (f.isFile() && absPath.startsWith(restrictingDir) && absPath.endsWith(CLASS_FILE_EXT)));
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription()
    {
      return "Class files";
    }
  }
  
  /**
   * Initializes file chooser for directories.
   *
   * @return Returns initialized {@link javax.swing.JFileChooser} component.
   */
  private JFileChooser initializeAlgorithmPathChooser()
  {
    final JFileChooser fc = new JFileChooser(DEFAULT_ALGORITHM_PATH);

    fc.setFileFilter(new FileFilter() {

      /**
       * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
       */
      @Override
      public boolean accept(File f)
      {
        return f.isDirectory();
      }

      /**
       * @see javax.swing.filechooser.FileFilter#getDescription()
       */
      @Override
      public String getDescription()
      {
        return "Directories";
      }

    });

    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setAcceptAllFileFilterUsed(false);

    return fc;
  }

  /**
   * Loads descendant of {@link BaseAlgorithm} class from the specified
   * file, using the specified path to load other classes.
   *
   * @param algorithmClassPath the specified file with {@link BaseAlgorithm}
   *        subclass.
   * @param algorithmLookupPath the specified path to load other classes,
   *        required by the selected {@link BaseAlgorithm} subclass. 
   */
  void loadClassFromFile(File algorithmClassPath, File algorithmLookupPath)
  {
    try
    {
      URL pathURL = algorithmLookupPath.toURI().toURL();
      if (pathURL != null)
      {
        URLClassLoader urlLoader = new URLClassLoader(new URL[] { pathURL }, getClass().getClassLoader() );
        
        String algBinPath = algorithmClassPath.getAbsolutePath().replace(algorithmLookupPath.getAbsolutePath() + File.separator, "");
        algBinPath = algBinPath.replace(File.separatorChar, BINARY_CLASS_NAME_SEPARATOR_CHAR);
        algBinPath = algBinPath.replace(CLASS_FILE_EXT, "");
        
        Class<?> cl = urlLoader.loadClass(algBinPath);
        try
        {
          Object clObject = cl.newInstance();
  
          if (BaseAlgorithmImpl.class.isInstance(clObject))
          {
            receiver.postMessage(new AlgorithmSelectedMessage((BaseAlgorithmImpl)clObject));
          }
          else
          {
            JOptionPane.showMessageDialog(this,
                                          "The specified class file does not contain definition of the class derived from BaseAlgorithm class!",
                                          "Class loading error",
                                          JOptionPane.ERROR_MESSAGE);
          }
        }
        catch (Throwable e)
        {
          JOptionPane.showMessageDialog(this,
                                        "The specified class file does not contain proper class definition\n" +
                                        "(it must be concrete class with public access and public nullary constructor)!",
                                        "Class loading error",
                                        JOptionPane.ERROR_MESSAGE);
        }
      }
      else
      {
        JOptionPane.showMessageDialog(this,
                                      "The specified file path is invalid!",
                                      "Class loading error",
                                      JOptionPane.ERROR_MESSAGE);
      }
    }
    catch (Throwable e)
    {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this,
                                    "The specified class file does not contain class definition!",
                                    "Class loading error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Initializes 'configure connection' button.
   *
   * @return Returns initialized {@link javax.swing.JButton} component.
   */
  private JButton initializeConfigureNetworkButton()
  {
    configConnectionButton = new JButton("Configure connection");

    configConnectionButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        connectionConfDialog.setVisible(true);
      }
    });

    return configConnectionButton;
  }

  /**
   * Initializes 'connect' button.
   *
   * @return Returns initialized {@link javax.swing.JButton} component.
   */
  private JButton initializeConnectButton()
  {
    connectButton = new JButton("Connect");

    connectButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        receiver.postMessage(new ClientConnectionRequestMessage());
      }
    });

    return connectButton;
  }

  /**
   * Initializes 'disconnect' button.
   *
   * @return Returns initialized {@link javax.swing.JButton} component.
   */
  private JButton initializeDisconnectButton()
  {
    disconnectButton = new JButton("Disconnect");

    disconnectButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        receiver.postMessage(new ClientDisconnectionRequestMessage());
      }
    });

    return disconnectButton;
  }

  /**
   * Initializes bottom panel.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JSplitPane initializeBottomPanel()
  {
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    splitPane.setTopComponent(initializeGamePanel());
    splitPane.setBottomComponent(logsManager.getLogPanel());

    return splitPane;
  }

  /**
   * Initializes game's panel.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeGamePanel()
  {
    gamePanel = new JPanel(null);
    Dimension panelDim = new Dimension(PREFERRED_GAME_PANEL_WIDTH,
                                       PREFERRED_GAME_PANEL_HEIGHT);
    gamePanel.setMinimumSize(panelDim);
    gamePanel.setPreferredSize(panelDim);

    return gamePanel;
  }

  /**
   * Sets initial values for connection configuration dialog, using the
   * specified object with connection configuration.
   *
   * @param clientConfig the specified object with connection's configuration.
   */
  void setConfigConnectionDialogValues(ClientConfig clientConfig)
  {
    connectionConfDialog.setInitialValues(clientConfig);
  }

  /**
   * This method should be invoked if algorithm was loaded from the file.
   */
  void algorithmLoaded()
  {
    SwingUtilities.invokeLater(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {
        loadAlgorithmButton.setEnabled(true);
        configConnectionButton.setEnabled(true);
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
      }
    });
  }

  /**
   * This method should be invoked if connection attempt with server was started.
   */
  void connectionStarted()
  {
    SwingUtilities.invokeLater(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {
        loadAlgorithmButton.setEnabled(false);
        configConnectionButton.setEnabled(false);
        connectButton.setEnabled(false);
        disconnectButton.setEnabled(false);
      }
    });
  }

  /**
   * This method should be invoked if connection to server was failed.
   */
  void connectionFailed()
  {
    algorithmLoaded();
  }

  /**
   * This method should be invoked if connection to server was successful.
   */
  void connected()
  {
    SwingUtilities.invokeLater(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {
        loadAlgorithmButton.setEnabled(false);
        configConnectionButton.setEnabled(false);
        connectButton.setEnabled(false);
        disconnectButton.setEnabled(true);
      }
    });
  }

  /**
   * This method should be invoked if disconnection from server was successful.
   */
  void disconnected()
  {
    SwingUtilities.invokeLater(new Runnable() {
      /**
       * @see java.lang.Runnable#run()
       */
      public void run()
      {
        loadAlgorithmButton.setEnabled(true);
        configConnectionButton.setEnabled(true);
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
      }
    });
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
   * Gets panel where simulation output will be presented.
   *
   * @return Returns panel where simulation output will be presented.
   */
  Container getGamePanel()
  {
    return gamePanel;
  }


  /**
   * Contains tests.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    ClientFrame frame = new ClientFrame(new RunnableReceiver());

    frame.pack();
    frame.setVisible(true);

    frame.addWindowListener(new WindowAdapter() {

      /**
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosed(WindowEvent e)
      {
        System.exit(0);
      }
    });
  }
}
