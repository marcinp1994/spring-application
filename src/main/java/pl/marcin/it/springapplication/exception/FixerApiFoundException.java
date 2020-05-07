package pl.marcin.it.springapplication.exception;

public class FixerApiFoundException extends  RuntimeException{
    public FixerApiFoundException(String message){
        super(message);
    }
}
