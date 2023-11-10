package com.ElectroMarket.orderservice.advice;

import com.ElectroMarket.orderservice.exceptions.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderControllerAdvice {
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String orderAlreadyExists(ResourceAlreadyExistsException e) {
        return e.getMessage();
    }
}
