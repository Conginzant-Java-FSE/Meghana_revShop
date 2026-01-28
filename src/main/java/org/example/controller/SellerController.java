package org.example.controller;

import org.example.dao.CategoryDao;
import org.example.models.Category;
import org.example.models.OrderItem;
import org.example.models.Product;
import org.example.models.Review;
import org.example.models.Seller;
import org.example.service.InventoryService;
import org.example.service.NotificationService;
import org.example.service.OrderService;
import org.example.service.PaymentService;
import org.example.service.ProductService;
import org.example.service.ReviewService;

import java.util.List;
import java.util.Scanner;

public class SellerController {

    private final ProductService productService = new ProductService();
    private final CategoryDao categoryDao = new CategoryDao();
    private final PaymentService paymentService = new PaymentService();
    private final OrderService orderService = new OrderService();
    private final NotificationService notificationService = new NotificationService();
    private final ReviewService reviewService = new ReviewService();
    private final InventoryService inventoryService = new InventoryService();

    private final Scanner sc = new Scanner(System.in);

    public void dashboard(Seller seller){

        while(true){

            double bal = paymentService.getSellerBalance(seller.getSellerId());
            System.out.println("\nWallet Balance: " + bal);

            System.out.println("""
===== SELLER DASHBOARD =====
1. View My Products
2. Add Product
3. Update Product
4. Delete Product
5. View My Orders
6. Notifications
7. Product Reviews
8. Inventory Alerts
0. Logout
""");

            System.out.print("Select an option: ");

            int ch;
            try{
                ch = Integer.parseInt(sc.nextLine());
            }catch(Exception e){
                System.out.println("Invalid input");
                continue;
            }

            switch(ch){
                case 1 -> viewProducts(seller);
                case 2 -> addProduct(seller);
                case 3 -> updateProduct(seller);
                case 4 -> deleteProduct(seller);
                case 5 -> viewOrders(seller);
                case 6 -> showNotifications(seller);
                case 7 -> showProductReviews(seller);
                case 8 -> showInventoryAlerts(seller);
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }


    private void viewProducts(Seller seller){

        List<Product> list = productService.sellerProducts(seller.getSellerId());

        if(list.isEmpty()){
            System.out.println("No products");
            return;
        }

        for(Product p:list){
            System.out.println("----------------");
            System.out.println("ID: "+p.getProductId());
            System.out.println("Name: "+p.getProductName());
            System.out.println("Category: "+p.getCategoryName());
            System.out.println("Price: "+p.getDiscountedPrice());
            System.out.println("Stock: "+p.getStockQuantity());
        }
    }

    private void addProduct(Seller seller){

        List<Category> cats = categoryDao.getAllCategories();

        System.out.println("\nCATEGORIES:");
        for(Category c:cats){
            System.out.println(c.getCategoryId()+" - "+c.getCategoryName());
        }

        Product p = new Product();

        System.out.print("Category ID: ");
        p.setCategoryId(Integer.parseInt(sc.nextLine()));

        System.out.print("Name: ");
        p.setProductName(sc.nextLine());

        System.out.print("Description: ");
        p.setDescription(sc.nextLine());

        System.out.print("MRP: ");
        p.setMrp(Double.parseDouble(sc.nextLine()));

        System.out.print("Discounted Price: ");
        p.setDiscountedPrice(Double.parseDouble(sc.nextLine()));

        System.out.print("Stock: ");
        p.setStockQuantity(Integer.parseInt(sc.nextLine()));

        System.out.print("Stock Threshold: ");
        p.setStockThreshold(Integer.parseInt(sc.nextLine()));

        p.setSellerId(seller.getSellerId());

        boolean ok = productService.addProduct(p);

        System.out.println(ok?"Product Added":"Failed");
    }

    private void updateProduct(Seller seller){

        viewProducts(seller);

        Product p = new Product();

        System.out.print("Product ID: ");
        p.setProductId(Integer.parseInt(sc.nextLine()));

        System.out.print("New Category ID: ");
        p.setCategoryId(Integer.parseInt(sc.nextLine()));

        System.out.print("New Name: ");
        p.setProductName(sc.nextLine());

        System.out.print("New Description: ");
        p.setDescription(sc.nextLine());

        System.out.print("New MRP: ");
        p.setMrp(Double.parseDouble(sc.nextLine()));

        System.out.print("New Discounted Price: ");
        p.setDiscountedPrice(Double.parseDouble(sc.nextLine()));

        System.out.print("New Stock: ");
        p.setStockQuantity(Integer.parseInt(sc.nextLine()));

        System.out.print("New Threshold: ");
        p.setStockThreshold(Integer.parseInt(sc.nextLine()));

        boolean ok = productService.updateProduct(seller.getSellerId(),p);

        System.out.println(ok?"Updated":"Failed");
    }

    private void deleteProduct(Seller seller){

        viewProducts(seller);

        System.out.print("Product ID: ");
        int id=Integer.parseInt(sc.nextLine());

        boolean ok = productService.deleteProduct(seller.getSellerId(),id);

        System.out.println(ok?"Deleted":"Failed");
    }


    private void viewOrders(Seller seller){

        List<OrderItem> items = orderService.sellerOrders(seller.getSellerId());

        if(items.isEmpty()){
            System.out.println("No orders");
            return;
        }

        for(OrderItem oi:items){
            System.out.println("----------------");
            System.out.println("Order ID: "+oi.getOrderId());
            System.out.println("Product: "+oi.getProductName());
            System.out.println("Qty: "+oi.getQuantity());
            System.out.println("Amount: "+oi.getLineTotal());
        }
    }


    private void showNotifications(Seller seller){
        notificationService.getSellerNotifications(seller.getSellerId())
                .forEach(n -> System.out.println(n.getMessage()));
    }

    private void showProductReviews(Seller seller){

        List<Review> list = reviewService.reviewsForSeller(seller.getSellerId());

        if(list.isEmpty()){
            System.out.println("No reviews");
            return;
        }

        for(Review r:list){
            System.out.println("\nProduct: "+r.getProductName());
            System.out.println("Buyer: "+r.getBuyerName());
            System.out.println("Rating: "+r.getRating());
            System.out.println("Comment: "+r.getComment());
            System.out.println("-----");
        }
    }


    private void showInventoryAlerts(Seller seller){

        List<Product> low = inventoryService.lowStockProducts(seller.getSellerId());

        if(low.isEmpty()){
            System.out.println("No low-stock products");
            return;
        }

        for(Product p:low){
            System.out.println("LOW STOCK: "+p.getProductName()+
                    " Qty="+p.getStockQuantity()+
                    " Threshold="+p.getStockThreshold());
        }
    }
}
