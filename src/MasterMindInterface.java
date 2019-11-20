// Name: Joshua Daniel
// Date: 11/20/2019

// Imports
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

// JFrame Application
public class MasterMindInterface extends JFrame implements ActionListener {
  // JLabels
  private JLabel lblLogo;

  // JButtons
  private JButton btnStart;
  private JButton btnSubmit;
  private JButton btnQuit;
  private JButton[][] btnGrid;
  private JButton[] btnInfo;

  // JPanels
  private JPanel pnlGrid;
  private JPanel pnlControls;

  // Colors
  private Color backgroundColor;
  private Color defaultBtnColor;

  // Containers
  private Container frame;

  // Other fields
  private int currentRow;
  private MasterMindBackend backend;

  // Constructor
  private MasterMindInterface() {
    // Set up Colors
    backgroundColor = new Color(243, 244, 248);
    defaultBtnColor = Color.LIGHT_GRAY;

    // Set up task bar logo
    ImageIcon applicationImg = new ImageIcon(".\\images\\taskbar-logo.jpg");
    setIconImage(applicationImg.getImage());

    // Set up JFrame
    frame = getContentPane();
    frame.setLayout(new GridBagLayout());
    frame.setBackground(backgroundColor);

    // Set up GridBagConstraints
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.gridwidth = 1;
    gbc.weightx = 100;
    gbc.gridx = 0;


    //// ROW 1 START ////
    // Set up ImageIcon imgIconLogo
    ImageIcon imgIconLogo = new ImageIcon(".\\images\\master-mind.png");
    Image imgLogo = imgIconLogo.getImage().getScaledInstance(300, -1, java.awt.Image.SCALE_SMOOTH);
    imgIconLogo = new ImageIcon(imgLogo);

    // Create JLabel lblLogo with ImageIcon
    lblLogo = new JLabel(imgIconLogo);

    // Add JLabel to JFrame
    gbc.gridy = 0;
    gbc.weighty = 10;
    frame.add(lblLogo, gbc);
    //// ROW 1 END ////


    ++gbc.gridy;


    //// ROW 2 START ////
    // Set up JPanel pnlGrid
    pnlGrid = new JPanel();
    pnlGrid.setLayout(new GridLayout(15, 5, 5, 5));
    pnlGrid.setBackground(backgroundColor);

    // Sets up 15x4 JButton[][] grid
    setUpGameGrid();

    // Add JPanel to JFrame
    gbc.insets = new Insets(0, 60, 0, 60);
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridwidth = 4;
    gbc.weightx = 100;
    gbc.weighty = 80;
    frame.add(pnlGrid, gbc);
    //// ROW 2 END ////


    ++gbc.gridy;


    //// ROW 3 START ////
    // Set up JPanel pnlControls
    pnlControls = new JPanel();
    pnlControls.setLayout(new GridLayout(1, 3, 5, 5));
    pnlControls.setBackground(backgroundColor);

    // Set up JButton btnStart
    btnStart = new JButton("START");
    btnStart.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
    btnStart.addActionListener(this);

    // Set up JButton btnSubmit
    btnSubmit = new JButton("SUBMIT");
    btnSubmit.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
    btnSubmit.addActionListener(this);
    btnSubmit.setEnabled(false);

    // Set up JButton btnQuit
    btnQuit = new JButton("QUIT");
    btnQuit.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
    btnQuit.addActionListener(this);

    // Add JButtons to JPanel pnlControls
    pnlControls.add(btnStart);
    pnlControls.add(btnSubmit);
    pnlControls.add(btnQuit);

    // Add JPanel to JFrame
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(0, 15, 10, 15);
    gbc.weighty = 10;
    frame.add(pnlControls, gbc);
    //// ROW 3 END ////


    // Will be used to set JFrame screen center
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    // Set JFrame properties and display
    setSize(350, 875);
    setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2 - getSize().height / 2);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("MasterMind");
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    // Find current row
    int row = -1;
    for (int i = 0; i < btnInfo.length; ++i) {
      if (e.getSource() == btnInfo[i]) {
        row = i;
        break;
      }
    }

    // Display information about selected row
    if (row != -1 && e.getSource() == btnInfo[row]) {
      // Get values and display
      String[] text = btnInfo[row].getText().split(", ");
      JOptionPane.showMessageDialog(
        null,
        text[0] + " color(s) correct\n" + text[1] + " color(s) in the correct location",
        "Row " + (row + 1),
        JOptionPane.INFORMATION_MESSAGE
      );
    } else if (e.getSource() == btnStart) {
      // Start game
      startGame();
      btnStart.setEnabled(false);
      btnSubmit.setEnabled(true);
    }
    // Submit user input
    else if (e.getSource() == btnSubmit) {
      // Determine if all colors are valid
      for (int i = 0; i < btnGrid[currentRow].length; ++i) {
        // Let user know and exit
        if (btnGrid[currentRow][i].getBackground() == defaultBtnColor) {
          JOptionPane.showMessageDialog(
            null,
            "You must select four (4) colors before hitting \"SUBMIT\"",
            "Error",
            JOptionPane.ERROR_MESSAGE
          );
          return;
        }
      }

      // Disable current row
      for (int i = 0; i < btnGrid[currentRow].length; ++i) {
        btnGrid[currentRow][i].setEnabled(false);
      }

      // Store colors from row
      Color[] colorUser = new Color[btnGrid[currentRow].length];
      for (int i = 0; i < btnGrid[currentRow].length; ++i) {
        colorUser[i] = btnGrid[currentRow][i].getBackground();
      }

      // Store information regarding turn
      int colorCorrect = backend.determineColorsCorrect(colorUser);
      int posCorrect = backend.determineLocationsCorrect(colorUser);

      // Display information regarding turn
      // Enable turn information for current row
      JOptionPane.showMessageDialog(
        null,
        colorCorrect + " color(s) correct\n" + posCorrect + " color(s) in the correct location",
        "Row " + (currentRow + 1),
        JOptionPane.INFORMATION_MESSAGE
      );
      btnInfo[currentRow].setText(colorCorrect + ", " + posCorrect);
      btnInfo[currentRow].setEnabled(true);

      // Player won
      if (posCorrect == btnGrid[currentRow].length) {
        JOptionPane.showMessageDialog(
          null,
          "Congratulations... You cracked the code!\nIt took you " + (currentRow + 1) + " attempt(s)!",
          "You win!",
          JOptionPane.INFORMATION_MESSAGE
        );
        gameEndHandling();
      }

      // Player lost
      if (currentRow == btnGrid.length - 1) {
        JOptionPane.showMessageDialog(
          null,
          "Game Over!\nYou failed to crack the code!\n\nThe code was: " + backend.secretToString(),
          "You lose!",
          JOptionPane.INFORMATION_MESSAGE
        );
        gameEndHandling();
      }

      // Enable next row
      ++currentRow;
      for (int i = 0; i < btnGrid[currentRow].length; ++i) {
        btnGrid[currentRow][i].setEnabled(true);
      }
    } else if (e.getSource() == btnQuit) {
      // Exit application
      System.exit(0);
    }
  }

  private void gameEndHandling() {
    // Ask user whether they want to continue
    int dialogResult = JOptionPane.showConfirmDialog(
      null,
      "Would you like to play again?",
      "Play Again?",
      JOptionPane.YES_NO_OPTION
    );

    // User wants to continue
    if (dialogResult == JOptionPane.YES_OPTION) {
      startGame();
    } else {
      // Exit application
      System.exit(0);
    }
  }

  // Starts game
  private void startGame() {
    // Reset currentRow and backend
    currentRow = 0;
    backend = new MasterMindBackend();

    // Iterate over all JButtons
    for (int i = 0; i < btnGrid.length; ++i) {
      for (int j = 0; j < btnGrid[i].length; ++j) {
        // Set JButton to disabled
        btnGrid[i][j].setEnabled(false);
        btnGrid[i][j].setBackground(defaultBtnColor);
      }

      // Last JButton in row
      btnInfo[i].setText("-, -");
      btnInfo[i].setEnabled(false);
    }

    // Enable first row
    for (int i = 0; i < btnGrid[0].length; ++i) {
      btnGrid[0][i].setEnabled(true);
    }
  }

  // Initializes JButton[][] btnGameGrid
  private void setUpGameGrid() {
    // Create JButton[][] 2D array and JButton[] 1D array
    btnGrid = new JButton[15][4];
    btnInfo = new JButton[15];

    // Iterate over all JButtons
    for (int i = 0; i < btnGrid.length; ++i) {
      for (int j = 0; j < btnGrid[i].length; ++j) {
        // Create JButton and set disabled
        btnGrid[i][j] = new JButton();
        btnGrid[i][j].setEnabled(false);
        btnGrid[i][j].setBackground(defaultBtnColor);

        // Add mouse listener to JButton
        btnGrid[i][j].addMouseListener(new MouseAdapter() {
          // Deals with mouseClicked inputs
          public void mouseClicked(MouseEvent e) {
            // Store the position of the button clicked
            int row = -1;
            int col = -1;

            // Iterate over all JButtons
            for (int i = 0; i < btnGrid.length; ++i) {
              for (int j = 0; j < btnGrid[i].length; ++j) {
                // Store position and break
                if (e.getSource() == btnGrid[i][j]) {
                  row = i;
                  col = j;
                  break;
                }
              }
            }

            // Make sure row and col are valid indices and source is enabled
            if (row != -1 && col != -1 && btnGrid[row][col].isEnabled()) {
              // Holds index of current color
              int currColor = -1;

              // Find matching color and store position
              for (int i = 0; i < backend.colorOptions.length; ++i) {
                if (btnGrid[row][col].getBackground() == backend.colorOptions[i]) {
                  currColor = i;
                  break;
                }
              }

              // Holds index of next color
              int nextColor = -1;

              // Right mouse button: scrolls backward through colors
              // Left mouse button:  scrolls forward through colors
              if (SwingUtilities.isLeftMouseButton(e)) {
                // Starting values
                if (currColor == -1) {
                  nextColor = 0;
                } else {
                  nextColor = (currColor + 1) % backend.colorOptions.length;
                }
              } else if (SwingUtilities.isRightMouseButton(e)) {
                // Starting values
                if (currColor == -1) {
                  nextColor = backend.colorOptions.length - 1;
                } else {
                  nextColor = (currColor + backend.colorOptions.length - 1) % backend.colorOptions.length;
                }
              }

              // Set next color for button
              btnGrid[row][col].setBackground(backend.colorOptions[nextColor]);
            }
          }
        });

        // Add JButton to JButton[][] btnGrid
        pnlGrid.add(btnGrid[i][j]);
      }

      // Last JButton in row
      btnInfo[i] = new JButton("-, -");
      btnInfo[i].setEnabled(false);
      btnInfo[i].addActionListener(this);
      btnInfo[i].setMargin(new Insets(0, 0, 0, 0));
      btnInfo[i].setFont(new Font("SansSerif", Font.BOLD, 10));

      // Add JButton to JButton[][] btnGrid
      pnlGrid.add(btnInfo[i]);
    }
  }

  // Program entry point
  public static void main(String[] args) {
    new MasterMindInterface();
  }
}
