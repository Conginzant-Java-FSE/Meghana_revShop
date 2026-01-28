package org.example.service;

import org.example.dao.ProductDao;
import org.example.models.Product;

import java.util.List;

public class InventoryService {

    private final ProductDao productDao = new ProductDao();
    private final NotificationService notificationService = new NotificationService();

    public List<Product> lowStockProducts(int sellerId){
        return productDao.lowStockProducts(sellerId);
    }

    public void checkAndNotifyLowStock(int sellerId,int productId){

        List<Product> low = productDao.lowStockProducts(sellerId);

        for(Product p:low){
            if(p.getProductId()==productId){
                notificationService.notifySeller(
                        sellerId,
                        "LOW STOCK ALERT: "+p.getProductName()+
                                " Remaining="+p.getStockQuantity()+
                                " Threshold="+p.getStockThreshold()
                );
            }
        }
    }
}
