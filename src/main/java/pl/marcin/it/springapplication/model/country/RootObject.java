package pl.marcin.it.springapplication.model.country;


import com.fasterxml.jackson.annotation.JsonProperty;

public class RootObject {
    @JsonProperty("RestResponse")
    private RestResponse restResponse;

    public RestResponse getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(RestResponse restResponse) {
        this.restResponse = restResponse;
    }
}
