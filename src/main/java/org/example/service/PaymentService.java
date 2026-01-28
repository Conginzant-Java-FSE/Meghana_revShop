package org.example.service;

import org.example.dao.PaymentDao;

import java.util.Scanner;

public class PaymentService {

    private final PaymentDao paymentDao = new PaymentDao();

    public boolean processPayment(int buyerId, int orderId, double amount, String method) {

        Scanner sc = new Scanner(System.in);

        if (method.equalsIgnoreCase("UPI")) {

            System.out.print("Enter UPI ID: ");
            String upi = sc.nextLine();

            // must contain @
            if (!upi.contains("@")) {
                System.out.println("Invalid UPI format. Please enter a valid UPI ID.");
                return false;
            }
        }

        if (method.equalsIgnoreCase("CARD")) {

            System.out.print("Enter Card Number: ");
            String card = sc.nextLine();

            if (card.length() != 16) {
                System.out.println("Invalid card number. Must be 16 digits.");
                return false;
            }

            System.out.print("Enter CVV: ");
            sc.nextLine();
        }

        if (method.equalsIgnoreCase("COD")) {
            System.out.println("Cash On Delivery selected.");
            return true;
        }

        return paymentDao.saveBuyerPayment(buyerId, orderId, amount, method);
    }

    public void settleSeller(int sellerId, double amount) {
        paymentDao.creditSeller(sellerId, amount);
    }

    public double getSellerBalance(int sellerId) {
        return paymentDao.getSellerBalance(sellerId);
    }
}
