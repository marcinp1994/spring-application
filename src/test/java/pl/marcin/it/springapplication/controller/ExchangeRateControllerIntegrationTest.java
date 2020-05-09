package pl.marcin.it.springapplication.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class ExchangeRateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    void shouldReturnOkStatusAndExchangeRateViewWithAllExchangeRates() throws Exception {
        //given
        String baseCurrency = "EUR";

        //when
        MvcResult mvcResult = mockMvc.perform(get("/rates/view/{baseCurrency}", baseCurrency)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(view().name(equalTo("exchange-rate-view"))).andReturn();

        //then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    void shouldReturnOkStatusAndExchangeRateViewForPLNandUSD() throws Exception {
        //given
        String baseCurrency = "EUR";

        //when
        MvcResult mvcResult = mockMvc.perform(get("/rates/view/{baseCurrency}", baseCurrency)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("currencies", "PLN,USD"))
                .andExpect(status().isOk())
                .andExpect(view().name(equalTo("exchange-rate-view")))
                .andReturn();

        //then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void shouldReturn401UnauthorizedForUnloggedUser() throws Exception {
        //when
        MvcResult mvcResult = mockMvc.perform(get("/rates/convert")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized()).andReturn();

        //then
        assertEquals("Unauthorized", mvcResult.getResponse().getErrorMessage());
    }

}