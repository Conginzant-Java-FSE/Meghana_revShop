package org.example.dao;

import org.example.models.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SellerDaoTest {

    private SellerDao sellerDao;

    @BeforeEach
    void setUp() {
        sellerDao = new SellerDao();
    }

    @Test
    void testLoginSeller_WithInvalidEmail_ShouldReturnNull() {
        Seller seller = sellerDao.loginSeller("invalid@email.com", "wrong123");
        assertNull(seller);
    }

    @Test
    void testLoginSeller_WithWrongPassword_ShouldReturnNull() {
        Seller seller = sellerDao.loginSeller("sam@gmail.com", "wrongPassword");
        assertNull(seller);
    }

    @Test
    void testFindSellerIdByEmail_WhenEmailNotExists_ShouldReturnNull() {
        Integer sellerId = sellerDao.findSellerIdByEmail("notfound@email.com");
        assertNull(sellerId);
    }

    @Test
    void testFindSellerEmailByPhone_WhenPhoneNotExists_ShouldReturnNull() {
        String email = sellerDao.findSellerEmailByPhone("9999999999");
        assertNull(email);
    }
}
