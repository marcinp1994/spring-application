package pl.marcin.it.springapplication.model.country;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse {
    private List<String> messages;
    private Result result;
    private String sayHello;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getSayHello() {
        return sayHello;
    }

    public void setSayHello(String sayHello) {
        this.sayHello = sayHello;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "messages=" + messages +
                '}';
    }
}
