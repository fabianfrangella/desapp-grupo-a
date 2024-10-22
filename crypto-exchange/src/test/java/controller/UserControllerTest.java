package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.crypto_exchange.api.dto.RegisterUserDTO;
import com.unq.crypto_exchange.api.dto.RegisterUserResponseDTO;
import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
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
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser() throws Exception {
        var registerDto = RegisterUserDTO.fromModel(CryptoUserBuilder.aCryptoUser().build());
        var response = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(response, RegisterUserResponseDTO.class);

        Assertions.assertNotNull(dto);
    }

}
