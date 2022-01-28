import Interfaces.IMarketData;
import Model.MarketDataModel;
import com.google.common.collect.ImmutableCollection;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.enums.BarsTimeFrame;
import net.jacobpeterson.alpaca.rest.exception.AlpacaAPIRequestException;
import net.jacobpeterson.domain.alpaca.bar.Bar;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<MarketDataModel> getMarketData(ZonedDateTime start, ZonedDateTime end, String symbol, BarsTimeFrame barsTimeFrame) throws Exception {
        List<MarketDataModel> marketData = new ArrayList<MarketDataModel>();
        try {
            Map<String, ArrayList<Bar>> bars = alpacaAPI.getBars(barsTimeFrame, symbol, null, start, end,
                    null, null);
            if(bars.equals(null)){
                throw new Exception("Couldn't get Marketdata.");
            }

            for (Bar bar : bars.get(symbol)) {
                MarketDataModel tempData = new MarketDataModel();
                tempData.setDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(bar.getT()), ZoneOffset.UTC));
                tempData.setOpenPrice(bar.getO());
                tempData.setClosePrice(bar.getC());
                marketData.add(tempData);
            }

        } catch (AlpacaAPIRequestException e) {
            e.printStackTrace();
        }
        return marketData;
    }
}
