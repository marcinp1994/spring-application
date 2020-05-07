package pl.marcin.it.springapplication.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@Service
public class URLBuilderUtil {
    @Value("${fixer.io.access.key}")
    private String accessKey;
    @Value("${fixer.io.base.url}")
    private String baseURL;

    private final RestTemplate restTemplate;

    public URLBuilderUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> getDataFromFixerApi(String url, Class<T> clazz){
        return restTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(url)
                        .build()
                        .toUri(), clazz);
    }

    public String buildURL(String fromCurrency, String toCurrency, BigDecimal amount, List<String> currenciesList, String method) {
        switch(method){
            case "symbols":
                return baseURL + "symbols?access_key=" + accessKey;
            case "convert":
                return baseURL + "convert?access_key=" + accessKey +
                        "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount;
            case "latest":
                if(currenciesList != null) {
                    return baseURL + "latest?access_key=" + accessKey +
                            "&base=" + fromCurrency + "&symbols=" +
                            String.join(",", currenciesList);
                } else {
                    return baseURL + "latest?access_key=" + accessKey +
                            "&base=" + fromCurrency;
                }
        }
        return null;
    }
}
