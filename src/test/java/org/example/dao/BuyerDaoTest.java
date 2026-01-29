package org.example.dao;

import org.example.models.Buyer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyerDaoTest {

    private final BuyerDao buyerDao = new BuyerDao();

    @Test
    void loginBuyer_shouldReturnNull_forNonExistingEmail() {

        Buyer buyer = buyerDao.loginBuyer(
                "nonexistent_email@test.com",
                "wrongpassword"
        );

        assertNull(buyer, "Login should return null for non-existing user");
    }

    @Test
    void findBuyerIdByEmail_shouldReturnNull_forUnknownEmail() {

        Integer buyerId = buyerDao.findBuyerIdByEmail(
                "unknown_email@test.com"
        );

        assertNull(buyerId, "Buyer ID should be null for unknown email");
    }

    @Test
    void findEmailByPhone_shouldReturnNull_forUnknownPhone() {

        String email = buyerDao.findEmailByPhone(
                "0000000000"
        );

        assertNull(email, "Email should be null for unknown phone");
    }
}
