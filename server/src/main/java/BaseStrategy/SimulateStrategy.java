package BaseStrategy;

import Interfaces.IStrategy;
import Model.MarketDataModel;
import Model.Order;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Simulates a Strategy to test how good a Strategy would have done
 */
public class SimulateStrategy {
    /**
     * simulates a strategy with the given data
     * @param strategy the strategy that should get tested
     * @param marketData the data with that the strategy should get tested
     * @return how good the strategy did and how much profit it got
     */
    public static Double simulate(IStrategy strategy, List<MarketDataModel> marketData){
        int timeFrame = zoneDateTimeDifferenceOfStrategy(strategy);

        double profit = 0;
        Boolean isOrderSet = false;
        try{
            for(int i = timeFrame; i < marketData.size() - 1; i++){
                Order order = strategy.strategy(marketData.subList(i - timeFrame, i), isOrderSet);
                if(order == Order.BUY){
                    isOrderSet = true;
                }
                else if(order == Order.SELL){
                    isOrderSet = false;
                }
                else if(order == Order.CLOSE){
                    isOrderSet = null;
                }

                if(isOrderSet == true){
                    profit += marketData.get(i).getClosePrice() - marketData.get(i + 1).getClosePrice();
                }
                else if(isOrderSet == false){
                    profit += marketData.get(i + 1).getClosePrice() - marketData.get(i).getClosePrice();
                }
            }

            return (profit * 100)/marketData.get(0).getOpenPrice();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * get's different of the zoneDateTimes of the zoneDateTimeStart and the zoneDateTimeEnd
     * @param strategy the Strategy that you want to extract the difference of zoneDateTimeStart and zoneDateTimeEnd
     * @return the difference in minutes
     */
    private static int zoneDateTimeDifferenceOfStrategy(IStrategy strategy){
        return zonedDateTimeDifference(strategy.getZoneDateTimeStart().getZoneDateTime(), strategy.getZoneDateTimeEnd().getZoneDateTime(), ChronoUnit.MINUTES);
    }

    /**
     * get's the difference of two ZoneDateTimes
     * @param d1 first ZoneDateTime
     * @param d2 second ZoneDateTime
     * @param unit in what unit you want the return value
     * @return the difference in the requested unit
     */
    private static int zonedDateTimeDifference(ZonedDateTime d1, ZonedDateTime d2, ChronoUnit unit){
        return (int) unit.between(d1, d2);
    }
}
