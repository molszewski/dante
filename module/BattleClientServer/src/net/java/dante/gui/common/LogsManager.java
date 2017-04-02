/*
 * Created on 2006-09-09
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.common;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;

/**
 * Class managing logging and displaying logs on frame.
 *
 * @author M.Olszewski
 */
public class LogsManager
{
  /**
   * Value indicating that no buttons should be displayed on logs panel.
   */
  public static int NO_BUTTONS = 0;
  /**
   * Value indicating whether 'scroll lock' button should be displayed
   * on logs panel. It should be passed as second argument of
   * {@link #LogsManager(int, int)} constructor.
   */
  public static int SCROLL_LOCK_BUTTON = 1;
  /**
   * Value indicating whether 'clear log' button should be displayed
   * on logs panel. It should be passed as second argument of
   * {@link #LogsManager(int, int)} constructor.
   */
  public static int CLEAR_LOG_BUTTON = 2;
  /**
   * Value indicating that all buttons should be displayed on logs panel.
   * It should be passed as second argument of {@link #LogsManager(int, int)}
   * constructor.
   */
  public static int ALL_BUTTONS = SCROLL_LOCK_BUTTON | CLEAR_LOG_BUTTON;

  /** Maximum number of log lines. */
  final int maxLogLinesCount;
  /** Logs panel. */
  private JComponent logsPanel = null;
  /** Logs area. */
  JTextArea logsArea = null;
  /** Logs scroll. */
  JScrollPane logsScroll = null;
  /** Value indicating which buttons should be displayed on logs panel. */
  private int buttons;
  /** Indicates whether scroll lock is enabled. */
  boolean scrollLock = false;


  /**
   * Creates instance of {@link LogsManager} class with the specified
   * parameters.
   *
   * @param maxLogLines maximum available log lines.
   */
  public LogsManager(int maxLogLines)
  {
    if (maxLogLines <= 0)
    {
      throw new IllegalArgumentException("Invalid argument maxLogLines - it must be positive integer!");
    }

    maxLogLinesCount = maxLogLines;
  }

  /**
   * Creates instance of {@link LogsManager} class with the specified
   * parameters.
   *
   * @param maxLogLines maximum available log lines.
   * @param requestedButtons flag containing which buttons should be displayed
   *        (clear log, scroll lock).
   */
  public LogsManager(int maxLogLines, int requestedButtons)
  {
    if (maxLogLines <= 0)
    {
      throw new IllegalArgumentException("Invalid argument maxLogLines - it must be positive integer!");
    }

    maxLogLinesCount = maxLogLines;
    buttons          = requestedButtons;
  }


  /**
   * Obtains reference to logs panel.
   * If such logs panel doesn't exist, one will be created.
   *
   * @return Returns reference to logs panel.
   */
  public JComponent getLogPanel()
  {
    if (logsPanel == null)
    {
      boolean showScrollLock = ((buttons & SCROLL_LOCK_BUTTON) == SCROLL_LOCK_BUTTON);
      boolean showClearLog   = ((buttons & CLEAR_LOG_BUTTON) == CLEAR_LOG_BUTTON);
      if (showScrollLock || showClearLog)
      {
        JPanel logsPane = new JPanel();

        logsPane.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 5, 0, 5);
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;

        logsPane.add(getLogsScroll(), constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridx = 0;
        constraints.gridy = 1;

        logsPane.add(createButtonsPanel(buttons), constraints);

        logsPanel = logsPane;
      }
      else
      {
        logsPanel = getLogsScroll();
      }
    }

    return logsPanel;
  }

  /**
   * Gets scroll panel around logs area. If such panel doesn't exist, one will be
   * created.
   *
   * @return Returns scroll panel around logs area.
   */
  JScrollPane getLogsScroll()
  {
    if (logsScroll == null)
    {
      logsScroll = new JScrollPane(getLogsArea(),
                                   ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    return logsScroll;
  }

  /**
   * Gets reference to text area containing logs.
   * If such text area doesn't exist, one will be created.
   *
   * @return Returns text area containing logs.
   */
  JTextArea getLogsArea()
  {
    if (logsArea == null)
    {
      logsArea = new JTextArea("Logging started...\n");
      logsArea.setEditable(false);
      logsArea.setFont(Utils.createFixedWidthFont(12));
    }

    return logsArea;
  }

  /**
   * Creates panel with requested buttons.
   *
   * @param requestedButtons flag containing requested buttons.
   *
   * @return Returns created panel with requested buttons.
   */
  private JPanel createButtonsPanel(int requestedButtons)
  {
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    boolean showScrollLock = ((buttons & SCROLL_LOCK_BUTTON) == SCROLL_LOCK_BUTTON);
    boolean showClearLog   = ((buttons & CLEAR_LOG_BUTTON) == CLEAR_LOG_BUTTON);

    if (showScrollLock)
    {
      final JCheckBox scrollLockButton = new JCheckBox("Scroll Lock", false);
      scrollLockButton.addActionListener(new ActionListener() {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
          scrollLock = scrollLockButton.isSelected();
        }
      });

      buttonsPanel.add(scrollLockButton);
    }

    if (showClearLog)
    {
      JButton clearLog = new JButton("Clear log");
      clearLog.addActionListener(new ActionListener() {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e)
        {
          logsArea.setText("");
        }
      });
      buttonsPanel.add(clearLog);
    }

    return buttonsPanel;
  }


  /**
   * Appends the specified log line to this log manager. If more than one
   * new line characters is found, line is split into appropriate number
   * of new lines and added one by one.
   *
   * @param logLine the specified log line.
   */
  public void appendLogLine(String logLine)
  {
    if (logLine != null)
    {
      final String[] logLines = logLine.split("\\n");

      SwingUtilities.invokeLater(new Runnable() {
        /**
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
          JTextArea log = getLogsArea();

          for (int i = 0; i < logLines.length; i++)
          {
            if ((log.getLineCount() - 1) >= maxLogLinesCount)
            {
              try
              {
                int endOffset = log.getLineEndOffset(0);
                log.replaceRange("", 0, endOffset);
              }
              catch (BadLocationException e)
              {
                if (maxLogLinesCount > 1)
                {
                  log.setText("BadLocationException caught - log was cleared!");
                }
              }
            }

            log.append(logLines[i]);
            log.append("\n");
          }

          if (!scrollLock)
          {
            SwingUtilities.invokeLater(new Runnable() {
              /**
               * @see java.lang.Runnable#run()
               */
              public void run()
              {
                JScrollBar scroll = getLogsScroll().getVerticalScrollBar();
                scroll.setValue(scroll.getMaximum());
              }
            });
          }
          else
          {
            final int currentScrollPosition = getLogsScroll().getVerticalScrollBar().getValue();
            SwingUtilities.invokeLater(new Runnable() {
              /**
               * @see java.lang.Runnable#run()
               */
              public void run()
              {
                JScrollBar scroll = getLogsScroll().getVerticalScrollBar();
                scroll.setValue(currentScrollPosition);
              }
            });
          }
        }
      });
    }
  }

  /**
   * Tests method.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    JFrame frame = new JFrame();
    frame.addWindowListener(new WindowAdapter() {
      /**
       * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });

    Container cnt = frame.getContentPane();
    cnt.setLayout(new BoxLayout(cnt, BoxLayout.Y_AXIS));

    final LogsManager lm = new LogsManager(20, ALL_BUTTONS);

    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton addLogButton = new JButton("Add log line");
    addLogButton.addActionListener(new ActionListener() {
      int counter = 0;

      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        lm.appendLogLine("Log line no." + counter);
        counter++;
      }
    });

    JButton addTripleLogLinesButton = new JButton("Add 3 log lines");
    addTripleLogLinesButton.addActionListener(new ActionListener() {
      int counter = 0;

      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        lm.appendLogLine("Log line no." + (++counter) + "[1/3]\nLog line no." + (++counter) + "[2/3]\nLog line no." + (++counter) + "[3/3]");
      }

    });

    buttonsPanel.add(addLogButton);
    buttonsPanel.add(addTripleLogLinesButton);

    cnt.add(buttonsPanel);
    cnt.add(lm.getLogPanel());

    frame.pack();
    frame.setVisible(true);
  }
}