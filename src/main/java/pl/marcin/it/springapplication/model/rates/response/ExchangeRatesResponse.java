package pl.marcin.it.springapplication.model.rates.response;


import pl.marcin.it.springapplication.model.rates.Error;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class ExchangeRatesResponse {
    private boolean success;
    private String base;
    private Date date;
    private Map<String, BigDecimal> rates;
    private Error error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }
}
