package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.sql.*;

public class CartDao {

    private static final Logger log =
            LoggerFactory.getLogger(CartDao.class);

    public int getOrCreateCart(int buyerId){

        try(var con = DBConnection.getConnection()){

            var ps = con.prepareStatement(
                    "SELECT cart_id FROM cart WHERE buyer_id=?");

            ps.setInt(1,buyerId);
            var rs = ps.executeQuery();

            if(rs.next()) return rs.getInt(1);

            ps = con.prepareStatement(
                    "INSERT INTO cart(buyer_id) VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1,buyerId);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            rs.next();

            return rs.getInt(1);

        }catch(Exception e){
            log.error("getOrCreateCart failed",e);
        }

        return -1;
    }

    public boolean addItem(int cartId,int pid,int qty){

        String sql = """
        INSERT INTO cart_item(cart_id,product_id,quantity)
        VALUES(?,?,?)
        ON DUPLICATE KEY UPDATE quantity=quantity+?
        """;

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(sql)){

            ps.setInt(1,cartId);
            ps.setInt(2,pid);
            ps.setInt(3,qty);
            ps.setInt(4,qty);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            log.error("addItem failed",e);
        }

        return false;
    }

    public List<CartItem> getCartItems(int cartId){

        List<CartItem> list = new ArrayList<>();

        String sql = """
        SELECT ci.product_id,ci.quantity,
               p.product_name,p.discounted_price,p.seller_id
        FROM cart_item ci
        JOIN product p ON ci.product_id=p.product_id
        WHERE ci.cart_id=?
        """;

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(sql)){

            ps.setInt(1,cartId);
            var rs=ps.executeQuery();

            while(rs.next()){
                CartItem c=new CartItem();
                c.setProductId(rs.getInt(1));
                c.setQuantity(rs.getInt(2));
                c.setProductName(rs.getString(3));
                c.setPrice(rs.getDouble(4));
                c.setSellerId(rs.getInt(5));
                c.setLineTotal(c.getQuantity()*c.getPrice());
                list.add(c);
            }

        }catch(Exception e){
            log.error("getCartItems failed",e);
        }

        return list;
    }

    public double calculateCartTotal(int cartId){

        double total=0;

        for(CartItem c:getCartItems(cartId)){
            total+=c.getLineTotal();
        }

        return total;
    }

    public boolean updateQty(int cartId,int pid,int qty){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "UPDATE cart_item SET quantity=? WHERE cart_id=? AND product_id=?")){

            ps.setInt(1,qty);
            ps.setInt(2,cartId);
            ps.setInt(3,pid);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            log.error("updateQty failed",e);
        }

        return false;
    }

    public boolean removeItem(int cartId,int pid){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "DELETE FROM cart_item WHERE cart_id=? AND product_id=?")){

            ps.setInt(1,cartId);
            ps.setInt(2,pid);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            log.error("removeItem failed",e);
        }

        return false;
    }

    public void clearCart(int cartId){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement(
                    "DELETE FROM cart_item WHERE cart_id=?")){

            ps.setInt(1,cartId);
            ps.executeUpdate();

        }catch(Exception e){
            log.error("clearCart failed",e);
        }
    }
}
