package org.example.service;

import org.example.dao.ProductDao;
import org.example.models.Category;
import org.example.models.Product;

import java.util.List;

public class ProductService {

    private final ProductDao dao = new ProductDao();
    public List<Product> viewAllProducts() {
        return dao.getAllActiveProducts();
    }

    public List<Product> productsByCategory(int categoryId) {
        return dao.byCategory(categoryId);
    }

    public List<Product> searchProducts(String key) {
        return dao.search(key);
    }

    public List<Category> getCategories() {
        return dao.getCategories();
    }

    public List<Product> sellerProducts(int sellerId) {
        return dao.getProductsBySeller(sellerId);
    }

    public boolean addProduct(Product p) {
        return dao.addProduct(p);
    }

    public boolean updateProduct(int sellerId, Product p) {
        return dao.updateProduct(sellerId, p);
    }

    public boolean deleteProduct(int sellerId, int pid) {
        return dao.deleteProduct(sellerId, pid);
    }
    public List<Product> lowStock(int sellerId){
        return dao.lowStock(sellerId);
    }
    public boolean updateThreshold(int sellerId,int productId,int threshold){
        return dao.updateThreshold(sellerId,productId,threshold);
    }

    public List<Product> lowStockProducts(int sellerId){
        return dao.lowStockProducts(sellerId);
    }

}
