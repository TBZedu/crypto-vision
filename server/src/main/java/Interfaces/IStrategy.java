package Interfaces;

import Model.MarketDataModel;
import Model.Order;
import Model.Time;
import net.jacobpeterson.alpaca.enums.BarsTimeFrame;
import net.jacobpeterson.alpaca.enums.OrderClass;
import net.jacobpeterson.alpaca.enums.OrderTimeInForce;
import net.jacobpeterson.alpaca.enums.OrderType;

import java.time.LocalTime;
import java.time.ZonedDateTime;
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

    OrderClass getOrderClass();

    BarsTimeFrame getBarsTimeFrame();

    IZoneDateTimeStart getZoneDateTimeStart();

    Time getTimeFrameForEvaluation();

    Order strategy(List<MarketDataModel> marketData, boolean isOrderSet) throws Exception;

    Double evaluateChancesToBeSuccessful(List<MarketDataModel> marketData);
}
