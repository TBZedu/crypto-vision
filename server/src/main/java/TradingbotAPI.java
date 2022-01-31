import Interfaces.ITradingbotAPI;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderSide;
import net.jacobpeterson.alpaca.model.endpoint.positions.Position;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import Configuration.TradingConfiguration;

import java.util.HashMap;
import java.util.HashSet;

public class TradingbotAPI implements ITradingbotAPI {

    private TradingConfiguration configuration;
    private AlpacaAPI alpacaAPI;

    public TradingbotAPI(TradingConfiguration configuration, AlpacaAPI alpacaAPI){
        this.configuration = configuration;
        this.alpacaAPI = alpacaAPI;
    }

    /**
     * The setOrder method set a new Order specified with value based on the given tradingConfigurations
     * @throws RuntimeException when not all Positions can be closed
     */
    @Override
    public void setOrder(OrderSide orderSide, Double stockPrice){
        System.out.println("SetOrder get's called (Orderside: " + orderSide + ").");
        try {
            if(!IsOrderSet(orderSide)){
                if(IsOrderSet()){
                    System.out.println("Closes Order of wrong OrderSide");
                    closeOrder();
                }
                System.out.println("Sets order");
                int accountCash = (int)(Double.parseDouble(this.alpacaAPI.account().get().getEquity()) / 100 * configuration.getRiskFactor() / stockPrice);

                this.alpacaAPI.orders().requestOrder(configuration.getSymbol(), (double)accountCash, null , orderSide, configuration.getStrategy().getOrderType(), configuration.getStrategy().getOrderTimeInForce(), configuration.getStrategy().getLimitPrice(), configuration.getStrategy().getStopPrice(), configuration.getStrategy().getTrailPrice(), configuration.getStrategy().getTrailPercent(), configuration.getStrategy().getExtendedHours(), configuration.getStrategy().getClientOrderID(), configuration.getStrategy().getOrderClass(), configuration.getStrategy().getTakeProfitLimitPrice(), configuration.getStrategy().getStopLossStopPrice(), configuration.getStrategy().getStopLossLimitPrice());
            }
        } catch (AlpacaClientException e) {
            System.out.println("Exception with Warning: " + e);
            closeAllOrders();
            e.printStackTrace();
        }
    }

    /**
     * The method closeOrder closes trades specified with value based on the given tradingConfigurations
     * @throws RuntimeException when not all Positions can be closed
     */
    @Override
    public void closeOrder(){
        System.out.println("closeOrder get's called");
        try {
            if(IsOrderSet()){
                System.out.println("Order get's closed");
                this.alpacaAPI.positions().close(configuration.getSymbol(), 0 , 100.0);
            }
            this.configuration.setRiskFactorChanged(true);
            return;
        } catch (AlpacaClientException e) {
            closeAllOrders();
            e.printStackTrace();
        }
    }

    /**
     * The method closeAllOrders closes all orders
     * @throws RuntimeException when not all Positions can be closed
     */
    @Override
    public void closeAllOrders(){
        try {
            System.out.println("All orders get closed");
            this.alpacaAPI.positions().closeAll(true);
        } catch (AlpacaClientException e) {
            throw new RuntimeException("Error with closing Positions, please check that all are closed!");
        }
    }

    /**
     * The method IsOrderSet checks if there is a Order set that fits the requirements of the param and the configuration symbol
     * @param orderSide checks if the Order is from the given Orderside
     * @return true if there is a Order set that fits the requirments, false if not
     */
    @Override
    public boolean IsOrderSet(OrderSide ... orderSide){
        var sideHashMap = new HashMap<String, OrderSide>();
        sideHashMap.put("long", OrderSide.BUY);
        sideHashMap.put("short", OrderSide.SELL);

        try {
            var openOrders = this.alpacaAPI.positions().get();
            if(orderSide == null){
                for(Position position:openOrders){
                    if(position.getSymbol().equals(configuration.getSymbol())){
                        return true;
                    }
                }
            }
            else {
                for(Position position:openOrders){
                    if(position.getSymbol().equals(configuration.getSymbol()) && sideHashMap.get(position.getSide()).equals(orderSide)){
                        return true;
                    }
                }
            }
            return false;
        } catch (AlpacaClientException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks if Market is online/active
     * @return true if Stockmarket is online, false if not
     */
    public boolean IsMarketOnline(){
      return true;
        /*try {
            return alpacaAPI.stockMarketData()..getIsOpen();
        } catch (AlpacaClientException e) {
            e.printStackTrace();
            return false;
        }*/
    }
}
