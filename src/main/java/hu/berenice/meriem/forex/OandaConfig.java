package hu.berenice.meriem.forex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * One RestClient that already knows the OANDA base URL and the API token.
 * Every OANDA call (yours and your partner's) can inject this same bean.
 */
@Configuration
public class OandaConfig {

    @Bean
    public RestClient oandaRestClient(RestClient.Builder builder,
                                      @Value("${oanda.base-url}") String baseUrl,
                                      @Value("${oanda.token}") String token) {
        return builder
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }
}
