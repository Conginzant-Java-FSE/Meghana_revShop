package org.example.service;

import org.example.dao.ProductDao;
import org.example.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private NotificationService notificationService;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() throws Exception {
        inventoryService = new InventoryService();

        // 🔧 Inject mocked ProductDao
        Field productDaoField =
                InventoryService.class.getDeclaredField("productDao");
        productDaoField.setAccessible(true);
        productDaoField.set(inventoryService, productDao);

        // 🔧 Inject mocked NotificationService
        Field notificationField =
                InventoryService.class.getDeclaredField("notificationService");
        notificationField.setAccessible(true);
        notificationField.set(inventoryService, notificationService);
    }

    @Test
    void checkAndNotifyLowStock_notificationSent() {

        Product p = new Product();
        p.setProductId(10);
        p.setProductName("Phone");
        p.setStockQuantity(2);
        p.setStockThreshold(5);

        when(productDao.lowStockProducts(1))
                .thenReturn(List.of(p));

        inventoryService.checkAndNotifyLowStock(1, 10);

        verify(notificationService).notifySeller(
                eq(1),
                contains("LOW STOCK ALERT")
        );
    }

    @Test
    void checkAndNotifyLowStock_productIdMismatch_noNotification() {

        Product p = new Product();
        p.setProductId(11);

        when(productDao.lowStockProducts(1))
                .thenReturn(List.of(p));

        inventoryService.checkAndNotifyLowStock(1, 10);

        verifyNoInteractions(notificationService);
    }

    @Test
    void checkAndNotifyLowStock_emptyLowStockList_noNotification() {

        when(productDao.lowStockProducts(1))
                .thenReturn(List.of());

        inventoryService.checkAndNotifyLowStock(1, 10);

        verifyNoInteractions(notificationService);
    }

    @Test
    void checkAndNotifyLowStock_multipleProducts_notifyOnlyMatching() {

        Product p1 = new Product();
        p1.setProductId(10);
        p1.setProductName("Laptop");
        p1.setStockQuantity(1);
        p1.setStockThreshold(5);

        Product p2 = new Product();
        p2.setProductId(20);

        when(productDao.lowStockProducts(1))
                .thenReturn(List.of(p1, p2));

        inventoryService.checkAndNotifyLowStock(1, 10);

        verify(notificationService, times(1))
                .notifySeller(eq(1), contains("Laptop"));
    }

    @Test
    void lowStockProducts_delegatesToDao() {

        when(productDao.lowStockProducts(5))
                .thenReturn(List.of());

        inventoryService.lowStockProducts(5);

        verify(productDao).lowStockProducts(5);
    }
}
