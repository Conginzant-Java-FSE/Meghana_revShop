package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationDao {

    private static final Logger log =
            LoggerFactory.getLogger(NotificationDao.class);

    public void addNotification(Integer buyerId,
                                Integer sellerId,
                                String message) {

        String sql = """
            INSERT INTO notification(buyer_id, seller_id, message)
            VALUES(?,?,?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, buyerId);
            ps.setObject(2, sellerId);
            ps.setString(3, message);

            ps.executeUpdate();

        } catch (Exception e) {
            log.error("addNotification failed", e);
        }
    }

    public List<Notification> getNotifications(Integer buyerId,
                                               Integer sellerId) {

        List<Notification> list = new ArrayList<>();

        String sql = """
            SELECT * FROM notification
            WHERE (buyer_id=? OR ? IS NULL)
              AND (seller_id=? OR ? IS NULL)
            ORDER BY created_at DESC
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, buyerId);
            ps.setObject(2, buyerId);

            ps.setObject(3, sellerId);
            ps.setObject(4, sellerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Notification n = new Notification();
                n.setNotificationId(rs.getInt("notification_id"));
                n.setBuyerId((Integer) rs.getObject("buyer_id"));
                n.setSellerId((Integer) rs.getObject("seller_id"));
                n.setMessage(rs.getString("message"));
                n.setRead(rs.getBoolean("is_read"));
                n.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(n);
            }

        } catch (Exception e) {
            log.error("getNotifications failed", e);
        }

        return list;
    }

    public void markAsRead(int notificationId) {

        String sql =
                "UPDATE notification SET is_read=TRUE WHERE notification_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("markAsRead failed", e);
        }
    }
}
