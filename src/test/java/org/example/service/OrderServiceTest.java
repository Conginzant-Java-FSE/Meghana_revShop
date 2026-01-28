package org.example.service;

import org.example.dao.OrderDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderDao orderDao;

    OrderService service;

    @BeforeEach
    void setup(){
        service = new OrderService(orderDao);
    }

    @Test
    void buyerOrdersTest() {

        when(orderDao.getOrdersByBuyer(1))
                .thenReturn(Collections.emptyList());

        assertNotNull(service.buyerOrderHistory(1));
    }
}
