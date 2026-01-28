package org.example.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDaoTest {

    OrderDao dao = new OrderDao();

    @Test
    void buyerOrdersTest() {
        assertNotNull(dao.getOrdersByBuyer(1));
    }
}
