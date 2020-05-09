package pl.marcin.it.springapplication.service;


import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.Error;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesResponse;
import pl.marcin.it.springapplication.utils.RestTemplateUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExchangeRateServiceTest {

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @Mock
    private RestTemplateUtil restTemplateUtil;

    @Test
    void shouldCorrectlyConvertAmountFromEURtoPLN(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "PLN";
        BigDecimal amount = BigDecimal.valueOf(2);
        BigDecimal expectedConvertedAmount = BigDecimal.valueOf(4);
        ExchangeRatesResponse exchangeRatesResponse = new ExchangeRatesResponse();
        Map<String, BigDecimal> ratesMap = Collections.singletonMap(toCurrency,BigDecimal.valueOf(2));
        exchangeRatesResponse.setRates(ratesMap);
        exchangeRatesResponse.setSuccess(true);

        when(restTemplateUtil.getDataFromFixerApi(any(), eq(ExchangeRatesResponse.class)))
                .thenReturn(new ResponseEntity<>(exchangeRatesResponse, HttpStatus.OK));

        //when
        Optional<BigDecimal> convertedAmount = exchangeRateService.getConvertedAmount(fromCurrency, toCurrency, amount);

        //then
        assertTrue(convertedAmount.isPresent());
        assertEquals(expectedConvertedAmount, convertedAmount.get());
    }

    @Test
    void shouldReturnEmptyOptionalDueToExceptionOnFixerApiSide(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "PLN";
        BigDecimal amount = BigDecimal.valueOf(2.0);
        ExchangeRatesResponse exchangeRatesResponse = new ExchangeRatesResponse();
        Map<String, BigDecimal> ratesMap = Collections.singletonMap(toCurrency,BigDecimal.valueOf(2.0));
        exchangeRatesResponse.setRates(ratesMap);
        exchangeRatesResponse.setSuccess(true);

        when(restTemplateUtil.getDataFromFixerApi(any(), eq(ExchangeRatesResponse.class)))
                .thenReturn(new ResponseEntity<>(exchangeRatesResponse, HttpStatus.INTERNAL_SERVER_ERROR));

        //when
        Optional<BigDecimal> convertedAmount = exchangeRateService.getConvertedAmount(fromCurrency, toCurrency, amount);

        //then
        assertFalse(convertedAmount.isPresent());
    }

    @Test
    void shouldThrowsFixerApiFoundExceptionDueToBadRequest(){
        //given
        String fromCurrency = "EUR";
        String toCurrency = "PLN";
        BigDecimal amount = BigDecimal.valueOf(2.0);
        ExchangeRatesResponse exchangeRatesResponse = new ExchangeRatesResponse();
        Map<String, BigDecimal> ratesMap = Collections.emptyMap();
        exchangeRatesResponse.setRates(ratesMap);
        exchangeRatesResponse.setSuccess(false);
        Error error = new Error();
        error.setInfo("Bad Request!");
        exchangeRatesResponse.setError(error);

        when(restTemplateUtil.getDataFromFixerApi(any(), eq(ExchangeRatesResponse.class)))
                .thenReturn(new ResponseEntity<>(exchangeRatesResponse, HttpStatus.OK));

        //when&then
        assertThrows(FixerApiFoundException.class, () -> exchangeRateService.getConvertedAmount(fromCurrency, toCurrency, amount));
    }
}