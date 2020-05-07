package pl.marcin.it.springapplication.model.rates.response;

import pl.marcin.it.springapplication.model.rates.Error;

import java.util.Map;

public class CurrencyResponse {
    private boolean success;
    private Map<String, String> symbols;
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

    public Map<String, String> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, String> symbols) {
        this.symbols = symbols;
    }
}