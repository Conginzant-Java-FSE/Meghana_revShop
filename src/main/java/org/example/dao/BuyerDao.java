package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Buyer;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class BuyerDao {

    private static final Logger log = LoggerFactory.getLogger(BuyerDao.class);

    public Integer registerBuyerAndGetId(Buyer buyer) {

        String sql = "INSERT INTO buyer(full_name,email,password,phone) VALUES(?,?,?,?)";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,buyer.getFullName());
            ps.setString(2,buyer.getEmail());
            ps.setString(3,buyer.getPassword());
            ps.setString(4,buyer.getPhone());

            ps.executeUpdate();
            var rs=ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);

        }catch(Exception e){}

        return null;
    }

    public Buyer loginBuyer(String email,String pwd){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("SELECT * FROM buyer WHERE email=?")){

            ps.setString(1,email);
            var rs=ps.executeQuery();

            if(!rs.next()) return null;

            if(!PasswordUtil.verifyPassword(pwd,rs.getString("password"))) return null;

            Buyer b=new Buyer();
            b.setBuyerId(rs.getInt("buyer_id"));
            b.setFullName(rs.getString("full_name"));
            b.setEmail(rs.getString("email"));
            b.setPhone(rs.getString("phone"));
            return b;

        }catch(Exception e){}

        return null;
    }

    public boolean resetPassword(String email,String newPwd){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "UPDATE buyer SET password=? WHERE email=?")){

            ps.setString(1, PasswordUtil.hashPassword(newPwd));
            ps.setString(2,email);
            return ps.executeUpdate()>0;

        }catch(Exception e){}

        return false;
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
