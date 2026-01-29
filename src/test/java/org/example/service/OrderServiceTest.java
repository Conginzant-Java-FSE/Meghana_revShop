package org.example.service;

import org.example.dao.CartDao;
import org.example.dao.OrderDao;
import org.example.models.CartItem;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartDao cartDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private InventoryService inventoryService;

    private OrderService orderService;

    @BeforeEach
    void setup() throws Exception {
        orderService = new OrderService(); // DEFAULT constructor only

        inject(orderService, "cartDao", cartDao);
        inject(orderService, "orderDao", orderDao);
        inject(orderService, "paymentService", paymentService);
        inject(orderService, "notificationService", notificationService);
        inject(orderService, "inventoryService", inventoryService);
    }

    // ------------------- CHECKOUT TESTS -------------------

    @Test
    void checkout_cartEmpty() {
        when(cartDao.getOrCreateCart(1)).thenReturn(10);
        when(cartDao.getCartItems(10)).thenReturn(List.of());

        String result = orderService.checkoutAndPlaceOrder(
                1, "addr", "addr", "COD");

        assertEquals("Cart empty", result);
    }

    @Test
    void checkout_orderCreationFails() {
        when(cartDao.getOrCreateCart(1)).thenReturn(10);
        when(cartDao.getCartItems(10)).thenReturn(List.of(mock(CartItem.class)));
        when(cartDao.calculateCartTotal(10)).thenReturn(500.0);
        when(orderDao.createOrder(any(Order.class))).thenReturn(null);

        String result = orderService.checkoutAndPlaceOrder(
                1, "addr", "addr", "COD");

        assertEquals("Order failed", result);
    }

    @Test
    void checkout_paymentFails() {
        when(cartDao.getOrCreateCart(1)).thenReturn(10);
        when(cartDao.getCartItems(10)).thenReturn(List.of(mock(CartItem.class)));
        when(cartDao.calculateCartTotal(10)).thenReturn(500.0);
        when(orderDao.createOrder(any(Order.class))).thenReturn(99);
        when(paymentService.processPayment(anyInt(), anyInt(), anyDouble(), anyString()))
                .thenReturn(false);

        String result = orderService.checkoutAndPlaceOrder(
                1, "addr", "addr", "UPI");

        assertEquals("Payment failed", result);
    }

    @Test
    void checkout_success() {
        CartItem item = new CartItem();
        item.setProductId(1);
        item.setSellerId(2);
        item.setQuantity(1);
        item.setPrice(500);

        when(cartDao.getOrCreateCart(1)).thenReturn(10);
        when(cartDao.getCartItems(10)).thenReturn(List.of(item));
        when(cartDao.calculateCartTotal(10)).thenReturn(500.0);
        when(orderDao.createOrder(any(Order.class))).thenReturn(100);
        when(paymentService.processPayment(anyInt(), anyInt(), anyDouble(), anyString()))
                .thenReturn(true);
        when(orderDao.reduceStock(1, 1)).thenReturn(true);

        String result = orderService.checkoutAndPlaceOrder(
                1, "addr", "addr", "COD");

        assertTrue(result.contains("Order placed successfully"));
    }

    // ------------------- READ METHODS -------------------

    @Test
    void buyerOrderHistory() {
        when(orderDao.getOrdersByBuyer(1)).thenReturn(List.of(new Order()));

        List<Order> orders = orderService.buyerOrderHistory(1);

        assertEquals(1, orders.size());
    }

    @Test
    void getOrderItems() {
        when(orderDao.getOrderItems(100))
                .thenReturn(List.of(new OrderItem()));

        List<OrderItem> items = orderService.getOrderItems(100);

        assertEquals(1, items.size());
    }

    @Test
    void sellerOrders() {
        when(orderDao.getOrdersForSeller(2))
                .thenReturn(List.of(new OrderItem()));

        List<OrderItem> items = orderService.sellerOrders(2);

        assertEquals(1, items.size());
    }

    // ------------------- REFLECTION INJECTOR -------------------

    private void inject(Object target, String fieldName, Object mock)
            throws Exception {

        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }
}
