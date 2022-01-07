package BaseStrategy;
import Model.MarketDataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class provides Indicators that can be used by Strategies
 */
public class Indicators {
    /**
     * Calculates the stochasticOscillatorIndex from the given data
     * @return stochasticOscillatorIndex
     * @param marketData list of open and close prices
     * @param pricesAmount how many prices the stochastic oscillator gets to calculate the index
     */
    public int stochasticOscillator(List<MarketDataModel> marketData , int pricesAmount) {
        List<Double> highs = new ArrayList<Double>();
        List<Double> lows = new ArrayList<Double>();

        for(int i = 0; i < pricesAmount; i++) {
            highs.add(marketData.get(i).getClosePrice());
            lows.add(marketData.get(i).getOpenPrice());
        }

        double highestHighPrice = Collections.max(highs);
        double lowestLowPrice = Collections.min(lows);
        double latestPrice = highs.get(highs.size() - 1);

        double indicatorValue = latestPrice - lowestLowPrice / (highestHighPrice - lowestLowPrice) * 100;
        int stochasticOscillatorIndex = (int)indicatorValue;
        return stochasticOscillatorIndex;
    }
}
