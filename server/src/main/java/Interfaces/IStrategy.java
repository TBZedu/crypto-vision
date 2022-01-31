package Interfaces;

import Model.MarketDataModel;
import Model.Order;
import Model.Time;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderClass;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderTimeInForce;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderType;

import java.util.List;

public interface IStrategy {
    String getName();

    Double getSecretKey();

    Double getLimitPrice();

    Double getStopPrice();

    Double getStopLossStopPrice();

    Double getStopLossLimitPrice();

    Double getTakeProfitLimitPrice();

    Double getTrailPrice();

    Double getTrailPercent();

    Boolean getExtendedHours();

    String getClientOrderID();

    OrderType getOrderType();

    OrderTimeInForce getOrderTimeInForce();

    IZoneDateTimeEnd getZoneDateTimeEnd();

    Integer getLimit();

    OrderClass getOrderClass();

    BarTimePeriod getBarsTimeFrame();

    IZoneDateTimeStart getZoneDateTimeStart();

    Time getTimeFrameForEvaluation();

    Order strategy(List<MarketDataModel> marketData, boolean isOrderSet) throws Exception;

    Double evaluateChancesToBeSuccessful(List<MarketDataModel> marketData);
}
