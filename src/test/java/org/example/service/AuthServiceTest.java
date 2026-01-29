package org.example.service;

import org.example.dao.BuyerDao;
import org.example.dao.SellerDao;
import org.example.models.Buyer;
import org.example.models.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;

    @Mock
    private BuyerDao buyerDao;

    @Mock
    private SellerDao sellerDao;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        authService = new AuthService();

        // 🔑 Inject mocks into final fields using reflection
        Field buyerField = AuthService.class.getDeclaredField("buyerDao");
        buyerField.setAccessible(true);
        buyerField.set(authService, buyerDao);

        Field sellerField = AuthService.class.getDeclaredField("sellerDao");
        sellerField.setAccessible(true);
        sellerField.set(authService, sellerDao);
    }

    @Test
    void registerBuyer_success() {
        when(buyerDao.registerBuyerAndGetId(any())).thenReturn(1);

        boolean result = authService.registerBuyer(
                "John", "buyer@test.com", "pass", "9999");

        assertTrue(result);
        verify(buyerDao).registerBuyerAndGetId(any());
    }

    @Test
    void registerBuyer_failure() {
        when(buyerDao.registerBuyerAndGetId(any())).thenReturn(null);

        boolean result = authService.registerBuyer(
                "John", "buyer@test.com", "pass", "9999");

        assertFalse(result);
    }

    @Test
    void registerSeller_success() {
        when(sellerDao.registerSellerAndGetId(any())).thenReturn(10);

        boolean result = authService.registerSeller(
                "Biz", "Owner", "seller@test.com", "pass", "8888");

        assertTrue(result);
        verify(sellerDao).registerSellerAndGetId(any());
    }

    @Test
    void registerSeller_failure() {
        when(sellerDao.registerSellerAndGetId(any())).thenReturn(null);

        boolean result = authService.registerSeller(
                "Biz", "Owner", "seller@test.com", "pass", "8888");

        assertFalse(result);
    }

    @Test
    void buyerLogin_success() {
        Buyer b = new Buyer();
        b.setEmail("buyer@test.com");

        when(buyerDao.loginBuyer("buyer@test.com", "pass")).thenReturn(b);

        Buyer result = authService.buyerLogin("buyer@test.com", "pass");

        assertNotNull(result);
        assertEquals("buyer@test.com", result.getEmail());
    }

    @Test
    void buyerLogin_failure() {
        when(buyerDao.loginBuyer("buyer@test.com", "wrong")).thenReturn(null);

        Buyer result = authService.buyerLogin("buyer@test.com", "wrong");

        assertNull(result);
    }

    @Test
    void sellerLogin_success() {
        Seller s = new Seller();
        s.setEmail("seller@test.com");

        when(sellerDao.loginSeller("seller@test.com", "pass")).thenReturn(s);

        Seller result = authService.sellerLogin("seller@test.com", "pass");

        assertNotNull(result);
        assertEquals("seller@test.com", result.getEmail());
    }

    @Test
    void sellerLogin_failure() {
        when(sellerDao.loginSeller("seller@test.com", "wrong")).thenReturn(null);

        Seller result = authService.sellerLogin("seller@test.com", "wrong");

        assertNull(result);
    }

    @Test
    void findBuyerIdByEmail() {
        when(buyerDao.findBuyerIdByEmail("buyer@test.com")).thenReturn(1);

        Integer id = authService.findBuyerIdByEmail("buyer@test.com");

        assertEquals(1, id);
    }

    @Test
    void findSellerIdByEmail() {
        when(sellerDao.findSellerIdByEmail("seller@test.com")).thenReturn(10);

        Integer id = authService.findSellerIdByEmail("seller@test.com");

        assertEquals(10, id);
    }

    @Test
    void findBuyerEmailByPhone() {
        when(buyerDao.findEmailByPhone("9999")).thenReturn("buyer@test.com");

        String email = authService.findBuyerEmailByPhone("9999");

        assertEquals("buyer@test.com", email);
    }

    @Test
    void findSellerEmailByPhone() {
        when(sellerDao.findSellerEmailByPhone("8888"))
                .thenReturn("seller@test.com");

        String email = authService.findSellerEmailByPhone("8888");

        assertEquals("seller@test.com", email);
    }
}
