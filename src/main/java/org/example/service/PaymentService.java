package org.example.service;

import org.example.dao.PaymentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class PaymentService {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentService.class);

    private final PaymentDao paymentDao = new PaymentDao();
    private final Scanner sc = new Scanner(System.in);

    public boolean processPayment(int buyerId,
                                  int orderId,
                                  double amount,
                                  String method) {

        if (method == null) {
            System.out.println("Invalid payment method");
            log.warn("Payment failed null method buyerId={}", buyerId);
            return false;
        }

        String paymentMethod = method.trim().toUpperCase();

        switch (paymentMethod) {

            case "UPI" -> {
                System.out.print("Enter UPI ID: ");
                String upi = sc.nextLine().trim();

                if (upi.isEmpty() || !upi.contains("@")) {
                    System.out.println("Invalid UPI ID");
                    log.warn("UPI payment failed invalid upiId buyerId={}", buyerId);
                    return false;
                }

                System.out.print("Enter UPI PIN: ");
                String pin = sc.nextLine().trim();

                if (pin.isEmpty()) {
                    System.out.println("Invalid UPI PIN");
                    log.warn("UPI payment failed empty pin buyerId={}", buyerId);
                    return false;
                }

                boolean saved =
                        paymentDao.saveBuyerPayment(buyerId, orderId, amount, paymentMethod);

                log.info("UPI payment processed buyerId={} orderId={} success={}",
                        buyerId, orderId, saved);

                return saved;
            }

            case "CARD" -> {
                System.out.print("Enter Card Number: ");
                String card = sc.nextLine().trim();

                if (!card.matches("\\d{16}")) {
                    System.out.println("Invalid card number");
                    log.warn("Card payment failed invalid card buyerId={}", buyerId);
                    return false;
                }

                System.out.print("Enter CVV: ");
                String cvv = sc.nextLine().trim();

                if (!cvv.matches("\\d{3}")) {
                    System.out.println("Invalid CVV");
                    log.warn("Card payment failed invalid cvv buyerId={}", buyerId);
                    return false;
                }

                boolean saved =
                        paymentDao.saveBuyerPayment(buyerId, orderId, amount, paymentMethod);

                log.info("Card payment processed buyerId={} orderId={} success={}",
                        buyerId, orderId, saved);

                return saved;
            }

            case "COD" -> {
                System.out.println("Cash On Delivery selected");
                log.info("COD selected buyerId={} orderId={}", buyerId, orderId);
                return true;
            }

            default -> {
                System.out.println("Invalid payment method");
                log.warn("Payment failed invalid method={} buyerId={}", method, buyerId);
                return false;
            }
        }
    }

    public void settleSeller(int sellerId, double amount) {
        paymentDao.creditSeller(sellerId, amount);
        log.info("Seller wallet credited sellerId={} amount={}", sellerId, amount);
    }

    public double getSellerBalance(int sellerId) {
        double bal = paymentDao.getSellerBalance(sellerId);
        log.info("Fetched seller balance sellerId={} balance={}", sellerId, bal);
        return bal;
    }
}
