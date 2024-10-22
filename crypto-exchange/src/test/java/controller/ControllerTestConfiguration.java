package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.unq.crypto_exchange.api.controller.CryptoActiveController;
import com.unq.crypto_exchange.api.controller.TradingIntentionController;
import com.unq.crypto_exchange.api.controller.TransactionController;
import com.unq.crypto_exchange.api.controller.UserController;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootConfiguration
@ComponentScan(basePackages = {"com.unq.crypto_exchange"})
public class ControllerTestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public MockMvc mockMvc(TransactionController transactionController,
                           TradingIntentionController tradingIntentionController,
                           CryptoActiveController cryptoActiveController,
                           UserController userController) {
        return MockMvcBuilders.standaloneSetup(transactionController,
                tradingIntentionController,
                cryptoActiveController,
                userController).build();
    }
}