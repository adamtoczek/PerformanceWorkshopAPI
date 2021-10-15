package com.example.performanceworkshopapi.xrfToken;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class XRFTokenMissingAdvice {
    @ResponseBody
    @ExceptionHandler(XRFTokenMissingException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String xrfTokenForbidenHandler(XRFTokenMissingException ex) {
        return ex.getMessage();
    }
}
