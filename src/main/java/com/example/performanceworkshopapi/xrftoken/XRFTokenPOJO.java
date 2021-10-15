package com.example.performanceworkshopapi.xrftoken;

public class XRFTokenPOJO {
    private Long id;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public XRFTokenPOJO(){

    }

    public XRFTokenPOJO(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public XRFTokenPOJO(XRFToken tk) {
        this.id = tk.getId();
        this.token = tk.getToken();
    }
}
