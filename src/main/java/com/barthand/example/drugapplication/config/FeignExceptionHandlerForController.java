package com.barthand.example.drugapplication.config;

import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Propagates {@link Exception}s Feign client indicated into the resulting servlet response. 
 */
@RestControllerAdvice
public class FeignExceptionHandlerForController {

    @ExceptionHandler(FeignException.class)
    public String handleFeignStatusException(FeignException e, HttpServletResponse response) {
        response.setStatus(e.status());
        return "{ \"error\": \"Forwarding HTTP client response\" }";
    }

}