package controller.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class TransactionBuilder {

    public static TransactionResponseDTO aTransaction(MockMvc mockMvc, ObjectMapper objectMapper) throws Exception {
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/intention/1/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, TransactionResponseDTO.class);
    }
}
