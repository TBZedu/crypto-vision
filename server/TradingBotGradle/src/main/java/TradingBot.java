import Configuration.TradingConfiguration;
import Interfaces.IMarketData;
import Interfaces.ITradingbotAPI;
import Model.MarketDataModel;
import Model.Order;
import Model.StopMode;
import net.jacobpeterson.alpaca.enums.OrderSide;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The TradingBot class controls the main actions of the program
 * The methods behaviour is get defined by the tradingConfiguration and gets transferred by the tradingConfiguration constructor
 */
public class TradingBot extends Thread {
    public boolean isRunning = true;
    private TradingConfiguration configuration;
    private final ITradingbotAPI tradingbotAPI;
    private final IMarketData marketData;
    private boolean noMarketData = true;
    private MainController mainController;


    public TradingBot(TradingConfiguration configuration, ITradingbotAPI tradingbotAPI, IMarketData marketData, MainController mainController){
        this.configuration = configuration;
        this.tradingbotAPI = tradingbotAPI;
        this.marketData = marketData;
        this.mainController = mainController;
    }

    /**
     * The start method will start the tradingbot.
     * - gets marketdata
     * - evaluates strategy and depending on the situation it opens or closes order/s
     * @throws RuntimeException when not all Positions can be closed
     */
    public void run() {
        try {
            Order tempResult;
            LocalDateTime localDateTime = LocalDateTime.now().minusSeconds(60);
            while (true){
                if(mainController.getStopMode(configuration.getSymbol()) == StopMode.fast){
                    tradingbotAPI.closeOrder();
                    isRunning = false;
                    System.out.println("Tradingbot on Market " + configuration.getSymbol() + " got shut down (fast).");
                    return;
                }

                if(mainController.getStopMode(configuration.getSymbol()) == StopMode.normal){
                    isRunning = false;
                    System.out.println("Tradingbot on Market " + configuration.getSymbol() + " will get shut down soon (normal mode).");
                }

                this.checkMarket();

                if(localDateTime.isAfter(LocalDateTime.now().minusSeconds(59))){ // TODO test this
                    System.out.println("Sleep gets called.");
                    Thread.sleep(1000);
                    continue;
                }

                System.out.println("Get MarketData");
                List<MarketDataModel> tempMarketData = this.marketData.getMarketData(configuration.getStrategy().getZoneDateTimeStart().getZoneDateTime(), configuration.getStrategy().getZoneDateTimeEnd().getZoneDateTime(), configuration.getSymbol(), configuration.getStrategy().getBarsTimeFrame());

                if(!this.verifyTempMarketData(tempMarketData)){
                    continue;
                }


                System.out.println("TempMarketData has values " + tempMarketData.size() + ", closePrice: " + tempMarketData.get(0).getClosePrice() + ", openPrice " + tempMarketData.get(0).getOpenPrice());

                tempResult = configuration.getStrategy().strategy(tempMarketData, this.tradingbotAPI.IsOrderSet(null));
                if(this.tradingbotAPI.IsOrderSet(tempResult.getOrderSide()) == false && !isRunning){ // todo bring this decision to Strategy
                    tradingbotAPI.closeOrder();
                    return;
                }

                this.setOrder(tempResult, tempMarketData);
                localDateTime = LocalDateTime.now();
            }
        }
        catch (Exception e){
            System.out.println("Error, warning: " + e);
            stopTrading();
            tradingbotAPI.closeAllOrders();
            e.printStackTrace();
        }
    }

    /**
     * Checks if noMarketData is true and if market is online. if market is offline, Thread will sleep
     */
    private void checkMarket() {
        if(this.noMarketData){
            if(!tradingbotAPI.IsMarketOnline()){
                System.out.println("Market is closed, sleeping.");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * verifys tempMarketData, if tempMarketData is null or empty noMarketData will be set true and it will return false
     * @param tempMarketData marketData that should be checked
     * @return true if MarketData is valid and false if MarketData isn't
     */
    private boolean verifyTempMarketData(List<MarketDataModel> tempMarketData){
        if(tempMarketData == null){
            System.out.println("MarketData is null");
            this.noMarketData = true;
            return false;
        }
        if(tempMarketData.size() == 0){
            System.out.println("MarketData has no values.");
            this.noMarketData = true;
            return false;
        }
        this.noMarketData = false;
        return true;
    }

    /**
     * checks param rempResult and set's the Order needed
     * @param tempResult defines what Orderside should be set
     * @param tempMarketData defines stockprize that will be set
     */
    private void setOrder(Order tempResult, List<MarketDataModel> tempMarketData) {
        if(tempResult == Order.BUY){
            tradingbotAPI.setOrder(OrderSide.BUY, tempMarketData.get(tempMarketData.size() - 1).getClosePrice());
        }
        else if(tempResult == Order.SELL){
            tradingbotAPI.setOrder(OrderSide.SELL, tempMarketData.get(tempMarketData.size() - 1).getClosePrice());
        }
        else if(tempResult == Order.CLOSE){
            tradingbotAPI.closeOrder();
        }
    }

    /**
     * The stopTrading method will stop the tradingbot and closes all orders
     * @throws RuntimeException when not all Positions can be closed
     */
    public void stopTrading(){
        isRunning = false;
        tradingbotAPI.closeAllOrders();
    }

    /**
     * The sendNotification method send a Notification base on given parameter
     * not implemented yet!
     * @param msg the message the use should get
     */
    // TODO implement a Notification that the user will see (email), use at every exception
    private void sendNotification(String msg){
        throw new NotImplementedException();
    }
}