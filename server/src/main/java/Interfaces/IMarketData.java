package Interfaces;

import Model.MarketDataModel;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Returns the demanded Data from the market.
 */
public interface IMarketData {
  /**
   * Gets the MarketData.
   * @param start The time from when on you want get the data.
   * @param end The time to when you want to get data.
   * @param symbol The market symbol of the market you want to get the data from.
   * @param barsTimeFrame The time that should be between every data
   * @return The data from the market specified with the parameters.
   */
  List<MarketDataModel> getMarketData(ZonedDateTime start, ZonedDateTime end, String symbol, BarTimePeriod barsTimeFrame) throws Exception;

  /**
   * Gets the MarketData of a cryptocurrency.
   * @param start The time from when on you want get the data.
   * @param limit The time to when you want to get data.
   * @param symbol The market symbol of the market you want to get the data from.
   * @param barsTimeFrame The time that should be between every data
   * @return The data from the market specified with the parameters.
   */
  List<MarketDataModel> getCryptoMarketData(ZonedDateTime start, Integer limit, String symbol, BarTimePeriod barsTimeFrame) throws Exception;
}
