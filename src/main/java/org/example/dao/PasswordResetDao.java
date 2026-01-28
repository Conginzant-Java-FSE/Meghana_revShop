package org.example.dao;

import org.example.config.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class PasswordResetDao {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetDao.class);

    public boolean updateBuyerPassword(int buyerId, String pwd) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps =
                     con.prepareStatement("UPDATE buyer SET password=? WHERE buyer_id=?")) {

            ps.setString(1, pwd);
            ps.setInt(2, buyerId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("updateBuyerPassword failed", e);
        }

        return false;
    }
    public boolean updateSellerPassword(int sellerId,String pwd){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "UPDATE seller SET password=? WHERE seller_id=?")){

            ps.setString(1,pwd);
            ps.setInt(2,sellerId);
            return ps.executeUpdate()>0;

        }catch(Exception e){
            log.error("updateSellerPassword failed",e);
        }

        return false;
    }

}
