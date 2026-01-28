package org.example;

import org.example.controller.AuthController;
import org.example.controller.BuyerController;
import org.example.controller.SellerController;
import org.example.models.Buyer;
import org.example.models.Seller;

import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);

    private static final AuthController authController = new AuthController();
    private static final BuyerController buyerController = new BuyerController();
    private static final SellerController sellerController = new SellerController();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n==== RevShop ====");
            System.out.println("1. Buyer Register");
            System.out.println("2. Buyer Login");
            System.out.println("3. Seller Register");
            System.out.println("4. Seller Login");
            System.out.println("0. Exit");

            System.out.print("Choose an option: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {

                case 1 -> authController.registerBuyer();

                case 2 -> {
                    Buyer buyer = authController.buyerLogin();
                    if (buyer != null) buyerController.dashboard(buyer);
                }

                case 3 -> authController.registerSeller();

                case 4 -> {
                    Seller seller = authController.sellerLogin();
                    if (seller != null) sellerController.dashboard(seller);
                }

                case 0 -> System.exit(0);

                default -> System.out.println("Invalid option");
            }
        }
    }
}
