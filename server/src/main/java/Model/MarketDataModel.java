package Model;

import java.time.ZonedDateTime;

/**
 *  The class MarketDataModel is a model that reflects the Data of the Market for one position.
 */
public class MarketDataModel {
    public MarketDataModel(){}
    public MarketDataModel(ZonedDateTime date, double openPrice, double closePrice){
        this.Date = date;
        this.OpenPrice = openPrice;
        this.ClosePrice = closePrice;
    }
    private ZonedDateTime Date;
    private double OpenPrice;
    private double ClosePrice;

    public ZonedDateTime getDate() {
        return Date;
    }

    public void setDate(ZonedDateTime date) {
        Date = date;
    }

    public double getOpenPrice() {
        return OpenPrice;
    }

    public void setOpenPrice(double openPrice) {
        OpenPrice = openPrice;
    }

    public double getClosePrice() {
        return ClosePrice;
    }

    public void setClosePrice(double closePrice) {
        ClosePrice = closePrice;
    }
}
