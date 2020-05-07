package pl.marcin.it.springapplication.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.Currency;
import pl.marcin.it.springapplication.model.rates.response.CurrencyResponse;
import pl.marcin.it.springapplication.utils.RestTemplateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private final RestTemplateUtil restTemplateUtil;

    public CurrencyService(RestTemplateUtil urlBuilderUtil) {
        this.restTemplateUtil = urlBuilderUtil;
    }

    public List<Currency> getAllSupportedCurrencies(){
        String url = restTemplateUtil.buildURL(null, null, null, null, "symbols");
        ResponseEntity<CurrencyResponse> currencyResponse = restTemplateUtil.getDataFromFixerApi(url, CurrencyResponse.class);
        if(currencyResponse.getStatusCode().is2xxSuccessful()){
            if(currencyResponse.getBody() != null && currencyResponse.getBody().isSuccess()){
                Map<String, String> map = currencyResponse.getBody().getSymbols();
                List<Currency> currencyList = new ArrayList<>();
                map.forEach((k,v) -> currencyList.add(new Currency(k,v)));
                return currencyList;
            }
        }
        throw new FixerApiFoundException(currencyResponse.getBody().getError().getInfo());
    }
}
