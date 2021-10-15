package com.example.performanceworkshopapi.xrftoken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XRFTokenController {

    private final XRFTokenRepository tokenRepo;

    XRFTokenController(XRFTokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    @PostMapping("/xrf-token")
    public XRFToken newXRFToken(XRFTokenPOJO newXRFTokenPOJO) {
        XRFToken newXRFToken = new XRFToken();
        return tokenRepo.save(newXRFToken);
    }
}
