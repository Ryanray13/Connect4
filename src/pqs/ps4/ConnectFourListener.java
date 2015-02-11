package pqs.ps4;

public interface ConnectFourListener {
  void makeMove(int row, int col, int playTurn);
  void gameWin(int playTurn);
  void gameEven();
  void startGame(int playTurn);
}
