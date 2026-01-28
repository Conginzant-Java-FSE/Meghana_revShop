package org.example.controller;

import org.example.models.Buyer;
import org.example.models.Product;
import org.example.service.FavoriteService;

import java.util.List;
import java.util.Scanner;

public class FavoriteController {

    private final FavoriteService service = new FavoriteService();
    private final Scanner sc = new Scanner(System.in);

    public void menu(Buyer buyer) {

        while (true) {

            System.out.println("===== FAVORITES =====");
            System.out.println("1. View Favorites");
            System.out.println("2. Add Favorite");
            System.out.println("3. Remove Favorite");
            System.out.println("0. Back");

            System.out.print("Select an option: ");

            int ch;
            try{
                ch=Integer.parseInt(sc.nextLine());
            }catch(Exception e){
                System.out.println("Invalid input");
                continue;
            }

            switch (ch) {
                case 1 -> view(buyer);
                case 2 -> add(buyer);
                case 3 -> remove(buyer);
                case 0 -> { return; }
                default -> System.out.println("Invalid");
            }
        }
    }

    private void add(Buyer b) {

        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        if (service.add(b.getBuyerId(), pid))
            System.out.println("Added to favorites");
        else
            System.out.println("Already in favorites");
    }

    private void remove(Buyer b) {

        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        if (service.remove(b.getBuyerId(), pid))
            System.out.println("Removed");
        else
            System.out.println("Not found");
    }

    private void view(Buyer b) {

        List<Product> list = service.list(b.getBuyerId());

        if (list.isEmpty()) {
            System.out.println("No favorites");
            return;
        }

        for (Product p : list) {
            System.out.println("----------------");
            System.out.println("ID: " + p.getProductId());
            System.out.println("Name: " + p.getProductName());
            System.out.println("Price: " + p.getDiscountedPrice());
            System.out.println("Stock: " + p.getStockQuantity());
        }
    }
}
