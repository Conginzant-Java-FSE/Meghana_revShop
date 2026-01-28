package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Seller;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SellerDao {

    private static final Logger log = LoggerFactory.getLogger(SellerDao.class);

    public boolean isEmailExists(String email) {

        String sql = "SELECT seller_id FROM seller WHERE email=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            return ps.executeQuery().next();

        } catch (Exception e) {
            log.error("Seller email check failed {}", email, e);
        }
        return false;
    }

    public Integer registerSellerAndGetId(Seller seller) {

        String sql = """
            INSERT INTO seller
            (business_name, owner_name, email, password, phone, address,
             gst_number, business_type, website)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, seller.getBusinessName());
            ps.setString(2, seller.getOwnerName());
            ps.setString(3, seller.getEmail());
            ps.setString(4, seller.getPassword());
            ps.setString(5, seller.getPhone());
            ps.setString(6, seller.getAddress());
            ps.setString(7, seller.getGstNumber());
            ps.setString(8, seller.getBusinessType());
            ps.setString(9, seller.getWebsite());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                int id = keys.getInt(1);
                log.info("Seller registered id={}", id);
                return id;
            }

        } catch (Exception e) {
            log.error("Seller registration failed {}", seller.getEmail(), e);
        }

        return null;
    }

    public Seller loginSeller(String email, String plainPassword) {

        String sql = "SELECT * FROM seller WHERE email=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;
            if (!rs.getBoolean("is_active")) return null;
            if (!PasswordUtil.verifyPassword(plainPassword, rs.getString("password"))) return null;

            Seller s = new Seller();
            s.setSellerId(rs.getInt("seller_id"));
            s.setBusinessName(rs.getString("business_name"));
            s.setOwnerName(rs.getString("owner_name"));
            s.setEmail(rs.getString("email"));
            s.setPhone(rs.getString("phone"));
            s.setAddress(rs.getString("address"));
            s.setGstNumber(rs.getString("gst_number"));
            s.setBusinessType(rs.getString("business_type"));
            s.setWebsite(rs.getString("website"));
            s.setVerified(rs.getBoolean("is_verified"));

            log.info("Seller login success sellerId={}", s.getSellerId());
            return s;

        } catch (Exception e) {
            log.error("Seller login error {}", email, e);
        }

        return null;
    }

    public boolean updateBusinessProfile(int sellerId, String gst, String website, String address) {

        String sql = """
        UPDATE seller
        SET gst_number=?,
            website=?,
            address=?,
            is_verified = CASE
                WHEN ? IS NOT NULL AND ? <> '' THEN TRUE
                ELSE is_verified
            END
        WHERE seller_id=?
        """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gst);
            ps.setString(2, website);
            ps.setString(3, address);
            ps.setString(4, gst);
            ps.setString(5, gst);
            ps.setInt(6, sellerId);

            boolean ok = ps.executeUpdate() > 0;
            log.info("Seller profile updated sellerId={} success={}", sellerId, ok);
            return ok;

        } catch (Exception e) {
            log.error("Seller profile update failed sellerId={}", sellerId, e);
        }

        return false;
    }
    public Integer findSellerIdByEmail(String email){

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(
                    "SELECT seller_id FROM seller WHERE email=?")){

            ps.setString(1,email);
            var rs = ps.executeQuery();
            if(rs.next()) return rs.getInt(1);

        }catch(Exception e){}

        return null;
    }

    public String findSellerEmailByPhone(String phone){

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(
                    "SELECT email FROM seller WHERE phone=?")){

            ps.setString(1,phone);
            var rs = ps.executeQuery();
            if(rs.next()) return rs.getString(1);

        }catch(Exception e){}

        return null;
    }
    public void creditWallet(int sellerId,double amount){

        String sql="""
    UPDATE seller
    SET wallet_balance = IFNULL(wallet_balance,0) + ?
    WHERE seller_id=?
    """;

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setDouble(1,amount);
            ps.setInt(2,sellerId);
            ps.executeUpdate();

        }catch(Exception e){
            log.error("creditWallet failed",e);
        }
    }

    public double getWalletBalance(int sellerId){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "SELECT IFNULL(wallet_balance,0) FROM seller WHERE seller_id=?")){

            ps.setInt(1,sellerId);
            var rs=ps.executeQuery();
            if(rs.next()) return rs.getDouble(1);

        }catch(Exception e){
            log.error("getWalletBalance failed",e);
        }

        return 0;
    }

}
