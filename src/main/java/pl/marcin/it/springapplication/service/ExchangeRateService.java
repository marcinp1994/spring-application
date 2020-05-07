package pl.marcin.it.springapplication.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesConvertResponse;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesResponse;
import pl.marcin.it.springapplication.utils.URLBuilderUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateService {

    private final URLBuilderUtil urlBuilderUtil;

    public ExchangeRateService(URLBuilderUtil urlBuilderUtil) {
        this.urlBuilderUtil = urlBuilderUtil;
    }

    public Optional<ExchangeRatesResponse> getAllExchangeRatesForGivenCurrency(String currency){
        String url = urlBuilderUtil.buildURL(currency, null, null, null, "latest");
        return getExchangeRatesResponse(url);
    }

    private Optional<ExchangeRatesResponse> getExchangeRatesResponse(String url) {
        ResponseEntity<ExchangeRatesResponse> rootResponse = urlBuilderUtil.getDataFromFixerApi(url, ExchangeRatesResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful()){
            if(rootResponse.getBody() != null && !rootResponse.getBody().isSuccess()){
                throw new FixerApiFoundException(rootResponse.getBody().getError().getInfo());
            }
            return Optional.ofNullable(rootResponse.getBody());
        }
        return Optional.empty();
    }

    public Optional<ExchangeRatesResponse> geSpecificExchangeRatesForGivenCurrencies(String currency, List<String> currencies){
        String url = urlBuilderUtil.buildURL(currency, null, null, currencies, "latest");
        return getExchangeRatesResponse(url);
    }

    public Optional<BigDecimal> getConvertedAmount(String fromCurrency, String toCurrency, BigDecimal amount){
        String url = urlBuilderUtil.buildURL(fromCurrency, toCurrency, amount, null, "latest");
        ResponseEntity<ExchangeRatesResponse> rootResponse = urlBuilderUtil.getDataFromFixerApi(url, ExchangeRatesResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful() && rootResponse.getBody() != null){
            if(!rootResponse.getBody().isSuccess()){
                throw new FixerApiFoundException(rootResponse.getBody().getError().getInfo());
            }
            BigDecimal exchangeRate = rootResponse.getBody().getRates().get(toCurrency);
            return Optional.ofNullable(amount.multiply(exchangeRate));
        }
        return Optional.empty();
    }

    public Optional<BigDecimal> getConvertedAmountFromFixer(String fromCurrency, String toCurrency, BigDecimal amount){
        String url = urlBuilderUtil.buildURL(fromCurrency, toCurrency, amount, null, "convert");
        ResponseEntity<ExchangeRatesConvertResponse> rootResponse = urlBuilderUtil.getDataFromFixerApi(url, ExchangeRatesConvertResponse.class);
        if(rootResponse.getStatusCode().is2xxSuccessful() && rootResponse.getBody() != null){
            if(!rootResponse.getBody().isSuccess()){
                throw new FixerApiFoundException(rootResponse.getBody().getError().getInfo());
            }
            BigDecimal exchangeRate = rootResponse.getBody().getResult();
            return Optional.ofNullable(exchangeRate);
        }
        return Optional.empty();
    }

}
