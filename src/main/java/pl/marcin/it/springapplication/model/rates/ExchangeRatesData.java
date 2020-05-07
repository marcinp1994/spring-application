package pl.marcin.it.springapplication.model.rates;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ExchangeRatesData {

    @NotNull(message = "Currency From can not be null!")
    private String fromCurrency;

    @NotNull(message = "Currency To can not be null!")
    private String toCurrency;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date exchangeRateDate;

    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Date getExchangeRateDate() {
        return exchangeRateDate;
    }

    public void setExchangeRateDate(Date exchangeRateDate) {
        this.exchangeRateDate = exchangeRateDate;
    }
}
