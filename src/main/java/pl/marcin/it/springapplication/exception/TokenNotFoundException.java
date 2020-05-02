package pl.marcin.it.springapplication.exception;

public class TokenNotFoundException extends  RuntimeException{
    public TokenNotFoundException(String message){
        super(message);
    }
}
