package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class WalletDao {

    private static final Logger log =
            LoggerFactory.getLogger(WalletDao.class);

    public void credit(String type, int id, double amount) {

        String sql = """
            INSERT INTO wallet(owner_type,owner_id,balance)
            VALUES(?,?,?)
            ON DUPLICATE KEY UPDATE balance = balance + ?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setInt(2, id);
            ps.setDouble(3, amount);
            ps.setDouble(4, amount);

            ps.executeUpdate();

            log.info("Wallet credited type={} id={} amount={}", type, id, amount);

        } catch (Exception e) {
            log.error("Wallet credit failed type={} id={}", type, id, e);
        }
    }

    public double getBalance(String type, int id) {

        String sql = "SELECT balance FROM wallet WHERE owner_type=? AND owner_id=?";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setInt(2, id);

            var rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            log.error("getBalance failed type={} id={}", type, id, e);
        }

        return 0;
    }
}
