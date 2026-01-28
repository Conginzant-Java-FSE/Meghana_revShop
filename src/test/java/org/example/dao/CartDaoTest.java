package org.example.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CartDaoTest {

    CartDao dao = new CartDao();

    @Test
    void createCartTest() {
        int id = dao.getOrCreateCart(1);
        assertTrue(id > 0);
    }
}
