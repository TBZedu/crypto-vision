import Configurations.Default.Strategy;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.account.Account;
import net.jacobpeterson.alpaca.model.endpoint.clock.Clock;
import net.jacobpeterson.alpaca.model.endpoint.orders.enums.OrderSide;
import net.jacobpeterson.alpaca.model.endpoint.positions.Position;
import net.jacobpeterson.alpaca.rest.AlpacaClientException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import Configuration.TradingConfiguration;

import java.lang.module.Configuration;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TradingbotAPITest {
    @Mock private AlpacaAPI alpacaAPI;
    @Mock private Clock clock;
    @Mock private Account account;
    private ArrayList<Position> position;

    @Before
    public void setup() throws AlpacaClientException {
        alpacaAPI = mock(AlpacaAPI.class);
        clock = mock(Clock.class);
        account = mock(Account.class);
        position = new ArrayList<Position>();
        position.add(new Position("1", "test", "", "", "", "", "long", "", "", "", "", "", "", "", "", ""));
        when(alpacaAPI.positions().get()).thenReturn(position);
        when(alpacaAPI.clock().get()).thenReturn(clock);
        when(clock.getIsOpen()).thenReturn(true);
        when(alpacaAPI.account().get()).thenReturn(account);
        when(account.getCash()).thenReturn("100000");
    }


    @Test
    public void IsMarektOnlineTest(){
        // Setup
        TradingConfiguration configuration = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPI = new TradingbotAPI(configuration, alpacaAPI);

        // Execute
        boolean isMarketOnline = tradingbotAPI.IsMarketOnline();

        // Verify
        Assert.assertTrue(isMarketOnline);
    }

    @Test
    public void IsOrderSetTest(){
        // Setup
        TradingConfiguration configurationTest = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPITest = new TradingbotAPI(configurationTest, alpacaAPI);

        TradingConfiguration configurationOffline = new TradingConfiguration("offline", new Strategy(), 100);
        TradingbotAPI tradingbotAPIOffline = new TradingbotAPI(configurationOffline, alpacaAPI);

        // Execute
        boolean isOrderSetTest = tradingbotAPITest.IsOrderSet();
        boolean isOrderSetTestBuy = tradingbotAPITest.IsOrderSet(OrderSide.BUY);
        boolean isOrderSetTestSell = tradingbotAPITest.IsOrderSet(OrderSide.SELL);
        boolean isOrderSetOffline = tradingbotAPIOffline.IsOrderSet();
        boolean isOrderSetOfflineBuy = tradingbotAPIOffline.IsOrderSet(OrderSide.BUY);
        boolean isOrderSetOfflineSell = tradingbotAPIOffline.IsOrderSet(OrderSide.SELL);

        // Verify
        Assert.assertTrue(isOrderSetTest);
        Assert.assertTrue(isOrderSetTestBuy);
        Assert.assertFalse(isOrderSetTestSell);
        Assert.assertFalse(isOrderSetOffline);
        Assert.assertFalse(isOrderSetOfflineBuy);
        Assert.assertFalse(isOrderSetOfflineSell);
    }

    @Test
    public void closeAllOrdersTest() throws AlpacaClientException {
        // Setup
        TradingConfiguration configuration = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPI = new TradingbotAPI(configuration, alpacaAPI);

        // Execute
        tradingbotAPI.closeAllOrders();

        // Verify
        verify(alpacaAPI, times(1)).positions().closeAll(true);
    }

    @Test
    public void closeOrderTest_with_Order_set() throws AlpacaClientException {
        // Setup
        TradingConfiguration configuration = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPI = new TradingbotAPI(configuration, alpacaAPI);

        // Execute
        tradingbotAPI.closeOrder();

        // Verify
        verify(alpacaAPI, times(1)).positions().close(configuration.getSymbol(), null, 100.0);
    }

    @Test
    public void closeOrderTest_without_Order_set() throws AlpacaClientException {
        // Setup
        TradingConfiguration configurationOffline = new TradingConfiguration("offline", new Strategy(), 100);
        TradingbotAPI tradingbotAPIOffline = new TradingbotAPI(configurationOffline, alpacaAPI);

        // Execute
        tradingbotAPIOffline.closeOrder();

        // Verify
        verify(alpacaAPI, times(0)).positions().close(anyString(), null, 100.0);
    }


    @Test
    public void setOrderTest_other_OrderSide_saved() throws AlpacaClientException {
        // Setup
        TradingConfiguration configuration = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPI = new TradingbotAPI(configuration, alpacaAPI);


        // Execute
        tradingbotAPI.setOrder(OrderSide.SELL, 20.0);

        // Verify
        verify(alpacaAPI, times(1)).positions().close(configuration.getSymbol(), null, 100.0);
        verify(alpacaAPI, times(1)).orders().requestOrder(configuration.getSymbol(), (double) (100000 / 100 * configuration.getRiskFactor() / 20), null, OrderSide.SELL, configuration.getStrategy().getOrderType(), configuration.getStrategy().getOrderTimeInForce(), configuration.getStrategy().getLimitPrice(), configuration.getStrategy().getStopPrice(), configuration.getStrategy().getTrailPrice(), configuration.getStrategy().getTrailPercent(), configuration.getStrategy().getExtendedHours(), configuration.getStrategy().getClientOrderID(), configuration.getStrategy().getOrderClass(), configuration.getStrategy().getTakeProfitLimitPrice(), configuration.getStrategy().getStopLossStopPrice(), configuration.getStrategy().getStopLossLimitPrice());
    }

    @Test
    public void setOrderTest_same_OrderSide_saved() throws AlpacaClientException {
        // Setup
        TradingConfiguration configuration = new TradingConfiguration("test", new Strategy(), 100);
        TradingbotAPI tradingbotAPI = new TradingbotAPI(configuration, alpacaAPI);


        // Execute
        tradingbotAPI.setOrder(OrderSide.BUY, 20.0);

        // Verify
        verify(alpacaAPI, times(0)).positions().close(configuration.getSymbol(), null, 100.0);
        verify(alpacaAPI, times(0)).orders().requestOrder(configuration.getSymbol(), (double) 100000 / 100 * configuration.getRiskFactor() / 20, null, OrderSide.SELL, configuration.getStrategy().getOrderType(), configuration.getStrategy().getOrderTimeInForce(), configuration.getStrategy().getLimitPrice(), configuration.getStrategy().getStopPrice(), configuration.getStrategy().getTrailPrice(), configuration.getStrategy().getTrailPercent(), configuration.getStrategy().getExtendedHours(), configuration.getStrategy().getClientOrderID(), configuration.getStrategy().getOrderClass(), configuration.getStrategy().getTakeProfitLimitPrice(), configuration.getStrategy().getStopLossStopPrice(), configuration.getStrategy().getStopLossLimitPrice());
    }
}
