package org.example.dao;

import org.example.models.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest {

    private final ProductDao dao = new ProductDao();

    @Test
    void addProductTest() {

        Product p = new Product();
        p.setSellerId(1);
        p.setCategoryId(1);
        p.setProductName("JUnit Product");
        p.setDescription("JUnit Desc");
        p.setMrp(100);
        p.setDiscountedPrice(90);
        p.setStockQuantity(5);
        p.setStockThreshold(2);

        assertTrue(dao.addProduct(p));
    }

    @Test
    void sellerProductsTest() {
        assertNotNull(dao.getProductsBySeller(1));
    }
}
