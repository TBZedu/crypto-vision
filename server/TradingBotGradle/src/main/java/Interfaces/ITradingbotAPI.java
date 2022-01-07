package Interfaces;

import net.jacobpeterson.alpaca.enums.OrderSide;
import org.apache.logging.log4j.core.config.Order;

public interface ITradingbotAPI {

    void setOrder(OrderSide orderSide, Double stockPrize);

    void closeOrder();

    void closeAllOrders();

    boolean IsOrderSet(OrderSide orderSide);

    boolean IsMarketOnline();
}
