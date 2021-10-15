package com.example.performanceworkshopapi.xrfToken;

public class XRFTokenMissingException extends RuntimeException {
    public XRFTokenMissingException() {
        super("Unauthorised");
    }
}
