import Interfaces.IMarketData;
import net.jacobpeterson.alpaca.AlpacaAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.baeldung.constructordi")
public class Setup {
    AlpacaAPI alpacaAPI = new AlpacaAPI();
    IMarketData marketData = new MarketData(alpacaAPI());

    @Bean
    public AlpacaAPI alpacaAPI(){
        return alpacaAPI;
    }

    @Bean
    public IMarketData marketData(){
        return marketData;
    }
}
