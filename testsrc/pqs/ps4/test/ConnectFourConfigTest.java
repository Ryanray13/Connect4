package pqs.ps4.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.After;

import pqs.ps4.ConnectFourConfig;
import pqs.ps4.ConnectFourModel;
import pqs.ps4.ConnectFourModel.PlayMode;
import pqs.ps4.ConnectFourModel.Role;

/* 
 * This test is purely test whether config works even though config class
 * doesn't have any meaningful public method.
 * You can always use model.printBoard() method to see what the board looks
 * like clearly in console.
 */
public class ConnectFourConfigTest {
  
  //This board is valid and in a potential win situation
  int[][] standardBoard = new int[][]{
    {1, 1, 1, -1, 0, 0, 0},
    {-1, -1, -1, -1, -1, -1, -1},
    {-1, -1, -1, -1, -1, -1, -1},
    {-1, -1, -1, -1, -1, -1, -1},
    {-1, -1, -1, -1, -1, -1, -1},
    {-1, -1, -1, -1, -1, -1, -1},
  };
  
  /*This is actually a invalid board since 0 is three more than 1. This will
    cause config to build default empty 7*9 board*/
  int[][] largeBoard = new int[][]{
      {1, 1, -1, -1, 0, 0, 0, 0, 0},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
      {-1, -1, -1, -1, -1, -1, -1, -1, -1},
    };
  
  ConnectFourConfig standardConfig = new ConnectFourConfig
      .Builder().board(standardBoard).build();
  ConnectFourConfig largeBoardConfig = new ConnectFourConfig.Builder()
      .width(9).height(7).connectCount(5).board(largeBoard).build();
  ConnectFourConfig defaultConfig = new ConnectFourConfig.Builder().build();
  
  ConnectFourModel model = ConnectFourModel.getInstance();
  
  @Test
  public void testCopyValidBoard() {
    ConnectFourModel.getCustomerInstance(standardConfig);
    model.startGame(PlayMode.PLAYERS);
    assertTrue(model.checkForWin(0, 3, 0));
    //model.printBoard();
  }
  
  @Test
  public void testCopyInValidBoard() {
    ConnectFourModel.getCustomerInstance(largeBoardConfig);
    model.startGame(PlayMode.PLAYERS);
    //board is empty, totalMove will be 0
    assertEquals(0, model.getTotalMove());
    //model.printBoard();
  }
  
  @Test
  public void testConnectFourFive() {
    ConnectFourModel model = ConnectFourModel
        .getCustomerInstance(largeBoardConfig);
    model.startGame(PlayMode.PLAYERS);
    int i;
    for(i = 0; i < 4; i++){
      model.makeMove(i, 0, Role.PLAYER); 
      model.makeMove(8 - i, 1, Role.PLAYER); 
    }
    //model.printBoard();
    assertTrue(model.checkForWin(0, 4, 0));  
  }
  
  @After
  public void restoreDefaultModel(){
    /* Change model to default since we are using singleton pattern
       Thus this will not affect other test*/
     ConnectFourModel.getCustomerInstance(defaultConfig);
  }
}
