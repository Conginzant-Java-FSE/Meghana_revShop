package org.example.service;

import org.example.dao.CartDao;
import org.example.models.CartItem;

import java.util.List;

public class CartService {

    private final CartDao dao;

    public CartService() {
        this.dao = new CartDao();
    }

    public CartService(CartDao dao) {
        this.dao = dao;
    }

    public int cartId(int buyerId){
        return dao.getOrCreateCart(buyerId);
    }

    public boolean add(int cartId,int pid,int qty){
        return dao.addItem(cartId,pid,qty);
    }

    public List<CartItem> items(int cartId){
        return dao.getCartItems(cartId);
    }

    public boolean update(int cartId,int pid,int qty){
        return dao.updateQty(cartId,pid,qty);
    }

    public boolean remove(int cartId,int pid){
        return dao.removeItem(cartId,pid);
    }

    public double total(int cartId){
        return dao.calculateCartTotal(cartId);
    }
}
