package pqs.ps4;

import pqs.ps4.ConnectFourModel.Role;

import java.util.List;
import java.util.Random;

public class ConnectFourRobot {
  
  private ConnectFourModel model;
  
  public ConnectFourRobot(ConnectFourModel model){
    this.model = model;
  }
  
  /**
   * Main method for robot. First get available columns with presents the
   * columns that are not full. Then try whether those columns have a move 
   * to win. If not randomly choose a column and make the move.
   * @param turn Indicates which piece Robot uses.
   * @return true if robot makes a win move false if a regular move.
   */
  public boolean getMove(int turn){
    List<Integer> availableColumn = model.getAvailableColumn();
    for(int i = 0; i < availableColumn.size(); i++){
      int col = availableColumn.get(i);
      int row = model.getAvailableRow(col);
      if(model.checkForWin(row, col, turn)){
        return model.makeMove(col, turn, Role.ROBOT);
      }
    }
    int randomIndex = new Random().nextInt(availableColumn.size());
    int col = availableColumn.get(randomIndex);
    model.makeMove(col, turn, Role.ROBOT);
    return false;
  }
}
