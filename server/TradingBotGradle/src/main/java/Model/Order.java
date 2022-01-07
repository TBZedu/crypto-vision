package Model;

import net.jacobpeterson.alpaca.enums.OrderSide;

/**
 * Order is an enum that has the 4 option: "BUY", "SELL", "CLOSE" and "DRAW".
 * Can set the Tradingdecision that the Order will take
 * BUY for long Order
 * SELL for short Order
 * CLOSE for closing an Order
 * DRAW for not doing anything
 */
public enum Order {
    BUY,
    SELL,
    CLOSE,
    DRAW;

    /**
     * convert an Order to an Orderside
     * if Order.CLOSE or Oder.DRAW is set, the return value is null
     * @return Orderside depending on given Order
     */
    public OrderSide getOrderSide(){
        OrderSide orderside;
        if(this == Order.SELL){
            orderside = OrderSide.SELL;
        }
        else if(this == Order.BUY){
            orderside = OrderSide.BUY;
        }
        else {
            orderside = null;
        }
        return orderside;
    }
}
