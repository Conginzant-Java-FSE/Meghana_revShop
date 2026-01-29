package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Seller;
import org.example.util.PasswordUtil;

public class SellerDao {

    public Integer registerSellerAndGetId(Seller s){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "INSERT INTO seller(business_name,owner_name,email,password,phone) VALUES(?,?,?,?,?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1,s.getBusinessName());
            ps.setString(2,s.getOwnerName());
            ps.setString(3,s.getEmail());
            ps.setString(4,s.getPassword());
            ps.setString(5,s.getPhone());

            ps.executeUpdate();
            var rs=ps.getGeneratedKeys();
            if(rs.next()) return rs.getInt(1);

        }catch(Exception e){}
        return null;
    }

    public Seller loginSeller(String email,String pwd){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("SELECT * FROM seller WHERE email=?")){

            ps.setString(1,email);
            var rs=ps.executeQuery();

            if(!rs.next()) return null;
            if(!PasswordUtil.verifyPassword(pwd,rs.getString("password"))) return null;

            Seller s=new Seller();
            s.setSellerId(rs.getInt("seller_id"));
            s.setBusinessName(rs.getString("business_name"));
            s.setEmail(rs.getString("email"));
            s.setPhone(rs.getString("phone"));
            return s;

        }catch(Exception e){}
        return null;
    }

    public boolean resetPassword(String email,String newPwd){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "UPDATE seller SET password=? WHERE email=?")){

            ps.setString(1, PasswordUtil.hashPassword(newPwd));
            ps.setString(2,email);
            return ps.executeUpdate()>0;

        }catch(Exception e){}
        return false;
    }

    public String findSellerEmailByPhone(String phone){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "SELECT email FROM seller WHERE phone=?")){

            ps.setString(1,phone);
            var rs=ps.executeQuery();
            if(rs.next()) return rs.getString(1);

        }catch(Exception e){}
        return null;
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

}
