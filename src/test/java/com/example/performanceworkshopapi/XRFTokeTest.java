package com.example.performanceworkshopapi;

import com.example.performanceworkshopapi.xrftoken.XRFToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XRFTokeTest {
    @Test
    public void tokensShouldBeEquals() {
        XRFToken t1 = new XRFToken("123456");
        XRFToken t2 = new XRFToken("123456");

        assertEquals(t1, t2);
    }


}
