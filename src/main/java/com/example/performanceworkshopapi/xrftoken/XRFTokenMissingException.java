package com.example.performanceworkshopapi.xrftoken;

public class XRFTokenMissingException extends RuntimeException {
    public XRFTokenMissingException() {
        super("Unauthorised");
    }
}
