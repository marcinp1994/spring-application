package pl.marcin.it.springapplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.marcin.it.springapplication.exception.CountryApiFoundException;
import pl.marcin.it.springapplication.model.country.RestResponse;
import pl.marcin.it.springapplication.service.CountryService;

import java.util.Optional;

@RestController
public class CountryController {

    CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/country/{code}")
    public RestResponse getCountryByCode(@PathVariable String code){
        Optional<RestResponse> countryResponse = countryService.getCountryFromRestApi(code);
        return countryResponse.orElseThrow(
                () -> new CountryApiFoundException("System encountered a problem while processing the country request. Please try again later!"));
    }
}
