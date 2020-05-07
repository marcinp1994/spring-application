package pl.marcin.it.springapplication.exception;

public class CountryApiFoundException extends  RuntimeException{
    public CountryApiFoundException(String message){
        super(message);
    }
}
