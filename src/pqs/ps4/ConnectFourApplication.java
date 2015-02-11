package pqs.ps4;

/*
 * The code in comment can enable some features of the model or add another
 * view.
 */
public class ConnectFourApplication {
  public static void main(String[] args){
    //ConnectFourConfig config = new ConnectFourConfig.Builder().width(10)
        //.height(8).connectCount(4).build();
    //ConnectFourModel model = ConnectFourModel.getCustomerInstance(config); 
    ConnectFourModel model = ConnectFourModel.getInstance(); 
    new ConnectFourView(model);
    new ConnectFourView(model);
  }
}
