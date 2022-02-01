package BaseStrategy;

import Configurations.Default.ZoneDataTimeNow;
import Configurations.Default.ZoneDateTimeStart;
import Interfaces.IStrategy;
import Interfaces.IZoneDateTimeEnd;
import Interfaces.IZoneDateTimeStart;
import Model.MarketDataModel;
import Model.Order;
import Model.Time;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderClass;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderTimeInForce;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderType;

import java.time.temporal.ChronoUnit;
import java.util.List;
// TODO get rid of references on BaseStrategy
public abstract class BaseStrategy implements IStrategy {

    protected String name = "Default Strategy";

    protected Double limitPrice = null;
    protected Double trailPercent = null;
    protected Double stopPrice = null;
    protected Double trailPrice = null;
    protected Double stopLossStopPrice = null;
    protected Double stopLossLimitPrice = null;
    protected Double takeProfitLimitPrice = null;
    protected Boolean extendedHours = null;
    protected String clientOrderID = null;

    protected IZoneDateTimeEnd zoneDateTimeEnd = new ZoneDataTimeNow();
    protected IZoneDateTimeStart zonedDateTimeStart = new ZoneDateTimeStart(1);
    protected OrderType orderType = OrderType.MARKET;
    protected OrderTimeInForce orderTimeInForce = OrderTimeInForce.GOOD_UNTIL_CANCELLED;
    protected OrderClass orderClass = null;
    protected BarTimePeriod barsTimeFrame = BarTimePeriod.MINUTE;
    protected Time timeFrameForEvaluation;

    protected Double secretKey;

    public String getName(){ return name; }

    public final Double getSecretKey(){
        return secretKey;
    }

    public Double getLimitPrice() {
        return limitPrice;
    }

    public Double getStopPrice() {
        return stopPrice;
    }

    public Double getStopLossStopPrice() {
        return stopLossStopPrice;
    }

    public Double getStopLossLimitPrice() {
        return stopLossLimitPrice;
    }

    public Double getTakeProfitLimitPrice() {
        return takeProfitLimitPrice;
    }

    public Double getTrailPrice() {
        return trailPrice;
    }

    public Double getTrailPercent() {
        return trailPercent;
    }

    public Boolean getExtendedHours() {
        return extendedHours;
    }

    public String getClientOrderID() {
        return clientOrderID;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public OrderTimeInForce getOrderTimeInForce() {
        return orderTimeInForce;
    }

    public IZoneDateTimeEnd getZoneDateTimeEnd() {
        return zoneDateTimeEnd;
    }

    public Integer getLimit() {
      return 10000; // TODO calculate this from getZoneDateTimeEnd / somehow doesn't accept null
    }

    public OrderClass getOrderClass() {
        return orderClass;
    }

    public BarTimePeriod getBarsTimeFrame(){
        return barsTimeFrame;
    }

    public IZoneDateTimeStart getZoneDateTimeStart(){ return zonedDateTimeStart; }

    public Time getTimeFrameForEvaluation() {
        return timeFrameForEvaluation;
    }

    public abstract Order strategy(List<MarketDataModel> marketData, boolean isOrderSet) throws Exception;

    public abstract Double evaluateChancesToBeSuccessful(List<MarketDataModel> marketData);

}
