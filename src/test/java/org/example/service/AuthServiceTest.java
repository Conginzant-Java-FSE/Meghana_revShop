package org.example.service;

import org.example.models.Buyer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private final AuthService service = new AuthService();

    @Test
    void registerBuyerTest(){

        Buyer b = service.registerBuyerReturnBuyer(
                "JUnit Buyer",
                "junit" + System.currentTimeMillis() + "@mail.com",
                "123",
                "999"
        );

        assertNotNull(b);
        assertTrue(b.getBuyerId() > 0);
    }
}
