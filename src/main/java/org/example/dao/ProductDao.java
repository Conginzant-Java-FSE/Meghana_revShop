package org.example.dao;

import org.example.config.DBConnection;
import org.example.models.Category;
import org.example.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    private static final Logger log = LoggerFactory.getLogger(ProductDao.class);

    public List<Category> getCategories() {

        List<Category> list = new ArrayList<>();

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement("SELECT * FROM category");
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));
                list.add(c);
            }

        } catch (Exception e) {
            log.error("getCategories failed", e);
        }

        return list;
    }


    public List<Product> getAllActiveProducts() {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT p.*,c.category_name,IFNULL(AVG(r.rating),0) avgRating
        FROM product p
        JOIN category c ON p.category_id=c.category_id
        LEFT JOIN review r ON p.product_id=r.product_id
        WHERE p.is_active=TRUE
        GROUP BY p.product_id
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql);
             var rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            log.error("getAllActiveProducts failed", e);
        }

        return list;
    }

    public List<Product> byCategory(int cid) {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT p.*,c.category_name,IFNULL(AVG(r.rating),0) avgRating
        FROM product p
        JOIN category c ON p.category_id=c.category_id
        LEFT JOIN review r ON p.product_id=r.product_id
        WHERE p.category_id=? AND p.is_active=TRUE
        GROUP BY p.product_id
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, cid);
            var rs = ps.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            log.error("byCategory failed", e);
        }

        return list;
    }

    public List<Product> search(String key) {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT p.*,c.category_name,IFNULL(AVG(r.rating),0) avgRating
        FROM product p
        JOIN category c ON p.category_id=c.category_id
        LEFT JOIN review r ON p.product_id=r.product_id
        WHERE p.product_name LIKE ?
        GROUP BY p.product_id
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + key + "%");
            var rs = ps.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            log.error("search failed", e);
        }

        return list;
    }


    public List<Product> getProductsBySeller(int sellerId) {

        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT p.*,c.category_name,IFNULL(AVG(r.rating),0) avgRating
        FROM product p
        JOIN category c ON p.category_id=c.category_id
        LEFT JOIN review r ON p.product_id=r.product_id
        WHERE p.seller_id=? AND p.is_active=TRUE
        GROUP BY p.product_id
        """;

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(sql)) {

            ps.setInt(1, sellerId);
            var rs = ps.executeQuery();

            while (rs.next()) list.add(map(rs));

        } catch (Exception e) {
            log.error("getProductsBySeller failed", e);
        }

        return list;
    }

    public boolean addProduct(Product p) {

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement("""
         INSERT INTO product
         (seller_id,category_id,product_name,description,mrp,discounted_price,
          stock_quantity,stock_threshold)
         VALUES(?,?,?,?,?,?,?,?)
         """)) {

            ps.setInt(1, p.getSellerId());
            ps.setInt(2, p.getCategoryId());
            ps.setString(3, p.getProductName());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getMrp());
            ps.setDouble(6, p.getDiscountedPrice());
            ps.setInt(7, p.getStockQuantity());

            // 🔥 dynamic threshold
            ps.setInt(8, p.getStockThreshold());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("addProduct failed", e);
        }

        return false;
    }


    public boolean updateProduct(int sellerId, Product p) {

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement("""
             UPDATE product
             SET category_id=?,product_name=?,description=?,mrp=?,discounted_price=?,stock_quantity=?
             WHERE product_id=? AND seller_id=?
             """)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getMrp());
            ps.setDouble(5, p.getDiscountedPrice());
            ps.setInt(6, p.getStockQuantity());
            ps.setInt(7, p.getProductId());
            ps.setInt(8, sellerId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("updateProduct failed", e);
        }

        return false;
    }

    public boolean deleteProduct(int sellerId, int pid) {

        try (var con = DBConnection.getConnection();
             var ps = con.prepareStatement(
                     "UPDATE product SET is_active=FALSE WHERE product_id=? AND seller_id=?")) {

            ps.setInt(1, pid);
            ps.setInt(2, sellerId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            log.error("deleteProduct failed", e);
        }

        return false;
    }

    private Product map(ResultSet rs) throws Exception {

        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setSellerId(rs.getInt("seller_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setProductName(rs.getString("product_name"));
        p.setDescription(rs.getString("description"));
        p.setCategoryName(rs.getString("category_name"));
        p.setMrp(rs.getDouble("mrp"));
        p.setDiscountedPrice(rs.getDouble("discounted_price"));
        p.setStockQuantity(rs.getInt("stock_quantity"));

        // IMPORTANT
        p.setStockThreshold(rs.getInt("stock_threshold"));

        p.setAvgRating(rs.getDouble("avgRating"));
        return p;
    }

    public List<Product> lowStock(int sellerId){

        List<Product> list=new ArrayList<>();

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("""
SELECT product_name,stock_quantity
FROM product WHERE seller_id=? AND stock_quantity<=stock_threshold
""")){

            ps.setInt(1,sellerId);
            var rs=ps.executeQuery();

            while(rs.next()){
                Product p=new Product();
                p.setProductName(rs.getString(1));
                p.setStockQuantity(rs.getInt(2));
                list.add(p);
            }

        }catch(Exception e){}

        return list;
    }
    public boolean updateThreshold(int sellerId,int productId,int threshold){

        try(var con=DBConnection.getConnection();
            var ps=con.prepareStatement("""
        UPDATE product SET stock_threshold=?
        WHERE product_id=? AND seller_id=?
        """)){

            ps.setInt(1,threshold);
            ps.setInt(2,productId);
            ps.setInt(3,sellerId);

            return ps.executeUpdate()>0;

        }catch(Exception e){
            log.error("updateThreshold failed",e);
        }
        return false;
    }

    public List<Product> lowStockProducts(int sellerId){

        List<Product> list = new ArrayList<>();

        String sql = """
    SELECT product_id,product_name,stock_quantity,stock_threshold
    FROM product
    WHERE seller_id=?
      AND stock_quantity <= stock_threshold
      AND is_active=TRUE
    """;

        try(var con = DBConnection.getConnection();
            var ps = con.prepareStatement(sql)){

            ps.setInt(1,sellerId);
            var rs = ps.executeQuery();

            while(rs.next()){
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setProductName(rs.getString("product_name"));
                p.setStockQuantity(rs.getInt("stock_quantity"));
                p.setStockThreshold(rs.getInt("stock_threshold"));
                list.add(p);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }


}
