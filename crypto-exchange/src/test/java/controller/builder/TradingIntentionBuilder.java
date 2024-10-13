package controller.builder;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.OperationTypeDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseListDTO;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

public class TradingIntentionBuilder {

    public static TradingIntentionResponseListDTO aTradingIntention(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var dto = TradingIntentionDTO.builder()
                .amount(BigDecimal.valueOf(10L))
                .cryptoCurrency(CryptoCurrencyType.ALICEUSDT)
                .operationType(OperationTypeDTO.PURCHASE)
                .quantity(1L)
                .build();

        var json = objectMapper.writeValueAsString(dto);
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/intention/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, TradingIntentionResponseListDTO.class);
    }
}
