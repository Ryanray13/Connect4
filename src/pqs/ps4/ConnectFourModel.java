package pqs.ps4;

import java.util.ArrayList;
import java.util.List;

public class ConnectFourModel {
  
  private static final ConnectFourModel INSTANCE = new ConnectFourModel();
  
  /* set the width and height of the board */
  private int WIDTH;
  private int HEIGHT;
  
  /* 
   * With this data member, the game could be extended to connect Five 
   * Value is the pieces need to be in a row to win. 
   */
  private int CONNECT_COUNT;
  
  /*
   * This is just for abstraction
   * playTurn = (playTurn + 1) % NUMBER_OF_PLAYER
   * In this way we can also get total moves of the game.
   */
  private final int NUMBER_OF_PLAYER = 2;
  
  /* 
   * Store the start turn, since we have customer board
   * start turn might not be 0. Start turn is from config
   * Same situation with total move.
   */
  private int startTurn = 0;
  private int playTurn = 0;
  private int startTotalMove = 0;
  private int totalMove = 0;
  private boolean isGameStarted = false;
  private PlayMode playMode;
  private ConnectFourRobot robot;
  
  private List<ConnectFourListener> listeners = 
      new ArrayList<ConnectFourListener>();
  
  public static enum Role { ROBOT, PLAYER };
  public static enum PlayMode {PLAYERS, PLAYERFIRST, ROBOTFIRST};
  
  /* 
   * A two-dimensional array to present the board. The left bottom corner is
   * board[0][0], right upper corner is board[HEIGHT-1][WIDTH-1]. Original
   * board is the initial board, since we may have a config file and a 
   * customer board and it will be stateless. Each time restart the game, 
   * set the board to original board, while the board is the real board for
   * playing whose state keeps changing. original board is from config.
   */
  private int board[][];
  private int originalBoard[][];
  
  private ConnectFourModel(){
    WIDTH = 7;
    HEIGHT = 6;
    CONNECT_COUNT = 4;
    originalBoard = new int[HEIGHT][WIDTH];
    board = new int[HEIGHT][WIDTH];
    setBoard();
  }
  
  /* Give the board default value which is -1 */
  private void setBoard() {
    for(int i = 0; i < HEIGHT; i ++){
      for(int j = 0; j < WIDTH; j++){
        originalBoard[i][j] = -1;
        board[i][j] = -1;
      }
    }
  }

  /**
   * Public static factory to return a ConnectFourModel
   * @return Singleton instance of ConnectFourModel
   */
  public static ConnectFourModel getInstance(){
    return INSTANCE;
  }
  
  /**
   * Public static factory to return a ConnectFourModel with Customer config.
   * @return Singleton instance of ConnectFourModel
   */
  public static ConnectFourModel getCustomerInstance(
      ConnectFourConfig config){
    INSTANCE.WIDTH = config.getWidth();
    INSTANCE.HEIGHT = config.getHeight();
    INSTANCE.CONNECT_COUNT = config.getConnectCount();
    INSTANCE.startTurn = config.getPlayTurn();
    INSTANCE.startTotalMove = config.getTotalMove();
    INSTANCE.originalBoard = config.getBoard();
    INSTANCE.board = new int
        [INSTANCE.originalBoard.length][INSTANCE.originalBoard[0].length];
    return INSTANCE;
  }

  /**
   * Get the width of the board.
   * @return WIDTH
   */
  public int getWidth(){
    return WIDTH;
  }
  
  /**
   * Get the height of the board.
   * @return HEIGHT
   */
  public int getHeight(){
    return HEIGHT;
  }
  
  /**
   * Get the number of players
   * @return NUMBER_OF_PLAER
   */
  public int getNumberofPlayer(){
    return NUMBER_OF_PLAYER;
  }
  
  /**
   * Get the total move of the game.
   * @return totalMove
   */
  public int getTotalMove(){
    return totalMove;
  }
  
  /**
   * Get the play mode of the model
   * @return playMode
   */
  public PlayMode getPlayMode(){
    return playMode;
  }
  
  public boolean makeMove(int col, int turn, Role role){
    int row = getAvailableRow(col);
    if (isGameStarted && turn == playTurn && validateCoordinate(row,col) 
        && validateMove(row,col)){
      totalMove++;
      playTurn = (playTurn + 1) % NUMBER_OF_PLAYER; 
      board[row][col] = turn;    
      fireMakeMoveEvent(row, col, turn);
      if(checkForWin(row,col,turn)){
        isGameStarted = false;
        fireGameWinEvent(turn);
        return true;
      }
      if(totalMove == HEIGHT * WIDTH){
        isGameStarted = false;
        fireGameEvenEvent();
        return true;
      }
      if(playMode != PlayMode.PLAYERS && role == Role.PLAYER){
        robot.getMove(playTurn);
      }
      return true;
    }
    else{
      return false;
    }
  }
  
  /* validate whether a player can makes this move */
  private boolean validateMove(int row, int col){
    return (row == 0 || board[row-1][col] != -1) && board[row][col] == -1;
  }  
  
  /* 
   * This method is to validate the coordinate, row should be greater or 
   * equal than 0 and less than HEIGHT, col should be greater or equal than
   * 0 and less  than WIDTH. This is just being defensive.
   */
  private boolean validateCoordinate(int row, int col){
    return (0 <= row && row < HEIGHT) && (0 <= col && col < WIDTH);
  }
  
  private void fireGameEvenEvent(){
    isGameStarted = false;
    for(ConnectFourListener listener : listeners){ 
      listener.gameEven();
    }
  }

  private void fireGameWinEvent(int turn) {
    isGameStarted = false;
    for(ConnectFourListener listener : listeners){ 
      listener.gameWin(turn);
    }
  }

  /**
   * Given the row, col and turn check whether we have a win situation.
   * Notice that in row, col the piece do not need to be droped, 
   * which means this can be used as peeking win situation.
   * @param row
   * @param col
   * @param turn
   * @return True if we have a win situation.
   */
  public boolean checkForWin(int row, int col, int turn) {
    if(!validateCoordinate(row, col)){
      return false;
    }
    if(row != 0 && board[row - 1][col] == -1){
      return false;
    }
    /* Store the originalValue, then give $turn to this piece.
       This is for test potential win without making the move. */
    int originalValue = board[row][col];
    board[row][col] = turn;
    int maxRow = row + CONNECT_COUNT - 1 < HEIGHT ? row 
        + CONNECT_COUNT - 1 : HEIGHT - 1;
    int minX = row - CONNECT_COUNT + 1 >= 0 ? row - CONNECT_COUNT + 1 : 0;
    int maxCol = col + CONNECT_COUNT - 1 < WIDTH ? col 
        + CONNECT_COUNT - 1 : WIDTH - 1;
    int minY = col - CONNECT_COUNT + 1 >= 0 ? col - CONNECT_COUNT + 1 : 0;  
    int leftOffset = maxRow - row > col - minY ? col - minY : maxRow - row;
    int rightOffset = row - minX > maxCol - col ? maxCol - col : row - minX;
    
    int rightDiagLeftOffset = row - minX > col - minY 
        ? col - minY : row - minX;  
    int rightDiagRightOffset = maxRow - row > maxCol - col 
        ? maxCol - col : maxRow - row; 
    boolean result = checkRow(row, minY, maxCol, turn) 
        || checkColumn(row, col, turn) 
        || checkLeftDiagonal(row, col, leftOffset, rightOffset, turn) 
        || checkRightDiagonal(row, col, 
            rightDiagLeftOffset,rightDiagRightOffset, turn);
    board[row][col] = originalValue;
    return result;
  }
  
  /* Check whether a row has four connected pieces */
  private boolean checkRow(int row, int startPosition, 
      int endPosition, int turn){
    int count = 0;
    for(int i = startPosition; i <= endPosition; i++){      
      if (board[row][i] == turn){
        count++;
      }
      else{
        count = 0;
      }
      
      if (count == CONNECT_COUNT){
        return true;
      }
    }
    return false;
  }
  
  /* Check whether a column has four connected pieces  */
  private boolean checkColumn(int row, int col, int turn){
    int count = 0;    
    /* If row-CONNECT_COUNT less than 0, 
       we don't have four pieces in this column */
    if(row - CONNECT_COUNT + 1 >= 0){
      for(int i = row - CONNECT_COUNT + 1; i <= row; i++){
        if(board[i][col] == turn){
          count++;
        }
        else{
          count = 0;
        }
      }
      if(count == CONNECT_COUNT){
        return true;
      }
    }
    return false;
  }

  /* Check form Northwest to Southeast */
  private boolean checkLeftDiagonal(int row, int col, 
      int leftOffset, int rightOffset, int turn){ 
    int count = 0;
    for(int i = -leftOffset; i <= rightOffset; i++){
      if(board[row-i][col+i] == turn){
        count++;
      }
      else{
        count = 0;
      }
      if(count == CONNECT_COUNT){
        return true;
      }
    }
    return false;
  }
  
  /* Check form Southwest to Northeast */
  private boolean checkRightDiagonal(int row, int col,
      int leftOffset, int rightOffset, int turn){
    int count = 0;
    for(int i = -leftOffset; i <= rightOffset; i++){
      if(board[row+i][col+i] == turn){
        count++;
      }
      else{
        count = 0;
      }
      if(count == CONNECT_COUNT ){
        return true;
      }
    }
    return false;
  }

  private void fireMakeMoveEvent(int row, int col, int turn) {
    for(ConnectFourListener listener : listeners){ 
      listener.makeMove(row, col, turn);
    }   
  }
  
  public void startGame(PlayMode mode){
    playMode = mode;
    board = copyBoard(originalBoard);
    totalMove = startTotalMove;
    isGameStarted = true;
    playTurn = startTurn;
    if(playMode != PlayMode.PLAYERS){
      robot = new ConnectFourRobot(this);
    }
    fireStartGameEvent(playTurn);
    if(playMode == PlayMode.ROBOTFIRST){
      robot.getMove(playTurn);
    }
  }
  
  /* clone originalBoard to board */
  private int[][] copyBoard(int[][] originalBoard){
    if(originalBoard == null){
      return null;
    }
    int height = originalBoard.length;
    int width = originalBoard[0].length;
    for(int i = 0; i < height; i++){
      for(int j = 0; j < width; j++){
        board[i][j] = originalBoard[i][j];
      }
    }
    return board;
  }
  
  /* Since we may have customer board with pieces on it. We need to
   * pain the view before it starts
   */
  public void paintView(){
    for(int i = 0; i < HEIGHT; i++){
      for(int j = 0; j < WIDTH; j++){
        if(originalBoard[i][j] != -1){
          fireMakeMoveEvent(i, j, originalBoard[i][j]);
        }
      }
    }
  }
  
  private void fireStartGameEvent(int turn) {
    for(ConnectFourListener listener : listeners){ 
      listener.startGame(turn);
    } 
  }
  
  /**
   * Get available Columns for the robot. 
   * @return List consist of available columns number.
   */
  public List<Integer> getAvailableColumn(){
    List<Integer> availableColumn = new ArrayList<Integer>();
    for (int i = 0; i < WIDTH; i++){
      if(getAvailableRow(i) != -1){
        availableColumn.add(i);
      }
    }
    return availableColumn;
  }
  
  /**
   * Given a column, find available row number, if the column is full
   * return -1.
   * @param column number
   * @return row number
   */
  public int getAvailableRow(int col){
    for (int i = 0; i < HEIGHT; i++){
      if(board[i][col] == -1){
        return i;
      }
    }
    return -1;
  }
  
  /**
   * Add connect four listener to the model
   * @param listener
   */
  public void addConnectFourListener(ConnectFourListener listener){
    listeners.add(listener);
  }
  
  /**
   * Remove listener from the model
   * @param listener
   */
  public void removeConnectFourListener(ConnectFourListener listener){
    listeners.remove(listener);
    if(listeners.size() == 0){
      System.exit(0);
    }
  }
  
  /**
   * This method is used to print out the board in console
   * Mainly for unit test.
   */
  public void printBoard(){
    for(int i = HEIGHT - 1; i >= 0; i--){
      for(int j = 0; j < WIDTH; j++){
        if(board[i][j] == 0){
          System.out.print(" O ");
        }
        else if(board[i][j] == 1){
          System.out.print(" X ");
        }
        else{
          System.out.print(" * ");
        }
      }
      System.out.println("");
    }
  }
}
