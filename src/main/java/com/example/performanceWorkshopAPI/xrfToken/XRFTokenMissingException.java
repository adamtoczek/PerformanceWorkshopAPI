package com.example.performanceWorkshopAPI.xrfToken;

public class XRFTokenMissingException extends RuntimeException {
    public XRFTokenMissingException() {
        super("Unauthorised");
    }
}
