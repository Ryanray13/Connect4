package pqs.ps4;

public class ConnectFourConfig {

  private int WIDTH;
  private int HEIGHT;
  private int CONNECT_COUNT;
  
  /* Given the customer board, calculate the play turn and total move */
  private int playTurn;
  private int totalMove;
  
  /* 
   * Customer board with some pieces already on the board.
   * Customer board is mainly for testing, but it could still be used
   * on none testing environment.
   */
  private int[][] board;
  
  public static class Builder{
    private int width = 7;
    private int height = 6;
    private int connectCount = 4;
    private int[][] board;
    
    /**
     * Set the board width
     * @param width
     * @return Builder
     */
    public Builder width(int width){
      if(width > 0){
        this.width = width;
      }
      return this;
    }
    
    /**
     * Set the board height
     * @param height
     * @return Builder
     */
    public Builder height(int height){
      if(height > 0){
        this.height = height;
      }
      return this;
    }
    
    /**
     * Set how many pieces(pieces - 1) need to be in a row to win
     * @param count
     * @return Builder
     */
    public Builder connectCount(int count){
      if(count > 0){
        connectCount = count;
      }
      return this;
    }
    
    /**
     * Set the customer board 
     * @param board
     * @return Builder
     */
    public Builder board(int[][] board){
      this.board = board;
      return this;
    }
    
    /**Build config using this builder */
    public ConnectFourConfig build(){
      return new ConnectFourConfig(this);
    } 
  }
  
  /**
   * Public constructor for config using the builder
   * @param config builder
   */
  public ConnectFourConfig(Builder builder){
    WIDTH = builder.width;
    HEIGHT = builder.height;
    CONNECT_COUNT = builder.connectCount;
    if(checkBoard(builder.board)){
      board = builder.board;
    }
    else{
      // If the board is invalid, make a default empty board
      board = new int[HEIGHT][WIDTH];
      setBoard();
      playTurn = 0;
      totalMove = 0;
    }
  }
  
  /* Validate the customer board. true means invalid */ 
  private boolean checkBoard(int[][] board) {
    if(board == null || board.length != HEIGHT || board[0].length != WIDTH){
      return false;
    }
    else{
      int turnOneCount = 0;
      int turnTwoCount = 0;
      for(int j = 0; j < WIDTH; j++){
        boolean isEmpty = false;
        for(int i = 0; i < HEIGHT; i++){
          if(isEmpty && board[i][j] != -1){
            return false;
          }
          if(!isEmpty){
            if(board[i][j] == -1){
              isEmpty = true;
            }
            else if(board[i][j] == 0){
              turnOneCount++;
            }
            else{
              turnTwoCount++;
            }
          }
        }
      }
      totalMove = turnOneCount + turnTwoCount;
      int difference = turnOneCount - turnTwoCount;
      /*The number of two kinds of Pieces should differ within 1.
        Turn one will not be less than turn two.*/
      if(difference == -1 || difference == 0 ){
        playTurn = 0;
        return true;
      }
      else if(difference == 1){
        playTurn = 1;
        return true;
      }
      else{
        return false;
      }
    }
  }
  
  /* Give the board default value which is -1 */
  private void setBoard() {
    for(int i = 0; i < HEIGHT; i ++){
      for(int j = 0; j < WIDTH; j++){
        board[i][j] = -1;
      }
    }
  }
  
  /**
   * Get WIDTH in config
   * @return WIDTH
   */
  public int getWidth(){
    return WIDTH;
  }
  
  /**
   * Get HEIGHT in config
   * @return HEIGHT
   */
  public int getHeight(){
    return HEIGHT;
  }
  
  /**
   * Get connectCount in config
   * @return CONNECT_COUNT
   */
  public int getConnectCount(){ 
    return CONNECT_COUNT;
  }
  
  /**
   * Get the customer Board
   * @return Board
   */
  public int[][] getBoard(){
    return board;
  }
  
  /**
   * Get the play turn
   * @return playTurn
   */
  public int getPlayTurn(){
    return playTurn;
  }
  
  /**
   * Get the total move
   * @return totalMove
   */
  public int getTotalMove(){
    return totalMove;
  }
}
