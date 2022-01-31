import Interfaces.IMarketData;
import Model.MarketDataModel;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.crypto.common.enums.Exchange;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.crypto.historical.bar.CryptoBar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.crypto.historical.bar.CryptoBarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBarsResponse;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarAdjustment;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.enums.BarFeed;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Returns the demanded Data from the market.
 */
public class MarketData implements IMarketData {
    private AlpacaAPI alpacaAPI;
    public MarketData(AlpacaAPI alpacaAPI){
        this.alpacaAPI = alpacaAPI;
    }
    /**
     * Gets the MarketData.
     * @param start The time from when on you want get the data.
     * @param end The time to when you want to get data.
     * @param symbol The market symbol of the market you want to get the data from.
     * @param barsTimeFrame The time that should be between every data
     * @return The data from the market specified with the parameters.
     */
    public List<MarketDataModel> getMarketData(ZonedDateTime start, ZonedDateTime end, String symbol, BarTimePeriod barsTimeFrame) throws Exception {
        List<MarketDataModel> marketData = new ArrayList<MarketDataModel>();
        try {
          StockBarsResponse stockBarsResponse = alpacaAPI.stockMarketData().getBars(symbol, start, end, null,
            null,1, barsTimeFrame, BarAdjustment.SPLIT, BarFeed.IEX);
          var bars = stockBarsResponse.getBars();
            if(bars.equals(null)){
                throw new Exception("Couldn't get Marketdata.");
            }

            for (StockBar bar : bars) {
                MarketDataModel tempData = new MarketDataModel();
                tempData.setDate(bar.getTimestamp());
                tempData.setOpenPrice(bar.getOpen());
                tempData.setClosePrice(bar.getClose());
                marketData.add(tempData);
            }

        } catch (AlpacaClientException e) {
            e.printStackTrace();
        }
        return marketData;
    }

  /**
   * Gets the MarketData of a cryptocurrency.
   * @param start The time from when on you want get the data.
   * @param limit The time to when you want to get data.
   * @param symbol The market symbol of the market you want to get the data from.
   * @param barsTimeFrame The time that should be between every data
   * @return The data from the market specified with the parameters.
   */
  public List<MarketDataModel> getCryptoMarketData(ZonedDateTime start, Integer limit, String symbol, BarTimePeriod barsTimeFrame) throws Exception {
    List<MarketDataModel> marketData = new ArrayList<MarketDataModel>();
    try {
      CryptoBarsResponse btcBarsResponse = alpacaAPI.cryptoMarketData().getBars(symbol, Collections.singleton(Exchange.ERISX), start, limit, null,
        1, barsTimeFrame);
      var bars = btcBarsResponse.getBars();
      if(bars == null){
        throw new Exception("Couldn't get Marketdata.");
      }

      for (CryptoBar bar : bars){
        MarketDataModel tempData = new MarketDataModel();
        tempData.setDate(bar.getTimestamp());
        tempData.setOpenPrice(bar.getOpen());
        tempData.setClosePrice(bar.getClose());
        marketData.add(tempData);
      }

    } catch (AlpacaClientException e) {
      e.printStackTrace();
    }
    return marketData;
  }
}
