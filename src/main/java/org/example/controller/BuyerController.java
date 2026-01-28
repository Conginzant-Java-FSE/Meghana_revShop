package org.example.controller;

import org.example.models.Buyer;
import org.example.models.Product;
import org.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class BuyerController {

    private static final Logger log =
            LoggerFactory.getLogger(BuyerController.class);

    private final FavoriteController favoriteController = new FavoriteController();
    private final ProductService productService = new ProductService();
    private final CartController cartController = new CartController();
    private final OrderController orderController = new OrderController();
    private final ReviewController reviewController = new ReviewController();
    private final NotificationController notificationController = new NotificationController();

    private final Scanner sc = new Scanner(System.in);

    public void dashboard(Buyer buyer) {

        while (true) {

            System.out.println("===== BUYER DASHBOARD =====");
            System.out.println("1. View All Products");
            System.out.println("2. Browse By Category");
            System.out.println("3. Search Product");
            System.out.println("4. Cart");
            System.out.println("5. Orders");
            System.out.println("6. Reviews");
            System.out.println("7. Notifications");
            System.out.println("8. Favorites");
            System.out.println("0. Logout");

            System.out.print("Select an option: ");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (ch) {
                case 1 -> showProducts(buyer, productService.viewAllProducts());
                case 2 -> browseByCategory(buyer);
                case 3 -> search(buyer);
                case 4 -> cartController.menu(buyer);
                case 5 -> orderController.buyerMenu(buyer);
                case 6 -> reviewController.reviewMenu(buyer);
                case 7 -> notificationController.buyerNotifications(buyer);
                case 8 -> favoriteController.menu(buyer);
                case 0 -> { return; }
                default -> System.out.println("Invalid");
            }
        }
    }

    private void showProducts(Buyer buyer, List<Product> list) {

        for (Product p : list) {
            System.out.println("--------------------------------");
            System.out.println("ID: " + p.getProductId());
            System.out.println("Name: " + p.getProductName());
            System.out.println("Category: " + p.getCategoryName());
            System.out.println("Price: " + p.getDiscountedPrice());
            System.out.println("Stock: " + p.getStockQuantity());
            System.out.println("Rating: " + p.getAvgRating());
            reviewController.showProductReviews(p.getProductId());
        }

        System.out.println("1. Go To Cart");
        System.out.println("0. Back");
        System.out.print("Select an option: ");

        int ch;
        try {
            ch = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input");
            return;
        }

        if (ch == 1) {
            cartController.menu(buyer);
        }
    }

    private void browseByCategory(Buyer buyer) {

        productService.getCategories().forEach(c ->
                System.out.println(c.getCategoryId() + "-" + c.getCategoryName()));

        System.out.print("Select Category ID: ");

        int cid;
        try {
            cid = Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input");
            return;
        }

        showProducts(buyer, productService.productsByCategory(cid));
    }

    private void search(Buyer buyer) {

        System.out.print("Enter search keyword: ");
        String k = sc.nextLine();

        showProducts(buyer, productService.searchProducts(k));
    }
}
