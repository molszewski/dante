/*
 * Created on 2006-09-08
 *
 * @author M.Olszewski
 */

package net.java.dante.gui.client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import net.java.dante.darknet.config.ClientConfig;
import net.java.dante.darknet.protocol.SupportedProtocol;
import net.java.dante.gui.common.messages.receiver.ConnectionConfigurationChangedMessage;
import net.java.dante.receiver.Receiver;
import net.java.dante.receiver.RunnableReceiver;


/**
 * Dialog representing connection configuration.
 *
 * @author M.Olszewski
 */
class ConnectionConfigDialog extends JDialog
{
  private static final long serialVersionUID = 1L;
  
  /** Preferred dialog's width. */
  private static final int PREFERRED_DIALOG_WIDTH  = 340;
  /** Preferred dialog's height. */
  private static final int PREFERRED_DIALOG_HEIGHT = 250;

  /** Port spinner default value. */
  private static final int DEFAULT_PORT_VALUE = 5335;
  /** Port spinner minimum value. */
  private static final int MIN_PORT_VALUE = 1;
  /** Port spinner maximum value. */
  private static final int MAX_PORT_VALUE = 65535;

  /** Time out spinner default value. */
  private static final int DEFAULT_TIME_OUT_VALUE = 10;
  /** Time out spinner minimum value. */
  private static final int MINIMUM_TIME_OUT_VALUE = 1;
  /** Time out spinner maximum value. */
  private static final int MAXIMUM_TIME_OUT_VALUE = 600;


  /** Messages receiver. */
  private Receiver receiver;

  /** Text field containing server's IP address. */
  private JTextField hostAddressField = null;
  /** Spinner field containing server's port. */
  private JSpinner portSpinner = null;
  /** Spinner field containing connection's time out. */
  private JSpinner timeOutSpinner = null;
  /** Buttons group containing selected behaviour. */
  private ButtonGroup behaviourGroup = new ButtonGroup();
  /** Button's model of creator button. */
  private ButtonModel creatorModel = null;

  private String hostName;
  private int portNumber;
  private int timeOut;
  private ButtonModel selectedModel;


  /**
   * Creates instance of {@link ConnectionConfigDialog} class with
   * the specified arguments. Created instance represents not initialized
   * modal dialog. Call {@link #initialize()} method to initialize this dialog.
   *
   * @param owner dialog's owner (frame). Cannot be <code>null</code>.
   * @param messagesReceiver messages receiver.
   */
  ConnectionConfigDialog(JFrame owner, Receiver messagesReceiver)
  {
    super(owner, "Connection Configuration", true);

    if (owner == null)
    {
      throw new NullPointerException("Specified owner is null!");
    }
    if (messagesReceiver == null)
    {
      throw new NullPointerException("Specified messagesReceiver is null!");
    }

    receiver = messagesReceiver;
  }


  /**
   * This method initializes connection configuration dialog.
   */
  void initialize()
  {
    setContentPane(initializeContentPane());
    setResizable(false);
    pack();

    saveSettings();
  }

  /**
   * Initializes content pane for this dialog.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeContentPane()
  {
    JPanel jContentPane = new JPanel();
    jContentPane.setPreferredSize(new Dimension(PREFERRED_DIALOG_WIDTH, PREFERRED_DIALOG_HEIGHT));
    jContentPane.setLayout(new BoxLayout(jContentPane, BoxLayout.Y_AXIS));
    jContentPane.add(initializeHostAddressTextPanel());
    jContentPane.add(initializePortSpinnerArea());
    jContentPane.add(initializeTimeOutSpinnerArea());
    jContentPane.add(initializeConnectionBehaviourArea());
    jContentPane.add(initializeButtonsArea());

    return jContentPane;
  }

  /**
   * Initializes panel containing server's address text field.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeHostAddressTextPanel()
  {
    JPanel panel = new JPanel(new GridBagLayout());

    GridBagConstraints textFieldConstraints = new GridBagConstraints();
    textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    textFieldConstraints.insets = new Insets(0, 5, 0, 5);
    textFieldConstraints.weightx = 1.0;

    hostAddressField = new JTextField("localhost");

    JLabel jLabel = new JLabel("Server's Address: ");
    jLabel.setLabelFor(hostAddressField);

    panel.add(jLabel, createDefaultLabelConstraints());
    panel.add(hostAddressField, createDefaultEditFieldConstraints());

    return panel;
  }

  /**
   * Initializes panel containing server's port spinner.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializePortSpinnerArea()
  {
    // Initialize spinner
    SpinnerModel model = new SpinnerNumberModel(DEFAULT_PORT_VALUE,
                                                MIN_PORT_VALUE,
                                                MAX_PORT_VALUE,
                                                1);
    portSpinner = new JSpinner(model);
    portSpinner.setEditor(new JSpinner.NumberEditor(portSpinner, "#"));

    // Initialize label for spinner
    JLabel jLabel = new JLabel("Server's Port: ");
    jLabel.setLabelFor(portSpinner);

    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(jLabel, createDefaultLabelConstraints());
    panel.add(portSpinner, createDefaultEditFieldConstraints());

    return panel;
  }

  /**
   * Initializes panel containing connection time out spinner.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeTimeOutSpinnerArea()
  {
    SpinnerModel model =
      new SpinnerNumberModel(DEFAULT_TIME_OUT_VALUE,
                             MINIMUM_TIME_OUT_VALUE,
                             MAXIMUM_TIME_OUT_VALUE,
                             1);
    timeOutSpinner = new JSpinner(model);
    timeOutSpinner.setEditor(new JSpinner.NumberEditor(timeOutSpinner, "#"));

    JLabel jLabel = new JLabel("Connection Time Out: ");
    jLabel.setLabelFor(timeOutSpinner);

    JLabel jSecondsLabel = new JLabel("seconds");

    JPanel panel = new JPanel(new GridBagLayout());
    panel.add(jLabel, createDefaultLabelConstraints());
    panel.add(timeOutSpinner, createDefaultEditFieldConstraints());
    panel.add(jSecondsLabel, createDefaultLabelConstraints());

    return panel;
  }

  /**
   * Initializes panel containing connection behaviour radio buttons.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeConnectionBehaviourArea()
  {
    JRadioButton creatorButton = new JRadioButton("Creating games", true);
    creatorModel = creatorButton.getModel();

    JRadioButton joinerButton = new JRadioButton("Joining games", false);

    behaviourGroup.add(creatorButton);
    behaviourGroup.add(joinerButton);

    JPanel groupPanel = new JPanel(null);
    groupPanel.setBorder(BorderFactory.createTitledBorder(" Select client behaviour: "));
    groupPanel.setPreferredSize(new Dimension(345, 80));

    groupPanel.add(creatorButton);
    Dimension creatorSize = creatorButton.getPreferredSize();
    creatorButton.setBounds(10, 20, creatorSize.width, creatorSize.height);

    groupPanel.add(joinerButton);
    Dimension joinerSize = creatorButton.getPreferredSize();
    joinerButton.setBounds(10, creatorSize.height + 20, joinerSize.width, joinerSize.height);

    return groupPanel;
  }

  /**
   * Initializes panel containing 'OK' and 'Cancel' buttons.
   *
   * @return Returns initialized {@link javax.swing.JPanel} component.
   */
  private JPanel initializeButtonsArea()
  {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        sendConfigurationParameters();
        setVisible(false);
      }
    });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      /**
       * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
       */
      public void actionPerformed(ActionEvent e)
      {
        // Simply hide dialog
        setVisible(false);
      }
    });

    panel.add(okButton);
    panel.add(cancelButton);

    return panel;
  }

  /**
   * @see java.awt.Component#setVisible(boolean)
   */
  @Override
  public void setVisible(boolean b)
  {
    if (b)
    {
      loadSettings();
    }

    super.setVisible(b);
  }


  /**
   * Sends parameters as receiver's messages if all are valid.
   */
  void sendConfigurationParameters()
  {
    InetAddress inetAddress = validateInternetAddress(hostAddressField.getText());
    if (inetAddress != null)
    {
      boolean creatorBehaviour = (behaviourGroup.getSelection() == creatorModel);
      int port = Integer.parseInt(portSpinner.getValue().toString());
      int maxTimeOut = Integer.parseInt(timeOutSpinner.getValue().toString());

      ClientConfig config = ClientConfig.customConfig(SupportedProtocol.TCP,
                                                      inetAddress,
                                                      port,
                                                      maxTimeOut);
      receiver.postMessage(new ConnectionConfigurationChangedMessage(config,
                                                                     creatorBehaviour));
      saveSettings();
    }
    else
    {
      JOptionPane.showMessageDialog(this,
                                    "Non-existing network address was specified!",
                                    "Configuration error",
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Checks the specified text containing some Internet address and returns
   * obtained Internet address if it was valid Internet address.
   *
   * @param inetAddressText the specified text with some Internet address.
   *
   * @return Returns {@link InetAddress} object representing Internet address
   *         or <code>null</code> if the specified text was not containing
   *         correct and existing Internet address.
   */
  private InetAddress validateInternetAddress(String inetAddressText)
  {
    InetAddress inetAddress = null;

    if ((inetAddressText != null) && (inetAddressText.length() > 0))
    {
      try
      {
        // Simply get 'InetAddress' by the specified name.
        inetAddress = InetAddress.getByName(inetAddressText);
      }
      catch (UnknownHostException e)
      {
        // Intentionally left empty.
      }
    }

    return inetAddress;
  }

  /**
   * Creates default constraints for label.
   *
   * @return Returns new object representing default constraints for label.
   */
  private static GridBagConstraints createDefaultLabelConstraints()
  {
    GridBagConstraints labelConstraints = new GridBagConstraints();
    labelConstraints.fill = GridBagConstraints.HORIZONTAL;
    labelConstraints.insets = new Insets(0, 5, 0, 5);


    return labelConstraints;
  }

  /**
   * Creates default constraints for edition field (e.g. server's address).
   *
   * @return Returns new object representing default constraints for edition
   *         field.
   */
  private static GridBagConstraints createDefaultEditFieldConstraints()
  {
    GridBagConstraints editFieldConstraints = new GridBagConstraints();
    editFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
    editFieldConstraints.insets = new Insets(0, 5, 0, 5);
    editFieldConstraints.weightx = 1.0;

    return editFieldConstraints;
  }

  /**
   * Sets initial values of this dialog using the specified object with
   * connection configuration.
   *
   * @param clientConfig the specified object with connection's configuration.
   */
  void setInitialValues(ClientConfig clientConfig)
  {
    hostAddressField.setText(clientConfig.getHostAddress().getHostAddress());
    portSpinner.setValue(Integer.valueOf(clientConfig.getPorts()[0]));
    timeOutSpinner.setValue(Integer.valueOf(clientConfig.getMaxTimeout()));

    saveSettings();
  }

  void saveSettings()
  {
    hostName      = hostAddressField.getText();
    portNumber    = ((Integer)portSpinner.getValue()).intValue();
    timeOut       = ((Integer)timeOutSpinner.getValue()).intValue();
    selectedModel = behaviourGroup.getSelection();
  }

  void loadSettings()
  {
    hostAddressField.setText(hostName);
    portSpinner.setValue(Integer.valueOf(portNumber));
    timeOutSpinner.setValue(Integer.valueOf(timeOut));
    behaviourGroup.setSelected(selectedModel, true);
  }

  /**
   * Test method.
   *
   * @param args - not used.
   */
  public static void main(String[] args)
  {
    ConnectionConfigDialog configDialog = new ConnectionConfigDialog(new JFrame(), new RunnableReceiver());
    configDialog.initialize();

    configDialog.addWindowListener(new WindowAdapter() {

      /**
       * @see java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent)
       */
      @Override
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }

    });

    configDialog.pack();
    configDialog.setVisible(true);
  }
}