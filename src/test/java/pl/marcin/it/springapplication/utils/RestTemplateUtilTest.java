package pl.marcin.it.springapplication.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class RestTemplateUtilTest {

    private static final String baseURL = "http://data.fixer.io/api/";
    private static final String accessKey = "660199deca680bdf89b9b121ed50ede8";

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Test
    public void shouldBuildProperlyURLToGetAllExchangeRates(){
        //given
        String fromCurrency = "EUR";
        String method = "latest";
        String expectedURL = baseURL + method + "?access_key=" + accessKey + "&base=" + fromCurrency;

        //when
        String url = restTemplateUtil.buildURL(fromCurrency, null , null, null , method);

        //then
        assertEquals(expectedURL, url);
    }

    @Test
    public void shouldBuildProperlyURLToGetAllSupportedCurrencies(){
        //given
        String method = "symbols";
        String expectedURL = baseURL + method + "?access_key=" + accessKey;

        //when
        String url = restTemplateUtil.buildURL(null, null , null, null , method);

        //then
        assertEquals(expectedURL, url);
    }

    @Test
    public void shouldBuildProperlyURLToGetExchangeRatesBetweenEURandPOL(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "POL";
        String method = "latest";
        String expectedURL = baseURL + method + "?access_key=" + accessKey +"&base=" + fromCurrency + "&symbols=" + toCurrency;

        //when
        String url = restTemplateUtil.buildURL(fromCurrency, null , null, Collections.singletonList(toCurrency) , method);

        //then
        assertEquals(expectedURL, url);
    }

    @Test
    public void shouldBuildProperlyURLToGetExchangeRatesBetweenEURandPOLandUSD(){
        //given
        String fromCurrency = "EUR";
        List<String> currencies = Arrays.asList("POL", "USD");
        String method = "latest";
        String expectedURL = baseURL + method + "?access_key=" + accessKey +"&base=" + fromCurrency + "&symbols=" + String.join(",", currencies);

        //when
        String url = restTemplateUtil.buildURL(fromCurrency, null , null, currencies , method);

        //then
        assertEquals(expectedURL, url);
    }

    @Test
    public void shouldBuildProperlyURLToGetConvertedAmount(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "POL";
        String method = "convert";
        BigDecimal amount = new BigDecimal(10);
        String expectedURL = baseURL + method + "?access_key=" + accessKey +
                "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;

        //when
        String url = restTemplateUtil.buildURL(fromCurrency, toCurrency , amount, null , method);

        //then
        assertEquals(expectedURL, url);
    }

    @Test
    public void shouldReturnNullForInvalidApiMethod(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "POL";
        String method = "invalid";

        //when
        String url = restTemplateUtil.buildURL(fromCurrency, toCurrency , null, null , method);

        //then
        assertNull(url);
    }

}