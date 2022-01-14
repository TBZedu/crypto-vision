import Model.StopMode;
import Model.ThreadDetails;
import net.jacobpeterson.alpaca.AlpacaAPI;

public class Main extends Thread {
    public static void main(String[] args) throws InterruptedException {
        AlpacaAPI alpacaAPI = new AlpacaAPI();
        MarketData marketData = new MarketData(alpacaAPI);
        MainController mainController = new MainController(marketData, alpacaAPI);
        Thread thread = new Thread(mainController);

        thread.start();

        Thread.sleep(10000);
        try{
            mainController.stop(StopMode.fast, "TSLA");
            System.out.println("Stopped TSLA fast");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
