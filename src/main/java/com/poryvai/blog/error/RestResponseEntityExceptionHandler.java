package com.poryvai.blog.error;


import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({PostNotFoundException.class, EntityNotFoundException.class})
    protected ResponseEntity<Object> postNotFoundException(PostNotFoundException exception, WebRequest request){
        ErrorMessage message = new ErrorMessage(NOT_FOUND,"Post Not Found Exception", exception.getMessage() );

        return ResponseEntity.status(NOT_FOUND).body(message);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        ErrorMessage message = new ErrorMessage(BAD_REQUEST,"Malformed JSON Request", ex.getMessage() );

        return ResponseEntity.status(BAD_REQUEST).body(message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,HttpStatus status,
                                                                      WebRequest request) {
        ErrorMessage message = new ErrorMessage();
        message.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        message.setDebugMessage(ex.getMessage());

        return new ResponseEntity<>(message, status);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(new ErrorMessage(NOT_FOUND,"No Handler Found", ex.getMessage()), status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request, HttpStatus status) {
        ErrorMessage message = new ErrorMessage(INTERNAL_SERVER_ERROR,"Internal Exception", ex.getMessage());
        return new ResponseEntity<>(message, status);
    }
}
