package pl.marcin.it.springapplication.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.Currency;
import pl.marcin.it.springapplication.model.rates.response.CurrencyResponse;
import pl.marcin.it.springapplication.utils.URLBuilderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {

    private final URLBuilderUtil urlBuilderUtil;

    public CurrencyService(URLBuilderUtil urlBuilderUtil) {
        this.urlBuilderUtil = urlBuilderUtil;
    }

    public List<Currency> getAllSupportedCurrencies(){
        String url = urlBuilderUtil.buildURL(null, null, null, null, "symbols");
        ResponseEntity<CurrencyResponse> currencyResponse = urlBuilderUtil.getDataFromFixerApi(url, CurrencyResponse.class);
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
