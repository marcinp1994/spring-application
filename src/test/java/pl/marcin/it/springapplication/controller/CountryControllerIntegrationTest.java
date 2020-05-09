package pl.marcin.it.springapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.marcin.it.springapplication.model.country.RestResponse;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "user1", password = "pwd", roles = "USER")
class CountryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindProperISO2CodeForPL() throws Exception {
        //given
        String validCountry2Code = "PL";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("PL", response.getResult().getAlpha2_code());
    }

    @Test
    void shouldFindValidISO3CodeForPL() throws Exception {
        //given
        String validCountry2Code = "PL";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("POL", response.getResult().getAlpha3_code());
    }

    @Test
    void shouldFindValidCountryNameForPL() throws Exception {
        //given
        String validCountry2Code = "PL";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("Poland", response.getResult().getName());
    }

    @Test
    void shouldFindValidSayHelloMessageForPL() throws Exception {
        //given
        String validCountry2Code = "PL";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("Welcome!", response.getSayHello());
    }

    @Test
    void shouldFindValidSayHelloMessageForFR() throws Exception {
        //given
        String validCountry2Code = "FR";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("Bonjour!", response.getSayHello());
    }

    @Test
    void shouldFindValidMessageForPL() throws Exception {
        //given
        String validCountry2Code = "PL";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("Country found matching code [PL].", response.getMessages().get(0));
    }

    @Test
    void shouldNotFindISOCodesForInvalidCountryCode() throws Exception {
        //given
        String invalidCountryCode = "BAD";

        //when
        MvcResult result = mockMvc.perform(get("/country/{code}", invalidCountryCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();

        //then
        String contentAsString = result.getResponse().getContentAsString();
        RestResponse response = objectMapper.readValue(contentAsString, RestResponse.class);
        assertEquals("No matching country found for requested code [BAD].", response.getMessages().get(0));
    }

    @Test
    void shouldThrownException404ForEmptyCountryCode() throws Exception {
        //given
        String validCountry2Code = "";

        //when&then
        MvcResult result = mockMvc.perform(get("/country/{code}", validCountry2Code)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError()).andReturn();
    }

}