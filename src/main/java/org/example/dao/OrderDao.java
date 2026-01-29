package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.CartItem;
import org.example.models.Order;
import org.example.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

    private static final Logger log =
            LoggerFactory.getLogger(OrderDao.class);

    public Integer createOrder(Order o) {

        String sql =
                "INSERT INTO orders(buyer_id,total_amount,payment_method,shipping_address,billing_address) VALUES(?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, o.getBuyerId());
            ps.setDouble(2, o.getTotalAmount());
            ps.setString(3, o.getPaymentMethod());
            ps.setString(4, o.getShippingAddress());
            ps.setString(5, o.getBillingAddress());

            ps.executeUpdate();

            var rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            log.error("createOrder failed", e);
        }

        return null;
    }

    public boolean reduceStock(int productId, int qty) {

        String lockSql =
                "SELECT stock_quantity FROM product WHERE product_id=? FOR UPDATE";

        String updateSql =
                "UPDATE product SET stock_quantity=? WHERE product_id=?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            int currentStock;

            try (var lockPs = con.prepareStatement(lockSql)) {
                lockPs.setInt(1, productId);
                var rs = lockPs.executeQuery();

                if (!rs.next()) {
                    con.rollback();
                    return false;
                }

                currentStock = rs.getInt(1);
            }

            if (currentStock < qty) {
                con.rollback();
                log.warn("reduceStock failed insufficient stock productId={} requested={} available={}",
                        productId, qty, currentStock);
                return false;
            }

            int newStock = currentStock - qty;

            try (var updatePs = con.prepareStatement(updateSql)) {
                updatePs.setInt(1, newStock);
                updatePs.setInt(2, productId);
                updatePs.executeUpdate();
            }

            con.commit();
            log.info("Stock reduced productId={} oldStock={} newStock={}",
                    productId, currentStock, newStock);

            return true;

        } catch (Exception e) {
            log.error("reduceStock failed", e);
        }

        return false;
    }

    public boolean createOrderItems(int orderId, List<CartItem> cartItems) {

        String sql =
                "INSERT INTO order_item(order_id,product_id,quantity,unit_price,line_total,seller_id) VALUES(?,?,?,?,?,?)";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            for (CartItem ci : cartItems) {

                double unitPrice = ci.getPrice();
                double lineTotal = unitPrice * ci.getQuantity();

                ps.setInt(1, orderId);
                ps.setInt(2, ci.getProductId());
                ps.setInt(3, ci.getQuantity());
                ps.setDouble(4, unitPrice);
                ps.setDouble(5, lineTotal);
                ps.setInt(6, ci.getSellerId());

                ps.addBatch();
            }

            ps.executeBatch();
            return true;

        } catch (Exception e) {
            log.error("createOrderItems failed", e);
        }

        return false;
    }

    public List<OrderItem> getOrdersForSeller(int sellerId) {

        List<OrderItem> list = new ArrayList<>();

        String sql = """
        SELECT oi.order_id,p.product_name,oi.quantity,oi.line_total
        FROM order_item oi
        JOIN product p ON oi.product_id=p.product_id
        WHERE oi.seller_id=?
        ORDER BY oi.order_id DESC
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            var rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem oi = new OrderItem();
                oi.setOrderId(rs.getInt(1));
                oi.setProductName(rs.getString(2));
                oi.setQuantity(rs.getInt(3));
                oi.setLineTotal(rs.getDouble(4));
                list.add(oi);
            }

        } catch (Exception e) {
            log.error("getOrdersForSeller failed", e);
        }

        return list;
    }

    public List<Order> getOrdersByBuyer(int buyerId) {

        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT order_id,total_amount FROM orders WHERE buyer_id=? ORDER BY order_id DESC";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            var rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("order_id"));
                o.setTotalAmount(rs.getDouble("total_amount"));
                list.add(o);
            }

        } catch (Exception e) {
            log.error("getOrdersByBuyer failed", e);
        }

        return list;
    }

    public List<OrderItem> getOrderItems(int orderId) {

        List<OrderItem> list = new ArrayList<>();

        String sql = """
        SELECT 
            oi.quantity,
            oi.line_total,
            p.product_name,
            s.business_name
        FROM order_item oi
        JOIN product p ON oi.product_id = p.product_id
        JOIN seller s ON oi.seller_id = s.seller_id
        WHERE oi.order_id = ?
    """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            var rs = ps.executeQuery();

            while (rs.next()) {
                OrderItem oi = new OrderItem();
                oi.setProductName(rs.getString("product_name"));
                oi.setSellerName(rs.getString("business_name")); // ✅ FIX
                oi.setQuantity(rs.getInt("quantity"));
                oi.setLineTotal(rs.getDouble("line_total"));
                list.add(oi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public boolean isBelowThreshold(int productId) {

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement("""
             SELECT stock_quantity <= stock_threshold
             FROM product WHERE product_id=?
             """)) {

            ps.setInt(1, productId);
            var rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean(1);

        } catch (Exception e) {
            log.error("isBelowThreshold failed", e);
        }

        return false;
    }
}
