package pl.marcin.it.springapplication.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.marcin.it.springapplication.exception.FixerApiFoundException;
import pl.marcin.it.springapplication.exception.TokenNotFoundException;
import pl.marcin.it.springapplication.exception.UserAlreadyExistException;
import pl.marcin.it.springapplication.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.Date;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FixerApiFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleFixerApiFoundException(FixerApiFoundException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleTokenNotFoundException(TokenNotFoundException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public final ResponseEntity<ExceptionResponse> handleUserAlreadyExistException(UserAlreadyExistException ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleDefaultException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<String> errors = new ArrayList<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> errors.add(errors.size() + 1 + ": " + e.getDefaultMessage()));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Validation failed", errors.toString());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
