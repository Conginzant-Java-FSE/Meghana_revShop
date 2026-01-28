package org.example.controller;

import org.example.models.Buyer;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.example.service.OrderService;

import java.util.List;
import java.util.Scanner;

public class OrderController {

    private final OrderService service = new OrderService();
    private final Scanner sc = new Scanner(System.in);

    public void buyerMenu(Buyer buyer) {

        while (true) {

            System.out.println("===== ORDERS =====");
            System.out.println("1. Checkout / Place Order");
            System.out.println("2. View My Orders");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1 -> System.out.println("Use Cart → Checkout");
                case 2 -> buyerOrders(buyer);
                case 0 -> { return; }
            }
        }
    }

    private void buyerOrders(Buyer buyer) {

        List<Order> orders = service.buyerOrderHistory(buyer.getBuyerId());

        if (orders.isEmpty()) {
            System.out.println("You have not placed any orders yet.");
            return;
        }

        for (Order o : orders) {

            System.out.println("\n==============================");
            System.out.println("Order ID : " + o.getOrderId());
            System.out.println("Total    : " + o.getTotalAmount());
            System.out.println("Status   : PLACED");

            List<OrderItem> items = service.getOrderItems(o.getOrderId());

            if (items.isEmpty()) {
                System.out.println("No items found.");
                continue;
            }

            System.out.println("Items:");

            for (OrderItem oi : items) {
                System.out.println(" - " + oi.getProductName()
                        + " | Seller: " + oi.getSellerName()
                        + " | Qty: " + oi.getQuantity()
                        + " | Amount: " + oi.getLineTotal());
            }
        }

        System.out.println("==============================");
    }
}
