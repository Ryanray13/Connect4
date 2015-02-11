package pqs.ps4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import pqs.ps4.ConnectFourModel;
import pqs.ps4.ConnectFourModel.PlayMode;
import pqs.ps4.ConnectFourModel.Role;
import pqs.ps4.ConnectFourRobot;

import org.junit.Before;
import org.junit.Test;

/* 
 * You can always use model.printBoard() method to see what the board looks
 * like clearly in console.
 */
public class ConnectFourModelTest {
  
  ConnectFourModel model = ConnectFourModel.getInstance();
  ConnectFourRobot robot = new ConnectFourRobot(model);
  
  @Before
  public void setUp(){
    model.startGame(PlayMode.PLAYERS);
  }  
  
  @Test
  public void testMakeCorrectMove() {
    assertTrue(model.makeMove(3, 0, Role.PLAYER));
  }

  @Test
  public void testGetAvailableRow() {
    model.makeMove(3, 0, Role.PLAYER);
    assertEquals(1, model.getAvailableRow(3));
  }
 
  @Test
  public void testGetAvailableColumn() {
    int i;
    //Make the column full
    for(i = 0; i < model.getHeight(); i++){
      model.makeMove(3, i%2, Role.PLAYER);
    }
    assertEquals(6, model.getAvailableColumn().size());
  }
  
  @Test
  public void testMakeWrongMove() {
    int i;
    //Make the column full
    for(i = 0; i < model.getHeight(); i++){
      model.makeMove(2, i%2, Role.PLAYER);
    }
    assertFalse(model.makeMove(2, i % 2, Role.PLAYER));
  }
  
  @Test
  public void testCheckForWinInNonWinSituation(){
    model.makeMove(3, 0, Role.PLAYER); 
    model.makeMove(2 ,1, Role.PLAYER); 
    //model.printBoard();
    assertFalse(model.checkForWin(0, 4, 0));  
  }
  
  @Test
  public void testRowWin(){
    int i;
    for(i = 0; i < 3; i++){
      model.makeMove(i, 0, Role.PLAYER); 
      model.makeMove(6 - i, 1, Role.PLAYER); 
    }
    //model.printBoard();
    assertTrue(model.checkForWin(0, 3, 0));  
  }
  
  @Test
  public void testColumnWin(){
    int i;
    for(i = 0; i < 3; i++){
      model.makeMove(3, 0, Role.PLAYER); 
      model.makeMove(2, 1, Role.PLAYER); 
    }
    //model.printBoard();
    assertTrue(model.checkForWin(3, 3, 0));
  }
  
  @Test
  public void testLeftDiagonalWin(){
    model.makeMove(4, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    model.makeMove(3, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(2, 0, Role.PLAYER);
    model.makeMove(1, 1, Role.PLAYER);
    model.makeMove(0, 0, Role.PLAYER);
    model.makeMove(1, 1, Role.PLAYER);
    //model.printBoard();
    assertTrue(model.checkForWin(3, 1, 0));
  }
  
  @Test
  public void testRightDiagonalWin(){
    model.makeMove(0, 0, Role.PLAYER);
    model.makeMove(1, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(3, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(2, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    model.makeMove(4, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    //model.printBoard();
    assertTrue(model.checkForWin(3, 3, 0));
  }
  
  @Test
  public void testGameTied(){
    model.makeMove(0, 0, Role.PLAYER);
    model.makeMove(0, 1, Role.PLAYER);
    model.makeMove(0, 0, Role.PLAYER);
    model.makeMove(0, 1, Role.PLAYER);
    model.makeMove(0, 0, Role.PLAYER);
    model.makeMove(0, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(1, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(1, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(2, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(2, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(1, 0, Role.PLAYER);
    model.makeMove(2, 1, Role.PLAYER);
    model.makeMove(3, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    model.makeMove(3, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    model.makeMove(3, 0, Role.PLAYER);
    model.makeMove(3, 1, Role.PLAYER);
    model.makeMove(4, 0, Role.PLAYER);
    model.makeMove(4, 1, Role.PLAYER);
    model.makeMove(4, 0, Role.PLAYER);
    model.makeMove(4, 1, Role.PLAYER);
    model.makeMove(4, 0, Role.PLAYER);
    model.makeMove(4, 1, Role.PLAYER);
    model.makeMove(5, 0, Role.PLAYER);
    model.makeMove(6, 1, Role.PLAYER);
    model.makeMove(6, 0, Role.PLAYER);
    model.makeMove(5, 1, Role.PLAYER);
    model.makeMove(5, 0, Role.PLAYER);
    model.makeMove(6, 1, Role.PLAYER);
    model.makeMove(6, 0, Role.PLAYER);
    model.makeMove(5, 1, Role.PLAYER);
    model.makeMove(5, 0, Role.PLAYER);
    model.makeMove(6, 1, Role.PLAYER);
    model.makeMove(5, 0, Role.PLAYER);
    model.makeMove(6, 1, Role.PLAYER);
    //model.printBoard();
    //The Board is full, there is no available move
    assertEquals(0, model.getAvailableColumn().size());
  }
}
