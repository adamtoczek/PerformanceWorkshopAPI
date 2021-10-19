package com.example.performanceworkshopapi;

import com.example.performanceworkshopapi.xrftoken.XRFToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class XRFTokeTest {
    @Test
    void tokensShouldBeEquals() {
        XRFToken t1 = new XRFToken("123456");
        XRFToken t2 = new XRFToken("123456");
        assertEquals(t1, t2);
    }

    @Test
    void tokenShouldNotEqualString(){
        XRFToken t1 = new XRFToken("123456");
        assertNotEquals(t1, "123456");
    }

    @Test
    void sameTokenShouldBeEqual(){
        XRFToken t1 = new XRFToken("123456");
        assertEquals(t1, t1);
    }

    @Test
    void setIdOnTokenShouldChangeID() {
        XRFToken token = new XRFToken();
        token.setId(789L);
        assertEquals(789L, token.getId());
    }

    @Test
    void setTokenOnTokenShouldChangeID() {
        XRFToken token = new XRFToken();
        token.setToken("456");
        assertEquals("456", token.getToken());
    }
}
