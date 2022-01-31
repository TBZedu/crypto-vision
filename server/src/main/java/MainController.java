import Configuration.TradingConfiguration;
import Configurations.Default.Strategy;
import BaseStrategy.BaseStrategy;
import Interfaces.IMarketData;
import Interfaces.IStrategy;
import Model.MarketDataModel;
import Model.StopMode;
import Model.ThreadDetails;
import net.jacobpeterson.alpaca.AlpacaAPI;
import org.reflections.Reflections;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController extends Thread{
    private boolean isRunning;
    private static final int PERCANTAGEOFTOTAL = 95;
    private IMarketData marketData;
    private AlpacaAPI alpacaAPI;
    private HashMap<String, ThreadDetails> runningThreads = new HashMap<>();
    private HashMap<String, ThreadDetails> oldRunningThreads = new HashMap<>();



    public MainController(IMarketData marketData, AlpacaAPI alpacaAPI){
        this.marketData = marketData;
        this.alpacaAPI = alpacaAPI;
    }

    /**
     * starts MainController
     * creates Tradingbots as new Threads
     */
    public void run(){
        var watchlist = new Properties();
        isRunning = true;
        while (isRunning) {
            refreshRunningThreads();

            if(!this.oldRunningThreads.equals(this.runningThreads)){
                rebalanceMoney();
            }

            if(!watchlist.equals(ReadWatchlistFile.readWatchlistFile("trading.Watchlist.properties"))){
                watchlist = (Properties) ReadWatchlistFile.readWatchlistFile("trading.Watchlist.properties").clone();
                startStrategys(watchlist);
                rebalanceMoney();
            }
            else{
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private HashMap<String, ThreadDetails> refreshRunningThreads() {
        ArrayList<String> toDelete = new ArrayList<>();
        this.runningThreads.forEach((symbol, threadDetails) -> {
            if(!threadDetails.getThread().isAlive()){
                System.out.println("Removed Thread " + symbol);
                toDelete.add(symbol);
            }
        });

        for(String symbolOfToDelete:toDelete){
            this.runningThreads.remove(symbolOfToDelete);
        }

        return this.runningThreads;
    }

    private void startStrategys(Properties watchlist){
        this.oldRunningThreads = (HashMap<String, ThreadDetails>) this.runningThreads.clone();
        var strategies = updateClasses();
        watchlist.forEach((k, v) ->{
            String symbol = (String) v;
            if(this.runningThreads.containsKey(symbol)){
                return;
            }

            var strategy = getBestStrategy(strategies, symbol);

            if(strategy != null){
                var riskFactor = 0;

                System.out.print(symbol + " get's started with strategy: " + strategy.getClass().getName() + "\n");
                var configuration = new TradingConfiguration(symbol, strategy, riskFactor); // TODO implement setuppservice
                var thread = new ThreadDetails(new Thread(new TradingBot(configuration, new TradingbotAPI(configuration, alpacaAPI), this.marketData, this)), configuration);
                thread.getThread().start();
                this.runningThreads.put(symbol, thread);
                System.out.println("Number of active threads : " + Thread.activeCount());
            }
            else{
                System.out.println("Couldn't find good strategy for Symbol: " + symbol);
            }
        });
    }

    public StopMode getStopMode(String marktOfThread) throws Exception {
        if(this.runningThreads.containsKey(marktOfThread)){
            return this.runningThreads.get(marktOfThread).getStopMode();
        }

        throw new Exception("Can't find a Tradingbot working on market: " + marktOfThread);
        // handle Thread that isn't being tract
    }

    public void stop(StopMode stopMode, String marketToClose) throws Exception {
        if (marketToClose == null){
            for(ThreadDetails thread:this.runningThreads.values()){
                thread.setStopMode(stopMode);
            }
            isRunning = false;
            return;
        }

        if(this.runningThreads.containsKey(marketToClose)){
            this.runningThreads.get(marketToClose).setStopMode(stopMode);
            isRunning = false;
            return;
        }

        throw new Exception("Can't find a Tradingbot working on market: " + marketToClose);
    }

    private void rebalanceMoney(){
        if(!this.oldRunningThreads.equals(this.runningThreads)){
            if(this.oldRunningThreads.size() < this.runningThreads.size()){
                this.runningThreads.forEach((k, v) -> {
                    if(this.oldRunningThreads.containsKey(k)){
                        v.getConfiguration().setRiskFactor(PERCANTAGEOFTOTAL/ this.runningThreads.size()); //Todo test this
                    }
                });

                AtomicBoolean notDone = new AtomicBoolean(false);

                while(notDone.get()){
                    notDone.set(false);
                    this.runningThreads.forEach((k, v) -> {
                        if(this.oldRunningThreads.containsKey(k)){
                            if(!v.getConfiguration().isRiskFactorChanged()){
                                notDone.set(true);
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                this.runningThreads.forEach((k, v) -> {
                    if(!this.oldRunningThreads.containsKey(k)){
                        v.getConfiguration().setRiskFactor(PERCANTAGEOFTOTAL/this.runningThreads.size());
                    }
                });
            }
            else{
                this.runningThreads.forEach((k, v)  -> {
                    v.getConfiguration().setRiskFactor(PERCANTAGEOFTOTAL/this.runningThreads.size());
                });
            }

            this.oldRunningThreads = (HashMap<String, ThreadDetails>) this.runningThreads.clone();
        }
    }


    /**
     * refreshes Strategy Classes and checks for scurity Key
     * @return all Strategys in this project as Class
     */
    private ArrayList<IStrategy> updateClasses(){
        Reflections reflections = new Reflections("Configurations");
        Set<Class<? extends BaseStrategy>> strategies = reflections.getSubTypesOf(BaseStrategy.class);
        strategies.add(Strategy.class);

        ClassLoader classLoader = MainController.class.getClassLoader();

        ArrayList<IStrategy> strategys = new ArrayList<>();

        for(Class<? extends BaseStrategy> strategy:strategies){
            try {
                strategys.add((IStrategy) classLoader.loadClass(strategy.getName()).newInstance());

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        strategys = checkSecretKey(strategys);

        return strategys;
    }

    /**
     * checks given Classes with their Scuritykey
     * @param strategies that need to get checked
     * @return the strategies given without the strategies
     */
    private ArrayList<IStrategy> checkSecretKey(ArrayList<IStrategy> strategies){
        try{
            var secretKeys = new ArrayList<Double>();
            for(IStrategy strategy:strategies){
                secretKeys.add(strategy.getSecretKey());
            }

            for(int i = 0; i < secretKeys.size(); i++){
                if(Collections.frequency(secretKeys, secretKeys.get(i)) > 1){
                    strategies.remove(strategies.get(i));
                    continue;
                }
                if(secretKeys.get(i) * 19 * 7 * 77 * 217 * 9 / 3 / 99 / 83 % 79 != 0){
                    strategies.remove(strategies.get(i));
                    continue;
                }
            }
            return strategies;
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * goes through given List of strategies and pics the best one for the given symbol
     * @param strategies all strategies that could be used as strategy
     * @param symbol the symbol of the market where the best strategy needs to be found
     * @return returns the strategy that should be the best one
     */
    private IStrategy getBestStrategy(List<IStrategy> strategies, String symbol){ // Todo implement that a Strategy that got "banned" from the user, won't get chosen for the same Market
        List<Double> strategyChance = new ArrayList<>();
        for(IStrategy strategy:strategies){
            List<MarketDataModel> data = null;
            try {
                data = marketData.getCryptoMarketData(ZonedDateTime.now(ZoneId.of("America/New_York")).minusMinutes(strategy.getTimeFrameForEvaluation().getMinute()), strategy.getLimit(), symbol, strategy.getBarsTimeFrame());
            } catch (Exception e) {
                e.printStackTrace(); // should mention something is wrong with mark
            }
            var profitLoss = strategy.evaluateChancesToBeSuccessful(data);
            System.out.println("Estimation of profit with " + strategy.getName() + ": " + profitLoss);
            strategyChance.add(profitLoss);
        }
        var bestChance = Collections.max(strategyChance);
        if(bestChance > 0){
            return strategies.get(strategyChance.indexOf(bestChance));
        }
        return null;
    }
}
