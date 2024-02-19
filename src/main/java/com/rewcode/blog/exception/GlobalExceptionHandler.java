package com.rewcode.blog.exception;

import com.rewcode.blog.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler{

    //Generic Exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception e, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                e.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Specific Exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException e, WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                e.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    //Specific Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException e, WebRequest webRequest){

        Map<String, String> errors = new HashMap<>();

        e.getAllErrors().forEach((error -> {
            String fieldName = ((FieldError)error).getField();
            String fieldMessage = error.getDefaultMessage();
            errors.put(fieldName, fieldMessage);
        }));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
