package com.example.performanceWorkshopAPI.xrfToken;

import org.springframework.data.domain.Example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Entity
public class XRFToken {
    private @Id @GeneratedValue Long id;
    private String token;

    public XRFToken() {
        this.token = UUID.randomUUID().toString();
    }
    public XRFToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.token);
    }

    @Override
    public String toString() {
        return this.token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof XRFToken))
            return false;
        XRFToken token = (XRFToken) o;
        return Objects.equals(this.hashCode(),token.hashCode());
    }
    public static void checkToken(String headerToken, XRFTokenRepository tokenRepo) {
        XRFToken token = new XRFToken(headerToken);
        Example<XRFToken> tokenExample = Example.of(token);
        Optional<XRFToken> tok = tokenRepo.findOne(tokenExample);
        if (!tok.isPresent())
            throw new XRFTokenMissingException();
    }
}
