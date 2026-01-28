package org.example.service;

import org.example.dao.NotificationDao;
import org.example.models.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationService.class);

    private final NotificationDao notificationDao = new NotificationDao();

    public List<Notification> getBuyerNotifications(int buyerId) {

        log.info("Fetching notifications for buyerId={}", buyerId);
        return notificationDao.getNotifications(buyerId, null);
    }

    public List<Notification> getSellerNotifications(int sellerId) {

        log.info("Fetching notifications for sellerId={}", sellerId);
        return notificationDao.getNotifications(null, sellerId);
    }

    public void markAsRead(int notificationId) {

        log.info("Marking notification as read id={}", notificationId);
        notificationDao.markAsRead(notificationId);
    }

    public void notifyBuyer(int buyerId, String message) {

        log.info("Notify buyerId={} msg={}", buyerId, message);
        notificationDao.addNotification(buyerId, null, message);
    }

    public void notifySeller(int sellerId, String message) {

        log.info("Notify sellerId={} msg={}", sellerId, message);
        notificationDao.addNotification(null, sellerId, message);
    }
}
