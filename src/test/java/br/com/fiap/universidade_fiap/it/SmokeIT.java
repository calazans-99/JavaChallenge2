package br.com.fiap.universidade_fiap.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SmokeIT {

    @Autowired
    MockMvc mvc;

    @Test
    void patiosRedirectsToLoginWhenSecured() throws Exception {
        mvc.perform(get("/patios"))
                .andExpect(status().is3xxRedirection());
    }
}
