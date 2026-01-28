package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PaymentDao {

    private static final Logger log =
            LoggerFactory.getLogger(PaymentDao.class);

    public boolean saveBuyerPayment(int buyerId,
                                    int orderId,
                                    double amount,
                                    String method) {

        String sql = """
        INSERT INTO payment
        (buyer_id, order_id, amount, payment_method, status)
        VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, orderId);
            ps.setDouble(3, amount);
            ps.setString(4, method.toUpperCase());
            ps.setString(5, "SUCCESS");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("saveBuyerPayment failed", e);
        }

        return false;
    }

    public void creditSeller(int sellerId, double amount) {

        String sql = """
        UPDATE seller
        SET wallet_balance = wallet_balance + ?
        WHERE seller_id = ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, sellerId);
            ps.executeUpdate();

        } catch (Exception e) {
            log.error("creditWallet failed", e);
        }
    }

    public double getSellerBalance(int sellerId) {

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(
                     "SELECT wallet_balance FROM seller WHERE seller_id=?")) {

            ps.setInt(1, sellerId);
            var rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            log.error("getSellerBalance failed", e);
        }

        return 0;
    }
}
