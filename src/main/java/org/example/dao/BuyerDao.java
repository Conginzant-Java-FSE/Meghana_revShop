package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Buyer;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BuyerDao {

    private static final Logger log = LoggerFactory.getLogger(BuyerDao.class);

    public boolean isEmailExists(String email) {

        String sql = "SELECT buyer_id FROM buyer WHERE email=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            return ps.executeQuery().next();

        } catch (Exception e) {
            log.error("Error checking buyer email {}", email, e);
        }
        return false;
    }

    public Integer registerBuyerAndGetId(Buyer buyer) {

        String sql = "INSERT INTO buyer(full_name, email, password, phone) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, buyer.getFullName());
            ps.setString(2, buyer.getEmail());
            ps.setString(3, buyer.getPassword());
            ps.setString(4, buyer.getPhone());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                int id = keys.getInt(1);
                log.info("Buyer registered id={}", id);
                return id;
            }

        } catch (Exception e) {
            log.error("Error registering buyer {}", buyer.getEmail(), e);
        }

        return null;
    }

    public Buyer loginBuyer(String email, String plainPassword) {

        String sql = "SELECT * FROM buyer WHERE email=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                log.warn("Buyer login failed email not found {}", email);
                return null;
            }

            if (!rs.getBoolean("is_active")) {
                log.warn("Buyer account inactive {}", email);
                return null;
            }

            if (!PasswordUtil.verifyPassword(plainPassword, rs.getString("password"))) {
                log.warn("Buyer wrong password {}", email);
                return null;
            }

            Buyer buyer = new Buyer();
            buyer.setBuyerId(rs.getInt("buyer_id"));
            buyer.setFullName(rs.getString("full_name"));
            buyer.setEmail(rs.getString("email"));
            buyer.setPhone(rs.getString("phone"));
            buyer.setActive(true);

            log.info("Buyer login success buyerId={}", buyer.getBuyerId());
            return buyer;

        } catch (Exception e) {
            log.error("Buyer login error {}", email, e);
        }

        return null;
    }
    public String findEmailByPhone(String phone){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "SELECT email FROM buyer WHERE phone=?")){

            ps.setString(1,phone);
            var rs=ps.executeQuery();
            if(rs.next()) return rs.getString(1);

        }catch(Exception e){}

        return null;
    }

    public Integer findBuyerIdByEmail(String email){

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(
                    "SELECT buyer_id FROM buyer WHERE email=?")){

            ps.setString(1,email);
            var rs = ps.executeQuery();
            if(rs.next()) return rs.getInt(1);

        }catch(Exception e){}

        return null;
    }

}
