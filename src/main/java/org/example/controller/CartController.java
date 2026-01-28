package org.example.controller;

import org.example.models.Buyer;
import org.example.models.CartItem;
import org.example.service.CartService;
import org.example.service.OrderService;

import java.util.List;
import java.util.Scanner;

public class CartController {

    private final CartService service = new CartService();
    private final Scanner sc = new Scanner(System.in);

    public void menu(Buyer buyer){

        int cartId = service.cartId(buyer.getBuyerId());

        while(true){

            List<CartItem> items = service.items(cartId);

            if(items.isEmpty()){
                System.out.println("\n No cart items.");
            }

            System.out.println("""
===== CART =====
1. View Cart
2. Add Product
3. Update Quantity
4. Remove Product
5. Checkout
0. Back
""");
            System.out.print("Select an option: ");

            int ch = Integer.parseInt(sc.nextLine());

            switch(ch){

                case 1 -> view(cartId);
                case 2 -> add(cartId);
                case 3 -> update(cartId);
                case 4 -> remove(cartId);
                case 5 -> checkout(buyer);
                case 0 -> { return; }

                default -> System.out.println("Invalid");
            }
        }
    }

    private void view(int cartId){

        List<CartItem> list = service.items(cartId);

        if(list.isEmpty()){
            System.out.println(" No cart items");
            return;
        }

        for(CartItem c:list){
            System.out.println("----------------");
            System.out.println("Product: "+c.getProductName());
            System.out.println("Qty: "+c.getQuantity());
            System.out.println("Price: "+c.getPrice());
            System.out.println("Total: "+c.getLineTotal());
        }

        System.out.println("Grand Total: "+service.total(cartId));
    }

    private void add(int cartId){

        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("Quantity: ");
        int q = Integer.parseInt(sc.nextLine());

        System.out.println(service.add(cartId,pid,q)?"Added":"Failed");
    }

    private void update(int cartId){

        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.print("New Quantity: ");
        int q = Integer.parseInt(sc.nextLine());

        System.out.println(service.update(cartId,pid,q)?"Updated":"Failed");
    }

    private void remove(int cartId){

        System.out.print("Product ID: ");
        int pid = Integer.parseInt(sc.nextLine());

        System.out.println(service.remove(cartId,pid)?"Removed":"Failed");
    }
    private void checkout(Buyer buyer){

        System.out.print("Shipping Address: ");
        String ship = sc.nextLine();

        System.out.print("Billing Address: ");
        String bill = sc.nextLine();

        System.out.print("Payment Method (COD/UPI/CARD): ");
        String pay = sc.nextLine();

        OrderService os = new OrderService();

        String res = os.checkoutAndPlaceOrder(
                buyer.getBuyerId(), ship, bill, pay);

        System.out.println(res);
    }

}
