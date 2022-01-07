package Configurations.Oscillator;

import BaseStrategy.BaseStrategy;
import BaseStrategy.Indicators;
import BaseStrategy.SimulateStrategy;
import Configurations.Default.ZoneDateTimeStart;
import Model.MarketDataModel;
import Model.Order;
import Model.Time;

import java.util.List;

public class Strategy extends BaseStrategy {
    public Strategy() {
        secretKey = 39.434110292188667851326802853084;
        timeFrameForEvaluation = new Time(2000);
        zonedDateTimeStart = new ZoneDateTimeStart(20);
    }

    public Order strategy(List<MarketDataModel> marketData, boolean isOrderSet) throws Exception {
        Indicators stochasticOscillator = new Indicators();

        if(stochasticOscillator.stochasticOscillator(marketData , 10) >= 80) {
            return Order.SELL;
        } else if(stochasticOscillator.stochasticOscillator(marketData ,10) <= 20) {
            return Order.BUY;
        }
        return Order.DRAW;
    }


    @Override
    public Double evaluateChancesToBeSuccessful(List<MarketDataModel> marketData) {
        return SimulateStrategy.simulate(new Strategy(), marketData);
    }
}