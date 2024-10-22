package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static controller.builder.TradingIntentionBuilder.aTradingIntention;
import static controller.builder.TransactionBuilder.aTransaction;
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTransaction() throws Exception {
        aTradingIntention(mockMvc, objectMapper);
        var dto = aTransaction(mockMvc, objectMapper);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(TransactionResponseDTO.TransactionStatus.PENDING, dto.getStatus());
    }

    @Test
    void confirmTransaction() throws Exception {
        aTradingIntention(mockMvc, objectMapper);
        aTransaction(mockMvc, objectMapper);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/confirm/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(response, TransactionResponseDTO.class);

        Assertions.assertEquals(TransactionResponseDTO.TransactionStatus.COMPLETED, dto.getStatus());
    }

    @Test
    void cancelTransaction() throws Exception {
        aTradingIntention(mockMvc, objectMapper);
        aTransaction(mockMvc, objectMapper);

        var response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(response, TransactionResponseDTO.class);

        Assertions.assertEquals(TransactionResponseDTO.TransactionStatus.CANCELED, dto.getStatus());
    }

}
