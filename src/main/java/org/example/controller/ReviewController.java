package org.example.controller;

import org.example.models.Buyer;
import org.example.models.Review;
import org.example.service.ReviewService;

import java.util.List;
import java.util.Scanner;

public class ReviewController {

    private final ReviewService reviewService = new ReviewService();
    private final Scanner sc = new Scanner(System.in);

    public void reviewMenu(Buyer buyer) {

        while (true) {

            System.out.println("===== REVIEWS =====");
            System.out.println("1. Add Review");
            System.out.println("2. View Product Reviews");
            System.out.println("0. Back");

            System.out.print("Choose: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {

                case 1 -> addReview(buyer);

                case 2 -> {
                    System.out.print("Product ID: ");
                    int pid = Integer.parseInt(sc.nextLine());
                    showProductReviews(pid);
                }

                case 0 -> { return; }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void addReview(Buyer buyer) {

        Review r = new Review();

        System.out.print("Product ID: ");
        r.setProductId(Integer.parseInt(sc.nextLine()));

        System.out.print("Rating (1-5): ");
        r.setRating(Integer.parseInt(sc.nextLine()));

        System.out.print("Comment: ");
        r.setComment(sc.nextLine());

        r.setBuyerId(buyer.getBuyerId());

        reviewService.addReview(r);

        System.out.println("Review added");
    }
    public void showProductReviews(int productId) {

        List<Review> list = reviewService.productReviews(productId);

        if (list == null || list.isEmpty()) {
            System.out.println("No reviews yet.");
            return;
        }

        for (Review r : list) {
            System.out.println("Reviews:");
            System.out.println("  Buyer: " + r.getBuyerName());
            System.out.println("  Rating: " + r.getRating());
            System.out.println("  Comment: " + r.getComment());
            System.out.println("----------------");


        }
    }
}
