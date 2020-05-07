package pl.marcin.it.springapplication.model.rates.response;

import pl.marcin.it.springapplication.model.rates.Error;

import java.math.BigDecimal;

public class ExchangeRatesConvertResponse {
    private boolean success;
    private BigDecimal result;
    private Error error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
