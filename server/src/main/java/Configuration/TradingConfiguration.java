package Configuration;

import Interfaces.IStrategy;

/**
 * This is a Class that provides all Data that you need to specify your Tradingbot.
 * It has the 3 private variables: symbol, strategy and riskFactor, accessible with public getter and setter methods
 */

public class TradingConfiguration {
    private String symbol;
    private IStrategy strategy;
    private Integer riskFactor;
    private boolean riskFactorChanged;

    public TradingConfiguration(String symbol, IStrategy strategy, Integer riskFactor){
        this.symbol = symbol;
        this.strategy = strategy;
        this.riskFactor = riskFactor;
        this.setRiskFactorChanged(false);
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
        this.setRiskFactorChanged(false);
    }

    public boolean isRiskFactorChanged() {
        return riskFactorChanged;
    }

    public void setRiskFactorChanged(boolean riskFactorChanged) {
        this.riskFactorChanged = riskFactorChanged;
    }
}
