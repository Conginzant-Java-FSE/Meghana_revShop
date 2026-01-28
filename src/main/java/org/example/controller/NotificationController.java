package org.example.controller;

import org.example.models.Buyer;
import org.example.models.Notification;
import org.example.models.Seller;
import org.example.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class NotificationController {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService = new NotificationService();
    private final Scanner sc = new Scanner(System.in);

    public void buyerNotifications(Buyer buyer) {

        List<Notification> list =
                notificationService.getBuyerNotifications(buyer.getBuyerId());

        if (list.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        for (Notification n : list) {
            System.out.println("----------------------------------");
            System.out.println("ID: " + n.getNotificationId());
            System.out.println("Message: " + n.getMessage());
            System.out.println("Status: " + (n.isRead() ? "Read" : "Unread"));
        }

        System.out.print("Enter Notification ID to mark as read (0 back): ");

        int id = Integer.parseInt(sc.nextLine());
        if (id != 0) notificationService.markAsRead(id);
    }

    public void sellerNotifications(Seller seller) {

        List<Notification> list =
                notificationService.getSellerNotifications(seller.getSellerId());

        if (list.isEmpty()) {
            System.out.println("No notifications.");
            return;
        }

        for (Notification n : list) {
            System.out.println("----------------------------------");
            System.out.println("ID: " + n.getNotificationId());
            System.out.println("Message: " + n.getMessage());
            System.out.println("Status: " + (n.isRead() ? "Read" : "Unread"));
        }

        System.out.print("Enter Notification ID to mark as read (0 back): ");

        int id = Integer.parseInt(sc.nextLine());
        if (id != 0) notificationService.markAsRead(id);
    }
}
