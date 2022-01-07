package Configuration;

import BaseStrategy.BaseStrategy;
import Interfaces.IStrategy;
import net.jacobpeterson.alpaca.AlpacaAPI;

/**
 * This is a Class that provides all Data that you need to specify your Tradingbot.
 * It has the 3 private variables: symbol, strategy and riskFactor, accessible with public getter and setter methods
 */

public class TradingConfiguration {
    private String symbol;
    private IStrategy strategy;
    private Integer riskFactor;

    public TradingConfiguration(String symbol, IStrategy strategy, Integer riskFactor){
        this.symbol = symbol;
        this.strategy = strategy;
        this.riskFactor = riskFactor;
    }
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public IStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public Integer getRiskFactor(){
        return riskFactor;
    }

    public void setRiskFactor(Integer riskFactor){
        this.riskFactor = riskFactor;
    }
}
