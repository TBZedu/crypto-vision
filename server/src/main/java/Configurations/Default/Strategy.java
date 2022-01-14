package Configurations.Default;

import BaseStrategy.BaseStrategy;
import Model.MarketDataModel;
import Model.Order;
import Model.Time;
import org.apache.commons.lang.NullArgumentException;
import BaseStrategy.SimulateStrategy;

import java.util.List;


public class Strategy extends BaseStrategy {

    public Strategy() {
        secretKey = 69.228771401842328005662609453192;
        timeFrameForEvaluation = new Time(1000);
    }

    @Override
    public Order strategy(List<MarketDataModel> marketData, boolean isOrderSet) throws Exception {
        if (marketData == null || marketData.get(0) == null){
            throw new NullArgumentException("Problem with marketdata!");
        }
        if (marketData.get(0).getOpenPrice() > marketData.get(0).getClosePrice()){
            return Order.BUY;
        }
        else if(marketData.get(0).getOpenPrice() <= marketData.get(0).getClosePrice()){
            return Order.SELL;
        }
        else {
            throw new Exception("Problem with marketdata!");
        }
    }

    @Override
    public Double evaluateChancesToBeSuccessful(List<MarketDataModel> marketData) {
        var profitLoss = SimulateStrategy.simulate(new Strategy(), marketData);
        return profitLoss;
    }
}
