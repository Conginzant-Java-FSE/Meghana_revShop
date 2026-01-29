package org.example.service;

import org.example.dao.CartDao;
import org.example.dao.OrderDao;
import org.example.models.CartItem;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);

    private final CartDao cartDao;
    private final OrderDao orderDao;
    private final NotificationService notificationService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public OrderService(){
        this.cartDao = new CartDao();
        this.orderDao = new OrderDao();
        this.notificationService = new NotificationService();
        this.paymentService = new PaymentService();
        this.inventoryService = new InventoryService();
    }

    public OrderService(OrderDao orderDao){
        this.cartDao = null;
        this.notificationService = null;
        this.paymentService = null;
        this.inventoryService = null;
        this.orderDao = orderDao;
    }

    public String checkoutAndPlaceOrder(int buyerId,
                                        String shipping,
                                        String billing,
                                        String paymentMethod) {

        int cartId = cartDao.getOrCreateCart(buyerId);
        List<CartItem> cartItems = cartDao.getCartItems(cartId);

        if (cartItems.isEmpty())
            return "Cart empty";

        double total = cartDao.calculateCartTotal(cartId);

        Order order = new Order(buyerId, total, paymentMethod, shipping, billing);

        Integer orderId = orderDao.createOrder(order);
        if (orderId == null) return "Order failed";

        boolean paid =
                paymentService.processPayment(buyerId, orderId, total, paymentMethod);

        if (!paid) return "Payment failed";

        for (CartItem ci : cartItems) {

            if (!orderDao.reduceStock(ci.getProductId(), ci.getQuantity()))
                return "Stock failed";

            paymentService.settleSeller(
                    ci.getSellerId(),
                    ci.getQuantity() * ci.getPrice()
            );
        }

        for (CartItem ci : cartItems) {
            inventoryService.checkAndNotifyLowStock(
                    ci.getSellerId(),
                    ci.getProductId()
            );
        }

        orderDao.createOrderItems(orderId, cartItems);

        notificationService.notifyBuyer(
                buyerId,
                "Order placed successfully! Order ID: " + orderId
        );

        Set<Integer> sellers = new HashSet<>();
        for (CartItem ci : cartItems) {
            if (sellers.add(ci.getSellerId())) {
                notificationService.notifySeller(
                        ci.getSellerId(),
                        "New order received. Order ID: " + orderId
                );
            }
        }

        cartDao.clearCart(cartId);

        log.info("Order completed buyerId={} orderId={}", buyerId, orderId);

        return "Order placed successfully! Order ID: " + orderId;
    }

    public List<Order> buyerOrderHistory(int buyerId){
        return orderDao.getOrdersByBuyer(buyerId);
    }

    public List<Order> getOderByBuyer(int buyerId){
        return buyerOrderHistory(buyerId);
    }

    public List<OrderItem> getOrderItems(int orderId){
        return orderDao.getOrderItems(orderId);
    }

    public List<OrderItem> sellerOrders(int sellerId){
        return orderDao.getOrdersForSeller(sellerId);
    }
}
