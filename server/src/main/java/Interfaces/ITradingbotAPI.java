package Interfaces;

import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderSide;

public interface ITradingbotAPI {

    void setOrder(OrderSide orderSide, Double stockPrize);

    void closeOrder();

    void closeAllOrders();

    boolean IsOrderSet(OrderSide ... orderSide);

    boolean IsMarketOnline();
}
