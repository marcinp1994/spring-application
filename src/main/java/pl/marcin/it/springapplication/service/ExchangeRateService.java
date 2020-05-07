package pl.marcin.it.springapplication.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesConvertResponse;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesResponse;
import pl.marcin.it.springapplication.utils.RestTemplateUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ExchangeRateService {
    private final RestTemplateUtil restTemplateUtil;

    public ExchangeRateService(RestTemplateUtil restTemplateUtil) {
        this.restTemplateUtil = restTemplateUtil;
    }

    public Optional<ExchangeRatesResponse> getAllExchangeRatesForGivenCurrency(String currency){
        String url = restTemplateUtil.buildURL(currency, null, null, null, "latest");
        return getExchangeRatesResponse(url);
    }

    public Optional<ExchangeRatesResponse> geSpecificExchangeRatesForGivenCurrencies(String currency, List<String> currencies){
        String url = restTemplateUtil.buildURL(currency, null, null, currencies, "latest");
        return getExchangeRatesResponse(url);
    }

    public Optional<BigDecimal> getConvertedAmount(String fromCurrency, String toCurrency, BigDecimal amount){
        String url = restTemplateUtil.buildURL(fromCurrency, toCurrency, amount, null, "latest");
        ResponseEntity<ExchangeRatesResponse> rootResponse = restTemplateUtil.getDataFromFixerApi(url, ExchangeRatesResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful() && rootResponse.getBody() != null){
            if(!rootResponse.getBody().isSuccess()){
                throw new FixerApiFoundException(buildErrorMessage(rootResponse));
            }
            BigDecimal exchangeRate = rootResponse.getBody().getRates().get(toCurrency);
            return Optional.ofNullable(amount.multiply(exchangeRate));
        }
        return Optional.empty();
    }

    public Optional<BigDecimal> getConvertedAmountFromFixer(String fromCurrency, String toCurrency, BigDecimal amount){
        String url = restTemplateUtil.buildURL(fromCurrency, toCurrency, amount, null, "convert");
        ResponseEntity<ExchangeRatesConvertResponse> rootResponse = restTemplateUtil.getDataFromFixerApi(url, ExchangeRatesConvertResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful() && rootResponse.getBody() != null){
            if(!rootResponse.getBody().isSuccess()){
                String errMsg = "Error type : " + rootResponse.getBody().getError().getType()
                        + " - [INFO]" + rootResponse.getBody().getError().getInfo();
                throw new FixerApiFoundException(errMsg);
            }
            BigDecimal exchangeRate = rootResponse.getBody().getResult();
            return Optional.ofNullable(exchangeRate);
        }
        return Optional.empty();
    }

    private Optional<ExchangeRatesResponse> getExchangeRatesResponse(String url) {
        ResponseEntity<ExchangeRatesResponse> rootResponse = restTemplateUtil.getDataFromFixerApi(url, ExchangeRatesResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful()){
            if(rootResponse.getBody() != null && !rootResponse.getBody().isSuccess()){
                throw new FixerApiFoundException(buildErrorMessage(rootResponse));
            }
            return Optional.ofNullable(rootResponse.getBody());
        }
        return Optional.empty();
    }

    private String buildErrorMessage(ResponseEntity<ExchangeRatesResponse> rootResponse){
        if(Objects.requireNonNull(rootResponse.getBody()).getError().getInfo() != null){
            return "Error TYPE : " + rootResponse.getBody().getError().getType()
                    + " - [INFO]" + rootResponse.getBody().getError().getInfo();
        } else {
            return "Error type : " + rootResponse.getBody().getError().getType();
        }
    }

}
