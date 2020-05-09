package pl.marcin.it.springapplication.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.ExchangeRatesData;
import pl.marcin.it.springapplication.model.rates.response.ExchangeRatesResponse;
import pl.marcin.it.springapplication.service.CurrencyService;
import pl.marcin.it.springapplication.service.ExchangeRateService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rates")
public class ExchangeRateController {
    private static final Logger LOGGER = LogManager.getLogger(ExchangeRateController.class);
    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;

    public ExchangeRateController(ExchangeRateService exchangeRateService, CurrencyService currencyService) {
        this.exchangeRateService = exchangeRateService;
        this.currencyService = currencyService;
    }

    @GetMapping("/convert")
    public String convertCurrency(Model model){
        model.addAttribute("currenciesList", currencyService.getAllSupportedCurrencies());
        model.addAttribute("exchangeRatesData", new ExchangeRatesData());
        return "convert-amount";
    }

    @PostMapping("/convert")
    public String convertCurrency(ExchangeRatesData exchangeRatesData,
                                     Model model) {
        BigDecimal amount = new BigDecimal(exchangeRatesData.getAmount());
        String fromCurrency = exchangeRatesData.getFromCurrency();
        String toCurrency = exchangeRatesData.getToCurrency();
        Optional<BigDecimal> convertedAmount = exchangeRateService.getConvertedAmount(fromCurrency, toCurrency, amount);
        if(!convertedAmount.isPresent()){
            LOGGER.error("The system has not converted the amount!");
            throw new FixerApiFoundException("System encountered a problem when converting the amount. Please try again later!");
        }

        model.addAttribute("currenciesList", currencyService.getAllSupportedCurrencies());
        model.addAttribute("exchangeRatesData", exchangeRatesData);
        model.addAttribute("convertedAmount", convertedAmount.get());
        LOGGER.info("The amount has been correctly converted from=" + amount + " to=" +convertedAmount.get());
        return "convert-amount";
    }

    @GetMapping(value = "/view/{baseCurrency}")
    public String getAllExchangeRatesForGivenCurrency(@PathVariable String baseCurrency, Model model) {
        Optional<ExchangeRatesResponse> response = exchangeRateService.getAllExchangeRatesForGivenCurrency(baseCurrency);
        final ExchangeRatesResponse exchangeRatesResponse = response.orElseThrow(() -> new FixerApiFoundException("System encountered a problem while processing the request. Please try again later!"));
        feedModel(model, exchangeRatesResponse, baseCurrency);

        return "exchange-rate-view";
    }

    @GetMapping(value="/view/{baseCurrency}", params = "currencies")
    public String geSpecificExchangeRatesForGivenCurrencies(@PathVariable String baseCurrency,
                                                            @RequestParam List<String> currencies,
                                                            Model model){
        Optional<ExchangeRatesResponse> response = exchangeRateService.geSpecificExchangeRatesForGivenCurrencies(baseCurrency, currencies);
        final ExchangeRatesResponse exchangeRatesResponse = response.orElseThrow(() -> new FixerApiFoundException("System encountered a problem while processing the request. Please try again later!"));
        feedModel(model, exchangeRatesResponse, baseCurrency);

        return "exchange-rate-view";
    }

    private void feedModel(Model model, ExchangeRatesResponse exchangeRatesResponse, String baseCurrency){
        model.addAttribute("baseCurrency", baseCurrency);
        model.addAttribute("ratesMap", exchangeRatesResponse.getRates());
    }

}
