package org.example.service;

import org.example.dao.CartDao;
import org.example.models.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartDao cartDao;

    CartService service;

    @BeforeEach
    void setup(){
        service = new CartService(cartDao);
    }

    @Test
    void addTest() {
        when(cartDao.addItem(1,2,1)).thenReturn(true);
        assertTrue(service.add(1,2,1));
    }

    @Test
    void itemsTest() {

        when(cartDao.getCartItems(1))
                .thenReturn(List.of(new CartItem()));

        assertEquals(1, service.items(1).size());
    }
}
