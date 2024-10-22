package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.OperatedCryptoDTO;
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

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CryptoActiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findOperatedCryptoBetweenTwoDates() throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.get("/cryptoactive/operated/1?from=2024-10-21&to=2024-10-22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(response, OperatedCryptoDTO.class);

        Assertions.assertEquals(0, dto.cryptos().size());
    }
}
