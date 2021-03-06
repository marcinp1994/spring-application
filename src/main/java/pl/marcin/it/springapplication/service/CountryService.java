package pl.marcin.it.springapplication.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.marcin.it.springapplication.model.country.RestResponse;
import pl.marcin.it.springapplication.model.country.RootObject;

import java.util.Locale;
import java.util.Optional;

@Service
public class CountryService {
    private static Logger LOGGER = LogManager.getLogger(CountryService.class);
    private final static String COUNTRY_CODE_URL = "http://www.groupkt.com/country/get/iso2code/";
    private final RestTemplate restTemplate;
    private final MessageSource messageSource;

    public CountryService(RestTemplate restTemplate, MessageSource messageSource) {
        this.restTemplate = restTemplate;
        this.messageSource = messageSource;
    }

    public Optional<RestResponse> getCountryFromRestApi(String code){
        ResponseEntity<RootObject> rootResponse = getDataFromApi(code);
        if(rootResponse.getStatusCode().is2xxSuccessful() && rootResponse.getBody() != null){
            RestResponse response = rootResponse.getBody().getRestResponse();
            response.setSayHello(messageSource.getMessage("hello.message",null, Locale.forLanguageTag(code)));
            LOGGER.info("ISO codes for country ['" + code + "'] were found");
            return Optional.of(response);
        }
        LOGGER.error("ISO codes for country ['" + code + "'] were not found");
        return Optional.empty();
    }

    private ResponseEntity<RootObject> getDataFromApi(String code) {
        return restTemplate.getForEntity(
                UriComponentsBuilder.fromHttpUrl(COUNTRY_CODE_URL + code)
                        .build()
                        .toUri(), RootObject.class);
    }
}
