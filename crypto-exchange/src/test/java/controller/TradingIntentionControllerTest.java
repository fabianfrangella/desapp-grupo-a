package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.OperationTypeDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseListDTO;
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

import java.util.List;

import static controller.builder.TradingIntentionBuilder.aTradingIntention;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TradingIntentionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void publishTradingIntention() throws Exception {
        var mappedResponse = aTradingIntention(mockMvc, objectMapper);

        Assertions.assertNotNull(mappedResponse);
        Assertions.assertEquals("ACTIVE", mappedResponse.getStatus());
        Assertions.assertEquals(1L, mappedResponse.getQuantity());
        Assertions.assertEquals(OperationTypeDTO.PURCHASE, mappedResponse.getOperation());
    }

    @Test
    public void getAllTradingIntentions() throws Exception {
        aTradingIntention(mockMvc, objectMapper);

        var intentions = mockMvc.perform(MockMvcRequestBuilders.get("/intention/find")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        List<TradingIntentionResponseListDTO> dtos = objectMapper.readValue(
                intentions,
                objectMapper.getTypeFactory().constructCollectionType(List.class, TradingIntentionResponseListDTO.class)
        );

        Assertions.assertNotNull(dtos);
        Assertions.assertEquals(1, dtos.size());
    }

    @Test
    public void getTradingIntentionById() throws Exception {
        aTradingIntention(mockMvc, objectMapper);

        var intention = mockMvc.perform(MockMvcRequestBuilders.get("/intention/find/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        TradingIntentionResponseDTO dto = objectMapper.readValue(intention, TradingIntentionResponseDTO.class);

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getId());
        Assertions.assertEquals(1L, dto.getQuantity());
        Assertions.assertEquals(OperationTypeDTO.PURCHASE, dto.getOperation());
    }


}
