package pqs.ps4.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pqs.ps4.ConnectFourModel;
import pqs.ps4.ConnectFourModel.PlayMode;
import pqs.ps4.ConnectFourModel.Role;
import pqs.ps4.ConnectFourRobot;

/* 
 * You can always use model.printBoard() method to see what the board looks
 * like clearly in console.
 */
public class ConnectFourRobotTest {

  ConnectFourModel model = ConnectFourModel.getInstance();
  ConnectFourRobot robot = new ConnectFourRobot(model);
  
  @Test
  public void testRobotMakeFirstRegularMove(){
    model.startGame(PlayMode.ROBOTFIRST);
    assertFalse(robot.getMove(0));
  }
  
  @Test
  public void testRobotMakeSecondRegularMove(){
    model.startGame(PlayMode.PLAYERFIRST);
    model.makeMove(3, 0, Role.PLAYER);
    //test robot made the move
    assertEquals(2, model.getTotalMove());
  }
  
  @Test
  public void testRobotMakeWinMove(){
    /*Since if we don't have a potential win situation the robot will
      Random choose a move makes it difficult to test. Thus we use a little
      trick that we use players mode to create a potential win situation
      and test whether robot make that win move */
    model.startGame(PlayMode.PLAYERS);
    int i;
    for(i = 0; i < 3; i++){
      model.makeMove(i, 0, Role.PLAYER); 
      model.makeMove(6 - i, 1, Role.PLAYER); 
    }
    assertTrue(robot.getMove(0));
    //model.printBoard();
  }
}
