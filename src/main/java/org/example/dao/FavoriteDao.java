package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDao {

    private static final Logger log = LoggerFactory.getLogger(FavoriteDao.class);

    public boolean addFavorite(int buyerId, int productId) {

        String sql = "INSERT IGNORE INTO favorite(buyer_id,product_id) VALUES(?,?)";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("addFavorite failed", e);
        }

        return false;
    }

    public boolean removeFavorite(int buyerId, int productId) {

        String sql = "DELETE FROM favorite WHERE buyer_id=? AND product_id=?";

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("removeFavorite failed", e);
        }

        return false;
    }

    public List<Product> getFavorites(int buyerId) {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT p.product_id,p.product_name,p.discounted_price,p.stock_quantity
        FROM favorite f
        JOIN product p ON f.product_id=p.product_id
        WHERE f.buyer_id=?
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, buyerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setDiscountedPrice(rs.getDouble("discounted_price"));
                p.setStockQuantity(rs.getInt("stock_quantity"));

                list.add(p);
            }

        } catch (Exception e) {
            log.error("getFavorites failed", e);
        }

        return list;
    }
}
