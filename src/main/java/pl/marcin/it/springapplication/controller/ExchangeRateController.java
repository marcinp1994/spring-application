package pl.marcin.it.springapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.model.rates.Currency;
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

    private final ExchangeRateService exchangeRateService;
    private final List<Currency> currenciesList;

    public ExchangeRateController(ExchangeRateService exchangeRateService, CurrencyService currencyService) {
        this.exchangeRateService = exchangeRateService;
        currenciesList = currencyService.getAllSupportedCurrencies();
    }

    @GetMapping("/convert")
    public String getExchangeRates(Model model){
        model.addAttribute("currenciesList", currenciesList);
        model.addAttribute("exchangeRatesData", new ExchangeRatesData());
        return "convert-amount";
    }

    @PostMapping("/convert")
    public String queryExchangeRates(ExchangeRatesData exchangeRatesData,
                                     Model model) {
        BigDecimal amount = new BigDecimal(exchangeRatesData.getAmount());
        String fromCurrency = exchangeRatesData.getFromCurrency();
        String toCurrency = exchangeRatesData.getToCurrency();
        Optional<BigDecimal> convertedAmount = exchangeRateService.getConvertedAmount(fromCurrency, toCurrency, amount);
        if(!convertedAmount.isPresent()){
            throw new FixerApiFoundException("System encountered a problem when converting the amount. Please try again later!");
        }

        model.addAttribute("currenciesList", currenciesList);
        model.addAttribute("exchangeRatesData", exchangeRatesData);
        model.addAttribute("convertedAmount", convertedAmount.get());
        return "convert-amount";
    }

    @GetMapping(value = "/view/{baseCurrency}")
    public String getAllExchangeRatesForGivenCurrency(@PathVariable String baseCurrency, Model model) {
        Optional<ExchangeRatesResponse> response = exchangeRateService.getAllExchangeRatesForGivenCurrency(baseCurrency);
        final ExchangeRatesResponse exchangeRatesResponse = response.orElseThrow(() -> new FixerApiFoundException("System encountered a problem while processing the request. Please try again later!"));
        model.addAttribute("baseCurrency", baseCurrency);
        model.addAttribute("ratesMap", exchangeRatesResponse.getRates());

        return "exchange-rate-view";
    }

    @GetMapping(value="/view/{baseCurrency}", params = "currencies")
    public String geSpecificExchangeRatesForGivenCurrencies(@PathVariable String baseCurrency,
                                                            @RequestParam List<String> currencies,
                                                            Model model){
        Optional<ExchangeRatesResponse> response = exchangeRateService.geSpecificExchangeRatesForGivenCurrencies(baseCurrency, currencies);
        final ExchangeRatesResponse exchangeRatesResponse = response.orElseThrow(() -> new FixerApiFoundException("System encountered a problem while processing the request. Please try again later!"));
        model.addAttribute("baseCurrency", baseCurrency);
        model.addAttribute("ratesMap", exchangeRatesResponse.getRates());

        return "exchange-rate-view";
    }

}
