package org.example.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartControllerTest {

    @Test
    void testCartControllerCreation() {

        CartController cc = new CartController();
        assertNotNull(cc);
    }
}
