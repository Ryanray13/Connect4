package pqs.ps4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import pqs.ps4.ConnectFourModel.PlayMode;
import pqs.ps4.ConnectFourModel.Role;

public class ConnectFourView implements ConnectFourListener{
  
  private ConnectFourModel model; 
  private int playTurn;
  private int firstTurn;
  private JButton button[][];
  private JButton upSideButton[];
  private JFrame frame;
  private JTextArea textArea = new JTextArea();
  private final int HEIGHT ;
  private final int WIDTH ;
  private final  int NUMBER_OF_PLAYER ;
  
  public ConnectFourView(ConnectFourModel model){
    this.model = model;
    HEIGHT = model.getHeight();
    WIDTH = model.getWidth();
    NUMBER_OF_PLAYER = model.getNumberofPlayer();
    button = new JButton[HEIGHT][WIDTH];
    upSideButton = new JButton[3];
    newButton();
    newUpSideButton();
    addButtonActionListener();
    addUpSideButtonActionListener();
    
    frame = new JFrame("ConnectFour");
    JPanel mainPanel = new JPanel();
    JPanel upSidePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    
    buttonPanel.setLayout(new GridLayout(HEIGHT,WIDTH));
    addButtonToPanel(buttonPanel);
    upSidePanel.setLayout(new GridLayout(1,3));
    upSidePanel.setPreferredSize(new Dimension(100,50));
    addupSideButtonToPanel(upSidePanel);
    
    mainPanel.setLayout(new BorderLayout());
    textArea.setPreferredSize(new Dimension(100,50));
    textArea.setFont(new Font("Arial", Font.PLAIN, 40));
    mainPanel.add(textArea, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);
    
    frame.setLayout(new BorderLayout());
    frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    frame.getContentPane().add(upSidePanel, BorderLayout.NORTH);
    frame.setSize(800, 700);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        removeFromModel();
      }
    });
    
    frame.setVisible(true);
    model.addConnectFourListener(this);
    model.paintView();
    textArea.setText("");
  }

  /* Remove this view from model */
  private void removeFromModel() {
    model.removeConnectFourListener(this);
  }
  
  /* Create buttons */
  private void newButton(){
    for (int row = 0; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        button[row][col] = new JButton();
        button[row][col].setOpaque(true);
        button[row][col].setBackground(Color.white);
        try {
          UIManager.setLookAndFeel(UIManager
              .getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } 
  }

  /* Create upside buttons */
  private void newUpSideButton() {
    for (int row = 0; row < 3; row++){
      upSideButton[row] = new JButton();
    } 
    upSideButton[0].setText("Player Vs Player");
    upSideButton[1].setText("Player First(Vs Robot)");
    upSideButton[2].setText("Robot First(Vs Player)");
  }

  /* For each button add a different action listener. */
  private void addButtonActionListener(){
    class BtnActionListener implements ActionListener{
      private final int col;
      BtnActionListener(int col){
        this.col = col;
      }
      public void actionPerformed(ActionEvent e) {
        buttonPressed(col);
      }
      private void buttonPressed(int col) {
        model.makeMove(col, playTurn, Role.PLAYER);
      }
    }
    
    for (int row = 0; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        button[row][col].addActionListener(new BtnActionListener(col));
      }
    }
  }

  /*  For each side button add a different action listener. */
  private void addUpSideButtonActionListener(){
    upSideButton[0].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        model.startGame(PlayMode.PLAYERS);
      }     
    });
    upSideButton[1].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        model.startGame(PlayMode.PLAYERFIRST);
      }     
    });
    upSideButton[2].addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
        model.startGame(PlayMode.ROBOTFIRST);
      }     
    });
  }
  
  /* Add buttons to the button panel */
  void addButtonToPanel(JPanel panel){
    for (int row = HEIGHT - 1; row >= 0; row--){
      for(int col = 0; col < WIDTH; col++){
        panel.add(button[row][col]);
      }
    }
  }
  
  /* Add side buttons to side panel */
  private void addupSideButtonToPanel(JPanel panel) {
    for (int row = 0; row < 3; row++){
      panel.add(upSideButton[row]);
    }
  }
  
  /* Clear the view frame. */
  private void clearView(){
    for (int row = 0; row < HEIGHT; row++){
      for(int col = 0; col < WIDTH; col++){
        button[row][col].setBackground(Color.white);
      }
    }
    model.paintView();
    textArea.setText("");
  }
  
  /**
   * Implement listener makeMove method, to make a move in view
   */
  public void makeMove(int row, int col, int turn) {
    button[row][col].setBackground(turn == 0 ? Color.RED : Color.BLACK);
    playTurn = (playTurn + 1)% NUMBER_OF_PLAYER;
    textArea.setText(getRoleString(playTurn) + "'s Turn.");
  }

  /**
   * Implement listener gameWin method, to show win situation
   */
  public void gameWin(int turn) {   
    textArea.setText( getRoleString(turn) + " Win !");
    JOptionPane.showMessageDialog(frame, getRoleString(turn) + " Win !");
  }

  /**
   * Implement listener gameEven method, to show even situation
   */
  public void gameEven() {
    textArea.setText("Game Tied!");
    JOptionPane.showMessageDialog(frame, "Game Tied!");
  }

  /**
   * Implement listener startGame method, to start the game in view
   */
  public void startGame(int turn) {
    clearView();
    playTurn = turn;
    firstTurn = turn;
    textArea.setText("Game Stated!  " + getRoleString(turn) + "'s Turn.");
  }
  
  /* Based on turn and whether play with robot display different string. */
  private String getRoleString(int turn){
    if(model.getPlayMode() != PlayMode.PLAYERS){
      if(model.getPlayMode() == PlayMode.PLAYERFIRST){
        return turn == firstTurn ? "Player" : "Robot";
      }
      else{
        return turn == firstTurn ? "Robot" : "Player";
      }
    }
    else{
      return turn == firstTurn ? "Player1" : "Player2";
    }
  }
}
